SELECT insert_droit('p_caiss_view_all_societe', 'Voir toutes les pieces de caisse de la socièté', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_pieces'), 16, 'A,B,C','R');					
	
SELECT insert_droit('stat_home_classement_client', 'Page des 5 meilleurs client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');					
	
SELECT insert_droit('stat_home_classement_vendeur', 'Page des 5 meilleurs vendeur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');					
	
SELECT insert_droit('stat_home_classement_point', 'Page des 5 meilleurs point', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');					
	
SELECT insert_droit('stat_home_classement_article', 'Page des 5 articles le bien vendu', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');				
	
SELECT insert_droit('stat_home_classement_fournisseur', 'Page des 5 grosses dette de fournisseur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');				
	
SELECT insert_droit('stat_home_classement_caisse', 'Page des 5 caisses les plus mouvementées', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');				
	
SELECT insert_droit('stat_home_classement_bon_provisoire', 'Page des 5 bons provisoires non justifié', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');					
	
SELECT insert_droit('stat_home_resume_salarial', 'Page du résumé salarial', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');	