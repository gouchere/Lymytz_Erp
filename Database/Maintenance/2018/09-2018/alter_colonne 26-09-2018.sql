
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_od_statut_encours_to_edit', 'Passer du statut en cours au statut éditable de l''OD ', 'Passer du statut en cours de validation au statut éditable de l''OD',
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_od_statut_valid_to_edit', 'Passer du statut validé au statut éditable de l''OD', 'Passer du statut validé au statut éditable de l''OD',
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'));
	
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('compta_od_annul_comptabilite', 'Annuler la comptabilisation de l''OD', 'Annuler la comptabilisation de l''OD',
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'));
	
	
ALTER TABLE yvs_compta_cout_sup_doc_divers ADD COLUMN lier_tiers boolean;
ALTER TABLE yvs_compta_cout_sup_doc_divers ALTER COLUMN lier_tiers SET DEFAULT false;
  
-- Table: yvs_compta_content_journal_facture_vente
-- DROP TABLE yvs_compta_content_journal_facture_vente;
CREATE TABLE yvs_compta_content_journal_facture_vente
(
  id bigserial NOT NULL,
  facture bigint,
  piece bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_content_journal_facture_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_content_journal_facture_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_content_journal_facture_vente_facture_fkey FOREIGN KEY (facture)
      REFERENCES yvs_com_doc_ventes (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_content_journal_facture_vente_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_pieces_comptable (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_content_journal_facture_vente
  OWNER TO postgres;

  
-- Table: yvs_compta_content_journal_bulletin
-- DROP TABLE yvs_compta_content_journal_bulletin;
CREATE TABLE yvs_compta_content_journal_bulletin
(
  id bigserial NOT NULL,
  bulletin bigint,
  piece bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_content_journal_bulletin_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_content_journal_bulletin_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_content_journal_bulletin_bulletin_fkey FOREIGN KEY (bulletin)
      REFERENCES yvs_grh_bulletins (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_content_journal_bulletin_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_pieces_comptable (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_content_journal_bulletin
  OWNER TO postgres;

  
-- Table: yvs_compta_caisse_piece_cout_divers
-- DROP TABLE yvs_compta_caisse_piece_cout_divers;
CREATE TABLE yvs_compta_caisse_piece_cout_divers
(
  id bigserial NOT NULL,
  cout bigint,
  piece bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_caisse_piece_cout_divers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_caisse_piece_cout_divers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_cout_divers_cout_fkey FOREIGN KEY (cout)
      REFERENCES yvs_compta_cout_sup_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_caisse_piece_cout_divers_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_caisse_piece_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_caisse_piece_cout_divers
  OWNER TO postgres;