ALTER TABLE yvs_workflow_valid_approvissionnement ADD COLUMN ordre_etape INTEGER DEFAULT 0;
ALTER TABLE yvs_workflow_valid_bon_provisoire ADD COLUMN ordre_etape INTEGER DEFAULT 0;
ALTER TABLE yvs_workflow_valid_conge ADD COLUMN ordre_etape INTEGER DEFAULT 0;
ALTER TABLE yvs_workflow_valid_doc_caisse ADD COLUMN ordre_etape INTEGER DEFAULT 0;
ALTER TABLE yvs_workflow_valid_doc_stock ADD COLUMN ordre_etape INTEGER DEFAULT 0;
ALTER TABLE yvs_workflow_valid_facture_achat ADD COLUMN ordre_etape INTEGER DEFAULT 0;
ALTER TABLE yvs_workflow_valid_facture_vente ADD COLUMN ordre_etape INTEGER DEFAULT 0;
ALTER TABLE yvs_workflow_valid_formation ADD COLUMN ordre_etape INTEGER DEFAULT 0;
ALTER TABLE yvs_workflow_valid_mission ADD COLUMN ordre_etape INTEGER DEFAULT 0;
ALTER TABLE yvs_workflow_valid_pc_mission ADD COLUMN ordre_etape INTEGER DEFAULT 0;

DROP TABLE yvs_com_compensation_ecart_entete_vente;
ALTER TABLE yvs_com_reglement_ecart_entete_vente RENAME TO yvs_com_reglement_ecart_vente;
ALTER TABLE yvs_com_reglement_ecart_vente DROP CONSTRAINT yvs_com_reglement_ecart_entete_vente_author_fkey;
ALTER TABLE yvs_com_reglement_ecart_vente DROP CONSTRAINT yvs_com_reglement_ecart_entete_vente_caisse_fkey;
ALTER TABLE yvs_com_reglement_ecart_vente DROP CONSTRAINT yvs_com_reglement_ecart_entete_vente_piece_fkey;
ALTER TABLE yvs_com_reglement_ecart_vente ADD CONSTRAINT yvs_com_reglement_ecart_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_com_reglement_ecart_vente ADD CONSTRAINT yvs_com_reglement_ecart_vente_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_com_reglement_ecart_vente ADD CONSTRAINT yvs_com_reglement_ecart_vente_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_com_ecart_entete_vente (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE;
ALTER SEQUENCE yvs_com_reglement_ecart_entete_vente_id_seq RENAME TO yvs_com_reglement_ecart_vente_id_seq;
ALTER TABLE yvs_com_reglement_ecart_vente ALTER COLUMN id SET DEFAULT nextval('yvs_com_reglement_ecart_vente_id_seq'::regclass);

ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN impact_valeur_inventaire boolean DEFAULT true;
ALTER TABLE yvs_grh_element_additionel ADD COLUMN comptabilise boolean DEFAULT false;

ALTER TABLE yvs_com_doc_ventes ADD COLUMN action character varying;


-- Table: yvs_module_active
-- DROP TABLE yvs_module_active;
CREATE TABLE yvs_module_active
(
  id serial NOT NULL,
  societe bigint,
  module bigint,
  actif boolean default true,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_module_active_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_module_active_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_module_active_module_fkey FOREIGN KEY (module)
      REFERENCES yvs_module (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_module_active_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_module_active
  OWNER TO postgres;
  
  
-- Table: yvs_base_departement
-- DROP TABLE yvs_base_departement;
CREATE TABLE yvs_base_departement
(
  id bigserial NOT NULL,
  chemin_parent character varying(255),
  code_departement character varying(255),
  description character varying(255),
  chemin_fichier character varying(255),
  intitule character varying(255),
  nivau integer,
  societe bigint,
  departement_parent integer,
  supp boolean DEFAULT false,
  actif boolean DEFAULT true,
  author bigint,
  abreviation character varying,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_base_departement_pkey PRIMARY KEY (id),
  CONSTRAINT fk_yvs_base_departement_departement_parent FOREIGN KEY (departement_parent)
      REFERENCES yvs_base_departement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_departement_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_departement_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_departement
  OWNER TO postgres;


-- Table: yvs_proj_departement
-- DROP TABLE yvs_proj_departement;
CREATE TABLE yvs_proj_departement
(
  id bigserial NOT NULL,
  service bigint,
  supp boolean DEFAULT false,
  actif boolean DEFAULT true,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_proj_departement_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_proj_departement_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_proj_departement_service_fkey FOREIGN KEY (service)
      REFERENCES yvs_base_departement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_proj_departement
  OWNER TO postgres;


-- Table: yvs_proj_projet
-- DROP TABLE yvs_proj_projet;
CREATE TABLE yvs_proj_projet
(
  id bigserial NOT NULL,
  code character varying,
  libelle character varying,
  description character varying,
  parent bigint,
  societe bigint,
  code_acces bigint,
  supp boolean DEFAULT false,
  actif boolean DEFAULT true,
  direct boolean DEFAULT false,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_proj_projet_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_proj_projet_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_proj_projet_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_proj_projet_parent_fkey FOREIGN KEY (parent)
      REFERENCES yvs_proj_projet (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_proj_projet_code_acces_fkey FOREIGN KEY (code_acces)
      REFERENCES yvs_base_code_acces (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_proj_projet
  OWNER TO postgres;


-- Table: yvs_proj_projet_service
-- DROP TABLE yvs_proj_projet_service;
CREATE TABLE yvs_proj_projet_service
(
  id bigserial NOT NULL,
  projet bigint,
  service bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_proj_projet_service_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_proj_projet_service_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_proj_projet_service_projet_fkey FOREIGN KEY (projet)
      REFERENCES yvs_proj_projet (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_proj_projet_service_service_fkey FOREIGN KEY (service)
      REFERENCES yvs_proj_departement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_proj_projet_service
  OWNER TO postgres;


ALTER TABLE yvs_grh_departement ADD COLUMN service bigint;;
ALTER TABLE yvs_grh_departement ADD CONSTRAINT yvs_grh_departement_service_fkey FOREIGN KEY (service)
      REFERENCES yvs_base_departement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_grh_plan_prelevement ADD COLUMN societe bigint;
ALTER TABLE yvs_grh_plan_prelevement
  ADD CONSTRAINT yvs_grh_plan_prelevement_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	
	
-- Table: yvs_proj_projet_contenu_doc_achat
-- DROP TABLE yvs_proj_projet_contenu_doc_achat;
CREATE TABLE yvs_proj_projet_contenu_doc_achat
(
  id bigserial NOT NULL,
  projet bigint,
  contenu bigint,
  quantite double precision default 0,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_proj_projet_contenu_doc_achat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_proj_projet_contenu_doc_achat_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_proj_projet_contenu_doc_achat_projet_fkey FOREIGN KEY (projet)
      REFERENCES yvs_proj_projet_service (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_proj_projet_contenu_doc_achat_contenu_fkey FOREIGN KEY (contenu)
      REFERENCES yvs_com_contenu_doc_achat (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_proj_projet_contenu_doc_achat
  OWNER TO postgres;
  
  
-- Table: yvs_proj_projet_caisse_doc_divers
-- DROP TABLE yvs_proj_projet_caisse_doc_divers;
CREATE TABLE yvs_proj_projet_caisse_doc_divers
(
  id bigserial NOT NULL,
  projet bigint,
  doc_divers bigint,
  montant double precision default 0,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_proj_projet_caisse_doc_divers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_proj_projet_caisse_doc_divers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_proj_projet_caisse_doc_divers_projet_fkey FOREIGN KEY (projet)
      REFERENCES yvs_proj_projet_service (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_proj_projet_caisse_doc_divers_doc_divers_fkey FOREIGN KEY (doc_divers)
      REFERENCES yvs_compta_caisse_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_proj_projet_caisse_doc_divers
  OWNER TO postgres;
  
  
-- Table: yvs_proj_projet_missions
-- DROP TABLE yvs_proj_projet_missions;
CREATE TABLE yvs_proj_projet_missions
(
  id bigserial NOT NULL,
  projet bigint,
  mission bigint,
  montant double precision default 0,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_proj_projet_missions_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_proj_projet_missions_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_proj_projet_missions_projet_fkey FOREIGN KEY (projet)
      REFERENCES yvs_proj_projet_service (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_proj_projet_missions_mission_fkey FOREIGN KEY (mission)
      REFERENCES yvs_grh_missions (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_proj_projet_missions
  OWNER TO postgres;  
  

ALTER TABLE yvs_base_model_reglement ADD COLUMN paye_before_valide boolean;
ALTER TABLE yvs_base_model_reglement ALTER COLUMN paye_before_valide SET DEFAULT false;

-- Table: yvs_compta_caisse_doc_divers_tiers
-- DROP TABLE yvs_compta_caisse_doc_divers_tiers;
CREATE TABLE yvs_compta_caisse_doc_divers_tiers
(
  id bigserial NOT NULL,
  doc_divers bigint,
  id_tiers bigint,
  table_tiers character varying,
  montant double precision DEFAULT 0,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_compta_caisse_doc_divers_tiers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_caisse_doc_divers_tiers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_doc_divers_tiers_doc_divers_fkey FOREIGN KEY (doc_divers)
      REFERENCES yvs_compta_caisse_doc_divers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_caisse_doc_divers_tiers
  OWNER TO postgres;

-- Table: yvs_grh_retenue_doc_divers
-- DROP TABLE yvs_grh_retenue_doc_divers;
CREATE TABLE yvs_grh_retenue_doc_divers
(
  id bigserial NOT NULL,
  retenue bigint,
  doc_divers bigint,
  tiers_divers bigint,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_grh_retenue_doc_divers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_retenue_doc_divers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_retenue_doc_divers_doc_divers_fkey FOREIGN KEY (doc_divers)
      REFERENCES yvs_compta_caisse_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_grh_retenue_doc_divers_tiers_divers_fkey FOREIGN KEY (tiers_divers)
      REFERENCES yvs_compta_caisse_doc_divers_tiers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_grh_retenue_doc_divers_retenue_fkey FOREIGN KEY (retenue)
      REFERENCES yvs_grh_element_additionel (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_retenue_doc_divers
  OWNER TO postgres;


-- Table: yvs_com_doc_stocks_ecart
-- DROP TABLE yvs_com_doc_stocks_ecart;
CREATE TABLE yvs_com_doc_stocks_ecart
(
  id bigserial,
  numero character varying,
  statut character varying(1) default 'E'::bpchar,
  statut_regle character(1) default 'W'::bpchar,
  doc_stock bigint,
  tiers bigint,
  taux double precision DEFAULT 1,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_com_doc_stocks_ecart_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_doc_stocks_ecart_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_stocks_ecart_doc_stock_fkey FOREIGN KEY (doc_stock)
      REFERENCES yvs_com_doc_stocks (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_com_doc_stocks_ecart_tiers_by_fkey FOREIGN KEY (tiers)
      REFERENCES yvs_base_tiers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_doc_stocks_ecart
  OWNER TO postgres;
  
  
-- Table: yvs_com_reglement_ecart_stock
-- DROP TABLE yvs_com_reglement_ecart_stock;
CREATE TABLE yvs_com_reglement_ecart_stock
(
  id bigserial NOT NULL,
  date_reglement date,
  montant double precision,
  piece bigint,
  caisse bigint,
  statut character(1) DEFAULT 'W'::bpchar,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  numero character varying,
  execute_trigger character varying,
  CONSTRAINT yvs_com_reglement_ecart_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_reglement_ecart_stock_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_reglement_ecart_stock_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_reglement_ecart_stock_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_com_doc_stocks_ecart (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_reglement_ecart_stock
  OWNER TO postgres;


-- Table: yvs_grh_retenue_ecart_stock
-- DROP TABLE yvs_grh_retenue_ecart_stock;
CREATE TABLE yvs_grh_retenue_ecart_stock
(
  id bigserial NOT NULL,
  retenue bigint,
  ecart bigint,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_grh_retenue_ecart_stock_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_retenue_ecart_stock_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_retenue_ecart_stock_decart_fkey FOREIGN KEY (ecart)
      REFERENCES yvs_com_reglement_ecart_stock (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_grh_retenue_ecart_stock_retenue_fkey FOREIGN KEY (retenue)
      REFERENCES yvs_grh_element_additionel (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_retenue_ecart_stock
  OWNER TO postgres;
  
  
-- Table: yvs_grh_retenue_ecart_vente
-- DROP TABLE yvs_grh_retenue_ecart_vente;
CREATE TABLE yvs_grh_retenue_ecart_vente
(
  id bigserial NOT NULL,
  retenue bigint,
  ecart bigint,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_grh_retenue_ecart_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_retenue_ecart_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_retenue_ecart_vente_ecart_fkey FOREIGN KEY (ecart)
      REFERENCES yvs_com_reglement_ecart_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_grh_retenue_ecart_vente_retenue_fkey FOREIGN KEY (retenue)
      REFERENCES yvs_grh_element_additionel (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_retenue_ecart_vente
  OWNER TO postgres;
  
DROP TRIGGER com_action_on_ecart_vente ON yvs_com_ecart_entete_vente;
ALTER TABLE yvs_com_ecart_entete_vente DROP COLUMN entete_doc;


-- Table: yvs_com_doc_stocks_valeur
-- DROP TABLE yvs_com_doc_stocks_valeur;
CREATE TABLE yvs_com_doc_stocks_valeur
(
  id bigserial NOT NULL,
  doc_stock bigint,
  montant double precision default 0,
  coefficient double precision default 1,
  valorise_mp_by character varying default 'A',
  valorise_pf_by character varying default 'V',
  valorise_pfs_by character varying default 'R',
  valorise_ms_by character varying default 'V',
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_com_doc_stocks_valeur_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_doc_stocks_valeur_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_stocks_valeur_doc_stock_fkey FOREIGN KEY (doc_stock)
      REFERENCES yvs_com_doc_stocks (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_doc_stocks_valeur
  OWNER TO postgres;