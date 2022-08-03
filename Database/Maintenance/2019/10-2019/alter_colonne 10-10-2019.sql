ALTER TABLE yvs_synchro_listen_table ADD COLUMN author bigint;
ALTER TABLE yvs_synchro_listen_table ADD COLUMN serveur bigint;
ALTER TABLE yvs_synchro_listen_table
  ADD CONSTRAINT yvs_synchro_listen_table_serveur_fkey FOREIGN KEY (serveur)
      REFERENCES yvs_synchro_serveurs (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;