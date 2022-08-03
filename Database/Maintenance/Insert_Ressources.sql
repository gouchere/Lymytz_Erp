INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('production_view_all_date', 'Afficher tous les ordres de productions sans restriction de date', 'Afficher tous les ordres de productions sans restriction de date',
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'));

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('production_view_all_site', 'Afficher les ordres de productions de tous les sites', 'Afficher les ordres de productions de tous les sites',
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'));

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('production_view_all_of', 'Afficher les ordres de productions indépendamment de leurs statuts', 'Afficher les ordres de productions indépendamment de leurs statuts',
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'));


INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('production_cloture_of', 'Clôturer un ordre de production', 'Clôturer un ordre de production',
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'));
	
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_od_valid_max_seuil_montant', 'Valider Montant superieur au seuil', 'Valider un OD dont le montant est superieur au seuil',
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'));
	
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('fv_change_categorie', 'Changer la catégorie comptable', 'Changer la catégorie comptable d''une facture enregistrée',
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fv'));
	
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('p_caiss_payer_acompte', 'Valider les reglements liés aux acomptes', 'Valider les reglements liés aux acomptes',
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_pieces'));
	
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('com_save_hors_limit', 'Création des documents au delai des limites de date', 'Création des documents au delai des limites de date',
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_paramG'));
	
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_del_reg_cout_cheque', 'Supprimer les pieces liées aux couts sur chèque', 'Supprimer les pieces liées aux couts sur les retours de chèques',
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_vente'));
	
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_cloture_exo', 'Cloturer un exercice', 'Cloturer un execirce',
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_exercices'));
	
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_uncloture_exo', 'Ouvrir un execirce', 'Ouvrir un execirce',
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_exercices'));	
	

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('d_stock_edit_when_doc_valid', 'Modifier exceptionnellement le contenu d''un document validé', 'Modifier exceptionnellement le contenu d''un document validé',
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_entree'));	

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('caiss_create_piece', 'Valider une pièce de reglement', 'Valider une pièce de reglement',
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_caisses'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('prod_view_all_site', 'Voir tous les sites de production', 'Voir tous les sites de production',
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_param_g'));

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('prod_view_all_equipe', 'Voir toutes les equipes de production', 'Voir toutes les equipes de production',
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_equipe'));
		
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('fv_can_reduce_prix', 'Appliquer un rabais à la facture', 'Appliquer un rabais à la facture', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fv'));

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('pv_view_all_societe', 'Voir tous les points de vente de la societé', 'Voir tous les points de vente de la societé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_pv'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_od_view_all', 'Voir tous les OD de la socièté', 'Voir tous les OD de la socièté',  
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'));

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_od_view_all_agence', 'Voir tous les OD de l''agence', 'Voir tous les OD de la socièté',  
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'));
	
-- 29-01-2019	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_article_save', 'Créer un article', 'Créer un article',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_article_update', 'Modifier un article', 'Modifier un article',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_article_delete', 'Supprimer un article', 'Supprimer un article',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_article_add_depot', 'Ajouter dans un dépôt', 'Ajouter dans un dépôt',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_article_save_tarifaire', 'Créer un plan tarifaire', 'Créer un plan tarifaire',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_article_defined_comptable', 'Définir la catégorie comptable', 'Définir la catégorie comptable',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_depots_save', 'Créer un dépôt', 'Créer un dépôt',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_depots_update', 'Modifier un dépôt', 'Modifier un dépôt',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_depots_delete', 'Supprimer un dépôt', 'Supprimer un dépôt',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_depots_lie_depot', 'Rattacher un dépôt à d''autres', 'Rattacher un dépôt à d''autres',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_depots_lie_point', 'Rattacher un dépôt à des points de vente', 'Rattacher un dépôt à des points de vente',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_tiers_save', 'Créer un tiers', 'Créer un tiers',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_tiers'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_tiers_update', 'Modifier un tiers', 'Modifier un tiers',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_tiers'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_tiers_delete', 'Supprimer un tiers', 'Supprimer un tiers',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_tiers'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_client_save', 'Créer un client', 'Créer un client',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_client'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_client_update', 'Modifier un client', 'Modifier un client',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_client'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_client_delete', 'Supprimer un client', 'Supprimer un client',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_client'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_client_change_ristourne', 'Changer le plan de ristourne du client', 'Changer le plan de ristourne du client',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_client'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_client_associer_tarifaire', 'Associer un plan tarifaire au client', 'Associer un plan tarifaire au client',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_client'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_fournisseur_save', 'Créer un fournisseur', 'Créer un fournisseur',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_fournisseur'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_fournisseur_update', 'Modifier un fournisseur', 'Modifier un fournisseur',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_fournisseur'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('base_fournisseur_delete', 'Supprimer un fournisseur', 'Supprimer un fournisseur',  
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_fournisseur'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('gescom_inventaire_print_with_pr', 'Imprimer l''inventaire valorisé', 'Imprimer l''inventaire valorisé',  
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('bla_valide_doc', 'Valider les receptions d''achat', 'Valider les receptions d''achat',  
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bla'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('blv_valide_doc', 'Valider les receptions de vente', 'Valider les receptions de vente',  
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_blv'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('fa_valide_doc', 'Valider les factures d''achats', 'Valider les factures d''achats',  
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fa'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('production_view_all_societe', 'Voir les productions de toute la socièté', 'Voir les productions de toute la socièté',  
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_virement_up_users_source', 'Changer le caissier source d''un virement', 'Changer le caissier source d''un virement',  
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_virement'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_virement_view_historique', 'Voir l''historique des virement', 'Voir l''historique des virement',  
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_virement'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_od_change_nature', 'Changer la nature d''une opération diverse', 'Changer la nature d''une opération diverse',  
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('fa_editer_doc', 'Annuler la validation des factures d''achats', 'Annuler la validation des factures d''achats',  
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fa'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('gescom_valide_sortie', 'Valider les documents de sortie de stock', 'Valider les documents de sortie de stock',  
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_sortie'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('gescom_editer_sortie', 'Annuler la validation des documents de sortie de stock', 'Annuler la validation des documents de sortie de stock',  
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_sortie'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('gescom_valide_entree', 'Valider les documents d''entrée de stock', 'Valider les documents d''entrée de stock',  
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_entree'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('gescom_editer_entree', 'Annuler la validation des documents d''entrée de stock', 'Annuler la validation des documents d''entrée de stock',  
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_entree'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('gescom_valide_recond', 'Valider les documents de reconditionnement de stock', 'Valider les documents de reconditionnement de stock',  
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_reconditionnment'));	
	
SELECT insert_droit('compta_reg_fv_change_caissier', 'Changer le caissier lors de la création d''un reglement', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_vente'), 16, 'A,B,C,E,F', 'R');
	
SELECT insert_droit('compta_virement_view_all_users', 'Voir les virements de tous les caissiers', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_virement'), 16, 'A,B,C,E,F', 'R');
	
SELECT insert_droit('compta_virement_view_all_caisse', 'Voir les virements de toutes les caisses', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_virement'), 16, 'A,B,C,E,F', 'R');
	
SELECT insert_droit('bla_editer_doc', 'Rendre editable un document déjà validé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bla'), 16, 'A,B,C,E,F', 'R');
	
SELECT insert_droit('bla_valide_doc', 'Valider un bon de reception', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bla'), 16, 'A,B,C,E,F', 'R');

SELECT insert_droit('ra_view_all_societe', 'Voir toutes les fiches de la société', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration'), 16, 'A,B','R');
	
SELECT insert_droit('ra_view_depot', 'Voir les fiches de tous les dépôts de la société', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration'), 16, 'A,B','R');
	
SELECT insert_droit('ra_view_all_historique', 'Voir les fiches de rations sans restrictions de date', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration'), 16, 'A,B','R');

SELECT insert_droit('ra_view_cloturer', 'Voir les fiches déjà clôturées', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration'), 16, 'A,B','R');
	
SELECT insert_droit('ra_unluck_fiche', 'Débloquer une fiche de ration clôturé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration'), 16, 'A,B','R');
	
SELECT insert_droit('ra_change_jr_usine', 'Modifier le nombre de jour usine', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration'), 16, 'A,B','R');
	
SELECT insert_droit('rist_recaculer_ventes', 'Recalculer la valeur de la ristourne sur les ventes', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_rist'), 16, 'A,B','R');
	
SELECT insert_droit('recalcul_pr', 'Recalculer la valeur du prix de revient', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_valorise'), 16, 'A,B','R');
	
SELECT insert_droit('gescom_mouv_delete', 'Supprimer les mouvements de stocks', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_valorise'), 16, 'A','R');

SELECT insert_droit('gescom_change_creneau_use', 'Modifier un créneau employé déjà utilisé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_creno_p'), 16, 'A,B','R');

SELECT insert_droit('base_view_all_user', 'Voir tous les utilisateurs de la société', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_user_'), 16, 'A,B','R');

SELECT insert_droit('base_view_all_user_groupe', 'Voir tous les utilisateurs du groupe de la société', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_user_'), 16, '','R');
	
SELECT insert_droit('prod_update_all_of', 'Modifier tous les ordres de fabrication', 
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 'A,B,J,F,D','R');
		
SELECT insert_droit('prod_force_declaration', 'Force la déclaration en dehors des marges prévues !', 
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 'A,B,J,F,D','R');

SELECT insert_droit('prod_view_session_all_user', 'Voir les sessions des autres producteurs', 
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 'A,B,J,F,D','R');

SELECT insert_droit('prod_view_session_all_date', 'Voir les sessions sans limitation de date', 
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 'A,B,J,F,D','R');

SELECT insert_droit('fa_update_when_paye', 'Modifier une facture d''achat déja réglé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fa'), 16, 'A','R');
	
SELECT insert_droit('gescom_stock_generer_entree', 'Générer une fiche d''entrée pour ajuster les restes à livrer', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_stock_dep'), 16, 'A','R');	
	
SELECT insert_droit('base_user_fusion', 'Fusionner les données', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_user_'), 16, 'A','R');
	
SELECT insert_droit('stock_clean_all_doc_societe', 'Faire le nettoyage des documents sans contenus', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_user_'), 16, 'A','R');	
	
SELECT insert_droit('fv_generer_entree', 'Generer une fiche d''entree correspondant a la facture de vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fv'), 16, 'A','R');		
		
SELECT insert_droit('gescom_inv_update', 'Modifier une fiche inventaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,D,E','R');	
	
SELECT insert_droit('gescom_inv_editer', 'Annuler une fiche inventaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,C,D,E,F','R');	
	
SELECT insert_droit('gescom_inv_valider', 'Valider une fiche inventaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,C,D,E,F','R');	
	
SELECT insert_droit('base_article_change_reference', 'Modifier la reference de l''article', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'), 16, 'A','R');
	
SELECT insert_droit('compta_od_valide_recette', 'Valider les recettes en OD', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'), 16, 'A,B,C','R');
	
SELECT insert_droit('compta_od_valide_depense', 'Valider les dépenses en OD', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'), 16, 'A,B,C','R');
	
	
SELECT insert_droit('view_all_depot_societe', 'Voir tous les dépôts de la société', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'), 16, 'A,B', 'R');
	

SELECT insert_droit('compta_od_save_all_type', 'Enregistre tous les types D''OD', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'), 16, 'A,B,C', 'R');
	
SELECT insert_droit('compta_od_view_all_type', 'Voir tous les type d''OD', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'), 16, 'A,B,C', 'R');
	
	
SELECT insert_droit('fv_change_num_doc', 'Modifier manuellement la référence d''une facture de vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fv'), 16, 'A,B,C','R');	


SELECT insert_droit('base_depots_add_article_emplacement', 'Ajouter un article a un emplacement', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'), 16, 'A,B,C','R');	

SELECT insert_droit('base_depots_add_creneau', 'Ajouter un créneau horaire au dépôt', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'), 16, 'A,B,C','R');	

SELECT insert_droit('base_depots_add_accessibilite', 'Ajouter une accessibilité au dépôt', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'), 16, 'A,B,C','R');		

SELECT insert_droit('p_caiss_view_all_societe', 'Voir toutes les pieces de caisse de la socièté', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_pieces'), 16, 'A,B,C','R');		

SELECT insert_droit('ret_annuler_when_payer', 'Annuler le prélèvement meme s''il est payé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');		

SELECT insert_droit('grh_regl_sal_fusion', 'Fusionner les éléments de salaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_regl_sal_'), 16, 'A,B,C','R');	
	
SELECT insert_droit('d_div_update_doc_valid', 'Modifier un document déjà validé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'), 16, 'A,B,C','R');	
	
SELECT insert_droit('gescom_print_without_transfert', 'Télécharger la fiche de stock sans tenir compte des transferts', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_stock_dep'), 16, 'A,B,C','R');	
	
SELECT insert_droit('compta_virement_cancel_pass', 'Annuler les virements dans le passé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_virement'), 16, 'A,B,C','R');		
	
SELECT insert_droit('bcv_view_all_doc', 'Voir toutes les commandes de la socièté', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bcv'), 16, 'A,B,C','R');		
	
SELECT insert_droit('bcv_view_only_doc_agence', 'Voir uniquement les commandes de l''agence', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bcv'), 16, 'A,B,C','R');		
	
SELECT insert_droit('bcv_view_only_doc_pv', 'Voir uniquement les commandes de mes points de vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bcv'), 16, 'A,B,C','R');		
	
SELECT insert_droit('bcv_view_only_doc_depot', 'Voir uniquement les commandes de mes dépôts', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bcv'), 16, 'A,B,C','R');			
	
SELECT insert_droit('compta_annule_lettrage_not_equilib', 'Annuler le lettrage meme si l''ecriture n''est pas equilibré', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_saisie_libre'), 16, 'A,B,C','R');	
	
SELECT insert_droit('param_warning_view_all', 'Voir toutes les alertes de la socièté', 
	(SELECT id FROM yvs_page_module WHERE reference = 'param_workflow'), 16, 'A,B,C','R');	
	
SELECT insert_droit('prod_update_ppte_composant', 'Modifier les proprités d''un composant', 
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 'A,B,J,F,D','R');

SELECT insert_droit('stock_update_qte_recu', 'Modifier les quantité receptionnées au transfert', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_transfert'), 16, 'A,B,J,F,D','R');
	
SELECT insert_droit('gescom_update_stock_after_valide', 'Passer un mouvement de stock avant un inventaire validé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,C','R');	
	
SELECT insert_droit('point_valideMe', 'Valider sa propre fiche de présence', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_presenc_'), 16, 'A,B,C','R');
	
SELECT insert_droit('journaux_view_all', 'Voir tous les journaux de la socièté', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_journaux'), 16, 'A,B,C','R');
	
SELECT insert_droit('journaux_view_only_agence', 'Voir uniquement les journaux de l''agence', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_journaux'), 16, 'A,B,C','R');
	
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
	
SELECT insert_droit('gescom_inv_update_valeur', 'Modifier la valeur de l''inventaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,C','R');
	
SELECT insert_droit('ret_generer_by_ecart', 'Générer la retenue à partir des écarts', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');
	
SELECT insert_droit('ret_generer_by_od', 'Générer la retenue à partir des OD', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');	
	
SELECT insert_droit('fv_clean_all_doc_societe', 'Néttoyer les factures de vente sans contenus', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fv'), 16, 'A,B,C','R');
	
SELECT insert_droit('fa_clean_all_doc_societe', 'Néttoyer les factures d''achat sans contenus', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fa'), 16, 'A,B,C','R');
	
SELECT insert_droit('ret_marque_regle_prelevement', 'Marqué un prélévement comme réglé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');
	
SELECT insert_droit('ret_suspendre_prelevement', 'Suspendre un prélévement', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');
	
SELECT insert_droit('ret_delete_prelevement', 'Supprimer un prélévement', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_prelvm_ret_'), 16, 'A,B,C','R');
	
SELECT insert_droit('tr_add_content_after_valide', 'Ajouter un contenu dans un transfert valide', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_transfert'), 16, 'A,B,C','R');
	
SELECT insert_droit('rist_change_agence', 'Changer l''agence lors du calcul de la ristourne' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_rist'), 16, 'A,B,C','R');
	
SELECT insert_droit('base_conditionnement_update', 'Modifier un conditionnement' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'), 16, 'A,B,C','R');
	
SELECT insert_droit('prod_gammes_update_num_ope', 'Modifier le numero d''une opération' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_gammes'), 16, 'A,B,C','R');
	
SELECT insert_droit('ra_view_all', 'Voir toutes les fiches de ration' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration'), 16, 'A,B,C','R');
	
SELECT insert_droit('ra_view_historique', 'Voir les fiches de ration sur un nombre de jour' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration'), 16, 'A,B,C','R');
	
SELECT insert_droit('fa_view_totaux', 'Voir les totaux d''une facture d''achat' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fa'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');
	
SELECT insert_droit('gescom_entree_view_totaux', 'Voir les totaux d''une fiche d''entrée' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_entree'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');
	
SELECT insert_droit('gescom_sortie_view_totaux', 'Voir les totaux d''une fiche de sortie' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_sortie'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');
	
SELECT insert_droit('base_client_print_actif', 'Télécharger les clients actif' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_client'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');	

SELECT insert_droit('change_requiere_lot_article_depot', 'Changer l''utilisation de lot pour un article meme s''il y''a du stock', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'), 16, 'A,B,C','R');
 	
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

SELECT insert_droit('compta_change_compte_content', 'Changer le compte général d''un contenu de journal', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_saisie_libre'), 16, 'A,B,C','R');
	
SELECT insert_droit('base_depot_send_all_stock', 'Transferer tout le stock vers un autre dépot', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'), 16, 'A,B,C','R');
	
SELECT insert_droit('compta_ope_client_change_agence_equilibre', 'Changer l''agence lors de l''équilibre', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_ope_client'), 16, 'A,B,C','R');
	
SELECT insert_droit('compta_ope_fsseur_change_agence_equilibre', 'Changer l''agence lors de l''équilibre', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_ope_fsseur'), 16, 'A,B,C','R');
	
	