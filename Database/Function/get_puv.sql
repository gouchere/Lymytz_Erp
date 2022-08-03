-- Function: get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, bigint, boolean)

-- DROP FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, bigint, boolean);

CREATE OR REPLACE FUNCTION get_puv(
    article_ bigint,
    qte_ double precision,
    prix_ double precision,
    client_ bigint,
    depot_ bigint,
    point_ bigint,
    date_ date,
    unite_ bigint,
    min_ boolean)
  RETURNS double precision AS
$BODY$
DECLARE
	puv_ double precision;
	garde_ double precision;
	data_ record;
	tarif_ record;
	valeur_ double precision default 0;
	pr_  double precision default null;

BEGIN
	valeur_ = qte_ * prix_;
	
	-- Recherche du prix du point de vente
	if(point_ is not null and point_ > 0)then
		if(unite_ is not null and unite_ > 0)then
			-- select into tarif_ y.*, a.prioritaire from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.id = unite_ and a.actif is true limit 1;
			-- if(tarif_.id is not null)then
				-- if(tarif_.prioritaire is true)then --Verification si ce prix est prioritaire
					-- if(min_)then
						-- if(tarif_.nature_prix_min = 'TAUX')then
							-- puv_ = (pr_ * tarif_.prix_min) /100;
						-- else
							-- puv_ = tarif_.prix_min;
						-- end if;
					-- else
					-- RAISE NOTICE 'Prix du point First ';
						-- puv_ = tarif_.puv;
					-- end if;
				-- end if;
			-- end if;
		end if;
	end if;
	
	if(puv_ is null or puv_ < 1)then
		-- Recherche du prix en fonction de la catégorie tarifaire
		if(client_ is not null and client_ > 0)then
		--Récupère les catégories du client
			for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
			loop
				if(data_.permanent is false)then
					if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
						if(unite_ is not null and unite_ > 0)then
							select into tarif_ y.* from yvs_base_plan_tarifaire y inner join yvs_base_conditionnement c on y.conditionnement = c.id where y.categorie = data_.categorie and y.article = article_ and c.id = unite_ and y.actif is true limit 1;
							if(tarif_.id is not null)then
								if(min_)then
									if(tarif_.nature_prix_min = 'TAUX')then
										if(pr_ is null)then
											pr_ = (select get_pr(article_, depot_, 0 ,date_, unite_));
										end if;
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
									RAISE NOTICE 'Prix catégoriel  périodique';
								end if;
							end if;
						end if;
						exit;
					end if;
				else
					if(unite_ is not null and unite_ > 0)then
						select into tarif_ y.* from yvs_base_plan_tarifaire y inner join yvs_base_conditionnement c on y.conditionnement = c.id where y.categorie = data_.categorie and y.article = article_ and c.id = unite_ and y.actif is true limit 1;
						if(tarif_.id is not null)then
							if(min_)then
								if(tarif_.nature_prix_min = 'TAUX')then
									if(pr_ is null)then	
										pr_ = (select get_pr(article_, depot_, 0 ,date_, unite_));
									end if;
									puv_ = (pr_ * tarif_.puv_min) /100;
								else
									puv_ = tarif_.puv_min;
								end if;
							else
								garde_ = tarif_.puv;
								select into tarif_ * from yvs_base_plan_tarifaire_tranche WHERE plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
								if(tarif_.id IS NOT NULL)then
									puv_ = tarif_.puv;
								end if;
								if(puv_ is null or puv_ < 1)then
									puv_ = garde_;
								end if;
								RAISE NOTICE 'Prix catégoriel  permanent';
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
				if(unite_ is not null and unite_ > 0)then
					select into tarif_ y.* from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.id = unite_ and a.actif is true limit 1;
					if(tarif_.id is not null)then
						if(min_)then
							if(tarif_.nature_prix_min = 'TAUX')then
								if(pr_ is null)then	
									pr_ = (select get_pr(article_, depot_, 0 ,date_, unite_));
								end if;
								puv_ = (pr_ * tarif_.prix_min) /100;
							else
								puv_ = tarif_.prix_min;
							end if;
						else
						RAISE NOTICE 'Prix point seconds';
							puv_ = tarif_.puv;
						end if;
					end if;				
				end if;
			end if;
			
			if(puv_ is null or puv_ < 1)then					
				-- Recherche du prix de l'article sur la fiche d'article
				if(unite_ is not null and unite_ > 0)then
					select into tarif_ * from yvs_base_conditionnement where id = unite_;
					if(tarif_.id is not null)then
						if(min_)then
							if(tarif_.nature_prix_min = 'TAUX')then
								if(pr_ is null)then
									pr_ = (select get_pr(article_, depot_, 0 ,date_, unite_));
								end if;
								puv_ = (pr_ * tarif_.prix_min) /100;
							else
								puv_ = tarif_.prix_min;
							end if;
						else
							RAISE NOTICE 'Prixcond';
							puv_ = tarif_.prix;
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
ALTER FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, bigint, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, bigint, boolean) IS 'retourne le prix de vente d'' article';
