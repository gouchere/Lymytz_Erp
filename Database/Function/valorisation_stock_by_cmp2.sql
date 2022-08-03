-- Function: valorisation_stock_by_cmp2(bigint, bigint, double precision, double precision, character varying, bigint)

-- DROP FUNCTION valorisation_stock_by_cmp2(bigint, bigint, double precision, double precision, character varying, bigint);

CREATE OR REPLACE FUNCTION valorisation_stock_by_cmp2(article_ bigint, depot_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE 
	
BEGIN
	return valorisation_stock_by_cmp2(article_ ,depot_ ,0 , quantite_ ,cout_ ,tableexterne_ ,idexterne_ ,current_date);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_cmp2(bigint, bigint, double precision, double precision, character varying, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_cmp2(bigint, bigint, double precision, double precision, character varying, bigint) IS 'Valorisation de stock pas la methode CMP II';
