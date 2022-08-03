ALTER TABLE yvs_prod_nomenclature DROP CONSTRAINT yvs_prod_nomenclature_article_fkey;
ALTER TABLE yvs_prod_detail_pdp DROP CONSTRAINT yvs_detail_pdp_article_fkey;
ALTER TABLE yvs_prod_capacite_poste_charge DROP CONSTRAINT yvs_prod_capacite_poste_charge_articles_fkey;
ALTER TABLE yvs_prod_composant_ordre_fabrication DROP CONSTRAINT yvs_prod_composant_ordre_fabrication_article_fkey;
ALTER TABLE yvs_prod_composant_nomenclature DROP CONSTRAINT yvs_prod_composant_nomenclature_article_fkey;
ALTER TABLE yvs_prod_gamme_article DROP CONSTRAINT yvs_prod_gamme_article_article_fkey;
ALTER TABLE yvs_prod_ordre_fabrication DROP CONSTRAINT yvs_prod_ordre_fabrication_article_fkey;

TRUNCATE yvs_prod_article_production CASCADE; 
DROP TABLE yvs_prod_article_production;

ALTER TABLE yvs_prod_nomenclature
  ADD CONSTRAINT yvs_prod_nomenclature_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_prod_detail_pdp
  ADD CONSTRAINT yvs_detail_pdp_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_prod_capacite_poste_charge
  ADD CONSTRAINT yvs_prod_capacite_poste_charge_articles_fkey FOREIGN KEY (articles)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_prod_composant_ordre_fabrication
  ADD CONSTRAINT yvs_prod_composant_ordre_fabrication_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_prod_composant_nomenclature
  ADD CONSTRAINT yvs_prod_composant_nomenclature_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_prod_gamme_article
  ADD CONSTRAINT yvs_prod_gamme_article_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_prod_ordre_fabrication
  ADD CONSTRAINT yvs_prod_ordre_fabrication_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_prod_nomenclature ADD COLUMN unite_preference character varying;
ALTER TABLE yvs_prod_nomenclature ALTER COLUMN unite_preference SET DEFAULT 'M'::character varying;
COMMENT ON COLUMN yvs_prod_nomenclature.unite_preference IS '''M'' pour unité de masse
''V'' pour unité de volume';

ALTER TABLE yvs_prod_composant_nomenclature DROP COLUMN always_valide;
ALTER TABLE yvs_prod_composant_nomenclature DROP COLUMN debut_validite;
ALTER TABLE yvs_prod_composant_nomenclature DROP COLUMN fin_validite;
ALTER TABLE yvs_prod_composant_nomenclature DROP COLUMN niveau;

ALTER TABLE yvs_prod_composant_nomenclature ADD COLUMN unite bigint;
ALTER TABLE yvs_prod_composant_nomenclature
  ADD CONSTRAINT yvs_prod_composant_nomenclature_unite_fkey FOREIGN KEY (unite)
      REFERENCES yvs_base_unite_mesure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_unite_mesure DROP COLUMN masse;
ALTER TABLE yvs_base_unite_mesure DROP COLUMN longueur;
ALTER TABLE yvs_base_unite_mesure DROP COLUMN volume;
ALTER TABLE yvs_base_unite_mesure DROP COLUMN temps;
