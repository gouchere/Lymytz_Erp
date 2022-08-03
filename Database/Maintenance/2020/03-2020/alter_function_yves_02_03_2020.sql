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
	session_ record;
	cout_ double precision;
	conditionnement_ bigint;
	mouv_ bigint;
	depot_ bigint;
	result_ boolean default false;
BEGIN
	IF(TG_OP = 'DELETE' OR TG_OP = 'TRUNCATE')THEN
	DELETE FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		RETURN OLD;
	END IF;
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	if(operation_.statut_op = 'R' OR operation_.statut_op = 'T') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		SELECT INTO cout_ op.cout FROM yvs_prod_suivi_operations op WHERE  op.id=NEW.id_operation;
		SELECT INTO session_ sp.* FROM yvs_prod_suivi_operations op INNER JOIN yvs_prod_session_of so ON op.session_of = so.id 
																	INNER JOIN yvs_prod_session_prod sp ON so.session_prod = sp.id 
																	WHERE op.id=NEW.id_operation;
		--Insertion mouvement stock
		SELECT INTO mouv_ id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id AND table_externe = TG_TABLE_NAME;
		depot_=COALESCE(composant_.depot_conso,session_.depot);
		IF(flux_.sens = 'E')then
			--vérifie si le mouvement existe déjà			
			IF(COALESCE(mouv_,0)<=0) THEN
				result_ = (select valorisation_stock(composant_.article,composant_.unite, depot_, session_.tranche, (NEW.quantite - NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
			END IF;
		ELSIF(flux_.sens = 'S') THEN
			IF(COALESCE(mouv_,0)<=0) THEN
				result_ = (select valorisation_stock(composant_.article,composant_.unite, depot_, session_.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session));
			END IF;
		ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
			IF(COALESCE(mouv_,0)<=0) THEN
				result_ = (select valorisation_stock(composant_.article,composant_.unite, depot_, session_.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session));
			END IF;
		END IF;
		-- Met à jour le prix de l'opération
		IF(cout_ IS NULL) THEN 
			cout_=0; 
		END IF;
		cout_=cout_ + COALESCE(NEW.cout,0);
		UPDATE yvs_prod_suivi_operations SET cout=cout_ WHERE id=NEW.id_operation;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_flux_composant()
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
	result_ boolean default false;
	mouv_ bigint;
	arts_ record;
	
	ACTION_  CHARACTER VARYING;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	CASE ACTION_
		WHEN 'DELETE' THEN
			delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
			RETURN OLD;
		WHEN 'INSERT' THEN
			IF(EXEC_) THEN
				select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre_fabrication;
				if(ordre_.statut_ordre = 'T') then
					SELECT INTO flux_ COUNT(f.id) FROM yvs_prod_flux_composant f INNER JOIN yvs_prod_composant_of c ON c.id = f.composant WHERE c.ordre_fabrication = NEW.ordre_fabrication; 
					IF(COALESCE(flux_, 0) < 1)THEN
						--Insertion mouvement stock
						IF(NEW.type = 'N')THEN
							result_ = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
						ELSIF(NEW.type='SP') THEN
							result_ = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', ordre_.date_debut));
						END IF;
					END IF;
				end if;
			END IF;
			RETURN NEW;
		WHEN 'UPDATE' THEN 
			IF(EXEC_) THEN
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
									result_ = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
								ELSIF(NEW.type='SP') THEN
									result_ = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', ordre_.date_debut));
								END IF;
							else
								update yvs_base_mouvement_stock set quantite = NEW.quantite_valide, cout_entree = prix_, conditionnement = NEW.unite where id = mouv_;
							end if;
						else
							IF(NEW.type = 'N')THEN
								result_ = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'S', ordre_.date_debut));
							ELSIF(NEW.type='SP') THEN
								result_ = (select valorisation_stock(NEW.article, NEW.depot_conso, 0, NEW.quantite_valide, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', ordre_.date_debut));
							END IF;
						end if;
					END IF;
				end if;
			END IF;
			RETURN NEW;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_composant_of()
  OWNER TO postgres;

