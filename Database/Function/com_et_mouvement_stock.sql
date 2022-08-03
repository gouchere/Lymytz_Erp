-- Function: com_et_mouvement_stock(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying)

-- DROP FUNCTION com_et_mouvement_stock(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION com_et_mouvement_stock(societe_ bigint, agence_ bigint, depot_ bigint, tranche_ bigint, article_ bigint, unite_ bigint, date_debut_ date, date_fin_ date, mouvement_ character varying)
  RETURNS SETOF yvs_base_mouvement_stock AS
$BODY$
declare 
   stock_ DOUBLE PRECISION DEFAULT 0;
   pr_ DOUBLE PRECISION DEFAULT 0;
   date_initial_ DATE DEFAULT COALESCE(date_debut_, CURRENT_DATE) - 1;
   
   query_ CHARACTER VARYING DEFAULT 'CREATE TEMP TABLE table_mouvement_stock AS SELECT y.* FROM yvs_base_mouvement_stock y INNER JOIN yvs_base_depots d ON y.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE a.societe = '||QUOTE_LITERAL(societe_);
   
begin 	
	-- IF(COALESCE(agence_, 0) > 0)THEN
		-- query_ = query_ || ' AND a.id = '||agence_;
	-- END IF;
	IF(COALESCE(depot_, 0) > 0)THEN
		query_ = query_ || ' AND y.depot = '||depot_;
	END IF;
	IF(COALESCE(tranche_, 0) > 0)THEN
		query_ = query_ || ' AND y.tranche = '||tranche_;
	END IF;
	IF(COALESCE(article_, 0) > 0)THEN
		query_ = query_ || ' AND y.article = '||article_;
		IF(date_debut_ IS NOT NULL)THEN		
			stock_ = (SELECT get_stock_reel(article_, tranche_, depot_, 0, societe_, date_initial_, unite_, 0));
			pr_ = (SELECT get_pr(article_, depot_, 0, date_initial_,unite_));
		END IF;
	END IF;
	IF(COALESCE(unite_, 0) > 0)THEN
		query_ = query_ || ' AND y.conditionnement = '||unite_;
	END IF;
	IF(date_debut_ IS NOT NULL AND date_fin_ IS NOT NULL)THEN
		query_ = query_ || ' AND y.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	ELSIF(date_debut_ IS NOT NULL)THEN
		query_ = query_ || ' AND y.date_doc >= '||QUOTE_LITERAL(date_debut_);
	ELSIF(date_fin_ IS NOT NULL)THEN
		query_ = query_ || ' AND y.date_doc <= '||QUOTE_LITERAL(date_fin_);
	END IF;
	IF(COALESCE(mouvement_, '') NOT IN ('', ' '))THEN
		query_ = query_ || ' AND y.mouvement = '||QUOTE_LITERAL(mouvement_);
	END IF;
	DROP TABLE IF EXISTS table_mouvement_stock;
	EXECUTE query_;
	IF(COALESCE(article_, 0) > 0 and date_debut_ IS NOT NULL)THEN
		INSERT INTO table_mouvement_stock(id, quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, date_mouvement, cout_stock, author, tranche, qualite, conditionnement, date_update, date_save, lot)
					     VALUES (-1, stock_, date_initial_, 'E', article_, false, true, -1, 'nothing', 'Stock Initial', depot_, null, pr_, date_initial_, pr_, 16, tranche_, null, unite_, current_date, current_date, null);
	END IF;
	RETURN QUERY SELECT * FROM table_mouvement_stock ORDER BY depot, article, date_doc, mouvement;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_mouvement_stock(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
