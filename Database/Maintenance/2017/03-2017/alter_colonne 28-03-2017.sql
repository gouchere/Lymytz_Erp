ALTER TABLE yvs_base_articles DROP COLUMN conditionnement;
ALTER TABLE yvs_base_articles_template DROP COLUMN conditionnement;
DROP TABLE yvs_base_conditionnement;

ALTER TABLE yvs_base_articles ADD COLUMN unite_stockage bigint;
ALTER TABLE yvs_base_articles ADD COLUMN unite_vente bigint;

ALTER TABLE yvs_base_articles_template ADD COLUMN unite_stockage bigint;
ALTER TABLE yvs_base_articles_template ADD COLUMN unite_vente bigint;
ALTER TABLE yvs_base_articles_template ADD COLUMN taux_equivalence_unite double precision DEFAULT 0;

ALTER TABLE yvs_base_articles
  ADD CONSTRAINT yvs_articles_unite_stockage_fkey FOREIGN KEY (unite_stockage)
      REFERENCES yvs_base_unite_mesure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_base_articles
  ADD CONSTRAINT yvs_articles_unite_vente_fkey FOREIGN KEY (unite_vente)
      REFERENCES yvs_base_unite_mesure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_base_articles_template
  ADD CONSTRAINT yvs_articles_unite_stockage_fkey FOREIGN KEY (unite_stockage)
      REFERENCES yvs_base_unite_mesure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_base_articles_template
  ADD CONSTRAINT yvs_articles_unite_vente_fkey FOREIGN KEY (unite_vente)
      REFERENCES yvs_base_unite_mesure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
CREATE TABLE yvs_base_conditionnement
(
  id bigserial NOT NULL,
  article bigint,
  conditionnement bigint,
  unite character varying,
  author bigint,
  CONSTRAINT yvs_conditionnement_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_conditionnement_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_conditionnement_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_conditionnement_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_unite_mesure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_conditionnement
  OWNER TO postgres;