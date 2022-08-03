-- Function: return_field(character varying, character varying, character varying)

-- DROP FUNCTION return_field(character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION return_field(IN table_ character varying, IN column_ character varying, IN table_liee_ character varying)
  RETURNS SETOF character varying AS
$BODY$
DECLARE 
	field_ CHARACTER VARYING default '';
	
begin 	
	CREATE TEMP TABLE IF NOT EXISTS table_field(_field CHARACTER VARYING); 
	DELETE FROM table_field;
	IF(table_ IS NULL OR table_ = '')THEN
		INSERT INTO table_field SELECT tablename FROM pg_tables WHERE tablename NOT LIKE 'pg_%' AND schemaname = 'public' ORDER BY tablename;
	ELSE
		IF(column_ IS NULL OR column_ = '')THEN
			INSERT INTO table_field SELECT column_name FROM information_schema.columns WHERE table_name = table_;
		ELSE
			IF(table_liee_ IS NULL OR table_liee_ = '')THEN
				INSERT INTO table_field SELECT DISTINCT(f.TABLE_NAME) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
				INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				WHERE k.table_schema = 'public' AND k.COLUMN_NAME = column_ AND k.TABLE_NAME = table_ AND k.COLUMN_NAME != f.COLUMN_NAME;
			ELSE
				INSERT INTO table_field SELECT DISTINCT(f.COLUMN_NAME) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
				INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				WHERE k.table_schema = 'public' AND k.COLUMN_NAME = column_ AND f.TABLE_NAME = table_liee_ AND k.TABLE_NAME = table_ AND k.COLUMN_NAME != f.COLUMN_NAME;
			END IF;
		END IF;
	END IF;
	RETURN QUERY SELECT * FROM table_field ORDER BY _field;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION return_field(character varying, character varying, character varying)
  OWNER TO postgres;
  
  -- Function: update_()

-- DROP FUNCTION update_();

CREATE OR REPLACE FUNCTION com_update_all_data_for_client(IN client_ BIGINT)
  RETURNS boolean AS
$BODY$   
DECLARE 
	vente_ RECORD;
	commercial_ RECORD;
BEGIN
	FOR commercial_ IN SELECT y.id, y.tiers, e.compte_tiers FROM yvs_com_comerciale y INNER JOIN yvs_grh_employes e ON e.code_users = y.utilisateur
	LOOP
		IF(commercial_.tiers IS NULL OR commercial_.tiers < 1)THEN
			UPDATE yvs_com_comerciale SET tiers = commercial_.compte_tiers WHERE id = commercial_.id;
		END IF;
	END LOOP;
	FOR vente_ IN SELECT y.id, c.users, l.tiers, l.suivi_comptable FROM yvs_com_doc_ventes y INNER JOIN yvs_com_client l ON y.client = l.id INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id 
	WHERE y.client = client_ AND y.type_doc in ('FV','FAV') AND y.id NOT IN (SELECT w.ref_externe FROM yvs_compta_content_journal w WHERE w.table_externe = 'DOC_VENTE')
	LOOP
		SELECT INTO commercial_ y.id, c.tiers FROM yvs_com_commercial_vente y INNER JOIN yvs_com_comerciale c ON y.commercial = c.id WHERE y.facture = vente_.id AND y.responsable IS TRUE;
		IF(commercial_.id IS NULL OR commercial_.id < 1)THEN
			SELECT INTO commercial_ y.id, c.tiers FROM yvs_com_commercial_vente y INNER JOIN yvs_com_comerciale c ON y.commercial = c.id WHERE y.facture = vente_.id AND c.utilisateur = vente_.users;
			IF(commercial_.id IS NULL OR commercial_.id < 1)THEN
				SELECT INTO commercial_ y.id, y.tiers FROM yvs_com_comerciale y WHERE y.utilisateur = vente_.users;
				IF(commercial_.id IS NOT NULL OR commercial_.id > 0)THEN
					INSERT INTO yvs_com_commercial_vente(commercial, facture, taux, responsable) VALUES(commercial_.id, vente_.id, 100, true);
				END IF;
			END IF;
		END IF;
		IF(vente_.suivi_comptable IS TRUE)THEN
			UPDATE yvs_com_doc_ventes SET tiers = vente_.tiers WHERE id = vente_.id;
		ELSIF(commercial_.tiers IS NOT NULL OR commercial_.tiers > 0)THEN
			UPDATE yvs_com_doc_ventes SET tiers = commercial_.tiers WHERE id = vente_.id;
		ELSE
			SELECT INTO commercial_ y.id, e.compte_tiers AS tiers FROM yvs_users y INNER JOIN yvs_grh_employes e ON y.id = e.code_users WHERE y.id = vente_.users;
-- 			RAISE NOTICE 'commercial_.tiers %',commercial_.tiers;
			IF(commercial_.tiers IS NOT NULL OR commercial_.tiers > 0)THEN
				UPDATE yvs_com_doc_ventes SET tiers = commercial_.tiers WHERE id = vente_.id;
			END IF;
		END IF;
	END LOOP;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_update_all_data_for_client(BIGINT)
  OWNER TO postgres;


  
  -- Function: arrondi(bigint, double precision)

-- DROP FUNCTION arrondi(bigint, double precision);

CREATE OR REPLACE FUNCTION arrondi(societe_ bigint, valeur_ double precision)
  RETURNS double precision AS
$BODY$   
DECLARE 
	_unite_ INT DEFAULT 0;
	_valeur_ DOUBLE PRECISION DEFAULT 0;
	_valeur_string_ CHARACTER VARYING DEFAULT '0';
	_params_ RECORD;
BEGIN
	SELECT INTO _params_ y.* FROM yvs_compta_parametre y WHERE y.societe = societe_;
	IF(_params_.id IS NOT NULL AND _params_.id > 0)THEN
		IF(_params_.decimal_arrondi IS TRUE)THEN
			_valeur_ = round(valeur_::numeric, _params_.valeur_arrondi);
		ELSE
			valeur_ = round(valeur_::numeric);
			IF(_params_.mode_arrondi IS NOT NULL)THEN
				_valeur_string_ = cast((valeur_::INT) AS CHARACTER VARYING);
				_unite_ = CAST((SUBSTRING(_valeur_string_, char_length(_valeur_string_), char_length(_valeur_string_))) AS INT);
				CASE _params_.mode_arrondi
					WHEN 'I' THEN 
						IF (_unite_ < _params_.multiple_arrondi) THEN
						    _valeur_ = valeur_ - _unite_;
						ELSE
						    _valeur_ = valeur_ - (_unite_ - _params_.multiple_arrondi);
						END IF;
					WHEN 'S' THEN 
						IF (_unite_ < _params_.multiple_arrondi) THEN
						    _valeur_ = valeur_ + (_params_.multiple_arrondi - _unite_);
						ELSE
						    _valeur_ = valeur_ + ((_params_.multiple_arrondi * 2) - _unite_);
						END IF;
					ELSE
						IF (_unite_ < _params_.multiple_arrondi) THEN
						    IF (_unite_ < (_params_.multiple_arrondi / 2)) THEN
							_valeur_ = valeur_ - _unite_;
						    ELSE
							_valeur_ = valeur_ + (_params_.multiple_arrondi - _unite_);
						    END IF;
						ELSE
						    IF (_unite_ < (_params_.multiple_arrondi + (_params_.multiple_arrondi / 2))) THEN
							_valeur_ = valeur_ - (_unite_ - _params_.multiple_arrondi);
						    ELSE
							_valeur_ = valeur_ + ((_params_.multiple_arrondi * 2) - _unite_);
						    END IF;
						END IF;
			        END CASE;
			ELSE
				_valeur_ = valeur_;
			END IF;
		END IF;
	ELSE
		_valeur_ = round(valeur_::numeric, 0);
	END IF;
	RETURN _valeur_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION arrondi(bigint, double precision)
  OWNER TO postgres;
