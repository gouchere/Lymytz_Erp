ALTER TABLE yvs_compta_caisse_piece_vente DROP COLUMN mouvement;
ALTER TABLE yvs_compta_caisse_piece_vente DROP COLUMN acompte;

ALTER TABLE yvs_compta_caisse_piece_achat DROP COLUMN acompte;

ALTER TABLE yvs_workflow_etape_validation ADD COLUMN livraison_here boolean;
ALTER TABLE yvs_workflow_etape_validation ALTER COLUMN livraison_here SET DEFAULT false;

ALTER TABLE yvs_workflow_model_doc ADD COLUMN defined_livraison boolean;
ALTER TABLE yvs_workflow_model_doc ALTER COLUMN defined_livraison SET DEFAULT false;

ALTER TABLE yvs_com_periode_validite_commision ADD COLUMN abbreviation character varying;
ALTER TABLE yvs_com_periode_ration ADD COLUMN abbreviation character varying;
ALTER TABLE yvs_com_periode_objectif ADD COLUMN abbreviation character varying;
ALTER TABLE yvs_grh_ordre_calcul_salaire ADD COLUMN abbreviation character varying;

ALTER TABLE yvs_compta_acompte_client ADD COLUMN model bigint;
ALTER TABLE yvs_compta_acompte_client
  ADD CONSTRAINT yvs_compta_acompte_client_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
DROP TABLE yvs_compta_notif_reglement;

CREATE TABLE yvs_compta_notif_reglement_vente
(
  id bigserial NOT NULL,
  piece_vente bigint,
  acompte bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_notif_reglement_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_notif_reglement_vente_acompte_fkey FOREIGN KEY (acompte)
      REFERENCES yvs_compta_acompte_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_notif_reglement_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_notif_reglement_vente_piece_vente_fkey FOREIGN KEY (piece_vente)
      REFERENCES yvs_compta_caisse_piece_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_notif_reglement_vente
  OWNER TO postgres;
	  
	  
CREATE TABLE yvs_compta_acompte_fournisseur
(
  id bigserial NOT NULL,
  montant double precision,
  date_acompte date,
  num_refrence character varying,
  commentaire character varying,
  author bigint,
  fournisseur bigint,
  caisse bigint,
  statut character(1) DEFAULT 'W'::bpchar,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  model bigint,
  CONSTRAINT yvs_compta_acompte_fournisseur_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_acompte_fournisseur_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_acompte_fournisseur_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_acompte_fournisseur_fournisseur_fkey FOREIGN KEY (fournisseur)
      REFERENCES yvs_base_fournisseur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_acompte_fournisseur_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_acompte_fournisseur
  OWNER TO postgres;
  
CREATE TABLE yvs_compta_notif_reglement_achat
(
  id bigserial NOT NULL,
  piece_achat bigint,
  acompte bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_notif_reglement_achat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_notif_reglement_achat_acompte_fkey FOREIGN KEY (acompte)
      REFERENCES yvs_compta_acompte_fournisseur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_notif_reglement_achat_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_notif_reglement_achat_piece_achat_fkey FOREIGN KEY (piece_achat)
      REFERENCES yvs_compta_caisse_piece_achat (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_notif_reglement_achat
  OWNER TO postgres;  
  
CREATE TABLE yvs_compta_credit_fournisseur
(
  id bigserial NOT NULL,
  num_reference character varying,
  motif character varying,
  date_credit date,
  montant double precision,
  fournisseur bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_credit_fournisseur_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_credit_fournisseur_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_credit_fournisseur_fournisseur_fkey FOREIGN KEY (fournisseur)
      REFERENCES yvs_base_fournisseur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_credit_fournisseur
  OWNER TO postgres;

CREATE TABLE yvs_compta_reglement_credit_fournisseur
(
  id bigserial NOT NULL,
  credit bigint,
  caisse bigint,
  valeur double precision,
  author bigint,
  date_reg date,
  heure_reg time without time zone,
  statut character(1) DEFAULT 'W'::bpchar,
  model bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_reglement_credit_fournisseur_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_reglement_credit_fournisseur_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_reglement_credit_fournisseur_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_reglement_credit_fournisseur_credit_fkey FOREIGN KEY (credit)
      REFERENCES yvs_compta_credit_fournisseur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_reglement_credit_fournisseur_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_reglement_credit_fournisseur
  OWNER TO postgres;
