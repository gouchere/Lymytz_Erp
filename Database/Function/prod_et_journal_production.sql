-- Function: prod_et_journal_production(bigint, bigint, bigint, character varying, date, date, character varying, integer, character varying, character varying)

-- DROP FUNCTION prod_et_journal_production(bigint, bigint, bigint, character varying, date, date, character varying, integer, character varying, character varying);

CREATE OR REPLACE FUNCTION prod_et_journal_production(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN article_ character varying, IN date_debut_ date, IN date_fin_ date, IN categorie_ character varying, IN cumule_by_ integer, IN valorise_by_ character varying, IN type_ character varying)
  RETURNS TABLE(id bigint, code character varying, designation character varying, unite bigint, reference character varying, production double precision, mp bigint, code_mp character varying, designation_mp character varying, unite_mp bigint, reference_mp character varying, equipe bigint, code_equipe character varying, nom_equipe character varying, prix_vente double precision, prix_achat double precision, prix_revient double precision, prix_prod double precision, quantite double precision, valeur double precision) AS
$BODY$
DECLARE 
	data_ RECORD;
	ligne_ RECORD;
	
	query_ CHARACTER VARYING DEFAULT 'SELECT a.id, a.ref_art, a.designation, c.id as unite, u.reference, COALESCE(SUM(y.quantite), 0) AS production FROM yvs_prod_ordre_fabrication o LEFT JOIN yvs_prod_declaration_production y ON y.ordre = o.id AND y.statut NOT IN (''E'', ''W'', ''A'')
					INNER JOIN yvs_prod_nomenclature n ON o.nomenclature = n.id INNER JOIN yvs_prod_session_of so ON o.id = so.ordre INNER JOIN yvs_prod_session_prod s ON s.id = so.session_prod 
					INNER JOIN yvs_base_depots d ON d.id = s.depot INNER JOIN yvs_base_conditionnement c ON c.id = n.unite_mesure INNER JOIN yvs_base_articles a ON c.article = a.id 
					INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = '||COALESCE(societe_, 0);						
	query_group_ CHARACTER VARYING DEFAULT 'a.id, c.id, u.id ORDER BY a.ref_art, u.reference';	
	save_ CHARACTER VARYING;		
	suivis_ CHARACTER VARYING;		
	suivi_group_ CHARACTER VARYING;	
	ids_ CHARACTER VARYING DEFAULT '''0''';
	id_ CHARACTER VARYING DEFAULT '0';
	
	prix_revient_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	production_ DOUBLE PRECISION DEFAULT 0;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_journal_production;
	CREATE TEMP TABLE IF NOT EXISTS table_journal_production(_id_ bigint, _code_ character varying, _designation_ character varying, _unite_ bigint, _reference_ character varying, _production_ double precision, _mp_ bigint, _code_mp_ character varying, _designation_mp_ character varying, _unite_mp_ bigint, _reference_mp_ character varying, _equipe_ bigint, _code_equipe_ character varying, _nom_equipe_ character varying, _prix_vente_ double precision, _prix_achat_ double precision, _prix_revient_ double precision, _prix_prod_ double precision, _quantite_ double precision, _valeur_ double precision);
	DELETE FROM table_journal_production;
	IF(COALESCE(type_, 'A') = 'C')THEN
		query_ = 'SELECT u.id, u.code_ref as ref_art, u.designation, u.id as unite, ''TOUS'' as reference, COALESCE(SUM(y.quantite), 0) AS production FROM yvs_prod_ordre_fabrication o LEFT JOIN yvs_prod_declaration_production y ON y.ordre = o.id
			INNER JOIN yvs_prod_nomenclature n ON o.nomenclature = n.id INNER JOIN yvs_prod_session_of so ON o.id = so.ordre INNER JOIN yvs_prod_session_prod s ON s.id = so.session_prod 
			INNER JOIN yvs_base_depots d ON d.id = s.depot INNER JOIN yvs_base_conditionnement c ON c.id = n.unite_mesure INNER JOIN yvs_base_articles a ON c.article = a.id 
			INNER JOIN yvs_base_classes_stat u ON a.classe1 = u.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE y.statut NOT IN (''E'', ''W'', ''A'') AND f.societe = '||COALESCE(societe_, 0);
		query_group_ = 'u.id ORDER BY u.code_ref';
	ELSIF(COALESCE(type_, 'A') = 'CP')THEN-- Par classe statistique parent
		query_ = 'SELECT u.id, u.code_ref as ref_art, u.designation, u.id as unite, ''TOUS'' as reference, COALESCE(SUM(y.quantite), 0) AS production FROM yvs_base_classes_stat u, yvs_prod_ordre_fabrication o 
			LEFT JOIN yvs_prod_declaration_production y ON y.ordre = o.id INNER JOIN yvs_prod_nomenclature n ON o.nomenclature = n.id  INNER JOIN yvs_prod_session_of so ON o.id = so.ordre 
			INNER JOIN yvs_prod_session_prod s ON s.id = so.session_prod  INNER JOIN yvs_base_depots d ON d.id = s.depot 
			INNER JOIN yvs_base_conditionnement c ON c.id = n.unite_mesure INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id 
			WHERE y.statut NOT IN (''E'', ''W'', ''A'') AND f.societe = '||COALESCE(societe_, 0)||' AND (a.classe1 = u.id OR a.classe1 IN (SELECT base_get_sous_classe_stat(u.id, true))) AND u.parent IS NULL';
		query_group_ = 'u.id ORDER BY u.code_ref';
	END IF;
	IF(COALESCE(categorie_, '') NOT IN ('', ' '))THEN
		query_ = query_ || ' AND a.categorie = '||QUOTE_LITERAL(categorie_);
	END IF;
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND d.agence = '||agence_;
	END IF;
	IF(COALESCE(depot_, 0) > 0)THEN
		query_ = query_ || ' AND s.depot = '||depot_;
	END IF;
	IF(date_debut_ IS NOT NULL AND date_fin_ IS NOT NULL)THEN
		query_ = query_ || ' AND s.date_session BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	END IF;
	IF(COALESCE(cumule_by_, 0) = 0)THEN -- Aucun groupe
		save_ = 'SELECT DISTINCT a.id, a.ref_art, a.designation, c.id as unite, u.reference, c.prix as prix_vente, c.prix_achat, c.prix_prod, SUM(f.quantite) AS quantite, null::bigint AS equipe, null AS code_equipe, null AS nom_equipe
			FROM yvs_prod_of_suivi_flux f INNER JOIN yvs_prod_flux_composant cp ON f.composant = cp.id INNER JOIN yvs_prod_composant_of co ON cp.composant = co.id INNER JOIN yvs_base_articles a ON co.article = a.id
			INNER JOIN yvs_base_conditionnement c ON co.unite = c.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_prod_ordre_fabrication o ON co.ordre_fabrication = o.id
			INNER JOIN yvs_prod_nomenclature n ON o.nomenclature = n.id INNER JOIN yvs_prod_suivi_operations p ON f.id_operation = p.id INNER JOIN yvs_prod_session_of so ON p.session_of = so.id 
			INNER JOIN yvs_prod_session_prod s ON s.id = so.session_prod INNER JOIN yvs_prod_equipe_production e ON s.equipe = e.id INNER JOIN yvs_base_articles y ON o.article = y.id';
		suivi_group_ = 'a.id, c.id, u.id';
	ELSIF(COALESCE(cumule_by_, 0) = 1)THEN -- Grouper par equipe
		save_ = 'SELECT DISTINCT a.id, a.ref_art, a.designation, c.id as unite, u.reference, c.prix as prix_vente, c.prix_achat, c.prix_prod, SUM(f.quantite) AS quantite, s.equipe, e.reference AS code_equipe, e.nom AS nom_equipe
			FROM yvs_prod_of_suivi_flux f INNER JOIN yvs_prod_flux_composant cp ON f.composant = cp.id INNER JOIN yvs_prod_composant_of co ON cp.composant = co.id INNER JOIN yvs_base_articles a ON co.article = a.id
			INNER JOIN yvs_base_conditionnement c ON co.unite = c.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_prod_ordre_fabrication o ON co.ordre_fabrication = o.id
			INNER JOIN yvs_prod_nomenclature n ON o.nomenclature = n.id INNER JOIN yvs_prod_suivi_operations p ON f.id_operation = p.id INNER JOIN yvs_prod_session_of so ON p.session_of = so.id 
			INNER JOIN yvs_prod_session_prod s ON s.id = so.session_prod INNER JOIN yvs_prod_equipe_production e ON s.equipe = e.id INNER JOIN yvs_base_articles y ON o.article = y.id';
		suivi_group_ = 'a.id, c.id, u.id, s.equipe, e.id';
	ELSIF(COALESCE(cumule_by_, 0) = 2)THEN -- Grouper par tranche
		save_ = 'SELECT DISTINCT a.id, a.ref_art, a.designation, c.id as unite, u.reference, c.prix as prix_vente, c.prix_achat, c.prix_prod, SUM(f.quantite) AS quantite, s.tranche AS equipe, e.titre AS code_equipe, e.titre AS nom_equipe
			FROM yvs_prod_of_suivi_flux f INNER JOIN yvs_prod_flux_composant cp ON f.composant = cp.id INNER JOIN yvs_prod_composant_of co ON cp.composant = co.id INNER JOIN yvs_base_articles a ON co.article = a.id
			INNER JOIN yvs_base_conditionnement c ON co.unite = c.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_prod_ordre_fabrication o ON co.ordre_fabrication = o.id
			INNER JOIN yvs_prod_nomenclature n ON o.nomenclature = n.id INNER JOIN yvs_prod_suivi_operations p ON f.id_operation = p.id INNER JOIN yvs_prod_session_of so ON p.session_of = so.id 
			INNER JOIN yvs_prod_session_prod s ON s.id = so.session_prod INNER JOIN yvs_grh_tranche_horaire e ON s.tranche = e.id INNER JOIN yvs_base_articles y ON o.article = y.id';
		suivi_group_ = 'a.id, c.id, u.id, s.tranche, e.id';
	END IF;
	save_ = save_ || ' WHERE cp.sens=''S'' AND o.societe = '||COALESCE(societe_, 0);
	IF(date_debut_ IS NOT NULL AND date_fin_ IS NOT NULL)THEN
		save_ = save_ || ' AND s.date_session BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	END IF;
	IF(COALESCE(categorie_, '') NOT IN ('', ' '))THEN
		save_ = save_ || ' AND y.categorie = '||QUOTE_LITERAL(categorie_);
	END IF;
	IF(COALESCE(article_, '') NOT IN ('', ' ', '0'))THEN
		FOR id_ IN SELECT head FROM regexp_split_to_table(article_,',') head WHERE CHAR_LENGTH(head) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(id_);
		END LOOP;
		query_ = query_ || ' AND a.id IN ('||ids_||')';
	END IF;
	FOR data_ IN EXECUTE query_ || ' GROUP BY '||query_group_
	LOOP
		IF(COALESCE(type_, 'A') = 'C')THEN
			suivis_ = save_ || ' AND COALESCE(y.classe1, 0) = '||data_.id;
		ELSIF(COALESCE(type_, 'A') = 'CP')THEN
			suivis_ = save_ || ' AND (COALESCE(y.classe1, 0) = '||data_.id||' OR y.classe1 IN (SELECT base_get_sous_classe_stat('||data_.id||', true)))';
		ELSE
			suivis_ = save_ || ' AND n.unite_mesure = '||data_.unite;
		END IF;
		FOR ligne_ IN EXECUTE suivis_ || 'GROUP BY '||suivi_group_
		LOOP
			IF(COALESCE(valorise_by_ , 'R') = 'V')THEN
				valeur_ = ligne_.prix_vente * ligne_.quantite;
			ELSIF(COALESCE(valorise_by_ , 'R') = 'A')THEN
				valeur_ = ligne_.prix_achat * ligne_.quantite;
			ELSIF(COALESCE(valorise_by_ , 'R') = 'P')THEN
				valeur_ = ligne_.prix_prod * ligne_.quantite;
			ELSE
				prix_revient_ := (SELECT get_pr(agence_,ligne_.id, depot_, 0, date_fin_, ligne_.unite, 0));
				valeur_ = prix_revient_ * ligne_.quantite;
			END IF;
			INSERT INTO table_journal_production VALUES(data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, data_.production, ligne_.id, ligne_.ref_art, ligne_.designation, ligne_.unite, ligne_.reference, ligne_.equipe, ligne_.code_equipe::text, ligne_.nom_equipe::text, COALESCE(ligne_.prix_vente, 0), COALESCE(ligne_.prix_achat, 0), COALESCE(prix_revient_, 0), COALESCE(ligne_.prix_prod, 0), COALESCE(ligne_.quantite, 0), COALESCE(valeur_, 0));
		END LOOP;
	END LOOP;
	IF(COALESCE(type_, 'A') = 'C' OR COALESCE(type_, 'A') = 'CP')THEN
		suivis_ = save_ || ' AND y.classe1 IS NULL ';
		FOR ligne_ IN EXECUTE suivis_ || 'GROUP BY '||suivi_group_
		LOOP
			IF(COALESCE(valorise_by_ , 'R') = 'V')THEN
				valeur_ = ligne_.prix_vente * ligne_.quantite;
			ELSIF(COALESCE(valorise_by_ , 'R') = 'A')THEN
				valeur_ = ligne_.prix_achat * ligne_.quantite;
			ELSIF(COALESCE(valorise_by_ , 'R') = 'P')THEN
				valeur_ = ligne_.prix_prod * ligne_.quantite;
			ELSE
				prix_revient_ := (SELECT get_pr(agence_,ligne_.id, depot_, 0, date_fin_, ligne_.unite, 0));
				valeur_ = prix_revient_ * ligne_.quantite;
			END IF;
			INSERT INTO table_journal_production VALUES(0, 'NO CLASSE', 'NO CLASSE', 0, 'TOUS', production_, ligne_.id, ligne_.ref_art, ligne_.designation, ligne_.unite, ligne_.reference, ligne_.equipe, ligne_.code_equipe::text, ligne_.nom_equipe::text, COALESCE(ligne_.prix_vente, 0), COALESCE(ligne_.prix_achat, 0), COALESCE(prix_revient_, 0), COALESCE(ligne_.prix_prod, 0), COALESCE(ligne_.quantite, 0), COALESCE(valeur_, 0));
		END LOOP;
	END IF;
	RETURN QUERY SELECT * FROM table_journal_production ORDER BY _code_, _designation_, _reference_, _code_mp_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION prod_et_journal_production(bigint, bigint, bigint, character varying, date, date, character varying, integer, character varying, character varying)
  OWNER TO postgres;
