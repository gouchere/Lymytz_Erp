DROP FUNCTION get_remise(bigint, double precision, double precision, bigint, date);

ALTER TABLE yvs_base_article_point ADD COLUMN remise double precision;
ALTER TABLE yvs_base_article_point ALTER COLUMN remise SET DEFAULT 0;
ALTER TABLE yvs_base_article_point ADD COLUMN nature_remise character varying;
ALTER TABLE yvs_base_article_point ALTER COLUMN nature_remise SET DEFAULT 'MONTANT'::character varying;

ALTER TABLE yvs_base_point_vente ADD COLUMN responsable bigint;
ALTER TABLE yvs_base_point_vente
  ADD CONSTRAINT yvs_base_point_vente_responsable_fkey FOREIGN KEY (responsable)
      REFERENCES yvs_grh_employes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
ALTER TABLE yvs_com_doc_ventes ADD COLUMN cloturer_by bigint;
ALTER TABLE yvs_com_doc_ventes
  ADD CONSTRAINT yvs_com_doc_ventes_cloturer_by_fkey FOREIGN KEY (cloturer_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_com_doc_achats ADD COLUMN cloturer_by bigint;
ALTER TABLE yvs_com_doc_achats
  ADD CONSTRAINT yvs_com_doc_achats_cloturer_by_fkey FOREIGN KEY (cloturer_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_com_doc_stocks ADD COLUMN cloturer_by bigint;
ALTER TABLE yvs_com_doc_stocks
  ADD CONSTRAINT yvs_com_doc_stocks_cloturer_by_fkey FOREIGN KEY (cloturer_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

      
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN statut character varying;
ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN statut character varying;
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN statut character varying;