-- Function: insert_contenu_conditionnement()

-- DROP FUNCTION insert_contenu_conditionnement();

CREATE OR REPLACE FUNCTION insert_contenu_conditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	entree_ record;
	sortie_ record;
	arts_ record;
	article_ record;
	mouv_ bigint;
	result_ boolean default false;
	
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
				select into doc_ id, statut, depot, date_conditionnement from yvs_prod_fiche_conditionnement where id = NEW.fiche;
				--Insertion mouvement stock
				if(doc_.statut = 'V')then
					if(doc_.depot is not null)then
						result_ = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_conditionnement));
					end if;
				end if;
			END IF;
			RETURN NEW;
		WHEN 'UPDATE' THEN 
			IF(EXEC_) THEN
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
				select into doc_ id, statut, depot, date_conditionnement from yvs_prod_fiche_conditionnement where id = NEW.fiche;
				--Insertion mouvement stock
				if(doc_.statut = 'V')then
					select into article_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement;
					if(doc_.depot is not null)then
						select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
						if(mouv_ is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
								result_ = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_conditionnement));
							else
								update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = article_.prix, conditionnement = NEW.conditionnement, calcul_pr = NEW.calcul_pr where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'E';
							end if;
						else
							result_ = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_conditionnement));
						end if;	
					end if;
				end if;
			END IF;
			RETURN NEW;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_conditionnement()
  OWNER TO postgres;
