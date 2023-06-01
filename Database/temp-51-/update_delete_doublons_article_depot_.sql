-- FUNCTION: public.update_delete_doublons_article_depot_(bigint)
-- DROP FUNCTION IF EXISTS public.update_delete_doublons_article_depot_(bigint);
CREATE OR REPLACE FUNCTION public.update_delete_doublons_article_depot_(societe_ bigint)
RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$
   
DECLARE	
	ligne_ RECORD;
BEGIN
	FOR ligne_ IN SELECT y.id as article, d.id as depot, MAX(ad.id) as id, COUNT(ad.id) as count
        FROM yvs_base_articles y INNER JOIN yvs_base_article_depot ad ON ad.article = y.id 
        INNER JOIN yvs_base_depots d ON ad.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id 
        WHERE a.societe = societe_ GROUP BY y.id, d.id HAVING COUNT(ad.id) > 1
	LOOP
		DELETE FROM yvs_base_article_depot WHERE article = ligne_.article AND depot = ligne_.depot AND id != ligne_.id;
	END LOOP;
	RETURN TRUE;
END;
$BODY$;

ALTER FUNCTION public.update_delete_doublons_article_depot_(bigint)
    OWNER TO postgres;
