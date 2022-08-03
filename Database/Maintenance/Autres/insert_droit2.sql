INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
    VALUES ('Paramètres Généreaux', 'gescom_paramG', 'Paramètres Généreaux', 3, 16);
INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Paramètres Commericaux', 'gescom_paramC', 'Paramètres Commericaux', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Personnel', 'gescom_personnel', 'Personnel', 3, 16);


INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Point de vente', 'gescom_pv', 'Point de vente', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Crénaux horaire', 'gescom_creno_p', 'Creneaux personnel', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Commission', 'gescom_commission', 'Paramétrage des commissions', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Ristourne', 'gescom_rist', 'Paramétrage des ristournes', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Plan tarifaires', 'gescom_cat_tarif', 'Paramétrage des plans tarifaires', 3, 16);
    INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Remises', 'gescom_remise', 'Gestion des remises', 3, 16);



INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Fiche approvisionnement', 'gescom_fiche_appro', 'Fiche approvisionnement', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Commande achat', 'gescom_bca', 'Commande achat', 3, 16);


INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Facture achat', 'gescom_fa', 'Facture achat', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Avoir Achat', 'gescom_avoira', 'Avoir Achat', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Livraison Achat', 'gescom_bla', 'Livraison achat', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Retour achat', 'gescom_retoura', 'retour achat', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Entrée en stock', 'gescom_entree', 'Entrée en stock', 3, 16);
    INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Sortie de stock', 'gescom_sortie', 'Sortie de stock', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Transfert stock', 'gescom_transfert', 'Transfert de stock', 3, 16);
    INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Fiche d''inventaire', 'gescom_inventaire', 'Fiche d''inventaire', 3, 16);


INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Stock /Depots', 'gescom_stock_dep', 'Stock / Dépôts', 3, 16);
    INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Valorisation des stocks', 'gescom_valorise', 'Valorisation des stocks', 3, 16);


INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Commande vente', 'gescom_bcv', 'Commande vente', 3, 16);


INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Facture vente', 'gescom_fv', 'Facture vente', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Avoir Vente', 'gescom_avoirv', 'Avoir Vente', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Livraison Vente', 'gescom_blv', 'Livraison Vente', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Retour vente', 'gescom_retourv', 'retour vente', 3, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)
            
    VALUES ('Tableau de bord général', 'gescom_tbG', 'Tableau de bord général', 3, 16);

--------------------------------Factures de vente------------------------------------
INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_valide_doc', 'Valider une facture de vente', 'Valider une facture de vente', 62, 16);
	
INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_save_doc', 'Enregistrer/Modifier une facture de vente', 'Enregistrer/Modifier une facture de vente', 62, 16);
	
INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_cancel_doc_valid', 'Annuler une facture validé', 'Annuler une facture validé', 62, 16);

INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_delete_doc', 'Supprimer une facture de vente', 'Supprimer une facture de vente', 62, 16);

INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_update_header', 'Modifier l''entête d''une facture', 'Modifier l''entête d''une facture', 62, 16);


INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_save_in_past', 'Enregistrer une facture au delà de la marge de retard autorisé', 'Enregistrer une facture au delà de la marge de retard autorisé', 62, 16);


INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_apply_rem_all', 'Appliquer une remise sur la facture', 'Appliquer une remise sur la facture', 62, 16);


INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_apply_remise', 'Modifier la remise d''une ligne de vente', 'Modifier la remise d''une ligne de vente', 62, 16);

INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_apply_rabais', 'Appliquer un rabais sur une ligne de vente', 'Appliquer un rabais sur une ligne de vente', 62, 16);

INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_create_client', 'Créer un clients', 'Créer un client à partir du formulaire de vente', 62, 16);

INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_create_client', 'Créer un clients', 'Créer un client à partir du formulaire de vente', 62, 16);

INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_create_reglement', 'Créer un Règlement', 'Créer un règlement à partir du formulaire de vente', 62, 16);

INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_clean_header', 'Nettoyer l''en-tête', 'Nettoyer l''entête de vente', 62, 16);

INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_livrer', 'Livrer', 'Livrer à partir du formulaire de vente', 62, 16);
	INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('fv_livrer_in_all_depot', 'Livrer dans tous les dépôts de l''agence', 'Dans n''importe quelle dépôt de l'agence', 62, 16);
----------------------------------------Donées de base------------------------------------------

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Paramétrage génarale', 'gen_view_parametre', 'Voir la page de paramétrage', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Dictionnaires', 'gen_view_dictionnaire', 'Voir la page du dictionnaires', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Modèles de référence', 'gen_view_model_ref', 'Voir la page des modèle de référence', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Articles', 'base_view_article', 'Voir la page des articles', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Templates tiers', 'base_view_template_tiers', 'Voir la page des templates tiers', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Tiers', 'base_view_tiers', 'Voir la page des tiers', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Clients', 'base_view_client', 'Voir la page des clients', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Fournisseurs', 'base_view_fournisseur', 'Voir la page des fournisseurs', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Dépots', 'base_view_depots', 'Voir la page des dépôts', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Exercices', 'base_view_exercices', 'Voir la page des exercices', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Plan des comptes', 'base_view_plan_comptable', 'Voir la page des comptes', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Caisses de trésoreries', 'base_view_caisses', 'Voir la page des caisses', 9, 16);

	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Journaux comptable', 'base_view_journaux', 'Journaux comptable', 9, 16);

-------------------------------------Compta finance-------------------------------------
	
INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Règlements ventes', 'compta_view_reg_vente', 'Voir la page de règlement des ventes', 5, 16);

INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Règlements Achat', 'compta_view_reg_achat', 'Voir la page de règlement des achat', 5, 16);
	
	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Règlements frais missions', 'compta_view_reg_missions', 'Voir la page de gestion des pièces de caisses missions', 5, 16);
	
	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Règlements des virements', 'compta_view_reg_virement', 'Voir la page gestion des virements', 5, 16);
	
	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Règlements Des pièces de caisses', 'compta_view_reg_pieces', 'Voir la page de règlement des pièces', 5, 16);
	
	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Paramétrage des schémas d''ecriture  ventes', 'compta_view_schema_s', 'Voir la page de paramétrage des schémas d''écriture', 5, 16);
	
	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Saisie comptable libre', 'compta_view_saisie_libre', 'Voir la page Saisie des pièces comptable', 5, 16);
	
	INSERT INTO yvs_page_module(
            libelle, reference, description, module, author)            
    VALUES ('Gestion des OD de caisse', 'compta_view_op_div', 'Gestion des OD trésoreries', 5, 16);

	------------------------------------ Piece caisses--------------------------------------------------

	
INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('p_caiss_view_all', 'Voir toutes les pièces', 'Voir toutes les pièces sans restrictions', 72, 16);
	
	INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('p_caiss_view_all_limit_time', 'Voir toutes les pièces avec une limitationdans le temps', 'Voir toutes les pièces avec une limitationdans le temps', 72, 16);
	
	INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('p_caiss_view_only_mine', 'Voir Selement les pièces des caisses dont je suis responsable', 'Voir Selement les pièces des caisses dont je suis responsable', 72, 16);
	
	INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('p_caiss_payer', 'Valider le paiement d''une pièce', 'Valider le paiement d''une pièce', 72, 16);
	
	INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('p_caiss_cancel_already_pay', 'Annuler le statut payé d''une pièce', 'Annuler le statut payé d''une pièce', 72, 16);
	
	INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('p_caiss_delete_payer', 'Supprimer une pièce enregistré', 'Supprimer une pièce enregistré', 72, 16);

	--------------------------------------Caisses----------------------------------------------------
	INSERT INTO yvs_ressources_page(reference, libelle, description, page_module, author)
    VALUES ('caiss_view_all', 'Voir toutes les caisses', 'l''Ensemble des caisses', 87, 16);
	
	
	
/****************************************************vues des ENTREE EN STOCK******************************************************************/
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('fv_view_only_doc_depot', 'Voir uniquement les factures liées à mon dépôt', 'Voir uniquement les factures liées à mon dépôt', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_fv'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('blv_view_all_doc', 'Voir tous les bons de livraison', 'Voir tous les bons de livraison', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_blv'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('blv_view_only_doc_agence', 'Voir les bons de livraison de l''agence', 'Voir les bons de livraison de l''agence', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_blv'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('blv_view_only_doc_depot', 'Voir uniquement les bons de livraison de mes dépôts', 'Voir uniquement les bons de livraison de mes dépôts', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom