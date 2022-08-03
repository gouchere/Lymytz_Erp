-- Function: update_doc_stocks()
-- DROP FUNCTION update_doc_stocks();
CREATE OR REPLACE FUNCTION update_doc_stocks()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	mouv_ record;	
	ligne_ record;
	entree_ record;
	sortie_ record;
	trancheD_ bigint;
	trancheS_ bigint;
	
	result_ boolean default false;
	prix_ double precision;
	last_pr_ double precision default 0;
	
	delai_ integer;
	duree_ integer;
	agence_ integer;
	titre_  CHARACTER VARYING;
	
	ACTION_  CHARACTER VARYING;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	CASE ACTION_
	WHEN 'DELETE' THEN
		for ligne_ in select c.id AS id_c, r.id AS id_r  from yvs_com_contenu_doc_stock c LEFT JOIN yvs_com_contenu_doc_stock_reception r ON r.contenu=c.id where doc_stock = OLD.id
		loop
			delete from yvs_base_mouvement_stock where (id_externe = ligne_.id_c and table_externe = 'yvs_com_contenu_doc_stock') OR (id_externe = COALESCE(ligne_.id_r,0) and table_externe = 'yvs_com_contenu_doc_stock_reception');
		end loop;
		RETURN OLD;
	WHEN 'UPDATE' THEN	
		IF(EXEC_) THEN
			-- On va utiliser deux boucle pour ne pas insérer sortie en doublon au cas où il y aurai deux reception pour une seule sortie; le left join renverait 2 ligne avec le même id de sortie
			select into trancheD_ tranche from yvs_com_creneau_depot where id = NEW.creneau_destinataire;
			select into trancheS_ tranche from yvs_com_creneau_depot where id = NEW.creneau_source;	
			if(NEW.type_doc = 'FT' and (NEW.statut = 'U' or NEW.statut = 'V' or NEW.statut = 'R')) then
				FOR ligne_ in select c.id id_c,article , c.quantite as qte_c, quantite_entree, prix, prix_entree, conditionnement_entree, conditionnement
					FROM yvs_com_contenu_doc_stock c WHERE doc_stock = NEW.id
				LOOP
					--Insertion mouvement stock				
					IF(NEW.source is not null and (NEW.statut = 'U' or NEW.statut = 'V')) THEN       --traitement de la sortie dans le depot source
						delete from yvs_base_mouvement_stock where id_externe = ligne_.id_c and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and mouvement = 'S';
						--insertion de la sortie
						result_ = (select valorisation_stock(ligne_.article, ligne_.conditionnement, NEW.source, trancheS_, ligne_.qte_c, ligne_.prix, 'yvs_com_contenu_doc_stock', ligne_.id_c, 'S', NEW.date_doc));
					END IF;
				END LOOP;
				FOR ligne_ in select c.id id_c, r.id id_r, article , c.quantite as qte_c, r.quantite as qte_r, quantite_entree, prix, prix_entree, conditionnement_entree, conditionnement, r.date_reception as date_r
					FROM yvs_com_contenu_doc_stock c LEFT JOIN yvs_com_contenu_doc_stock_reception r ON r.contenu=c.id where doc_stock = NEW.id
				LOOP
					IF((COALESCE(ligne_.id_r,0)!=0) AND NEW.destination is not null and (NEW.statut = 'R' or NEW.statut = 'V') or NEW.statut = 'R') THEN       --traitement de l'entree dans le depot destination
						delete from yvs_base_mouvement_stock where id_externe = ligne_.id_r and table_externe = 'yvs_com_contenu_doc_stock_reception' and depot = NEW.destination and mouvement = 'E';
						--insertion de l'entree
						result_ = (select valorisation_stock(ligne_.article, ligne_.conditionnement_entree, NEW.destination, trancheD_, ligne_.qte_r, ligne_.prix_entree, 'yvs_com_contenu_doc_stock_reception', ligne_.id_r, 'E', ligne_.date_r));
					END IF;
				END LOOP;
			ELSIF(NEW.type_doc = 'FT' and NEW.statut != 'V' and NEW.statut != 'U' and NEW.statut != 'R') then
				for ligne_ in select c.id AS id_c,r.id AS id_r  from yvs_com_contenu_doc_stock c LEFT JOIN yvs_com_contenu_doc_stock_reception r ON r.contenu=c.id where doc_stock = NEW.id
				loop			
					delete from yvs_base_mouvement_stock where (id_externe = ligne_.id_c and table_externe = 'yvs_com_contenu_doc_stock') OR (id_externe = COALESCE(ligne_.id_r,0) and table_externe = 'yvs_com_contenu_doc_stock_reception');
				end loop;		
			ELSIF(NEW.type_doc = 'SS' and NEW.statut = 'V') then
				for ligne_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
				loop
					--Insertion mouvement stock
					if(NEW.source is not null)then
						delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = ligne_.article and mouvement = 'S';
						result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.source, trancheS_, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_stock', ligne_.id, 'S', NEW.date_doc));
					end if;
				end loop;
			ELSIF(NEW.type_doc = 'SS' and NEW.statut != 'V') then
				for ligne_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
				loop			
					--Recherche mouvement stock				
					delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'S';
				end loop;
			ELSIF(NEW.type_doc = 'ES' and NEW.statut = 'V') then
				for ligne_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
				loop
					--Insertion mouvement stock
					if(NEW.destination is not null)then
						delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = ligne_.article and mouvement = 'E';
						result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement_entree, NEW.destination, trancheD_, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_stock', ligne_.id, 'E', NEW.date_doc));
					end if;
				end loop;
			ELSIF(NEW.type_doc = 'ES' and NEW.statut != 'V') then
				for ligne_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
				loop			
					delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E';
				end loop;
			ELSIF(NEW.type_doc = 'IN' and NEW.statut = 'V') then
				for ligne_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
				loop
					--Insertion mouvement stock
					if(NEW.source is not null)then
						delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = ligne_.article;
						if(ligne_.qte>0)then
							result_ = (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.destination, trancheD_, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_stock', ligne_.id, 'S', NEW.date_doc));
						elsif(ligne_.qte<0)then
							result_ = (select valorisation_stock(ligne_.article, ligne_.conditionnement_entree, NEW.destination, trancheD_, ligne_.qte, ligne_.prix, 'yvs_com_contenu_doc_stock', ligne_.id, 'E', NEW.date_doc));
						end if;
					end if;
				end loop;
			ELSIF(NEW.type_doc = 'IN' and NEW.statut != 'V') then
				for ligne_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
				loop			
					delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock';
				end loop;
			ELSIF(NEW.type_doc = 'TR' and NEW.statut = 'V') then
				for ligne_ in select id, article , quantite, quantite_entree as resultante, prix, prix_entree ,conditionnement as sortie, conditionnement_entree as entree, calcul_pr from yvs_com_contenu_doc_stock where doc_stock = OLD.id
				loop
					select into entree_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = ligne_.article and u.id = ligne_.entree;
					select into sortie_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = ligne_.article and u.id = ligne_.sortie;
					
					--Insertion mouvement stock
					select into mouv_ id, cout_stock from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = ligne_.article and mouvement = 'E';
					if(mouv_.id is not null)then
						if(entree_.methode_val = 'FIFO')then
							delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = ligne_.article and mouvement = 'E';									
							result_ = (select valorisation_stock(ligne_.article,ligne_.entree, NEW.source, trancheD_, ligne_.resultante, ligne_.prix_entree, 'yvs_com_contenu_doc_stock', ligne_.id, 'E', NEW.date_doc));
						else
							-- Recherche de la derniere entree de l'article dans le dépot
							last_pr_= COALESCE(get_pr(ligne_.article, NEW.source, 0, NEW.date_doc, ligne_.entree, mouv_.id), 0);
							PERFORM insert_mouvement_stock(ligne_.article, NEW.source, trancheD_, ligne_.resultante, ligne_.prix_entree, mouv_.cout_stock, null, 'yvs_com_contenu_doc_stock', ligne_.id, 'E', NEW.date_doc, last_pr_, mouv_.id);
						end if;
					else
						result_ = (select valorisation_stock(ligne_.article, ligne_.entree, NEW.source, trancheD_, ligne_.resultante, ligne_.prix_entree, 'yvs_com_contenu_doc_stock', ligne_.id, 'E', NEW.date_doc));
					end if;
					
					select into mouv_ id, cout_stock from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = ligne_.article and mouvement = 'S';
					if(mouv_.id is not null)then
						if(sortie_.methode_val = 'FIFO')then
							delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = ligne_.article and mouvement = 'S';																	
							result_ = (select valorisation_stock(ligne_.article,ligne_.sortie, NEW.source, trancheS_, ligne_.quantite, ligne_.prix, 'yvs_com_contenu_doc_stock', ligne_.id, 'S', NEW.date_doc));
						else
							-- Recherche de la derniere entree de l'article dans le dépot
							last_pr_= COALESCE(get_pr(ligne_.article, NEW.source, 0, NEW.date_doc, ligne_.sortie, mouv_.id), 0);
							PERFORM insert_mouvement_stock(ligne_.article, NEW.source, trancheS_, ligne_.quantite, ligne_.prix, mouv_.cout_stock, null, 'yvs_com_contenu_doc_stock', ligne_.id, 'S', NEW.date_doc, last_pr_, mouv_.id);
						end if;
					else
						result_ = (select valorisation_stock(ligne_.article,ligne_.sortie, NEW.source, trancheS_, ligne_.quantite, ligne_.prix, 'yvs_com_contenu_doc_stock', ligne_.id, 'S', NEW.date_doc));
					end if;	
				end loop;
			ELSIF(NEW.type_doc = 'TR' and NEW.statut != 'V') then
				for ligne_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
				loop			
					delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock';
				end loop;
			end if;
		END IF;
		RETURN NEW;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_stocks()
  OWNER TO postgres;
