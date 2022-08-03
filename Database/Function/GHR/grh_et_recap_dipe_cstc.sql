-- Function: grh_et_recap_dipe_cstc(bigint, character varying, character varying)

-- DROP FUNCTION grh_et_recap_dipe_cstc(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_recap_dipe_cstc(IN societe_ bigint, IN agence_ character varying, IN header_ character varying)
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
	if(header_ is not null and header_ != '')then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom, e.matricule_cnps as numero from yvs_grh_bulletins b INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
		inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id
		where h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) as montant, coalesce(d.taux_patronal, 0) as taux from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				else
					select into valeur_ coalesce(d.retenu_salariale, 0) as montant, coalesce(d.taux_salarial, 0) as taux from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
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
ALTER FUNCTION grh_et_recap_dipe_cstc(bigint, character varying, character varying)
  OWNER TO postgres;
