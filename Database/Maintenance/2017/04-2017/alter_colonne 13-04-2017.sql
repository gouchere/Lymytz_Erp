
DELETE FROM yvs_mut_mutualiste;
ALTER TABLE yvs_mut_mutualiste DROP CONSTRAINT yvs_mut_mutualiste_employe_fkey;
ALTER TABLE yvs_mut_mutualiste
  ADD CONSTRAINT yvs_mut_mutualiste_employe_fkey FOREIGN KEY (employe)
      REFERENCES yvs_grh_employes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
DROP TABLE yvs_mut_employe;