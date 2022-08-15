-- Table: yvs_contactez_nous
-- DROP TABLE yvs_contactez_nous;
CREATE TABLE yvs_contactez_nous
(
  id bigserial NOT NULL,
  email character varying,
  phone character varying,
  societe character varying,
  message character varying,
  actif boolean default true,
  execute_trigger character varying,
  CONSTRAINT yvs_contactez_nous_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_contactez_nous
  OWNER TO postgres;