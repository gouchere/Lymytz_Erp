-- ============================================================================
-- DÉPLOIEMENT RAPIDE - OPTIMISATIONS SQL
-- ============================================================================
-- Ce script permet un déploiement rapide de toutes les optimisations
-- EN UNE SEULE FOIS (pour environnement de test uniquement)
-- ============================================================================

-- AVERTISSEMENT: Ne pas exécuter en production sans tests préalables!
-- Toujours tester d'abord sur un environnement de développement/test

\echo '============================================================================'
\echo 'DÉPLOIEMENT DES OPTIMISATIONS SQL'
\echo '============================================================================'
\echo ''

-- Désactiver les notices temporairement
SET client_min_messages TO WARNING;

\echo 'ÉTAPE 1/3: Création des index...'
\echo ''

-- ============================================================================
-- FONCTION HELPER POUR CRÉER DES INDEX (compatible toutes versions PostgreSQL)
-- ============================================================================

CREATE OR REPLACE FUNCTION create_index_if_not_exists(
    index_name text,
    table_name text,
    index_definition text
) RETURNS void AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes WHERE indexname = index_name
    ) THEN
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
-- INDEX CRITIQUES (les plus importants)
-- ============================================================================

-- INDEX pour yvs_base_mouvement_stock (TABLE CRITIQUE)
DO $$
BEGIN
    IF current_setting('server_version_num')::integer >= 110000 THEN
        PERFORM create_index_if_not_exists(
            'idx_mouvement_stock_complete',
            'yvs_base_mouvement_stock',
            '(article, depot, date_doc DESC, mouvement, calcul_pr) INCLUDE (cout_stock, conditionnement)'
        );
    ELSE
        PERFORM create_index_if_not_exists(
            'idx_mouvement_stock_complete',
            'yvs_base_mouvement_stock',
            '(article, depot, date_doc DESC, mouvement, calcul_pr, cout_stock, conditionnement)'
        );
    END IF;
END $$;

SELECT create_index_if_not_exists(
    'idx_mouvement_stock_article_date',
    'yvs_base_mouvement_stock',
    '(article, date_doc DESC, mouvement) WHERE COALESCE(calcul_pr, TRUE) IS TRUE'
);

SELECT create_index_if_not_exists(
    'idx_mouvement_stock_depot_article',
    'yvs_base_mouvement_stock',
    '(depot, article, date_doc DESC) WHERE COALESCE(calcul_pr, TRUE) IS TRUE AND mouvement = ''E'''
);

-- INDEX pour yvs_base_article_depot
SELECT create_index_if_not_exists(
    'idx_article_depot_article_depot',
    'yvs_base_article_depot',
    '(article, depot)'
);

SELECT create_index_if_not_exists(
    'idx_article_depot_article_default',
    'yvs_base_article_depot',
    '(article) WHERE default_pr IS TRUE'
);

-- INDEX pour yvs_prod_composant_nomenclature
DO $$
BEGIN
    IF current_setting('server_version_num')::integer >= 110000 THEN
        PERFORM create_index_if_not_exists(
            'idx_composant_nomenclature_complete',
            'yvs_prod_composant_nomenclature',
            '(nomenclature, article, actif, inside_cout, alternatif) INCLUDE (unite, quantite, composant_lie)'
        );
    ELSE
        PERFORM create_index_if_not_exists(
            'idx_composant_nomenclature_complete',
            'yvs_prod_composant_nomenclature',
            '(nomenclature, article, actif, inside_cout, alternatif, unite, quantite, composant_lie)'
        );
    END IF;
END $$;

SELECT create_index_if_not_exists(
    'idx_composant_nomenclature_nomenclature',
    'yvs_prod_composant_nomenclature',
    '(nomenclature) WHERE actif IS TRUE AND inside_cout IS TRUE AND alternatif IS FALSE'
);

-- INDEX pour yvs_prod_nomenclature
SELECT create_index_if_not_exists(
    'idx_nomenclature_article_unite',
    'yvs_prod_nomenclature',
    '(article, unite_mesure) WHERE actif IS TRUE'
);

-- INDEX pour conversions
SELECT create_index_if_not_exists(
    'idx_table_conversion_unites',
    'yvs_base_table_conversion',
    '(unite, unite_equivalent)'
);

-- INDEX pour dépôts
SELECT create_index_if_not_exists(
    'idx_depots_id_agence',
    'yvs_base_depots',
    '(id, agence)'
);

-- Analyser les tables
ANALYZE yvs_base_mouvement_stock;
ANALYZE yvs_base_article_depot;
ANALYZE yvs_prod_composant_nomenclature;
ANALYZE yvs_prod_nomenclature;

\echo 'Index créés avec succès!'
\echo ''
\echo 'ÉTAPE 2/3: Création des fonctions optimisées...'
\echo ''

-- ============================================================================
-- FONCTION get_pr_reel_optimized
-- ============================================================================

CREATE OR REPLACE FUNCTION get_pr_reel_optimized(
    agence_ bigint,
    article_ bigint,
    depot_ bigint,
    tranche_ bigint,
    date_ date,
    unite_ bigint,
    current_ bigint,
    enable_logging boolean DEFAULT FALSE
)
RETURNS double precision
LANGUAGE plpgsql
AS $$
DECLARE
    _depot_ bigint;
    _agence bigint DEFAULT NULL;
    pr_ double precision;
    coef_ double precision;
    ecart_ double precision;
    prix_nom_ double precision;
    categorie_ character varying;
    valorise_from_of_ boolean;
    cout_stock_ double precision;
    conditionnement_ bigint;
    taux_ecart_pr_ double precision;
    categorie_art_ character varying;
BEGIN
    -- Récupération optimisée en une requête
    SELECT ad.depot_pr, ad.categorie, d.agence
    INTO _depot_, categorie_, _agence
    FROM yvs_base_article_depot ad
    LEFT JOIN yvs_base_depots d ON d.id = ad.depot
    WHERE ad.article = article_ AND ad.depot = COALESCE(depot_, 0)
    LIMIT 1;

    IF COALESCE(_depot_, 0) <= 0 THEN
        IF COALESCE(depot_, 0) > 0 THEN
            SELECT d.agence INTO _agence FROM yvs_base_depots d WHERE d.id = depot_;
            agence_ = _agence;
        END IF;

        IF COALESCE(agence_, 0) <= 0 THEN
            SELECT ad.depot, ad.categorie INTO _depot_, categorie_
            FROM yvs_base_article_depot ad WHERE ad.article = article_ AND ad.default_pr IS TRUE LIMIT 1;
        ELSE
            SELECT ad.depot, ad.categorie INTO _depot_, categorie_
            FROM yvs_base_article_depot ad INNER JOIN yvs_base_depots d ON d.id = ad.depot
            WHERE ad.article = article_ AND ad.default_pr IS TRUE AND d.agence = agence_ LIMIT 1;
        END IF;
    END IF;

    depot_ = COALESCE(_depot_, depot_);
    IF COALESCE(agence_, 0) <= 0 THEN
        SELECT d.agence INTO agence_ FROM yvs_base_depots d WHERE d.id = depot_;
    END IF;

    SELECT p.valorise_from_of INTO valorise_from_of_
    FROM yvs_prod_parametre p INNER JOIN yvs_agences a ON a.societe = p.societe
    WHERE a.id = agence_ LIMIT 1;
    valorise_from_of_ = COALESCE(valorise_from_of_, TRUE);

    -- Requête préparée optimisée
    SELECT m.cout_stock, a.categorie, a.taux_ecart_pr, m.conditionnement
    INTO cout_stock_, categorie_art_, taux_ecart_pr_, conditionnement_
    FROM yvs_base_mouvement_stock m
    INNER JOIN yvs_base_depots d ON m.depot = d.id
    INNER JOIN yvs_base_articles a ON a.id = m.article
    WHERE COALESCE(m.calcul_pr, TRUE) IS TRUE AND m.mouvement = 'E'
        AND m.article = article_ AND m.date_doc <= COALESCE(date_, CURRENT_DATE)
        AND (COALESCE(depot_, 0) = 0 OR m.depot = depot_)
        AND (COALESCE(tranche_, 0) = 0 OR m.tranche = tranche_)
        AND (COALESCE(current_, 0) = 0 OR m.id != current_)
    ORDER BY m.date_doc DESC LIMIT 1;

    ecart_ = COALESCE(taux_ecart_pr_, 0);
    categorie_ = COALESCE(categorie_, categorie_art_);

    IF (categorie_ = 'PF' OR categorie_ = 'PSF') AND COALESCE(valorise_from_of_, FALSE) IS FALSE THEN
        pr_ = get_prix_nomenclature_optimized(article_, unite_, depot_, date_);
    ELSE
        IF conditionnement_ = unite_ THEN
            pr_ = COALESCE(cout_stock_, 0);
        ELSE
            SELECT t.taux_change INTO coef_
            FROM yvs_base_table_conversion t
            INNER JOIN yvs_base_conditionnement us ON us.unite = t.unite
            INNER JOIN yvs_base_conditionnement ud ON ud.unite = t.unite_equivalent
            WHERE us.id = unite_ AND ud.id = conditionnement_;
            coef_ = COALESCE(coef_, 0);
            pr_ = COALESCE(cout_stock_, 0) * coef_;
        END IF;

        IF ecart_ > 0 AND (categorie_ = 'PF' OR categorie_ = 'PSF') THEN
            prix_nom_ = get_prix_nomenclature_optimized(article_, unite_, depot_, date_);
            IF COALESCE(prix_nom_, 0) > 0 AND ABS(pr_ - prix_nom_) > ecart_ THEN
                pr_ = prix_nom_;
            END IF;
        END IF;

        IF pr_ <= 0 THEN
            IF categorie_art_ IS NULL THEN
                SELECT COALESCE(ad.categorie, a.categorie) INTO categorie_
                FROM yvs_base_articles a LEFT JOIN yvs_base_article_depot ad ON ad.article = a.id
                WHERE a.id = article_ LIMIT 1;
            ELSE
                categorie_ = categorie_art_;
            END IF;

            IF categorie_ = 'PF' OR categorie_ = 'PSF' THEN
                pr_ = get_prix_nomenclature_optimized(article_, unite_, depot_, date_);
                pr_ = COALESCE(pr_, 0);
            ELSE
                pr_ = 0;
            END IF;
        END IF;
    END IF;

    RETURN pr_;
END;
$$;

\echo 'Fonction get_pr_reel_optimized créée!'
\echo ''

-- ============================================================================
-- FONCTION get_prix_nomenclature_optimized
-- ============================================================================

CREATE OR REPLACE FUNCTION get_prix_nomenclature_optimized(
    article_ bigint,
    unite_ bigint,
    depot_ bigint,
    date_ date,
    enable_logging boolean DEFAULT FALSE
)
RETURNS double precision
LANGUAGE plpgsql
AS $$
DECLARE
    pr_ double precision;
    prix_ double precision DEFAULT 0;
    prix_N_ double precision DEFAULT 0;
    quantite_N double precision DEFAULT 0;
    composant_ record;
    quantite_liee_ double precision;
BEGIN
    FOR composant_ IN
        SELECT c.id, c.article, c.unite, c.quantite, c.composant_lie, n.quantite as qteN,
            (SELECT COUNT(n2.id) FROM yvs_prod_nomenclature n2
             WHERE n2.article = c.article AND n2.actif IS TRUE) as est_produit_intermediaire
        FROM yvs_prod_composant_nomenclature c
        INNER JOIN yvs_prod_nomenclature n ON n.id = c.nomenclature
        WHERE n.article = article_ AND n.unite_mesure = unite_
            AND c.inside_cout = TRUE AND c.actif IS TRUE AND c.alternatif IS FALSE AND n.actif IS TRUE
    LOOP
        quantite_N = composant_.qteN;

        IF COALESCE(composant_.est_produit_intermediaire, 0) > 0 THEN
            prix_N_ = prix_N_ + (get_prix_nomenclature_optimized(composant_.article, composant_.unite, depot_, date_, enable_logging) * composant_.quantite);
        ELSE
            pr_ = get_pr(composant_.article, depot_, 0, date_, composant_.unite, 0);

            IF composant_.composant_lie IS NOT NULL THEN
                SELECT c1.quantite INTO quantite_liee_ FROM yvs_prod_composant_nomenclature c1 WHERE c1.id = composant_.composant_lie;
                prix_ = prix_ + COALESCE(pr_ * (composant_.quantite * COALESCE(quantite_liee_, 0)), 0);
            ELSE
                prix_ = prix_ + COALESCE(pr_ * composant_.quantite, 0);
            END IF;
        END IF;
    END LOOP;

    IF COALESCE(quantite_N, 0) > 0 THEN
        RETURN (prix_ / quantite_N) + (prix_N_ / quantite_N);
    ELSE
        RETURN prix_;
    END IF;
END;
$$;

\echo 'Fonction get_prix_nomenclature_optimized créée!'
\echo ''

-- Restaurer les messages
SET client_min_messages TO NOTICE;

\echo 'ÉTAPE 3/3: Vérification...'
\echo ''

-- Compter les index créés
SELECT COUNT(*) as "Nombre d'index créés"
FROM pg_indexes
WHERE indexname LIKE 'idx_%mouvement_stock%'
   OR indexname LIKE 'idx_%article_depot%'
   OR indexname LIKE 'idx_%nomenclature%'
   OR indexname LIKE 'idx_%composant%';

\echo ''
\echo '============================================================================'
\echo 'DÉPLOIEMENT TERMINÉ!'
\echo '============================================================================'
\echo ''
\echo 'Prochaines étapes:'
\echo '1. Exécuter: \\i test_performance_optimizations.sql'
\echo '2. Comparer les performances avant/après'
\echo '3. Valider les résultats'
\echo '4. Si OK, remplacer les fonctions originales par les optimisées'
\echo ''
\echo 'Pour remplacer les fonctions originales:'
\echo '  - Renommer get_pr_reel_optimized en get_pr_reel'
\echo '  - Renommer get_prix_nomenclature_optimized en get_prix_nomenclature'
\echo ''
\echo '============================================================================'

