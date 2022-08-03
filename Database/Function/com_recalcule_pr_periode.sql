-- Function: com_recalcule_pr_periode(bigint, bigint, bigint, character varying, date, date)

-- DROP FUNCTION com_recalcule_pr_periode(bigint, bigint, bigint, character varying, date, date);

CREATE OR REPLACE FUNCTION com_recalcule_pr_periode(
    societe_ bigint,
    agence_ bigint,
    depot_ bigint,
    article_ character varying,
    debut_ date,
    fin_ date)
  RETURNS double precision AS
$BODY$
declare
	line_ record;

	query_ character varying default 'SELECT m.* FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE m.date_doc BETWEEN '||QUOTE_LITERAL(debut_)||' AND '|| QUOTE_LITERAL(fin_);
	query_max_ character varying default 'SELECT m.* FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE m.date_doc BETWEEN '||QUOTE_LITERAL(debut_)||' AND '|| QUOTE_LITERAL(fin_);
	ids_ character varying default '''0''';
	id_ character varying default '0';
	type_ character varying default '';

	total_ double precision default 0;
	pr_ double precision default 0;
	last_pr_ double precision default 0;
	new_cout double precision default 0;
	stock_ double precision default 0;
	prix_arr_ numeric;

	last_article_ BIGINT DEFAULT 0;
	id_fact_ BIGINT DEFAULT 0;
	date_ date;
BEGIN
	IF(COALESCE(depot_, 0) <= 0) THEN
		--Le dépôt est un paramètre obligatoire
		return 0;
	END IF;
	IF(COALESCE(societe_, 0) > 0) THEN
		query_= query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(COALESCE(agence_, 0) > 0) THEN
		query_= query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(depot_, 0) > 0) THEN
		query_= query_ || ' AND d.id = '||depot_;
	END IF;
	IF(COALESCE(article_, '') NOT IN ('', ' ', '0'))THEN
		FOR id_ IN SELECT art FROM regexp_split_to_table(article_,',') art WHERE CHAR_LENGTH(art) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(id_);
		END LOOP;
		query_= query_ || ' AND m.article::text IN ('||ids_||')';
	END IF;
	FOR line_ IN EXECUTE query_ || ' ORDER BY m.conditionnement, m.date_doc ASC, m.mouvement'
	LOOP
		IF(last_article_ != line_.conditionnement)THEN
			--changement d'article
			stock_ = get_stock_reel(line_.article, 0, line_.depot, 0, 0, line_.date_doc - 1, line_.conditionnement, 0);
			last_pr_ = get_pr(agence_,line_.article, line_.depot, 0, line_.date_doc - 1, line_.conditionnement, line_.id);
			last_article_ = line_.conditionnement;
			pr_ = last_pr_;
			--Met à jour la dernière date de calcul PR
			SELECT INTO date_ MAX(m.date_doc) FROM yvs_base_mouvement_stock m WHERE m.conditionnement=line_.conditionnement AND m.depot=depot_ AND m.date_doc BETWEEN debut_ AND fin_;
			RAISE NOTICE 'Date = %',date_;
		ELSE
			last_pr_ = pr_;
		END IF;
		date_=line_.date_doc;
		IF(line_.mouvement = 'E' AND line_.calcul_pr IS true)THEN
			-- Retourne le nouveau cout moyen calculé
			IF(COALESCE(line_.quantite, 0) + stock_ != 0)THEN
				new_cout = COALESCE((((stock_ * COALESCE(last_pr_, 0)) + ((COALESCE(line_.quantite, 0) * COALESCE(line_.cout_entree, 0)))) / (COALESCE(line_.quantite, 0) + stock_)), 0);
				-- Arrondi les chiffres
				pr_ = round(new_cout::numeric, 3);
			ELSE
				pr_ = COALESCE(last_pr_, 0);
			END IF;
		END IF;
		IF(line_.table_externe = 'yvs_com_contenu_doc_stock' OR line_.table_externe = 'yvs_com_contenu_doc_stock_reception')THEN
			IF(line_.table_externe = 'yvs_com_contenu_doc_stock')THEN
				SELECT INTO type_ d.type_doc FROM yvs_com_doc_stocks d INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = d.id WHERE c.id = line_.id_externe;
			ELSE
				SELECT INTO type_ d.type_doc FROM yvs_com_doc_stocks d INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = d.id INNER JOIN yvs_com_contenu_doc_stock_reception r ON r.contenu = c.id WHERE r.id = line_.id_externe;
			END IF;
			IF(type_ = 'FT' OR type_ = 'TR')THEN
				IF(line_.mouvement = 'E')THEN
					UPDATE yvs_com_contenu_doc_stock SET prix_entree = pr_, execute_trigger='OK' WHERE id = line_.id_externe;
				ELSE
					UPDATE yvs_com_contenu_doc_stock SET prix = pr_, execute_trigger='OK' WHERE id = line_.id_externe;
				END IF;
				UPDATE yvs_base_mouvement_stock SET cout_entree = pr_, execute_trigger='OK' WHERE id = line_.id;
				line_.cout_entree = pr_;
			ELSIF(line_.mouvement = 'S')THEN
				UPDATE yvs_com_contenu_doc_stock SET prix_entree = pr_, prix = pr_, execute_trigger='OK' WHERE id = line_.id_externe;
				UPDATE yvs_base_mouvement_stock SET cout_entree = pr_, execute_trigger='OK' WHERE id = line_.id;
				line_.cout_entree = pr_;
			END IF;
		ELSIF(line_.table_externe = 'yvs_prod_of_suivi_flux')THEN
			IF(line_.mouvement = 'S') THEN
				UPDATE yvs_prod_of_suivi_flux SET cout = pr_, execute_trigger='OK' WHERE id = line_.id_externe;
				UPDATE yvs_base_mouvement_stock SET cout_entree = pr_, execute_trigger='OK' WHERE id = line_.id;
				line_.cout_entree = pr_;
			END IF;
		ELSIF(line_.table_externe = 'yvs_com_contenu_doc_vente')THEN
			SELECT INTO id_fact_ d.document_lie FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE c.id=line_.id_externe;
				UPDATE yvs_com_contenu_doc_vente SET pr = pr_, execute_trigger='OK' WHERE id = line_.id_externe OR id IN 
				(select c.id from yvs_com_contenu_doc_vente c
				WHERE c.doc_vente=id_fact_ AND c.conditionnement=line_.conditionnement);
		END IF;
		
		IF(line_.mouvement = 'E')THEN
			stock_ = stock_ + COALESCE(line_.quantite, 0);
		ELSE
			stock_ = stock_ - COALESCE(line_.quantite, 0);
		END IF;
		UPDATE yvs_base_mouvement_stock SET cout_stock = pr_, execute_trigger='OK' WHERE id = line_.id;		
		--PERFORM com_recalcule_pr_periode(debut_, fin_, line_.conditionnement);
	END LOOP;
	RETURN pr_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_recalcule_pr_periode(bigint, bigint, bigint, character varying, date, date)
  OWNER TO postgres;
