-- Function: compta_action_on_content_journal_facture_vente()

-- DROP FUNCTION compta_action_on_content_journal_facture_vente();

CREATE OR REPLACE FUNCTION compta_control_delete_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	id_compta bigint;
	id_header bigint;
	table_ character varying ;
BEGIN	
	table_ =TG_TABLE_NAME;
	CASE table_
		WHEN 'yvs_compta_content_journal_facture_vente' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.facture AND c.table_externe='DOC_VENTE';
			IF(id_compta IS NULL) THEN
				SELECT INTO id_header d.entete_doc FROM yvs_com_doc_ventes d WHERE d.id=OLD.facture;
				SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=id_header AND c.table_externe='JOURNAL_VENTE';
			END IF;
		WHEN 'yvs_compta_content_journal_retenue_salaire' THEN
		WHEN 'yvs_compta_content_journal_reglement_credit_fournisseur' THEN
		WHEN 'yvs_compta_content_journal_reglement_credit_client' THEN
			
		WHEN 'yvs_compta_content_journal_piece_virement' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.reglement AND c.table_externe='DOC_VIREMENT';
		WHEN 'yvs_compta_content_journal_piece_vente' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.reglement AND c.table_externe='CAISSE_VENTE';
		WHEN 'yvs_compta_content_journal_piece_mission' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.reglement AND c.table_externe='FRAIS_MISSION';
		WHEN 'yvs_compta_content_journal_piece_divers' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.reglement AND c.table_externe='CAISSE_DIVERS';
		WHEN 'yvs_compta_content_journal_piece_achat' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.reglement AND c.table_externe='CAISSE_ACHAT';
		WHEN 'yvs_compta_content_journal_facture_achat' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.facture AND c.table_externe='DOC_ACHAT';
		WHEN 'yvs_compta_content_journal_etape_reglement_credit_fournisseur' THEN
		WHEN 'yvs_compta_content_journal_etape_reglement_credit_client' THEN
		WHEN 'yvs_compta_content_journal_etape_piece_virement' THEN
			
		WHEN 'yvs_compta_content_journal_etape_piece_vente' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.etape AND c.table_externe='PHASE_CAISSE_VENTE';
		WHEN 'yvs_compta_content_journal_etape_piece_divers' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.etape AND c.table_externe='PHASE_CAISSE_DIVERS';
		WHEN 'yvs_compta_content_journal_etape_piece_achat' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.etape AND c.table_externe='PHASE_CAISSE_ACHAT';
		WHEN 'yvs_compta_content_journal_etape_acompte_vente' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.etape AND c.table_externe='PHASE_ACOMPTE_VENTE';
		WHEN 'yvs_compta_content_journal_etape_acompte_achat' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.etape AND c.table_externe='PHASE_ACOMPTE_ACHAT';
		WHEN 'yvs_compta_content_journal_entete_facture_vente' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.entete AND c.table_externe='JOURNAL_VENTE';
		WHEN 'yvs_compta_content_journal_entete_bulletin' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.entete AND c.table_externe='ORDRE_SALAIRE';
		WHEN 'yvs_compta_content_journal_doc_divers' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.divers AND c.table_externe='DOC_DIVERS';
		WHEN 'yvs_compta_content_journal_credit_fournisseur' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.credit AND c.table_externe='CREDIT_ACHAT';
		WHEN 'yvs_compta_content_journal_credit_client' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.credit AND c.table_externe='CREDIT_VENTE';
		WHEN 'yvs_compta_content_journal_bulletin' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.bulletin AND c.table_externe='BULLETIN';
		WHEN 'yvs_compta_content_journal_acompte_fournisseur' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.acompte AND c.table_externe='ACOMPTE_ACHAT';						
		WHEN 'yvs_compta_content_journal_acompte_client' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.acompte AND c.table_externe='ACOMPTE_VENTE';						
		WHEN 'yvs_compta_content_journal_abonnement_divers' THEN
			SELECT INTO id_compta id FROM yvs_compta_content_journal c WHERE c.ref_externe=OLD.abonnement AND c.table_externe='ABONNEMENT_DIVERS';						
	END CASE;
	IF(id_compta IS NOT NULL) THEN 
				 RAISE EXCEPTION 'La relation ne peut être supprimé !';
	END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_control_delete_facture_vente()
  OWNER TO postgres;
