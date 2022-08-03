SELECT insert_droit('compta_view_report_nouveau', 'Page de report à nouveau', 
	(SELECT id FROM yvs_module WHERE reference = 'compta_'), 16, 'A,B,C','P');
SELECT insert_droit('rist_change_agence', 'Changer l''agence lors du calcul de la ristourne' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_rist'), 16, 'A,B,C','R');
	
	
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, defined_livraison, defined_update, description, defined_reglement, workflow)
    VALUES ('VENTE_NON_COMPTABILISE', 'yvs_com_doc_ventes', FALSE, FALSE, 'Factures de vente non comptabilisées', FALSE, FALSE);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, defined_livraison, defined_update, description, defined_reglement, workflow)
    VALUES ('ACHAT_NON_COMPTABILISE', 'yvs_com_doc_achats', FALSE, FALSE, 'Factures d''achat non comptabilisées', FALSE, FALSE);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, defined_livraison, defined_update, description, defined_reglement, workflow)
    VALUES ('OD_NON_COMPTABILISE', 'yvs_compta_caisse_doc_divers', FALSE, FALSE, 'Opérations diverses non comptabilisées', FALSE, FALSE);
	
DELETE FROM yvs_compta_content_journal WHERE debit = 0 AND credit = 0;

ALTER TABLE yvs_base_mode_reglement ALTER COLUMN designation TYPE character varying;
ALTER TABLE yvs_base_mode_reglement ALTER COLUMN description TYPE character varying;

DROP FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, bigint, character varying, boolean, character varying);
DROP FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying);
DROP FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean);

DROP FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, bigint, character varying, boolean, character varying);
DROP FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, character varying);
DROP FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean);

DROP FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying);
DROP FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean);
DROP FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying);

DROP FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean);
DROP FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying);
DROP FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying);