SELECT alter_action_colonne_key('yvs_com_facteur_taux', 'yvs_com_periode_validite_commision', true, true);

ALTER TABLE yvs_base_conditionnement ADD COLUMN photo character varying;

ALTER TABLE yvs_com_lot_reception DROP COLUMN depot;
ALTER TABLE yvs_base_article_depot ADD COLUMN requiere_lot boolean DEFAULT false;
	  
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
	  
ALTER TABLE yvs_com_param_ration ADD COLUMN proportionnel boolean;
ALTER TABLE yvs_com_param_ration ALTER COLUMN proportionnel SET DEFAULT false;
ALTER TABLE yvs_com_param_ration ADD COLUMN suspendu boolean;
ALTER TABLE yvs_com_param_ration ALTER COLUMN suspendu SET DEFAULT false;
ALTER TABLE yvs_com_param_ration ADD COLUMN debut_suspension date;
ALTER TABLE yvs_com_param_ration ALTER COLUMN debut_suspension SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_param_ration ADD COLUMN fin_suspension date;
ALTER TABLE yvs_com_param_ration ALTER COLUMN fin_suspension SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_param_ration ADD COLUMN periode integer SET DEFAULT 0;
	  
ALTER TABLE yvs_com_doc_ration ADD COLUMN nbr_jr_usine integer;
ALTER TABLE yvs_com_doc_ration ALTER COLUMN nbr_jr_usine SET DEFAULT 30;
	  
	  
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('ra_suspendre', 'Suspendre ration', 'Suspendre une ration', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_ration'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES ('ra_activer', 'Activer/Désactiver ration', 'Activer/Désactiver une ration', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_ration'), 16);