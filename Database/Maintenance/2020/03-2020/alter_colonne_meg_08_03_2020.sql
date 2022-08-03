ALTER TABLE yvs_base_point_vente ADD COLUMN parent bigint;
ALTER TABLE yvs_base_point_vente
  ADD CONSTRAINT yvs_base_point_vente_parent_fkey FOREIGN KEY (parent)
      REFERENCES yvs_base_point_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;