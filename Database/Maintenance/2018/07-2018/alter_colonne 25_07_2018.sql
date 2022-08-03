ALTER TABLE yvs_base_articles DROP COLUMN code_barre;
ALTER TABLE yvs_base_conditionnement ADD COLUMN code_barre character varying;

-- Table: yvs_base_article_code_barre
-- DROP TABLE yvs_base_article_code_barre;
CREATE TABLE yvs_base_article_code_barre
(
  id bigserial NOT NULL,
  code_barre character varying,
  description character varying,
  article bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_base_article_code_barre_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_article_code_barre_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_article_code_barre_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_article_code_barre
  OWNER TO postgres;

-- Table: yvs_synchro_serveurs
-- DROP TABLE yvs_synchro_serveurs;
CREATE TABLE yvs_synchro_serveurs
(
  id serial NOT NULL,
  nom_serveur character varying,
  adresse_ip character varying,
  actif boolean DEFAULT true,
  CONSTRAINT yvs_synchro_serveur_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_synchro_serveurs
  OWNER TO postgres;
  
  
-- Table: yvs_synchro_listen_table
-- DROP TABLE yvs_synchro_listen_table;
CREATE TABLE yvs_synchro_listen_table
(
  id bigserial NOT NULL,
  name_table character varying,
  id_source bigint,
  date_save timestamp without time zone,
  groupe_table character varying,
  to_listen boolean,
  action_name character varying, -- Précise le type d'action à réaliser sur la table
  ordre bigserial NOT NULL,
  CONSTRAINT yvs_synchro_listen_table_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_synchro_listen_table
  OWNER TO postgres;
COMMENT ON COLUMN yvs_synchro_listen_table.action_name IS 'Précise le type d''action à réaliser sur la table';


-- Table: yvs_synchro_data_synchro
-- DROP TABLE yvs_synchro_data_synchro;
CREATE TABLE yvs_synchro_data_synchro
(
  id bigserial NOT NULL,
  id_listen bigint,
  serveur integer,
  date_save timestamp without time zone,
  id_distant bigint,
  CONSTRAINT yvs_synchro_data_synchro_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_synchro_data_synchro_id_listen_fkey FOREIGN KEY (id_listen)
      REFERENCES yvs_synchro_listen_table (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_synchro_data_synchro_serveur_fkey FOREIGN KEY (serveur)
      REFERENCES yvs_synchro_serveurs (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_synchro_data_synchro
  OWNER TO postgres;