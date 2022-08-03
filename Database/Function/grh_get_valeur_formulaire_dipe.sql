-- Function: grh_get_valeur_formulaire_dipe(character varying, bigint, character varying, bigint, character varying)
-- DROP FUNCTION grh_get_valeur_formulaire_dipe(character varying, bigint, character varying, bigint, character varying);
CREATE OR REPLACE FUNCTION grh_get_valeur_formulaire_dipe(agence_ character varying, element_ bigint, header_ character varying, service_ bigint, type_ character varying)
  RETURNS double precision AS
$BODY$
declare 
	sous_valeur_ double precision default 0; 
	valeur_ double precision default 0; 
	formulaire_ boolean default false;
	retenue_ boolean default false;
	with_sous_ boolean default true;
	sous_ bigint;
	regle_ record;
	
begin 	
	select into regle_ * from yvs_stat_grh_element_dipe where id = element_;
	if(regle_.id is not null)then
		formulaire_ = regle_.by_formulaire;
		if(formulaire_ is true)then
			IF(upper(regle_.libelle) = upper('Net à payer')
			OR upper(regle_.libelle) = upper('Net a payer')
			OR upper(regle_.libelle) = upper('Net à payé')
			OR upper(regle_.libelle) = upper('Net a payé'))THEN				
				if(type_ = 'E')then
					SELECT INTO valeur_ SUM(yvs_grh_detail_bulletin."montant_payer" - yvs_grh_detail_bulletin."retenu_salariale") AS Salaire
					FROM
					     "public"."yvs_grh_bulletins" yvs_grh_bulletins INNER JOIN "public"."yvs_grh_detail_bulletin" yvs_grh_detail_bulletin ON yvs_grh_bulletins."id" = yvs_grh_detail_bulletin."bulletin"
					     INNER JOIN "public"."yvs_grh_element_salaire" yvs_grh_element_salaire ON yvs_grh_detail_bulletin."element_salaire" = yvs_grh_element_salaire."id"
					     INNER JOIN "public"."yvs_grh_contrat_emps" yvs_grh_contrat_emps ON yvs_grh_bulletins."contrat" = yvs_grh_contrat_emps."id"
					     INNER JOIN "public"."yvs_grh_employes" yvs_grh_employes ON yvs_grh_contrat_emps."employe" = yvs_grh_employes."id"
					     INNER JOIN "public"."yvs_grh_poste_de_travail" yvs_grh_poste_de_travail ON yvs_grh_employes."poste_actif" = yvs_grh_poste_de_travail."id"
					WHERE yvs_grh_bulletins."entete"::character varying IN (select val from regexp_split_to_table(header_,',') val) 
					AND yvs_grh_element_salaire."visible_bulletin" IS true AND
					(yvs_grh_detail_bulletin."montant_payer" >0 or yvs_grh_detail_bulletin."retenu_salariale" > 0 or yvs_grh_detail_bulletin."montant_employeur"> 0 ) 
					and yvs_grh_detail_bulletin."now_visible" is true
					AND yvs_grh_employes."id" = service_ and (yvs_grh_bulletins."statut" IN ('V', 'P'));
				elsif(type_ = 'S')then
					select into with_sous_ coalesce(print_with_children, false) from yvs_grh_departement where id = service_;
					SELECT INTO valeur_ SUM(d.montant_payer - d.retenu_salariale) AS Salaire 
						from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire l on d.element_salaire = l.id
						inner join yvs_grh_bulletins b on d.bulletin = b.id
						INNER JOIN yvs_grh_header_bulletin h on h.bulletin = b.id
						inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
						where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) 
						and l.visible_bulletin is true and d.now_visible is true
						and (d.montant_payer > 0 or d.retenu_salariale > 0 or d.montant_employeur > 0)
						and h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) 
						and h.id_service = service_ and (b.statut IN ('V', 'P'));	
					if(with_sous_ is true)then
						for sous_ in select y.id from grh_get_sous_service(service_, true) y
						loop
							select into sous_valeur_ SUM(d.montant_payer - d.retenu_salariale) AS Salaire 
								from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire l on d.element_salaire = l.id
								inner join yvs_grh_bulletins b on d.bulletin = b.id
								inner join yvs_grh_header_bulletin h on h.bulletin = b.id
								inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
								where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) 
								and l.visible_bulletin is true and d.now_visible is true
								and (d.montant_payer > 0 or d.retenu_salariale > 0 or d.montant_employeur > 0)
								and h.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) 
								and h.id_service = sous_ and (b.statut IN ('V', 'P'));	

							valeur_ = coalesce(valeur_, 0) + coalesce(sous_valeur_, 0);
						end loop;	
					end if;
				else
					valeur_ = 0;
				end if;
			ELSE
				valeur_ = 0;
			END IF;
		else
			valeur_ = (select grh_get_valeur_element_dipe(regle_, header_, element_));
		end if;	 
	end if;
	return COALESCE(valeur_, 0);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_get_valeur_formulaire_dipe(character varying, bigint, character varying, bigint, character varying)
  OWNER TO postgres;
