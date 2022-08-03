-- Function: com_recalcule_pr_periode(bigint, bigint, bigint, character varying, date, date)
-- DROP FUNCTION com_recalcule_pr_periode(bigint, bigint, bigint, character varying, date, date);
CREATE OR REPLACE FUNCTION com_recalcule_pr_periode(societe_ bigint, agence_ bigint, depot_ bigint, article_ character varying, debut_ date, fin_ date)
  RETURNS double precision AS
$BODY$
declare
	line_ record;

	query_ character varying default 'SELECT m.* FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE m.date_doc BETWEEN '||QUOTE_LITERAL(debut_)||' AND '|| QUOTE_LITERAL(fin_);
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
begin
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
	ALTER TABLE yvs_base_mouvement_stock DISABLE TRIGGER stock_maj_prix_mvt;
	FOR line_ IN EXECUTE query_ || ' ORDER BY m.conditionnement, m.date_doc ASC, m.mouvement'
	LOOP
		IF(last_article_ != line_.conditionnement)THEN
			stock_ = get_stock_reel(line_.article, 0, line_.depot, 0, 0, line_.date_doc - 1, line_.conditionnement, 0);
			last_pr_ = get_pr(line_.article, line_.depot, 0, line_.date_doc - 1, line_.conditionnement, line_.id);
			last_article_ = line_.conditionnement;
			pr_ = last_pr_;
		ELSE
			last_pr_ = pr_;
		END IF;

		IF(line_.table_externe = 'yvs_com_contenu_doc_stock' OR line_.table_externe = 'yvs_com_contenu_doc_stock_reception')THEN
			IF(line_.table_externe = 'yvs_com_contenu_doc_stock')THEN
				SELECT INTO type_ d.type_doc FROM yvs_com_doc_stocks d INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = d.id WHERE c.id = line_.id_externe;
			ELSE
				SELECT INTO type_ d.type_doc FROM yvs_com_doc_stocks d INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = d.id INNER JOIN yvs_com_contenu_doc_stock_reception r ON r.contenu = c.id WHERE r.id = line_.id_externe;
			END IF;
			ALTER TABLE yvs_com_contenu_doc_stock DISABLE TRIGGER update_;
			IF(type_ = 'FT' OR type_ = 'TR')THEN
				IF(line_.mouvement = 'E')THEN
					UPDATE yvs_com_contenu_doc_stock SET prix_entree = pr_ WHERE id = line_.id_externe;
				ELSE
					UPDATE yvs_com_contenu_doc_stock SET prix = pr_ WHERE id = line_.id_externe;
				END IF;
				UPDATE yvs_base_mouvement_stock SET cout_entree = pr_ WHERE id = line_.id;
				line_.cout_entree = pr_;
			ELSIF(line_.mouvement = 'S')THEN
				UPDATE yvs_com_contenu_doc_stock SET prix_entree = pr_, prix = pr_ WHERE id = line_.id_externe;
				UPDATE yvs_base_mouvement_stock SET cout_entree = pr_ WHERE id = line_.id;
				line_.cout_entree = pr_;
			END IF;
			ALTER TABLE yvs_com_contenu_doc_stock ENABLE TRIGGER update_;
		ELSIF(line_.table_externe = 'yvs_prod_of_suivi_flux')THEN
			UPDATE yvs_prod_of_suivi_flux SET cout = pr_ WHERE id = line_.id_externe;
			UPDATE yvs_base_mouvement_stock SET cout_entree = pr_ WHERE id = line_.id;
			line_.cout_entree = pr_;
		ELSIF(line_.table_externe = 'yvs_com_contenu_doc_vente')THEN
			ALTER TABLE yvs_com_contenu_doc_vente DISABLE TRIGGER update_;
			UPDATE yvs_com_contenu_doc_vente SET pr = pr_ WHERE id = line_.id_externe;
			ALTER TABLE yvs_com_contenu_doc_vente ENABLE TRIGGER update_;
		END IF;

		IF(line_.mouvement = 'E')THEN
			-- Retourne le nouveau cout moyen calculé
			IF(COALESCE(line_.quantite, 0) + stock_ != 0)THEN
				new_cout = COALESCE((((stock_ * COALESCE(last_pr_, 0)) + ((COALESCE(line_.quantite, 0) * COALESCE(line_.cout_entree, 0)))) / (COALESCE(line_.quantite, 0) + stock_)), 0);
				-- Arrondi les chiffres
				pr_ = round(new_cout::numeric, 3);
			ELSE
				pr_ = COALESCE(last_pr_, 0);
			END IF;
		END IF;
		IF(line_.mouvement = 'E')THEN
			stock_ = stock_ + COALESCE(line_.quantite, 0);
		ELSE
			stock_ = stock_ - COALESCE(line_.quantite, 0);
		END IF;
		UPDATE yvs_base_mouvement_stock SET cout_stock = pr_ WHERE id = line_.id;
	END LOOP;
	ALTER TABLE yvs_base_mouvement_stock ENABLE TRIGGER stock_maj_prix_mvt;
	RETURN pr_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_recalcule_pr_periode(bigint, bigint, bigint, character varying, date, date)
  OWNER TO postgres;


-- Function: com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
-- DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN agence_ bigint, IN depot_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision) AS
$BODY$
DECLARE 
BEGIN 	
	RETURN QUERY SELECT * FROM com_inventaire_preparatoire(agence_, depot_, 0, '', 0, date_, print_all_, option_print_, type_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;


-- Function: com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, character varying, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN agence_ bigint, IN depot_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision) AS
$BODY$
DECLARE 
	articles_ RECORD;
	unites_ RECORD;

	insert_ BOOLEAN DEFAULT false;

	unite_ BIGINT DEFAULT 0;
	
	prix_ DOUBLE PRECISION DEFAULT 0;
	stock_ DOUBLE PRECISION DEFAULT 0;
	reservation_ DOUBLE PRECISION DEFAULT 0;
	reste_a_livre_ DOUBLE PRECISION DEFAULT 0;

	query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.article, y.depot, a.ref_art, a.designation, f.reference_famille, f.designation as famille, a.pua, a.puv, d.agence, y.actif FROM yvs_base_article_depot y 
		INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON f.id = a.famille INNER JOIN yvs_base_depots d ON y.depot = d.id WHERE y.article IS NOT NULL';
	
BEGIN 	
	DROP TABLE IF EXISTS table_inventaire_preparatoire;
	CREATE TEMP TABLE IF NOT EXISTS table_inventaire_preparatoire(_depot bigint, _article bigint, _code character varying, _designation character varying, numero_ character varying, _famille character varying, _unite bigint, _reference character varying, prix_ double precision, _puv double precision, _pua double precision, _pr double precision, _stock double precision, _reservation double precision, _reste_a_livre double precision); 
	DELETE FROM table_inventaire_preparatoire;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND d.agence = '||agence_;
	END IF;
	IF(depot_ IS NOT NULL AND depot_ > 0)THEN
		query_ = query_ || ' AND d.id = '||depot_;
	END IF;
	IF(famille_ IS NOT NULL AND famille_ > 0)THEN
		query_ = query_ || ' AND a.famille = '||famille_;
	END IF;
	IF(groupe_ IS NOT NULL AND groupe_ > 0)THEN
		query_ = query_ || ' AND a.groupe = '||groupe_;
	END IF;
	IF(TRIM(COALESCE(categorie_, '')) NOT IN ('', ' '))THEN
		query_ = query_ || ' AND a.categorie = '||QUOTE_LITERAL(categorie_);
	END IF;
	option_print_ = COALESCE(option_print_, '');
-- 	RAISE NOTICE '%',query_;
	FOR articles_ IN EXECUTE query_
	LOOP
		FOR unites_ IN SELECT y.id, y.unite, u.reference, COALESCE(y.prix, articles_.puv) AS puv, COALESCE(y.prix_achat, articles_.pua) AS pua FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = articles_.article
		LOOP
			insert_ = false;
			stock_ = (SELECT get_stock(articles_.article, 0, articles_.depot, articles_.agence, 0, date_, unites_.id, 0));
			SELECT INTO reservation_ SUM(c.quantite) FROM yvs_com_reservation_stock c WHERE c.depot = articles_.depot AND c.article = articles_.article AND c.conditionnement = unites_.id AND c.statut = 'V' AND c.date_echeance <= date_;
			SELECT INTO reste_a_livre_ ((COALESCE((select sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on a.id=c.article inner join yvs_com_doc_ventes d on d.id=c.doc_vente  inner join yvs_com_entete_doc_vente en on en.id=d.entete_doc
					where d.type_doc = 'FV' and d.statut = 'V' and en.date_entete <= date_ AND c.article = articles_.article AND d.depot_livrer = articles_.depot AND c.conditionnement = unites_.id limit 1), 0))
				- (COALESCE((select sum(c1.quantite) from yvs_com_contenu_doc_vente c1 inner join yvs_base_articles a1 on a1.id=c1.article inner join yvs_com_doc_ventes d1 on d1.id=c1.doc_vente 
					where d1.type_doc = 'BLV' and d1.statut = 'V' and d1.date_livraison <= date_ AND c1.article = articles_.article AND d1.depot_livrer = articles_.depot AND c1.conditionnement = unites_.id limit 1), 0)));
			IF(reste_a_livre_ < 0)THEN
				reste_a_livre_ = 0;
			END IF;
			IF(print_all_)THEN
				IF(stock_ != 0)THEN
					insert_ = true;
				ELSE
					insert_ = articles_.actif;					
				END IF;
			ELSE
				IF(option_print_ = 'P')THEN
					IF(stock_ > 0)THEN
						insert_ = true;
					END IF;
				ELSIF(option_print_ = 'N')THEN
					IF(stock_ < 0)THEN
						insert_ = true;
					END IF;
				ELSE
					IF(stock_ != 0)THEN
						insert_ = true;
					END IF;
				END IF;
			END IF;
			IF(insert_)THEN
				IF(type_ = 'A')THEN
					prix_ = COALESCE((SELECT get_pua(articles_.article, 0, depot_, unites_.id)), 0);
				ELSIF(type_ = 'V')THEN
					prix_ = unites_.puv;
				ELSE
					prix_ = COALESCE((SELECT get_pr(articles_.article, depot_, 0, date_, unites_.id)), 0);
				END IF;
				INSERT INTO table_inventaire_preparatoire VALUES(articles_.depot, articles_.article, articles_.ref_art, articles_.designation, articles_.reference_famille, articles_.famille, unites_.unite, unites_.reference, prix_, unites_.puv, unites_.pua, 0, stock_, COALESCE(reservation_, 0), COALESCE(reste_a_livre_, 0));
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_inventaire_preparatoire ORDER BY _depot, _famille, _code;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;


-- Function: prod_et_synthese_consommation_mp(bigint, bigint, bigint, character varying, date, date, boolean, character varying)
-- DROP FUNCTION prod_et_synthese_consommation_mp(bigint, bigint, bigint, character varying, date, date, boolean, character varying);
CREATE OR REPLACE FUNCTION prod_et_synthese_consommation_mp(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN article_ character varying, IN date_debut_ date, IN date_fin_ date, IN by_journee boolean, IN type_ character varying)
  RETURNS TABLE(id bigint, article bigint, reference character varying, designation character varying, unite bigint, numero character varying, classe bigint, code character varying, intitule character varying, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	data_ RECORD;
	classe_ RECORD;
	unite_ RECORD;
	dates_ RECORD;
	
	query_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT a.id, a.ref_art, a.designation, c.id as unite, u.reference FROM yvs_base_articles a INNER JOIN yvs_base_conditionnement c ON a.id = c.article INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id 
						INNER JOIN yvs_base_famille_article o ON a.famille = o.id WHERE a.categorie = ''MP''';
	mouvement_ CHARACTER VARYING DEFAULT 'SELECT SUM(y.quantite) FROM yvs_base_mouvement_stock y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_depots d ON y.depot = d.id INNER JOIN yvs_agences f ON d.agence = f.id WHERE y.quantite IS NOT NULL';
	inner_es CHARACTER VARYING DEFAULT 'LEFT JOIN yvs_com_contenu_doc_stock_reception r ON y.id_externe = r.id INNER JOIN yvs_com_contenu_doc_stock s ON r.contenu = s.id INNER JOIN yvs_com_doc_stocks o ON s.doc_stock = o.id WHERE';
	inner_ss CHARACTER VARYING DEFAULT 'LEFT JOIN yvs_com_contenu_doc_stock s ON y.id_externe = s.id INNER JOIN yvs_com_doc_stocks o ON s.doc_stock = o.id WHERE';
	stock_ CHARACTER VARYING;
	ids_ CHARACTER VARYING DEFAULT '''0''';
	id_ CHARACTER VARYING DEFAULT '0';
	
	transfert_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	taux_ DOUBLE PRECISION DEFAULT 0;

	exist_ BIGINT DEFAULT 0;
	serial_ BIGINT DEFAULT 1;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_et_synthese_consommation_mp;
	CREATE TEMP TABLE IF NOT EXISTS table_et_synthese_consommation_mp(_id bigint, _article bigint, _reference character varying, _designation character varying, _unite bigint, _numero character varying, _classe bigint, _code character varying, _intitule character varying, _valeur double precision, _rang integer);
	DELETE FROM table_et_synthese_consommation_mp;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND o.societe = '||societe_;
		mouvement_ = mouvement_ || ' AND f.societe = '||societe_;
	END IF;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		mouvement_ = mouvement_ || ' AND f.id = '||agence_;
	END IF;
	IF(depot_ IS NOT NULL AND  depot_ > 0)THEN
		mouvement_ = mouvement_ || ' AND d.id = '|| depot_;
	END IF;
	IF(date_debut_ IS NOT NULL AND date_fin_ IS NOT NULL)THEN
		mouvement_ = mouvement_ || ' AND y.date_doc BETWEEN '''||date_debut_||''' AND '''||date_fin_||'''';
	END IF;
	IF(COALESCE(article_, '') NOT IN ('', ' ', '0'))THEN
		FOR id_ IN SELECT head FROM regexp_split_to_table(article_,',') head WHERE CHAR_LENGTH(head) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(id_);
		END LOOP;
		query_ = query_ || ' AND a.id IN ('||ids_||')';
	END IF;
	FOR data_ IN EXECUTE query_ || 'ORDER BY a.ref_art, u.reference'
	LOOP
		stock_ = mouvement_ || ' AND y.article = '||data_.id||' AND y.conditionnement = '||data_.unite;
		valeur_ = (SELECT get_stock(data_.id, 0, depot_, agence_, societe_, (date_debut_ - interval '1 day')::date, data_.unite));
		IF(COALESCE(valeur_, 0) != 0)THEN
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'S.I', 'STOCK INITIAL', valeur_, 1);
		END IF;
		serial_ = serial_ + 1;
		EXECUTE stock_ || ' AND y.mouvement = ''E''' INTO valeur_;
		IF(depot_ IS NULL OR depot_ < 1)THEN
			EXECUTE REPLACE(stock_, 'WHERE', inner_es) || ' AND o.type_doc IN (''FT'', ''TR'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'', ''yvs_com_contenu_doc_stock_reception'')' INTO transfert_;
		END IF;
		RAISE NOTICE 'transfert_ : %',transfert_;
		RAISE NOTICE 'valeur_ : %',valeur_;
		valeur_ = COALESCE(valeur_, 0) - COALESCE(transfert_, 0);
		IF(COALESCE(valeur_, 0) != 0)THEN
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'APPROV', 'APPROVISSIONNEMENT', valeur_, 2);
		END IF;
		serial_ = serial_ + 1;
		IF(COALESCE(by_journee, FALSE) IS FALSE)THEN			
			SELECT INTO valeur_ SUM(u.quantite) FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id  
				INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id INNER JOIN yvs_prod_suivi_operations p ON u.id_operation = p.id 
				INNER JOIN yvs_prod_session_of s ON p.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id 
				WHERE e.date_session BETWEEN date_debut_ AND date_fin_ AND a.classe1 IS NULL AND a.classe2 IS NULL AND y.article = data_.id AND y.unite = data_.unite;
			IF(valeur_ != 0)THEN
				INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'NO CLASSE', 'NO CLASSE', valeur_, 3);
			END IF;
			serial_ = serial_ + 1;
			
			FOR classe_ IN SELECT c.id, c.code_ref, c.designation FROM yvs_base_classes_stat c WHERE c.societe = societe_
			LOOP
				SELECT INTO valeur_ SUM(u.quantite) FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id 
					INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id INNER JOIN yvs_prod_suivi_operations p ON u.id_operation = p.id 
					INNER JOIN yvs_prod_session_of s ON p.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id
					WHERE e.date_session BETWEEN date_debut_ AND date_fin_ AND (a.classe1 = classe_.id OR a.classe2 = classe_.id) AND y.article = data_.id AND y.unite = data_.unite;
				IF(valeur_ != 0)THEN
					INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, classe_.id, classe_.code_ref, classe_.designation, valeur_, 4);
				END IF;
				serial_ = serial_ + 1;
			END LOOP;
		ELSE
			FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, 'J', true)
			LOOP
				SELECT INTO valeur_ SUM(u.quantite) FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id 
					INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id INNER JOIN yvs_prod_suivi_operations p ON u.id_operation = p.id 
					INNER JOIN yvs_prod_session_of s ON p.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id 
					WHERE e.date_session BETWEEN dates_.date_debut AND dates_.date_fin AND y.article = data_.id AND y.unite = data_.unite;
				IF(valeur_ != 0)THEN
					INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, dates_.intitule, dates_.intitule, valeur_, 4);
				END IF;
				serial_ = serial_ + 1;
			END LOOP;
		END IF;

		SELECT INTO valeur_ SUM(y._valeur) FROM table_et_synthese_consommation_mp y WHERE y._article = data_.id AND y._unite = data_.unite AND y._rang IN (3, 4);
		IF(valeur_ != 0)THEN
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'TOTAL', 'TOTAL CONSO', valeur_, 5);
		END IF;
		serial_ = serial_ + 1;
		
		EXECUTE stock_|| ' AND y.mouvement = ''S'' AND y.table_externe NOT IN (''yvs_prod_composant_of'', ''yvs_prod_flux_composant'', ''yvs_prod_of_suivi_flux'')' INTO valeur_;
		IF(depot_ IS NULL OR depot_ < 1)THEN
			EXECUTE REPLACE(stock_, 'WHERE', inner_ss) || ' AND o.type_doc IN (''FT'', ''TR'') AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO transfert_;
		END IF;
		RAISE NOTICE 'transfert_ : %',transfert_;
		RAISE NOTICE 'valeur_ : %',valeur_;
		valeur_ = COALESCE(valeur_, 0) - COALESCE(transfert_, 0);
		IF(COALESCE(valeur_, 0) != 0)THEN
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'SORTIE', 'AUTRES SORTIE', valeur_, 6);
		END IF;
		serial_ = serial_ + 1;
		
		valeur_ = (SELECT get_stock(data_.id, 0, depot_, agence_, societe_, date_fin_, data_.unite));
		IF(COALESCE(valeur_, 0) != 0)THEN
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'S.F', 'STOCK FINAL', valeur_, 7);
		END IF;
		serial_ = serial_ + 1;
	END LOOP;
	IF(COALESCE(type_, '') NOT IN ('', ' '))THEN
		IF(COALESCE(type_, '') = 'D')THEN
			FOR classe_ IN SELECT DISTINCT y._article AS article FROM table_et_synthese_consommation_mp y
			LOOP
				SELECT INTO unite_ c.id as unite, u.id, u.reference FROM yvs_base_conditionnement c INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE c.article = classe_.article AND c.by_prod IS TRUE AND c.defaut IS TRUE LIMIT 1;
				IF(COALESCE(unite_.id, 0) > 0)THEN
					FOR data_ IN SELECT y.*, c.unite FROM table_et_synthese_consommation_mp y INNER JOIN yvs_base_conditionnement c ON y._unite = c.id WHERE y._article = classe_.article AND y._unite != COALESCE(unite_.unite, 0)
					LOOP
						SELECT INTO taux_ t.taux_change FROM yvs_base_table_conversion t WHERE t.unite = COALESCE(data_.unite, 0) AND t.unite_equivalent = COALESCE(unite_.id, 0);
						IF(COALESCE(taux_, 0) = 0)THEN
							SELECT INTO taux_ t.taux_change FROM yvs_base_table_conversion t WHERE t.unite = COALESCE(unite_.id, 0) AND t.unite_equivalent = COALESCE(data_.unite, 0);
							IF(COALESCE(taux_, 0) = 0)THEN
								valeur_ = 0;
							ELSE
								valeur_ = data_._valeur / COALESCE(taux_, 0);
							END IF;
						ELSE
							valeur_ = data_._valeur * COALESCE(taux_, 0);							
						END IF;
						DELETE FROM table_et_synthese_consommation_mp WHERE _id = data_._id;
						SELECT INTO exist_ _id FROM table_et_synthese_consommation_mp y WHERE y._article = classe_.article AND y._code = data_._code AND y._unite = COALESCE(unite_.unite, 0);
						IF(COALESCE(exist_, 0) = 0)THEN
							INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_._article, data_._reference, data_._designation, unite_.unite, unite_.reference, data_._classe, data_._code, data_._intitule, valeur_, data_._rang);
						ELSE
							UPDATE table_et_synthese_consommation_mp y SET _valeur = y._valeur + valeur_ WHERE y._id = exist_;
						END IF;
						serial_ = serial_ + 1;
					END LOOP;
				END IF;
			END LOOP;
		END IF;
	END IF;
	RETURN QUERY SELECT * FROM table_et_synthese_consommation_mp ORDER BY _rang, _code, _designation, _reference, _numero;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION prod_et_synthese_consommation_mp(bigint, bigint, bigint, character varying, date, date, boolean, character varying)
  OWNER TO postgres;


-- Function: com_et_synthese_approvision_distribution(bigint, bigint, bigint, date, date, boolean)
-- DROP FUNCTION com_et_synthese_approvision_distribution(bigint, bigint, bigint, date, date, boolean);
CREATE OR REPLACE FUNCTION com_et_synthese_approvision_distribution(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN date_debut_ date, IN date_fin_ date, IN for_prod_ boolean)
  RETURNS TABLE(id bigint, groupe character varying, libelle character varying, type bigint, classe bigint, code character varying, intitule character varying, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	data_ RECORD;
	classe_ RECORD;

	query_ CHARACTER VARYING DEFAULT 'SELECT SUM(y.quantite * COALESCE(c.prix, 0)) FROM yvs_base_mouvement_stock y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_depots e ON y.depot = e.id
						INNER JOIN yvs_base_famille_article f ON a.famille = f.id INNER JOIN yvs_base_conditionnement c ON y.conditionnement = c.id WHERE y.quantite IS NOT NULL';
	inner_tr CHARACTER VARYING DEFAULT 'INNER JOIN yvs_com_contenu_doc_stock_reception r ON y.id_externe = r.id INNER JOIN yvs_com_contenu_doc_stock s ON r.contenu = s.id INNER JOIN yvs_com_doc_stocks d ON s.doc_stock = d.id WHERE';
	inner_st CHARACTER VARYING DEFAULT 'INNER JOIN yvs_com_contenu_doc_stock s ON y.id_externe = s.id INNER JOIN yvs_com_doc_stocks d ON s.doc_stock = d.id WHERE';
	inner_fv CHARACTER VARYING DEFAULT 'INNER JOIN yvs_com_contenu_doc_vente s ON y.id_externe = s.id INNER JOIN yvs_com_doc_ventes d ON s.doc_vente = d.id WHERE';
	inner_fa CHARACTER VARYING DEFAULT 'INNER JOIN yvs_com_contenu_doc_achat s ON y.id_externe = s.id INNER JOIN yvs_com_doc_achats d ON s.doc_achat = d.id WHERE';
	quantite_ CHARACTER VARYING DEFAULT '';

	transfert_ DOUBLE PRECISION DEFAULT 0;
	entree_ DOUBLE PRECISION DEFAULT 0;
	sortie_ DOUBLE PRECISION DEFAULT 0;
	stock_ DOUBLE PRECISION DEFAULT 0;
	
	valeur_si_ DOUBLE PRECISION DEFAULT 0;
	valeur_fa_ DOUBLE PRECISION DEFAULT 0;
	valeur_pd_ DOUBLE PRECISION DEFAULT 0;
	valeur_en_ DOUBLE PRECISION DEFAULT 0;
	valeur_ex_ DOUBLE PRECISION DEFAULT 0;
	valeur_in_ DOUBLE PRECISION DEFAULT 0;
	
	valeur_fv_ DOUBLE PRECISION DEFAULT 0;
	valeur_dn_ DOUBLE PRECISION DEFAULT 0;
	valeur_ra_ DOUBLE PRECISION DEFAULT 0;
	valeur_mq_ DOUBLE PRECISION DEFAULT 0;
	valeur_dp_ DOUBLE PRECISION DEFAULT 0;
	valeur_om_ DOUBLE PRECISION DEFAULT 0;
	valeur_ss_ DOUBLE PRECISION DEFAULT 0;
	valeur_sss_ DOUBLE PRECISION DEFAULT 0;
	valeur_sf_ DOUBLE PRECISION DEFAULT 0;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_et_synthese_approvision_distribution;
	CREATE TEMP TABLE IF NOT EXISTS table_et_synthese_approvision_distribution(_id bigint, _groupe character varying, _libelle character varying, _type bigint, _classe bigint, _code character varying, _intitule character varying, _valeur double precision, _rang integer);
	DELETE FROM table_et_synthese_approvision_distribution;
	IF(COALESCE(for_prod_, false))THEN
		query_ = query_ || ' AND a.categorie IN (''PSF'', ''PF'')';
	END IF;
	IF(societe_ IS NOT NULL AND societe_ != 0)THEN
		query_ = query_ || ' AND f.societe = '||societe_;
	END IF;
	IF(agence_ IS NOT NULL AND agence_ != 0)THEN
		query_ = query_ || ' AND e.agence = '||agence_;
	END IF;
	IF(depot_ IS NOT NULL AND depot_ != 0)THEN
		query_ = query_ || ' AND e.id = '||depot_;
	END IF;
	-- VALEUR SUR LES ARTICLES QUI N'ONT PAS DE CLASSE
	quantite_ = query_ || ' AND (a.classe1 IS NULL AND a.classe2 IS NULL)';
	-- STOCK INITIAL
	EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.date_doc < '||QUOTE_LITERAL(date_debut_) INTO entree_;
	EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.date_doc < '||QUOTE_LITERAL(date_debut_) INTO sortie_;
	valeur_si_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	IF(valeur_si_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'STOCK INITIAL', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_si_, 1);
	END IF;
	
	-- STOCK FINAL
	EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.date_doc <= '||QUOTE_LITERAL(date_fin_) INTO entree_;
	EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.date_doc <= '||QUOTE_LITERAL(date_fin_) INTO sortie_;
	valeur_sf_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	IF(valeur_sf_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'STOCK FINAL', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_sf_, 8);
	END IF;
	
	quantite_ = quantite_ || ' AND y.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||'';
	-- PRODUCTION
	EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_prod_of_suivi_flux'', ''yvs_prod_declaration_production'')' INTO entree_;
	sortie_ = 0;
	valeur_pd_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	IF(valeur_pd_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'PRODUCTION', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_pd_, 2);
	END IF;
	-- ACHAT
	EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_achat'')' INTO entree_;
	sortie_ = 0;
	valeur_fa_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	IF(valeur_fa_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'ACHAT', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_fa_, 3);
	END IF;
	-- ENTREE
	IF(COALESCE(depot_, 0) > 0 OR TRUE)THEN
		EXECUTE REPLACE(quantite_, 'WHERE', inner_tr) || ' AND d.type_doc IN (''FT'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock_reception'')' INTO transfert_;
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''ES'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
		entree_ = COALESCE(entree_, 0) + COALESCE(transfert_, 0);
	ELSE
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''ES'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
	END IF;
	sortie_ = 0;
	valeur_en_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''INV'' AND d.nature = ''EXCEDENT'' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
	valeur_ex_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	valeur_in_ = COALESCE(valeur_en_, 0) + COALESCE(valeur_ex_, 0);
	IF(valeur_in_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'AUTRE MVT(E)', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_in_, 4);
	END IF;
	
	-- VENTE
	EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_vente'')' INTO entree_;
	EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_vente'')' INTO sortie_;
	valeur_fv_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	IF(valeur_fv_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'VENTE', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_fv_, 1);
	END IF;
	
	-- DON ET RECEPTION
	entree_ = 0;
	EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''SS'' AND d.nature = ''DONS'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
	valeur_dn_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	IF(valeur_dn_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'DONS', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_dn_, 3);
	END IF;
	
	-- RATION
	entree_ = 0;
	EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_ration'')' INTO sortie_;
	valeur_ra_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	IF(valeur_ra_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'RATION', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_ra_, 4);
	END IF;
	
	-- MANQUANT
	entree_ = 0;
	EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''INV'' AND d.nature = ''MANQUANT'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
	valeur_mq_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	IF(valeur_mq_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'MANQUANT', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_mq_, 5);
	END IF;
	
	-- REJET ET CONSTAT
	entree_ = 0;
	EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''SS'' AND d.nature = ''DEPRECIATION'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
	valeur_dp_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	IF(valeur_dp_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'REJETS', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_dp_, 6);
	END IF;
	
	-- AUTRE MVT
	SELECT INTO entree_ 0;
	EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''SS'') AND d.nature NOT IN (''DONS'', ''DEPRECIATION'') AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
	valeur_sss_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	sortie_ = 0;
	IF(COALESCE(depot_, 0) > 0 OR TRUE)THEN
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''FT'') AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
	END IF;
	valeur_ss_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_prod_of_suivi_flux'', ''yvs_prod_declaration_production'')' INTO sortie_;
	valeur_om_ = COALESCE(valeur_ss_, 0) + COALESCE(valeur_sss_, 0) + COALESCE(sortie_, 0);
	IF(valeur_om_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'AUTRE MVT(S)', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_om_, 7);
	END IF;
	
	
	-- VALEUR SUR LES ARTICLES QUI ONT UNE CLASSE
	FOR classe_ IN SELECT c.id, c.code_ref, c.designation FROM yvs_base_classes_stat c WHERE c.societe = societe_
	LOOP
		quantite_ = query_ || ' AND (a.classe1 = '||classe_.id||' OR a.classe2  = '||classe_.id||')';
		-- STOCK INITIAL
		EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.date_doc < '||QUOTE_LITERAL(date_debut_) INTO entree_;
		EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.date_doc < '||QUOTE_LITERAL(date_debut_) INTO sortie_;
		valeur_si_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
		IF(valeur_si_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'STOCK INITIAL', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_si_, 1);
		END IF;
		
		-- STOCK FINAL
		EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.date_doc <= '||QUOTE_LITERAL(date_fin_) INTO entree_;
		EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.date_doc <= '||QUOTE_LITERAL(date_fin_) INTO sortie_;
		valeur_sf_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);	
		IF(valeur_sf_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'STOCK FINAL', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_sf_, 8);
		END IF;
		
		quantite_ = quantite_ || ' AND y.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||'';
		-- PRODUCTION
		EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_prod_of_suivi_flux'', ''yvs_prod_declaration_production'')' INTO entree_;
		sortie_ = 0;
		valeur_pd_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
		IF(valeur_pd_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'PRODUCTION', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_pd_, 2);
		END IF;
		-- ACHAT
		EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_achat'')' INTO entree_;
		sortie_ = 0;
		valeur_fa_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
		IF(valeur_fa_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'ACHAT', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_fa_, 3);
		END IF;
		-- ENTREE
		IF(COALESCE(depot_, 0) > 0 OR TRUE)THEN
			EXECUTE REPLACE(quantite_, 'WHERE', inner_tr) || ' AND d.type_doc IN (''FT'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock_reception'')' INTO transfert_;
			EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''ES'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
			entree_ = COALESCE(entree_, 0) + COALESCE(transfert_, 0);
		ELSE
			EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''ES'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
		END IF;
		sortie_ = 0;
		valeur_en_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''INV'' AND d.nature = ''EXCEDENT'' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
		valeur_ex_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
		valeur_in_ = COALESCE(valeur_en_, 0) + COALESCE(valeur_ex_, 0);
		IF(valeur_in_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'AUTRE MVT(E)', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_in_, 4);
		END IF;
		
		-- VENTE
		EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_vente'')' INTO entree_;
		EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_vente'')' INTO sortie_;
		valeur_fv_ =  COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		IF(valeur_fv_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'VENTE', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_fv_, 1);
		END IF;
		
		-- DON ET RECEPTION
		entree_ = 0;
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''SS'' AND d.nature = ''DONS'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
		valeur_dn_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		IF(valeur_dn_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'DONS', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_dn_, 3);
		END IF;
		
		-- RATION
		entree_ = 0;
		EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_ration'')' INTO sortie_;
		valeur_ra_ =  COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		IF(valeur_ra_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'RATION', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_ra_, 4);
		END IF;
		
		-- MANQUANT
		entree_ = 0;
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''INV'' AND d.nature = ''MANQUANT'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
		valeur_mq_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		IF(valeur_mq_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'MANQUANT', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_mq_, 5);
		END IF;
		
		-- REJET ET CONSTAT
		entree_ = 0;
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''SS'' AND d.nature = ''DEPRECIATION'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
		valeur_dp_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		IF(valeur_dp_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'REJETS', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_dp_, 6);
		END IF;
		
		-- AUTRE MVT
		SELECT INTO entree_ 0;
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''SS'') AND d.nature NOT IN (''DONS'', ''DEPRECIATION'') AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
		valeur_sss_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		sortie_ = 0;
		IF(COALESCE(depot_, 0) > 0 OR TRUE)THEN
			EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''FT'') AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
		END IF;
		valeur_ss_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_prod_of_suivi_flux'', ''yvs_prod_declaration_production'')' INTO sortie_;
		valeur_om_ = COALESCE(valeur_ss_, 0) + COALESCE(valeur_sss_, 0) + COALESCE(sortie_, 0);
		IF(valeur_om_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'AUTRE MVT(S)', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_om_, 7);
		END IF;	
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_synthese_approvision_distribution ORDER BY _id, _rang, _type, _intitule, _code;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_synthese_approvision_distribution(bigint, bigint, bigint, date, date, boolean)
  OWNER TO postgres;


-- Function: get_pr(bigint, bigint, bigint, date, bigint, bigint)
-- DROP FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint);
CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pr_ double precision;
	_depot_ bigint ;
	query_ character varying default 'SELECT m.cout_stock FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE COALESCE(m.calcul_pr, TRUE) IS TRUE AND m.mouvement = ''E''';

BEGIN
	SELECT INTO _depot_ depot_pr FROM yvs_base_article_depot WHERE article = article_ AND depot = depot_;
	_depot_ = COALESCE(_depot_, depot_);
	query_ = query_ || ' AND m.article = '||COALESCE(article_, 0)||' AND m.date_doc <= '||QUOTE_LITERAL(COALESCE(date_, CURRENT_DATE));
	IF(COALESCE(_depot_, 0) > 0)THEN
		query_ = query_ || ' AND m.depot = '||_depot_;
	END IF;
	IF(COALESCE(tranche_, 0) > 0)THEN
		query_ = query_ || ' AND m.tranche = '||tranche_;
	END IF;
	IF(COALESCE(unite_, 0) > 0)THEN
		query_ = query_ || ' AND m.conditionnement = '||unite_;
	END IF;
	IF(COALESCE(current_, 0) > 0)THEN
		query_ = query_ || ' AND m.id != '||current_;
	END IF;
	EXECUTE query_ INTO pr_;
	IF(pr_ IS NULL OR pr_ <=0)THEN
		pr_ = get_pua(article_, 0, 0, unite_);
	END IF;
	RETURN pr_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint) IS 'retourne le prix de revient d'' article';



-- Function: compta_action_on_content_journal_piece_virement()
-- DROP FUNCTION compta_action_on_content_journal_piece_virement();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_piece_virement()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_caisse_piece_virement DISABLE TRIGGER compta_action_on_piece_virement;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_caisse_piece_virement SET comptabilise = TRUE WHERE id = NEW.reglement;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_caisse_piece_virement SET comptabilise = FALSE WHERE id = OLD.reglement;
	END IF;
	ALTER TABLE yvs_compta_caisse_piece_virement ENABLE TRIGGER compta_action_on_piece_virement;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_piece_virement()
  OWNER TO postgres;