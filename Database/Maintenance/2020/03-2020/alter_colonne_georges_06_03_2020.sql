
ALTER TABLE yvs_base_conditionnement ADD COLUMN marge_min double precision;
ALTER TABLE yvs_base_conditionnement ALTER COLUMN marge_min SET DEFAULT 0;