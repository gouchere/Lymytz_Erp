-- Function: valorisation_stock(bigint, bigint, bigint, double precision, double precision, character varying, bigint, character varying, date)
-- DROP FUNCTION valorisation_stock(bigint, bigint, bigint, double precision, double precision, character varying, bigint, character varying, date);
CREATE OR REPLACE FUNCTION valorisation_stock(article_ bigint, conditionnement_ bigint, depot_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE 

BEGIN
	return valorisation_stock(article_, conditionnement_, depot_, 0, quantite_ , cout_ , tableexterne_, idexterne_, mouvement_, date_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock(bigint, bigint, bigint, double precision, double precision, character varying, bigint, character varying, date)
  OWNER TO postgres;
  
  
-- Function: dblink(text)
-- DROP FUNCTION dblink(text);
CREATE OR REPLACE FUNCTION dblink(text)
  RETURNS SETOF record AS
'$libdir/dblink', 'dblink_record'
  LANGUAGE c VOLATILE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION dblink(text)
  OWNER TO postgres;
  
  
-- Function: dblink_disconnect(text)
-- DROP FUNCTION dblink_disconnect(text);
CREATE OR REPLACE FUNCTION dblink_disconnect(text)
  RETURNS text AS
'$libdir/dblink', 'dblink_disconnect'
  LANGUAGE c VOLATILE STRICT
  COST 1;
ALTER FUNCTION dblink_disconnect(text)
  OWNER TO postgres;
  
  
-- Function: get_config(text)
-- DROP FUNCTION get_config(text);
CREATE OR REPLACE FUNCTION get_config(variable text)
  RETURNS boolean AS
$BODY$
DECLARE 
	"value" BOOLEAN DEFAULT TRUE;   
BEGIN 	
	BEGIN
		"value" := COALESCE((SELECT current_setting('myapp.'||variable)), 'true')::boolean;
		EXCEPTION WHEN OTHERS THEN RETURN TRUE;
	END;
	RETURN "value";
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_config(text)
  OWNER TO postgres;

  
  
-- Function: update_contenu_doc_vente()
-- DROP FUNCTION update_contenu_doc_vente();
CREATE OR REPLACE FUNCTION update_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	dep_ record;
	mouv_ bigint;
	arts_ record;
	ligne_ record;
	result boolean default false;
BEGIN
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;		
	IF(doc_.type_doc = 'BRV' or doc_.type_doc = 'BLV') then
			select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
			select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
				on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
				where e.id = doc_.entete_doc;
		--Insertion mouvement stock
		RAISE NOTICE 'doc_.type_doc : %',doc_.type_doc;
		if(doc_.statut = 'V')then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article;
						if(doc_.type_doc = 'BLV')then
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
						else
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = (NEW.quantite), cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot, calcul_pr = NEW.calcul_pr, date_doc = doc_.date_livraison, tranche = doc_.tranche_livrer where id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					if(doc_.type_doc = 'BLV')then
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
					else
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
					end if;
				end if;	
			end if;
		end if;
	ELSE 
		--Mettre à jour les statuts
		IF(doc_.statut='V')THEN
			PERFORM equilibre_vente(doc_.id);
		END IF;
	END IF;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_vente()
  OWNER TO postgres;


-- Function: delete_contenu_doc_vente()
-- DROP FUNCTION delete_contenu_doc_vente();
CREATE OR REPLACE FUNCTION delete_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
BEGIN
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer, date_livraison 
	FROM yvs_com_doc_ventes WHERE id = OLD.doc_vente;		
	delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		IF((doc_.type_doc='FV' OR doc_.type_doc='BCV') AND doc_.statut='V')  THEN
			PERFORM equilibre_vente(doc_.id);
		END IF;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_contenu_doc_vente()
  OWNER TO postgres;
  
  
-- Function: com_action_on_ecart_vente()
-- DROP FUNCTION com_action_on_ecart_vente();
CREATE OR REPLACE FUNCTION com_action_on_ecart_vente()
  RETURNS trigger AS
$BODY$    
DECLARE	
	action_ character varying;
	EXECUTE_TRIGGER BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(EXECUTE_TRIGGER)THEN
		RAISE NOTICE 'ACTION : %', action_;
		IF (action_='UPDATE') THEN 
			UPDATE yvs_compta_caisse_piece_ecart_vente SET date_update = current_timestamp WHERE piece = NEW.id;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_action_on_ecart_vente()
  OWNER TO postgres;



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
		current_ := (SELECT y.id FROM yvs_synchro_listen_table y WHERE y.id_source = id_ AND y.name_table = TG_TABLE_NAME ORDER BY y.id DESC LIMIT 1);
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

