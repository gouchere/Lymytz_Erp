-- Function: grh_et_recap_dipe_rav(bigint, bigint, bigint)

DROP FUNCTION grh_et_recap_dipe_rav(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION grh_et_recap_dipe_rav(IN societe_ bigint, IN agence_ character varying, IN header_ bigint)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, entete character varying, valeur double precision, montant double precision) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_RAV';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_rav;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_rav(_employe bigint, _matricule character varying, _nom character varying, _entete character varying, _valeur double precision, _montant double precision);
	delete from table_recap_dipe_rav;
	if(header_ is not null and header_ > 0)then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.element_salaire as id from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				for tranche_ in select e.montant, e.tranche_min, e.tranche_max from yvs_stat_grh_grille_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
							where s.code = titre_ and s.societe = societe_
				loop				
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id 
						where d.element_salaire = element_.id and c.employe = employe_.id and b.entete = header_
						and d.tranche_min <= tranche_.tranche_min and d.tranche_max >= tranche_.tranche_max;

					valeur_ = coalesce(valeur_, 0);
					insert into table_recap_dipe_rav values(employe_.id, employe_.matricule, employe_.nom, 'RAV'||tranche_.montant, tranche_.montant, valeur_);
					somme_ = somme_ + valeur_;
				end loop;
			end loop;
			if(somme_ = 0)then
				delete from table_recap_dipe_rav where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_rav order by _matricule, _valeur;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_rav(bigint, character varying, bigint)
  OWNER TO postgres;

  
  -- Function: grh_et_recap_dipe_cf(bigint, bigint, bigint)

DROP FUNCTION grh_et_recap_dipe_cf(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION grh_et_recap_dipe_cf(IN societe_ bigint, IN agence_ character varying, IN header_ bigint)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, entete character varying, valeur double precision, montant double precision, is_taux boolean) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_CF';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
	taux_ double precision default 0;
	ordre_ integer default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_cf;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_cf(_employe bigint, _matricule character varying, _nom character varying, _entete character varying, _valeur double precision, _montant double precision, _is_taux boolean);
	delete from table_recap_dipe_cf;
	if(header_ is not null and header_ > 0)then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop			
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id and b.entete = header_;
				else
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id and b.entete = header_;
				end if;
				valeur_ = coalesce(valeur_, 0);
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, element_.libelle, ordre_, valeur_, false);
				somme_ = somme_ + valeur_;
			end loop;
			
			ordre_ = ordre_ + 1;
			select into valeur_ coalesce(y._montant, 0) from table_recap_dipe_cf y where y._employe = employe_.id and y._valeur = 1;
			select into taux_ coalesce(y.taux, 0) from yvs_stat_grh_taux_contribution y inner join yvs_stat_grh_etat s on s.id = y.etat where s.code = titre_ and s.societe = societe_ and y.type_taux = 'P' limit 1;
			if(taux_ > 0)then
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'Base CFPat', ordre_, valeur_, false);
				ordre_ = ordre_ + 1;
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'Tx Pat', ordre_+10, taux_, true);
				taux_ = valeur_ * taux_ / 100;
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'CF Pat', ordre_, taux_, false);
			end if;
			
			ordre_ = ordre_ + 1;
			select into taux_ coalesce(y.taux, 0) from yvs_stat_grh_taux_contribution y inner join yvs_stat_grh_etat s on s.id = y.etat where s.code = titre_ and s.societe = societe_ and y.type_taux = 'S' limit 1;
			if(taux_ > 0)then
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'Base CFSal', ordre_, valeur_, false);
				ordre_ = ordre_ + 1;
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'Tx Sal', ordre_+10, taux_, true);
				taux_ = valeur_ * taux_ / 100;
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'CF Sal', ordre_, taux_, false);
			end if;
			
			ordre_ = ordre_ + 1;
			select into valeur_ sum(coalesce(y._montant, 0)) from table_recap_dipe_cf y where y._employe = employe_.id and y._valeur in (3,5);
			insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'Total CF', ordre_, valeur_, false);
			
			if(somme_ = 0)then
				delete from table_recap_dipe_cf where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_cf order by _matricule, _valeur;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_cf(bigint, character varying, bigint)
  OWNER TO postgres;
  
  
  -- Function: grh_et_recap_dipe_cnps(bigint, character varying, bigint, character varying)

DROP FUNCTION grh_et_recap_dipe_cnps(bigint, bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION grh_et_recap_dipe_cnps(IN societe_ bigint, IN agence_ character varying, IN header_ bigint, IN sommable_ character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, numero character varying, entete character varying, rang integer, montant double precision) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_CNPS2';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	ordre_ integer default 0; 
	valeur_ double precision default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_cnps;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_cnps(_employe bigint, _matricule character varying, _nom character varying, _numero character varying, _entete character varying, _rang integer, _montant double precision);
	delete from table_recap_dipe_cnps;
	if(header_ is not null and header_ > 0)then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom, e.matricule_cnps as numero from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.id as ids, e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				valeur_ = (select grh_get_valeur_element_dipe(element_.ids, header_, employe_.id));
				
				insert into table_recap_dipe_cnps values(employe_.id, employe_.matricule, employe_.nom, employe_.numero, element_.libelle, ordre_, valeur_);
				somme_ = somme_ + valeur_;
			end loop;		
			if(sommable_ is not null and sommable_ != '')then	
				select into valeur_ coalesce(sum(_montant), 0) from table_recap_dipe_cnps where _employe = employe_.id and _rang::character varying in (select colonne from regexp_split_to_table(sommable_,',') colonne);
				select into ordre_ max(colonne::integer) from regexp_split_to_table(sommable_,',') colonne;
				insert into table_recap_dipe_cnps values(employe_.id, employe_.matricule, employe_.nom, employe_.numero, 'Total PV', ordre_+1, valeur_);
			end if;
			if(somme_ = 0)then
				delete from table_recap_dipe_cnps where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_cnps order by _matricule, _rang;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_cnps(bigint, character varying, bigint, character varying)
  OWNER TO postgres;

  
  -- Function: grh_et_recap_dipe_cstc(bigint, character varying, bigint)

DROP FUNCTION grh_et_recap_dipe_cstc(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION grh_et_recap_dipe_cstc(IN societe_ bigint, IN agence_ character varying, IN header_ bigint)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, numero character varying, entete character varying, rang integer, montant double precision) AS
$BODY$
declare 
	titre_ character varying default 'CSTC';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	ordre_ integer default 0; 
	valeur_ record;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_cstc;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_cstc(_employe bigint, _matricule character varying, _nom character varying, _numero character varying, _entete character varying, _rang integer, _montant double precision);
	delete from table_recap_dipe_cstc;
	if(header_ is not null and header_ > 0)then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom, e.matricule_cnps as numero from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) as montant, coalesce(d.taux_patronal, 0) as taux from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id and b.entete = header_;
				else
					select into valeur_ coalesce(d.retenu_salariale, 0) as montant, coalesce(d.taux_salarial, 0) as taux from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id and b.entete = header_;
				end if;
				
				insert into table_recap_dipe_cstc values(employe_.id, employe_.matricule, employe_.nom, employe_.numero, 'Montant', 2, coalesce(valeur_.montant,0));
				insert into table_recap_dipe_cstc values(employe_.id, employe_.matricule, employe_.nom, employe_.numero, 'Taux', 1, coalesce(valeur_.taux,0));
				somme_ = somme_ + coalesce(valeur_.montant,0);
			end loop;	
			if(somme_ = 0)then
				delete from table_recap_dipe_cstc where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_cstc order by _matricule, _rang;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_cstc(bigint, character varying, bigint)
  OWNER TO postgres;

  
  -- Function: grh_et_recap_dipe_fne(bigint, character varying, bigint)

DROP FUNCTION grh_et_recap_dipe_fne(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION grh_et_recap_dipe_fne(IN societe_ bigint, IN agence_ character varying, IN header_ bigint)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, entete character varying, valeur double precision, montant double precision, is_taux boolean) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_FNE';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
	taux_ double precision default 0;
	ordre_ integer default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_fne;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_fne(_employe bigint, _matricule character varying, _nom character varying, _entete character varying, _valeur double precision, _montant double precision, _is_taux boolean);
	delete from table_recap_dipe_fne;
	if(header_ is not null and header_ > 0)then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop			
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id and b.entete = header_;
				else
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id and b.entete = header_;
				end if;
				valeur_ = coalesce(valeur_, 0);
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, element_.libelle, ordre_, valeur_, false);
				somme_ = somme_ + valeur_;
			end loop;
			
			ordre_ = ordre_ + 1;
			select into valeur_ coalesce(y._montant, 0) from table_recap_dipe_fne y where y._employe = employe_.id and y._valeur = 1;
			select into taux_ coalesce(y.taux, 0) from yvs_stat_grh_taux_contribution y inner join yvs_stat_grh_etat s on s.id = y.etat where s.code = titre_ and s.societe = societe_ and y.type_taux = 'P' limit 1;
			if(taux_ > 0)then
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'Base CFPat', ordre_, valeur_, false);
				ordre_ = ordre_ + 1;
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'Tx Pat', ordre_+10, taux_, true);
				taux_ = valeur_ * taux_ / 100;
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'CF Pat', ordre_, taux_, false);
			end if;
			
			ordre_ = ordre_ + 1;
			select into taux_ coalesce(y.taux, 0) from yvs_stat_grh_taux_contribution y inner join yvs_stat_grh_etat s on s.id = y.etat where s.code = titre_ and s.societe = societe_ and y.type_taux = 'S' limit 1;
			if(taux_ > 0)then
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'Base CFSal', ordre_, valeur_, false);
				ordre_ = ordre_ + 1;
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'Tx Sal', ordre_+10, taux_, true);
				taux_ = valeur_ * taux_ / 100;
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'CF Sal', ordre_, taux_, false);
			end if;
			
			ordre_ = ordre_ + 1;
			select into valeur_ sum(coalesce(y._montant, 0)) from table_recap_dipe_fne y where y._employe = employe_.id and y._valeur in (3);
			insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'Total CF', ordre_, valeur_, false);
			
			if(somme_ = 0)then
				delete from table_recap_dipe_fne where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_fne order by _matricule, _valeur;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_fne(bigint, character varying, bigint)
  OWNER TO postgres;

  
  -- Function: grh_et_recap_dipe_irpp2(bigint, character varying, bigint, character varying)

DROP FUNCTION grh_et_recap_dipe_irpp2(bigint, bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION grh_et_recap_dipe_irpp2(IN societe_ bigint, IN agence_ character varying, IN header_ bigint, IN sommable character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, entete character varying, rang integer, montant double precision) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_IRPP_2';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	ordre_ integer default 0; 
	valeur_ double precision default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_irpp;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_irpp(_employe bigint, _matricule character varying, _nom character varying, _entete character varying, _rang integer, _montant double precision);
	delete from table_recap_dipe_irpp;
	if(header_ is not null and header_ > 0)then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id and b.entete = header_;
				else
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id and b.entete = header_;
				end if;

				valeur_ = coalesce(valeur_, 0);
				insert into table_recap_dipe_irpp values(employe_.id, employe_.matricule, employe_.nom, element_.libelle, ordre_, valeur_);
				somme_ = somme_ + valeur_;
			end loop;	
			if(sommable is not null and sommable != '')then	
				select into valeur_ coalesce(sum(_montant), 0) from table_recap_dipe_irpp where _employe = employe_.id and _rang::character varying in (select colonne from regexp_split_to_table(sommable,',') colonne);
				insert into table_recap_dipe_irpp values(employe_.id, employe_.matricule, employe_.nom, 'Total imp', ordre_+1, valeur_);
			end if;
			if(somme_ = 0)then
				delete from table_recap_dipe_irpp where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_irpp order by _matricule, _rang;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_irpp2(bigint, character varying, bigint, character varying)
  OWNER TO postgres;


  
  -- Function: grh_et_recap_dipe_taxe_com(bigint, character varying, bigint)

DROP FUNCTION grh_et_recap_dipe_taxe_com(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION grh_et_recap_dipe_taxe_com(IN societe_ bigint, IN agence_ character varying, IN header_ bigint)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, entete character varying, valeur double precision, montant double precision) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_TC';
	
	employe_ record;
	element_ record;
	tranche_ record;
	search_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_taxe_com;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_taxe_com(_employe bigint, _matricule character varying, _nom character varying, _entete character varying, _valeur double precision, _montant double precision);
	delete from table_recap_dipe_taxe_com;
	if(header_ is not null and header_ > 0)then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.element_salaire as id from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				for tranche_ in select e.montant, e.tranche_min, e.tranche_max from yvs_stat_grh_grille_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
							where s.code = titre_ and s.societe = societe_
				loop
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id 
						where d.element_salaire = element_.id and c.employe = employe_.id and b.entete = header_
						and d.tranche_min <= tranche_.tranche_min and d.tranche_max >= tranche_.tranche_max;

					valeur_ = coalesce(valeur_, 0);
					insert into table_recap_dipe_taxe_com values(employe_.id, employe_.matricule, employe_.nom, 'TC'||tranche_.montant, tranche_.montant, valeur_);
					somme_ = somme_ + valeur_;
				end loop;
			end loop;
			if(somme_ = 0)then
				delete from table_recap_dipe_taxe_com where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_taxe_com order by _matricule, _valeur;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_taxe_com(bigint, character varying, bigint)
  OWNER TO postgres;
  
  
  -- Function: grh_et_livre_paie(bigint, bigint, character varying, character varying, character varying)

DROP FUNCTION grh_et_livre_paie(bigint, bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_livre_paie(IN societe_ bigint, IN header_ bigint, IN agence_ character varying, IN code_ character varying, IN type_ character varying)
  RETURNS TABLE(regle bigint, numero integer, libelle character varying, groupe bigint, element bigint, montant double precision, rang integer, is_group boolean, is_total boolean) AS
$BODY$
declare 
	titre_ character varying default 'LIVRE DE PAIE';
	
	element_ bigint;
	regles_ record;
	second_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
	is_total_ boolean default false;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_livre_paie;
	CREATE TEMP TABLE IF NOT EXISTS table_livre_paie(_regle bigint, _numero integer, _libelle character varying, _groupe bigint, _element bigint, _montant double precision, _rang integer, _is_group boolean, _is_total boolean);
	delete from table_livre_paie;
	if((header_ is not null and header_ > 0) and (type_ is not null))then
		for regles_ in select e.element_salaire as id, e.libelle, coalesce(y.num_sequence, 0) as numero, coalesce(e.ordre, 0) as ordre, y.retenue, e.groupe_element as groupe from yvs_stat_grh_element_dipe e 
		inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.libelle = titre_ and s.societe = societe_ order by e.ordre
		loop
			somme_ = 0;
			if(regles_.numero = 0)then
				is_total_ = true;
			else
				is_total_ = false;
			end if;
			if(type_ = 'E')then
				for element_ in select distinct(e.id) from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
				where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
				loop
					if(regles_.retenue is false)then
						select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regles_.id and c.employe = element_ and b.entete = header_;
					else
						select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regles_.id and c.employe = element_ and b.entete = header_;
					end if;
					insert into table_livre_paie values(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_, valeur_, regles_.ordre, false, is_total_);
					somme_ = somme_ + valeur_;
				end loop;
			elsif(type_ = 'S')then
				for element_ in select distinct(d.id) from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id
				inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id inner join yvs_grh_departement d on p.departement = d.id where d.visible_on_livre_paie is true 
				and e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
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
					insert into table_livre_paie values(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_, valeur_, regles_.ordre, false, is_total_);
					somme_ = somme_ + valeur_;
				end loop;
			end if;
			if(somme_ = 0)then
				delete from table_livre_paie where _regle = regles_.id;
			end if;
		end loop;
		
		-- Récuperation les totaux par groupe
		for regles_ in select distinct _groupe as groupe, g.libelle from table_livre_paie y left join yvs_stat_grh_groupe_element g on y._groupe = g.id where y._groupe > 0
		loop
			for second_ in select _element, sum(_montant) as valeur_, count(_rang) ordre from table_livre_paie y where y._groupe = regles_.groupe group by _element
			loop
				insert into table_livre_paie values(0, 0, regles_.libelle, regles_.groupe, second_._element, second_.valeur_, (second_.ordre + 1), true, false);
			end loop;
		end loop;
	end if;
	return QUERY select * from table_livre_paie order by _is_total, _groupe, _rang, _element;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_livre_paie(bigint, bigint, character varying, character varying, character varying)
  OWNER TO postgres;


  
  -- Function: grh_et_journal_paye_gain(bigint, bigint, bigint)

DROP FUNCTION grh_et_journal_paye_gain(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION grh_et_journal_paye_gain(IN societe_ bigint, IN agence_ character varying, IN header_ bigint)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, element character varying, valeur character varying, rang integer, is_double boolean) AS
$BODY$
declare 
	titre_ character varying default 'GEN_JS';
	
	employe_ record;
	element_ record;	
	valeur_ double precision default 0; 
	somme_ double precision default 0; 
	ordre_ integer default 0; 
   
begin 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	CREATE TEMP TABLE IF NOT EXISTS table_journal_paye(_employe bigint, _matricule character varying, _nom character varying, _element character varying, _valeur character varying, _rang integer, _is_double boolean);
	delete from table_journal_paye;
	
	if((header_ is not null and header_ > 0))then	
		for employe_ in select distinct(e.id) as id, e.matricule, concat(e.nom, ' ', e.prenom) as nom, ca.categorie, ec.echelon from yvs_grh_bulletins b 
			inner join yvs_grh_contrat_emps c on b.contrat = c.id 
			inner join yvs_grh_employes e on c.employe = e.id 
			inner join yvs_grh_convention_employe ce on ce.employe = e.id 
			inner join yvs_grh_convention_collective cc on cc.id = ce.convention
			inner join yvs_grh_echelons ec on cc.echellon = ec.id
			inner join yvs_grh_categorie_professionelle ca on cc.categorie = ca.id
			where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) and ce.actif is true
		loop
			insert into table_journal_paye values(employe_.id, employe_.matricule, employe_.nom, 'CAT  EC', concat(employe_.categorie,'  ',employe_.echelon), 0, false);
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e 
			inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id 
			where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				RAISE NOTICE 'element %',element_.libelle;
				ordre_ = element_.ordre;
				if(element_.retenue is true)then
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
					inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id
					where b.entete = header_ and e.id = element_.id and c.employe = employe_.id;
				elsif(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
					inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id
					where b.entete = header_ and e.id = element_.id and c.employe = employe_.id;
				else
					valeur_ = 0;
				end if;
				somme_ = somme_ + coalesce(valeur_, 0);
				insert into table_journal_paye values(employe_.id, employe_.matricule, employe_.nom, element_.libelle, valeur_::character varying, ordre_, true);
			end loop;
			if(somme_ = 0)then
				delete from table_journal_paye where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_journal_paye order by _nom, _matricule, _rang;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_journal_paye_gain(bigint, character varying, bigint)
  OWNER TO postgres;

  
  -- Function: grh_et_recapitulatif_paye(bigint, bigint, character varying)

DROP FUNCTION grh_et_recapitulatif_paye(bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION grh_et_recapitulatif_paye(IN societe_ bigint, IN header_ bigint, IN agence_ character varying, IN sommable character varying)
  RETURNS TABLE(employe bigint, numero character varying, matricule character varying, nom character varying, element character varying, valeur double precision, rang integer, is_day boolean) AS
$BODY$
declare 
	titre_ character varying default 'RECAPITULATIF PAYE';
	
	employe_ record;
	element_ record;	
	valeur_ double precision default 0; 
	somme_ double precision default 0; 
	ordre_ integer default 0; 
	_is_day_ boolean default true;
   
begin 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	CREATE TEMP TABLE IF NOT EXISTS table_recapitulatif_paye(_employe bigint, _numero character varying, _matricule character varying, _nom character varying, _element character varying, _valeur double precision, _rang integer, _is_day boolean);
	delete from table_recapitulatif_paye;
	
	if((header_ is not null and header_ > 0))then	
		for employe_ in select distinct(e.id) as id, e.matricule_cnps as numero, e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b 
		inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e 
			inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.libelle = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is true)then
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
					inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id
					where b.entete = header_ and e.id = element_.id and c.employe = employe_.id;
				elsif(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
					inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id
					where b.entete = header_ and e.id = element_.id and c.employe = employe_.id;
				else
					valeur_ = 0;
				end if;
				somme_ = somme_ + coalesce(valeur_, 0);
				insert into table_recapitulatif_paye values(employe_.id, employe_.numero, employe_.matricule, employe_.nom, 
										element_.libelle, valeur_, ordre_, _is_day_);
				_is_day_ = false;
			end loop;	
			if(sommable is not null and sommable != '')then	
				select into valeur_ coalesce(sum(_valeur), 0) from table_recapitulatif_paye where _employe = employe_.id and _rang::character varying in (select colonne from regexp_split_to_table(sommable,',') colonne);
				insert into table_recapitulatif_paye values(employe_.id, employe_.numero, employe_.matricule, employe_.nom, 
										'IRPP+CAC', valeur_, ordre_+1);
			end if;
			if(somme_ = 0)then
				delete from table_recapitulatif_paye where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recapitulatif_paye order by _nom, _matricule, _rang;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recapitulatif_paye(bigint, bigint, character varying, character varying)
  OWNER TO postgres;
