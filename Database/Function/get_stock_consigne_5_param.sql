-- Function: get_stock_consigne(bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	 
BEGIN
	return get_stock_consigne(article_, depot_ ,agence_ , societe_, date_, 0::bigint);
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, date)
  OWNER TO postgres;
