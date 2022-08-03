ALTER TABLE yvs_base_article_depot ADD COLUMN last_date_calcul_pr date;
COMMENT ON COLUMN yvs_base_article_depot.last_date_calcul_pr IS 'Date du dernier recalcul du pr d''un article.
Cette date sert de limite pour le recalcul automatique des pr';
