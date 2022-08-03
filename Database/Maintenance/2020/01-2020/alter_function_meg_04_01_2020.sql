SELECT set_config('myapp.EXECUTE_MVT_STOCK', 'FALSE', FALSE);
ALTER TABLE yvs_prod_composant_nomenclature DISABLE TRIGGER action_listen_yvs_prod_composant_nomenclature;
ALTER TABLE yvs_prod_composant_of DISABLE TRIGGER action_listen_yvs_prod_composant_of;

UPDATE yvs_prod_composant_nomenclature SET inside_cout = FALSE WHERE type = 'SP';
UPDATE yvs_prod_composant_nomenclature SET inside_cout = TRUE WHERE type != 'SP';

UPDATE yvs_prod_composant_of SET inside_cout = FALSE WHERE type = 'SP';
UPDATE yvs_prod_composant_of SET inside_cout = TRUE WHERE type != 'SP';

ALTER TABLE yvs_prod_composant_nomenclature ENABLE TRIGGER action_listen_yvs_prod_composant_nomenclature;
ALTER TABLE yvs_prod_composant_of ENABLE TRIGGER action_listen_yvs_prod_composant_of;
SELECT set_config('myapp.EXECUTE_MVT_STOCK', 'TRUE', FALSE);