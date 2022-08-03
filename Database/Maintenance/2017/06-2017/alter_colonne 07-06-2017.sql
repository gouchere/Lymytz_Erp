ALTER TABLE yvs_stat_export_colonne ADD COLUMN ordre integer;
ALTER TABLE yvs_stat_export_colonne ALTER COLUMN ordre SET DEFAULT 0;

ALTER TABLE yvs_stat_export_etat ADD COLUMN author bigint;	
ALTER TABLE yvs_stat_export_etat
  ADD CONSTRAINT yvs_stat_export_etat_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_stat_export_colonne ADD COLUMN author bigint;	
ALTER TABLE yvs_stat_export_colonne
  ADD CONSTRAINT yvs_stat_export_colonne_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;