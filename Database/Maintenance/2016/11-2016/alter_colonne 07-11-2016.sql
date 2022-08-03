
ALTER TABLE yvs_com_doc_stocks ADD COLUMN description character varying(255);
ALTER TABLE yvs_com_doc_achats ADD COLUMN description character varying(255);
ALTER TABLE yvs_com_doc_ventes ADD COLUMN description character varying(255);

ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN commentaire character varying(255);
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN num_serie character varying(255);
ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN num_serie character varying(255);
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN num_serie character varying(255);