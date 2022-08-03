select alter_action_colonne_key('yvs_com_doc_achats', 'yvs_com_mensualite_facture_achat', true, true);
select alter_action_colonne_key('yvs_com_doc_ventes', 'yvs_com_mensualite_facture_vente', true, true);

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
						select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
						
						--Insertion mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = arts_.id and mouvement = 'S';
						if(mouv_ is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id = mouv_;
								if(NEW.type_doc = 'BLV')then
									result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
								else
									result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', dep_.date_doc));
								end if;
							else
								update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
							end if;
						else
							if(NEW.type_doc = 'BLV')then
								result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
							else
								result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', dep_.date_doc));
							end if;
						end if;	
					end loop;
				elsif(OLD.statut = 'VALIDE' and (NEW.statut = 'EDITABLE' or NEW.statut = 'ANNULE'))then
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
			if(OLD.livrer = true and NEW.statut != OLD.statut)then
				if(NEW.statut = 'VALIDE' and OLD.statut != 'REGLE') then
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
								if(NEW.type_doc = 'BLV')then
									result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
								else 
									result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', dep_.date_doc));
								end if;								
							else
								update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
							end if;
						else
							if(NEW.type_doc = 'BLV')then
								result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', dep_.date_doc));
							else
								result = (select valorisation_stock(arts_.id, NEW.depot_livrer, NEW.tranche_livrer, cont_.qte, 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', dep_.date_doc));
							end if;
						end if;	
					end loop;
				elsif(OLD.statut = 'VALIDE' and (NEW.statut = 'EDITABLE' or NEW.statut = 'ANNULE'))then
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
	if(doc_.type_doc = 'FRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = arts_.id and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						if(doc_.type_doc = 'BLV')then
							result = (select valorisation_stock(arts_.id, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
						else
							result = (select valorisation_stock(arts_.id, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', dep_.date_doc));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_.id;
					end if;
				else
					if(doc_.type_doc = 'BLV')then
						result = (select valorisation_stock(arts_.id, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
					else
						result = (select valorisation_stock(arts_.id, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', dep_.date_doc));
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
	if(doc_.type_doc = 'FRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			if(doc_.depot_livrer is not null)then
				if(doc_.type_doc = 'BLV')then
					result = (select valorisation_stock(arts_.id, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
				else
					result = (select valorisation_stock(arts_.id, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', dep_.date_doc));
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
							select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
							
							--Insertion mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and article = arts_.id and mouvement = 'E';
							if(mouv_ is not null)then
								if(arts_.methode_val = 'FIFO')then
									delete from yvs_base_mouvement_stock where id = mouv_;
									if(NEW.type_doc = 'BLA')then
										result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
									else
										result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
									end if;
								else
									update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
								end if;
							else
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							end if;	
						end loop;
					elsif(OLD.statut = 'VALIDE' and (NEW.statut = 'ANNULE' or NEW.statut = 'EDITABLE'))then
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
				else
					if(OLD.statut = 'VALIDE')then
						for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
						loop
							select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
							
							--Insertion mouvement stock
							select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and article = arts_.id and mouvement = 'E';
							if(mouv_ is not null)then
								if(arts_.methode_val = 'FIFO')then
									delete from yvs_base_mouvement_stock where id = mouv_;
									if(NEW.type_doc = 'BLA')then
										result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
									else
										result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
									end if;
								else
									update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
								end if;
							else
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							end if;	
						end loop;
					end if;
				end if;
			else
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
		else
			if(OLD.livrer = true and NEW.statut != OLD.statut)then
				if(NEW.statut = 'VALIDE' and 'REGLE' != OLD.statut)then
					for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
					loop
						select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = cont_.article;
						
						--Insertion mouvement stock
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and depot = NEW.depot_reception and tranche = NEW.tranche and article = arts_.id and mouvement = 'E';
						if(mouv_ is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id = mouv_;
								if(NEW.type_doc = 'BLA')then
									result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
								else
									result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
								end if;
							else
								update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
							end if;
						else
							if(NEW.type_doc = 'BLA')then
								result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
							else
								result = (select valorisation_stock(arts_.id, NEW.depot_reception, NEW.tranche, cont_.qte, (select get_cout_achat_contenu(cont_.id)), 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
							end if;
						end if;	
					end loop;
				elsif(OLD.statut = 'VALIDE' and (NEW.statut = 'ANNULE' or NEW.statut = 'EDITABLE'))then
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
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_achats()
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
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock, livrer from yvs_com_doc_achats where id = NEW.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			if( doc_.type_doc = 'BLA')then
				result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu, (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			else
				result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu, (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
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
	select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = OLD.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock , livrer from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = arts_.id and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id = mouv_;
					if(doc_.type_doc = 'BLA')then
						result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					end if;
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_attendu, cout_entree = NEW.pua_attendu where id = mouv_;
				end if;
			else
				if(doc_.type_doc = 'BLA')then			
					result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
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
			somme_ = somme_ - pause_;
			
			if((p_.compensation_heure is null OR p_.compensation_heure = false) and (p_.heure_supplementaire is null OR p_.heure_supplementaire = false)) then
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

CREATE OR REPLACE FUNCTION add_presence(societe_ bigint, employe_ bigint, date_debut_ date, date_fin_ date)
  RETURNS boolean AS
$BODY$   
DECLARE 
	em_ record;
	pe_ record;
	pa_ record;
	date_ date;
BEGIN
	select into pa_ duree_retard_autorise from yvs_parametre_grh where societe = societe_;
	if(employe_ is not null and employe_ > 0)then
		date_ = date_debut_;
		while (date_ <= date_fin_) loop
			select into pe_ id from yvs_grh_presence where employe = employe_ and date_ between date_debut and date_fin;
			if(pe_.id is null)then
				insert into yvs_grh_presence(employe, marge_approuve, valider, date_debut, date_fin, heure_debut, heure_fin, duree_pause)
					values (employe_, pa_.duree_retard_autorise, false, date_, date_, '07:30:00', '17:30:00', '01:30:00');
					
				select into pe_ id from yvs_grh_presence order by id desc limit 1;

				insert into yvs_grh_pointage(valider, actif, horaire_normale, presence, pointage_automatique, heure_entree, heure_sortie, heure_pointage,  date_save_entree, date_save_sortie)
					    values (true, true, true, pe_.id, true, (select ((date_ || ' ' || '07:30:00')::timestamp)), (select ((date_ || ' ' || '17:30:00')::timestamp)), 
					    (select ((date_ || ' ' || '17:30:00')::timestamp)), (select ((date_ || ' ' || '07:30:00')::timestamp)), (select ((date_ || ' ' || '17:30:00')::timestamp)));
			end if;
			date_ = date_ + interval '1 day';
		end loop;
	else
		for em_ in select e.id as id from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id where a.societe = societe_
		loop
			PERFORM add_presence(societe_, em_.id, date_debut_, date_fin_);
		end loop;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION add_presence(bigint, bigint, date, date)
  OWNER TO postgres;
