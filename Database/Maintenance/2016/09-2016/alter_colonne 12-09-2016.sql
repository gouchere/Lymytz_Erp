
ALTER TABLE yvs_com_fiche_approvisionnement ADD COLUMN auto boolean;
ALTER TABLE yvs_com_fiche_approvisionnement ALTER COLUMN auto SET DEFAULT false;