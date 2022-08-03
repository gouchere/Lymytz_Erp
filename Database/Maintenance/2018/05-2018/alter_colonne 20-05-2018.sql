
ALTER TABLE yvs_mut_interet ADD COLUMN taux double precision;
ALTER TABLE yvs_mut_interet ALTER COLUMN taux SET DEFAULT 0;
ALTER TABLE yvs_mut_interet ADD COLUMN periode bigint;
ALTER TABLE yvs_mut_interet
  ADD CONSTRAINT yvs_mut_interet_periode_fkey FOREIGN KEY (periode)
      REFERENCES yvs_mut_periode_exercice (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_mut_reglement_prime ADD COLUMN periode bigint;
ALTER TABLE yvs_mut_reglement_prime
  ADD CONSTRAINT yvs_mut_reglement_prime_periode_fkey FOREIGN KEY (periode)
      REFERENCES yvs_mut_periode_exercice (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_mut_reglement_prime ADD COLUMN exercice bigint;
ALTER TABLE yvs_mut_reglement_prime
  ADD CONSTRAINT yvs_mut_reglement_prime_exercice_fkey FOREIGN KEY (exercice)
      REFERENCES yvs_base_exercice (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  

UPDATE yvs_mut_type_compte SET type_compte = UPPER(type_compte);
------

