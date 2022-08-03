ALTER TABLE yvs_base_point_livraison ADD COLUMN telephone CHARACTER VARYING;
ALTER TABLE yvs_base_point_livraison ADD COLUMN lieu_dit CHARACTER VARYING;
ALTER TABLE yvs_base_point_livraison ADD COLUMN description CHARACTER VARYING;
ALTER TABLE yvs_base_point_livraison ADD COLUMN client BIGINT;
ALTER TABLE yvs_base_point_livraison
  ADD CONSTRAINT yvs_base_point_livraison_client_fkey FOREIGN KEY (client)
      REFERENCES yvs_com_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_doc_ventes_informations
  ADD CONSTRAINT yvs_com_doc_ventes_informations_adresse_livraison_fkey FOREIGN KEY (adresse_livraison)
      REFERENCES yvs_base_point_livraison (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_point_vente ADD COLUMN telephone CHARACTER VARYING;

ALTER TABLE yvs_societes ADD COLUMN a_propos CHARACTER VARYING;

ALTER TABLE yvs_societe_infos_vente RENAME TO yvs_societes_infos_vente;

ALTER TABLE yvs_base_mode_reglement ADD COLUMN numero_marchand CHARACTER VARYING;
ALTER TABLE yvs_base_mode_reglement ADD COLUMN code_paiement CHARACTER VARYING;

ALTER TABLE yvs_base_tarif_point_livraison ADD COLUMN delai_retour INTEGER DEFAULT 0;
ALTER TABLE yvs_base_tarif_point_livraison ADD COLUMN delai_for_retrait INTEGER DEFAULT 0;
ALTER TABLE yvs_base_tarif_point_livraison RENAME delai_livraison TO delai_for_livraison;

CREATE TABLE yvs_base_mode_reglement_informations
(
  id bigserial NOT NULL,
  authorisation_header character varying,
  merchant_key character varying,
  currency character varying,
  "mode" integer,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_base_mode_reglement_informations_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_mode_reglement_informations_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_mode_reglement_informations_mode_fkey FOREIGN KEY (mode)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_mode_reglement_informations
  OWNER TO postgres;

CREATE TABLE yvs_dictionnaire_informations
(
  id bigserial NOT NULL,
  active_livraison boolean DEFAULT false,
  lieux integer,
  societe integer,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_dictionnaire_informations_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_dictionnaire_informations_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_dictionnaire_informations_lieux_fkey FOREIGN KEY (lieux)
      REFERENCES yvs_dictionnaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_dictionnaire_informations_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_dictionnaire_informations
  OWNER TO postgres;