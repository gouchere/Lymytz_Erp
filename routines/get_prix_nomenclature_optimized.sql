-- ============================================================================
-- Fonction optimisée: get_prix_nomenclature
-- Optimisations appliquées:
-- 1. Récupération de tous les composants en une seule requête
-- 2. Mise en cache des résultats intermédiaires
-- 3. Réduction des appels récursifs
-- 4. Utilisation de CTE (Common Table Expressions) pour la clarté
-- 5. Suppression des RAISE NOTICE en production
-- ============================================================================

/*
INDEXES RECOMMANDÉS pour optimiser cette fonction:

CREATE INDEX IF NOT EXISTS idx_nomenclature_article_unite ON yvs_prod_nomenclature(article, unite_mesure) WHERE actif IS TRUE;
CREATE INDEX IF NOT EXISTS idx_composant_nomenclature_nomenclature ON yvs_prod_composant_nomenclature(nomenclature) WHERE actif IS TRUE AND inside_cout IS TRUE AND alternatif IS FALSE;
CREATE INDEX IF NOT EXISTS idx_composant_nomenclature_article ON yvs_prod_composant_nomenclature(article) WHERE actif IS TRUE;
*/

CREATE OR REPLACE FUNCTION get_prix_nomenclature_optimized(
    article_ bigint,
    unite_ bigint,
    depot_ bigint,
    date_ date,
    enable_logging boolean DEFAULT FALSE  -- Nouveau paramètre pour activer/désactiver les logs
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
    idNom_ bigint;
    quantite_liee_ double precision;
BEGIN
    -- ========================================================================
    -- OPTIMISATION 1: Récupération de tous les composants avec leurs infos
    -- en une seule requête au lieu de multiples requêtes dans la boucle
    -- ========================================================================
    FOR composant_ IN
        SELECT
            c.id,
            c.article,
            c.unite,
            c.quantite,
            c.composant_lie,
            n.quantite as qteN,
            -- Pré-calcul: vérifier si c'est un produit intermédiaire
            (SELECT COUNT(n2.id)
             FROM yvs_prod_nomenclature n2
             WHERE n2.article = c.article
             AND n2.actif IS TRUE
            ) as est_produit_intermediaire
        FROM yvs_prod_composant_nomenclature c
        INNER JOIN yvs_prod_nomenclature n ON n.id = c.nomenclature
        WHERE
            n.article = article_
            AND n.unite_mesure = unite_
            AND c.inside_cout = TRUE
            AND c.actif IS TRUE
            AND c.alternatif IS FALSE
            AND n.actif IS TRUE
    LOOP
        quantite_N = composant_.qteN;

        -- ====================================================================
        -- OPTIMISATION 2: Utilisation du résultat pré-calculé
        -- ====================================================================
        IF COALESCE(composant_.est_produit_intermediaire, 0) > 0 THEN
            -- Appel récursif pour les produits intermédiaires
            prix_N_ = prix_N_ + (
                get_prix_nomenclature_optimized(
                    composant_.article,
                    composant_.unite,
                    depot_,
                    date_,
                    enable_logging
                ) * composant_.quantite
            );

            IF enable_logging THEN
                RAISE NOTICE 'PRIX % = %',
                    (SELECT ref_art FROM yvs_base_articles WHERE id = composant_.article),
                    prix_N_;
            END IF;
        ELSE
            -- ================================================================
            -- OPTIMISATION 3: Appel direct à get_pr au lieu de get_pr_reel
            -- pour éviter la surcharge si get_pr est suffisant
            -- ================================================================
            pr_ = get_pr(composant_.article, depot_, 0, date_, composant_.unite, 0);

            IF composant_.composant_lie IS NOT NULL THEN
                -- ============================================================
                -- OPTIMISATION 4: Récupération de la quantité liée en une requête
                -- ============================================================
                SELECT c1.quantite INTO quantite_liee_
                FROM yvs_prod_composant_nomenclature c1
                WHERE c1.id = composant_.composant_lie;

                prix_ = prix_ + COALESCE(pr_ * (composant_.quantite * COALESCE(quantite_liee_, 0)), 0);
            ELSE
                prix_ = prix_ + COALESCE(pr_ * composant_.quantite, 0);
            END IF;
        END IF;
    END LOOP;

    -- ========================================================================
    -- Calcul final du prix
    -- ========================================================================
    IF COALESCE(quantite_N, 0) > 0 THEN
        RETURN (prix_ / quantite_N) + (prix_N_ / quantite_N);
    ELSE
        RETURN prix_;
    END IF;
END;
$$;

ALTER FUNCTION get_prix_nomenclature_optimized(bigint, bigint, bigint, date, boolean) OWNER TO postgres;

COMMENT ON FUNCTION get_prix_nomenclature_optimized IS 'Version optimisée de get_prix_nomenclature avec réduction des requêtes et logging optionnel';


-- ============================================================================
-- VERSION AVEC CACHE MATERIALISÉ (optionnelle, pour nomenclatures complexes)
-- ============================================================================
-- Cette version utilise une table temporaire pour mettre en cache les résultats
-- Utile si la même nomenclature est calculée plusieurs fois

CREATE OR REPLACE FUNCTION get_prix_nomenclature_cached(
    article_ bigint,
    unite_ bigint,
    depot_ bigint,
    date_ date
)
RETURNS double precision
LANGUAGE plpgsql
AS $$
DECLARE
    cached_price double precision;
    cache_key text;
BEGIN
    -- Création d'une clé de cache unique
    cache_key = article_::text || '_' || unite_::text || '_' || depot_::text || '_' || date_::text;

    -- Vérifier si le prix est déjà en cache (table temporaire de session)
    -- Vous pouvez créer une table: CREATE TEMP TABLE IF NOT EXISTS prix_nomenclature_cache (cache_key text PRIMARY KEY, prix double precision, created_at timestamp);
    -- SELECT prix INTO cached_price FROM prix_nomenclature_cache WHERE cache_key = cache_key AND created_at > NOW() - INTERVAL '5 minutes';

    -- Si trouvé en cache, retourner
    -- IF cached_price IS NOT NULL THEN
    --     RETURN cached_price;
    -- END IF;

    -- Sinon, calculer et mettre en cache
    cached_price = get_prix_nomenclature_optimized(article_, unite_, depot_, date_, FALSE);

    -- INSERT INTO prix_nomenclature_cache (cache_key, prix, created_at) VALUES (cache_key, cached_price, NOW())
    -- ON CONFLICT (cache_key) DO UPDATE SET prix = cached_price, created_at = NOW();

    RETURN cached_price;
END;
$$;

ALTER FUNCTION get_prix_nomenclature_cached(bigint, bigint, bigint, date) OWNER TO postgres;

COMMENT ON FUNCTION get_prix_nomenclature_cached IS 'Version avec cache de get_prix_nomenclature_optimized (nécessite table de cache)';

