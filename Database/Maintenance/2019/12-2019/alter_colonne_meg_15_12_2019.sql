ALTER TABLE yvs_synchro_serveurs ADD COLUMN online boolean;
ALTER TABLE yvs_synchro_serveurs ALTER COLUMN online SET DEFAULT false;

ALTER TABLE yvs_grh_type_element_additionel ADD COLUMN compte bigint;
ALTER TABLE yvs_grh_type_element_additionel
  ADD CONSTRAINT yvs_grh_type_element_additionel_compte_fkey FOREIGN KEY (compte)
      REFERENCES yvs_base_plan_comptable (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;