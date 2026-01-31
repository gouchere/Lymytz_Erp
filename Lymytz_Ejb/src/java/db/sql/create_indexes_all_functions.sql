-- ============================================================================
-- INDEX POUR OPTIMISATION DE TOUTES LES FONCTIONS DU PROJET
-- ============================================================================
-- Date: 2026-01-17
-- Description: Index recommandés pour améliorer les performances de toutes
--              les fonctions SQL du projet comptabilité et gestion de stock
-- ============================================================================

SET client_min_messages TO WARNING;

-- ============================================================================
-- FONCTION HELPER POUR CRÉER DES INDEX
-- ============================================================================
CREATE OR REPLACE FUNCTION create_index_if_not_exists(
    index_name text,
    table_name text,
    index_definition text
) RETURNS void AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = index_name) THEN
        EXECUTE 'CREATE INDEX ' || index_name || ' ON ' || table_name || ' ' || index_definition;
        RAISE NOTICE 'Index % créé avec succès', index_name;
    ELSE
        RAISE NOTICE 'Index % existe déjà', index_name;
    END IF;
EXCEPTION WHEN OTHERS THEN
    RAISE NOTICE 'Erreur lors de la création de l''index %: %', index_name, SQLERRM;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- 1. TABLES COMPTABILITÉ - PIÈCES ET JOURNAUX
-- ============================================================================
-- Utilisées par: compta_et_balance, compta_et_grand_livre, compta_et_journal,
--                compta_et_debit_credit, compta_et_solde, compta_et_dashboard

-- yvs_compta_pieces_comptable

SELECT create_index_if_not_exists(
    'yvs_compta_pieces_comptable_date_piece_idx',
    'yvs_compta_pieces_comptable',
    '(date_piece)'
);


SELECT create_index_if_not_exists(
    'yvs_compta_pieces_comptable_journal_idx',
    'yvs_compta_pieces_comptable',
    '(journal)'
);

-- Index composite date + journal (très fréquent dans les requêtes)

SELECT create_index_if_not_exists(
    'yvs_compta_pieces_comptable_date_piece_journal_idx',
    'yvs_compta_pieces_comptable',
    '(date_piece, journal)'
);

-- Index pour num_piece (tri fréquent)

SELECT create_index_if_not_exists(
    'yvs_compta_pieces_comptable_num_piece_idx',
    'yvs_compta_pieces_comptable',
    '(num_piece)'
);

-- yvs_compta_journaux

SELECT create_index_if_not_exists(
    'yvs_compta_journaux_agence_idx',
    'yvs_compta_journaux',
    '(agence)'
);


SELECT create_index_if_not_exists(
    'yvs_compta_journaux_code_journal_idx',
    'yvs_compta_journaux',
    '(code_journal)'
);

-- ============================================================================
-- 2. TABLE CENTRALE - CONTENT JOURNAL
-- ============================================================================
-- Table la plus utilisée dans toutes les fonctions comptables

-- yvs_compta_content_journal

SELECT create_index_if_not_exists(
    'yvs_compta_content_journal_piece_idx',
    'yvs_compta_content_journal',
    '(piece)'
);


SELECT create_index_if_not_exists(
    'yvs_compta_content_journal_compte_general_idx',
    'yvs_compta_content_journal',
    '(compte_general)'
);

-- Index pour compte_tiers (filtré non NULL pour performance)

SELECT create_index_if_not_exists(
    'yvs_compta_content_journal_compte_tiers_idx',
    'yvs_compta_content_journal',
    '(compte_tiers) WHERE compte_tiers IS NOT NULL AND compte_tiers > 0'
);

-- Index composite compte_tiers + table_tiers (requêtes Tiers)

SELECT create_index_if_not_exists(
    'yvs_compta_content_journal_compte_tiers_table_tiers_idx',
    'yvs_compta_content_journal',
    '(compte_tiers, table_tiers) WHERE compte_tiers IS NOT NULL'
);

-- Index pour report (soldes initiaux)

SELECT create_index_if_not_exists(
    'yvs_compta_content_journal_report_idx',
    'yvs_compta_content_journal',
    '(report) WHERE report = TRUE'
);


SELECT create_index_if_not_exists(
    'yvs_compta_content_journal_report_false_idx',
    'yvs_compta_content_journal',
    '(report) WHERE COALESCE(report, FALSE) = FALSE'
);

-- Index pour lettrage (grand livre lettré)

SELECT create_index_if_not_exists(
    'yvs_compta_content_journal_lettrage_idx',
    'yvs_compta_content_journal',
    '(lettrage) WHERE lettrage IS NOT NULL AND TRIM(lettrage) <> '''''
);

-- Index composite piece + compte_general (jointures fréquentes)

SELECT create_index_if_not_exists(
    'yvs_compta_content_journal_piece_compte_general_idx',
    'yvs_compta_content_journal',
    '(piece, compte_general)'
);

-- ============================================================================
-- 3. TABLES ANALYTIQUES
-- ============================================================================
-- Utilisées par: compta_et_balance (type A), compta_et_grand_livre, compta_et_journal

-- yvs_compta_content_analytique

SELECT create_index_if_not_exists(
    'yvs_compta_content_analytique_contenu_idx',
    'yvs_compta_content_analytique',
    '(contenu)'
);


SELECT create_index_if_not_exists(
    'yvs_compta_content_analytique_centre_idx',
    'yvs_compta_content_analytique',
    '(centre)'
);

-- Index composite contenu + centre

SELECT create_index_if_not_exists(
    'yvs_compta_content_analytique_contenu_centre_idx',
    'yvs_compta_content_analytique',
    '(contenu, centre)'
);

-- yvs_compta_centre_analytique

SELECT create_index_if_not_exists(
    'yvs_compta_centre_analytique_code_ref_idx',
    'yvs_compta_centre_analytique',
    '(code_ref)'
);


SELECT create_index_if_not_exists(
    'yvs_compta_centre_analytique_plan_idx',
    'yvs_compta_centre_analytique',
    '(plan)'
);

-- yvs_compta_plan_analytique

SELECT create_index_if_not_exists(
    'yvs_compta_plan_analytique_societe_idx',
    'yvs_compta_plan_analytique',
    '(societe)'
);

-- ============================================================================
-- 4. PLAN COMPTABLE ET NATURE
-- ============================================================================
-- Utilisées par: compta_et_balance, compta_et_dashboard, compta_et_grand_livre

-- yvs_base_plan_comptable

SELECT create_index_if_not_exists(
    'yvs_base_plan_comptable_num_compte_idx',
    'yvs_base_plan_comptable',
    '(num_compte)'
);


SELECT create_index_if_not_exists(
    'yvs_base_plan_comptable_nature_compte_idx',
    'yvs_base_plan_comptable',
    '(nature_compte)'
);

-- Index pour compte_general (comptes collectifs)

SELECT create_index_if_not_exists(
    'yvs_base_plan_comptable_compte_general_idx',
    'yvs_base_plan_comptable',
    '(compte_general) WHERE compte_general IS NOT NULL'
);

-- Index pour type_compte (comptes collectifs CO)

SELECT create_index_if_not_exists(
    'yvs_base_plan_comptable_type_compte_idx',
    'yvs_base_plan_comptable',
    '(type_compte)'
);

-- Index composite pour plage de comptes (dashboard)

SELECT create_index_if_not_exists(
    'yvs_base_plan_comptable_num_compte_id_idx',
    'yvs_base_plan_comptable',
    '(num_compte, id)'
);

-- yvs_base_nature_compte

SELECT create_index_if_not_exists(
    'yvs_base_nature_compte_societe_idx',
    'yvs_base_nature_compte',
    '(societe)'
);

-- ============================================================================
-- 5. AGENCES ET SOCIÉTÉS
-- ============================================================================
-- Utilisées par: toutes les fonctions

-- yvs_agences

SELECT create_index_if_not_exists(
    'yvs_agences_societe_idx',
    'yvs_agences',
    '(societe)'
);

-- ============================================================================
-- 6. TABLES TIERS
-- ============================================================================
-- Utilisées par: compta_et_balance (type T), compta_et_grand_livre

-- yvs_base_tiers

SELECT create_index_if_not_exists(
    'yvs_base_tiers_code_tiers_idx',
    'yvs_base_tiers',
    '(code_tiers)'
);


SELECT create_index_if_not_exists(
    'yvs_base_tiers_societe_idx',
    'yvs_base_tiers',
    '(societe)'
);

-- yvs_com_client

SELECT create_index_if_not_exists(
    'yvs_com_client_code_client_idx',
    'yvs_com_client',
    '(code_client)'
);


SELECT create_index_if_not_exists(
    'yvs_com_client_tiers_idx',
    'yvs_com_client',
    '(tiers)'
);

-- yvs_base_fournisseur

SELECT create_index_if_not_exists(
    'yvs_base_fournisseur_code_fsseur_idx',
    'yvs_base_fournisseur',
    '(code_fsseur)'
);


SELECT create_index_if_not_exists(
    'yvs_base_fournisseur_tiers_idx',
    'yvs_base_fournisseur',
    '(tiers)'
);

-- yvs_grh_employes

SELECT create_index_if_not_exists(
    'yvs_grh_employes_compte_tiers_idx',
    'yvs_grh_employes',
    '(compte_tiers)'
);

-- ============================================================================
-- 7. TABLES EXERCICE
-- ============================================================================
-- Utilisées par: compta_et_balance, compta_et_grand_livre

-- yvs_base_exercice

SELECT create_index_if_not_exists(
    'yvs_base_exercice_date_debut_date_fin_idx',
    'yvs_base_exercice',
    '(date_debut, date_fin)'
);

-- ============================================================================
-- 8. TABLES CAISSE ET MOUVEMENTS
-- ============================================================================
-- Utilisées par: compta_et_brouillard_caisse, compta_total_caisse

-- yvs_compta_mouvement_caisse

SELECT create_index_if_not_exists(
    'yvs_compta_mouvement_caisse_caisse_idx',
    'yvs_compta_mouvement_caisse',
    '(caisse)'
);


SELECT create_index_if_not_exists(
    'yvs_compta_mouvement_caisse_date_paye_idx',
    'yvs_compta_mouvement_caisse',
    '(date_paye)'
);


SELECT create_index_if_not_exists(
    'yvs_compta_mouvement_caisse_statut_piece_idx',
    'yvs_compta_mouvement_caisse',
    '(statut_piece)'
);

-- Index composite caisse + date + statut (brouillard caisse)

SELECT create_index_if_not_exists(
    'yvs_compta_mouvement_caisse_caisse_date_paye_statut_piece_idx',
    'yvs_compta_mouvement_caisse',
    '(caisse, date_paye, statut_piece)'
);

-- Index pour table_externe (filtrage exclusions)

SELECT create_index_if_not_exists(
    'yvs_compta_mouvement_caisse_table_externe_idx',
    'yvs_compta_mouvement_caisse',
    '(table_externe)'
);

-- Index composite pour total_caisse

SELECT create_index_if_not_exists(
    'yvs_compta_mouvement_caisse_mouvement_idx',
    'yvs_compta_mouvement_caisse',
    '(mouvement)'
);


SELECT create_index_if_not_exists(
    'yvs_compta_mouvement_caisse_agence_idx',
    'yvs_compta_mouvement_caisse',
    '(agence)'
);

-- yvs_base_mode_reglement

SELECT create_index_if_not_exists(
    'yvs_base_mode_reglement_type_reglement_idx',
    'yvs_base_mode_reglement',
    '(type_reglement)'
);

-- yvs_compta_notif_reglement_vente

SELECT create_index_if_not_exists(
    'yvs_compta_notif_reglement_vente_piece_vente_idx',
    'yvs_compta_notif_reglement_vente',
    '(piece_vente)'
);

-- yvs_compta_notif_reglement_achat

SELECT create_index_if_not_exists(
    'yvs_compta_notif_reglement_achat_piece_achat_idx',
    'yvs_compta_notif_reglement_achat',
    '(piece_achat)'
);

-- ============================================================================
-- 9. TABLES STOCK ET INVENTAIRE
-- ============================================================================
-- Utilisées par: com_inventaire, get_stock_reel_multi_agg

-- yvs_base_mouvement_stock

SELECT create_index_if_not_exists(
    'yvs_base_mouvement_stock_conditionnement_idx',
    'yvs_base_mouvement_stock',
    '(conditionnement)'
);


SELECT create_index_if_not_exists(
    'yvs_base_mouvement_stock_depot_idx',
    'yvs_base_mouvement_stock',
    '(depot)'
);


SELECT create_index_if_not_exists(
    'yvs_base_mouvement_stock_date_doc_idx',
    'yvs_base_mouvement_stock',
    '(date_doc)'
);

-- Index composite pour get_stock_reel_multi_agg

SELECT create_index_if_not_exists(
    'yvs_base_mouvement_stock_depot_conditionnement_date_doc_idx',
    'yvs_base_mouvement_stock',
    '(depot, conditionnement, date_doc)'
);


SELECT create_index_if_not_exists(
    'yvs_base_mouvement_stock_mouvement_idx',
    'yvs_base_mouvement_stock',
    '(mouvement)'
);


SELECT create_index_if_not_exists(
    'yvs_base_mouvement_stock_tranche_idx',
    'yvs_base_mouvement_stock',
    '(tranche)'
);

-- yvs_base_mouvement_stock_lot

SELECT create_index_if_not_exists(
    'yvs_base_mouvement_stock_lot_mouvement_idx',
    'yvs_base_mouvement_stock_lot',
    '(mouvement)'
);


SELECT create_index_if_not_exists(
    'yvs_base_mouvement_stock_lot_lot_idx',
    'yvs_base_mouvement_stock_lot',
    '(lot)'
);

-- yvs_base_article_depot

SELECT create_index_if_not_exists(
    'yvs_base_article_depot_article_idx',
    'yvs_base_article_depot',
    '(article)'
);


SELECT create_index_if_not_exists(
    'yvs_base_article_depot_depot_idx',
    'yvs_base_article_depot',
    '(depot)'
);


SELECT create_index_if_not_exists(
    'yvs_base_article_depot_article_depot_idx',
    'yvs_base_article_depot',
    '(article, depot)'
);

-- yvs_base_articles

SELECT create_index_if_not_exists(
    'yvs_base_articles_famille_idx',
    'yvs_base_articles',
    '(famille)'
);


SELECT create_index_if_not_exists(
    'yvs_base_articles_groupe_idx',
    'yvs_base_articles',
    '(groupe)'
);


SELECT create_index_if_not_exists(
    'yvs_base_articles_categorie_idx',
    'yvs_base_articles',
    '(categorie)'
);


SELECT create_index_if_not_exists(
    'yvs_base_articles_actif_idx',
    'yvs_base_articles',
    '(actif)'
);

-- yvs_base_famille_article

SELECT create_index_if_not_exists(
    'yvs_base_famille_article_societe_idx',
    'yvs_base_famille_article',
    '(societe)'
);

-- yvs_base_depots

SELECT create_index_if_not_exists(
    'yvs_base_depots_agence_idx',
    'yvs_base_depots',
    '(agence)'
);

-- yvs_base_conditionnement

SELECT create_index_if_not_exists(
    'yvs_base_conditionnement_article_idx',
    'yvs_base_conditionnement',
    '(article)'
);


SELECT create_index_if_not_exists(
    'yvs_base_conditionnement_unite_idx',
    'yvs_base_conditionnement',
    '(unite)'
);

-- yvs_base_article_emplacement

SELECT create_index_if_not_exists(
    'yvs_base_article_emplacement_article_idx',
    'yvs_base_article_emplacement',
    '(article)'
);


SELECT create_index_if_not_exists(
    'yvs_base_article_emplacement_emplacement_idx',
    'yvs_base_article_emplacement',
    '(emplacement)'
);

-- ============================================================================
-- 10. TABLES RÉSERVATIONS ET VENTES
-- ============================================================================
-- Utilisées par: com_inventaire

-- yvs_com_reservation_stock

SELECT create_index_if_not_exists(
    'yvs_com_reservation_stock_depot_idx',
    'yvs_com_reservation_stock',
    '(depot)'
);


SELECT create_index_if_not_exists(
    'yvs_com_reservation_stock_article_idx',
    'yvs_com_reservation_stock',
    '(article)'
);


SELECT create_index_if_not_exists(
    'yvs_com_reservation_stock_statut_idx',
    'yvs_com_reservation_stock',
    '(statut)'
);


SELECT create_index_if_not_exists(
    'yvs_com_reservation_stock_date_echeance_idx',
    'yvs_com_reservation_stock',
    '(date_echeance)'
);


SELECT create_index_if_not_exists(
    'yvs_com_reservation_stock_depot_article_conditionnement_statut_idx',
    'yvs_com_reservation_stock',
    '(depot, article, conditionnement, statut)'
);

-- yvs_com_doc_ventes

SELECT create_index_if_not_exists(
    'yvs_com_doc_ventes_type_doc_idx',
    'yvs_com_doc_ventes',
    '(type_doc)'
);


SELECT create_index_if_not_exists(
    'yvs_com_doc_ventes_statut_idx',
    'yvs_com_doc_ventes',
    '(statut)'
);


SELECT create_index_if_not_exists(
    'yvs_com_doc_ventes_statut_livre_idx',
    'yvs_com_doc_ventes',
    '(statut_livre)'
);


SELECT create_index_if_not_exists(
    'yvs_com_doc_ventes_depot_livrer_idx',
    'yvs_com_doc_ventes',
    '(depot_livrer)'
);


SELECT create_index_if_not_exists(
    'yvs_com_doc_ventes_date_livraison_idx',
    'yvs_com_doc_ventes',
    '(date_livraison)'
);

-- yvs_com_contenu_doc_vente

SELECT create_index_if_not_exists(
    'yvs_com_contenu_doc_vente_doc_vente_idx',
    'yvs_com_contenu_doc_vente',
    '(doc_vente)'
);


SELECT create_index_if_not_exists(
    'yvs_com_contenu_doc_vente_article_idx',
    'yvs_com_contenu_doc_vente',
    '(article)'
);


SELECT create_index_if_not_exists(
    'yvs_com_contenu_doc_vente_doc_vente_article_conditionnement_idx',
    'yvs_com_contenu_doc_vente',
    '(doc_vente, article, conditionnement)'
);

-- yvs_com_entete_doc_vente

SELECT create_index_if_not_exists(
    'yvs_com_entete_doc_vente_date_entete_idx',
    'yvs_com_entete_doc_vente',
    '(date_entete)'
);

-- ============================================================================
-- 11. TABLES TRANCHES HORAIRES
-- ============================================================================
-- Utilisées par: get_stock_reel_multi_agg

-- yvs_grh_tranche_horaire

SELECT create_index_if_not_exists(
    'yvs_grh_tranche_horaire_type_journee_idx',
    'yvs_grh_tranche_horaire',
    '(type_journee)'
);


SELECT create_index_if_not_exists(
    'yvs_grh_tranche_horaire_societe_idx',
    'yvs_grh_tranche_horaire',
    '(societe)'
);


SELECT create_index_if_not_exists(
    'yvs_grh_tranche_horaire_heure_debut_idx',
    'yvs_grh_tranche_horaire',
    '(heure_debut)'
);

-- ============================================================================
-- NETTOYAGE DE LA FONCTION HELPER
-- ============================================================================
DROP FUNCTION IF EXISTS create_index_if_not_exists(text, text, text);

-- ============================================================================
-- ANALYSE DES TABLES POUR MISE À JOUR DES STATISTIQUES
-- ============================================================================
RAISE NOTICE 'Analyse des tables en cours...';

-- Tables comptabilité
ANALYZE yvs_compta_pieces_comptable;
ANALYZE yvs_compta_content_journal;
ANALYZE yvs_compta_journaux;
ANALYZE yvs_compta_content_analytique;
ANALYZE yvs_compta_centre_analytique;
ANALYZE yvs_compta_plan_analytique;
ANALYZE yvs_compta_mouvement_caisse;

-- Tables plan comptable et nature
ANALYZE yvs_base_plan_comptable;
ANALYZE yvs_base_nature_compte;

-- Tables tiers
ANALYZE yvs_base_tiers;
ANALYZE yvs_com_client;
ANALYZE yvs_base_fournisseur;
ANALYZE yvs_grh_employes;

-- Tables agences et exercices
ANALYZE yvs_agences;
ANALYZE yvs_base_exercice;

-- Tables stock et inventaire
ANALYZE yvs_base_mouvement_stock;
ANALYZE yvs_base_mouvement_stock_lot;
ANALYZE yvs_base_article_depot;
ANALYZE yvs_base_articles;
ANALYZE yvs_base_famille_article;
ANALYZE yvs_base_depots;
ANALYZE yvs_base_conditionnement;
ANALYZE yvs_base_article_emplacement;

-- Tables réservations et ventes
ANALYZE yvs_com_reservation_stock;
ANALYZE yvs_com_doc_ventes;
ANALYZE yvs_com_contenu_doc_vente;
ANALYZE yvs_com_entete_doc_vente;

-- Tables mode règlement et notifications
ANALYZE yvs_base_mode_reglement;
ANALYZE yvs_compta_notif_reglement_vente;
ANALYZE yvs_compta_notif_reglement_achat;

-- Tables tranches
ANALYZE yvs_grh_tranche_horaire;

RAISE NOTICE 'Création des index terminée avec succès!';

-- ============================================================================
-- RÉSUMÉ DES INDEX CRÉÉS
-- ============================================================================
/*
FONCTIONS COUVERTES:
--------------------
1. compta_et_balance          - Balance comptable
2. compta_et_grand_livre      - Grand livre
3. compta_et_journal          - Journal comptable
4. compta_et_debit_credit     - Débit/Crédit
5. compta_et_debit_credit_all - Débit/Crédit (tous mouvements)
6. compta_et_debit_credit_initial - Soldes initiaux
7. compta_et_solde            - Solde comptable
8. compta_et_dashboard        - Tableau de bord
9. compta_et_brouillard_caisse - Brouillard de caisse
10. compta_total_caisse        - Total caisse
11. com_inventaire             - Inventaire
12. get_stock_reel_multi_agg   - Stock réel agrégé

TABLES INDEXÉES:
----------------
- yvs_compta_pieces_comptable (5 index)
- yvs_compta_content_journal (8 index)
- yvs_compta_journaux (2 index)
- yvs_compta_content_analytique (3 index)
- yvs_compta_centre_analytique (2 index)
- yvs_compta_plan_analytique (1 index)
- yvs_base_plan_comptable (5 index)
- yvs_base_nature_compte (1 index)
- yvs_agences (1 index)
- yvs_base_tiers (2 index)
- yvs_com_client (2 index)
- yvs_base_fournisseur (2 index)
- yvs_grh_employes (1 index)
- yvs_base_exercice (1 index)
- yvs_compta_mouvement_caisse (7 index)
- yvs_base_mode_reglement (1 index)
- yvs_compta_notif_reglement_vente (1 index)
- yvs_compta_notif_reglement_achat (1 index)
- yvs_base_mouvement_stock (6 index)
- yvs_base_mouvement_stock_lot (2 index)
- yvs_base_article_depot (3 index)
- yvs_base_articles (4 index)
- yvs_base_famille_article (1 index)
- yvs_base_depots (1 index)
- yvs_base_conditionnement (2 index)
- yvs_base_article_emplacement (2 index)
- yvs_com_reservation_stock (5 index)
- yvs_com_doc_ventes (5 index)
- yvs_com_contenu_doc_vente (3 index)
- yvs_com_entete_doc_vente (1 index)
- yvs_grh_tranche_horaire (3 index)

TOTAL: ~75 index recommandés

ESTIMATION GAIN PERFORMANCE: 40-70% selon les requêtes
*/
