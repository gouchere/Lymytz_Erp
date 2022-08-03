-- Function: get_stock_consigne(bigint, bigint, date)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock_consigne(art_ bigint, depot_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE 
 
BEGIN
	return get_stock_consigne(art_ ,0::bigint ,depot_ ,0::bigint ,0::bigint ,date_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, date)
  OWNER TO postgres;
