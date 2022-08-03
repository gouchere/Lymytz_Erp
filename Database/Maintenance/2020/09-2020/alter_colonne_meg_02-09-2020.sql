ALTER TABLE yvs_base_point_vente ADD COLUMN type character DEFAULT 'C';
COMMENT ON COLUMN yvs_base_point_vente.type IS 'C--Commerce Général , R--Restaurant/Snack-Bar';
