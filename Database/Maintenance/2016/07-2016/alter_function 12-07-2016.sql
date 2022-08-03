-- Function: get_puv_total(bigint)

-- DROP FUNCTION get_puv_total(bigint);

CREATE OR REPLACE FUNCTION get_puv_total(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	contenu_ record;
	remise_ double precision;
	somme_ double precision;
	total_ double precision;

BEGIN
	select into contenu_ quantite as qte, prix, remise_art, remise_cat  
		from yvs_com_contenu_doc_vente where id = id_;
	somme_ =  contenu_.qte * contenu_.prix;
	if(somme_ is null)then
		somme_ = 0;
	end if;
	remise_ = somme_ * ((contenu_.remise_art + contenu_.remise_cat) / 100);
	if(remise_ is null)then
		remise_ = 0;
	end if;
	total_ = somme_ - remise_;
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_puv_total(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_puv_total(bigint) IS 'retourne le prix d''achat d'' article';



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
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, livrer, depot_livrer, tranche_livrer from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = arts_.id and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						result = (select valorisation_stock(arts_.id, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					result = (select valorisation_stock(arts_.id, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
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
	select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = OLD.article;
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, livrer, depot_livrer from yvs_com_doc_ventes where id = OLD.doc_vente;
	
	if(doc_.type_doc = 'FV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = arts_.id and mouvement = 'S';
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
	select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = NEW.article;
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, livrer, depot_livrer, tranche_livrer from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			if(doc_.depot_livrer is not null)then
				result = (select valorisation_stock(arts_.id, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
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
			select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
			
			--Recherche mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = OLD.depot_livrer and article = arts_.id and mouvement = 'S';
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
	if(NEW.type_doc = 'FV' or NEW.type_doc = 'BLV' and OLD.mouv_stock = true) then
		if(OLD.livrer != NEW.livrer)then
			if(NEW.livrer = true)then
				if(NEW.statut = 'VALIDE' and NEW.statut != OLD.statut) then
					select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
						on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
						where e.id = NEW.entete_doc;
					for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
					loop
						RAISE NOTICE 'cont_ : %',cont_.id; 
						select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
						
						--Insertion mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = arts_.id and mouvement = 'S';
						if(mouv_ is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id = mouv_;
								result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
							else
								update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
							end if;
						else
							result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
						end if;	
					end loop;
				elsif(OLD.statut = 'VALIDE' and (NEW.statut != 'EDITABLE' or NEW.statut != 'ANNULE'))then
					select into dep_ h.depot as depot, h.tranche as tranche from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
					on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
					where e.id = OLD.entete_doc;
					for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
					loop
						select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
						
						--Recherche mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = arts_.id and mouvement = 'S';
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
					select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
					
					--Recherche mouvement stock
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = arts_.id and mouvement = 'S';
					if(mouv_ is not null)then
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
				end loop;
			end if;
		else
			if(OLD.livrer = true)then
				if(NEW.statut = 'VALIDE' and NEW.statut != OLD.statut) then
					select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
						on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
						where e.id = NEW.entete_doc;
					for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
					loop
						RAISE NOTICE 'cont_ : %',cont_.id; 
						select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
						
						--Insertion mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = arts_.id and mouvement = 'S';
						if(mouv_ is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id = mouv_;
								result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
							else
								update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
							end if;
						else
							result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
						end if;	
					end loop;
				elsif(OLD.statut = 'VALIDE' and (NEW.statut != 'EDITABLE' or NEW.statut != 'ANNULE'))then
					select into dep_ h.depot as depot, h.tranche as tranche from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
					on e.creneau = c.id inner join yvs_com_creneau_horaire h on c.creneau = h.id
					where e.id = OLD.entete_doc;
					for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_vente where doc_vente = OLD.id
					loop
						select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
						
						--Recherche mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = arts_.id and mouvement = 'S';
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
  
  
  -- Function: mensualite_doc_divers_delete()

-- DROP FUNCTION mensualite_doc_divers_delete();

CREATE OR REPLACE FUNCTION mensualite_doc_divers_delete()
  RETURNS trigger AS
$BODY$    
DECLARE
	montant_ double precision default 0;
	total_ double precision default 0;
	somme_ double precision default 0;
	etat_ varchar default 'EDITABLE';
	solde_ boolean default false;
	id_reg bigint;
BEGIN
	select into montant_ montant from yvs_base_doc_divers where id = OLD.doc_divers;
	if(montant_ is null)then
		montant_ = 0;
	end if;
	for id_reg in select id from yvs_base_mensualite_doc_divers where doc_divers = OLD.doc_divers
	loop
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = id_reg and table_externe = 'yvs_base_mensualite_doc_divers' and (statut = 'VALIDE' or statut = 'REGLE');
		if(total_ is null)then
			total_ = 0;
		end if;
		somme_ = somme_ + total_;
	end loop;	
	if(somme_ is null)then
		somme_ = 0;
	end if;
	if(somme_ >= montant_)then
		etat_ = 'REGLE';
		solde_  = true;
	elsif (somme_ > 0)then
		etat_ = 'ENCOURS';
	end if;
	update yvs_base_doc_divers set statut = etat_, solde = solde_ where id = OLD.doc_divers AND statut != 'EDITABLE' AND statut != 'REFUSE' and cloturer = false;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mensualite_doc_divers_delete()
  OWNER TO postgres;

  
  
  -- Function: mensualite_doc_divers_insert()

-- DROP FUNCTION mensualite_doc_divers_insert();

CREATE OR REPLACE FUNCTION mensualite_doc_divers_insert()
  RETURNS trigger AS
$BODY$    
DECLARE
	montant_ double precision default 0;
	total_ double precision default 0;
	somme_ double precision default 0;
	etat_ varchar default 'EDITABLE';
	solde_ boolean default false;
	id_reg bigint;
BEGIN
	select into montant_ montant from yvs_base_doc_divers where id = NEW.doc_divers;
	if(montant_ is null)then
		montant_ = 0;
	end if;
	for id_reg in select id from yvs_base_mensualite_doc_divers where doc_divers = NEW.doc_divers
	loop
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = id_reg and table_externe = 'yvs_base_mensualite_doc_divers' and (statut = 'VALIDE' or statut = 'REGLE');
		if(total_ is null)then
			total_ = 0;
		end if;
		somme_ = somme_ + total_;
	end loop;	
	if(somme_ is null)then
		somme_ = 0;
	end if;
	if(somme_ >= montant_)then
		etat_ = 'REGLE';
		solde_  = true;
	elsif (somme_ > 0)then
		etat_ = 'ENCOURS';
	end if;
	update yvs_base_doc_divers set statut = etat_, solde = solde_ where id = NEW.doc_divers AND statut != 'EDITABLE' AND statut != 'REFUSE' and cloturer = false;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mensualite_doc_divers_insert()
  OWNER TO postgres;

  
  
  -- Function: mensualite_doc_divers_update()

-- DROP FUNCTION mensualite_doc_divers_update();

CREATE OR REPLACE FUNCTION mensualite_doc_divers_update()
  RETURNS trigger AS
$BODY$    
DECLARE
	montant_ double precision default 0;
	total_ double precision default 0;
	somme_ double precision default 0;
	etat_ varchar default 'EDITABLE';
	solde_ boolean default false;
	id_reg bigint;
BEGIN
	select into montant_ montant from yvs_base_doc_divers where id = OLD.doc_divers;
	if(montant_ is null)then
		montant_ = 0;
	end if;
	for id_reg in select id from yvs_base_mensualite_doc_divers where doc_divers = OLD.doc_divers
	loop
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = id_reg and table_externe = 'yvs_base_mensualite_doc_divers' and (statut = 'VALIDE' or statut = 'REGLE');
		if(total_ is null)then
			total_ = 0;
			NEW.etat = 'EDITABLE';
		end if;
		if(total_ > 0 and NEW.montant > total_)then
			NEW.etat = 'ENCOURS';
		end if;
		somme_ = somme_ + total_;
	end loop;	
	if(somme_ is null)then
		somme_ = 0;
	end if;
	if(somme_ >= montant_)then
		etat_ = 'REGLE';
		solde_  = true;
	elsif (somme_ > 0)then
		etat_ = 'ENCOURS';
	end if;
	update yvs_base_doc_divers set statut = etat_, solde = solde_ where id = OLD.doc_divers AND statut != 'EDITABLE' AND statut != 'REFUSE' and cloturer = false;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mensualite_doc_divers_update()
  OWNER TO postgres;
  
  
DROP TRIGGER delete_ ON yvs_base_piece_tresorerie;
DROP TRIGGER insert_ ON yvs_base_piece_tresorerie;
DROP TRIGGER update_ ON yvs_base_piece_tresorerie;
DROP TRIGGER delete_ ON yvs_base_mensualite_doc_divers;
DROP TRIGGER insert_ ON yvs_base_mensualite_doc_divers;
DROP TRIGGER update_ ON yvs_base_mensualite_doc_divers;
DROP TRIGGER delete_ ON yvs_com_mensualite_facture_achat;
DROP TRIGGER insert_ ON yvs_com_mensualite_facture_achat;
DROP TRIGGER update_ ON yvs_com_mensualite_facture_achat;
DROP TRIGGER delete_ ON yvs_com_mensualite_facture_vente;
DROP TRIGGER insert_ ON yvs_com_mensualite_facture_vente;
DROP TRIGGER update_ ON yvs_com_mensualite_facture_vente;


