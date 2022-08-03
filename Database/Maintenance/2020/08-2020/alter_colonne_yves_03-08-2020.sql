ALTER TABLE yvs_prod_parametre ADD COLUMN valorise_from_of boolean;
ALTER TABLE yvs_prod_parametre ALTER COLUMN valorise_from_of SET DEFAULT true;
COMMENT ON COLUMN yvs_prod_parametre.valorise_from_of IS 'Précise si la société veut valoriser sa production PF au prix de sa nomenclature ou au cout de production';
