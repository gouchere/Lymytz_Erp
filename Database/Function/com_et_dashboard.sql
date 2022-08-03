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
	-- chiffre d'affaire validé
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
	--EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V',null, null, 0, 0, 0,users_, 0, date_debut_, date_fin_,'TV');
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
	-- EXECUTE query_ INTO autres_;
	-- EXECUTE REPLACE(query_, 't.augmentation IS FALSE', 't.augmentation IS TRUE') INTO valeur_;
	-- valeur_ = COALESCE(valeur_, 0) - COALESCE(autres_, 0);
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V',null, null, 0, 0, 0,users_, 0, date_debut_, date_fin_,'SUP');
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
		WHERE d.type_doc = ''FAV'' AND d.statut = ''V'' AND a.societe = '||societe_||' AND d.date_livraison BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	-- EXECUTE query_ INTO autres_;
	EXECUTE REPLACE(query_, 'SUM(c.prix_total)', 'COUNT(DISTINCT d.id)') INTO nombre_;
	-- query_ = REPLACE(REPLACE(query_, 'd.date_livraison', 'e.date_entete'), 'BRV', 'FAV');
	--EXECUTE query_ INTO valeur_;
	-- EXECUTE REPLACE(query_, 'SUM(c.prix_total)', 'COUNT(DISTINCT d.id)') INTO nombre_;
	-- valeur_ = COALESCE(valeur_, 0) + COALESCE(autres_, 0);
	-- nombre_ = COALESCE(nombre_, 0) + COALESCE(nombre_2, 0);
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V',null, null, 0, 0, 0,users_, 0, date_debut_, date_fin_,'AV');
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
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut = ''E'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	-- EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'E',null, null, 0, 0, 0,users_, 0, date_debut_, date_fin_,'CA');
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
	-- EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'R',null, null, 0, 0, 0,users_, 0, date_debut_, date_fin_,'CA');
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
	-- EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'A',null, null, 0, 0, 0,users_, 0, date_debut_, date_fin_,'CA');
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
	-- EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V','L', null, 0, 0, 0,users_, 0, date_debut_, date_fin_,'CA');
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
	-- EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V','R', null, 0, 0, 0,users_, 0, date_debut_, date_fin_,'CA');
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
	-- EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V','W', null, 0, 0, 0,users_, 0, date_debut_, date_fin_,'CA');
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
	-- EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V',null, 'P', 0, 0, 0,users_, 0, date_debut_, date_fin_,'CA');
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 11);
	
	-- chiffre d'affaire reglement en cours
	code_ = 'caVenteEnCoursRegle';
	libelle_ = 'Facture en cours de reglement';
	query_ = 'SELECT SUM(c.montant) FROM yvs_compta_caisse_piece_vente c INNER JOIN yvs_com_doc_ventes d ON c.vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_regle = ''R'' AND c.statut_piece=''P'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	-- EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V',null, 'R', 0, 0, 0,users_, 0, date_debut_, date_fin_,'CA');
	EXECUTE REPLACE(query_, 'SUM(c.montant)', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 12);
	
	-- chiffre d'affaire attente
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
	-- EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V',null, 'W', 0, 0, 0,users_, 0, date_debut_, date_fin_,'CA');
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
	-- EXECUTE query_ INTO valeur_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, null,null, null, 0, 0, 0,users_, 0, date_debut_, date_fin_,'CA');
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
