ALTER TABLE yvs_base_parametre ADD COLUMN nombre_elt_accueil integer DEFAULT 5;

ALTER TABLE yvs_base_fournisseur ADD COLUMN exclus_for_home boolean default false;
ALTER TABLE yvs_com_client ADD COLUMN exclus_for_home boolean default false;