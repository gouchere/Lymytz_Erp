UPDATE yvs_print_facture_vente SET view_nui_client = FALSE, view_rcc_client = FALSE, view_image_payer = TRUE, view_image_livrer = TRUE;		
	
SELECT insert_droit('compta_view_centre_anal', 'Page de centre analytique', 
	(SELECT id FROM yvs_module WHERE reference = 'compta_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('compta_view_mens_vente', 'Page de mensualité de vente', 
	(SELECT id FROM yvs_module WHERE reference = 'compta_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('compta_view_mens_achat', 'Page de mensualité d''achat', 
	(SELECT id FROM yvs_module WHERE reference = 'compta_'), 16, 'A,B,C','P');