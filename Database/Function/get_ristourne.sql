-- Function: get_ristourne(bigint, double precision, double precision, bigint, date)

-- DROP FUNCTION get_ristourne(bigint, double precision, double precision, bigint, date);

CREATE OR REPLACE FUNCTION get_ristourne(cond_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date)
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
			select into plan_ id from yvs_com_plan_ristourne WHERE actif = true and id = data_.plan_ristourne;
			if(plan_ is not null)then
			RAISE NOTICE 'Here' ;
				for data_ in select * from yvs_com_ristourne WHERE actif = true and plan = plan_ AND conditionnement=cond_ AND nature='R'
				loop
				RAISE NOTICE 'begin ....%',data_.permanent ;
					if(data_.permanent is false)then
						if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
							select into tarif_ * from yvs_com_grille_ristourne where ristourne = data_.id and ((base = 'QTE' and qte_ between montant_minimal and montant_maximal)or(base = 'CATTC' and valeur_ between montant_minimal and montant_maximal));
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
						RAISE NOTICE 'PERMANENT ....%',data_.id;
						RAISE NOTICE 'qte_ ....%',qte_;
						RAISE NOTICE 'valeur_ ....%',valeur_;
						select into tarif_ * from yvs_com_grille_ristourne where ristourne = data_.id and ((base = 'QTE' and qte_ between montant_minimal and montant_maximal)or(base = 'CATTC' and valeur_ between montant_minimal and montant_maximal));
						if(tarif_.id is not null)then
						RAISE NOTICE 'CALCUL  ....';
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
