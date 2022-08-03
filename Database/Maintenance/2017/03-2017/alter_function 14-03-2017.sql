CREATE OR REPLACE FUNCTION connection_insert()
  RETURNS trigger AS
$BODY$
DECLARE
	id_ bigint;
    
BEGIN
	SELECT INTO id_ id FROM yvs_connection_historique WHERE users = NEW.users AND agence = NEW.agence AND adresse_ip = NEW.adresse_ip AND debut_navigation::date = NEW.debut_navigation::date;
	IF(id_ IS NULL OR id_ < 1)THEN
		INSERT INTO yvs_connection_historique(id, adresse_ip, adresse_mac, users, date_connexion, agence, author, id_session, debut_navigation)
		VALUES (NEW.id, NEW.adresse_ip, NEW.adresse_mac, NEW.users, NEW.date_connexion, NEW.agence, NEW.author, NEW.id_session, NEW.debut_navigation);
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION connection_insert()
  OWNER TO postgres;
  
CREATE OR REPLACE FUNCTION connection_page_insert()
  RETURNS trigger AS
$BODY$
DECLARE
	id_ bigint;
	record_ record;
    
BEGIN
	SELECT INTO record_ * FROM yvs_connection WHERE id = NEW.auteur_page;
	IF(record_ IS NOT NULL AND record_.id > 0)THEN
		SELECT INTO id_ id FROM yvs_connection_historique WHERE users = record_.users AND agence = record_.agence AND adresse_ip = record_.adresse_ip AND debut_navigation::date = record_.debut_navigation::date;
		IF(id_ IS NOT NULL AND id_ > 0)THEN
			INSERT INTO yvs_connection_pages_historique(id, titre_page, auteur_page, date_ouverture, author)
			VALUES (NEW.id, NEW.titre_page, id_, NEW.date_ouverture, NEW.author);
		END IF;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION connection_page_insert()
  OWNER TO postgres;