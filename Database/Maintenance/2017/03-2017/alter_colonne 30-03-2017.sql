ALTER TABLE yvs_com_parametre ADD COLUMN duree_inactiv integer default 0;
ALTER TABLE yvs_com_client ADD COLUMN seuil_solde double precision default 0;
ALTER TABLE yvs_base_fournisseur ADD COLUMN seuil_solde double precision default 0;