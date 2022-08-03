
CREATE OR REPLACE FUNCTION get_remise(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	remise_ double precision;
	garde_ double precision;
	data_ record;
	tarif_ record;
	valeur_ double precision default 0;

BEGIN
	valeur_ = qte_ * prix_;
	
	if(client_ is not null and client_ > 0)then
		for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
		loop
			if(data_.permanent is false)then
				if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
					select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
						else
							garde_ = tarif_.remise;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL)then
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
							else
								remise_ = tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ < 1)then
							remise_ = garde_;
						end if;
					end if;
					exit;
				end if;
			else
				select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
				if(tarif_.id is not null)then
					if(tarif_.nature_remise = 'TAUX')then
						garde_ = valeur_ * (tarif_.remise /100);
					else
						garde_ = tarif_.remise;
					end if;
					select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
					if(tarif_.id IS NOT NULL)then
						if(tarif_.nature_remise = 'TAUX')then
							remise_ = valeur_ * (tarif_.remise /100);
						else
							remise_ = tarif_.remise;
						end if;
					end if;
					if(remise_ is null or remise_ < 1)then
						remise_ = garde_;
					end if;
				end if;
				exit;
			end if;
		end loop;
	end if;
	if(remise_ is null or remise_ < 1)then
		select into remise_ remise from yvs_base_articles where id = article_; 
	end if;
	
	if(remise_ is null or remise_ <1)then
		remise_ = 0;
	end if;
	return remise_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise(bigint, double precision, double precision, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise(bigint, double precision, double precision, bigint, date) IS 'retourne la remise d'' article';

-- Function: get_puv(bigint, bigint, bigint, bigint, date, boolean)

-- DROP FUNCTION get_puv(bigint, bigint, bigint, bigint, date, boolean);

CREATE OR REPLACE FUNCTION get_puv(article_ bigint, client_ bigint, depot_ bigint, point_ bigint, date_ date, min_ boolean)
  RETURNS double precision AS
$BODY$
DECLARE
	puv_ double precision;
	garde_ double precision;
	data_ record;
	tarif_ record;

BEGIN
	-- Recherche du prix du point de vente
	if(point_ is not null and point_ > 0)then
		select into tarif_ * from yvs_base_article_point where article = article_ and point = point_ limit 1;
		if(tarif_.id is not null)then
			if(tarif_.prioritaire is true)then --Verification si ce prix est prioritaire
				if(min_)then
					puv_ = tarif_.puv_min;
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
								garde_ = tarif_.puv_min;
							else
								garde_ = tarif_.puv;
							end if;
							select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
							if(tarif_.id IS NOT NULL)then
								puv_ = tarif_.puv;
							end if;
							if(remise_ is null or remise_ < 1)then
								puv_ = garde_;
							end if;
						end if;
						exit;
					end if;
				else
					select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
					if(tarif_.id is not null)then
						if(min_)then
							garde_ = tarif_.puv_min;
						else
							garde_ = tarif_.puv;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL)then
							puv_ = tarif_.puv;
						end if;
						if(remise_ is null or remise_ < 1)then
							puv_ = garde_;
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
						puv_ = tarif_.puv_min;
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
							puv_ = tarif_.puv_min;
						else
							puv_ = tarif_.pr;
						end if;
					end if;
					
					-- Recherche du prix de l'article sur la fiche d'article
					if(puv_ is null or puv_ <1)then
						select into tarif_ * from yvs_base_articles where id = article_; 
						if(tarif_.id is not null)then
							if(min_)then
								puv_ = tarif_.prix_min;
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
ALTER FUNCTION get_puv(bigint, bigint, bigint, bigint, date, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION get_puv(bigint, bigint, bigint, bigint, date, boolean) IS 'retourne le prix de vente d'' article';
