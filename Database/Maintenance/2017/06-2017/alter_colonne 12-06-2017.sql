ALTER TABLE yvs_stat_export_etat ADD COLUMN table_principal character varying;
ALTER TABLE yvs_stat_export_etat ADD COLUMN colonne_principal character varying;

ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN statut_livree character varying(1) default 'W';