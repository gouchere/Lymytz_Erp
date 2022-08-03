INSERT INTO yvs_base_element_reference(designation, module, model_courant, default_prefix) VALUES ('Conge', 'GRH', TRUE, 'CGE');

UPDATE yvs_print_contrat_employe_header SET titre = model;
UPDATE yvs_print_contrat_employe_header SET model = 'contrat_employe';
