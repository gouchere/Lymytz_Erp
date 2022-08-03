
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('fa_update_doc_valid', 'Modifier une facture déjà validée', 'Modifier une facture même si elle est déjà validée',
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fa'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_cheq_reg_vente_annul_comptabilite', 'Annuler la comptabilisation des chèques de ventes', 'Annuler la comptabilisation de ldes chèques de ventes',
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_cheq_reg_vente'));
	
-- Table: yvs_compta_content_journal_etape_piece_vente
DROP TABLE yvs_compta_content_journal_etape_piece_vente;

CREATE TABLE yvs_compta_content_journal_etape_piece_vente
(
  id bigserial NOT NULL,
  etape bigint,
  piece bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_content_journal_etape_piece_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_content_journal_etape_piece_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_content_journal_etape_piece_vente_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_content_journal_piece_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_content_journal_etape_piece_vente_reglement_fkey FOREIGN KEY (etape)
      REFERENCES yvs_compta_phase_piece (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_content_journal_etape_piece_vente
  OWNER TO postgres;
