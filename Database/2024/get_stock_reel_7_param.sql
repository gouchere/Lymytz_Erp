-- DROP FUNCTION public.get_stock_reel(int8, int8, int8, int8, int8, date, int8, int8);

CREATE OR REPLACE FUNCTION public.get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint, lot_ bigint)
 RETURNS double precision
 LANGUAGE plpgsql
AS $function$
DECLARE 
    query_ character varying default 'SELECT COALESCE(SUM(m.quantite), 0) FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE m.article = '||art_;
    entree_ double precision default 0; 
    sortie_ double precision default 0; 
    stock_ double precision default 0; 
    date_last_report_ date default '01-01-2015';
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
        IF(date_ IS NULL)THEN	
            date_ = current_date;
        END IF;
        EXECUTE REPLACE(query_, 'COALESCE(SUM(m.quantite), 0)', 'm.date_doc') || ' AND m.date_doc <= '||QUOTE_LITERAL(date_)||' AND m.num_doc = ''REPORT'' ORDER BY m.date_doc DESC LIMIT 1' INTO date_last_report_;
		RAISE NOTICE 'date_last_report_ : %',date_last_report_;
        IF(date_last_report_ IS NULL)THEN	
            date_last_report_ = '01-01-2015';
        END IF;
        EXECUTE query_ || ' AND m.mouvement = ''E'' AND m.date_doc BETWEEN '||QUOTE_LITERAL(date_last_report_)||' AND '||QUOTE_LITERAL(date_) INTO entree_;
        EXECUTE query_ || ' AND m.mouvement = ''S'' AND m.date_doc BETWEEN '||QUOTE_LITERAL(date_last_report_)||' AND '||QUOTE_LITERAL(date_) INTO sortie_;
        stock_ = (entree_ - sortie_);
        if(COALESCE(tranche_,0)>0)THEN	
            query_ = query_ || ' AND m.tranche IN (select y.id from yvs_grh_tranche_horaire y inner join yvs_grh_tranche_horaire t on y.type_journee = t.type_journee WHERE y.societe = t.societe AND t.id = '||tranche_||' and t.heure_debut <= y.heure_debut and t.id != y.id order by t.heure_debut)';
            EXECUTE REPLACE(query_, 'COALESCE(SUM(m.quantite), 0)', 'm.date_doc') || ' AND m.date_doc <= '||QUOTE_LITERAL(date_)||' AND m.num_doc = ''REPORT'' ORDER BY m.date_doc DESC LIMIT 1' INTO date_last_report_;
            RAISE NOTICE 'date_last_report_ : %',date_last_report_;
            IF(date_last_report_ IS NULL)THEN	
                date_last_report_ = '01-01-2015';
            END IF;
            EXECUTE query_ || ' AND m.mouvement = ''E'' AND m.date_doc BETWEEN '||QUOTE_LITERAL(date_last_report_)||' AND '||QUOTE_LITERAL(date_) INTO entree_;
            EXECUTE query_ || ' AND m.mouvement = ''S'' AND m.date_doc BETWEEN '||QUOTE_LITERAL(date_last_report_)||' AND '||QUOTE_LITERAL(date_) INTO sortie_;
            stock_ = stock_ - (entree_ - sortie_);
        end if;
    END IF;
    return stock_;
END;
$function$
;
