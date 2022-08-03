UPDATE yvs_compta_acompte_client SET nature = 'A' WHERE nature IS NULL;
UPDATE yvs_compta_acompte_fournisseur SET nature = 'A' WHERE nature IS NULL;

SELECT insert_droit('fa_update_when_paye', 'Modifier une facture d''achat déja réglé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fa'), 16, 'A','R');