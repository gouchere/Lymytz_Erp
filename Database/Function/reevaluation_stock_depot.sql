-- Function: reevaluation_stock_depot(bigint, date, date)

-- DROP FUNCTION reevaluation_stock_depot(bigint, date, date);

CREATE OR REPLACE FUNCTION reevaluation_stock_depot(depot_ bigint, date_debut date, date_fin date)
  RETURNS boolean AS
$BODY$
DECLARE 	
	article_ bigint default 0;
	result_ boolean default false;
BEGIN
	for article_ in select id from yvs_base_article_depot where depot = depot_
	loop
		result_ = (select reevaluation_stock_article(article_, depot_, date_debut, date_fin));
	end loop;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION reevaluation_stock_depot(bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION reevaluation_stock_depot(bigint, date, date) IS 'fonction de réevaluation des stocks d''un depot';
