ALTER TABLE yvs_prod_operations_of DROP COLUMN phase;

ALTER TABLE yvs_prod_operations_of ADD COLUMN machine integer;
ALTER TABLE yvs_prod_operations_of ADD COLUMN main_oeuvre integer;
ALTER TABLE yvs_prod_operations_of ADD COLUMN date_debut date;
ALTER TABLE yvs_prod_operations_of ADD COLUMN date_fin date;
ALTER TABLE yvs_prod_operations_of ADD COLUMN heure_debut time without time zone;
ALTER TABLE yvs_prod_operations_of ADD COLUMN heure_fin time without time zone;
ALTER TABLE yvs_prod_operations_of ADD COLUMN numero integer;
ALTER TABLE yvs_prod_operations_of ADD COLUMN code_ref character varying;

ALTER TABLE yvs_prod_operations_of
  ADD CONSTRAINT yvs_prod_operations_of_machine_fkey FOREIGN KEY (machine)
      REFERENCES yvs_prod_poste_charge (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_prod_operations_of
  ADD CONSTRAINT yvs_prod_operations_of_main_oeuvre_fkey FOREIGN KEY (main_oeuvre)
      REFERENCES yvs_prod_poste_charge (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE yvs_prod_operations_of RENAME COLUMN duree_arret TO temps_reglage;
ALTER TABLE yvs_prod_operations_of RENAME COLUMN duree_execution TO temps_operation;

INSERT INTO yvs_base_element_reference(
            designation, module, author, date_update, date_save)
    VALUES ( 'Ordre Fabrication', 'PROD', 16, current_timestamp,current_timestamp);

	
ALTER TABLE yvs_prod_ordre_fabrication RENAME COLUMN reference TO code_ref
ALTER TABLE yvs_prod_ordre_fabrication DROP COLUMN header_of;
ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN societe bigint;

ALTER TABLE yvs_prod_ordre_fabrication
  ADD CONSTRAINT yvs_prod_ordre_fabrication_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_prod_composant_op ADD COLUMN quantite double precision;

DROP TABLE yvs_prod_flux_composant;

CREATE TABLE yvs_prod_flux_composant
(
  id bigserial NOT NULL,
  quantite double precision,
  sens character(1),
  operation bigint,
  composant bigint,
  CONSTRAINT yvs_prod_flux_composant_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_flux_composant_composant_fkey FOREIGN KEY (composant)
      REFERENCES yvs_prod_composant_of (id) MATCH SIMPLE
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

ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN depot_mp bigint;
ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN depot_pf bigint;


ALTER TABLE yvs_prod_ordre_fabrication
  ADD CONSTRAINT yvs_prod_ordre_fabrication_depot_mp_fkey FOREIGN KEY (depot_mp)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

	  ALTER TABLE yvs_prod_ordre_fabrication
  ADD CONSTRAINT yvs_prod_ordre_fabrication_depot_pf_fkey FOREIGN KEY (depot_pf)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE yvs_prod_composant_of ADD COLUMN depot_conso bigint;

ALTER TABLE yvs_prod_composant_of
  ADD CONSTRAINT yvs_prod_composant_of_depot_conso_fkey FOREIGN KEY (depot_conso)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
	ALTER TABLE yvs_prod_composant_of ADD COLUMN unite bigint;

	ALTER TABLE yvs_prod_composant_of
	  ADD CONSTRAINT yvs_prod_composant_of_unite_fkey FOREIGN KEY (unite)
		  REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
		  ON UPDATE NO ACTION ON DELETE NO ACTION;
		  
	ALTER TABLE yvs_prod_composant_of ADD COLUMN lot_sortie bigint;

	ALTER TABLE yvs_prod_composant_of
	  ADD CONSTRAINT yvs_prod_composant_of_lot_sortie_fkey FOREIGN KEY (lot_sortie)
		  REFERENCES yvs_com_lot_reception (id) MATCH SIMPLE
		  ON UPDATE NO ACTION ON DELETE NO ACTION;