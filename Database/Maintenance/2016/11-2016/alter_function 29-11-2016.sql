-- Function: get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean)

-- DROP FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean);

CREATE OR REPLACE FUNCTION get_puv(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, depot_ bigint, point_ bigint, date_ date, min_ boolean)
  RETURNS double precision AS
$BODY$
DECLARE
	puv_ double precision;
	garde_ double precision;
	data_ record;
	tarif_ record;
	valeur_ double precision default 0;
	pr_  double precision default 0;

BEGIN
	valeur_ = qte_ * prix_;
	pr_ = (select get_pr(article_, depot_, 0 ,date_));
	
	-- Recherche du prix du point de vente
	if(point_ is not null and point_ > 0)then
		select into tarif_ * from yvs_base_article_point where article = article_ and point = point_ limit 1;
		if(tarif_.id is not null)then
			if(tarif_.prioritaire is true)then --Verification si ce prix est prioritaire
				if(min_)then
					if(tarif_.nature_prix_min = 'TAUX')then
						puv_ = (pr_ * tarif_.puv_min) /100;
					else
						puv_ = tarif_.puv_min;
					end if;
				else
					puv_ = tarif_.puv;
				end if;
			end if;
		end if;
	end if;
	
	if(puv_ is null or puv_ < 1)then
		-- Recherche du prix en fonction de la catégorie tarifaire
		if(client_ is not null and client_ > 0)then
			for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
			loop
				if(data_.permanent is false)then
					if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
						select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
						if(tarif_.id is not null)then
							if(min_)then
								if(tarif_.nature_prix_min = 'TAUX')then
									puv_ = (pr_ * tarif_.puv_min) /100;
								else
									puv_ = tarif_.puv_min;
								end if;
							else
								garde_ = tarif_.puv;
								select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
								if(tarif_.id IS NOT NULL)then
									puv_ = tarif_.puv;
								end if;
								if(puv_ is null or puv_ < 1)then
									puv_ = garde_;
								end if;
							end if;
						end if;
						exit;
					end if;
				else
					select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
					if(tarif_.id is not null)then
						if(min_)then
							if(tarif_.nature_prix_min = 'TAUX')then
								puv_ = (pr_ * tarif_.puv_min) /100;
							else
								puv_ = tarif_.puv_min;
							end if;
						else
							garde_ = tarif_.puv;
							select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
							if(tarif_.id IS NOT NULL)then
								puv_ = tarif_.puv;
							end if;
							if(puv_ is null or puv_ < 1)then
								puv_ = garde_;
							end if;
						end if;
					end if;
					exit;
				end if;
			end loop;
		end if;
		
		if(puv_ is null or puv_ < 1)then
			-- Recherche du prix du point de vente non prioritaire
			if(point_ is not null and point_ > 0)then
				select into tarif_ * from yvs_base_article_point where article = article_ and point = point_ limit 1;
				if(tarif_.id is not null)then
					if(min_)then
						if(tarif_.nature_prix_min = 'TAUX')then
							puv_ = (pr_ * tarif_.puv_min) /100;
						else
							puv_ = tarif_.puv_min;
						end if;
					else
						puv_ = tarif_.puv;
					end if;
				end if;
			end if;
			
			if(puv_ is null or puv_ < 1)then
				-- Recherche du prix du depot
				if(depot_ is not null and depot_ > 0)then
					select into tarif_ * from yvs_base_article_depot where depot = depot_ and article = article_ limit 1; 
					if(tarif_.id is not null)then
						if(min_)then
							if(tarif_.nature_prix_min = 'TAUX')then
								puv_ = (pr_ * tarif_.puv_min) /100;
							else
								puv_ = tarif_.puv_min;
							end if;
						else
							puv_ = tarif_.pr;
						end if;
					end if;
					
					-- Recherche du prix de l'article sur la fiche d'article
					if(puv_ is null or puv_ <1)then
						select into tarif_ * from yvs_base_articles where id = article_; 
						if(tarif_.id is not null)then
							if(min_)then
								if(tarif_.nature_prix_min = 'TAUX')then
									puv_ = (pr_ * tarif_.prix_min) /100;
								else
									puv_ = tarif_.prix_min;
								end if;
							else
								puv_ = tarif_.puv;
							end if;
						end if;
					end if;
				end if;
			end if;
		end if;
	end if;
						
	if(puv_ is null or puv_ <1)then
		puv_ = 0;
	end if;
	return puv_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, boolean) IS 'retourne le prix de vente d'' article';


-- Function: get_taxe(bigint, bigint, bigint, double precision, double precision, double precision)

-- DROP FUNCTION get_taxe(bigint, bigint, bigint, double precision, double precision, double precision);

CREATE OR REPLACE FUNCTION get_taxe(article_ bigint, categorie_ bigint, compte_ bigint, remise_ double precision, qte_ double precision, prix_ double precision)
  RETURNS double precision AS
$BODY$
DECLARE
	taxe_ double precision default 0;
	valeur_ double precision default 0;
	data_ record;
	id_ bigint;

BEGIN
	valeur_ = qte_ * prix_;
	if(compte_ is not null and compte_ > 0)then
		select into id_ id from yvs_base_article_categorie_comptable where article = article_ and categorie = categorie_ and compte = compte_ and actif = true;
	else
		select into id_ id from yvs_base_article_categorie_comptable where article = article_ and categorie = categorie_ and actif = true;
	end if;
	if(id_ is not null and id_ > 0)then
		for data_ in select c.app_remise , t.taux from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
		loop
			if(data_.app_remise)then
				taxe_ = taxe_ + (((valeur_ - remise_) * data_.taux) / 100);
			else
				taxe_ = taxe_ + ((valeur_ * data_.taux) / 100);
			end if;
		end loop;
	end if;	
	if(taxe_ is null or taxe_ <1)then
		taxe_ = 0;
	end if;
	return taxe_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_taxe(bigint, bigint, bigint, double precision, double precision, double precision)
  OWNER TO postgres;
COMMENT ON FUNCTION get_taxe(bigint, bigint, bigint, double precision, double precision, double precision) IS 'retourne la taxe d'' article';
