CREATE INDEX yvs_com_doc_achats_statut
  ON yvs_com_doc_achats
  USING btree
  (statut COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_achats_statut_regle
  ON yvs_com_doc_achats
  USING btree
  (statut_regle COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_achats_statut_livre
  ON yvs_com_doc_achats
  USING btree
  (statut_livre COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_ventes_statut
  ON yvs_com_doc_ventes
  USING btree
  (statut COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_ventes_statut_regle
  ON yvs_com_doc_ventes
  USING btree
  (statut_regle COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_ventes_statut_livre
  ON yvs_com_doc_ventes
  USING btree
  (statut_livre COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_stocks_statut
  ON yvs_com_doc_stocks
  USING btree
  (statut COLLATE pg_catalog."default");
  
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES ('gescom_ration', 'Page rations', 'Page des rations', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('ra_attribuer', 'Attribuer ration', 'Attribuer une ration', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_ration'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('ra_valider_fiche', 'Valider fiche ration', 'Valider une fiche de ration', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_ration'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('ra_save_fiche', 'Créer fiche ration', 'Crée une fiche de ration', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_ration'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('ra_annuler_fiche', 'Annuler fiche ration', 'Annuler une fiche de ration', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_ration'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('ra_delete_fiche', 'Supprimer fiche ration', 'Supprimer une fiche de ration', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_ration'), 16);


