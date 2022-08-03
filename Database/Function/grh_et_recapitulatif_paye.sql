-- Function: grh_et_recapitulatif_paye(bigint, bigint, character varying, character varying)
-- DROP FUNCTION grh_et_recapitulatif_paye(bigint, bigint, character varying, character varying);
CREATE OR REPLACE FUNCTION grh_et_recapitulatif_paye(IN societe_ bigint, IN header_ bigint, IN agence_ character varying, IN sommable character varying)
  RETURNS TABLE(employe bigint, numero character varying, matricule character varying, nom character varying, element character varying, valeur double precision, rang integer, is_day boolean) AS
$BODY$
declare 
	titre_ character varying default 'RECAP_PAYE';
	
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
		INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) and b.entete = header_
		loop
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e 
			inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is true)then
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
					inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id
					where b.entete = header_ and e.id = element_.id and c.employe = employe_.id and (b.statut IN ('V', 'P'));
				elsif(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
					inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id
					where b.entete = header_ and e.id = element_.id and c.employe = employe_.id and (b.statut IN ('V', 'P'));
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
