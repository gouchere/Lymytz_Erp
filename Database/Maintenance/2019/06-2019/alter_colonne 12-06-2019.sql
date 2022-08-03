SELECT insert_droit('fv_generer_entree', 'Generer une fiche d''entree correspondant a la facture de vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fv'), 16, 'A','R');	
	
SELECT insert_droit('gescom_inv_update', 'Modifier une fiche inventaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,D,E','R');	
	
ALTER TABLE yvs_base_article_depot ADD COLUMN sell_without_stock boolean;
ALTER TABLE yvs_base_article_depot ALTER COLUMN sell_without_stock SET DEFAULT true;

ALTER TABLE yvs_com_doc_ventes DROP CONSTRAINT yvs_com_doc_ventes_document_lie_fkey;
ALTER TABLE yvs_com_doc_ventes
  ADD CONSTRAINT yvs_com_doc_ventes_document_lie_fkey FOREIGN KEY (document_lie)
      REFERENCES yvs_com_doc_ventes (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;