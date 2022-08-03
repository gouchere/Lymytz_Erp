-- Function: insert_categorie_client()

-- DROP FUNCTION insert_categorie_client();

CREATE OR REPLACE FUNCTION insert_categorie_client()
  RETURNS trigger AS
$BODY$
DECLARE
    qte REAL;
    
BEGIN
	if(NEW.defaut = true)then
		update yvs_base_plan_tarifaire set defaut = false where societe = NEW.societe and id != NEW.id;
	end if;
	return new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_categorie_client()
  OWNER TO postgres;

  
  -- Function: update_categorie_client()

-- DROP FUNCTION update_categorie_client();

CREATE OR REPLACE FUNCTION update_categorie_client()
  RETURNS trigger AS
$BODY$
DECLARE
    qte REAL;
    
BEGIN
	if(NEW.defaut = true)then
		update yvs_base_plan_tarifaire set defaut = false where societe = OLD.societe and id != OLD.id;
	end if;
	return new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_categorie_client()
  OWNER TO postgres;
  
  
  -- Function: get_pua(bigint)

-- DROP FUNCTION get_pua(bigint);

CREATE OR REPLACE FUNCTION get_pua(article_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pua_ double precision;

BEGIN
	select into pua_ c.pua_attendu from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat where c.article = article_ order by d.date_doc desc limit 1;
	if(pua_ is null)then
		select into pua_ pua from yvs_base_articles where id = article_;
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


-- Function: get_puv(bigint, bigint, bigint, bigint)

-- DROP FUNCTION get_puv(bigint, bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION get_puv(article_ bigint, client_ bigint, depot_ bigint, point_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	puv_ double precision;

BEGIN
	if(client_ is not null and client_ > 0)then
		select into puv_ t.puv from yvs_base_plan_tarifaire t inner join yvs_com_client c on t.categorie = c.categorie where c.id = client_ and t.article = article_;
	end if;
	if(puv_ is null or puv_ < 1)then
		select into puv_ t.puv_minimal from yvs_base_plan_tarifaire_article t where t.article = article_ and actif = true;
		if(puv_ is null or puv_ <1)then
			select into puv_ pr from yvs_base_article_depot where depot = depot_ and article = article_; 
			if(puv_ is null or puv_ <1)then
				select into puv_ puv from yvs_base_articles where id = article_; 
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


-- Function: get_remise(bigint, bigint, bigint, bigint)

-- DROP FUNCTION get_remise(bigint, bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION get_remise(article_ bigint, client_ bigint, depot_ bigint, point_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	remise_ double precision;

BEGIN
	if(client_ is not null and client_ > 0)then
		select into remise_ t.remise from yvs_base_plan_tarifaire t inner join yvs_com_client c on t.categorie = c.categorie where c.id = client_ and t.article = article_;
	end if;
	if(remise_ is null or remise_ < 1)then
		select into remise_ t.remise from yvs_base_plan_tarifaire_article t where t.article = article_ and actif = true;
		if(remise_ is null or remise_ <1)then
			select into remise_ remise from yvs_base_articles where id = article_; 
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
				inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id
				where d.livrer = false and d.consigner = true and d.depot_livrer = depot_ and c.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
		else
			select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
				inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id 
				where d.livrer = false and d.consigner = true and d.depot_livrer = depot_ and c.article = article_ and d.date_livraison <= date_;
		end if;
	else
		if(agence_ is not null and agence_ >0)then
			if(tranche_ is not null and tranche_ > 0)then
				select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
					where d.livrer = false and d.consigner = true and p.agence = agence_ and c.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
			else
				select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
					where d.livrer = false and d.consigner = true and p.agence = agence_ and c.article = article_ and d.date_livraison <= date_;
			end if;
		else
			if(societe_ is not null and societe_ >0)then
				if(tranche_ is not null and tranche_ >0)then
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
						inner join yvs_agences a on p.agence = a.id
						where d.livrer = false and d.consigner = true and a.societe = societe_ and c.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
				else
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id inner join yvs_base_depots p on d.depot_livrer = p.id
						inner join yvs_agences a on p.agence = a.id
						where d.livrer = false and d.consigner = true and a.societe = societe_ and c.article = article_ and d.date_livraison <= date_;
				end if;
			else
				if(tranche_ is not null and tranche_ >0)then
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_horaire h on u.creneau = h.id
						where d.livrer = false and d.consigner = true and c.article = article_ and d.date_livraison <= date_ and h.tranche = tranche_;
				else
					select into stock_ SUM(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id 
						where d.livrer = false and d.consigner = true and c.article = article_ and d.date_livraison <= date_;
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

  
  
  -- Function: get_taxe_total_achat(bigint)

-- DROP FUNCTION get_taxe_total_achat(bigint);

CREATE OR REPLACE FUNCTION get_taxe_total_achat(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	contenu_ record;
	taxes_ record;
	remise_ double precision default 0;
	somme_ double precision default 0;
	taux_ double precision default 0;

BEGIN
	select into contenu_ c.article, c.doc_achat, c.quantite_attendu as qte, c.pua_attendu as pua, c.remise_attendu as rem , d.categorie_comptable
		from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d
		on d.id = c.doc_achat where c.id = id_;

	somme_ =  contenu_.qte * contenu_.pua;
	if(somme_ is null)then
		somme_ = 0;
	end if;
	remise_ = somme_ * (contenu_.rem / 100);
	if(remise_ is null)then
		remise_ = 0;
	end if;
	
	for taxes_ in select t.taux, ta.app_remise from yvs_base_article_categorie_comptable_taxe ta 
		inner join yvs_base_taxes t on ta.taxe = t.id inner join yvs_base_article_categorie_comptable c on ta.article_categorie = c.id
		where ta.actif = true and c.categorie = contenu_.categorie_comptable and c.article = contenu_.article
	loop
		taux_ = taux_ + (somme_ * (taxes_.taux / 100));
		if(taxes_.app_remise = true)then
			taux_ = taux_ + (remise_ * (taxes_.taux / 100));
		end if;
	end loop;
	
	if(taux_ is null)then
		taux_ = 0;
	end if;
    return taux_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_taxe_total_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_taxe_total_achat(bigint) IS 'retourne le montant total des taxes d'' article';


-- Function: get_taxe_total_vente(bigint)

-- DROP FUNCTION get_taxe_total_vente(bigint);

CREATE OR REPLACE FUNCTION get_taxe_total_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	contenu_ record;
	taxes_ record;
	remise_ double precision default 0;
	somme_ double precision default 0;
	taux_ double precision default 0;

BEGIN
	select into contenu_ c.article, c.doc_vente, c.quantite as qte, c.prix, c.remise_art, c.remise_cat, d.categorie_comptable
		from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id where c.id = id_;

	somme_ =  contenu_.qte * contenu_.prix;
	if(somme_ is null)then
		somme_ = 0;
	end if;
	remise_ = contenu_.remise_art + contenu_.remise_cat;
	if(remise_ is null)then
		remise_ = 0;
	end if;

	for taxes_ in select t.taux, ta.app_remise from yvs_base_article_categorie_comptable_taxe ta 
		inner join yvs_base_taxes t on ta.taxe = t.id inner join yvs_base_article_categorie_comptable c on ta.article_categorie = c.id
		where ta.actif = true and c.categorie = contenu_.categorie_comptable and c.article = contenu_.article
	loop
		taux_ = taux_ + (somme_ * (taxes_.taux / 100));
		if(taxes_.app_remise = true)then
			taux_ = taux_ + (remise_ * (taxes_.taux / 100));
		end if;
	end loop;
	
	if(taux_ is null)then
		taux_ = 0;
	end if;
    return taux_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_taxe_total_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_taxe_total_vente(bigint) IS 'retourne le montant total des taxes d'' article';


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
		select into reste c.pua_attendu from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat where c.article = article_ order by d.date_doc desc limit 1;
		if(reste is null)then
			select into reste pua from yvs_base_articles where id = article_;
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


-- Function: valorisation_stock(bigint, bigint, bigint, double precision, double precision, character varying, bigint, character varying, date)

-- DROP FUNCTION valorisation_stock(bigint, bigint, bigint, double precision, double precision, character varying, bigint, character varying, date);

CREATE OR REPLACE FUNCTION valorisation_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE 
	meth_ character varying;
	new_cout_ double precision default 0;
BEGIN
	select into meth_ methode_val from yvs_base_articles where id = article_;
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



-- Function: delete_contenu_doc_achat()

-- DROP FUNCTION delete_contenu_doc_achat();

CREATE OR REPLACE FUNCTION delete_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	mouv_ bigint;
BEGIN
	select into doc_ id, type_doc, depot_reception as depot, statut, mouv_stock from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = OLD.article and mouvement = 'E';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_contenu_doc_achat()
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
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock, livrer from yvs_com_doc_achats where id = NEW.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			if( doc_.type_doc = 'BLA')then
				result = (select valorisation_stock(NEW.article, doc_.depot, doc_.tranche, NEW.quantite_attendu, (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			else
				result = (select valorisation_stock(NEW.article, doc_.depot, doc_.tranche, NEW.quantite_attendu, (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
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
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock , livrer from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = OLD.article and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id = mouv_;
					if(doc_.type_doc = 'BLA')then
						result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					end if;
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_attendu, cout_entree = NEW.pua_attendu where id = mouv_;
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

  
  
  -- Function: delete_contenu_doc_stock()

-- DROP FUNCTION delete_contenu_doc_stock();

CREATE OR REPLACE FUNCTION delete_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	mouv_ bigint;
BEGIN
	select into doc_ id, type_doc, statut, source, destination from yvs_com_doc_stocks where id = OLD.doc_stock;
	if(doc_.type_doc = 'FT' and doc_.statut = 'VALIDE') then
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = OLD.article and mouvement = 'E';
		if(mouv_ is not null)then
			delete from yvs_base_mouvement_stock where id = mouv_;
		end if;
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = OLD.article and mouvement = 'S';
		if(mouv_ is not null)then
			delete from yvs_base_mouvement_stock where id = mouv_;
		end if;
	elsif(doc_.type_doc = 'SS' and doc_.statut = 'VALIDE') then
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = OLD.article and mouvement = 'S';
		if(mouv_ is not null)then
			delete from yvs_base_mouvement_stock where id = mouv_;
		end if;
	elsif(doc_.type_doc = 'ES' and doc_.statut = 'VALIDE') then
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = OLD.article and mouvement = 'E';
		if(mouv_ is not null)then
			delete from yvs_base_mouvement_stock where id = mouv_;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_contenu_doc_stock()
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
	select into trancheD_ tranche from yvs_com_creneau_horaire where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_horaire where id = doc_.creneau_source;
	
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
	select into trancheD_ tranche from yvs_com_creneau_horaire where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_horaire where id = doc_.creneau_source;
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

  
  
  -- Function: delete_contenu_doc_vente()

-- DROP FUNCTION delete_contenu_doc_vente();

CREATE OR REPLACE FUNCTION delete_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	mouv_ bigint;
	arts_ record;
BEGIN
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, livrer, depot_livrer from yvs_com_doc_ventes where id = OLD.doc_vente;
	
	if(doc_.type_doc = 'FV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = OLD.article and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_contenu_doc_vente()
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
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, livrer, depot_livrer, tranche_livrer from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
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
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, livrer, depot_livrer, tranche_livrer from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
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

  
  -- Function: delete_doc_achats()

-- DROP FUNCTION delete_doc_achats();

CREATE OR REPLACE FUNCTION delete_doc_achats()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
BEGIN
	if((OLD.type_doc = 'FA' or OLD.type_doc = 'BLA') and OLD.statut = 'VALIDE' and OLD.mouv_stock = true) then
		for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
		loop
			
			--Recherche mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and article = cont_.article and mouvement = 'E';
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
ALTER FUNCTION delete_doc_achats()
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
	if(NEW.type_doc = 'FRA' or NEW.type_doc = 'BLA' and OLD.mouv_stock = true and OLD.livrer = true)then
		if(OLD.livrer != NEW.livrer)then
			if(NEW.livrer = true)then
				if(NEW.statut != OLD.statut)then
					if(NEW.statut = 'VALIDE' and 'REGLE' != OLD.statut)then
						for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
						loop
							select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
							
							--Insertion mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and cont_.article and mouvement = 'E';
							if(mouv_ is not null)then
								if(arts_.methode_val = 'FIFO')then
									delete from yvs_base_mouvement_stock where id = mouv_;
									if(NEW.type_doc = 'BLA')then
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
									else
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
									end if;
								else
									update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
								end if;
							else
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							end if;	
						end loop;
					elsif(OLD.statut = 'VALIDE' and (NEW.statut = 'ANNULE' or NEW.statut = 'EDITABLE'))then
						for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
						loop
							
							
							--Recherche mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E';
							if(mouv_ is not null)then
								delete from yvs_base_mouvement_stock where id = mouv_;
							end if;
						end loop;
					end if;
				else
					if(OLD.statut = 'VALIDE')then
						for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
						loop
							select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
							
							--Insertion mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and cont_.article and mouvement = 'E';
							if(mouv_ is not null)then
								if(arts_.methode_val = 'FIFO')then
									delete from yvs_base_mouvement_stock where id = mouv_;
									if(NEW.type_doc = 'BLA')then
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
									else
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
									end if;
								else
									update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
								end if;
							else
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							end if;	
						end loop;
					end if;
				end if;
			else
				for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
				loop
					
					
					--Recherche mouvement stock
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
				end loop;
			end if;
		else
			if(OLD.livrer = true and NEW.statut != OLD.statut)then
				if(NEW.statut = 'VALIDE' and 'REGLE' != OLD.statut)then
					for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
					loop
						select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
						
						--Insertion mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and cont_.article and mouvement = 'E';
						if(mouv_ is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id = mouv_;
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							else
								update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
							end if;
						else
							if(NEW.type_doc = 'BLA')then
								result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
							else
								result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
							end if;
						end if;	
					end loop;
				elsif(OLD.statut = 'VALIDE' and (NEW.statut = 'ANNULE' or NEW.statut = 'EDITABLE'))then
					for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
					loop
						
						
						--Recherche mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E';
						if(mouv_ is not null)then
							delete from yvs_base_mouvement_stock where id = mouv_;
						end if;
					end loop;
				end if;
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_achats()
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
	if(OLD.type_doc = 'FT' and OLD.statut = 'VALIDE') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			
			--Recherche mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
			
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		end loop;
	elsif(OLD.type_doc = 'SS' and OLD.statut = 'VALIDE') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			
			--Recherche mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		end loop;
	elsif(OLD.type_doc = 'ES' and OLD.statut = 'VALIDE') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			
			--Recherche mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
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
ALTER FUNCTION delete_doc_stocks()
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
				
				
				--Insertion mouvement stock
				if(NEW.destination is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and tranche = trancheD_ and article = cont_.article and mouvement = 'E';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;

				if(NEW.source is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and tranche = trancheS_ and article = cont_.article and mouvement = 'S';
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
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and tranche = trancheD_ and article = cont_.article and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
				
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and tranche = trancheS_ and article = cont_.article and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
			end loop;		
		elsif(NEW.type_doc = 'SS' and NEW.statut = 'VALIDE') then
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				
				
				--Insertion mouvement stock
				if(NEW.source is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and tranche = trancheS_ and article = cont_.article and mouvement = 'S';
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
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and tranche = trancheD_ and article = cont_.article and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
				
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and tranche = trancheS_ and article = cont_.article and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
			end loop;
		elsif(NEW.type_doc = 'ES' and NEW.statut = 'VALIDE') then
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				
				
				--Insertion mouvement stock
				if(NEW.destination is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and tranche = trancheD_ and article = cont_.article and mouvement = 'E';
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
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and tranche = trancheD_ and article = cont_.article and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
				
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and tranche = trancheS_ and article = cont_.article and mouvement = 'S';
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
	if((OLD.type_doc = 'FV' or OLD.type_doc = 'BLV') and OLD.statut = 'VALIDE' and OLD.mouv_stock = true and OLD.livrer = true) then
		select into dep_ h.depot from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
			on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
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
		if(OLD.livrer != NEW.livrer)then
			if(NEW.livrer = true and NEW.statut != OLD.statut)then
				if(NEW.statut = 'VALIDE' and 'REGLE' != OLD.statut) then
					select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
						on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
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
					on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
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
			else
				select into dep_ h.depot as depot, h.tranche as tranche from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
				on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
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
		else
			if(OLD.livrer = true and NEW.statut != OLD.statut)then
				if(NEW.statut = 'VALIDE' and OLD.statut != 'REGLE') then
					select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
						on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
						where e.id = NEW.entete_doc;
					for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
					loop
						RAISE NOTICE 'cont_ : %',cont_.id; 
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
					on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
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
		end if;		
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ventes()
  OWNER TO postgres;
  
  
  -- Function: insert_article_()

-- DROP FUNCTION insert_article_();

CREATE OR REPLACE FUNCTION insert_article_()
  RETURNS trigger AS
$BODY$    
DECLARE
	idCat_ bigint;
	action_ character varying;	
	line_ record;
	table_ character varying;	
BEGIN
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	table_=TG_TABLE_NAME;
	IF table_='yvs_base_articles' THEN
	IF (action_='INSERT' and NEW.societe IS NOT NULL) THEN
	  -- relie l'article à toutes les catégories comptable existante
	  for idCat_ IN SELECT  ca.id FROM yvs_base_categorie_comptable ca WHERE ca.societe=NEW.societe
	      loop
		INSERT INTO yvs_base_article_categorie_comptable (article, categorie,compte, actif) VALUES(NEW.id, idCat_,null, true);
	      END loop;
	 ELSIF (action_='UPDATE' and NEW.societe IS NOT NULL) THEN
	  for idCat_ IN SELECT  ca.id FROM yvs_base_categorie_comptable ca WHERE ca.societe=NEW.societe
	      loop
	      SELECT INTO line_ * FROM yvs_base_article_categorie_comptable c WHERE (c.article=NEW.article AND c.categorie=idCat_);
	      IF line_ IS NULL THEN 
		INSERT INTO yvs_base_article_categorie_comptable (article, categorie,compte, actif) VALUES(NEW.id, idCat_,null, true);
	      END IF;
	  END loop;
	 END IF;
    ELSIF table_='yvs_base_categorie_comptable' THEN
	IF (action_='INSERT' and NEW.societe IS NOT NULL) THEN
	  -- relie la catégorie à tous les articles existant
	  for idCat_ IN SELECT  ca.id FROM yvs_base_articles ca INNER JOIN yvs_base_famille_article fa ON ca.famille = fa.id WHERE fa.societe=NEW.societe
	      loop
		INSERT INTO yvs_base_article_categorie_comptable (article, categorie,compte, actif) VALUES(idCat_, NEW.id, null, true);
	      END loop;
	 ELSIF (action_='UPDATE' and NEW.societe IS NOT NULL) THEN
	  for idCat_ IN SELECT  ca.id FROM yvs_base_articles ca INNER JOIN yvs_base_famille_article fa ON ca.famille = fa.id WHERE fa.societe=NEW.societe
	      loop
	      SELECT INTO line_ * FROM yvs_base_article_categorie_comptable c WHERE (c.article=idCat_ AND c.categorie=NEW.id);
	      IF line_ IS NULL THEN 
		INSERT INTO yvs_base_article_categorie_comptable(article, categorie,compte, actif) VALUES(idCat_,NEW.id,null, true);
	      END IF;
	  END loop;
	 END IF;
    END IF;
    RETURN NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_article_()
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
	if(OLD.type_doc = 'FRA' or OLD.type_doc = 'BLA' and NEW.mouv_stock = true)then
		if(OLD.livrer != NEW.livrer)then
			if(NEW.livrer = true)then
				if(NEW.statut != OLD.statut)then
					if(NEW.statut = 'VALIDE' and 'REGLE' != OLD.statut)then
						for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
						loop
							select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
							
							--Insertion mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and cont_.article and mouvement = 'E';
							if(mouv_ is not null)then
								if(arts_.methode_val = 'FIFO')then
									delete from yvs_base_mouvement_stock where id = mouv_;
									if(NEW.type_doc = 'BLA')then
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
									else
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
									end if;
								else
									update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
								end if;
							else
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							end if;	
						end loop;
					elsif(OLD.statut = 'VALIDE' and (NEW.statut = 'ANNULE' or NEW.statut = 'EDITABLE'))then
						for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
						loop
							
							
							--Recherche mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E';
							if(mouv_ is not null)then
								delete from yvs_base_mouvement_stock where id = mouv_;
							end if;
						end loop;
					end if;
				else
					if(OLD.statut = 'VALIDE')then
						for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
						loop
							select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
							
							--Insertion mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and article = cont_.article and mouvement = 'E';
							if(mouv_ is not null)then
								if(arts_.methode_val = 'FIFO')then
									delete from yvs_base_mouvement_stock where id = mouv_;
									if(NEW.type_doc = 'BLA')then
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
									else
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
									end if;
								else
									update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
								end if;
							else
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							end if;	
						end loop;
					end if;
				end if;
			else
				for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
				loop
					
					
					--Recherche mouvement stock
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
				end loop;
			end if;
		else
			if(OLD.livrer = true and NEW.statut != OLD.statut)then
				if(NEW.statut = 'VALIDE' and 'REGLE' != OLD.statut)then
					for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
					loop
						select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
						
						--Insertion mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and article = cont_.article and mouvement = 'E';
						if(mouv_ is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id = mouv_;
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							else
								update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
							end if;
						else
							if(NEW.type_doc = 'BLA')then
								result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
							else
								result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
							end if;
						end if;	
					end loop;
				elsif(OLD.statut = 'VALIDE' and (NEW.statut = 'ANNULE' or NEW.statut = 'EDITABLE'))then
					for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
					loop
						
						
						--Recherche mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E';
						if(mouv_ is not null)then
							delete from yvs_base_mouvement_stock where id = mouv_;
						end if;
					end loop;
				end if;
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_achats()
  OWNER TO postgres;

  
  -- Function: get_cout_achat_contenu(bigint)

-- DROP FUNCTION get_cout_achat_contenu(bigint);

CREATE OR REPLACE FUNCTION get_cout_total_achat_contenu(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	record_ record;
	pua double precision default 0;
	cout double precision default 0;
	total_ double precision;

BEGIN
	pua = (select get_pua_total(id_));
	if(pua is null)then
		pua = 0;
	end if;
	for record_ in select montant from yvs_com_cout_additionel where contenu = id_
	loop
		if(record_.montant is null)then
			record_.montant = 0;
		end if;
		cout = cout + record_.montant;
	end loop;
	if(cout is null)then
		cout = 0;
	end if;
	total_ = pua + cout;
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_cout_total_achat_contenu(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_cout_total_achat_contenu(bigint) IS 'retourne le cout d''achat d'' article';


-- Function: get_cout_achat_contenu(bigint)

-- DROP FUNCTION get_cout_achat_contenu(bigint);

CREATE OR REPLACE FUNCTION get_cout_achat_contenu(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	record_ record;
	pua double precision default 0;
	cout double precision default 0;
	total_ double precision;

BEGIN
	select into pua pua_attendu from yvs_com_contenu_doc_achat where id = id_;
	if(pua is null)then
		pua = 0;
	end if;
	for record_ in select montant from yvs_com_cout_additionel where contenu = id_
	loop
		if(record_.montant is null)then
			record_.montant = 0;
		end if;
		cout = cout + record_.montant;
	end loop;
	if(cout is null)then
		cout = 0;
	end if;
	total_ = pua + cout;
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_cout_achat_contenu(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_cout_achat_contenu(bigint) IS 'retourne le cout d''achat d'' article';


-- Function: insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)

-- DROP FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date);

CREATE OR REPLACE FUNCTION insert_mouvement_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, coutentree_ double precision, cout_ double precision, parent_ bigint, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE
    
BEGIN
	if(parent_ is not null)then
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, '', depot_, tranche_, parent_, coutentree_, cout_, current_timestamp);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, '', depot_, parent_, coutentree_, cout_, current_timestamp);
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
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
	if(NEW.type_doc = 'FRA' or NEW.type_doc = 'BLA')then
		if(OLD.livrer != NEW.livrer)then
			if(NEW.livrer = true)then
				if(NEW.statut != OLD.statut)then
					if(NEW.statut = 'VALIDE' and 'REGLE' != OLD.statut)then
						for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
						loop
							select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
							
							--Insertion mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and cont_.article and mouvement = 'E';
							if(mouv_ is not null)then
								if(arts_.methode_val = 'FIFO')then
									delete from yvs_base_mouvement_stock where id = mouv_;
									if(NEW.type_doc = 'BLA')then
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
									else
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
									end if;
								else
									update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
								end if;
							else
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							end if;	
						end loop;
					elsif(OLD.statut = 'VALIDE' and 'REGLE' != NEW.statut)then
						for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
						loop		
							
							--Recherche mouvement stock
							for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E'
							loop
								delete from yvs_base_mouvement_stock where id = mouv_;
							end loop;
						end loop;
					end if;
				else
					if(OLD.statut = 'VALIDE')then
						for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
						loop
							select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
							
							--Insertion mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and article = cont_.article and mouvement = 'E';
							if(mouv_ is not null)then
								if(arts_.methode_val = 'FIFO')then
									delete from yvs_base_mouvement_stock where id = mouv_;
									if(NEW.type_doc = 'BLA')then
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
									else
										result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
									end if;
								else
									update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
								end if;
							else
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							end if;	
						end loop;
					end if;
				end if;
			else
				for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
				loop					
					
					--Recherche mouvement stock
					for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E'
					loop
						delete from yvs_base_mouvement_stock where id = mouv_;
					end loop;
				end loop;
			end if;
		else
			if(OLD.livrer = true and NEW.statut != OLD.statut)then
				if(NEW.statut = 'VALIDE' and 'REGLE' != OLD.statut)then
					for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
					loop
						select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
						
						--Insertion mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and article = cont_.article and mouvement = 'E';
						if(mouv_ is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id = mouv_;
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							else
								update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
							end if;
						else
							if(NEW.type_doc = 'BLA')then
								result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
							else
								result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
							end if;
						end if;	
					end loop;
				elsif(OLD.statut = 'VALIDE' and 'REGLE' != NEW.statut)then
					for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
					loop						
						
						--Recherche mouvement stock
						for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E'
						loop
							delete from yvs_base_mouvement_stock where id = mouv_;
						end loop;
					end loop;
				end if;
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_achats()
  OWNER TO postgres;
  
  
  
  -- Function: delete_doc_achats()

-- DROP FUNCTION delete_doc_achats();

CREATE OR REPLACE FUNCTION delete_doc_achats()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
BEGIN
	if((OLD.type_doc = 'FRA' or OLD.type_doc = 'BLA') and OLD.statut = 'VALIDE') then
		for cont_ in select id, article , quantite_attendu as qte from yvs_com_contenu_doc_achat where doc_achat = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and mouvement = 'E'
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
ALTER FUNCTION delete_doc_achats()
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
	if(OLD.type_doc = 'FT' and OLD.statut = 'VALIDE') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	elsif(OLD.type_doc = 'SS' and OLD.statut = 'VALIDE') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'S'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	elsif(OLD.type_doc = 'ES' and OLD.statut = 'VALIDE') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E'
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
				for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E'
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
 