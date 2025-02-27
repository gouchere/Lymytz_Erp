
INSERT INTO yvs_page_module(reference, libelle, description, module)
	VALUES ('base_code_barre', 'Page des codes barres article', 'Page des codes barres article', 9); 
	
INSERT INTO yvs_page_module(reference, libelle, description, module)
	VALUES ('compta_view_bon_prov', 'Page des bons provisoires', 'Page des bons provisoires', 5); 	

INSERT INTO yvs_page_module(reference, libelle, description, module)
	VALUES ('gescom_reconditionnment', 'Page des reconditionnements', 'Page des reconditionnements', 3); 		
	
SELECT insert_droit('stat_general_com', 'Page statistique général commercial', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_ca', 'Page chiffre d''affaire globale', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_classement', 'Page des classements', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_ecart_vendeur', 'Page des ecarts vendeur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_classement_vendeur', 'Page des classements vendeur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_creance_vendeur', 'Page des créances vendeur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_ca_client', 'Page chiffre d''affaire par client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_classement_client', 'Page des classements client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');					
	
SELECT insert_droit('stat_journal_vente_client', 'Page des journaux vente par client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_progression_client', 'Page progressions des clients', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_valorisation_stock', 'Page de valorisation des stocks', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_distribution_stock', 'Page de distribution des stocks', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_resume', 'Page des résumés statistique', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_journal_production', 'Page des journaux de production', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_synthese_production', 'Page des synthèses de production', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_ecart_production', 'Page des écarts de production', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_production_vente', 'Page des productions / ventes', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_bon_provisoire_encours', 'Page des bon provisoires encours', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_solde_caisse', 'Page des soldes des caisses', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_brouillard_caisse', 'Page des brouillards de caisse', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_facture_achat_impaye', 'Page des factures d''achats impayées', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_ca_fournisseur', 'Page chiffre d''affaire par fournisseur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_classement_fournisseur', 'Page des classements fournisseur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_ca_article', 'Page chiffre d''affaire par article', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_classement_article', 'Page des classements article', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');				
	
SELECT insert_droit('stat_marge_article', 'Page des marges article', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');				
	
SELECT insert_droit('stat_listing_vente', 'Page des listings vente', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');				
	
SELECT insert_droit('stat_journal_vente_vendeur', 'Page des journaux vente par vendeur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');				
	
SELECT insert_droit('stat_ristourne_client', 'Page des ristournes client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');				
	
SELECT insert_droit('stat_listing_vente_client', 'Page des listings vente par client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');					
	
SELECT insert_droit('stat_listing_vente_article', 'Page des listings vente par article', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');			
	
SELECT insert_droit('gen_view_publicites', 'Page des publicités', 
	(SELECT id FROM yvs_module WHERE reference = 'base_'), 16, 'A,B,C','P');   		
	
SELECT insert_droit('proj_departement', 'Page des departements', 
	(SELECT id FROM yvs_module WHERE reference = 'proj_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('proj_projet', 'Page des projets', 
	(SELECT id FROM yvs_module WHERE reference = 'proj_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('compta_view_report_nouveau', 'Page de report à nouveau', 
	(SELECT id FROM yvs_module WHERE reference = 'compta_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('gescom_pfv', 'Page des proformat vente', 
	(SELECT id FROM yvs_module WHERE reference = 'com_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('compta_view_centre_anal', 'Page de centre analytique', 
	(SELECT id FROM yvs_module WHERE reference = 'compta_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('compta_view_mens_vente', 'Page de mensualité de vente', 
	(SELECT id FROM yvs_module WHERE reference = 'compta_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('compta_view_mens_achat', 'Page de mensualité d''achat', 
	(SELECT id FROM yvs_module WHERE reference = 'compta_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('compta_view_maj_comptable', 'Page de mise à jour comptable', 
	(SELECT id FROM yvs_module WHERE reference = 'compta_'), 16, 'A,B,C','P');					
	
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
	
SELECT insert_droit('stat_home_transfert_incoherent', 'Page des transferts incoherents', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C,D,E,F,G,H,I,J,K,P,Q,R,S,T,U,V,W,X,Y,Z','P');	