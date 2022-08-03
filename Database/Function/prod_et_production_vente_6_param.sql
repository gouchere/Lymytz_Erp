-- Function: prod_et_production_vente(bigint, character varying, date, date, character varying, character varying)
DROP FUNCTION prod_et_production_vente(bigint, character varying, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION prod_et_production_vente(IN societe_ bigint, IN articles_ character varying, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN type_ character varying)
  RETURNS TABLE(id bigint, reference character varying, designation character varying, unite bigint, intitule character varying, entete character varying, date_debut date, date_fin date, production double precision, vente double precision, ecart double precision, production_val double precision, vente_val double precision, ecart_val double precision) AS
$BODY$
DECLARE 
   
BEGIN 	
	RETURN QUERY SELECT * FROM prod_et_production_vente(societe_, articles_, date_debut_, date_fin_, periode_, type_, 'R');
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION prod_et_production_vente(bigint, character varying, date, date, character varying, character varying)
  OWNER TO postgres;
