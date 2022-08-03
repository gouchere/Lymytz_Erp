-- Function: action_on_all_tables_maj()
-- DROP FUNCTION action_on_all_tables_maj();
CREATE OR REPLACE FUNCTION action_on_all_tables_maj()
  RETURNS trigger AS
$BODY$    
DECLARE
	id_ bigint;
	author_ bigint;
	current_ bigint;
	serveur_ bigint;
	
	date_update_ timestamp default current_timestamp;
	
	action_ character varying;
	search_colonne character varying;	
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		id_ = NEW.id;
	ELSE
		id_ = OLD.id;
	END IF;
	search_colonne := (SELECT column_name FROM information_schema.columns WHERE table_name = TG_TABLE_NAME AND column_name = 'author');
	IF(COALESCE(search_colonne, '') NOT IN ('', ' '))THEN
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			author_ = NEW.author;
		ELSE
			author_ = OLD.author;
		END IF;
	END IF;
	search_colonne := (SELECT column_name FROM information_schema.columns WHERE table_name = TG_TABLE_NAME AND column_name = 'date_update');
	IF(COALESCE(search_colonne, '') NOT IN ('', ' '))THEN
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			date_update_ = NEW.date_update;
		ELSE
			date_update_ = OLD.date_update;
		END IF;
	END IF;
	serveur_ := (SELECT y.id FROM yvs_synchro_serveurs y WHERE y.adresse_ip IN ('localhost', '127.0.0.1') LIMIT 1);
	IF(COALESCE(serveur_, 0) < 1)THEN
		INSERT INTO yvs_synchro_serveurs(nom_serveur, adresse_ip, actif) VALUES('localhost', '127.0.0.1', false);
		serveur_ := (SELECT y.id FROM yvs_synchro_serveurs y WHERE y.adresse_ip IN ('localhost', '127.0.0.1') LIMIT 1);
	END IF;
	IF(action_ = 'UPDATE')THEN
		current_ := (SELECT y.id FROM yvs_synchro_listen_table y WHERE y.id_source = id_ AND y.name_table = TG_TABLE_NAME AND action_name= 'UPDATE' ORDER BY y.id DESC LIMIT 1);
		IF(COALESCE(current_, 0) > 0)THEN
			DELETE FROM yvs_synchro_data_synchro WHERE id_listen = current_;
			UPDATE yvs_synchro_listen_table SET date_save = date_update_, to_listen = TRUE, author = author_ WHERE id = current_;
		ELSE
			INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, groupe_table, to_listen, action_name, author, serveur) VALUES (TG_TABLE_NAME, id_, date_update_, '', true, action_, author_, serveur_);	
		END IF;
	ELSE
		INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, groupe_table, to_listen, action_name, author, serveur) VALUES (TG_TABLE_NAME, id_, date_update_, '', true, action_, author_, serveur_);		
	END IF;
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		RETURN NEW;
	ELSE
		RETURN OLD;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_all_tables_maj()
  OWNER TO postgres;
