UPDATE yvs_base_tiers r SET tel = y.numero FROM yvs_base_tiers_telephone y INNER JOIN yvs_base_tiers t ON t.id = y.tiers WHERE r.id = t.id AND r.tel IS NULL;
DELETE FROM yvs_page_module WHERE reference = 'stat_general';
UPDATE yvs_base_article_fournisseur r SET pua_ttc = y.pua_ttc FROM yvs_base_articles y WHERE r.article = y.id;

SELECT insert_droit('stat_listing_vente', 'Page des listings vente', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,O,E,C,F,D,B,M','P');				
	
SELECT insert_droit('stat_journal_vente_vendeur', 'Page des journaux vente par vendeur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,O,E,C,F,D,B,M','P');				
	
SELECT insert_droit('stat_ristourne_client', 'Page des ristournes client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,O,E,C,F,D,B,M','P');				
	
SELECT insert_droit('stat_listing_vente_client', 'Page des listings vente par client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,O,E,C,F,D,B,M','P');					
	
SELECT insert_droit('stat_listing_vente_article', 'Page des listings vente par article', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,O,E,C,F,D,B,M','P');

-- Function: grh_actions_in_mission()
-- DROP FUNCTION grh_actions_in_mission();
CREATE OR REPLACE FUNCTION grh_warning_in_mission()
  RETURNS trigger AS
$BODY$    
DECLARE
	id_ bigint;
	agence_ bigint;
	
	duree_ integer;	

	action_ character varying;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		id_ = NEW.id;
	ELSE
		id_ = OLD.id;
	END IF;
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		SELECT INTO duree_ (current_date - COALESCE(NEW.date_mission, current_date));
		SELECT INTO agence_ agence FROM yvs_grh_employes WHERE id = NEW.id;
		IF(NEW.statut_mission NOT IN ('V', 'C'))THEN
			PERFORM workflow_add_warning(NEW.id, 'MISSIONS', 'VALIDATION', duree_, TRUE, agence_, NEW.author);
		ELSE
			PERFORM workflow_add_warning(NEW.id, 'MISSIONS', 'VALIDATION', duree_, FALSE, agence_, NEW.author);
		END IF;
		RETURN NEW;
	ELSE
		PERFORM workflow_add_warning(OLD.id, 'MISSIONS', 'VALIDATION', duree_, FALSE, agence_, NEW.author);
		RETURN OLD;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_warning_in_mission()
  OWNER TO postgres;

-- Trigger: grh_warning_in_mission on yvs_grh_missions
-- DROP TRIGGER grh_warning_in_mission ON yvs_grh_missions;
CREATE TRIGGER grh_warning_in_mission
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_grh_missions
  FOR EACH ROW
  EXECUTE PROCEDURE grh_warning_in_mission();
  
  
-- Function: grh_actions_in_conge_emps()
-- DROP FUNCTION grh_actions_in_conge_emps();
CREATE OR REPLACE FUNCTION grh_warning_in_conge_emps()
  RETURNS trigger AS
$BODY$    
DECLARE
	id_ bigint;
	agence_ bigint;
	
	duree_ integer;	

	action_ character varying;
	titre_ character varying default 'CONGES';
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		id_ = NEW.id;
	ELSE
		id_ = OLD.id;
	END IF;
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		SELECT INTO duree_ (current_date - COALESCE(NEW.date_conge, current_date));
		SELECT INTO agence_ agence FROM yvs_grh_employes WHERE id = NEW.id;
		IF(NEW.nature = 'P')THEN
			titre_ = 'PERMISSION_'||NEW.duree_permission||'D';
		END IF;
		IF(NEW.statut NOT IN ('V', 'C'))THEN
			PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, TRUE, agence_, NEW.author);
		ELSE
			PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
		END IF;
		RETURN NEW;
	ELSE
		PERFORM workflow_add_warning(OLD.id, 'CONGES', 'VALIDATION', duree_, FALSE, agence_, NEW.author);
		RETURN OLD;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_warning_in_conge_emps()
  OWNER TO postgres;

-- Trigger: grh_warning_in_conge_emps on yvs_grh_missions
-- DROP TRIGGER grh_warning_in_conge_emps ON yvs_grh_missions;
CREATE TRIGGER grh_warning_in_conge_emps
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_grh_conge_emps
  FOR EACH ROW
  EXECUTE PROCEDURE grh_warning_in_conge_emps();
  
  
  -- Function: com_actions_in_doc_achat()
-- DROP FUNCTION com_actions_in_doc_achat();
CREATE OR REPLACE FUNCTION com_warning_in_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	id_ bigint;
	agence_ bigint;
	
	duree_ integer;	

	action_ character varying;
	titre_ character varying default 'FACTURE_ACHAT';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		id_ = NEW.id;
	ELSE
		id_ = OLD.id;
	END IF;
	IF(EXEC_T_) THEN
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			-- Traitement des alertes factures vente
			agence_= NEW.agence;
			IF(agence_ IS NULL) THEN
				SELECT INTO agence_ d.agence FROM yvs_com_doc_achats a INNER JOIN yvs_base_depots d ON d.id=a.depot_reception WHERE a.id=NEW.id;
			END IF;
			IF(NEW.type_doc='FA') THEN
				-- Alertes retard validation					
				SELECT INTO duree_ (current_date - COALESCE(NEW.date_doc, current_date));
				IF(NEW.statut NOT IN ('V', 'C'))THEN
					PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, TRUE, agence_, NEW.author);
				ELSE	
					PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
					-- Alertes retard Livraison
					titre_ = 'FACTURE_ACHAT_LIVRE';
					IF(NEW.statut_livre != 'L')THEN
						PERFORM workflow_add_warning(NEW.id, titre_, 'LIVRAISON', duree_, TRUE, agence_, NEW.author);
					ELSE
						PERFORM workflow_add_warning(NEW.id, titre_, 'LIVRAISON', duree_, FALSE, agence_, NEW.author);
					END IF;						
					-- Alertes retard Réglement
					titre_ = 'FACTURE_ACHAT_REGLE';
					-- L'alerte retard rÃ¨glement est basÃ© sur la date de la derniÃ¨re mensualitÃ©
					IF(NEW.statut_regle != 'R')THEN
						SELECT INTO duree_ (current_date - COALESCE(m.date_reglement, current_date)) FROM yvs_com_mensualite_facture_achat m WHERE m.facture = NEW.id ORDER BY m.date_reglement DESC LIMIT 1;
						PERFORM workflow_add_warning(NEW.id, titre_, 'REGLEMENT', duree_, TRUE, agence_, NEW.author);
					ELSE
						PERFORM workflow_add_warning(NEW.id, titre_, 'REGLEMENT', duree_, FALSE, agence_, NEW.author);
					END IF;	
				END IF;
			ELSIF(NEW.type_doc='BLA' OR NEW.type_doc='FAA' OR NEW.type_doc='BRA') THEN
				duree_ = (current_date - COALESCE(NEW.date_doc, current_date));
				CASE NEW.type_doc
					WHEN 'BLA' THEN
						titre_ = 'BON_LIVRAISON_ACHAT';
					WHEN 'FAA' THEN
						titre_ = 'AVOIR_ACHAT';
					WHEN 'BRA' THEN	
						titre_ = 'RETOUR_ACHAT';
				END CASE;
				-- Traite les retard de validation
				IF(NEW.statut NOT IN ('V', 'C'))THEN
					PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, TRUE, agence_, NEW.author);
				ELSE						 
					PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
				END IF;	
			END IF;
		ELSE
			IF(NEW.type_doc='FA') THEN
				PERFORM workflow_add_warning(OLD.id, 'FACTURE_ACHAT', 'VALIDATION', duree_, FALSE, agence_, NEW.author);
				PERFORM workflow_add_warning(OLD.id, 'FACTURE_ACHAT_LIVRE', 'LIVRAISON', duree_, FALSE, agence_, NEW.author);
				PERFORM workflow_add_warning(OLD.id, 'FACTURE_ACHAT_REGLE', 'REGLEMENT', duree_, FALSE, agence_, NEW.author);
			ELSE
				CASE NEW.type_doc
					WHEN 'BLA' THEN
						titre_ = 'BON_LIVRAISON_ACHAT';
					WHEN 'FAA' THEN
						titre_ = 'AVOIR_ACHAT';
					WHEN 'BRA' THEN	
						titre_ = 'RETOUR_ACHAT';
				END CASE;
				PERFORM workflow_add_warning(OLD.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
			END IF;
		END IF;
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
ALTER FUNCTION com_warning_in_doc_achat()
  OWNER TO postgres;

-- Trigger: com_warning_in_doc_achat on yvs_com_doc_achats
-- DROP TRIGGER com_warning_in_doc_achat ON yvs_com_doc_achats;
CREATE TRIGGER com_warning_in_doc_achat
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_com_doc_achats
  FOR EACH ROW
  EXECUTE PROCEDURE com_warning_in_doc_achat();
  
  
-- Function: com_warning_in_doc_vente()
-- DROP FUNCTION com_warning_in_doc_vente();
CREATE OR REPLACE FUNCTION com_warning_in_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	ligne_ RECORD;
	
	id_ bigint;
	agence_ bigint;
	
	duree_ integer;	

	action_ character varying;
	titre_ character varying default 'FACTURE_VENTE';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		id_ = NEW.id;
	ELSE
		id_ = OLD.id;
	END IF;
	IF(EXEC_T_) THEN
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			-- Traitement des alertes factures vente
			SELECT INTO ligne_ e.date_entete, e.agence FROM yvs_com_entete_doc_vente e WHERE e.id = NEW.entete_doc;
			agence_ = ligne_.agence;
			IF(NEW.type_doc='FV') THEN
				-- Alertes retard validation					
				SELECT INTO duree_ (current_date - COALESCE(ligne_.date_entete, current_date));
				IF(NEW.statut NOT IN ('V', 'C'))THEN
					PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, TRUE, agence_, NEW.author);
				ELSE	
					PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
					-- Alertes retard Livraison
					titre_ = 'FACTURE_VENTE_LIVRE';
					IF(NEW.statut_livre != 'L')THEN
						PERFORM workflow_add_warning(NEW.id, titre_, 'LIVRAISON', duree_, TRUE, agence_, NEW.author);
					ELSE
						PERFORM workflow_add_warning(NEW.id, titre_, 'LIVRAISON', duree_, FALSE, agence_, NEW.author);
					END IF;						
					-- Alertes retard Réglement
					titre_ = 'FACTURE_VENTE_REGLE';
					-- L'alerte retard rÃ¨glement est basÃ© sur la date de la derniÃ¨re mensualitÃ©
					IF(NEW.statut_regle != 'P')THEN
						SELECT INTO duree_ (current_date - COALESCE(m.date_reglement, current_date)) FROM yvs_com_mensualite_facture_vente m WHERE m.facture = NEW.id ORDER BY m.date_reglement DESC LIMIT 1;
						RAISE NOTICE 'duree_ : %',duree_;
						PERFORM workflow_add_warning(NEW.id, titre_, 'REGLEMENT', duree_, TRUE, agence_, NEW.author);
					ELSE
						PERFORM workflow_add_warning(NEW.id, titre_, 'REGLEMENT', duree_, FALSE, agence_, NEW.author);
					END IF;	
				END IF;
			ELSIF(NEW.type_doc='BLV' OR NEW.type_doc='FAV' OR NEW.type_doc='BRV') THEN
				duree_=(current_date - COALESCE(NEW.date_livraison_prevu::date, current_date));
				CASE NEW.type_doc
					WHEN 'BLV' THEN
						titre_ = 'BON_LIVRAISON_VENTE';
					WHEN 'FAV' THEN
						titre_ = 'AVOIR_VENTE';
					WHEN 'BRV' THEN	
						titre_ = 'RETOUR_VENTE';
				END CASE;
				-- Traite les retard de validation
				IF(NEW.statut NOT IN ('V', 'C'))THEN
					PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, TRUE, agence_, NEW.author);
				ELSE						 
					PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
				END IF;	
			END IF;
		ELSE
			IF(NEW.type_doc='FV') THEN
				PERFORM workflow_add_warning(OLD.id, 'FACTURE_VENTE', 'VALIDATION', duree_, FALSE, agence_, NEW.author);
				PERFORM workflow_add_warning(OLD.id, 'FACTURE_VENTE_LIVRE', 'LIVRAISON', duree_, FALSE, agence_, NEW.author);
				PERFORM workflow_add_warning(OLD.id, 'FACTURE_VENTE_REGLE', 'REGLEMENT', duree_, FALSE, agence_, NEW.author);
			ELSE
				CASE NEW.type_doc
					WHEN 'BLV' THEN
						titre_ = 'BON_LIVRAISON_VENTE';
					WHEN 'FAV' THEN
						titre_ = 'AVOIR_VENTE';
					WHEN 'BRV' THEN	
						titre_ = 'RETOUR_VENTE';
				END CASE;
				PERFORM workflow_add_warning(OLD.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
			END IF;
		END IF;
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
ALTER FUNCTION com_warning_in_doc_vente()
  OWNER TO postgres;  
  
-- Trigger: com_warning_in_doc_vente on yvs_com_doc_ventes
-- DROP TRIGGER com_warning_in_doc_vente ON yvs_com_doc_ventes;
CREATE TRIGGER com_warning_in_doc_vente
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_com_doc_ventes
  FOR EACH ROW
  EXECUTE PROCEDURE com_warning_in_doc_vente();
  
  
-- Function: com_warning_in_doc_stock()
-- DROP FUNCTION com_warning_in_doc_stock();
CREATE OR REPLACE FUNCTION com_warning_in_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	ligne_ RECORD;
	
	id_ bigint;
	agence_ bigint;
	
	duree_ integer;	

	action_ character varying;
	titre_ character varying default 'SORTIE_STOCK';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		id_ = NEW.id;
	ELSE
		id_ = OLD.id;
	END IF;
	IF(EXEC_T_) THEN
		CASE NEW.type_doc
			WHEN 'SS' THEN
				titre_='SORTIE_STOCK';
			WHEN 'TR' THEN
				titre_='RECONDITIONNEMENT_STOCK';
			WHEN 'IN' THEN
				titre_='INVENTAIRE_STOCK';
			WHEN 'FT' THEN
				titre_='TRANSFERT_STOCK';
			ELSE
				titre_='ENTREE_STOCK';
				
		END CASE;
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			IF(NEW.type_doc = 'ES')THEN
				SELECT INTO agence_ de.agence FROM yvs_com_doc_stocks d INNER JOIN yvs_base_depots de ON de.id=d.destination WHERE d.id=NEW.id;
			ELSE
				SELECT INTO agence_ de.agence FROM yvs_com_doc_stocks d INNER JOIN yvs_base_depots de ON de.id=d.source WHERE d.id=NEW.id;
			END IF;
			SELECT INTO duree_ (current_date - COALESCE(NEW.date_doc, current_date));
			IF(NEW.statut NOT IN ('V', 'C'))THEN
				PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, TRUE, agence_, NEW.author);
			ELSE						 
				PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
			END IF;	
		ELSE
			PERFORM workflow_add_warning(OLD.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
		END IF;
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
ALTER FUNCTION com_warning_in_doc_stock()
  OWNER TO postgres;  
  
-- Trigger: com_warning_in_doc_stock on yvs_com_doc_stocks
-- DROP TRIGGER com_warning_in_doc_stock ON yvs_com_doc_stocks;
CREATE TRIGGER com_warning_in_doc_stock
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_com_doc_stocks
  FOR EACH ROW
  EXECUTE PROCEDURE com_warning_in_doc_stock();
  
  
-- Function: compta_warning_doc_divers()
-- DROP FUNCTION compta_warning_doc_divers();
CREATE OR REPLACE FUNCTION compta_warning_doc_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	ligne_ RECORD;
	
	id_ bigint;
	agence_ bigint;
	
	duree_ integer;	

	action_ character varying;
	titre_ character varying default 'OPERATION_DIVERS';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		id_ = NEW.id;
	ELSE
		id_ = OLD.id;
	END IF;
	IF(EXEC_T_) THEN 
		IF(NEW.mouvement='D') THEN
			titre_ = 'DOC_DIVERS_DEPENSE';
		ELSIF(NEW.mouvement='R') THEN
			titre_ = 'DOC_DIVERS_RECETTE';
		END IF;
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			agence_ = NEW.agence;
			SELECT INTO duree_ (current_date - COALESCE(NEW.date_doc::date, current_date));
			IF(NEW.statut_doc NOT IN ('V', 'C'))THEN
				PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, TRUE, agence_, NEW.author);
			ELSE						 
				PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
			END IF;	
		ELSE
			PERFORM workflow_add_warning(OLD.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
		END IF;
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
ALTER FUNCTION compta_warning_doc_divers()
  OWNER TO postgres;

-- Trigger: compta_warning_doc_divers on yvs_compta_caisse_doc_divers
-- DROP TRIGGER compta_warning_doc_divers ON yvs_compta_caisse_doc_divers;
CREATE TRIGGER compta_warning_doc_divers
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_compta_caisse_doc_divers
  FOR EACH ROW
  EXECUTE PROCEDURE compta_warning_doc_divers();
  
  
-- Function: compta_warning_bon_provisoire()
-- DROP FUNCTION compta_warning_bon_provisoire();
CREATE OR REPLACE FUNCTION compta_warning_bon_provisoire()
  RETURNS trigger AS
$BODY$    
DECLARE
	ligne_ RECORD;
	
	id_ bigint;
	agence_ bigint;
	
	duree_ integer;	

	action_ character varying;
	titre_ character varying default 'BON_OPERATION_DIVERS';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		id_ = NEW.id;
	ELSE
		id_ = OLD.id;
	END IF;
	IF(EXEC_T_) THEN 
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			agence_ = NEW.agence;
			SELECT INTO duree_ (current_date - COALESCE(NEW.date_bon::date, current_date));
			IF(NEW.statut NOT IN ('V', 'C'))THEN
				PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, TRUE, agence_, NEW.author);
			ELSE						 
				PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
			END IF;	
		ELSE
			PERFORM workflow_add_warning(OLD.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
		END IF;
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
ALTER FUNCTION compta_warning_bon_provisoire()
  OWNER TO postgres;

-- Trigger: compta_warning_bon_provisoire on yvs_compta_bon_provisoire
-- DROP TRIGGER compta_warning_bon_provisoire ON yvs_compta_bon_provisoire;
CREATE TRIGGER compta_warning_bon_provisoire
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_compta_bon_provisoire
  FOR EACH ROW
  EXECUTE PROCEDURE compta_warning_bon_provisoire();
  
  
-- Function: com_warning_approvisionnement()
-- DROP FUNCTION com_warning_approvisionnement();
CREATE OR REPLACE FUNCTION com_warning_approvisionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	ligne_ RECORD;
	
	id_ bigint;
	agence_ bigint;
	
	duree_ integer;	

	action_ character varying;
	titre_ character varying default 'APPROVISIONNEMENT';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		id_ = NEW.id;
	ELSE
		id_ = OLD.id;
	END IF;
	IF(EXEC_T_) THEN 
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			SELECT INTO agence_ d.agence FROM yvs_base_depots d WHERE d.id = NEW.depot;
			SELECT INTO duree_ (current_date - COALESCE(NEW.date_approvisionnement::date, current_date));
			IF(NEW.etat NOT IN ('V', 'C'))THEN
				PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, TRUE, agence_, NEW.author);
			ELSE						 
				PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
			END IF;	
		ELSE
			PERFORM workflow_add_warning(OLD.id, titre_, 'VALIDATION', duree_, FALSE, agence_, NEW.author);
		END IF;
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
ALTER FUNCTION com_warning_approvisionnement()
  OWNER TO postgres;

-- Trigger: com_warning_approvisionnement on yvs_com_fiche_approvisionnement
-- DROP TRIGGER com_warning_approvisionnement ON yvs_com_fiche_approvisionnement;
CREATE TRIGGER com_warning_approvisionnement
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_com_fiche_approvisionnement
  FOR EACH ROW
  EXECUTE PROCEDURE com_warning_approvisionnement();