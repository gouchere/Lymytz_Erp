-- FUNCTION: public.grh_get_brut_annuel(bigint, bigint, bigint)

-- DROP FUNCTION IF EXISTS public.grh_get_brut_annuel(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION public.grh_get_brut_annuel(
	societe_ bigint,
	employe_ bigint,
	header_ bigint,
	brouillon boolean)
RETURNS double precision
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$
declare 
	titre_ character varying default 'GEN_SMO';
		
	valeur_ double precision default 0; 
   
begin 	
	SELECT INTO valeur_
	     yvs_grh_detail_bulletin."montant_payer" AS yvs_grh_detail_bulletin_montant_payer
	FROM
	     "public"."yvs_grh_contrat_emps" yvs_grh_contrat_emps
	     INNER JOIN "public"."yvs_grh_bulletins" yvs_grh_bulletins ON yvs_grh_contrat_emps."id" = yvs_grh_bulletins."contrat"
	     INNER JOIN "public"."yvs_grh_detail_bulletin" yvs_grh_detail_bulletin ON yvs_grh_bulletins."id" = yvs_grh_detail_bulletin."bulletin"
	     INNER JOIN "public"."yvs_stat_grh_element_dipe" yvs_stat_grh_element_dipe ON yvs_grh_detail_bulletin."element_salaire" = yvs_stat_grh_element_dipe."element_salaire"
	     INNER JOIN "public"."yvs_stat_grh_etat" yvs_stat_grh_etat ON yvs_stat_grh_element_dipe."etat" = yvs_stat_grh_etat."id"
	WHERE
	     yvs_stat_grh_etat."code" = titre_
	 AND yvs_grh_bulletins."entete" = header_
	 AND yvs_stat_grh_etat."societe" = societe_
	 AND yvs_grh_contrat_emps."employe" = employe_
     AND ((brouillon IS FALSE AND yvs_grh_bulletins.statut IN ('V', 'P')) OR (brouillon IS TRUE AND yvs_grh_bulletins.statut IS NOT NULL));
	 
	return COALESCE(valeur_, 0);
end;
$BODY$;

ALTER FUNCTION public.grh_get_brut_annuel(bigint, bigint, bigint, boolean)
    OWNER TO postgres;
