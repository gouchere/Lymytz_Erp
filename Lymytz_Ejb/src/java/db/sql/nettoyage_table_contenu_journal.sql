-- DROP FUNCTION public.remove_not_source_contenu_journal(int8, varchar, date, date);

CREATE OR REPLACE FUNCTION public.remove_not_source_contenu_journal(societe_ bigint, table_name_ varchar, date_debut_ date, date_fin_ date)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$   
DECLARE 
	record_ RECORD;
BEGIN
	SELECT y.id, y.ref_externe, y.table_externe
	INTO record_
	FROM yvs_compta_content_journal y INNER JOIN yvs_compta_pieces_comptable p ON y.piece = p.id
		INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id 
		WHERE a.societe = societe_ AND y.echeance BETWEEN date_debut_ AND date_fin_
		AND ((COALESCE(table_name_, '') = '' AND y.table_externe IS NOT NULL) OR (COALESCE(table_name_, '') != '' AND y.table_externe = table_name_));
	RAISE NOTICE '%', record_;
	IF(table_name_ = 'ABONNEMENT_DIVERS')THEN
	ELSIF(table_name_ = 'ACOMPTE_ACHAT')THEN
	ELSIF(table_name_ = 'ACOMPTE_VENTE')THEN
	ELSIF(table_name_ = 'AVOIR_ACHAT')THEN
	ELSIF(table_name_ = 'AVOIR_VENTE')THEN
	ELSIF(table_name_ = 'CAISSE_ACHAT')THEN
	ELSIF(table_name_ = 'CAISSE_CREDIT_ACHAT')THEN
	ELSIF(table_name_ = 'CAISSE_DIVERS')THEN
	ELSIF(table_name_ = 'CAISSE_VENTE')THEN
	ELSIF(table_name_ = 'DOC_ACHAT')THEN
	ELSIF(table_name_ = 'DOC_DIVERS')THEN
	ELSIF(table_name_ = 'DOC_VENTE')THEN
	ELSIF(table_name_ = 'DOC_VIREMENT')THEN
	ELSIF(table_name_ = 'FRAIS_MISSION')THEN
	ELSIF(table_name_ = 'JOURNAL_VENTE')THEN
	ELSIF(table_name_ = 'ORDRE_SALAIRE')THEN
	ELSIF(table_name_ = 'PHASE_ACOMPTE_ACHAT')THEN
	ELSIF(table_name_ = 'PHASE_ACOMPTE_VENTE')THEN
	ELSIF(table_name_ = 'PHASE_CAISSE_ACHAT')THEN
	ELSIF(table_name_ = 'PHASE_CAISSE_DIVERS')THEN
	ELSIF(table_name_ = 'PHASE_CAISSE_VENTE')THEN
	ELSIF(table_name_ = 'RETENUE')THEN
	END IF;
	return true;
END;$function$
;