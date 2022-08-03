-- Function: compta_total_caisses(bigint, bigint, character varying,bigint, character varying, character varying, character varying, character, date, date, character varying)
-- DROP FUNCTION compta_total_caisses(bigint, bigint, character varying, bigint, character varying, character varying, character varying, character, date, date, character varying);
CREATE OR REPLACE FUNCTION compta_total_caisses(societe_ bigint, agence_ bigint, caisse_ character varying, mode_ bigint, table_ character varying, mouvement_ character varying, type_ character varying, statut_ character, date_debut_ date, date_fin_ date, periode_ character varying)
  RETURNS TABLE(caisse bigint, code character varying, intitule character varying, periode character varying, recette double precision, depense double precision, solde double precision, rang integer) AS
$BODY$
DECLARE
	caisses_ RECORD;
	dates_ RECORD;

	depense_ double precision default 0;
	recette_ double precision default 0;
	query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.code, y.intitule FROM yvs_base_caisse y INNER JOIN yvs_compta_journaux j ON y.journal = j.id INNER JOIN yvs_agences a ON a.id=j.agence';
	query_total CHARACTER VARYING DEFAULT 'SELECT COALESCE(SUM(COALESCE(y.montant, 0)), 0) FROM yvs_compta_mouvement_caisse y LEFT JOIN yvs_base_mode_reglement m ON y.model = m.id INNER JOIN yvs_agences a ON a.id=y.agence WHERE montant IS NOT NULL';

BEGIN
	DROP TABLE IF EXISTS table_total_caisses;
	CREATE TEMP TABLE IF NOT EXISTS table_total_caisses(_caisse_ bigint, _code_ character varying, _intitule_ character varying, _periode_ character varying, _recette_ double precision, _depense_ double precision, _solde_ double precision, _rang_ integer);
	IF(COALESCE(societe_, 0) > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND j.agence = '||agence_;
	END IF;
	IF(caisse_ IS NOT NULL AND caisse_ != '')THEN
		query_ = query_ || ' AND y.id::character varying IN (SELECT TRIM(val) FROM regexp_split_to_table('''||caisse_||''', '','') val)';
	END IF;
-- 	RAISE NOTICE 'query_ : %',query_;
	FOR caisses_ IN EXECUTE query_
	LOOP
		for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_, true)
		loop
			query_ = query_total || ' AND y.caisse = '||caisses_.id;
			IF(mode_ IS NOT NULL AND mode_ > 0)THEN
				query_ = query_ || ' AND y.model = '||mode_;
			END IF;
			IF(table_ IS NOT NULL AND table_ != '')THEN
				query_ = query_ || ' AND y.table_externe = '''||table_||'''';
			END IF;
			IF(statut_ IS NOT NULL AND statut_ != '')THEN
				query_ = query_ || ' AND y.statut_piece = '''||statut_||'''';
			END IF;
			IF(type_ IS NOT NULL AND type_ != '')THEN
				query_ = query_ || ' AND m.type_reglement IN (SELECT TRIM(val) FROM regexp_split_to_table('''||type_||''','','') val)';
			END IF;
			query_ = query_ || ' AND y.date_paye BETWEEN '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''';
			IF(COALESCE(mouvement_, '') = 'D' OR COALESCE(mouvement_, '') = 'R')THEN
				IF(mouvement_ = 'D')THEN
					EXECUTE query_ || ' AND y.mouvement = ''D''' INTO depense_;
				ELSIF(mouvement_ = 'R')THEN
					EXECUTE query_ || ' AND y.mouvement = ''R''' INTO recette_;
				END IF;
			ELSE
				EXECUTE query_ || ' AND y.mouvement = ''D''' INTO depense_;
				EXECUTE query_ || ' AND y.mouvement = ''R''' INTO recette_;				
			END IF;
			IF(COALESCE(recette_, 0) != 0 or COALESCE(depense_, 0) != 0)THEN
				INSERT INTO table_total_caisses VALUES(caisses_.id, caisses_.code, caisses_.intitule, dates_.intitule, recette_, depense_, (recette_ - depense_), dates_.position);
			END IF;
		END LOOP;
	END LOOP;
	return QUERY select * from table_total_caisses order by _caisse_, _rang_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_total_caisses(bigint, bigint, character varying,bigint, character varying, character varying, character varying, character, date, date, character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION compta_total_caisses(bigint, bigint, character varying,bigint, character varying, character varying, character varying, character, date, date, character varying) IS 'retourne le total (dépense ou recette) d''une caisse ';
