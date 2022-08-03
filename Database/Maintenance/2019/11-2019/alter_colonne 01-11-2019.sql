ALTER TABLE yvs_base_point_vente ADD COLUMN secteur bigint;
ALTER TABLE yvs_base_point_vente
  ADD CONSTRAINT yvs_base_point_vente_secteur_fkey FOREIGN KEY (secteur)
      REFERENCES yvs_dictionnaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;