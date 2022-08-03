-- Function: get_stock_reel(bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock_reel(bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, depot_ bigint, date_ date, tranche_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 

BEGIN
	return get_stock_reel(art_ ,tranche_ ,depot_ ,0::bigint ,0::bigint ,date_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, date, bigint)
  OWNER TO postgres;
