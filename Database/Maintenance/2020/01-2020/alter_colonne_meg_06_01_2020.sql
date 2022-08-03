
ALTER TABLE yvs_synchro_serveurs ADD COLUMN database character varying;
ALTER TABLE yvs_synchro_serveurs ALTER COLUMN database SET DEFAULT 'lymytz_demo_0'::character varying;
ALTER TABLE yvs_synchro_serveurs ADD COLUMN port integer;
ALTER TABLE yvs_synchro_serveurs ALTER COLUMN port SET DEFAULT 5432;
ALTER TABLE yvs_synchro_serveurs ADD COLUMN users character varying;
ALTER TABLE yvs_synchro_serveurs ALTER COLUMN users SET DEFAULT 'postgres'::character varying;
ALTER TABLE yvs_synchro_serveurs ADD COLUMN password character varying;
ALTER TABLE yvs_synchro_serveurs ALTER COLUMN password SET DEFAULT 'yves1910/'::character varying;