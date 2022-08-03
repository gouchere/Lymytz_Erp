-- Function: compta_et_solde(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying)
-- DROP FUNCTION compta_et_solde(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying);
CREATE OR REPLACE FUNCTION compta_et_solde(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, journal_ bigint, type_ character varying, general_ boolean, table_tiers_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   compte_ record;
   comptes_ record;
   solde_ double precision default 0;
   query_ CHARACTER VARYING;
   
begin 	
-- 	DROP TABLE IF EXISTS temp_yvs_compta_content_journal;
-- 	CREATE TEMP TABLE temp_yvs_compta_content_journal AS SELECT * FROM yvs_compta_content_journal;
	IF(type_ = 'A')THEN
		query_ = 'select sum(o.debit) - sum(o.credit) from yvs_compta_content_analytique o inner join yvs_compta_content_journal c on o.contenu = c.id inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where COALESCE(c.report, FALSE) IS FALSE';
	ELSE
		query_ = 'select sum(c.debit) - sum(c.credit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where COALESCE(c.report, FALSE) IS FALSE';
	END IF;
	IF(date_debut_ IS NOT NULL AND date_fin_ IS NOT NULL)THEN		
		query_ = query_ || ' AND p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
	ELSIF(date_fin_ IS NOT NULL)THEN		
		query_ = query_ || ' AND p.date_piece <= '''||date_fin_||'''';
	ELSIF(date_debut_ IS NOT NULL)THEN		
		query_ = query_ || ' AND p.date_piece >= '''||date_debut_||'''';
	END IF;
	IF(journal_ IS NOT NULL AND journal_ > 0)THEN		
		query_ = query_ || ' and j.id = '||journal_;
	END IF;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
		query_ = query_ || ' and j.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN		
		query_ = query_ || ' and a.societe = '||societe_;
	END IF;
	IF(type_ = 'T')THEN
		query_ = query_ ||' and c.compte_tiers = '||valeur_;
		IF(COALESCE(table_tiers_, '') NOT IN ('', ' '))THEN
			query_ = query_ ||' and c.table_tiers = '||QUOTE_LITERAL(table_tiers_);
		END IF;
		EXECUTE query_ INTO solde_;
	ELSIF(type_ = 'C')THEN
		query_ = query_ ||' and c.compte_general = '||valeur_;
		EXECUTE query_ INTO solde_;
		IF(general_)THEN
			SELECT INTO compte_ id, type_compte FROM yvs_base_plan_comptable WHERE id = valeur_;
			IF(compte_.id IS NOT NULL AND compte_.id > 0)then
				solde_ = COALESCE(solde_, 0);
				IF(compte_.type_compte = 'CO')THEN
					FOR comptes_ IN SELECT id FROM yvs_base_plan_comptable WHERE compte_general = valeur_
					LOOP
						solde_ = solde_ + compta_et_solde(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, journal_, type_, false, table_tiers_);
					END LOOP;
				END IF;
			END IF;
		END IF;
	ELSE
		query_ = query_ ||' and o.centre = '||valeur_;
		EXECUTE query_ INTO solde_;
	END IF;
	RETURN COALESCE(solde_, 0);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_solde(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying)
  OWNER TO postgres;
