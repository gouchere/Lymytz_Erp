ALTER TABLE yvs_base_conditionnement_point ADD COLUMN actif boolean;
ALTER TABLE yvs_base_conditionnement_point ALTER COLUMN actif  SET DEFAULT true;

UPDATE yvs_base_conditionnement_point SET actif = TRUE;