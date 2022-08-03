-- ALTER TABLE yvs_workflow_etape_validation DROP COLUMN can_edit_bp_here;

ALTER TABLE yvs_workflow_etape_validation ADD COLUMN can_edit_bp_here boolean;
ALTER TABLE yvs_workflow_etape_validation ALTER COLUMN can_edit_bp_here SET DEFAULT false;

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('mission_param_avance', 'Paramétrage avancé', 'Autoriser le paramétrage avancé des ordres de missions (Impression, liaison avec les Bp)', (SELECT id FROM yvs_page_module WHERE reference = 'grh_mission_'));