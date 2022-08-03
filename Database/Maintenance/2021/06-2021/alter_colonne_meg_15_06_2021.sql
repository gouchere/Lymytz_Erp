-- Table: yvs_base_tiers_document
-- DROP TABLE yvs_base_tiers_document;
CREATE TABLE yvs_base_tiers_document
(
  id bigserial NOT NULL,
  titre character varying,
  fichier character varying,
  "extension" character varying,
  tiers bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_base_tiers_document_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_tiers_document_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_tiers_document_tiers_fkey FOREIGN KEY (tiers)
      REFERENCES yvs_base_tiers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_tiers_document
  OWNER TO postgres;
  

ALTER TABLE yvs_base_articles ADD COLUMN requiere_lot boolean DEFAULT false;
ALTER TABLE yvs_base_depots ADD COLUMN requiere_lot boolean DEFAULT false;