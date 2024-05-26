-- DROP FUNCTION public.get_pr(int8, int8, int8, int8, date, int8, int8);

CREATE OR REPLACE FUNCTION public.get_pr(agence_ bigint, article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint)
 RETURNS double precision
 LANGUAGE plpgsql
AS $function$
DECLARE
	depot_ bigint ;
	_depot_ bigint ;
	line_ad_ record ;
	_agence bigint DEFAULT NULL;
	pr_ double precision;
	line_ record;
	date_debut date default date_;

BEGIN
	SELECT INTO _depot_ depot_pr FROM yvs_base_article_depot WHERE article = article_ AND depot = depot_;
	IF(COALESCE(_depot_,0)<=0) THEN
		IF(COALESCE(depot_,0)>0) THEN
			-- Récupère l'agence du dépôt
			SELECT INTO _agence d.agence FROM yvs_base_depots d WHERE d.id=depot_;
			agence_=_agence;
		END IF;
		IF(COALESCE(agence_,0)<=0) THEN
			SELECT INTO line_ad_ depot, categorie FROM yvs_base_article_depot WHERE article = article_ AND default_pr IS TRUE  LIMIT 1;
		ELSE
			SELECT INTO line_ad_ depot, categorie FROM yvs_base_article_depot ad INNER JOIN yvs_base_depots d ON d.id=ad.depot 
																		WHERE article = article_ AND default_pr IS TRUE AND d.agence=agence_ LIMIT 1;
		END IF;
		_depot_=line_ad_.depot;
	ELSE
	     -- Récupère la catégorie du produit dans le dépôt
	    -- SELECT INTO categorie_ ad.categorie FROM yvs_base_article_depot ad WHERE ad.article=article_ AND ad.depot=_depot_;
	END IF;
	depot_ = COALESCE(_depot_, depot_);
        -- A partir d'ici, je récupère le PR dans la table historique, qui fait office de cache pour le PR
	-- calcul de la prériode de référence
	IF(depot_ IS NOT NULL) THEN 
		date_debut=(date_ - interval '7 day')::date;
		select into line_ * from yvs_historique_pr y where y.conditionnement=unite_ and y.date_evaluation between date_debut and date_ and y.depot=depot_ LIMIT 1;
		if(line_.id is not null and line_.pr>0) then  
			return line_.pr;
		else
		   pr_=get_pr_reel(agence_, article_, depot_, tranche_, date_, unite_, current_);
		  --insert dans la table cache
		  insert into yvs_historique_pr (depot, conditionnement, date_evaluation, pr) values (depot_, unite_, date_, pr_);
		  return pr_;
		end if;
	ELSE 
		RETURN 0;
	END IF;
end;$function$
;
