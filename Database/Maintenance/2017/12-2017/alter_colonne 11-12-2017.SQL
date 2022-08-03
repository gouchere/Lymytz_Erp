ALTER TABLE yvs_compta_phase_reglement DROP COLUMN num_compte;

ALTER TABLE yvs_com_doc_stocks ADD COLUMN editeur bigint;
ALTER TABLE yvs_com_doc_stocks
  ADD CONSTRAINT yvs_com_doc_stocks_editeur_fkey FOREIGN KEY (editeur)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_contenu_doc_achat RENAME quantite_attendu TO quantite_attendu_;
ALTER TABLE yvs_com_contenu_doc_achat RENAME quantite_recu TO quantite_attendu;
ALTER TABLE yvs_com_contenu_doc_achat RENAME quantite_attendu_ TO quantite_recu;
ALTER TABLE yvs_com_contenu_doc_achat RENAME pua_attendu TO pua_attendu_;
ALTER TABLE yvs_com_contenu_doc_achat RENAME pua_recu TO pua_attendu;
ALTER TABLE yvs_com_contenu_doc_achat RENAME pua_attendu_ TO pua_recu;
ALTER TABLE yvs_com_contenu_doc_achat RENAME remise_attendu TO remise_attendu_;
ALTER TABLE yvs_com_contenu_doc_achat RENAME remise_recu TO remise_attendu;
ALTER TABLE yvs_com_contenu_doc_achat RENAME remise_attendu_ TO remise_recu;
	  
ALTER TABLE yvs_com_contenu_doc_stock RENAME quantite_resultante TO quantite_entree;
ALTER TABLE yvs_com_contenu_doc_stock RENAME conditionnement_resultante TO conditionnement_entree;
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN prix_entree double precision default 0;
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN qualite_entree bigint;
ALTER TABLE yvs_com_contenu_doc_stock
  ADD CONSTRAINT yvs_com_contenu_doc_stock_qualite_entree_fkey FOREIGN KEY (qualite_entree)
      REFERENCES yvs_com_qualite (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_compta_piece_caisse_mission RENAME TO yvs_compta_caisse_piece_mission;
ALTER SEQUENCE yvs_compta_piece_caisse_mission_id_seq RENAME TO yvs_compta_caisse_piece_mission_id_seq;

CREATE TABLE yvs_compta_piece_caisse_mission
(
  id bigserial NOT NULL,
  piece bigint,
  mission bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_piece_caisse_mission_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_piece_caisse_mission_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_piece_caisse_mission_mission_fkey FOREIGN KEY (mission)
      REFERENCES yvs_grh_missions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_piece_caisse_mission_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_caisse_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_piece_caisse_mission
  OWNER TO postgres;
COMMENT ON TABLE yvs_compta_piece_caisse_mission
  IS 'Reglement des missions par bon provisoire';

