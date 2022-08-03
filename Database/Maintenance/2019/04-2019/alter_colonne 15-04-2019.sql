ALTER TABLE yvs_prod_of_suivi_flux ADD COLUMN perte double precision;
ALTER TABLE yvs_prod_of_suivi_flux ALTER COLUMN perte SET DEFAULT 0;


ALTER TABLE yvs_prod_flux_composant ADD COLUMN quantite_perdue double precision;
ALTER TABLE yvs_prod_flux_composant ALTER COLUMN quantite_perdue SET DEFAULT 0;

ALTER TABLE yvs_prod_composant_op ADD COLUMN taux_perte double precision;
ALTER TABLE yvs_prod_composant_op ALTER COLUMN taux_perte SET DEFAULT 0;
COMMENT ON COLUMN yvs_prod_composant_op.taux_perte IS 'Taux de perte du composant à l''opération en pourcentage';



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
					IF(NEW.type = 'N')THEN
						result = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
					ELSIF(NEW.type='SP') THEN
						result = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', ordre_.date_debut));
					END IF;
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_valide, cout_entree = prix_, conditionnement = NEW.unite where id = mouv_;
				end if;
			else
				IF(NEW.type = 'N')THEN
					result = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
				ELSIF(NEW.type='SP') THEN
					result = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', ordre_.date_debut));
				END IF;
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
			IF(NEW.type = 'N')THEN
				result = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
			ELSIF(NEW.type='SP') THEN
				result = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', ordre_.date_debut));
			END IF;
		END IF;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_composant_of()
  OWNER TO postgres;
  
  
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
    conditionnement_ bigint;
	result boolean default false;
BEGIN
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	if(operation_.statut_op = 'R' OR operation_.statut_op = 'T') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		--Insertion mouvement stock
		IF(flux_.sens = 'E')then
			result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, (NEW.quantite - NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
		ELSIF(flux_.sens = 'S') THEN
			result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
		ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
			result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
		END IF;
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
	mouv_ bigint;
	arts_ record;
    conditionnement_ bigint;
	result boolean default false;
BEGIN
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	IF(operation_.statut_op = 'R' OR operation_.statut_op = 'T') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME LIMIT 1;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a where a.id = composant_.article;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				IF(flux_.sens = 'E')then
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, (NEW.quantite-NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
				ELSIF(flux.sens='S') THEN
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
				ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
				END IF;
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.cout, conditionnement = composant_.unite, tranche = NEW.tranche where id = mouv_;
			end if;
		else
			if(flux_.sens = 'E')then
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, (NEW.quantite-NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
			ELSIF(flux.sens='S') THEN
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));			
			ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
			END IF;
		end if;	
	ELSE
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	END IF;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_flux_composant()
  OWNER TO postgres;



