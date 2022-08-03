-- Function: prod_et_production_consommation(bigint, bigint, bigint, bigint, date, date, character varying, integer, character varying)
-- DROP FUNCTION prod_et_production_consommation(bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION prod_et_production_consommation(IN article_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN valorise_by_ character varying)
  RETURNS TABLE(article bigint, code character varying, designation character varying, unite bigint, reference character varying, periode character varying, production double precision, intitule character varying, prix_vente double precision, prix_achat double precision, prix_revient double precision, prix_prod double precision, quantite double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	dates_ RECORD;
	data_ RECORD;
	ligne_ RECORD;
	
	query_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT a.id, a.ref_art, a.designation, c.id as unite, u.reference, c.prix as prix_vente, c.prix_achat, c.prix_prod, SUM(f.quantite) AS quantite
			FROM yvs_prod_of_suivi_flux f INNER JOIN yvs_prod_flux_composant cp ON f.composant = cp.id INNER JOIN yvs_prod_composant_of co ON cp.composant = co.id INNER JOIN yvs_base_articles a ON co.article = a.id
			INNER JOIN yvs_base_conditionnement c ON co.unite = c.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_prod_ordre_fabrication o ON co.ordre_fabrication = o.id
			INNER JOIN yvs_prod_nomenclature n ON o.nomenclature = n.id INNER JOIN yvs_prod_suivi_operations p ON f.id_operation = p.id INNER JOIN yvs_prod_session_of so ON p.session_of = so.id 
			INNER JOIN yvs_prod_session_prod s ON s.id = so.session_prod INNER JOIN yvs_prod_equipe_production e ON s.equipe = e.id INNER JOIN yvs_base_articles y ON o.article = y.id 
			WHERE cp.sens=''S'' AND n.unite_mesure = '||COALESCE(article_, 0);	
	suivi_group_ CHARACTER VARYING DEFAULT 'a.id, c.id, u.id';							
	save_ CHARACTER VARYING;	
	
	prix_revient_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	production_ DOUBLE PRECISION DEFAULT 0;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_production_consommation;
	CREATE TEMP TABLE IF NOT EXISTS table_production_consommation(_article_ bigint, _code_ character varying, _designation_ character varying, _unite_ bigint, _reference_ character varying, _periode_ character varying, _production_ double precision, _intitule_ character varying, _prix_vente_ double precision, _prix_achat_ double precision, _prix_revient_ double precision, _prix_prod_ double precision, _quantite_ double precision, _valeur_ double precision, _rang_ integer);
	DELETE FROM table_production_consommation;
	FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_,  periode_, TRUE)
	LOOP
		SELECT INTO data_ COALESCE(SUM(y.quantite), 0) AS production, u.reference FROM yvs_prod_ordre_fabrication o LEFT JOIN yvs_prod_declaration_production y ON y.ordre = o.id
			INNER JOIN yvs_prod_nomenclature n ON o.nomenclature = n.id INNER JOIN yvs_prod_session_of so ON o.id = so.ordre INNER JOIN yvs_prod_session_prod s ON s.id = so.session_prod
			INNER JOIN yvs_base_conditionnement c ON n.unite_mesure = c.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id
			WHERE y.statut NOT IN ('E', 'W', 'A') AND n.unite_mesure = COALESCE(article_, 0) AND s.date_session BETWEEN dates_.date_debut AND dates_.date_fin GROUP BY u.reference;
		production_ = COALESCE(data_.production, 0);
		save_ = query_ || ' AND s.date_session BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
		FOR ligne_ IN EXECUTE save_ || 'GROUP BY '||suivi_group_
		LOOP
			IF(COALESCE(valorise_by_ , 'R') = 'V')THEN
				valeur_ = ligne_.prix_vente * ligne_.quantite;
			ELSIF(COALESCE(valorise_by_ , 'R') = 'A')THEN
				valeur_ = ligne_.prix_achat * ligne_.quantite;
			ELSIF(COALESCE(valorise_by_ , 'R') = 'P')THEN
				valeur_ = ligne_.prix_prod * ligne_.quantite;
			ELSE
				prix_revient_ := (SELECT get_pr(ligne_.id, 0, 0, date_fin_, ligne_.unite, 0));
				valeur_ = prix_revient_ * ligne_.quantite;
			END IF;
			INSERT INTO table_production_consommation VALUES(ligne_.id, ligne_.ref_art, ligne_.designation, ligne_.unite, ligne_.reference, dates_.intitule, production_, data_.reference, COALESCE(ligne_.prix_vente, 0), COALESCE(ligne_.prix_achat, 0), COALESCE(prix_revient_, 0), COALESCE(ligne_.prix_prod, 0), COALESCE(ligne_.quantite, 0), COALESCE(valeur_, 0), dates_.position);
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_production_consommation ORDER BY _rang_, _code_, _designation_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION prod_et_production_consommation(bigint, date, date, character varying, character varying)
  OWNER TO postgres;
