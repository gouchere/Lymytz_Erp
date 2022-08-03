UPDATE yvs_base_article_depot
   SET  
       suivi_stock=true
 WHERE suivi_stock!=false;
	ALTER TABLE yvs_prod_composant_nomenclature  RENAME COLUMN rebut to coefficient
	
ALTER TABLE yvs_prod_composant_of ADD COLUMN coefficient double precision;
ALTER TABLE yvs_prod_composant_of ALTER COLUMN coefficient SET DEFAULT 1;
DROP TRIGGER after_update_composant_of ON yvs_prod_composant_of;
DROP FUNCTION update_composant_of();

ALTER TABLE yvs_prod_parametre ADD COLUMN suivi_op_requis boolean;
ALTER TABLE yvs_prod_parametre ALTER COLUMN suivi_op_requis SET DEFAULT true;

ALTER TABLE yvs_prod_parametre ADD COLUMN num_cmde_requis boolean;
ALTER TABLE yvs_prod_parametre ALTER COLUMN num_cmde_requis SET DEFAULT false;

INSERT INTO yvs_page_module(
             libelle, reference, description, module, author, date_update, 
            date_save)
    VALUES ( 'Paramètres Généraux', 'ges_prod_param_g', 'Paramétrage généreaux', (SELECT id FROM yvs_module WHERE reference = 'prod_'), 16, current_timestamp, 
            current_timestamp);
INSERT INTO yvs_page_module(
             libelle, reference, description, module, author, date_update, 
            date_save)
    VALUES ( 'Gestion des nomenclatures', 'ges_prod_nomenclature', 'Gestion des nomenclatures', (SELECT id FROM yvs_module WHERE reference = 'prod_'), 16, current_timestamp, 
            current_timestamp);

INSERT INTO yvs_page_module(
             libelle, reference, description, module, author, date_update, 
            date_save)
    VALUES ( 'Gestion des Postes de charges', 'ges_prod_poste_charge', 'Gestion des Postes de charges', (SELECT id FROM yvs_module WHERE reference = 'prod_'), 16, current_timestamp, 
            current_timestamp);


INSERT INTO yvs_page_module(
             libelle, reference, description, module, author, date_update, 
            date_save)
    VALUES ( 'Gestion des gammes de fabrication', 'ges_prod_gammes', 'Gestion des gammes de fabrications', (SELECT id FROM yvs_module WHERE reference = 'prod_'), 16, current_timestamp, 
            current_timestamp);

INSERT INTO yvs_page_module(
             libelle, reference, description, module, author, date_update, 
            date_save)
    VALUES ( 'Gestion des Ordres de fabrication', 'ges_prod_of', 'Gestion des Ordres de fabrications', (SELECT id FROM yvs_module WHERE reference = 'prod_'), 16, current_timestamp, 
            current_timestamp);

INSERT INTO yvs_page_module(
             libelle, reference, description, module, author, date_update, 
            date_save)
    VALUES ( 'Conditionnement Production', 'ges_prod_conditionnement', 'Conditionnement Production ', (SELECT id FROM yvs_module WHERE reference = 'prod_'), 16, current_timestamp, 
            current_timestamp);

INSERT INTO yvs_ressources_page( reference_ressource, libelle, description, page_module, author, 
            date_update, date_save)
    VALUES ('prod_launch_of', 'Autoriser le lancement d''un OF', 'Autoriser le lancement d''un OF', (SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 
            current_timestamp, current_timestamp);
INSERT INTO yvs_ressources_page( reference_ressource, libelle, description, page_module, author, 
            date_update, date_save)
    VALUES ('prod_update_of_encours', 'Modifier un ordre en cours', 'Modifier un ordre en cours', (SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 
            current_timestamp, current_timestamp);

INSERT INTO yvs_ressources_page( reference_ressource, libelle, description, page_module, author, 
            date_update, date_save)
    VALUES ('prod_valid_declaration_prod', 'Valider une déclaration de fabrication', 'Valider une déclaration de fabrication', (SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 
            current_timestamp, current_timestamp);
			