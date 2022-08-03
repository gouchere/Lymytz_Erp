
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN pr double precision;
ALTER TABLE yvs_com_contenu_doc_vente ALTER COLUMN pr SET DEFAULT 0;

ALTER TABLE yvs_base_article_point ADD COLUMN nature_prix_min character varying;
ALTER TABLE yvs_base_article_point ALTER COLUMN nature_prix_min SET DEFAULT 'MONTANT'::character varying;

ALTER TABLE yvs_base_article_depot ADD COLUMN nature_prix_min character varying;
ALTER TABLE yvs_base_article_depot ALTER COLUMN nature_prix_min SET DEFAULT 'MONTANT'::character varying;

ALTER TABLE yvs_base_plan_tarifaire ADD COLUMN nature_prix_min character varying;
ALTER TABLE yvs_base_plan_tarifaire ALTER COLUMN nature_prix_min SET DEFAULT 'MONTANT'::character varying;

ALTER TABLE yvs_base_articles ADD COLUMN nature_prix_min character varying;
ALTER TABLE yvs_base_articles ALTER COLUMN nature_prix_min SET DEFAULT 'MONTANT'::character varying;