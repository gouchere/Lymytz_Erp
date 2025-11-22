-- DROP FUNCTION public.remove_not_source_contenu_journal(int8, varchar, date, date, boolean);

CREATE OR REPLACE FUNCTION public.remove_not_source_contenu_journal(
    societe_ bigint,
    table_name_ varchar,
    date_debut_ date,
    date_fin_ date,
    delete_ boolean
)
RETURNS SETOF yvs_compta_content_journal
LANGUAGE plpgsql
AS $function$
DECLARE
    rec RECORD;
    filter_table varchar := NULLIF(table_name_, '');
BEGIN
    -- TABLE TEMPORAIRE MAPPING
    CREATE TEMP TABLE yvs_contenu_journal_mapping (
        table_externe VARCHAR(150) PRIMARY KEY,
        table_source  VARCHAR(150) NOT NULL
    ) ON COMMIT DROP;

    INSERT INTO yvs_contenu_journal_mapping VALUES
        ('ABONNEMENT_DIVERS',        'yvs_compta_abonement_doc_divers'),
        ('ACOMPTE_ACHAT',            'yvs_compta_acompte_fournisseur'),
        ('ACOMPTE_VENTE',            'yvs_compta_acompte_client'),
        ('AVOIR_ACHAT',              'yvs_com_doc_achats'),
        ('DOC_ACHAT',                'yvs_com_doc_achats'),
        ('AVOIR_VENTE',              'yvs_com_doc_ventes'),
        ('DOC_VENTE',                'yvs_com_doc_ventes'),
        ('BULLETIN',                 'yvs_grh_bulletins'),
        ('CAISSE_ACHAT',             'yvs_compta_caisse_piece_achat'),
        ('CAISSE_CREDIT_ACHAT',      'yvs_compta_reglement_credit_fournisseur'),
        ('CAISSE_CREDIT_VENTE',      'yvs_compta_reglement_credit_client'),
        ('CAISSE_DIVERS',            'yvs_compta_caisse_piece_divers'),
        ('CAISSE_VENTE',             'yvs_compta_caisse_piece_vente'),
        ('CREDIT_ACHAT',             'yvs_compta_credit_fournisseur'),
        ('CREDIT_VENTE',             'yvs_compta_credit_client'),
        ('DOC_DIVERS',               'yvs_compta_caisse_doc_divers'),
        ('DOC_VIREMENT',             'yvs_compta_caisse_piece_virement'),
        ('FRAIS_MISSION',            'yvs_compta_caisse_piece_mission'),
        ('JOURNAL_VENTE',            'yvs_com_entete_doc_vente'),
        ('ORDRE_SALAIRE',            'yvs_grh_ordre_calcul_salaire'),
        ('PHASE_ACOMPTE_ACHAT',      'yvs_compta_phase_acompte_achat'),
        ('PHASE_ACOMPTE_VENTE',      'yvs_compta_phase_acompte_vente'),
        ('PHASE_CAISSE_ACHAT',       'yvs_compta_phase_piece_achat'),
        ('PHASE_CAISSE_DIVERS',      'yvs_compta_phase_piece_divers'),
        ('PHASE_CAISSE_VENTE',       'yvs_compta_phase_piece'),
        ('RETENUE',                  'yvs_grh_element_additionel');

    -- TABLE TEMPORAIRE IDENTIQUE À yvs_compta_content_journal
    CREATE TEMP TABLE deleted_rows
        (LIKE yvs_compta_content_journal INCLUDING ALL)
        ON COMMIT DROP;

    -- PROCESS : un DELETE par type dans le mapping
    FOR rec IN
        SELECT *
        FROM yvs_contenu_journal_mapping
        WHERE filter_table IS NULL OR table_externe = filter_table
    LOOP
        -- 1️⃣ STOCKER LES LIGNES À SUPPRIMER
        EXECUTE format($sql$
            INSERT INTO deleted_rows
            SELECT y.*
            FROM yvs_compta_content_journal y
            JOIN yvs_compta_pieces_comptable p ON y.piece = p.id
            JOIN yvs_compta_journaux j ON p.journal = j.id
            JOIN yvs_agences a ON j.agence = a.id
            WHERE a.societe = %L
              AND y.echeance BETWEEN %L AND %L
              AND y.table_externe = %L
              AND NOT EXISTS (
                    SELECT 1 FROM %I src WHERE src.id = y.ref_externe
              )
        $sql$, societe_, date_debut_, date_fin_, rec.table_externe, rec.table_source);
		
        -- 2️⃣ SUPPRIMER CES LIGNES
        IF(delete_)THEN
            EXECUTE format($sql$
                DELETE FROM yvs_compta_content_journal y
                USING yvs_compta_pieces_comptable p,
                      yvs_compta_journaux j,
                      yvs_agences a
                WHERE y.piece = p.id
                  AND p.journal = j.id
                  AND j.agence = a.id
                  AND a.societe = %L
                  AND y.echeance BETWEEN %L AND %L
                  AND y.table_externe = %L
                  AND NOT EXISTS (
                        SELECT 1 FROM %I src WHERE src.id = y.ref_externe
                  )
            $sql$, societe_, date_debut_, date_fin_, rec.table_externe, rec.table_source);
        END IF;
    END LOOP;

    -- 3️⃣ RETOURNER TOUTES LES LIGNES SUPPRIMÉES (même structure que yvs_compta_content_journal)
    RETURN QUERY SELECT * FROM deleted_rows;

END;
$function$;


;
