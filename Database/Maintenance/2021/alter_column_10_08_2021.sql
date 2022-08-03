ALTER TABLE yvs_com_parametre_vente ADD COLUMN livre_bcv_without_paye boolean DEFAULT false;

ALTER TABLE yvs_workflow_model_doc ADD COLUMN nature character varying default 'W';