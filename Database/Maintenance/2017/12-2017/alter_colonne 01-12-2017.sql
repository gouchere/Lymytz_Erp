
CREATE TABLE yvs_compta_bon_provisoire
(
  id bigserial NOT NULL,
  tiers bigint,
  piece bigint,
  date_save timestamp without time zone DEFAULT now(),
  author bigint,
  date_update timestamp without time zone DEFAULT now(),
  CONSTRAINT yvs_compta_bon_provisoire_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_bon_provisoire_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_bon_provisoire_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_caisse_piece_divers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_bon_provisoire_tiers_fkey FOREIGN KEY (tiers)
      REFERENCES yvs_base_tiers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_bon_provisoire
  OWNER TO postgres;

CREATE TABLE yvs_compta_justificatif_bon
(
  id bigserial NOT NULL,
  montant double precision DEFAULT 0,
  date_justifier date DEFAULT ('now'::text)::date,
  piece bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_compta_justificatif_bon_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_justificatif_bon_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_justificatif_bon_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_bon_provisoire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_justificatif_bon
  OWNER TO postgres;  
  
CREATE TABLE yvs_compta_plan_analytique
(
  id bigserial NOT NULL,
  code_plan character varying,
  intitule character varying,
  description character varying,
  actif boolean,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  societe bigint,
  CONSTRAINT yvs_compta_plan_analytique_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_plan_analytique_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_plan_analytique_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_plan_analytique
  OWNER TO postgres;
  
CREATE TABLE yvs_compta_repartition_analytique
(
  id bigserial NOT NULL,
  principal bigint,
  secondaire bigint,
  taux double precision DEFAULT 0,
  unite integer,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_compta_repartition_analytique_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_repartition_analytique_principal_fkey FOREIGN KEY (principal)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_repartition_analytique_secondaire_fkey FOREIGN KEY (secondaire)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_repartition_analytique_unite_fkey FOREIGN KEY (unite)
      REFERENCES yvs_base_unite_mesure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_repartition_analytique
  OWNER TO postgres;  
  
CREATE TABLE yvs_prod_ressource_production_employe
(
  id serial NOT NULL,
  employe bigint,
  ressource bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_prod_ressource_production_employe_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_ressource_production_employe_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_ressource_production_employe_employe_fkey FOREIGN KEY (employe)
      REFERENCES yvs_grh_employes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_ressource_production_employe_ressourcefkey FOREIGN KEY (ressource)
      REFERENCES yvs_prod_ressource_production (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_ressource_production_employe
  OWNER TO postgres;  
  
CREATE TABLE yvs_prod_ressource_production_tiers
(
  id serial NOT NULL,
  tiers bigint,
  ressource bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_prod_ressource_production_tiers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_ressource_production_tiers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_ressource_production_tiers_tiers_fkey FOREIGN KEY (tiers)
      REFERENCES yvs_base_tiers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_ressource_production_tiers_ressourcefkey FOREIGN KEY (ressource)
      REFERENCES yvs_prod_ressource_production (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_ressource_production_tiers
  OWNER TO postgres;
  
  
DROP TABLE yvs_compta_decomposition_ce_anal;
  
  
ALTER TABLE yvs_compta_caisse_doc_divers ADD COLUMN parent bigint;
ALTER TABLE yvs_compta_caisse_doc_divers
  ADD CONSTRAINT yvs_compta_caisse_doc_divers_parent_fkey FOREIGN KEY (parent)
      REFERENCES yvs_compta_caisse_doc_divers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;	  
  
ALTER TABLE yvs_compta_centre_analytique ADD COLUMN type_centre character varying;
ALTER TABLE yvs_compta_centre_analytique ADD COLUMN unite_oeuvre integer;
ALTER TABLE yvs_compta_centre_analytique 
 ADD CONSTRAINT yvs_compta_centre_analytique_unite_oeuvre_fkey FOREIGN KEY (unite_oeuvre)
      REFERENCES yvs_base_unite_mesure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_compta_centre_analytique ADD COLUMN plan integer;
ALTER TABLE yvs_compta_centre_analytique
  ADD CONSTRAINT yvs_compta_centre_analytique_plan_fkey FOREIGN KEY (plan)
      REFERENCES yvs_compta_plan_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_centre_valorisation ADD COLUMN type_valeur character(1);
ALTER TABLE yvs_base_centre_valorisation ALTER COLUMN type_valeur SET DEFAULT 'D'::bpchar;
ALTER TABLE yvs_base_centre_valorisation ADD COLUMN centre_analyse bigint;
ALTER TABLE yvs_base_centre_valorisation
  ADD CONSTRAINT yvs_base_centre_valorisation_centre_analyse_fkey FOREIGN KEY (centre_analyse)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_prod_poste_charge ADD COLUMN centre_valorisation integer;
ALTER TABLE yvs_prod_poste_charge
  ADD CONSTRAINT yvs_prod_poste_charge_centre_valorisation_fkey FOREIGN KEY (centre_valorisation)
      REFERENCES yvs_base_centre_valorisation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_prod_ressource_production DROP COLUMN employe;
ALTER TABLE yvs_prod_ressource_production DROP COLUMN tiers;
ALTER TABLE yvs_prod_ressource_production ADD COLUMN type_valeur character(1);
ALTER TABLE yvs_prod_ressource_production ALTER COLUMN type_valeur SET DEFAULT 'D'::bpchar;

