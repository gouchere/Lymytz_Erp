-- Table: yvs_prod_ordre_articles

-- DROP TABLE yvs_prod_ordre_articles;

CREATE TABLE yvs_prod_ordre_articles
(
  id bigserial NOT NULL,
  article bigint,
  ordre bigint,
  quantite double precision,
  nomenclature bigint,
  gamme bigint,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  CONSTRAINT yvs_prod_ordre_articles_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_ordre_articles_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_ordre_articles_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_ordre_articles_gamme_fkey FOREIGN KEY (gamme)
      REFERENCES yvs_prod_gamme_article (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_ordre_articles_nomenclature_fkey FOREIGN KEY (nomenclature)
      REFERENCES yvs_prod_nomenclature (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_ordre_articles_ordre_fkey FOREIGN KEY (ordre)
      REFERENCES yvs_prod_ordre_fabrication (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_ordre_articles
  OWNER TO postgres;
