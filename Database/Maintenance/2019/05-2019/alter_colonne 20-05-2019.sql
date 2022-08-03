ALTER TABLE yvs_com_doc_stocks ADD COLUMN date_reception DATE;
ALTER TABLE yvs_com_doc_stocks DISABLE TRIGGER update_;
UPDATE yvs_com_doc_stocks SET date_reception = date_doc;
ALTER TABLE yvs_com_doc_stocks ENABLE TRIGGER update_;

ALTER TABLE yvs_com_parametre ADD COLUMN converter_cs integer DEFAULT 2;

UPDATE yvs_compta_caisse_piece_achat SET statut_piece = 'W' WHERE statut_piece = 'A';

CREATE INDEX yvs_grh_parametres_bulletin_code_element ON yvs_grh_parametres_bulletin USING btree (code_element COLLATE pg_catalog."default");  

CREATE INDEX yvs_com_doc_ventes_nom_client ON yvs_com_doc_ventes USING btree (nom_client COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_client_nom ON yvs_com_client USING btree(nom COLLATE pg_catalog."default");  
CREATE INDEX yvs_com_client_prenom ON yvs_com_client USING btree (prenom COLLATE pg_catalog."default");
  
CREATE INDEX yvs_base_tiers_nom ON yvs_base_tiers USING btree (nom COLLATE pg_catalog."default");
CREATE INDEX yvs_base_tiers_prenom ON yvs_base_tiers USING btree (prenom COLLATE pg_catalog."default");
  
CREATE INDEX yvs_base_fournisseur_nom ON yvs_base_fournisseur USING btree (nom COLLATE pg_catalog."default");
CREATE INDEX yvs_base_fournisseur_prenom ON yvs_base_fournisseur USING btree (prenom COLLATE pg_catalog."default");