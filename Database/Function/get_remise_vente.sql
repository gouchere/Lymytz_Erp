-- Function: get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer)

-- DROP FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer);

CREATE OR REPLACE FUNCTION get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ integer)
  RETURNS double precision AS
$BODY$
DECLARE
	data_ record;
	tarif_ record;
	
	valeur_ double precision default 0;
	remise_ double precision;
	garde_ double precision;

	famille_ bigint;

	control_ boolean default false;
	planifier_ boolean default false;
BEGIN
	valeur_ = qte_ * prix_;	
	if(client_ is not null and client_ > 0)then
		select into famille_ y.famille from yvs_base_articles y where y.id = article_;
		for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
		loop
			control_ = false;
			planifier_ = false;
-- 			RAISE NOTICE 'categorie : %',data_.categorie;
			if(data_.permanent is false)then
				if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
					control_ = true;
					planifier_ = true;
				end if;
			else
				control_ = true;
			end if;
-- 			RAISE NOTICE 'control_ : %',control_;
			if(control_)then
				if(unite_ is not null and unite_ > 0)then
					select into tarif_ y.* from yvs_base_plan_tarifaire y where y.categorie = data_.categorie and y.actif is true and ((y.article is not null and (y.article = article_ and y.conditionnement = (select c.id from yvs_base_conditionnement c where c.article = y.article and c.unite = unite_ limit 1))) or (y.article is null and y.famille = famille_)) order by y.article limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
						else
							garde_ = qte_ * tarif_.remise;
						end if;
-- 						RAISE NOTICE 'garde_ : %',garde_;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL) then
-- 							RAISE NOTICE 'tarif_.id : %',tarif_.id;
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
							else
								remise_ = qte_ * tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ <= 0)then
							remise_ = garde_;
						end if;
						exit;
					end if;
				else
					select into tarif_ y.* from yvs_base_plan_tarifaire y where y.categorie = data_.categorie and y.actif is true and ((y.article is not null and y.article = article_) or (y.article is null and y.famille = famille_)) order by y.article limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
						else
							garde_ = qte_ * tarif_.remise;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL) then
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
							else
								remise_ = qte_ * tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ <= 0)then
							remise_ = garde_;
						end if;
						exit;
					end if;
				end if;
				if(planifier_)then
					exit;
				end if;
			end if;
		end loop;
	end if;
	if(remise_ is null or remise_ <= 0)then
		if(unite_ is not null and unite_ > 0)then
			select into tarif_ y.* from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.unite = unite_ and a.actif is true limit 1;
			if(tarif_.id IS NOT NULL) then
					if(tarif_.nature_remise = 'TAUX')then
						remise_ = valeur_ * (tarif_.remise /100);
					else
						remise_ = qte_ * tarif_.remise;
					end if;
			end if;
		else
			select into tarif_ * from yvs_base_article_point where point = point_ and article = article_ and actif is true limit 1;
			if(tarif_.id IS NOT NULL) then
				if(tarif_.nature_remise = 'TAUX')then
					remise_ = valeur_ * (tarif_.remise /100);
				else
					remise_ = qte_ * tarif_.remise;
				end if;
			end if;
		end if;
		if(remise_ is null or remise_ <= 0)then
			if(unite_ is not null and unite_ > 0)then
				select into remise_ remise from yvs_base_conditionnement where article = article_ and unite = unite_ limit 1;
				if(remise_ is not null) then
					remise_ = valeur_ * (remise_/100);
				end if;
			else
				select into remise_ remise from yvs_base_articles where id = article_; 
				if(remise_ is not null) then
					remise_ = valeur_ * (remise_/100);
				end if;
			end if;
		end if;	
	end if;	
	if(remise_ is null or remise_ <=0)then
		remise_ = 0;
	end if;	
	return remise_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer) IS 'retourne la remise sur vente d'' article';
