DELETE FROM yvs_workflow_alertes WHERE silence IS TRUE;
ALTER TABLE yvs_workflow_alertes DROP COLUMN silence;
ALTER TABLE yvs_base_articles ALTER COLUMN date_last_mvt SET DEFAULT ('now'::text)::date;

