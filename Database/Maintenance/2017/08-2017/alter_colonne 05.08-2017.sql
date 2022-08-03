
ALTER TABLE yvs_base_article_depot ADD COLUMN generer_document boolean;
ALTER TABLE yvs_base_article_depot ALTER COLUMN generer_document SET DEFAULT false;
ALTER TABLE yvs_base_article_depot ADD COLUMN type_document_generer character varying;
ALTER TABLE yvs_base_article_depot ALTER COLUMN type_document_generer SET DEFAULT 'FA'::character varying;

ALTER TABLE yvs_com_doc_stocks ADD COLUMN automatique boolean;
ALTER TABLE yvs_com_doc_stocks ALTER COLUMN automatique SET DEFAULT false;

ALTER TABLE yvs_com_doc_achats ADD COLUMN automatique boolean;
ALTER TABLE yvs_com_doc_achats ALTER COLUMN automatique SET DEFAULT false;