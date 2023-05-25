CREATE OR REPLACE FUNCTION public.get_ttc_vente(id_ bigint)
 RETURNS double precision
 LANGUAGE plpgsql
AS $function$
DECLARE	
BEGIN
	RETURN get_ttc_vente(id_, TRUE);
END;$function$
;
