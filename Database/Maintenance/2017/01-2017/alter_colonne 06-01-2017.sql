ALTER TABLE yvs_com_lot_reception DROP CONSTRAINT yvs_com_lot_reception_depot_fkey;
UPDATE yvs_com_lot_reception SET depot = NULL;
ALTER TABLE yvs_com_lot_reception RENAME COLUMN depot TO societe;
ALTER TABLE yvs_com_lot_reception
  ADD CONSTRAINT yvs_com_lot_reception_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_critere_lot ADD COLUMN comparable boolean DEFAULT false;
ALTER TABLE yvs_com_lot_reception ADD COLUMN numero character varying;

ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN lot bigint;
ALTER TABLE yvs_com_contenu_doc_vente
  ADD CONSTRAINT yvs_com_contenu_doc_vente_lot_fkey FOREIGN KEY (lot)
      REFERENCES yvs_com_lot_reception (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN lot_entree bigint;
ALTER TABLE yvs_com_contenu_doc_stock
  ADD CONSTRAINT yvs_com_contenu_doc_stock_lot_entree_fkey FOREIGN KEY (lot_entree)
      REFERENCES yvs_com_lot_reception (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN lot_sortie bigint;
ALTER TABLE yvs_com_contenu_doc_stock
  ADD CONSTRAINT yvs_com_contenu_doc_stock_lot_sortie_fkey FOREIGN KEY (lot_sortie)
      REFERENCES yvs_com_lot_reception (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_base_mouvement_stock ADD COLUMN lot bigint;
ALTER TABLE yvs_base_mouvement_stock
  ADD CONSTRAINT yvs_base_mouvement_stock_lot_fkey FOREIGN KEY (lot)
      REFERENCES yvs_com_lot_reception (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_ration ADD COLUMN lot bigint;
ALTER TABLE yvs_com_ration
  ADD CONSTRAINT yvs_com_ration_lot_fkey FOREIGN KEY (lot)
      REFERENCES yvs_com_lot_reception (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;