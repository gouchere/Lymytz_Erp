UPDATE yvs_workflow_model_doc SET name_table = 'yvs_grh_conge_emps' WHERE name_table = 'yvs_grh_conges';
UPDATE yvs_workflow_model_doc SET name_table = 'yvs_grh_formation_emps' WHERE name_table = 'yvs_grh_formations';
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, description, workflow)
    VALUES ('HIGH_PR_ARTICLE', 'yvs_base_mouvement_stock', 16, 'Mouvement de stock avec un PR anormal', FALSE);


UPDATE yvs_stat_dashboard SET code = 'JOURNAL_PRODUCTION', libelle = 'Journal de production' WHERE code = 'JOURNAL_PRODUCTION_PF';
UPDATE yvs_stat_dashboard SET code = 'JOURNAL_PRODUCTION_EQUIPE', libelle = 'Journal de production par equipe' WHERE code = 'JOURNAL_PRODUCTION_PFS';
UPDATE yvs_stat_dashboard SET code = 'JOURNAL_PRODUCTION_TRANCHE', libelle = 'Journal de production par tranche' WHERE code = 'JOURNAL_PRODUCTION_PF_EQUIPE';
UPDATE yvs_stat_dashboard SET code = 'RECAPITULATIF_OF', libelle = 'Recapitulatif OF' WHERE code = 'JOURNAL_PRODUCTION_PSF_EQUIPE';
UPDATE yvs_stat_dashboard SET code = 'CONSOMMATION_PRODUCTION', libelle = 'consommation / Production' WHERE code = 'JOURNAL_PRODUCTION_PF_TRANCHE';
UPDATE yvs_stat_dashboard SET code = 'PRODUCTION_CONSOMMATION_EQUIPE', libelle = 'Production et Consommation par équipe' WHERE code = 'JOURNAL_PRODUCTION_PSF_TRANCHE';

-- Trigger: compta_warning_caisse_divers on yvs_compta_caisse_piece_divers
-- DROP TRIGGER compta_warning_caisse_divers ON yvs_compta_caisse_piece_divers;
CREATE TRIGGER compta_warning_caisse_divers
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_compta_caisse_piece_divers
  FOR EACH ROW
  EXECUTE PROCEDURE compta_warning_caisse_divers();
  
-- Trigger: base_warning_mouvement_stock on yvs_base_mouvement_stock
DROP TRIGGER base_warning_mouvement_stock ON yvs_base_mouvement_stock;
CREATE TRIGGER base_warning_mouvement_stock
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_base_mouvement_stock
  FOR EACH ROW
  EXECUTE PROCEDURE base_warning_mouvement_stock();
  
DROP FUNCTION com_et_total_articles(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying, character varying, character varying, bigint, bigint);
DROP FUNCTION et_total_article_client(bigint, bigint, date, date, character varying);
DROP FUNCTION et_total_article_commercial(bigint, bigint, date, date, character varying);
DROP FUNCTION et_total_article_pt_vente(bigint, bigint, date, date, character varying);
DROP FUNCTION et_total_article_pt_vente(bigint, bigint, character varying, date, date, character varying);
DROP FUNCTION et_total_article_pt_vente(bigint, bigint, bigint, character varying, date, date, character varying);
DROP FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying);
DROP FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying, character varying);
DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying, character varying);
DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying);
DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying);

DROP FUNCTION et_total_one_pt_vente(bigint, bigint, date, date, character varying);
DROP FUNCTION et_total_pt_vente(bigint, date, date, character varying);
DROP FUNCTION et_total_pt_vente(bigint, bigint, date, date, character varying);
DROP FUNCTION et_total_vendeurs(bigint, date, date, character varying);
DROP FUNCTION et_total_vendeurs(bigint, bigint, date, date, character varying);