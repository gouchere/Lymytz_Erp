
ALTER TABLE yvs_base_point_vente ADD COLUMN validation_reglement boolean;
ALTER TABLE yvs_base_point_vente ALTER COLUMN validation_reglement SET DEFAULT false;

SELECT insert_droit('gescom_change_creneau_use', 'Modifier un créneau employé déjà utilisé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_creno_p'), 16, 'A,B','R');