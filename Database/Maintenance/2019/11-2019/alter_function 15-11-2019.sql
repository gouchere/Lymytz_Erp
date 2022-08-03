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
		current_ := (SELECT y.id FROM yvs_synchro_listen_table y WHERE y.id_source = id_ AND y.name_table = TG_TABLE_NAME AND action_name = 'UPDATE' ORDER BY y.id DESC LIMIT 1);
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


-- Function: insert_contenu_doc_vente()
-- DROP FUNCTION insert_contenu_doc_vente();
CREATE OR REPLACE FUNCTION insert_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
	DECLARE
		doc_ record;
		arts_ record;
		dep_ record;
		mouv_ BIGINT;	
		ligne_ record;
		
		date_ date;
		result_ boolean default false;
		
		ACTION_  CHARACTER VARYING;
		EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
		EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
	BEGIN
		ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
		CASE ACTION_
			WHEN 'DELETE' THEN
				select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer, date_livraison 
				FROM yvs_com_doc_ventes WHERE id = OLD.doc_vente;		
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				IF((doc_.type_doc='FV' OR doc_.type_doc='BCV') AND doc_.statut='V')  THEN
					PERFORM equilibre_vente(doc_.id);
				END IF;
				return new;
			WHEN 'INSERT' THEN
				IF(EXEC_) THEN
					select into doc_ id, type_doc, statut, entete_doc, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;
					select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c on e.creneau = c.id 
																																	   inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
																									   where e.id = doc_.entete_doc;
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
				IF(EXEC_ OR EXEC_T_) THEN
					select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;		
				END IF;
				IF(EXEC_) THEN				
					IF((doc_.type_doc = 'BRV' or doc_.type_doc = 'BRL' or doc_.type_doc = 'BLV') AND doc_.statut = 'V') then
							select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
							select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
								on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
								where e.id = doc_.entete_doc;
						--Insertion mouvement stock
							if(doc_.depot_livrer is not null)then
								select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article;
								if(mouv_ is not null)then
									if(arts_.methode_val = 'FIFO')then
										delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article;
										if(doc_.type_doc = 'BLV')then
											result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
										else
											result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
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
										result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
									else
										result_ = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
									end if;
								end if;	
							end if;
					END IF;
				END IF;
				IF(EXEC_T_)	THEN
					IF(doc_.type_doc = 'FV') THEN
						--Mettre Ã  jour les statuts
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


-- Function: update_doc_ventes()
-- DROP FUNCTION update_doc_ventes();
CREATE OR REPLACE FUNCTION update_doc_ventes()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	mouv_ BIGINT;	
	ligne_ record;
	
	result_ boolean default false;
	prix_ double precision;
	delai_ integer;
	duree_ integer;
	agence_ integer;
	model_ integer;
	titre_  CHARACTER VARYING;
	
	ACTION_  CHARACTER VARYING;
	_next BOOLEAN DEFAULT false;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	CASE ACTION_
		WHEN 'DELETE' THEN
			for ligne_ in select id from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_vente';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
			end loop;
			RETURN OLD;
		WHEN 'UPDATE' THEN	
			IF(EXEC_) THEN
				if(NEW.type_doc = 'BLV' or NEW.type_doc = 'BRL' or NEW.type_doc = 'BRV') then
					if(NEW.statut = 'V') then
						for ligne_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix, conditionnement, calcul_pr from yvs_com_contenu_doc_vente where doc_vente = NEW.id
						loop
							select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = ligne_.article;
							--Insertion mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and article = ligne_.article;
							if(mouv_ is not null)then
								if(arts_.methode_val = 'FIFO')then
									delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = ligne_.article;
									if(NEW.type_doc = 'BLV')then
										result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (ligne_.qte), 0, 'yvs_com_contenu_doc_vente', ligne_.id, 'S', NEW.date_livraison));
									else
										result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (ligne_.qte), 0, 'yvs_com_contenu_doc_vente', ligne_.id, 'E', NEW.date_livraison));
									end if;
								else
									UPDATE yvs_base_mouvement_stock SET quantite = (ligne_.qte), cout_entree = ligne_.prix, conditionnement=ligne_.conditionnement, calcul_pr=ligne_.calcul_pr, tranche = NEW.tranche_livrer
																	WHERE id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and article = ligne_.article;
								end if;
							else
								if(NEW.type_doc = 'BLV')then
									result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (ligne_.qte), 0, 'yvs_com_contenu_doc_vente', ligne_.id, 'S', NEW.date_livraison));
								else
									result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (ligne_.qte), 0, 'yvs_com_contenu_doc_vente', ligne_.id, 'E', NEW.date_livraison));
								end if;
							end if;	
						end loop;
					elsif(NEW.statut != 'V')then
						for ligne_ in select id from yvs_com_contenu_doc_vente where doc_vente = OLD.id
						loop				
							--Recherche mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_vente';
							if(mouv_ is not null)then
								delete from yvs_base_mouvement_stock where id = mouv_;
							end if;
						end loop;
					end if;
				end if;
			END IF;
			IF(EXEC_T_) THEN 
				if(NEW.type_doc = 'FV' AND NEW.statut_livre = 'L') then
					for ligne_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix, id_reservation from yvs_com_contenu_doc_vente where doc_vente = NEW.id
						LOOP
						--change le statut de la reservation
						IF(ligne_.id_reservation IS NOT NULL) THEN
							UPDATE yvs_com_reservation_stock SET statut='T' WHERE id=ligne_.id_reservation AND statut='V';
						END IF;
						
						END LOOP;
				end if;
				-- Traitement des alertes factures vente
				IF(NEW.type_doc='FV' OR NEW.type_doc='FAV' OR NEW.type_doc='BRV' OR NEW.type_doc='BLV') THEN
					SELECT INTO ligne_ e.date_entete, e.agence FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE d.id=NEW.id;
						agence_=COALESCE(ligne_.agence,0);
				END IF;
				IF (NEW.type_doc='FV') THEN
						-- Alertes retard validation					
						SELECT INTO duree_ (current_date - COALESCE(ligne_.date_entete,current_date));
						IF(NEW.statut='V')THEN
							--DÃ©sactive les alertes de retards Validation
							UPDATE yvs_workflow_alertes SET silence=TRUE FROM yvs_workflow_model_doc m
														WHERE (model_doc=m.id) AND id_element=NEW.id AND titre_doc='FACTURE_VENTE' AND nature_alerte='VALIDATION';
							_next=true;
						ELSE						 
							_next =false;
							--Voir si la facture est en retard de validation 						
							SELECT INTO delai_ COALESCE(ecart,0) FROM yvs_warning_model_doc w INNER JOIN yvs_workflow_model_doc m ON m.id=w.model WHERE m.titre_doc='FACTURE_VENTE';
							IF(delai_>0 AND duree_>delai_) THEN
								--INSERT ALERT
									-- Teste d'abord si la ligne n'existe pas dÃ©jÃ 
									SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a INNER JOIN yvs_workflow_model_doc m ON m.id=a.model_doc WHERE silence=false AND titre_doc='FACTURE_VENTE' AND nature_alerte='VALIDATION' AND id_element=NEW.id;
									IF(COALESCE(mouv_,0)<=0) THEN
										INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau)
												VALUES ((SELECT id FROM yvs_workflow_model_doc WHERE titre_doc='FACTURE_VENTE' LIMIT 1), 'VALIDATION', NEW.id, FALSE, current_timestamp, current_timestamp, NEW.author, agence_, (duree_/delai_)::integer);
									END IF;
							END IF;
						END IF;
						IF(_next) THEN
						-- Alertes retard Livraison
							IF(NEW.statut_livre!='L')THEN
								SELECT INTO delai_ ecart FROM yvs_warning_model_doc w INNER JOIN yvs_workflow_model_doc m ON m.id=w.model WHERE m.titre_doc='FACTURE_VENTE_LIVRE';
								IF(delai_>0 AND duree_>delai_) THEN
									--INSERT ALERT
										-- Teste d'abord si la ligne n'existe pas dÃ©jÃ 
									SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a INNER JOIN yvs_workflow_model_doc m ON m.id=a.model_doc WHERE silence=false AND m.titre_doc='FACTURE_VENTE' AND nature_alerte='LIVRAISON' AND id_element=NEW.id;
									IF(COALESCE(mouv_,0)<=0) THEN
										INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau)
											VALUES ((SELECT id FROM yvs_workflow_model_doc WHERE titre_doc='FACTURE_VENTE' LIMIT 1), 'LIVRAISON', NEW.id, FALSE, current_timestamp, current_timestamp, NEW.author, agence_, (duree_/delai_)::integer);
									END IF;
								END IF;
							ELSE
								--DÃ©sactive les alertes de retards livraison de la facture
								UPDATE yvs_workflow_alertes SET silence=TRUE FROM yvs_workflow_model_doc m
														WHERE (model_doc=m.id) AND id_element=NEW.id AND titre_doc='FACTURE_VENTE' AND nature_alerte='LIVRAISON';
							END IF;						
						-- Alertes retard RÃ¨glement
							-- L'alerte retard rÃ¨glement est basÃ© sur la date de la derniÃ¨re mensualitÃ©
							IF(NEW.statut_regle!='R')THEN
								SELECT INTO duree_ (current_date - COALESCE(m.date_reglement,current_date)) FROM yvs_com_mensualite_facture_vente m WHERE m.facture=NEW.id ORDER BY m.date_reglement DESC LIMIT 1;
								SELECT INTO delai_ ecart FROM yvs_warning_model_doc w INNER JOIN yvs_workflow_model_doc m ON m.id=w.model WHERE m.titre_doc='FACTURE_VENTE_REGLE';
								IF(delai_>0 AND duree_>delai_) THEN
									--INSERT ALERT
									-- Teste d'abord si la ligne n'existe pas dÃ©jÃ 
									SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a INNER JOIN yvs_workflow_model_doc m ON m.id=a.model_doc WHERE silence=false AND titre_doc='FACTURE_VENTE' AND nature_alerte='REGLEMENT' AND id_element=NEW.id;									
									IF(COALESCE(mouv_,0)<=0) THEN
										INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau)
											VALUES ((SELECT id FROM yvs_workflow_model_doc WHERE titre_doc='FACTURE_VENTE' LIMIT 1), 'REGLEMENT', NEW.id, FALSE, current_timestamp, current_timestamp, NEW.author, agence_, (duree_/delai_)::integer);
									END IF;
								END IF;
							ELSE
								--DÃ©sactive les alertes de retards rÃ¨glement de la facture
								UPDATE yvs_workflow_alertes SET silence=TRUE FROM yvs_workflow_model_doc m
														WHERE (model_doc=m.id) AND id_element=NEW.id AND titre_doc='FACTURE_VENTE' AND nature_alerte='REGLEMENT';
							END IF;							
						END IF;
				ELSIF (NEW.type_doc='BLV' OR NEW.type_doc='BRV' OR NEW.type_doc='FAV') THEN
						duree_=(current_date - COALESCE(NEW.date_livraison_prevu, current_date));
						CASE NEW.type_doc
							WHEN 'BLV' THEN
								titre_='BON_LIVRAISON_VENTE';
							WHEN 'FAV' THEN
								titre_='AVOIR_VENTE';
							WHEN 'BRV' THEN	
								titre_='RETOUR_VENTE';
						END CASE;
						-- Traite les retard de validation
						IF(NEW.statut='V')THEN
							--DÃ©sactive les alertes de retards Validation
							UPDATE yvs_workflow_alertes SET silence=TRUE FROM yvs_workflow_model_doc m
														WHERE (model_doc=m.id) AND id_element=NEW.id AND titre_doc=titre_ AND nature_alerte='VALIDATION';
						ELSE						 
							--Voir si la facture est en retard de validation 						
							SELECT INTO delai_ COALESCE(ecart,0) FROM yvs_warning_model_doc w INNER JOIN yvs_workflow_model_doc m ON m.id=w.model WHERE m.titre_doc=titre_;
							IF(delai_>0 AND duree_>delai_) THEN
								--INSERT ALERT
								-- Teste d'abord si la ligne n'existe pas dÃ©jÃ 
								SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a INNER JOIN yvs_workflow_model_doc m ON m.id=a.model_doc WHERE silence=false AND titre_doc=titre_ AND nature_alerte='VALIDATION' AND id_element=NEW.id;									
								IF(COALESCE(mouv_,0)<=0) THEN
									INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau)
											VALUES ((SELECT id FROM yvs_workflow_model_doc WHERE titre_doc=titre_ LIMIT 1), 'VALIDATION', NEW.id, FALSE, current_timestamp, current_timestamp, NEW.author, agence_, (duree_/delai_)::integer);
								END IF;
							END IF;
						END IF;
						
					
				END IF;
			END IF;
		    RETURN NEW;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ventes()
  OWNER TO postgres;
