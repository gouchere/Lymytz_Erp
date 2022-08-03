ALTER TABLE yvs_compta_centre_analytique RENAME COLUMN reference TO code_ref
ALTER TABLE yvs_compta_centre_analytique DROP CONSTRAINT yvs_compta_centre_analytique_societe_fkey;
ALTER TABLE yvs_compta_centre_analytique DROP COLUMN societe;
ALTER TABLE yvs_compta_centre_analytique ADD COLUMN plan_anal bigint;


CREATE TABLE yvs_compta_plan_analytique
(
  id bigserial NOT NULL,
  intitule character varying,
  code_plan character varying,
  description character varying,
  actif boolean,
  societe bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_plan_analytique_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_plan_analytique_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_plan_analytique_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_plan_analytique
  OWNER TO postgres;

ALTER TABLE yvs_compta_centre_analytique
  ADD CONSTRAINT yvs_compta_centre_analytique_plan_anal_fkey FOREIGN KEY (plan_anal)
      REFERENCES yvs_compta_plan_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;