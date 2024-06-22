SELECT insert_droit('gescom_hist_pr', 'Page historique PR', 
	(SELECT id FROM yvs_module WHERE reference = 'com_'), 16, 'A,B,C','P');	