ALTER TABLE yvs_com_fiche_approvisionnement ADD COLUMN creneau bigint;
ALTER TABLE yvs_com_fiche_approvisionnement
  ADD CONSTRAINT yvs_com_fiche_approvisionnement_creneau_fkey FOREIGN KEY (creneau)
      REFERENCES yvs_com_creneau_horaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	    

ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN externe bigint;
ALTER TABLE yvs_com_contenu_doc_achat
  ADD CONSTRAINT yvs_com_contenu_doc_achat_externe_fkey FOREIGN KEY (externe)
      REFERENCES yvs_com_article_approvisionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
	  
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN externe bigint;
ALTER TABLE yvs_com_contenu_doc_stock
  ADD CONSTRAINT yvs_com_contenu_doc_stock_externe_fkey FOREIGN KEY (externe)
      REFERENCES yvs_com_article_approvisionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
	  
	  
ALTER TABLE yvs_com_fiche_approvisionnement ADD COLUMN cloturer boolean;
ALTER TABLE yvs_com_fiche_approvisionnement ALTER COLUMN cloturer SET DEFAULT false;
ALTER TABLE yvs_com_fiche_approvisionnement ADD COLUMN date_cloturer date;

ALTER TABLE yvs_base_piece_tresorerie ADD COLUMN avance boolean;
ALTER TABLE yvs_base_piece_tresorerie ALTER COLUMN avance SET DEFAULT false;

ALTER TABLE yvs_grh_pointage ADD COLUMN system_in boolean;
ALTER TABLE yvs_grh_pointage ALTER COLUMN system_in SET DEFAULT false;
ALTER TABLE yvs_grh_pointage ADD COLUMN system_out boolean;
ALTER TABLE yvs_grh_pointage ALTER COLUMN system_out SET DEFAULT false;

update yvs_grh_pointage set system_in = true where heure_entree = (select ((date_debut || ' ' || heure_debut)::timestamp) from yvs_grh_presence where id = presence)
update yvs_grh_pointage set system_out = true where heure_sortie = (select ((date_fin || ' ' || heure_fin)::timestamp) from yvs_grh_presence where id = presence)