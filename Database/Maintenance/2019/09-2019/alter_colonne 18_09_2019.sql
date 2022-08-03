SELECT insert_droit('fv_change_num_doc', 'Modifier manuellement la référence d''une facture de vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fv'), 16, 'A,B,C','R');	