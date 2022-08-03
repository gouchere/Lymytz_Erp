-- Function: base_article_vente_inactif(bigint)

-- DROP FUNCTION base_article_vente_inactif(bigint);

CREATE OR REPLACE FUNCTION base_article_vente_inactif(IN societe_ bigint)
  RETURNS TABLE(article bigint, last_date date) AS
$BODY$
DECLARE 
	data_ RECORD;
	
	last_date_ DATE;

	duree_ INTEGER DEFAULT 0;
BEGIN
	--DROP TABLE IF EXISTS table_article_vente_inactif;
	CREATE TEMP TABLE IF NOT EXISTS table_article_vente_inactif(_article BIGINT, _last_date DATE); 
	DELETE FROM table_article_vente_inactif;
	IF(COALESCE(societe_, 0) > 0)THEN
		duree_ = COALESCE((SELECT duree_inactiv_article FROM yvs_base_parametre WHERE societe = societe_), 30);
		FOR data_ IN SELECT y.id FROM yvs_base_articles y INNER JOIN yvs_base_famille_article f ON y.famille = f.id WHERE y.categorie IN ('PF','MARCHANDISE') AND f.societe = societe_
		LOOP
			-- SELECT INTO last_date_ e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_doc_ventes d ON d.entete_doc = e.id INNER JOIN yvs_com_contenu_doc_vente c ON c.doc_vente = d.id
			SELECT INTO last_date_ m.date_doc FROM yvs_base_mouvement_stock m 
				WHERE m.article = data_.id ORDER BY m.date_doc DESC LIMIT 1;
			IF(COALESCE(last_date_, '01-01-2000') < CURRENT_DATE - duree_)THEN
				INSERT INTO table_article_vente_inactif VALUES(data_.id, last_date_);
			END IF;
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_article_vente_inactif;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION base_article_vente_inactif(bigint)
  OWNER TO postgres;
