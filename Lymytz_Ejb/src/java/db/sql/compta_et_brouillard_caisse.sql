-- DROP FUNCTION public.compta_et_brouillard_caisse(int8, date, date);

CREATE OR REPLACE FUNCTION public.compta_et_brouillard_caisse(
    caisse_ bigint, 
    date_debut_ date, 
    date_fin_ date
)
RETURNS TABLE(
    id bigint, 
    numero character varying, 
    date_mvt date, 
    note character varying, 
    tiers character varying, 
    mode character varying, 
    montant double precision, 
    type character varying, 
    solde double precision, 
    solde_periode double precision, 
    id_externe bigint, 
    table_externe character varying
)
 LANGUAGE plpgsql
 STABLE
AS $function$
DECLARE 
    solde_initial double precision DEFAULT 0;
BEGIN 	
    -- Vérification du paramètre caisse
    IF caisse_ IS NULL OR caisse_ <= 0 THEN
        RETURN;
    END IF;
    
    -- Calcul du solde initial
    solde_initial := compta_total_caisse(0, caisse_, 0, '', '', 'ESPECE,BANQUE', 'P', (date_debut_ - interval '1 day')::date);
    
    -- Retour du résultat en une seule requête optimisée avec window functions
    RETURN QUERY
    WITH mouvements AS (
        SELECT 
            y.id,
            y.numero,
            y.date_paye,
            y.note,
            y.name_tiers,
            m.designation,
            y.montant,
            y.mouvement,
            y.id_externe,
            y.table_externe,
            y.reference_externe,
            -- Description intelligente avec CASE
            COALESCE(
                NULLIF(TRIM(y.note), ''),
                CASE y.table_externe
                    WHEN 'DOC_ACHAT' THEN 'Reglement Achat N° ' || y.reference_externe
                    WHEN 'DOC_VENTE' THEN 'Reglement Vente N° ' || y.reference_externe
                    WHEN 'MISSION' THEN 'Reglement Mission N° ' || y.reference_externe
                    WHEN 'DOC_VIREMENT' THEN 'Virement N° ' || y.reference_externe
                    WHEN 'ACOMPTE_VENTE' THEN 'Acompte Client N° ' || y.reference_externe
                    WHEN 'ACOMPTE_ACHAT' THEN 'Acompte Fournisseur N° ' || y.reference_externe
                    WHEN 'CREDIT_VENTE' THEN 'Crédit Client N° ' || y.reference_externe
                    WHEN 'CREDIT_ACHAT' THEN 'Crédit Fournisseur N° ' || y.reference_externe
                    ELSE y.note
                END
            ) AS description,
            CASE WHEN y.mouvement = 'R' THEN 'RECETTE' ELSE 'DEPENSE' END AS type_mouvement,
            CASE WHEN y.mouvement = 'R' THEN y.montant ELSE -y.montant END AS montant_signe
        FROM yvs_compta_mouvement_caisse y
        LEFT JOIN yvs_base_mode_reglement m ON y.model = m.id
        WHERE y.statut_piece = 'P' 
            AND y.caisse = caisse_
            AND y.date_paye BETWEEN date_debut_ AND date_fin_
            AND y.table_externe NOT IN ('NOTIF_ACHAT', 'NOTIF_VENTE', 'BON_PROVISOIRE')
            AND (
                y.table_externe NOT IN ('DOC_VENTE', 'DOC_ACHAT') 
                OR (y.table_externe = 'DOC_VENTE' AND NOT EXISTS (
                    SELECT 1 FROM yvs_compta_notif_reglement_vente a WHERE a.piece_vente = y.id_externe
                ))
                OR (y.table_externe = 'DOC_ACHAT' AND NOT EXISTS (
                    SELECT 1 FROM yvs_compta_notif_reglement_achat a WHERE a.piece_achat = y.id_externe
                ))
            )
    ),
    avec_soldes AS (
        SELECT
            m.id,
            m.numero,
            m.date_paye AS date_mvt,
            m.description AS note,
            m.name_tiers AS tiers,
            m.designation AS mode,
            m.montant,
            m.type_mouvement AS type,
            solde_initial + SUM(m.montant_signe) OVER (ORDER BY m.date_paye, m.mouvement DESC, m.numero, m.id) AS solde,
            SUM(m.montant_signe) OVER (ORDER BY m.date_paye, m.mouvement DESC, m.numero, m.id) AS solde_periode,
            m.id_externe,
            m.table_externe
        FROM mouvements m
    )
    -- Solde initial
    SELECT 
        0::bigint AS id,
        'S.I'::character varying AS numero,
        (date_debut_ - interval '1 day')::date AS date_mvt,
        'SOLDE INITIAL'::character varying AS note,
        ''::character varying AS tiers,
        'ESPECE'::character varying AS mode,
        0::double precision AS montant,
        ''::character varying AS type,
        solde_initial AS solde,
        0::double precision AS solde_periode,
        0::bigint AS id_externe,
        NULL::character varying AS table_externe
    
    UNION ALL
    
    -- Mouvements avec soldes calculés
    SELECT * FROM avec_soldes
    
    ORDER BY date_mvt, type DESC, numero;
END;
$function$
;
