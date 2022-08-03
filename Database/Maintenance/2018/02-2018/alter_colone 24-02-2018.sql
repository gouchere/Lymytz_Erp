-- Table: yvs_compta_affec_anal_emp

-- DROP TABLE yvs_compta_affec_anal_emp;

CREATE TABLE yvs_compta_affec_anal_emp
(
  id bigserial NOT NULL,
  centre bigint,
  employe bigint,
  coeficient double precision,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  CONSTRAINT yvs_compta_affec_anal_emp_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_affec_anal_emp_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_affec_anal_emp_centre_fkey FOREIGN KEY (centre)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_affec_anal_emp_employe_fkey FOREIGN KEY (employe)
      REFERENCES yvs_grh_employes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_affec_anal_emp
  OWNER TO postgres;

  -- Table: yvs_compta_affec_anal_emp

-- DROP TABLE yvs_compta_affec_anal_emp;

CREATE TABLE yvs_compta_affec_anal_departement
(
  id bigserial NOT NULL,
  centre bigint,
  departement bigint,
  coeficient double precision,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  CONSTRAINT yvs_compta_affec_anal_departement_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_affec_anal_departement_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_affec_anal_departement_centre_fkey FOREIGN KEY (centre)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_affec_anal_departement_departement_fkey FOREIGN KEY (departement)
      REFERENCES yvs_grh_departement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_affec_anal_departement
  OWNER TO postgres;
 
ALTER TABLE yvs_base_caisse ADD COLUMN default_caisse boolean;
ALTER TABLE yvs_base_caisse ALTER COLUMN default_caisse SET DEFAULT false;
