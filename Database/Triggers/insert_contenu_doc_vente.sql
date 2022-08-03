-- Function: insert_contenu_doc_vente()
-- DROP FUNCTION insert_contenu_doc_vente();
CREATE OR REPLACE FUNCTION insert_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
	DECLARE
		doc_ record;
		arts_ record;
		dep_ record;
		mouv_ record;	
		ligne_ record;
		last_pr_ double precision default 0;
		
		date_ date;
		result_ boolean default false;
		
		action_  CHARACTER VARYING;
		EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
		EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
		run_ BOOLEAN;
	BEGIN
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			IF(NEW.execute_trigger='OK') THEN
				run_=false;
			ELSE
				run_=true;
			END IF;
		ELSE
			IF(OLD.execute_trigger='OK') THEN
				run_=false;
			ELSE
				run_=true;
			END IF;
		END IF;
		CASE action_
		WHEN 'DELETE' THEN					
			DELETE from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
			IF(run_) THEN
				select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer, date_livraison 
					FROM yvs_com_doc_ventes WHERE id = OLD.doc_vente;
				IF((doc_.type_doc='FV' OR doc_.type_doc='BCV') AND doc_.statut='V')  THEN
					PERFORM equilibre_vente(doc_.id);
				END IF;
			END IF;
			return new;
		WHEN 'INSERT' THEN
			IF(run_) THEN
				select into doc_ id, type_doc, statut, entete_doc, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;
				select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c on e.creneau = c.id 
					inner join yvs_com_creneau_depot h on c.creneau_depot = h.id where e.id = doc_.entete_doc;
				if((doc_.type_doc = 'BRV' or doc_.type_doc = 'BRL' or doc_.type_doc = 'BLV') AND doc_.statut = 'V') then
					--Insertion mouvement stock
					if(doc_.depot_livrer is not null) then
						if(doc_.type_doc = 'BLV')then
							result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
						else
							result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
						end if;
					end if;
				end if;
			END IF;
			RETURN NEW;
		WHEN 'UPDATE' THEN	
			IF(run_) THEN
				select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;		
			END IF;
			IF(run_) THEN				
				IF((doc_.type_doc = 'BRV' or doc_.type_doc = 'BRL' or doc_.type_doc = 'BLV') AND doc_.statut = 'V') then
					select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
					select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
						on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id where e.id = doc_.entete_doc;
					--Insertion mouvement stock
					if(doc_.depot_livrer is not null)then
						select into mouv_ id, cout_stock from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article;
						if(mouv_.id is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article;
								if(doc_.type_doc = 'BLV')then
									result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
								else
									result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
								end if;
							else
								-- Recherche de la derniere entree de l'article dans le dépot
								last_pr_= COALESCE(get_pr(NEW.article, doc_.depot_livrer, 0, doc_.date_livraison, NEW.conditionnement, mouv_.id), 0);
								IF(doc_.type_doc = 'BLV') THEN
									PERFORM insert_mouvement_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, NEW.prix, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison, last_pr_, mouv_.id);
								ELSE
									PERFORM insert_mouvement_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, NEW.prix, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison, last_pr_, mouv_.id);
								END IF;
								DELETE FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL AND id != mouv_.id;
							end if;
						else
							if(doc_.type_doc = 'BLV')then
								result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
							else
								result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
							end if;
						end if;	
					end if;
				END IF;
			END IF;
			IF(run_)THEN
				IF(doc_.type_doc = 'FV') THEN
					--Mettre a jour les statuts
					PERFORM equilibre_vente(doc_.id);
				END IF;
			END IF;
			RETURN NEW;
		END CASE;
	END;
	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_vente()
  OWNER TO postgres;
