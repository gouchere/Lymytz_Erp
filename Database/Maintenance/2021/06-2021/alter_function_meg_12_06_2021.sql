-- Trigger: update on yvs_com_contenu_doc_stock
DROP TRIGGER update ON yvs_com_contenu_doc_stock;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_contenu_doc_stock
  FOR EACH ROW
  WHEN ((((((((((((COALESCE(new.article, 0) <> COALESCE(old.article, 0)) OR (COALESCE(new.conditionnement, 0) <> COALESCE(old.conditionnement, 0))) OR (COALESCE(new.quantite, 0) <> COALESCE(old.quantite, 0))) OR (COALESCE(new.prix, 0) <> COALESCE(old.prix, 0))) OR (COALESCE(new.qualite, 0) <> COALESCE(old.qualite, 0))) OR (COALESCE(new.lot_sortie, 0) <> COALESCE(old.lot_sortie, 0))) OR (COALESCE(new.conditionnement_entree, 0) <> COALESCE(old.conditionnement_entree, 0))) OR (COALESCE(new.prix_entree, 0) <> COALESCE(old.prix_entree, 0))) OR (COALESCE(new.qualite_entree, 0) <> COALESCE(old.qualite_entree, 0))) OR (COALESCE(new.lot_entree, 0) <> COALESCE(old.lot_entree, 0))) OR ((new.execute_trigger)::text = 'OUI'::text)))
  EXECUTE PROCEDURE insert_contenu_doc_stock();

-- Trigger: update on yvs_com_contenu_doc_achat
DROP TRIGGER update ON yvs_com_contenu_doc_achat;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_contenu_doc_achat
  FOR EACH ROW
  WHEN ((((((((COALESCE(new.quantite_recu, 0) <> COALESCE(old.quantite_recu, 0)) OR (COALESCE(new.pua_recu, 0) <> COALESCE(old.pua_recu, 0))) OR (COALESCE(new.article, 0) <> COALESCE(old.article, 0))) OR (COALESCE(new.conditionnement, 0) <> COALESCE(old.conditionnement, 0))) OR (COALESCE(new.qualite, 0) <> COALESCE(old.qualite, 0))) OR (COALESCE(new.lot, 0) <> COALESCE(old.lot, 0))) OR ((new.execute_trigger)::text = 'OUI'::text)))
  EXECUTE PROCEDURE insert_contenu_doc_achat();
  
-- Trigger: update on yvs_com_contenu_doc_vente
DROP TRIGGER update ON yvs_com_contenu_doc_vente;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_contenu_doc_vente
  FOR EACH ROW
  WHEN ((((((((COALESCE(new.quantite, 0) <> COALESCE(old.quantite, 0)) OR (COALESCE(new.prix, 0) <> COALESCE(old.prix, 0))) OR (COALESCE(new.article, 0) <> COALESCE(old.article, 0))) OR (COALESCE(new.conditionnement, 0) <> COALESCE(old.conditionnement, 0))) OR (COALESCE(new.qualite, 0) <> COALESCE(old.qualite, 0))) OR (COALESCE(new.lot, 0) <> COALESCE(old.lot, 0))) OR ((new.execute_trigger)::text = 'OUI'::text)))
  EXECUTE PROCEDURE insert_contenu_doc_vente();
  
-- Trigger: update on yvs_com_contenu_doc_stock_reception
DROP TRIGGER update ON yvs_com_contenu_doc_stock_reception;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_contenu_doc_stock_reception
  FOR EACH ROW
  WHEN ((COALESCE(new.quantite, 0) <> COALESCE(old.quantite, 0)))
  EXECUTE PROCEDURE insert_contenu_doc_stock_reception();
