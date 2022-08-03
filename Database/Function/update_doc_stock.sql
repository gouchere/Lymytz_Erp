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
		if(NEW.type_doc = 'FT' and NEW.statut = 'VALIDE') then
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
		elsif(NEW.type_doc = 'ES' and NEW.statut = 'VALIDE') then		--cas d'une entrée en stock
			for cont_ in select id, article , quantite as qte, prix from yvs_com_contenu_doc_stock where doc_stock = OLD.id
			loop							
				--Insertion mouvement stock
				if(NEW.destination is not null)then --l'entrée utilise le dépot destination
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
					if(mouv_ is not null)then --si le mouvement exite déjà, on le supprime
						delete from yvs_base_mouvement_stock where id = mouv_;
					end if;
					result = (select valorisation_stock(cont_.article, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;
			end loop;
		elsif(NEW.type_doc = 'ES' and NEW.statut != 'VALIDE') then	--cas d'une sortie
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
