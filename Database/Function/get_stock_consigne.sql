-- Function: get_stock_consigne(bigint, bigint, bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	 
BEGIN
	return get_stock_consigne(article_, 0, depot_ ,agence_ , societe_, date_, 0::bigint);
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;
