
SELECT insert_droit('production_view_all_user', 'Afficher les ordres de fabrication de tous les utilisateurs', 
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 'A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','R');	