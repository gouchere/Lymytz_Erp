-- DROP FUNCTION public.compta_total_caisse(int8, int8, int8, varchar, varchar, varchar, bpchar, date);

CREATE OR REPLACE FUNCTION public.compta_total_caisse(
    societe_ bigint, 
    caisse_ bigint, 
    mode_ bigint, 
    table_ character varying, 
    mouvement_ character varying, 
    type_ character varying, 
    statut_ character, 
    date_ date
)
 RETURNS double precision
 LANGUAGE plpgsql
 STABLE
AS $function$
DECLARE
    recette_ double precision DEFAULT 0;
    depense_ double precision DEFAULT 0;
BEGIN
    -- Requête unique optimisée avec CASE pour calculer recettes et dépenses en un seul parcours
    SELECT 
        COALESCE(SUM(CASE WHEN y.mouvement = 'R' THEN y.montant ELSE 0 END), 0) AS recettes,
        COALESCE(SUM(CASE WHEN y.mouvement = 'D' THEN y.montant ELSE 0 END), 0) AS depenses
    INTO recette_, depense_
    FROM yvs_compta_mouvement_caisse y
    INNER JOIN yvs_agences a ON a.id = y.agence
    LEFT JOIN yvs_base_mode_reglement m ON y.model = m.id
    WHERE y.montant IS NOT NULL
        AND (societe_ IS NULL OR societe_ <= 0 OR a.societe = societe_)
        AND (caisse_ IS NULL OR caisse_ <= 0 OR y.caisse = caisse_)
        AND (mode_ IS NULL OR mode_ <= 0 OR y.model = mode_)
        AND (date_ IS NULL OR y.date_paye <= date_)
        AND (table_ IS NULL OR table_ = '' OR y.table_externe = table_)
        AND (statut_ IS NULL OR statut_ = '' OR y.statut_piece = statut_)
        AND (type_ IS NULL OR type_ = '' OR m.type_reglement = ANY(string_to_array(type_, ',')))
        AND (mouvement_ IS NULL OR mouvement_ = '' OR mouvement_ NOT IN ('D', 'R') OR y.mouvement = mouvement_);

    RETURN recette_ - depense_;
END;$function$
;
