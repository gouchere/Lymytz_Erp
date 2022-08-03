
SELECT insert_droit('prod_gammes_update_num_ope', 'Modifier le numero d''une opération' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_gammes'), 16, 'A,B,C','R');
	
SELECT insert_droit('fa_view_totaux', 'Voir les totaux d''une facture d''achat' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fa'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');
	
SELECT insert_droit('gescom_entree_view_totaux', 'Voir les totaux d''une fiche d''entrée' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_entree'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');
	
SELECT insert_droit('gescom_sortie_view_totaux', 'Voir les totaux d''une fiche de sortie' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_sortie'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');