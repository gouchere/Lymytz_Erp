 ALTER TABLE yvs_com_rabais DROP CONSTRAINT yvs_com_rabais_article_fkey;

ALTER TABLE yvs_com_rabais
  ADD CONSTRAINT yvs_com_rabais_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_conditionnement_point (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
