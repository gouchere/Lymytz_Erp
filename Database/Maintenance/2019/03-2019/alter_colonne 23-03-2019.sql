-- Table: yvs_com_param_ration_suspension
-- DROP TABLE yvs_com_param_ration_suspension;
CREATE TABLE yvs_com_param_ration_suspension
(
  id bigserial NOT NULL,
  personnel bigint,
  debut_suspension date DEFAULT ('now'::text)::date,
  fin_suspension date DEFAULT ('now'::text)::date,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  CONSTRAINT yvs_com_param_ration_suspension_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_param_ration_suspension_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_param_ration_suspension_personnel_fkey FOREIGN KEY (personnel)
      REFERENCES yvs_com_param_ration (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_param_ration_suspension
  OWNER TO postgres;
  
  
ALTER TABLE yvs_com_param_ration DROP COLUMN suspendu;
ALTER TABLE yvs_com_param_ration DROP COLUMN debut_suspension;
ALTER TABLE yvs_com_param_ration DROP COLUMN fin_suspension;