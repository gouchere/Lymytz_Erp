CREATE INDEX yvs_users_agence_agence_idx
  ON yvs_users_agence
  USING btree
  (agence);

CREATE INDEX yvs_users_agence_users_idx
  ON yvs_users_agence
  USING btree
  (users);
  
CREATE INDEX yvs_base_plan_comptable_nature_compte_idx
  ON yvs_base_plan_comptable
  USING btree
  (nature_compte);
  
  
CREATE INDEX yvs_base_tiers_agence_idx
  ON yvs_base_tiers
  USING btree
  (agence);

CREATE INDEX yvs_base_caisse_caissier_idx
  ON yvs_base_caisse
  USING btree
  (caissier);
  
CREATE INDEX yvs_base_caisse_code_acces_idx
  ON yvs_base_caisse
  USING btree
  (code_acces);

CREATE INDEX yvs_base_caisse_compte_idx
  ON yvs_base_caisse
  USING btree
  (compte);

CREATE INDEX yvs_base_caisse_journal_idx
  ON yvs_base_caisse
  USING btree
  (journal);

CREATE INDEX yvs_base_caisse_mode_reg_defaut_idx
  ON yvs_base_caisse
  USING btree
  (mode_reg_defaut);

CREATE INDEX yvs_compta_mouvement_caisse_agence_idx
  ON yvs_compta_mouvement_caisse
  USING btree
  (agence);

CREATE INDEX yvs_compta_mouvement_caisse_caisse_idx
  ON yvs_compta_mouvement_caisse
  USING btree
  (caisse);

CREATE INDEX yvs_compta_mouvement_caisse_id_externe_table_externe_idx
  ON yvs_compta_mouvement_caisse
  USING btree
  (id_externe, table_externe COLLATE pg_catalog."default");

CREATE INDEX yvs_compta_mouvement_caisse_societe_idx
  ON yvs_compta_mouvement_caisse
  USING btree
  (societe);


CREATE INDEX yvs_compta_caisse_doc_divers_agence_idx
  ON yvs_compta_caisse_doc_divers
  USING btree
  (agence);

CREATE INDEX yvs_compta_caisse_doc_divers_caisse_idx
  ON yvs_compta_caisse_doc_divers
  USING btree
  (caisse);

CREATE INDEX yvs_compta_caisse_doc_divers_compte_general_idx
  ON yvs_compta_caisse_doc_divers
  USING btree
  (compte_general);

CREATE INDEX yvs_compta_caisse_doc_divers_date_doc_idx
  ON yvs_compta_caisse_doc_divers
  USING btree
  (date_doc);

CREATE INDEX yvs_compta_caisse_doc_divers_id_tiers_table_tiers_idx
  ON yvs_compta_caisse_doc_divers
  USING btree
  (id_tiers, table_tiers COLLATE pg_catalog."default");

CREATE INDEX yvs_compta_caisse_doc_divers_num_piece_reference_externe_idx
  ON yvs_compta_caisse_doc_divers
  USING btree
  (num_piece COLLATE pg_catalog."default", reference_externe COLLATE pg_catalog."default");

CREATE INDEX yvs_compta_caisse_doc_divers_societe_idx
  ON yvs_compta_caisse_doc_divers
  USING btree
  (societe);

CREATE INDEX yvs_compta_caisse_doc_divers_statut_doc_idx
  ON yvs_compta_caisse_doc_divers
  USING btree
  (statut_doc COLLATE pg_catalog."default");

CREATE INDEX yvs_compta_caisse_doc_divers_type_doc_idx
  ON yvs_compta_caisse_doc_divers
  USING btree
  (type_doc);

CREATE INDEX yvs_compta_caisse_doc_divers_montant_idx
  ON yvs_compta_caisse_doc_divers
  USING btree
  (montant);

CREATE INDEX yvs_compta_mouvement_caisse_numero_numero_externe_reference_idx
  ON yvs_compta_mouvement_caisse
  USING btree
  (numero COLLATE pg_catalog."default", numero_externe COLLATE pg_catalog."default", reference_externe COLLATE pg_catalog."default");

CREATE INDEX yvs_compta_mouvement_caisse_date_mvt_idx
  ON yvs_compta_mouvement_caisse
  USING btree
  (date_mvt);

CREATE INDEX yvs_compta_mouvement_caisse_date_paiment_prevu_idx
  ON yvs_compta_mouvement_caisse
  USING btree
  (date_paiment_prevu);

CREATE INDEX yvs_compta_mouvement_caisse_date_paye_idx
  ON yvs_compta_mouvement_caisse
  USING btree
  (date_paye);

CREATE INDEX yvs_compta_caisse_piece_vente_vente_statut_piece_idx
  ON yvs_compta_caisse_piece_vente
  USING btree
  (vente, statut_piece COLLATE pg_catalog."default");

CREATE INDEX yvs_compta_caisse_piece_vente_caisse_idx
  ON yvs_compta_caisse_piece_vente
  USING btree
  (caisse);

CREATE INDEX yvs_compta_caisse_piece_achat_achat_idx
  ON yvs_compta_caisse_piece_achat
  USING btree
  (achat);

CREATE INDEX yvs_compta_caisse_piece_achat_caisse_idx
  ON yvs_compta_caisse_piece_achat
  USING btree
  (caisse);

CREATE INDEX yvs_compta_caisse_piece_achat_date_paiement_idx
  ON yvs_compta_caisse_piece_achat
  USING btree
  (date_paiement);

CREATE INDEX yvs_compta_caisse_piece_achat_statut_piece_idx
  ON yvs_compta_caisse_piece_achat
  USING btree
  (statut_piece COLLATE pg_catalog."default");

CREATE INDEX yvs_compta_caisse_piece_divers_date_piece_idx
  ON yvs_compta_caisse_piece_divers
  USING btree
  (date_piece);

CREATE INDEX yvs_compta_caisse_piece_divers_doc_divers_idx
  ON yvs_compta_caisse_piece_divers
  USING btree
  (doc_divers);

CREATE INDEX yvs_com_contenu_doc_achat_article_idx
  ON yvs_com_contenu_doc_achat
  USING btree
  (article);


CREATE INDEX yvs_com_contenu_doc_stock_conditionnement_idx
  ON yvs_com_contenu_doc_stock
  USING btree
  (conditionnement);

CREATE INDEX yvs_com_contenu_doc_stock_doc_stock_idx
  ON yvs_com_contenu_doc_stock
  USING btree
  (doc_stock);

CREATE INDEX yvs_com_contenu_doc_vente_article_vente_idx
  ON yvs_com_contenu_doc_vente
  USING btree
  (article, doc_vente);

CREATE INDEX yvs_com_contenu_doc_vente_conditionnement_idx
  ON yvs_com_contenu_doc_vente
  USING btree
  (conditionnement);

CREATE INDEX yvs_com_contenu_doc_vente_idx
  ON yvs_com_contenu_doc_vente
  USING btree
  (doc_vente);
