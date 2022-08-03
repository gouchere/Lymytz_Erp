-- Function: grh_et_recap_dipe_rav(bigint, character varying, character varying)
-- DROP FUNCTION grh_et_recap_dipe_rav(bigint, character varying, character varying);
CREATE OR REPLACE FUNCTION grh_et_recap_dipe_rav(IN societe_ bigint, IN agence_ character varying, IN header_ character varying)
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
	if(header_ is not null and header_ != '')then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
		inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
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
						where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'))
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
ALTER FUNCTION grh_et_recap_dipe_rav(bigint, character varying, character varying)
  OWNER TO postgres;
