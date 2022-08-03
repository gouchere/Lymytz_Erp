-- Function: com_et_ration(bigint, bigint, bigint, character varying, date, date, character varying, character varying)
-- DROP FUNCTION com_et_ration(bigint, bigint, bigint, character varying, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION com_et_ration(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN tiers_ character varying, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN valorise_by_ character varying)
  RETURNS TABLE(tiers bigint, code character varying, nom character varying, article bigint, reference character varying, designation character varying, unite bigint, libelle character varying, periode character varying, prix double precision, ration double precision, prevu double precision, rang integer) AS
$BODY$
DECLARE 
	ligne_ RECORD;
	dates_ RECORD;
	
	suspendu_ BIGINT DEFAULT 0;
	
	ecart_ DOUBLE PRECISION DEFAULT 0;
	nombre_ DOUBLE PRECISION DEFAULT 0;
	taux_ DOUBLE PRECISION DEFAULT 0;
	prix_ DOUBLE PRECISION DEFAULT 0;
	quantite_ DOUBLE PRECISION DEFAULT 0;
	
	query_ CHARACTER VARYING DEFAULT 'SELECT SUM(y.quantite) FROM yvs_com_ration y INNER JOIN yvs_com_doc_ration d ON y.doc_ration = d.id INNER JOIN yvs_base_depots e ON d.depot = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE d.statut = ''V'' AND a.societe = '||COALESCE(societe_, 0);
	query_quantite_ CHARACTER VARYING DEFAULT '';
    
BEGIN
-- 	DROP TABLE IF EXISTS table_et_ration;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ration(_tiers bigint, _code character varying, _nom character varying, _article bigint, _reference character varying, _designation character varying, _unite bigint, _libelle character varying, _periode character varying, _prix double precision, _ration double precision, _prevu double precision, _rang integer); 
	DELETE FROM table_et_ration;
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND e.agence = '||COALESCE(agence_, 0);
	END IF;
	IF(COALESCE(depot_, 0) > 0)THEN
		query_ = query_ || ' AND d.depot = '||COALESCE(depot_, 0);
	END IF;
	ecart_ := (SELECT (date_fin_ - date_debut_) + 1);
	
	FOR ligne_ IN SELECT y.personnel AS tiers, t.code_tiers AS code, CONCAT(t.nom, ' ', t.prenom) AS nom, y.article, a.ref_art AS reference, a.designation, y.conditionnement AS unite, u.reference AS libelle, c.prix, y.quantite, y.periode, y.actif, y.date_prise_effet
		FROM yvs_com_param_ration y INNER JOIN yvs_base_tiers t ON y.personnel = t.id INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_conditionnement c ON y.conditionnement = c.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id 
		WHERE t.societe = societe_ AND y.date_prise_effet <= date_fin_ AND ((COALESCE(tiers_, '') IN ('', ' ', '0', '0-') AND t.id IS NOT NULL) OR (COALESCE(tiers_, '') NOT IN ('', ' ', '0', '0-') AND t.id::character varying in (select val from regexp_split_to_table(tiers_,'-') val)))
		ORDER BY t.nom, t.prenom, t.code_tiers
	LOOP
		nombre_ = COALESCE(ligne_.periode, 0);
		IF(COALESCE(nombre_, 0) = 0)THEN
			nombre_ = 1;
		END IF;
		IF(periode_ = 'A')THEN
			taux_ = 365;
		ELSIF(periode_ = 'T')THEN
			taux_ =  90;
		ELSIF(periode_ = 'M')THEN
			taux_ = 30;
		ELSIF(periode_ = 'S')THEN
			taux_ = 7;
		ELSIF(periode_ = 'J')THEN
			taux_ = 1;
		END IF;
		ligne_.quantite = (COALESCE(ligne_.quantite, 1) * taux_) / nombre_;
		query_quantite_ = query_ || ' AND y.personnel = ' || ligne_.tiers || ' AND y.article = ' || ligne_.article || ' AND y.conditionnement = ' || ligne_.unite;
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, TRUE)
		LOOP
			IF(COALESCE(valorise_by_, 'V') = 'R')THEN
				prix_ := (SELECT get_pr(ligne_.article, COALESCE(depot_, 0), 0, dates_.date_fin, ligne_.unite, 0));
			ELSE
				prix_ = COALESCE(ligne_.prix);
			END IF;
			query_quantite_ = query_quantite_ || ' AND d.date_fiche BETWEEN ' || QUOTE_LITERAL(dates_.date_debut) || ' AND ' || QUOTE_LITERAL(dates_.date_fin);
			EXECUTE query_quantite_ INTO quantite_;
			SELECT INTO suspendu_ COUNT(s.id) FROM yvs_com_param_ration_suspension s WHERE s.personnel = ligne_.tiers AND (dates_.date_debut BETWEEN s.debut_suspension AND s.fin_suspension OR dates_.date_fin BETWEEN s.debut_suspension AND s.fin_suspension);
			IF(ligne_.actif OR COALESCE(quantite_, 0) > 0 OR COALESCE(suspendu_, 0) < 1)THEN
				INSERT INTO table_et_ration VALUES (ligne_.tiers, ligne_.code, ligne_.nom, ligne_.article, ligne_.reference, ligne_.designation, ligne_.unite, ligne_.libelle, dates_.intitule, prix_, quantite_, ligne_.quantite, dates_.position);
			END IF;
		END LOOP;
	END LOOP;
	return QUERY SELECT DISTINCT * FROM table_et_ration ORDER BY _reference, _nom, _code, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ration(bigint, bigint, bigint, character varying, date, date, character varying, character varying)
  OWNER TO postgres;
