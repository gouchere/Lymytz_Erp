
ALTER TABLE yvs_base_conditionnement ADD COLUMN prix_achat double precision;
ALTER TABLE yvs_base_conditionnement ALTER COLUMN prix_achat SET DEFAULT 0;

ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN prix_total double precision;
ALTER TABLE yvs_com_contenu_doc_achat ALTER COLUMN prix_total SET DEFAULT 0;
ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN statut_livree character(1);
ALTER TABLE yvs_com_contenu_doc_achat ALTER COLUMN statut_livree SET DEFAULT 'W'::bpchar;	  
	  
CREATE TABLE yvs_base_conditionnement_fournisseur
(
  id bigserial NOT NULL,
  conditionnement bigint,
  pua double precision DEFAULT 0,
  article bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_base_conditionnement_fournisseur_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_conditionnement_fournisseur_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_article_fournisseur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_conditionnement_fournisseur_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_contenu_doc_stock_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_conditionnement_fournisseur
  OWNER TO postgres;

CREATE TABLE yvs_com_taxe_contenu_achat
(
  id bigserial NOT NULL,
  contenu bigint,
  taxe bigint,
  montant double precision,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_cout_taxe_contenu_achat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_taxe_contenu_achat_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_taxe_contenu_achat_contenu_fkey FOREIGN KEY (contenu)
      REFERENCES yvs_com_contenu_doc_achat (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_com_taxe_contenu_achat_taxe_fkey FOREIGN KEY (taxe)
      REFERENCES yvs_base_taxes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_taxe_contenu_achat
  OWNER TO postgres;