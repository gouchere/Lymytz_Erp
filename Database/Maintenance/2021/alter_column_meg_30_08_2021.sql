-- Table: yvs_print_header
-- DROP TABLE yvs_print_header;
CREATE TABLE yvs_print_header
(
  id bigserial NOT NULL,
  nom character varying,
  model character varying,
  societe bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT now(),
  date_save timestamp without time zone DEFAULT now(),
  view_name_agence boolean DEFAULT true,
  view_name_societe boolean DEFAULT true,
  view_code_societe boolean DEFAULT true,
  view_description_societe boolean DEFAULT true,
  view_logo_societe boolean DEFAULT true,
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
  CONSTRAINT yvs_print_header_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_print_header_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_print_header_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_print_header
  OWNER TO postgres;
  
  
-- Table: yvs_print_facture_vente
-- DROP TABLE yvs_print_facture_vente;
CREATE TABLE yvs_print_facture_vente
(
  id bigserial NOT NULL,
  nom character varying,
  model character varying,
  societe bigint,
  header bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT now(),
  date_save timestamp without time zone DEFAULT now(),
  defaut boolean,
  view_date_facture boolean DEFAULT true,
  view_numero_facture boolean DEFAULT true,
  view_taxe_facture boolean DEFAULT true,
  view_cout_facture boolean DEFAULT true,
  view_service_facture boolean DEFAULT true,
  view_reglement_facture boolean DEFAULT true,
  view_statut_facture boolean DEFAULT true,
  view_name_vendeur boolean DEFAULT true,
  view_point_vente boolean DEFAULT true,
  view_name_client boolean DEFAULT true,
  view_phone_client boolean DEFAULT true,
  view_signature_vendeur boolean DEFAULT true,
  view_signature_caissier boolean DEFAULT true,
  view_signature_client boolean DEFAULT true,
  CONSTRAINT yvs_print_facture_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_print_facture_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_print_facture_vente_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_print_facture_vente_header_fkey FOREIGN KEY (header)
      REFERENCES yvs_print_header (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_print_facture_vente
  OWNER TO postgres;


