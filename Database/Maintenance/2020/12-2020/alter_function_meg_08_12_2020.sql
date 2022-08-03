INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, defined_livraison, defined_update, description, defined_reglement, workflow)
    VALUES ('ORDRE_FABRICATION_DECLARE', 'yvs_prod_ordre_fabrication', FALSE, FALSE, 'Ordre de fabrication non déclarée', FALSE, FALSE);
	
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, defined_livraison, defined_update, description, defined_reglement, workflow)
    VALUES ('ORDRE_FABRICATION_TERMINE', 'yvs_prod_ordre_fabrication', FALSE, FALSE, 'Ordre de fabrication non terminée', FALSE, FALSE);
	
SELECT set_config('myapp.EXECUTE_MVT_STOCK', 'false', false);
SELECT set_config('myapp.EXECUTE_TRIGGER_ALERTE', 'false', false);
UPDATE yvs_com_doc_stocks SET author_transmis = author, date_transmis = date_update;
SELECT set_config('myapp.EXECUTE_MVT_STOCK', 'true', false);
SELECT set_config('myapp.EXECUTE_TRIGGER_ALERTE', 'true', false);