SELECT insert_droit('journaux_view_all', 'Voir tous les journaux de la socièté', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_journaux'), 16, 'A,B,C','R');
	
SELECT insert_droit('journaux_view_only_agence', 'Voir uniquement les journaux de l''agence', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_journaux'), 16, 'A,B,C','R');