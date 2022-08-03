-- Function: connection_insert()

-- DROP FUNCTION connection_insert();

CREATE OR REPLACE FUNCTION connection_insert()
  RETURNS trigger AS
$BODY$
DECLARE
    
BEGIN
	INSERT INTO yvs_connection_historique(id, adresse_ip, adresse_mac, users, date_connexion, agence, author, id_session, debut_navigation)
	VALUES (NEW.id, NEW.adresse_ip, NEW.adresse_mac, NEW.users, NEW.date_connexion, NEW.agence, NEW.author, NEW.id_session, NEW.debut_navigation);
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION connection_insert()
  OWNER TO postgres;
  
  
CREATE TRIGGER connection_insert
  AFTER INSERT
  ON yvs_connection
  FOR EACH ROW
  EXECUTE PROCEDURE connection_insert();

  -- Function: connection_page_insert()

-- DROP FUNCTION connection_page_insert();

CREATE OR REPLACE FUNCTION connection_page_insert()
  RETURNS trigger AS
$BODY$
DECLARE
	id_ bigint;
    
BEGIN
	SELECT INTO id_ id FROM yvs_connection_historique WHERE id = NEW.auteur_page;
	IF(id_ IS NOT NULL and id_ > 0)THEN
		INSERT INTO yvs_connection_pages_historique(id, titre_page, auteur_page, date_ouverture, author)
		VALUES (NEW.id, NEW.titre_page, NEW.auteur_page, NEW.date_ouverture, NEW.author);
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION connection_page_insert()
  OWNER TO postgres;
  
  
CREATE TRIGGER connection_page_insert
  AFTER INSERT
  ON yvs_connection_pages
  FOR EACH ROW
  EXECUTE PROCEDURE connection_page_insert();