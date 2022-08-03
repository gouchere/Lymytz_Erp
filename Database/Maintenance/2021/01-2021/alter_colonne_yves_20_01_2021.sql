ALTER TABLE yvs_base_parametre ADD COLUMN display_avis boolean;
ALTER TABLE yvs_base_parametre ADD COLUMN display_caracteristique boolean;
ALTER TABLE yvs_base_parametre ADD COLUMN display_analytique boolean;
ALTER TABLE yvs_base_parametre ADD COLUMN display_fournisseur boolean;
ALTER TABLE yvs_base_parametre ADD COLUMN display_tarif_zone boolean;
ALTER TABLE yvs_base_parametre ADD COLUMN display_equivalent boolean;
ALTER TABLE yvs_base_parametre ADD COLUMN display_production boolean;

CREATE INDEX yvs_synchro_listen_table_id_source_name_table_action_name_idx
  ON yvs_synchro_listen_table
  USING btree
  (id_source, name_table COLLATE pg_catalog."default", action_name COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_stocks_date_doc_idx
  ON yvs_com_doc_stocks
  USING btree
  (date_doc);

DROP INDEX yvs_com_doc_stocks_type_doc;

CREATE INDEX yvs_com_doc_stocks_type_doc
  ON yvs_com_doc_stocks
  USING btree
  (type_doc COLLATE pg_catalog."default");
  
CREATE INDEX yvs_com_doc_stocks_source_idx
  ON yvs_com_doc_stocks
  USING btree
  (source);
 
CREATE INDEX yvs_com_doc_stocks_destination_idx
  ON yvs_com_doc_stocks
  USING btree
  (destination);

CREATE INDEX yvs_com_doc_stocks_societe_idx
  ON yvs_com_doc_stocks
  USING btree
  (societe);
