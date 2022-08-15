DELETE FROM yvs_base_article_point WHERE id NOT IN (SELECT DISTINCT article FROM yvs_base_conditionnement_point);
UPDATE yvs_base_conditionnement c SET actif = a.actif FROM yvs_base_conditionnement y INNER JOIN yvs_base_articles a ON y.article = a.id WHERE y.id = c.id AND c.actif IS NULL;
UPDATE yvs_base_conditionnement c SET actif = FALSE FROM yvs_base_conditionnement y INNER JOIN yvs_base_articles a ON y.article = a.id WHERE y.id = c.id AND a.actif IS FALSE AND c.actif IS TRUE;
SELECT alter_action_colonne_key('yvs_base_conditionnement', 'yvs_prod_composant_op', true, true);
DELETE FROM yvs_base_conditionnement WHERE article IS NULL;

SELECT a.id, a.ref_art, c.id FROM yvs_base_articles a INNER JOIN yvs_base_conditionnement c ON c.article = a.id INNER JOIN yvs_base_conditionnement_point cp ON cp.conditionnement = c.id 
GROUP BY a.id, c.id HAVING COUNT(cp.id) > 1;
