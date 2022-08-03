-- Trigger: compta_insert_piece_commission on yvs_compta_caisse_piece_commission
-- DROP TRIGGER compta_insert_piece_commission ON yvs_compta_caisse_piece_commission;
CREATE TRIGGER compta_insert_piece_commission
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_caisse_piece_commission
  FOR EACH ROW
  EXECUTE PROCEDURE compta_insert_piece_commission();