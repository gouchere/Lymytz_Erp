-- Function: com_et_objectif(bigint, bigint, character varying)

-- DROP FUNCTION com_et_objectif(bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN objectif_ bigint, IN type_ character varying)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	periode_ BIGINT;
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif;			
	FOR periode_ IN SELECT p.id FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
	LOOP
		INSERT INTO table_objectif SELECT * FROM com_et_objectif_by_periode(periode_, objectif_, type_);
	END LOOP;
	return QUERY SELECT * FROM table_objectif ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, bigint, character varying)
  OWNER TO postgres;

  
  -- Function: com_et_objectif_by_periode(bigint, bigint, character varying)

-- DROP FUNCTION com_et_objectif_by_periode(bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif_by_periode(IN periode_ bigint, IN objectif_ bigint, IN type_ character varying)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	ligne_ RECORD;
	valeur_ DOUBLE PRECISION DEFAULT 0;

	i INTEGER DEFAULT 0;
	somme_ DOUBLE PRECISION DEFAULT 0;
	attente_ DOUBLE PRECISION DEFAULT 0;
	entete_ CHARACTER VARYING;
    
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif_by_periode;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif_by_periode(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif_by_periode;
	SELECT INTO entete_ p.code_ref FROM yvs_com_periode_objectif p WHERE p.id = periode_;
	i = 0;
	IF(type_ IS NULL OR type_ = '')THEN
		FOR ligne_ IN SELECT c.id, c.code_ref AS code, c.nom, c.prenom, o.valeur AS attente FROM yvs_com_comerciale c INNER JOIN yvs_com_objectifs_comercial o ON o.commercial = c.id WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			i = i + 1;
			attente_ = COALESCE(ligne_.attente , 0);
			valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_));
			valeur_ = COALESCE(valeur_ , 0);
			IF(valeur_ != 0 OR attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom ||' '||ligne_.prenom, periode_, entete_, attente_, valeur_, i);
			END IF;	
		END LOOP;
	ELSIF(type_ = 'A')THEN
		FOR ligne_ IN SELECT y.id, y.codeagence AS code, y.designation AS nom, o.valeur AS attente FROM yvs_agences y INNER JOIN yvs_com_objectifs_agence o ON o.agence = y.id WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			i = i + 1;
			attente_ = COALESCE(ligne_.attente , 0);
			IF(attente_ IS NULL OR attente_ < 0)THEN
				SELECT INTO attente_ SUM(COALESCE(o.valeur, 0)) FROM yvs_com_objectifs_comercial o INNER JOIN yvs_com_comerciale y ON o.commercial = y.id WHERE o.periode = periode_ AND o.objectif = objectif_ AND y.agence = ligne_.id;
			END IF;
			attente_ = COALESCE(ligne_.attente , 0);
			valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_));
			valeur_ = COALESCE(valeur_ , 0);
			IF(valeur_ != 0 OR attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom, periode_, entete_, attente_, valeur_, i);
			END IF;
		END LOOP;
	ELSE
		FOR ligne_ IN SELECT y.id, y.code, y.libelle AS nom, o.valeur AS attente FROM yvs_base_point_vente y INNER JOIN yvs_com_objectifs_point_vente o ON o.point_vente = y.id WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			i = i + 1;
			attente_ = COALESCE(ligne_.attente , 0);
			IF(attente_ IS NULL OR attente_ < 0)THEN
				SELECT INTO attente_ SUM(COALESCE(o.valeur, 0)) FROM yvs_com_objectifs_comercial o INNER JOIN yvs_com_comerciale c ON o.commercial = c.id INNER JOIN yvs_com_creneau_horaire_users ch ON c.utilisateur = ch.users INNER JOIN yvs_com_creneau_point cp ON ch.creneau_point = cp.id
				WHERE o.periode = periode_ AND o.objectif = objectif_ AND cp.point = ligne_.id;
			END IF;
			attente_ = COALESCE(ligne_.attente , 0);
			valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_));
			valeur_ = COALESCE(valeur_ , 0);
			IF(valeur_ != 0 OR attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom, periode_, entete_, attente_, valeur_, i);
			END IF;
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_objectif_by_periode ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif_by_periode(bigint, bigint, character varying)
  OWNER TO postgres;

  
  -- Function: alter_action_colonne_key(character varying, boolean, boolean)

-- DROP FUNCTION alter_action_colonne_key(character varying, boolean, boolean);

CREATE OR REPLACE FUNCTION alter_action_colonne_key(table_ character varying, update_ boolean, delete_ boolean)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	table_name_ character varying;
	constraint_ record;	
	query_ character varying;
	compteur_ integer default 0;
	
BEGIN	
	-- Debut de la modification des action cles primaires 	
	compteur_ = 0;
	RAISE NOTICE 'Table° : %',table_;

	-- Recherche de toutes les tables rattachées a la table actuell
	for table_name_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = 'public' order by tablename
	loop	
		-- Recherche de la clé secondaire liée a la clé primaire donnée
		FOR constraint_ IN SELECT k.CONSTRAINT_NAME, k.TABLE_NAME, k.COLUMN_NAME, f.TABLE_NAME AS TABLE_NAME_, f.COLUMN_NAME AS COLUMN_NAME_ 
			FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
			INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			WHERE k.table_schema = 'public' AND k.TABLE_NAME = table_name_ AND c.CONSTRAINT_TYPE = 'FOREIGN KEY' AND f.TABLE_NAME = table_
		LOOP
			compteur_ = compteur_ + 1;
			--Suppression de la contrainte
			execute 'ALTER TABLE public.'||table_name_||' DROP CONSTRAINT '||constraint_.CONSTRAINT_NAME;
			
			query_ = 'ALTER TABLE public.'||table_name_||'
					ADD CONSTRAINT '||table_name_||'_'||constraint_.COLUMN_NAME||'_fkey FOREIGN KEY ('||constraint_.COLUMN_NAME||')
					REFERENCES public.'||table_||' ('||constraint_.COLUMN_NAME_||') MATCH SIMPLE';
			if(update_ = true and delete_ = true)then
				query_ = query_ || ' ON UPDATE CASCADE ON DELETE CASCADE';
			elsif(update_ = true and delete_ = false)then
				query_ = query_ || ' ON UPDATE CASCADE ON DELETE NO ACTION';
			elsif(update_ = false and delete_ = true)then
				query_ = query_ || ' ON UPDATE NO ACTION ON DELETE CASCADE';
			elsif(update_ = false and delete_ = false)then
				query_ = query_ || ' ON UPDATE NO ACTION ON DELETE NO ACTION';
			end if;
			execute query_;
			
			RAISE NOTICE ' constraint N° : %',compteur_ ||' -- table : '||table_name_||' /colonne : '||constraint_.COLUMN_NAME||' /contrainte : '||constraint_.CONSTRAINT_NAME;
		END LOOP;
	end loop;
	RAISE NOTICE '%','';
	return true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_action_colonne_key(character varying, boolean, boolean)
  OWNER TO postgres;
