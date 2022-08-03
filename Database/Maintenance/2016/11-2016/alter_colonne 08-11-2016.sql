
ALTER TABLE yvs_com_doc_ventes ADD COLUMN model_reglement integer;
ALTER TABLE yvs_com_doc_ventes
  ADD CONSTRAINT yvs_com_doc_ventes_model_reglement_fkey FOREIGN KEY (model_reglement)
      REFERENCES yvs_base_model_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE yvs_com_mensualite_facture_vente ADD COLUMN mode_reglement integer;
ALTER TABLE yvs_com_mensualite_facture_vente
  ADD CONSTRAINT yvs_com_mensualite_facture_vente_mode_reglement_fkey FOREIGN KEY (mode_reglement)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_plan_tarifaire ADD COLUMN ristourne double precision;
ALTER TABLE yvs_base_plan_tarifaire ALTER COLUMN ristourne SET DEFAULT 0;
ALTER TABLE yvs_base_plan_tarifaire ADD COLUMN nature_remise character varying;
ALTER TABLE yvs_base_plan_tarifaire ALTER COLUMN nature_remise SET DEFAULT 'TAUX'::character varying;
ALTER TABLE yvs_base_plan_tarifaire ADD COLUMN nature_ristourne character varying;
ALTER TABLE yvs_base_plan_tarifaire ALTER COLUMN nature_ristourne SET DEFAULT 'TAUX'::character varying;


CREATE TABLE yvs_compta_caisse_piece_vente
(
  id bigserial NOT NULL,
  numero_piece character varying,
  montant double precision,
  statut_piece character(1),
  vente bigint,
  caisse bigint,
  author bigint,
  date_piece date,
  date_paiement timestamp without time zone,
  note character varying,
  model bigint,
  caissier bigint,
  date_paiment_prevu date,
  CONSTRAINT yvs_compta_caisse_piece_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_caisse_piece_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  ADD CONSTRAINT yvs_compta_piece_caisse_mission_caissier_fkey FOREIGN KEY (caissier)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_vente_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_vente_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_vente_vente_fkey FOREIGN KEY (vente)
      REFERENCES yvs_com_mensualite_facture_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_caisse_piece_vente
  OWNER TO postgres;
  
  ALTER TABLE yvs_base_model_reglement RENAME TO yvs_base_mode_reglement
  
 CREATE TABLE yvs_base_model_reglement
(
  id bigserial NOT NULL,
  reference character varying,
  description character varying,
  societe bigint,
  actif boolean DEFAULT true,
  author bigint,
  CONSTRAINT yvs_base_model_reglement_fk PRIMARY KEY (id),
  CONSTRAINT yvs_base_model_reglement_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_model_de_reglement_societe FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_model_de_reglement_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_model_reglement
  OWNER TO postgres;
  
  -- Table: yvs_base_tranche_reglement_fournisseur

-- DROP TABLE yvs_base_tranche_reglement_fournisseur;

CREATE TABLE yvs_base_tranche_reglement
(
  id bigserial NOT NULL,
  model bigint,
  mod bigint,
  numero integer,
  taux double precision DEFAULT 0,
  interval_jour integer,
  author bigint,
  CONSTRAINT yvs_base_tranche_reglement_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_tranche_reglement_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_tranche_reglement_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_model_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_tranche_reglement_mod_fkey FOREIGN KEY (mod)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_tranche_reglement
  OWNER TO postgres;

ALTER TABLE yvs_base_fournisseur ADD COLUMN model bigint;
ALTER TABLE yvs_base_fournisseur
  ADD CONSTRAINT yvs_base_fournisseur_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_model_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_client ADD COLUMN model bigint;
ALTER TABLE yvs_com_client
  ADD CONSTRAINT yvs_com_client_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_model_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_categorie_client ADD COLUMN model bigint;
ALTER TABLE yvs_base_categorie_client
  ADD CONSTRAINT yvs_base_categorie_client_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_model_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_model_reglement ADD COLUMN type character(1);
ALTER TABLE yvs_base_model_reglement ALTER COLUMN type SET DEFAULT 'C'::bpchar;

ALTER TABLE yvs_base_point_vente ADD COLUMN reglement_auto boolean;
ALTER TABLE yvs_base_point_vente ALTER COLUMN reglement_auto SET DEFAULT false;