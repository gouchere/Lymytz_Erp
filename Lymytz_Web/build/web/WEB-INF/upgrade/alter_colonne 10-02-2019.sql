SELECT alter_action_colonne_key('yvs_com_plan_ristourne', 'yvs_com_ristourne', true, true);
SELECT alter_action_colonne_key('yvs_com_ristourne', 'yvs_com_grille_ristourne', true, true);

-- Table: yvs_compta_content_journal_abonnement_divers
-- DROP TABLE yvs_compta_content_journal_abonnement_divers;
CREATE TABLE yvs_compta_content_journal_abonnement_divers
(
  id bigserial NOT NULL,
  abonnement bigint,
  piece bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_content_journal_abonnement_divers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_content_journal_abonnement_divers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_content_journal_abonnement_divers_abonnement_fkey FOREIGN KEY (abonnement)
      REFERENCES yvs_compta_abonement_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_content_journal_abonnement_divers_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_pieces_comptable (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_content_journal_abonnement_divers
  OWNER TO postgres;
