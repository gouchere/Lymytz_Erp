ALTER TABLE yvs_base_point_vente ADD COLUMN client bigint;
ALTER TABLE yvs_base_point_vente
  ADD CONSTRAINT yvs_base_point_vente_client_fkey FOREIGN KEY (client)
      REFERENCES yvs_com_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;