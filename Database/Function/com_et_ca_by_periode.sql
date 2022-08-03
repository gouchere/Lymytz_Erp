-- Function: com_et_ca_by_periode(bigint, bigint, character varying, date, date, character varying, character varying)

-- DROP FUNCTION com_et_ca_by_periode(bigint, bigint, character varying, date, date, character varying, character varying);

CREATE OR REPLACE FUNCTION com_et_ca_by_periode(IN societe_ bigint, IN agence_ bigint, IN element_ character varying, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN nature_ character varying)
  RETURNS TABLE(id bigint, code character varying, intitule character varying, periode character varying, date_debut date, date_fin date, montant double precision, rang integer) AS
$BODY$
declare 
   ligne_ RECORD;
   dates_ RECORD;
   
   ids_ character varying default '0';
   
   valeur_ double precision default 0;
   
   query_ CHARACTER VARYING;
   query_valeur_ character varying;
   
begin 	
	nature_ = COALESCE(nature_, 'C');
	--DROP TABLE table_et_ca_by_periode;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ca_by_periode(_id_ bigint, _code_ character varying, _intitule_ character varying, _periode_ character varying, _date_debut_ date, _date_fin_ date, _montant_ double precision, _rang_ integer);
	DELETE FROM table_et_ca_by_periode;
	IF(COALESCE(nature_, 'C') = 'C')THEN
		query_ = 'SELECT a.id::bigint, UPPER(a.code_ref) as code, UPPER(a.designation) as intitule FROM yvs_base_classes_stat a WHERE a.code_ref IS NOT NULL ';
	ELSIF(COALESCE(nature_, 'C') = 'CP')THEN
		query_ = 'SELECT a.id::bigint, UPPER(a.code_ref) as code, UPPER(a.designation) as intitule FROM yvs_base_classes_stat a WHERE a.code_ref IS NOT NULL AND a.parent IS NULL';
	ELSIF(COALESCE(nature_, 'C') = 'F')THEN
		query_ = 'SELECT a.id::bigint, UPPER(a.reference_famille) as code, UPPER(a.designation) as intitule FROM yvs_base_famille_article a WHERE a.reference_famille IS NOT NULL ';
	ELSIF(COALESCE(nature_, 'C') = 'FP')THEN
		query_ = 'SELECT a.id::bigint, UPPER(a.reference_famille) as code, UPPER(a.designation) as intitule FROM yvs_base_famille_article a WHERE a.reference_famille IS NOT NULL AND a.famille_parent IS NULL';
	ELSE
		query_ = 'SELECT y.id::bigint, UPPER(y.code_users) as code, UPPER(y.nom_users) as intitule FROM yvs_users y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.code_users IS NOT NULL';
		IF(agence_ IS NOT NULL AND agence_ > 0)THEN
			query_ = query_ || ' AND a.id = '||agence_;
		END IF;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	ids_ = '0';
	IF(element_ IS NOT NULL AND element_ NOT IN ('', ' ', '0'))THEN
		FOR ligne_ IN select val from regexp_split_to_table(element_,',') val
		LOOP
			ids_ = ids_ ||','||ligne_.val;
		END LOOP;
		query_ = query_ || ' AND a.id in ('||ids_||')';
	END IF;
	FOR ligne_ IN EXECUTE query_
	LOOP
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
		LOOP
			query_valeur_ = 'SELECT SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y INNER JOIN yvs_com_doc_ventes d ON d.id = y.doc_vente INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc 
				INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id INNER JOIN yvs_com_creneau_point cp ON hu.creneau_point = cp.id INNER JOIN yvs_base_point_vente pt ON cp.point = pt.id
				WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND d.document_lie IS NULL AND e.date_entete BETWEEN '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''';
			IF(COALESCE(nature_, 'C') = 'C')THEN
				query_valeur_ = query_valeur_ || ' AND a.classe1 = '||ligne_.id;
			ELSIF(COALESCE(nature_, 'C') = 'CP')THEN
				query_valeur_ = query_valeur_ || ' AND (a.classe1 = '||ligne_.id||' OR a.classe1 IN (SELECT base_get_sous_classe_stat('||ligne_.id||', true)))';
			ELSIF(COALESCE(nature_, 'C') = 'F')THEN
				query_valeur_ = query_valeur_ || ' AND a.famille = '||ligne_.id;
			ELSIF(COALESCE(nature_, 'C') = 'FP')THEN
				query_valeur_ = query_valeur_ || ' AND (a.famille = '||ligne_.id||' OR a.famille IN (SELECT base_get_sous_famille('||ligne_.id||', true)))';
			ELSE
				query_valeur_ = query_valeur_ || ' AND hu.users = '||ligne_.id;
			END IF;
			IF(agence_ IS NOT NULL AND agence_ > 0)THEN
				query_ = query_ || ' AND pt.agence = '||agence_;
			END IF;
	RAISE NOTICE 'query_valeur_:%',query_valeur_;
			EXECUTE query_valeur_ INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_ca_by_periode values (ligne_.id, ligne_.code, ligne_.intitule, dates_.intitule, dates_.date_debut, dates_.date_fin, COALESCE(valeur_, 0), dates_.position);
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ca_by_periode ORDER BY _id_, _code_, _rang_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ca_by_periode(bigint, bigint, character varying, date, date, character varying, character varying)
  OWNER TO postgres;
