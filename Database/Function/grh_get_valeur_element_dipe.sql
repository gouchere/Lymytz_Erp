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
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
				WHEN 'Q' THEN 
					select into valeur_ coalesce(sum(d.quantite), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
				WHEN 'TS' THEN 
					select into valeur_ coalesce(sum(d.taux_salarial), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
				WHEN 'TP' THEN 
					select into valeur_ coalesce(sum(d.taux_patronal), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
				WHEN 'TSP' THEN 
					select into valeur_ coalesce(sum(d.taux_patronal), 0)+coalesce(sum(d.taux_salarial), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
				WHEN 'RS' THEN 
					select into valeur_ coalesce(sum(d.retenu_salariale), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
				WHEN 'RP' THEN 
					select into valeur_ coalesce(sum(d.montant_employeur), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
				ELSE
					select into valeur_ coalesce(sum(d.montant_payer), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
			END CASE;
		else
			select into retenue_ retenue from yvs_grh_element_salaire where id = regle_.element_salaire;
			if(element_.retenue is false)then
				select into valeur_ coalesce(sum(d.montant_payer), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
					and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
			else
				select into valeur_ coalesce(sum(d.retenu_salariale), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
					and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
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
