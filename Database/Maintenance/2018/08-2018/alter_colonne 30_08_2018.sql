ALTER TABLE yvs_com_remise ADD COLUMN description character varying;
DROP TABLE yvs_com_plan_remise;

ALTER TABLE yvs_base_caisse ADD COLUMN caissier bigint;
ALTER TABLE yvs_base_caisse
  ADD CONSTRAINT yvs_base_caisse_caissier_fkey FOREIGN KEY (caissier)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Table: yvs_base_groupe_societe
-- DROP TABLE yvs_base_groupe_societe;
CREATE TABLE yvs_base_groupe_societe
(
  id serial NOT NULL,
  libelle character varying,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  CONSTRAINT yvs_base_groupe_societe_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_groupe_societe_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_groupe_societe
  OWNER TO postgres;
  
-- Table: yvs_base_classes_stat
-- DROP TABLE yvs_base_classes_stat;
CREATE TABLE yvs_base_classes_stat
(
  id bigserial NOT NULL,
  code_ref character varying,
  designation character varying,
  actif boolean,
  visible_synthese boolean,
  visible_journal boolean,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  societe bigint,
  CONSTRAINT yvs_base_classes_stat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_classes_stat_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_classes_stat_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_classes_stat
  OWNER TO postgres;

  
UPDATE yvs_societes SET groupe = NULL;
ALTER TABLE yvs_societes
  ADD CONSTRAINT yvs_societes_groupe_fkey FOREIGN KEY (groupe)
      REFERENCES yvs_base_groupe_societe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;