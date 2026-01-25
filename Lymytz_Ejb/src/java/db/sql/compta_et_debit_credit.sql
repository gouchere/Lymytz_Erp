CREATE OR REPLACE FUNCTION public.compta_et_debit_credit(
    agence_ bigint, 
    societe_ bigint, 
    valeur_ bigint, 
    date_debut_ date, 
    date_fin_ date, 
    journal_ bigint, 
    type_ character varying, 
    general boolean, 
    table_tiers_ character varying,
	delete_ boolean
)
 RETURNS TABLE(debit double precision, credit double precision)
 LANGUAGE plpgsql
 STABLE
AS $function$
BEGIN 	
    -- Fonction optimisée sans injection SQL ni table temporaire persistante
    RETURN QUERY
    WITH RECURSIVE
    -- CTE pour gérer les comptes collectifs de manière récursive
    comptes_recursifs AS (
        -- Compte principal
        SELECT valeur_ AS compte_id, 1 AS niveau
        
        UNION ALL
        
        -- Sous-comptes si type = 'C', general = true et compte collectif
        SELECT p.id, cr.niveau + 1
        FROM comptes_recursifs cr
        INNER JOIN yvs_base_plan_comptable pc ON pc.id = cr.compte_id
        INNER JOIN yvs_base_plan_comptable p ON p.compte_general = cr.compte_id
        WHERE type_ = 'C' 
            AND general = true 
            AND pc.type_compte = 'CO'
            AND cr.niveau = 1  -- Limite à un niveau de récursivité
    ),
    -- CTE pour données analytiques (type_ = 'A')
    data_analytique AS (
        SELECT 
            SUM(o.debit) AS total_debit, 
            SUM(o.credit) AS total_credit
        FROM view_compta_analytique_debit_credit o
        WHERE type_ = 'A'
            AND COALESCE(o.report, FALSE) IS FALSE
            AND o.date_piece BETWEEN date_debut_ AND date_fin_
            AND (journal_ IS NULL OR journal_ <= 0 OR o.journal_id = journal_)
            AND (agence_ IS NULL OR agence_ <= 0 OR o.agence = agence_)
            AND (societe_ IS NULL OR societe_ <= 0 OR o.societe = societe_)
            AND o.centre = valeur_
    ),
    -- CTE pour données comptables générales (type_ != 'A')
    data_comptable AS (
        SELECT 
            SUM(c.debit) AS total_debit, 
            SUM(c.credit) AS total_credit
        FROM view_compta_debit_credit c
        WHERE type_ != 'A'
            AND COALESCE(c.report, FALSE) IS FALSE
            AND c.date_piece BETWEEN date_debut_ AND date_fin_
            AND (journal_ IS NULL OR journal_ <= 0 OR c.journal_id = journal_)
            AND (agence_ IS NULL OR agence_ <= 0 OR c.agence = agence_)
            AND (societe_ IS NULL OR societe_ <= 0 OR c.societe = societe_)
            AND (
                -- Type Tiers
                (type_ = 'T' AND c.compte_tiers = valeur_ 
                    AND (table_tiers_ IS NULL OR TRIM(table_tiers_) = '' OR c.table_tiers = table_tiers_))
                OR
                -- Type Compte (avec gestion récursive des sous-comptes)
                (type_ = 'C' AND c.compte_general IN (SELECT compte_id FROM comptes_recursifs))
            )
    )
    -- Résultat final selon le type
    SELECT 
        COALESCE(
            CASE 
                WHEN type_ = 'A' THEN (SELECT total_debit FROM data_analytique)
                ELSE (SELECT total_debit FROM data_comptable)
            END, 
            0
        )::double precision AS debit,
        COALESCE(
            CASE 
                WHEN type_ = 'A' THEN (SELECT total_credit FROM data_analytique)
                ELSE (SELECT total_credit FROM data_comptable)
            END, 
            0
        )::double precision AS credit;
END;
$function$
;
