-- Function: update_doc_achats()

-- DROP FUNCTION update_doc_achats();

CREATE OR REPLACE FUNCTION update_doc_achats()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	mouv_ BIGINT;	
	ligne_ record;
	
	delai_ integer;
	duree_ integer;
	agence_ integer;
	model_ integer;
	titre_  CHARACTER VARYING;
	
	date_ date;
	result_ boolean default false;
	_next BOOLEAN DEFAULT false;
	
	ACTION_  CHARACTER VARYING;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	CASE ACTION_
		WHEN 'DELETE' THEN
			if((OLD.type_doc = 'FRA' or OLD.type_doc = 'BLA') and OLD.statut = 'V') then
				for ligne_ in select id from yvs_com_contenu_doc_achat where doc_achat = OLD.id
				loop			
					--Recherche mouvement stock
					for mouv_ in select id from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_achat'
					loop
						delete from yvs_base_mouvement_stock where id = mouv_;
					end loop;
				end loop;
			end if;
			RETURN OLD;
		WHEN 'UPDATE' THEN	
			IF(EXEC_) THEN
				if(NEW.type_doc = 'BRA' or NEW.type_doc = 'BLA')then	
					if(NEW.statut = 'V')then
						IF(NEW.date_livraison IS NULL) THEN 
						  date_=NEW.date_doc;
						ELSE
						  date_=NEW.date_livraison;
						END IF;
						for ligne_ in select id, article , quantite_recu as qte, pua_recu as prix, conditionnement, calcul_pr 
							FROM yvs_com_contenu_doc_achat where doc_achat = NEW.id
						loop
							select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = ligne_.article;					
							--Insertion mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_achat' ;
							if(mouv_ is not null)then
								if(arts_.methode_val = 'FIFO')then
									delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_achat' and article = ligne_.article;
									if(NEW.type_doc = 'BLA')then
										result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_reception, NEW.tranche, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_achat', ligne_.id, 'E', date_));
									else
										result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_reception, NEW.tranche, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_achat', ligne_.id, 'S', date_));
									end if;
								else
									UPDATE yvs_base_mouvement_stock SET quantite = ligne_.qte, cout_entree = ligne_.prix , conditionnement=ligne_.conditionnement , calcul_pr=ligne_.calcul_pr, tranche = NEW.tranche,
																		date_doc=date_
									WHERE id = mouv_;
								end if;
							else
								if(NEW.type_doc = 'BLA')then
									result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_reception, NEW.tranche, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_achat', ligne_.id, 'E', date_));
								else
									result_ = (select valorisation_stock(ligne_.article, ligne_.conditionnement,NEW.depot_reception, NEW.tranche, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_achat', ligne_.id, 'S', OLD.date_livraison));
								end if;
							end if;	
						end loop;
					elsif(NEW.statut != 'V')then
						for ligne_ in select id from yvs_com_contenu_doc_achat WHERE doc_achat = NEW.id
						loop		
							--Recherche mouvement stock
							for mouv_ in select id from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_achat'
							loop
								delete from yvs_base_mouvement_stock where id = mouv_;
							end loop;
						end loop;
					end if;
				end if;	
			END IF;
			IF(EXEC_T_) THEN
				-- Traitement des alertes factures vente
				agence_=NEW.agence;
				IF (NEW.type_doc='FA') THEN					
						-- Alertes retard validation					
						SELECT INTO duree_ (current_date - COALESCE(NEW.date_doc,current_date));
						IF(NEW.statut='V')THEN
							--DÃ©sactive les alertes de retards Validation
							UPDATE yvs_workflow_alertes SET silence=TRUE FROM yvs_workflow_model_doc m
														WHERE (model_doc=m.id) AND id_element=NEW.id AND m.titre_doc='FACTURE_ACHAT' AND nature_alerte='VALIDATION';
							_next=true;
						ELSE						 
							_next =false;
							--Voir si la facture est en retard de validation 						
							SELECT INTO delai_ COALESCE(ecart,0) FROM yvs_warning_model_doc w INNER JOIN yvs_workflow_model_doc m ON m.id=w.model WHERE m.titre_doc='FACTURE_ACHAT';
							IF(delai_>0 AND duree_>delai_) THEN
								--INSERT ALERT
									-- Teste d'abord si la ligne n'existe pas dÃ©jÃ 
									SELECT INTO mouv_ w.id FROM yvs_workflow_alertes w INNER JOIN yvs_workflow_model_doc m ON m.id=w.model_doc WHERE silence=false AND titre_doc='FACTURE_ACHAT' AND nature_alerte='VALIDATION' AND id_element=NEW.id;
									IF(COALESCE(mouv_,0)<=0) THEN
										INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau)
												VALUES ((SELECT id FROM yvs_workflow_model_doc WHERE titre_doc='FACTURE_ACHAT' LIMIT 1), 'VALIDATION', NEW.id, FALSE, current_timestamp, current_timestamp, NEW.author, agence_, (duree_/delai_)::integer);
									END IF;
							END IF;
						END IF;
						IF(_next) THEN
						-- Alertes retard Livraison
							IF(NEW.statut_livre!='L')THEN
								SELECT INTO delai_ ecart FROM yvs_warning_model_doc w INNER JOIN yvs_workflow_model_doc m ON m.id=w.model WHERE m.titre_doc='FACTURE_ACHAT_LIVRE';
								IF(delai_>0 AND duree_>delai_) THEN
									--INSERT ALERT
										-- Teste d'abord si la ligne n'existe pas dÃ©jÃ 
									SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a INNER JOIN yvs_workflow_model_doc m ON m.id=a.model_doc  WHERE silence=false AND titre_doc='FACTURE_ACHAT' AND nature_alerte='LIVRAISON' AND id_element=NEW.id;
									IF(COALESCE(mouv_,0)<=0) THEN
										INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau)
											VALUES ((SELECT id FROM yvs_workflow_model_doc WHERE titre_doc='FACTURE_ACHAT' LIMIT 1), 'LIVRAISON', NEW.id, FALSE, current_timestamp, current_timestamp, NEW.author, agence_, (duree_/delai_)::integer);
									END IF;
								END IF;
							ELSE
								--DÃ©sactive les alertes de retards livraison de la facture
								UPDATE yvs_workflow_alertes SET silence=TRUE FROM yvs_workflow_model_doc m
														WHERE (model_doc=m.id) AND id_element=NEW.id AND titre_doc='FACTURE_ACHAT' AND nature_alerte='LIVRAISON';
							END IF;						
						-- Alertes retard RÃ¨glement
							-- L'alerte retard rÃ¨glement est basÃ© sur la date de la derniÃ¨re mensualitÃ©
							IF(NEW.statut_regle!='R')THEN
								SELECT INTO duree_ (current_date - COALESCE(m.date_reglement,current_date)) FROM yvs_com_mensualite_facture_achat m WHERE m.facture=NEW.id ORDER BY m.date_reglement DESC LIMIT 1;
								SELECT INTO delai_ ecart FROM yvs_warning_model_doc w INNER JOIN yvs_workflow_model_doc m ON m.id=w.model WHERE m.titre_doc='FACTURE_ACHAT_REGLE';
								IF(delai_>0 AND duree_>delai_) THEN
									--INSERT ALERT
									-- Teste d'abord si la ligne n'existe pas dÃ©jÃ 
									SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a INNER JOIN yvs_workflow_model_doc m ON m.id=a.model_doc  WHERE silence=false AND titre_doc='FACTURE_ACHAT' AND nature_alerte='REGLEMENT' AND id_element=NEW.id;									
									IF(COALESCE(mouv_,0)<=0) THEN
										INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau)
											VALUES ((SELECT id FROM yvs_workflow_model_doc WHERE titre_doc='FACTURE_ACHAT' LIMIT 1), 'REGLEMENT', NEW.id, FALSE, current_timestamp, current_timestamp, NEW.author, agence_, (duree_/delai_)::integer);
									END IF;
								END IF;
							ELSE
								--DÃ©sactive les alertes de retards rÃ¨glement de la facture
								UPDATE yvs_workflow_alertes SET silence=TRUE FROM yvs_workflow_model_doc m
														WHERE (model_doc=m.id) AND id_element=NEW.id AND titre_doc='FACTURE_ACHAT' AND nature_alerte='REGLEMENT';
							END IF;							
						END IF;
					ELSIF(NEW.type_doc='BLA' OR NEW.type_doc='FAA' OR NEW.type_doc='BRA') THEN
						duree_=(current_date - COALESCE(NEW.date_doc, current_date));
						CASE NEW.type_doc
							WHEN 'BLA' THEN
								titre_='BON_LIVRAISON_ACHAT';
							WHEN 'FAA' THEN
								titre_='AVOIR_ACHAT';
							WHEN 'BRA' THEN	
								titre_='RETOUR_ACHAT';
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
								SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a INNER JOIN yvs_workflow_model_doc m ON m.id=a.model_doc  WHERE silence=false AND titre_doc=titre_ AND nature_alerte='VALIDATION' AND id_element=NEW.id;									
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
ALTER FUNCTION update_doc_achats()
  OWNER TO postgres;
