
ALTER TABLE yvs_base_parametre ADD COLUMN generer_reference_article boolean;
ALTER TABLE yvs_base_parametre ALTER COLUMN generer_reference_article SET DEFAULT false;
ALTER TABLE yvs_base_parametre ADD COLUMN taille_lettre_reference_article integer;
ALTER TABLE yvs_base_parametre ALTER COLUMN taille_lettre_reference_article SET DEFAULT 3;
ALTER TABLE yvs_base_parametre ADD COLUMN taille_numero_reference_article integer;
ALTER TABLE yvs_base_parametre ALTER COLUMN taille_numero_reference_article SET DEFAULT 3;

ALTER TABLE yvs_base_article_depot ADD COLUMN default_pr boolean;
ALTER TABLE yvs_base_article_depot ALTER COLUMN default_pr SET DEFAULT false;
COMMENT ON COLUMN yvs_base_article_depot.default_pr IS 'Propriété unite pour un article. défini quel dépôt nous sert de référence pour obtenir le PR dans les etats de synthèse et statistiques de marge';