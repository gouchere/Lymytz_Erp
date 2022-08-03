-- Function: get_pr(bigint, bigint, bigint, date, bigint, bigint)

-- DROP FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint);

CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	_depot_ bigint ;
	pr_ double precision;
	coef_ double precision;
	ecart_ double precision;
	prix_nom_ double precision;
	line_ record;
	categorie_ character varying;
	query_ character varying default 'SELECT m.cout_stock, a.categorie, a.taux_ecart_pr, m.conditionnement FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id 
																						  INNER JOIN yvs_base_articles a ON a.id=m.article
														  WHERE COALESCE(m.calcul_pr, TRUE) IS TRUE AND m.mouvement = ''E''';

BEGIN
	SELECT INTO _depot_ depot_pr FROM yvs_base_article_depot WHERE article = article_ AND (depot = depot_ OR default_pr IS TRUE);
	_depot_ = COALESCE(_depot_, depot_);
	query_ = query_ || ' AND m.article = '||COALESCE(article_, 0)||' AND m.date_doc <= '||QUOTE_LITERAL(COALESCE(date_, CURRENT_DATE));
	IF(COALESCE(_depot_, 0) > 0)THEN
		query_ = query_ || ' AND m.depot = '||_depot_;
	END IF;
	IF(COALESCE(tranche_, 0) > 0)THEN
		query_ = query_ || ' AND m.tranche = '||tranche_;
	END IF;
	-- IF(COALESCE(unite_, 0) > 0)THEN
		-- query_ = query_ || ' AND m.conditionnement = '||unite_;
	-- END IF;
	IF(COALESCE(current_, 0) > 0)THEN
		query_ = query_ || ' AND m.id != '||current_;
	END IF;
	query_ = query_ || ' ORDER BY m.date_doc DESC LIMIT 1';
	EXECUTE query_ INTO line_;
	ecart_= COALESCE(line_.taux_ecart_pr,0);
	categorie_=line_.categorie;
	IF(line_.conditionnement=unite_) THEN
		pr_=COALESCE(line_.cout_stock,0);
	ELSE		
		--recherche le lien de conversion entre line_.conditionnement et unite_
		SELECT INTO coef_ taux_change FROM yvs_base_table_conversion t INNER JOIN yvs_base_conditionnement us ON us.unite=t.unite
													  INNER JOIN yvs_base_conditionnement ud ON ud.unite=t.unite_equivalent
													  WHERE us.id=unite_ AND ud.id=line_.conditionnement;
		coef_=COALESCE(coef_,0);
		pr_=COALESCE(line_.cout_stock,0)*coef_; 
		RAISE NOTICE 'here % %',line_.conditionnement, line_.cout_stock;
	END IF;	
	--RAISE NOTICE 'coût : % catégorie :%',line_.cout_stock, line_.categorie;
	IF(ecart_>0) THEN
		IF(categorie_='PF' OR categorie_='PSF') THEN			
			prix_nom_ = get_prix_nomenclature(article_, unite_, depot_, date_);
			IF(COALESCE(prix_nom_,0)>0 AND (abs(pr_- prix_nom_))>ecart_) THEN
				pr_=prix_nom_;
			END IF;
		END IF;
	END IF;
	IF(pr_ <=0)THEN
		IF(line_.categorie IS NULL) THEN 
		   SELECT INTO categorie_ a.categorie FROM yvs_base_articles a WHERE a.id=article_;
		ELSE
		   categorie_=line_.categorie;
		END IF;
		IF(categorie_='PF' OR categorie_='PSF') THEN			
			pr_ = get_prix_nomenclature(article_, unite_, depot_, date_);
			IF(COALESCE(pr_,0)<=0) THEN
			 pr_ = get_pua(article_, 0, 0, unite_);
			END IF;
		ELSE
			 pr_ = get_pua(article_, 0, 0, unite_);
		END IF;
	END IF;
	RETURN pr_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint) IS 'retourne le prix de revient d'' article';
