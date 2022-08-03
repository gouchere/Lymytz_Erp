DROP TRIGGER insert_ ON yvs_com_creneau_horaire_users;

-- Function: get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean)

-- DROP FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean);

CREATE OR REPLACE FUNCTION get_puv(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, depot_ bigint, point_ bigint, date_ date, min_ boolean)
  RETURNS double precision AS
$BODY$
DECLARE
	puv_ double precision;
	garde_ double precision;
	data_ record;
	tarif_ record;
	valeur_ double precision default 0;
	pr_  double precision default 0;

BEGIN
	valeur_ = qte_ * prix_;
	pr_ = (select get_pr(article_, depot_, 0 ,date_));
	
	-- Recherche du prix du point de vente
	if(point_ is not null and point_ > 0)then
		select into tarif_ * from yvs_base_article_point where article = article_ and point = point_ limit 1;
		if(tarif_.id is not null)then
			if(tarif_.prioritaire is true)then --Verification si ce prix est prioritaire
				if(min_)then
					if(tarif_.nature_prix_min = 'TAUX')then
						puv_ = (pr_ * tarif_.puv_min) /100;
					else
						puv_ = tarif_.puv_min;
					end if;
				else
					puv_ = tarif_.puv;
				end if;
			end if;
		end if;
	end if;
	
	if(puv_ is null or puv_ < 1)then
		-- Recherche du prix en fonction de la catégorie tarifaire
		if(client_ is not null and client_ > 0)then
			for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
			loop
				if(data_.permanent is false)then
					if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
						select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
						if(tarif_.id is not null)then
							if(min_)then
								if(tarif_.nature_prix_min = 'TAUX')then
									puv_ = (pr_ * tarif_.puv_min) /100;
								else
									puv_ = tarif_.puv_min;
								end if;
							else
								garde_ = tarif_.puv;
								select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
								if(tarif_.id IS NOT NULL)then
									puv_ = tarif_.puv;
								end if;
								if(puv_ is null or puv_ < 1)then
									puv_ = garde_;
								end if;
							end if;
						end if;
						exit;
					end if;
				else
					select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
					if(tarif_.id is not null)then
						if(min_)then
							if(tarif_.nature_prix_min = 'TAUX')then
								puv_ = (pr_ * tarif_.puv_min) /100;
							else
								puv_ = tarif_.puv_min;
							end if;
						else
							garde_ = tarif_.puv;
							select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
							if(tarif_.id IS NOT NULL)then
								puv_ = tarif_.puv;
							end if;
							if(puv_ is null or puv_ < 1)then
								puv_ = garde_;
							end if;
						end if;
					end if;
					exit;
				end if;
			end loop;
		end if;
		
		if(puv_ is null or puv_ < 1)then
			-- Recherche du prix du point de vente non prioritaire
			if(point_ is not null and point_ > 0)then
				select into tarif_ * from yvs_base_article_point where article = article_ and point = point_ limit 1;
				if(tarif_.id is not null)then
					if(min_)then
						if(tarif_.nature_prix_min = 'TAUX')then
							puv_ = (pr_ * tarif_.puv_min) /100;
						else
							puv_ = tarif_.puv_min;
						end if;
					else
						puv_ = tarif_.puv;
					end if;
				end if;
			end if;
			
			if(puv_ is null or puv_ < 1)then					
				-- Recherche du prix de l'article sur la fiche d'article
				if(puv_ is null or puv_ <1)then
					select into tarif_ * from yvs_base_articles where id = article_; 
					if(tarif_.id is not null)then
						if(min_)then
							if(tarif_.nature_prix_min = 'TAUX')then
								puv_ = (pr_ * tarif_.prix_min) /100;
							else
								puv_ = tarif_.prix_min;
							end if;
						else
							puv_ = tarif_.puv;
						end if;
					end if;
				end if;
			end if;
		end if;
	end if;
						
	if(puv_ is null or puv_ <1)then
		puv_ = 0;
	end if;
	return puv_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean) IS 'retourne le prix de vente d'' article';


-- Function: delete_doc_ventes()

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
	if((OLD.type_doc = 'FV' or OLD.type_doc = 'BLV') and OLD.statut = 'VALIDE') then
		select into dep_ h.depot from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
			on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
			where e.id = OLD.entete_doc;
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
		loop
			
			--Recherche mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = OLD.depot_livrer and article = cont_.article and mouvement = 'S';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_doc_ventes()
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
	if(NEW.type_doc = 'BLV' or NEW.type_doc = 'FRV' and OLD.mouv_stock = true) then
		if(NEW.statut = 'VALIDE' and 'REGLE' != OLD.statut) then
			select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
				on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
				where e.id = NEW.entete_doc;
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
-- 						RAISE NOTICE 'cont_ : %',cont_.id; 
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
				
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						if(NEW.type_doc = 'BLV')then
							result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
						else
							result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', dep_.date_doc));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					if(NEW.type_doc = 'BLV')then
						result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
					else
						result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', dep_.date_doc));
					end if;
				end if;	
			end loop;
		elsif(OLD.statut = 'VALIDE' and (NEW.statut = 'EDITABLE' or NEW.statut = 'ANNULE'))then
			select into dep_ h.depot as depot, h.tranche as tranche from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
			on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
			where e.id = OLD.entete_doc;
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
			end loop;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ventes()
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
	trancheD_ bigint;
	trancheS_ bigint;
	result boolean default false;
BEGIN
	select into trancheD_ tranche from yvs_com_creneau_depot where id = NEW.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = NEW.creneau_source;
	
	if(NEW.statut != OLD.statut) then
		if(NEW.type_doc = 'FT' and NEW.statut = 'VALIDE') then
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				
				
				--Insertion mouvement stock
				if(NEW.destination is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;

				if(NEW.source is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(cont_.article, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				end if;
			end loop;
		elsif(NEW.type_doc = 'FT' and NEW.statut != 'VALIDE') then
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop			
				
				--Recherche mouvement stock
				for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
				loop
					delete from yvs_base_mouvement_stock where id = mouv_;
				end loop;
			end loop;		
		elsif(NEW.type_doc = 'SS' and NEW.statut = 'VALIDE') then
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				
				
				--Insertion mouvement stock
				if(NEW.source is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(cont_.article, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				end if;
			end loop;
		elsif(NEW.type_doc = 'SS' and NEW.statut != 'VALIDE') then
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop			
				
				--Recherche mouvement stock				
				for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E'
				loop
					delete from yvs_base_mouvement_stock where id = mouv_;
				end loop;
			end loop;
		elsif(NEW.type_doc = 'ES' and NEW.statut = 'VALIDE') then
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				
				
				--Insertion mouvement stock
				if(NEW.destination is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;
			end loop;
		elsif(NEW.type_doc = 'ES' and NEW.statut != 'VALIDE') then
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop			
				
				--Recherche mouvement stock
				for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E'
				loop
					delete from yvs_base_mouvement_stock where id = mouv_;
				end loop;
			end loop;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_stocks()
  OWNER TO postgres;

  
  -- Function: insert_contenu_doc_vente()

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
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE')then
			if(doc_.depot_livrer is not null)then
				if(doc_.type_doc = 'BLV')then
					result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
				else
					result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', dep_.date_doc));
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
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE')then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						if(doc_.type_doc = 'BLV')then
							result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
						else
							result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', dep_.date_doc));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					if(doc_.type_doc = 'BLV')then
						result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
					else
						result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', dep_.date_doc));
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
	
	if(doc_.statut = 'VALIDE') then
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
	if(doc_.statut = 'VALIDE') then
		if(doc_.type_doc = 'FT') then
			if(doc_.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = NEW.article and mouvement = 'E';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;	
			end if;

			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME, NEW.id||'', NEW.id, 'S', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
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
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
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
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
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
  
  -- Function: get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	  stock_ double precision default 0;
	 
BEGIN
	if(depot_ is not null and depot_ >0)then
		if(tranche_ is not null and tranche_ > 0)then
			select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
				inner join yvs_com_creneau_depot_users u on e.creneau = u.id inner join yvs_com_creneau_depot h on u.creneau = h.id
				where d.statut_livre != 'L' and d.consigner = true and d.depot_livrer = depot_ and c.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
		else
			select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
				inner join yvs_com_creneau_depot_users u on e.creneau = u.id inner join yvs_com_creneau_depot h on u.creneau = h.id 
				where d.statut_livre != 'L' and d.consigner = true and d.depot_livrer = depot_ and c.article = article_ and d.date_livraison <= date_;
		end if;
	else
		if(agence_ is not null and agence_ >0)then
			if(tranche_ is not null and tranche_ > 0)then
				select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
					inner join yvs_com_creneau_depot_users u on e.creneau = u.id inner join yvs_com_creneau_depot h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
					where d.statut_livre != 'L' and d.consigner = true and p.agence = agence_ and c.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
			else
				select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
					inner join yvs_com_creneau_depot_users u on e.creneau = u.id inner join yvs_com_creneau_depot h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
					where d.statut_livre != 'L' and d.consigner = true and p.agence = agence_ and c.article = article_ and d.date_livraison <= date_;
			end if;
		else
			if(societe_ is not null and societe_ >0)then
				if(tranche_ is not null and tranche_ >0)then
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_depot_users u on e.creneau = u.id inner join yvs_com_creneau_depot h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
						inner join yvs_agences a on p.agence = a.id
						where d.statut_livre != 'L' and d.consigner = true and a.societe = societe_ and c.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
				else
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_depot_users u on e.creneau = u.id inner join yvs_com_creneau_depot h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
						inner join yvs_agences a on p.agence = a.id
						where d.statut_livre != 'L' and d.consigner = true and a.societe = societe_ and c.article = article_ and d.date_livraison <= date_;
				end if;
			else
				if(tranche_ is not null and tranche_ >0)then
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_depot_users u on e.creneau = u.id inner join yvs_com_creneau_depot h on u.creneau = h.id
						where d.statut_livre != 'L' and d.consigner = true and c.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
				else
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id 
						where d.statut_livre != 'L' and d.consigner = true and c.article = article_ and d.date_livraison <= date_;
				end if;
			end if;
		end if;
	end if;
	
	if(stock_ is null)then
		stock_ = 0;
	end if;
	RETURN stock_;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date)
  OWNER TO postgres;

