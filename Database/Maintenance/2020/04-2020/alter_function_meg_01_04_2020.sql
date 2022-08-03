ALTER TABLE yvs_base_mouvement_stock DISABLE TRIGGER base_warning_mouvement_stock;
ALTER TABLE yvs_base_mouvement_stock DISABLE TRIGGER stock_maj_prix_mvt;

UPDATE yvs_base_mouvement_stock m SET type_doc = y.type_doc, num_doc = y.num_doc FROM yvs_com_doc_ventes y INNER JOIN yvs_com_contenu_doc_vente c ON c.doc_vente = y.id
	WHERE table_externe = 'yvs_com_contenu_doc_vente' AND m.id_externe = c.id;
UPDATE yvs_base_mouvement_stock m SET type_doc = y.type_doc, num_doc = y.num_doc FROM yvs_com_doc_achats y INNER JOIN yvs_com_contenu_doc_achat c ON c.doc_achat = y.id
	WHERE table_externe = 'yvs_com_contenu_doc_achat' AND m.id_externe = c.id;
UPDATE yvs_base_mouvement_stock m SET type_doc = y.type_doc, num_doc = y.num_doc FROM yvs_com_doc_stocks y INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = y.id
	WHERE table_externe = 'yvs_com_contenu_doc_stock' AND m.id_externe = c.id;
UPDATE yvs_base_mouvement_stock m SET type_doc = y.type_doc, num_doc = y.num_doc FROM yvs_com_doc_stocks y INNER JOIN yvs_com_contenu_doc_stock d ON d.doc_stock = y.id 
	INNER JOIN yvs_com_contenu_doc_stock_reception c ON c.contenu = d.id
	WHERE table_externe = 'yvs_com_contenu_doc_stock_reception' AND m.id_externe = c.id;
UPDATE yvs_base_mouvement_stock m SET type_doc = d.type_doc FROM yvs_com_doc_stocks y INNER JOIN  yvs_com_doc_stocks d ON y.document_lie = d.id
	INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = y.id
	WHERE table_externe = 'yvs_com_contenu_doc_stock' AND m.id_externe = c.id AND y.document_lie IS NOT NULL;
UPDATE yvs_base_mouvement_stock m SET type_doc = 'RA', num_doc = y.num_doc FROM yvs_com_doc_ration y INNER JOIN yvs_com_ration c ON c.doc_ration = y.id
	WHERE table_externe = 'yvs_com_ration' AND m.id_externe = c.id;
UPDATE yvs_base_mouvement_stock m SET type_doc = 'DE', num_doc = y.code_ref FROM yvs_prod_ordre_fabrication y INNER JOIN yvs_prod_declaration_production c ON c.ordre = y.id
	WHERE table_externe = 'yvs_prod_declaration_production' AND m.id_externe = c.id;
UPDATE yvs_base_mouvement_stock m SET type_doc = 'CO', num_doc = y.code_ref FROM yvs_prod_ordre_fabrication y INNER JOIN yvs_prod_composant_of o ON o.ordre_fabrication = y.id
 INNER JOIN yvs_prod_flux_composant f ON f.composant = o.id INNER JOIN yvs_prod_of_suivi_flux c ON c.composant = f.id
	WHERE table_externe = 'yvs_prod_of_suivi_flux' AND m.id_externe = c.id;
	
ALTER TABLE yvs_base_mouvement_stock ENABLE TRIGGER base_warning_mouvement_stock;
ALTER TABLE yvs_base_mouvement_stock ENABLE TRIGGER stock_maj_prix_mvt;

