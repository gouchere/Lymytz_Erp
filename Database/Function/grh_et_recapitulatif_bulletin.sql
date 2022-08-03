-- Function: grh_et_recapitulatif_bulletin(bigint, character varying)
-- DROP FUNCTION grh_et_recapitulatif_bulletin(bigint, character varying);
CREATE OR REPLACE FUNCTION grh_et_recapitulatif_bulletin(IN employe_ bigint, IN header_ character varying)
  RETURNS TABLE(element bigint, numero integer, libelle character varying, montant double precision, rang integer, is_total boolean) AS
$BODY$
declare 
	titre_ character varying default 'RECAP_BUL';
    
	element_ record;
	valeur_ double precision default 0;
	societe_ bigint;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recapitulatif_bulletin;
	CREATE TEMP TABLE IF NOT EXISTS table_recapitulatif_bulletin(_element bigint, _numero integer, _libelle character varying, _montant double precision, _rang integer, _is_total boolean);
	delete from table_recapitulatif_bulletin;
	select into societe_ societe  from yvs_agences a inner join yvs_grh_employes e on e.agence = a.id where e.id = employe_;
	for element_ in select e.element_salaire as id, e.libelle, coalesce(y.num_sequence, 0) as numero, coalesce(e.ordre, 0) as ordre, y.retenue, e.groupe_element as groupe from yvs_stat_grh_element_dipe e 
	inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
	loop
		if(element_.retenue is false)then
			select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
				inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_ordre_calcul_salaire o on b.entete = o.id
				where d.element_salaire = element_.id and c.employe = employe_ and b.entete::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
		else
			select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
				inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_ordre_calcul_salaire o on b.entete = o.id
				where d.element_salaire = element_.id and c.employe = employe_ and b.entete::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(header_,',') val) and (b.statut IN ('V', 'P'));
		end if;
		insert into table_recapitulatif_bulletin values(element_.id, element_.numero, element_.libelle, coalesce(valeur_, 0), element_.ordre, false);
-- 		insert into table_recapitulatif_bulletin values(element_.id, element_.numero, element_.libelle, coalesce(valeur_, 0), element_.ordre, true);
	end loop;
	return QUERY select * from table_recapitulatif_bulletin order by _is_total, _rang, _element;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recapitulatif_bulletin(bigint, character varying)
  OWNER TO postgres;
