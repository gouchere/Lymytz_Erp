-- FUNCTION: public.grh_et_journal_paye_gain(bigint, character varying, character varying)

DROP FUNCTION IF EXISTS public.grh_et_journal_paye_gain(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION public.grh_et_journal_paye_gain(
	societe_ bigint,
	agence_ character varying,
	header_ character varying,
	brouillon boolean)
RETURNS TABLE(employe bigint, matricule character varying, nom character varying, element character varying, valeur character varying, rang integer, is_double boolean) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$
declare 
	titre_ character varying default 'GEN_JS';
	
	employe_ record;
	element_ record;	
	valeur_ double precision default 0; 
	somme_ double precision default 0; 
	ordre_ integer default 0; 
   
begin 	
	RETURN QUERY SELECT * FROM grh_et_journal_salaire();
end;
$BODY$;

ALTER FUNCTION public.grh_et_journal_paye_gain(bigint, character varying, character varying, boolean)
    OWNER TO postgres;
