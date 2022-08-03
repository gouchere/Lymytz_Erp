-- Function: valorisation_stock_by_cmp2(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date)

-- DROP FUNCTION valorisation_stock_by_cmp2(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date);

CREATE OR REPLACE FUNCTION valorisation_stock_by_cmp2(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE 
	entree_ record;  
	new_cout double precision default 0;
BEGIN
	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_cmp2(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_cmp2(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date) IS 'Valorisation de stock pas la methode CMP II';
