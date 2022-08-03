ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN statut_declaration character varying;
COMMENT ON COLUMN yvs_prod_ordre_fabrication.statut_declaration IS 'En Attente
EnCours
Termine
';

ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN type_of character varying;
ALTER TABLE yvs_prod_ordre_fabrication ALTER COLUMN type_of SET DEFAULT 'CONTINU'::character varying;

ALTER TABLE yvs_prod_nomenclature ADD COLUMN marge_qte double precision DEFAULT 0;

ALTER TABLE yvs_workflow_etape_validation ADD COLUMN ordre_etape integer;
ALTER TABLE yvs_workflow_etape_validation ALTER COLUMN ordre_etape SET DEFAULT 0;


ALTER TABLE yvs_workflow_autorisation_valid_doc DROP CONSTRAINT yvs_workflow_autorisation_valid_doc_etape_valide_fkey;

ALTER TABLE yvs_workflow_autorisation_valid_doc
  ADD CONSTRAINT yvs_workflow_autorisation_valid_doc_etape_valide_fkey FOREIGN KEY (etape_valide)
      REFERENCES yvs_workflow_etape_validation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

	  
	  ALTER TABLE yvs_workflow_autorisation_valid_doc DROP CONSTRAINT yvs_workflow_autorisation_valid_doc_etape_valide_fkey;

ALTER TABLE yvs_workflow_autorisation_valid_doc
  ADD CONSTRAINT yvs_workflow_autorisation_valid_doc_etape_valide_fkey FOREIGN KEY (etape_valide)
      REFERENCES yvs_workflow_etape_validation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

	  ALTER TABLE yvs_workflow_valid_facture_achat DROP CONSTRAINT yvs_workflow_valid_facture_achat_etape_fkey;

ALTER TABLE yvs_workflow_valid_facture_achat
  ADD CONSTRAINT yvs_workflow_valid_facture_achat_etape_fkey FOREIGN KEY (etape)
      REFERENCES yvs_workflow_etape_validation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;


-- Function: update_fiche_conditionnement()

-- DROP FUNCTION update_fiche_conditionnement();

CREATE OR REPLACE FUNCTION update_fiche_conditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	depot_ bigint;
	article_ record;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		select into cont_ * from yvs_prod_nomenclature where id = NEW.nomenclature;
		select into article_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.unite_mesure;			
		--récupère le dépôt
		SELECT INTO depot_ d.depot FROM yvs_prod_conditionnement_declaration cd INNER JOIN yvs_prod_declaration_production d ON d.id=cd.declaration WHERE cd.conditionnement=NEW.id;
		IF(depot_ IS NULL) THEN
			depot_=NEW.depot;
		END IF;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = 'yvs_prod_fiche_conditionnement' and depot = depot_ and article = cont_.article;
		if(mouv_ is not null)then
			if(article_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = 'yvs_prod_fiche_conditionnement' and depot = depot_ and article = cont_.article;
				result = (select valorisation_stock(cont_.article, depot_, 0, NEW.quantite, article_.prix, 'yvs_prod_fiche_conditionnement', NEW.id, 'S', NEW.date_conditionnement));
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = entree_.prix where id_externe = NEW.id and table_externe = 'yvs_prod_fiche_conditionnement' and depot = depot_ and article = cont_.article and mouvement = 'S';
			end if;
		else
			result = (select valorisation_stock(cont_.article, depot_, 0, NEW.quantite, article_.prix, 'yvs_prod_fiche_conditionnement', NEW.id, 'S', NEW.date_conditionnement));
		end if;	
		
		for cont_ in select id, article , quantite, conditionnement, consommable from yvs_prod_contenu_conditionnement where fiche = OLD.id
		loop
			select into article_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.conditionnement;			
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot =depot_ and article = cont_.article;
			if(mouv_ is not null)then
				if(article_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot = depot_ and article = cont_.article;
					if(cont_.consommable)then
						result = (select valorisation_stock(cont_.article, depot_, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'S', NEW.date_conditionnement));
					else
						result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'E', NEW.date_conditionnement));
					end if;
				else
					if(cont_.consommable)then
						update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = entree_.prix where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot = depot_ and article = cont_.article and mouvement = 'S';
					else
						update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = entree_.prix where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot = NEW.depot and article = cont_.article and mouvement = 'E';
					end if;
				end if;
			else
				if(cont_.consommable)then
					result = (select valorisation_stock(cont_.article, depot_, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'S', NEW.date_conditionnement));
				else
					result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'E', NEW.date_conditionnement));
				end if;
			end if;	
		end loop;
	elsif(NEW.statut != 'V')then
		delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = 'yvs_prod_fiche_conditionnement';
		for cont_ in select id from yvs_prod_contenu_conditionnement where fiche = OLD.id
		loop				
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_fiche_conditionnement()
  OWNER TO postgres;

