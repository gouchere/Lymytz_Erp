
SELECT insert_droit('compta_ope_client_change_agence_equilibre', 'Changer l''agence lors de l''équilibre', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_ope_client'), 16, 'A,B,C','R');
	
SELECT insert_droit('compta_ope_fsseur_change_agence_equilibre', 'Changer l''agence lors de l''équilibre', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_ope_fsseur'), 16, 'A,B,C','R');		
	
SELECT insert_droit('compta_view_maj_comptable', 'Page de mise à jour comptable', 
	(SELECT id FROM yvs_module WHERE reference = 'compta_'), 16, 'A,B,C','P');	