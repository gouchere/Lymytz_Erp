CREATE INDEX yvs_prod_of_suivi_flux_date_flux_idx
  ON yvs_prod_of_suivi_flux
  USING btree
  (date_flux);
  
CREATE INDEX yvs_prod_of_suivi_flux_composant_idx
  ON yvs_prod_of_suivi_flux
  USING btree
  (composant);  
  
CREATE INDEX yvs_prod_of_suivi_flux_equipe_idx
  ON yvs_prod_of_suivi_flux
  USING btree
  (equipe);
  
CREATE INDEX yvs_prod_of_suivi_flux_id_operation_idx
  ON yvs_prod_of_suivi_flux
  USING btree
  (id_operation); 
  
CREATE INDEX yvs_prod_of_suivi_flux_tranche_idx
  ON yvs_prod_of_suivi_flux
  USING btree
  (tranche);
    
CREATE INDEX yvs_prod_operations_of_ordre_fabrication_idx
  ON yvs_prod_operations_of
  USING btree
  (ordre_fabrication);    
  
CREATE INDEX yvs_prod_composant_of_ordre_fabrication_idx
  ON yvs_prod_composant_of
  USING btree
  (ordre_fabrication);
 
CREATE INDEX yvs_prod_composant_of_unite_idx
  ON yvs_prod_composant_of
  USING btree
  (unite);

CREATE INDEX yvs_prod_composant_of_article_idx
  ON yvs_prod_composant_of
  USING btree
  (article);
 
CREATE INDEX yvs_prod_composant_of_depot_conso_idx
  ON yvs_prod_composant_of
  USING btree
  (depot_conso);
   
CREATE INDEX yvs_prod_composant_of_lot_sortie_idx
  ON yvs_prod_composant_of
  USING btree
  (lot_sortie);   
  
CREATE INDEX yvs_prod_flux_composant_unite_idx
  ON yvs_prod_flux_composant
  USING btree
  (unite);
    
CREATE INDEX yvs_prod_flux_composant_operation_idx
  ON yvs_prod_flux_composant
  USING btree
  (operation);
     
CREATE INDEX yvs_prod_flux_composant_composant_idx
  ON yvs_prod_flux_composant
  USING btree
  (composant);
      
CREATE INDEX yvs_prod_suivi_operations_operation_of_idx
  ON yvs_prod_suivi_operations
  USING btree
  (operation_of);
       
CREATE INDEX yvs_prod_suivi_operations_session_of_idx
  ON yvs_prod_suivi_operations
  USING btree
  (session_of);
           
CREATE INDEX yvs_prod_session_of_ordre_idx
  ON yvs_prod_session_of
  USING btree
  (ordre);
     
CREATE INDEX yvs_prod_session_of_session_prod_idx
  ON yvs_prod_session_of
  USING btree
  (session_prod);
     
CREATE INDEX yvs_prod_session_prod_depot_idx
  ON yvs_prod_session_prod
  USING btree
  (depot);
       
CREATE INDEX yvs_prod_session_prod_equipe_idx
  ON yvs_prod_session_prod
  USING btree
  (equipe);
        
CREATE INDEX yvs_prod_session_prod_producteur_idx
  ON yvs_prod_session_prod
  USING btree
  (producteur);
         
CREATE INDEX yvs_prod_session_prod_tranche_idx
  ON yvs_prod_session_prod
  USING btree
  (tranche);
          
CREATE INDEX yvs_prod_ordre_fabrication_article_idx
  ON yvs_prod_ordre_fabrication
  USING btree
  (article);
            
CREATE INDEX yvs_prod_ordre_fabrication_gamme_idx
  ON yvs_prod_ordre_fabrication
  USING btree
  (gamme);  
  
CREATE INDEX yvs_prod_ordre_fabrication_nomenclature_idx
  ON yvs_prod_ordre_fabrication
  USING btree
  (nomenclature);
   
CREATE INDEX yvs_prod_ordre_fabrication_site_production_idx
  ON yvs_prod_ordre_fabrication
  USING btree
  (site_production);
     
CREATE INDEX yvs_prod_ordre_fabrication_site_societe_idx
  ON yvs_prod_ordre_fabrication
  USING btree
  (societe);
 
ALTER TABLE yvs_prod_composant_nomenclature ADD COLUMN free_use boolean default false;
ALTER TABLE yvs_prod_composant_of ADD COLUMN free_use boolean default false;


COMMENT ON COLUMN yvs_prod_composant_op.marge_qte IS 'Valeur donnée en pourcentage';
  
 CREATE INDEX yvs_prod_declaration_production_conditionnement_idx
  ON yvs_prod_declaration_production
  USING btree
  (conditionnement);
   
 CREATE INDEX yvs_prod_declaration_production_ordre_idx
  ON yvs_prod_declaration_production
  USING btree
  (ordre);
   
 CREATE INDEX yvs_prod_declaration_production_session_of_idx
  ON yvs_prod_declaration_production
  USING btree
  (session_of);
  
 CREATE INDEX yvs_prod_declaration_production_doc_stock_idx
  ON yvs_prod_declaration_production
  USING btree
  (doc_stock);

DROP TRIGGER update ON yvs_prod_declaration_production;

CREATE TRIGGER update
  AFTER UPDATE
  ON yvs_prod_declaration_production
  FOR EACH ROW
  WHEN (((((new.quantite <> old.quantite) OR (new.conditionnement <> old.conditionnement)) OR (new.cout_production <> old.cout_production)) OR ((new.execute_trigger)::text = 'OUI'::text)))
  EXECUTE PROCEDURE insert_declaration_production();
  
ALTER TABLE yvs_prod_declaration_production DROP COLUMN tranche;
ALTER TABLE yvs_prod_declaration_production DROP COLUMN equipe;
ALTER TABLE yvs_prod_declaration_production DROP COLUMN depot;
ALTER TABLE yvs_prod_declaration_production DROP COLUMN date_declaration; 
  

CREATE INDEX yvs_base_mouvement_stock_id_and_table_externe_idx
  ON yvs_base_mouvement_stock
  USING btree
  (id_externe, table_externe COLLATE pg_catalog."default");
  
CREATE INDEX yvs_prod_composant_nomenclature_unite_idx
  ON yvs_prod_composant_nomenclature
  USING btree
  (unite);
CREATE INDEX yvs_prod_composant_nomenclature_nomenclature_idx
  ON yvs_prod_composant_nomenclature
  USING btree
  (nomenclature);

CREATE INDEX yvs_prod_nomenclature_article_idx
  ON yvs_prod_nomenclature
  USING btree
  (article);
  
CREATE INDEX yvs_prod_nomenclature_unite_mesure_idx
  ON yvs_prod_nomenclature
  USING btree
  (unite_mesure);
