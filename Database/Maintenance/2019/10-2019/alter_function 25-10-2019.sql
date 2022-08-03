DROP TRIGGER insert_ ON yvs_base_article_emplacement;
DROP TRIGGER update_ ON yvs_base_article_emplacement;
DROP FUNCTION article_emplacement_insert();
DROP FUNCTION article_emplacement_update();

DROP TRIGGER insert_ ON yvs_base_categorie_client;
DROP TRIGGER update_ ON yvs_base_categorie_client;
DROP FUNCTION insert_categorie_client();
DROP FUNCTION update_categorie_client();

DROP TRIGGER insert_ ON yvs_base_emplacement_depot;
DROP TRIGGER update_ ON yvs_base_emplacement_depot;
DROP FUNCTION update_emplacement_depot();

CREATE TRIGGER insert_update_
  BEFORE INSERT OR UPDATE
  ON yvs_base_emplacement_depot
  FOR EACH ROW
  EXECUTE PROCEDURE insert_emplacement_depot();

DROP TRIGGER insert_ ON yvs_base_tiers_telephone;
DROP TRIGGER update_ ON yvs_base_tiers_telephone;
DROP FUNCTION update_tiers_telephone();

CREATE TRIGGER insert_update_
  BEFORE INSERT OR UPDATE
  ON yvs_base_tiers_telephone
  FOR EACH ROW
  EXECUTE PROCEDURE insert_tiers_telephone();
  
  DROP TRIGGER insert ON yvs_com_article_approvisionnement;
  DROP FUNCTION insert_article_approvissionnement();
  
DROP TRIGGER delete_ ON yvs_com_contenu_doc_achat;
DROP TRIGGER insert_ ON yvs_com_contenu_doc_achat;
DROP TRIGGER update_ ON yvs_com_contenu_doc_achat;
DROP FUNCTION delete_contenu_doc_achat();
DROP FUNCTION update_contenu_doc_achat();

CREATE TRIGGER action_contenu_achat_
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_com_contenu_doc_achat
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_achat();
  
 DROP TRIGGER delete_ ON yvs_com_contenu_doc_stock;
 DROP TRIGGER insert_ ON yvs_com_contenu_doc_stock;
 DROP TRIGGER update_ ON yvs_com_contenu_doc_stock;
 DROP FUNCTION delete_contenu_doc_stock();
 DROP FUNCTION update_contenu_doc_stock();
 
 CREATE TRIGGER action_contenu_stock_
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_com_contenu_doc_stock
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_stock();
  
  DROP TRIGGER delete_ ON yvs_com_contenu_doc_stock_reception;
  DROP TRIGGER insert_ ON yvs_com_contenu_doc_stock_reception;
  DROP TRIGGER update_ ON yvs_com_contenu_doc_stock_reception;
  DROP FUNCTION delete_contenu_doc_stock_reception();
  DROP FUNCTION update_contenu_doc_stock_reception();
  
  CREATE TRIGGER action_contenu_reception_
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_com_contenu_doc_stock_reception
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_stock_reception();
  
  DROP TRIGGER delete_ ON yvs_com_contenu_doc_vente;
  DROP TRIGGER insert_ ON yvs_com_contenu_doc_vente;
  DROP TRIGGER update_ ON yvs_com_contenu_doc_vente;
  DROP FUNCTION delete_contenu_doc_vente();
  DROP FUNCTION update_contenu_doc_vente();
  
  CREATE TRIGGER action_contenu_vente_
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_com_contenu_doc_vente
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_vente();
  
  DROP TRIGGER delete_ ON yvs_com_doc_achats;
  DROP TRIGGER update_ ON yvs_com_doc_achats;
  DROP FUNCTION delete_doc_achats();
  
  CREATE TRIGGER update_delete_doc_achat_
  BEFORE UPDATE OR DELETE
  ON yvs_com_doc_achats
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_achats();
  
  DROP TRIGGER delete_ ON yvs_com_doc_ration;
  DROP TRIGGER update_ ON yvs_com_doc_ration;
  DROP FUNCTION delete_doc_ration();
  
  CREATE TRIGGER update_delete_doc_ration_
  BEFORE UPDATE OR DELETE
  ON yvs_com_doc_ration
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_ration();
  
  DROP TRIGGER delete_ ON yvs_com_doc_stocks;
  DROP TRIGGER update_ ON yvs_com_doc_stocks;
  DROP FUNCTION delete_doc_stocks();
  
  CREATE TRIGGER update_delete_doc_stocks
  BEFORE UPDATE OR DELETE
  ON yvs_com_doc_stocks
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_stocks();
  
  DROP TRIGGER delete_ ON yvs_com_doc_ventes;
  DROP TRIGGER update_ ON yvs_com_doc_ventes;
  DROP FUNCTION delete_doc_ventes();
  
  CREATE TRIGGER update_delete_doc_ventes_
  BEFORE UPDATE OR DELETE
  ON yvs_com_doc_ventes
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_ventes();
  
  DROP TRIGGER delete_ ON yvs_com_ration;
  DROP TRIGGER update_ ON yvs_com_ration;
  DROP TRIGGER insert_ ON yvs_com_ration;
  DROP FUNCTION delete_ration();
  DROP FUNCTION update_ration();
  
  CREATE TRIGGER action_table_ration_
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_com_ration
  FOR EACH ROW
  EXECUTE PROCEDURE insert_ration();

  DROP TRIGGER insert_new_retenue ON yvs_grh_element_additionel;
  DROP FUNCTION grh_salaire_insert_new_retenue();
  
  DROP TRIGGER delete_pointage ON yvs_grh_pointage;
  DROP FUNCTION delete_pointage();
  DROP TRIGGER update_pointage ON yvs_grh_pointage;
  DROP FUNCTION update_pointage();
  DROP TRIGGER insert_pointage ON yvs_grh_pointage;
  
  CREATE TRIGGER action_on_pointage_
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_grh_pointage
  FOR EACH ROW
  EXECUTE PROCEDURE insert_pointage();
  
  DROP TRIGGER delete_ ON yvs_prod_composant_of;
  DROP TRIGGER insert_ ON yvs_prod_composant_of;
  DROP TRIGGER update_ ON yvs_prod_composant_of;
  DROP FUNCTION delete_composant_of();
  DROP FUNCTION update_composant_of();
  
  CREATE TRIGGER action_composant_of_
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_prod_composant_of
  FOR EACH ROW
  EXECUTE PROCEDURE insert_composant_of();
  
  DROP TRIGGER delete_ ON yvs_prod_contenu_conditionnement;
  DROP TRIGGER insert_ ON yvs_prod_contenu_conditionnement;
  DROP TRIGGER update_ ON yvs_prod_contenu_conditionnement;
  DROP FUNCTION delete_contenu_conditionnement();
  DROP FUNCTION update_contenu_conditionnement();
  
  CREATE TRIGGER action_contenu_conditionnement_
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_prod_contenu_conditionnement
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_conditionnement();
  
  DROP TRIGGER delete_ ON yvs_prod_declaration_production;
  DROP TRIGGER insert_ ON yvs_prod_declaration_production;
  DROP TRIGGER update_ ON yvs_prod_declaration_production;
  DROP FUNCTION delete_declaration_production();
  DROP FUNCTION update_declaration_production();
  
  CREATE TRIGGER action_declaration_production_
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_prod_declaration_production
  FOR EACH ROW
  EXECUTE PROCEDURE insert_declaration_production();

  DROP TRIGGER delete_ ON yvs_prod_fiche_conditionnement;
  DROP FUNCTION delete_fiche_conditionnement();
  DROP TRIGGER update_ ON yvs_prod_fiche_conditionnement;

  CREATE TRIGGER action_on_fiche_conditionnement_
  BEFORE UPDATE OR DELETE
  ON yvs_prod_fiche_conditionnement
  FOR EACH ROW
  EXECUTE PROCEDURE update_fiche_conditionnement();
  
  DROP TRIGGER delete_ ON yvs_prod_of_suivi_flux;
  DROP TRIGGER insert_ ON yvs_prod_of_suivi_flux;
  DROP TRIGGER update_ ON yvs_prod_of_suivi_flux;
  DROP FUNCTION delete_flux_composant();
  DROP FUNCTION update_flux_composant();
  
CREATE TRIGGER action_on_of_suivi_flux_
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_prod_of_suivi_flux
  FOR EACH ROW
  EXECUTE PROCEDURE insert_flux_composant();






