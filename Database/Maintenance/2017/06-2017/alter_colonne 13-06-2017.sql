CREATE TABLE yvs_mut_frais_type_credit
(
  id bigserial NOT NULL,
  type bigint,
  credit bigint,
  montant double precision DEFAULT 0,
  author bigint,
  CONSTRAINT yvs_mut_frais_type_credit_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_mut_frais_type_credit_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_mut_frais_type_credit_credit_fkey FOREIGN KEY (credit)
      REFERENCES yvs_mut_type_credit (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_mut_frais_type_credit_type_fkey FOREIGN KEY (type)
      REFERENCES yvs_grh_type_cout (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_mut_frais_type_credit
  OWNER TO postgres;
  
  
ALTER TABLE yvs_mut_type_credit ADD COLUMN nbre_avalise double precision default 0;
ALTER TABLE yvs_mut_type_credit ADD COLUMN periodicite integer default 1;
ALTER TABLE yvs_mut_type_credit ADD COLUMN type_mensualite character varying(1) default 'M';
ALTER TABLE yvs_mut_type_credit ADD COLUMN formule_interet character varying(1) default 'S';
ALTER TABLE yvs_mut_type_credit ADD COLUMN model_remboursement character varying(1) default 'M';
ALTER TABLE yvs_mut_type_credit ADD COLUMN reechellonage_possible boolean default false;
ALTER TABLE yvs_mut_type_credit ADD COLUMN fusion_possible boolean default false;
ALTER TABLE yvs_mut_type_credit ADD COLUMN anticipation_possible boolean default false;
ALTER TABLE yvs_mut_type_credit ADD COLUMN penalite_anticipation boolean default false;
ALTER TABLE yvs_mut_type_credit ADD COLUMN taux_penalite_anticipation double precision default 0;
ALTER TABLE yvs_mut_type_credit ADD COLUMN base_penalite_anticipation character varying(1) default 'M';
ALTER TABLE yvs_mut_type_credit ADD COLUMN suspension_possible boolean default false;
ALTER TABLE yvs_mut_type_credit ADD COLUMN penalite_suspension boolean default false;
ALTER TABLE yvs_mut_type_credit ADD COLUMN taux_penalite_suspension double precision default 0;
ALTER TABLE yvs_mut_type_credit ADD COLUMN base_penalite_suspension character varying(1) default 'M';
ALTER TABLE yvs_mut_type_credit ADD COLUMN penalite_retard boolean default false;
ALTER TABLE yvs_mut_type_credit ADD COLUMN taux_penalite_retard double precision default 0;
ALTER TABLE yvs_mut_type_credit ADD COLUMN base_penalite_retard character varying(1) default 'M';