SELECT insert_droit('compta_virement_view_all_users', 'Voir les virements de tous les caissiers', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_virement'), 16, 'A,B,C,E,F', 'R');
	
SELECT insert_droit('compta_virement_view_all_caisse', 'Voir les virements de toutes les caisses', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_virement'), 16, 'A,B,C,E,F', 'R');
	

ALTER TABLE yvs_compta_acompte_client ADD COLUMN statut_notif character(1);
ALTER TABLE yvs_compta_acompte_client ALTER COLUMN statut_notif SET DEFAULT 'W'::bpchar;

ALTER TABLE yvs_compta_acompte_fournisseur ADD COLUMN statut_notif character(1);
ALTER TABLE yvs_compta_acompte_fournisseur ALTER COLUMN statut_notif SET DEFAULT 'W'::bpchar;

ALTER TABLE yvs_com_parametre_achat ADD COLUMN generer_facture_auto boolean;
ALTER TABLE yvs_com_parametre_achat ALTER COLUMN generer_facture_auto SET DEFAULT false;

ALTER TABLE yvs_com_doc_achats ADD COLUMN generer_facture_auto boolean;
ALTER TABLE yvs_com_doc_achats ALTER COLUMN generer_facture_auto SET DEFAULT false;

ALTER TABLE yvs_com_parametre_stock ADD COLUMN taille_code_ration integer;
ALTER TABLE yvs_com_parametre_stock ALTER COLUMN taille_code_ration SET DEFAULT 5;

SELECT equilibre_acompte_client(id) FROM yvs_compta_acompte_client;
SELECT equilibre_acompte_fournisseur(id) FROM yvs_compta_acompte_fournisseur;