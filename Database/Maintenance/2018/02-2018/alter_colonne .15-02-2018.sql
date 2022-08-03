select alter_action_colonne_key('yvs_prod_operations_of', true, true);
select alter_action_colonne_key('yvs_prod_composant_of', true, true);

ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN suivi_stock_by_operation boolean;
ALTER TABLE yvs_prod_ordre_fabrication ALTER COLUMN suivi_stock_by_operation SET DEFAULT false;

ALTER TABLE yvs_prod_composant_nomenclature ADD COLUMN stockable boolean;
ALTER TABLE yvs_prod_composant_nomenclature ALTER COLUMN stockable SET DEFAULT false;

ALTER TABLE yvs_prod_flux_composant ADD COLUMN taux_composant double precision;
ALTER TABLE yvs_prod_flux_composant ALTER COLUMN taux_composant SET DEFAULT 0;

ALTER TABLE yvs_prod_gamme_article DROP COLUMN unite_temps;
ALTER TABLE yvs_prod_gamme_article ADD COLUMN unite_temps bigint;
ALTER TABLE yvs_prod_gamme_article
  ADD CONSTRAINT yvs_prod_gamme_article_unite_temps_fkey FOREIGN KEY (unite_temps)
      REFERENCES yvs_base_unite_mesure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;