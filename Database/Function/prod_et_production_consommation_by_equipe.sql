-- Function: prod_et_production_consommation_by_equipe(bigint, bigint, bigint, bigint, date, date, character varying)

-- DROP FUNCTION prod_et_production_consommation_by_equipe(bigint, bigint, bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION prod_et_production_consommation_by_equipe(IN societe_ bigint, IN agence_ bigint, IN site_ bigint, IN depot_ bigint, IN date_debut_ date, IN date_fin_ date, IN valorise_by_ character varying)
  RETURNS TABLE(equipe bigint, code character varying, nom character varying, article bigint, reference character varying, designation character varying, unite bigint, numero character varying, categorie character varying, quantite double precision, prix double precision, valeur double precision, entree boolean, groupe character varying, rang integer) AS
$BODY$
DECLARE
	equipe_ RECORD;
	conso_ RECORD;
	prod_ RECORD;

	query_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT e.id as equipe, e.reference as code, e.nom FROM yvs_prod_equipe_production e INNER JOIN yvs_prod_site_production s ON e.site = s.id INNER JOIN yvs_agences a ON s.agence = a.id WHERE a.societe = '||COALESCE(societe_, 0);

	prix_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;

	groupe_ CHARACTER VARYING DEFAULT 'MATIERES PREMIERES';
	rang_ INTEGER DEFAULT 1;

	entree_ BOOLEAN DEFAULT FALSE;
BEGIN
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_et_production_consommation_by_equipe;
	CREATE TEMP TABLE IF NOT EXISTS table_et_production_consommation_by_equipe(_equipe bigint, _code character varying, _nom character varying, _article bigint, _reference character varying, _designation character varying, _unite bigint, _numero character varying, _categorie character varying, _quantite double precision, _prix double precision, _valeur double precision, _entree boolean, _groupe character varying, _rang integer);
	DELETE FROM table_et_production_consommation_by_equipe;
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND s.agence = '||agence_;
	END IF;
	IF(COALESCE(site_, 0) > 0)THEN
		query_ = query_ || ' AND e.site = '||site_;
	END IF;
	FOR equipe_ IN EXECUTE query_ || 'ORDER BY e.nom'
	LOOP
		FOR conso_ IN SELECT a.id as article, a.ref_art as reference, a.designation, a.categorie, c.id as unite, u.reference as numero, SUM(x.quantite) as quantite, c.prix, c.prix_achat, c.prix_prod, f.sens
				FROM yvs_prod_of_suivi_flux x INNER JOIN yvs_prod_flux_composant f ON x.composant = f.id INNER JOIN yvs_prod_composant_of y ON f.composant = y.id
				INNER JOIN yvs_base_conditionnement c ON y.unite = c.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_base_articles a ON y.article = a.id
				INNER JOIN yvs_prod_suivi_operations p ON x.id_operation = p.id INNER JOIN yvs_prod_session_of s ON p.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id 
				INNER JOIN yvs_base_depots d ON e.depot = d.id INNER JOIN yvs_prod_ordre_fabrication o ON s.ordre = o.id
				WHERE e.date_session BETWEEN date_debut_ AND date_fin_ AND e.equipe = equipe_.equipe
				AND ((COAlESCE(site_, 0) > 0 AND o.site_production = COALESCE(site_, 0)) OR (COAlESCE(site_, 0) < 1 AND o.site_production IS NOT NULL))
				AND ((COAlESCE(agence_, 0) > 0 AND d.agence = COALESCE(agence_, 0)) OR (COAlESCE(agence_, 0) < 1 AND d.agence IS NOT NULL))
				AND ((COAlESCE(depot_, 0) > 0 AND e.depot = COALESCE(depot_, 0)) OR (COAlESCE(depot_, 0) < 1 AND e.depot IS NOT NULL)) 
			GROUP BY a.id, c.id, u.id, f.sens ORDER BY a.designation, a.ref_art
		LOOP
			IF(conso_.sens = 'E')THEN
				entree_ = true;
			ELSE
				entree_ = false;
			END IF;
			IF(conso_.categorie = 'MP')THEN
				groupe_ = 'MATIERES PREMIERES';
				rang_ = 1;
				IF(COALESCE(valorise_by_, 'R') = 'R')THEN
					prix_ := (SELECT get_pr(agence_,conso_.article, depot_, 0, date_fin_, conso_.unite, 0));
				ELSE
					prix_ = conso_.prix_achat;				
				END IF;
			ELSE
				groupe_ = 'PRODUITS SEMI FINIS';
				rang_ = 2;
				prix_ := (SELECT get_pr(agence_,conso_.article, depot_, 0, date_fin_, conso_.unite, 0));
			END IF;	
			valeur_ = COALESCE(conso_.quantite, 0) * COALESCE(prix_, 0);
			INSERT INTO table_et_production_consommation_by_equipe VALUES(equipe_.equipe, equipe_.code, equipe_.nom, conso_.article, conso_.reference, conso_.designation, conso_.unite, conso_.numero, conso_.categorie, COALESCE(conso_.quantite, 0), COALESCE(prix_, 0), COALESCE(valeur_, 0), entree_, groupe_, rang_);
		END LOOP;
		
		entree_ = true;
		FOR prod_ IN SELECT a.id as article, a.ref_art as reference, a.designation, a.categorie, c.id as unite, u.reference as numero, SUM(y.quantite) as quantite, c.prix, c.prix_achat, c.prix_prod
				FROM yvs_prod_declaration_production y INNER JOIN yvs_base_conditionnement c ON y.conditionnement = c.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_base_articles a ON c.article = a.id
				INNER JOIN yvs_prod_session_of s ON y.session_of = s.id INNER JOIN yvs_prod_session_prod e ON s.session_prod = e.id 
				INNER JOIN yvs_base_depots d ON e.depot = d.id INNER JOIN yvs_prod_ordre_fabrication o ON s.ordre = o.id
				WHERE e.date_session BETWEEN date_debut_ AND date_fin_ AND e.equipe = equipe_.equipe
				AND ((COAlESCE(site_, 0) > 0 AND o.site_production = COALESCE(site_, 0)) OR (COAlESCE(site_, 0) < 1 AND o.site_production IS NOT NULL))
				AND ((COAlESCE(agence_, 0) > 0 AND d.agence = COALESCE(agence_, 0)) OR (COAlESCE(agence_, 0) < 1 AND d.agence IS NOT NULL))
				AND ((COAlESCE(depot_, 0) > 0 AND e.depot = COALESCE(depot_, 0)) OR (COAlESCE(depot_, 0) < 1 AND e.depot IS NOT NULL)) 
			GROUP BY a.id, c.id, u.id ORDER BY a.designation, a.ref_art
		LOOP
			IF(prod_.categorie = 'PF')THEN
				groupe_ = 'PRODUITS FINIS';
				rang_ = 3;
				IF(COALESCE(valorise_by_, 'R') = 'R')THEN
					prix_ := (SELECT get_pr(agence_,prod_.article, depot_, 0, date_fin_, prod_.unite, 0));
				ELSE
					prix_ = prod_.prix;				
				END IF;
			ELSE
				groupe_ = 'PRODUITS SEMI FINIS';
				rang_ = 2;
				prix_ := (SELECT get_pr(agence_,prod_.article, depot_, 0, date_fin_, prod_.unite, 0));
			END IF;	
			valeur_ = COALESCE(prod_.quantite, 0) * COALESCE(prix_, 0);
			INSERT INTO table_et_production_consommation_by_equipe VALUES(equipe_.equipe, equipe_.code, equipe_.nom, prod_.article, prod_.reference, prod_.designation, prod_.unite, prod_.numero, prod_.categorie, COALESCE(prod_.quantite, 0), COALESCE(prix_, 0), COALESCE(valeur_, 0), entree_, groupe_, rang_);
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_production_consommation_by_equipe ORDER BY _code, _rang, _designation, _reference, _numero, _entree desc;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION prod_et_production_consommation_by_equipe(bigint, bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
