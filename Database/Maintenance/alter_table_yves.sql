ALTER TABLE yvs_parametre_grh ADD COLUMN min_retard_val double precision;
COMMENT ON COLUMN yvs_parametre_grh.min_retard_val IS 'Seuil minimal de retard à valoriser (en heure)';

ALTER TABLE yvs_grh_presence ADD COLUMN total_retard double precision;
ALTER TABLE yvs_grh_presence ALTER COLUMN total_retard SET DEFAULT 0;
COMMENT ON COLUMN yvs_grh_presence.total_retard IS 'Cumul du retard par fiche';

CREATE TABLE yvs_grh_valorisation_retard
(
  id bigserial NOT NULL,
  fiche bigint,
  bulletin bigint,
  retard_valorise double precision,
  CONSTRAINT yvs_grh_valorisation_retard_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_valorisation_retard_bulletin_fkey FOREIGN KEY (bulletin)
      REFERENCES yvs_grh_bulletins (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT yvs_grh_valorisation_retard_fiche_fkey FOREIGN KEY (fiche)
      REFERENCES yvs_grh_presence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_valorisation_retard
  OWNER TO postgres;

CREATE TABLE yvs_grh_valorisation_retenues
(
  id bigserial NOT NULL,
  retenue bigint,
  bulletin bigint,
  CONSTRAINT yvs_grh_valorisation_retenues_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_valorisation_retenues_bulletin_fkey FOREIGN KEY (bulletin)
      REFERENCES yvs_grh_bulletins (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT yvs_grh_valorisation_retenues_retenue_fkey FOREIGN KEY (retenue)
      REFERENCES yvs_grh_detail_prelevement_emps (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_valorisation_retenues
  OWNER TO postgres;
  
  CREATE TABLE yvs_resource_page_group
(
  id bigserial NOT NULL,
  designation character varying,
  CONSTRAINT yvs_resource_page_group_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_resource_page_group
  OWNER TO postgres;
ALTER TABLE yvs_ressources_page ADD COLUMN groupe bigint;

ALTER TABLE yvs_ressources_page
  ADD CONSTRAINT yvs_ressources_page_groupe_fkey FOREIGN KEY (groupe)
      REFERENCES yvs_resource_page_group (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO yvs_resource_page_group(designation) VALUES ('AFFICHAGE');
INSERT INTO yvs_resource_page_group(designation) VALUES ('CREATION / MODIFICATION');