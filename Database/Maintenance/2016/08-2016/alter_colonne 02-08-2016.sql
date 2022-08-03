-- Table: yvs_base_tiers_template

-- DROP TABLE yvs_base_tiers_template;

CREATE TABLE yvs_base_tiers_template
(
  id bigint NOT NULL DEFAULT nextval('yvs_tiers_template_id_seq'::regclass),
  add_secteur boolean DEFAULT true,
  taille_secteur integer,
  add_nom boolean DEFAULT true,
  taille_nom integer,
  add_prenom boolean DEFAULT true,
  taille_prenom integer,
  add_separateur boolean DEFAULT true,
  separateur character varying(255),
  compte_collectif bigint,
  categorie_comptable bigint,
  categorie_tarifaire bigint,
  ristourne bigint,
  comission bigint,
  mdr integer,
  always_visible boolean,
  agence integer,
  secteur bigint,
  author bigint,
  pays bigint,
  ville bigint,
  CONSTRAINT yvs_tiers_template_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_tiers_template_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_tiers_template_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_tiers_template_categorie_comptable_fkey FOREIGN KEY (categorie_comptable)
      REFERENCES yvs_base_categorie_comptable (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_tiers_template_categorie_tarifaire_fkey FOREIGN KEY (categorie_tarifaire)
      REFERENCES yvs_base_plan_tarifaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_tiers_template_comission_fkey FOREIGN KEY (comission)
      REFERENCES yvs_plan_de_recompense (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT yvs_tiers_template_compte_collectif_fkey FOREIGN KEY (compte_collectif)
      REFERENCES yvs_base_plan_comptable (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_tiers_template_mdr_fkey FOREIGN KEY (mdr)
      REFERENCES yvs_base_model_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_tiers_template_pays_fkey FOREIGN KEY (pays)
      REFERENCES yvs_dictionnaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_tiers_template_ristourne_fkey FOREIGN KEY (ristourne)
      REFERENCES yvs_plan_de_recompense (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_tiers_template_secteur_fkey FOREIGN KEY (secteur)
      REFERENCES yvs_dictionnaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_tiers_template_ville_fkey FOREIGN KEY (ville)
      REFERENCES yvs_dictionnaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_tiers_template
  OWNER TO postgres;
