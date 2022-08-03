-- Function: grh_et_journal_paye_gain(bigint, character varying, character varying)

-- DROP FUNCTION grh_et_journal_paye_gain(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_journal_paye_gain(IN societe_ bigint, IN agence_ character varying, IN header_ character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, element character varying, valeur character varying, rang integer, is_double boolean) AS
$BODY$
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
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_journal_paye_gain(bigint, character varying, character varying)
  OWNER TO postgres;
