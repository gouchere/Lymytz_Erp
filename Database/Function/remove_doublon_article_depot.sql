-- Function: remove_doublon_article_depot(bigint)
-- DROP FUNCTION remove_doublon_article_depot(bigint);
CREATE OR REPLACE FUNCTION remove_doublon_article_depot(societe_ bigint)
  RETURNS boolean AS
$BODY$   
DECLARE 
	record_ RECORD;
BEGIN
	FOR record_ IN SELECT MIN(y.id) AS id, y.article, y.depot, COUNT(y.id) AS count FROM yvs_base_article_depot y INNER JOIN yvs_base_depots d ON y.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id 
		WHERE a.societe = societe_ GROUP BY y.article, y.depot HAVING COUNT(y.id) > 1 ORDER BY y.depot
	LOOP
		RAISE NOTICE 'LIGNE : %, DEPOT : %, ARTICLE : % (%) ', record_.id, record_.depot, record_.article, record_.count;
		DELETE FROM yvs_base_article_depot WHERE article = record_.article AND depot = record_.depot AND id != record_.id ;
	END LOOP;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION remove_doublon_article_depot(bigint)
  OWNER TO postgres;
