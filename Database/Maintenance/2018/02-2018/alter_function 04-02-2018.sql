-- Function: delete_flux_composant()

-- DROP FUNCTION delete_flux_composant();

CREATE OR REPLACE FUNCTION delete_flux_composant()
  RETURNS trigger AS
$BODY$    
DECLARE

BEGIN
	delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_flux_composant()
  OWNER TO postgres;
  
CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_prod_flux_composant
  FOR EACH ROW
  EXECUTE PROCEDURE delete_flux_composant();
  
  -- Function: insert_flux_composant()

-- DROP FUNCTION insert_flux_composant();

CREATE OR REPLACE FUNCTION insert_flux_composant()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	operation_ record;
	composant_ record;
	prix_ double precision default 0;
	result boolean default false;
BEGIN
	select into operation_ * from yvs_prod_operations_of where id = NEW.operation;
	if(operation_.statut_op = 'T') then
		select into composant_ * from yvs_prod_composant_of where id = NEW.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		--Insertion mouvement stock
		if(NEW.sens = 'E')then
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

CREATE TRIGGER insert_
  AFTER INSERT
  ON yvs_prod_flux_composant
  FOR EACH ROW
  EXECUTE PROCEDURE insert_flux_composant();
  
  -- Function: update_flux_composant()

-- DROP FUNCTION update_flux_composant();

CREATE OR REPLACE FUNCTION update_flux_composant()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	operation_ record;
	composant_ record;
	prix_ double precision default 0;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into operation_ * from yvs_prod_operations_of where id = NEW.operation;
	if(operation_.statut_op = 'T') then
		select into composant_ * from yvs_prod_composant_of where id = NEW.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a where a.id = composant_.article;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				if(NEW.sens = 'E')then
					result = (select valorisation_stock(composant_.article, composant_.depot_conso, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', ordre_.date_debut));
				else
					result = (select valorisation_stock(composant_.article, composant_.depot_conso, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
				end if;
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = prix_, conditionnement = composant_.unite where id = mouv_;
			end if;
		else
			if(NEW.sens = 'E')then
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

CREATE TRIGGER update_
  AFTER UPDATE
  ON yvs_prod_flux_composant
  FOR EACH ROW
  EXECUTE PROCEDURE update_flux_composant();
  
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
	IF(NEW.statut_op = 'T') then
		select into ordre_ * from yvs_prod_ordre_fabrication where id = OLD.ordre_fabrication;
		for cont_ in select y.id, article , y.quantite, unite, depot_conso, sens from yvs_prod_flux_composant y inner join yvs_prod_composant_of c on y.composant = c.id where y.operation = OLD.id
		loop
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_flux_composant';
			if(mouv_ is not null)then
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_flux_composant';
					if(cont_.sens = 'E')then
						result = (select valorisation_stock(cont_.article, cont_.depot_conso, 0, cont_.quantite, prix_, 'yvs_prod_flux_composant', cont_.id, 'E', ordre_.date_debut));
					else
						result = (select valorisation_stock(cont_.article, cont_.depot_conso, 0, cont_.quantite, prix_, 'yvs_prod_flux_composant', cont_.id, 'S', ordre_.date_debut));
					end if;
				else
					update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = prix_ where id_externe = cont_.id and table_externe = 'yvs_prod_flux_composant';
				end if;
			else
				if(cont_.sens = 'E')then
					result = (select valorisation_stock(cont_.article, cont_.depot_conso, 0, cont_.quantite, prix_, 'yvs_prod_flux_composant', cont_.id, 'E', ordre_.date_debut));
				else
					result = (select valorisation_stock(cont_.article, cont_.depot_conso, 0, cont_.quantite, prix_, 'yvs_prod_flux_composant', cont_.id, 'S', ordre_.date_debut));
				end if;
			end if;	
		end loop;
	elsif(NEW.statut_op != 'T') then
		for cont_ in select id from yvs_prod_flux_composant where operation = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_flux_composant'
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
  

CREATE TRIGGER update_
  BEFORE UPDATE
  ON yvs_prod_operations_of
  FOR EACH ROW
  EXECUTE PROCEDURE update_operations_of();
  
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
	if(OLD.statut_op = 'T') then
		for cont_ in select id from yvs_prod_flux_composant where operation = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_flux_composant'
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

CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_prod_operations_of
  FOR EACH ROW
  EXECUTE PROCEDURE delete_operations_of();
  
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
		WHEN 'yvs_prod_flux_composant' THEN	
			select into ligne_ c.unite as conditionnement, null::integer as qualite, null::integer as lot  from yvs_prod_flux_composant y inner join yvs_prod_composant_of c on y.composant = c.id where y.id = idexterne_;
			if(mouvement_='S') then
				operation_='Sortie';
			else
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


-- Function: get_ttc_achat(bigint)

-- DROP FUNCTION get_ttc_achat(bigint);

CREATE OR REPLACE FUNCTION get_ttc_achat(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	qte_ double precision default 0;
	cs_p double precision default 0;
	cs_m double precision default 0;
	data_ record;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into total_ sum(prix_total) from yvs_com_contenu_doc_achat where doc_achat = id_;
	if(total_ is null)then
		total_ = 0;
	end if;
	
	-- Recupere le total des couts supplementaire d'une facture
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_achat o inner join yvs_grh_type_cout t on o.type_cout = t.id where o.doc_achat = id_ and o.actif is true and t.augmentation is true;
	if(cs_p is null)then
		cs_p = 0;
	end if;
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_achat o inner join yvs_grh_type_cout t on o.type_cout = t.id where o.doc_achat = id_ and o.actif is true and t.augmentation is false;
	if(cs_m is null)then
		cs_m = 0;
	end if;
	total_ = total_ + cs_p - cs_m;
		
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ttc_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ttc_achat(bigint) IS 'retourne le montant TTC d''un doc achat';
