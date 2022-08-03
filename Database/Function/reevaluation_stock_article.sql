-- Function: reevaluation_stock_article(bigint, bigint, date, date)

-- DROP FUNCTION reevaluation_stock_article(bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION reevaluation_stock_article(article_ bigint, depot_ bigint, date_debut date, date_fin date)
  RETURNS boolean AS
$BODY$
DECLARE 	
	mouv_ record;
	entree_ record;
	meth_ character varying;
	new_cout_ double precision default 0;
    
BEGIN
	select into meth_ methode_val from yvs_articles where id = article_;
	if (meth_ = 'FIFO')then
		for mouv_ in select id, cout_entree, cout_stock, quantite, parent from yvs_base_mouvement_stock where article = article_ and depot = depot_ 
					 and mouvement = 'S' and date_doc between date_debut and date_fin order by date_doc, id
		loop	
			select into new_cout_ cout_stock from yvs_base_mouvement_stock where id = mouv_.parent;
			if(new_cout_ is null)then
				new_cout_ = 0;
			end if;
			if(new_cout_ != mouv_.cout_entree)then
				update yvs_base_mouvement_stock set cout_entree = new_cout_, cout_stock = new_cout_ where id = mouv_.id;
			end if;	
		end loop;	
	else
		for mouv_ in select id, cout_entree, cout_stock, quantite, mouvement, date_doc from yvs_base_mouvement_stock where article = article_ 
					 and depot = depot_ and date_doc between date_debut and date_fin order by date_doc, id
		loop	
			if(mouv_.mouvement = 'E')then
				-- Recherche de la derniere entree de l'article dans le dépot
				select into entree_ quantite, cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ 
						and depot = depot_ and date_doc < mouv_.date_doc order by date_doc, id desc limit 1;
				-- Recherche des valeurs null
				if(entree_.quantite is null)then
					entree_.quantite = 0;
				end if;
				if(entree_.cout_stock is null)then
					entree_.cout_stock = 0;
				end if;
				if(mouv_.quantite is null)then
					mouv_.quantite = 0;
				end if;
				if(mouv_.cout_stock is null)then
					mouv_.cout_stock = 0;
				end if;
				-- Calcul du cout de stockage
				new_cout_ = (((entree_.quantite * entree_.cout_stock) + (mouv_.quantite * mouv_.cout_entree)) / (entree_.quantite + mouv_.quantite));
				if(new_cout is null)then
					new_cout_ = 0;
				end if;
				if(new_cout_ != mouv_.cout_stock)then
					update yvs_base_mouvement_stock set cout_stock = new_cout_ where id = mouv_.id;
				end if;	
			else
				if(new_cout_ != mouv_.cout_entree)then
					update yvs_base_mouvement_stock set cout_entree = new_cout_, cout_stock = new_cout_ where id = mouv_.id;
				end if;					
			end if;
		end loop;
	end if;	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION reevaluation_stock_article(bigint, bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION reevaluation_stock_article(bigint, bigint, date, date) IS 'fonction de réevaluation des stocks de l''article';
