-- Function: compta_total_caisse(bigint, bigint, bigint, character varying, character varying, character varying, character, date)

-- DROP FUNCTION compta_total_caisse(bigint, bigint, bigint, character varying, character varying, character varying, character, date);

CREATE OR REPLACE FUNCTION compta_total_caisse(societe_ bigint, caisse_ bigint, mode_ bigint, table_ character varying, mouvement_ character varying, type_ character varying, statut_ character, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	depense_ double precision default 0;
	recette_ double precision default 0;
	query_ CHARACTER VARYING DEFAULT 'SELECT COALESCE(SUM(COALESCE(y.montant, 0)), 0) FROM yvs_compta_mouvement_caisse y LEFT JOIN yvs_base_mode_reglement m ON y.model = m.id INNER JOIN yvs_agences a ON a.id=y.agence WHERE montant IS NOT NULL';
	query_0 CHARACTER VARYING DEFAULT '';

BEGIN
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(caisse_ IS NOT NULL AND caisse_ > 0)THEN
		query_ = query_ || ' AND y.caisse = '||caisse_;
	END IF;
	IF(mode_ IS NOT NULL AND mode_ > 0)THEN
		query_ = query_ || ' AND y.model = '||mode_;
	END IF;
	IF(date_ IS NOT NULL)THEN
		query_ = query_ || ' AND y.date_paye <= '''||date_||'''';
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
	IF(mouvement_ IS NOT NULL AND mouvement_ != '')THEN
		IF(mouvement_ = 'D')THEN
			query_ = query_ || ' AND y.mouvement = ''D''';
		ELSIF(mouvement_ = 'R')THEN
			query_ = query_ || ' AND y.mouvement = ''R''';
		ELSE
			query_0 = query_ || ' AND y.mouvement = ''D''';
			query_ = query_ || ' AND y.mouvement = ''R''';
		END IF;
	ELSE
		query_0 = query_ || ' AND y.mouvement = ''D''';
		query_ = query_ || ' AND y.mouvement = ''R''';		
	END IF;
	IF(query_0 IS NOT NULL AND query_0 != '')THEN
		EXECUTE query_0 INTO depense_;
	END IF;	
	EXECUTE query_ INTO recette_;
	RETURN COALESCE(recette_, 0) - COALESCE(depense_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_total_caisse(bigint, bigint, bigint, character varying, character varying, character varying, character, date)
  OWNER TO postgres;
COMMENT ON FUNCTION compta_total_caisse(bigint, bigint, bigint, character varying, character varying, character varying, character, date) IS 'retourne le total (dépense ou recette) d''une caisse ';
