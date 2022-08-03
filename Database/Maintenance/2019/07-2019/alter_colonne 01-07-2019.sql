-- Table: yvs_compta_content_journal_entete_facture_vente
-- DROP TABLE yvs_compta_content_journal_entete_facture_vente;
CREATE TABLE yvs_compta_content_journal_entete_facture_vente
(
  id bigserial NOT NULL,
  entete bigint,
  piece bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_content_journal_entete_facture_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_content_journal_entete_facture_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_content_journal_entete_facture_vente_entete_fkey FOREIGN KEY (entete)
      REFERENCES yvs_com_entete_doc_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_content_journal_entete_facture_vente_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_pieces_comptable (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_content_journal_entete_facture_vente
  OWNER TO postgres;
  
-- Table: yvs_compta_content_journal_entete_bulletin
-- DROP TABLE yvs_compta_content_journal_entete_bulletin;
CREATE TABLE yvs_compta_content_journal_entete_bulletin
(
  id bigserial NOT NULL,
  entete bigint,
  piece bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_content_journal_entete_bulletin_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_content_journal_entete_bulletin_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_content_journal_entete_bulletin_entete_fkey FOREIGN KEY (entete)
      REFERENCES yvs_grh_ordre_calcul_salaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_content_journal_entete_bulletin_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_pieces_comptable (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_content_journal_entete_bulletin
  OWNER TO postgres;
  
  
-- Table: yvs_compta_content_journal_piece_mission
-- DROP TABLE yvs_compta_content_journal_piece_mission;
CREATE TABLE yvs_compta_content_journal_piece_mission
(
  id bigserial NOT NULL,
  reglement bigint,
  piece bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_content_journal_piece_mission_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_content_journal_piece_mission_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_content_journal_piece_mission_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_pieces_comptable (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_content_journal_piece_mission_reglement_fkey FOREIGN KEY (reglement)
      REFERENCES yvs_compta_caisse_piece_mission (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_content_journal_piece_mission
  OWNER TO postgres;
  
  
ALTER TABLE yvs_com_doc_ventes ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_com_doc_ventes ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_com_doc_achats ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_com_doc_achats ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_com_entete_doc_vente ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_com_entete_doc_vente ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_caisse_doc_divers ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_caisse_doc_divers ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_grh_ordre_calcul_salaire ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_grh_ordre_calcul_salaire ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_grh_bulletins ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_grh_bulletins ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_caisse_piece_vente ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_caisse_piece_vente ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_caisse_piece_divers ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_caisse_piece_divers ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_caisse_piece_achat ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_caisse_piece_achat ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_acompte_client ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_acompte_client ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_acompte_fournisseur ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_acompte_fournisseur ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_reglement_credit_client ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_reglement_credit_client ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_reglement_credit_fournisseur ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_reglement_credit_fournisseur ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_abonement_doc_divers ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_abonement_doc_divers ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_credit_client ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_credit_client ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_credit_fournisseur ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_credit_fournisseur ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_phase_acompte_achat ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_phase_acompte_achat ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_phase_acompte_vente ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_phase_acompte_vente ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_phase_piece_achat ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_phase_piece_achat ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_phase_piece_divers ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_phase_piece_divers ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_phase_piece ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_phase_piece ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_phase_piece_virement ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_phase_piece_virement ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_phase_reglement_credit_client ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_phase_reglement_credit_client ALTER COLUMN comptabilise SET DEFAULT false;

ALTER TABLE yvs_compta_phase_reglement_credit_fournisseur ADD COLUMN comptabilise boolean;
ALTER TABLE yvs_compta_phase_reglement_credit_fournisseur ALTER COLUMN comptabilise SET DEFAULT false;