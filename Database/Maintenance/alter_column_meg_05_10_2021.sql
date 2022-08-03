-- Table: yvs_print_ticket_vente
-- DROP TABLE yvs_print_ticket_vente;
CREATE TABLE yvs_print_ticket_vente
(
  id bigserial NOT NULL,
  nom character varying,
  model character varying,
  societe bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT now(),
  date_save timestamp without time zone DEFAULT now(),
  defaut boolean,
  view_name_societe boolean DEFAULT true,
  view_rcc_societe boolean DEFAULT true,
  view_nui_societe boolean DEFAULT true,
  view_phone_societe boolean DEFAULT true,
  view_adresse_societe boolean DEFAULT true,
  view_image_qr_facture boolean DEFAULT true,
  view_name_vendeur boolean DEFAULT true,
  view_numero_facture boolean DEFAULT true,
  view_date_facture boolean DEFAULT true,
  view_name_client boolean DEFAULT true,
  view_phone_client boolean DEFAULT true,
  view_statut_regler_facture boolean DEFAULT true,
  CONSTRAINT yvs_print_ticket_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_print_ticket_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_print_ticket_vente_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_print_ticket_vente
  OWNER TO postgres;
