SELECT insert_droit('gescom_fv_v2', 'Facture vente V2' , 
	(SELECT id FROM yvs_module WHERE reference = 'com_'), 16, 'A,B,C,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z','P');
	
SELECT insert_droit('gescom_fv_service', 'Facture vente, location et service', 
	(SELECT id FROM yvs_module WHERE reference = 'com_'), 16, 'A,B,C','P');	