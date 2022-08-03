-- Function: get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint, bigint)

-- DROP FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint, bigint);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint, lot_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE entree_ double precision; 
	sortie_ double precision; 
	stock_ double precision; 
	query_ character varying default 'SELECT COALESCE(SUM(m.quantite), 0) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE m.quantite IS NOT NULL';
BEGIN
	if(societe_ is not null and societe_ >0)then
		query_ = query_ || ' AND a.societe = '||societe_;
	end if;
	if(agence_ is not null and agence_ >0)then
		query_ = query_ || ' AND d.agence = '||agence_;
	end if;
	if(depot_ is not null and depot_ >0)then
		query_ = query_ || ' AND m.depot = '||depot_;
	end if;
	if(art_ is not null and art_ >0)then
		query_ = query_ || ' AND m.article = '||art_;
	end if;
	if(unite_ is not null and unite_ >0)then
		query_ = query_ || ' AND m.conditionnement = '||unite_;
	end if;
	if(lot_ is not null and lot_ > 0)then
		query_ = query_ || ' AND m.lot IN (SELECT com_get_sous_lot('||lot_||', true))';
	end if;
	if(date_ is not null)then
			query_ = query_ || ' AND m.date_doc <= '''||date_||'''';
	end if;
	IF(query_ IS NOT NULL)THEN
		EXECUTE query_ || ' AND m.mouvement=''E''' INTO entree_;
		entree_ = COALESCE(entree_, 0);
		EXECUTE query_ || ' AND m.mouvement=''S''' INTO sortie_;
		sortie_ = COALESCE(sortie_, 0);
		stock_ = (entree_ - sortie_);
		if(COALESCE(tranche_,0)>0)THEN
			query_=replace(query_, 'AND m.date_doc <=', 'AND m.date_doc =');
			query_ = query_ || ' AND m.tranche IN (select y.id from yvs_grh_tranche_horaire y inner join yvs_grh_tranche_horaire t on y.type_journee = t.type_journee WHERE y.societe='||societe_||' AND t.id = '||tranche_||' and t.heure_debut <= y.heure_debut and t.id != y.id order by t.heure_debut)';
			EXECUTE query_ || ' AND m.mouvement=''E''' INTO entree_;
			entree_ = COALESCE(entree_, 0);
			EXECUTE query_ || ' AND m.mouvement=''S''' INTO sortie_;
			sortie_ = COALESCE(sortie_, 0);
			stock_ = stock_ - (entree_ - sortie_);
		end if;
	END IF;
	return stock_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;
