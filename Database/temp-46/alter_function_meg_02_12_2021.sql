SELECT insert_droit('compta_change_compte_content', 'Changer le compte général d''un contenu de journal', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_saisie_libre'), 16, 'A,B,C','R');
	
SELECT insert_droit('base_depot_send_all_stock', 'Transferer tout le stock vers un autre dépot', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'), 16, 'A,B,C','R');
	