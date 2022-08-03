-- Function: insert_article_rayon()

-- DROP FUNCTION insert_article_rayon();

CREATE OR REPLACE FUNCTION insert_article_approvissionnement()
  RETURNS trigger AS
$BODY$    
BEGIN
    -- supprimer toutes les lignes sans fiche
    delete from yvs_com_article_approvisionnement where fiche is null;    
    if(NEW.fiche is null)then
		NEW = null;
    end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_article_approvissionnement()
  OWNER TO postgres;

  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_com_article_approvisionnement
  FOR EACH ROW
  EXECUTE PROCEDURE insert_article_approvissionnement();