-- FUNCTION: public.grh_get_valeur_formulaire_dipe(character varying, bigint, character varying, bigint, character varying)

-- DROP FUNCTION IF EXISTS public.grh_get_valeur_formulaire_dipe(character varying, bigint, character varying, bigint, character varying);

CREATE OR REPLACE FUNCTION public.grh_get_valeur_formulaire_dipe(
	agence_ character varying,
	element_ bigint,
	header_ character varying,
	service_ bigint,
	type_ character varying)
RETURNS double precision
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$
declare 
	
begin 	
	return (SELECT grh_get_valeur_formulaire_dipe(agence_, element_, header_, service_, type_, false));
end;
$BODY$;

ALTER FUNCTION public.grh_get_valeur_formulaire_dipe(character varying, bigint, character varying, bigint, character varying)
    OWNER TO postgres;
