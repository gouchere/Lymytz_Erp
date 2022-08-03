ALTER TABLE yvs_com_parametre_stock ADD COLUMN active_ration boolean DEFAULT true;

ALTER TABLE yvs_base_conditionnement ADD COLUMN by_achat boolean DEFAULT false;
ALTER TABLE yvs_base_conditionnement ADD COLUMN by_prod boolean DEFAULT false;
ALTER TABLE yvs_base_conditionnement ADD COLUMN defaut boolean DEFAULT true;

