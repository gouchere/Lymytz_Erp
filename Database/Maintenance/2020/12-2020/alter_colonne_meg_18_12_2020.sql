ALTER TABLE yvs_compta_content_journal ADD COLUMN report boolean DEFAULT false;

ALTER TABLE yvs_compta_parametre ADD COLUMN report_by_agence boolean DEFAULT false;

ALTER TABLE yvs_base_mode_reglement ADD COLUMN visible_on_print_vente boolean DEFAULT false;

-- Table: yvs_compta_report_compte********************
-- DROP TABLE yvs_compta_report_compte;
CREATE TABLE yvs_compta_report_compte
(
  id bigserial NOT NULL,
  compte bigint,
  exercice bigint,
  agence bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_compta_report_compte_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_report_compte_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_report_compte_compte_fkey FOREIGN KEY (compte)
      REFERENCES yvs_base_plan_comptable (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_report_compte_exercice_fkey FOREIGN KEY (exercice)
      REFERENCES yvs_base_exercice (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_report_compte_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_report_compte
  OWNER TO postgres;
  
  
-- Table: yvs_base_mode_reglement_banque
-- DROP TABLE yvs_base_mode_reglement_banque;
CREATE TABLE yvs_base_mode_reglement_banque
(
  id bigserial NOT NULL,
  code_banque character varying,
  code_guichet character varying,
  numero character varying,
  cle character varying,
  mode integer,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_base_mode_reglement_banque_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_mode_reglement_banque_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_mode_reglement_banque_mode_fkey FOREIGN KEY (mode)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_mode_reglement_banque
  OWNER TO postgres;
