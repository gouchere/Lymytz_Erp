-- Table: yvs_base_articles_avis
-- DROP TABLE yvs_base_articles_avis;
CREATE TABLE yvs_base_articles_avis
(
  id bigserial NOT NULL,
  note integer,
  commentaire character varying,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  auteur character varying,
  telephone character varying,
  email character varying,
  article bigint,
  execute_trigger character varying DEFAULT 'OK'::character varying,
  CONSTRAINT yvs_base_articles_avis_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_articles_avis_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_articles_avis
  OWNER TO postgres;
