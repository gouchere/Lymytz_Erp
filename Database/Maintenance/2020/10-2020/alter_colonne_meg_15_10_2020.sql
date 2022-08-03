ALTER TABLE yvs_compta_journaux ADD COLUMN code_acces bigint;
ALTER TABLE yvs_compta_journaux
  ADD CONSTRAINT yvs_compta_journaux_code_acces_fkey FOREIGN KEY (code_acces)
      REFERENCES yvs_base_code_acces (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
-- Table: yvs_synchro_serveurs_version
-- DROP TABLE yvs_synchro_serveurs_version;
CREATE TABLE yvs_synchro_serveurs_version
(
  id bigserial NOT NULL,
  adresse character varying,
  version character varying,
  execute_trigger character varying,
  CONSTRAINT yvs_synchro_serveurs_version_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_synchro_serveurs_version
  OWNER TO postgres;