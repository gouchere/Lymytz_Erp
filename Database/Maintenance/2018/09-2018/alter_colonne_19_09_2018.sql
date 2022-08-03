ALTER TABLE yvs_prod_indicateur_op RENAME COLUMN description TO commentaire;
ALTER TABLE yvs_prod_indicateur_op RENAME COLUMN reference TO type_indicateur;
ALTER TABLE yvs_prod_indicateur_op RENAME COLUMN phase_gamme TO composant_op;
ALTER TABLE yvs_prod_indicateur_op DROP COLUMN indicateur_reussite;
ALTER TABLE yvs_prod_indicateur_op DROP COLUMN valeur;

ALTER TABLE yvs_prod_indicateur_op DROP CONSTRAINT yvs_prod_indicateur_phase_phase_gamme_fkey;

ALTER TABLE yvs_prod_indicateur_op
  ADD CONSTRAINT yvs_prod_indicateur_composant_op_fkey FOREIGN KEY (composant_op)
      REFERENCES yvs_prod_composant_op (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

	  

CREATE TABLE yvs_prod_valeurs_qualitative
(
  id bigserial NOT NULL,
  valeur_text character varying,
  code_couleur character varying,
  ordre integer,
  indicateur bigint,
  valeur_quantitative double precision,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  CONSTRAINT yvs_prod_valeurs_qualitative_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_valeurs_qualitative_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_valeurs_qualitative_indicateur_fkey FOREIGN KEY (indicateur)
      REFERENCES yvs_prod_indicateur_op (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_valeurs_qualitative
  OWNER TO postgres;

  -- Table: yvs_prod_of_suivi_flux

-- DROP TABLE yvs_prod_of_suivi_flux;

CREATE TABLE yvs_prod_of_suivi_flux
(
  id bigserial NOT NULL,
  quantite double precision,
  equipe bigint,
  date_flux date,
  tranche_horaire bigint,
  composant bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  CONSTRAINT yvs_prod_of_suivi_flux_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_of_suivi_flux_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_of_suivi_flux_composant_fkey FOREIGN KEY (composant)
      REFERENCES yvs_prod_flux_composant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_of_suivi_flux_equipe_fkey FOREIGN KEY (equipe)
      REFERENCES yvs_prod_equipe_production (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_of_suivi_flux_tranche_horaire_fkey FOREIGN KEY (tranche_horaire)
      REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_of_suivi_flux
  OWNER TO postgres;

  -- Table: yvs_prod_of_suivi_flux

-- DROP TABLE yvs_prod_of_suivi_flux;

CREATE TABLE yvs_prod_of_indicateur_suivi
(
  id bigserial NOT NULL,
  quantite_borne double precision, 
  valeur_text character varying,   
  code_couleur character varying,   
  composant bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  CONSTRAINT yvs_prod_of_suivi_flux_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_of_suivi_flux_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_of_suivi_flux_composant_fkey FOREIGN KEY (composant)
      REFERENCES yvs_prod_flux_composant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_of_indicateur_suivi
  OWNER TO postgres;

 ALTER TABLE yvs_prod_of_indicateur_suivi DROP CONSTRAINT yvs_prod_of_indicateur_suivi_composant_fkey;

ALTER TABLE yvs_prod_of_indicateur_suivi
  ADD CONSTRAINT yvs_prod_of_indicateur_suivi_composant_fkey FOREIGN KEY (composant)
      REFERENCES yvs_prod_flux_composant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;


ALTER TABLE yvs_workflow_etape_validation ADD COLUMN societe bigint;
ALTER TABLE yvs_workflow_etape_validation ALTER COLUMN societe SET DEFAULT 2297;
ALTER TABLE yvs_workflow_etape_validation
  ADD CONSTRAINT yvs_workflow_etape_validation_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_prod_ordre_fabrication DROP COLUMN site_production;

ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN site_production bigint;
ALTER TABLE yvs_prod_ordre_fabrication
  ADD CONSTRAINT yvs_prod_ordre_fabrication_site_production_fkey FOREIGN KEY (site_production)
      REFERENCES yvs_prod_site_production (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_prod_parametre ADD COLUMN limite_vu_of integer;
ALTER TABLE yvs_prod_parametre ADD COLUMN limite_create_of integer;


ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN taux_evolution double precision;
ALTER TABLE yvs_prod_ordre_fabrication ALTER COLUMN taux_evolution SET DEFAULT 0;


ALTER TABLE yvs_compta_mouvement_caisse ADD COLUMN etape_total integer default 1;
ALTER TABLE yvs_compta_mouvement_caisse ADD COLUMN etape_valide integer default 1;


ALTER TABLE yvs_base_article_depot ADD COLUMN default_cond bigint;
COMMENT ON COLUMN yvs_base_article_depot.default_cond IS 'Conditionnement par défaut de l''article dans le dépôt';

ALTER TABLE yvs_base_article_depot
  ADD CONSTRAINT yvs_base_article_depot_default_cond_fkey FOREIGN KEY (default_cond)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

