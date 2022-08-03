SELECT insert_droit('base_client_print_actif', 'Télécharger les clients actif' , 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_client'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');
	
SELECT insert_droit('change_requiere_lot_article_depot', 'Changer l''utilisation de lot pour un article meme s''il y''a du stock', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_depots'), 16, 'A,B,C','R');	