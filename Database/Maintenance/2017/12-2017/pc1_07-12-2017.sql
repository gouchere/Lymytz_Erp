ALTER TABLE yvs_com_reconditionnement DROP CONSTRAINT yvs_com_reconditionnement_fiche_fkey;
ALTER TABLE yvs_com_reconditionnement
  ADD CONSTRAINT yvs_com_reconditionnement_fiche_fkey FOREIGN KEY (fiche)
      REFERENCES yvs_com_transformation (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
