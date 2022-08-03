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
