ALTER TABLE yvs_base_famille_article ADD COLUMN prefixe character varying;
ALTER TABLE yvs_base_conditionnement ADD COLUMN prix_prod double precision;
ALTER TABLE yvs_prod_parametre ADD COLUMN valoriser_by character varying;
ALTER TABLE yvs_prod_parametre ALTER COLUMN valoriser_by SET DEFAULT 'V'::character varying;