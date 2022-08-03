-- Index: yvs_base_article_depot_article_idx
-- DROP INDEX yvs_base_article_depot_article_idx;
CREATE INDEX yvs_base_article_depot_article_idx
  ON yvs_base_article_depot
  USING btree
  (article);


-- Index: yvs_base_article_depot_depot_idx
-- DROP INDEX yvs_base_article_depot_depot_idx;
CREATE INDEX yvs_base_article_depot_depot_idx
  ON yvs_base_article_depot
  USING btree
  (depot);


-- Index: yvs_base_article_depot_depot_pr_idx
-- DROP INDEX yvs_base_article_depot_depot_pr_idx;
CREATE INDEX yvs_base_article_depot_depot_pr_idx
  ON yvs_base_article_depot
  USING btree
  (depot_pr);


-- Index: yvs_com_proformat_vente_client_idx
-- DROP INDEX yvs_com_proformat_vente_client_idx;
CREATE INDEX yvs_com_proformat_vente_client_idx
  ON yvs_com_proformat_vente
  USING btree
  (client);


-- Index: yvs_com_proformat_vente_agence_idx
-- DROP INDEX yvs_com_proformat_vente_agence_idx;
CREATE INDEX yvs_com_proformat_vente_agence_idx
  ON yvs_com_proformat_vente
  USING btree
  (agence);


-- Index: yvs_com_proformat_vente_contenu_conditionnement_idx
-- DROP INDEX yvs_com_proformat_vente_contenu_conditionnement_idx;
CREATE INDEX yvs_com_proformat_vente_contenu_conditionnement_idx
  ON yvs_com_proformat_vente_contenu
  USING btree
  (conditionnement);


-- Index: yvs_com_proformat_vente_contenu_proformat_idx
-- DROP INDEX yvs_com_proformat_vente_contenu_proformat_idx;
CREATE INDEX yvs_com_proformat_vente_contenu_proformat_idx
  ON yvs_com_proformat_vente_contenu
  USING btree
  (proformat);
  
	
SELECT insert_droit('base_view_article_puv', 'Voir le prix de vente' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');
	
SELECT insert_droit('base_view_article_pua', 'Voir le prix d''achat' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');
	
SELECT insert_droit('base_view_article_pr', 'Voir le prix de revient' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');
	
SELECT correction_users_multi_agence();

UPDATE yvs_users_agence SET can_action = TRUE WHERE actif IS TRUE AND can_action IS FALSE;

INSERT INTO yvs_base_element_reference(designation, module, model_courant, default_prefix)
    VALUES ('Proforma Vente', 'COM', true, 'PFV');		
	
SELECT insert_droit('gescom_pfv', 'Page des proforma vente', 
	(SELECT id FROM yvs_module WHERE reference = 'com_'), 16, 'A,B,C','P');	
