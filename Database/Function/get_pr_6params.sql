-- Function: get_pr(bigint, bigint, bigint, date, bigint, bigint)

-- DROP FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint);

CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	

BEGIN
	return (select get_pr(0,article_, depot_, tranche_, date_, unite_, 0));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint) IS 'retourne le prix de revient d'' article';
