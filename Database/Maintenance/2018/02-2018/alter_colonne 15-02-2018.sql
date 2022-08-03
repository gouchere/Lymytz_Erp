ALTER TABLE yvs_prod_operations_of ADD COLUMN nb_machine INTEGER DEFAULT 0;
ALTER TABLE yvs_prod_operations_of ADD COLUMN nb_main_oeuvre INTEGER DEFAULT 0;

ALTER TABLE yvs_prod_composant_nomenclature DROP CONSTRAINT yvs_prod_composant_nomenclature_unite_fkey;

ALTER TABLE yvs_prod_composant_nomenclature
  ADD CONSTRAINT yvs_prod_composant_nomenclature_unite_fkey FOREIGN KEY (unite)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_article_approvisionnement ADD COLUMN conditionnement bigint;

ALTER TABLE yvs_com_article_approvisionnement
  ADD CONSTRAINT yvs_com_article_approvisionnement_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author)
    VALUES ('appro_valid_fiche', 'Valider une fiche d''approvisionnement ', 'Valider une fiche d''approvisionnement ', (SELECT id FROM yvs_page_module WHERE reference='gescom_fiche_appro' LIMIT 1), 16);

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author)
    VALUES ('appro_cancel_fiche', 'Annuler une fiche d''approvisionnement déjà validé', 'Annuler une fiche d''approvisionnement déjà validé', (SELECT id FROM yvs_page_module WHERE reference='gescom_fiche_appro' LIMIT 1), 16);
