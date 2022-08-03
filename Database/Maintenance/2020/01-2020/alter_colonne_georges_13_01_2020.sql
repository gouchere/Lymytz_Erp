-- Table: yvs_compta_notif_reglement_doc_divers
-- DROP TABLE yvs_compta_notif_reglement_doc_divers;
CREATE TABLE yvs_compta_notif_reglement_doc_divers
(
  id bigserial NOT NULL,
  date_save date,
  date_update date,
  author bigint,
  piece_doc_divers bigint,
  acompte_client bigint,
  acompte_fournisseur bigint,
  CONSTRAINT yvs_compta_notif_reglement_doc_divers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_notif_reglement_doc_divers_acompte_client_fkey FOREIGN KEY (acompte_client)
      REFERENCES yvs_compta_acompte_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_notif_reglement_doc_divers_acompte_fournisseur_fkey FOREIGN KEY (acompte_fournisseur)
      REFERENCES yvs_compta_acompte_fournisseur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_notif_reglement_doc_divers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_notif_reglement_doc_divers_piece_doc_divers_fkey FOREIGN KEY (piece_doc_divers)
      REFERENCES yvs_compta_caisse_piece_divers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_notif_reglement_doc_divers
  OWNER TO postgres;
