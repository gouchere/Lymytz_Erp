ALTER TABLE yvs_base_article_depot ADD COLUMN marg_stock_moyen double precision;
ALTER TABLE yvs_base_article_depot ALTER COLUMN marg_stock_moyen SET DEFAULT 0;
ALTER TABLE yvs_base_article_depot ADD COLUMN stock_net double precision;
ALTER TABLE yvs_base_article_depot ALTER COLUMN stock_net SET DEFAULT -1;