ALTER TABLE yvs_prod_composant_nomenclature ADD COLUMN ordre INTEGER DEFAULT 0;
ALTER TABLE yvs_prod_composant_of ADD COLUMN ordre INTEGER DEFAULT 0;


INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('prod_gamme_load_all', 'Charger toutes les gammes', 'Charger toutes les gammes sans distinction du site', (SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_gammes'));
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('prod_nomenc_load_all', 'Charger toutes les nomclatures', 'Charger toutes les nomclatures sans distinction du site', (SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_nomenclature'));

-- Table: yvs_prod_gamme_site
-- DROP TABLE yvs_prod_gamme_site;
CREATE TABLE yvs_prod_gamme_site
(
  id bigserial NOT NULL,
  gamme integer,
  site integer,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_prod_gamme_site_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_gamme_site_gamme_fkey FOREIGN KEY (gamme)
      REFERENCES yvs_prod_gamme_article (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_gamme_site_site_fkey FOREIGN KEY (site)
      REFERENCES yvs_prod_site_production (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_gamme_site_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_gamme_site
  OWNER TO postgres;
  
-- Table: yvs_prod_nomenclature_site
-- DROP TABLE yvs_prod_nomenclature_site;
CREATE TABLE yvs_prod_nomenclature_site
(
  id bigserial NOT NULL,
  nomenclature integer,
  site integer,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_prod_nomenclature_site_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_nomenclature_site_nomenclature_fkey FOREIGN KEY (nomenclature)
      REFERENCES yvs_prod_nomenclature (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_nomenclature_site_site_fkey FOREIGN KEY (site)
      REFERENCES yvs_prod_site_production (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_nomenclature_site_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_nomenclature_site
  OWNER TO postgres;
  
  
ALTER TABLE yvs_prod_creneau_equipe_production DROP COLUMN crenau;
ALTER TABLE yvs_prod_creneau_equipe_production ADD COLUMN permanent boolean;
ALTER TABLE yvs_prod_creneau_equipe_production ADD COLUMN site bigint;
ALTER TABLE yvs_prod_creneau_equipe_production
  ADD CONSTRAINT yvs_prod_creneau_equipe_production_site_fkey FOREIGN KEY (site)
      REFERENCES yvs_prod_site_production (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_prod_creneau_equipe_production ADD COLUMN tranche bigint;
ALTER TABLE yvs_prod_creneau_equipe_production
  ADD CONSTRAINT yvs_prod_creneau_equipe_production_tranche_fkey FOREIGN KEY (tranche)
      REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_prod_creneau_equipe_production ADD COLUMN users bigint;
ALTER TABLE yvs_prod_creneau_equipe_production
  ADD CONSTRAINT yvs_prod_creneau_equipe_production_users_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
ALTER TABLE yvs_prod_of_suivi_flux DROP COLUMN tranche_horaire;
ALTER TABLE yvs_prod_of_suivi_flux DROP COLUMN equipe;
ALTER TABLE yvs_prod_of_suivi_flux ADD COLUMN creneau bigint;
ALTER TABLE yvs_prod_of_suivi_flux
  ADD CONSTRAINT yvs_prod_of_suivi_flux_creneau_fkey FOREIGN KEY (creneau)
      REFERENCES yvs_prod_creneau_equipe_production (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_prod_of_suivi_flux DROP COLUMN creneau;
ALTER TABLE yvs_prod_of_suivi_flux ADD COLUMN equipe bigint;
ALTER TABLE yvs_prod_of_suivi_flux
  ADD CONSTRAINT yvs_prod_of_suivi_flux_equipe_fkey FOREIGN KEY (equipe)
      REFERENCES yvs_prod_equipe_production (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_prod_of_suivi_flux ADD COLUMN tranche bigint;
ALTER TABLE yvs_prod_of_suivi_flux
  ADD CONSTRAINT yvs_prod_of_suivi_flux_tranche_fkey FOREIGN KEY (tranche)
      REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_prod_gamme_article ADD COLUMN acces bigint;
ALTER TABLE yvs_prod_gamme_article
  ADD CONSTRAINT yvs_prod_gamme_article_acces_fkey FOREIGN KEY (acces)
      REFERENCES yvs_base_code_acces (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_prod_nomenclature ADD COLUMN acces bigint;
ALTER TABLE yvs_prod_nomenclature
  ADD CONSTRAINT yvs_prod_nomenclature_acces_fkey FOREIGN KEY (acces)
      REFERENCES yvs_base_code_acces (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;