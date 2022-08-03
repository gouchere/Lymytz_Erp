ALTER TABLE yvs_prod_gamme_article RENAME COLUMN reference TO code_ref;
ALTER TABLE yvs_prod_phase_gamme RENAME TO yvs_prod_operations_gamme
ALTER TABLE yvs_prod_operations_gamme RENAME COLUMN reference TO code_ref; 
ALTER TABLE yvs_prod_phase_ordre_fabrication RENAME TO yvs_prod_operations_of;
ALTER TABLE yvs_prod_indicateur_phase RENAME TO yvs_prod_indicateur_op; 
ALTER TABLE yvs_prod_poste_phase RENAME TO yvs_prod_poste_operation; 
ALTER TABLE yvs_prod_composant_ordre_fabrication RENAME TO yvs_prod_composant_of; 



ALTER TABLE yvs_prod_gamme_article ADD COLUMN debut_validite date;
ALTER TABLE yvs_prod_gamme_article ADD COLUMN fin_validite date;
ALTER TABLE yvs_prod_gamme_article ADD COLUMN unite_temps character varying;
ALTER TABLE yvs_prod_gamme_article ADD COLUMN permanant Boolean;
ALTER TABLE yvs_prod_composant_of ADD COLUMN cout_composant double precision;

ALTER TABLE yvs_prod_operations_gamme DROP COLUMN niveau;
ALTER TABLE yvs_prod_operations_gamme DROP COLUMN designation ;
 ALTER TABLE yvs_prod_operations_gamme DROP COLUMN duree;
ALTER TABLE yvs_prod_operations_gamme DROP COLUMN unite_duree;
ALTER TABLE yvs_prod_operations_gamme DROP COLUMN parent;
ALTER TABLE yvs_prod_operations_gamme DROP COLUMN valorisation_phase;
ALTER TABLE yvs_prod_operations_gamme DROP COLUMN type_duree;
ALTER TABLE yvs_prod_operations_gamme RENAME COLUMN phase TO operation; 

ALTER TABLE yvs_prod_operations_gamme ADD COLUMN temps_reglage double precision;
ALTER TABLE yvs_prod_operations_gamme ADD COLUMN temps_operation double precision;
ALTER TABLE yvs_prod_operations_gamme ADD COLUMN type_temps character varying;
ALTER TABLE yvs_prod_operations_gamme ADD COLUMN taux_efficience double precision;
ALTER TABLE yvs_prod_operations_gamme ADD COLUMN taux_perte double precision;

ALTER TABLE yvs_prod_operations_gamme ADD COLUMN quantite_base double precision;
ALTER TABLE yvs_prod_operations_gamme ADD COLUMN cadence double precision;
ALTER TABLE yvs_prod_operations_gamme ADD COLUMN quantite_min double precision;


ALTER TABLE yvs_prod_poste_operation DROP COLUMN pourcentage_utilisation;
ALTER TABLE yvs_prod_poste_operation DROP COLUMN niveau;
ALTER TABLE yvs_prod_poste_operation DROP COLUMN capacite_q;
ALTER TABLE yvs_prod_poste_operation DROP COLUMN capacite_h;
ALTER TABLE yvs_prod_poste_operation DROP COLUMN main_oeuvre_h;
ALTER TABLE yvs_prod_poste_operation DROP COLUMN main_oeuvre_q;
ALTER TABLE yvs_prod_poste_operation DROP COLUMN temps_rebus;
 
 
ALTER TABLE yvs_prod_poste_operation RENAME COLUMN phase_gamme TO operations; 
ALTER TABLE yvs_prod_poste_operation ADD COLUMN type_charge character varying; --Main d'oeuvre ou machine

-- Table: yvs_prod_composant_op

-- DROP TABLE yvs_prod_composant_op;

CREATE TABLE yvs_prod_composant_op
(
  id serial NOT NULL,
  sens character(1),
  composant bigint,
  operation bigint,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  CONSTRAINT yvs_prod_composant_op_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_composant_op_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_composant_op_composant_fkey FOREIGN KEY (composant)
      REFERENCES yvs_prod_composant_nomenclature (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_composant_op_operation_fkey FOREIGN KEY (operation)
      REFERENCES yvs_prod_operations_gamme (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_composant_op
  OWNER TO postgres;

  -- Table: yvs_prod_flux_composant

-- DROP TABLE yvs_prod_flux_composant;

CREATE TABLE yvs_prod_flux_composant
(
  id bigint NOT NULL,
  quantite double precision,
  sens character(1),
  operation bigint,
  composant bigint,
  CONSTRAINT yvs_prod_flux_composant_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_flux_composant_composant_fkey FOREIGN KEY (composant)
      REFERENCES yvs_prod_composant_ordre_fabrication (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_flux_composant_operation_fkey FOREIGN KEY (operation)
      REFERENCES yvs_prod_operations_of (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_flux_composant
  OWNER TO postgres;
COMMENT ON TABLE yvs_prod_flux_composant
  IS 'Liaison entre les tables opérationOf et ComposantOf pour la matérialisation des mouvements de matière lors d''une opération de l''ordre de fabrication';

 
ALTER TABLE yvs_prod_nomenclature ADD COLUMN unite_mesure bigint;
ALTER TABLE yvs_prod_nomenclature
  ADD CONSTRAINT yvs_prod_nomenclature_unite_mesure_fkey FOREIGN KEY (unite_mesure)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
