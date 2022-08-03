SELECT insert_droit('ra_view_all', 'Voir toutes les fiches de ration' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration'), 16, 'A,B,C','R');	
	
UPDATE yvs_ressources_page SET page_module = (SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration') WHERE reference_ressource = 'ra_view_cloturer';
UPDATE yvs_ressources_page SET page_module = (SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration') WHERE reference_ressource = 'ra_view_all_societe';
UPDATE yvs_ressources_page SET page_module = (SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration') WHERE reference_ressource = 'ra_view_depot';
UPDATE yvs_ressources_page SET page_module = (SELECT id FROM yvs_page_module WHERE reference = 'gescom_ration') WHERE reference_ressource = 'ra_view_cloturer';