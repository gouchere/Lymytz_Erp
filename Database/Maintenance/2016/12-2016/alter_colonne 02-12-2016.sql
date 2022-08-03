ALTER TABLE yvs_base_article_depot DROP COLUMN pr;
ALTER TABLE yvs_base_article_depot DROP COLUMN change_prix;
ALTER TABLE yvs_base_article_depot DROP COLUMN puv_min;
ALTER TABLE yvs_base_article_depot DROP COLUMN nature_prix_min;

ALTER TABLE yvs_com_creneau_horaire DROP COLUMN type_creno;
ALTER TABLE yvs_com_creneau_horaire RENAME TO yvs_com_creneau_depot;

CREATE TABLE yvs_com_creneau_point
(
  id bigserial NOT NULL,
  jour integer,
  tranche bigint,
  point integer,
  actif boolean DEFAULT true,
  author bigint,
  permanent boolean,
  CONSTRAINT yvs_com_creneau_depot_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_creneau_depot_fkey FOREIGN KEY (point)
      REFERENCES yvs_base_point_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_creneau_point_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_creneau_point_tranche_fkey FOREIGN KEY (tranche)
      REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_creneau_jour_fkey FOREIGN KEY (jour)
      REFERENCES yvs_jours_ouvres (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_creneau_depot
  OWNER TO postgres;

ALTER TABLE yvs_com_creneau_horaire_users ADD COLUMN type character varying;
ALTER TABLE yvs_com_creneau_horaire_users ALTER COLUMN type SET DEFAULT 'V'::character varying;
ALTER TABLE yvs_com_creneau_horaire_users DROP COLUMN point;
ALTER TABLE yvs_com_creneau_horaire_users RENAME creneau TO creneau_depot;
ALTER TABLE yvs_com_creneau_horaire_users ADD COLUMN creneau_point bigint;
ALTER TABLE yvs_com_creneau_horaire_users
  ADD CONSTRAINT yvs_com_creneau_horaire_point_fkey FOREIGN KEY (creneau_point)
      REFERENCES yvs_com_creneau_point (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;