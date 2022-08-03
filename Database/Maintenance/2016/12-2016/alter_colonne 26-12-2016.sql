ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN puv_min double precision;
ALTER TABLE yvs_com_contenu_doc_vente ALTER COLUMN puv_min SET DEFAULT 0;