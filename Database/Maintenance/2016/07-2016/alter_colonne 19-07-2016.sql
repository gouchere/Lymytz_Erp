
ALTER TABLE yvs_base_article_depot ADD COLUMN change_prix boolean;
ALTER TABLE yvs_base_article_depot ALTER COLUMN change_prix SET DEFAULT false;