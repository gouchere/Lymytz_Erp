ALTER TABLE yvs_com_cout_sup_doc_vente ADD COLUMN service boolean DEFAULT false;
ALTER TABLE yvs_prod_nomenclature ADD COLUMN for_conditionnement boolean DEFAULT false;

ALTER TABLE yvs_base_depots ALTER COLUMN id TYPE bigint;
ALTER SEQUENCE yvs_depots_id_seq RENAME TO yvs_base_depots_id_seq;

CREATE TABLE yvs_prod_fiche_conditionnement
(
  id bigserial NOT NULL,
  date_conditionnement date DEFAULT ('now'::text)::date,
  nomenclature bigint,
  depot bigint,
  quantite double precision DEFAULT 0,
  statut character(1) DEFAULT 'E'::bpchar,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  CONSTRAINT yvs_prod_fiche_conditionnement_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_fiche_conditionnement_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_fiche_conditionnement_depot_fkey FOREIGN KEY (depot)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_fiche_conditionnement_nomenclature_fkey FOREIGN KEY (nomenclature)
      REFERENCES yvs_prod_nomenclature (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_fiche_conditionnement
  OWNER TO postgres;
  
 
CREATE TABLE yvs_prod_contenu_conditionnement
(
  id bigserial NOT NULL,
  article bigint,
  fiche bigint,
  conditionnement bigint,
  quantite double precision DEFAULT 0,
  consommable boolean DEFAULT true,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  CONSTRAINT yvs_prod_contenu_conditionnement_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_contenu_conditionnement_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_contenu_conditionnement_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_contenu_conditionnement_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_contenu_conditionnement_fiche_fkey FOREIGN KEY (fiche)
      REFERENCES yvs_prod_fiche_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_contenu_conditionnement
  OWNER TO postgres;