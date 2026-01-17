-- DROP FUNCTION public.compta_et_solde(int8, int8, int8, date, date, int8, varchar, bool, varchar);

CREATE OR REPLACE FUNCTION public.compta_et_solde(
    agence_ bigint, 
    societe_ bigint, 
    valeur_ bigint, 
    date_debut_ date, 
    date_fin_ date, 
    journal_ bigint, 
    type_ character varying, 
    general_ boolean, 
    table_tiers_ character varying
)
 RETURNS double precision
 LANGUAGE plpgsql
 STABLE
AS $function$
DECLARE 
    solde_ double precision;
BEGIN 	
    -- Fonction optimisée sans injection SQL ni récursion inefficace
    
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
            AND general_ = true 
            AND pc.type_compte = 'CO'
            AND cr.niveau = 1  -- Limite à un niveau de récursivité
    ),
    -- CTE pour données analytiques (type_ = 'A')
    data_analytique AS (
        SELECT 
            SUM(o.debit) - SUM(o.credit) AS solde
        FROM yvs_compta_content_analytique o
        INNER JOIN yvs_compta_content_journal c ON o.contenu = c.id
        INNER JOIN yvs_compta_pieces_comptable p ON c.piece = p.id
        INNER JOIN yvs_compta_journaux j ON j.id = p.journal
        INNER JOIN yvs_agences a ON j.agence = a.id
        WHERE type_ = 'A'
            AND COALESCE(c.report, FALSE) IS FALSE
            AND (date_debut_ IS NULL OR p.date_piece >= date_debut_)
            AND (date_fin_ IS NULL OR p.date_piece <= date_fin_)
            AND (journal_ IS NULL OR journal_ <= 0 OR j.id = journal_)
            AND (agence_ IS NULL OR agence_ <= 0 OR j.agence = agence_)
            AND (societe_ IS NULL OR societe_ <= 0 OR a.societe = societe_)
            AND o.centre = valeur_
    ),
    -- CTE pour données comptables générales (type_ != 'A')
    data_comptable AS (
        SELECT 
            SUM(c.debit) - SUM(c.credit) AS solde
        FROM yvs_compta_content_journal c
        INNER JOIN yvs_compta_pieces_comptable p ON c.piece = p.id
        INNER JOIN yvs_compta_journaux j ON j.id = p.journal
        INNER JOIN yvs_agences a ON j.agence = a.id
        WHERE type_ != 'A'
            AND COALESCE(c.report, FALSE) IS FALSE
            AND (date_debut_ IS NULL OR p.date_piece >= date_debut_)
            AND (date_fin_ IS NULL OR p.date_piece <= date_fin_)
            AND (journal_ IS NULL OR journal_ <= 0 OR j.id = journal_)
            AND (agence_ IS NULL OR agence_ <= 0 OR j.agence = agence_)
            AND (societe_ IS NULL OR societe_ <= 0 OR a.societe = societe_)
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
                WHEN type_ = 'A' THEN (SELECT solde FROM data_analytique)
                ELSE (SELECT solde FROM data_comptable)
            END, 
            0
        )
    INTO solde_;
    
    RETURN solde_;
END;
$function$
;
