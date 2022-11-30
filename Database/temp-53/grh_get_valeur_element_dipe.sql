-- FUNCTION: public.grh_get_valeur_element_dipe(bigint, character varying, bigint)

-- DROP FUNCTION IF EXISTS public.grh_get_valeur_element_dipe(bigint, character varying, bigint);

CREATE OR REPLACE FUNCTION public.grh_get_valeur_element_dipe(
	element_ bigint,
	header_ character varying,
	employe_ bigint)
RETURNS double precision
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$
declare 
	
begin 	
	return (SELECT grh_get_valeur_element_dipe(element_, header_, employe_, false));
end;
$BODY$;

ALTER FUNCTION public.grh_get_valeur_element_dipe(bigint, character varying, bigint)
    OWNER TO postgres;
