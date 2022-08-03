
ALTER TABLE yvs_compta_parametre ADD COLUMN jour_anterieur_cancel integer;
ALTER TABLE yvs_compta_parametre ALTER COLUMN jour_anterieur_cancel SET DEFAULT 1;

ALTER TABLE yvs_base_depots ADD COLUMN verify_all_valid_inventaire boolean;
ALTER TABLE yvs_base_depots ALTER COLUMN verify_all_valid_inventaire SET DEFAULT true;