-- Function: connection_insert()
-- DROP FUNCTION connection_insert();
CREATE OR REPLACE FUNCTION connection_insert()
  RETURNS trigger AS
$BODY$
DECLARE
	id_ bigint;
    
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	IF(EXEC_) THEN 	
		SELECT INTO id_ id FROM yvs_connection_historique WHERE author = NEW.author AND adresse_ip = NEW.adresse_ip AND id_session = NEW.id_session;
		IF(id_ IS NULL OR id_ < 1)THEN
			INSERT INTO yvs_connection_historique(adresse_ip, adresse_mac, users, date_connexion, agence, author, id_session, debut_navigation)
				VALUES (NEW.adresse_ip, NEW.adresse_mac, NEW.users, NEW.date_connexion, NEW.agence, NEW.author, NEW.id_session, NEW.debut_navigation);
		END IF;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION connection_insert()
  OWNER TO postgres;
