update yvs_com_doc_ventes set nom_client = (select concat(nom, ' ', prenom) from yvs_com_client where id = client) where nom_client is null;
-- Table: yvs_compta_content_analytique

-- DROP TABLE yvs_compta_content_analytique;

CREATE TABLE yvs_compta_content_analytique
(
  id bigserial NOT NULL,
  contenu bigint,
  centre bigint,
  debit double precision,
  credit double precision,
  date_sasie date,
  coefficient double precision DEFAULT 1,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_content_analytique_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_content_analytique_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_content_analytique_contenu_fkey FOREIGN KEY (contenu)
      REFERENCES yvs_compta_content_journal (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_content_analytique_centre_fkey FOREIGN KEY (centre)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_content_analytique
  OWNER TO postgres;
