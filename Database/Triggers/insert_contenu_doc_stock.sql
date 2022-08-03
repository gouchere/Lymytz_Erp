-- Function: insert_contenu_doc_stock()
-- DROP FUNCTION insert_contenu_doc_stock();
CREATE OR REPLACE FUNCTION insert_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	trancheD_ bigint;
	trancheS_ bigint;
	result_ boolean default false;
	
	entree_ record;
	sortie_ record;
	ligne_ record;
	
	mouv_ record;
	last_pr_ double precision default 0;
	
	ACTION_  CHARACTER VARYING;
	--EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
	run_ BOOLEAN;
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE  
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
	CASE ACTION_
		WHEN 'DELETE' THEN
			DELETE FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME;
			RETURN OLD;
		WHEN 'INSERT' THEN
			IF(run_) THEN
				SELECT INTO doc_ * FROM yvs_com_doc_stocks WHERE id = NEW.doc_stock;
				SELECT INTO trancheD_ tranche FROM yvs_com_creneau_depot WHERE id = doc_.creneau_destinataire;
				SELECT INTO trancheS_ tranche FROM yvs_com_creneau_depot WHERE id = doc_.creneau_source;
				
				IF(doc_.statut = 'V' or doc_.statut = 'U' or doc_.statut = 'R') THEN
					IF(doc_.type_doc = 'FT') THEN
						--Insertion mouvement stock		
						IF(doc_.source IS NOT NULL AND (doc_.statut = 'U' or doc_.statut = 'V' or doc_.statut = 'R'))THEN
							result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						END IF;
					ELSIF(doc_.type_doc = 'SS') THEN
						IF(doc_.source IS NOT NULL)THEN
							result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						END IF;
					ELSIF(doc_.type_doc = 'ES') THEN
						IF(doc_.destination IS NOT NULL) THEN
							result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
						END IF;
					ELSIF(doc_.type_doc = 'IN') THEN
						IF(doc_.source IS NOT NULL)THEN
							IF(NEW.quantite>0)THEN
								result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
							ELSIF(NEW.quantite<0)THEN
								result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
							END IF;
						END IF;
					ELSIF(doc_.type_doc = 'TR') THEN
						IF(doc_.source IS NOT NULL)THEN
							result_ = (SELECT valorisation_stock(NEW.article, NEW.conditionnement,doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
							result_ = (SELECT valorisation_stock(NEW.article, NEW.conditionnement_entree,doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
						END IF;
					END IF;
				END IF;
			END IF;
			RETURN NEW;
		WHEN 'UPDATE' THEN 
			IF(run_) THEN
				SELECT INTO doc_ * FROM yvs_com_doc_stocks WHERE id = NEW.doc_stock;
				SELECT INTO trancheD_ tranche FROM yvs_com_creneau_depot WHERE id = doc_.creneau_destinataire;
				SELECT INTO trancheS_ tranche FROM yvs_com_creneau_depot WHERE id = doc_.creneau_source;
				SELECT INTO arts_ a.id, a.methode_val FROM yvs_base_articles a WHERE a.id = NEW.article;
				--Insertion mouvement stock
				IF(doc_.statut = 'V' or doc_.statut = 'U' or doc_.statut = 'R') THEN
					IF(doc_.type_doc = 'FT') THEN	
						IF(doc_.source IS NOT NULL AND (doc_.statut = 'U' or doc_.statut = 'V' or doc_.statut = 'R'))THEN
							SELECT INTO mouv_ id, cout_stock FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id AND table_externe = TG_TABLE_NAME AND depot = doc_.source AND mouvement = 'S' ORDER BY id;				
							IF(coalesce(mouv_.id, 0) > 0)THEN
								IF(arts_.methode_val = 'FIFO')THEN
									DELETE FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id AND table_externe = TG_TABLE_NAME AND mouvement = 'S';
									result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
								ELSE
									-- Recherche de la derniere entree de l'article dans le dépot
									last_pr_= COALESCE(get_pr(NEW.article, doc_.source, 0, doc_.date_doc, NEW.conditionnement, mouv_.id), 0);
									PERFORM insert_mouvement_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc, last_pr_, mouv_.id);
									DELETE FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND mouvement = 'S' AND parent IS NULL AND id != mouv_.id;
								END IF;
							ELSE
								result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
							END IF;	
							FOR ligne_ IN SELECT y.id, y.quantite, y.date_reception FROM yvs_com_contenu_doc_stock_reception y WHERE contenu = NEW.id
							LOOP
								DELETE FROM yvs_base_mouvement_stock WHERE id_externe = ligne_.id AND table_externe = 'yvs_com_contenu_doc_stock_reception';
								result_ = (SELECT valorisation_stock(NEW.article, NEW.conditionnement_entree, doc_.destination, trancheD_, ligne_.quantite, NEW.prix_entree, 'yvs_com_contenu_doc_stock_reception', ligne_.id, 'E', ligne_.date_reception));
							END LOOP;
						END IF;	
					ELSIF(doc_.type_doc = 'SS') THEN
						IF(doc_.source IS NOT NULL)THEN
							SELECT INTO mouv_ id, cout_stock FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id AND table_externe = TG_TABLE_NAME;
							IF(mouv_.id IS NOT NULL)THEN
								IF(arts_.methode_val = 'FIFO')THEN
									DELETE FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME AND depot = doc_.source AND article = NEW.article AND mouvement = 'S';
									result_ = (SELECT valorisation_stock(NEW.article, NEW.conditionnement,doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
								ELSE
									-- Recherche de la derniere entree de l'article dans le dépot
									last_pr_= COALESCE(get_pr(NEW.article, doc_.source, 0, doc_.date_doc, NEW.conditionnement, mouv_.id), 0);
									PERFORM insert_mouvement_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc, last_pr_, mouv_.id);
									DELETE FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL AND id != mouv_.id;
								END IF;
							ELSE
								result_ = (SELECT valorisation_stock(NEW.article, NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
							END IF;	
						END IF;
					ELSIF(doc_.type_doc = 'ES') THEN
						IF(doc_.destination IS NOT NULL)THEN
							SELECT INTO mouv_ id, cout_stock FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME AND depot = doc_.destination AND mouvement = 'E';
							IF(mouv_.id IS NOT NULL)THEN
								IF(arts_.methode_val = 'FIFO')THEN
									DELETE FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME AND depot = doc_.destination AND article = NEW.article AND mouvement = 'E';
									result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
								ELSE
									-- Recherche de la derniere entree de l'article dans le dépot
									last_pr_= COALESCE(get_pr(NEW.article, doc_.destination, 0, doc_.date_doc, NEW.conditionnement_entree, mouv_.id), 0);
									PERFORM insert_mouvement_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc, last_pr_, mouv_.id);
									DELETE FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL AND id != mouv_.id;
								END IF;
							ELSE
								result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
							END IF;	
						END IF;
					ELSIF(doc_.type_doc = 'IN') THEN
						IF(doc_.source IS NOT NULL)THEN
							SELECT INTO mouv_ id, cout_stock FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME AND depot = doc_.source AND article = NEW.article;
							IF(mouv_.id IS NOT NULL)THEN
								IF(arts_.methode_val = 'FIFO')THEN
									DELETE FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME AND depot = doc_.source AND article = NEW.article;
									IF(NEW.quantite>0)THEN
										result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
									ELSIF(NEW.quantite<0)THEN
										result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
									END IF;
								ELSE
									-- Recherche de la derniere entree de l'article dans le dépot
									IF(NEW.quantite>0)THEN
										last_pr_= COALESCE(get_pr(NEW.article, doc_.source, 0, doc_.date_doc, NEW.conditionnement, mouv_.id), 0);
										PERFORM insert_mouvement_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc, last_pr_, mouv_.id);
									ELSIF(NEW.quantite<0)THEN
										last_pr_= COALESCE(get_pr(NEW.article, doc_.source, 0, doc_.date_doc, NEW.conditionnement_entree, mouv_.id), 0);
										PERFORM insert_mouvement_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc, last_pr_, mouv_.id);
									END IF;
									DELETE FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL AND id != mouv_.id;
								END IF;
							ELSE
								IF(NEW.quantite>0)THEN
									result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
								ELSIF(NEW.quantite<0)THEN
									result_ = (SELECT valorisation_stock(NEW.article, NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
								END IF;
							END IF;	
						END IF;
					ELSIF(doc_.type_doc = 'TR') THEN
						IF(doc_.source IS NOT NULL)THEN
							SELECT INTO entree_ a.id, a.methode_val, c.prix FROM yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id WHERE a.id = NEW.article AND c.id = NEW.conditionnement;
							SELECT INTO sortie_ a.id, a.methode_val, c.prix FROM yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id WHERE a.id = NEW.article AND c.id = NEW.conditionnement_entree;

							SELECT INTO mouv_ id, cout_stock FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME AND depot = doc_.source AND article = NEW.article AND mouvement = 'E';
							IF(mouv_.id IS NOT NULL)THEN
								IF(entree_.methode_val = 'FIFO')THEN
									DELETE FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME AND depot = doc_.source AND article = NEW.article AND mouvement = 'E';
									result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement_entree,  doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
								ELSE
									last_pr_= COALESCE(get_pr(NEW.article, doc_.source, 0, doc_.date_doc, NEW.conditionnement_entree, mouv_.id), 0);
									PERFORM insert_mouvement_stock(NEW.article, doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc, last_pr_, mouv_.id);
								END IF;
							ELSE
								result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement_entree,  doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
							END IF;
							
							SELECT INTO mouv_ id, cout_stock FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME AND depot = doc_.source AND article = NEW.article AND mouvement = 'S';
							IF(mouv_.id IS NOT NULL)THEN
								IF(sortie_.methode_val = 'FIFO')THEN
									DELETE FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id AND table_externe = TG_TABLE_NAME AND depot = doc_.source AND article = NEW.article AND mouvement = 'S';
									result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement,  doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
								ELSE
									last_pr_= COALESCE(get_pr(NEW.article, doc_.source, 0, doc_.date_doc, NEW.conditionnement, mouv_.id), 0);
									PERFORM insert_mouvement_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc, last_pr_, mouv_.id);
								END IF;
							ELSE
								result_ = (SELECT valorisation_stock(NEW.article,NEW.conditionnement,  doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
							END IF;	
						END IF;
					END IF;
				END IF;
				return new;
			END IF;
	END CASE;
	IF(ACTION_ = 'INSERT' OR ACTION_ = 'UPDATE')THEN
		RETURN NEW;
	ELSE
		RETURN OLD;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_stock()
  OWNER TO postgres;
