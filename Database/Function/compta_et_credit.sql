-- Function: compta_et_credit(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying)
-- DROP FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying);
CREATE OR REPLACE FUNCTION compta_et_credit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, journal_ bigint, type_ character varying, general_ boolean, table_tiers_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   compte_ record;
   comptes_ record;
   credit_ double precision default 0;
   query_ CHARACTER VARYING;
   
begin 	
	IF(type_ = 'A')THEN
		query_ = 'select sum(o.credit) from yvs_compta_content_analytique o inner join yvs_compta_content_journal c on o.contenu = c.id inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where COALESCE(c.report, FALSE) IS FALSE AND p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
	ELSE
		query_ = 'select sum(c.credit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where COALESCE(c.report, FALSE) IS FALSE AND p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
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
		EXECUTE query_ INTO credit_;
	ELSIF(type_ = 'C')THEN
		query_ = query_ ||' and c.compte_general = '||valeur_;
		SELECT INTO compte_ id, type_compte FROM yvs_base_plan_comptable WHERE id = valeur_;
		IF(compte_.id IS NOT NULL AND compte_.id > 0)then
			EXECUTE query_ INTO credit_;
			credit_ = COALESCE(credit_, 0);
			IF(general_)THEN
				IF(compte_.type_compte = 'CO')THEN
					FOR comptes_ IN SELECT id FROM yvs_base_plan_comptable WHERE compte_general = valeur_
					LOOP
						credit_ = credit_ + compta_et_credit(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, type_);
					END LOOP;
				END IF;
			END IF;
		END IF;
	ELSE
		query_ = query_ ||' and o.centre = '||valeur_;
		EXECUTE query_ INTO credit_;
	END IF;
	RETURN COALESCE(credit_, 0);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying)
  OWNER TO postgres;
