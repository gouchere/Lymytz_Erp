UPDATE yvs_grh_contrat_emps SET statut = 'C' WHERE actif IS TRUE;

SELECT insert_droit('bcv_delete_doc', 'Supprimer un bon de commande vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bcv'), 16, 'A,B,C','R');	

SELECT insert_droit('brv_delete_doc', 'Supprimer un bon de retour vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_retourv'), 16, 'A,B,C','R');	

SELECT insert_droit('fav_delete_doc', 'Supprimer un bon d''avoir vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_avoirv'), 16, 'A,B,C','R');

SELECT insert_droit('blv_delete_doc', 'Supprimer un bon de livraison vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_blv'), 16, 'A,B,C','R');

SELECT insert_droit('bra_delete_doc', 'Supprimer un bon de retour achat', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_retoura'), 16, 'A,B,C','R');	

SELECT insert_droit('faa_delete_doc', 'Supprimer un bon d''avoir achat', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_avoira'), 16, 'A,B,C','R');

SELECT insert_droit('bla_delete_doc', 'Supprimer un bon de reception achat', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bla'), 16, 'A,B,C','R');
	
SELECT insert_droit('gescom_pfv_delete', 'Supprimer un profoma vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_pfv'), 16, 'A,B,C','R');
	
SELECT insert_droit('gescom_delete_recond', 'Supprimer une fiche de reconditionnement', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_reconditionnment'), 16, 'A,B,C','R');
	
SELECT insert_droit('appro_delete_fiche', 'Supprimer une fiche d''approvisionnement', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fiche_appro'), 16, 'A,B,C','R');
	
SELECT insert_droit('gescom_inv_delete', 'Supprimer une fiche d''inventaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,C','R');

SELECT insert_droit('gescom_delete_entree', 'Supprimer un bon d''entree en stock', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_entree'), 16, 'A,B,C','R');

SELECT insert_droit('gescom_delete_sortie', 'Supprimer un bon de sortie en stock', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_sortie'), 16, 'A,B,C','R');

SELECT insert_droit('tr_delete_doc', 'Supprimer un fiche de transfert', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_transfert'), 16, 'A,B,C','R');

SELECT insert_droit('base_user_attrib_code_acces', 'Attribuer un code d''accès à un utilisateur', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_user_'), 16, 'A,B,C','R');

SELECT insert_droit('base_user_active', 'Activer / Désactiver un utilisateur', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_user_'), 16, 'A,B,C','R');

SELECT insert_droit('base_tiers_attrib_ration', 'Attribuer un code ration a un tiers', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_tiers'), 16, 'A,B,C','R');

SELECT insert_droit('grh_contrat_change_statut', 'Changer le statut d''un contrat', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_contrat_'), 16, 'A,B,C','R');

SELECT insert_droit('grh_bull_add_regle', 'Ajouter une regle a un bulletin', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_bull_'), 16, 'A,B,C','R');