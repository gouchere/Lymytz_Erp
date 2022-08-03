ALTER TABLE yvs_base_article_fournisseur ADD COLUMN pua_ttc boolean;
ALTER TABLE yvs_base_article_fournisseur ALTER COLUMN pua_ttc SET DEFAULT false;

ALTER TABLE yvs_compta_parametre ADD COLUMN jour_anterieur integer;
ALTER TABLE yvs_compta_parametre ALTER COLUMN jour_anterieur SET DEFAULT 7;

ALTER TABLE yvs_base_type_doc_divers ADD COLUMN can_reception boolean;
ALTER TABLE yvs_base_type_doc_divers ALTER COLUMN can_reception SET DEFAULT true;

CREATE TABLE yvs_base_type_doc_categorie
(
  id bigserial NOT NULL,
  categorie character varying,
  type_doc bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  CONSTRAINT yvs_base_type_doc_categorie_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_type_doc_categorie_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_type_doc_categorie_type_doc_fkey FOREIGN KEY (type_doc)
      REFERENCES yvs_base_type_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_type_doc_categorie
  OWNER TO postgres;
