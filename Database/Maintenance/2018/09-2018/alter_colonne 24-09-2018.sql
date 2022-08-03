ALTER TABLE yvs_base_nature_compte ADD COLUMN nature character varying;
ALTER TABLE yvs_base_nature_compte ALTER COLUMN nature SET DEFAULT 'AUTRE'::character varying;