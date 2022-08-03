UPDATE yvs_prod_site_production SET agence = null;
ALTER TABLE yvs_prod_site_production DROP CONSTRAINT yvs_prod_site_production_agence_fkey;
ALTER TABLE yvs_prod_site_production RENAME agence TO societe;

ALTER TABLE yvs_prod_site_production
  ADD CONSTRAINT yvs_prod_site_production_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_calendrier ADD COLUMN module character varying;
ALTER TABLE yvs_calendrier ALTER COLUMN module SET DEFAULT 'GRH'::character varying;

ALTER TABLE yvs_base_centre_valorisation ADD COLUMN societe bigint;
ALTER TABLE yvs_base_centre_valorisation
  ADD CONSTRAINT yvs_base_centre_valorisation_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_type_etat ADD COLUMN societe bigint;
ALTER TABLE yvs_base_type_etat
  ADD CONSTRAINT yvs_base_type_etat_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;