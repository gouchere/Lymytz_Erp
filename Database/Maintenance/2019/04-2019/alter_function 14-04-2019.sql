-- Function: com_et_dashboard(bigint, bigint, bigint, date, date)
-- DROP FUNCTION com_et_dashboard(bigint, bigint, bigint, date, date);
CREATE OR REPLACE FUNCTION com_et_dashboard(IN societe_ bigint, IN agence_ bigint, IN users_ bigint, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(nombre bigint, valeur double precision, code character varying, libelle character varying, rang integer) AS
$BODY$
declare 

   query_ character varying;
   code_ character varying;
   libelle_ character varying;
   
   valeur_ double precision default 0;
   autres_ double precision default 0;
   
   nombre_ bigint default 0;
   nombre_2 bigint default 0;
   
begin 	
	DROP TABLE IF EXISTS table_et_dashboard;
	CREATE TEMP TABLE IF NOT EXISTS table_et_dashboard(_nombre bigint, _valeur double precision, _code character varying, _libelle character varying, _rang integer);
	DELETE FROM table_et_dashboard;
	-- chiffre d'affaire reel ((ca valide + ca service) - ca avoir)
	-- chiffre d'affaire valider
	code_ = 'caVenteValide';
	libelle_ = 'Facture completement validée';
	query_ = 'SELECT SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(c.prix_total)', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 5);
	-- chiffre d'affaire sur services supplementaire
	code_ = 'caVenteValideSS';
	libelle_ = 'Service supplementaire validé';
	query_ = 'SELECT SUM(c.montant) FROM yvs_com_cout_sup_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id INNER JOIN yvs_grh_type_cout t ON c.type_cout = t.id
		WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND a.societe = '||societe_||' AND c.service IS TRUE AND t.augmentation IS FALSE AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO autres_;
	EXECUTE REPLACE(query_, 't.augmentation IS FALSE', 't.augmentation IS TRUE') INTO valeur_;
	valeur_ = COALESCE(valeur_, 0) - COALESCE(autres_, 0);
	INSERT INTO table_et_dashboard VALUES (COALESCE(0, 0), COALESCE(valeur_, 0), code_, libelle_, -1);
	-- chiffre d'affaire sur couts supplementaire
	code_ = 'caVenteValideCS';
	libelle_ = 'Cout supplementaire validé';
	query_ = REPLACE(query_, 'c.service IS TRUE', 'c.service IS FALSE');
	EXECUTE query_ INTO autres_;
	EXECUTE REPLACE(query_, 't.augmentation IS FALSE', 't.augmentation IS TRUE') INTO valeur_;
	valeur_ = COALESCE(valeur_, 0) - COALESCE(autres_, 0);
	INSERT INTO table_et_dashboard VALUES (COALESCE(0, 0), COALESCE(valeur_, 0), code_, libelle_, -2);
	-- chiffre d'affaire avoir
	code_ = 'caVenteAvoir';
	libelle_ = 'Facture d''avoir validée';
	query_ = 'SELECT SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''BRV'' AND d.statut = ''V'' AND a.societe = '||societe_||' AND d.date_livraison BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO autres_;
	EXECUTE REPLACE(query_, 'SUM(c.prix_total)', 'COUNT(DISTINCT d.id)') INTO nombre_2;
	query_ = REPLACE(REPLACE(query_, 'd.date_livraison', 'e.date_entete'), 'BRV', 'FAV');
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(c.prix_total)', 'COUNT(DISTINCT d.id)') INTO nombre_;
	valeur_ = COALESCE(valeur_, 0) + COALESCE(autres_, 0);
	nombre_ = COALESCE(nombre_, 0) + COALESCE(nombre_2, 0);
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 6);
	autres_ = valeur_;
	nombre_ = nombre_2;
	-- chiffre d'affaire reel
	code_ = 'caVente';
	libelle_ = 'Chiffre d''affaire réel';
	SELECT INTO valeur_ SUM(_valeur) FROM table_et_dashboard WHERE _code IN ('caVenteValide', 'caVenteValideSS');
	SELECT INTO nombre_ _nombre FROM table_et_dashboard WHERE _code = 'caVenteValide';
	valeur_ = COALESCE(valeur_, 0) - COALESCE(autres_, 0);
	nombre_ = COALESCE(nombre_, 0) - COALESCE(nombre_2, 0);
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 0);
	
	-- chiffre d'affaire en attente
	code_ = 'caVenteAttence';
	libelle_ = 'Facture en attente de validation';
	query_ = 'SELECT sum(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut = ''E'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 2);
	
	-- chiffre d'affaire en cours
	code_ = 'caVenteEnCours';
	libelle_ = 'Facture en cours de validation';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut = ''R'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 3);
	
	-- chiffre d'affaire annulé
	code_ = 'caVenteAnnule';
	libelle_ = 'Facture annulée';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut = ''A'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 4);
	
	-- chiffre d'affaire livré
	code_ = 'caVenteLivre';
	libelle_ = 'Facture completement livrée';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_livre = ''L'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 7);
	
	-- chiffre d'affaire livraison en cours
	code_ = 'caVenteEnCoursLivre';
	libelle_ = 'Facture en cours de livraison';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_livre = ''R'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 8);
	
	-- chiffre d'affaire livraison en attence
	code_ = 'caVenteNotLivre';
	libelle_ = 'Facture en attente de livraison';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_livre = ''W'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 9);
	
	-- chiffre d'affaire livraison en retard
	code_ = 'caVenteRetardLivr';
	libelle_ = 'Facture en retard de livraison';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_livre != ''L'' AND a.societe = '||societe_||' AND d.date_livraison_prevu < '||QUOTE_LITERAL(date_fin_)||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 10);
	
	-- chiffre d'affaire réglé
	code_ = 'caVenteRegle';
	libelle_ = 'Facture completement réglée';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_regle = ''P'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 11);
	
	-- chiffre d'affaire reglement en cours
	code_ = 'caVenteEnCoursRegle';
	libelle_ = 'Facture en cours de reglement';
	query_ = 'SELECT SUM(c.montant) FROM yvs_compta_caisse_piece_vente c INNER JOIN yvs_com_doc_ventes d ON c.vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_regle = ''R'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(c.montant)', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 12);
	
	-- chiffre d'affaire réglé
	code_ = 'caVenteNotRegle';
	libelle_ = 'Facture en attente de reglement';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_regle = ''W'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 14);
	
	-- total facture
	code_ = 'ca';
	libelle_ = 'Chiffre d''affaire Provisoire';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	SELECT INTO autres_ SUM(_valeur) FROM table_et_dashboard WHERE _code = 'caVenteAvoir';
	valeur_ = COALESCE(valeur_, 0) - COALESCE(autres_, 0);
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 1);
	
	RETURN QUERY SELECT * FROM table_et_dashboard ORDER BY _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_dashboard(bigint, bigint, bigint, date, date)
  OWNER TO postgres;

  
-- Function: com_et_mouvement_stock(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying)
-- DROP FUNCTION com_et_mouvement_stock(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION com_et_mouvement_stock(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN tranche_ bigint, IN article_ bigint, IN unite_ bigint, IN date_debut_ date, IN date_fin_ date, IN mouvement_ character varying)
  RETURNS SETOF yvs_base_mouvement_stock AS
$BODY$
declare 
   stock_ DOUBLE PRECISION DEFAULT 0;
   date_initial_ DATE DEFAULT COALESCE(date_debut_, CURRENT_DATE) - 1;
   
   query_ CHARACTER VARYING DEFAULT 'CREATE TEMP TABLE table_mouvement_stock AS SELECT y.* FROM yvs_base_mouvement_stock y INNER JOIN yvs_base_depots d ON y.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE a.societe = '||QUOTE_LITERAL(societe_);
   
begin 	
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(depot_, 0) > 0)THEN
		query_ = query_ || ' AND y.depot = '||depot_;
	END IF;
	IF(COALESCE(tranche_, 0) > 0)THEN
		query_ = query_ || ' AND y.tranche = '||tranche_;
	END IF;
	IF(COALESCE(article_, 0) > 0)THEN
		query_ = query_ || ' AND y.article = '||article_;
		IF(date_debut_ IS NOT NULL)THEN		
			stock_ = (SELECT get_stock_reel(article_, tranche_, depot_, agence_, societe_, date_initial_, unite_, 0));
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
					     VALUES (-1, stock_, date_initial_, 'E', article_, false, true, -1, 'nothing', 'Stock Initial', depot_, null, 0, date_initial_, 0, 16, tranche_, null, unite_, current_date, current_date, null);
	END IF;
	RETURN QUERY SELECT * FROM table_mouvement_stock ORDER BY depot, article, date_doc, mouvement;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_mouvement_stock(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;

  
  
-- Function: get_pr(bigint, bigint, bigint, date, bigint)
-- DROP FUNCTION get_pr(bigint, bigint, bigint, date, bigint);
CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE

BEGIN
	return (select get_pr(article_, depot_, tranche_, date_, unite_, 0));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date, bigint) IS 'retourne le prix de revient d'' article';


-- Function: get_pr(bigint, bigint, bigint, date, bigint, bigint)
-- DROP FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint);
CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pr_ double precision;
	query_ character varying default 'SELECT cout_stock FROM yvs_base_mouvement_stock WHERE calcul_pr IS TRUE AND mouvement = ''E'' AND article = '||COALESCE(article_, 0)||' AND date_doc <= '||QUOTE_LITERAL(COALESCE(date_, CURRENT_DATE));

BEGIN
	IF(COALESCE(depot_, 0) > 0)THEN
		query_ = query_ || ' AND depot = '||depot_;
	END IF;
	IF(COALESCE(tranche_, 0) > 0)THEN
		query_ = query_ || ' AND tranche = '||tranche_;
	END IF;
	IF(COALESCE(unite_, 0) > 0)THEN
		query_ = query_ || ' AND conditionnement = '||unite_;
	END IF;
	IF(COALESCE(current_, 0) > 0)THEN
		query_ = query_ || ' AND id != '||current_;
	END IF;
	EXECUTE query_ INTO pr_;
	IF(pr_ IS NULL OR pr_ <=0)THEN
		pr_ = get_pua(article_, 0, depot_, unite_);
	END IF;
	RETURN pr_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint) IS 'retourne le prix de revient d'' article';


-- Function: com_recalcule_remise_periode(bigint, bigint, bigint, date, date, bigint)
-- DROP FUNCTION com_recalcule_remise_periode(bigint, bigint, bigint, date, date, bigint);
CREATE OR REPLACE FUNCTION com_recalcule_remise_periode(societe_ bigint, agence_ bigint, client_ bigint, debut_ date, fin_ date, point_ bigint)
  RETURNS double precision AS
$BODY$
declare 
	line_ record;
	query_ character varying default 'SELECT c.id, c.article, c.conditionnement, c.quantite, c.prix, c.rabais, d.client, e.date_entete, cp.point::bigint, co.unite, a.puv_ttc, d.categorie_comptable as categorie 
						FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_base_articles a ON c.article = a.id 
						INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc
						INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point
						INNER JOIN yvs_base_point_vente pv ON cp.point = pv.id INNER JOIN yvs_agences ag ON pv.agence = ag.id
						INNER JOIN yvs_base_conditionnement co ON c.conditionnement = co.id INNER JOIN yvs_base_unite_mesure un ON co.unite = un.id
						WHERE e.date_entete BETWEEN '''||debut_||''' AND '''|| fin_||'''';
	total_ double precision default 0;
	prix_total_ double precision;
	taxe_ double precision default 0;
	prix_ double precision;
	remise_ double precision;
begin 
	IF(COALESCE(societe_, 0) > 0) THEN 
		query_= query_ || 'AND ag.societe = '||societe_;
	END IF;
	IF(COALESCE(agence_, 0) > 0) THEN 
		query_= query_ || 'AND ag.id = '||agence_;
	END IF;
	IF(COALESCE(point_, 0) > 0) THEN 
		query_= query_ || 'AND pv.id = '||point_;
	END IF;
	IF(COALESCE(client_, 0) > 0) THEN 
		query_= query_ || 'AND d.client = '||client_;
	END IF;
	ALTER TABLE yvs_com_contenu_doc_vente DISABLE TRIGGER update_;
	FOR line_ IN execute query_
	LOOP
		prix_ = line_.prix - COALESCE(line_.rabais, 0);
		remise_ = COALESCE(get_remise_vente(line_.article, line_.quantite, prix_, line_.client, 0, line_.date_entete, line_.unite), 0);		
		IF(line_.puv_ttc IS FALSE AND COALESCE(line_.categorie, 0) > 0)THEN	
			taxe_ = (SELECT get_taxe(line_.article, line_.categorie, 0, remise_, line_.quantite, prix_, true));
		END IF;
		prix_total_ = (line_.quantite * prix_) + taxe_ - remise_; 
		UPDATE yvs_com_contenu_doc_vente SET remise = COALESCE(remise_, 0), prix_total = prix_total_ WHERE id = line_.id;
		total_ = total_ + COALESCE(remise_, 0);
	END LOOP;
	ALTER TABLE yvs_com_contenu_doc_vente ENABLE TRIGGER update_;
	RETURN total_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_recalcule_remise_periode(bigint, bigint, bigint, date, date, bigint)
  OWNER TO postgres;

  
-- Function: com_recalcule_ristourne_periode(bigint, bigint, bigint, date, date, bigint)
-- DROP FUNCTION com_recalcule_ristourne_periode(bigint, bigint, bigint, date, date, bigint);
CREATE OR REPLACE FUNCTION com_recalcule_ristourne_periode(societe_ bigint, agence_ bigint, client_ bigint, debut_ date, fin_ date, point_ bigint)
  RETURNS double precision AS
$BODY$
declare 
	line_ record;
	query_ character varying default 'SELECT c.id, c.conditionnement, c.quantite, c.prix, d.client, e.date_entete FROM yvs_com_contenu_doc_vente c 
						INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc
						INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point
						INNER JOIN yvs_base_point_vente pv ON cp.point = pv.id INNER JOIN yvs_agences ag ON pv.agence = ag.id
						WHERE e.date_entete BETWEEN '''||debut_||''' AND '''|| fin_||'''';
	total_ double precision default 0;
	rist_ double precision;
begin 
	IF(COALESCE(societe_, 0) > 0) THEN 
		query_= query_ || 'AND ag.societe = '||societe_;
	END IF;
	IF(COALESCE(agence_, 0) > 0) THEN 
		query_= query_ || 'AND ag.id = '||agence_;
	END IF;
	IF(COALESCE(point_, 0) > 0) THEN 
		query_= query_ || 'AND pv.id = '||point_;
	END IF;
	IF(COALESCE(client_, 0) > 0) THEN 
		query_= query_ || 'AND d.client = '||client_;
	END IF;
	ALTER TABLE yvs_com_contenu_doc_vente DISABLE TRIGGER update_;
	FOR line_ IN execute query_
	LOOP
		rist_ = COALESCE(get_ristourne(line_.conditionnement, line_.quantite, line_.prix, line_.client, line_.date_entete), 0);
		UPDATE yvs_com_contenu_doc_vente SET ristourne = COALESCE(rist_, 0) WHERE id=line_.id;
		total_ = total_ + COALESCE(rist_, 0);
	END LOOP;
	ALTER TABLE yvs_com_contenu_doc_vente ENABLE TRIGGER update_;
	RETURN total_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_recalcule_ristourne_periode(bigint, bigint, bigint, date, date, bigint)
  OWNER TO postgres;  
  


-- Function: stock_maj_prix_mvt()
-- DROP FUNCTION stock_maj_prix_mvt();
CREATE OR REPLACE FUNCTION stock_maj_prix_mvt()
  RETURNS trigger AS
$BODY$    
DECLARE
	prix_arr_ double precision;
	stock_ double precision;
	last_pr_ double precision;
	new_cout double precision;
	new_quantite double precision;
	
	action_ character varying;
BEGIN
	-- fonction de calcul du prix de revient dans la table mvt de stock
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_='INSERT') THEN  
		new_quantite = 0;
	ELSIF(action_='UPDATE') THEN  
		new_quantite = NEW.quantite - OLD.quantite;
	END IF;
	last_pr_= COALESCE(get_pr(NEW.article, NEW.depot, 0, NEW.date_doc, NEW.conditionnement, NEW.id), 0);
	IF(NEW.mouvement = 'E' AND NEW.calcul_pr IS TRUE) THEN 
		RAISE NOTICE 'Prix %',last_pr_;
		--calcule le stock
		stock_= get_stock_reel(NEW.article,0,NEW.depot, 0, 0, NEW.date_doc, NEW.conditionnement, 0) + new_quantite;
		RAISE NOTICE 'Stock  %',stock_;
		-- Recherche des valeurs null
		if(stock_ is null) THEN
			stock_=0;
		END IF;
		if(NEW.quantite is null)then
			NEW.quantite = 0;
		end if;
		if(NEW.cout_entree is null)then
			NEW.cout_entree = 0;
		end if;
		-- Calcul du cout de stockage
		prix_arr_ = stock_ + NEW.quantite;
		RAISE NOTICE 'Quantité inséré %, %',stock_,prix_arr_;
		if(prix_arr_ <= 0)then
			prix_arr_ = 1;
		end if;
		new_cout = (((stock_ * last_pr_) + (NEW.quantite * NEW.cout_entree)) / (prix_arr_));
		-- Retourne le nouveau cout moyen calculé
		if(new_cout is null)then
			new_cout = 0;
		end if;
	ELSE 
		new_cout = last_pr_;
	END IF;
	NEW.cout_stock = ROUND(new_cout::decimal, 3);
	return NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION stock_maj_prix_mvt()
  OWNER TO postgres;


  
-- Function: insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
-- DROP FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date);
CREATE OR REPLACE FUNCTION insert_mouvement_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, coutentree_ double precision, cout_ double precision, parent_ bigint, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE
       operation_ character varying default '';
       ligne_ record;
BEGIN
	CASE tableexterne_
		WHEN 'yvs_com_contenu_doc_vente' THEN  
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_contenu_doc_vente where id = idexterne_;
			operation_='Vente';
		WHEN 'yvs_com_contenu_doc_achat' THEN	
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_contenu_doc_achat where id = idexterne_;
			operation_='Achat';
		WHEN 'yvs_com_ration' THEN	
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_ration where id = idexterne_;
			operation_='Ration';
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			select into ligne_ d.type_doc, c.qualite, c.conditionnement, c.conditionnement_entree, c.lot_sortie as lot, calcul_pr FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
				if(mouvement_ = 'E')then
					ligne_.conditionnement = ligne_.conditionnement_entree;
				end if;
			elsif(ligne_.type_doc='TR')then
				if(mouvement_='S') then
					operation_='Reconditionnement';
				else
					select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot, calcul_pr from yvs_com_contenu_doc_stock where id = idexterne_;
					operation_='Reconditionnement';
				end if;
			else
				if(mouvement_='S') then
					operation_='Sortie';
				else
					operation_='Entrée';
				end if;
			end if;	
		WHEN 'yvs_com_reconditionnement' THEN	
			if(mouvement_='S') then
				select into ligne_ qualite as qualite, conditionnement as conditionnement, lot as lot, calcul_pr from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			else
				select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot, calcul_pr from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			end if;	
		WHEN 'yvs_prod_of_suivi_flux' THEN	
			select into ligne_ c.unite as conditionnement, null::bigint as qualite, null::bigint as lot, calcul_pr from yvs_prod_of_suivi_flux y inner join yvs_prod_flux_composant f on y.composant = f.id inner join yvs_prod_composant_of c on f.composant = c.id where y.id = idexterne_;
			if(mouvement_='S') then
				operation_='Consommation';
			else
				operation_='Production';
			end if;
		WHEN 'yvs_prod_declaration_production' THEN	
			select into ligne_ conditionnement, null::bigint as qualite, null::bigint as lot, calcul_pr from yvs_prod_declaration_production where id = idexterne_;
			operation_='Production';
		WHEN 'yvs_prod_contenu_conditionnement' THEN	
			select into ligne_ conditionnement, null::bigint as qualite, null::bigint as lot, calcul_pr from yvs_prod_contenu_conditionnement where id = idexterne_;
			operation_='Conditionnement';
		WHEN 'yvs_prod_fiche_conditionnement' THEN	
			select into ligne_ unite_mesure as conditionnement, null::bigint as qualite, null::bigint as lot, null::boolean calcul_pr from yvs_prod_fiche_conditionnement f inner join yvs_prod_nomenclature n on f.nomenclature = n.id where f.id = idexterne_;
			operation_='Conditionnement';
		ELSE
			RETURN -1;
	END CASE;
	if(parent_ is not null)then
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, tranche_::bigint, parent_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, parent_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, tranche_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date) IS 'Insert une ligne de sortie de mouvement de stock';



-- Function: update_contenu_doc_achat()
-- DROP FUNCTION update_contenu_doc_achat();
CREATE OR REPLACE FUNCTION update_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	mouv_ bigint;
	arts_ record;
	ligne_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, date_doc from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'E';
					if(doc_.type_doc = 'BLA')then
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					end if;
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_recu, cout_entree = NEW.pua_recu, conditionnement = NEW.conditionnement, lot = NEW.lot, calcul_pr = NEW.calcul_pr, date_doc = doc_.date_doc, tranche = doc_.tranche where id = mouv_;
					FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
					LOOP
						IF(ligne_.id != mouv_)THEN
							DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
						END IF;
					END LOOP;
				end if;
			else
				if(doc_.type_doc = 'BLA')then			
					result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					result = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_achat()
  OWNER TO postgres;

  
-- Function: update_contenu_doc_vente()
-- DROP FUNCTION update_contenu_doc_vente();
CREATE OR REPLACE FUNCTION update_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	dep_ record;
	mouv_ bigint;
	arts_ record;
	ligne_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'BRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article;
						if(doc_.type_doc = 'BLV')then
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
						else
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = (NEW.quantite), cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot, calcul_pr = NEW.calcul_pr, date_doc = doc_.date_livraison, tranche = doc_.tranche_livrer where id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					if(doc_.type_doc = 'BLV')then
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
					else
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
					end if;
				end if;	
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_vente()
  OWNER TO postgres;
  
  
  
-- Function: update_contenu_doc_stock()
-- DROP FUNCTION update_contenu_doc_stock();
CREATE OR REPLACE FUNCTION update_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	entree_ record;
	sortie_ record;
	ligne_ record;
	
	mouv_ bigint;
	trancheD_ bigint;
	trancheS_ bigint;
	
	result boolean default false;
BEGIN
	select into doc_ * from yvs_com_doc_stocks where id = NEW.doc_stock;
	select into trancheD_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = doc_.creneau_source;
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	--Insertion mouvement stock
	if(doc_.statut = 'V' or doc_.statut = 'U') then
		if(doc_.type_doc = 'FT') then	
			if(doc_.source is not null and (doc_.statut = 'U' or doc_.statut = 'V'))then
				select into mouv_ id from yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME and depot = doc_.source and mouvement = 'S' ORDER BY id;				
				if(coalesce(mouv_, 0) > 0)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'S';
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr, date_doc=doc_.date_doc WHERE id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'S' AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;	
			end if;	
			if(doc_.statut = 'V' or (NEW.statut = 'V' and doc_.statut = 'U'))then
				if(doc_.destination is not null)then			
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and mouvement = 'E' ORDER BY id;
					if(mouv_ is not null)then
						if(arts_.methode_val = 'FIFO')then
							delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'E';
							result = (select valorisation_stock(NEW.article, NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_reception));
						else
							UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite_entree, cout_entree = NEW.prix_entree, conditionnement = NEW.conditionnement_entree, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr, date_doc = NEW.date_reception WHERE id = mouv_;
							FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'E' AND parent IS NULL ORDER BY id
							LOOP
								IF(ligne_.id != mouv_)THEN
									DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
								END IF;
							END LOOP;
						end if;
					else
						result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_reception));
					end if;	
				end if;
			else
				delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME;
			end if;
		elsif(doc_.type_doc = 'SS') then
			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
						result = (select valorisation_stock(NEW.article, NEW.conditionnement,doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr , date_doc=doc_.date_doc WHERE id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					result = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;	
			end if;
		elsif(doc_.type_doc = 'ES') then
			IF(doc_.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination AND mouvement = 'E';
				IF(mouv_ is not null)THEN
						IF(arts_.methode_val = 'FIFO')then
							delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = NEW.article and mouvement = 'E';
							result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
						ELSE
							UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement_entree, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr , date_doc=doc_.date_doc WHERE id = mouv_;
							FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
						END IF;
				ELSE
					result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				END IF;	
			END IF;
		elsif(doc_.type_doc = 'IN') then
			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
						if(NEW.quantite>0)then
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						elsif(NEW.quantite<0)then
							result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
						end if;
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr , date_doc=doc_.date_doc WHERE id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					if(NEW.quantite>0)then
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					elsif(NEW.quantite<0)then
						result = (select valorisation_stock(NEW.article, NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					end if;
				end if;	
			end if;
		elsif(doc_.type_doc = 'TR') then
			if(doc_.source is not null)then
				select into entree_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement;
				select into sortie_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement_entree;

				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
						result = (select valorisation_stock(NEW.article,NEW.conditionnement,  doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree,  doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr, date_doc=doc_.date_doc
													    WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite_entree, cout_entree = NEW.prix_entree, conditionnement = NEW.conditionnement_entree, lot = NEW.lot_entree, calcul_pr = NEW.calcul_pr, date_doc=doc_.date_doc 
														WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'E';
					end if;
				else
					result = (select valorisation_stock(NEW.article,NEW.conditionnement,  doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree,  doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;	
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_stock()
  OWNER TO postgres;


  
 -- Function: update_doc_achats()
-- DROP FUNCTION update_doc_achats();
CREATE OR REPLACE FUNCTION update_doc_achats()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
	depot_ bigint;
	tranche_ bigint;
BEGIN
	if(NEW.type_doc = 'FRA' or NEW.type_doc = 'BLA')then
			if(NEW.statut = 'V')then
				for cont_ in select id, article , quantite_recu as qte, pua_recu as prix, conditionnement, calcul_pr FROM yvs_com_contenu_doc_achat where doc_achat = NEW.id
				loop
					select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;					
					--Insertion mouvement stock
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and article = cont_.article;
					if(mouv_ is not null)then
						if(arts_.methode_val = 'FIFO')then
							delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and article = cont_.article;
							if(NEW.type_doc = 'BLA')then
								result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
							else
								result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
							end if;
						else
							UPDATE yvs_base_mouvement_stock SET quantite = cont_.qte, cout_entree = cont_.prix , conditionnement=cont_.conditionnement , calcul_pr=cont_.calcul_pr, tranche = NEW.tranche WHERE id = mouv_;
						end if;
					else
						if(NEW.type_doc = 'BLA')then
							result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
						else
							result = (select valorisation_stock(cont_.article, cont_.conditionnement,NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
						end if;
					end if;	
				end loop;
			elsif(NEW.statut != 'V')then
				for cont_ in select id from yvs_com_contenu_doc_achat where doc_achat = OLD.id
				loop		
					
					--Recherche mouvement stock
					for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat'
					loop
						delete from yvs_base_mouvement_stock where id = mouv_;
					end loop;
				end loop;
			end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_achats()
  OWNER TO postgres;

  
-- Function: update_doc_ration()
-- DROP FUNCTION update_doc_ration();
CREATE OR REPLACE FUNCTION update_doc_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	doc_ record;
	mouv_ bigint;
	article_ record;
	result_ boolean default false;
	prix_ double precision;
BEGIN
	if(NEW.statut = 'V') then
		SELECT INTO doc_ cd.id, cd.tranche FROM yvs_com_doc_ration d INNER JOIN yvs_com_creneau_depot cd ON cd.id=d.creneau_horaire WHERE d.id=NEW.id;
		for cont_ in select id, article , quantite, conditionnement, date_ration, calcul_pr FROM yvs_com_ration r  WHERE r.doc_ration= NEW.id
		loop
			SELECT INTO article_ a.id, a.methode_val, u.prix FROM yvs_base_articles a INNER JOIN yvs_base_conditionnement u on u.article = a.id WHERE a.id = cont_.article and u.id = cont_.conditionnement;
			prix_ = get_pr(cont_.article, NEW.depot, 0, cont_.date_ration, cont_.conditionnement);
			--Insertion mouvement stock
			SELECT INTO mouv_ id FROM yvs_base_mouvement_stock WHERE id_externe = cont_.id and table_externe = 'yvs_com_ration';
			if(mouv_ is not null)then
				if(article_.methode_val = 'FIFO') THEN
					DELETE FROM yvs_base_mouvement_stock WHERE id_externe = cont_.id and table_externe = 'yvs_com_ration';
					result_ = (select valorisation_stock(cont_.article, cont_.conditionnement, NEW.depot, doc_.tranche, cont_.quantite, prix_, 'yvs_com_ration', cont_.id, 'S', cont_.date_ration));
				else
					UPDATE yvs_base_mouvement_stock SET quantite = cont_.quantite, cout_entree = prix_, conditionnement = cont_.conditionnement, calcul_pr = cont_.calcul_pr, tranche = doc_.tranche WHERE id_externe = cont_.id and table_externe = 'yvs_com_ration';
				end if;
			else
				result_= (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot, doc_.tranche, cont_.quantite,prix_, 'yvs_com_ration', cont_.id, 'S', cont_.date_ration));
			end if;	
		end loop;
	elsif(NEW.statut != 'V')then
		for cont_ in select id from yvs_com_ration where doc_ration = OLD.id
		loop				
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_ration';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ration()
  OWNER TO postgres;

  
  
-- Function: update_doc_stocks()
-- DROP FUNCTION update_doc_stocks();
CREATE OR REPLACE FUNCTION update_doc_stocks()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	entree_ record;
	sortie_ record;
	trancheD_ bigint;
	trancheS_ bigint;
	result boolean default false;
BEGIN
	select into trancheD_ tranche from yvs_com_creneau_depot where id = NEW.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = NEW.creneau_source;	
	if(NEW.type_doc = 'FT' and (NEW.statut = 'U' or NEW.statut = 'V')) then
		FOR cont_ in select id, article , quantite as qte, quantite_entree, prix, prix_entree, conditionnement_entree, conditionnement, date_reception FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock				
			IF(NEW.source is not null and (NEW.statut = 'U' or NEW.statut = 'V')) THEN       --traitement de la sortie dans le dépôt source
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
				result = (select valorisation_stock(cont_.article, cont_.conditionnement, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
			END IF;
			if(NEW.destination is not null and NEW.statut = 'V')then  --traitement de l'entrée dans le dépôt de destination
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
				result = (select valorisation_stock(cont_.article, cont_.conditionnement_entree, NEW.destination, trancheD_, cont_.quantite_entree, cont_.prix_entree, 'yvs_com_contenu_doc_stock', cont_.id, 'E', cont_.date_reception));
			end if;
		end loop;
	ELSIF(NEW.type_doc = 'FT' and NEW.statut != 'V' and NEW.statut != 'U') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;		
	ELSIF(NEW.type_doc = 'SS' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				end if;
				result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
			end if;
		end loop;
	ELSIF(NEW.type_doc = 'SS' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock				
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'S'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	ELSIF(NEW.type_doc = 'ES' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
				end if;
				result = (select valorisation_stock(cont_.article,cont_.conditionnement_entree, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
			end if;
		end loop;
	ELSIF(NEW.type_doc = 'ES' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	ELSIF(NEW.type_doc = 'IN' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
				end if;
				if(cont_.qte>0)then
					result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				elsif(cont_.qte<0)then
					result = (select valorisation_stock(cont_.article, cont_.conditionnement_entree, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;
			end if;
		end loop;
	ELSIF(NEW.type_doc = 'IN' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	ELSIF(NEW.type_doc = 'TR' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite, quantite_entree as resultante, prix, prix_entree ,conditionnement as entree, conditionnement_entree as sortie, calcul_pr from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			select into entree_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.entree;
			select into sortie_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.sortie;
			
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
			if(mouv_ is not null)then
				if(entree_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
					result = (select valorisation_stock(cont_.article,cont_.entree, NEW.source, trancheS_, cont_.quantite, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
					result = (select valorisation_stock(cont_.article,cont_.sortie, NEW.source, trancheS_, cont_.resultante, cont_.prix_entree, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				else
					UPDATE yvs_base_mouvement_stock SET quantite = cont_.quantite, cout_entree = cont_.prix, conditionnement=cont_.sortie, calcul_pr=cont_.calcul_pr, date_doc=NEW.date_doc, tranche = trancheS_
													WHERE id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'E';
					UPDATE yvs_base_mouvement_stock SET quantite = cont_.resultante, cout_entree = cont_.prix_entree , conditionnement=cont_.entree, calcul_pr=cont_.calcul_pr, date_doc=NEW.date_doc, tranche = trancheS_
													WHERE id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				end if;
			else
				result = (select valorisation_stock(cont_.article,cont_.entree, NEW.source, trancheS_, cont_.quantite, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				result = (select valorisation_stock(cont_.article, cont_.sortie, NEW.source, trancheS_, cont_.resultante, cont_.prix_entree, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
			end if;	
		end loop;
	ELSIF(NEW.type_doc = 'TR' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_stocks()
  OWNER TO postgres;

  
  
-- Function: update_doc_ventes()
-- DROP FUNCTION update_doc_ventes();
CREATE OR REPLACE FUNCTION update_doc_ventes()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	dep_ record;
	result boolean default false;
BEGIN
	if(NEW.type_doc = 'BLV' or NEW.type_doc = 'BRV') then
		if(NEW.statut = 'V') then
			select into dep_ e.date_entete as date_doc from yvs_com_entete_doc_vente e where e.id = NEW.entete_doc;
			for cont_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix, conditionnement, calcul_pr from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
-- 				RAISE NOTICE 'cont_ : %',cont_.id; 
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
				
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and article = cont_.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article;
						if(NEW.type_doc = 'BLV')then
							result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', NEW.date_livraison));
						else
							result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', NEW.date_livraison));
						end if;
					else
						UPDATE yvs_base_mouvement_stock SET quantite = (cont_.qte), cout_entree = cont_.prix, conditionnement=cont_.conditionnement, calcul_pr=cont_.calcul_pr, tranche = NEW.tranche_livrer
														WHERE id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and article = cont_.article;
					end if;
				else
					if(NEW.type_doc = 'BLV')then
						result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', NEW.date_livraison));
					else
						result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', NEW.date_livraison));
					end if;
				end if;	
			end loop;
		elsif(NEW.statut != 'V')then
			for cont_ in select id from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
			end loop;
		end if;
	end if;
	if(NEW.type_doc = 'FV' AND NEW.statut_livre = 'L') then
		for cont_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix, id_reservation from yvs_com_contenu_doc_vente where doc_vente = NEW.id
		    LOOP
			--change le statut de la reservation
			IF(cont_.id_reservation IS NOT NULL) THEN
				UPDATE yvs_com_reservation_stock SET statut='T' WHERE id=cont_.id_reservation AND statut='V';
			END IF;
			
		    END LOOP;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ventes()
  OWNER TO postgres;

  
  
-- Function: update_ration()
-- DROP FUNCTION update_ration();
CREATE OR REPLACE FUNCTION update_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	article_ record;
	mouv_ bigint;
	tranche_ integer;
	result boolean default false;
	prix_ double precision;
BEGIN
	select into doc_ id, statut, depot from yvs_com_doc_ration where id = NEW.doc_ration;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		select into article_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id WHERE a.id = NEW.article and c.id = NEW.conditionnement;
		if(doc_.depot is not null)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
			SELECT INTO tranche_ cd.tranche FROM yvs_com_doc_ration d INNER JOIN yvs_com_creneau_depot cd ON cd.id=d.creneau_horaire WHERE d.id=NEW.doc_ration;
			if(mouv_ is not null)then
			prix_=get_pr(NEW.article, doc_.depot,0, NEW.date_ration, NEW.conditionnement);
				if(article_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
					result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, tranche_, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
				else
					UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = prix_, cout_stock=prix_, conditionnement = NEW.conditionnement, lot = NEW.lot, calcul_pr = NEW.calcul_pr, tranche=tranche_
													WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'S';
				end if;
			else
				result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, tranche_, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_ration()
  OWNER TO postgres;

  
-- Function: update_declaration_production()
-- DROP FUNCTION update_declaration_production();
CREATE OR REPLACE FUNCTION update_declaration_production()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
 	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where c.id = NEW.conditionnement;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				result = (select valorisation_stock(ordre_.article,NEW.conditionnement, NEW.depot, NEW.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_declaration));
			else
				RAISE NOTICE 'NEW.tranche : %',NEW.tranche;
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.cout_production, conditionnement = NEW.conditionnement, calcul_pr = NEW.calcul_pr, tranche = NEW.tranche where id = mouv_;
			end if;
		else
			result = (select valorisation_stock(ordre_.article,NEW.conditionnement, NEW.depot, NEW.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_declaration));
		end if;	
	elsif(NEW.statut != 'V')then
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_declaration_production()
  OWNER TO postgres;

  
  
-- Function: update_flux_composant()
-- DROP FUNCTION update_flux_composant();
CREATE OR REPLACE FUNCTION update_flux_composant()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	operation_ record;
	composant_ record;
	flux_ record;
	mouv_ bigint;
	arts_ record;
    conditionnement_ bigint;
	result boolean default false;
BEGIN
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	IF(operation_.statut_op = 'R' OR operation_.statut_op = 'T') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME LIMIT 1;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a where a.id = composant_.article;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				IF(flux_.sens = 'E')then
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, (NEW.quantite-NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
				ELSIF(flux.sens='S') THEN
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
				ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
				END IF;
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.cout, conditionnement = composant_.unite, calcul_pr = NEW.calcul_pr, tranche = NEW.tranche where id = mouv_;
			end if;
		else
			if(flux_.sens = 'E')then
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, (NEW.quantite-NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
			ELSIF(flux.sens='S') THEN
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));			
			ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
			END IF;
		end if;	
	ELSE
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	END IF;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_flux_composant()
  OWNER TO postgres;

  
  
-- Function: update_contenu_conditionnement()
-- DROP FUNCTION update_contenu_conditionnement();
CREATE OR REPLACE FUNCTION update_contenu_conditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	article_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, statut, depot, date_conditionnement from yvs_prod_fiche_conditionnement where id = NEW.fiche;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		select into article_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement;
		if(doc_.depot is not null)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_conditionnement));
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = article_.prix, conditionnement = NEW.conditionnement, calcul_pr = NEW.calcul_pr where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'E';
				end if;
			else
				result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_conditionnement));
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_conditionnement()
  OWNER TO postgres;

  
-- Function: update_fiche_conditionnement()
-- DROP FUNCTION update_fiche_conditionnement();
CREATE OR REPLACE FUNCTION update_fiche_conditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	depot_ bigint;
	article_ record;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		select into cont_ * from yvs_prod_nomenclature where id = NEW.nomenclature;
		select into article_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.unite_mesure;			
		--récupère le dépôt
		SELECT INTO depot_ d.depot FROM yvs_prod_conditionnement_declaration cd INNER JOIN yvs_prod_declaration_production d ON d.id=cd.declaration WHERE cd.conditionnement=NEW.id;
		IF(depot_ IS NULL) THEN
			depot_=NEW.depot;
		END IF;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = 'yvs_prod_fiche_conditionnement' and depot = depot_ and article = cont_.article;
		if(mouv_ is not null)then
			if(article_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = 'yvs_prod_fiche_conditionnement' and depot = depot_ and article = cont_.article;
				result = (select valorisation_stock(cont_.article, depot_, 0, NEW.quantite, article_.prix, 'yvs_prod_fiche_conditionnement', NEW.id, 'E', NEW.date_conditionnement));
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = entree_.prix where id_externe = NEW.id and table_externe = 'yvs_prod_fiche_conditionnement' and depot = depot_ and article = cont_.article and mouvement = 'E';
			end if;
		else
			result = (select valorisation_stock(cont_.article, depot_, 0, NEW.quantite, article_.prix, 'yvs_prod_fiche_conditionnement', NEW.id, 'E', NEW.date_conditionnement));
		end if;	
		
		for cont_ in select id, article , quantite, conditionnement, consommable, calcul_pr from yvs_prod_contenu_conditionnement where fiche = OLD.id
		loop
			select into article_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.conditionnement;			
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot =depot_ and article = cont_.article;
			if(mouv_ is not null)then
				if(article_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot = depot_ and article = cont_.article;
					if(cont_.consommable)then
						result = (select valorisation_stock(cont_.article, depot_, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'S', NEW.date_conditionnement));
					else
						result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'E', NEW.date_conditionnement));
					end if;
				else
					if(cont_.consommable)then
						update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = entree_.prix, calcul_pr = cont_.calcul_pr where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot = depot_ and article = cont_.article and mouvement = 'S';
					else
						update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = entree_.prix, calcul_pr = cont_.calcul_pr where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot = NEW.depot and article = cont_.article and mouvement = 'E';
					end if;
				end if;
			else
				if(cont_.consommable)then
					result = (select valorisation_stock(cont_.article, depot_, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'S', NEW.date_conditionnement));
				else
					result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'E', NEW.date_conditionnement));
				end if;
			end if;	
		end loop;
	elsif(NEW.statut != 'V')then
		delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = 'yvs_prod_fiche_conditionnement';
		for cont_ in select id from yvs_prod_contenu_conditionnement where fiche = OLD.id
		loop				
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_fiche_conditionnement()
  OWNER TO postgres;

  

-- Function: fusion_data_for_table(character varying, bigint, character varying)
-- DROP FUNCTION fusion_data_for_table(character varying, bigint, character varying);
CREATE OR REPLACE FUNCTION fusion_data_for_table(table_ character varying, new_value bigint, old_value character varying)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	result_ boolean default false;
BEGIN	
	if(table_ = 'yvs_base_conditionnement')then
		ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey;
	end if;
	result_ =  fusion_data_for_table_all(table_, new_value, old_value);
	if(table_ = 'yvs_base_conditionnement')then
		UPDATE yvs_base_mouvement_stock SET conditionnement = new_value WHERE conditionnement::character varying in (select val from regexp_split_to_table(old_value,',') val);
		ALTER TABLE yvs_base_mouvement_stock
		  ADD CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey FOREIGN KEY (conditionnement)
		      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
		      ON UPDATE CASCADE ON DELETE NO ACTION;
	elsif(table_ = 'yvs_base_tiers')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'TIERS';
	elsif(table_ = 'yvs_base_fournisseur')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'FOURNISSEUR';
	elsif(table_ = 'yvs_com_client')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'CLIENT';
	elsif(table_ = 'yvs_grh_employes')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'EMPLOYE';
	end if;
	return result_;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fusion_data_for_table(character varying, bigint, character varying)
  OWNER TO postgres;

  
  
-- Function: yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean)
-- DROP FUNCTION yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean);
CREATE OR REPLACE FUNCTION yvs_compta_content_journal(IN societe_ bigint, IN agences_ character varying, IN element_ bigint, IN table_ character varying, IN clear_ boolean)
  RETURNS TABLE(id bigint, jour integer, num_piece character varying, num_ref character varying, compte_general bigint, compte_tiers bigint, libelle character varying, debit double precision, credit double precision, echeance date, ref_externe bigint, table_externe character varying, statut character varying, error character varying, contenu bigint, centre bigint, coefficient double precision, numero integer, agence bigint, warning character varying, table_tiers character varying) AS
$BODY$
DECLARE
	_id_ BIGINT DEFAULT 0;
	_jour_ INTEGER DEFAULT 0; 
	_num_piece_ CHARACTER VARYING DEFAULT '';
	_num_ref_ CHARACTER VARYING DEFAULT ''; 
	_compte_general_ BIGINT; 
	_compte_tiers_ BIGINT; 
	_libelle_ CHARACTER VARYING DEFAULT ''; 
	_debit_ DOUBLE PRECISION DEFAULT 0; 
	_credit_ DOUBLE PRECISION DEFAULT 0; 
	_echeance_ DATE DEFAULT CURRENT_DATE; 
	_ref_externe_ BIGINT DEFAULT element_; 
	_table_externe_ CHARACTER VARYING DEFAULT table_; 
	_statut_ CHARACTER VARYING DEFAULT 'V';
	_error_ CHARACTER VARYING DEFAULT '';
	_warning_ CHARACTER VARYING DEFAULT '';
	_contenu_ BIGINT;
	_centre_ BIGINT;
	_coefficient_ DOUBLE PRECISION DEFAULT 0;
	_numero_ INTEGER DEFAULT 0;
	_agence_ BIGINT DEFAULT 0;
	_table_tiers_ CHARACTER VARYING DEFAULT '';

	ligne_ RECORD;
	sous_ RECORD;
	data_ RECORD;
	
	_compte_secondaire_ BIGINT; 
	
	taux_ DOUBLE PRECISION DEFAULT 100;
	total_ DOUBLE PRECISION DEFAULT 0; 
	somme_ DOUBLE PRECISION DEFAULT 0; 
	valeur_ DOUBLE PRECISION DEFAULT 0; 
	valeur_limite_arrondi_ DOUBLE PRECISION DEFAULT 0;
	
	i_ INTEGER DEFAULT 1;

	titre_ CHARACTER VARYING;
BEGIN    
	IF(clear_)THEN
		DROP TABLE IF EXISTS table_compta_content_journal;
		CREATE TEMP TABLE IF NOT EXISTS table_compta_content_journal(_id BIGINT, _jour INTEGER, _num_piece CHARACTER VARYING, _num_ref CHARACTER VARYING, _compte_general BIGINT, _compte_tiers BIGINT, _libelle CHARACTER VARYING, _debit DOUBLE PRECISION, _credit DOUBLE PRECISION, _echeance DATE, _ref_externe BIGINT, _table_externe CHARACTER VARYING, _statut CHARACTER VARYING, _error character varying, _contenu bigint, _centre bigint, _coefficient double precision, _numero integer, _agence bigint, _warning character varying, _table_tiers character varying);
		DELETE FROM table_compta_content_journal;
	END IF;
	IF(table_ IS NOT NULL AND table_ NOT IN ('', ' '))THEN
		SELECT INTO valeur_limite_arrondi_ valeur_limite_arrondi FROM yvs_compta_parametre WHERE societe = societe_;
		valeur_limite_arrondi_ = COALESCE(valeur_limite_arrondi_, 0);
		IF(table_ = 'DOC_VENTE' OR table_ = 'AVOIR_VENTE')THEN	
			SELECT INTO data_ d.num_doc, d.nom_client, d.tiers, d.client, e.date_entete, c.compte, p.saisie_compte_tiers, u.agence FROM yvs_com_doc_ventes d LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_client c ON d.client = c.id 
				 LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_base_plan_comptable p ON c.compte = p.id WHERE d.id = element_;
			IF(data_.num_doc IS NOT NULL AND data_.num_doc NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				total_ = (SELECT get_ttc_vente(element_)); -- RECUPERATION DU TTC DE LA FACTURE
				IF(total_ IS NOT NULL AND total_ > 0)THEN	
					-- COMPTABILISATION DU TTC DE LA FACTURE
					IF(data_.client IS NULL OR data_.client < 1)THEN
						_error_ = 'cette facture de vente n''est pas rattachée à un client';
					ELSIF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette facture de vente n''est pas rattachée à un compte tiers';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette facture de vente est rattachée à un client qui n''a pas de compte collectif';
					END IF;
					_jour_ = to_char(data_.date_entete ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_entete;
					_compte_general_ = data_.compte;
					IF(data_.saisie_compte_tiers)THEN
						_compte_tiers_ = data_.tiers;
					ELSE
						_compte_tiers_ = null;
					END IF;
					_numero_ = 1;
					FOR ligne_ IN SELECT t.* FROM yvs_base_tranche_reglement t INNER JOIN yvs_base_model_reglement m ON t.model = m.id INNER JOIN yvs_com_doc_ventes d ON d.model_reglement = m.id WHERE d.id = element_
					LOOP
						_debit_ = 0;
						_credit_ = 0;
						IF(table_ = 'DOC_VENTE')THEN
							_debit_ = (total_ * ligne_.taux / 100);
							somme_ = somme_ + _debit_;
							_libelle_ = UPPER('Echéancier '||ligne_.taux||'% pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);
						ELSE	
							_credit_ = (total_ * ligne_.taux / 100);
							somme_ = somme_ + _credit_;
							_libelle_ = UPPER('Echéancier '||ligne_.taux||'% pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;
					IF(total_ > somme_)THEN
						_debit_ = 0;
						_credit_ = 0;
						IF(somme_ > 0)THEN
							taux_ = (((total_ - somme_) / total_) * 100);
						END IF;
						IF(table_ = 'DOC_VENTE')THEN
							_debit_ = (total_ - somme_);
							_libelle_ = UPPER('Echéancier '||taux_||'% pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);
						ELSE	
							_credit_ = (total_ - somme_);
							_libelle_ = UPPER('Echéancier '||taux_||'% pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
					_error_ = null;
					-- COMPTABILISATION DES ARTICLES
					SELECT INTO _compte_secondaire_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = 'RISTOURNE_VENTE'; -- COMPTE DE RISTOURNE SUR VENTE
					DROP TABLE IF EXISTS table_montant_compte_contenu; -- REGROUPEMENT DES MONTANT PAR COMPTES
					CREATE TEMP TABLE IF NOT EXISTS table_montant_compte_contenu(_compte BIGINT, _montant DOUBLE PRECISION, _ristourne DOUBLE PRECISION);
					FOR ligne_ IN SELECT co.prix_total, co.ristourne, (SELECT ca.compte FROM yvs_base_article_categorie_comptable ca WHERE ca.article = co.article AND ca.categorie = dv.categorie_comptable LIMIT 1) AS compte
						FROM yvs_com_contenu_doc_vente co INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente WHERE dv.id = element_
					LOOP
						SELECT INTO sous_ y.* FROM table_montant_compte_contenu y WHERE y._compte = ligne_.compte;
						IF(sous_._compte IS NOT NULL AND sous_._compte > 0)THEN
							UPDATE table_montant_compte_contenu SET _montant = _montant + ligne_.prix_total,  _ristourne = _ristourne + ligne_.ristourne WHERE _compte = ligne_.compte;
						ELSE
							INSERT INTO table_montant_compte_contenu VALUES (ligne_.compte, ligne_.prix_total, COALESCE(ligne_.ristourne, 0));
						END IF;
					END LOOP;
					FOR ligne_ IN SELECT SUM(_montant) AS total, SUM(_ristourne) AS ristourne, ca._compte AS compte, pc.intitule, pc.saisie_compte_tiers FROM table_montant_compte_contenu ca LEFT JOIN yvs_base_plan_comptable pc ON ca._compte = pc.id GROUP BY ca._compte, pc.id
					LOOP
						_debit_ = 0;					
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _credit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_vente tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_vente tc INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente 
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE ca.compte = ligne_.compte AND dv.id = element_);
						IF(table_ = 'DOC_VENTE')THEN
							_credit_ = ligne_.total - COALESCE(_credit_, 0);
							_libelle_ = UPPER(ligne_.intitule||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);	
							IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
								_credit_ = _credit_ - ligne_.ristourne;
							END IF;
						ELSE	
							_debit_ = ligne_.total - COALESCE(_credit_, 0);
							_libelle_ = UPPER(ligne_.intitule||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);	
							_credit_ = 0;		
							IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
								_debit_ = _debit_ - ligne_.ristourne;
							END IF;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						IF(ligne_.saisie_compte_tiers)THEN
							_compte_tiers_ = data_.tiers;
						ELSE
							_compte_tiers_ = null;
						END IF;					
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;

						IF(COALESCE(_compte_secondaire_, 0) > 0 and COALESCE(ligne_.ristourne, 0) > 0)THEN
							IF(table_ = 'DOC_VENTE')THEN
								_credit_ = ligne_.ristourne;
								_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
							ELSE	
								_debit_ = ligne_.ristourne;
								_libelle_ = UPPER('Ristourne restituée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							_compte_general_ = _compte_secondaire_;
							_compte_tiers_ = null;		
							_id_ = _id_ - 1;
							
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES TAXES
					_numero_ = 2;
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation, pc.saisie_compte_tiers FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_vente tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						LEFT JOIN yvs_base_plan_comptable pc ON tx.compte = pc.id WHERE dv.id = element_ GROUP BY tx.compte, tx.designation, pc.id 
					LOOP
						IF(ligne_.total != 0)THEN
							_debit_ = 0;
							_credit_ = 0;
							IF(table_ = 'DOC_VENTE')THEN
								_credit_ = ligne_.total;
								_libelle_ = ligne_.designation||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;	
							ELSE	
								_credit_ = ligne_.total;
								_libelle_ = ligne_.designation||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client;	
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;					
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation, pc.saisie_compte_tiers FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_vente co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						LEFT JOIN yvs_base_plan_comptable pc ON tx.compte = pc.id WHERE dv.id = element_ GROUP BY tx.id, tx.libelle, pc.id 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_VENTE')THEN
								IF(ligne_.augmentation)THEN
									_credit_ = ligne_.total;
								ELSE
									_debit_ = ligne_.total;
								END IF;
								_libelle_ = ligne_.libelle||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;
							ELSE	
								IF(ligne_.augmentation)THEN
									_debit_ = ligne_.total;
								ELSE
									_credit_ = ligne_.total;
								END IF;
								_libelle_ = ligne_.libelle||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							IF(ligne_.saisie_compte_tiers)THEN
								_compte_tiers_ = data_.tiers;
							ELSE
								_compte_tiers_ = null;
							END IF;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'JOURNAL_VENTE')THEN
			SELECT INTO data_ u.code_users, e.date_entete, p.code AS point, u.agence FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id 
				LEFT JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id LEFT JOIN yvs_base_point_vente p ON cp.point = p.id 
				WHERE e.id = element_;
			IF(data_.code_users IS NOT NULL AND data_.code_users NOT IN ('', ' '))THEN		
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;		
				total_ = (SELECT get_ttc_entete_vente(element_)); -- RECUPERATION DU TTC DE LA JOURNEE DE VENTE
				IF(total_ IS NOT NULL AND total_ > 0)THEN	
					-- COMPTABILISATION DU TTC DE LA JOURNEE DE VENTE
					_jour_ = to_char(data_.date_entete ,'dd')::integer;
					_num_piece_ = data_.code_users || '/';
					IF(data_.point IS NOT NULL AND data_.point NOT IN ('', ' '))THEN
						_num_piece_ = _num_piece_ || data_.point || '/';
					ELSE
						_error_ = 'ce journal de vente n'' pas rattaché à un point de vente';
					END IF;
					_numero_ = 1;
					_num_piece_ = _num_piece_ || to_char(data_.date_entete ,'ddMMyy');	
					_echeance_ = data_.date_entete;
					FOR ligne_ IN SELECT SUM(get_ttc_vente(dv.id)) as total , ts.compte, dv.tiers, CONCAT(ts.nom, ' ', ts.prenom) AS nom, pc.saisie_compte_tiers FROM yvs_com_doc_ventes dv INNER JOIN yvs_com_client cl ON dv.client = cl.id 
						INNER JOIN yvs_com_client ts ON dv.tiers = ts.id LEFT JOIN yvs_base_plan_comptable pc ON ts.compte = pc.id WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_
						AND dv.id NOT IN (SELECT _f.facture FROM yvs_compta_content_journal_facture_vente _f INNER JOIN yvs_compta_pieces_comptable _p ON _f.piece = _p.id INNER JOIN yvs_compta_journaux _j ON _p.journal = _j.id
									INNER JOIN yvs_agences _a ON _j.agence = _a.id WHERE _a.societe = societe_)
						GROUP BY dv.entete_doc, ts.compte, dv.tiers, ts.id, pc.id
					LOOP
						_debit_ = ligne_.total;
						_credit_ = 0;
						somme_ = somme_ + _debit_;
						_libelle_ = 'Echéancier 100% pour le journal de vente N° '||_num_piece_;
						IF(i_ < 10)THEN
							_num_ref_ = _num_piece_ ||'-0'||i_;
						ELSE
							_num_ref_ = _num_piece_ ||'-'||i_;
						END IF;
						_compte_general_ = ligne_.compte;
						IF(ligne_.saisie_compte_tiers)THEN
							_compte_tiers_ = ligne_.tiers;
						ELSE
							_compte_tiers_ = null;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES ARTICLES
					DROP TABLE IF EXISTS table_montant_compte_contenu; -- REGROUPEMENT DES MONTANT PAR COMPTES
					SELECT INTO _compte_secondaire_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = 'RISTOURNE_VENTE'; -- COMPTE DE RISTOURNE SUR VENTE
					CREATE TEMP TABLE IF NOT EXISTS table_montant_compte_contenu(_compte BIGINT, _montant DOUBLE PRECISION, _ristourne DOUBLE PRECISION);
					FOR ligne_ IN SELECT co.prix_total, co.ristourne, (SELECT ca.compte FROM yvs_base_article_categorie_comptable ca WHERE ca.article = co.article AND ca.categorie = dv.categorie_comptable LIMIT 1) AS compte
						FROM yvs_com_contenu_doc_vente co INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_
						AND dv.id NOT IN (SELECT _f.facture FROM yvs_compta_content_journal_facture_vente _f INNER JOIN yvs_compta_pieces_comptable _p ON _f.piece = _p.id INNER JOIN yvs_compta_journaux _j ON _p.journal = _j.id
									INNER JOIN yvs_agences _a ON _j.agence = _a.id WHERE _a.societe = societe_)
					LOOP
						SELECT INTO sous_ y.* FROM table_montant_compte_contenu y WHERE y._compte = ligne_.compte;
						IF(sous_._compte IS NOT NULL AND sous_._compte > 0)THEN
							UPDATE table_montant_compte_contenu SET _montant = _montant + ligne_.prix_total, _ristourne = _ristourne + ligne_.ristourne WHERE _compte = ligne_.compte;
						ELSE
							INSERT INTO table_montant_compte_contenu VALUES (ligne_.compte, ligne_.prix_total, ligne_.ristourne);
						END IF;
					END LOOP;
					_numero_ = 2;
					FOR ligne_ IN SELECT SUM(_montant) AS total, SUM(_ristourne) AS ristourne, ca._compte AS compte, pc.intitule, pc.saisie_compte_tiers FROM table_montant_compte_contenu ca LEFT JOIN yvs_base_plan_comptable pc ON ca._compte = pc.id GROUP BY ca._compte, pc.id
					LOOP
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _credit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_vente tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_vente tc INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente 
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND ca.compte = ligne_.compte AND dv.entete_doc = element_
								AND dv.id NOT IN (SELECT _f.facture FROM yvs_compta_content_journal_facture_vente _f INNER JOIN yvs_compta_pieces_comptable _p ON _f.piece = _p.id INNER JOIN yvs_compta_journaux _j ON _p.journal = _j.id
									INNER JOIN yvs_agences _a ON _j.agence = _a.id WHERE _a.societe = societe_));
						_credit_ = ligne_.total - COALESCE(_credit_, 0);
						IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
							_credit_ = _credit_ - ligne_.ristourne;
						END IF;
						_debit_ = 0;
						IF(i_ < 10)THEN
							_num_ref_ = _num_piece_ ||'-0'||i_;
						ELSE
							_num_ref_ = _num_piece_ ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;
						_libelle_ = ligne_.intitule||' pour le journal de vente N° '||_num_piece_;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;

						IF(COALESCE(_compte_secondaire_, 0) > 0 and COALESCE(ligne_.ristourne, 0) > 0)THEN
							_credit_ = ligne_.ristourne;
							_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||_num_piece_);
							IF(i_ < 10)THEN
								_num_ref_ = _num_piece_ ||'-0'||i_;
							ELSE
								_num_ref_ = _num_piece_ ||'-'||i_;
							END IF;
							_compte_general_ = _compte_secondaire_;
							_compte_tiers_ = null;		
							_id_ = _id_ - 1;
							
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT SUM(tc.montant) AS total, tx.compte, tx.designation FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_vente tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id 
						INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_
						AND dv.id NOT IN (SELECT _f.facture FROM yvs_compta_content_journal_facture_vente _f INNER JOIN yvs_compta_pieces_comptable _p ON _f.piece = _p.id INNER JOIN yvs_compta_journaux _j ON _p.journal = _j.id
									INNER JOIN yvs_agences _a ON _j.agence = _a.id WHERE _a.societe = societe_)
						GROUP BY tx.compte, tx.designation 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = ligne_.total;
							_debit_ = 0;
							IF(i_ < 10)THEN
								_num_ref_ = _num_piece_ ||'-0'||i_;
							ELSE
								_num_ref_ = _num_piece_ ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.designation||' pour le journal de vente N° '||_num_piece_;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT SUM(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_vente co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_ GROUP BY tx.id, tx.libelle 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(ligne_.augmentation)THEN
								_credit_ = ligne_.total;
								_numero_ = 2;
							ELSE
								_debit_ = ligne_.total;
								_numero_ = 1;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = _num_piece_ ||'-0'||i_;
							ELSE
								_num_ref_ = _num_piece_ ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.libelle||' pour le journal de vente N° '||_num_piece_;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'DOC_ACHAT' OR table_ = 'AVOIR_ACHAT')THEN
			SELECT INTO data_ d.num_doc, d.date_doc, d.fournisseur, CONCAT(c.nom, ' ', c.prenom) AS nom, c.compte, d.fournisseur as tiers, pc.saisie_compte_tiers, d.agence, COALESCE(d.description, '') AS description FROM yvs_com_doc_achats d LEFT JOIN yvs_base_fournisseur c ON d.fournisseur = c.id LEFT JOIN yvs_base_plan_comptable pc ON c.compte = pc.id WHERE d.id = element_;
			IF(data_.num_doc IS NOT NULL AND data_.num_doc NOT IN ('', ' '))THEN		
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;			
				total_ = (SELECT get_ttc_achat(element_)); -- RECUPERATION DU TTC DE LA FACTURE
				IF(total_ IS NOT NULL AND total_ > 0)THEN	
					-- COMPTABILISATION DU TTC DE LA FACTURE
					IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
						_error_ = 'cette facture d''achat n''est pas rattachée à un fournisseur';
					ELSIF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette facture d''achat est rattachée à un fournisseur qui n''a pas de compte tiers';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette facture d''achat est rattachée à un fournisseur qui n''a pas de compte collectif';
					END IF;
					_jour_ = to_char(data_.date_doc ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_doc;
					_compte_general_ = data_.compte;
					_libelle_ = data_.description;
					IF(data_.saisie_compte_tiers)THEN
						_compte_tiers_ = data_.tiers;
					ELSE
						_compte_tiers_ = null;
					END IF;
					_numero_ = 2;
					FOR ligne_ IN SELECT t.* FROM yvs_base_tranche_reglement t INNER JOIN yvs_base_model_reglement m ON t.model = m.id INNER JOIN yvs_com_doc_achats d ON d.model_reglement = m.id WHERE d.id = element_
					LOOP
						_debit_ = 0;
						_credit_ = 0;
						IF(table_ = 'DOC_ACHAT')THEN
							_credit_ = (SELECT arrondi(societe_, (total_ * ligne_.taux / 100)));
							somme_ = somme_ + _credit_;
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||ligne_.taux||'% pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE	
							_debit_ = (SELECT arrondi(societe_, (total_ * ligne_.taux / 100)));
							somme_ = somme_ + _debit_;
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||ligne_.taux||'% pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;
					IF(total_ > somme_)THEN
						_debit_ = 0;
						_credit_ = 0;
						IF(somme_ > 0)THEN
							taux_ = (((total_ - somme_) / total_) * 100);
						END IF;
						IF(table_ = 'DOC_ACHAT')THEN
							_credit_ = (SELECT arrondi(societe_, (total_ - somme_)));
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||taux_||'% pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE	
							_debit_ = (SELECT arrondi(societe_, (total_ - somme_)));
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||taux_||'% pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;

					-- COMPTABILISATION DES ARTICLES
					DROP TABLE IF EXISTS table_montant_compte_contenu; -- REGROUPEMENT DES MONTANT PAR COMPTES
					CREATE TEMP TABLE IF NOT EXISTS table_montant_compte_contenu(_compte BIGINT, _montant DOUBLE PRECISION);
					FOR ligne_ IN SELECT co.prix_total, (SELECT ca.compte FROM yvs_base_article_categorie_comptable ca WHERE ca.article = co.article AND ca.categorie = dv.categorie_comptable LIMIT 1) AS compte
						FROM yvs_com_contenu_doc_achat co INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat WHERE dv.id = element_
					LOOP
						SELECT INTO sous_ y.* FROM table_montant_compte_contenu y WHERE y._compte = ligne_.compte;
						IF(sous_._compte IS NOT NULL AND sous_._compte > 0)THEN
							UPDATE table_montant_compte_contenu SET _montant = _montant + ligne_.prix_total WHERE _compte = ligne_.compte;
						ELSE
							INSERT INTO table_montant_compte_contenu VALUES (ligne_.compte, ligne_.prix_total);
						END IF;
					END LOOP;
					_numero_ = 1;
					FOR ligne_ IN SELECT SUM(_montant) AS total, ca._compte AS compte, pc.intitule, pc.saisie_analytique FROM table_montant_compte_contenu ca LEFT JOIN yvs_base_plan_comptable pc ON ca._compte = pc.id GROUP BY ca._compte, pc.id
					LOOP
						_credit_ = 0;
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _debit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_achat tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_achat tc INNER JOIN yvs_com_contenu_doc_achat co ON tc.contenu = co.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE ca.compte = ligne_.compte AND dv.id = element_);
						IF(table_ = 'DOC_ACHAT')THEN
							_debit_ = ligne_.total - COALESCE(_debit_, 0);
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = ligne_.intitule||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE
							_credit_ = ligne_.total - COALESCE(_debit_, 0);
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = ligne_.intitule||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
							_debit_ = 0;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
						IF(table_ = 'DOC_ACHAT')THEN
							valeur_ = _debit_;
						ELSE
							valeur_ = _credit_;
						END IF;
						IF(ligne_.saisie_analytique IS TRUE AND (valeur_ IS NOT NULL AND valeur_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
							_contenu_ = _id_;
							RAISE NOTICE 'valeur_ : %',valeur_;
							RAISE NOTICE '_compte_general_ : %',_compte_general_;
							FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, 0, 0, _compte_general_, null, valeur_, '', table_, FALSE, FALSE)
							LOOP
								_error_ = sous_.error;
								_centre_ = sous_.centre;
								_coefficient_ = sous_.coefficient;
								IF(table_ = 'DOC_ACHAT')THEN
									_debit_ = sous_.valeur;
								ELSE
									_credit_ = sous_.valeur;
								END IF;
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
							END LOOP;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_achat tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_achat co ON tc.contenu = co.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
						WHERE dv.id = element_ GROUP BY tx.compte, tx.designation 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_ACHAT')THEN
								_debit_ = ligne_.total;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.designation||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							ELSE
								_credit_ = ligne_.total;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.designation||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_achat co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
						WHERE dv.id = element_ GROUP BY tx.id, tx.libelle 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_ACHAT')THEN
								IF(ligne_.augmentation IS TRUE)THEN
									_debit_ = ligne_.total;
									_numero_ = 1;
								ELSE
									_credit_ = ligne_.total;
									_numero_ = 2;
								END IF;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.libelle||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							ELSE
								IF(ligne_.augmentation IS TRUE)THEN
									_credit_ = ligne_.total;
									_numero_ = 2;
								ELSE
									_debit_ = ligne_.total;
									_numero_ = 1;
								END IF;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.libelle||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'FRAIS_MISSION')THEN
			SELECT INTO data_ f.id, f.numero_piece, f.date_paiement, f.montant, c.journal, c.compte, o.compte_charge, m.ordre, m.objet_mission, m.numero_mission, e.agence, p.saisie_analytique, f.caisse FROM yvs_compta_caisse_piece_mission f LEFT JOIN yvs_grh_missions m ON f.mission = m.id 
				LEFT JOIN yvs_grh_employes e ON m.employe = e.id LEFT JOIN yvs_grh_objets_mission o ON m.objet_mission = o.id LEFT JOIN yvs_base_plan_comptable p ON o.compte_charge = p.id LEFT JOIN yvs_base_caisse c ON f.caisse = c.id WHERE f.id = element_;	
			_table_tiers_ = 'EMPLOYE';	
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero_mission;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.ordre;
				_compte_tiers_ = null;
				
				IF(data_.objet_mission IS NULL OR data_.objet_mission < 1)THEN
					_error_ = 'ce reglement est lié à une mission qui n''a pas d''objet de mission';
				ELSIF(data_.compte_charge IS NULL OR data_.compte_charge < 1)THEN
					_error_ = 'ce reglement est lié à une mission dont l''objet de mission n''a pas de compte de charge';
				END IF;
				_credit_ = 0;
				_debit_ = data_.montant;
				_numero_ = 1;
				_compte_general_ = data_.compte_charge;	
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
				valeur_ = _debit_;
				IF(data_.saisie_analytique IS TRUE AND (valeur_ IS NOT NULL AND valeur_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
					_contenu_ = _id_;
					FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, 0, 0, _compte_general_, null, valeur_, '', table_, FALSE, FALSE)
					LOOP
						_error_ = sous_.error;
						_centre_ = sous_.centre;
						_coefficient_ = sous_.coefficient;
						_debit_ = sous_.valeur;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
					END LOOP;
				END IF;

				_error_ = null;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est pas lié à une caisse';
				ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'ce reglement est lié à une caisse qui n''a pas de compte';
				END IF;
				_credit_ = data_.montant;
				_numero_ = 2;
				_debit_ = 0;
				_compte_general_ = data_.compte;	
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, y.caisse, p.vente, d.num_doc, d.client, t.compte as compte_tiers, t.suivi_comptable, d.tiers, m.compte_tiers AS tiers_interne, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, u.agence,
				r.libelle, r.mode_reglement, r.code_comptable, c.intitule, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.numero_piece) AS reference_externe FROM yvs_compta_phase_piece y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_vente p ON y.piece_vente = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_doc_ventes d ON p.vente = d.id LEFT JOIN yvs_com_client t ON d.client = t.id LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
				LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_grh_employes m ON u.id = m.code_users WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.vente IS NULL OR data_.vente < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune facture';
				ELSE
					_libelle_ = _libelle_|| ' du client '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece p ON p.phase_reg = r.id WHERE p.piece_vente = data_.id;
				RAISE NOTICE '%',data_.numero_phase;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSE
					IF(data_.numero_phase = ligne_.max)THEN
						IF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;					
					ELSE
						IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
							_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
						ELSE
							_compte_general_ = data_.compte_caisse;
						END IF;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.vente IS NOT NULL AND data_.vente > 0)THEN
						IF(data_.client IS NULL OR data_.client < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de client';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_vente v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_vente p ON y.piece_vente = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece _y INNER JOIN yvs_compta_caisse_piece_vente _p ON _y.piece_vente = _p.id INNER JOIN yvs_compta_phase_piece _y0 ON _y0.piece_vente = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.suivi_comptable)THEN
						IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
							_error_ = 'car ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte tiers';
						ELSE
							_compte_tiers_ = data_.tiers;
						END IF;
					ELSE
						IF(data_.tiers_interne IS NULL OR data_.tiers_interne < 1)THEN
							_error_ = 'car ce reglement est rattaché à une facture qui est liée à un vendeur qui n''a pas de compte tiers';
						ELSE
							_compte_tiers_ = data_.tiers_interne;
						END IF;
					END IF;		
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, y.caisse, p.achat, d.num_doc, d.fournisseur, t.compte as compte_tiers, d.fournisseur as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, d.agence,
				r.libelle, r.mode_reglement, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.numero_piece) AS reference_externe FROM yvs_compta_phase_piece_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_doc_achats d ON p.achat = d.id LEFT JOIN yvs_base_fournisseur t ON d.fournisseur = t.id WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.achat IS NULL OR data_.achat < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune facture';
				ELSE
					_libelle_ = _libelle_||' du fournisseur '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece_achat p ON p.phase_reg = r.id WHERE p.piece_achat = data_.id;
				RAISE NOTICE 'data_.numero_phase %',data_.numero_phase;
				RAISE NOTICE 'ligne_.max %',ligne_.max;
				RAISE NOTICE 'ligne_.min %',ligne_.min;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSE
					IF(data_.numero_phase = ligne_.min)THEN
						IF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;					
					ELSE
						IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
							_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
						ELSE
							_compte_general_ = data_.compte_caisse;
						END IF;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 2;
					_credit_ = data_.montant;	
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.achat IS NOT NULL AND data_.achat > 0)THEN
						IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de fournisseur';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_achat v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece_achat _y INNER JOIN yvs_compta_caisse_piece_achat _p ON _y.piece_achat = _p.id INNER JOIN yvs_compta_phase_piece_achat _y0 ON _y0.piece_achat = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'car ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;	
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE			
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN
			SELECT INTO data_ p.id, p.num_piece as numero_piece, p.date_valider as date_paiement, p.montant, y.caisse, p.doc_divers, d.num_piece as num_doc, d.id_tiers as tiers, name_tiers(d.id_tiers, d.table_tiers, 'C')::integer as compte_tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, d.table_tiers, d.agence,
				r.libelle, r.mode_reglement, y.statut, d.mouvement, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, name_tiers(d.id_tiers, d.table_tiers, 'N') AS nom_prenom,
				COALESCE(p.reference_externe, p.num_piece) AS reference_externe FROM yvs_compta_phase_piece_divers y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_divers p ON y.piece_divers = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_compta_caisse_doc_divers d ON p.doc_divers = d.id WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = data_.table_tiers;		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.doc_divers IS NULL OR data_.doc_divers < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune operation diverse';
				ELSIF(data_.nom_prenom NOT IN ('', ' '))THEN
					_libelle_ = _libelle_||' du tiers '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min, 0 as tmp FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece_divers p ON p.phase_reg = r.id WHERE p.piece_divers = data_.id;
				IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
					ligne_.tmp = ligne_.max;
					ligne_.max = ligne_.min;
					ligne_.min = ligne_.tmp;
				END IF;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune caisse';
					ELSIF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 2;
						_credit_ = data_.montant;
					ELSE
						_numero_ = 1;
						_debit_ = data_.montant;
					END IF;
				ELSE		
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 1;
						_debit_ = data_.montant;
					ELSE
						_numero_ = 2;
						_credit_ = data_.montant;
					END IF;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.doc_divers IS NOT NULL AND data_.doc_divers > 0)THEN
						IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
							_error_ = 'ce reglement est rattaché à une operation diverse d''un tiers qui n''a pas de compte collectif';
						ELSE
							_compte_general_ = data_.compte_tiers;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece_divers y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_divers v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_divers p ON y.piece_divers = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece_divers _y INNER JOIN yvs_compta_caisse_piece_divers _p ON _y.piece_divers = _p.id INNER JOIN yvs_compta_phase_piece_divers _y0 ON _y0.piece_divers = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'ce reglement est rattaché à une operation diverse qui n''a pas de tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;	
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 1;
						_debit_ = data_.montant;
					ELSE
						_numero_ = 2;
						_credit_ = data_.montant;
					END IF;
				ELSE		
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 2;
						_credit_ = data_.montant;
					ELSE
						_numero_ = 1;
						_debit_ = data_.montant;
					END IF;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN
			SELECT INTO data_ p.id, p.num_refrence, p.date_paiement, p.montant, y.caisse, t.compte as compte_tiers, p.fournisseur as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, y.piece_achat, m.societe, j.agence, 
				r.libelle, r.mode_reglement, r.code_comptable, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.num_refrence) AS reference_externe FROM yvs_compta_phase_acompte_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_acompte_fournisseur p ON y.piece_achat = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_base_fournisseur t ON p.fournisseur = t.id LEFT JOIN yvs_base_mode_reglement m ON p.model = m.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE y.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_refrence;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.piece_achat IS NULL OR data_.piece_achat < 1)THEN
					_error_ = 'cette phase n''est rattachée a aucun acompte';
				ELSE
					_libelle_ = _libelle_||' du fournisseur '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_acompte_achat p ON p.phase_reg = r.id WHERE p.piece_achat = data_.id;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE			
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.max)THEN
					SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
					IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
						_error_ = 'Le compte des acomptes fournisseur n''est pas paramètré';
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_acompte_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_acompte_achat v ON v.etape = y.id INNER JOIN yvs_compta_acompte_fournisseur p ON y.piece_achat = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_acompte_achat _y INNER JOIN yvs_compta_acompte_fournisseur _p ON _y.piece_achat = _p.id INNER JOIN yvs_compta_phase_acompte_achat _y0 ON _y0.piece_achat = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;			
				END IF;	
				IF(data_.reglement_ok)THEN
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette phase est liée a un acompte fournisseur qui n'' pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;			
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN
			SELECT INTO data_ p.id, p.num_refrence, p.date_paiement, p.montant, y.caisse, t.compte as compte_tiers, p.client as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, y.piece_vente, m.societe, j.agence,
				r.libelle, r.mode_reglement, r.code_comptable, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.num_refrence) AS reference_externe FROM yvs_compta_phase_acompte_vente y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_acompte_client p ON y.piece_vente = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_client t ON p.client = t.id LEFT JOIN yvs_base_mode_reglement m ON p.model = m.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE y.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_refrence;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.piece_vente IS NULL OR data_.piece_vente < 1)THEN
					_error_ = 'cette phase n''est rattachée a aucun acompte';
				ELSE
					_libelle_ = _libelle_||' du client '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_acompte_vente p ON p.phase_reg = r.id WHERE p.piece_vente = data_.id;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
					IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
						_error_ = 'Le compte des acomptes client n''est pas paramètré';
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_acompte_vente y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_acompte_vente v ON v.etape = y.id INNER JOIN yvs_compta_acompte_client p ON y.piece_vente = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_acompte_vente _y INNER JOIN yvs_compta_acompte_client _p ON _y.piece_vente = _p.id INNER JOIN yvs_compta_phase_acompte_vente _y0 ON _y0.piece_vente = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;			
				END IF;	
				IF(data_.reglement_ok)THEN
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette phase est liée a un acompte client qui n'' pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;			
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_VENTE')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, p.caisse, p.vente, d.num_doc, d.client, t.compte as compte_tiers, t.suivi_comptable, d.tiers, m.compte_tiers AS tiers_interne, c.compte, mo.type_reglement, mo.societe, u.agence
				FROM yvs_compta_caisse_piece_vente p LEFT JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_base_mode_reglement mo ON p.model = mo.id
				LEFT JOIN yvs_com_doc_ventes d ON p.vente = d.id LEFT JOIN yvs_com_client t ON d.client = t.id LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
				LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_grh_employes m ON u.id = m.code_users WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' ') AND data_.type_reglement != 'SALAIRE')THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece p WHERE p.piece_vente = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_vente = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_VENTE', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_paiement ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_paiement;
					_libelle_ = 'Reglement Facture vente N° ';		
					_compte_tiers_ = null;	
					IF(data_.vente IS NULL OR data_.vente < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune facture';
					ELSE
						_libelle_ = _libelle_||data_.num_doc;
					END IF;
					IF(data_.type_reglement = 'COMPENSATION')THEN					
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'COMPENSATION';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des attentes pour compensation n''est pas paramètré';
						END IF;
					ELSE				
						SELECT INTO ligne_ COUNT(n.id) as count FROM yvs_compta_notif_reglement_vente n WHERE n.piece_vente = element_;
						IF(ligne_.count IS NOT NULL AND ligne_.count > 0)THEN		
							SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
							IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
								_error_ = 'Le compte des acomptes client n''est paramètré';
							END IF;		
							IF(data_.suivi_comptable)THEN	
								_compte_tiers_ = data_.tiers;
							ELSE
								_compte_tiers_ = data_.tiers_interne;	
							END IF;			
						ELSE
							IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
								_error_ = 'ce reglement n''est associé à aucune caisse';
							ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
								_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte;
							END IF;
						END IF;
					END IF;
					_numero_ = 1;			
					_credit_ = 0;
					_debit_ = data_.montant;	
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
						
					_error_ = '';
					IF(data_.vente IS NOT NULL AND data_.vente > 0)THEN
						IF(data_.client IS NULL OR data_.client < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de client';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
							IF(data_.suivi_comptable)THEN
								IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
									_error_ = 'car ce reglement est rattaché à une facture qui n''a pas de compte tiers';
								ELSE
									_compte_tiers_ = data_.tiers;
								END IF;
							ELSE
								IF(data_.tiers_interne IS NULL OR data_.tiers_interne < 1)THEN
									_error_ = 'car ce reglement est rattaché à une facture qui est liée à un vendeur qui n''a pas de compte tiers';
								ELSE
									_compte_tiers_ = data_.tiers_interne;
								END IF;
							END IF;
						END IF;
					ELSE
						_error_ = 'ce reglement n''est associé à aucune facture';
					END IF;
					_ref_externe_ = data_.id;
					_table_externe_ = 'CAISSE_VENTE';
					_numero_ = 2;
					_credit_ = data_.montant;
					_debit_ = 0;	
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CAISSE_ACHAT')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, p.caisse, p.achat, d.num_doc, d.fournisseur, t.compte AS compte_tiers, d.fournisseur as tiers, c.compte, mo.type_reglement, mo.societe, d.agence FROM yvs_compta_caisse_piece_achat p 
				LEFT JOIN yvs_base_caisse c ON p.caisse = c.id  LEFT JOIN yvs_base_mode_reglement mo ON p.model = mo.id
				LEFT JOIN yvs_com_doc_achats d ON p.achat = d.id LEFT JOIN yvs_base_fournisseur t ON d.fournisseur = t.id WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece_achat p WHERE p.piece_achat = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece_achat p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_achat = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_ACHAT', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_paiement ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_paiement;
					_libelle_ = 'Reglement Facture achat N° ';
					
					IF(data_.achat IS NULL OR data_.achat < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune facture';
					ELSE
						_libelle_ = _libelle_||data_.num_doc;
						IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de fournisseur';
						ELSE
							IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
								_error_ = 'car ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte tiers';
							ELSE
								_compte_tiers_ = data_.tiers;
							END IF;
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
					_numero_ = 1;
					_credit_ = 0;
					_debit_ = data_.montant;
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
					
					_error_ = '';			
					_compte_tiers_ = null;	
					IF(data_.type_reglement = 'COMPENSATION')THEN					
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'COMPENSATION';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des attentes pour compensation n''est pas paramètré';
						END IF;
					ELSE
						SELECT INTO ligne_ COUNT(n.id) as count FROM yvs_compta_notif_reglement_achat n WHERE n.piece_achat = element_;
						IF(ligne_.count IS NOT NULL AND ligne_.count > 0)THEN		
							SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
							IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
								_error_ = 'Le compte des acomptes fournisseur n''est paramètré';
							END IF;		
							_compte_tiers_ = data_.tiers;				
						ELSE
							IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
								_error_ = 'ce reglement n''est associé à aucune caisse';
							ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
								_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
							ELSE	
								_compte_general_ = data_.compte;
							END IF;
						END IF;
					END IF;
					_numero_ = 2;			
					_credit_ = data_.montant;
					_debit_ = 0;		
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CAISSE_DIVERS')THEN
			SELECT INTO data_ p.id, p.num_piece, p.date_piece, p.montant, p.caisse, p.doc_divers, d.num_piece as num_doc, CONCAT(d.type_doc, '') AS type_doc, CONCAT(d.mouvement, '') AS mouvement, d.id_tiers as tiers, d.compte_general, name_tiers(d.id_tiers, d.table_tiers, 'C')::integer as compte_collectif, 
				c.compte, mo.type_reglement, COALESCE(d.libelle_comptable, d.num_piece) AS description, d.table_tiers, d.agence 
				FROM yvs_compta_caisse_piece_divers p LEFT JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_base_mode_reglement mo ON p.mode_paiement = mo.id 
				LEFT JOIN yvs_compta_caisse_doc_divers d ON p.doc_divers = d.id WHERE p.id = element_;
			IF(data_.num_piece IS NOT NULL AND data_.num_piece NOT IN ('', ' '))THEN		
				_agence_ = data_.agence;
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece_divers p WHERE p.piece_divers = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece_divers p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_divers = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_DIVERS', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					SELECT INTO ligne_ COUNT(p.id) FROM yvs_compta_caisse_piece_cout_divers p WHERE p.piece = data_.id;
					IF(COALESCE(ligne_.count, 0) > 0)THEN
						_error_ = 'ce reglement est rattaché à un cout';
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					ELSE
						_table_tiers_ = data_.table_tiers;	
						_jour_ = to_char(data_.date_piece ,'dd')::integer;
						_num_piece_ = data_.num_doc;	
						_echeance_ = data_.date_piece;
						_libelle_ = 'Reglement N° '||data_.num_piece||' sur ';	
						
						IF(data_.doc_divers IS NULL OR data_.doc_divers < 1)THEN
							_error_ = 'ce reglement n''est associé à aucun document';
						ELSE
							_libelle_ = _libelle_||data_.description;
						END IF;
						
						_error_ = '';
						_compte_general_ = null;
						_compte_tiers_ = null;
						_debit_ = 0;
						_credit_ = 0;		
						IF(data_.mouvement = 'D')THEN
							_debit_ = data_.montant;
							_numero_ = 1;
						ELSE	
							_credit_ = data_.montant;	
							_numero_ = 2;			
						END IF;
						IF(data_.tiers IS NULL OR data_.tiers < 1)THEN							
							_error_ = 'devez comptabiliser le documents en question';
						ELSE
							_compte_tiers_ = data_.tiers;
							IF(data_.compte_collectif IS NULL OR data_.compte_collectif < 1)THEN							
								_error_ = 'car ce reglement est rattaché à un document d''un tiers qui n''a pas de compte collectif';
							ELSE
								_compte_general_ = data_.compte_collectif;
							END IF;	
						END IF;	
						_num_ref_ = data_.num_piece;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
						
						_error_ = '';
						_compte_general_ = null;
						_compte_tiers_ = null;
						_credit_ = 0;
						_debit_ = 0;
						IF(data_.mouvement = 'D')THEN
							_credit_ = data_.montant;
							_numero_ = 2;
						ELSE
							_debit_ = data_.montant;
							_numero_ = 1;		
						END IF;	
						IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
							_error_ = 'ce reglement n''est associé à aucune caisse';
						ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;	
						_num_ref_ = data_.num_piece;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
				END IF;
			END IF;
		ELSIF(table_ = 'ACOMPTE_VENTE')THEN
			SELECT INTO data_ a.id, a.date_acompte, a.num_refrence, a.montant, m.societe, c.compte, a.client as tiers, m.type_reglement, j.agence FROM yvs_compta_acompte_client a  LEFT JOIN yvs_base_caisse c ON a.caisse = c.id
				INNER JOIN yvs_base_mode_reglement m ON a.model = m.id LEFT JOIN yvs_com_client f ON a.client = f.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE a.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_acompte_vente p WHERE p.piece_vente = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_acompte_vente p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_vente = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_ACOMPTE_VENTE', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_acompte ,'dd')::integer;
					_num_piece_ = data_.num_refrence;	
					_echeance_ = data_.date_acompte;
					_libelle_ = 'Acompte N° '||data_.num_refrence;				
					SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
					IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
						_error_ = 'Le compte des acomptes client n''est pas paramètré';
					END IF;
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cet acompte est rattaché à un fournisseur qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;				
					END IF;				
					_credit_ = data_.montant;
					_debit_ = 0;	
					_numero_ = 2;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;

					_error_ = null;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cet acompte est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;
					_credit_ = 0;
					_debit_ = data_.montant;	
					_numero_ = 1;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'ACOMPTE_ACHAT')THEN
			SELECT INTO data_ a.id, a.date_acompte, a.num_refrence, a.montant, m.societe, c.compte, a.fournisseur as tiers, m.type_reglement, j.agence FROM yvs_compta_acompte_fournisseur a LEFT JOIN yvs_base_caisse c ON a.caisse = c.id 
				INNER JOIN yvs_base_mode_reglement m ON a.model = m.id INNER JOIN yvs_base_fournisseur f ON a.fournisseur = f.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE a.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_acompte_achat p WHERE p.piece_achat = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_acompte_achat p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_achat = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_ACOMPTE_ACHAT', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_acompte ,'dd')::integer;
					_num_piece_ = data_.num_refrence;	
					_echeance_ = data_.date_acompte;
					_libelle_ = 'Acompte N° '||data_.num_refrence;				
					SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
					IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
						_error_ = 'Le compte des acomptes fournisseur n''est pas paramètré';
					END IF;
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cet acompte est rattaché à un fournisseur qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;				
					END IF;			
					_credit_ = 0;
					_debit_ = data_.montant;	
					_numero_ = 1;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;

					_error_ = null;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cet acompte est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;
					_credit_ = data_.montant;
					_debit_ = 0;	
					_numero_ = 2;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CREDIT_VENTE')THEN
			SELECT INTO data_ a.date_credit, a.num_reference, a.montant, m.societe, c.compte, a.client as tiers, t.compte as compte_general, j.agence FROM yvs_compta_credit_client a 
				INNER JOIN yvs_com_client c ON a.client = c.id LEFT JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				LEFT JOIN yvs_grh_type_cout t ON a.type_credit = t.id WHERE a.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_credit ,'dd')::integer;
				_num_piece_ = data_.num_reference;	
				_echeance_ = data_.date_credit;
				_libelle_ = 'Credit N° '||data_.num_reference;				
				IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
					_error_ = 'Le credit est rattaché a un type qui n''a pas de compte';
				ELSE
					_compte_general_ = data_.compte_general;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				_compte_tiers_ = data_.tiers;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le client n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CREDIT_ACHAT')THEN
			SELECT INTO data_ a.date_credit, a.num_reference, a.montant, m.societe, c.compte, a.fournisseur as tiers, t.compte as compte_general, j.agence FROM yvs_compta_credit_fournisseur a 
				INNER JOIN yvs_base_fournisseur c ON a.fournisseur = c.id INNER JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				LEFT JOIN yvs_grh_type_cout t ON a.type_credit = t.id WHERE a.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_credit ,'dd')::integer;
				_num_piece_ = data_.num_reference;	
				_echeance_ = data_.date_credit;
				_libelle_ = 'Credit N° '||data_.num_reference;					
				IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
					_error_ = 'Le credit est rattaché a un type qui n''a pas de compte';
				ELSE
					_compte_general_ = data_.compte_general;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				_compte_tiers_ = data_.tiers;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le fournisseur n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_CREDIT_VENTE')THEN
			SELECT INTO data_ r.date_paiement, r.numero, a.num_reference, r.valeur as montant, m.societe, c.compte, a.client as tiers, t.compte as compte_general, j.agence ,r.caisse
				FROM yvs_compta_reglement_credit_client r LEFT JOIN yvs_compta_credit_client a ON r.credit = a.id LEFT JOIN yvs_base_caisse t ON r.caisse = t.id
				LEFT JOIN yvs_com_client c ON a.client = c.id INNER JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				WHERE r.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero;	
				_echeance_ = data_.date_paiement;
				_libelle_ = 'REGLEMENT N°'||data_.numero||' DU Credit N° '||data_.num_reference;					
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'Le reglement n''est pas rattaché à une caisse';
				ELSE
					IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
						_error_ = 'Le reglement est rattaché a une caisse qui n''a pas de compte';
					ELSE
						_compte_general_ = data_.compte_general;
					END IF;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 2;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le client n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_compte_tiers_ = data_.tiers;	
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_CREDIT_ACHAT')THEN
			SELECT INTO data_ r.date_paiement, r.numero, a.num_reference, r.valeur as montant, m.societe, c.compte, a.fournisseur as tiers, t.compte as compte_general, j.agence ,r.caisse
				FROM yvs_compta_reglement_credit_fournisseur r LEFT JOIN yvs_compta_credit_fournisseur a ON r.credit = a.id LEFT JOIN yvs_base_caisse t ON r.caisse = t.id
				LEFT JOIN yvs_base_fournisseur c ON a.fournisseur = c.id INNER JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				WHERE r.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero;	
				_echeance_ = data_.date_paiement;
				_libelle_ = 'REGLEMENT N°'||data_.numero||' DU Credit N° '||data_.num_reference;					
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'Le reglement n''est pas rattaché à une caisse';
				ELSE
					IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
						_error_ = 'Le reglement est rattaché a une caisse qui n''a pas de compte';
					ELSE
						_compte_general_ = data_.compte_general;
					END IF;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le fournisseur n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_compte_tiers_ = data_.tiers;	
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'DOC_VIREMENT')THEN
			SELECT INTO data_ p.numero_piece, p.date_paiement, p.montant, p.cible, p.source, COALESCE(p.note, 'Virement N° '||p.numero_piece) AS note, c.compte AS compte_cible, s.compte AS compte_source, (SELECT l.compte FROM yvs_base_liaison_caisse l WHERE l.caisse_cible = p.cible AND l.caisse_source = p.source LIMIT 1) AS compte_interne, j.agence
				FROM yvs_compta_caisse_piece_virement p LEFT JOIN yvs_base_caisse s ON p.source = s.id LEFT JOIN yvs_base_caisse c ON p.cible = c.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'EMPLOYE';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero_piece;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.note;

				_error_ = '';
				_debit_ = 0;
				_compte_general_ = null;
				IF(data_.source IS NULL OR data_.source < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune emetteur';
				ELSIF(data_.compte_source IS NULL OR data_.compte_source < 1)THEN
					_error_ = 'ce reglement est rattaché à un emetteur qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte_source;
				END IF;
				_credit_ = data_.montant;
				_numero_ = 2;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				_credit_ = 0;
				IF(data_.compte_interne IS NULL OR data_.compte_interne < 1)THEN
					_error_ = 'ces comptes n''ont pas de compte de liaison';
				ELSE
					_compte_general_ = data_.compte_interne;
				END IF;
				_debit_ = data_.montant;
				_numero_ = 2;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				
				_debit_ = 0;
				_credit_ = data_.montant;
				_numero_ = 1;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				_credit_ = 0;
				IF(data_.cible IS NULL OR data_.cible < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune recepteur';
				ELSIF(data_.compte_cible IS NULL OR data_.compte_cible < 1)THEN
					_error_ = 'ce reglement est rattaché à un recepteur qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte_cible;
				END IF;
				_debit_ = data_.montant;
				_numero_ = 1;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_numero_ = 2;
				FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_compta_cout_sup_piece_virement co ON co.type_cout = tx.id INNER JOIN yvs_compta_caisse_piece_virement dv ON dv.id = co.virement
					WHERE dv.id = element_ GROUP BY tx.id, tx.libelle 
				LOOP
					_compte_tiers_ = null;	
					IF(ligne_.total != 0)THEN
						_libelle_ = ligne_.libelle||' pour le virement N° '||data_.numero_piece;
						
						_credit_ = 0;
						_debit_ = ligne_.total;
						IF(i_ < 10)THEN
							_num_ref_ = data_.numero_piece ||'-0'||i_;
						ELSE
							_num_ref_ = data_.numero_piece ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
						END IF;
						_compte_general_ = ligne_.compte;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;

						
						_credit_ = ligne_.total;
						_debit_ = 0;
						IF(i_ < 10)THEN
							_num_ref_ = data_.numero_piece ||'-0'||i_;
						ELSE
							_num_ref_ = data_.numero_piece ||'-'||i_;
						END IF;
						IF(data_.source IS NULL OR data_.source < 1)THEN
							_error_ = 'ce reglement n''est associé à aucune emetteur';
						ELSIF(data_.compte_source IS NULL OR data_.compte_source < 1)THEN
							_error_ = 'ce reglement est rattaché à un emetteur qui n''a pas de compte général';
						END IF;
						_compte_general_ = data_.compte_source;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
				END LOOP;
			END IF;
		ELSIF(table_ = 'ORDRE_SALAIRE')THEN
			SELECT INTO data_ o.* FROM yvs_grh_ordre_calcul_salaire o WHERE o.id = element_;
			IF(data_.reference IS NOT NULL AND data_.reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'TIERS';	
				_jour_ = to_char(data_.date_execution ,'dd')::integer;
				_num_piece_ = data_.reference;	
				_echeance_ = data_.date_execution;
				-- Recupere gains
				-- récupère les éléments de gains en groupant par compte charge du gains
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, COALESCE(SUM(db.montant_payer),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition FROM yvs_grh_detail_bulletin db 
					LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat 
					LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id 
					WHERE el.visible_bulletin IS TRUE AND NOT (COALESCE(db.montant_payer,0)=0) AND el.actif IS TRUE AND el.retenue IS FALSE AND b.entete = element_ 
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, co.id ORDER BY ag.designation, el.nom
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;
					
					_error_ = '';
					_credit_ = 0;
					_numero_ = 1;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.id IS NULL OR ligne_.id < 1)THEN
						_error_ = 'certains bulletins ne sont pas rattachés aux elements de salaire';
					ELSIF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'le gain '||ligne_.nom||' n''est rattaché à aucun compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
-- 					RAISE NOTICE '- %',_compte_general_;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- récupère du net a payer en groupant par compte collectif employé
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, COALESCE(co.intitule, '') AS intitule, e.compte_collectif, (COALESCE(SUM(db.montant_payer),0) - COALESCE(SUM(db.retenu_salariale),0)) AS montant, co.saisie_analytique 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON e.compte_collectif = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.montant_payer,0)!=0 OR COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND b.entete = element_
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, co.id, e.compte_collectif ORDER BY ag.designation, e.compte_collectif, co.intitule
				LOOP
					_libelle_ = ligne_.intitule ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 1;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_collectif IS NULL OR ligne_.compte_collectif < 1)THEN
						_error_ = 'certains employes n''ont pas de compte collectif';
					ELSE
						_compte_general_ = ligne_.compte_collectif;
					END IF;
					_credit_ = ligne_.montant;
					IF(ligne_.montant = 177871.63)THEN
						RAISE NOTICE '_libelle_ : %', _libelle_;
						RAISE NOTICE '_compte_general_ : %', _compte_general_;
					END IF;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, 0, _compte_general_, _compte_tiers_, _credit_, '', table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- Recupere retenues
				-- récupère les retenues patronales
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, el.compte_cotisation, COALESCE(SUM(db.montant_employeur),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.montant_employeur,0)!=0) AND el.actif IS TRUE AND el.retenue IS TRUE AND b.entete = element_
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, el.compte_charge, el.compte_cotisation, co.id ORDER BY ag.designation , el.compte_charge
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 5;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_cotisation IS NULL OR ligne_.compte_cotisation < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de cotisation';
					ELSE
						_compte_general_ = ligne_.compte_cotisation;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;	

					_error_ = '';
					_credit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- récupère les retenues salariales sans saisie tiers en groupant par compte de cotisation des retenues
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_cotisation, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON el.compte_cotisation = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND (COALESCE(el.saisi_compte_tiers, false) IS FALSE) 
					AND el.retenue IS TRUE AND b.entete = element_ AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, el.compte_cotisation, co.id ORDER BY ag.designation , el.nom
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 2;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_cotisation IS NULL OR ligne_.compte_cotisation < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de cotisation';
					ELSE
						_compte_general_ = ligne_.compte_cotisation;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;	
				-- récupère les retenues salariales avec saisie tiers en groupant par compte de charge des retenues
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition, t.id AS tiers, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom
                                        FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
                                        LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
                                        LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id LEFT JOIN yvs_base_tiers t ON e.compte_tiers = t.id 
                                        WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND COALESCE(el.saisi_compte_tiers, false) IS TRUE 
                                        AND el.retenue IS TRUE AND b.entete = element_ AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val))))
                                        GROUP BY ag.id, el.id, el.compte_charge, co.id, t.id ORDER BY ag.designation, el.nom
				LOOP
					_libelle_ = ligne_.nom ||' liée '|| ligne_.nom_prenom;
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 3;
					_compte_general_ = null;
					IF(ligne_.tiers IS NULL OR ligne_.tiers < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' est rattachée à des employés qui n''ont pas de compte tiers';
					ELSE
						_compte_tiers_ = ligne_.tiers;
					END IF;
					IF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;	
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;	
			END IF;
		END IF;
	END IF;
	-- DETERMINATION DE L'ARRONDI
	SELECT INTO _debit_ SUM(_debit) FROM table_compta_content_journal;
	SELECT INTO _credit_ SUM(_credit) FROM table_compta_content_journal;
	somme_ = COALESCE(_debit_, 0) - COALESCE(_credit_, 0);
	IF(somme_ != 0 AND abs(somme_) <= valeur_limite_arrondi_)THEN
		_compte_tiers_ = null;
		IF(somme_ > 0)THEN
			SELECT INTO data_ * FROM table_compta_content_journal WHERE _credit > 0 ORDER BY _numero LIMIT 1;
			_libelle_ = 'arrondis gain';
			titre_ = 'ARRONDI_GAIN';
			IF(table_ = 'DOC_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'JOURNAL_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'DOC_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _credit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'FRAIS_MISSION')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _credit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CREDIT_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'CREDIT_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'DOC_VIREMENT')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'ORDRE_SALAIRE')THEN	
				_table_tiers_ = 'EMPLOYE';	
			END IF;				
			SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = titre_;
			IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
				_error_ = 'Le compte des '||_libelle_||' n''est pas paramètré';
			END IF;
			_credit_ = somme_;
			_debit_ = 0;
		ELSE
			SELECT INTO data_ * FROM table_compta_content_journal WHERE _debit > 0 ORDER BY _numero LIMIT 1;
			_libelle_ = 'arrondis perte';
			titre_ = 'ARRONDI_PERTE';
			IF(table_ = 'DOC_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _debit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'JOURNAL_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'DOC_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'FRAIS_MISSION')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _debit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CREDIT_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'CREDIT_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'DOC_VIREMENT')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'ORDRE_SALAIRE')THEN	
				_table_tiers_ = 'EMPLOYE';	
			END IF;
			_libelle_ = 'Arrondi Perte';				
			SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = titre_;
			IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
				_error_ = 'Le compte des '||_libelle_||' n''est pas paramètré';
			END IF;
			_credit_ = 0;
			_debit_ = -somme_;
		END IF;	
		_jour_ = data_._jour;
		_num_piece_ = data_._num_piece;
		_echeance_ = data_._echeance;
		_numero_ = data_._numero;	
		IF(i_ < 10)THEN
			_num_ref_ = _num_piece_ ||'-0'||i_;
		ELSE
			_num_ref_ = _num_piece_ ||'-'||i_;
		END IF;		
		_numero_ = COALESCE(_numero_, 0) + 1;
		_id_ = _id_ - 1;
		INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
	END IF;
	RETURN QUERY SELECT * FROM table_compta_content_journal ORDER BY _numero, _debit DESC, _credit DESC, _id DESC, _jour, _num_piece;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean)
  OWNER TO postgres;

  
-- Function: update_()
-- DROP FUNCTION update_();
CREATE OR REPLACE FUNCTION update_()
  RETURNS boolean AS
$BODY$   
DECLARE 
	
	id_ BIGINT;

	serveur CHARACTER VARYING DEFAULT '192.168.8.126';
	database CHARACTER VARYING DEFAULT 'lymytz_demo_0';
	users CHARACTER VARYING DEFAULT 'postgres';
	password CHARACTER VARYING DEFAULT 'yves1910/';
BEGIN
	FOR id_  IN SELECT DISTINCT compte_general FROM yvs_compta_content_journal WHERE COALESCE(compte_tiers, 0) = 0
	LOOP
		UPDATE yvs_compta_content_journal SET compte_tiers = 0 WHERE compte_general = id_ AND compte_tiers IN (736) AND table_tiers = 'CLIENT';
		UPDATE yvs_compta_content_journal SET compte_tiers = 0 WHERE compte_general = id_ AND compte_tiers IN (3453, 2829) AND table_tiers = 'TIERS';
	END LOOP;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_()
  OWNER TO postgres;
  
  
-- Function: compta_et_debit_initial(bigint, bigint, bigint, date, bigint, character varying, boolean, character varying)
DROP FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean, character varying);
CREATE OR REPLACE FUNCTION compta_et_debit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, journal_ bigint, type_ character varying, general boolean, table_tiers_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   date_fin_ date;
   exo_ record;

   date_initial_ DATE DEFAULT '01-01-2000';
   
begin 	
	date_fin_ = date_debut_ - interval '1 day';	
	return (select compta_et_debit(agence_, societe_, valeur_, date_initial_, date_fin_, journal_, type_, general, table_tiers_));
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, bigint, character varying, boolean, character varying)
  OWNER TO postgres;

  
-- Function: compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean)
-- DROP FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_debit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, type_ character varying, general boolean)
  RETURNS double precision AS
$BODY$
declare 
   
begin 	
	return compta_et_debit_initial(agence_, societe_, valeur_, date_debut_, 0, type_, general, 'TIERS');
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying, boolean)
  OWNER TO postgres;

  
-- Function: compta_et_credit_initial(bigint, bigint, bigint, date, bigint, character varying, boolean, character varying)
DROP FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean, character varying);
CREATE OR REPLACE FUNCTION compta_et_credit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, journal_ bigint, type_ character varying, general boolean, table_tiers_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   date_fin_ date;
   exo_ record;
   
   date_initial_ DATE DEFAULT '01-01-2000';
begin 	
	date_fin_ = date_debut_ - interval '1 day';	
	return (select compta_et_credit(agence_, societe_, valeur_, date_initial_, date_fin_, journal_, type_, general, table_tiers_));
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, bigint, character varying, boolean, character varying)
  OWNER TO postgres;


-- Function: compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean)
-- DROP FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_credit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, type_ character varying, general boolean)
  RETURNS double precision AS
$BODY$
declare 
   
begin 	
	return compta_et_credit_initial(agence_, societe_, valeur_, date_debut_, 0, type_, general, 'TIERS');
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit_initial(bigint, bigint, bigint, date, character varying, boolean)
  OWNER TO postgres;


  
-- Function: compta_et_credit(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying)
DROP FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean, character varying);
CREATE OR REPLACE FUNCTION compta_et_credit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, journal_ bigint, type_ character varying, general_ boolean, table_tiers_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   compte_ record;
   comptes_ record;
   credit_ double precision default 0;
   query_ CHARACTER VARYING;
   
begin 	
	IF(type_ = 'A')THEN
		query_ = 'select sum(o.credit) from yvs_compta_content_analytique o inner join yvs_compta_content_journal c on o.contenu = c.id inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
	ELSE
		query_ = 'select sum(c.credit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
	END IF;
	IF(journal_ IS NOT NULL AND journal_ > 0)THEN		
		query_ = query_ || ' and j.id = '||journal_;
	END IF;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
		query_ = query_ || ' and j.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN		
		query_ = query_ || ' and a.societe = '||societe_;
	END IF;
	IF(type_ = 'T')THEN
		query_ = query_ ||' and c.compte_tiers = '||valeur_;
		IF(COALESCE(table_tiers_, '') NOT IN ('', ' '))THEN
			query_ = query_ ||' and c.table_tiers = '||QUOTE_LITERAL(table_tiers_);
		END IF;
		EXECUTE query_ INTO credit_;
	ELSIF(type_ = 'C')THEN
		query_ = query_ ||' and c.compte_general = '||valeur_;
		SELECT INTO compte_ id, type_compte FROM yvs_base_plan_comptable WHERE id = valeur_;
		IF(compte_.id IS NOT NULL AND compte_.id > 0)then
			EXECUTE query_ INTO credit_;
			credit_ = COALESCE(credit_, 0);
			IF(general_)THEN
				IF(compte_.type_compte = 'CO')THEN
					FOR comptes_ IN SELECT id FROM yvs_base_plan_comptable WHERE compte_general = valeur_
					LOOP
						credit_ = credit_ + compta_et_credit(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, type_);
					END LOOP;
				END IF;
			END IF;
		END IF;
	ELSE
		query_ = query_ ||' and o.centre = '||valeur_;
		EXECUTE query_ INTO credit_;
	END IF;
	RETURN COALESCE(credit_, 0);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying)
  OWNER TO postgres;

  
  
-- Function: compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean)
-- DROP FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_credit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, type_ character varying, general_ boolean)
  RETURNS double precision AS
$BODY$
declare 
   
begin 	
	RETURN compta_et_credit(agence_, societe_, valeur_, date_debut_, date_fin_, 0, type_ , general_, 'TIERS');
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_credit(bigint, bigint, bigint, date, date, character varying, boolean)
  OWNER TO postgres;

  

-- Function: compta_et_debit(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying)
DROP FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean, character varying);
CREATE OR REPLACE FUNCTION compta_et_debit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, journal_ bigint, type_ character varying, general boolean, table_tiers_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   compte_ record;
   comptes_ record;
   debit_ double precision default 0;
   query_ CHARACTER VARYING;
   
begin 	
	IF(type_ = 'A')THEN
		query_ = 'select sum(o.debit) from yvs_compta_content_analytique o inner join yvs_compta_content_journal c on o.contenu = c.id inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
	ELSE
		query_ = 'select sum(c.debit) from yvs_compta_content_journal c inner join yvs_compta_pieces_comptable p on c.piece = p.id inner join yvs_compta_journaux j on j.id = p.journal inner join yvs_agences a on j.agence = a.id 
			where p.date_piece between '''||date_debut_||''' and '''||date_fin_||'''';
	END IF;
	IF(journal_ IS NOT NULL AND journal_ > 0)THEN		
		query_ = query_ || ' and j.id = '||journal_;
	END IF;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
		query_ = query_ || ' and j.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN		
		query_ = query_ || ' and a.societe = '||societe_;
	END IF;
	IF(type_ = 'T')THEN
		query_ = query_ ||' and c.compte_tiers = '||valeur_;
		IF(COALESCE(table_tiers_, '') NOT IN ('', ' '))THEN
			query_ = query_ ||' and c.table_tiers = '||QUOTE_LITERAL(table_tiers_);
		END IF;
		EXECUTE query_ INTO debit_;
	ELSIF(type_ = 'C')THEN
		query_ = query_ ||' and c.compte_general = '||valeur_;
		SELECT INTO compte_ id, type_compte FROM yvs_base_plan_comptable WHERE id = valeur_;
		IF(compte_.id IS NOT NULL AND compte_.id > 0)then
			EXECUTE query_ INTO debit_;
			debit_ = COALESCE(debit_, 0);
			IF(general)THEN
				IF(compte_.type_compte = 'CO')THEN
					FOR comptes_ IN SELECT id FROM yvs_base_plan_comptable WHERE compte_general = valeur_
					LOOP
						debit_ = debit_ + compta_et_debit(agence_ , societe_ , comptes_.id, date_debut_, date_fin_, type_);
					END LOOP;
				END IF;
			END IF;
		END IF;
	ELSE
		query_ = query_ ||' and o.centre = '||valeur_;
		EXECUTE query_ INTO debit_;
	END IF;
	RETURN COALESCE(debit_, 0);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, bigint, character varying, boolean, character varying)
  OWNER TO postgres;

  
-- Function: compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean)
-- DROP FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_debit(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, date_fin_ date, type_ character varying, general boolean)
  RETURNS double precision AS
$BODY$
declare 
   
begin 	
	RETURN compta_et_debit(agence_, societe_, valeur_, date_debut_, date_fin_, 0, type_, general, 'TIERS');
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit(bigint, bigint, bigint, date, date, character varying, boolean)
  OWNER TO postgres;
  
  
  
-- Function: compta_et_balance(bigint, bigint, character varying, date, date, bigint, character varying)
DROP FUNCTION compta_et_balance(bigint, bigint, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION compta_et_balance(IN agence_ bigint, IN societe_ bigint, IN comptes_ character varying, IN date_debut_ date, IN date_fin_ date, IN journal_ bigint, IN type_ character varying)
  RETURNS TABLE(numero bigint, code character varying, general character varying, debit_initial double precision, credit_initial double precision, debit_periode double precision, credit_periode double precision, debit_solde_periode double precision, credit_solde_periode double precision, debit_solde_cumul double precision, credit_solde_cumul double precision, is_general boolean, table_tiers character varying, id_general bigint) AS
$BODY$
declare 
	query_ character varying default '';
	connexion character varying default 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
	data_ record;
	sous_ bigint default 0;
	general_ boolean default false;
	
	gen_ record;
	
	di_ double precision default 0;
	ci_ double precision default 0;
	si_ double precision default 0;
	dp_ double precision default 0;
	cp_ double precision default 0;
	sp_ double precision default 0;
	dsp_ double precision default 0;
	csp_ double precision default 0;
	dsc_ double precision default 0;
	csc_ double precision default 0;
	ssc_ double precision default 0;
	
	ids_ character varying default '''0''';
	valeur_ character varying default '0';
	
begin 	
	DROP TABLE IF EXISTS table_balance;
	CREATE TEMP TABLE IF NOT EXISTS table_balance(num bigint, cod character varying, gen character varying, di double precision, ci double precision, dp double precision, cp double precision,
							dsp double precision, csp double precision, dsc double precision, csc double precision, is_gen boolean, tt character varying, id_gen bigint); 
	DELETE FROM table_balance;
	if(type_ = 'T')then
		query_ = 'select DISTINCT(y.compte_tiers) as id, name_tiers(y.compte_tiers, y.table_tiers, ''R'') as code, y.table_tiers from yvs_compta_content_journal y inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where coalesce(y.compte_tiers, 0) > 0';			
	elsif(type_ = 'C') then
		query_ = 'select DISTINCT(c.id) as id, c.num_compte as code, null as table_tiers from yvs_compta_content_journal y inner join yvs_base_plan_comptable c on y.compte_general = c.id inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where c.num_compte is not null';			
	else
		query_ = 'select DISTINCT(c.id) as id, c.code_ref as code, null as table_tiers from yvs_compta_content_analytique o INNER JOIN yvs_compta_content_journal y ON o.contenu = y.id inner join yvs_compta_centre_analytique c on o.centre = c.id inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where c.code_ref is not null';
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ || ' and a.id = '||agence_||'';
	end if;
	if(societe_ is not null and societe_ > 0)then
		query_ = query_ || ' and a.societe = '||societe_||'';
	end if;
	IF(COALESCE(comptes_, '') NOT IN ('', ' ', '0'))THEN
		FOR valeur_ IN SELECT head FROM regexp_split_to_table(comptes_,',') head WHERE CHAR_LENGTH(head) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(valeur_);
		END LOOP;
		IF(type_ = 'T')THEN
			query_ = query_ || ' AND name_tiers(y.compte_tiers, y.table_tiers, ''R'') IN ('||ids_||')';		
		ELSIF(type_ = 'C') THEN
			query_ = query_ || ' AND c.num_compte IN ('||ids_||')';	
		ELSE
			query_ = query_ || ' AND c.code_ref IN ('||ids_||')';	
		END IF;
	END IF;
	if(query_ is not null and query_ != '')then
		FOR data_ IN EXECUTE query_
		loop
			dsp_ = 0;
			dsc_ = 0;		
			csp_ = 0;
			csc_ = 0;

			di_ = (select compta_et_debit_initial(agence_, societe_, data_.id, date_debut_, journal_, type_, false, data_.table_tiers));
			ci_ = (select compta_et_credit_initial(agence_, societe_, data_.id, date_debut_, journal_, type_, false, data_.table_tiers));
			si_ = di_ - ci_ ;
			
			dp_ = (select compta_et_debit(agence_, societe_, data_.id, date_debut_, date_fin_, journal_, type_, false, data_.table_tiers));
			cp_ = (select compta_et_credit(agence_, societe_, data_.id, date_debut_, date_fin_, journal_, type_, false, data_.table_tiers));	
			sp_ = dp_ - cp_ ;	
				
			if(si_ > 0)then
				di_ = si_;
				ci_ = 0;			
			elsif(si_ < 0) then
				ci_ = -(si_);
				di_ = 0;
			else
				ci_ = 0;
				di_ = 0;
			end if;		
				
			if(sp_ > 0)then
				dsp_ = sp_;
				csp_ = 0;			
			elsif(sp_ < 0)then
				csp_ = -(sp_);
				dsp_ = 0;
			else
				csp_ = 0;
				dsp_ = 0;
			end if;

			ssc_ = si_ + sp_;
			if(ssc_ > 0)then
				dsc_ = ssc_;
				csc_ = 0;			
			elsif(ssc_ < 0)then
				csc_ = -(ssc_);
				dsc_ = 0;
			else
				csc_ = 0;
				dsc_ = 0;
			end if;		
			
			if(di_ != 0 or ci_ != 0 or dp_ !=0 or cp_ != 0 or dsp_ != 0 or csp_ != 0 or dsc_ != 0 or csc_ !=0)then
				general_ = false;
				if(type_ = 'C')then
					select into gen_ coalesce(g.id, c.id) as id, coalesce(g.num_compte, c.num_compte) as numero from yvs_base_plan_comptable g right join yvs_base_plan_comptable c on g.id = c.compte_general where c.id = data_.id;
					select into sous_ count(c.id) from yvs_base_plan_comptable c where c.compte_general = data_.id;
					if(sous_ is not null and sous_ > 0)then
						general_ = true;
					end if;
					insert into table_balance values(data_.id, data_.code, gen_.numero, di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_, general_, data_.table_tiers, gen_.id);
				else
					insert into table_balance values(data_.id, data_.code, '', di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_, general_, data_.table_tiers, 0);
				end if;
			end if;
		end loop;
	end if;
	return QUERY SELECT * from table_balance order by num;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_balance(bigint, bigint, character varying, date, date, bigint, character varying)
  OWNER TO postgres;


  
-- Function: compta_et_grand_livre(bigint, bigint, character varying, date, date, bigint, character varying, boolean)
DROP FUNCTION compta_et_grand_livre(bigint, bigint, character varying, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_grand_livre(IN societe_ bigint, IN agence_ bigint, IN comptes_ character varying, IN date_debut_ date, IN date_fin_ date, IN journal_ bigint, IN type_ character varying, IN lettrer_ boolean)
  RETURNS TABLE(id bigint, code character varying, intitule character varying, compte character varying, designation character varying, numero character varying, date_piece date, jour integer, libelle character varying, lettrage character varying, journal character varying, debit double precision, credit double precision, compte_tiers bigint, table_tiers character varying, solde boolean) AS
$BODY$
declare 
	data_ record;
	
	credit_si_ double precision default 0;
	debit_si_ double precision default 0;
	solde_ double precision default 0;
	
	query_ character varying default '';
	last_code_ character varying default '';
	
	ids_ character varying default '''0''';
	valeur_ character varying default '0';
begin 	
	DROP TABLE IF EXISTS table_et_grand_livre;
	CREATE TEMP TABLE IF NOT EXISTS table_et_grand_livre(_id bigint, _code character varying, _intitule character varying, _compte character varying, _designation character varying, _numero character varying, _date_piece date, _jour integer, _libelle character varying, _lettrage character varying, _journal character varying, _debit double precision, _credit double precision, _compte_tiers bigint, _table_tiers character varying, _solde boolean); 
	DELETE FROM table_et_grand_livre;
	IF(type_ = 'T')THEN
		query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, y.debit, y.credit, y.compte_tiers as id, c.num_compte as compte, c.intitule as designation, p.date_piece, j.code_journal as journal, name_tiers(y.compte_tiers, y.table_tiers, ''R'') as code, name_tiers(y.compte_tiers, y.table_tiers, ''N'') as intitule, y.compte_tiers, y.table_tiers
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id';
	ELSIF(type_ = 'C') THEN
		query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, y.debit, y.credit, c.id, c.num_compte as code, c.intitule, p.date_piece, j.code_journal as journal, null as compte, null as designation, y.compte_tiers, y.table_tiers
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id';
			
	ELSE
		query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, l.debit, l.credit, n.id, c.num_compte as compte, c.intitule as designation, p.date_piece, j.code_journal as journal, n.code_ref as code, n.designation as intitule, y.compte_tiers, y.table_tiers
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id
				INNER JOIN yvs_compta_content_analytique l ON y.id = l.contenu INNER JOIN yvs_compta_centre_analytique n ON n.id = l.centre';
	END IF;
	query_ = query_ || ' WHERE p.date_piece BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(journal_ IS NOT NULL AND journal_ > 0)THEN
		query_ = query_ || ' AND j.id = '||journal_||'';
	END IF;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_||'';
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_||'';
	END IF;
	IF(COALESCE(comptes_, '') NOT IN ('', ' ', '0'))THEN
		FOR valeur_ IN SELECT head FROM regexp_split_to_table(comptes_,',') head WHERE CHAR_LENGTH(head) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(valeur_);
		END LOOP;
		IF(type_ = 'T')THEN
			query_ = query_ || ' AND name_tiers(y.compte_tiers, y.table_tiers, ''R'') IN ('||ids_||')';		
		ELSIF(type_ = 'C') THEN
			query_ = query_ || ' AND c.num_compte IN ('||ids_||')';	
		ELSE
			query_ = query_ || ' AND n.code_ref IN ('||ids_||')';	
		END IF;
	END IF;
	IF(lettrer_ IS NOT NULL)THEN
		query_ = query_ || ' AND COALESCE(LENGTH(y.lettrage), 0)';
		IF(lettrer_)THEN
			query_ = query_ || '>0';
		ELSE
			query_ = query_ || '<=0';
		END IF;
	END IF;
	IF(type_ = 'T')THEN
		query_ = query_ || ' ORDER BY name_tiers(y.compte_tiers, y.table_tiers, ''R''), p.date_piece, p.num_piece';
	ELSIF(type_ = 'C')THEN
		query_ = query_ || ' ORDER BY c.num_compte, p.date_piece, p.num_piece';
	ELSE
		query_ = query_ || ' ORDER BY n.code_ref, p.date_piece, p.num_piece';
	END IF;
	RAISE NOTICE 'query_ : %',query_;
	IF(query_ IS NOT NULL AND query_ != '')THEN
		FOR data_ IN EXECUTE query_
		LOOP
			IF(COALESCE(data_.id, 0) > 0)THEN
				IF(last_code_ != data_.code)THEN
					debit_si_ = (select compta_et_debit_initial(agence_, societe_, data_.id, data_.date_piece, journal_, type_, false, data_.table_tiers));
					credit_si_ = (select compta_et_credit_initial(agence_, societe_, data_.id, data_.date_piece, journal_, type_, false, data_.table_tiers));		
					solde_ = debit_si_ - credit_si_;
					IF(solde_ >= 0)THEN
						debit_si_ = solde_;
						credit_si_ = 0;
					ELSE
						debit_si_ = 0;
						credit_si_ = -(solde_);
					END IF;
					last_code_ = data_.code;
					INSERT INTO table_et_grand_livre VALUES(data_.id, data_.code, data_.intitule, data_.compte, data_.designation, 'S.I', (data_.date_piece - interval '1 day'), to_char((data_.date_piece - interval '1 day'), 'dd')::integer, 'SOLDE INITIAL', '', data_.journal, debit_si_, credit_si_, data_.compte_tiers, data_.table_tiers, true);
				END IF;
				INSERT INTO table_et_grand_livre VALUES(data_.id, data_.code, data_.intitule, data_.compte, data_.designation, data_.num_piece, data_.date_piece, data_.jour, UPPER(data_.libelle), data_.lettrage, data_.journal, data_.debit, data_.credit, data_.compte_tiers, data_.table_tiers, false);
			END IF;
		END LOOP;			
	END IF;
	FOR data_ IN SELECT DISTINCT _id, _code, _intitule, _compte, _designation, _table_tiers, SUM(_debit) AS _debit, SUM(_credit) AS _credit FROM table_et_grand_livre GROUP BY _id, _code, _intitule, _compte, _designation, _table_tiers
	LOOP
		debit_si_ = data_._debit - data_._credit;
		IF(debit_si_ > 0)THEN
			credit_si_ = 0;
		ELSE
			credit_si_ = - debit_si_;
			debit_si_ = 0;
		END IF;
		-- INSERT INTO table_et_grand_livre VALUES(data_._id, data_._code, data_._intitule, data_._compte, data_._designation, 'S.F', date_fin_, to_char((date_fin_ - interval '1 day'), 'dd')::integer, 'Solde Final', '', '', debit_si_, credit_si_, data_._table_tiers, true);
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_grand_livre ORDER BY _solde DESC, _code, _date_piece, _numero;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_grand_livre(bigint, bigint, character varying, date, date, bigint, character varying, boolean)
  OWNER TO postgres;
