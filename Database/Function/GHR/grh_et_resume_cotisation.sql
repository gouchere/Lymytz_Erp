-- Function: grh_et_resume_cotisation(character varying, character varying)

-- DROP FUNCTION grh_et_resume_cotisation(character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_resume_cotisation(IN header_ character varying, IN agence_ character varying)
  RETURNS TABLE(element bigint, groupe bigint, libelle character varying, taux_salarial double precision, taux_patronal double precision, taux_global double precision, assiette_annuel double precision, base double precision, montant_salarial double precision, montant_patronal double precision, montant_global double precision, nbre_homme integer, nbre_femme integer, is_group boolean, is_total boolean) AS
$BODY$
declare 
	titre_ character varying default 'RESUME DES COTISATIONS';
	
	valeur_ record;
	element_ record;	
	taux_salarial_ double precision default 0; 
	taux_patronal_ double precision default 0;
	taux_global_ double precision default 0;
	assiette_annuel_ double precision default 0;
	base_ double precision default 0;
	montant_salarial_ double precision default 0;
	montant_patronal_ double precision default 0;
	montant_global_ double precision default 0;
	nbre_homme_ integer default 0;
	nbre_femme_ integer default 0;
   
begin 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	CREATE TEMP TABLE IF NOT EXISTS table_resume_cotisation(_element bigint, _groupe bigint, _libelle character varying, _taux_salarial double precision, _taux_patronal double precision, _taux_global double precision, _assiette_annuel double precision,
		_base double precision, _montant_salarial double precision, _montant_patronal double precision, _montant_global double precision, _nbre_homme integer, _nbre_femme integer, _is_group boolean, _is_total boolean);
	delete from table_resume_cotisation;
	
	if((header_ is not null and header_ !='') and (agence_ is not null and agence_ !=''))then
		for element_ in select e.element_salaire as id, coalesce(e.groupe_element, 0) as groupe, e.libelle from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat where s.libelle = titre_
		loop
			-- Récuperation du taux salarial
			select into taux_salarial_ coalesce(d.taux_salarial, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
			inner join yvs_grh_bulletins b on d.bulletin = b.id INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
			where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id 
			and h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val);
			-- Récuperation du taux patronal
			select into taux_patronal_ coalesce(d.taux_patronal, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
			inner join yvs_grh_bulletins b on d.bulletin = b.id INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
			where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id 
			and h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val);
			-- Calcul du taux global
			taux_global_ = taux_salarial_ + taux_patronal_;
			-- Récuperation de l'assiette annuelle de cotisation
			assiette_annuel_ = 0;
			-- Récuperation de la base
			select into base_ coalesce(sum(d.base), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
			inner join yvs_grh_bulletins b on d.bulletin = b.id INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
			where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id 
			and h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val);
			-- Récuperation du montant salarial
			select into montant_salarial_ coalesce(sum(d.retenu_salariale), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
			inner join yvs_grh_bulletins b on d.bulletin = b.id INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
			where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id 
			and h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val);
			-- Récuperation du montant patronal
			select into montant_patronal_ coalesce(sum(d.montant_employeur), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
			inner join yvs_grh_bulletins b on d.bulletin = b.id INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
			where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id 
			and h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val);
			-- Calcul du montant global
			montant_global_ = montant_salarial_ + montant_patronal_;
			-- Récuperation du nombre d'employé homme qui a cotisé
			select into nbre_homme_ coalesce(count(y.id), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
			inner join yvs_grh_bulletins b on d.bulletin = b.id INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
			where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id 
			and h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) and y.civilite = 'M.';
			-- Récuperation du nombre d'employé femme qui a cotisé
			select into nbre_femme_ coalesce(count(y.id), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
			inner join yvs_grh_bulletins b on d.bulletin = b.id INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id 
			where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id 
			and h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) and y.civilite != 'M.';

			insert into table_resume_cotisation values(element_.id, element_.groupe, element_.libelle, taux_salarial_, taux_patronal_, taux_global_, assiette_annuel_, base_,
									montant_salarial_, montant_patronal_, montant_global_, nbre_homme_, nbre_femme_, false, false);
		end loop;
		-- Récuperation les totaux par groupe
		for element_ in select distinct _groupe as groupe, g.libelle from table_resume_cotisation y left join yvs_stat_grh_groupe_element g on y._groupe = g.id
		loop
			select into valeur_ coalesce(sum(_taux_salarial), 0) as taux_salarial, coalesce(sum(_taux_patronal), 0) as taux_patronal, coalesce(sum(_taux_global), 0) as taux_global, 
				coalesce(sum(_assiette_annuel), 0) as assiette_annuel, coalesce(sum(_base), 0) as base, coalesce(sum(_montant_salarial), 0) as montant_salarial, coalesce(sum(_montant_patronal), 0) as montant_patronal, 
				coalesce(sum(_montant_global), 0) as montant_global, coalesce(sum(_nbre_homme), 0) as nbre_homme, coalesce(sum(_nbre_femme), 0) as nbre_femme
				from table_resume_cotisation where _groupe = element_.groupe;
				
			insert into table_resume_cotisation values(0, element_.groupe, element_.libelle, 0, 0, 0, 0, 0, valeur_.montant_salarial, 
									valeur_.montant_patronal, valeur_.montant_global, 0, 0, true, false);
		end loop;
		-- Récuperation du total des totaux par groupe
		select into valeur_ coalesce(sum(_taux_salarial), 0) as taux_salarial, coalesce(sum(_taux_patronal), 0) as taux_patronal, coalesce(sum(_taux_global), 0) as taux_global, 
			coalesce(sum(_assiette_annuel), 0) as assiette_annuel, coalesce(sum(_base), 0) as base, coalesce(sum(_montant_salarial), 0) as montant_salarial, coalesce(sum(_montant_patronal), 0) as montant_patronal, 
			coalesce(sum(_montant_global), 0) as montant_global, coalesce(sum(_nbre_homme), 0) as nbre_homme, coalesce(sum(_nbre_femme), 0) as nbre_femme
			from table_resume_cotisation where _is_group is true;
			
		insert into table_resume_cotisation values(0, 0, '', 0, 0, 0, 0, 0, valeur_.montant_salarial, valeur_.montant_patronal, 
								valeur_.montant_global, 0, 0, false, true);
	end if;
	return QUERY select * from table_resume_cotisation order by _is_total, _groupe, _is_group, _element;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_resume_cotisation(character varying, character varying)
  OWNER TO postgres;
