-- Table: yvs_societes_connexion
-- DROP TABLE yvs_societes_connexion;
CREATE TABLE yvs_societes_connexion
(
  id bigserial NOT NULL,
  users character varying,
  password character varying,
  domain character varying,
  port integer DEFAULT 0,
  type_connexion character varying DEFAULT 'DESKTOP'::character varying,
  societe bigint,
  CONSTRAINT yvs_societes_connexion_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_societes_connexion_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_societes_connexion
  OWNER TO postgres;
