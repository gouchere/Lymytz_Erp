ALTER TABLE yvs_base_articles_template DROP COLUMN masse_net;
ALTER TABLE yvs_base_articles_template DROP COLUMN prix_min;
ALTER TABLE yvs_base_articles_template DROP COLUMN pua;
ALTER TABLE yvs_base_articles_template DROP COLUMN puv;
ALTER TABLE yvs_base_articles_template DROP COLUMN remise;

ALTER TABLE yvs_base_articles_template RENAME COLUMN ref_template TO ref_art;
ALTER TABLE yvs_base_articles_template ADD COLUMN code_barre character varying;

ALTER TABLE yvs_base_articles_template ADD COLUMN unite_volume integer;
ALTER TABLE yvs_base_articles_template
  ADD CONSTRAINT yvs_base_articles_template_unite_volume_fkey FOREIGN KEY (unite_volume)
      REFERENCES yvs_base_unite_mesure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;	 
ALTER TABLE yvs_base_articles_template ADD COLUMN societe integer;
ALTER TABLE yvs_base_articles_template
  ADD CONSTRAINT yvs_base_articles_template_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;	  
	  
ALTER TABLE yvs_base_plan_tarifaire ADD COLUMN template bigint;
ALTER TABLE yvs_base_plan_tarifaire
  ADD CONSTRAINT yvs_base_plan_tarifaire_template_fkey FOREIGN KEY (template)
      REFERENCES yvs_base_articles_template (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_article_categorie_comptable ADD COLUMN template bigint;
ALTER TABLE yvs_base_article_categorie_comptable
  ADD CONSTRAINT yvs_base_compte_article_template_fkey FOREIGN KEY (template)
      REFERENCES yvs_base_articles_template (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;