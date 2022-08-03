-- Function: compta_et_dashboard(bigint, bigint, character varying, character varying, date, character varying, character varying)

-- DROP FUNCTION compta_et_dashboard(bigint, bigint, character varying, character varying, date, character varying, character varying);

CREATE OR REPLACE FUNCTION compta_et_dashboard(IN agence_ bigint, IN societe_ bigint, IN comptedebut_ character varying, IN comptefin_ character varying, IN current_ date, IN type_ character varying, IN periode_ character varying)
  RETURNS TABLE(numero character varying, entete character varying, valeur character varying, montant double precision, num_classe character varying, is_head boolean, is_produit boolean, is_charge boolean, is_total boolean, rang integer) AS
$BODY$
declare 
	query_ character varying default '';
	
	comptes_ record;
	exo_ record;
	dates_ record;
	
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
-- 	DROP TABLE table_dashboard;
	CREATE TEMP TABLE IF NOT EXISTS table_dashboard(num character varying, titre character varying, element character varying, mtant double precision, classe character varying, head boolean, produit boolean, charge boolean, total boolean, _rang integer);
	DELETE FROM table_dashboard;

	-- Recherche de l'exercice
	select into exo_ id, date_debut, date_fin from yvs_base_exercice where current_ between date_debut and date_fin limit 1;
	if(exo_ is not null and exo_.id is not null)then
		date_save_ = exo_.date_debut;
		date_fin_ = exo_.date_fin;

		query_ = 'SELECT c.id, c.num_compte as code, c.intitule, substring(c.num_compte, 0,2) as classe FROM yvs_base_plan_comptable c 
																									INNER JOIN yvs_base_nature_compte n on n.id = c.nature_compte 
																									WHERE c.id in 
																			(SELECT y.compte_general FROM yvs_base_plan_comptable y WHERE y.compte_general is not null)';
		if(agence_ is not null and agence_ > 0)then
			query_ = query_ || ' and n.societe = (select a.societe from yvs_agences a where a.id = '||agence_||')';
		elsif(societe_ is not null and societe_ > 0)then
			query_ = query_ || ' and n.societe = '||societe_||'';
		end if;
		if(type_ = 'R')then -- Operation sur le 'Resultat'
			query_ = query_ || ' and (c.num_compte like ''6%'' or c.num_compte like ''7%'') and (c.num_compte between '''||comptedebut_||''' and '''||comptefin_||''') order by c.num_compte';
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
					debit_ = (select compta_et_debit(agence_, societe_, comptes_.id, date_debut_, date_fin_, 'C', true));
					insert into table_dashboard values (comptes_.code, 'DEBIT', comptes_.intitule, debit_, comptes_.classe, false, pdt_, chg_, false); --/

					-- Récuperation du total débit de la classe
					select into valeur_ num from table_dashboard where classe = comptes_.classe and titre = 'DEBIT' and head = true and num = 'TOTAL';
					if(valeur_ is null)then
						insert into table_dashboard values ('TOTAL', 'DEBIT', 'CLASSE '||comptes_.classe, debit_, comptes_.classe, true, pdt_, chg_, false); --/
					else
						update table_dashboard set mtant = mtant + debit_ where classe = comptes_.classe and titre = 'DEBIT' and head = true and num = 'TOTAL';
					end if;
						
					-- Récuperation du crédit du compte
					credit_ = (select compta_et_credit(agence_, societe_, comptes_.id, date_debut_, date_fin_, 'C', true));				
					insert into table_dashboard values (comptes_.code, 'CREDIT', comptes_.intitule, -(credit_), comptes_.classe, false, pdt_, chg_, false); --/

					-- Récuperation du total crédit de la classe
					select into valeur_ num from table_dashboard where classe = comptes_.classe and titre = 'CREDIT' and head = true and num = 'TOTAL';
					if(valeur_ is null)then
						insert into table_dashboard values ('TOTAL', 'CREDIT', 'CLASSE '||comptes_.classe, -(credit_), comptes_.classe, true, pdt_, chg_, false); --/
					else
						update table_dashboard set mtant = mtant - credit_ where classe = comptes_.classe and titre = 'CREDIT' and head = true and num = 'TOTAL';
					end if;

				else -- Operation autre que sur l'année	
					compteur_ = 0; -- Reinitialisation du compteur;
					for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_)
					loop
						jour_ = dates_.intitule;
						compteur_ = compteur_ +1 ;
						IF(COALESCE(agence_,0)>0) THEN
							SELECT INTO solde_ SUM(COALESCE(c.debit,0)-COALESCE(c.credit,0)) FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece INNER JOIN yvs_compta_journaux j ON j.id=p.journal INNER JOIN yvs_agences a ON a.id=j.agence
																WHERE (c.compte_general=comptes_.id OR c.compte_general IN (SELECT cg.id FROM yvs_base_plan_comptable cg WHERE cg.compte_general=comptes_.id))
																AND a.id=agence_
																AND p.date_piece BETWEEN dates_.date_debut AND dates_.date_fin;
						ELSE
							SELECT INTO solde_ SUM(COALESCE(c.debit,0)-COALESCE(c.credit,0)) FROM yvs_compta_content_journal c INNER JOIN yvs_compta_pieces_comptable p ON p.id=c.piece 
																WHERE (c.compte_general=comptes_.id OR c.compte_general IN (SELECT cg.id FROM yvs_base_plan_comptable cg WHERE cg.compte_general=comptes_.id))
																AND p.date_piece BETWEEN dates_.date_debut AND dates_.date_fin;
						END IF;
						insert into table_dashboard values (comptes_.code, jour_, comptes_.intitule, solde_, comptes_.classe, false, pdt_, chg_, false, compteur_); --/

						-- Récuperation du total 'jour_' de la classe
						select into valeur_ num from table_dashboard where classe = comptes_.classe and titre = jour_ and head = true and num = 'TOTAL';
						if(valeur_ is null)then
							insert into table_dashboard values ('TOTAL', jour_, 'CLASSE '||comptes_.classe, solde_, comptes_.classe, true, pdt_, chg_, false, compteur_); --/
						else
							update table_dashboard set mtant = mtant + solde_ where classe = comptes_.classe and titre = jour_ and head = true and num = 'TOTAL';
						end if;
					end loop;
				end if;
				select into solde_ SUM(mtant) from table_dashboard where num = comptes_.code;
				IF(COALESCE(solde_, 0) = 0)THEN
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
			date_debut_ = date_save_; -- Reinitialisation de la date début;
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
	end if;
	return QUERY select * from table_dashboard order by classe, num, _rang;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_dashboard(bigint, bigint, character varying, character varying, date, character varying, character varying)
  OWNER TO postgres;
