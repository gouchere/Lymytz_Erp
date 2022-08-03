-- Function: audit_tiers(bigint)

-- DROP FUNCTION audit_tiers(bigint);

CREATE OR REPLACE FUNCTION alt_migration_data_article_production()
  RETURNS BOOLEAN AS
$BODY$
DECLARE 
	ordre_ RECORD;
    
BEGIN
	FOR ordre_ IN SELECT * FROM yvs_prod_ordre_fabrication o 
		LOOP 
			INSERT INTO yvs_prod_ordre_articles(article, ordre, quantite, nomenclature, gamme, author, date_save, date_update)
			VALUES (ordre_.article, ordre_.id, ordre_.quantite_fabrique, ordre_.nomenclature, ordre_.gamme, ordre_.author, ordre_.date_save, ordre_.date_update);

		END LOOP;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alt_migration_data_article_production()
  OWNER TO postgres;

  
 CREATE OR REPLACE FUNCTION insert_article_gamme()
 RETURNS BOOLEAN AS
$BODY$    
DECLARE
	ordre_ RECORD;
BEGIN
	FOR ordre_ IN SELECT * FROM yvs_prod_gamme_article g
	LOOP
		INSERT INTO yvs_prod_gamme_article_article(article, gamme, author, date_save,date_update, actif)
		VALUES(ordre_.article, ordre_.id, ordre_.author, ordre_.date_save, ordre_.date_save, true);
	END LOOP;
	RETURN TRUE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_article_gamme()
  OWNER TO postgres;
