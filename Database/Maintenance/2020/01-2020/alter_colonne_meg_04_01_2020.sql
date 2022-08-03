
ALTER TABLE yvs_prod_composant_nomenclature ADD COLUMN inside_cout boolean;
ALTER TABLE yvs_prod_composant_nomenclature ALTER COLUMN inside_cout SET DEFAULT true;

ALTER TABLE yvs_prod_composant_of ADD COLUMN inside_cout boolean;
ALTER TABLE yvs_prod_composant_of ALTER COLUMN inside_cout SET DEFAULT true;

ALTER TABLE yvs_compta_parametre ADD COLUMN ecart_day_solde_client integer;
ALTER TABLE yvs_compta_parametre ALTER COLUMN ecart_day_solde_client SET DEFAULT 7;

ALTER TABLE yvs_compta_parametre ADD COLUMN nombre_ligne_solde_client integer;
ALTER TABLE yvs_compta_parametre ALTER COLUMN nombre_ligne_solde_client SET DEFAULT 4;