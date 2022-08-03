-- Function: update_doc_ration()
-- DROP FUNCTION update_doc_ration();
CREATE OR REPLACE FUNCTION update_doc_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	doc_ record;
	mouv_ bigint;
	article_ record;
	result_ boolean default false;
	prix_ double precision;
BEGIN
	if(NEW.statut = 'V') then
		SELECT INTO doc_ cd.id, cd.tranche FROM yvs_com_doc_ration d INNER JOIN yvs_com_creneau_depot cd ON cd.id=d.creneau_horaire WHERE d.id=NEW.id;
		for cont_ in select id, article , quantite, conditionnement, date_ration FROM yvs_com_ration r  WHERE r.doc_ration= NEW.id
		loop
			SELECT INTO article_ a.id, a.methode_val, u.prix FROM yvs_base_articles a INNER JOIN yvs_base_conditionnement u on u.article = a.id WHERE a.id = cont_.article and u.id = cont_.conditionnement;
			prix_ = get_pr(cont_.article, NEW.depot, 0, cont_.date_ration, cont_.conditionnement);
			--Insertion mouvement stock
			SELECT INTO mouv_ id FROM yvs_base_mouvement_stock WHERE id_externe = cont_.id and table_externe = 'yvs_com_ration';
			if(mouv_ is not null)then
				if(article_.methode_val = 'FIFO') THEN
					DELETE FROM yvs_base_mouvement_stock WHERE id_externe = cont_.id and table_externe = 'yvs_com_ration';
					result_ = (select valorisation_stock(cont_.article, cont_.conditionnement, NEW.depot, doc_.tranche, cont_.quantite, prix_, 'yvs_com_ration', cont_.id, 'S', cont_.date_ration));
				else
					UPDATE yvs_base_mouvement_stock SET quantite = cont_.quantite, cout_entree = prix_, conditionnement = cont_.conditionnement, tranche = doc_.tranche WHERE id_externe = cont_.id and table_externe = 'yvs_com_ration';
				end if;
			else
				result_= (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot, doc_.tranche, cont_.quantite,prix_, 'yvs_com_ration', cont_.id, 'S', cont_.date_ration));
			end if;	
		end loop;
	elsif(NEW.statut != 'V')then
		for cont_ in select id from yvs_com_ration where doc_ration = OLD.id
		loop				
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_ration';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ration()
  OWNER TO postgres;
  

-- Function: update_doc_stocks()
-- DROP FUNCTION update_doc_stocks();
CREATE OR REPLACE FUNCTION update_doc_stocks()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	entree_ record;
	sortie_ record;
	trancheD_ bigint;
	trancheS_ bigint;
	result boolean default false;
BEGIN
	select into trancheD_ tranche from yvs_com_creneau_depot where id = NEW.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = NEW.creneau_source;	
	if(NEW.type_doc = 'FT' and NEW.statut = 'V') then
		FOR cont_ in select id, article , quantite as qte, quantite_entree, prix, prix_entree, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				--Insertion mouvement stock				
				IF(NEW.source is not null) THEN       --traitement de la sortie dans le dépôt source
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
					end if;
					result = (select valorisation_stock(cont_.article, cont_.conditionnement,NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				END IF;
				if(NEW.destination is not null)then  --traitement de l'entrée dans le dépôt de destination
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
					end if;
					result = (select valorisation_stock(cont_.article,cont_.conditionnement_entree, NEW.destination, trancheD_, cont_.quantite_entree, cont_.prix_entree, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;
			end loop;
	ELSIF(NEW.type_doc = 'FT' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;		
	ELSIF(NEW.type_doc = 'SS' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				end if;
				result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
			end if;
		end loop;
	ELSIF(NEW.type_doc = 'SS' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock				
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'S'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	ELSIF(NEW.type_doc = 'ES' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
				end if;
				result = (select valorisation_stock(cont_.article,cont_.conditionnement_entree, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
			end if;
		end loop;
	ELSIF(NEW.type_doc = 'ES' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	ELSIF(NEW.type_doc = 'IN' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
				end if;
				if(cont_.qte>0)then
					result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				elsif(cont_.qte<0)then
					result = (select valorisation_stock(cont_.article, cont_.conditionnement_entree, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;
			end if;
		end loop;
	ELSIF(NEW.type_doc = 'IN' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	ELSIF(NEW.type_doc = 'TR' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite, quantite_entree as resultante, prix, prix_entree ,conditionnement as entree, conditionnement_entree as sortie from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			select into entree_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.entree;
			select into sortie_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.sortie;
			
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
			if(mouv_ is not null)then
				if(entree_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
					result = (select valorisation_stock(cont_.article,cont_.entree, NEW.source, trancheS_, cont_.quantite, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
					result = (select valorisation_stock(cont_.article,cont_.sortie, NEW.source, trancheS_, cont_.resultante, cont_.prix_entree, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				else
					UPDATE yvs_base_mouvement_stock SET quantite = cont_.quantite, cout_entree = cont_.prix, conditionnement=cont_.sortie, date_doc=NEW.date_doc, tranche = trancheS_
													WHERE id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E';
					UPDATE yvs_base_mouvement_stock SET quantite = cont_.resultante, cout_entree = cont_.prix_entree , conditionnement=cont_.entree, date_doc=NEW.date_doc, tranche = trancheS_
													WHERE id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'S';
				end if;
			else
				result = (select valorisation_stock(cont_.article,cont_.entree, NEW.source, trancheS_, cont_.quantite, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				result = (select valorisation_stock(cont_.article, cont_.sortie, NEW.source, trancheS_, cont_.resultante, cont_.prix_entree, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
			end if;	
		end loop;
	ELSIF(NEW.type_doc = 'TR' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_stocks()
  OWNER TO postgres;


  
-- Function: update_doc_ventes()
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
			for cont_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix, conditionnement from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
-- 				RAISE NOTICE 'cont_ : %',cont_.id; 
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
				
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and article = cont_.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article;
						if(NEW.type_doc = 'BLV')then
							result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', NEW.date_livraison));
						else
							result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', NEW.date_livraison));
						end if;
					else
						UPDATE yvs_base_mouvement_stock SET quantite = (cont_.qte), cout_entree = cont_.prix, conditionnement=cont_.conditionnement, tranche = NEW.tranche_livrer
														WHERE id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and article = cont_.article;
					end if;
				else
					if(NEW.type_doc = 'BLV')then
						result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', NEW.date_livraison));
					else
						result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', NEW.date_livraison));
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
  
  
-- Function: update_contenu_doc_vente()
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
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
						else
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = (NEW.quantite), cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot, date_doc = doc_.date_livraison, tranche = doc_.tranche_livrer where id = mouv_;
					end if;
				else
					if(doc_.type_doc = 'BLV')then
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
					else
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
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


-- Function: insert_declaration_production()
-- DROP FUNCTION insert_declaration_production();
CREATE OR REPLACE FUNCTION insert_declaration_production()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
 	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre;
		--Insertion mouvement stock
		result = (select valorisation_stock(ordre_.article,NEW.conditionnement, NEW.depot, NEW.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_declaration));
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_declaration_production()
  OWNER TO postgres;

  
-- Function: update_declaration_production()
-- DROP FUNCTION update_declaration_production();
CREATE OR REPLACE FUNCTION update_declaration_production()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
 	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where c.id = NEW.conditionnement;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				result = (select valorisation_stock(ordre_.article,NEW.conditionnement, NEW.depot, NEW.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_declaration));
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.cout_production, conditionnement = NEW.conditionnement, tranche = NEW.tranche where id = mouv_;
			end if;
		else
			result = (select valorisation_stock(ordre_.article,NEW.conditionnement, NEW.depot, NEW.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_declaration));
		end if;	
	elsif(NEW.statut != 'V')then
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_declaration_production()
  OWNER TO postgres;
  
  
-- Function: insert_flux_composant()
-- DROP FUNCTION insert_flux_composant();
CREATE OR REPLACE FUNCTION insert_flux_composant()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	operation_ record;
	composant_ record;
	flux_ record;
    conditionnement_ bigint;
	result boolean default false;
BEGIN
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	if(operation_.statut_op = 'R' OR operation_.statut_op = 'T') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		--Insertion mouvement stock
		if(flux_.sens = 'E')then
			result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
		else
			result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_flux_composant()
  OWNER TO postgres;


-- Function: update_flux_composant()
-- DROP FUNCTION update_flux_composant();
CREATE OR REPLACE FUNCTION update_flux_composant()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	operation_ record;
	composant_ record;
	flux_ record;
	mouv_ bigint;
	arts_ record;
    conditionnement_ bigint;
	result boolean default false;
BEGIN
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	IF(operation_.statut_op = 'R' OR operation_.statut_op = 'T') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a where a.id = composant_.article;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				if(flux_.sens = 'E')then
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
				else
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
				end if;
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.cout, conditionnement = composant_.unite, tranche = NEW.tranche where id = mouv_;
			end if;
		else
			if(flux_.sens = 'E')then
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
			else
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
			end if;
		end if;	
	ELSE
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	END IF;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_flux_composant()
  OWNER TO postgres;
  

-- Function: base_legende_classe_statistique(bigint, integer, integer)
-- DROP FUNCTION base_legende_classe_statistique(bigint, integer, integer);
CREATE OR REPLACE FUNCTION base_legende_classe_statistique(IN societe_ bigint, IN offset_ integer, IN limit_ integer)
  RETURNS TABLE(legende CHARACTER VARYING, valeur CHARACTER VARYING, designation CHARACTER VARYING) AS
$BODY$
DECLARE 
	result_ RECORD;

BEGIN
	DROP TABLE IF EXISTS table_legende_classe_statistique;
	CREATE TEMP TABLE IF NOT EXISTS table_legende_classe_statistique(_legende CHARACTER VARYING, _valeur CHARACTER VARYING, _designation CHARACTER VARYING); 
	DELETE FROM table_legende_classe_statistique;
	IF(COALESCE(offset_, 0) = 0)THEN
		INSERT INTO table_legende_classe_statistique VALUES('VALEUR', '0', 'AUTRES');
	END IF;
	FOR result_ IN SELECT y.id, y.designation FROM yvs_base_classes_stat y WHERE y.societe = societe_ OFFSET offset_ LIMIT limit_
	LOOP
		INSERT INTO table_legende_classe_statistique VALUES('VALEUR', result_.id::TEXT, result_.designation);
	END LOOP;
	return QUERY SELECT * FROM table_legende_classe_statistique;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION base_legende_classe_statistique(bigint, integer, integer)
  OWNER TO postgres;

