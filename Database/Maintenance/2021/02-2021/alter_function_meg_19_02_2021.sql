-- Index: yvs_compta_caisse_piece_vente_vente_statut_piece_idx
-- DROP INDEX yvs_compta_caisse_piece_vente_vente_statut_piece_idx;
CREATE INDEX yvs_compta_caisse_piece_vente_vente_statut_piece_idx
  ON yvs_compta_caisse_piece_vente
  USING btree
  (vente, statut_piece COLLATE pg_catalog."default");
  
-- Index: yvs_compta_caisse_piece_vente_vente_idx
-- DROP INDEX yvs_compta_caisse_piece_vente_vente_idx;
CREATE INDEX yvs_compta_caisse_piece_vente_vente_idx
  ON yvs_compta_caisse_piece_vente
  USING btree
  (vente);


-- Trigger: base_warning_article on yvs_base_articles
DROP TRIGGER base_warning_article ON yvs_base_articles;
CREATE TRIGGER base_warning_article
  BEFORE INSERT OR DELETE
  ON yvs_base_articles
  FOR EACH ROW
  EXECUTE PROCEDURE base_warning_article();		
		
SELECT insert_droit('gescom_inv_update', 'Modifier une fiche inventaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,D,E','R');	
	
SELECT insert_droit('gescom_inv_editer', 'Annuler une fiche inventaire', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, 'A,B,C,D,E,F','R');	
  
  