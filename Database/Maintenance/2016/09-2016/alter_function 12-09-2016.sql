
CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
	DECLARE entree_ double precision; 
		sortie_ double precision; 
BEGIN
	if(depot_ is not null and depot_ >0)then
		if(tranche_ is not null and tranche_ >0)then
			entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
			sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
		else					
			entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='E'));
			sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='S'));
		end if;
	else	
		if(agence_ is not null and agence_ >0)then
			if(tranche_ is not null and tranche_ >0)then
				entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
				sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
			else					
				entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='E'));
				sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND m.date_doc<=date_ AND m.mouvement='S'));
			end if;
		else
			if(societe_ is not null and societe_ >0)then
				if(tranche_ is not null and tranche_ >0)then
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
				else					
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='E'));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND m.date_doc<=date_ AND m.mouvement='S'));
				end if;
			else
				if(tranche_ is not null and tranche_ >0)then
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));
				else					
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='E'));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.date_doc<=date_ AND m.mouvement='S'));
				end if;
			end if;
		end if;
	end if;

	IF entree_ IS null THEN
	 entree_:=0;	
	END IF;
	IF sortie_ IS null THEN
	  sortie_:=0;	
	END IF;
	return (entree_ - sortie_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date)
  OWNER TO postgres;
  
  
CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, depot_ bigint, date_ date, tranche_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 

BEGIN
	return get_stock_reel(art_ ,tranche_ ,depot_ ,0::bigint ,0::bigint ,date_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, date, bigint)
  OWNER TO postgres;
  
  
CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, depot_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE 
 
BEGIN
	return get_stock_reel(art_ ,0::bigint ,depot_ ,0::bigint ,0::bigint ,date_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, date)
  OWNER TO postgres;
  
  
CREATE OR REPLACE FUNCTION get_stock(art_ bigint, depot_ bigint, date_ date, tranche_ bigint)
  RETURNS double precision AS
$BODY$
	DECLARE entree_ double precision; 
		sortie_ double precision; 
BEGIN
	return get_stock(art_ ,tranche_ ,depot_ ,0::bigint ,0::bigint ,date_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock(bigint, bigint, date, integer)
  OWNER TO postgres;
  
  
CREATE OR REPLACE FUNCTION get_stock_consigne(art_ bigint, depot_ bigint, date_ date, tranche_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 

BEGIN
	return get_stock_consigne(art_ ,tranche_ ,depot_ ,0::bigint ,0::bigint ,date_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, date, bigint)
  OWNER TO postgres;
  
  
CREATE OR REPLACE FUNCTION get_stock_consigne(art_ bigint, depot_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE 
 
BEGIN
	return get_stock_consigne(art_ ,0::bigint ,depot_ ,0::bigint ,0::bigint ,date_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, date)
  OWNER TO postgres;  
    
  
CREATE OR REPLACE FUNCTION periode_fr_en(mois character varying)
  RETURNS character varying AS
$BODY$
	 
BEGIN
	if(mois is not null)then
		if(mois = 'J' or mois = 'j' or mois = 'J(s)' or mois = 'j(s)' or mois = 'Js' or mois = 'js' or mois = 'Jr' or mois = 'jr' or mois = 'Jr(s)' or mois = 'jr(s)'  or mois = 'Jrs' or mois = 'jrs' or mois = 'Jour' or mois = 'jour' or mois = 'Jour(s)' or mois = 'jour(s)' or mois = 'Jours' or mois = 'jours')then
			return 'day';
		elsif(mois = 'W' or mois = 'w' or mois = 'W(s)' or mois = 'w(s)' or mois = 'Ws' or mois = 'ws' or mois = 'Sem' or mois = 'sem' or mois = 'Sem(s)' or mois = 'sem(s)' or mois = 'Sems' or mois = 'sems' or mois = 'Semaine' or mois = 'semaine' or mois = 'Semaine(s)' or mois = 'semaine(s)' or mois = 'Semaines' or mois = 'semaines') then
			return 'week';
		elsif(mois = 'M' or mois = 'm' or mois = 'M(s)' or mois = 'm(s)' or mois = 'Ms' or mois = 'ms' or mois = 'Mois' or mois = 'mois' or mois = 'Men(s)' or mois = 'men(s)' or mois = 'Mens' or mois = 'mens' or mois = 'Mensuel' or mois = 'mensuel' or mois = 'Mensuel(s)' or mois = 'mensuel(s)' or mois = 'Mensuels' or mois = 'mensuels') then
			return 'month';
		elsif(mois = 'A' or mois = 'a' or mois = 'A(s)' or mois = 'a(s)' or mois = 'As' or mois = 'as' or mois = 'An' or mois = 'an' or mois = 'Ans' or mois = 'ans' or mois = 'An(s)' or mois = 'an(s)' or mois = 'Annee' or mois = 'annee' or mois = 'Annee(s)' or mois = 'annee(s)' or mois = 'Annees' or mois = 'annees' or mois = 'Année' or mois = 'année' or mois = 'Année(s)' or mois = 'année(s)' or mois = 'Années' or mois = 'années') then
			return 'year';
		end if;
		return mois;
	end if;
	return 'day';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION periode_fr_en(character varying)
  OWNER TO postgres;