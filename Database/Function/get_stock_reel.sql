-- Function: get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint, bigint)
-- DROP FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, bigint, bigint);
CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint, lot_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
	query_ character varying default 'SELECT COALESCE(SUM(m.quantite), 0) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE m.article = '||art_;
	entree_ double precision default 0; 
	sortie_ double precision default 0; 
	stock_ double precision default 0; 
BEGIN
	if(COALESCE(depot_,0)>0)then
		query_ = query_ || ' AND m.depot = '||depot_;
	end if;
	if(COALESCE(agence_,0)>0)then
		query_ = query_ || ' AND d.agence = '||agence_;
	end if;
	if(COALESCE(societe_,0)>0)then
		query_ = query_ || ' AND a.societe = '||societe_;		
	end if;
	if(COALESCE(unite_,0)>0)then
		query_ = query_ || ' AND m.conditionnement = '||unite_;		
	end if;
	if(COALESCE(lot_,0)>0)then
		query_ = REPLACE(query_, 'FROM yvs_base_mouvement_stock m INNER JOIN', 'FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_mouvement_stock_lot l ON m.id = l.mouvement INNER JOIN') || ' AND l.lot = '||lot_;		
	end if;
	RAISE NOTICE 'query_ : %',query_;
	IF(query_ IS NOT NULL)THEN
		IF(date_ IS null)THEN	
			date_ = current_date;
		END IF;
		EXECUTE query_ || ' AND m.date_doc <= '||QUOTE_LITERAL(date_)||' AND m.mouvement = ''E''' INTO entree_;
		EXECUTE query_ || ' AND m.date_doc <= '||QUOTE_LITERAL(date_)||' AND m.mouvement = ''S''' INTO sortie_;
		stock_ = (entree_ - sortie_);
		if(COALESCE(tranche_,0)>0)THEN	
			query_ = query_ || ' AND m.tranche IN (select y.id from yvs_grh_tranche_horaire y inner join yvs_grh_tranche_horaire t on y.type_journee = t.type_journee WHERE y.societe = t.societe AND t.id = '||tranche_||' and t.heure_debut <= y.heure_debut and t.id != y.id order by t.heure_debut)';
			EXECUTE query_ || ' AND m.date_doc = '||QUOTE_LITERAL(date_)||' AND m.mouvement = ''E''' INTO entree_;
			EXECUTE query_ || ' AND m.date_doc = '||QUOTE_LITERAL(date_)||' AND m.mouvement = ''S''' INTO sortie_;
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
