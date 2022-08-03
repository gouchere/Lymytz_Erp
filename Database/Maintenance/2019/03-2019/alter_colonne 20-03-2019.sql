-- Table: yvs_base_parametre
-- DROP TABLE yvs_base_parametre;
CREATE TABLE yvs_base_parametre
(
  id bigserial,
  generer_password boolean,
  defaut_password character varying,
  taille_password integer,
  societe bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_base_parametre_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_parametre_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_parametre_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_parametre
  OWNER TO postgres;

