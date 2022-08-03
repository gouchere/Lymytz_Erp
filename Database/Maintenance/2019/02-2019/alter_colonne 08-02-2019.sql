-- Table: yvs_base_depots_user
-- DROP TABLE yvs_base_depots_user;
CREATE TABLE yvs_base_depots_user
(
  id bigserial NOT NULL,
  users bigint,
  depot bigint,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  actif boolean DEFAULT true,
  CONSTRAINT yvs_base_depots_user_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_depots_user_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_depots_user_depot_fkey FOREIGN KEY (depot)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_depots_user_users_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_depots_user
  OWNER TO postgres;
  
  
  
ALTER TABLE yvs_prod_site_production ADD COLUMN agence bigint;
ALTER TABLE yvs_prod_site_production
  ADD CONSTRAINT yvs_prod_site_production_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

UPDATE yvs_prod_site_production y SET agence = 
(SELECT DISTINCT ua.agence FROM yvs_prod_ordre_fabrication of INNER JOIN yvs_prod_site_production sp ON of.site_production = sp.id INNER JOIN yvs_users_agence ua ON of.author = ua.id WHERE sp.societe = y.societe);

-- ALTER TABLE yvs_prod_site_production DROP COLUMN societe;