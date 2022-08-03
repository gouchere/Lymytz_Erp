ALTER TABLE yvs_compta_caisse_piece_mission ADD COLUMN reference_externe character varying;

-- Table: yvs_grh_objets_mission_analytique
-- DROP TABLE yvs_grh_objets_mission_analytique;
CREATE TABLE yvs_grh_objets_mission_analytique
(
  id bigserial NOT NULL,
  coefficient double precision DEFAULT 0,
  objet_mission bigint,
  centre bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  CONSTRAINT yvs_grh_objets_mission_analytique_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_objets_mission_analytique_objet_mission_fkey FOREIGN KEY (objet_mission)
      REFERENCES yvs_grh_objets_mission (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_grh_objets_mission_analytique_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_objets_mission_analytique_centre_fkey FOREIGN KEY (centre)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_objets_mission_analytique
  OWNER TO postgres;