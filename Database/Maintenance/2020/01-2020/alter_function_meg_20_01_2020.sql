-- Trigger: action_on_creneau_horaire_users on yvs_com_creneau_horaire_users
-- DROP TRIGGER action_on_creneau_horaire_users ON yvs_com_creneau_horaire_users;
CREATE TRIGGER action_on_creneau_horaire_users
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_com_creneau_horaire_users
  FOR EACH ROW
  EXECUTE PROCEDURE com_action_on_creneau_horaire();
  
SELECT insert_droit('gescom_print_without_transfert', 'Télécharger la fiche de stock sans tenir compte des transferts', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_stock_dep'), 16, 'A,B,C','R');

SELECT insert_droit('compta_virement_cancel_pass', 'Annuler les virements dans le passé', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_virement'), 16, 'A,B,C','R');	