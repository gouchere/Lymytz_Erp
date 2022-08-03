select alter_action_colonne_key('yvs_com_doc_achats', true, true);
select alter_action_colonne_key('yvs_com_doc_stocks', true, true);
select alter_action_colonne_key('yvs_com_doc_ventes', true, true);

CREATE OR REPLACE FUNCTION insert_mouvement_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, coutentree_ double precision, cout_ double precision, parent_ bigint, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE
    
BEGIN
	if(parent_ is not null)then
		if(tranche_!=null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, '', depot_, tranche_, parent_, coutentree_, cout_, current_timestamp);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, '', depot_, parent_, coutentree_, cout_, current_timestamp);
		end if;
	else
		if(tranche_!=null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, '', depot_, tranche_, coutentree_, cout_, current_timestamp);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, '', depot_, coutentree_, cout_, current_timestamp);
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date) IS 'Insert une ligne de sortie de mouvement de stock';



-- Function: valorisation_stock_by_cmp1(bigint, bigint, double precision, double precision, character varying, bigint)

-- DROP FUNCTION valorisation_stock_by_cmp1(bigint, bigint, double precision, double precision, character varying, bigint);

CREATE OR REPLACE FUNCTION valorisation_stock_by_cmp2(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE 
	entree_ record;  
	new_cout double precision default 0;
BEGIN
	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_cmp2(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_cmp2(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date) IS 'Valorisation de stock pas la methode CMP II';



-- Function: valorisation_stock_by_cmp1(bigint, bigint, double precision, double precision, character varying, bigint)

-- DROP FUNCTION valorisation_stock_by_cmp1(bigint, bigint, double precision, double precision, character varying, bigint);

CREATE OR REPLACE FUNCTION valorisation_stock_by_cmp1(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE 
	entree_ record;  
	new_cout double precision default 0;
BEGIN
	-- Calcul du cout de stockage	
	-- Recherche de la derniere entree de l'article dans le dépot
	if(tranche_ != null and tranche_ >0)then
		select into entree_ quantite, cout_stock as cout from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ and tranche = tranche_ order by date_doc, id desc limit 1;
	else
		select into entree_ quantite, cout_stock as cout from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ order by date_doc, id desc limit 1;
	end if;
	-- Recherche des valeurs null
	if(quantite_ is null)then
		quantite_ = 0;
	end if;
	if(cout_ is null)then
		cout_ = 0;
	end if;
	if(entree_.quantite is null)then
		entree_.quantite = 0;
	end if;
	if(entree_.cout is null)then
		entree_.cout = 0;
	end if;
	-- Calcul du cout de stockage
	new_cout = (((entree_.quantite * entree_.cout) + (quantite_ * cout_)) / (entree_.quantite + quantite_));
	-- Retourne le nouveau cout moyen calculé
	if(new_cout is null)then
		new_cout = 0;
	end if;
	-- Insertion du mouvement de stock
	if (select insert_mouvement_stock(article_, depot_, tranche_, quantite_ , cout_, new_cout, null, tableexterne_, idexterne_, 'E', date_))then
		return true;
	else
		return false;
	end if;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_cmp1(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_cmp1(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date) IS 'Valorisation de stock pas la methode CMP I';



-- Function: valorisation_stock_by_peps(bigint, bigint, double precision, character varying, bigint)

-- DROP FUNCTION valorisation_stock_by_peps(bigint, bigint, double precision, character varying, bigint);

CREATE OR REPLACE FUNCTION valorisation_stock_by_peps(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, tableexterne_ character varying, idexterne_ bigint, date_ date)
  RETURNS integer AS
$BODY$
DECLARE 
	i integer default 0;  
	entree_ record;  
	qte double precision;
	stock double precision default 0;
	reste double precision default 0;
BEGIN
	-- Sauvegarde la valeur de la quantitée demandée pour ne pas la perdre	
	RAISE NOTICE 'quantite_ is %', quantite_;
	qte = quantite_;
	-- Recherche des entrees d'un article dans un dépot dont le stock est superieur à 0
	if(tranche_ != null and tranche_ >0)then
		for entree_ in select m.id as id, m.quantite as quantite, m.cout_stock as cout from yvs_base_mouvement_stock m where m.mouvement = 'E' and 
				((m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) is null or
				(m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) > 0)
				and m.article = article_ and m.depot = depot_ and tranche = tranche_ order by m.date_doc,id
		loop
			-- Controle pour l'arret de la boucle (la qte doit etre egale à 0 pour sortir)
			if(qte > 0)then
				-- Recherche du reste en stock du lot courant
				select into reste (quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = entree_.id))
					from yvs_base_mouvement_stock where id = entree_.id;
				if(reste is null)then
					reste = entree_.quantite;
				end if;
				-- Teste pour vérifier si le lot courant peut couvrir la demande
				if(reste > qte)then
					stock = qte;
					qte = 0;
				else
					stock = reste;
					qte  = qte - stock;
				end if;
				-- Insertion du mouvement de stock
				if (select insert_mouvement_stock(article_, depot_, tranche_, stock, entree_.cout, entree_.cout, entree_.id, tableexterne_, idexterne_, 'S', date_))then
					i = i + 1;
				end if;
			else
				exit;
			end if;		
		end loop;
	else
		for entree_ in select m.id as id, m.quantite as quantite, m.cout_stock as cout from yvs_base_mouvement_stock m where m.mouvement = 'E' and 
			((m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) is null or
			(m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) > 0)
			and m.article = article_ and m.depot = depot_ order by m.date_doc,id
		loop
			-- Controle pour l'arret de la boucle (la qte doit etre egale à 0 pour sortir)
			if(qte > 0)then
				-- Recherche du reste en stock du lot courant
				select into reste (quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = entree_.id))
					from yvs_base_mouvement_stock where id = entree_.id;
				if(reste is null)then
					reste = entree_.quantite;
				end if;
				-- Teste pour vérifier si le lot courant peut couvrir la demande
				if(reste > qte)then
					stock = qte;
					qte = 0;
				else
					stock = reste;
					qte  = qte - stock;
				end if;
				-- Insertion du mouvement de stock
				if (select insert_mouvement_stock(article_, depot_, tranche_, stock, entree_.cout, entree_.cout, entree_.id, tableexterne_, idexterne_, 'S', date_))then
					i = i + 1;
				end if;
			else
				exit;
			end if;		
		end loop;
	end if;
	return i;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, double precision, character varying, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, double precision, character varying, bigint, date) IS 'Valorisation de stock pas la methode PEPS';


-- Function: valorisation_stock(bigint, bigint, double precision, double precision, character varying, bigint, character varying)

-- DROP FUNCTION valorisation_stock(bigint, bigint, double precision, double precision, character varying, bigint, character varying);

CREATE OR REPLACE FUNCTION valorisation_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE 
	meth_ character varying;
	new_cout_ double precision default 0;
BEGIN
	select into meth_ methode_val from yvs_articles where id = article_;
	if(cout_ is null)then
		cout_ = 0;
	end if;
	if(mouvement_ = 'E')then
		if(meth_ = 'FIFO')then
			return (select insert_mouvement_stock(article_, depot_, tranche_, quantite_, cout_, cout_, null, tableexterne_, idexterne_, 'E', date_));	
		elsif (meth_ = 'CMPI')then
			return (select valorisation_stock_by_cmp1(article_, depot_, tranche_, quantite_, cout_, tableexterne_, idexterne_, date_));
		elsif (meth_ = 'CMPII')then
		
		end if;
	else 
		if(meth_ = 'FIFO')then
			if(select valorisation_stock_by_peps(article_, depot_, tranche_, quantite_, tableexterne_, idexterne_, date_) > 0)then
				return true;
			end if;		
		elsif (meth_ = 'CMPI')then
			select into new_cout_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ order by date_doc, id desc limit 1;
			if(new_cout_ is null)then
				new_cout_ = 0;
			end if;
			return (select insert_mouvement_stock(article_ , depot_ , tranche_, quantite_ , new_cout_ , new_cout_ , null, tableexterne_, idexterne_, 'S', date_));
		elsif (meth_ = 'CMPII')then
		
		end if;
	end if;
	return false;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock(bigint, bigint, bigint, double precision, double precision, character varying, bigint, character varying, date)
  OWNER TO postgres;


-- Function: valorisation_stock_by_cmp1(bigint, bigint, double precision, double precision, character varying, bigint)

-- DROP FUNCTION valorisation_stock_by_cmp1(bigint, bigint, double precision, double precision, character varying, bigint);

CREATE OR REPLACE FUNCTION valorisation_stock_by_cmp1(article_ bigint, depot_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE 
	
BEGIN
	return valorisation_stock_by_cmp1(article_ ,depot_ ,0, quantite_ ,cout_ ,tableexterne_ ,idexterne_, current_date);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_cmp1(bigint, bigint, double precision, double precision, character varying, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_cmp1(bigint, bigint, double precision, double precision, character varying, bigint) IS 'Valorisation de stock pas la methode CMP I';



-- Function: valorisation_stock_by_peps(bigint, bigint, double precision, character varying, bigint)

-- DROP FUNCTION valorisation_stock_by_peps(bigint, bigint, double precision, character varying, bigint);

CREATE OR REPLACE FUNCTION valorisation_stock_by_peps(article_ bigint, depot_ bigint, quantite_ double precision, tableexterne_ character varying, idexterne_ bigint)
  RETURNS integer AS
$BODY$
DECLARE 

BEGIN
	return valorisation_stock_by_peps(article_ ,depot_ , 0, quantite_ ,tableexterne_ ,idexterne_ , current_date);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_peps(bigint, bigint, double precision, character varying, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_peps(bigint, bigint, double precision, character varying, bigint) IS 'Valorisation de stock pas la methode PEPS';


-- Function: valorisation_stock_by_cmp2(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date)

-- DROP FUNCTION valorisation_stock_by_cmp2(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date);

CREATE OR REPLACE FUNCTION valorisation_stock_by_cmp2(article_ bigint, depot_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE 
	
BEGIN
	return valorisation_stock_by_cmp2(article_ ,depot_ ,0 , quantite_ ,cout_ ,tableexterne_ ,idexterne_ ,current_date);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_cmp2(bigint, bigint, double precision, double precision, character varying, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_cmp2(bigint, bigint, double precision, double precision, character varying, bigint) IS 'Valorisation de stock pas la methode CMP II';



-- Function: insert_mouvement_stock(bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying)

-- DROP FUNCTION insert_mouvement_stock(bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying);

CREATE OR REPLACE FUNCTION insert_mouvement_stock(article_ bigint, depot_ bigint, quantite_ double precision, coutentree_ double precision, cout_ double precision, parent_ bigint, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying)
  RETURNS boolean AS
$BODY$
DECLARE 
	
    
BEGIN
	return insert_mouvement_stock(article_, depot_ , 0,quantite_ ,coutentree_ ,cout_ ,parent_ ,tableexterne_ ,idexterne_ ,mouvement_ ,current_date);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying) IS 'Insert une ligne de sortie de mouvement de stock';


-- Function: valorisation_stock(bigint, bigint, double precision, double precision, character varying, bigint, character varying)

-- DROP FUNCTION valorisation_stock(bigint, bigint, double precision, double precision, character varying, bigint, character varying);

CREATE OR REPLACE FUNCTION valorisation_stock(article_ bigint, depot_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying)
  RETURNS boolean AS
$BODY$
DECLARE 
BEGIN
	return valorisation_stock(article_ ,depot_ , 0,quantite_ ,cout_ ,tableexterne_ ,idexterne_ ,mouvement_ , current_date);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock(bigint, bigint, double precision, double precision, character varying, bigint, character varying)
  OWNER TO postgres;

  
  
  -- Function: update_doc_achats()

-- DROP FUNCTION update_doc_achats();

CREATE OR REPLACE FUNCTION update_doc_achats()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	if(NEW.type_doc = 'FA' or NEW.type_doc = 'BLA')then
		if(NEW.statut = 'VALIDE' and NEW.statut != OLD.statut and OLD.mouv_stock = true)then
			for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
			loop
				select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and article = arts_.id and mouvement = 'E';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
				end if;	
			end loop;
		elsif(OLD.statut = 'VALIDE' and NEW.statut != 'VALIDE')then
			for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche  and article = arts_.id and mouvement = 'E';
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
ALTER FUNCTION update_doc_achats()
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
	if(NEW.type_doc = 'FV' or NEW.type_doc = 'BLV') then
		if((NEW.type_doc = 'FV' or NEW.type_doc = 'BLV') and NEW.statut = 'VALIDE' and NEW.statut != OLD.statut and OLD.mouv_stock = true) then
			select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
				on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
				where e.id = NEW.entete_doc;
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
				select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = dep_.depot and tranche = dep_.tranche and article = arts_.id and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, dep_.depot, dep_.tranche, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(arts_.id, dep_.depot, dep_.tranche, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
				end if;	
			end loop;
		elsif(OLD.statut = 'VALIDE' and NEW.statut != 'VALIDE')then
			select into dep_ h.depot as depot, h.tranche as tranche from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
			on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
			where e.id = OLD.entete_doc;
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = dep_.depot and tranche = dep_.tranche and article = arts_.id and mouvement = 'S';
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
	select into trancheD_ tranche from yvs_com_creneau_horaire where id = NEW.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_horaire where id = NEW.creneau_source;
	
	if(NEW.statut != OLD.statut) then
		if(NEW.type_doc = 'FT' and NEW.statut = 'VALIDE') then
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Insertion mouvement stock
				if(NEW.destination is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and tranche = trancheD_ and article = arts_.id and mouvement = 'E';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(arts_.id, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;

				if(NEW.source is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and tranche = trancheS_ and article = arts_.id and mouvement = 'S';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(arts_.id, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				end if;
			end loop;
		elsif(NEW.type_doc = 'FT' and NEW.statut != 'VALIDE') then
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and tranche = trancheD_ and article = arts_.id and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
				
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and tranche = trancheS_ and article = arts_.id and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
			end loop;		
		elsif(NEW.type_doc = 'SS' and NEW.statut = 'VALIDE') then
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Insertion mouvement stock
				if(NEW.source is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and tranche = trancheS_ and article = arts_.id and mouvement = 'S';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(arts_.id, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				end if;
			end loop;
		elsif(NEW.type_doc = 'SS' and NEW.statut != 'VALIDE') then
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and tranche = trancheD_ and article = arts_.id and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
				
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and tranche = trancheS_ and article = arts_.id and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
			end loop;
		elsif(NEW.type_doc = 'ES' and NEW.statut = 'VALIDE') then
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Insertion mouvement stock
				if(NEW.destination is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and tranche = trancheD_ and article = arts_.id and mouvement = 'E';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(arts_.id, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;
			end loop;
		elsif(NEW.type_doc = 'ES' and NEW.statut != 'VALIDE') then
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and tranche = trancheD_ and article = arts_.id and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
				
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and tranche = trancheS_ and article = arts_.id and mouvement = 'S';
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
ALTER FUNCTION update_doc_stocks()
  OWNER TO postgres;

  
  -- Function: insert_contenu_doc_achat()

-- DROP FUNCTION insert_contenu_doc_achat();

CREATE OR REPLACE FUNCTION insert_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = NEW.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock from yvs_com_doc_achats where id = NEW.doc_achat;
	if(doc_.type_doc = 'FA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true)then
			result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu, (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_achat()
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
	select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = OLD.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = arts_.id and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id = mouv_;
					result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
				end if;
			else
				result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
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
	select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = NEW.article;
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true)then
			if(dep_ is not null)then
				result = (select valorisation_stock(arts_.id, dep_.depot, dep_.tranche, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
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
	select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = NEW.article;
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true)then
			if(dep_ is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = dep_.depot and article = arts_.id and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, dep_.depot, dep_.tranche, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(arts_.id, dep_.depot, dep_.tranche, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
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
	select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = NEW.article;
	select into doc_ id, type_doc, date_doc, statut, source, destination, creneau_destinataire, creneau_source from yvs_com_doc_stocks where id = NEW.doc_stock;
	select into trancheD_ tranche from yvs_com_creneau_horaire where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_horaire where id = doc_.creneau_source;
	
	if(doc_.statut = 'VALIDE') then
		if(doc_.type_doc = 'FT') then
		--Insertion mouvement stock
			if(doc_.destination is not null)then
				result = (select valorisation_stock(arts_.id, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			end if;
			if(doc_.source is not null)then
				result = (select valorisation_stock(arts_.id, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'SS') then
			if(doc_.source is not null)then
				result = (select valorisation_stock(arts_.id, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'ES') then
			if(doc_.destination is not null)then
				result = (select valorisation_stock(arts_.id, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
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
	select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = NEW.article;
	select into doc_ id, type_doc, date_doc, statut, source, destination, creneau_destinataire, creneau_source from yvs_com_doc_stocks where id = NEW.doc_stock;
	select into trancheD_ tranche from yvs_com_creneau_horaire where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_horaire where id = doc_.creneau_source;
	
	--Insertion mouvement stock
	if(doc_.statut = 'VALIDE') then
		if(doc_.type_doc = 'FT') then
			if(doc_.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = arts_.id and mouvement = 'E';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(arts_.id, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;	
			end if;

			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = arts_.id and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME, NEW.id||'', NEW.id, 'S', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(arts_.id, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;	
			end if;
		elsif(doc_.type_doc = 'SS') then
			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = arts_.id and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(arts_.id, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;	
			end if;
		elsif(doc_.type_doc = 'ES') then
			if(doc_.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = arts_.id and mouvement = 'E';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(arts_.id, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
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


-- Function: update_doc_achats()

-- DROP FUNCTION update_doc_achats();

CREATE OR REPLACE FUNCTION update_doc_achats()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
	depot_ bigint;
	tranche_ bigint;
BEGIN
	if(NEW.type_doc = 'FA' or NEW.type_doc = 'BLA')then
		if(NEW.statut = 'VALIDE' and NEW.statut != OLD.statut and OLD.mouv_stock = true)then
			for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
			loop
				select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and article = arts_.id and mouvement = 'E';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
					end if;
				else
					result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
				end if;	
			end loop;
		elsif(OLD.statut = 'VALIDE' and NEW.statut != 'VALIDE')then
			for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E';
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
ALTER FUNCTION update_doc_achats()
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
	select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = OLD.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = arts_.id and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id = mouv_;
					result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_attendu, cout_entree = NEW.pua_attendu where id = mouv_;
				end if;
			else
				result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
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

  
  
  -- Function: valorisation_stock_by_peps(bigint, bigint, bigint, double precision, character varying, bigint, date)

-- DROP FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, double precision, character varying, bigint, date);

CREATE OR REPLACE FUNCTION valorisation_stock_by_peps(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, tableexterne_ character varying, idexterne_ bigint, date_ date)
  RETURNS integer AS
$BODY$
DECLARE 
	i integer default 0;  
	entree_ record;  
	qte double precision;
	stock double precision default 0;
	reste double precision default 0;
BEGIN
	-- Sauvegarde la valeur de la quantitée demandée pour ne pas la perdre	
	RAISE NOTICE 'quantite_ is %', quantite_;
	qte = quantite_;
	-- Recherche des entrees d'un article dans un dépot dont le stock est superieur à 0
	if(tranche_ != null and tranche_ >0)then
		for entree_ in select m.id as id, m.quantite as quantite, m.cout_stock as cout from yvs_base_mouvement_stock m where m.mouvement = 'E' and 
				((m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) is null or
				(m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) > 0)
				and m.article = article_ and m.depot = depot_ and tranche = tranche_ order by m.date_doc,id
		loop
			-- Controle pour l'arret de la boucle (la qte doit etre egale à 0 pour sortir)
			if(qte > 0)then
				-- Recherche du reste en stock du lot courant
				select into reste (quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = entree_.id))
					from yvs_base_mouvement_stock where id = entree_.id;
				if(reste is null)then
					reste = entree_.quantite;
				end if;
				-- Teste pour vérifier si le lot courant peut couvrir la demande
				if(reste > qte)then
					stock = qte;
					qte = 0;
				else
					stock = reste;
					qte  = qte - stock;
				end if;
				-- Insertion du mouvement de stock
				if (select insert_mouvement_stock(article_, depot_, tranche_, stock, entree_.cout, entree_.cout, entree_.id, tableexterne_, idexterne_, 'S', date_))then
					i = i + 1;
				end if;
			else
				exit;
			end if;		
		end loop;
	else
		for entree_ in select m.id as id, m.quantite as quantite, m.cout_stock as cout from yvs_base_mouvement_stock m where m.mouvement = 'E' and 
			((m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) is null or
			(m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) > 0)
			and m.article = article_ and m.depot = depot_ order by m.date_doc,id
		loop
			-- Controle pour l'arret de la boucle (la qte doit etre egale à 0 pour sortir)
			if(qte > 0)then
				-- Recherche du reste en stock du lot courant
				select into reste (quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = entree_.id))
					from yvs_base_mouvement_stock where id = entree_.id;
				if(reste is null)then
					reste = entree_.quantite;
				end if;
				-- Teste pour vérifier si le lot courant peut couvrir la demande
				if(reste > qte)then
					stock = qte;
					qte = 0;
				else
					stock = reste;
					qte  = qte - stock;
				end if;
				-- Insertion du mouvement de stock
				if (select insert_mouvement_stock(article_, depot_, tranche_, stock, entree_.cout, entree_.cout, entree_.id, tableexterne_, idexterne_, 'S', date_))then
					i = i + 1;
				end if;
			else
				exit;
			end if;		
		end loop;
	end if;
	if(i<1)then
		select into reste c.pua_attendu from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat inner join yvs_com_article a on a.id = c.article where a.article = article_ order by d.date_doc desc limit 1;
		if(reste is null)then
			select into reste pua from yvs_articles where id = article_;
			if(reste is null)then
				reste = 0;
			end if;
		end if;
		if (select insert_mouvement_stock(article_, depot_, tranche_, quantite_, reste, reste, entree_.id, tableexterne_, idexterne_, 'S', date_))then
			i = i + 1;
		end if;
	end if;
	RAISE NOTICE '-- i : %',i; 
	return i;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, double precision, character varying, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, double precision, character varying, bigint, date) IS 'Valorisation de stock pas la methode PEPS';


-- Function: get_stock_reel(bigint, bigint, date, integer)

-- DROP FUNCTION get_stock_reel(bigint, bigint, integer, integer, integer, date);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
	DECLARE entree_ double precision; 
		sortie_ double precision; 
BEGIN
	if(depot_ is not null and depot_ >0)then
		if(tranche_ is not null and tranche_ >0)then
			entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
			sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
		else					
			entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='E'));
			sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='S'));
		end if;
	else	
		if(agence_ is not null and agence_ >0)then
			if(tranche_ is not null and tranche_ >0)then
				entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
				sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
			else					
				entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='E'));
				sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='S'));
			end if;
		else
			if(societe_ is not null and societe_ >0)then
				if(tranche_ is not null and tranche_ >0)then
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
				else					
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='E'));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='S'));
				end if;
			else
				if(tranche_ is not null and tranche_ >0)then
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
				else					
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='E'));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='S'));
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
ALTER FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date)
  OWNER TO postgres;

  
  -- Function: get_stock(bigint, bigint, date, integer)

-- DROP FUNCTION get_stock(bigint, bigint, date, integer);

CREATE OR REPLACE FUNCTION get_stock(art_ bigint, depot_ bigint, date_ date, tranche_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 

BEGIN
	return get_stock(art_ ,tranche_ ,depot_ ,0::bigint ,0::bigint ,date_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock(bigint, bigint, date, bigint)
  OWNER TO postgres;

  
  -- Function: get_stock(bigint, bigint, date)

-- DROP FUNCTION get_stock(bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock(art_ bigint, depot_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE 
 
BEGIN
	return get_stock(art_ ,0::bigint ,depot_ ,0::bigint ,0::bigint ,date_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock(bigint, bigint, date)
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
			select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
				inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id
				where d.livrer = false and d.consigner = true and d.depot_livrer = depot_ and o.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
		else
			select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
				inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id 
				where d.livrer = false and d.consigner = true and d.depot_livrer = depot_ and o.article = article_ and d.date_livraison <= date_;
		end if;
	else
		if(agence_ is not null and agence_ >0)then
			if(tranche_ is not null and tranche_ > 0)then
				select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
					where d.livrer = false and d.consigner = true and p.agence = agence_ and o.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
			else
				select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
					where d.livrer = false and d.consigner = true and p.agence = agence_ and o.article = article_ and d.date_livraison <= date_;
			end if;
		else
			if(societe_ is not null and societe_ >0)then
				if(tranche_ is not null and tranche_ >0)then
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
						inner join yvs_agences a on p.agence = a.id
						where d.livrer = false and d.consigner = true and a.societe = societe_ and c.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
				else
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
						inner join yvs_agences a on p.agence = a.id
						where d.livrer = false and d.consigner = true and a.societe = societe_ and o.article = article_ and d.date_livraison <= date_;
				end if;
			else
				if(tranche_ is not null and tranche_ >0)then
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id
						where d.livrer = false and d.consigner = true and o.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
				else
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id 
						where d.livrer = false and d.consigner = true and o.article = article_ and d.date_livraison <= date_;
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
  
  -- Function: get_stock_reel(bigint, bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE 
	reel_ double precision; 
	consign_ double precision; 
BEGIN
	reel_ = (select get_stock_reel(art_ ,tranche_ ,depot_ ,agence_ ,societe_ ,date_));
	consign_ = (select get_stock_consigne(art_ ,tranche_ ,depot_ ,agence_ ,societe_ ,date_));
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
ALTER FUNCTION get_stock(bigint, bigint, bigint, bigint, bigint, date)
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
	if(NEW.type_doc = 'FV' or NEW.type_doc = 'BLV' and OLD.mouv_stock = true and OLD.livrer = true) then
		if(NEW.statut = 'VALIDE' and NEW.statut != OLD.statut) then
			select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
				on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
				where e.id = NEW.entete_doc;
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
				RAISE NOTICE 'cont_ : %',cont_.id; 
				select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = dep_.depot and tranche = dep_.tranche and article = arts_.id and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, dep_.depot, dep_.tranche, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(arts_.id, dep_.depot, dep_.tranche, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
				end if;	
			end loop;
		elsif(OLD.statut = 'VALIDE' and NEW.statut != 'VALIDE')then
			select into dep_ h.depot as depot, h.tranche as tranche from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
			on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
			where e.id = OLD.entete_doc;
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = dep_.depot and tranche = dep_.tranche and article = arts_.id and mouvement = 'S';
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

  
  
  
  -- Function: update_doc_achats()

-- DROP FUNCTION update_doc_achats();

CREATE OR REPLACE FUNCTION update_doc_achats()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
	depot_ bigint;
	tranche_ bigint;
BEGIN
	if(NEW.type_doc = 'FA' or NEW.type_doc = 'BLA' and OLD.mouv_stock = true and OLD.livrer = true)then
		if(NEW.statut = 'VALIDE' and NEW.statut != OLD.statut)then
			for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
			loop
				select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and article = arts_.id and mouvement = 'E';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
					end if;
				else
					result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
				end if;	
			end loop;
		elsif(OLD.statut = 'VALIDE' and NEW.statut != 'VALIDE')then
			for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
			loop
				select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E';
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
ALTER FUNCTION update_doc_achats()
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
	select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = NEW.article;
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			if(dep_ is not null)then
				result = (select valorisation_stock(arts_.id, dep_.depot, dep_.tranche, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
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
	select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = NEW.article;
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			if(dep_ is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = dep_.depot and article = arts_.id and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, dep_.depot, dep_.tranche, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(arts_.id, dep_.depot, dep_.tranche, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
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
	select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = OLD.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = arts_.id and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id = mouv_;
					result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_attendu, cout_entree = NEW.pua_attendu where id = mouv_;
				end if;
			else
				result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
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

  
  -- Function: insert_contenu_doc_achat()

-- DROP FUNCTION insert_contenu_doc_achat();

CREATE OR REPLACE FUNCTION insert_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = NEW.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock from yvs_com_doc_achats where id = NEW.doc_achat;
	if(doc_.type_doc = 'FA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu, (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_achat()
  OWNER TO postgres;

  
  
  -- Function: get_pua_total(bigint)

-- DROP FUNCTION get_pua_total(bigint);

CREATE OR REPLACE FUNCTION get_puv(article_ bigint, client_ bigint, depot_ bigint, point_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	puv_ double precision;

BEGIN
	if(client_ is not null and client_ > 0)then
		select into puv_ t.puv from yvs_com_categorie_tarifaire_client t inner join yvs_com_article a on a.id = t.article inner join yvs_com_client c on t.categorie = c.categorie where c.id = client_ and t.article = article_;
	end if;
	if(puv_ is null or puv_ < 1)then
		select into puv_ t.puv_minimal from yvs_base_plan_tarifaire_article t where t.article = article_ and actif = true;
		if(puv_ is null or puv_ <1)then
			select into puv_ pr from yvs_base_article_depot where depot = depot_ and article = article_; 
			if(puv_ is null or puv_ <1)then
				select into puv_ puv from yvs_articles where id = article_; 
				if(puv_ is null or puv_ <1)then
					puv_ = 0;
				end if;
			end if;
		end if;
	end if;
	return puv_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_puv(bigint, bigint, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_puv(bigint, bigint, bigint, bigint) IS 'retourne le prix d''achat d'' article';



-- Function: get_pua(bigint)

-- DROP FUNCTION get_pua(bigint);

CREATE OR REPLACE FUNCTION get_pua(article_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pua_ double precision;

BEGIN
	select into pua_ c.pua_attendu from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat inner join yvs_com_article a on a.id = c.article where a.article = article_ order by d.date_doc desc limit 1;
	if(pua_ is null)then
		select into pua_ pua from yvs_articles where id = article_;
		if(pua_ is null)then
			pua_ = 0;
		end if;
	end if;
	return pua_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pua(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pua(bigint) IS 'retourne le prix d''achat d'' article';



-- Function: get_pua_total(bigint)

-- DROP FUNCTION get_puv(bigint, bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION get_remise(article_ bigint, client_ bigint, depot_ bigint, point_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	remise_ double precision;

BEGIN
	if(client_ is not null and client_ > 0)then
		select into remise_ t.remise from yvs_com_categorie_tarifaire_client t inner join yvs_com_article a on a.id = t.article inner join yvs_com_client c on t.categorie = c.categorie where c.id = client_ and t.article = article_;
	end if;
	if(remise_ is null or remise_ < 1)then
		select into remise_ t.remise from yvs_base_plan_tarifaire_article t where t.article = article_ and actif = true;
		if(remise_ is null or remise_ <1)then
			select into remise_ remise from yvs_articles where id = article_; 
			if(remise_ is null or remise_ <1)then
				remise_ = 0;
			end if;
		end if;
	end if;
	return remise_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise(bigint, bigint, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise(bigint, bigint, bigint, bigint) IS 'retourne la remise d'' article';



CREATE OR REPLACE FUNCTION get_ristourne(article_ bigint, client_ bigint, depot_ bigint, point_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	ristourne_ double precision;

BEGIN
	select into ristourne_ t.ristourne from yvs_base_plan_tarifaire_article t where t.article = article_ and actif = true;
	if(ristourne_ is null or ristourne_ <1)then
		ristourne_ = 0;
	end if;
	return ristourne_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ristourne(bigint, bigint, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ristourne(bigint, bigint, bigint, bigint) IS 'retourne la ristourne d'' article';


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
			select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
				inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id
				where d.livrer = false and d.consigner = true and d.depot_livrer = depot_ and o.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
		else
			select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
				inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id 
				where d.livrer = false and d.consigner = true and d.depot_livrer = depot_ and o.article = article_ and d.date_livraison <= date_;
		end if;
	else
		if(agence_ is not null and agence_ >0)then
			if(tranche_ is not null and tranche_ > 0)then
				select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
					where d.livrer = false and d.consigner = true and p.agence = agence_ and o.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
			else
				select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
					where d.livrer = false and d.consigner = true and p.agence = agence_ and o.article = article_ and d.date_livraison <= date_;
			end if;
		else
			if(societe_ is not null and societe_ >0)then
				if(tranche_ is not null and tranche_ >0)then
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
						inner join yvs_agences a on p.agence = a.id
						where d.livrer = false and d.consigner = true and a.societe = societe_ and c.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
				else
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
						inner join yvs_agences a on p.agence = a.id
						where d.livrer = false and d.consigner = true and a.societe = societe_ and o.article = article_ and d.date_livraison <= date_;
				end if;
			else
				if(tranche_ is not null and tranche_ >0)then
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id
						where d.livrer = false and d.consigner = true and o.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
				else
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_article o on c.article = o.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id 
						where d.livrer = false and d.consigner = true and o.article = article_ and d.date_livraison <= date_;
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

  
  
  -- Function: insert_creneau_horaire()

-- DROP FUNCTION insert_creneau_horaire();

CREATE OR REPLACE FUNCTION insert_creneau_horaire()
  RETURNS trigger AS
$BODY$    
DECLARE
	type_ record;
BEGIN
	select into type_ actif from yvs_grh_tranche_horaire where id = NEW.tranche;
	if(type_.actif = false)then
		NEW.actif = false;
	end if;
	return new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_creneau_horaire()
  OWNER TO postgres;
  
  
-- Function: delete_pointage()

-- DROP FUNCTION delete_pointage();

CREATE OR REPLACE FUNCTION delete_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE 
	HS_ DOUBLE PRECISION default 0;
	HE_ DOUBLE PRECISION default 0;
	OLD_somme_ DOUBLE PRECISION default 0;
    
BEGIN
	if (OLD.valider = true and OLD.actif = true and OLD.heure_sortie is not null) then
		HS_ = ((select extract(hour from OLD.heure_sortie)) + ((select extract(minute from OLD.heure_sortie))/60));
		if(HS_ is null)then
			HS_ = 0;
		end if;
		if(OLD.heure_entree is not null)then
			HE_ = ((select extract(hour from OLD.heure_entree)) + ((select extract(minute from OLD.heure_entree))/60));
		end if;
		if(HE_ is null)then
			HE_ = 0;
		end if;
		OLD_somme_ = HS_ - HE_;
		if(OLD_somme_<0)then
			OLD_somme_ = OLD_somme_ + 24;
		end if;
		if(OLD.compensation_heure = false and OLD.heure_supplementaire = false) then
			update yvs_grh_presence set total_presence = total_presence - OLD_somme_ where id = OLD.presence;
		else
			IF(OLD.heure_supplementaire = true) THEN
				update yvs_grh_presence set total_heure_sup = total_heure_sup - OLD_somme_ where id = OLD.presence;
			END IF;
			IF(OLD.compensation_heure = true) THEN
				update yvs_grh_presence set total_heure_compensation = total_heure_compensation - OLD_somme_ where id = OLD.presence;
			END IF;
		end if;
	end if;
	return OLD ;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_pointage()
  OWNER TO postgres;

  
  
-- Function: update_pointage()

-- DROP FUNCTION update_pointage();

CREATE OR REPLACE FUNCTION update_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	NEW_somme_ DOUBLE PRECISION default 0;
	OLD_somme_ DOUBLE PRECISION default 0;
    
BEGIN
	-- recuperation de l'ancien interval heure fin - heure debut
	if(OLD.heure_sortie is not null and OLD.heure_entree is not null)then
		HS_ = ((select extract(hour from OLD.heure_sortie)) + ((select extract(minute from OLD.heure_sortie))/60));
		HE_ = ((select extract(hour from OLD.heure_entree)) + ((select extract(minute from OLD.heure_entree))/60));
		OLD_somme_ = HS_ - HE_;
		if(OLD_somme_ < 0)then
			OLD_somme_ = OLD_somme_ + 24;
		end if;
	end if;
	if(OLD_somme_ is null)then
		OLD_somme_ = 0;
	end if;
	
	-- recuperation du nouveau interval heure fin - heure debut
	if(NEW.heure_sortie is not null and NEW.heure_entree is not null)then
		HS_ = ((select extract(hour from NEW.heure_sortie)) + ((select extract(minute from NEW.heure_sortie))/60));
		HE_ = ((select extract(hour from NEW.heure_entree)) + ((select extract(minute from NEW.heure_entree))/60));
		NEW_somme_ = HS_ - HE_;
		if(NEW_somme_ < 0)then
			NEW_somme_ = NEW_somme_ + 24;
		end if;
	end if;
	if(NEW_somme_ is null)then
		NEW_somme_ = 0;
	end if;

	-- Verification si l'on modifie 'valider' ou 'actif'
	if(OLD.valider != NEW.valider or OLD.actif != NEW.actif)then -- Si c'est le cas
		-- Verification si valider and actif sont a 'true'
		if(NEW.valider = true and NEW.actif = true)then -- Si c'est le cas on ajout l'heure
			if(OLD.compensation_heure = false and OLD.heure_supplementaire = false) then
				update yvs_grh_presence set total_presence = total_presence + NEW_somme_ - OLD_somme_ where id = OLD.presence;
			else
				if(OLD.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				end if;
				if(OLD.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				end if;
			end if;
		else -- Si ce n'est pas le cas on retirer l'heure
			if(OLD.compensation_heure = false and OLD.heure_supplementaire = false) then
				update yvs_grh_presence set total_presence = total_presence - NEW_somme_ where id = OLD.presence; 
			else
				if(OLD.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup - NEW_somme_ where id = OLD.presence;
				end if;
				if(OLD.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation - NEW_somme_ where id = OLD.presence;
				end if;
			end if;
		end if;
	else -- S ce n'est pas le cas
		-- Verification si l'on modifie 'heure_sortie' ou 'heure_entree'
		if((OLD.heure_sortie != NEW.heure_sortie or OLD.heure_entree != NEW.heure_entree) and (NEW.valider = true and NEW.actif = true))then -- Si c'est le cas
			if(OLD.compensation_heure = false and OLD.heure_supplementaire = false) then
				update yvs_grh_presence set total_presence = total_presence + NEW_somme_ - OLD_somme_ where id = OLD.presence;
			else
				if(OLD.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				end if;
				if(OLD.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				end if;
			end if;
		end if;
	end if;
	
	--recuperation de l'heure de pointage
	if (NEW.heure_sortie is null) then
		NEW.heure_pointage = NEW.heure_entree;
	else
		NEW.heure_pointage = NEW.heure_sortie;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_pointage()
  OWNER TO postgres;

  
-- Function: insert_pointage()

-- DROP FUNCTION insert_pointage();

CREATE OR REPLACE FUNCTION insert_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE 
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	somme_ DOUBLE PRECISION default 0;
    
BEGIN
	if(NEW.valider = true and NEW.actif = true and NEW.heure_entree is not null and NEW.heure_sortie is not null)then
		-- recuperation de l'interval heure fin - heure debut
		HS_ = ((select extract(hour from NEW.heure_sortie)) + ((select extract(minute from NEW.heure_sortie))/60));
		HE_ = ((select extract(hour from NEW.heure_entree)) + ((select extract(minute from NEW.heure_entree))/60));
		somme_ = HS_ - HE_;
		if(somme_ is null)then
			somme_ = 0;
		end if;
		if(somme_ < 0)then
			somme_ = somme_ + 24;
		end if;
		if(NEW.compensation_heure = false and NEW.heure_supplementaire = false) then
			update yvs_grh_presence set total_presence = total_presence + somme_ where id = NEW.presence;
		else
			if(NEW.heure_supplementaire = true) then
				update yvs_grh_presence set total_heure_sup = total_heure_sup + somme_ where id = NEW.presence;
			end if;
			if(NEW.compensation_heure = true) then
				update yvs_grh_presence set total_heure_compensation = total_heure_compensation + somme_ where id = NEW.presence;
			end if;	
		end if;		
	end if;
	if (NEW.heure_sortie is null) then
		NEW.heure_pointage = NEW.heure_entree;
	else
		NEW.heure_pointage = NEW.heure_sortie;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_pointage()
  OWNER TO postgres;

  
-- Function: update_presence()

-- DROP FUNCTION update_presence();

CREATE OR REPLACE FUNCTION update_presence()
  RETURNS trigger AS
$BODY$   
    DECLARE
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	pause_ DOUBLE PRECISION default 0;
	somme_ DOUBLE PRECISION default 0;
	marge_ DOUBLE PRECISION default 0;
	requis_ DOUBLE PRECISION default 0;
	p_ RECORD;
    
BEGIN
	if(NEW.valider != OLD.valider and NEW.valider = true)then	
		if(NEW.marge_approuve is not null)then
			marge_ = ((select extract(hour from NEW.marge_approuve)) + ((select extract(minute from NEW.marge_approuve))/60));
		end if;
		if(marge_ is null)then
			marge_ = 0;
		end if;
		
		if(NEW.duree_pause is not null)then
			pause_ = ((select extract(hour from NEW.duree_pause)) + ((select extract(minute from NEW.duree_pause))/60));
		end if;
		if(pause_ is null)then
			pause_ = 0;
		end if;
		
		HS_ = ((select extract(hour from OLD.heure_debut)) + ((select extract(minute from OLD.heure_debut))/60));
		HE_ = ((select extract(hour from OLD.heure_fin)) + ((select extract(minute from OLD.heure_fin))/60));
		requis_ = HS_ - HE_;
		if(requis_ is null)then
			requis_ = 0;
		end if;
		if(requis_ < 0)then
			requis_ = requis_ + 24;
		end if;
		requis_ = requis_ - pause_;
		
		update yvs_grh_presence set total_presence = 0 where id = OLD.id;
		for p_ in select * from yvs_grh_pointage where presence = OLD.id and valider = true and actif = true and heure_entree is not null and heure_sortie is not null
		loop
			-- recuperation de l'interval heure fin - heure debut
			HS_ = ((select extract(hour from p_.heure_sortie)) + ((select extract(minute from p_.heure_sortie))/60));
			HE_ = ((select extract(hour from p_.heure_entree)) + ((select extract(minute from p_.heure_entree))/60));
			somme_ = HS_ - HE_;
			if(somme_ is null)then
				somme_ = 0;
			end if;
			if(somme_ < 0)then
				somme_ = somme_ + 24;
			end if;

			somme_ = somme_ + marge_;
			if(somme_ > requis_)then
				somme_ = requis_;
			end if;
			
			if(p_.compensation_heure = false and p_.heure_supplementaire = false) then
				update yvs_grh_presence set total_presence = total_presence + somme_ where id = OLD.id;
			else
				if(p_.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup + somme_ where id = OLD.id;
				end if;
				if(p_.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation + somme_ where id = OLD.id;
				end if;	
			end if;		
		end loop;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_presence()
  OWNER TO postgres;

  
-- Function: update_()

-- DROP FUNCTION update_();

CREATE OR REPLACE FUNCTION update_valider_pointage()
  RETURNS boolean AS
$BODY$   
DECLARE 
	p_ record;
	o_ record;  
	date_debut_ timestamp;
	date_fin_ timestamp;
BEGIN
	for p_ in select id, date_debut, heure_debut, date_fin, heure_fin from yvs_grh_presence 
	loop
		date_debut_ =  (select ((p_.date_debut || ' ' || p_.heure_debut)::timestamp));
		date_fin_ =  (select ((p_.date_fin || ' ' || p_.heure_fin)::timestamp));
		if(p_.heure_debut<p_.heure_fin)then
			date_fin_ = date_fin_ + interval '1 day';
		end if;
		RAISE NOTICE 'date_debut_ : %',date_debut_ || ' -- date_fin_ : '||date_fin_; 
		
		for o_ in select id, heure_entree, heure_sortie from yvs_grh_pointage where presence = p_.id
		loop
			if(((date_debut_ <= o_.heure_entree) and (o_.heure_entree <= date_fin_)) and ((date_debut_ <= o_.heure_sortie) and (o_.heure_sortie <= date_fin_)))then
				update yvs_grh_pointage set valider = true where id = o_.id;
			else
				update yvs_grh_pointage set valider = false where id = o_.id;
			end if;
		end loop;
	end loop;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_valider_pointage()
  OWNER TO postgres;




-- Function: update_()

-- DROP FUNCTION update_();

CREATE OR REPLACE FUNCTION update_()
  RETURNS boolean AS
$BODY$   
DECLARE 
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	somme_ DOUBLE PRECISION default 0;
	pause_ DOUBLE PRECISION default 0;
	marge_ DOUBLE PRECISION default 0;
	requis_ DOUBLE PRECISION default 0;
	p_ RECORD;
	o_ RECORD;
BEGIN
	for o_ in select * from yvs_grh_presence
	loop
		if(o_.marge_approuve is not null)then
			marge_ = ((select extract(hour from o_.marge_approuve)) + ((select extract(minute from o_.marge_approuve))/60));
		end if;
		if(marge_ is null)then
			marge_ = 0;
		end if;
		
		if(o_.duree_pause is not null)then
			pause_ = ((select extract(hour from o_.duree_pause)) + ((select extract(minute from o_.duree_pause))/60));
		end if;
		if(pause_ is null)then
			pause_ = 0;
		end if;
		
		HS_ = ((select extract(hour from o_.heure_debut)) + ((select extract(minute from o_.heure_debut))/60));
		HE_ = ((select extract(hour from o_.heure_fin)) + ((select extract(minute from o_.heure_fin))/60));
		requis_ = HS_ - HE_;
		if(requis_ is null)then
			requis_ = 0;
		end if;
		if(requis_ < 0)then
			requis_ = requis_ + 24;
		end if;
		requis_ = requis_ - pause_;
		
		update yvs_grh_presence set total_presence = 0 where id = o_.id;
		for p_ in select * from yvs_grh_pointage where presence = o_.id and valider = true and actif = true and heure_entree is not null and heure_sortie is not null
		loop
			-- recuperation de l'interval heure fin - heure debut
			HS_ = ((select extract(hour from p_.heure_sortie)) + ((select extract(minute from p_.heure_sortie))/60));
			HE_ = ((select extract(hour from p_.heure_entree)) + ((select extract(minute from p_.heure_entree))/60));
			somme_ = HS_ - HE_;
			if(somme_ is null)then
				somme_ = 0;
			end if;
			if(somme_ < 0)then
				somme_ = somme_ + 24;
			end if;
			
			somme_ = somme_ + marge_;
			if(somme_ > requis_)then
				somme_ = requis_;
			end if;
			
			if(p_.compensation_heure = false and p_.heure_supplementaire = false) then			
				update yvs_grh_presence set total_presence = total_presence + somme_ where id = o_.id;
			else
				if(p_.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup + somme_ where id = o_.id;
				end if;
				if(p_.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation + somme_ where id = o_.id;
				end if;	
			end if;		
		end loop;
	end loop;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_()
  OWNER TO postgres;
select update_()


-- Function: alter_action_colonne_key(character varying, boolean, boolean)

-- DROP FUNCTION alter_action_colonne_key(character varying, boolean, boolean);

CREATE OR REPLACE FUNCTION alter_action_colonne_key(table_ character varying,cible_ character varying, update_ boolean, delete_ boolean)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	constraint_ record;	
	query_ character varying;
	compteur_ integer default 0;
	
BEGIN	
	-- Debut de la modification des action cles primaires 	
	compteur_ = 0;
	RAISE NOTICE 'Table° : %',table_;
	
	-- Recherche de la clé secondaire liée a la clé primaire donnée
	FOR constraint_ IN SELECT k.CONSTRAINT_NAME, k.TABLE_NAME, k.COLUMN_NAME, f.TABLE_NAME AS TABLE_NAME_, f.COLUMN_NAME AS COLUMN_NAME_ 
		FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
		INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
		INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
		WHERE k.table_schema = 'public' AND k.TABLE_NAME = cible_ AND c.CONSTRAINT_TYPE = 'FOREIGN KEY' AND f.TABLE_NAME = table_
	LOOP
		compteur_ = compteur_ + 1;
		--Suppression de la contrainte
		execute 'ALTER TABLE public.'||cible_||' DROP CONSTRAINT '||constraint_.CONSTRAINT_NAME;
		
		query_ = 'ALTER TABLE public.'||cible_||'
				ADD CONSTRAINT '||cible_||'_'||constraint_.COLUMN_NAME||'_fkey FOREIGN KEY ('||constraint_.COLUMN_NAME||')
				REFERENCES public.'||table_||' ('||constraint_.COLUMN_NAME_||') MATCH SIMPLE';
		if(update_ = true and delete_ = true)then
			query_ = query_ || ' ON UPDATE CASCADE ON DELETE CASCADE';
		elsif(update_ = true and delete_ = false)then
			query_ = query_ || ' ON UPDATE CASCADE ON DELETE NO ACTION';
		elsif(update_ = false and delete_ = true)then
			query_ = query_ || ' ON UPDATE NO ACTION ON DELETE CASCADE';
		elsif(update_ = false and delete_ = false)then
			query_ = query_ || ' ON UPDATE NO ACTION ON DELETE NO ACTION';
		end if;
		
		RAISE NOTICE ' constraint N° : %',compteur_ ||' -- table : '||cible_||' /colonne : '||constraint_.COLUMN_NAME||' /contrainte : '||constraint_.CONSTRAINT_NAME;
		execute query_;
	END LOOP;
	RAISE NOTICE '%','';
	return true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_action_colonne_key(character varying, character varying, boolean, boolean)
  OWNER TO postgres;

select alter_action_colonne_key('yvs_com_doc_achats', 'yvs_com_contenu_doc_achat', true, true);
select alter_action_colonne_key('yvs_com_doc_stocks', 'yvs_com_contenu_doc_stock', true, true);
select alter_action_colonne_key('yvs_com_doc_ventes', 'yvs_com_contenu_doc_vente', true, true);