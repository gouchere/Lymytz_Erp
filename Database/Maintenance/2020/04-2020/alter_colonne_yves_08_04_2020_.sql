ALTER TABLE yvs_prod_composant_op ADD COLUMN type_cout CHARACTER;
ALTER TABLE yvs_prod_flux_composant ADD COLUMN type_cout CHARACTER ;

ALTER TABLE yvs_prod_nomenclature_site DROP CONSTRAINT yvs_prod_nomenclature_site_nomenclature_fkey;
ALTER TABLE yvs_prod_nomenclature_site
  ADD CONSTRAINT yvs_prod_nomenclature_site_nomenclature_fkey FOREIGN KEY (nomenclature)
      REFERENCES yvs_prod_nomenclature (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
	 
ALTER TABLE yvs_prod_gamme_site DROP CONSTRAINT yvs_prod_gamme_site_gamme_fkey;

ALTER TABLE yvs_prod_gamme_site
  ADD CONSTRAINT yvs_prod_gamme_site_gamme_fkey FOREIGN KEY (gamme)
      REFERENCES yvs_prod_gamme_article (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

