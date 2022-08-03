-- Function: grh_et_journal_salaire(bigint, character varying, character varying, character varying)
-- DROP FUNCTION grh_et_journal_salaire(bigint, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION grh_et_journal_salaire(IN societe_ bigint, IN agence_ character varying, IN header_ character varying, IN titre_ character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, element character varying, valeur character varying, rang integer, is_double boolean) AS
$BODY$
declare 	
	employe_ record;
	element_ record;	
	valeur_ double precision default 0; 
	somme_ double precision default 0; 
	ordre_ integer default 0; 
   
begin 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	CREATE TEMP TABLE IF NOT EXISTS table_journal_paye(_employe bigint, _matricule character varying, _nom character varying, _element character varying, _valeur character varying, _rang integer, _is_double boolean);
	delete from table_journal_paye;
	
	if((header_ is not null and header_ != ''))then	
		for employe_ in select distinct(e.id) as id, e.matricule, concat(e.nom, ' ', e.prenom) as nom, ca.categorie, ec.echelon from yvs_grh_bulletins b 
			INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
			inner join yvs_grh_contrat_emps c on b.contrat = c.id 
			inner join yvs_grh_employes e on c.employe = e.id 
			inner join yvs_grh_convention_employe ce on ce.employe = e.id 
			inner join yvs_grh_convention_collective cc on cc.id = ce.convention
			inner join yvs_grh_echelons ec on cc.echellon = ec.id
			inner join yvs_grh_categorie_professionelle ca on cc.categorie = ca.id
			where h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) and ce.actif is true
		loop
			insert into table_journal_paye values(employe_.id, employe_.matricule, employe_.nom, 'CAT  EC', concat(employe_.categorie,'  ',employe_.echelon), -1, false);
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e 
			inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id 
			where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is true)then
					select into valeur_ SUM(coalesce(d.retenu_salariale, 0)) from yvs_grh_detail_bulletin d 
					inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
					inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id
					where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id and c.employe = employe_.id and (b.statut IN ('V', 'P'));
				elsif(element_.retenue is false)then
					select into valeur_ SUM(coalesce(d.montant_payer, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
					inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id
					where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id and c.employe = employe_.id and (b.statut IN ('V', 'P'));
				else
					valeur_ = 0;
				end if;
				somme_ = somme_ + coalesce(valeur_, 0);
				if(valeur_ > 0)then
					insert into table_journal_paye values(employe_.id, employe_.matricule, employe_.nom, element_.libelle, valeur_::character varying, ordre_, true);
				end if;
			end loop;
			if(somme_ = 0)then
				delete from table_journal_paye where _employe = employe_.id;
			else
				insert into table_journal_paye values(employe_.id, employe_.matricule, employe_.nom, 'Salaire Brut', somme_, 1000, true);
			end if;
		end loop;
		for employe_ in select distinct _element, _rang, sum(_valeur::double precision) as _somme from table_journal_paye where _is_double group by _element, _rang
		loop
			insert into table_journal_paye values(0, 'ZZZ.', 'TOTAL', employe_._element, employe_._somme, employe_._rang, true);
		end loop;
	end if;
	return QUERY select * from table_journal_paye order by _nom, _matricule, _rang;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_journal_salaire(bigint, character varying, character varying, character varying)
  OWNER TO postgres;
