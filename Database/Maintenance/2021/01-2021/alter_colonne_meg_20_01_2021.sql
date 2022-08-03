ALTER TABLE yvs_compta_parametre ADD COLUMN journal_report bigint;
ALTER TABLE yvs_compta_parametre
  ADD CONSTRAINT yvs_compta_parametre_journal_report_fkey FOREIGN KEY (journal_report)
      REFERENCES yvs_compta_journaux (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_compta_parametre ADD COLUMN compte_benefice_report bigint;
ALTER TABLE yvs_compta_parametre
  ADD CONSTRAINT yvs_compta_parametre_compte_benefice_report_fkey FOREIGN KEY (compte_benefice_report)
      REFERENCES yvs_base_plan_comptable (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_compta_parametre ADD COLUMN compte_perte_report bigint;
ALTER TABLE yvs_compta_parametre
  ADD CONSTRAINT yvs_compta_parametre_compte_perte_report_fkey FOREIGN KEY (compte_perte_report)
      REFERENCES yvs_base_plan_comptable (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;