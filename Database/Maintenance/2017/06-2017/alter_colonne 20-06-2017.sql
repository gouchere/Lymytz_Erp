-- Table: yvs_ext_tiers

-- DROP TABLE yvs_ext_tiers;

CREATE TABLE yvs_ext_tiers
(
  id bigserial NOT NULL,
  tiers bigint,
  code_externe character varying,
  date_save timestamp without time zone default current_timestamp,
  author bigint,
  CONSTRAINT yvs_ext_tiers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_ext_tiers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_ext_tiers_tiers_fkey FOREIGN KEY (tiers)
      REFERENCES yvs_base_tiers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_ext_tiers
  OWNER TO postgres;	
  
  -- Table: yvs_ext_client

-- DROP TABLE yvs_ext_client;

CREATE TABLE yvs_ext_client
(
  id bigserial NOT NULL,
  client bigint,
  code_externe character varying,
  date_save timestamp without time zone default current_timestamp,
  author bigint,
  CONSTRAINT yvs_ext_client_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_ext_client_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_ext_client_client_fkey FOREIGN KEY (client)
      REFERENCES yvs_com_client (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_ext_client
  OWNER TO postgres;	
  
  
  -- Table: yvs_ext_article

-- DROP TABLE yvs_ext_article;

CREATE TABLE yvs_ext_article
(
  id bigserial NOT NULL,
  article bigint,
  code_externe character varying,
  date_save timestamp without time zone default current_timestamp,
  author bigint,
  CONSTRAINT yvs_ext_article_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_ext_article_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_ext_article_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_ext_article
  OWNER TO postgres;	
  
  
  
CREATE TABLE yvs_com_taxe_contenu_vente
(
  id bigserial NOT NULL,
  contenu bigint,
  taxe bigint,
  montant double precision,
  author bigint,
  CONSTRAINT yvs_cout_sup_contenu_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_taxe_contenu_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_taxe_contenu_vente_contenu_fkey FOREIGN KEY (contenu)
      REFERENCES yvs_com_contenu_doc_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_com_taxe_contenu_vente_taxe_fkey FOREIGN KEY (taxe)
      REFERENCES yvs_base_taxes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_taxe_contenu_vente
  OWNER TO postgres;
  
  
ALTER TABLE yvs_compta_content_journal ADD COLUMN numero integer;

ALTER TABLE yvs_stat_export_colonne ADD COLUMN order_by character(1);
ALTER TABLE yvs_stat_export_colonne ADD COLUMN sens_contrainte character(1) default 'N';

ALTER TABLE yvs_stat_export_etat ADD COLUMN order_by character varying;
ALTER TABLE yvs_stat_export_etat ADD COLUMN type_formule character(1) default 'S';
ALTER TABLE yvs_stat_export_etat ADD COLUMN formule character varying default '';

