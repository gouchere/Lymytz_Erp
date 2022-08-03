
ALTER TABLE yvs_mut_mutuelle ADD COLUMN retenue_fixe double precision;
ALTER TABLE yvs_mut_mutuelle ALTER COLUMN retenue_fixe SET DEFAULT 0;
COMMENT ON COLUMN yvs_mut_mutuelle.retenue_fixe IS 'Valeur de la retenue fixe pour le fonctionnement de la mutuelle';

ALTER TABLE yvs_mut_type_credit ADD COLUMN coefficient_remboursement double precision;
ALTER TABLE yvs_mut_type_credit ALTER COLUMN coefficient_remboursement SET DEFAULT 0;


DROP TABLE yvs_mut_type_avance;
ALTER TABLE yvs_mut_reglement_mensualite DROP CONSTRAINT yvs_mut_reglement_mensualite_souce_reglement_fkey;
DROP TABLE yvs_mut_condition_avance;
DROP TABLE yvs_mut_reglement_avance;
DROP TABLE yvs_mut_paiement_salaire;
DROP TABLE yvs_mut_periode_salaire;
DROP TABLE yvs_mut_avance_salaire;