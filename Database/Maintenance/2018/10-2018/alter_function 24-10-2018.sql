-- Function: compta_et_credit(bigint, bigint, bigint, date, date, character varying)
-- DROP FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION compta_et_credit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, type_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   compte_ record;
   comptes_ record;
   credit_ double precision default 0;
   query_ CHARACTER VARYING;
   
begin 	
	query_ = 'select sum(c.credit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
		query_ = query_ || ' and j.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN		
		query_ = query_ || ' and a.societe = '||societe_;
	END IF;
	IF(type_ = 'T')THEN
		query_ = query_ ||' and c.compte_tiers = '||valeur_;
		EXECUTE query_ INTO credit_;
	ELSIF(type_ = 'C')THEN
		query_ = query_ ||' and c.compte_general = '||valeur_;
		SELECT INTO compte_ id, type_compte FROM yvs_base_plan_comptable WHERE id = valeur_;
		IF(compte_.id IS NOT NULL AND compte_.id > 0)then
			EXECUTE query_ INTO credit_;
			credit_ = COALESCE(credit_, 0);
			IF(compte_.type_compte = 'CO')THEN
				FOR comptes_ IN SELECT id FROM yvs_base_plan_comptable WHERE compte_general = valeur_
				LOOP
					credit_ = credit_ + compta_et_credit(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, type_);
				END LOOP;
			END IF;
		END IF;
	ELSE
		
	END IF;
	RETURN COALESCE(credit_, 0);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
  
  
-- Function: compta_et_debit(bigint, bigint, bigint, date, date, character varying)
-- DROP FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION compta_et_debit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, type_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   compte_ record;
   comptes_ record;
   debit_ double precision default 0;
   query_ CHARACTER VARYING;
   
begin 	
	query_ = 'select sum(c.debit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
		query_ = query_ || ' and j.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN		
		query_ = query_ || ' and a.societe = '||societe_;
	END IF;
	IF(type_ = 'T')THEN
		query_ = query_ ||' and c.compte_tiers = '||valeur_;
		EXECUTE query_ INTO debit_;
	ELSIF(type_ = 'C')THEN
		query_ = query_ ||' and c.compte_general = '||valeur_;
		SELECT INTO compte_ id, type_compte FROM yvs_base_plan_comptable WHERE id = valeur_;
		IF(compte_.id IS NOT NULL AND compte_.id > 0)then
			EXECUTE query_ INTO debit_;
			debit_ = COALESCE(debit_, 0);
			IF(compte_.type_compte = 'CO')THEN
				FOR comptes_ IN SELECT id FROM yvs_base_plan_comptable WHERE compte_general = valeur_
				LOOP
					debit_ = debit_ + compta_et_debit(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, type_);
				END LOOP;
			END IF;
		END IF;
	ELSE
		
	END IF;
	RETURN COALESCE(debit_, 0);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;

  
-- Function: compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying)
-- DROP FUNCTION compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION compta_et_balance(IN agence_ bigint, IN societe_ bigint, IN compte_debut_ character varying, IN compte_fin_ character varying, IN date_debut_ date, IN date_fin_ date, IN type_ character varying)
  RETURNS TABLE(numero bigint, debit_initial double precision, credit_initial double precision, debit_periode double precision, credit_periode double precision, debit_solde_periode double precision, credit_solde_periode double precision, debit_solde_cumul double precision, credit_solde_cumul double precision, is_general boolean) AS
$BODY$
declare 
	query_ character varying default '';
	connexion character varying default 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
	comptes_ record;
	sous_ bigint default 0;
	general_ boolean default false;
	
	di_ double precision default 0;
	ci_ double precision default 0;
	si_ double precision default 0;
	dp_ double precision default 0;
	cp_ double precision default 0;
	sp_ double precision default 0;
	dsp_ double precision default 0;
	csp_ double precision default 0;
	dsc_ double precision default 0;
	csc_ double precision default 0;
	ssc_ double precision default 0;
	
begin 	
	CREATE TEMP TABLE IF NOT EXISTS table_balance(num bigint, di double precision, ci double precision,
					dp double precision, cp double precision, dsp double precision,
					csp double precision, dsc double precision, csc double precision, is_gen boolean); 
	DELETE FROM table_balance;
	if(type_ = 'T')then
		query_ = 'select DISTINCT(t.id) as id, t.code_tiers as code from yvs_compta_content_journal y inner join yvs_base_tiers t on y.compte_tiers = t.id inner join yvs_agences a on a.societe = t.societe where t.code_tiers between '''||compte_debut_||''' and '''||compte_fin_||'''';			
	elsif(type_ = 'C') then
		query_ = 'select DISTINCT(c.id) as id, c.num_compte as code from yvs_compta_content_journal y inner join yvs_base_plan_comptable c on y.compte_general = c.id inner join yvs_base_nature_compte n on c.nature_compte = n.id inner join yvs_agences a on a.societe = n.societe where c.num_compte between '''||compte_debut_||''' and '''||compte_fin_||'''';			
	else

	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ || ' and a.id = '||agence_||'';
	end if;
	if(societe_ is not null and societe_ > 0)then
		query_ = query_ || ' and a.societe = '||societe_||'';
	end if;
	RAISE NOTICE 'query_ %',query_;
	if(query_ is not null and query_ != '')then
		FOR comptes_ IN EXECUTE query_
		loop
			dsp_ = 0;
			dsc_ = 0;		
			csp_ = 0;
			csc_ = 0;

			di_ = (select compta_et_debit_initial(agence_, societe_, comptes_.id, date_debut_, type_));
			ci_ = (select compta_et_credit_initial(agence_, societe_, comptes_.id, date_debut_, type_));
			si_ = di_ - ci_ ;
			
			dp_ = (select compta_et_debit(agence_, societe_, comptes_.id, date_debut_, date_fin_, type_));
			cp_ = (select compta_et_credit(agence_, societe_, comptes_.id, date_debut_, date_fin_, type_));	
			sp_ = dp_ - cp_ ;	
				
			if(si_ > 0)then
				di_ = si_;
				ci_ = 0;			
			elsif(si_ < 0) then
				ci_ = -(si_);
				di_ = 0;
			else
				ci_ = 0;
				di_ = 0;
			end if;		
				
			if(sp_ > 0)then
				dsp_ = sp_;
				csp_ = 0;			
			elsif(sp_ < 0)then
				csp_ = -(sp_);
				dsp_ = 0;
			else
				csp_ = 0;
				dsp_ = 0;
			end if;

			ssc_ = si_ + sp_;
			if(ssc_ > 0)then
				dsc_ = ssc_;
				csc_ = 0;			
			elsif(ssc_ < 0)then
				csc_ = -(ssc_);
				dsc_ = 0;
			else
				csc_ = 0;
				dsc_ = 0;
			end if;		
			
			if(di_ != 0 or ci_ != 0 or dp_ !=0 or cp_ != 0 or dsp_ != 0 or csp_ != 0 or dsc_ != 0 or csc_ !=0)then
				general_ = false;
				if(type_ = 'C')then
					select into sous_ count(c.id) from yvs_base_plan_comptable c where c.compte_general = comptes_.id;
					if(sous_ is not null and sous_ > 0)then
						general_ = true;
					end if;
				end if;
				insert into table_balance values(comptes_.id, di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_, general_);
			end if;
		end loop;
	end if;
	return QUERY SELECT * from table_balance order by num;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying)
  OWNER TO postgres;
