-- Table: yvs_grh_ioem_device
-- DROP TABLE yvs_grh_ioem_device;
CREATE TABLE yvs_grh_ioem_device
(
  id bigserial NOT NULL,
  machine text,
  employe bigint,
  verify_mode integer,
  in_out_mode integer,
  work_code integer,
  reserved integer,
  date_action date,
  time_action time without time zone,
  date_time_action timestamp without time zone,
  pointeuse integer,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::timestamp,
  date_save timestamp without time zone DEFAULT ('now'::text)::timestamp,
  CONSTRAINT yvs_grh_ioem_device_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_ioem_device_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_ioem_device_pointeuse_fkey FOREIGN KEY (pointeuse)
      REFERENCES yvs_pointeuse (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_ioem_device
  OWNER TO postgres;

ALTER TABLE yvs_pointeuse ADD COLUMN agence bigint;
ALTER TABLE yvs_pointeuse
  ADD CONSTRAINT yvs_pointeuse_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
