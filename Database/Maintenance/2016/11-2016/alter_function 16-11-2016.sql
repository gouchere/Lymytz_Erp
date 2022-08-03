
ALTER TABLE yvs_com_grille_commission RENAME TO yvs_com_commission

-- Function: get_puv(bigint, bigint, bigint, bigint, date, boolean)

-- DROP FUNCTION get_puv(bigint, bigint, bigint, bigint, date, boolean);

CREATE OR REPLACE FUNCTION get_puv(article_ bigint, client_ bigint, depot_ bigint, point_ bigint, date_ date, min_ boolean)
  RETURNS double precision AS
$BODY$
DECLARE
	puv_ double precision;
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
								puv_ = tarif_.puv_min;
							else
								if(tarif_.nature_coef_augmentation = 'TAUX')then
									puv_ = tarif_.puv + ((tarif_.puv * tarif_.coef_augmentation)/100);
								else
									puv_ = tarif_.puv + tarif_.coef_augmentation;
								end if;
							end if;
						end if;
						exit;
					end if;
				else
					select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
					if(tarif_.id is not null)then
						if(min_)then
							puv_ = tarif_.puv_min;
						else
							if(tarif_.nature_coef_augmentation = 'TAUX')then
								puv_ = tarif_.puv + ((tarif_.puv * tarif_.coef_augmentation)/100);
							else
								puv_ = tarif_.puv + tarif_.coef_augmentation;
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


DROP FUNCTION get_ristourne(bigint, bigint, bigint, bigint);
-- Function: get_remise(bigint, bigint, bigint, bigint)
DROP FUNCTION get_remise(bigint, bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION get_remise(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	remise_ double precision;
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
							remise_ = valeur_ * (tarif_.remise /100);
						else
							remise_ = tarif_.remise;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL)then
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
							else
								remise_ = tarif_.remise;
							end if;
							exit;
						end if;
					end if;
					exit;
				end if;
			else
				select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
				if(tarif_.id is not null)then
					if(tarif_.nature_remise = 'TAUX')then
						remise_ = valeur_ * (tarif_.remise /100);
					else
						remise_ = tarif_.remise;
					end if;
					select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
					if(tarif_.id IS NOT NULL)then
						if(tarif_.nature_remise = 'TAUX')then
							remise_ = valeur_ * (tarif_.remise /100);
						else
							remise_ = tarif_.remise;
						end if;
						exit;
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


-- Function: get_ristourne(bigint, double precision, double precision, bigint, date)

-- DROP FUNCTION get_ristourne(bigint, double precision, double precision, bigint, date);

CREATE OR REPLACE FUNCTION get_ristourne(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	ristourne_ double precision;
	data_ record;
	plan_ bigint;
	tarif_ record;
	valeur_ double precision default 0;

BEGIN
	valeur_ = qte_ * prix_;
	
	if(client_ is not null and client_ > 0)then
		select into data_ * from yvs_com_client where id = client_;
		if(data_.plan_ristourne is not null)then
			select into plan_ id from yvs_com_plan_ristourne where actif = true and id = data_.plan_ristourne;
			if(plan_ is not null)then
				for data_ in select * from yvs_com_ristourne where actif = true and plan = plan_
				loop
					if(data_.permanent is false)then
						if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
							select into tarif_ * from yvs_com_grille_ristourne where ristourne = data_.id and ((base = 'QTE' and qte_ between montant_minimal and montant_maximal)or(base = 'CA' and valeur_ between montant_minimal and montant_maximal));
							if(tarif_.id is not null)then
								if(tarif_.nature_montant = 'TAUX')then
									ristourne_ = valeur_ * (tarif_.montant_ristourne /100);
								else
									ristourne_ = tarif_.montant_ristourne;
								end if;
							end if;
							exit;
						end if;
					else
						select into tarif_ * from yvs_com_grille_ristourne where ristourne = data_.id and ((base = 'QTE' and qte_ between montant_minimal and montant_maximal)or(base = 'CA' and valeur_ between montant_minimal and montant_maximal));
						if(tarif_.id is not null)then
							if(tarif_.nature_montant = 'TAUX')then
								ristourne_ = valeur_ * (tarif_.montant_ristourne /100);
							else
								ristourne_ = tarif_.montant_ristourne;
							end if;
						end if;
						exit;
					end if;	
				end loop;
			end if;	
		end if;
		RAISE NOTICE '%',ristourne_;
		if(ristourne_ is null or ristourne_ < 1)then
			for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
			loop
				if(data_.permanent is false)then
					if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
						select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
						if(tarif_.id is not null)then
							if(tarif_.nature_ristourne = 'TAUX')then
								ristourne_ = valeur_ * (tarif_.ristourne /100);
							else
								ristourne_ = tarif_.ristourne;
							end if;
							for tarif_ in select * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id
							loop
								if(tarif_.base = 'QTE')then
									if(tarif_.valeur_min <= qte_ and qte <= tarif_.valeur_max)then
										if(tarif_.nature_ristourne = 'TAUX')then
											ristourne_ = valeur_ * (tarif_.ristourne /100);
										else
											ristourne_ = tarif_.ristourne;
										end if;
										exit;
									end if;
								else
									if(tarif_.valeur_min <= valeur_ and valeur_ <= tarif_.valeur_max)then
										if(tarif_.nature_ristourne = 'TAUX')then
											ristourne_ = valeur_ * (tarif_.ristourne /100);
										else
											ristourne_ = tarif_.ristourne;
										end if;
										exit;
									end if;
								end if;
							end loop;
						end if;
						exit;
					end if;
				else
					select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
					if(tarif_.id is not null)then
						RAISE NOTICE '%',valeur_;
						if(tarif_.nature_ristourne = 'TAUX')then
							ristourne_ = valeur_ * (tarif_.ristourne /100);
						else
							ristourne_ = tarif_.ristourne;
						end if;
						for tarif_ in select * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id
						loop
							if(tarif_.base = 'QTE')then
								if(tarif_.valeur_min <= qte_ and qte <= tarif_.valeur_max)then
									if(tarif_.nature_ristourne = 'TAUX')then
										ristourne_ = valeur_ * (tarif_.ristourne /100);
									else
										ristourne_ = tarif_.ristourne;
									end if;
									exit;
								end if;
							else
								if(tarif_.valeur_min <= valeur_ and valeur_ <= tarif_.valeur_max)then
									if(tarif_.nature_ristourne = 'TAUX')then
										ristourne_ = valeur_ * (tarif_.ristourne /100);
									else
										ristourne_ = tarif_.ristourne;
									end if;
									exit;
								end if;
							end if;
						end loop;
					end if;
					exit;
				end if;
			end loop;
		end if;		
	end if;
	
	if(ristourne_ is null or ristourne_ <1)then
		ristourne_ = 0;
	end if;
	return ristourne_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ristourne(bigint, double precision, double precision, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ristourne(bigint, double precision, double precision, bigint, date) IS 'retourne la ristourne d'' article';

-- Function: get_commission(bigint, double precision, double precision, bigint, date)

-- DROP FUNCTION get_commission(bigint, double precision, double precision, bigint, date);

CREATE OR REPLACE FUNCTION get_commission(article_ bigint, qte_ double precision, prix_ double precision, users_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	commission_ double precision;
	data_ record;
	plan_ bigint;
	tarif_ record;
	valeur_ double precision default 0;

BEGIN
	valeur_ = qte_ * prix_;
	
	if(users_ is not null and users_ > 0)then
		select into data_ * from yvs_users where id = users_;
		if(data_.plan_commission is not null)then
			select into plan_ id from yvs_com_plan_commission where actif = true and id = data_.plan_commission;
			if(plan_ is not null)then
				for data_ in select * from yvs_com_commission where actif = true and plan = plan_
				loop
					if(data_.permanent is false)then
						if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
							select into tarif_ * from yvs_com_grille_commission where commission = data_.id and ((base = 'QTE' and qte_ between montant_minimal and montant_maximal)or(base = 'CA' and valeur_ between montant_minimal and montant_maximal));
							if(tarif_.id is not null)then
								if(tarif_.nature_montant = 'TAUX')then
									commission_ = valeur_ * (tarif_.montant_commission /100);
								else
									commission_ = tarif_.montant_commission;
								end if;
							end if;
							exit;
						end if;
					else
						select into tarif_ * from yvs_com_grille_commission where commission = data_.id and ((base = 'QTE' and qte_ between montant_minimal and montant_maximal)or(base = 'CA' and valeur_ between montant_minimal and montant_maximal));
						if(tarif_.id is not null)then
							if(tarif_.nature_montant = 'TAUX')then
								commission_ = valeur_ * (tarif_.montant_commission /100);
							else
								commission_ = tarif_.montant_commission;
							end if;
						end if;
						exit;
					end if;	
				end loop;
			end if;	
		end if;
		RAISE NOTICE '%',commission_;	
	end if;
	
	if(commission_ is null or commission_ <1)then
		commission_ = 0;
	end if;
	return commission_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_commission(bigint, double precision, double precision, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_commission(bigint, double precision, double precision, bigint, date) IS 'retourne la commission d'' article';

