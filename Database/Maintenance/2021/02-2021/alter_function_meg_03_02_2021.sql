DROP FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date);
DROP FUNCTION insert_mouvement_stock(bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying);

ALTER TABLE yvs_base_mouvement_stock DISABLE TRIGGER stock_maj_prix_mvt;

DROP TRIGGER action_contenu_stock_ ON yvs_com_contenu_doc_stock;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_contenu_doc_stock
  FOR EACH ROW
  WHEN (((new.article <> old.article) OR (new.conditionnement <> old.conditionnement) OR (new.quantite <> old.quantite) OR (new.prix <> old.prix) OR (new.qualite <> old.qualite) OR (new.lot_sortie <> old.lot_sortie) OR (new.conditionnement_entree <> old.conditionnement_entree) OR (new.prix_entree <> old.prix_entree) OR (new.qualite_entree <> old.qualite_entree) OR (new.lot_entree <> old.lot_entree) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE insert_contenu_doc_stock();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_com_contenu_doc_stock
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_stock();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_com_contenu_doc_stock
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_stock();
  
  
DROP TRIGGER action_contenu_reception_ ON yvs_com_contenu_doc_stock_reception;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_contenu_doc_stock_reception
  FOR EACH ROW
  WHEN ((new.quantite <> old.quantite))
  EXECUTE PROCEDURE insert_contenu_doc_stock_reception();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_com_contenu_doc_stock_reception
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_stock_reception();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_com_contenu_doc_stock_reception
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_stock_reception();
  
  
DROP TRIGGER action_contenu_achat_ ON yvs_com_contenu_doc_achat;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_contenu_doc_achat
  FOR EACH ROW
  WHEN (((new.quantite_recu <> old.quantite_recu) OR (new.pua_recu <> old.pua_recu) OR (new.article <> old.article) OR (new.conditionnement <> old.conditionnement) OR (new.qualite <> old.qualite) OR (new.lot <> old.lot) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE insert_contenu_doc_achat();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_com_contenu_doc_achat
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_achat();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_com_contenu_doc_achat
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_achat();
  
  
DROP TRIGGER action_contenu_vente_ ON yvs_com_contenu_doc_vente;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_contenu_doc_vente
  FOR EACH ROW
  WHEN (((new.quantite <> old.quantite) OR (new.prix <> old.prix) OR (new.article <> old.article) OR (new.conditionnement <> old.conditionnement) OR (new.qualite <> old.qualite) OR (new.lot <> old.lot) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE insert_contenu_doc_vente();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_com_contenu_doc_vente
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_vente();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_com_contenu_doc_vente
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_vente();
  
  
DROP TRIGGER action_declaration_production_ ON yvs_prod_declaration_production;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_prod_declaration_production
  FOR EACH ROW
  WHEN (((new.quantite <> old.quantite) OR (new.date_declaration <> old.date_declaration) OR (new.conditionnement <> old.conditionnement) OR (new.cout_production <> old.cout_production) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE insert_declaration_production();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_prod_declaration_production
  FOR EACH ROW
  EXECUTE PROCEDURE insert_declaration_production();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_prod_declaration_production
  FOR EACH ROW
  EXECUTE PROCEDURE insert_declaration_production();
  
  
DROP TRIGGER action_on_of_suivi_flux_ ON yvs_prod_of_suivi_flux;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_prod_of_suivi_flux
  FOR EACH ROW
  WHEN (((new.quantite <> old.quantite) OR (new.composant <> old.composant) OR (new.quantite_perdue <> old.quantite_perdue) OR (new.cout <> old.cout) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE insert_flux_composant();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_prod_of_suivi_flux
  FOR EACH ROW
  EXECUTE PROCEDURE insert_flux_composant();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_prod_of_suivi_flux
  FOR EACH ROW
  EXECUTE PROCEDURE insert_flux_composant();
  
  
DROP TRIGGER action_table_ration_ ON yvs_com_ration;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_ration
  FOR EACH ROW
  WHEN (((new.quantite <> old.quantite) OR (new.article <> old.article) OR (new.date_ration <> old.date_ration) OR (new.conditionnement <> old.conditionnement) OR (new.qualite <> old.qualite) OR (new.lot <> old.lot) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE insert_ration();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_com_ration
  FOR EACH ROW
  EXECUTE PROCEDURE insert_ration();
  
CREATE TRIGGER insert
  AFTER INSERT
  ON yvs_com_ration
  FOR EACH ROW
  EXECUTE PROCEDURE insert_ration();
  
  
DROP TRIGGER update_delete_doc_achat_ ON yvs_com_doc_achats;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_doc_achats
  FOR EACH ROW
  WHEN (((new.type_doc IN ('BLA', 'BRA')) OR (new.num_doc <> old.num_doc) OR (new.date_livraison <> old.date_livraison) OR (new.statut <> old.statut) OR (new.tranche <> old.tranche) OR (new.depot_reception <> old.depot_reception) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE update_doc_achats();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_com_doc_achats
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_achats();
  
  
DROP TRIGGER update_delete_doc_ventes_ ON yvs_com_doc_ventes;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_doc_ventes
  FOR EACH ROW
  WHEN (((new.type_doc IN ('BLV', 'BRV')) OR (new.num_doc <> old.num_doc) OR (new.date_livraison <> old.date_livraison) OR (new.statut <> old.statut) OR (new.tranche_livrer <> old.tranche_livrer) OR (new.depot_livrer <> old.depot_livrer) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE update_doc_ventes();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_com_doc_ventes
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_ventes();
  
  
DROP TRIGGER update_delete_doc_stocks ON yvs_com_doc_stocks;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_doc_stocks
  FOR EACH ROW
  WHEN (((new.type_doc <> old.type_doc) OR (new.num_doc <> old.num_doc) OR (new.date_doc <> old.date_doc) OR (new.statut <> old.statut) OR (new.creneau_destinataire <> old.creneau_destinataire) OR (new.creneau_source <> old.creneau_source) OR (new.destination <> old.destination) OR (new.source <> old.source) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE update_doc_stocks();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_com_doc_stocks
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_stocks();
  
  
DROP TRIGGER update_delete_doc_ration_ ON yvs_com_doc_ration;
CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_com_doc_ration
  FOR EACH ROW
  WHEN (((new.num_doc <> old.num_doc) OR (new.date_fiche <> old.date_fiche) OR (new.statut <> old.statut) OR (new.creneau_horaire <> old.creneau_horaire) OR (new.depot <> old.depot) OR (new.execute_trigger = 'OUI' AND new.execute_trigger <> old.execute_trigger)))
  EXECUTE PROCEDURE update_doc_ration();
  
CREATE TRIGGER delete
  AFTER DELETE
  ON yvs_com_doc_ration
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_ration();