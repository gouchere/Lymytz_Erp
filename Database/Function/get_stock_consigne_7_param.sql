-- Function: get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date, bigint)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	  stock_ double precision default 0;
	 
BEGIN
	if(depot_ is not null and depot_ >0)then
		if(unite_ is not null and unite_ >0)then
			select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and c.depot = depot_ and c.conditionnement = unite_;
		else
			select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and c.depot = depot_;
		end if;
	else
		if(agence_ is not null and agence_ >0)then
			if(unite_ is not null and unite_ >0)then
				select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c inner join yvs_base_depots d on c.depot = d.id
					where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and d.agence = agence_ and c.conditionnement = unite_;
			else
				select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c inner join yvs_base_depots d on c.depot = d.id
					where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and d.agence = agence_;
			end if;
		else
			if(societe_ is not null and societe_ >0)then
				if(unite_ is not null and unite_ >0)then
					select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c inner join yvs_base_depots d on c.depot = d.id
						inner join yvs_agences a on d.agence = a.id
						where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and a.societe = societe and c.conditionnement = unite_;
				else
					select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c inner join yvs_base_depots d on c.depot = d.id
						inner join yvs_agences a on d.agence = a.id
						where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and a.societe = societe;
				end if;
			else
				stock_ = 0;
			end if;
		end if;
	end if;	
	if(stock_ is null)then
		stock_ = 0;
	end if;
	RETURN stock_;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;
