DROP INDEX yvs_compta_content_journal_num_piece_num_ref_ref_externe_ta_idx;
DROP INDEX yvs_base_articles_ref_art_designation_categorie_type_servic_idx;
DROP INDEX yvs_base_mouvement_stock_date_doc_article_conditionnement_d_idx;
DROP INDEX doc_vente_idx;

CREATE INDEX yvs_compta_content_journal_compte_general_idx
  ON yvs_compta_content_journal
  USING btree
  (compte_general);

CREATE INDEX yvs_compta_content_journal_compte_tiers_table_tiers_idx
  ON yvs_compta_content_journal
  USING btree
  (compte_tiers, table_tiers COLLATE pg_catalog."default");
  
CREATE INDEX yvs_compta_content_journal_num_piece_idx
  ON yvs_compta_content_journal
  USING btree
  (num_piece COLLATE pg_catalog."default");
  
CREATE INDEX yvs_compta_content_journal_num_ref_idx
  ON yvs_compta_content_journal
  USING btree
  (num_ref COLLATE pg_catalog."default");
  
CREATE INDEX yvs_compta_content_journal_piece_idx
  ON yvs_compta_content_journal
  USING btree
  (piece);
  
CREATE INDEX yvs_compta_content_journal_report_idx
  ON yvs_compta_content_journal
  USING btree
  (report);  
  
CREATE INDEX yvs_compta_pieces_comptable_journal_idx
  ON yvs_compta_pieces_comptable
  USING btree
  (journal);
  
CREATE INDEX yvs_compta_journaux_agence_idx
  ON yvs_compta_journaux
  USING btree
  (agence);
  
CREATE INDEX yvs_agences_societe_idx
  ON yvs_agences
  USING btree
  (societe);
  
CREATE INDEX yvs_compta_pieces_comptable_num_piece_idx
  ON yvs_compta_pieces_comptable
  USING btree
  (num_piece COLLATE pg_catalog."default");
  
CREATE INDEX yvs_compta_content_journal_table_externe_idx
  ON yvs_compta_content_journal
  USING btree
  (table_externe COLLATE pg_catalog."default");  

CREATE INDEX yvs_base_depots_agence_idx
  ON yvs_base_depots
  USING btree
  (agence);

CREATE INDEX yvs_base_famille_article_societe_idx
  ON yvs_base_famille_article
  USING btree
  (societe);

CREATE INDEX yvs_base_articles_famille_idx
  ON yvs_base_articles
  USING btree
  (famille);

CREATE INDEX yvs_base_mouvement_stock_article_idx
  ON yvs_base_mouvement_stock
  USING btree
  (article);

CREATE INDEX yvs_base_mouvement_stock_conditionnement_idx
  ON yvs_base_mouvement_stock
  USING btree
  (conditionnement);

CREATE INDEX yvs_base_mouvement_stock_date_doc_idx
  ON yvs_base_mouvement_stock
  USING btree
  (date_doc);

CREATE INDEX yvs_base_mouvement_stock_mouvement_idx
  ON yvs_base_mouvement_stock
  USING btree
  (mouvement COLLATE pg_catalog."default");

CREATE INDEX yvs_base_mouvement_stock_conditionnement_depot_mouvement_idx
  ON yvs_base_mouvement_stock
  USING btree
  (conditionnement, depot, mouvement COLLATE pg_catalog."default");

CREATE INDEX yvs_base_mouvement_stock_conditionnement_depot_mouvement_tranche_idx
  ON yvs_base_mouvement_stock
  USING btree
  (conditionnement, depot, mouvement COLLATE pg_catalog."default", tranche);

CREATE INDEX yvs_base_mouvement_stock_conditionnement_depot_mouvement_date_doc_idx
  ON yvs_base_mouvement_stock
  USING btree
  (conditionnement, depot, mouvement COLLATE pg_catalog."default", date_doc);

CREATE INDEX yvs_base_mouvement_stock_conditionnement_depot_tranche_mouvement_date_doc_idx
  ON yvs_base_mouvement_stock
  USING btree
  (conditionnement, depot, mouvement COLLATE pg_catalog."default", date_doc, tranche);

CREATE INDEX yvs_com_doc_ventes_type_doc_statut_depot_livrer_idx
  ON yvs_com_doc_ventes
  USING btree
  (type_doc COLLATE pg_catalog."default", statut COLLATE pg_catalog."default", depot_livrer);

CREATE INDEX yvs_com_doc_ventes_type_doc_statut_depot_livrer_date_livraison_idx
  ON yvs_com_doc_ventes
  USING btree
  (type_doc COLLATE pg_catalog."default", statut COLLATE pg_catalog."default", depot_livrer, date_livraison);

CREATE INDEX yvs_com_contenu_doc_vente_conditionnement_idx
  ON yvs_com_contenu_doc_vente
  USING btree
  (conditionnement);

CREATE INDEX yvs_com_entete_doc_vente_agence_idx
  ON yvs_com_entete_doc_vente
  USING btree
  (agence);

CREATE INDEX yvs_com_entete_doc_vente_creneau_idx
  ON yvs_com_entete_doc_vente
  USING btree
  (creneau);

CREATE INDEX yvs_com_creneau_horaire_users_creneau_depot_idx
  ON yvs_com_creneau_horaire_users
  USING btree
  (creneau_depot);

CREATE INDEX yvs_com_creneau_horaire_users_creneau_point_idx
  ON yvs_com_creneau_horaire_users
  USING btree
  (creneau_point);

CREATE INDEX yvs_com_creneau_point_point_idx
  ON yvs_com_creneau_point
  USING btree
  (point);

CREATE INDEX yvs_com_creneau_depot_depot_idx
  ON yvs_com_creneau_depot
  USING btree
  (depot);

CREATE INDEX yvs_base_point_vente_agence_idx
  ON yvs_base_point_vente
  USING btree
  (agence);
DROP INDEX yvs_grh_ordre_calcul_salaire_reference_idx1;

CREATE INDEX yvs_grh_detail_bulletin_bulletin_idx
  ON yvs_grh_detail_bulletin
  USING btree
  (bulletin);

CREATE INDEX yvs_grh_detail_bulletin_element_salaire_idx
  ON yvs_grh_detail_bulletin
  USING btree
  (element_salaire);

CREATE INDEX yvs_grh_bulletins_contrat_idx
  ON yvs_grh_bulletins
  USING btree
  (contrat);

CREATE INDEX yvs_grh_bulletins_entete_idx
  ON yvs_grh_bulletins
  USING btree
  (entete);

CREATE INDEX yvs_grh_contrat_emps_employe_idx
  ON yvs_grh_contrat_emps
  USING btree
  (employe);

CREATE INDEX yvs_grh_ordre_calcul_salaire_societe_idx
  ON yvs_grh_ordre_calcul_salaire
  USING btree
  (societe);

CREATE INDEX yvs_grh_detail_bulletin_now_visible_idx
  ON yvs_grh_detail_bulletin
  USING btree
  (now_visible);

CREATE INDEX yvs_grh_detail_bulletin_bulletin_now_visible_idx
  ON yvs_grh_detail_bulletin
  USING btree
  (bulletin, now_visible);

CREATE INDEX yvs_grh_element_additionel_contrat_idx
  ON yvs_grh_element_additionel
  USING btree
  (contrat);

CREATE INDEX yvs_grh_element_salaire_visible_bulletin_idx
  ON yvs_grh_element_salaire
  USING btree
  (visible_bulletin);

CREATE INDEX yvs_grh_employes_agence_idx
  ON yvs_grh_employes
  USING btree
  (agence);

CREATE INDEX yvs_grh_employes_poste_actif_idx
  ON yvs_grh_employes
  USING btree
  (poste_actif);

CREATE INDEX yvs_grh_detail_bulletin_montant_payer_montant_employeur_ret_idx
  ON yvs_grh_detail_bulletin
  USING btree
  (montant_payer, montant_employeur, retenu_salariale);