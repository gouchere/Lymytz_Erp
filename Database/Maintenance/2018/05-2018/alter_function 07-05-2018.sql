-- Function: update_doc_ventes();
-- DROP FUNCTION update_doc_ventes();

CREATE OR REPLACE FUNCTION update_doc_ventes()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	dep_ record;
	result boolean default false;
BEGIN
	if(NEW.type_doc = 'BLV' or NEW.type_doc = 'BRV') then
		if(NEW.statut = 'V') then
			select into dep_ e.date_entete as date_doc from yvs_com_entete_doc_vente e where e.id = NEW.entete_doc;
			for cont_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
-- 				RAISE NOTICE 'cont_ : %',cont_.id; 
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
				
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article;
						if(NEW.type_doc = 'BLV')then
							result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', NEW.date_livraison));
						else
							result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', NEW.date_livraison));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = (cont_.qte), cout_entree = cont_.prix where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article;
					end if;
				else
					if(NEW.type_doc = 'BLV')then
						result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', NEW.date_livraison));
					else
						result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', NEW.date_livraison));
					end if;
				end if;	
			end loop;
		elsif(NEW.statut != 'V')then
			for cont_ in select id from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
			end loop;
		end if;
	end if;
	if(NEW.type_doc = 'FV' AND NEW.statut_livre = 'L') then
		for cont_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix, id_reservation from yvs_com_contenu_doc_vente where doc_vente = NEW.id
		    LOOP
			--change le statut de la reservation
			IF(cont_.id_reservation IS NOT NULL) THEN
				UPDATE yvs_com_reservation_stock SET statut='T' WHERE id=cont_.id_reservation AND statut='V';
			END IF;
			
		    END LOOP;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ventes()
  OWNER TO postgres;

-- Function: delete_doc_ventes();
-- DROP FUNCTION delete_doc_ventes();

CREATE OR REPLACE FUNCTION delete_doc_ventes()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	dep_ bigint;
BEGIN
	for cont_ in select id from yvs_com_contenu_doc_vente where doc_vente = OLD.id
	loop		
		--Recherche mouvement stock
		for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente'
		loop
			delete from yvs_base_mouvement_stock where id = mouv_;
		end loop;
	end loop;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_doc_ventes()
  OWNER TO postgres;

  
-- Function: insert_contenu_doc_vente();
-- DROP FUNCTION insert_contenu_doc_vente();

CREATE OR REPLACE FUNCTION insert_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	dep_ record;
	arts_ record;
	result boolean default false;
BEGIN
	select into doc_ id, type_doc, statut, entete_doc, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'BRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			if(doc_.depot_livrer is not null)then
				if(doc_.type_doc = 'BLV')then
					result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
				else
					result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
				end if;
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_vente()
  OWNER TO postgres;

  
-- Function: update_contenu_doc_vente();
-- DROP FUNCTION update_contenu_doc_vente();

CREATE OR REPLACE FUNCTION update_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	dep_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'BRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article and mouvement = 'S';
						if(doc_.type_doc = 'BLV')then
							result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
						else
							result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = (NEW.quantite), cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot where id = mouv_;
					end if;
				else
					if(doc_.type_doc = 'BLV')then
						result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
					else
						result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
					end if;
				end if;	
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_vente()
  OWNER TO postgres;
