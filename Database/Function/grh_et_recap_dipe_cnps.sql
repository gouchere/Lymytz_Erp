-- Function: grh_et_recap_dipe_cnps(bigint, character varying, character varying, character varying)

-- DROP FUNCTION grh_et_recap_dipe_cnps(bigint, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_recap_dipe_cnps(IN societe_ bigint, IN agence_ character varying, IN header_ character varying, IN sommable_ character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, numero character varying, entete character varying, rang integer, montant double precision, is_sum boolean, is_total boolean) AS
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
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_cnps(_employe bigint, _matricule character varying, _nom character varying, _numero character varying, _entete character varying, _rang integer, _montant double precision, _is_sum boolean, _is_total boolean);
	delete from table_recap_dipe_cnps;
	if(header_ is not null and header_ != '')then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom, e.matricule_cnps as numero from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.id as ids, e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				valeur_ = (select grh_get_valeur_element_dipe(element_.ids, header_, employe_.id));
				
				insert into table_recap_dipe_cnps values(employe_.id, employe_.matricule, employe_.nom, employe_.numero, element_.libelle, ordre_, valeur_, false, false);
				somme_ = somme_ + valeur_;
			end loop;		
			if(sommable_ is not null and sommable_ != '')then	
				select into valeur_ coalesce(sum(_montant), 0) from table_recap_dipe_cnps where _employe = employe_.id and _rang::character varying in (select colonne from regexp_split_to_table(sommable_,',') colonne);
				select into ordre_ max(colonne::integer) from regexp_split_to_table(sommable_,',') colonne;
				insert into table_recap_dipe_cnps values(employe_.id, employe_.matricule, employe_.nom, employe_.numero, 'Total PV', ordre_+1, valeur_, true, false);
			end if;
			
			select into valeur_ coalesce(sum(_montant), 0) from table_recap_dipe_cnps where _employe = employe_.id and _rang >= (select _rang from table_recap_dipe_cnps where _employe = employe_.id and _is_sum = true);
			select into ordre_ max(_rang) from table_recap_dipe_cnps where _employe = employe_.id;
			insert into table_recap_dipe_cnps values(employe_.id, employe_.matricule, employe_.nom, employe_.numero, 'Total CNPS', ordre_+1, valeur_, true, true);
			
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
ALTER FUNCTION grh_et_recap_dipe_cnps(bigint, character varying, character varying, character varying)
  OWNER TO postgres;
