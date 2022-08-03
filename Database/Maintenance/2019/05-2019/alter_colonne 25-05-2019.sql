
ALTER TABLE yvs_compta_acompte_client ADD COLUMN repartir_automatique boolean;
ALTER TABLE yvs_compta_acompte_client ALTER COLUMN repartir_automatique SET DEFAULT true;

ALTER TABLE yvs_compta_acompte_fournisseur ADD COLUMN repartir_automatique boolean;
ALTER TABLE yvs_compta_acompte_fournisseur ALTER COLUMN repartir_automatique SET DEFAULT true;

ALTER TABLE yvs_base_unite_mesure ADD COLUMN defaut boolean;
ALTER TABLE yvs_base_unite_mesure ALTER COLUMN defaut SET DEFAULT false;

UPDATE yvs_page_module SET module = (SELECT m.id FROM yvs_module m WHERE m.reference = 'base_') WHERE reference = 'ges_prod_unites';

ALTER TABLE yvs_base_point_vente ADD COLUMN validation_reglement boolean;
ALTER TABLE yvs_base_point_vente ALTER COLUMN validation_reglement SET DEFAULT false;

SELECT insert_droit('gescom_change_creneau_use', 'Modifier un créneau employé déjà utilisé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_creno_p'), 16, 'A,B','R');