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
			select into ligne_ qualite, conditionnement, lot from yvs_com_contenu_doc_vente where id = idexterne_;
			operation_='Vente';
		WHEN 'yvs_com_contenu_doc_achat' THEN	
			select into ligne_ qualite, conditionnement, lot from yvs_com_contenu_doc_achat where id = idexterne_;
			operation_='Achat';
		WHEN 'yvs_com_ration' THEN	
			select into ligne_ qualite, conditionnement, lot from yvs_com_ration where id = idexterne_;
			operation_='Ration';
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			select into ligne_ d.type_doc, c.qualite, c.conditionnement, c.lot_sortie as lot FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
			elsif(ligne_.type_doc='TR')then
				if(mouvement_='S') then
					operation_='Sortie';
				else
					select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot from yvs_com_contenu_doc_stock where id = idexterne_;
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
				select into ligne_ qualite as qualite, conditionnement as conditionnement, lot as lot from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Sortie';
			else
				select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Entrée';
			end if;
		ELSE
		  RETURN -1;
	END CASE;
	if(parent_ is not null)then
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, parent_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, parent_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date) IS 'Insert une ligne de sortie de mouvement de stock';

-- Function: update_ration()

-- DROP FUNCTION update_ration();

CREATE OR REPLACE FUNCTION update_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	article_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, statut, depot from yvs_com_doc_ration where id = NEW.doc_ration;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		select into article_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement;
		if(doc_.depot is not null)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = article_.prix, conditionnement = NEW.conditionnement, lot = NEW.lot where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'S';
				end if;
			else
				result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_ration()
  OWNER TO postgres;

  
  -- Function: update_contenu_doc_achat()

-- DROP FUNCTION update_contenu_doc_achat();

CREATE OR REPLACE FUNCTION update_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = OLD.article and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = OLD.article and mouvement = 'E';
					if(doc_.type_doc = 'BLA')then
						result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  NEW.pua_attendu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  NEW.pua_attendu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					end if;
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_attendu, cout_entree = NEW.pua_attendu, conditionnement = NEW.conditionnement, lot = NEW.lot where id = mouv_;
				end if;
			else
				if(doc_.type_doc = 'BLA')then			
					result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  NEW.pua_attendu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  NEW.pua_attendu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_achat()
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
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie where id = mouv_;
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
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie where id = mouv_;
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
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie where id = mouv_;
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
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie where id = mouv_;
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
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie where id = mouv_;
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
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
						update yvs_base_mouvement_stock set quantite = NEW.quantite_entree, cout_entree = NEW.prix_entree, conditionnement = NEW.conditionnement_entree, lot = NEW.lot_entree where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'E';
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
	if(doc_.type_doc = 'FRV' or doc_.type_doc = 'BLV') then
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
  
  -- Function: get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint, lot_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE entree_ double precision; 
	sortie_ double precision; 
	query_ character varying default 'SELECT COALESCE(SUM(m.quantite), 0) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE m.quantite IS NOT NULL';
BEGIN
	if(societe_ is not null and societe_ >0)then
		query_ = query_ || ' AND a.societe = '||societe_;
	end if;
	if(agence_ is not null and agence_ >0)then
		query_ = query_ || ' AND d.agence = '||agence_;
	end if;
	if(depot_ is not null and depot_ >0)then
		query_ = query_ || ' AND m.depot = '||depot_;
	end if;
	if(tranche_ is not null and tranche_ >0)then
		query_ = query_ || ' AND m.tranche = '||tranche_;
	end if;
	if(art_ is not null and art_ >0)then
		query_ = query_ || ' AND m.article = '||art_;
	end if;
	if(unite_ is not null and unite_ >0)then
		query_ = query_ || ' AND m.conditionnement = '||unite_;
	end if;
	if(lot_ is not null and lot_ >0)then
		query_ = query_ || ' AND m.lot IN (SELECT com_get_sous_lot('||lot_||', true))';
	end if;
	if(date_ is not null)then
		query_ = query_ || ' AND m.date_doc <= '''||date_||'''';
	end if;
	
	EXECUTE query_ || ' AND m.mouvement=''E''' INTO entree_;
	IF entree_ IS null THEN
	 entree_:=0;	
	END IF;
	EXECUTE query_ || ' AND m.mouvement=''S''' INTO sortie_;
	IF sortie_ IS null THEN
	  sortie_:=0;	
	END IF;
	return (entree_ - sortie_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;


  
  -- Function: get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
	DECLARE entree_ double precision; 
		sortie_ double precision; 
BEGIN
	return get_stock_reel(art_, tranche_, depot_, agence_, societe_, date_, unite_, 0);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;

  -- Function: get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint, lot_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	  stock_ double precision default 0;
	 
BEGIN
	if(depot_ is not null and depot_ >0)then
		if(unite_ is not null and unite_ >0)then
			select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and c.depot = depot_ and c.conditionnement = unite_;
		else
			select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and c.depot = depot_;
		end if;
	else
		if(agence_ is not null and agence_ >0)then
			if(unite_ is not null and unite_ >0)then
				select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c inner join yvs_base_depots d on c.depot = d.id
					where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and d.agence = agence_ and c.conditionnement = unite_;
			else
				select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c inner join yvs_base_depots d on c.depot = d.id
					where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and d.agence = agence_;
			end if;
		else
			if(societe_ is not null and societe_ >0)then
				if(unite_ is not null and unite_ >0)then
					select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c inner join yvs_base_depots d on c.depot = d.id
						inner join yvs_agences a on d.agence = a.id
						where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and a.societe = societe and c.conditionnement = unite_;
				else
					select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c inner join yvs_base_depots d on c.depot = d.id
						inner join yvs_agences a on d.agence = a.id
						where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and a.societe = societe;
				end if;
			else
				stock_ = 0;
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
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;
  
  
  -- Function: get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	  stock_ double precision default 0;
	 
BEGIN
	RETURN get_stock_consigne(article_, tranche_, depot_, agence_, societe_, date_, unite_, 0);
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;

  -- Function: get_stock(bigint, bigint, bigint, bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock(bigint, bigint, bigint, bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint, lot_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
	reel_ double precision; 
	consign_ double precision; 
BEGIN
	reel_ = (select get_stock_reel(art_ ,tranche_ ,depot_ ,agence_ ,societe_ ,date_ ,unite_, lot_));
	consign_ = (select get_stock_consigne(art_ ,tranche_ ,depot_ ,agence_ ,societe_ ,date_ ,unite_, lot_));
	IF reel_ IS null THEN
	 reel_:=0;	
	END IF;
	IF consign_ IS null THEN
	  consign_:=0;	
	END IF;
	return (reel_ - consign_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock(bigint, bigint, bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;

  
  -- Function: grh_get_sous_service(bigint, boolean)

-- DROP FUNCTION grh_get_sous_service(bigint, boolean);

CREATE OR REPLACE FUNCTION com_get_sous_lot(IN parent_ bigint, IN clear_table_ boolean)
  RETURNS SETOF bigint AS
$BODY$
declare 
	verify_ bigint;
	service_ bigint;
begin 	
-- 	DROP TABLE table_sous_lot;
	CREATE TEMP TABLE IF NOT EXISTS table_sous_lot(_id bigint);
	if(clear_table_)then
		DELETE FROM table_sous_lot;
	end if;
	SELECT INTO verify_ coalesce(_id, 0) FROM table_sous_lot WHERE _id = parent_;
	IF(verify_ IS NULL OR verify_ < 1)THEN
		insert into table_sous_lot values(parent_);
	END IF;
	for service_ in select y.id from yvs_com_lot_reception y where y.parent = parent_ order by y.id
	loop
		select into verify_ coalesce(_id, 0) from table_sous_lot where _id = service_;
		if(verify_ is null or verify_ < 1)then
			insert into table_sous_lot values(service_);
			select into verify_ coalesce(count(y.id), 0) from yvs_com_lot_reception y where y.parent = service_;
			if(verify_ > 0)then
				PERFORM com_get_sous_lot(service_, false);
			end if;
		end if;
	end loop;
	RETURN QUERY select * from table_sous_lot order by _id;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_get_sous_lot(bigint, boolean)
  OWNER TO postgres;
