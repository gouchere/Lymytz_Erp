-- Table: yvs_societes_infos_suppl
-- DROP TABLE yvs_societes_infos_suppl;
CREATE TABLE yvs_societes_infos_suppl
(
  id bigserial NOT NULL,
  titre character varying,
  valeur character varying,
  type character varying,
  societe bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_societes_infos_suppl_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_societes_infos_suppl_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_societes_infos_suppl_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_societes_infos_suppl
  OWNER TO postgres;
  
  
-- Table: yvs_print_footer
-- DROP TABLE yvs_print_footer;
CREATE TABLE yvs_print_footer
(
  id bigserial NOT NULL,
  nom character varying,
  model character varying,
  societe bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT now(),
  date_save timestamp without time zone DEFAULT now(),
  view_adresse_societe boolean DEFAULT true,
  view_siege_societe boolean DEFAULT true,
  view_bp_societe boolean DEFAULT true,
  view_fax_societe boolean DEFAULT true,
  view_email_societe boolean DEFAULT true,
  view_phone_societe boolean DEFAULT true,
  view_site_societe boolean DEFAULT true,
  view_capital_societe boolean DEFAULT true,
  view_contrib_societe boolean DEFAULT true,
  view_registr_societe boolean DEFAULT true,
  view_agreement_societe boolean DEFAULT true,
  CONSTRAINT yvs_print_footer_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_print_footer_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_print_footer_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_print_footer
  OWNER TO postgres;
  
  
ALTER TABLE yvs_print_facture_vente ADD COLUMN view_adresse_client boolean DEFAULT true;
ALTER TABLE yvs_print_facture_vente ADD COLUMN view_nui_client boolean DEFAULT true;
ALTER TABLE yvs_print_facture_vente ADD COLUMN view_rcc_client boolean DEFAULT true;
ALTER TABLE yvs_print_facture_vente ADD COLUMN view_image_payer boolean DEFAULT true;
ALTER TABLE yvs_print_facture_vente ADD COLUMN view_image_livrer boolean DEFAULT true;
ALTER TABLE yvs_print_facture_vente ADD COLUMN view_impaye_vente boolean DEFAULT true;
ALTER TABLE yvs_print_facture_vente ADD COLUMN view_penalite_facture boolean DEFAULT true;

ALTER TABLE yvs_print_facture_vente ADD COLUMN footer bigint;
ALTER TABLE yvs_print_facture_vente
  ADD CONSTRAINT yvs_print_facture_vente_footer_fkey FOREIGN KEY (footer)
      REFERENCES yvs_print_footer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

  
ALTER TABLE yvs_base_tiers_document ADD COLUMN numero character varying;
ALTER TABLE yvs_base_tiers_document ADD COLUMN type character varying;


ALTER TABLE yvs_base_point_vente ADD COLUMN saisie_phone_obligatoire boolean default false;