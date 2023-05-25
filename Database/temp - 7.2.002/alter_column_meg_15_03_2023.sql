ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN producteur bigint;
ALTER TABLE yvs_prod_ordre_fabrication
  ADD CONSTRAINT yvs_prod_ordre_fabrication_client_fkey FOREIGN KEY (producteur)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;