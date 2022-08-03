DROP TRIGGER compta_action_on_piece_achat ON yvs_compta_caisse_piece_achat;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_compta_caisse_piece_achat
  FOR EACH ROW
  WHEN (((new.numero_piece <> old.numero_piece) OR (new.montant <> old.montant) OR (new.statut_piece <> old.statut_piece) OR (new.caisse <> old.caisse) OR (new.date_piece <> old.date_piece) OR (new.date_paiement <> old.date_paiement) OR (new.date_paiment_prevu <> old.date_paiment_prevu) OR (new.model <> old.model) OR (new.caissier <> old.caissier) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE compta_action_on_piece_caisse_achat();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_compta_caisse_piece_achat
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_achat();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_compta_caisse_piece_achat
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_achat();
  
  
DROP TRIGGER compta_insert_piece_commission ON yvs_compta_caisse_piece_commission;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_compta_caisse_piece_commission
  FOR EACH ROW
  WHEN (((new.numero_piece <> old.numero_piece) OR (new.montant <> old.montant) OR (new.statut_piece <> old.statut_piece) OR (new.caisse <> old.caisse) OR (new.date_piece <> old.date_piece) OR (new.date_paiement <> old.date_paiement) OR (new.date_paiment_prevu <> old.date_paiment_prevu) OR (new.model <> old.model) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE compta_insert_piece_commission();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_compta_caisse_piece_commission
  FOR EACH ROW
  EXECUTE PROCEDURE compta_insert_piece_commission();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_compta_caisse_piece_commission
  FOR EACH ROW
  EXECUTE PROCEDURE compta_insert_piece_commission();
  
  
DROP TRIGGER compta_action_on_piece_divers ON yvs_compta_caisse_piece_divers;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_compta_caisse_piece_divers
  FOR EACH ROW
  WHEN (((new.num_piece <> old.num_piece) OR (new.montant <> old.montant) OR (new.statut_piece <> old.statut_piece) OR (new.caisse <> old.caisse) OR (new.date_piece <> old.date_piece) OR (new.date_valider <> old.date_valider) OR (new.date_paiment_prevu <> old.date_paiment_prevu) OR (new.mode_paiement <> old.mode_paiement) OR (new.mouvement <> old.mouvement) OR (new.caissier <> old.caissier) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE compta_action_on_piece_caisse_divers();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_compta_caisse_piece_divers
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_divers();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_compta_caisse_piece_divers
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_divers();
  
  
DROP TRIGGER compta_action_on_caisse_piece_ecart_vente ON yvs_compta_caisse_piece_ecart_vente;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_compta_caisse_piece_ecart_vente
  FOR EACH ROW
  WHEN (((new.caisse <> old.caisse) OR (new.model <> old.model) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE compta_action_on_caisse_piece_ecart_vente();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_compta_caisse_piece_ecart_vente
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_caisse_piece_ecart_vente();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_compta_caisse_piece_ecart_vente
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_caisse_piece_ecart_vente();
  
  
DROP TRIGGER compta_insert_new_piece_mission ON yvs_compta_caisse_piece_mission;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_compta_caisse_piece_mission
  FOR EACH ROW
  WHEN (((new.numero_piece <> old.numero_piece) OR (new.montant <> old.montant) OR (new.statut_piece <> old.statut_piece) OR (new.caisse <> old.caisse) OR (new.date_piece <> old.date_piece) OR (new.date_paiement <> old.date_paiement) OR (new.date_paiment_prevu <> old.date_paiment_prevu) OR (new.model <> old.model) OR (new.caissier <> old.caissier) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE compta_insert_piece_mission();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_compta_caisse_piece_mission
  FOR EACH ROW
  EXECUTE PROCEDURE compta_insert_piece_mission();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_compta_caisse_piece_mission
  FOR EACH ROW
  EXECUTE PROCEDURE compta_insert_piece_mission();
  
  
DROP TRIGGER compta_action_on_piece_salaire ON yvs_compta_caisse_piece_salaire;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_compta_caisse_piece_salaire
  FOR EACH ROW
  WHEN (((new.numero_piece <> old.numero_piece) OR (new.montant <> old.montant) OR (new.statut_piece <> old.statut_piece) OR (new.caisse <> old.caisse) OR (new.date_piece <> old.date_piece) OR (new.date_paiement <> old.date_paiement) OR (new.date_paiment_prevu <> old.date_paiment_prevu) OR (new.model <> old.model) OR (new.caissier <> old.caissier) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE compta_action_on_piece_caisse_salaire();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_compta_caisse_piece_salaire
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_salaire();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_compta_caisse_piece_salaire
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_salaire();
  
  
DROP TRIGGER compta_action_on_piece_vente ON yvs_compta_caisse_piece_vente;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_compta_caisse_piece_vente
  FOR EACH ROW
  WHEN (((new.numero_piece <> old.numero_piece) OR (new.montant <> old.montant) OR (new.statut_piece <> old.statut_piece) OR (new.caisse <> old.caisse) OR (new.date_piece <> old.date_piece) OR (new.date_paiement <> old.date_paiement) OR (new.date_paiment_prevu <> old.date_paiment_prevu) OR (new.model <> old.model) OR (new.caissier <> old.caissier) OR (new.mouvement <> old.mouvement) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE compta_action_on_piece_caisse_vente();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_compta_caisse_piece_vente
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_vente();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_compta_caisse_piece_vente
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_vente();
  
  
DROP TRIGGER compta_action_on_piece_virement ON yvs_compta_caisse_piece_virement;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_compta_caisse_piece_virement
  FOR EACH ROW
  WHEN (((new.numero_piece <> old.numero_piece) OR (new.montant <> old.montant) OR (new.statut_piece <> old.statut_piece) OR (new.source <> old.source) OR (new.cible <> old.cible) OR (new.date_piece <> old.date_piece) OR (new.date_paiement <> old.date_paiement) OR (new.date_paiment_prevu <> old.date_paiment_prevu) OR (new.model <> old.model) OR (new.caissier_source <> old.caissier_source) OR (new.caissier_cible <> old.caissier_cible) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE compta_action_on_piece_caisse_virement();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_compta_caisse_piece_virement
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_virement();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_compta_caisse_piece_virement
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_virement();