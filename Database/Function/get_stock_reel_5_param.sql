-- Function: get_stock_reel(bigint, bigint, bigint, date, date)

-- DROP FUNCTION get_stock_reel(bigint, bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE 

BEGIN
	return get_stock_reel(art_ ,tranche_ ,depot_ ,0::bigint ,0::bigint , date_debut_, date_fin_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, bigint, date, date)
  OWNER TO postgres;
