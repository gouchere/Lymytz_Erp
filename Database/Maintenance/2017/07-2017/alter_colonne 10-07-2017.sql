ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN conditionnement bigint;
ALTER TABLE yvs_com_contenu_doc_achat
  ADD CONSTRAINT yvs_com_contenu_doc_achat_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN conditionnement bigint;
ALTER TABLE yvs_com_contenu_doc_vente
  ADD CONSTRAINT yvs_com_contenu_doc_vente_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN conditionnement bigint;
ALTER TABLE yvs_com_contenu_doc_stock
  ADD CONSTRAINT yvs_com_contenu_doc_stock_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_mouvement_stock ADD COLUMN conditionnement bigint;
ALTER TABLE yvs_base_mouvement_stock
  ADD CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_reservation_stock ADD COLUMN conditionnement bigint;
ALTER TABLE yvs_com_reservation_stock
  ADD CONSTRAINT yvs_com_reservation_stock_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_plan_tarifaire ADD COLUMN conditionnement bigint;
ALTER TABLE yvs_base_plan_tarifaire
  ADD CONSTRAINT yvs_base_plan_tarifaire_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_conditionnement DROP COLUMN unite;
ALTER TABLE yvs_base_conditionnement RENAME conditionnement TO unite;
ALTER TABLE yvs_base_conditionnement ADD COLUMN prix double precision;
ALTER TABLE yvs_base_conditionnement ALTER COLUMN prix SET DEFAULT 0;
ALTER TABLE yvs_base_conditionnement ADD COLUMN prix_min double precision;
ALTER TABLE yvs_base_conditionnement ALTER COLUMN prix_min SET DEFAULT 0;
ALTER TABLE yvs_base_conditionnement ADD COLUMN nature_prix_min character varying;
ALTER TABLE yvs_base_conditionnement ALTER COLUMN nature_prix_min SET DEFAULT 'MONTANT'::character varying;
ALTER TABLE yvs_base_conditionnement ADD COLUMN remise double precision;
ALTER TABLE yvs_base_conditionnement ALTER COLUMN remise SET DEFAULT 0;
	  
CREATE TABLE yvs_base_conditionnement_depot
(
  id bigserial NOT NULL,
  conditionnement bigint,
  article bigint,
  operation bigint,
  author bigint,
  CONSTRAINT yvs_base_conditionnement_depot_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_conditionnement_depot_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_article_depot (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_conditionnement_depot_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_conditionnement_depot_operation_fkey FOREIGN KEY (operation)
      REFERENCES yvs_base_depot_operation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_contenu_doc_stock_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_conditionnement_depot
  OWNER TO postgres;
  
  -- Table: yvs_base_conditionnement_point

-- DROP TABLE yvs_base_conditionnement_point;

CREATE TABLE yvs_base_conditionnement_point
(
  id bigserial NOT NULL,
  conditionnement bigint,
  article bigint,
  puv double precision DEFAULT 0,
  prix_min double precision DEFAULT 0,
  nature_prix_min character varying DEFAULT 'MONTANT'::character varying,
  remise double precision DEFAULT 0,
  nature_remise character varying DEFAULT 'MONTANT'::character varying,
  author bigint,
  CONSTRAINT yvs_base_conditionnement_point_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_conditionnement_point_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_article_point (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_conditionnement_point_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_contenu_doc_stock_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_conditionnement_point
  OWNER TO postgres;
  
  
CREATE TABLE yvs_com_transformation
(
  id bigserial NOT NULL,
  reference character varying,
  depot bigint,
  editeur bigint,
  description character varying,
  date_doc date DEFAULT ('now'::text)::date,
  statut character(1) DEFAULT 'E'::bpchar,
  author bigint,
  CONSTRAINT yvs_com_transformation_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_transformation_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_transformation_depot_fkey FOREIGN KEY (depot)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_transformation_editeur_fkey FOREIGN KEY (editeur)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_transformation
  OWNER TO postgres;
  
  
  -- Table: yvs_com_reconditionnement

-- DROP TABLE yvs_com_reconditionnement;

CREATE TABLE yvs_com_reconditionnement
(
  id bigserial NOT NULL,
  article bigint,
  quantite double precision DEFAULT 0,
  unite_source bigint,
  unite_destination bigint,
  qualite_source bigint,
  qualite_destination bigint,
  resultante double precision DEFAULT 0,
  fiche bigint,
  author bigint,
  CONSTRAINT yvs_com_reconditionnement_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_reconditionnement_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_transformation_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_reconditionnement_fiche_fkey FOREIGN KEY (fiche)
      REFERENCES yvs_com_transformation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_reconditionnement_unite_destination_fkey FOREIGN KEY (unite_destination)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_reconditionnement_unite_source_fkey FOREIGN KEY (unite_source)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_reconditionnement_qualite_source_fkey FOREIGN KEY (qualite_source)
      REFERENCES yvs_com_qualite (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_reconditionnement_qualite_destination_fkey FOREIGN KEY (qualite_destination)
      REFERENCES yvs_com_qualite (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_reconditionnement
  OWNER TO postgres;

