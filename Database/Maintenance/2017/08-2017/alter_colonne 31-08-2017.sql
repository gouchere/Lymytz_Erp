
ALTER TABLE yvs_com_parametre ADD COLUMN converter character varying;
ALTER TABLE yvs_com_parametre ALTER COLUMN converter SET DEFAULT 'DN'::character varying;

ALTER TABLE yvs_compta_parametre ADD COLUMN converter character varying;
ALTER TABLE yvs_compta_parametre ALTER COLUMN converter SET DEFAULT 'DN'::character varying;

-- Table: yvs_compta_cout_sup_doc_divers

-- DROP TABLE yvs_compta_cout_sup_doc_divers;

CREATE TABLE yvs_compta_cout_sup_doc_divers
(
  id bigserial NOT NULL,
  montant double precision,
  doc_divers bigint,
  supp boolean DEFAULT false,
  actif boolean DEFAULT true,
  author bigint,
  type_cout bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_cout_sup_doc_divers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_cout_sup_doc_divers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_cout_sup_doc_divers_doc_stock_fkey FOREIGN KEY (doc_divers)
      REFERENCES yvs_compta_caisse_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_compta_cout_sup_doc_divers_type_cout_fkey FOREIGN KEY (type_cout)
      REFERENCES yvs_grh_type_cout (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_cout_sup_doc_divers
  OWNER TO postgres;


-- Table: yvs_compa_taxe_doc_divers

-- DROP TABLE yvs_compa_taxe_doc_divers;

CREATE TABLE yvs_compta_taxe_doc_divers
(
  id bigserial NOT NULL,
  doc_divers bigint,
  taxe bigint,
  montant double precision,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_taxe_doc_divers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_taxe_doc_divers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_taxe_doc_divers_contenu_fkey FOREIGN KEY (doc_divers)
      REFERENCES yvs_compta_caisse_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_compta_taxe_doc_divers_taxe_fkey FOREIGN KEY (taxe)
      REFERENCES yvs_base_taxes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_taxe_doc_divers
  OWNER TO postgres;