-- Function: get_stock_reel(bigint, bigint, date, date, character varying)

-- DROP FUNCTION get_stock_reel(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, depot_ bigint, date_debut_ date, date_fin_ date, type_ character varying)
  RETURNS double precision AS
$BODY$
DECLARE 
 
BEGIN
	return get_stock_reel(art_ ,0::bigint ,depot_ ,0::bigint ,0::bigint , date_debut_, date_fin_, type_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
