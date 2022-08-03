-- Function: get_stock(bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock(bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock(art_ bigint, depot_ bigint, date_ date, tranche_ bigint)
  RETURNS double precision AS
$BODY$
	DECLARE entree_ double precision; 
		sortie_ double precision; 
BEGIN
	return get_stock(art_ ,tranche_ ,depot_ ,0::bigint ,0::bigint ,date_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock(bigint, bigint, date, bigint)
  OWNER TO postgres;
