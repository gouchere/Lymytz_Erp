-- Function: grh_et_fiche_individuel(bigint, date, date, character varying)
-- DROP FUNCTION grh_et_fiche_individuel(bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION grh_et_fiche_individuel(IN employe_ bigint, IN debut_ date, IN fin_ date, IN periode_ character varying)
  RETURNS TABLE(element bigint, numero integer, libelle character varying, groupe bigint, entete character varying, montant double precision, rang integer, is_group boolean, is_total boolean) AS
$BODY$
declare 
	titre_ character varying default 'LIVRE_PAIE';

	date_save date default debut_;
	date_ date;
	taux_ double precision default 0;
	jour_ character varying;
	jour_0 character varying;
	mois_ character varying;
	mois_0 character varying;
	annee_ character varying;
	annee_0 character varying;
    
	element_ record;
	second_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
	is_total_ boolean default false;
	societe_ bigint;

    dates_ RECORD;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_fiche_individuel;
	CREATE TEMP TABLE IF NOT EXISTS table_fiche_individuel(_element bigint, _numero integer, _libelle character varying, _groupe bigint, _entete character varying, _montant double precision, _rang integer, _is_group boolean, _is_total boolean);
	delete from table_fiche_individuel;
	select into societe_ societe  from yvs_agences a inner join yvs_grh_employes e on e.agence = a.id where e.id = employe_;
	for element_ in select e.element_salaire as id, e.libelle, coalesce(y.num_sequence, 0) as numero, coalesce(e.ordre, 0) as ordre, y.retenue, e.groupe_element as groupe from yvs_stat_grh_element_dipe e 
	inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
	loop
		somme_ = 0;
		debut_ = date_save;
		for dates_ in select * from decoupage_interval_date(debut_, fin_, periode_)
		loop
			jour_ = dates_.intitule;

			if(element_.retenue is false)then
				select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_ordre_calcul_salaire o on b.entete = o.id
					where d.element_salaire = element_.id and c.employe = employe_ and o.debut_mois >= dates_.date_debut and o.fin_mois <= dates_.date_fin and (b.statut IN ('V', 'P'));
			else
				select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_ordre_calcul_salaire o on b.entete = o.id
					where d.element_salaire = element_.id and c.employe = employe_ and o.debut_mois >= dates_.date_debut and o.fin_mois <= dates_.date_fin and (b.statut IN ('V', 'P'));
			end if;
			insert into table_fiche_individuel values(element_.id, element_.numero, element_.libelle, element_.groupe, jour_, coalesce(valeur_, 0), element_.ordre, false, false);
			somme_ = somme_ + coalesce(valeur_, 0);
			
		end loop;
		if(somme_ = 0)then
			delete from table_fiche_individuel where _element = element_.id;
		end if;
	end loop;
	return QUERY select * from table_fiche_individuel order by _is_total, _rang, _element;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_fiche_individuel(bigint, date, date, character varying)
  OWNER TO postgres;
