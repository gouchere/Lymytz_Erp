ALTER TABLE yvs_synchro_serveurs_version ADD COLUMN date_update timestamp without time zone DEFAULT current_timestamp;

ALTER TABLE yvs_user_view_alertes ADD COLUMN voir boolean default true;

-- Table: yvs_workflow_alertes_users
-- DROP TABLE yvs_workflow_alertes_users;
CREATE TABLE yvs_workflow_alertes_users
(
  id bigserial NOT NULL,
  alerte bigint,
  users bigint,
  voir boolean DEFAULT true,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  execute_trigger character varying,
  CONSTRAINT yvs_workflow_alertes_users_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_alertes_users_users_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT users_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_alertes_users_alerte_fkey FOREIGN KEY (alerte)
      REFERENCES yvs_workflow_alertes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_alertes_users
  OWNER TO postgres;
  

-- Table: yvs_print_contrat_employe_header
-- DROP TABLE yvs_print_contrat_employe_header;
CREATE TABLE yvs_print_contrat_employe_header
(
  id bigserial NOT NULL,
  nom character varying,
  model character varying,
  defaut boolean,
  partie_societe character varying,
  partie_prestataire character varying,
  preambule character varying,
  definition character varying,
  societe bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT now(),
  date_save timestamp without time zone DEFAULT now(),
  execute_trigger character varying,
  CONSTRAINT yvs_print_contrat_employe_header_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_print_contrat_employe_header_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_print_contrat_employe_header_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_print_contrat_employe_header
  OWNER TO postgres;


-- Table: yvs_print_contrat_employe_article
-- DROP TABLE yvs_print_contrat_employe_article;
CREATE TABLE yvs_print_contrat_employe_article
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
  CONSTRAINT yvs_print_contrat_employe_article_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_print_contrat_employe_article_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_print_contrat_employe_article_header_fkey FOREIGN KEY (header)
      REFERENCES yvs_print_contrat_employe_header (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_print_contrat_employe_article
  OWNER TO postgres;

