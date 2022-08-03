INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('SYNTHESE_CONSO_MP', 'Synthèse consommation MP', 'SYNTHESE');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('ECART_PRODUCTION', 'Ecarts de production (Inventaire)', 'ECART');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('PRODUCTION_VENTE', 'Ecarts prodution vente', 'ECART');
UPDATE yvs_stat_dashboard SET groupe = 'SYNTHESE' WHERE code = 'SYNTHESE_DISTRIBUTION';

SELECT insert_droit('gescom_update_stock_after_valide', 'Passer un mouvement de stock avant un inventaire validé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_inventaire'), 16, '','R');	
