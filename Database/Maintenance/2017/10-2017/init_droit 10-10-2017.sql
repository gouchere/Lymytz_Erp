INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('gescom_param_ration', 'Parametrage des rations', 'Parametrage des rations', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('gescom_param_obj_vente', 'Parametrage objectifs de vente', 'Parametrage objectifs de vente', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('gescom_plan_remise', 'Plan de remise', 'Plan de remise', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('gescom_reserv', 'Reservations', 'Reservations', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('gescom_recond', 'Reconditionnement', 'Reconditionnement', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('gescom_obj_vente', 'Objectif de vente', 'Objectif de vente', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('gescom_calcul_com', 'Calcul des commissions', 'Calcul des commissions', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('gescom_dashboard_client', 'Tableau de bord des clients', 'Tableau de bord des clients', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('gescom_jorunal_vente', 'Journal de vente', 'Journal de vente', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('gescom_age_client', 'Balance AGE des clients', 'Balance AGE des clients', (SELECT y.id FROM yvs_module y WHERE reference = 'com_'), 16);

INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('base_template_article', 'Template articles', 'Template articles', (SELECT y.id FROM yvs_module y WHERE reference = 'base_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('base_famille_article', 'Famille articles', 'Famille articles', (SELECT y.id FROM yvs_module y WHERE reference = 'base_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('base_groupe_article', 'Groupe articles', 'Groupe articles', (SELECT y.id FROM yvs_module y WHERE reference = 'base_'), 16);

INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('compta_cheq_reg_vente', 'Suivi des chèques de reglement vente', 'Suivi des chèques de reglement vente', (SELECT y.id FROM yvs_module y WHERE reference = 'compta_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('compta_param_g', 'Paramètres généraux', 'Paramètres généraux', (SELECT y.id FROM yvs_module y WHERE reference = 'compta_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('compta_ope_client', 'Opérations sur les clients', 'Opérations sur les clients', (SELECT y.id FROM yvs_module y WHERE reference = 'compta_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('compta_ope_fsseur', 'Opérations sur les fournisseurs', 'Opérations sur les fournisseurs', (SELECT y.id FROM yvs_module y WHERE reference = 'compta_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('compta_rapp_compta', 'Rapports comptables', 'Rapports comptables', (SELECT y.id FROM yvs_module y WHERE reference = 'compta_'), 16);

INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('param_model_form', 'Modèles de formulaire', 'Modèles de formulaire', (SELECT y.id FROM yvs_module y WHERE reference = 'param_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('param_import_export', 'Importation / Exportation', 'Importation / Exportation', (SELECT y.id FROM yvs_module y WHERE reference = 'param_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('param_workflow', 'Workflow', 'Workflow', (SELECT y.id FROM yvs_module y WHERE reference = 'param_'), 16);

INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_param_g', 'Paramètres Généraux', 'Paramètres Généraux', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_mutuelle', 'Gestion de la mutuelle', 'Gestion de la mutuelle', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_mutualiste', 'Gestion des mutualistes', 'Gestion des mutualistes', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_type_credit', 'Types de crédit', 'Types de crédit', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_caisse', 'Gestion des caisses', 'Gestion des caisses', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_eparg_assuranc', 'Epargnes et assurances', 'Epargnes et assurances', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_credit', 'Gestion des crédits', 'Gestion des crédits', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_periode', 'Gestion des periodes', 'Gestion des periodes', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_evenement', 'Gestion des événements', 'Gestion des événements', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_echeancier', 'Gestion de l''écheancier', 'Gestion de l''écheancier', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_avance', 'Gestion des avances', 'Gestion des avances', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_prime', 'Gestion des primes', 'Gestion des primes', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_paie', 'Gestion des paies', 'Gestion des paies', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);
INSERT INTO yvs_page_module(reference, libelle, description, module, author) VALUES 
('mutuel_interet', 'Gestion des interets', 'Gestion des interets', (SELECT y.id FROM yvs_module y WHERE reference = 'mutuel_'), 16);