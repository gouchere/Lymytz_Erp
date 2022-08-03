ALTER TABLE yvs_com_parametre_vente ADD COLUMN generer_facture_auto boolean DEFAULT false;
ALTER TABLE yvs_base_type_doc_divers ADD COLUMN module character varying DEFAULT 'OD';
ALTER TABLE yvs_com_doc_achats ADD COLUMN type_achat bigint;
ALTER TABLE yvs_com_doc_achats
  ADD CONSTRAINT yvs_com_doc_achats_type_achat_fkey FOREIGN KEY (type_achat)
      REFERENCES yvs_base_type_doc_divers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Table: yvs_com_contrats_client
-- DROP TABLE yvs_com_contrats_client;
CREATE TABLE yvs_com_contrats_client
(
  id bigserial NOT NULL,
  reference character varying,
  reference_externe character varying,
  type character(1) DEFAULT 'I'::bpchar,
  date_expiration date,
  date_debut_facturation date,
  interval_facturation integer DEFAULT 1,
  periode_facturation character(1) DEFAULT 'M'::bpchar,
  actif boolean DEFAULT true,
  client bigint,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  CONSTRAINT yvs_com_contrats_client_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_contrats_client_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_contrats_client_client_fkey FOREIGN KEY (client)
      REFERENCES yvs_com_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_contrats_client
  OWNER TO postgres;


-- Table: yvs_com_element_contrats_client
-- DROP TABLE yvs_com_element_contrats_client;
CREATE TABLE yvs_com_element_contrats_client
(
  id bigserial NOT NULL,
  contrat bigint,
  article bigint,
  quantite double precision DEFAULT 0,
  prix double precision DEFAULT 0,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  CONSTRAINT yvs_com_element_contrats_client_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_element_contrats_client_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_element_contrats_client_contrat_fkey FOREIGN KEY (contrat)
      REFERENCES yvs_com_contrats_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_element_contrats_client_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_element_contrats_client
  OWNER TO postgres;


-- Table: yvs_com_element_add_contrats_client
-- DROP TABLE yvs_com_element_add_contrats_client;
CREATE TABLE yvs_com_element_add_contrats_client
(
  id bigserial NOT NULL,
  contrat bigint,
  type_cout bigint,
  montant double precision DEFAULT 0,
  application character(1) DEFAULT 'I'::bpchar,  
  periodicite_application integer DEFAULT 1,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  CONSTRAINT yvs_com_element_add_contrats_client_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_element_add_contrats_client_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_element_add_contrats_client_contrat_fkey FOREIGN KEY (contrat)
      REFERENCES yvs_com_contrats_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_element_add_contrats_client_type_cout_fkey FOREIGN KEY (type_cout)
      REFERENCES yvs_grh_type_cout (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_element_add_contrats_client
  OWNER TO postgres;
  
  
-- Table: yvs_synchro_tables
-- DROP TABLE yvs_synchro_tables;
CREATE TABLE yvs_synchro_tables
(
  id serial NOT NULL,
  table_name character varying,
  use_societe boolean DEFAULT true,
  interval_load integer DEFAULT 0,
  query character varying,
  CONSTRAINT yvs_synchro_tables_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_synchro_tables
  OWNER TO postgres;