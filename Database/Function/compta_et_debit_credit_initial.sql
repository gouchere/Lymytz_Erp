-- Function: compta_et_debit_credit_initial(bigint, bigint, bigint, date, bigint, character varying, boolean, character varying, boolean)
DROP FUNCTION compta_et_debit_credit_initial(bigint, bigint, bigint, date, bigint, character varying, boolean, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_debit_credit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_fin_ date, journal_ bigint, type_ character varying, general boolean, table_tiers_ character varying, IN delete_ boolean)
  RETURNS TABLE(debit double precision, credit double precision) AS
$BODY$
declare 
   compte_ record;
   comptes_ record;
   result_ record;
   
   count_ BIGINT;
   
   query_ CHARACTER VARYING;
   
   date_initial_ DATE DEFAULT '01-01-2000';
begin 	
	CREATE TEMP TABLE IF NOT EXISTS table_debit_credit_initial(_debit_ double precision, _credit_ double precision);
	IF(delete_)THEN
		DELETE FROM table_debit_credit_initial;
	END IF;
	SELECT INTO date_initial_ date_debut FROM yvs_base_exercice WHERE date_fin_ BETWEEN date_debut AND date_fin; 
	IF(type_ = 'A')THEN
		query_ = 'select SUM(o.debit) AS debit, SUM(o.credit) AS credit from yvs_compta_content_analytique o inner join yvs_compta_content_journal c on o.contenu = c.id inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where COALESCE(c.report, FALSE) IS TRUE AND p.date_piece between '''||date_initial_||''' and '''||date_fin_||'''';
	ELSE
		query_ = 'select SUM(c.debit) AS debit, SUM(c.credit) AS credit from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where COALESCE(c.report, FALSE) IS TRUE AND p.date_piece between '''||date_initial_||''' and '''||date_fin_||'''';
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
		EXECUTE query_ INTO result_;
		INSERT INTO table_debit_credit_initial VALUES(result_.debit, result_.credit);
	ELSIF(type_ = 'C')THEN
		query_ = query_ ||' and c.compte_general = '||valeur_;
		RAISE NOTICE 'query_ : %',query_;
		EXECUTE query_ INTO result_;
		SELECT INTO count_ COUNT(*) FROM table_debit_credit_initial;
		IF(COALESCE(count_, 0) > 0)THEN
			UPDATE table_debit_credit_initial SET _debit_ = COALESCE(_debit_, 0) +  COALESCE(result_.debit, 0), _credit_ = COALESCE(_credit_, 0) +  COALESCE(result_.credit, 0);
		ELSIF(COALESCE(result_.debit, 0) > 0 OR COALESCE(result_.credit, 0) > 0)THEN
			INSERT INTO table_debit_credit_initial VALUES(COALESCE(result_.debit, 0), COALESCE(result_.credit, 0));
		END IF;
		IF(general)THEN
			SELECT INTO compte_ id, type_compte FROM yvs_base_plan_comptable WHERE id = valeur_;
			IF(compte_.id IS NOT NULL AND compte_.id > 0)then
				IF(compte_.type_compte = 'CO')THEN
					FOR comptes_ IN SELECT id FROM yvs_base_plan_comptable WHERE compte_general = valeur_
					LOOP
						PERFORM compta_et_debit_credit_initial(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, journal_, type_, general, table_tiers_, false);
					END LOOP;
				END IF;
			END IF;
		END IF;
	ELSE
		query_ = query_ ||' and o.centre = '||valeur_;
		EXECUTE query_ INTO result_;
		INSERT INTO table_debit_credit_initial VALUES(result_.debit, result_.credit);
	END IF;	
	return QUERY SELECT * FROM table_debit_credit_initial;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit_credit_initial(bigint, bigint, bigint, date, bigint, character varying, boolean, character varying, boolean)
  OWNER TO postgres;
