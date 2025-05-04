SELECT insert_droit('base_uncloture_periode_exo', 'Ouvrir une periode d''un execirce', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_exercices'), 16, 'A,B','R');	
	
SELECT insert_droit('base_cloture_periode_exo', 'Cloturer une periode d''un execirce', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_exercices'), 16, 'A,B','R');