UPDATE yvs_com_doc_ration SET creneau_horaire = 32 WHERE depot = 1908 AND creneau_horaire IS NULL;
UPDATE yvs_com_doc_ration SET nbr_jr_usine = 1 WHERE nbr_jr_usine IS NULL;
UPDATE yvs_com_doc_ration SET cloturer = FALSE WHERE cloturer IS NULL;

UPDATE yvs_com_doc_stocks SET creneau_destinataire = 32, creneau_source = 32 WHERE type_doc = 'TR' AND destination = 1908 AND source = 1908 AND creneau_source IS NULL AND creneau_destinataire IS NULL;
UPDATE yvs_com_doc_stocks SET creneau_destinataire = 40, creneau_source = 40 WHERE type_doc = 'TR' AND destination = 1914 AND source = 1914 AND creneau_source IS NULL AND creneau_destinataire IS NULL;
UPDATE yvs_com_doc_stocks SET creneau_destinataire = 35, creneau_source = 35 WHERE type_doc = 'TR' AND destination = 1911 AND source = 1911 AND creneau_source IS NULL AND creneau_destinataire IS NULL;

UPDATE yvs_com_doc_ventes SET tranche_livrer = 11 WHERE depot_livrer = 1914 AND tranche_livrer IS NULL AND type_doc = 'BRV';

UPDATE yvs_prod_declaration_production SET tranche = 21 WHERE depot = 1921 AND tranche IS NULL;
UPDATE yvs_prod_declaration_production SET tranche = 20 WHERE depot = 1922 AND tranche IS NULL;
UPDATE yvs_prod_declaration_production SET cout_production = COALESCE(cout_production, 0) + 0;

UPDATE yvs_prod_of_suivi_flux SET cout = COALESCE(cout, 0) + 0;