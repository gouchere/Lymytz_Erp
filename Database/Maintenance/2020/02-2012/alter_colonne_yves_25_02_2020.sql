
ALTER TABLE yvs_prod_composant_nomenclature ADD COLUMN alternatif boolean;
ALTER TABLE yvs_prod_composant_nomenclature ALTER COLUMN alternatif SET DEFAULT false;

ALTER TABLE yvs_prod_nomenclature ADD COLUMN masquer boolean;
ALTER TABLE yvs_prod_nomenclature ALTER COLUMN masquer SET DEFAULT false;
ALTER TABLE yvs_prod_gamme_article ADD COLUMN masquer boolean;
ALTER TABLE yvs_prod_gamme_article ALTER COLUMN masquer SET DEFAULT false;
ALTER TABLE yvs_prod_nomenclature DISABLE TRIGGER action_listen_yvs_prod_nomenclature;
ALTER TABLE yvs_prod_gamme_article DISABLE TRIGGER action_listen_yvs_prod_gamme_article;
update yvs_prod_nomenclature set masquer=false;
update yvs_prod_gamme_article set masquer=false;
ALTER TABLE yvs_prod_nomenclature ENABLE TRIGGER action_listen_yvs_prod_nomenclature;
ALTER TABLE yvs_prod_gamme_article ENABLE TRIGGER action_listen_yvs_prod_gamme_article;

ALTER TABLE yvs_prod_composant_nomenclature DISABLE TRIGGER action_listen_yvs_prod_composant_nomenclature;
UPDATE yvs_prod_composant_nomenclature SET alternatif=false;
ALTER TABLE yvs_prod_composant_nomenclature ENABLE TRIGGER action_listen_yvs_prod_composant_nomenclature;

ALTER TABLE yvs_prod_operations_gamme ADD COLUMN actif boolean;
ALTER TABLE yvs_prod_operations_gamme ALTER COLUMN actif SET DEFAULT true;
