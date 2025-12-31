-- DROP FUNCTION public.com_inventaire(int8, int8, int8, int8, int8, varchar, int8, date, bool, varchar, varchar, varchar, int8, int8, bool, bool);

CREATE OR REPLACE FUNCTION public.com_inventaire(societe_ bigint, agence_ bigint, depot_ bigint, emplacement_ bigint, famille_ bigint, categorie_ character varying, groupe_ bigint, date_ date, print_all_ boolean, option_print_ character varying, type_ character varying, article_ character varying, offset_ bigint, limit_ bigint, preparatoire_ boolean, with_child_ boolean)
 RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, emplacement bigint, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision, "position" double precision, count double precision, lot bigint)
 LANGUAGE plpgsql
AS $function$
DECLARE
    count_ bigint;
    option_print_clean_ character varying;
BEGIN
    option_print_clean_ := UPPER(TRIM(COALESCE(option_print_, '')));

    -- =====================================================
    -- 1. TABLE DES ARTICLES FILTRÉS
    -- =====================================================
    DROP TABLE IF EXISTS tmp_article;
    CREATE TEMP TABLE tmp_article AS
    SELECT DISTINCT
        y.id,
        y.article,
        y.depot,
        a.ref_art,
        a.designation,
        f.reference_famille,
        f.designation AS famille_nom,
        d.agence,
        a.actif,
        c.id AS unite,
        u.id AS mesure,
        u.reference,
        COALESCE(c.prix, a.puv) AS puv,
        COALESCE(c.prix_achat, a.pua) AS pua,
        e.emplacement,
        y.requiere_lot
    FROM yvs_base_article_depot y
    INNER JOIN yvs_base_articles a ON y.article = a.id
    INNER JOIN yvs_base_famille_article f ON f.id = a.famille
    INNER JOIN yvs_base_depots d ON y.depot = d.id
    INNER JOIN yvs_base_conditionnement c ON c.article = a.id
    INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id
    LEFT JOIN yvs_base_article_emplacement e ON y.id = e.article
    WHERE f.societe = societe_
        AND (COALESCE(agence_, 0) = 0 OR d.agence = agence_)
        AND (COALESCE(depot_, 0) = 0 OR d.id = depot_)
        AND (COALESCE(groupe_, 0) = 0 OR a.groupe = groupe_)
        AND (COALESCE(emplacement_, 0) = 0 OR e.emplacement = emplacement_)
        AND (TRIM(COALESCE(categorie_, '')) = '' OR a.categorie = categorie_)
        AND (COALESCE(famille_, 0) = 0 
             OR (with_child_ IS NOT TRUE AND a.famille = famille_)
             OR (with_child_ IS TRUE AND a.famille IN (SELECT base_get_sous_famille(famille_, true))))
        AND (TRIM(COALESCE(article_, '')) = '' 
             OR a.id IN (SELECT unnest(string_to_array(article_, ','))::bigint));

    CREATE INDEX idx_tmp_art_1 ON tmp_article(depot, unite);
    CREATE INDEX idx_tmp_art_2 ON tmp_article(depot, article, unite);

    SELECT COUNT(*) INTO count_ FROM tmp_article;
    
    RAISE NOTICE 'Articles trouvés: %', count_;

    -- =====================================================
    -- 2. STOCK - On garde la même structure que l'original
    -- =====================================================
    DROP TABLE IF EXISTS tmp_stock;
    CREATE TEMP TABLE tmp_stock AS
    SELECT * FROM public.get_stock_reel_multi_agg(NULL, 0, depot_, agence_, date_, 0);
    
    CREATE INDEX idx_tmp_stock_1 ON tmp_stock(depot, article);
    CREATE INDEX idx_tmp_stock_2 ON tmp_stock(depot, article, lot);

    RAISE NOTICE 'Lignes stock: %', (SELECT COUNT(*) FROM tmp_stock);

    -- =====================================================
    -- 3. RÉSERVATIONS (seulement si pas préparatoire)
    -- =====================================================
    DROP TABLE IF EXISTS tmp_reservation;
    CREATE TEMP TABLE tmp_reservation AS
    SELECT 
        c.depot,
        c.article,
        c.conditionnement,
        SUM(c.quantite) AS total_reservation
    FROM yvs_com_reservation_stock c
    WHERE COALESCE(preparatoire_, FALSE) IS FALSE AND c.statut = 'V' 
        AND c.date_echeance <= date_
        AND (COALESCE(depot_, 0) = 0 OR c.depot = depot_)
    GROUP BY c.depot, c.article, c.conditionnement;

    CREATE INDEX idx_tmp_res ON tmp_reservation(depot, article, conditionnement);

    -- =====================================================
    -- 4. RESTE À LIVRER (seulement si pas préparatoire)
    -- =====================================================
    DROP TABLE IF EXISTS tmp_reste_a_livrer;
    CREATE TEMP TABLE tmp_reste_a_livrer AS
    SELECT 
        sub.depot,
        sub.article,
        sub.conditionnement,
        GREATEST(0, COALESCE(sub.total_fv, 0) - COALESCE(sub.total_blv, 0)) AS reste
    FROM (
        SELECT 
            d.depot_livrer AS depot,
            c.article,
            c.conditionnement,
            SUM(CASE WHEN d.type_doc = 'FV' AND d.statut_livre != 'L' THEN c.quantite ELSE 0 END) AS total_fv,
            SUM(CASE WHEN d.type_doc = 'BLV' THEN c.quantite ELSE 0 END) AS total_blv
        FROM yvs_com_contenu_doc_vente c
        INNER JOIN yvs_com_doc_ventes d ON d.id = c.doc_vente
        LEFT JOIN yvs_com_entete_doc_vente en ON en.id = d.entete_doc
        WHERE COALESCE(preparatoire_, FALSE) IS FALSE AND d.statut = 'V'
            AND (
                (d.type_doc = 'FV' AND d.statut_livre != 'L' AND en.date_entete <= date_)
                OR (d.type_doc = 'BLV' AND d.date_livraison <= date_)
            )
            AND (COALESCE(depot_, 0) = 0 OR d.depot_livrer = depot_)
        GROUP BY d.depot_livrer, c.article, c.conditionnement
    ) sub;

    CREATE INDEX idx_tmp_ral ON tmp_reste_a_livrer(depot, article, conditionnement);

    -- =====================================================
    -- 5. TABLE DE RÉSULTAT INTERMÉDIAIRE
    -- =====================================================
    DROP TABLE IF EXISTS tmp_resultat;
    CREATE TEMP TABLE tmp_resultat AS
    SELECT
        a.depot,
        a.article,
        a.ref_art,
        a.designation,
        a.reference_famille,
        a.famille_nom,
        a.unite,
        a.reference,
        a.emplacement,
        a.puv,
        a.pua,
        a.actif,
        a.requiere_lot,
        -- Stock: jointure sur 'unite' comme dans l'original (s.article = articles_.unite)
        COALESCE(s.stock, 0) AS stock_val,
        s.lot AS lot_id,
        CASE 
            WHEN COALESCE(preparatoire_, FALSE) THEN 0::double precision
            ELSE COALESCE(r.total_reservation, 0) 
        END AS reservation_val,
        CASE 
            WHEN COALESCE(preparatoire_, FALSE) THEN 0::double precision
            ELSE COALESCE(ral.reste, 0) 
        END AS reste_a_livrer_val
    FROM tmp_article a
    LEFT JOIN tmp_stock s ON s.article = a.unite AND s.depot = a.depot  -- Comme dans l'original
    LEFT JOIN tmp_reservation r 
        ON r.depot = a.depot 
        AND r.article = a.article 
        AND r.conditionnement = a.unite
    LEFT JOIN tmp_reste_a_livrer ral 
        ON ral.depot = a.depot 
        AND ral.article = a.article 
        AND ral.conditionnement = a.unite
	WHERE 
        -- Logique de filtrage identique à l'original
        CASE 
            WHEN COALESCE(print_all_, false) THEN 
                COALESCE(s.stock, 0) != 0 OR a.actif = true
            WHEN option_print_clean_ = 'P' THEN 
                COALESCE(s.stock, 0) > 0
            WHEN option_print_clean_ = 'N' THEN 
                COALESCE(s.stock, 0) < 0
            WHEN option_print_clean_ = 'E' THEN 
                COALESCE(s.stock, 0) = 0 AND a.actif = true
            ELSE 
                COALESCE(s.stock, 0) != 0
        END;

    RAISE NOTICE 'Résultats avant filtrage: %', (SELECT COUNT(*) FROM tmp_resultat);

    -- =====================================================
    -- 6. RÉSULTAT FINAL AVEC FILTRAGE
    -- =====================================================
    RETURN QUERY
    SELECT
        r.depot,
        r.article,
        r.ref_art::character varying AS code,
        r.designation::character varying,
        r.reference_famille::character varying AS numero,
        r.famille_nom::character varying AS famille,
        r.unite,
        r.reference::character varying,
        r.emplacement,
        -- Prix selon le type
        CASE type_
            WHEN 'A' THEN COALESCE(get_pua(r.article, 0, r.depot, r.unite, date_), 0)
            WHEN 'V' THEN COALESCE(get_puv(r.article, 0, 0, 0, r.depot, 0, date_, r.unite, false), 0)
            ELSE COALESCE(get_pr(r.article, r.depot, 0, date_, r.unite), 0)
        END::double precision AS prix,
        r.puv::double precision,
        r.pua::double precision,
        0::double precision AS pr,
        r.stock_val::double precision AS stock,
        r.reservation_val::double precision AS reservation,
        r.reste_a_livrer_val::double precision AS reste_a_livre,
        ROW_NUMBER() OVER (ORDER BY r.designation, r.ref_art)::double precision AS "position",
        count_::double precision AS count,
        COALESCE(r.lot_id, 0)::bigint AS lot
    FROM tmp_resultat r
    ORDER BY r.designation, r.ref_art, r.depot, r.famille_nom
    OFFSET COALESCE(offset_, 0)
    LIMIT COALESCE(NULLIF(limit_, 0), count_);

    -- Nettoyage
    DROP TABLE IF EXISTS tmp_article;
    DROP TABLE IF EXISTS tmp_stock;
    DROP TABLE IF EXISTS tmp_reservation;
    DROP TABLE IF EXISTS tmp_reste_a_livrer;
    DROP TABLE IF EXISTS tmp_resultat;

END;
$function$
;
