ALTER TABLE yvs_com_plan_remise DROP COLUMN IF EXISTS remise;
ALTER TABLE yvs_com_plan_remise ADD COLUMN remise bigint;
ALTER TABLE yvs_com_plan_remise
  ADD CONSTRAINT yvs_com_plan_remise_remise_fkey FOREIGN KEY (remise)
      REFERENCES yvs_com_remise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;