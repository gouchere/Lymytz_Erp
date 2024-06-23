
SELECT insert_droit('pv_download_article_with_pr', 'Télécharger la liste des articles avec le prix de revient', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_pv'), 16, 'A,B,C','R');
	
SELECT insert_droit('pv_load_pr_article', 'Charger le prix de revient des articles', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_pv'), 16, 'A,B,C','R');