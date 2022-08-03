
ALTER TABLE yvs_base_articles ADD COLUMN puv_ttc boolean;
ALTER TABLE yvs_base_articles ALTER COLUMN puv_ttc SET DEFAULT false;