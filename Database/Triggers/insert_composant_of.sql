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
