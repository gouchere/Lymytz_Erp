CREATE OR REPLACE FUNCTION compta_et_debit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, type_ character varying)
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
	
	return (select compta_et_debit(agence_, societe_, valeur_, date_debut_, date_fin_, type_));
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying)
  OWNER TO postgres;  
  
  
CREATE OR REPLACE FUNCTION compta_et_debit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, type_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   compte_ record;
   comptes_ record;
   debit_ double precision default 0;
   
begin 	
	if(agence_ is not null and agence_ > 0)then			
		if(type_ = 'T')then
			select into debit_ sum(c.debit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux a on a.id = p.journal 
			where a.agence = agence_ and c.compte_tiers = valeur_ and p.date_piece between date_debut_ and date_fin_;
		elsif(type_ = 'C') then
			select into compte_ id, type_compte from yvs_base_plan_comptable where id = valeur_;
			if(compte_ is not null and compte_.id is not null)then
				select into debit_ sum(c.debit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux a on a.id = p.journal 
					where a.agence = agence_ and c.compte_general = valeur_ and p.date_piece between date_debut_ and date_fin_;
				if(compte_.type_compte = 'CO')then
					if(debit_ is null)then
						debit_ = 0;
					end if;
					for comptes_ in select id, type_compte from yvs_base_plan_comptable where compte_general = valeur_
					loop
						debit_ = debit_ + compta_et_debit(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, type_);
					end loop;
				end if;
			end if;
		else
			
		end if;
	else
		if(societe_ is not null and societe_ > 0)then	
			if(type_ = 'T')then
				select into debit_ sum(c.debit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux a on a.id = p.journal inner join yvs_agences e on a.agence = e.id 
				where e.societe = societe_ and c.compte_tiers = valeur_ and p.date_piece between date_debut_ and date_fin_;
			elsif(type_ = 'C') then
				select into compte_ id, type_compte from yvs_base_plan_comptable where id = valeur_;
				if(compte_ is not null and compte_.id is not null)then
					select into debit_ sum(c.debit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux a on a.id = p.journal inner join yvs_agences e on a.agence = e.id 
						where e.societe = societe_ and c.compte_general = valeur_ and p.date_piece between date_debut_ and date_fin_;
					if(compte_.type_compte = 'CO')then
						if(debit_ is null)then
							debit_ = 0;
						end if;
						for comptes_ in select id, type_compte from yvs_base_plan_comptable where compte_general = valeur_
						loop
							debit_ = debit_ + compta_et_debit(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, type_);
						end loop;
					end if;
				end if;
			else
				
			end if;
		end if;
	end if;	
	if(debit_ is null)then
		debit_ = 0;
	end if;
	return debit_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
  
  CREATE OR REPLACE FUNCTION compta_et_credit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, type_ character varying)
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
	
	return (select compta_et_credit(agence_, societe_, valeur_, date_debut_, date_fin_, type_));
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, character varying)
  OWNER TO postgres;  
  
  
  
CREATE OR REPLACE FUNCTION compta_et_credit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, type_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   compte_ record;
   comptes_ record;
   credit_ double precision default 0;
   
begin 	
	if(agence_ is not null and agence_ > 0)then			
		if(type_ = 'T')then
			select into credit_ sum(c.credit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux a on a.id = p.journal 
			where a.agence = agence_ and c.compte_tiers = valeur_ and p.date_piece between date_debut_ and date_fin_;
		elsif(type_ = 'C') then
			select into compte_ id, type_compte from yvs_base_plan_comptable where id = valeur_;
			if(compte_ is not null and compte_.id is not null)then
				select into credit_ sum(c.credit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux a on a.id = p.journal 
					where a.agence = agence_ and c.compte_general = valeur_ and p.date_piece between date_debut_ and date_fin_;
				if(compte_.type_compte = 'CO')then
					if(credit_ is null)then
						credit_ = 0;
					end if;
					for comptes_ in select id, type_compte from yvs_base_plan_comptable where compte_general = valeur_
					loop
						credit_ = credit_ + compta_et_credit(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, type_);
					end loop;
				end if;
			end if;
		else
			
		end if;
	else
		if(societe_ is not null and societe_ > 0)then	
			if(type_ = 'T')then
				select into credit_ sum(c.credit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux a on a.id = p.journal inner join yvs_agences e on a.agence = e.id 
				where e.societe = societe_ and c.compte_tiers = valeur_ and p.date_piece between date_debut_ and date_fin_;
			elsif(type_ = 'C') then
				select into compte_ id, type_compte from yvs_base_plan_comptable where id = valeur_;
				if(compte_ is not null and compte_.id is not null)then
					select into credit_ sum(c.credit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux a on a.id = p.journal inner join yvs_agences e on a.agence = e.id 
						where e.societe = societe_ and c.compte_general = valeur_ and p.date_piece between date_debut_ and date_fin_;
					if(compte_.type_compte = 'CO')then
						if(credit_ is null)then
							credit_ = 0;
						end if;
						for comptes_ in select id, type_compte from yvs_base_plan_comptable where compte_general = valeur_
						loop
							credit_ = credit_ + compta_et_credit(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, type_);
						end loop;
					end if;
				end if;
			else
				
			end if;
		end if;
	end if;	
	if(credit_ is null)then
		credit_ = 0;
	end if;
	return credit_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
  
 
  -- Function: et_balance_analytique(character varying, character varying, character varying, date, date)

--DROP FUNCTION compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying);

CREATE OR REPLACE FUNCTION compta_et_balance(IN agence_ bigint, IN societe_ bigint, IN compte_debut_ character varying, IN compte_fin_ character varying, IN date_debut_ date, IN date_fin_ date, type_ character varying)
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

	if(agence_ is not null and agence_ > 0)then
		if(type_ = 'T')then
			query_ = 'select DISTINCT(t.id), t.code_tiers from yvs_compta_content_journal y inner join yvs_base_tiers t on y.compte_tiers = t.id inner join yvs_agences a on a.societe = t.societe where a.id = '||agence_||' and t.code_tiers between '''||compte_debut_||''' and '''||compte_fin_||'''';			
		elsif(type_ = 'C') then
			query_ = 'select DISTINCT(c.id), c.num_compte from yvs_compta_content_journal y inner join yvs_base_plan_comptable c on y.compte_general = c.id inner join yvs_base_nature_compte n on c.nature_compte = n.id inner join yvs_agences a on a.societe = n.societe where a.id = '||agence_||' and c.num_compte between '''||compte_debut_||''' and '''||compte_fin_||'''';			
		else

		end if;
	elsif(societe_ is not null and societe_ > 0)then
		if(type_ = 'T')then
			query_ = 'select DISTINCT(t.id), t.code_tiers from yvs_compta_content_journal y inner join yvs_base_tiers t on y.compte_tiers = t.id where t.societe = '||societe_||' and t.code_tiers between '''||compte_debut_||''' and '''||compte_fin_||'''';			
		elsif(type_ = 'C') then
			query_ = 'select DISTINCT(c.id), c.num_compte from yvs_compta_content_journal y inner join yvs_base_plan_comptable c on y.compte_general = c.id inner join yvs_base_nature_compte n on c.nature_compte = n.id where n.societe = '||societe_|||' and c.num_compte between '''||compte_debut_||''' and '''||compte_fin_||'''';			
		else

		end if;
	end if;
	RAISE NOTICE 'query_ %',query_;
	if(query_ is not null and query_ != '')then
		FOR comptes_  IN SELECT * FROM dblink(connexion, query_) AS t(id bigint, code character varying)
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

  
   -- Function: et_dashboard(character varying, character varying, character varying, date, character varying, character varying)

--DROP FUNCTION compta_et_dashboard(bigint, bigint, character varying, character varying, date, character varying, character varying);

CREATE OR REPLACE FUNCTION compta_et_dashboard(IN agence_ bigint, IN societe_ bigint, IN comptedebut_ character varying, IN comptefin_ character varying, IN current_ date, IN type_ character varying, IN periode_ character varying)
  RETURNS TABLE(numero character varying, entete character varying, valeur character varying, montant double precision, num_classe character varying, is_head boolean, is_produit boolean, is_charge boolean, is_total boolean) AS
$BODY$
declare 
	query_ character varying default '';
	connexion character varying default 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
	
	comptes_ record;
	exo_ record;
	
	jour_ character varying;
	
	valeur_ character varying;
	solde_ double precision default 0;
	debit_ double precision default 0;
	credit_ double precision default 0;

	date_save_ date;
	date_debut_ date;
	date_fin_ date;
	date_ date;
   
	pdt_ boolean;
	chg_ boolean;
	compteur_ integer default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
	CREATE TEMP TABLE IF NOT EXISTS table_dashboard(num character varying, titre character varying, element character varying, mtant double precision, classe character varying, head boolean, produit boolean, charge boolean, total boolean);
	DELETE FROM table_dashboard;

	-- Recherche de l'exercice
	select into exo_ id, date_debut, date_fin from yvs_base_exercice where current_ between date_debut and date_fin limit 1;
	if(exo_ is not null and exo_.id is not null)then
		date_save_ = exo_.date_debut;
		date_fin_ = exo_.date_fin;

		query_ = 'select c.id, c.num_compte, c.intitule, substring(c.num_compte, 0,2) as classe from yvs_base_plan_comptable c inner join yvs_base_nature_compte n on n.id = c.nature_compte where c.id in (select y.compte_general from yvs_base_plan_comptable y where y.compte_general is not null) and ';
		if(agence_ is not null and agence_ > 0)then
			if(type_ = 'R')then -- Operation sur le 'Resultat'
				query_ = query_ || 'n.societe = (select a.societe from yvs_agences a where a.id = '||agence_||') and (c.num_compte like ''6%'' or c.num_compte like ''7%'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
			elsif(type_ = 'P')then -- Operation sur le 'Produit'
				query_ = query_ || 'n.societe = (select a.societe from yvs_agences a where a.id = '||agence_||') and (c.num_compte like ''7%'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
			elsif(type_ = 'C')then -- Operation sur le 'Charge'
				query_ = query_ || 'n.societe = (select a.societe from yvs_agences a where a.id = '||agence_||') and (c.num_compte like ''6%'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
			elsif(type_ = 'B')then -- Operation sur le 'Bilan'
				query_ = query_ || 'n.societe = (select a.societe from yvs_agences a where a.id = '||agence_||') and (substring(c.num_compte, 0,2) < ''6'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
			else -- Operation sur la 'Balance'
				query_ = query_ || 'n.societe = (select a.societe from yvs_agences a where a.id = '||agence_||') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
			end if;
		elsif(societe_ is not null and societe_ > 0)then
			if(type_ = 'R')then -- Operation sur le 'Resultat'
				query_ = query_ || 'n.societe = '||societe_||' and (c.num_compte like ''6%'' or c.num_compte like ''7%'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
			elsif(type_ = 'P')then -- Operation sur le 'Produit'
				query_ = query_ || 'n.societe = '||societe_||' and (c.num_compte like ''7%'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
			elsif(type_ = 'C')then -- Operation sur le 'Charge'
				query_ = query_ || 'n.societe = '||societe_||' and (c.num_compte like ''6%'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
			elsif(type_ = 'B')then -- Operation sur le 'Bilan'
				query_ = query_ || 'n.societe = '||societe_||' and (substring(c.num_compte, 0,2) < ''6'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
			else -- Operation sur la 'Balance'
				query_ = query_ || 'n.societe = '||societe_||' and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
			end if;
		end if;

		RAISE NOTICE 'query_ %',query_;
		if(query_ is not null and query_ != '')then
			RAISE NOTICE 'INT %','THIS';
			FOR comptes_  IN SELECT * FROM dblink(connexion, query_) AS t(id bigint, code character varying, intitule character varying, classe character varying)
			loop
				-- Définition si c'est un compte produit ou charge
				if(comptes_.classe = '6')then
					pdt_ = false;
					chg_ = true;
				elsif(comptes_.classe = '7')then
					pdt_ = true;
					chg_ = false;
				else
					pdt_ = false;
					chg_ = false;
				end if;
				
				date_debut_ = date_save_; -- Reinitialisation de la date début;
				if(periode_ = 'A')then -- Operation sur l'année		
					-- Récuperation du débit du compte
					debit_ = (select compta_et_debit(agence_, societe_, comptes_.id, date_debut_, date_fin_, 'C'));
					if(debit_ is null)then
						debit_ = 0;
					end if;	
					insert into table_dashboard values (comptes_.code, 'DEBIT', comptes_.intitule, debit_, comptes_.classe, false, pdt_, chg_, false); --/

					-- Récuperation du total débit de la classe
					select into valeur_ num from table_dashboard where classe = comptes_.classe and titre = 'DEBIT' and head = true and num = 'TOTAL';
					if(valeur_ is null)then
						insert into table_dashboard values ('TOTAL', 'DEBIT', 'CLASSE '||comptes_.classe, debit_, comptes_.classe, true, pdt_, chg_, false); --/
					else
						update table_dashboard set mtant = mtant + debit_ where classe = comptes_.classe and titre = 'DEBIT' and head = true and num = 'TOTAL';
					end if;
						
					-- Récuperation du crédit du compte
					credit_ = (select compta_et_credit(agence_, societe_, comptes_.id, date_debut_, date_fin_, 'C'));
					if(credit_ is null)then
						credit_ = 0;
					end if;	
					insert into table_dashboard values (comptes_.code, 'CREDIT', comptes_.intitule, -(credit_), comptes_.classe, false, pdt_, chg_, false); --/

					-- Récuperation du total crédit de la classe
					select into valeur_ num from table_dashboard where classe = comptes_.classe and titre = 'CREDIT' and head = true and num = 'TOTAL';
					if(valeur_ is null)then
						insert into table_dashboard values ('TOTAL', 'CREDIT', 'CLASSE '||comptes_.classe, -(credit_), comptes_.classe, true, pdt_, chg_, false); --/
					else
						update table_dashboard set mtant = mtant - credit_ where classe = comptes_.classe and titre = 'CREDIT' and head = true and num = 'TOTAL';
					end if;

				else -- Operation autre que sur l'année	
					compteur_ = 1; -- Reinitialisation du compteur des trimestres;
					while(date_debut_ <= date_fin_)
					loop
						if(periode_ = 'T')then -- Operation sur le trimestre
							date_ = (date_debut_ + interval '3 month' - interval '1 day');

							 -- Construction de l'entete de la colonne
							jour_ = 'Trim '||compteur_;
							compteur_ = compteur_ +1 ;
							
						else -- Operation sur le mois
							date_ = (date_debut_ + interval '1 month' - interval '1 day');
							
							 -- Construction de l'entete de la colonne
							jour_ = (select extract(month from date_debut_));
							if(char_length(jour_)<2)then
								jour_ = '0'||jour_;
							end if; 
						end if;

						-- Récuperation du débit du compte
						debit_ = (select compta_et_debit(agence_, societe_, comptes_.id, date_debut_, date_fin_, 'C'));
						if(debit_ is null)then
							debit_ = 0;
						end if;	
						-- Récuperation du crédit du compte
						credit_ = (select compta_et_credit(agence_, societe_, comptes_.id, date_debut_, date_fin_, 'C'));
						if(credit_ is null)then
							credit_ = 0;
						end if;	
							
						-- Récuperation du solde du compte
						solde_ = (debit_ - credit_);
						insert into table_dashboard values (comptes_.code, jour_, comptes_.intitule, solde_, comptes_.classe, false, pdt_, chg_, false); --/

						-- Récuperation du total 'jour_' de la classe
						select into valeur_ num from table_dashboard where classe = comptes_.classe and titre = jour_ and head = true and num = 'TOTAL';
						if(valeur_ is null)then
							insert into table_dashboard values ('TOTAL', jour_, 'CLASSE '||comptes_.classe, solde_, comptes_.classe, true, pdt_, chg_, false); --/
						else
							update table_dashboard set mtant = mtant + solde_ where classe = comptes_.classe and titre = jour_ and head = true and num = 'TOTAL';
						end if;
						
						if(periode_ = 'T')then
							date_debut_ = date_debut_ + interval '3 month';
						else
							date_debut_ = date_debut_ + interval '1 month';
						end if;
					end loop;
				end if;
			end loop;
		end if;

		 -- Recuperation du total global des colonnes
		if(periode_ = 'A')then -- Operation sur l'année	
			-- Récuperation du total global de débit
			select into solde_ sum(mtant) from table_dashboard where titre = 'DEBIT' and head = false;
			insert into table_dashboard values ('', 'DEBIT', '', solde_, 'Z', false, false, false, true); --/

			-- Récuperation du total global de crédit
			select into solde_ sum(mtant) from table_dashboard where titre = 'CREDIT' and head = false;
			insert into table_dashboard values ('', 'CREDIT', '', solde_, 'Z', false, false, false, true); --/

		else -- Operation autre que sur l'année	
			date_debut_ = date_save_; -- Reinitialisation de la date début;
			compteur_ = 1; -- Reinitialisation du compteur des trimestres;
			while(date_debut_ <= date_fin_)
			loop
				if(periode_ = 'T')then -- Operation sur le trimestre
					date_ = (date_debut_ + interval '3 month' - interval '1 day');

					 -- Construction de l'entete de la colonne
					jour_ = 'Trim '||compteur_;
					compteur_ = compteur_ +1 ;
					
				else -- Operation sur le mois
					date_ = (date_debut_ + interval '1 month' - interval '1 day');
					
					 -- Construction de l'entete de la colonne
					jour_ = (select extract(month from date_debut_));
					if(char_length(jour_)<2)then
						jour_ = '0'||jour_;
					end if; 
				end if;
					
				-- Récuperation du total global de crédit
				select into solde_ sum(mtant) from table_dashboard where titre = jour_ and head = false;
				insert into table_dashboard values ('', jour_, '', solde_, 'Z', false, false, false, true); --/
				
				if(periode_ = 'T')then
					date_debut_ = date_debut_ + interval '3 month';
				else
					date_debut_ = date_debut_ + interval '1 month';
				end if;
			end loop;
		end if;
	end if;
	return QUERY select * from table_dashboard order by classe, num;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_dashboard(bigint, bigint, character varying, character varying, date, character varying, character varying)
  OWNER TO postgres;
  
  
  
DROP FUNCTION grh_et_livre_paie(bigint, character varying, character varying);
DROP FUNCTION grh_et_livre_paie_service(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_livre_paie(IN header_ bigint, IN debut_ character varying, IN fin_ character varying, type_ character varying)
  RETURNS TABLE(regle bigint, element bigint, montant double precision) AS
$BODY$
declare 
	element_ bigint;
	regles_ record;
	valeur_ double precision default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
	CREATE TEMP TABLE IF NOT EXISTS table_livre_paie(_regle bigint, _element bigint, _montant double precision);
	delete from table_livre_paie;
	if((header_ is not null and header_ > 0) and (type_ is not null))then
		for regles_ in select distinct(e.id) as id, e.retenue, o.societe from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
			inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_ordre_calcul_salaire o on b.entete = o.id where e.visible_on_livre_paie is true and o.id = header_
		loop
			if(type_ = 'E')then
				for element_ in select distinct(e.id) from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id inner join yvs_agences a on e.agence = a.id where e.matricule between debut_ and fin_ and a.societe = regles_.societe
				loop
					if(regles_.retenue is false)then
						select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regles_.id and c.employe = element_ and b.entete = header_;
					else
						select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regles_.id and c.employe = element_ and b.entete = header_;
					end if;
					insert into table_livre_paie values(regles_.id, element_, valeur_);
				end loop;
			elsif(type_ = 'S')then
				for element_ in select distinct(d.id) from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id inner join yvs_agences a on e.agence = a.id 
				inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id inner join yvs_grh_departement d on p.departement = d.id where d.visible_on_livre_paie is true and d.code_departement between debut_ and fin_ and a.societe = regles_.societe
				loop
					if(regles_.retenue is false)then
						select into valeur_ coalesce(sum(d.montant_payer), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
							inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id where d.element_salaire = regles_.id and p.departement = element_ and b.entete = header_;
					else
						select into valeur_ coalesce(sum(d.retenu_salariale), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
							inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id where d.element_salaire = regles_.id and p.departement = element_ and b.entete = header_;
					end if;
					insert into table_livre_paie values(regles_.id, element_, valeur_);
				end loop;
			end if;
		end loop;
	end if;
	return QUERY select * from table_livre_paie order by _regle, _element;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_livre_paie(bigint, character varying, character varying, character varying)
  OWNER TO postgres;
  

  
  -- Function: grh_presence_durees(bigint, bigint, bigint, date, date)

-- DROP FUNCTION grh_presence_durees(bigint, bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION grh_presence_durees(IN employe_ bigint, IN agence_ bigint, IN societe_ bigint, IN debut_ date, IN fin_ date)
  RETURNS TABLE(agence bigint, employe bigint, valeur double precision, element character varying) AS
$BODY$
DECLARE 
	line_ bigint;
	date_ date;
	jour_ character varying;
	str_repos_ character varying default '';
	repos_ boolean default false;
	jour_repos integer default 0;
	valeur_ double precision default 0;
	type_ character varying default '';

BEGIN 	
	IF(fin_ > current_date)THEN
		fin_ = current_date;
	END IF;
	IF(employe_ IS NOT NULL AND employe_ > 0)THEN
		CREATE TEMP TABLE IF NOT EXISTS  table_presence_durees_by_employe(_agence bigint, _employe bigint, _valeur double precision, _element character varying); 
		DELETE FROM table_presence_durees_by_employe;
		IF(agence_ IS NULL OR agence_ < 1)THEN
			SELECT INTO agence_ e.agence FROM yvs_grh_employes e WHERE e.id = employe_;
		END IF;
		date_ = debut_;
		WHILE(date_ <= fin_) LOOP
			type_ = '';
			line_ = 0;
			valeur_ = 0;
			repos_ = false;	
			
			jour_ = (select convert_integer_to_jourweek(extract(DOW from date_)::integer));	
			-- Vérifie si cette journées est ouvrée
			SELECT INTO line_ jo.id FROM yvs_jours_ouvres jo where jo.calendrier = (select c.calendrier FROM yvs_grh_contrat_emps c where c.employe=employe_ and c.contrat_principal is true limit 1) AND jo.jour=jour_ AND jo.jour_de_repos IS TRUE;
			IF (line_ IS NOT NULL and line_ > 0) THEN
				repos_ = true;
			END IF;	
			-- Vérifie s'il l'employé est présent normal
			SELECT INTO line_ p.id FROM yvs_grh_presence p WHERE p.employe = employe_ AND p.valider IS TRUE AND p.date_debut = date_ AND ((total_heure_sup IS NULL OR total_heure_sup <= 0) AND (total_heure_compensation IS NULL OR total_heure_compensation <= 0));
			IF (line_ IS NOT NULL and line_ > 0) THEN
				IF(repos_ IS FALSE)THEN
					type_ = 'Jn';
				ELSE
					type_ = 'Js';
				END IF;
			ELSE
				-- Vérifie s'il l'employé est présent compensatoire
				SELECT INTO line_ p.id FROM yvs_grh_presence p WHERE p.employe = employe_ AND p.valider IS TRUE AND p.date_debut = date_ AND total_heure_compensation > 0;
				IF (line_ IS NOT NULL and line_ > 0) THEN
					type_ = 'Jc';
				ELSE
					-- Vérifie s'il l'employé est présent supplementaire
					SELECT INTO line_ p.id FROM yvs_grh_presence p WHERE p.employe = employe_ AND p.valider IS TRUE AND p.date_debut = date_ AND total_heure_sup > 0;
					IF (line_ IS NOT NULL and line_ > 0) THEN
						type_ = 'Js';
					ELSE
						-- Vérifie s'il l'employé est en mission
						SELECT INTO line_ m.id FROM yvs_grh_missions m WHERE m.employe=employe_ AND (date_ BETWEEN m.date_debut AND m.date_fin) AND (m.statut_mission IN ('V', 'T', 'C'));
						IF (line_ IS NOT NULL and line_ > 0) THEN
							type_ = 'Mi';
						ELSE
							-- Vérifie s'il l'employé est en formation
							SELECT INTO line_ f.id FROM yvs_grh_formation_emps f INNER JOIN yvs_grh_formation m ON f.formation = m.id WHERE f.employe=employe_ AND (date_ BETWEEN f.date_debut AND f.date_fin) AND (m.statut_formation IN ('V', 'T', 'C'));
							IF (line_ IS NOT NULL and line_ > 0) THEN
								type_ = 'Fo';
							ELSE
								-- Vérifie s'il l'employé est en congé maladie
								SELECT INTO line_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'Maladie';
								IF (line_ IS NOT NULL and line_ > 0) THEN
									type_ = 'Cm';
								ELSE
									-- Vérifie s'il l'employé est en congé annuel
									SELECT INTO line_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'Annuel';
									IF (line_ IS NOT NULL and line_ > 0) THEN
										type_ = 'Ca';
									ELSE
										-- Vérifie s'il l'employé est en congé technique
										SELECT INTO line_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'CT';
										IF (line_ IS NOT NULL and line_ > 0) THEN
											type_ = 'Ct';
										ELSE
											-- Vérifie s'il l'employé est en permission courte durée
											SELECT INTO line_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='P' AND co.duree_permission = 'C';
											IF (line_ IS NOT NULL and line_ > 0) THEN
												type_ = 'Pc';
											ELSE
												-- Vérifie s'il l'employé est en permission longue durée
												SELECT INTO line_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='P' AND co.duree_permission = 'L';
												IF (line_ IS NOT NULL and line_ > 0) THEN
													type_ = 'Pl';
												ELSE
													-- Verifier si le jour n'est pas un jour férié
													SELECT INTO line_ j.* FROM yvs_jours_feries j WHERE (j.jour=date_ AND j.all_year IS FALSE) OR (alter_date(j.jour, 'year', date_)=date_ AND j.all_year IS TRUE);
													IF (line_ IS NOT NULL AND line_ > 0) THEN 
														type_ = 'Fe';
													ELSE
														line_ = 1;
														IF(repos_ IS FALSE)THEN
															type_ = 'Ab';
														ELSE
															type_ = 'Re';
														END IF;
													END IF;
												END IF;
											END IF;
										END IF;
									END IF;
								END IF;
							END IF;
						END IF;
					END IF;
				END IF;
			END IF;
			IF((line_ IS NOT NULL and line_ > 0) and type_ != '')THEN
				SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
				IF(valeur_ IS NOT NULL)THEN
					UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + 1 WHERE _employe = employe_ AND _element = type_;
				ELSE
					INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, 1, type_);
				END IF;
			END IF;
			date_ = date_ + interval '1 day';								
		END LOOP;
		-- Recuperation de la durée de travail requis
		FOR jour_ IN SELECT jo.jour FROM yvs_jours_ouvres jo where jo.calendrier = (select c.calendrier FROM yvs_grh_contrat_emps c where c.employe=employe_ and c.contrat_principal is true limit 1) AND jo.jour_de_repos IS TRUE
		LOOP
			str_repos_= str_repos_ ||','||(select convert_jourweek_to_integer(jour_));
		END LOOP;
		jour_repos = (SELECT COUNT(current_) FROM generate_series(debut_, fin_, '1 day'::interval) current_  WHERE ((SELECT EXTRACT(DOW FROM current_))::character in (select val from regexp_split_to_table(str_repos_,',') val)));
		INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, ((select (fin_ - debut_)) +1 - jour_repos), 'Tr');
		-- Recuperation de la durée de repos requis
		INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, jour_repos, 'Rr');
		-- Recuperation de la durée de travail effectif
		SELECT INTO valeur_ SUM(p._valeur) FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element IN ('Js','Jn','Jc');
		IF(valeur_ IS NOT NULL AND valeur_ > 0)THEN
			INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, 'Je');
		END IF;
				
		RETURN QUERY SELECT * FROM table_presence_durees_by_employe ORDER BY _agence, _employe;
	ELSIF(agence_ IS NOT NULL AND agence_ > 0)THEN
		CREATE TEMP TABLE IF NOT EXISTS  table_presence_durees_by_agence(_agence bigint, _employe bigint, _valeur double precision, _element character varying); 
		DELETE FROM table_presence_durees_by_agence;
		FOR employe_ IN SELECT e.id FROM yvs_grh_employes e WHERE e.agence = agence_
		LOOP
			INSERT INTO table_presence_durees_by_agence SELECT y.agence, y.employe, y.valeur, y.element FROM grh_presence_durees(employe_, agence_, societe_, debut_, fin_) y;
		END LOOP;
		RETURN QUERY SELECT * FROM table_presence_durees_by_agence ORDER BY _agence, _employe;
	ELSIF(societe_ IS NOT NULL AND societe_ > 0)THEN
		CREATE TEMP TABLE IF NOT EXISTS  table_presence_durees_by_societe(_agence bigint, _employe bigint, _valeur double precision, _element character varying); 
		DELETE FROM table_presence_durees_by_societe;
		FOR agence_ IN SELECT a.id FROM yvs_agences a WHERE a.societe = societe_
		LOOP
			INSERT INTO table_presence_durees_by_societe SELECT y.agence, y.employe, y.valeur, y.element FROM grh_presence_durees(employe_, agence_, societe_, debut_, fin_) y;
		END LOOP;
		RETURN QUERY SELECT * FROM table_presence_durees_by_societe ORDER BY _agence, _employe;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_presence_durees(bigint, bigint, bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION grh_presence_durees(bigint, bigint, bigint, date, date) 
IS 'en fontion du type de résultat, retourne le nombre de présence, d''absence, de mission, 
de congé autorisé, de congé technique, de congé non autorisé d''un employé sur une période';

