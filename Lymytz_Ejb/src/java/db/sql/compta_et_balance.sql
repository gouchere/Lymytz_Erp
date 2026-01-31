-- DROP FUNCTION public.compta_et_balance(int8, int8, varchar, date, date, int8, varchar, varchar);

CREATE OR REPLACE FUNCTION public.compta_et_balance(
    agence_ bigint, 
    societe_ bigint, 
    comptes_ character varying, 
    date_debut_ date, 
    date_fin_ date, 
    journal_ bigint, 
    type_ character varying, 
    nature_ character varying
)
 RETURNS TABLE(
    numero bigint, 
    code character varying, 
    general character varying, 
    debit_initial double precision, 
    credit_initial double precision, 
    debit_periode double precision, 
    credit_periode double precision, 
    debit_solde_periode double precision, 
    credit_solde_periode double precision, 
    debit_solde_cumul double precision, 
    credit_solde_cumul double precision, 
    is_general boolean, 
    table_tiers character varying, 
    id_general bigint
)
 LANGUAGE plpgsql
 STABLE
AS $function$
DECLARE 
    date_initial_ DATE;
    comptes_array_ text[];
BEGIN 	
    -- Fonction optimisée sans injection SQL ni table temporaire
    
    -- Récupération de la date de début de l'exercice
    SELECT date_debut 
    INTO date_initial_ 
    FROM yvs_base_exercice 
    WHERE date_debut_ BETWEEN date_debut AND date_fin
    LIMIT 1;
    
    -- Si aucune date trouvée, utiliser date_fin_ comme défaut
    date_initial_ := COALESCE(date_initial_, date_fin_);
    
    -- Conversion de la liste de comptes en array
    IF COALESCE(comptes_, '') NOT IN ('', ' ', '0') THEN
        SELECT array_agg(TRIM(elem))
        INTO comptes_array_
        FROM regexp_split_to_table(comptes_, ',') AS elem
        WHERE TRIM(elem) != '';
    ELSE
        comptes_array_ := NULL;
    END IF;
    
    RETURN QUERY
    WITH 
    -- CTE 1: Entités avec mouvements dans la période
    entites_avec_mouvements AS (
        SELECT 
            CASE 
                WHEN type_ = 'T' THEN y.compte_tiers
                WHEN type_ = 'C' THEN c.id
                ELSE ca.id
            END AS entite_id,
            CASE 
                WHEN type_ = 'T' THEN name_tiers(y.compte_tiers, y.table_tiers, 'R')
                WHEN type_ = 'C' THEN c.num_compte
                ELSE ca.code_ref
            END AS entite_code,
            CASE 
                WHEN type_ = 'T' THEN y.table_tiers
                ELSE NULL
            END AS entite_table_tiers,
            SUM(CASE 
                WHEN type_ = 'A' THEN COALESCE(o.debit, 0)
                ELSE COALESCE(y.debit, 0)
            END) AS debit_brut,
            SUM(CASE 
                WHEN type_ = 'A' THEN COALESCE(o.credit, 0)
                ELSE COALESCE(y.credit, 0)
            END) AS credit_brut
        FROM yvs_compta_content_journal y
        LEFT JOIN yvs_base_plan_comptable c ON type_ = 'C' AND y.compte_general = c.id
        LEFT JOIN yvs_compta_content_analytique o ON type_ = 'A' AND o.contenu = y.id
        LEFT JOIN yvs_compta_centre_analytique ca ON type_ = 'A' AND o.centre = ca.id
        INNER JOIN yvs_compta_pieces_comptable p ON y.piece = p.id
        INNER JOIN yvs_compta_journaux j ON p.journal = j.id
        INNER JOIN yvs_agences a ON a.id = j.agence
        WHERE p.date_piece BETWEEN date_debut_ AND date_fin_
            AND (agence_ IS NULL OR agence_ <= 0 OR a.id = agence_)
            AND (societe_ IS NULL OR societe_ <= 0 OR a.societe = societe_)
            AND CASE
                WHEN type_ = 'T' THEN 
                    COALESCE(y.compte_tiers, 0) > 0
                    AND (nature_ NOT IN ('T', 'R', 'S', 'E', 'C', 'F', 'CF') 
                        OR (nature_ IN ('T', 'R', 'S') AND y.table_tiers = 'TIERS')
                        OR (nature_ = 'E' AND y.table_tiers = 'EMPLOYE')
                        OR (nature_ = 'C' AND y.table_tiers = 'CLIENT')
                        OR (nature_ = 'F' AND y.table_tiers = 'FOURNISSEUR')
                        OR (nature_ = 'CF' AND y.table_tiers IN ('CLIENT', 'FOURNISSEUR')))
                WHEN type_ = 'C' THEN c.num_compte IS NOT NULL
                ELSE ca.code_ref IS NOT NULL
            END
            AND (comptes_array_ IS NULL 
                OR CASE 
                    WHEN type_ = 'T' THEN name_tiers(y.compte_tiers, y.table_tiers, 'R') = ANY(comptes_array_)
                    WHEN type_ = 'C' THEN c.num_compte = ANY(comptes_array_)
                    ELSE ca.code_ref = ANY(comptes_array_)
                END)
        GROUP BY 1, 2, 3
    ),
    -- CTE 2: Entités demandées sans mouvement dans la période
    entites_sans_mouvements AS (
        SELECT 
            CASE 
                WHEN type_ = 'T' THEN t.id
                WHEN type_ = 'C' THEN c.id
                ELSE ca.id
            END AS entite_id,
            CASE 
                WHEN type_ = 'T' AND nature_ IN ('T', 'R', 'S') THEN t.code_tiers
                WHEN type_ = 'T' AND nature_ = 'C' THEN cl.code_client
                WHEN type_ = 'T' AND nature_ = 'F' THEN f.code_fsseur
                WHEN type_ = 'C' THEN c.num_compte
                ELSE ca.code_ref
            END AS entite_code,
            CASE 
                WHEN type_ = 'T' AND nature_ IN ('T', 'R', 'S') THEN 'TIERS'
                WHEN type_ = 'T' AND nature_ = 'E' THEN 'EMPLOYE'
                WHEN type_ = 'T' AND nature_ = 'C' THEN 'CLIENT'
                WHEN type_ = 'T' AND nature_ = 'F' THEN 'FOURNISSEUR'
                ELSE NULL
            END AS entite_table_tiers
        FROM (SELECT unnest(comptes_array_) AS compte_code) comptes_demandes
        LEFT JOIN yvs_base_tiers t ON type_ = 'T' AND nature_ IN ('T', 'R', 'S', 'E') AND t.code_tiers = comptes_demandes.compte_code
        LEFT JOIN yvs_grh_employes e ON type_ = 'T' AND nature_ = 'E' AND e.compte_tiers = t.id
        LEFT JOIN yvs_com_client cl ON type_ = 'T' AND nature_ = 'C' AND cl.code_client = comptes_demandes.compte_code
        LEFT JOIN yvs_base_fournisseur f ON type_ = 'T' AND nature_ = 'F' AND f.code_fsseur = comptes_demandes.compte_code
        LEFT JOIN yvs_base_plan_comptable c ON type_ = 'C' AND c.num_compte = comptes_demandes.compte_code
        LEFT JOIN yvs_base_nature_compte nc ON type_ = 'C' AND c.nature_compte = nc.id
        LEFT JOIN yvs_compta_centre_analytique ca ON type_ = 'A' AND ca.code_ref = comptes_demandes.compte_code
        LEFT JOIN yvs_compta_plan_analytique pa ON type_ = 'A' AND ca.plan = pa.id
        WHERE comptes_array_ IS NOT NULL
            AND CASE
                WHEN type_ = 'T' AND nature_ IN ('T', 'R', 'S', 'E') THEN t.id IS NOT NULL
                WHEN type_ = 'T' AND nature_ = 'C' THEN cl.id IS NOT NULL
                WHEN type_ = 'T' AND nature_ = 'F' THEN f.id IS NOT NULL
                WHEN type_ = 'C' THEN c.id IS NOT NULL
                ELSE ca.id IS NOT NULL
            END
            AND (societe_ IS NULL OR societe_ <= 0 
                OR CASE
                    WHEN type_ = 'T' AND nature_ IN ('T', 'R', 'S', 'E') THEN t.societe = societe_
                    WHEN type_ = 'T' AND nature_ = 'C' THEN (SELECT tp.societe FROM yvs_base_tiers tp WHERE tp.id = cl.tiers) = societe_
                    WHEN type_ = 'T' AND nature_ = 'F' THEN (SELECT tp.societe FROM yvs_base_tiers tp WHERE tp.id = f.tiers) = societe_
                    WHEN type_ = 'C' THEN nc.societe = societe_
                    ELSE pa.societe = societe_
                END)
            AND (agence_ IS NULL OR agence_ <= 0
                OR CASE
                    WHEN type_ = 'C' THEN nc.societe = (SELECT ag.societe FROM yvs_agences ag WHERE ag.id = agence_)
                    WHEN type_ = 'A' THEN pa.societe = (SELECT ag.societe FROM yvs_agences ag WHERE ag.id = agence_)
                    ELSE true
                END)
            AND NOT EXISTS (
                SELECT 1 FROM entites_avec_mouvements eam 
                WHERE eam.entite_code = comptes_demandes.compte_code
            )
    ),
    -- CTE 3: Union des deux sources
    toutes_entites AS (
        SELECT entite_id, entite_code, entite_table_tiers, debit_brut, credit_brut FROM entites_avec_mouvements
        UNION ALL
        SELECT entite_id, entite_code, entite_table_tiers, 0 AS debit_brut, 0 AS credit_brut FROM entites_sans_mouvements
    ),
    -- CTE 4: Calcul des soldes initiaux et mouvements
    balance_complete AS (
        SELECT 
            te.entite_id,
            te.entite_code,
            te.entite_table_tiers,
            te.debit_brut,
            te.credit_brut,
            CASE 
                WHEN date_initial_ < date_debut_ THEN
                    (SELECT debit FROM compta_et_debit_credit_all(agence_, societe_, te.entite_id, date_initial_, (date_debut_ - interval '1 day')::date, journal_, type_, false, te.entite_table_tiers, false))
                ELSE
                    (SELECT debit FROM compta_et_debit_credit_initial(agence_, societe_, te.entite_id, date_fin_, journal_, type_, false, te.entite_table_tiers, false))
            END AS di_raw,
            CASE 
                WHEN date_initial_ < date_debut_ THEN
                    (SELECT credit FROM compta_et_debit_credit_all(agence_, societe_, te.entite_id, date_initial_, (date_debut_ - interval '1 day')::date, journal_, type_, false, te.entite_table_tiers, false))
                ELSE
                    (SELECT credit FROM compta_et_debit_credit_initial(agence_, societe_, te.entite_id, date_fin_, journal_, type_, false, te.entite_table_tiers, false))
            END AS ci_raw
        FROM toutes_entites te
    ),
    -- CTE 5: Calculs finaux
    balance_finale AS (
        SELECT 
            bc.entite_id,
            bc.entite_code,
            bc.entite_table_tiers,
            COALESCE(bc.di_raw, 0) AS di,
            COALESCE(bc.ci_raw, 0) AS ci,
            COALESCE(bc.di_raw, 0) - COALESCE(bc.ci_raw, 0) AS si,
            CASE 
                WHEN date_initial_ < date_debut_ THEN COALESCE(bc.debit_brut, 0)
                ELSE COALESCE(bc.debit_brut, 0) - COALESCE(bc.di_raw, 0)
            END AS dp,
            CASE 
                WHEN date_initial_ < date_debut_ THEN COALESCE(bc.credit_brut, 0)
                ELSE COALESCE(bc.credit_brut, 0) - COALESCE(bc.ci_raw, 0)
            END AS cp
        FROM balance_complete bc
    )
    -- Résultat final
    SELECT 
        bf.entite_id AS numero,
        bf.entite_code AS code,
        CASE 
            WHEN type_ = 'C' THEN COALESCE(g.num_compte, c.num_compte)
            ELSE ''::character varying
        END AS general,
        CASE WHEN bf.si > 0 THEN bf.si ELSE 0::double precision END AS debit_initial,
        CASE WHEN bf.si < 0 THEN -bf.si ELSE 0::double precision END AS credit_initial,
        bf.dp AS debit_periode,
        bf.cp AS credit_periode,
        CASE WHEN (bf.dp - bf.cp) > 0 THEN (bf.dp - bf.cp) ELSE 0::double precision END AS debit_solde_periode,
        CASE WHEN (bf.dp - bf.cp) < 0 THEN -(bf.dp - bf.cp) ELSE 0::double precision END AS credit_solde_periode,
        CASE WHEN (bf.si + bf.dp - bf.cp) > 0 THEN (bf.si + bf.dp - bf.cp) ELSE 0::double precision END AS debit_solde_cumul,
        CASE WHEN (bf.si + bf.dp - bf.cp) < 0 THEN -(bf.si + bf.dp - bf.cp) ELSE 0::double precision END AS credit_solde_cumul,
        CASE 
            WHEN type_ = 'C' THEN EXISTS(SELECT 1 FROM yvs_base_plan_comptable pc WHERE pc.compte_general = bf.entite_id)
            ELSE false
        END AS is_general,
        COALESCE(bf.entite_table_tiers, '')::character varying AS table_tiers,
        CASE 
            WHEN type_ = 'C' THEN COALESCE(g.id, c.id)
            ELSE 0::bigint
        END AS id_general
    FROM balance_finale bf
    LEFT JOIN yvs_base_plan_comptable c ON type_ = 'C' AND c.id = bf.entite_id
    LEFT JOIN yvs_base_plan_comptable g ON type_ = 'C' AND g.id = c.compte_general
    WHERE (bf.di != 0 OR bf.ci != 0 OR bf.dp != 0 OR bf.cp != 0 
        OR (bf.dp - bf.cp) != 0 OR (bf.si + bf.dp - bf.cp) != 0)
    ORDER BY bf.entite_id;
END;
$function$
;
