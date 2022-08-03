ALTER TABLE yvs_base_tiers DROP CONSTRAINT yvs_tiers_comission_fkey;
ALTER TABLE yvs_base_tiers DROP CONSTRAINT yvs_tiers_ristourne_fkey;
ALTER TABLE yvs_base_tiers
  ADD CONSTRAINT yvs_tiers_ristourne_fkey FOREIGN KEY (ristourne)
      REFERENCES yvs_com_plan_ristourne (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_base_tiers
  ADD CONSTRAINT yvs_tiers_comission_fkey FOREIGN KEY (comission)
      REFERENCES yvs_com_plan_commission (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
      
ALTER TABLE yvs_base_tiers_template DROP CONSTRAINT yvs_tiers_template_comission_fkey;
ALTER TABLE yvs_base_tiers_template DROP CONSTRAINT yvs_tiers_template_ristourne_fkey;
ALTER TABLE yvs_base_tiers_template
  ADD CONSTRAINT yvs_tiers_template_ristourne_fkey FOREIGN KEY (ristourne)
      REFERENCES yvs_com_plan_ristourne (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_base_tiers_template
  ADD CONSTRAINT yvs_tiers_template_comission_fkey FOREIGN KEY (comission)
      REFERENCES yvs_com_plan_commission (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
	  
ALTER TABLE yvs_base_tiers DROP COLUMN categorie_tarifaire;
	  
DROP TABLE yvs_adresse_emps;
DROP TABLE yvs_adresse_professionnel;
DROP TABLE yvs_agences_yvs_crenaux_horaire;
DROP TABLE yvs_agences_yvs_depots;
DROP TABLE yvs_article_rayon;
DROP TABLE yvs_articles_recompense;
DROP TABLE yvs_categorie_article;
DROP TABLE yvs_classstatproduit;
DROP TABLE yvs_depot_crenaux;
DROP TABLE yvs_emails_emps;
DROP TABLE yvs_fax;
DROP TABLE yvs_groupe_art_cat_taxes;
DROP TABLE yvs_groupe_article_compte;
DROP TABLE yvs_groupe_produit_depot;
DROP TABLE yvs_historique_conge;
DROP TABLE yvs_historiqueprix;
DROP TABLE yvs_liaison_depots;
DROP TABLE yvs_nature_cout_mission;
DROP TABLE yvs_periode_supplementaire;
DROP TABLE yvs_plan_tarif;
DROP TABLE yvs_plan_tarif_groupe;
DROP TABLE yvs_preferences;
DROP TABLE yvs_prixclient;
DROP TABLE yvs_prixclientgroup;
DROP TABLE yvs_prixfournisseur;
DROP TABLE yvs_prixfournisseurgroup;
DROP TABLE yvs_rayon_secteur;
DROP TABLE yvs_secteur_depot;
DROP TABLE yvs_niveau_depot;
DROP TABLE yvs_telephone_emps;
DROP TABLE yvs_titre_sanction;
DROP TABLE yvs_traite_mdr;
DROP TABLE yvs_plan_de_recompense;
DROP TABLE yvs_cat_tarif;

CREATE INDEX yvs_base_articles_ref_art
  ON yvs_base_articles
  USING btree
  (ref_art COLLATE pg_catalog."default");
  
CREATE INDEX yvs_base_fournisseur_code_fsseur
  ON yvs_base_fournisseur
  USING btree
  (code_fsseur COLLATE pg_catalog."default");
  
CREATE INDEX yvs_base_tiers_code_tiers
  ON yvs_base_tiers
  USING btree
  (code_tiers COLLATE pg_catalog."default");
  
CREATE INDEX yvs_base_plan_comptable_num_compte
  ON yvs_base_plan_comptable
  USING btree
  (num_compte COLLATE pg_catalog."default");
  
CREATE INDEX yvs_base_model_reglement_reference
  ON yvs_base_model_reglement
  USING btree
  (reference COLLATE pg_catalog."default");
  
CREATE INDEX yvs_base_tiers_telephone_numero
  ON yvs_base_tiers_telephone
  USING btree
  (numero COLLATE pg_catalog."default");
  
CREATE INDEX yvs_base_unite_mesure_reference
  ON yvs_base_unite_mesure
  USING btree
  (reference COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_client_code_client
  ON yvs_com_client
  USING btree
  (code_client COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_achats_num_doc
  ON yvs_com_doc_achats
  USING btree
  (num_doc COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_achats_type_doc
  ON yvs_com_doc_achats
  USING btree
  (type_doc COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_ventes_num_doc
  ON yvs_com_doc_ventes
  USING btree
  (num_doc COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_ventes_type_doc
  ON yvs_com_doc_ventes
  USING btree
  (type_doc COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_stocks_num_doc
  ON yvs_com_doc_stocks
  USING btree
  (num_doc COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_stocks_type_doc
  ON yvs_com_doc_stocks
  USING btree
  (type_doc COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_fiche_approvisionnement_reference
  ON yvs_com_fiche_approvisionnement
  USING btree
  (reference COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_ration_num_doc
  ON yvs_com_doc_ration
  USING btree
  (num_doc COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_transformation_reference
  ON yvs_com_transformation
  USING btree
  (reference COLLATE pg_catalog."default");
  
CREATE INDEX yvs_grh_employes_matricule
  ON yvs_grh_employes
  USING btree
  (matricule COLLATE pg_catalog."default");