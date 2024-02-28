-- DROP FUNCTION public.get_pr_reel(int8, int8, int8, int8, date, int8, int8);

CREATE OR REPLACE FUNCTION public.get_pr_reel(agence_ bigint, article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint)
 RETURNS double precision
 LANGUAGE plpgsql
AS $function$
DECLARE
	_depot_ bigint ;
	_agence bigint DEFAULT NULL;
	pr_ double precision;
	coef_ double precision;
	ecart_ double precision;
	prix_nom_ double precision;
	line_ record;
	line_ad_ record;
	categorie_ character varying;
	valorise_from_of_ Boolean;
	query_ character varying default 'SELECT m.cout_stock, a.categorie, a.taux_ecart_pr, m.conditionnement FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id 
																						  INNER JOIN yvs_base_articles a ON a.id=m.article
														  WHERE COALESCE(m.calcul_pr, TRUE) IS TRUE AND m.mouvement = ''E''';

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
		categorie_=line_ad_.categorie;
	ELSE
	     -- Récupère la catégorie du produit dans le dépôt
	     SELECT INTO categorie_ ad.categorie FROM yvs_base_article_depot ad WHERE ad.article=article_ AND ad.depot=_depot_;
	END IF;
	depot_ = COALESCE(_depot_, depot_);
	IF(COALESCE(agence_,0)<=0) THEN
	     SELECT INTO agence_ d.agence FROM yvs_base_depots d WHERE d.id=depot_;
	END IF;
	RAISE NOTICE 'AGENCE= % DEPOT= %, CATEGORIE= %',agence_, depot_, categorie_;
	SELECT INTO valorise_from_of_ valorise_from_of FROM yvs_prod_parametre WHERE societe=(SELECT a.societe FROM yvs_agences a WHERE a.id=agence_);
	valorise_from_of_=COALESCE(valorise_from_of_,true);
	query_ = query_ || ' AND m.article = '||COALESCE(article_, 0)||' AND m.date_doc <= '||QUOTE_LITERAL(COALESCE(date_, CURRENT_DATE));
	IF(COALESCE(_depot_, 0) > 0)THEN
		query_ = query_ || ' AND m.depot = '||_depot_;
	END IF;
	IF(COALESCE(tranche_, 0) > 0)THEN
		query_ = query_ || ' AND m.tranche = '||tranche_;
	END IF;			
	IF(COALESCE(current_, 0) > 0)THEN
		query_ = query_ || ' AND m.id != '||current_;
	END IF;
	query_ = query_ || ' ORDER BY m.date_doc DESC LIMIT 1';
	EXECUTE query_ INTO line_;
	ecart_= COALESCE(line_.taux_ecart_pr,0);	
	categorie_=COALESCE(categorie_,line_.categorie);
	IF((categorie_='PF' OR categorie_='PSF') AND COALESCE(valorise_from_of_,false) IS FALSE) THEN
			-- Retourne le prix de la nomenclature si la société evalue ses PF au PR
			pr_ = get_prix_nomenclature(article_, unite_, depot_, date_);
	ELSE					
			IF(line_.conditionnement=unite_) THEN
				pr_=COALESCE(line_.cout_stock,0);
			ELSE		
				--recherche le lien de conversion entre line_.conditionnement et unite_
				SELECT INTO coef_ taux_change FROM yvs_base_table_conversion t INNER JOIN yvs_base_conditionnement us ON us.unite=t.unite
															  INNER JOIN yvs_base_conditionnement ud ON ud.unite=t.unite_equivalent
															  WHERE us.id=unite_ AND ud.id=line_.conditionnement;
				coef_=COALESCE(coef_,0);
				pr_=COALESCE(line_.cout_stock,0)*coef_; 
			END IF;	
			--RAISE NOTICE 'coût : % catégorie :%',line_.cout_stock, line_.categorie;
			IF(ecart_>0) THEN
			-- si une valeur d'ecart est définie pour surveiller les grands changement du pr, alors...
				IF(categorie_='PF' OR categorie_='PSF') THEN			
					-- dans le le cas des produit fabriqué, le prix barrière peut être le prix de la nomenclature
					prix_nom_ = get_prix_nomenclature(article_, unite_, depot_, date_);
					IF(COALESCE(prix_nom_,0)>0 AND (abs(pr_- prix_nom_))>ecart_) THEN
						pr_=prix_nom_;
					END IF;
				END IF;
			END IF;
			IF(pr_ <=0)THEN
				IF(line_.categorie IS NULL) THEN 
				   SELECT INTO categorie_ a.categorie FROM yvs_base_article_depot a WHERE a.article=article_;
				   IF(categorie_ IS NULL) THEN
						SELECT INTO categorie_ a.categorie FROM yvs_base_articles a WHERE a.id=article_;
				   END IF;
				ELSE
				   categorie_=line_.categorie;
				END IF;
				IF(categorie_='PF' OR categorie_='PSF') THEN			
					pr_ = get_prix_nomenclature(article_, unite_, depot_, date_);
					IF(COALESCE(pr_,0)<=0) THEN
					 pr_ = 0;
					END IF;
				ELSE
					 pr_ = 0;
				END IF;
			END IF;
	END IF;
	RETURN pr_;
end;$function$
;
