ALTER TABLE yvs_grh_contrat_emps ADD COLUMN type_tranche character varying;
ALTER TABLE yvs_grh_contrat_emps ALTER COLUMN type_tranche SET DEFAULT 'JN'::character varying;
ALTER TABLE yvs_parametre_grh ADD COLUMN calcul_planning_dynamique boolean;
ALTER TABLE yvs_parametre_grh ALTER COLUMN calcul_planning_dynamique SET DEFAULT false;
ALTER TABLE yvs_parametre_grh ADD COLUMN time_marge time without time zone;
ALTER TABLE yvs_parametre_grh ALTER COLUMN time_marge SET DEFAULT '01:00:00'::time without time zone;