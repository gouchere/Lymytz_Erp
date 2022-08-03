-- Function: prod_et_synthese_production_distribution(bigint, bigint, bigint, date, date)
DROP FUNCTION prod_et_synthese_production_distribution(bigint, bigint, bigint, date, date);
CREATE OR REPLACE FUNCTION prod_et_synthese_production_distribution(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN date_debut_ date, IN date_fin_ date, IN type_ character varying)
  RETURNS TABLE(id bigint, groupe character varying, libelle character varying, type bigint, classe bigint, code character varying, intitule character varying, prix double precision, quantite double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
   
BEGIN 		
	RETURN QUERY SELECT * FROM com_et_synthese_approvision_distribution(societe_, agence_, depot_, date_debut_, date_fin_, true, type_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION prod_et_synthese_production_distribution(bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
