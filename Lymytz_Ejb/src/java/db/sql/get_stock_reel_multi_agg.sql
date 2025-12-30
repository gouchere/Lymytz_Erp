-- DROP FUNCTION public.get_stock_reel_multi_agg(bigint[], bigint, bigint, bigint, date, bigint);
CREATE OR REPLACE FUNCTION public.get_stock_reel_multi_agg(
    art_ids  bigint[],   -- liste d'articles
    tranche_ bigint,
    depot_   bigint,
    agence_  bigint,
    date_    date,
    lot_     bigint      -- 0 = tous lots, sinon stock du lot
)
    RETURNS TABLE(depot bigint, article bigint, lot bigint, stock double precision)
    LANGUAGE sql
    STABLE
AS $$
WITH params AS (
    SELECT
        COALESCE(date_, current_date) AS d,
        COALESCE(tranche_, 0)         AS tranche_id,
        COALESCE(depot_, 0)           AS depot_id,
        COALESCE(agence_, 0)          AS agence_id,
        COALESCE(lot_, 0)             AS lot_id
),
-- 1) Stock cumulé jusqu'à la date via l'agrégat
     stock_agg AS (
         SELECT
			sj.depot,
             sj.conditionnement,
             l.lot,
             SUM(
                     CASE WHEN sj.mouvement = 'E' THEN sj.quantite
                          WHEN sj.mouvement = 'S' THEN -sj.quantite
                          ELSE 0
                    END
             ) AS qty
         FROM yvs_base_mouvement_stock sj
                  JOIN yvs_base_depots d   ON d.id = sj.depot
                  JOIN yvs_agences a       ON a.id = d.agence
                  JOIN params p            ON TRUE
                  LEFT JOIN yvs_base_mouvement_stock_lot l ON l.mouvement = sj.id				
         WHERE (art_ids IS NULL OR array_length(art_ids, 1) IS NULL OR sj.conditionnement = ANY(art_ids))
           AND (p.depot_id   = 0 OR sj.depot = p.depot_id)
           AND (p.agence_id  = 0 OR d.agence = p.agence_id)
           AND sj.date_doc <= p.d
           AND (p.lot_id    = 0 OR l.lot = p.lot_id)   -- filtre lot
         GROUP BY sj.depot, sj.conditionnement, l.lot
     ),
-- 2) Tranches à exclure (comme ta fonction)
     tranches_excl AS (
         SELECT y.id
         FROM yvs_grh_tranche_horaire t
                  JOIN yvs_grh_tranche_horaire y
                       ON y.type_journee = t.type_journee
                           AND y.societe      = t.societe
                  JOIN params p ON TRUE
         WHERE p.tranche_id > 0
           AND t.id = p.tranche_id
           AND t.heure_debut <= y.heure_debut
           AND t.id <> y.id
     ),
-- 3) Correction “tranche” sur la date exacte, en respectant aussi le lot
     correction_tranche AS (
         SELECT
			 m.depot, 
             m.conditionnement,
             l.lot,
             SUM(
                     CASE WHEN m.mouvement = 'E' THEN m.quantite
                          WHEN m.mouvement = 'S' THEN -m.quantite
                          ELSE 0
                    END
             ) AS qty_tranche
         FROM yvs_base_mouvement_stock m
                  JOIN yvs_base_depots d   ON d.id = m.depot
                  JOIN yvs_agences a       ON a.id = d.agence
                  LEFT JOIN yvs_base_mouvement_stock_lot l ON l.mouvement = m.id
                  JOIN params p ON TRUE
         WHERE (art_ids IS NULL OR array_length(art_ids, 1) IS NULL OR m.conditionnement = ANY(art_ids))
           AND (p.depot_id   = 0 OR m.depot = p.depot_id)
           AND (p.agence_id  = 0 OR d.agence = p.agence_id)
           AND (p.lot_id     = 0 OR l.lot = p.lot_id)
           AND p.tranche_id  > 0
           AND m.date_doc    = p.d
           AND m.tranche IN (SELECT id FROM tranches_excl)
         GROUP BY m.depot, m.conditionnement, l.lot
     )
SELECT
    COALESCE(sa.depot, ct.depot) AS depot,
    COALESCE(sa.conditionnement, ct.conditionnement) AS article,
    COALESCE(sa.lot, ct.lot) AS lot,
    COALESCE(sa.qty, 0) - COALESCE(ct.qty_tranche, 0) AS stock
FROM stock_agg sa
         FULL JOIN correction_tranche ct ON ct.depot = sa.depot AND ct.conditionnement = sa.conditionnement AND ct.lot = sa.lot;
$$;