-- Function: get_pr(bigint, bigint, bigint, bigint, date, bigint, bigint)
-- DROP FUNCTION get_pr(bigint, bigint, bigint, bigint, date, bigint, bigint);
CREATE OR REPLACE FUNCTION get_pr(article_ bigint, agence_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	_depot_ bigint ;

BEGIN
	IF(COALESCE(depot_, 0) > 0)THEN
		RETURN get_pr(article_, depot_, tranche_, date_, unite_, current_);
	ELSE
		_depot_ := (SELECT ad.depot FROM yvs_base_article_depot ad INNER JOIN yvs_base_depots d ON ad.depot = d.id WHERE ad.article = article_ AND ad.default_pr IS TRUE AND d.agence = agence_ LIMIT 1);
		RETURN get_pr(article_, _depot_, tranche_, date_, unite_, current_);
	END IF;
	RETURN 0;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, bigint, date, bigint, bigint) IS 'retourne le prix de revient d'' article';
