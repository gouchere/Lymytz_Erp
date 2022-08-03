-- Table: yvs_compta_phase_reglement_credit_fournisseur
-- DROP TABLE yvs_compta_phase_reglement_credit_fournisseur;
CREATE TABLE yvs_compta_phase_reglement_credit_fournisseur
(
  id bigserial NOT NULL,
  reglement bigint,
  phase_reg bigint,
  phase_ok boolean,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_phase_reglement_credit_fournisseur_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_phase_reglement_credit_fournisseur_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_phase_reglement_credit_fournisseur_phase_reg_fkey FOREIGN KEY (phase_reg)
      REFERENCES yvs_compta_phase_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_phase_reglement_credit_fournisseur_reglement_fkey FOREIGN KEY (reglement)
      REFERENCES yvs_compta_reglement_credit_fournisseur (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_phase_reglement_credit_fournisseur
  OWNER TO postgres;
  
  
ALTER TABLE yvs_compta_credit_client ADD COLUMN journal bigint;
ALTER TABLE yvs_compta_credit_client
  ADD CONSTRAINT yvs_compta_credit_client_journal_fkey FOREIGN KEY (journal)
      REFERENCES yvs_compta_journaux (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_compta_credit_client ADD COLUMN type_credit bigint;
ALTER TABLE yvs_compta_credit_client
  ADD CONSTRAINT yvs_compta_credit_client_type_credit_fkey FOREIGN KEY (type_credit)
      REFERENCES yvs_grh_type_cout (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_compta_credit_fournisseur ADD COLUMN journal bigint;
ALTER TABLE yvs_compta_credit_fournisseur
  ADD CONSTRAINT yvs_compta_credit_fournisseur_journal_fkey FOREIGN KEY (journal)
      REFERENCES yvs_compta_journaux (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_compta_credit_fournisseur ADD COLUMN type_credit bigint;
ALTER TABLE yvs_compta_credit_fournisseur
  ADD CONSTRAINT yvs_compta_credit_fournisseur_type_credit_fkey FOREIGN KEY (type_credit)
      REFERENCES yvs_grh_type_cout (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;