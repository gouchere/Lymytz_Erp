-- Function: com_et_journal_achat(bigint, bigint, date, date, character varying, character varying, character varying)

-- DROP FUNCTION com_et_journal_achat(bigint, bigint, date, date, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION com_et_journal_achat(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN type_ character varying, IN periode_ character varying, IN group_ character varying)
  RETURNS TABLE(id bigint, code character varying, nom character varying, classe bigint, reference character varying, designation character varying, montant double precision, is_classe boolean, is_vendeur boolean, rang integer) AS
$BODY$
declare 
   dates_ RECORD;
   classe_ record;
   
   journal_ character varying;
   
   valeur_ double precision default 0;
   totaux_ double precision;
   
   query_ CHARACTER VARYING ;
   query_avoir_ CHARACTER VARYING;

   position_ INTEGER DEFAULT 1;
   is_classe_ BOOLEAN DEFAULT true;
   
begin 	
	--DROP TABLE table_et_journal_achat;
	CREATE TEMP TABLE IF NOT EXISTS table_et_journal_achat(_id BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _classe BIGINT, _reference CHARACTER VARYING, _designation CHARACTER VARYING, _montant DOUBLE PRECISION, _is_classe BOOLEAN, _is_vendeur BOOLEAN, _rang INTEGER);
	DELETE FROM table_et_journal_achat;
	IF(group_ = 'F')THEN
		IF(type_ = 'A')THEN
			query_ = 'SELECT f.id, code_fsseur AS code, CONCAT(f.nom, '' '', f.prenom) AS nom, a.id AS article, a.ref_art, a.designation, SUM(y.prix_total) AS valeur';
		ELSE
			query_ = 'SELECT f.id, code_fsseur AS code, CONCAT(f.nom, '' '', f.prenom) AS nom, SUM(y.prix_total) AS valeur';
		END IF;
	ELSE
		IF(type_ = 'A')THEN
			query_ = 'SELECT a.id, a.ref_art, a.designation, SUM(y.prix_total) AS valeur';
		ELSE
			query_ = 'SELECT SUM(y.prix_total)';
		END IF;
	END IF;
	query_ = query_ || ' FROM yvs_com_contenu_doc_achat y INNER JOIN yvs_com_doc_achats d ON y.doc_achat = d.id INNER JOIN yvs_base_articles a ON y.article = a.id 
				INNER JOIN yvs_agences g ON d.agence = g.id INNER JOIN yvs_base_fournisseur f ON d.fournisseur = f.id
				WHERE d.statut = ''V'' AND g.societe = '||societe_||' AND d.type_doc = ''FA'' AND d.document_lie IS NULL';
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND d.agence = '||agence_;
	END IF;
	-- LIGNE DES CA PAR CLASSE STAT
	IF(type_ = 'A')THEN
		IF(group_ = 'F')THEN
			FOR dates_ IN EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' GROUP BY f.id, a.id ORDER BY f.nom, f.prenom'
			LOOP
				INSERT INTO table_et_journal_achat values (dates_.id, dates_.code, dates_.nom, dates_.article, dates_.ref_art, dates_.designation, COALESCE(dates_.valeur, 0), TRUE, TRUE, position_);
				position_ = position_ + 1;
			END LOOP;
		ELSE				
			FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
			LOOP
				FOR classe_ IN EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' GROUP BY a.id ORDER BY a.ref_art, a.designation'
				LOOP
					INSERT INTO table_et_journal_achat values (dates_.position, dates_.intitule, dates_.intitule, classe_.id, classe_.ref_art, classe_.designation, COALESCE(classe_.valeur, 0), TRUE, TRUE, position_);
					position_ = position_ + 1;
				END LOOP;
			END LOOP;
		END IF;
	ELSIF(type_ = 'F')THEN
		-- Vente directe par famille d'article
		FOR classe_ IN SELECT s.id, TRIM(UPPER(s.reference_famille)) AS code, UPPER(s.designation) AS intitule FROM yvs_base_famille_article s WHERE s.societe = societe_ AND s.famille_parent IS NULL ORDER BY TRIM(UPPER(s.reference_famille))
		LOOP
			-- CA Des article classé
			IF(group_ = 'F')THEN
				FOR dates_ IN EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND a.famille = '||classe_.id||' GROUP BY f.id ORDER BY f.nom, f.prenom'
				LOOP
					INSERT INTO table_et_journal_achat values (dates_.id, dates_.code, dates_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(dates_.valeur, 0), TRUE, TRUE, position_);
					position_ = position_ + 1;
				END LOOP;
			ELSE				
				FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
				LOOP
					EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND a.famille = '||classe_.id INTO valeur_;
					IF(COALESCE(valeur_, 0) > 0)THEN
						INSERT INTO table_et_journal_achat values (dates_.position, dates_.intitule, dates_.intitule, classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, position_);
						position_ = position_ + 1;
					END IF;
				END LOOP;
			END IF;
		END LOOP;
	ELSIF(type_ = 'FP')THEN
		-- Vente directe par famille d'article parent
		FOR classe_ IN SELECT s.id, UPPER(s.reference_famille) AS code, UPPER(s.designation) AS intitule FROM yvs_base_famille_article s WHERE s.societe = societe_ AND s.famille_parent IS NULL
		LOOP
			-- CA Des article classé
			IF(group_ = 'F')THEN
				FOR dates_ IN EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND (a.famille = '||classe_.id||' OR a.famille IN (SELECT base_get_sous_famille('||classe_.id||', true))) GROUP BY f.id ORDER BY f.nom, f.prenom'
				LOOP
					INSERT INTO table_et_journal_achat values (dates_.id, dates_.code, dates_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(dates_.valeur, 0), TRUE, TRUE, position_);
					position_ = position_ + 1;
				END LOOP;
			ELSE	
				FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
				LOOP
					EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND (a.famille = '||classe_.id||' OR a.famille IN (SELECT base_get_sous_famille('||classe_.id||', true)))' INTO valeur_;
					IF(COALESCE(valeur_, 0) > 0)THEN
						INSERT INTO table_et_journal_achat values (dates_.position, dates_.intitule, dates_.intitule, classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, position_);
						position_ = position_ + 1;
					END IF;
				END LOOP;
			END IF;
		END LOOP;
	ELSIF(type_ = 'CP')THEN
		-- Vente directe par classe statistique parent
		FOR classe_ IN SELECT s.id, UPPER(s.code_ref) AS code, UPPER(s.designation) AS intitule FROM yvs_base_classes_stat s WHERE s.societe = societe_ AND s.parent IS NULL
		LOOP
			-- CA Des article classé
			IF(group_ = 'F')THEN
				FOR dates_ IN EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND (a.classe1 = '||classe_.id||' OR a.classe1 IN (SELECT base_get_sous_classe_stat('||classe_.id||', true))) GROUP BY f.id ORDER BY f.nom, f.prenom'
				LOOP
					INSERT INTO table_et_journal_achat values (dates_.id, dates_.code, dates_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(dates_.valeur, 0), TRUE, TRUE, position_);
					position_ = position_ + 1;
				END LOOP;
			ELSE	
				FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
				LOOP
					EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND (a.classe1 = '||classe_.id||' OR a.classe1 IN (SELECT base_get_sous_classe_stat('||classe_.id||', true)))' INTO valeur_;
					IF(COALESCE(valeur_, 0) > 0)THEN
						INSERT INTO table_et_journal_achat values (dates_.position, dates_.intitule, dates_.intitule, classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, position_);
						position_ = position_ + 1;
					END IF;
				END LOOP;
			END IF;
		END LOOP;
		
		-- CA Des article non classé
		IF(group_ = 'F')THEN
			FOR dates_ IN EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND a.classe1 IS NULL GROUP BY f.id ORDER BY f.nom, f.prenom'
			LOOP
				INSERT INTO table_et_journal_achat values (dates_.id, dates_.code, dates_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(dates_.valeur, 0), TRUE, TRUE, position_);
				position_ = position_ + 1;
			END LOOP;
		ELSE	
			FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
			LOOP
				EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND a.classe1 IS NULL' INTO valeur_;
				IF(COALESCE(valeur_, 0) > 0)THEN
					INSERT INTO table_et_journal_achat values (dates_.position, dates_.intitule, dates_.intitule, 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), TRUE, TRUE, position_);
					position_ = position_ + 1;
				END IF;
			END LOOP;
		END IF;
	ELSE
		-- Vente directe par classe statistique
		FOR classe_ IN SELECT s.id, UPPER(s.code_ref) AS code, UPPER(s.designation) AS intitule FROM yvs_base_classes_stat s WHERE s.societe = societe_ AND s.parent IS NULL ORDER BY s.code_ref
		LOOP
			-- CA Des article classé
			IF(group_ = 'F')THEN
				position_ = 1;
				FOR dates_ IN EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND a.classe1 = '||classe_.id||' GROUP BY f.id ORDER BY f.nom, f.prenom'
				LOOP
					INSERT INTO table_et_journal_achat values (dates_.id, dates_.code, dates_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(dates_.valeur, 0), TRUE, TRUE, position_);
					position_ = position_ + 1;
				END LOOP;
			ELSE	
				position_ = 1;
				FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
				LOOP
					EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND a.classe1 = '||classe_.id INTO valeur_;
					IF(COALESCE(valeur_, 0) > 0)THEN
						INSERT INTO table_et_journal_achat values (dates_.position, dates_.intitule, dates_.intitule, classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, position_);
						position_ = position_ + 1;
					END IF;
				END LOOP;
			END IF;
		END LOOP;
		
		-- CA Des article non classé
		IF(group_ = 'F')THEN
			FOR dates_ IN EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND a.classe1 IS NULL GROUP BY f.id ORDER BY f.nom, f.prenom'
			LOOP
				INSERT INTO table_et_journal_achat values (dates_.id, dates_.code, dates_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(dates_.valeur, 0), TRUE, TRUE, position_);
				position_ = position_ + 1;
			END LOOP;
		ELSE	
			FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
			LOOP
				EXECUTE query_ || ' AND d.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND a.classe1 IS NULL' INTO valeur_;
				IF(COALESCE(valeur_, 0) > 0)THEN
					INSERT INTO table_et_journal_achat values (dates_.position, dates_.intitule, dates_.intitule, 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), TRUE, TRUE, position_);
				END IF;
				position_ = position_ + 1;
			END LOOP;
		END IF;
	END IF;
	FOR classe_ IN SELECT _id, _code, _nom, SUM(y._montant) AS _valeur, MAX(_rang) AS _position FROM table_et_journal_achat y GROUP BY _id, _code, _nom
	LOOP
		INSERT INTO table_et_journal_achat values (classe_._id, classe_._code, classe_._nom, -1, 'TOTAUX', 'TOTAUX', COALESCE(classe_._valeur, 0), FALSE, TRUE, classe_._position + 1);
	END LOOP;
	position_ = 1;
	FOR classe_ IN SELECT _classe, _reference, _designation, SUM(y._montant) AS _valeur FROM table_et_journal_achat y GROUP BY _classe, _reference, _designation ORDER BY _reference
	LOOP
		IF(classe_._reference = 'TOTAUX')THEN
			is_classe_ = FALSE;
		ELSE
			is_classe_ = TRUE;
		END IF;
		INSERT INTO table_et_journal_achat values (-1, 'TOTAUX', 'TOTAUX', classe_._classe, classe_._reference, classe_._designation, COALESCE(classe_._valeur, 0), is_classe_, FALSE, position_);
		position_ = position_ + 1;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_journal_achat ORDER BY _is_vendeur DESC, _code, _rang, _is_classe DESC, _classe;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_journal_achat(bigint, bigint, date, date, character varying, character varying, character varying)
  OWNER TO postgres;
