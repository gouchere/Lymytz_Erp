-- Function: get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, date)

-- DROP FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE 
	init_ double precision; 
	entree_ double precision; 
	sortie_ double precision; 
BEGIN

	init_ = (select get_stock_reel(art_, tranche_, depot_ , agence_ , societe_, (date_debut_- interval '1 day')::date));
	if(depot_ is not null and depot_ >0)then
		if(tranche_ is not null and tranche_ >0)then
			entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='E' AND m.tranche=tranche_));
			sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='S' AND m.tranche=tranche_));
		else					
			entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='E'));
			sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='S'));
		end if;
	else	
		if(agence_ is not null and agence_ >0)then
			if(tranche_ is not null and tranche_ >0)then
				entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='E' AND m.tranche=tranche_));
				sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='S' AND m.tranche=tranche_));
			else					
				entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='E'));
				sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE (m.article=art_ AND d.agence=agence_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='S'));
			end if;
		else
			if(societe_ is not null and societe_ >0)then
				if(tranche_ is not null and tranche_ >0)then
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='E' AND m.tranche=tranche_));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='S' AND m.tranche=tranche_));
				else					
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='E'));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE (m.article=art_ AND a.societe=societe_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='S'));
				end if;
			else
				if(tranche_ is not null and tranche_ >0)then
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='E' AND m.tranche=tranche_));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='S' AND m.tranche=tranche_));
				else					
					entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='E'));
					sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND (m.date_doc BETWEEN date_debut_ AND date_fin_) AND m.mouvement='S'));
				end if;
			end if;
		end if;
	end if;

	IF entree_ IS null THEN
	 entree_:=0;	
	END IF;
	RAISE NOTICE 'entree_ %',entree_;
	IF sortie_ IS null THEN
	  sortie_:=0;	
	END IF;
	RAISE NOTICE 'sortie_ %',sortie_;
	IF init_ IS null THEN
	  init_:=0;	
	END IF;
	RAISE NOTICE 'init_ %',init_;
	return (init_ + entree_ - sortie_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, date)
  OWNER TO postgres;
