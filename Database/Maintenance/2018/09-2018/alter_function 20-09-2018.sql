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
					operation_='Reconditionnement';
				else
					select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot from yvs_com_contenu_doc_stock where id = idexterne_;
					operation_='Reconditionnement';
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
				operation_='Reconditionnement';
			else
				select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			end if;	
		WHEN 'yvs_prod_of_suivi_flux' THEN	
			select into ligne_ c.unite as conditionnement, null::integer as qualite, null::integer as lot  from yvs_prod_of_suivi_flux y inner join yvs_prod_flux_composant f on y.composant = f.id inner join yvs_prod_composant_of c on f.composant = c.id where y.id = idexterne_;
			if(mouvement_='S') then
				operation_='Consommation';
			else
				operation_='Production';
			end if;
		WHEN 'yvs_prod_declaration_production' THEN	
			select into ligne_ conditionnement, null::integer as qualite, null::integer as lot from yvs_prod_declaration_production where id = idexterne_;
			operation_='Production';
		WHEN 'yvs_prod_contenu_conditionnement' THEN	
			select into ligne_ conditionnement, null::integer as qualite, null::integer as lot from yvs_prod_contenu_conditionnement where id = idexterne_;
			operation_='Conditionnement';
		WHEN 'yvs_prod_fiche_conditionnement' THEN	
			select into ligne_ unite_mesure as conditionnement, null::integer as qualite, null::integer as lot from yvs_prod_fiche_conditionnement f inner join yvs_prod_nomenclature n on f.nomenclature = n.id where f.id = idexterne_;
			operation_='Conditionnement';
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


-- Trigger: delete_ on yvs_prod_flux_composant
DROP TRIGGER delete_ ON yvs_prod_flux_composant;

CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_prod_of_suivi_flux
  FOR EACH ROW
  EXECUTE PROCEDURE delete_flux_composant();

-- Trigger: insert_ on yvs_prod_flux_composant
DROP TRIGGER insert_ ON yvs_prod_flux_composant;

CREATE TRIGGER insert_
  AFTER INSERT
  ON yvs_prod_of_suivi_flux
  FOR EACH ROW
  EXECUTE PROCEDURE insert_flux_composant();

-- Trigger: update_ on yvs_prod_flux_composant
DROP TRIGGER update_ ON yvs_prod_flux_composant;

CREATE TRIGGER update_
  AFTER UPDATE
  ON yvs_prod_of_suivi_flux
  FOR EACH ROW
  EXECUTE PROCEDURE update_flux_composant();

  
-- Function: insert_flux_composant()
-- DROP FUNCTION insert_flux_composant();

CREATE OR REPLACE FUNCTION insert_flux_composant()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	operation_ record;
	composant_ record;
	flux_ record;
	prix_ double precision default 0;
	result boolean default false;
BEGIN
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	if(operation_.statut_op = 'R') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		--Insertion mouvement stock
		if(flux_.sens = 'E')then
			result = (select valorisation_stock(composant_.article, composant_.depot_conso, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', ordre_.date_debut));
		else
			result = (select valorisation_stock(composant_.article, composant_.depot_conso, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_flux_composant()
  OWNER TO postgres;

  
-- Function: update_flux_composant()
-- DROP FUNCTION update_flux_composant();

CREATE OR REPLACE FUNCTION update_flux_composant()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	operation_ record;
	composant_ record;
	flux_ record;
	prix_ double precision default 0;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	if(operation_.statut_op = 'R') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a where a.id = composant_.article;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				if(flux_.sens = 'E')then
					result = (select valorisation_stock(composant_.article, composant_.depot_conso, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', ordre_.date_debut));
				else
					result = (select valorisation_stock(composant_.article, composant_.depot_conso, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
				end if;
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = prix_, conditionnement = composant_.unite where id = mouv_;
			end if;
		else
			if(flux_.sens = 'E')then
				result = (select valorisation_stock(composant_.article, composant_.depot_conso, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', ordre_.date_debut));
			else
				result = (select valorisation_stock(composant_.article, composant_.depot_conso, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
			end if;
		end if;	
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_flux_composant()
  OWNER TO postgres;

  
-- Function: delete_operations_of()
-- DROP FUNCTION delete_operations_of();

CREATE OR REPLACE FUNCTION delete_operations_of()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
BEGIN
	if(OLD.statut_op in ('R', 'T')) then
		for cont_ in select y.id from yvs_prod_of_suivi_flux y inner join yvs_prod_flux_composant f on y.composant = f.id where f.operation = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_of_suivi_flux'
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
ALTER FUNCTION delete_operations_of()
  OWNER TO postgres;
  

-- Function: update_operations_of()
-- DROP FUNCTION update_operations_of();

CREATE OR REPLACE FUNCTION update_operations_of()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	ordre_ record;
	mouv_ bigint;
	arts_ record;
	prix_ double precision default 0;
	result boolean default false;
BEGIN
	IF(NEW.statut_op in ('R', 'T')) then
		select into ordre_ * from yvs_prod_ordre_fabrication where id = OLD.ordre_fabrication;
		for cont_ in select y.id, article , y.quantite, unite, depot_conso, sens from yvs_prod_of_suivi_flux y inner join yvs_prod_flux_composant f on y.composant = f.id inner join yvs_prod_composant_of c on f.composant = c.id where f.operation = OLD.id
		loop
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_of_suivi_flux';
			if(mouv_ is not null)then
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_of_suivi_flux';
					if(cont_.sens = 'E')then
						result = (select valorisation_stock(cont_.article, cont_.depot_conso, 0, cont_.quantite, prix_, 'yvs_prod_of_suivi_flux', cont_.id, 'E', ordre_.date_debut));
					else
						result = (select valorisation_stock(cont_.article, cont_.depot_conso, 0, cont_.quantite, prix_, 'yvs_prod_of_suivi_flux', cont_.id, 'S', ordre_.date_debut));
					end if;
				else
					update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = prix_ where id_externe = cont_.id and table_externe = 'yvs_prod_of_suivi_flux';
				end if;
			else
				if(cont_.sens = 'E')then
					result = (select valorisation_stock(cont_.article, cont_.depot_conso, 0, cont_.quantite, prix_, 'yvs_prod_of_suivi_flux', cont_.id, 'E', ordre_.date_debut));
				else
					result = (select valorisation_stock(cont_.article, cont_.depot_conso, 0, cont_.quantite, prix_, 'yvs_prod_of_suivi_flux', cont_.id, 'S', ordre_.date_debut));
				end if;
			end if;	
		end loop;
	elsif(NEW.statut_op not in ('R', 'T')) then
		for cont_ in select y.id from yvs_prod_of_suivi_flux y inner join yvs_prod_flux_composant f on y.composant = f.id where f.operation = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_of_suivi_flux'
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
ALTER FUNCTION update_operations_of()
  OWNER TO postgres;

