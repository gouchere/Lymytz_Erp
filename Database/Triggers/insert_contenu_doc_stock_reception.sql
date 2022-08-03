-- Function: insert_contenu_doc_stock_reception()
-- DROP FUNCTION insert_contenu_doc_stock_reception();
CREATE OR REPLACE FUNCTION insert_contenu_doc_stock_reception()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	contenu_ record;
	tranche_ bigint;
	result_ boolean default false;
	arts_ record;
	ligne_ record;
	
	mouv_ record;
	last_pr_ double precision default 0;
	
	ACTION_  CHARACTER VARYING;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	CASE ACTION_
		WHEN 'DELETE' THEN
			DELETE FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME;
			RETURN OLD;
		WHEN 'INSERT' THEN
			IF(EXEC_) THEN
				select into contenu_ * from yvs_com_contenu_doc_stock where id = NEW.contenu;
				select into doc_ * from yvs_com_doc_stocks where id = contenu_.doc_stock;
				select into tranche_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
				
				result_ = (select valorisation_stock(contenu_.article, contenu_.conditionnement_entree, doc_.destination, tranche_, NEW.quantite, contenu_.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_reception));
			END IF;
			RETURN NEW;
		WHEN 'UPDATE' THEN 
			IF(EXEC_) THEN
				select into contenu_ * from yvs_com_contenu_doc_stock where id = NEW.contenu;
				select into doc_ * from yvs_com_doc_stocks where id = contenu_.doc_stock;
				select into tranche_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = contenu_.article;
				--Insertion mouvement stock
				if(doc_.destination is not null)then			
					select into mouv_ id, cout_stock from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and mouvement = 'E' ORDER BY id;
					if(mouv_ is not null)then
						if(arts_.methode_val = 'FIFO')then
							delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'E';
							result_ = (select valorisation_stock(contenu_.article, contenu_.conditionnement_entree, doc_.destination, tranche_, NEW.quantite, contenu_.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_reception));
						else
							-- Recherche de la derniere entree de l'article dans le dépot
							last_pr_= COALESCE(get_pr(contenu_.article, doc_.destination, 0, NEW.date_reception, contenu_.conditionnement_entree, mouv_.id), 0);
							PERFORM insert_mouvement_stock(contenu_.article, doc_.destination, tranche_, NEW.quantite, contenu_.prix_entree, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_reception, last_pr_, mouv_.id);
							DELETE FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND mouvement = 'E' AND parent IS NULL AND id != mouv_.id;
						end if;
					else
						result_ = (select valorisation_stock(contenu_.article, contenu_.conditionnement_entree, doc_.destination, tranche_, NEW.quantite, contenu_.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_reception));
					end if;	
				end if;
				return new;
			END IF;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_stock_reception()
  OWNER TO postgres;
