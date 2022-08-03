-- Function: insert_declaration_production()
-- DROP FUNCTION insert_declaration_production();
CREATE OR REPLACE FUNCTION insert_declaration_production()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	session_ record;
	mouv_ record;
	arts_ record;
	result_ boolean default false;
	last_pr_ double precision default 0;
	
	ACTION_  CHARACTER VARYING;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	CASE ACTION_
		WHEN 'DELETE' THEN
			delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
			RETURN OLD;
		WHEN 'INSERT' THEN
			IF(EXEC_) THEN
				if(NEW.statut = 'V') then
					select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre;
					SELECT INTO session_ sp.* FROM yvs_prod_session_of so INNER JOIN yvs_prod_session_prod sp ON so.session_prod = sp.id WHERE so.id=NEW.session_of;
					--Insertion mouvement stock
					result_ = (select valorisation_stock(ordre_.article,NEW.conditionnement, session_.depot, session_.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
				end if;
			END IF;
			RETURN NEW;
		WHEN 'UPDATE' THEN 
			IF(EXEC_) THEN
				if(NEW.statut = 'V') then
					select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre;
					SELECT INTO session_ sp.* FROM yvs_prod_session_of so INNER JOIN yvs_prod_session_prod sp ON so.session_prod = sp.id WHERE so.id=NEW.session_of;
					--Insertion mouvement stock
					select into mouv_ id, cout_stock from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
					if(mouv_.id is not null)then
						select into arts_ * from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where c.id = NEW.conditionnement;
						if(arts_.methode_val = 'FIFO')then
							delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
							result_ = (select valorisation_stock(ordre_.article,NEW.conditionnement, session_.depot, session_.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
						else
							RAISE NOTICE 'session_.tranche : %',session_.tranche;
							last_pr_= COALESCE(get_pr(ordre_.article, session_.depot, 0, session_.date_session, NEW.conditionnement, mouv_.id), 0);
							PERFORM insert_mouvement_stock(ordre_.article, session_.depot, session_.tranche, NEW.quantite, NEW.cout_production, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session, last_pr_, mouv_.id);
						end if;
					else
						result_ = (select valorisation_stock(ordre_.article,NEW.conditionnement, session_.depot, session_.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
					end if;	
				elsif(NEW.statut != 'V')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				end if;
			END IF;
			RETURN NEW;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_declaration_production()
  OWNER TO postgres;
