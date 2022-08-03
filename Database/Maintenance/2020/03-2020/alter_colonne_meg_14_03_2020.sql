ALTER TABLE yvs_base_parametre ADD COLUMN monitor_pr BOOLEAN DEFAULT FALSE;
ALTER TABLE yvs_base_parametre ADD COLUMN taux_ecart_pr DOUBLE PRECISION DEFAULT 0;
ALTER TABLE yvs_com_parametre_stock ADD COLUMN calcul_pr BOOLEAN DEFAULT true;

DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying);
DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying);
DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint);
DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying);
DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying);

DROP INDEX yvs_com_doc_ventes_nom_client;
DROP INDEX yvs_com_doc_ventes_num_doc;
DROP INDEX yvs_com_doc_ventes_statut;
DROP INDEX yvs_com_doc_ventes_statut_livre;
DROP INDEX yvs_com_doc_ventes_statut_regle;
DROP INDEX yvs_com_doc_ventes_type_doc;


-- Index: yvs_base_mouvement_stock_date_doc_article_conditionnement_d_idx
-- DROP INDEX yvs_base_mouvement_stock_date_doc_article_conditionnement_d_idx;
CREATE INDEX yvs_base_mouvement_stock_date_doc_article_conditionnement_d_idx
  ON yvs_base_mouvement_stock
  USING btree
  (date_doc, article, conditionnement, depot, mouvement COLLATE pg_catalog."default");
  
-- Index: yvs_com_reservation_stock_depot_article_conditionnement_sta_idx
-- DROP INDEX yvs_com_reservation_stock_depot_article_conditionnement_sta_idx;
CREATE INDEX yvs_com_reservation_stock_depot_article_conditionnement_sta_idx
  ON yvs_com_reservation_stock
  USING btree
  (date_echeance, article, conditionnement, depot, statut COLLATE pg_catalog."default");
  
  
-- Index: yvs_com_doc_ventes_type_doc_num_doc_statut_statut_livre_sta_idx
-- DROP INDEX yvs_com_doc_ventes_type_doc_num_doc_statut_statut_livre_sta_idx;
CREATE INDEX yvs_com_doc_ventes_type_doc_num_doc_statut_statut_livre_sta_idx
  ON yvs_com_doc_ventes
  USING btree
  (type_doc COLLATE pg_catalog."default", num_doc COLLATE pg_catalog."default", statut COLLATE pg_catalog."default", statut_livre COLLATE pg_catalog."default", statut_regle COLLATE pg_catalog."default");