
ALTER TABLE yvs_base_tiers ADD COLUMN responsable character varying;

ALTER TABLE yvs_base_plan_tarifaire_tranche ADD COLUMN puv double precision;
ALTER TABLE yvs_base_plan_tarifaire_tranche ALTER COLUMN puv SET DEFAULT 0;