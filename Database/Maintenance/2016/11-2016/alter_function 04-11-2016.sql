DROP FUNCTION get_puv(bigint, bigint, bigint, bigint);
CREATE OR REPLACE FUNCTION get_puv(article_ bigint, client_ bigint, depot_ bigint, point_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	puv_ double precision;
	data_ record;
	tarif_ record;

BEGIN
	if(client_ is not null and client_ > 0)then
		for data_ in select * from yvs_com_categorie_tarifaire where (permanent is true or (permanent is false and (date_ between date_debut and date_fin))) and actif = true order by priorite desc
		loop
			select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif = true;
			if(tarif_ is not null)then
				puv_ = tarif_.puv;
				return puv_;
			end if;
		end loop;
	end if;
	if(puv_ is null or puv_ < 1)then
		select into puv_ pr from yvs_base_article_depot where depot = depot_ and article = article_; 
		if(puv_ is null or puv_ <1)then
			select into puv_ puv from yvs_base_articles where id = article_; 
			if(puv_ is null or puv_ <1)then
				puv_ = 0;
			end if;
		end if;
	end if;
	return puv_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_puv(bigint, bigint, bigint, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_puv(bigint, bigint, bigint, bigint, date) IS 'retourne le prix de vente d'' article';

-- Function: get_puv(bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_puv(bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	pr_ double precision;

BEGIN
	if(depot_ is not null and depot_ > 0)then
		if(tranche_ is not null and tranche_ > 0)then
			select into pr_ cout_entree from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ and tranche = tranche_ and date_doc >= date_ order by date_doc desc limit 1;
		else
			select into pr_ cout_entree from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ and date_doc >= date_ order by date_doc desc limit 1;
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			select into pr_ cout_entree from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and tranche = tranche_ and date_doc >= date_ order by date_doc desc limit 1;
		else
			select into pr_ cout_entree from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and date_doc >= date_ order by date_doc desc limit 1;
		end if;
	end if;
	if(pr_ is null or pr_ < 1)then
		pr_ = get_pua(article_);
	end if;
	return pr_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date) IS 'retourne le prix de revient d'' article';
