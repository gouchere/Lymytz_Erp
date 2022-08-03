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
		for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.destination is not null)then  --traitement de l'entrée dans le dépôt de destination
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
				end if;
				result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
			end if;
			if(NEW.source is not null)then       --traitement de la sortie dans le dépôt source
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				end if;
				result = (select valorisation_stock(cont_.article, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
			end if;
		end loop;
	elsif(NEW.type_doc = 'FT' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;		
	elsif(NEW.type_doc = 'SS' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				end if;
				result = (select valorisation_stock(cont_.article, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
			end if;
		end loop;
	elsif(NEW.type_doc = 'SS' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock				
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	elsif(NEW.type_doc = 'ES' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
				end if;
				result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
			end if;
		end loop;
	elsif(NEW.type_doc = 'ES' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	elsif(NEW.type_doc = 'IN' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
				end if;
				if(cont_.qte>0)then
					result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				elsif(cont_.qte<0)then
					result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;
			end if;
		end loop;
	elsif(NEW.type_doc = 'IN' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	elsif(NEW.type_doc = 'TR' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite, quantite_entree as resultante, prix, prix_entree ,conditionnement as entree, conditionnement_entree as sortie from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			select into entree_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.entree;
			select into sortie_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.sortie;
			
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
			if(mouv_ is not null)then
				if(entree_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
					result = (select valorisation_stock(cont_.article, NEW.source, trancheS_, cont_.quantite, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
					result = (select valorisation_stock(cont_.article, NEW.source, trancheS_, cont_.resultante, cont_.prix_entree, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				else
					update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = cont_.prix where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'E';
					update yvs_base_mouvement_stock set quantite = cont_.resultante, cout_entree = cont_.prix_entree where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				end if;
			else
				result = (select valorisation_stock(cont_.article, NEW.source, trancheS_, cont_.quantite, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				result = (select valorisation_stock(cont_.article, NEW.source, trancheS_, cont_.resultante, cont_.prix_entree, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
			end if;	
		end loop;
	elsif(NEW.type_doc = 'TR' and NEW.statut != 'V') then
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

  
  -- Function: insert_contenu_doc_stock()

-- DROP FUNCTION insert_contenu_doc_stock();

CREATE OR REPLACE FUNCTION insert_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	trancheD_ bigint;
	trancheS_ bigint;
	result boolean default false;
BEGIN
	select into doc_ id, type_doc, date_doc, statut, source, destination, creneau_destinataire, creneau_source from yvs_com_doc_stocks where id = NEW.doc_stock;
	select into trancheD_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = doc_.creneau_source;
	
	if(doc_.statut = 'V') then
		if(doc_.type_doc = 'FT') then
		--Insertion mouvement stock
			if(doc_.destination is not null)then
				result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			end if;
			if(doc_.source is not null)then
				result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'SS') then
			if(doc_.source is not null)then
				result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'ES') then
			if(doc_.destination is not null)then
				result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'IN') then
			if(doc_.source is not null)then
				if(NEW.quantite>0)then
					result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				elsif(NEW.quantite<0)then
					result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;
			end if;
		elsif(doc_.type_doc = 'TR') then
			if(doc_.source is not null)then
				result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_stock()
  OWNER TO postgres;

  
  -- Function: update_contenu_doc_stock()

-- DROP FUNCTION update_contenu_doc_stock();

CREATE OR REPLACE FUNCTION update_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	entree_ record;
	sortie_ record;
	mouv_ bigint;
	trancheD_ bigint;
	trancheS_ bigint;
	result boolean default false;
BEGIN
	select into doc_ id, type_doc, date_doc, statut, source, destination, creneau_destinataire, creneau_source from yvs_com_doc_stocks where id = NEW.doc_stock;
	select into trancheD_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = doc_.creneau_source;
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	--Insertion mouvement stock
	if(doc_.statut = 'V') then
		if(doc_.type_doc = 'FT') then
			if(doc_.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = NEW.article and mouvement = 'E';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = NEW.article and mouvement = 'E';
						result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement where id = mouv_;
					end if;
				else
					result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;	
			end if;

			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
						result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement where id = mouv_;
					end if;
				else
					result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;	
			end if;
		elsif(doc_.type_doc = 'SS') then
			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
						result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement where id = mouv_;
					end if;
				else
					result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;	
			end if;
		elsif(doc_.type_doc = 'ES') then
			if(doc_.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = NEW.article and mouvement = 'E';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = NEW.article and mouvement = 'E';
						result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement where id = mouv_;
					end if;
				else
					result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;	
			end if;
		elsif(doc_.type_doc = 'IN') then
			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
						if(NEW.quantite>0)then
							result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						elsif(NEW.quantite<0)then
							result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement where id = mouv_;
					end if;
				else
					if(NEW.quantite>0)then
						result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					elsif(NEW.quantite<0)then
						result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					end if;
				end if;	
			end if;
		elsif(doc_.type_doc = 'TR') then
			if(doc_.source is not null)then
				select into entree_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement;
				select into sortie_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement_entree;

				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
						result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
						update yvs_base_mouvement_stock set quantite = NEW.quantite_entree, cout_entree = NEW.prix_entree, conditionnement = NEW.conditionnement_entree where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'E';
					end if;
				else
					result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;	
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_stock()
  OWNER TO postgres;

  
  -- Function: delete_doc_stocks()

-- DROP FUNCTION delete_doc_stocks();

CREATE OR REPLACE FUNCTION delete_doc_stocks()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
BEGIN
	if(OLD.statut = 'V') then
		for cont_ in select id from yvs_com_contenu_doc_stock where doc_stock = OLD.id
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
ALTER FUNCTION delete_doc_stocks()
  OWNER TO postgres;
  
  -- Function: insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)

-- DROP FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date);

CREATE OR REPLACE FUNCTION insert_mouvement_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, coutentree_ double precision, cout_ double precision, parent_ bigint, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE
       operation_ character varying default '';
       ligne_ record;
BEGIN
	CASE tableexterne_
		WHEN 'yvs_com_contenu_doc_vente' THEN  
			select into ligne_ qualite, conditionnement from yvs_com_contenu_doc_vente where id = idexterne_;
			operation_='Vente';
		WHEN 'yvs_com_contenu_doc_achat' THEN	
			select into ligne_ qualite, conditionnement from yvs_com_contenu_doc_achat where id = idexterne_;
			operation_='Achat';
		WHEN 'yvs_com_ration' THEN	
			select into ligne_ qualite, conditionnement from yvs_com_ration where id = idexterne_;
			operation_='Ration';
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			select into ligne_ d.type_doc, c.qualite, c.conditionnement FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
			elsif(ligne_.type_doc='TR')then
				if(mouvement_='S') then
					operation_='Sortie';
				else
					select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement from yvs_com_contenu_doc_stock where id = idexterne_;
					operation_='Entrée';
				end if;
			else
				if(mouvement_='S') then
					operation_='Sortie';
				else
					operation_='Entrée';
				end if;
			end if;	
		WHEN 'yvs_com_reconditionnement' THEN	
			if(mouvement_='S') then
				select into ligne_ qualite_source as qualite, unite_source as conditionnement from yvs_com_reconditionnement where id = idexterne_;
				operation_='Sortie';
			else
				select into ligne_ qualite_destination as qualite, unite_destination as conditionnement from yvs_com_reconditionnement where id = idexterne_;
				operation_='Entrée';
			end if;
		ELSE
		  RETURN -1;
	END CASE;
	if(parent_ is not null)then
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, parent_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, parent_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement);
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement, qualite, conditionnement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement, qualite, conditionnement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement);
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date) IS 'Insert une ligne de sortie de mouvement de stock';



-- Function: import_data()

-- DROP FUNCTION import_data();

CREATE OR REPLACE FUNCTION import_data()
  RETURNS boolean AS
$BODY$
DECLARE
    data_ RECORD;
    other_ RECORD;
    id_ bigint default 0;
	entree_ double precision default 0;
	sortie_ double precision default 0;

BEGIN
	FOR data_  IN SELECT * FROM yvs_com_transformation WHERE depot IS NOT NULL
	loop
		INSERT INTO yvs_com_doc_stocks(type_doc, num_doc, source, editeur, description, date_doc, statut, author, date_update, date_save,
		destination, nature, cloturer)
		VALUES ('TR', data_.reference, data_.depot, data_.editeur, data_.description, data_.date_doc, data_.statut, data_.author, data_.date_update, data_.date_save,
		data_.depot, 'RECONDITIONNEMENT', false);

		SELECT INTO id_ id FROM yvs_com_doc_stocks WHERE num_doc = data_.reference;
		    
		FOR other_  IN SELECT * FROM yvs_com_reconditionnement WHERE fiche = data_.id
		loop
			select into sortie_ u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = other_.article and u.id = other_.unite_source;
			select into entree_ u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = other_.article and u.id = other_.unite_destination;
			
			INSERT INTO yvs_com_contenu_doc_stock(article, quantite, conditionnement, conditionnement_entree, quantite_entree, doc_stock, author, date_update, date_save,
			prix, prix_entree, statut, commentaire)
			VALUES (other_.article, other_.quantite, other_.unite_source, other_.unite_destination, other_.resultante, id_, other_.author, other_.date_update, other_.date_save,
			sortie_, entree_, data_.statut, '');
			
		END LOOP;
		DELETE FROM yvs_com_reconditionnement WHERE fiche = data_.id;
	END LOOP;
	DELETE FROM yvs_com_transformation;
	
	DROP TABLE yvs_com_reconditionnement;
	DROP TABLE yvs_com_transformation;
	
	return true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION import_data()
  OWNER TO postgres;
  
 
