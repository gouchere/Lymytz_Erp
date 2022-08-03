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
		if(NEW.statut = 'VALIDE' and 'REGLE' != OLD.statut) then
			select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
				on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
				where e.id = NEW.entete_doc;
			for cont_ in select id, article , quantite as qte,prix from yvs_com_contenu_doc_vente where doc_vente = OLD.id
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
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
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

  
  -- Function: equilibre_vente(bigint)

-- DROP FUNCTION equilibre_vente(bigint);

CREATE OR REPLACE FUNCTION equilibre_vente(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	av_ double precision default 0;
	contenu_ record;
	qte_ double precision default 0;
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

BEGIN
	-- Equilibre de l'etat reglé
	ttc_ = (select get_ttc_vente(id_));
	select into av_ sum(montant) from yvs_compta_caisse_piece_vente where vente = id_ and statut_piece = 'P';
	if(av_ is null)then
		av_ = 0;
	end if;

	-- Equilibre de l'etat livré
	for contenu_ in select article, sum(quantite) as qte from yvs_com_contenu_doc_vente where doc_vente = id_ group by article
	loop
		in_ = true;
		select into qte_ sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id where d.document_lie = id_ and c.article = contenu_.article and d.statut = 'VALIDE';
		if(qte_ is null)then
			qte_ = 0;
		end if;
		if(qte_ < contenu_.qte)then
			if(qte_ > 0)then
				encours = true;
				exit;
			end if;
			correct = false;
		end if;
	end loop;
	if(in_)then
		if(av_>=ttc_)then
			update yvs_com_doc_ventes set statut_regle = 'P' where id = id_;
		elsif (av_ > 0) then
			update yvs_com_doc_ventes set statut_regle = 'R' where id = id_;
		else
			update yvs_com_doc_ventes set statut_regle = 'W' where id = id_;
		end if;
		
		if(correct)then
			update yvs_com_doc_ventes set statut_livre = 'L' where id = id_;
		else
			if(encours)then
				update yvs_com_doc_ventes set statut_livre = 'R' where id = id_;
			else
				update yvs_com_doc_ventes set statut_livre = 'W' where id = id_;
			end if;
		end if;	
	else
		update yvs_com_doc_ventes set statut_regle = 'W', statut_livre = 'W' where id = id_;
	end if;	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';


CREATE OR REPLACE FUNCTION get_remise(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	remise_ double precision;
	garde_ double precision;
	data_ record;
	tarif_ record;
	valeur_ double precision default 0;

BEGIN
	valeur_ = qte_ * prix_;	
	if(client_ is not null and client_ > 0)then
		for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
		loop
			if(data_.permanent is false)then
				if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
					select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
							garde_ = (tarif_.remise /100);
						else
							garde_ = tarif_.remise;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL) then
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
								remise_ = (tarif_.remise /100);
							else
								remise_ = tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ < 1)then
							remise_ = garde_;
						end if;
					end if;
					exit;
				end if;
			else
				select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
				if(tarif_.id is not null)then
					if(tarif_.nature_remise = 'TAUX')then
						garde_ = valeur_ * (tarif_.remise /100);
						garde_ = (tarif_.remise /100);
						RAISE NOTICE 'Remise %',garde_;
					else
						garde_ = tarif_.remise;
					end if;
					select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
					if(tarif_.id IS NOT NULL) then
						if(tarif_.nature_remise = 'TAUX')then
							remise_ = valeur_ * (tarif_.remise /100);
							remise_ = (tarif_.remise /100);
						else
							remise_ = tarif_.remise;
						end if;
					end if;
					if(remise_ is null or remise_ <= 0)then
						remise_ = garde_;
					end if;
					exit;
				end if;
				--exit;
			end if;
		end loop;
	end if;
	if(remise_ is null or remise_ <= 0)then
		select into tarif_ * from yvs_base_article_point where point = point_ and article = article_ and actif is true limit 1;
		if(tarif_.id IS NOT NULL) then
			if(tarif_.nature_remise = 'TAUX')then
				remise_ = valeur_ * (tarif_.remise /100);
				remise_ = (tarif_.remise /100);
			else
				remise_ = tarif_.remise;
			end if;
		end if;
		if(remise_ is null or remise_ <= 0)then
			select into remise_ remise from yvs_base_articles where id = article_; 
			if(remise_ is not null) then
				remise_ = valeur_ * (remise_/100);
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
ALTER FUNCTION get_remise(bigint, double precision, double precision, bigint, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise(bigint, double precision, double precision, bigint, bigint, date) IS 'retourne la remise d'' article';


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
	if(OLD.type_doc = 'FT' and OLD.statut = 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	elsif(OLD.type_doc = 'SS' and OLD.statut = 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'S'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	elsif(OLD.type_doc = 'ES' and OLD.statut = 'V') then
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
	select into trancheD_ tranche from yvs_com_creneau_depot where id = NEW.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = NEW.creneau_source;
	
	if(NEW.statut != OLD.statut) then	-- si le statut du document a changé
		if(NEW.type_doc = 'FT' and NEW.statut = 'V') then
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				--Insertion mouvement stock
				if(NEW.destination is not null)then  --traitement de l'entrée dans le dépôt de destination
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;
				if(NEW.source is not null)then       --traitement de la sortie dans le dépôt source
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
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
						delete from yvs_base_mouvement_stock where id = mouv_;
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
						delete from yvs_base_mouvement_stock where id = mouv_;
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
		end if;
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
	if(doc_.statut = 'V') then
		if(doc_.type_doc = 'FT') then
			if(doc_.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = NEW.article and mouvement = 'E';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix where id = mouv_;
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
						result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix where id = mouv_;
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
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix where id = mouv_;
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
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix where id = mouv_;
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
	if(doc_.statut = 'V')then
		if(doc_.type_doc = 'FT') then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = OLD.article and mouvement = 'E';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = OLD.article and mouvement = 'S';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		elsif(doc_.type_doc = 'SS') then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = OLD.article and mouvement = 'S';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		elsif(doc_.type_doc = 'ES') then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = OLD.article and mouvement = 'E';
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
ALTER FUNCTION delete_contenu_doc_stock()
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
	select into doc_ id, type_doc, depot_reception as depot, statut from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
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
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut from yvs_com_doc_achats where id = NEW.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
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
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
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
	if((OLD.type_doc = 'FRA' or OLD.type_doc = 'BLA') and OLD.statut = 'V') then
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
		if(NEW.statut != OLD.statut)then
			if(NEW.statut = 'V' and 'P' != OLD.statut)then
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
			elsif(OLD.statut = 'V' and 'P' != NEW.statut)then
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
			if(OLD.statut = 'V')then
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
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_achats()
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
	if((OLD.type_doc = 'FV' or OLD.type_doc = 'BLV') and OLD.statut = 'V') then
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
	if(NEW.type_doc = 'BLV' or NEW.type_doc = 'FRV') then
		if(NEW.statut = 'V' and 'P' != OLD.statut) then
			select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
				on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
				where e.id = NEW.entete_doc;
			for cont_ in select id, article , quantite as qte,prix from yvs_com_contenu_doc_vente where doc_vente = OLD.id
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
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
					end if;
				else
					if(NEW.type_doc = 'BLV')then
						result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
					else
						result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', dep_.date_doc));
					end if;
				end if;	
			end loop;
		elsif(OLD.statut = 'V' and (NEW.statut = 'EDITABLE' or NEW.statut = 'ANNULE'))then
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
	select into doc_ id, type_doc, statut, entete_doc, depot_livrer from yvs_com_doc_ventes where id = OLD.doc_vente;
	
	if(doc_.type_doc = 'FRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
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
	select into doc_ id, type_doc, statut, entete_doc, depot_livrer, tranche_livrer from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
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
		if(doc_.statut = 'V')then
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
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
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
  
  
  -- Function: equilibre_vente(bigint)

-- DROP FUNCTION equilibre_vente(bigint);

CREATE OR REPLACE FUNCTION equilibre_vente(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	av_ double precision default 0;
	contenu_ record;
	qte_ double precision default 0;
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

BEGIN
	-- Equilibre de l'etat reglé
	ttc_ = (select get_ttc_vente(id_));
	select into av_ sum(montant) from yvs_compta_caisse_piece_vente where vente = id_ and statut_piece = 'P';
	if(av_ is null)then
		av_ = 0;
	end if;

	-- Equilibre de l'etat livré
	for contenu_ in select article, sum(quantite) as qte from yvs_com_contenu_doc_vente where doc_vente = id_ group by article
	loop
		in_ = true;
		select into qte_ sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id where d.document_lie = id_ and c.article = contenu_.article and d.statut = 'V';
		if(qte_ is null)then
			qte_ = 0;
		end if;
		if(qte_ < contenu_.qte)then
			if(qte_ > 0)then
				encours = true;
				exit;
			end if;
			correct = false;
		end if;
	end loop;
	if(in_)then
		if(av_>=ttc_)then
			update yvs_com_doc_ventes set statut_regle = 'P' where id = id_;
		elsif (av_ > 0) then
			update yvs_com_doc_ventes set statut_regle = 'R' where id = id_;
		else
			update yvs_com_doc_ventes set statut_regle = 'W' where id = id_;
		end if;
		
		if(correct)then
			update yvs_com_doc_ventes set statut_livre = 'L' where id = id_;
		else
			if(encours)then
				update yvs_com_doc_ventes set statut_livre = 'R' where id = id_;
			else
				update yvs_com_doc_ventes set statut_livre = 'W' where id = id_;
			end if;
		end if;	
	else
		update yvs_com_doc_ventes set statut_regle = 'W', statut_livre = 'W' where id = id_;
	end if;	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';


-- Function: get_pr(bigint, bigint, bigint, date)

-- DROP FUNCTION get_pr(bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	pr_ double precision;

BEGIN
	if(depot_ is not null and depot_ > 0)then
		if(tranche_ is not null and tranche_ > 0)then
			select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ and tranche = tranche_ and date_doc <= date_ order by date_doc desc, id desc limit 1;
		else
			select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ and date_doc <= date_ order by date_doc desc, id desc limit 1;
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and tranche = tranche_ and date_doc <= date_ order by date_doc desc,id desc limit 1;
		else
			select into pr_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and date_doc <= date_ order by date_doc desc , id desc limit 1;
		end if;
	end if;
	if(pr_ is null or pr_ <=0)then
		pr_ = get_pua(article_);
	end if;
	return pr_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date) IS 'retourne le prix de revient d'' article';



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



-- Function: get_remise(bigint, double precision, double precision, bigint, bigint, date)

-- DROP FUNCTION get_remise(bigint, double precision, double precision, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_remise(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	remise_ double precision;
	garde_ double precision;
	data_ record;
	tarif_ record;
	valeur_ double precision default 0;

BEGIN
	valeur_ = qte_ * prix_;	
	if(client_ is not null and client_ > 0)then
		for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
		loop
			if(data_.permanent is false)then
				if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
					select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
							garde_ = (tarif_.remise /100);
						else
							garde_ = tarif_.remise;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL) then
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
								remise_ = (tarif_.remise /100);
							else
								remise_ = tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ < 1)then
							remise_ = garde_;
						end if;
					end if;
					exit;
				end if;
			else
				select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
				if(tarif_.id is not null)then
					if(tarif_.nature_remise = 'TAUX')then
						garde_ = valeur_ * (tarif_.remise /100);
						garde_ = (tarif_.remise /100);
						RAISE NOTICE 'Remise %',garde_;
					else
						garde_ = tarif_.remise;
					end if;
					select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
					if(tarif_.id IS NOT NULL) then
						if(tarif_.nature_remise = 'TAUX')then
							remise_ = valeur_ * (tarif_.remise /100);
							remise_ = (tarif_.remise /100);
						else
							remise_ = tarif_.remise;
						end if;
					end if;
					if(remise_ is null or remise_ <= 0)then
						remise_ = garde_;
					end if;
					exit;
				end if;
				--exit;
			end if;
		end loop;
	end if;
	if(remise_ is null or remise_ <= 0)then
		select into tarif_ * from yvs_base_article_point where point = point_ and article = article_ and actif is true limit 1;
		if(tarif_.id IS NOT NULL) then
			if(tarif_.nature_remise = 'TAUX')then
				remise_ = valeur_ * (tarif_.remise /100);
				remise_ = (tarif_.remise /100);
			else
				remise_ = tarif_.remise;
			end if;
		end if;
		if(remise_ is null or remise_ <= 0)then
			select into remise_ remise from yvs_base_articles where id = article_; 
			if(remise_ is not null) then
				remise_ = valeur_ * (remise_/100);
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
ALTER FUNCTION get_remise(bigint, double precision, double precision, bigint, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise(bigint, double precision, double precision, bigint, bigint, date) IS 'retourne la remise d'' article';


