ALTER TABLE yvs_com_doc_achats DISABLE TRIGGER update_;
UPDATE yvs_com_doc_achats y SET agence = (SELECT d.agence FROM yvs_com_doc_achats d WHERE d.id = y.document_lie) WHERE agence IS NULL;
UPDATE yvs_com_doc_achats y SET agence = 2315 WHERE agence IS NULL AND (num_doc like '%TYO%' OR num_doc like '%SAL%');
UPDATE yvs_com_doc_achats y SET agence = 2308 WHERE agence IS NULL AND (num_doc like '%MAG%' OR num_doc like '%DD%');
ALTER TABLE yvs_com_doc_achats ENABLE TRIGGER update_;


ALTER TABLE yvs_base_mouvement_stock ADD COLUMN calcul_pr boolean DEFAULT true;
ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN calcul_pr boolean DEFAULT true;
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN calcul_pr boolean DEFAULT true;
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN calcul_pr boolean DEFAULT true;
ALTER TABLE yvs_com_ration ADD COLUMN calcul_pr boolean DEFAULT true;
ALTER TABLE yvs_prod_of_suivi_flux ADD COLUMN calcul_pr boolean DEFAULT true;
ALTER TABLE yvs_prod_declaration_production ADD COLUMN calcul_pr boolean DEFAULT true;
ALTER TABLE yvs_prod_contenu_conditionnement ADD COLUMN calcul_pr boolean DEFAULT true;

ALTER TABLE yvs_com_comerciale ADD COLUMN defaut boolean DEFAULT false;
ALTER TABLE yvs_com_commercial_vente ADD COLUMN diminue_ca boolean DEFAULT false;
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN date_reception date;
ALTER TABLE yvs_com_contenu_doc_stock DISABLE TRIGGER update_;
UPDATE yvs_com_contenu_doc_stock SET date_reception = (SELECT y.date_reception FROM yvs_com_doc_stocks y WHERE y.id = doc_stock);
ALTER TABLE yvs_com_contenu_doc_stock ENABLE TRIGGER update_;
ALTER TABLE yvs_com_doc_stocks DROP COLUMN date_reception;