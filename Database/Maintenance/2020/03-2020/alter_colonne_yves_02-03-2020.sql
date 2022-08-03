
ALTER TABLE yvs_prod_flux_composant ADD COLUMN ordre integer;

-- Table: yvs_prod_default_depot_composants

-- DROP TABLE yvs_prod_default_depot_composants;

CREATE TABLE yvs_prod_default_depot_composants
(
  id bigserial NOT NULL,
  composant bigint,
  depot_conso bigint,
  site bigint,
  author bigint,
  CONSTRAINT yvs_prod_default_depot_composants_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_default_depot_composants_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_default_depot_composants_composant_fkey FOREIGN KEY (composant)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_default_depot_composants_depot_conso_fkey FOREIGN KEY (depot_conso)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_default_depot_composants_site_fkey FOREIGN KEY (site)
      REFERENCES yvs_prod_site_production (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_default_depot_composants
  OWNER TO postgres;

SELECT insert_droit('prod_update_ppte_composant', 'Modifier les proprités d''un composant', 
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 'A,B,J,F,D','R');