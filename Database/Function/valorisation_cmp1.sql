-- Function: valorisation_stock_by_cmp1(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date)

-- DROP FUNCTION valorisation_stock_by_cmp1(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date);

CREATE OR REPLACE FUNCTION valorisation_stock_by_cmp1(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE 
	entree_ record;  
	new_cout double precision default 0;
	stock_ double precision default 0;
	prix_arr_ numeric;
BEGIN
	-- Calcul du cout de stockage	
	-- Recherche de la derniere entree de l'article dans le dépot
	if(tranche_ != null and tranche_ >0)then
		select into entree_ quantite, cout_stock as cout from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ and tranche = tranche_ order by date_doc DESC, id desc limit 1;
	else
		select into entree_ quantite, cout_stock as cout from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ order by date_doc DESC, id desc limit 1;
	end if;
	--calcule le stockt
		stock_=get_stock(article_,depot_,date_);
	-- Recherche des valeurs null
	if(stock_ is null) THEN
		stock_=0;
	END IF;
	if(quantite_ is null)then
		quantite_ = 0;
	end if;
	if(cout_ is null)then
		cout_ = 0;
	end if;
	if(entree_.quantite is null)then
		entree_.quantite = 0;
	end if;
	if(entree_.cout is null)then
		entree_.cout = 0;
	end if;
	-- Calcul du cout de stockage
	new_cout = (((stock_ * entree_.cout) + (quantite_ * cout_)) / (stock_ + quantite_));
	-- Retourne le nouveau cout moyen calculé
	if(new_cout is null)then
		new_cout = 0;
	end if;
	--arrondi les chiffres
	prix_arr_=new_cout AS numeric;
	SELECT INTO new_cout round(prix_arr_,3);
	-- Insertion du mouvement de stock
	if (select insert_mouvement_stock(article_, depot_, tranche_, quantite_ , cout_, new_cout, null, tableexterne_, idexterne_, 'E', date_))then
		return true;
	else
		return false;
	end if;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_cmp1(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_cmp1(bigint, bigint, bigint, double precision, double precision, character varying, bigint, date) IS 'Valorisation de stock pas la methode CMP I';
