SELECT insert_droit('p_caiss_view_all_societe', 'Voir toutes les pieces de caisse de la socièté', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_pieces'), 16, 'A,B,C','R');	