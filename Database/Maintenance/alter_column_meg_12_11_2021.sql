
ALTER TABLE yvs_print_contrat_employe_header ADD COLUMN titre character varying;

-- Table: yvs_print_decision_conge_header
-- DROP TABLE yvs_print_decision_conge_header;
CREATE TABLE yvs_print_decision_conge_header
(
  id bigserial NOT NULL,
  nom character varying,
  titre character varying,
  model character varying,
  defaut boolean,
  introduction character varying,
  definition_conventive character varying,
  societe bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT now(),
  date_save timestamp without time zone DEFAULT now(),
  execute_trigger character varying,
  CONSTRAINT yvs_print_decision_conge_header_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_print_decision_conge_header_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_print_decision_conge_header_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_print_decision_conge_header
  OWNER TO postgres;


-- Table: yvs_print_decision_conge_article
-- DROP TABLE yvs_print_decision_conge_article;
CREATE TABLE yvs_print_decision_conge_article
(
  id bigserial NOT NULL,
  titre character varying,
  contenu character varying,
  indice character varying,
  niveau integer default 0,
  header bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT now(),
  date_save timestamp without time zone DEFAULT now(),
  execute_trigger character varying,
  CONSTRAINT yvs_print_decision_conge_article_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_print_decision_conge_article_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_print_decision_conge_article_header_fkey FOREIGN KEY (header)
      REFERENCES yvs_print_decision_conge_header (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_print_decision_conge_article
  OWNER TO postgres;