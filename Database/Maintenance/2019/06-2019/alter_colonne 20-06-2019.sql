ALTER TABLE yvs_com_contenu_doc_stock DISABLE TRIGGER update_;
UPDATE yvs_com_contenu_doc_stock SET conditionnement_entree = conditionnement WHERE id IN (SELECT c.id FROM yvs_com_contenu_doc_stock c INNER JOIN yvs_com_doc_stocks d ON c.doc_stock = d.id WHERE d.type_doc = 'FT' AND c.conditionnement_entree IS NULL);
UPDATE yvs_com_contenu_doc_stock SET conditionnement_entree = conditionnement WHERE id IN (SELECT c.id FROM yvs_com_contenu_doc_stock c INNER JOIN yvs_com_doc_stocks d ON c.doc_stock = d.id WHERE d.type_doc = 'ES' AND c.conditionnement_entree IS NULL);
ALTER TABLE yvs_com_contenu_doc_stock ENABLE TRIGGER update_;

ALTER TABLE yvs_base_mouvement_stock DISABLE TRIGGER stock_maj_prix_mvt;
UPDATE yvs_base_mouvement_stock SET conditionnement = (SELECT c.conditionnement_entree FROM yvs_com_contenu_doc_stock c INNER JOIN yvs_com_contenu_doc_stock_reception r ON c.id = r.contenu WHERE r.id = id_externe)
WHERE conditionnement IS NULL AND table_externe = 'yvs_com_contenu_doc_stock_reception';
UPDATE yvs_base_mouvement_stock SET conditionnement = (SELECT c.conditionnement_entree FROM yvs_com_contenu_doc_stock c WHERE c.id = id_externe) WHERE conditionnement IS NULL AND table_externe = 'yvs_com_contenu_doc_stock';
ALTER TABLE yvs_base_mouvement_stock ENABLE TRIGGER stock_maj_prix_mvt;


SELECT insert_droit('base_article_change_reference', 'Modifier la reference de l''article', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'), 16, 'A','R');