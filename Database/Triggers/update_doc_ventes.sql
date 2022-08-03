-- Function: update_doc_ventes()

-- DROP FUNCTION update_doc_ventes();

CREATE OR REPLACE FUNCTION update_doc_ventes()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	mouv_ record;	
	ligne_ record;
	last_pr_ double precision default 0;
	
	result_ boolean default false;
	prix_ double precision;
	delai_ integer;
	duree_ integer;
	agence_ integer;
	model_ integer;
	titre_  CHARACTER VARYING;
	
	ACTION_  CHARACTER VARYING;
	_next BOOLEAN DEFAULT false;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	CASE ACTION_
	WHEN 'DELETE' THEN
		for ligne_ in select id from yvs_com_contenu_doc_vente where doc_vente = OLD.id
		loop			
			delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_vente';
		end loop;
		RETURN OLD;
	WHEN 'UPDATE' THEN	
		IF(EXEC_) THEN
			if(NEW.type_doc = 'BLV' or NEW.type_doc = 'BRV') then
				if(NEW.statut = 'V') then
					for ligne_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix, conditionnement, calcul_pr from yvs_com_contenu_doc_vente where doc_vente = NEW.id
					loop
						select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = ligne_.article;
						--Insertion mouvement stock
						select into mouv_ id, cout_stock from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and article = ligne_.article;
						if(mouv_.id is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = ligne_.article;
								if(NEW.type_doc = 'BLV')then
									result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (ligne_.qte), 0, 'yvs_com_contenu_doc_vente', ligne_.id, 'S', NEW.date_livraison));
								else
									result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (ligne_.qte), 0, 'yvs_com_contenu_doc_vente', ligne_.id, 'E', NEW.date_livraison));
								end if;
							else
								-- Recherche de la derniere entree de l'article dans le dépot
								last_pr_= COALESCE(get_pr(ligne_.article, NEW.depot_livrer, 0, NEW.date_livraison, ligne_.conditionnement, mouv_.id), 0);
								if(NEW.type_doc = 'BLV')then
									PERFORM insert_mouvement_stock(ligne_.article, NEW.depot_livrer, NEW.tranche_livrer, ligne_.qte, ligne_.prix, mouv_.cout_stock, null, 'yvs_com_contenu_doc_vente', ligne_.id, 'S', NEW.date_livraison, last_pr_, mouv_.id);
								else
									PERFORM insert_mouvement_stock(ligne_.article, NEW.depot_livrer, NEW.tranche_livrer, ligne_.qte, ligne_.prix, mouv_.cout_stock, null, 'yvs_com_contenu_doc_vente', ligne_.id, 'E', NEW.date_livraison, last_pr_, mouv_.id);
								end if;	
							end if;
						else
							if(NEW.type_doc = 'BLV')then
								result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (ligne_.qte), 0, 'yvs_com_contenu_doc_vente', ligne_.id, 'S', NEW.date_livraison));
							else
								result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (ligne_.qte), 0, 'yvs_com_contenu_doc_vente', ligne_.id, 'E', NEW.date_livraison));
							end if;
						end if;	
					end loop;
				elsif(NEW.statut != 'V')then
					for ligne_ in select id from yvs_com_contenu_doc_vente where doc_vente = OLD.id
					loop				
						delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_vente';
					end loop;
				end if;
			end if;
		END IF;
		IF(EXEC_T_) THEN 
			IF(NEW.type_doc = 'FV' AND NEW.statut_livre = 'L') then
				for ligne_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix, id_reservation from yvs_com_contenu_doc_vente where doc_vente = NEW.id
				LOOP
					--change le statut de la reservation
					IF(ligne_.id_reservation IS NOT NULL) THEN
						UPDATE yvs_com_reservation_stock SET statut='T' WHERE id=ligne_.id_reservation AND statut='V';
					END IF;				
				END LOOP;
			END IF;
		END IF;
		RETURN NEW;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ventes()
  OWNER TO postgres;
