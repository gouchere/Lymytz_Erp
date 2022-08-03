
ALTER TABLE yvs_com_entete_doc_vente ADD COLUMN cloturer boolean;
ALTER TABLE yvs_com_entete_doc_vente ALTER COLUMN cloturer SET DEFAULT false;
ALTER TABLE yvs_com_entete_doc_vente ADD COLUMN date_cloturer date;

ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN commentaire character varying;

ALTER TABLE yvs_com_doc_ventes ADD COLUMN tranche_livrer bigint;
ALTER TABLE yvs_com_doc_ventes
  ADD CONSTRAINT yvs_com_doc_ventes_tranche_livrer_fkey FOREIGN KEY (tranche_livrer)
      REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	 

ALTER TABLE yvs_base_doc_divers ADD COLUMN cloturer boolean;
ALTER TABLE yvs_base_doc_divers ALTER COLUMN cloturer SET DEFAULT false;
ALTER TABLE yvs_base_doc_divers ADD COLUMN date_cloturer date;

ALTER TABLE yvs_base_doc_divers ADD COLUMN date_valider date;
ALTER TABLE yvs_base_doc_divers ADD COLUMN valider_by bigint;
ALTER TABLE yvs_base_doc_divers
  ADD CONSTRAINT yvs_base_doc_divers_valider_by_fkey FOREIGN KEY (valider_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE yvs_base_doc_divers ADD COLUMN date_annuler date;
ALTER TABLE yvs_base_doc_divers ADD COLUMN annuler_by bigint;
ALTER TABLE yvs_base_doc_divers
  ADD CONSTRAINT yvs_base_doc_divers_annuler_by_fkey FOREIGN KEY (annuler_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
	  
	  
ALTER TABLE yvs_base_piece_tresorerie ADD COLUMN cloturer boolean;
ALTER TABLE yvs_base_piece_tresorerie ALTER COLUMN cloturer SET DEFAULT false;
ALTER TABLE yvs_base_piece_tresorerie ADD COLUMN date_cloturer date;

ALTER TABLE yvs_base_piece_tresorerie ADD COLUMN date_valider date;
ALTER TABLE yvs_base_piece_tresorerie ADD COLUMN valider_by bigint;
ALTER TABLE yvs_base_piece_tresorerie
  ADD CONSTRAINT yvs_base_piece_tresorerie_valider_by_fkey FOREIGN KEY (valider_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE yvs_base_piece_tresorerie ADD COLUMN date_annuler date;
ALTER TABLE yvs_base_piece_tresorerie ADD COLUMN annuler_by bigint;
ALTER TABLE yvs_base_piece_tresorerie
  ADD CONSTRAINT yvs_base_piece_tresorerie_annuler_by_fkey FOREIGN KEY (annuler_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
