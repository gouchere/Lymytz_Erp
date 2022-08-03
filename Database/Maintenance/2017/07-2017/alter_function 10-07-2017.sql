-- Function: get_stock_reel(bigint, bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
	DECLARE entree_ double precision; 
		sortie_ double precision; 
BEGIN
	if(depot_ is not null and depot_ >0)then
		if(tranche_ is not null and tranche_ >0)then			
			if(unite_ is not null and unite_ >0)then
				entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_ AND m.conditionnement = unite_));
				sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_ AND m.conditionnement = unite_));
			else
				entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
				sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
			end if;
		else							
			if(unite_ is not null and unite_ >0)then
				entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.conditionnement = unite_));
				sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.conditionnement = unite_));
			else
				entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='E'));
				sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='S'));
			end if;
		end if;
	else	
		if(agence_ is not null and agence_ >0)then
			if(tranche_ is not null and tranche_ >0)then	
				if(unite_ is not null and unite_ >0)then
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_ AND m.conditionnement = unite_));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_ AND m.conditionnement = unite_));
				else
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
				end if;
			else					
				if(unite_ is not null and unite_ >0)then
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.conditionnement = unite_));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.conditionnement = unite_));
				else
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='E'));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='S'));
				end if;
			end if;
		else
			if(societe_ is not null and societe_ >0)then
				if(tranche_ is not null and tranche_ >0)then
					if(unite_ is not null and unite_ >0)then
						entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_ AND m.conditionnement = unite_));
						sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_ AND m.conditionnement = unite_));
					else
						entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
						sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
					end if;
				else			
					if(unite_ is not null and unite_ >0)then
						entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.conditionnement = unite_));
						sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.conditionnement = unite_));
					else		
						entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='E'));
						sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='S'));
					end if;
				end if;
			else
				if(tranche_ is not null and tranche_ >0)then
					if(unite_ is not null and unite_ >0)then
						entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_ AND m.conditionnement = unite_));
						sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_ AND m.conditionnement = unite_));
					else
						entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
						sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
					end if;
				else				
					if(unite_ is not null and unite_ >0)then
						entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.conditionnement = unite_));
						sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.conditionnement = unite_));
					else	
						entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='E'));
						sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='S'));
					end if;
				end if;
			end if;
		end if;
	end if;

	IF entree_ IS null THEN
	 entree_:=0;	
	END IF;
	IF sortie_ IS null THEN
	  sortie_:=0;	
	END IF;
	return (entree_ - sortie_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;

  
  -- Function: get_stock_reel(bigint, bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE 
		
BEGIN
	return get_stock_reel(art_, tranche_, depot_, agence_, societe_, date_, 0::bigint);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date)
  OWNER TO postgres;
  
  -- Function: get_stock_consigne(bigint, bigint, bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint)
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
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;

  
  -- Function: get_stock_consigne(bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	 
BEGIN
	return get_stock_consigne(article_, 0, depot_ ,agence_ , societe_, date_, 0::bigint);
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;


  
  -- Function: get_stock(bigint, bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_stock(bigint, bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
	reel_ double precision; 
	consign_ double precision; 
BEGIN
	reel_ = (select get_stock_reel(art_ ,tranche_ ,depot_ ,agence_ ,societe_ ,date_ ,unite_));
	consign_ = (select get_stock_consigne(art_ ,tranche_ ,depot_ ,agence_ ,societe_ ,date_ ,unite_));
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
ALTER FUNCTION get_stock(bigint, bigint, bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;
  
  
  -- Function: get_stock_consigne(bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	 
BEGIN
	return get_stock_consigne(article_, depot_ ,agence_ , societe_, date_, 0::bigint);
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, date)
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
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			select into ligne_ d.type_doc, c.qualite, c.conditionnement FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
			else
				if(mouvement_='S') then
					operation_='Sortie';
				else
					operation_='Entrée';
				end if;
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
						result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					end if;
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_attendu, cout_entree = NEW.pua_attendu, conditionnement = NEW.conditionnement where id = mouv_;
				end if;
			else
				if(doc_.type_doc = 'BLA')then			
					result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
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
							result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite + NEW.quantite_bonus), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
						else
							result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite + NEW.quantite_bonus), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = (NEW.quantite + NEW.quantite_bonus), cout_entree = NEW.prix, conditionnement = NEW.conditionnement where id = mouv_;
					end if;
				else
					if(doc_.type_doc = 'BLV')then
						result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite + NEW.quantite_bonus), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
					else
						result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite + NEW.quantite_bonus), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
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

  
  -- Function: get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer)

-- DROP FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer);

CREATE OR REPLACE FUNCTION get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	data_ record;
	tarif_ record;
	
	valeur_ double precision default 0;
	remise_ double precision;
	garde_ double precision;

BEGIN
	valeur_ = qte_ * prix_;	
	if(client_ is not null and client_ > 0)then
		for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
		loop
			if(data_.permanent is false)then
				if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
					if(unite_ is not null and unite_ > 0)then
						select into tarif_ y.* from yvs_base_plan_tarifaire y inner join yvs_base_conditionnement c on y.conditionnement = c.id where y.categorie = data_.categorie and y.article = article_ and c.id = unite_ and y.actif is true limit 1;
						if(tarif_.id is not null)then
							if(tarif_.nature_remise = 'TAUX')then
								garde_ = valeur_ * (tarif_.remise /100);
							else
								garde_ = tarif_.remise;
							end if;
							select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
							if(tarif_.id IS NOT NULL) then
								if(tarif_.nature_remise = 'TAUX')then
									remise_ = valeur_ * (tarif_.remise /100);
								else
									remise_ = tarif_.remise;
								end if;
							end if;
							if(remise_ is null or remise_ < 1)then
								remise_ = garde_;
							end if;
						end if;
					else
						select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
						if(tarif_.id is not null)then
							if(tarif_.nature_remise = 'TAUX')then
								garde_ = valeur_ * (tarif_.remise /100);
							else
								garde_ = tarif_.remise;
							end if;
							select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
							if(tarif_.id IS NOT NULL) then
								if(tarif_.nature_remise = 'TAUX')then
									remise_ = valeur_ * (tarif_.remise /100);
								else
									remise_ = tarif_.remise;
								end if;
							end if;
							if(remise_ is null or remise_ < 1)then
								remise_ = garde_;
							end if;
						end if;
					end if;
					exit;
				end if;
			else
				if(unite_ is not null and unite_ > 0)then
					select into tarif_ y.* from yvs_base_plan_tarifaire y inner join yvs_base_conditionnement c on y.conditionnement = c.id where y.categorie = data_.categorie and y.article = article_ and c.id = unite_ and y.actif is true limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
						else
							garde_ = tarif_.remise;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL) then
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
							else
								remise_ = tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ < 1)then
							remise_ = garde_;
						end if;
					end if;
				else
					select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
						else
							garde_ = tarif_.remise;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL) then
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
							else
								remise_ = tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ <= 0)then
							remise_ = garde_;
						end if;
						exit;
					end if;
				end if;
				--exit;
			end if;
		end loop;
	end if;
	if(remise_ is null or remise_ <= 0)then
		if(unite_ is not null and unite_ > 0)then
			select into tarif_ y.* from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.id = unite_ and actif is true limit 1;
			if(tarif_.id IS NOT NULL) then
					if(tarif_.nature_remise = 'TAUX')then
						remise_ = valeur_ * (tarif_.remise /100);
					else
						remise_ = tarif_.remise;
					end if;
			end if;
		else
			select into tarif_ * from yvs_base_article_point where point = point_ and article = article_ and actif is true limit 1;
			if(tarif_.id IS NOT NULL) then
				if(tarif_.nature_remise = 'TAUX')then
					remise_ = valeur_ * (tarif_.remise /100);
				else
					remise_ = tarif_.remise;
				end if;
			end if;
			if(remise_ is null or remise_ <= 0)then
				if(unite_ is not null and unite_ > 0)then
					select into remise_ remise from yvs_base_conditionnement where id = unite_;
					if(remise_ is not null) then
						remise_ = valeur_ * (remise_/100);
					end if;
				else
					select into remise_ remise from yvs_base_articles where id = article_; 
					if(remise_ is not null) then
						remise_ = valeur_ * (remise_/100);
					end if;
				end if;
			end if;	
		end if;
	end if;	
	if(remise_ is null or remise_ <=0)then
		remise_ = 0;
	end if;	
	return remise_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, bigint) IS 'retourne la remise sur vente d'' article';


-- Function: get_remise_vente(bigint, double precision, double precision, bigint, bigint, date)

-- DROP FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE

BEGIN
	return (select get_remise_vente(article_, qte_, prix_, client_, point_, date_, 0::bigint));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date) IS 'retourne la remise sur vente d'' article';


-- Function: get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean)

-- DROP FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean);

CREATE OR REPLACE FUNCTION get_puv(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, depot_ bigint, point_ bigint, date_ date, unite_ bigint, min_ boolean)
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
	pr_ = (select get_pr(article_, depot_, 0 ,date_, unite_));
	
	-- Recherche du prix du point de vente
	if(point_ is not null and point_ > 0)then
		if(unite_ is not null and unite_ > 0)then
			select into tarif_ y.*, a.prioritaire from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.id = unite_ and actif is true limit 1;
			if(tarif_.id is not null)then
				if(tarif_.prioritaire is true)then --Verification si ce prix est prioritaire
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
		else
			select into tarif_ * from yvs_base_article_point where article = article_ and point = point_ limit 1;
			if(tarif_.id is not null)then
				if(tarif_.prioritaire is true)then --Verification si ce prix est prioritaire
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
	
	if(puv_ is null or puv_ < 1)then
		-- Recherche du prix en fonction de la catégorie tarifaire
		if(client_ is not null and client_ > 0)then
			for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
			loop
				if(data_.permanent is false)then
					if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
						if(unite_ is not null and unite_ > 0)then
							select into tarif_ y.* from yvs_base_plan_tarifaire y inner join yvs_base_conditionnement c on y.conditionnement = c.id where y.categorie = data_.categorie and y.article = article_ and c.id = unite_ and y.actif is true limit 1;
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
						end if;
						exit;
					end if;
				else
					if(unite_ is not null and unite_ > 0)then
						select into tarif_ y.* from yvs_base_plan_tarifaire y inner join yvs_base_conditionnement c on y.conditionnement = c.id where y.categorie = data_.categorie and y.article = article_ and c.id = unite_ and y.actif is true limit 1;
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
					end if;
					exit;
				end if;
			end loop;
		end if;
		
		if(puv_ is null or puv_ < 1)then
			-- Recherche du prix du point de vente non prioritaire
			if(point_ is not null and point_ > 0)then
				if(unite_ is not null and unite_ > 0)then
					select into tarif_ y.* from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.id = unite_ and actif is true limit 1;
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
				else
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
			end if;
			
			if(puv_ is null or puv_ < 1)then					
				-- Recherche du prix de l'article sur la fiche d'article
				if(unite_ is not null and unite_ > 0)then
					select into tarif_ * from yvs_base_conditionnement where id = unite_;
					if(tarif_.id is not null)then
						if(min_)then
							if(tarif_.nature_prix_min = 'TAUX')then
								puv_ = (pr_ * tarif_.prix_min) /100;
							else
								puv_ = tarif_.prix_min;
							end if;
						else
							puv_ = tarif_.prix;
						end if;
					end if;
				else
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
ALTER FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, bigint, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, bigint, boolean) IS 'retourne le prix de vente d'' article';


-- Function: get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean)

-- DROP FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean);

CREATE OR REPLACE FUNCTION get_puv(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, depot_ bigint, point_ bigint, date_ date, min_ boolean)
  RETURNS double precision AS
$BODY$
DECLARE

BEGIN
	
	return (select get_puv(article_, qte_, prix_, client_, depot_, point_, date_, 0::bigint, min_));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean) IS 'retourne le prix de vente d'' article';



-- Function: get_pr(bigint, bigint, bigint, date)

-- DROP FUNCTION get_pr(bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pr_ double precision;

BEGIN
	if(depot_ is not null and depot_ > 0)then
		if(tranche_ is not null and tranche_ > 0)then
			if(unite_ is not null and unite_ > 0)then
				select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ and tranche = tranche_ and date_doc <= date_ and conditionnement = unite_ order by date_doc desc, id desc limit 1;
			else
				select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ and tranche = tranche_ and date_doc <= date_ order by date_doc desc, id desc limit 1;
			end if;
		else
			if(unite_ is not null and unite_ > 0)then
				select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ and date_doc <= date_ and conditionnement = unite_ order by date_doc desc, id desc limit 1;
			else
				select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ and date_doc <= date_ order by date_doc desc, id desc limit 1;
			end if;
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			if(unite_ is not null and unite_ > 0)then
				select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and tranche = tranche_ and date_doc <= date_ and conditionnement = unite_ order by date_doc desc,id desc limit 1;
			else
				select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and tranche = tranche_ and date_doc <= date_ order by date_doc desc,id desc limit 1;
			end if;
		else
			if(unite_ is not null and unite_ > 0)then
				select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and date_doc <= date_ and conditionnement = unite_ order by date_doc desc,id desc limit 1;
			else
				select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and date_doc <= date_ order by date_doc desc , id desc limit 1;
			end if;
		end if;
	end if;
	if(pr_ is null or pr_ <=0)then
		pr_ = get_pua(article_,0);
	end if;
	return pr_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date, bigint) IS 'retourne le prix de revient d'' article';


-- Function: get_pr(bigint, bigint, bigint, date)

-- DROP FUNCTION get_pr(bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE

BEGIN
	return (select get_pr(article_, depot_, tranche_, date_, 0::bigint));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date) IS 'retourne le prix de revient d'' article';


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
	if(NEW.type_doc = 'BLV' or NEW.type_doc = 'FRV') then
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
							result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte + cont_.bonus), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', NEW.date_livraison));
						else
							result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte + cont_.bonus), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', NEW.date_livraison));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = (cont_.qte + cont_.bonus), cout_entree = cont_.prix where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article;
					end if;
				else
					if(NEW.type_doc = 'BLV')then
						result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte + cont_.bonus), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', NEW.date_livraison));
					else
						result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte + cont_.bonus), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', NEW.date_livraison));
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


-- Function: delete_doc_ventes()

-- DROP FUNCTION delete_doc_ventes();

CREATE OR REPLACE FUNCTION delete_doc_reconditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
BEGIN
	if(OLD.statut = 'V') then
		for cont_ in select id from yvs_com_reconditionnement where fiche = OLD.id
		loop
			
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_reconditionnement';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_doc_reconditionnement()
  OWNER TO postgres;
  
CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_com_transformation
  FOR EACH ROW
  EXECUTE PROCEDURE delete_doc_reconditionnement();
  
  -- Function: update_doc_ventes()

-- DROP FUNCTION update_doc_ventes();

CREATE OR REPLACE FUNCTION update_doc_reconditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	entree_ record;
	sortie_ record;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		for cont_ in select id, article , quantite, resultante ,unite_source as entree, unite_destination as sortie from yvs_com_reconditionnement where fiche = OLD.id
		loop
			select into entree_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.entree;
			select into sortie_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.sortie;
			
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_reconditionnement' and depot = NEW.depot and article = cont_.article;
			if(mouv_ is not null)then
				if(entree_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_reconditionnement' and depot = NEW.depot and article = cont_.article;
					result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, entree_.prix, 'yvs_com_reconditionnement', cont_.id, 'S', NEW.date_doc));
					result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.resultante, sortie_.prix, 'yvs_com_reconditionnement', cont_.id, 'E', NEW.date_doc));
				else
					update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = entree_.prix where id_externe = cont_.id and table_externe = 'yvs_com_reconditionnement' and depot = NEW.depot and article = cont_.article and mouvement = 'E';
					update yvs_base_mouvement_stock set quantite = cont_.resultante, cout_entree = sortie_.prix where id_externe = cont_.id and table_externe = 'yvs_com_reconditionnement' and depot = NEW.depot and article = cont_.article and mouvement = 'S';
				end if;
			else
				result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, entree_.prix, 'yvs_com_reconditionnement', cont_.id, 'S', NEW.date_doc));
				result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.resultante, sortie_.prix, 'yvs_com_reconditionnement', cont_.id, 'E', NEW.date_doc));
			end if;	
		end loop;
	elsif(NEW.statut != 'V')then
		for cont_ in select id from yvs_com_reconditionnement where fiche = OLD.id
		loop				
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_reconditionnement';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_reconditionnement()
  OWNER TO postgres;
  
  -- Trigger: update_ on yvs_com_doc_ventes

-- DROP TRIGGER update_ ON yvs_com_doc_ventes;

CREATE TRIGGER update_
  BEFORE UPDATE
  ON yvs_com_transformation
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_reconditionnement();

  
  
  -- Function: delete_contenu_doc_vente()

-- DROP FUNCTION delete_contenu_doc_vente();

CREATE OR REPLACE FUNCTION delete_reconditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	mouv_ bigint;
BEGIN
	select into doc_ id, statut, depot from yvs_com_transformation where id = OLD.fiche;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_reconditionnement()
  OWNER TO postgres;
  
CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_com_reconditionnement
  FOR EACH ROW
  EXECUTE PROCEDURE delete_reconditionnement();


  -- Function: update_contenu_doc_vente()

-- DROP FUNCTION update_contenu_doc_vente();

CREATE OR REPLACE FUNCTION update_reconditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	entree_ record;
	sortie_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, statut, depot, date_doc from yvs_com_transformation where id = NEW.fiche;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		select into entree_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.unite_source;
		select into sortie_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.unite_destination;
		if(doc_.depot is not null)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, entree_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.resultante, sortie_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = entree_.prix, conditionnement = NEW.unite_source where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'S';
					update yvs_base_mouvement_stock set quantite = NEW.resultante, cout_entree = sortie_.prix, conditionnement = NEW.unite_destination where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'E';
				end if;
			else
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, entree_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.resultante, sortie_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_reconditionnement()
  OWNER TO postgres;

  
CREATE TRIGGER update_
  BEFORE UPDATE
  ON yvs_com_reconditionnement
  FOR EACH ROW
  EXECUTE PROCEDURE update_reconditionnement();

  
  -- Function: insert_contenu_doc_vente()

-- DROP FUNCTION insert_contenu_doc_vente();

CREATE OR REPLACE FUNCTION insert_reconditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	entree_ record;
	sortie_ record;
	arts_ record;
	result boolean default false;
BEGIN
	select into doc_ id, statut, depot, date_doc from yvs_com_transformation where id = NEW.fiche;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		if(doc_.depot is not null)then
			result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.dac_doc));
			result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.resultante, 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.dac_doc));
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_reconditionnement()
  OWNER TO postgres;

  
CREATE TRIGGER insert_
  AFTER INSERT
  ON yvs_com_reconditionnement
  FOR EACH ROW
  EXECUTE PROCEDURE insert_reconditionnement();
  
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
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			select into ligne_ d.type_doc, c.qualite, c.conditionnement FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
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
