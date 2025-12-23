CREATE OR REPLACE FUNCTION get_prix_nomenclature_setbased(
    article_ bigint,
    unite_   bigint,
    depot_   bigint,
    date_    date
)
RETURNS double precision
LANGUAGE sql
STABLE
AS $$
WITH RECURSIVE
-- 1) Nomenclature racine (on récupère sa quantité de sortie)
root_nom AS (
    SELECT n.id AS nom_id,
           n.quantite AS root_out_qty
    FROM yvs_prod_nomenclature n
    WHERE n.article = article_
      AND n.unite_mesure = unite_
      AND n.actif IS TRUE
    ORDER BY n.id DESC
    LIMIT 1
),

-- 2) Expansion récursive des composants
--    multiplier = facteur global qui intègre:
--      - la quantité demandée du composant à ce niveau
--      - la division par la quantité de sortie de la nomenclature du composant intermédiaire (si intermédiaire)
--    On applique la division finale par root_out_qty au démarrage (plus simple & fidèle).
bom AS (
    -- Niveau 1 : composants de la racine
    SELECT
        rn.nom_id                             AS parent_nom_id,
        c.id                                  AS comp_id,
        c.article                              AS comp_article,
        c.unite                                AS comp_unite,
        /* qty_effective reproduit:
           - si composant_lie != null => c.quantite * qte(composant_lie) (sinon 0)
           - sinon => c.quantite
        */
        CASE
            WHEN c.composant_lie IS NOT NULL THEN
                c.quantite * COALESCE(cl.quantite, 0)
            ELSE
                c.quantite
        END
        AS qty_effective,

        /* multiplier démarre en intégrant la division finale par la quantité racine */
        CASE
            WHEN rn.root_out_qty IS NULL OR rn.root_out_qty = 0 THEN 0::double precision
            ELSE
                (CASE
                    WHEN c.composant_lie IS NOT NULL THEN
                        c.quantite * COALESCE(cl.quantite, 0)
                    ELSE
                        c.quantite
                 END)::double precision / rn.root_out_qty
        END
       AS multiplier,

        /* cycle guard */
        ARRAY[article_]::bigint[] AS path_articles, 1  yveAS depth
    FROM root_nom rn
    JOIN yvs_prod_composant_nomenclature c
      ON c.nomenclature = rn.nom_id
     AND c.actif IS TRUE
     AND c.inside_cout IS TRUE
     AND c.alternatif IS FALSE
    LEFT JOIN yvs_prod_composant_nomenclature cl
      ON cl.id = c.composant_lie

    UNION ALL

    -- Niveaux suivants : si le composant courant est un produit intermédiaire, on déroule sa nomenclature
    SELECT
        n2.id                                   AS parent_nom_id,
        c2.id                                   AS comp_id,
        c2.article                               AS comp_article,
        c2.unite                                 AS comp_unite,

        CASE
            WHEN c2.composant_lie IS NOT NULL THEN
                c2.quantite * COALESCE(cl2.quantite, 0)
            ELSE
                c2.quantite
        END                                     AS qty_effective,

        /* IMPORTANT: division par n2.quantite (quantité de sortie du produit intermédiaire),
           comme le fait ta fonction via get_prix_nomenclature(intermediate) = sum(...) / n2.quantite
        */
        b.multiplier
        * (
            CASE
                WHEN n2.quantite IS NULL OR n2.quantite = 0 THEN 0::double precision
                ELSE
                    (CASE
                        WHEN c2.composant_lie IS NOT NULL THEN
                            c2.quantite * COALESCE(cl2.quantite, 0)
                        ELSE
                            c2.quantite
                     END)::double precision / n2.quantite
            END
          )                                     AS multiplier,

        b.path_articles || b.comp_article        AS path_articles,
        b.depth + 1                              AS depth
    FROM bom b
    -- nomenclature active du composant (si intermédiaire)
    JOIN LATERAL (
        SELECT n.id, n.quantite
        FROM yvs_prod_nomenclature n
        WHERE n.article = b.comp_article
          AND n.unite_mesure = b.comp_unite
          AND n.actif IS TRUE
        ORDER BY n.id DESC
        LIMIT 1
    ) n2 ON TRUE
    JOIN yvs_prod_composant_nomenclature c2
      ON c2.nomenclature = n2.id
     AND c2.actif IS TRUE
     AND c2.inside_cout IS TRUE
     AND c2.alternatif IS FALSE
    LEFT JOIN yvs_prod_composant_nomenclature cl2
      ON cl2.id = c2.composant_lie
    WHERE
      -- anti-cycle (si boucle de nomenclature)
      NOT (b.comp_article = ANY (b.path_articles))
      -- garde-fou profondeur (optionnel)
      AND b.depth < 50
),

-- 3) Identification des feuilles :
--    une feuille = composant qui N'A PAS de nomenclature active (article+unite)
leaves AS (
    SELECT
        b.comp_article AS article,
        b.comp_unite   AS unite,
        SUM(b.multiplier) AS total_multiplier
    FROM bom b
    LEFT JOIN LATERAL (
        SELECT 1
        FROM yvs_prod_nomenclature n
        WHERE n.article = b.comp_article
          AND n.unite_mesure = b.comp_unite
          AND n.actif IS TRUE
        LIMIT 1
    ) has_nom ON TRUE
    WHERE has_nom IS NULL
    GROUP BY b.comp_article, b.comp_unite
),

-- 4) Prix des feuilles (1 appel get_pr par couple article/unite)
leaf_prices AS (
    SELECT
        l.article,
        l.unite,
        l.total_multiplier,
        get_pr(l.article, depot_, 0, date_, l.unite, 0) AS pr
    FROM leaves l
)

SELECT
    COALESCE(SUM(lp.pr * lp.total_multiplier), 0::double precision)
FROM leaf_prices lp;
$$;

COMMENT ON FUNCTION get_prix_nomenclature_setbased(bigint, bigint, bigint, date)
IS 'Version set-based de get_prix_nomenclature: expansion via WITH RECURSIVE + agrégation des feuilles, division par quantite à chaque niveau.';
