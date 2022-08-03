INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES 
('fa_apply_remise', 'Appliquer une remise', 'Appliquer une remise', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_fa'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES 
('bla_view_all_doc', 'Voir tous les documents', 'Voir tous les documents', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_bla'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES 
('bla_view_only_doc_agence', 'Voir uniquement les documents de l''agence', 'Voir uniquement les documents de l''agence', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_bla'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES 
('bla_view_only_doc_depot', 'Voir uniquement les documents du depot', 'Voir uniquement les documents du depot', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_bla'), 16);