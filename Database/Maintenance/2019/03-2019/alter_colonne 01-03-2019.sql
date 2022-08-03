ALTER TABLE yvs_com_ristourne ADD COLUMN famille bigint;
ALTER TABLE yvs_com_ristourne
  ADD CONSTRAINT yvs_com_ristourne_famille_fkey FOREIGN KEY (famille)
      REFERENCES yvs_base_famille_article (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  

ALTER TABLE yvs_base_plan_tarifaire ADD COLUMN famille bigint;
ALTER TABLE yvs_base_plan_tarifaire
  ADD CONSTRAINT yvs_base_plan_tarifaire_famille_fkey FOREIGN KEY (famille)
      REFERENCES yvs_base_famille_article (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
SELECT action_in_header_vente_or_piece_virement(n.entete_doc) FROM yvs_compta_notif_versement_vente n;