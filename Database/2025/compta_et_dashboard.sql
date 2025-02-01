-- DROP FUNCTION public.compta_et_dashboard(int8, int8, varchar, varchar, date, date, varchar, varchar);

CREATE OR REPLACE FUNCTION public.compta_et_dashboard(agence_ bigint, societe_ bigint, comptedebut_ character varying, comptefin_ character varying, date_debut_ date, date_fin_ date, type_ character varying, periode_ character varying)
 RETURNS TABLE(numero character varying, entete character varying, valeur character varying, montant double precision, num_classe character varying, is_head boolean, is_produit boolean, is_charge boolean, is_total boolean, rang integer)
 LANGUAGE plpgsql
AS $function$
declare 
	query_ character varying default '';
	
	comptes_ record;
	exo_ record;
	dates_ record;
	soldes_ record;
	
	jour_ character varying;
	
	valeur_ character varying;
	solde_ double precision default 0;
	debit_ double precision default 0;
	credit_ double precision default 0;
	
	date_ date;
   
	hide_compte_ boolean default true;
	pdt_ boolean;
	chg_ boolean;
	compteur_ integer default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_dashboard;
	CREATE TEMP TABLE IF NOT EXISTS table_dashboard(num character varying, titre character varying, element character varying, mtant double precision, classe character varying, head boolean, produit boolean, charge boolean, total boolean, _rang integer);
	DELETE FROM table_dashboard;	

	query_ = 'select distinct c.id, c.num_compte as code, c.intitule, substring(c.num_compte, 0,2) as classe from yvs_base_plan_comptable c inner join yvs_base_nature_compte n on n.id = c.nature_compte inner join yvs_base_plan_comptable y on y.compte_general = c.id where c.id is not null';
	if(societe_ is not null and societe_ > 0)then
		query_ = query_ || ' and n.societe = '||societe_||'';
	elsif(agence_ is not null and agence_ > 0)then
		query_ = query_ || ' and n.societe = (select a.societe from yvs_agences a where a.id = '||agence_||')';
	end if;
	if(type_ = 'R')then -- Operation sur le 'Resultat'
		query_ = query_ || ' and (c.num_compte like ''6%'' or c.num_compte like ''7%'' or c.num_compte like ''8%'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
	elsif(type_ = 'P')then -- Operation sur le 'Produit'
		query_ = query_ || ' and (c.num_compte like ''7%'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
	elsif(type_ = 'C')then -- Operation sur le 'Charge'
		query_ = query_ || ' and (c.num_compte like ''6%'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
	elsif(type_ = 'B')then -- Operation sur le 'Bilan'
		query_ = query_ || ' and (substring(c.num_compte, 0,2) < ''6'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
	else -- Operation sur la 'Balance'
		query_ = query_ || ' and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
	end if;

	RAISE NOTICE 'query_ %',query_;
	if(query_ is not null and query_ != '')then
		FOR comptes_  IN EXECUTE query_
		loop
			hide_compte_ = true;
			-- Définition si c'est un compte produit ou charge
			if(comptes_.classe = '6')then
				pdt_ = false;
				chg_ = true;
			elsif(comptes_.classe = '7')then
				pdt_ = true;
				chg_ = false;
			elsif(comptes_.classe = '8')then
				pdt_ = true;
				chg_ = true;
			else
				pdt_ = false;
				chg_ = false;
			end if;
			
			if(periode_ = 'A')then -- Operation sur l'année	
				-- Récuperation du débit et credit du compte
				SELECT INTO soldes_ debit, credit FROM compta_et_debit_credit(agence_, societe_, comptes_.id, date_debut_, date_fin_, 0, 'C', true, '', true);
				-- Récuperation du débit du compte
				debit_ = COALESCE(soldes_.debit, 0);
				insert into table_dashboard values (comptes_.code, 'DEBIT', comptes_.intitule, debit_, comptes_.classe, false, pdt_, chg_, false); --/

				-- Récuperation du total débit de la classe
				select into valeur_ num from table_dashboard where classe = comptes_.classe and titre = 'DEBIT' and head = true and num = 'TOTAL';
				if(valeur_ is null)then
					insert into table_dashboard values ('TOTAL', 'DEBIT', 'CLASSE '||comptes_.classe, debit_, comptes_.classe, true, pdt_, chg_, false); --/
				else
					update table_dashboard set mtant = mtant + debit_ where classe = comptes_.classe and titre = 'DEBIT' and head = true and num = 'TOTAL';
				end if;
					
				-- Récuperation du crédit du compte
				credit_ = COALESCE(soldes_.credit, 0);			
				insert into table_dashboard values (comptes_.code, 'CREDIT', comptes_.intitule, -(credit_), comptes_.classe, false, pdt_, chg_, false); --/

				-- Récuperation du total crédit de la classe
				select into valeur_ num from table_dashboard where classe = comptes_.classe and titre = 'CREDIT' and head = true and num = 'TOTAL';
				if(valeur_ is null)then
					insert into table_dashboard values ('TOTAL', 'CREDIT', 'CLASSE '||comptes_.classe, -(credit_), comptes_.classe, true, pdt_, chg_, false); --/
				else
					update table_dashboard set mtant = mtant - credit_ where classe = comptes_.classe and titre = 'CREDIT' and head = true and num = 'TOTAL';
				end if;
				if(debit_ != 0 or credit_ != 0)then
					hide_compte_ = false;
				end if;
			else -- Operation autre que sur l'année	
				compteur_ = 0; -- Reinitialisation du compteur;
				for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_)
				loop
-- 						RAISE NOTICE '	DATES : % - %',dates_.date_debut, dates_.date_fin;
					jour_ = dates_.intitule;
					compteur_ = compteur_ +1 ;

					-- Récuperation du débit et credit du compte
					solde_ = (SELECT compta_et_solde(agence_, societe_, comptes_.id, dates_.date_debut, dates_.date_fin, 0, 'C', true, ''));
					
					solde_ = COALESCE(solde_, 0);	
					insert into table_dashboard values (comptes_.code, jour_, comptes_.intitule, solde_, comptes_.classe, false, pdt_, chg_, false, compteur_); --/

					-- Récuperation du total 'jour_' de la classe
					select into valeur_ num from table_dashboard where classe = comptes_.classe and titre = jour_ and head = true and num = 'TOTAL';
					if(valeur_ is null)then
						insert into table_dashboard values ('TOTAL', jour_, 'CLASSE '||comptes_.classe, solde_, comptes_.classe, true, pdt_, chg_, false, compteur_); --/
					else
						update table_dashboard set mtant = mtant + solde_ where classe = comptes_.classe and titre = jour_ and head = true and num = 'TOTAL';
					end if;
					if(solde_ != 0)then
						hide_compte_ = false;
					end if;
				end loop;
			end if;
			IF(hide_compte_)THEN
				DELETE FROM table_dashboard WHERE num = comptes_.code;
			END IF;
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
		compteur_ = 0; -- Reinitialisation du compteur des trimestres;
		for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_)
		loop
			jour_ = dates_.intitule;
			compteur_ = compteur_ +1 ;
				
			-- Récuperation du total global de crédit
			select into solde_ sum(mtant) from table_dashboard where titre = jour_ and head = false;
			insert into table_dashboard values ('', jour_, '', solde_, 'Z', false, false, false, true, compteur_); --/
		end loop;
	end if;
	FOR comptes_ IN SELECT classe, SUM(mtant) FROM table_dashboard GROUP BY classe HAVING SUM(mtant) = 0
	LOOP
		DELETE FROM table_dashboard WHERE classe = comptes_.classe;
	END LOOP;
	return QUERY select * from table_dashboard order by classe, num, _rang;
end;
$function$
;
