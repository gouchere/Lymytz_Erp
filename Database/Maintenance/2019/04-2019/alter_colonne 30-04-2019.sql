ALTER TABLE yvs_compta_acompte_client ADD COLUMN nature character(1);
ALTER TABLE yvs_compta_acompte_client ALTER COLUMN nature SET DEFAULT 'A'::bpchar;

ALTER TABLE yvs_compta_acompte_fournisseur ADD COLUMN nature character(1);
ALTER TABLE yvs_compta_acompte_fournisseur ALTER COLUMN nature SET DEFAULT 'A'::bpchar;

ALTER TABLE yvs_prod_fiche_conditionnement ADD COLUMN numero character varying;

INSERT INTO yvs_base_element_reference(designation, module, author, model_courant, default_prefix)
    VALUES ('Conditionnement', 'PROD', 16, FALSE, 'RC');