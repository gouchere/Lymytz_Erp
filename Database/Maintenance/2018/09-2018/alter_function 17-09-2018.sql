-- Function: delete_composant_of()
-- DROP FUNCTION delete_composant_of();
CREATE OR REPLACE FUNCTION delete_composant_of()
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
ALTER FUNCTION delete_composant_of()
  OWNER TO postgres;
  
-- Trigger: delete_ on yvs_prod_composant_of
-- DROP TRIGGER delete_ ON yvs_prod_composant_of;
CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_prod_composant_of
  FOR EACH ROW
  EXECUTE PROCEDURE delete_composant_of();
  
  
-- Function: insert_composant_of()
-- DROP FUNCTION insert_composant_of();
CREATE OR REPLACE FUNCTION insert_composant_of()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	flux_ bigint default 0;
	prix_ double precision default 0;
	result boolean default false;
BEGIN
	select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre_fabrication;
	if(ordre_.statut_ordre = 'T') then
		SELECT INTO flux_ COUNT(f.id) FROM yvs_prod_flux_composant f INNER JOIN yvs_prod_composant_of c ON c.id = f.composant WHERE c.ordre_fabrication = NEW.ordre_fabrication; 
		IF(COALESCE(flux_, 0) < 1)THEN
			--Insertion mouvement stock
			result = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
		END IF;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_composant_of()
  OWNER TO postgres;
  
-- Trigger: insert_ on yvs_prod_composant_of
-- DROP TRIGGER insert_ ON yvs_prod_composant_of;
CREATE TRIGGER insert_
  AFTER INSERT
  ON yvs_prod_composant_of
  FOR EACH ROW
  EXECUTE PROCEDURE insert_composant_of();
  
  
-- Function: update_composant_of()
-- DROP FUNCTION update_composant_of();
CREATE OR REPLACE FUNCTION update_composant_of()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	flux_ bigint default 0;
	prix_ double precision default 0;
	result boolean default false;
	mouv_ bigint;
	arts_ record;
BEGIN
	select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre_fabrication;
	if(ordre_.statut_ordre = 'T') then
		SELECT INTO flux_ COUNT(f.id) FROM yvs_prod_flux_composant f INNER JOIN yvs_prod_composant_of c ON c.id = f.composant WHERE c.ordre_fabrication = NEW.ordre_fabrication; 
		IF(COALESCE(flux_, 0) < 1)THEN
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
			if(mouv_ is not null)then
				select into arts_ * from yvs_base_articles a where a.id = NEW.article;
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
					result = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_valide, cout_entree = prix_, conditionnement = NEW.unite where id = mouv_;
				end if;
			else
				result = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
			end if;
		END IF;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_composant_of()
  OWNER TO postgres;
  
-- Trigger: update_ on yvs_prod_composant_of
-- DROP TRIGGER update_ ON yvs_prod_composant_of;
CREATE TRIGGER update_
  AFTER UPDATE
  ON yvs_prod_composant_of
  FOR EACH ROW
  EXECUTE PROCEDURE update_composant_of();


