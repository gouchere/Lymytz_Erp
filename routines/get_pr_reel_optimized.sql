-- ============================================================================
-- Fonction optimisée: get_pr_reel
-- Optimisations appliquées:
-- 1. Requête préparée au lieu de construction dynamique
-- 2. Regroupement des SELECT INTO avec JOINs
-- 3. Suppression des RAISE NOTICE (logging désactivable)
-- 4. Meilleure gestion du cache avec variables
-- 5. Index suggérés en commentaire
-- ============================================================================

/*
INDEXES RECOMMANDÉS pour optimiser cette fonction:

CREATE INDEX IF NOT EXISTS idx_article_depot_article_depot ON yvs_base_article_depot(article, depot);
CREATE INDEX IF NOT EXISTS idx_article_depot_article_default ON yvs_base_article_depot(article) WHERE default_pr IS TRUE;
CREATE INDEX IF NOT EXISTS idx_mouvement_stock_article_date ON yvs_base_mouvement_stock(article, date_doc DESC, mouvement) WHERE COALESCE(calcul_pr, TRUE) IS TRUE;
CREATE INDEX IF NOT EXISTS idx_mouvement_stock_depot ON yvs_base_mouvement_stock(depot, article, date_doc DESC) WHERE COALESCE(calcul_pr, TRUE) IS TRUE;
CREATE INDEX IF NOT EXISTS idx_table_conversion_unite ON yvs_base_table_conversion(unite, unite_equivalent);
CREATE INDEX IF NOT EXISTS idx_conditionnement_unite ON yvs_base_conditionnement(id, unite);
*/

CREATE OR REPLACE FUNCTION get_pr_reel_optimized(
    agence_ bigint,
    article_ bigint,
    depot_ bigint,
    tranche_ bigint,
    date_ date,
    unite_ bigint,
    current_ bigint,
    enable_logging boolean DEFAULT FALSE  -- Nouveau paramètre pour activer/désactiver les logs
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
    -- ========================================================================
    -- OPTIMISATION 1: Récupération du dépôt et catégorie en une seule requête
    -- ========================================================================
    SELECT
        ad.depot_pr,
        ad.categorie,
        d.agence
    INTO
        _depot_,
        categorie_,
        _agence
    FROM yvs_base_article_depot ad
    LEFT JOIN yvs_base_depots d ON d.id = ad.depot
    WHERE ad.article = article_ AND ad.depot = COALESCE(depot_, 0)
    LIMIT 1;

    -- Si pas de dépôt PR trouvé, chercher le dépôt par défaut
    IF COALESCE(_depot_, 0) <= 0 THEN
        IF COALESCE(depot_, 0) > 0 THEN
            -- Récupère l'agence du dépôt fourni
            SELECT d.agence INTO _agence
            FROM yvs_base_depots d
            WHERE d.id = depot_;
            agence_ = _agence;
        END IF;

        -- Récupération du dépôt par défaut en une requête optimisée
        IF COALESCE(agence_, 0) <= 0 THEN
            SELECT ad.depot, ad.categorie INTO _depot_, categorie_
            FROM yvs_base_article_depot ad
            WHERE ad.article = article_ AND ad.default_pr IS TRUE
            LIMIT 1;
        ELSE
            SELECT ad.depot, ad.categorie INTO _depot_, categorie_
            FROM yvs_base_article_depot ad
            INNER JOIN yvs_base_depots d ON d.id = ad.depot
            WHERE ad.article = article_ AND ad.default_pr IS TRUE AND d.agence = agence_
            LIMIT 1;
        END IF;
    END IF;

    depot_ = COALESCE(_depot_, depot_);

    -- Récupération de l'agence si non définie
    IF COALESCE(agence_, 0) <= 0 THEN
        SELECT d.agence INTO agence_
        FROM yvs_base_depots d
        WHERE d.id = depot_;
    END IF;

    -- Logging optionnel (désactivé par défaut pour performance)
    IF enable_logging THEN
        RAISE NOTICE 'AGENCE= % DEPOT= %, CATEGORIE= %', agence_, depot_, categorie_;
    END IF;

    -- ========================================================================
    -- OPTIMISATION 2: Récupération du paramètre avec cache possible
    -- ========================================================================
    SELECT p.valorise_from_of INTO valorise_from_of_
    FROM yvs_prod_parametre p
    INNER JOIN yvs_agences a ON a.societe = p.societe
    WHERE a.id = agence_
    LIMIT 1;

    valorise_from_of_ = COALESCE(valorise_from_of_, TRUE);

    -- ========================================================================
    -- OPTIMISATION 3: Requête préparée avec tous les paramètres
    -- Plus de construction dynamique, meilleure utilisation du cache de plan
    -- ========================================================================
    SELECT
        m.cout_stock,
        a.categorie,
        a.taux_ecart_pr,
        m.conditionnement
    INTO
        cout_stock_,
        categorie_art_,
        taux_ecart_pr_,
        conditionnement_
    FROM yvs_base_mouvement_stock m
    INNER JOIN yvs_base_depots d ON m.depot = d.id
    INNER JOIN yvs_base_articles a ON a.id = m.article
    WHERE
        COALESCE(m.calcul_pr, TRUE) IS TRUE
        AND m.mouvement = 'E'
        AND m.article = article_
        AND m.date_doc <= COALESCE(date_, CURRENT_DATE)
        AND (COALESCE(depot_, 0) = 0 OR m.depot = depot_)
        AND (COALESCE(tranche_, 0) = 0 OR m.tranche = tranche_)
        AND (COALESCE(current_, 0) = 0 OR m.id != current_)
    ORDER BY m.date_doc DESC
    LIMIT 1;

    ecart_ = COALESCE(taux_ecart_pr_, 0);
    categorie_ = COALESCE(categorie_, categorie_art_);

    -- ========================================================================
    -- OPTIMISATION 4: Calcul du prix selon la catégorie
    -- ========================================================================
    IF (categorie_ = 'PF' OR categorie_ = 'PSF') AND COALESCE(valorise_from_of_, FALSE) IS FALSE THEN
        -- Retourne le prix de la nomenclature si la société évalue ses PF au PR
        pr_ = get_prix_nomenclature_optimized(article_, unite_, depot_, date_);
    ELSE
        IF conditionnement_ = unite_ THEN
            pr_ = COALESCE(cout_stock_, 0);
        ELSE
            -- Recherche le coefficient de conversion en une requête optimisée
            SELECT t.taux_change INTO coef_
            FROM yvs_base_table_conversion t
            INNER JOIN yvs_base_conditionnement us ON us.unite = t.unite
            INNER JOIN yvs_base_conditionnement ud ON ud.unite = t.unite_equivalent
            WHERE us.id = unite_ AND ud.id = conditionnement_;

            coef_ = COALESCE(coef_, 0);
            pr_ = COALESCE(cout_stock_, 0) * coef_;
        END IF;

        -- Gestion de l'écart de prix
        IF ecart_ > 0 THEN
            IF categorie_ = 'PF' OR categorie_ = 'PSF' THEN
                prix_nom_ = get_prix_nomenclature_optimized(article_, unite_, depot_, date_);
                IF COALESCE(prix_nom_, 0) > 0 AND ABS(pr_ - prix_nom_) > ecart_ THEN
                    pr_ = prix_nom_;
                END IF;
            END IF;
        END IF;

        -- Si pas de prix trouvé, essayer via nomenclature
        IF pr_ <= 0 THEN
            -- Récupération de la catégorie si non définie
            IF categorie_art_ IS NULL THEN
                SELECT COALESCE(ad.categorie, a.categorie) INTO categorie_
                FROM yvs_base_articles a
                LEFT JOIN yvs_base_article_depot ad ON ad.article = a.id
                WHERE a.id = article_
                LIMIT 1;
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

ALTER FUNCTION get_pr_reel_optimized(bigint, bigint, bigint, bigint, date, bigint, bigint, boolean) OWNER TO postgres;

COMMENT ON FUNCTION get_pr_reel_optimized IS 'Version optimisée de get_pr_reel avec requêtes préparées et logging optionnel';

