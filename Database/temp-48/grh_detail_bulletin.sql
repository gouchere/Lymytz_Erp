-- Function: grh_detail_bulletin(bigint, integer)
-- DROP FUNCTION grh_detail_bulletin(bigint, integer);
CREATE OR REPLACE FUNCTION grh_detail_bulletin(IN bulletin_ bigint, IN nombre_ integer)
  RETURNS TABLE(numero integer, designation character varying, base double precision, nombre double precision, taux_salarial double precision, retenu_salarial double precision, montant_payer double precision, taux_patronal double precision, montant_employeur double precision, rubrique character varying, rang integer) AS
$BODY$
declare 
	_detail RECORD;
	_rang INTEGER DEFAULT 1;
	
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_detail_bulletin;
	CREATE TEMP TABLE IF NOT EXISTS table_detail_bulletin(_numero_ integer, _designation_ character varying, _base_ double precision, _nombre_ double precision, _taux_salarial_ double precision, _retenu_salarial_ double precision, _montant_payer_ double precision, _taux_patronal_ double precision, _montant_employeur_ double precision, _rubrique_ character varying, _rang_ integer);
	delete from table_detail_bulletin;

	for _detail in SELECT e."nom" AS yvs_element_salaire_nom, e."code" AS yvs_element_salaire_code, e."categorie" AS yvs_element_salaire_categorie, e."visible_bulletin" AS yvs_element_salaire_visible_bulletin, 
		     e."retenue" AS yvs_element_salaire_retenue, d."element_salaire" AS yvs_grh_detail_bulletin_element_salaire, d."bulletin" AS yvs_grh_detail_bulletin_bulletin,
		     d."base" AS yvs_grh_detail_bulletin_base, d."quantite" AS yvs_grh_detail_bulletin_quantite, d."taux_salarial" AS yvs_grh_detail_bulletin_taux_salarial, 
		     d."taux_patronal" AS yvs_grh_detail_bulletin_taux_patronal, e."num_sequence" AS yvs_element_salaire_num_sequence, r."designation" AS yvs_grh_rubrique_bulletin_designation,
		     r."code" AS yvs_grh_rubrique_bulletin_code, d."retenu_salariale" AS yvs_grh_detail_bulletin_retenu_salariale, d."montant_payer" AS yvs_grh_detail_bulletin_montant_payer, 
		     d."montant_employeur" AS yvs_grh_detail_bulletin_montant_employeur
		FROM
		     "public"."yvs_grh_element_salaire" e INNER JOIN "public"."yvs_grh_detail_bulletin" d ON e."id" = d."element_salaire" INNER JOIN "public"."yvs_grh_rubrique_bulletin" r ON e."rubrique" = r."id"
		WHERE (d."retenu_salariale" > 0 OR d."montant_payer" >=0 OR d."montant_employeur" >0) AND e."visible_bulletin" = true AND d.now_visible = true AND d."bulletin" = bulletin_
		ORDER BY e."num_sequence" ASC, e."categorie" ASC, e."num_sequence" ASC, e."retenue" ASC, r."designation" ASC
	loop
		insert into table_detail_bulletin values(_detail.yvs_element_salaire_num_sequence, _detail.yvs_element_salaire_nom, _detail.yvs_grh_detail_bulletin_base, _detail.yvs_grh_detail_bulletin_quantite, _detail.yvs_grh_detail_bulletin_taux_salarial, _detail.yvs_grh_detail_bulletin_retenu_salariale, _detail.yvs_grh_detail_bulletin_montant_payer, _detail.yvs_grh_detail_bulletin_taux_patronal, _detail.yvs_grh_detail_bulletin_montant_employeur, _detail.yvs_grh_rubrique_bulletin_designation, _rang);
		_rang = _rang + 1;
	end loop;
	for i in _rang .. nombre_ loop
		insert into table_detail_bulletin values(0, '', 0, 0, 0, 0, 0, 0, 0, '', _rang);
	end loop;
	return QUERY select * from table_detail_bulletin order by _rang_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_detail_bulletin(bigint, integer)
  OWNER TO postgres;
