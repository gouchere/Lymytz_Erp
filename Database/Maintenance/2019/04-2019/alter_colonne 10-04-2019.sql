-- Table: yvs_compta_centre_mission
-- DROP TABLE yvs_compta_centre_mission;
CREATE TABLE yvs_compta_centre_mission
(
  id bigserial NOT NULL,
  mission bigint,
  centre bigint,
  coefficient double precision,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_centre_mission_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_centre_mission_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_centre_mission_centre_fkey FOREIGN KEY (centre)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_centre_mission_mission_fkey FOREIGN KEY (mission)
      REFERENCES yvs_grh_missions (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_centre_mission
  OWNER TO postgres;
