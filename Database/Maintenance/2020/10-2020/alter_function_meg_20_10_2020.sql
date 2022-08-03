INSERT INTO yvs_module_active(societe, module, actif, author) SELECT s.id, m.id, true, s.author FROM yvs_module m, yvs_societes s;

INSERT INTO yvs_module(libelle, description, reference) VALUES ('Gestion de projet', 'Module de gestion des projets', 'proj_');

INSERT INTO yvs_base_departement (chemin_parent, code_departement, description, chemin_fichier, intitule, nivau, societe, supp, actif, author, abreviation, date_update, date_save, execute_trigger)
SELECT y.chemin_parent, y.code_departement, y.description,  y.chemin_fichier, y.intitule, y.nivau, y.societe, y.supp, y.actif, y.author, y.abreviation, y.date_update, y.date_save, y.execute_trigger FROM yvs_grh_departement y;

UPDATE yvs_base_departement y SET departement_parent = (SELECT DISTINCT p.id FROM yvs_base_departement p INNER JOIN yvs_grh_departement _p ON (_p.code_departement = p.code_departement AND _p.societe = p.societe) INNER JOIN yvs_grh_departement _d ON _d.departement_parent = _p.id WHERE _d.code_departement = y.code_departement AND _d.societe = y.societe);	
	
UPDATE yvs_grh_plan_prelevement y SET societe = a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON ua.agence = a.id WHERE y.author = ua.id;

ALTER TABLE yvs_workflow_valid_approvissionnement DISABLE TRIGGER action_on_workflow_approvissionnement;
ALTER TABLE yvs_workflow_valid_bon_provisoire DISABLE TRIGGER action_on_workflow_bon_provisoire;
ALTER TABLE yvs_workflow_valid_conge DISABLE TRIGGER action_on_workflow_conge;
ALTER TABLE yvs_workflow_valid_doc_caisse DISABLE TRIGGER action_on_workflow_doc_caisse;
ALTER TABLE yvs_workflow_valid_doc_stock DISABLE TRIGGER action_on_workflow_doc_stock;
ALTER TABLE yvs_workflow_valid_facture_achat DISABLE TRIGGER action_on_workflow_facture_achat;
ALTER TABLE yvs_workflow_valid_facture_vente DISABLE TRIGGER action_on_workflow_facture_vente;
ALTER TABLE yvs_workflow_valid_mission DISABLE TRIGGER action_on_workflow_mission;
UPDATE yvs_workflow_valid_approvissionnement y SET ordre_etape = e.ordre_etape FROM yvs_workflow_etape_validation e WHERE e.id = y.etape;
UPDATE yvs_workflow_valid_bon_provisoire y SET ordre_etape = e.ordre_etape FROM yvs_workflow_etape_validation e WHERE e.id = y.etape;
UPDATE yvs_workflow_valid_conge y SET ordre_etape = e.ordre_etape FROM yvs_workflow_etape_validation e WHERE e.id = y.etape;
UPDATE yvs_workflow_valid_doc_caisse y SET ordre_etape = e.ordre_etape FROM yvs_workflow_etape_validation e WHERE e.id = y.etape;
UPDATE yvs_workflow_valid_doc_stock y SET ordre_etape = e.ordre_etape FROM yvs_workflow_etape_validation e WHERE e.id = y.etape;
UPDATE yvs_workflow_valid_facture_achat y SET ordre_etape = e.ordre_etape FROM yvs_workflow_etape_validation e WHERE e.id = y.etape;
UPDATE yvs_workflow_valid_facture_vente y SET ordre_etape = e.ordre_etape FROM yvs_workflow_etape_validation e WHERE e.id = y.etape;
UPDATE yvs_workflow_valid_formation y SET ordre_etape = e.ordre_etape FROM yvs_workflow_etape_validation e WHERE e.id = y.etape;
UPDATE yvs_workflow_valid_mission y SET ordre_etape = e.ordre_etape FROM yvs_workflow_etape_validation e WHERE e.id = y.etape;
UPDATE yvs_workflow_valid_pc_mission y SET ordre_etape = e.ordre_etape FROM yvs_workflow_etape_validation e WHERE e.id = y.etape;
ALTER TABLE yvs_workflow_valid_approvissionnement ENABLE TRIGGER action_on_workflow_approvissionnement;
ALTER TABLE yvs_workflow_valid_bon_provisoire ENABLE TRIGGER action_on_workflow_bon_provisoire;
ALTER TABLE yvs_workflow_valid_conge ENABLE TRIGGER action_on_workflow_conge;
ALTER TABLE yvs_workflow_valid_doc_caisse ENABLE TRIGGER action_on_workflow_doc_caisse;
ALTER TABLE yvs_workflow_valid_doc_stock ENABLE TRIGGER action_on_workflow_doc_stock;
ALTER TABLE yvs_workflow_valid_facture_achat ENABLE TRIGGER action_on_workflow_facture_achat;
ALTER TABLE yvs_workflow_valid_facture_vente ENABLE TRIGGER action_on_workflow_facture_vente;
ALTER TABLE yvs_workflow_valid_mission ENABLE TRIGGER action_on_workflow_mission;


SELECT insert_droit('proj_departement', 'Page des departements', 
	(SELECT id FROM yvs_module WHERE reference = 'proj_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('proj_projet', 'Page des projets', 
	(SELECT id FROM yvs_module WHERE reference = 'proj_'), 16, 'A,B,C','P');
	
SELECT insert_droit('proj_departement_save', 'Créer un département', 
	(SELECT id FROM yvs_page_module WHERE reference = 'proj_departement'), 16, 'A,B,C','R');
	
SELECT insert_droit('proj_departement_update', 'Modifier un département', 
	(SELECT id FROM yvs_page_module WHERE reference = 'proj_departement'), 16, 'A,B,C','R');
	
SELECT insert_droit('proj_departement_delete', 'Supprimer un département', 
	(SELECT id FROM yvs_page_module WHERE reference = 'proj_departement'), 16, 'A,B,C','R');
	
SELECT insert_droit('proj_projet_view', 'Voir tous les projet', 
	(SELECT id FROM yvs_page_module WHERE reference = 'proj_projet'), 16, 'A,B,C','R');
	
SELECT insert_droit('proj_projet_save', 'Créer un projet', 
	(SELECT id FROM yvs_page_module WHERE reference = 'proj_projet'), 16, 'A,B,C','R');
	
SELECT insert_droit('proj_projet_update', 'Modifier un projet', 
	(SELECT id FROM yvs_page_module WHERE reference = 'proj_projet'), 16, 'A,B,C','R');
	
SELECT insert_droit('proj_projet_delete', 'Supprimer un projet', 
	(SELECT id FROM yvs_page_module WHERE reference = 'proj_projet'), 16, 'A,B,C','R');
	
SELECT insert_droit('proj_projet_add_service', 'Ajouter un service à un projet', 
	(SELECT id FROM yvs_page_module WHERE reference = 'proj_projet'), 16, 'A,B,C','R');
	
SELECT insert_droit('gescom_inv_attrib_ecart', 'Attribuer des ecarts inventaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,C','R');
	
SELECT insert_droit('ret_generer_by_ecart', 'Générer la retenue à partir des écarts', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');
	
SELECT insert_droit('ret_generer_by_od', 'Générer la retenue à partir des OD', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');
	
SELECT insert_droit('gescom_inv_update_valeur', 'Modifier la valeur de l''inventaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,C','R');
	

-- Function: compta_action_on_content_journal_retenue_salaire()
-- DROP FUNCTION compta_action_on_content_journal_retenue_salaire();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_retenue_salaire()
  RETURNS trigger AS
$BODY$    
DECLARE
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
IF(EXEC_) THEN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_grh_element_additionel SET comptabilise = TRUE WHERE id = NEW.retenue;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_grh_element_additionel SET comptabilise = FALSE WHERE id = OLD.retenue;
	END IF;
END IF; 
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_retenue_salaire()
  OWNER TO postgres;


-- Trigger: compta_action_on_content_journal_retenue_salaire on yvs_compta_content_journal_retenue_salaire
-- DROP TRIGGER compta_action_on_content_journal_retenue_salaire ON yvs_compta_content_journal_retenue_salaire;
CREATE TRIGGER compta_action_on_content_journal_retenue_salaire
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_retenue_salaire
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_retenue_salaire();

