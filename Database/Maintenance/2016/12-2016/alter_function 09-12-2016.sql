-- Function: get_taxe(bigint, bigint, bigint, double precision, double precision, double precision)

-- DROP FUNCTION get_taxe(bigint, bigint, bigint, double precision, double precision, double precision);

CREATE OR REPLACE FUNCTION get_taxe(article_ bigint, categorie_ bigint, compte_ bigint, remise_ double precision, qte_ double precision, prix_ double precision)
  RETURNS double precision AS
$BODY$
DECLARE
	art_ record;
	taxe_ double precision default 0;
	valeur_ double precision default 0;
	data_ record;
	id_ bigint;

BEGIN
	select into art_ * from yvs_base_articles where id = article_;
	
	-- Recherche de la categorie comptable taxable
	if(compte_ is not null and compte_ > 0)then
		select into id_ id from yvs_base_article_categorie_comptable where article = article_ and categorie = categorie_ and compte = compte_ and actif = true;
	else
		select into id_ id from yvs_base_article_categorie_comptable where article = article_ and categorie = categorie_ and actif = true;
	end if;
	
	-- Verification si la categorie comptable taxable existe
	if(id_ is not null and id_ > 0)then
		-- Verification si le prix de vente est le prix TTC
		if(art_.puv_ttc)then
			-- Calcul de la taxe sur le prix de vente
			for data_ in select t.taux from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
			loop
				taxe_ = taxe_ + ((data_.taux / 100 ) * (prix_ / (1 + (data_.taux / 100))));
			end loop;
			-- On retire la taxe sur le prix de vente
			prix_ = prix_ - taxe_;
		end if;
		-- Calcul de la valeur
		valeur_ = qte_ * prix_;
		
		-- Calcul de la taxe sur la valeur
		for data_ in select c.app_remise , t.taux from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
		loop
			-- Verification si la taxe s'applique sur la remise
			if(data_.app_remise)then
				taxe_ = taxe_ + (((valeur_ - remise_) * data_.taux) / 100);
			else
				taxe_ = taxe_ + ((valeur_ * data_.taux) / 100);
			end if;
		end loop;
	end if;	
	if(taxe_ is null or taxe_ <1)then
		taxe_ = 0;
	end if;
	return taxe_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_taxe(bigint, bigint, bigint, double precision, double precision, double precision)
  OWNER TO postgres;
COMMENT ON FUNCTION get_taxe(bigint, bigint, bigint, double precision, double precision, double precision) IS 'retourne la taxe d'' article';

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
		elsif(OLD.statut = 'V' and (NEW.statut = 'E' or NEW.statut = 'A'))then
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
			update yvs_com_doc_ventes set statut_livre = 'L' where consigner is false and id = id_;
		else
			if(encours)then
				update yvs_com_doc_ventes set statut_livre = 'R' where consigner is false and id = id_;
			else
				update yvs_com_doc_ventes set statut_livre = 'W' where consigner is false and id = id_;
			end if;
		end if;	
	else
		update yvs_com_doc_ventes set statut_regle = 'W', statut_livre = 'W' where consigner is false and id = id_;
	end if;	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';



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
		elsif(NEW.type_doc = 'INV' and NEW.statut = 'V') then
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop
				--Insertion mouvement stock
				if(NEW.source is not null)then
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					if(cont_.qte>0)then
						result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
					elsif(cont_.qte<0)then
						result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
					end if;
				end if;
			end loop;
		elsif(NEW.type_doc = 'INV' and NEW.statut != 'V') then
			for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop			
				--Recherche mouvement stock
				for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
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
		elsif(doc_.type_doc = 'INV') then
			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						if(NEW.quantite>0)then
							result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						elsif(NEW.quantite<0)then
							result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix where id = mouv_;
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
