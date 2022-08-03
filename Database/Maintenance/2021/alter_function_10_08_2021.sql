SELECT insert_droit('gescom_stock_view_totaux', 'Voir le total en valeur des stocks' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_stock_dep'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');
	
UPDATE yvs_workflow_model_doc SET nature = 'A' WHERE workflow IS FALSE;

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, date_update, date_save, defined_livraison, defined_update, description, defined_reglement, workflow, nature)
VALUES ('ARTICLE', 'yvs_base_articles', current_timestamp, current_timestamp, false, false, 'Nouvel article', false, false , 'I');

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, date_update, date_save, defined_livraison, defined_update, description, defined_reglement, workflow, nature)
VALUES ('CLIENT', 'yvs_com_client', current_timestamp, current_timestamp, false, false, 'Nouveau client', false, false , 'I');

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, date_update, date_save, defined_livraison, defined_update, description, defined_reglement, workflow, nature)
VALUES ('USERS', 'yvs_users', current_timestamp, current_timestamp, false, false, 'Nouvel utilisateur', false, false , 'I');

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, date_update, date_save, defined_livraison, defined_update, description, defined_reglement, workflow, nature)
VALUES ('FOURNISSEUR', 'yvs_base_fournisseur', current_timestamp, current_timestamp, false, false, 'Nouveau fournisseur', false, false , 'I');

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, date_update, date_save, defined_livraison, defined_update, description, defined_reglement, workflow, nature)
VALUES ('EMPLOYE', 'yvs_grh_employes', current_timestamp, current_timestamp, false, false, 'Nouvel employe', false, false , 'I');

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, date_update, date_save, defined_livraison, defined_update, description, defined_reglement, workflow, nature)
VALUES ('CAISSE', 'yvs_base_caisse', current_timestamp, current_timestamp, false, false, 'Nouvelle caisse', false, false , 'I');

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, date_update, date_save, defined_livraison, defined_update, description, defined_reglement, workflow, nature)
VALUES ('DEPOT', 'yvs_base_depots', current_timestamp, current_timestamp, false, false, 'Nouveau dépôt', false, false , 'I');

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, date_update, date_save, defined_livraison, defined_update, description, defined_reglement, workflow, nature)
VALUES ('POINTVENTE', 'yvs_base_point_vente', current_timestamp, current_timestamp, false, false, 'Nouveau point de vente', false, false , 'I');

UPDATE yvs_base_articles SET suivi_en_stock = TRUE WHERE categorie IN ('MARCHANDISE', 'NEGOCES') AND suivi_en_stock IS FALSE;