-- Function: grh_et_recap_dipe_cf(bigint, character varying, character varying)

-- DROP FUNCTION grh_et_recap_dipe_cf(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_recap_dipe_cf(IN societe_ bigint, IN agence_ character varying, IN header_ character varying)
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
	if(header_ is not null and header_ != '')then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
		inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop			
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				else
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
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
ALTER FUNCTION grh_et_recap_dipe_cf(bigint, character varying, character varying)
  OWNER TO postgres;
