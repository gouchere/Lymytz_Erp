-- Function: get_stock(bigint, bigint, bigint, bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock(bigint, bigint, bigint, bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
	reel_ double precision; 
	consign_ double precision; 
BEGIN
	reel_ = (select get_stock_reel(art_ ,tranche_ ,depot_ ,agence_ ,societe_ ,date_ ,unite_));
	consign_ = (select get_stock_consigne(art_ ,tranche_ ,depot_ ,agence_ ,societe_ ,date_ ,unite_));
	IF reel_ IS null THEN
	 reel_:=0;	
	END IF;
	IF consign_ IS null THEN
	  consign_:=0;	
	END IF;
	return (reel_ - consign_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock(bigint, bigint, bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;
