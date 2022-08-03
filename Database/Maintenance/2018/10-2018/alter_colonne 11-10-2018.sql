ALTER TABLE yvs_grh_contrat_emps DROP CONSTRAINT yvs_contrat_emps_mode_paiement_fkey;

UPDATE yvs_grh_contrat_emps SET mode_paiement = 2315 WHERE mode_paiement = 14;
UPDATE yvs_grh_contrat_emps SET mode_paiement = 2324 WHERE mode_paiement = 15;
UPDATE yvs_grh_contrat_emps SET mode_paiement = 2324 WHERE mode_paiement = 20;

ALTER TABLE yvs_grh_contrat_emps
  ADD CONSTRAINT yvs_contrat_emps_mode_paiement_fkey FOREIGN KEY (mode_paiement)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE SET NULL ON DELETE SET NULL;
	  
ALTER TABLE yvs_grh_contrat_emps ADD COLUMN caisse bigint;
ALTER TABLE yvs_grh_contrat_emps
  ADD CONSTRAINT yvs_contrat_emps_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
-- Table: yvs_compta_caisse_piece_salaire
-- DROP TABLE yvs_compta_caisse_piece_salaire;
CREATE TABLE yvs_compta_caisse_piece_salaire
(
  id bigserial NOT NULL,
  numero_piece character varying,
  montant double precision,
  statut_piece character(1),
  caisse bigint,
  author bigint,
  date_piece date,
  date_paiement timestamp without time zone,
  note character varying,
  model bigint,
  caissier bigint,
  date_paiment_prevu date,
  reference_externe character varying,
  date_valide date,
  valide_by bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  montant_recu double precision DEFAULT 0,
  CONSTRAINT yvs_compta_caisse_piece_salaire_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_caisse_piece_salaire_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_salaire_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_salaire_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_salaire_valide_by_fkey FOREIGN KEY (valide_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_piece_caisse_mission_caissier_fkey FOREIGN KEY (caissier)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_caisse_piece_salaire
  OWNER TO postgres;

-- Table: yvs_compta_caisse_piece_bulletin
-- DROP TABLE yvs_compta_caisse_piece_bulletin;
CREATE TABLE yvs_compta_caisse_piece_bulletin
(
  id bigserial NOT NULL,
  piece bigint,
  bulletin bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_caisse_piece_bulletin_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_caisse_piece_bulletin_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_bulletin_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_caisse_piece_salaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_caisse_piece_bulletin_bulletin_fkey FOREIGN KEY (bulletin)
      REFERENCES yvs_grh_bulletins (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_caisse_piece_bulletin
  OWNER TO postgres;


-- Table: yvs_compta_phase_piece_salaire
-- DROP TABLE yvs_compta_phase_piece_salaire;
CREATE TABLE yvs_compta_phase_piece_salaire
(
  id bigserial NOT NULL,
  piece bigint,
  phase_reg bigint,
  phase_ok boolean,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_phase_piece_salaire_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_phase_piece_salaire_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_phase_piece_salaire_phase_reg_fkey FOREIGN KEY (phase_reg)
      REFERENCES yvs_compta_phase_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_phase_piece_salaire_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_caisse_piece_salaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_phase_piece_salaire
  OWNER TO postgres;