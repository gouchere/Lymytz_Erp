-- Function: prod_et_synthese_consommation_mp(bigint, bigint, bigint, character varying, date, date, character varying, character varying)

-- DROP FUNCTION prod_et_synthese_consommation_mp(bigint, bigint, bigint, character varying, date, date, character varying, character varying);

CREATE OR REPLACE FUNCTION prod_et_synthese_consommation_mp(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN article_ character varying, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN type_ character varying)
  RETURNS TABLE(id bigint, article bigint, reference character varying, designation character varying, unite bigint, numero character varying, classe bigint, code character varying, intitule character varying, quantite double precision, prix double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE
	data_ RECORD;
	classe_ RECORD;
	unite_ RECORD;
	dates_ RECORD;

	query_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT a.id, a.ref_art, a.designation, c.id as unite, u.reference, c.prix AS prix_vente, c.prix_achat, c.prix_prod FROM yvs_base_articles a INNER JOIN yvs_base_conditionnement c ON a.id = c.article
					INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_base_famille_article o ON a.famille = o.id WHERE a.categorie = ''MP''';
	mouvement_ CHARACTER VARYING DEFAULT 'SELECT SUM(y.quantite) FROM yvs_base_mouvement_stock y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_depots d ON y.depot = d.id INNER JOIN yvs_agences f ON d.agence = f.id WHERE y.quantite IS NOT NULL';
	--inner_es CHARACTER VARYING DEFAULT 'LEFT JOIN yvs_com_contenu_doc_stock_reception r ON y.id_externe = r.id INNER JOIN yvs_com_contenu_doc_stock s ON r.contenu = s.id INNER JOIN yvs_com_doc_stocks o ON s.doc_stock = o.id WHERE';
	--inner_ss CHARACTER VARYING DEFAULT 'LEFT JOIN yvs_com_contenu_doc_stock s ON y.id_externe = s.id INNER JOIN yvs_com_doc_stocks o ON s.doc_stock = o.id WHERE';
	stock_ CHARACTER VARYING;
	ids_ CHARACTER VARYING DEFAULT '''0''';
	id_ CHARACTER VARYING DEFAULT '0';

	transfert_ DOUBLE PRECISION DEFAULT 0;
	quantite_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	prix_revient_ DOUBLE PRECISION DEFAULT 0;
	taux_ DOUBLE PRECISION DEFAULT 0;

	exist_ BIGINT DEFAULT 0;
	serial_ BIGINT DEFAULT 1;

BEGIN
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_et_synthese_consommation_mp;
	CREATE TEMP TABLE IF NOT EXISTS table_et_synthese_consommation_mp(_id bigint, _article bigint, _reference character varying, _designation character varying, _unite bigint, _numero character varying, _classe bigint, _code character varying, _intitule character varying, _quantite double precision,_prix double precision, _valeur double precision, _rang integer);
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
		FOR id_ IN SELECT head FROM regexp_split_to_table(article_,'-') head WHERE CHAR_LENGTH(head) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(id_);
		END LOOP;
		query_ = query_ || ' AND a.id IN ('||ids_||')';
	END IF;
	FOR data_ IN EXECUTE query_ || 'ORDER BY a.ref_art, u.reference'
	LOOP
		prix_revient_ := (SELECT get_pr(agence_,data_.id, 0, 0, date_fin_, data_.unite, 0));
		stock_ = mouvement_ || ' AND y.article = '||data_.id||' AND y.conditionnement = '||data_.unite;
		quantite_ = (SELECT get_stock(data_.id, 0, depot_, agence_, societe_, (date_debut_ - interval '1 day')::date, data_.unite));
		IF(COALESCE(quantite_, 0) != 0)THEN
			valeur_ = prix_revient_ * quantite_;
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'S.I', 'STOCK INITIAL', quantite_,prix_revient_, valeur_, 1);
		END IF;
		serial_ = serial_ + 1;
		--calcul des appro
		IF(depot_ IS NULL OR depot_ < 1)THEN
			EXECUTE stock_||' AND y.mouvement = ''E'' AND y.type_doc NOT IN(''FT'', ''TR'')' INTO quantite_;
		ELSE
			EXECUTE stock_ || ' AND y.mouvement = ''E''' INTO quantite_;
		END IF;
		IF(COALESCE(quantite_, 0) != 0)THEN
			valeur_ = prix_revient_ * quantite_;
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'APPROV', 'APPROVISSIONNEMENT', quantite_, prix_revient_, valeur_, 2);
		END IF;
		serial_ = serial_ + 1;
		RAISE NOTICE 'MATIERE PREMIERE : %',data_.ref_art;
		IF(COALESCE(periode_, '') = 'PF') THEN -- Synthèse par PF
			FOR classe_ IN SELECT a.id, a.ref_art AS code_ref, a.designation, SUM(u.quantite) AS quantite FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id
					INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id INNER JOIN yvs_prod_suivi_operations p ON u.id_operation = p.id
					INNER JOIN yvs_prod_session_of s ON p.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id INNER JOIN yvs_base_depots d ON e.depot = d.id
					INNER JOIN yvs_base_famille_article f ON f.id= a.famille
				WHERE e.date_session BETWEEN date_debut_ AND date_fin_ AND y.article = data_.id AND y.unite = data_.unite AND f.societe = societe_ AND (a.categorie='PF' OR a.categorie='PSF')
				AND ((COAlESCE(agence_, 0) > 0 AND d.agence = COALESCE(agence_, 0)) OR (COAlESCE(agence_, 0) < 1 AND d.agence IS NOT NULL))
				AND ((COAlESCE(depot_, 0) > 0 AND d.id = COALESCE(depot_, 0)) OR (COAlESCE(depot_, 0) < 1 AND d.id IS NOT NULL)) GROUP BY a.id
			LOOP
				quantite_ := classe_.quantite;
				RAISE NOTICE '	ARTICLE : %',classe_.code_ref;
				IF(COALESCE(quantite_,0) != 0)THEN
					valeur_ = prix_revient_ * quantite_;
					INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, classe_.id, classe_.code_ref, classe_.designation, quantite_,prix_revient_, valeur_, 4);
				END IF;
				serial_ = serial_ + 1;
			END LOOP;
		ELSIF(COALESCE(periode_, '') = 'CL') THEN -- Synthèse par classe statistiques
			--Consommation des produits non classés (n'ayant pas de classe statistiques)
			SELECT INTO quantite_ SUM(u.quantite) FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id
				INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id INNER JOIN yvs_prod_suivi_operations p ON u.id_operation = p.id
				INNER JOIN yvs_prod_session_of s ON p.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id INNER JOIN yvs_base_depots d ON e.depot = d.id
				WHERE e.date_session BETWEEN date_debut_ AND date_fin_ AND a.classe1 IS NULL AND a.classe2 IS NULL AND y.article = data_.id AND y.unite = data_.unite
				AND ((COAlESCE(agence_, 0) > 0 AND d.agence = COALESCE(agence_, 0)) OR (COAlESCE(agence_, 0) < 1 AND d.agence IS NOT NULL))
				AND ((COAlESCE(depot_, 0) > 0 AND d.id = COALESCE(depot_, 0)) OR (COAlESCE(depot_, 0) < 1 AND d.id IS NOT NULL));
			IF(quantite_ != 0)THEN
				valeur_ = prix_revient_ * quantite_;
				INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'NO CLASSE', 'NO CLASSE', quantite_,prix_revient_, valeur_, 3);
			END IF;
			serial_ = serial_ + 1;

			FOR classe_ IN SELECT cl.id, cl.code_ref, cl.designation, SUM(u.quantite) as quantite FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id
					INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id INNER JOIN yvs_prod_suivi_operations p ON u.id_operation = p.id
					INNER JOIN yvs_prod_session_of s ON p.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id INNER JOIN yvs_base_depots d ON e.depot = d.id
					INNER JOIN yvs_base_classes_stat cl ON (a.classe1 = cl.id OR a.classe2 = cl.id)
					WHERE e.date_session BETWEEN date_debut_ AND date_fin_ AND y.article = data_.id AND y.unite = data_.unite
				AND ((COAlESCE(agence_, 0) > 0 AND d.agence = COALESCE(agence_, 0)) OR (COAlESCE(agence_, 0) < 1 AND d.agence IS NOT NULL))
				AND ((COAlESCE(depot_, 0) > 0 AND d.id = COALESCE(depot_, 0)) OR (COAlESCE(depot_, 0) < 1 AND d.id IS NOT NULL)) GROUP BY cl.id
			LOOP
				quantite_ := classe_.quantite;
				IF(COALESCE(quantite_,0) != 0)THEN
					valeur_ = prix_revient_ * quantite_;
					INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, classe_.id, classe_.code_ref, classe_.designation, quantite_,prix_revient_, valeur_, 4);
				END IF;
				serial_ = serial_ + 1;
			END LOOP;
		ELSIF(COALESCE(periode_, '') = 'CP') THEN -- Synthèse par classe statistiques parent
			--Consommation des produits non classés (n'ayant pas de classe statistiques)
			SELECT INTO quantite_ SUM(u.quantite) FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id
				INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id INNER JOIN yvs_prod_suivi_operations p ON u.id_operation = p.id
				INNER JOIN yvs_prod_session_of s ON p.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id INNER JOIN yvs_base_depots d ON e.depot = d.id
				WHERE e.date_session BETWEEN date_debut_ AND date_fin_ AND a.classe1 IS NULL AND a.classe2 IS NULL AND y.article = data_.id AND y.unite = data_.unite
				AND ((COAlESCE(agence_, 0) > 0 AND d.agence = COALESCE(agence_, 0)) OR (COAlESCE(agence_, 0) < 1 AND d.agence IS NOT NULL))
				AND ((COAlESCE(depot_, 0) > 0 AND d.id = COALESCE(depot_, 0)) OR (COAlESCE(depot_, 0) < 1 AND d.id IS NOT NULL));
			IF(quantite_ != 0)THEN
				valeur_ = prix_revient_ * quantite_;
				INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'NO CLASSE', 'NO CLASSE', quantite_,prix_revient_, valeur_, 3);
			END IF;
			serial_ = serial_ + 1;

			FOR classe_ IN SELECT c.id, c.code_ref, c.designation FROM yvs_base_classes_stat c WHERE c.societe = societe_ AND parent IS NULL
			LOOP
				SELECT INTO quantite_ SUM(u.quantite) FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id
					INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id INNER JOIN yvs_prod_suivi_operations p ON u.id_operation = p.id
					INNER JOIN yvs_prod_session_of s ON p.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id INNER JOIN yvs_base_depots d ON e.depot = d.id
					WHERE e.date_session BETWEEN date_debut_ AND date_fin_ AND (a.classe1 = classe_.id OR a.classe1 IN (SELECT base_get_sous_classe_stat(classe_.id, true))) AND y.article = data_.id AND y.unite = data_.unite
				AND ((COAlESCE(agence_, 0) > 0 AND d.agence = COALESCE(agence_, 0)) OR (COAlESCE(agence_, 0) < 1 AND d.agence IS NOT NULL))
				AND ((COAlESCE(depot_, 0) > 0 AND d.id = COALESCE(depot_, 0)) OR (COAlESCE(depot_, 0) < 1 AND d.id IS NOT NULL));
				IF(COALESCE(quantite_,0) != 0)THEN
					valeur_ = prix_revient_ * quantite_;
					INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, classe_.id, classe_.code_ref, classe_.designation, quantite_,prix_revient_, valeur_, 4);
				END IF;
				serial_ = serial_ + 1;
			END LOOP;
		ELSE			
			FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
			LOOP
				SELECT INTO quantite_ SUM(u.quantite) FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id
					INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id INNER JOIN yvs_prod_suivi_operations p ON u.id_operation = p.id
					INNER JOIN yvs_prod_session_of s ON p.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id INNER JOIN yvs_base_depots d ON e.depot = d.id
					WHERE e.date_session BETWEEN dates_.date_debut AND dates_.date_fin AND y.article = data_.id AND y.unite = data_.unite
					AND ((COAlESCE(agence_, 0) > 0 AND d.agence = COALESCE(agence_, 0)) OR (COAlESCE(agence_, 0) < 1 AND d.agence IS NOT NULL))
					AND ((COAlESCE(depot_, 0) > 0 AND d.id = COALESCE(depot_, 0)) OR (COAlESCE(depot_, 0) < 1 AND d.id IS NOT NULL));
				IF(COALESCE(quantite_,0) != 0)THEN
					valeur_ = prix_revient_ * quantite_;
					INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, dates_.intitule, dates_.intitule, quantite_,prix_revient_, valeur_, 4);
				END IF;
				serial_ = serial_ + 1;
			END LOOP;
		END IF;	
		-- Total des consommations
		SELECT INTO quantite_ SUM(y._quantite) FROM table_et_synthese_consommation_mp y WHERE y._article = data_.id AND y._unite = data_.unite AND y._rang IN (3, 4);
		IF(quantite_ != 0)THEN
			valeur_ = prix_revient_ * quantite_;
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'TOTAL', 'TOTAL CONSO', quantite_,prix_revient_, valeur_, 5);
		END IF;
		serial_ = serial_ + 1;
		--calcul les sorties qui ne sont pas les consommations MP	
		IF(depot_ IS NULL OR depot_ < 1)THEN
			EXECUTE stock_|| ' AND y.mouvement = ''S'' AND y.table_externe NOT IN (''yvs_prod_composant_of'', ''yvs_prod_flux_composant'', ''yvs_prod_of_suivi_flux'') AND y.type_doc NOT IN (''FT'', ''TR'') ' INTO quantite_;
		ELSE
			EXECUTE stock_|| ' AND y.mouvement = ''S'' AND y.table_externe NOT IN (''yvs_prod_composant_of'', ''yvs_prod_flux_composant'', ''yvs_prod_of_suivi_flux'')' INTO quantite_;
		END IF;
		IF(COALESCE(quantite_, 0) != 0)THEN
			valeur_ = prix_revient_ * quantite_;
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'SORTIE', 'AUTRES SORTIE', quantite_,prix_revient_, valeur_, 6);
		END IF;
		serial_ = serial_ + 1;
		-- Calcul du stocks final
		quantite_ = (SELECT get_stock(data_.id, 0, depot_, agence_, societe_, date_fin_, data_.unite));
		IF(COALESCE(quantite_, 0) != 0)THEN
			valeur_ = prix_revient_ * quantite_;
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'S.F', 'STOCK FINAL', quantite_,prix_revient_, valeur_, 7);
		END IF;
		serial_ = serial_ + 1;
	END LOOP;
	IF(COALESCE(type_, '') NOT IN ('', ' '))THEN
		IF(COALESCE(type_, '') = 'D')THEN
			FOR classe_ IN SELECT DISTINCT y._article AS article FROM table_et_synthese_consommation_mp y
			LOOP
				SELECT INTO unite_ c.id as unite, u.id, u.reference, c.prix AS prix_vente, c.prix_achat, c.prix_prod FROM yvs_base_conditionnement c INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE c.article = classe_.article AND c.by_prod IS TRUE AND c.defaut IS TRUE LIMIT 1;
				IF(COALESCE(unite_.id, 0) > 0)THEN
					prix_revient_ := (SELECT get_pr(agence_,classe_.article, 0, 0, date_fin_, unite_.unite, 0));
					FOR data_ IN SELECT y.*, c.unite FROM table_et_synthese_consommation_mp y INNER JOIN yvs_base_conditionnement c ON y._unite = c.id WHERE y._article = classe_.article AND y._unite != COALESCE(unite_.unite, 0)
					LOOP
						SELECT INTO taux_ t.taux_change FROM yvs_base_table_conversion t WHERE t.unite = COALESCE(data_.unite, 0) AND t.unite_equivalent = COALESCE(unite_.id, 0);
						IF(COALESCE(taux_, 0) = 0)THEN
							SELECT INTO taux_ t.taux_change FROM yvs_base_table_conversion t WHERE t.unite = COALESCE(unite_.id, 0) AND t.unite_equivalent = COALESCE(data_.unite, 0);
							IF(COALESCE(taux_, 0) = 0)THEN
								quantite_ = 0;
							ELSE
								quantite_ = data_._quantite / COALESCE(taux_, 0);
							END IF;
						ELSE
							quantite_ = data_._quantite * COALESCE(taux_, 0);
						END IF;
						DELETE FROM table_et_synthese_consommation_mp WHERE _id = data_._id;
						SELECT INTO exist_ _id FROM table_et_synthese_consommation_mp y WHERE y._article = classe_.article AND y._code = data_._code AND y._unite = COALESCE(unite_.unite, 0);

						valeur_ = prix_revient_ * quantite_;
						IF(COALESCE(exist_, 0) = 0)THEN
							INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_._article, data_._reference, data_._designation, unite_.unite, unite_.reference, data_._classe, data_._code, data_._intitule, quantite_, prix_revient_, valeur_, data_._rang);
						ELSE
							UPDATE table_et_synthese_consommation_mp y SET _valeur = y._valeur + valeur_, _quantite = y._quantite + quantite_, _prix = prix_revient_ WHERE y._id = exist_;
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
ALTER FUNCTION prod_et_synthese_consommation_mp(bigint, bigint, bigint, character varying, date, date, character varying, character varying)
  OWNER TO postgres;
