-- Function: valorisation_stock_by_cmp1(bigint, bigint, bigint, bigint, double precision, double precision, character varying, bigint, date)
-- DROP FUNCTION valorisation_stock_by_cmp1(bigint, bigint, bigint, bigint, double precision, double precision, character varying, bigint, date);
CREATE OR REPLACE FUNCTION valorisation_stock_by_cmp1(
    article_ bigint,
    conditionnement_ bigint,
    depot_ bigint,
    tranche_ bigint,
    quantite_ double precision,
    cout_ double precision,
    tableexterne_ character varying,
    idexterne_ bigint,
    date_ date)
  RETURNS boolean AS
$BODY$
DECLARE 
	entree_ record;  
	new_cout double precision default 0;
	stock_ double precision default 0;
	last_pr_ double precision default 0;
	prix_arr_ numeric;
BEGIN
	-- Calcul du cout de stockage	
	-- Recherche de la derniere entree de l'article dans le dépot
	last_pr_= COALESCE(get_pr(article_, depot_, 0, date_, conditionnement_, 0), 0);
	--calcule le stock
	stock_ = COALESCE(get_stock_reel(article_,0,depot_,0,0,date_,conditionnement_,0), 0);
	-- Calcul du cout de stockage
	prix_arr_ = stock_ + COALESCE(quantite_, 0);
	if(prix_arr_ <= 0)then
		prix_arr_ = 1;
	end if;
	new_cout = (((stock_ * last_pr_) + (COALESCE(quantite_, 0) * COALESCE(cout_, 0))) / (prix_arr_));
	-- Retourne le nouveau cout moyen calculé
	--arrondi les chiffres
	prix_arr_= new_cout AS numeric;
	SELECT INTO new_cout round(prix_arr_,3);
	-- Insertion du mouvement de stock
	if (select insert_mouvement_stock(article_, depot_, tranche_, COALESCE(quantite_, 0), COALESCE(cout_, 0), new_cout, null, tableexterne_, idexterne_, 'E', date_, last_pr_, 0))then
		return true;
	else
		return false;
	end if;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_cmp1(bigint, bigint, bigint, bigint, double precision, double precision, character varying, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_cmp1(bigint, bigint, bigint, bigint, double precision, double precision, character varying, bigint, date) IS 'Valorisation de stock pas la methode CMP I';
