-- Function: update_doc_achats()
-- DROP FUNCTION update_doc_achats();
CREATE OR REPLACE FUNCTION update_doc_achats()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	mouv_ record;	
	ligne_ record;
	last_pr_ double precision default 0;
	
	delai_ integer;
	duree_ integer;
	agence_ integer;
	model_ integer;
	titre_  CHARACTER VARYING;
	
	date_ date;
	result_ boolean default false;
	_next BOOLEAN DEFAULT false;
	
	ACTION_  CHARACTER VARYING;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	RAISE NOTICE 'EXEC_ : %',EXEC_;
	CASE ACTION_
	WHEN 'DELETE' THEN
		if((OLD.type_doc = 'BRA' or OLD.type_doc = 'BLA') and OLD.statut = 'V') then
			for ligne_ in select id from yvs_com_contenu_doc_achat where doc_achat = OLD.id
			loop		
				delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_achat';
			end loop;
		end if;
		RETURN OLD;
	WHEN 'UPDATE' THEN	
		IF(EXEC_) THEN
			if(NEW.type_doc = 'BRA' or NEW.type_doc = 'BLA')then	
				if(NEW.statut = 'V')then
					IF(NEW.date_livraison IS NULL) THEN 
					  date_=NEW.date_doc;
					ELSE
					  date_=NEW.date_livraison;
					END IF;
					for ligne_ in select id, article , quantite_recu as qte, pua_recu as prix, conditionnement, calcul_pr 
						FROM yvs_com_contenu_doc_achat where doc_achat = NEW.id
					loop
						select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = ligne_.article;					
						--Insertion mouvement stock
						select into mouv_ id, cout_stock from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_achat' ;
						if(mouv_.id is not null)then
							if(arts_.methode_val = 'FIFO')then
								delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_achat' and article = ligne_.article;
								if(NEW.type_doc = 'BLA')then
									result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_reception, NEW.tranche, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_achat', ligne_.id, 'E', date_));
								else
									result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_reception, NEW.tranche, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_achat', ligne_.id, 'S', date_));
								end if;
							else
								-- Recherche de la derniere entree de l'article dans le dépot
								last_pr_= COALESCE(get_pr(ligne_.article, NEW.depot_reception, 0, date_, ligne_.conditionnement, mouv_.id), 0);
								if(NEW.type_doc = 'BLA')then
									PERFORM insert_mouvement_stock(ligne_.article, NEW.depot_reception, NEW.tranche, ligne_.qte, ligne_.prix, mouv_.cout_stock, null, 'yvs_com_contenu_doc_achat', ligne_.id, 'E', date_, last_pr_, mouv_.id);
								else
									PERFORM insert_mouvement_stock(ligne_.article, NEW.depot_reception, NEW.tranche, ligne_.qte, ligne_.prix, mouv_.cout_stock, null, 'yvs_com_contenu_doc_achat', ligne_.id, 'S', date_, last_pr_, mouv_.id);
								end if;								
							end if;
						else
							if(NEW.type_doc = 'BLA')then
								result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot_reception, NEW.tranche, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_achat', ligne_.id, 'E', date_));
							else
								result_ = (select valorisation_stock(ligne_.article, ligne_.conditionnement,NEW.depot_reception, NEW.tranche, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_achat', ligne_.id, 'S', NEW.date_livraison));
							end if;
						end if;	
					end loop;
				elsif(NEW.statut != 'V')then
					for ligne_ in select id from yvs_com_contenu_doc_achat WHERE doc_achat = NEW.id
					loop		
						delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_achat';
					end loop;
				end if;
			end if;
		END IF;
		RETURN NEW;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_achats()
  OWNER TO postgres;
