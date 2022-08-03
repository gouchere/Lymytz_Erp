
ALTER TABLE yvs_com_client ADD COLUMN vente_online boolean;
ALTER TABLE yvs_com_client ALTER COLUMN vente_online SET DEFAULT false;

ALTER TABLE yvs_base_articles ADD COLUMN tags character varying;
