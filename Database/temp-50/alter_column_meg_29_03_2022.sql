ALTER TABLE yvs_base_parametre ADD COLUMN nombre_page_min integer;
ALTER TABLE yvs_base_parametre ALTER COLUMN nombre_page_min SET DEFAULT 10;