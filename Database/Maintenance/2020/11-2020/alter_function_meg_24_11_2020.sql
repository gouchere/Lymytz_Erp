INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, defined_livraison, defined_update, description, defined_reglement, workflow)
    VALUES ('LOWER_MARGIN', 'yvs_com_contenu_doc_vente', FALSE, FALSE, 'Marge insuffisante', FALSE, FALSE);
	
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, defined_livraison, defined_update, description, defined_reglement, workflow)
    VALUES ('CP_UPPER_PR', 'yvs_prod_declaration_production', FALSE, FALSE, 'Cout de production superieur au PUV', FALSE, FALSE);
	
SELECT insert_droit('fv_clean_all_doc_societe', 'Néttoyer les factures de vente sans contenus', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fv'), 16, 'A,B,C','R');
	
SELECT insert_droit('fa_clean_all_doc_societe', 'Néttoyer les factures d''achat sans contenus', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fa'), 16, 'A,B,C','R');	
	
UPDATE yvs_base_tiers y SET agence = e.agence FROM yvs_grh_employes e WHERE e.compte_tiers = y.id;

UPDATE yvs_grh_detail_prelevement_emps SET date_begin = date_prelevement;
	
SELECT insert_droit('ret_marque_regle_prelevement', 'Marqué un prélévement comme réglé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');
	
SELECT insert_droit('ret_suspendre_prelevement', 'Suspendre un prélévement', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');
	
SELECT insert_droit('ret_delete_prelevement', 'Supprimer un prélévement', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');
	
SELECT insert_droit('tr_add_content_after_valide', 'Ajouter un contenu dans un transfert valide', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_transfert'), 16, 'A,B,C','R');
