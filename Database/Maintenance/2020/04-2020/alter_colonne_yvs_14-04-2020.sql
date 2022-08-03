DROP FUNCTION com_get_versement_attendu(character varying);
DROP FUNCTION com_get_versement_attendu_vendeur(bigint, date, date);
ALTER TABLE yvs_prod_nomenclature ADD COLUMN type_nomenclature character varying;
ALTER TABLE yvs_prod_nomenclature ALTER COLUMN type_nomenclature SET DEFAULT 'P'::character varying;

ALTER TABLE yvs_grh_planning_employe ADD COLUMN repos boolean;
ALTER TABLE yvs_grh_planning_employe ALTER COLUMN repos SET DEFAULT false;
	
ALTER TABLE yvs_users_agence ADD COLUMN user_systeme boolean;
ALTER TABLE yvs_users_agence ALTER COLUMN user_systeme SET DEFAULT false;

ALTER TABLE yvs_com_parametre ADD COLUMN facture_outside_seuil boolean;
COMMENT ON COLUMN yvs_com_parametre.facture_outside_seuil IS 'Decide si l''on peut facturer à un client ayant depasser son seuil';	

SELECT insert_droit('grh_valid_conge', 'Valider un ordre de congé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_conge_'), 16, 'A,B,C','R');	
SELECT insert_droit('grh_valid_mission', 'Valider un ordre de mission', 
	(SELECT id FROM yvs_page_module WHERE reference = 'grh_mission_'), 16, 'A,B,C','R');	
	
SELECT insert_droit('fv_can_save_for_other', 'Enregistrer une facture au nom d''un autre vendeur', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fv'), 16, 'A,B,C','R');	

SELECT insert_droit('fv_can_save_outside_seuil', 'Enregistrer une facture lorsque le client a dépasser son seuil', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_fv'), 16, 'A,B,C','R');
	
	
