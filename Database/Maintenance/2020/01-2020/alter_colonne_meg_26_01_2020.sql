
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN date_justofy date;
ALTER TABLE yvs_compta_bon_provisoire ALTER COLUMN date_justofy SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN justify_by bigint;
ALTER TABLE yvs_compta_bon_provisoire
  ADD CONSTRAINT yvs_compta_bon_provisoire_justify_by_fkey FOREIGN KEY (justify_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_classes_stat ADD COLUMN parent bigint;
ALTER TABLE yvs_base_classes_stat
  ADD CONSTRAINT yvs_base_classes_stat_parent_fkey FOREIGN KEY (parent)
      REFERENCES yvs_base_classes_stat (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;