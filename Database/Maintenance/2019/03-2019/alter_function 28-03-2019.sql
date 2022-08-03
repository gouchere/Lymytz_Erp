-- Function: compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean, character varying)
-- DROP FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean, character varying);
CREATE OR REPLACE FUNCTION compta_et_credit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, type_ character varying, general_ boolean, table_tiers_ character varying)
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
			where p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
	ELSE
		query_ = 'select sum(c.credit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
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
ALTER FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean, character varying)
  OWNER TO postgres;

  
-- Function: compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean)
-- DROP FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_credit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, type_ character varying, general_ boolean)
  RETURNS double precision AS
$BODY$
declare 
   
begin 	
	RETURN compta_et_credit(agence_, societe_, valeur_, date_debut_, date_fin_, type_ , general_, 'TIERS');
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean)
  OWNER TO postgres;

  

-- Function: compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean, character varying)
-- DROP FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean, character varying);
CREATE OR REPLACE FUNCTION compta_et_debit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, type_ character varying, general boolean, table_tiers_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   compte_ record;
   comptes_ record;
   debit_ double precision default 0;
   query_ CHARACTER VARYING;
   
begin 	
	IF(type_ = 'A')THEN
		query_ = 'select sum(o.debit) from yvs_compta_content_analytique o inner join yvs_compta_content_journal c on o.contenu = c.id inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
	ELSE
		query_ = 'select sum(c.debit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
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
		EXECUTE query_ INTO debit_;
	ELSIF(type_ = 'C')THEN
		query_ = query_ ||' and c.compte_general = '||valeur_;
		SELECT INTO compte_ id, type_compte FROM yvs_base_plan_comptable WHERE id = valeur_;
		IF(compte_.id IS NOT NULL AND compte_.id > 0)then
			EXECUTE query_ INTO debit_;
			debit_ = COALESCE(debit_, 0);
			IF(general)THEN
				IF(compte_.type_compte = 'CO')THEN
					FOR comptes_ IN SELECT id FROM yvs_base_plan_comptable WHERE compte_general = valeur_
					LOOP
						debit_ = debit_ + compta_et_debit(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, type_);
					END LOOP;
				END IF;
			END IF;
		END IF;
	ELSE
		query_ = query_ ||' and o.centre = '||valeur_;
		EXECUTE query_ INTO debit_;
	END IF;
	RETURN COALESCE(debit_, 0);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean, character varying)
  OWNER TO postgres;

  
-- Function: compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean)
-- DROP FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_debit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, type_ character varying, general boolean)
  RETURNS double precision AS
$BODY$
declare 
   
begin 	
	RETURN compta_et_debit(agence_, societe_, valeur_, date_debut_, date_fin_, type_, general, 'TIERS');
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean)
  OWNER TO postgres;
  
  
-- Function: compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean, character varying)
-- DROP FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean, character varying);
CREATE OR REPLACE FUNCTION compta_et_credit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, type_ character varying, general boolean, table_tiers_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   date_fin_ date;
   exo_ record;
   
begin 	
	select into exo_ date_debut , date_fin from yvs_base_exercice where date_debut_ between date_debut and date_fin;

	if(exo_.date_debut = date_debut_)then
		date_fin_ = date_debut_ - interval '1 month';		
		select into exo_ date_debut , date_fin from yvs_base_exercice where date_fin_ between date_debut and date_fin;
		date_fin_ = exo_.date_fin;			
	else
		date_fin_ = date_debut_ - interval '1 day';
	end if;
	
	return (select compta_et_credit(agence_, societe_, valeur_, date_debut_, date_fin_, type_, general, table_tiers_));
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean, character varying)
  OWNER TO postgres;

  
-- Function: compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean)
-- DROP FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_credit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, type_ character varying, general boolean)
  RETURNS double precision AS
$BODY$
declare 
   
begin 	
	return compta_et_credit_initial(agence_, societe_, valeur_, date_debut_, type_, general, 'TIERS');
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean)
  OWNER TO postgres;

  
  
-- Function: compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean, character varying)
-- DROP FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_debit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, type_ character varying, general boolean, table_tiers_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   date_fin_ date;
   exo_ record;
   
begin 	
	select into exo_ date_debut , date_fin from yvs_base_exercice where date_debut_ between date_debut and date_fin;

	if(exo_.date_debut = date_debut_)then
		date_fin_ = date_debut_ - interval '1 month';		
		select into exo_ date_debut , date_fin from yvs_base_exercice where date_fin_ between date_debut and date_fin;
		date_fin_ = exo_.date_fin;			
	else
		date_fin_ = date_debut_ - interval '1 day';
	end if;
	
	return (select compta_et_debit(agence_, societe_, valeur_, date_debut_, date_fin_, type_, general, table_tiers_));
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean, character varying)
  OWNER TO postgres;

  
-- Function: compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean)
-- DROP FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_debit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, type_ character varying, general boolean)
  RETURNS double precision AS
$BODY$
declare 
   
begin 	
	return compta_et_debit_initial(agence_, societe_, valeur_, date_debut_, type_, general, 'TIERS');
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean)
  OWNER TO postgres;

  
  
-- Function: compta_et_balance(bigint, bigint, character varying, date, date, character varying)
-- DROP FUNCTION compta_et_balance(bigint, bigint, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION compta_et_balance(IN agence_ bigint, IN societe_ bigint, IN comptes_ character varying, IN date_debut_ date, IN date_fin_ date, IN type_ character varying)
  RETURNS TABLE(numero bigint, code character varying, general character varying, debit_initial double precision, credit_initial double precision, debit_periode double precision, credit_periode double precision, debit_solde_periode double precision, credit_solde_periode double precision, debit_solde_cumul double precision, credit_solde_cumul double precision, is_general boolean, table_tiers character varying, id_general bigint) AS
$BODY$
declare 
	query_ character varying default '';
	connexion character varying default 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
	data_ record;
	sous_ bigint default 0;
	general_ boolean default false;
	
	gen_ record;
	
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
	
	ids_ character varying default '''0''';
	valeur_ character varying default '0';
	
begin 	
	DROP TABLE IF EXISTS table_balance;
	CREATE TEMP TABLE IF NOT EXISTS table_balance(num bigint, cod character varying, gen character varying, di double precision, ci double precision, dp double precision, cp double precision,
							dsp double precision, csp double precision, dsc double precision, csc double precision, is_gen boolean, tt character varying, id_gen bigint); 
	DELETE FROM table_balance;
	if(type_ = 'T')then
		query_ = 'select DISTINCT(y.compte_tiers) as id, name_tiers(y.compte_tiers, y.table_tiers, ''R'') as code, y.table_tiers from yvs_compta_content_journal y inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where coalesce(y.compte_tiers, 0) > 0';			
	elsif(type_ = 'C') then
		query_ = 'select DISTINCT(c.id) as id, c.num_compte as code, null as table_tiers from yvs_compta_content_journal y inner join yvs_base_plan_comptable c on y.compte_general = c.id inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where c.num_compte is not null';			
	else
		query_ = 'select DISTINCT(c.id) as id, c.code_ref as code, null as table_tiers from yvs_compta_content_analytique o INNER JOIN yvs_compta_content_journal y ON o.contenu = y.id inner join yvs_compta_centre_analytique c on o.centre = c.id inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where c.code_ref is not null';
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ || ' and a.id = '||agence_||'';
	end if;
	if(societe_ is not null and societe_ > 0)then
		query_ = query_ || ' and a.societe = '||societe_||'';
	end if;
	IF(COALESCE(comptes_, '') NOT IN ('', ' ', '0'))THEN
		FOR valeur_ IN SELECT head FROM regexp_split_to_table(comptes_,',') head WHERE CHAR_LENGTH(head) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(valeur_);
		END LOOP;
		IF(type_ = 'T')THEN
			query_ = query_ || ' AND name_tiers(y.compte_tiers, y.table_tiers, ''R'') IN ('||ids_||')';		
		ELSIF(type_ = 'C') THEN
			query_ = query_ || ' AND c.num_compte IN ('||ids_||')';	
		ELSE
			query_ = query_ || ' AND c.code_ref IN ('||ids_||')';	
		END IF;
	END IF;
	if(query_ is not null and query_ != '')then
		FOR data_ IN EXECUTE query_
		loop
			dsp_ = 0;
			dsc_ = 0;		
			csp_ = 0;
			csc_ = 0;

			di_ = (select compta_et_debit_initial(agence_, societe_, data_.id, date_debut_, type_, false, data_.table_tiers));
			ci_ = (select compta_et_credit_initial(agence_, societe_, data_.id, date_debut_, type_, false, data_.table_tiers));
			si_ = di_ - ci_ ;
			
			dp_ = (select compta_et_debit(agence_, societe_, data_.id, date_debut_, date_fin_, type_, false, data_.table_tiers));
			cp_ = (select compta_et_credit(agence_, societe_, data_.id, date_debut_, date_fin_, type_, false, data_.table_tiers));	
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
					select into gen_ coalesce(g.id, c.id) as id, coalesce(g.num_compte, c.num_compte) as numero from yvs_base_plan_comptable g right join yvs_base_plan_comptable c on g.id = c.compte_general where c.id = data_.id;
					select into sous_ count(c.id) from yvs_base_plan_comptable c where c.compte_general = data_.id;
					if(sous_ is not null and sous_ > 0)then
						general_ = true;
					end if;
					insert into table_balance values(data_.id, data_.code, gen_.numero, di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_, general_, data_.table_tiers, gen_.id);
				else
					insert into table_balance values(data_.id, data_.code, '', di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_, general_, data_.table_tiers, 0);
				end if;
			end if;
		end loop;
	end if;
	return QUERY SELECT * from table_balance order by num;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_balance(bigint, bigint, character varying, date, date, character varying)
  OWNER TO postgres;
