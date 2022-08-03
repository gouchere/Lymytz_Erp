-- Function: insert_contenu_doc_achat()
-- DROP FUNCTION insert_contenu_doc_achat();
CREATE OR REPLACE FUNCTION insert_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	mouv_ record;	
	ligne_ record;
	last_pr_ double precision default 0;
	
	date_ date;
	result_ boolean default false;
	
	ACTION_  CHARACTER VARYING;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	CASE ACTION_
		WHEN 'DELETE' THEN
			DELETE FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME;
			RETURN OLD;
		WHEN 'INSERT' THEN
			IF(EXEC_) THEN
				SELECT INTO doc_ id, type_doc, date_livraison, date_doc, depot_reception as depot, tranche, statut FROM yvs_com_doc_achats WHERE id = NEW.doc_achat;
				IF(doc_.type_doc = 'FRA' OR doc_.type_doc = 'BLA') THEN
					--Insertion mouvement stock
					IF(doc_.statut = 'V') THEN
						date_ = COALESCE(doc_.date_livraison, doc_.date_doc) ;	
						IF( doc_.type_doc = 'BLA') THEN
							result_ = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu, NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'E', date_));
						ELSE
							result_ = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu, NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'S', date_));
						END IF;
					END IF;
				END IF;
			END IF;
			RETURN NEW;
		WHEN 'UPDATE' THEN	
			IF(EXEC_) THEN
				SELECT INTO arts_ a.id, a.methode_val FROM yvs_base_articles a WHERE a.id = NEW.article;
				SELECT INTO doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, date_livraison FROM yvs_com_doc_achats WHERE id = OLD.doc_achat;
				IF(doc_.type_doc = 'BRA' OR doc_.type_doc = 'BLA') THEN
					--Insertion mouvement stock
					SELECT INTO mouv_ id, cout_stock FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME;
					IF(doc_.statut = 'V') THEN
						date_ = COALESCE(doc_.date_livraison, doc_.date_doc);					
						IF(mouv_.id IS NOT NULL) THEN
							IF(arts_.methode_val = 'FIFO') THEN
								delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'E';
								IF(doc_.type_doc = 'BLA') THEN
									result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'E', date_));
								ELSE
									result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'S', date_));
								END IF;
							ELSE	
								-- Recherche de la derniere entree de l'article dans le dépot
								last_pr_= COALESCE(get_pr(NEW.article, doc_.depot, 0, date_, NEW.conditionnement, mouv_.id), 0);
								IF(doc_.type_doc = 'BLA') THEN
									PERFORM insert_mouvement_stock(NEW.article, doc_.depot, doc_.tranche, NEW.quantite_recu, NEW.pua_recu, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'E', date_, last_pr_, mouv_.id);
								ELSE
									PERFORM insert_mouvement_stock(NEW.article, doc_.depot, doc_.tranche, NEW.quantite_recu, NEW.pua_recu, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'S', date_, last_pr_, mouv_.id);
								END IF;
								DELETE FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL AND id != mouv_.id;
							END IF;
						ELSE
							IF(doc_.type_doc = 'BLA')THEN
								result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'E', date_));
							ELSE
								result_ = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'S', date_));
							END IF;
						END IF;
					ELSE
						DELETE FROM yvs_base_mouvement_stock WHERE id = mouv_.id;
					END IF;
				END IF;
			END IF;		
		    RETURN NEW;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_achat()
  OWNER TO postgres;
