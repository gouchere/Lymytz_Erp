-- Function: insert_ration()
-- DROP FUNCTION insert_ration();
CREATE OR REPLACE FUNCTION insert_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	contenu_ record;
	tranche_ bigint;
	result_ boolean default false;
	arts_ record;
	ligne_ record;
	last_pr_ double precision default 0;
	
	mouv_ record;
	prix_ double precision;
	
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
				select into doc_ id, statut, depot from yvs_com_doc_ration WHERE id = NEW.doc_ration;
				SELECT INTO tranche_ cd.tranche FROM yvs_com_doc_ration d INNER JOIN yvs_com_creneau_depot cd ON cd.id=d.creneau_horaire WHERE d.id=NEW.doc_ration;
				--Insertion mouvement stock
				if(doc_.statut = 'V')then
					if(doc_.depot is not null)then
						result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, tranche_, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
					end if;
				end if;
			END IF;
			RETURN NEW;
		WHEN 'UPDATE' THEN 
			IF(EXEC_) THEN
				select into doc_ id, statut, depot from yvs_com_doc_ration where id = NEW.doc_ration;
				--Insertion mouvement stock
				if(doc_.statut = 'V')then
					select into arts_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id WHERE a.id = NEW.article and c.id = NEW.conditionnement;
					if(doc_.depot is not null)then
						select into mouv_ id, cout_stock from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
						SELECT INTO tranche_ cd.tranche FROM yvs_com_doc_ration d INNER JOIN yvs_com_creneau_depot cd ON cd.id=d.creneau_horaire WHERE d.id=NEW.doc_ration;
						if(mouv_.id is not null)then
							prix_=get_pr(NEW.article, doc_.depot,0, NEW.date_ration, NEW.conditionnement);
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
								result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, tranche_, NEW.quantite, arts_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
							else
								PERFORM insert_mouvement_stock(NEW.article, doc_.depot, tranche_, NEW.quantite, arts_.prix, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration, prix_, mouv_.id);
							end if;
						else
							result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, tranche_, NEW.quantite, arts_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
						end if;	
					end if;
				end if;
			END IF;
			RETURN NEW;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_ration()
  OWNER TO postgres;
