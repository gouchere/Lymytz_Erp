-- FUNCTION: public.grh_get_brut_annuel(bigint, bigint, bigint)

-- DROP FUNCTION IF EXISTS public.grh_get_brut_annuel(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION public.grh_get_brut_annuel(
	societe_ bigint,
	employe_ bigint,
	header_ bigint)
RETURNS double precision
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$
declare 
   
begin 
	 
	return (SELECT grh_get_brut_annuel(societe_, employe_, header_, false));
end;
$BODY$;

ALTER FUNCTION public.grh_get_brut_annuel(bigint, bigint, bigint)
    OWNER TO postgres;
