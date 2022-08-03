SELECT insert_droit('gen_view_publicites', 'Page des publicités', 
	(SELECT id FROM yvs_module WHERE reference = 'base_'), 16, 'A,B,C','P');