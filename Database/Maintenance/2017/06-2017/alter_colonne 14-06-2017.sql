ALTER TABLE yvs_mut_type_credit ADD COLUMN by_fusion boolean;
ALTER TABLE yvs_mut_type_credit ALTER COLUMN by_fusion SET DEFAULT false;