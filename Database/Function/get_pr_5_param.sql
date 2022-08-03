-- Function: get_pr(bigint, bigint, bigint, date, bigint, bigint)

-- DROP FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint);

CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pr_ double precision;
	_depot_ bigint ;
	query_ character varying default 'SELECT m.cout_stock FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE COALESCE(m.calcul_pr, TRUE) IS TRUE AND m.mouvement = ''E''';

BEGIN
	SELECT INTO _depot_ depot_pr FROM yvs_base_article_depot WHERE article = article_ AND depot = depot_;
	IF(COALESCE(_depot_,0)<=0) THEN
		SELECT INTO _depot_ depot FROM yvs_base_article_depot WHERE article = article_ AND default_pr IS TRUE;
	END IF;
	_depot_ = COALESCE(_depot_, depot_);
	query_ = query_ || ' AND m.article = '||COALESCE(article_, 0)||' AND m.date_doc <= '||QUOTE_LITERAL(COALESCE(date_, CURRENT_DATE));
	IF(COALESCE(_depot_, 0) > 0)THEN
		query_ = query_ || ' AND m.depot = '||_depot_;
	END IF;
	IF(COALESCE(tranche_, 0) > 0)THEN
		query_ = query_ || ' AND m.tranche = '||tranche_;
	END IF;
	IF(COALESCE(unite_, 0) > 0)THEN
		query_ = query_ || ' AND m.conditionnement = '||unite_;
	END IF;
	IF(COALESCE(current_, 0) > 0)THEN
		query_ = query_ || ' AND m.id != '||current_;
	END IF;
	query_ = query_ || ' ORDER BY m.date_doc DESC LIMIT 1';
	EXECUTE query_ INTO pr_;
	IF(pr_ IS NULL OR pr_ <=0)THEN
		pr_ = get_pua(article_, 0, 0, unite_);
	END IF;
	RETURN pr_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint) IS 'retourne le prix de revient d'' article';
