CREATE TABLE yvs_prod_gamme_article_article
(
  id bigserial NOT NULL,
  article bigint,
  gamme bigint,
  date_save date,
  date_update date,
  author bigint,
  actif boolean,
  CONSTRAINT yvs_prod_gamme_article_article_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_gamme_article_article_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_gamme_article_article_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_gamme_article_article_gamme_fkey FOREIGN KEY (gamme)
      REFERENCES yvs_prod_gamme_article (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_gamme_article_article
  OWNER TO postgres;
