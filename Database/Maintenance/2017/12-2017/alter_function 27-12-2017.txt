-- Function: grh_et_livre_paie(bigint, character varying, character varying, character varying, character varying)

-- DROP FUNCTION grh_et_livre_paie(bigint, character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_livre_paie(IN societe_ bigint, IN header_ character varying, IN agence_ character varying, IN code_ character varying, IN type_ character varying)
  RETURNS TABLE(regle bigint, numero integer, libelle character varying, groupe bigint, element bigint, montant double precision, rang integer, is_group boolean, is_total boolean) AS
$BODY$
declare
	titre_ character varying default 'LIVRE DE PAIE';

	element_ record;
	regles_ record;
	second_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
	is_total_ boolean default false;
	query_ character varying;

begin
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_livre_paie;
	CREATE TEMP TABLE IF NOT EXISTS table_livre_paie(_regle bigint, _numero integer, _libelle character varying, _groupe bigint, _element bigint, _montant double precision, _rang integer, _is_group boolean, _is_total boolean);
	delete from table_livre_paie;
	if((header_ is not null and header_ != '') and (type_ is not null))then
		for regles_ in select e.id as ids, e.element_salaire as id, e.libelle, coalesce(y.num_sequence, 0) as numero, coalesce(e.ordre, 0) as ordre, y.retenue, e.groupe_element as groupe, by_formulaire from yvs_stat_grh_element_dipe e
		inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.libelle = titre_ and s.societe = societe_ order by e.ordre
		loop
			RAISE NOTICE '% %',regles_.libelle,regles_.by_formulaire;
			somme_ = 0;
			if(regles_.numero = 0)then
				is_total_ = true;
			else
				is_total_ = false;
			end if;
			if(type_ = 'E')then
				for element_ in select distinct(e.id) as id from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id
				where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
				loop
					if(regles_.by_formulaire is true)then
						valeur_ = (select grh_get_valeur_formulaire_dipe(agence_, regles_.ids, header_, element_.id, type_));
					else
						if(regles_.retenue is false)then
							select into valeur_ coalesce(sum(d.montant_payer), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
								inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regles_.id and c.employe = element_.id
								and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
						else
							select into valeur_ coalesce(sum(d.retenu_salariale), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
								inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regles_.id and c.employe = element_.id
								and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
						end if;
					end if;
					valeur_ = coalesce(valeur_, 0);
					insert into table_livre_paie values(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, valeur_, regles_.ordre, false, is_total_);
					somme_ = somme_ + valeur_;
				end loop;
			elsif(type_ = 'S')then
				for element_ in select d.id as id, d.print_with_children from yvs_grh_departement d where d.visible_on_livre_paie is true
				loop
					if(regles_.by_formulaire is true)then
						valeur_ = (select grh_get_valeur_formulaire_dipe(agence_, regles_.ids, header_, element_.id, type_));
					else
						if(regles_.retenue is false)then
							select into valeur_ coalesce(sum(d.montant_payer), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
								inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id
								inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id inner join yvs_grh_departement de on p.departement = de.id
								where d.element_salaire = regles_.id and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val)
								and e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
								and (p.departement = element_.id or (element_.print_with_children is true and p.departement in (select id from grh_get_sous_service(element_.id, true))));
						else
							select into valeur_ coalesce(sum(d.retenu_salariale), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
								inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id
								inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id inner join yvs_grh_departement de on p.departement = de.id
								where d.element_salaire = regles_.id and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val)
								and e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
								and (p.departement = element_.id or (element_.print_with_children is true and p.departement in (select id from grh_get_sous_service(element_.id, true))));
						end if;
					end if;
					valeur_ = coalesce(valeur_, 0);
					if(valeur_ != 0)then
						insert into table_livre_paie values(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, valeur_, regles_.ordre, false, is_total_);
					end if;
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
ALTER FUNCTION grh_et_livre_paie(bigint, character varying, character varying, character varying, character varying)
  OWNER TO postgres;



  -- Function: grh_get_valeur_element_dipe(bigint, character varying, bigint)

-- DROP FUNCTION grh_get_valeur_element_dipe(bigint, character varying, bigint);

CREATE OR REPLACE FUNCTION grh_get_valeur_element_dipe(element_ bigint, header_ character varying, employe_ bigint)
  RETURNS double precision AS
$BODY$
declare
	valeur_ double precision default 0;
	type_ character varying default 'V';
	retenue_ boolean default false;
	regle_ record;
begin
	select into regle_ * from yvs_stat_grh_element_dipe where id = element_;
	if(regle_.id is not null)then
		type_ = regle_.champ_valeur;
		if(type_ is not null and type_ != '')then
			CASE type_
				WHEN 'B' THEN
					select into valeur_ coalesce(sum(d.base), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'Q' THEN
					select into valeur_ coalesce(sum(d.quantite), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'TS' THEN
					select into valeur_ coalesce(sum(d.taux_salarial), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'TP' THEN
					select into valeur_ coalesce(sum(d.taux_patronal), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'TSP' THEN
					select into valeur_ coalesce(sum(d.taux_patronal), 0)+coalesce(sum(d.taux_salarial), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'RS' THEN
					select into valeur_ coalesce(sum(d.retenu_salariale), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'RP' THEN
					select into valeur_ coalesce(sum(d.montant_employeur), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				ELSE
					select into valeur_ coalesce(sum(d.montant_payer), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
			END CASE;
		else
			select into retenue_ retenue from yvs_grh_element_salaire where id = regle_.element_salaire;
			if(element_.retenue is false)then
				select into valeur_ coalesce(sum(d.montant_payer), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
					inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_
					and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
			else
				select into valeur_ coalesce(sum(d.retenu_salariale), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id
					inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_
					and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
			end if;
		end if;
	end if;
	return COALESCE(valeur_, 0);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_get_valeur_element_dipe(bigint, character varying, bigint)
  OWNER TO postgres;
