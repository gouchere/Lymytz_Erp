-- Function: com_get_valeur_objectif(bigint, bigint, bigint)

-- DROP FUNCTION com_get_valeur_objectif(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION com_get_valeur_objectif(commercial_ bigint, periode_ bigint, objectif_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
	objectif RECORD;

	colonne CHARACTER VARYING DEFAULT '*';
	condition CHARACTER VARYING DEFAULT '';
	query CHARACTER VARYING DEFAULT '';
	
	valeur DOUBLE PRECISION DEFAULT 0;
	
	count_article BIGINT DEFAULT 0;
	count_client BIGINT DEFAULT 0;
	count_zone BIGINT DEFAULT 0;
    
BEGIN
	-- Recuperation des informations de l'objectif
	SELECT INTO objectif * FROM yvs_com_modele_objectif WHERE id = objectif_;

	-- Verification si l'objectif porte sur les articles
	SELECT INTO count_article COALESCE(count(e.id), 0) FROM yvs_com_cible_objectif e WHERE e.objectif = objectif_ AND table_externe = 'BASE_ARTICLE';
	IF(count_article > 0)THEN
		condition = ' AND (c.article in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''BASE_ARTICLE'' AND o.id = '||objectif_||'))';
	END IF;
	-- Verification si l'objectif porte sur les clients
	SELECT INTO count_client COALESCE(count(e.id), 0) FROM yvs_com_cible_objectif e WHERE e.objectif = objectif_ AND table_externe = 'BASE_CLIENT';
	IF(count_client > 0)THEN
		condition = condition ||' AND (d.client in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''BASE_CLIENT'' AND o.id = '||objectif_||'))';
	END IF;
	-- Verification si l'objectif porte sur les zones
	SELECT INTO count_zone COALESCE(count(e.id), 0) FROM yvs_com_cible_objectif e WHERE e.objectif = objectif_ AND table_externe = 'DOC_ZONE';
	IF(count_zone > 0)THEN
		condition = condition ||' AND (d.adresse in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''DOC_ZONE'' AND o.id = '||objectif_||'))';
	END IF;

	query = 'SELECT * FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.id = c.doc_vente INNER JOIN yvs_com_commercial_vente y ON y.facture = d.id
		WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND y.commercial = '||commercial_||''||condition;	

	CASE objectif.indicateur
		WHEN 'CA' THEN 	
			colonne = 'COALESCE(sum(c.prix_total), 0)';
		WHEN 'QUANTITE' THEN 
			colonne = 'COALESCE(sum(c.quantite), 0)';
		WHEN 'MARGE' THEN 
			colonne = '*';
		ELSE
			colonne = '*';
	END CASE;
	query = (select replace(query, '*', colonne));
	RAISE NOTICE 'query % ',query;
	execute query INTO valeur;

	return COALESCE(valeur, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_valeur_objectif(bigint, bigint, bigint)
  OWNER TO postgres;
  
  -- Function: com_et_objectif_one_periode(BIGINT, BIGINT)

-- DROP FUNCTION com_et_objectif_one_periode(BIGINT, BIGINT);

CREATE OR REPLACE FUNCTION com_et_objectif_by_periode(periode_ BIGINT, objectif_ BIGINT)
  RETURNS TABLE(commercial BIGINT, code CHARACTER VARYING, nom CHARACTER VARYING, periode BIGINT, entete CHARACTER VARYING, attente DOUBLE PRECISION, valeur DOUBLE PRECISION, rang INTEGER) AS
$BODY$
DECLARE 
	ligne_ RECORD;
	valeur_ DOUBLE PRECISION DEFAULT 0;

	i INTEGER DEFAULT 0;
    
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif_by_periode;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif_by_periode(_commercial BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif_by_periode;
	FOR ligne_ IN SELECT c.id, c.code_ref AS code, c.nom, c.prenom, p.code_ref AS entete, o.valeur AS attente FROM yvs_com_comerciale c INNER JOIN yvs_com_objectifs_comercial o ON o.commercial = c.id INNER JOIN yvs_com_periode_objectif p ON o.periode = p.id WHERE o.periode = periode_ AND o.objectif = objectif_
	LOOP
		i = i + 1;
		valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_));
		INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom ||' '||ligne_.prenom, periode_, ligne_.entete, ligne_.attente, valeur_, i);
	END LOOP;
	return QUERY SELECT * FROM table_objectif_by_periode ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_et_objectif_by_periode(BIGINT, BIGINT)
  OWNER TO postgres;
  
  
  -- Function: com_et_objectif(BIGINT, BIGINT)

-- DROP FUNCTION com_et_objectif(BIGINT, BIGINT);

CREATE OR REPLACE FUNCTION com_et_objectif(societe_ BIGINT, objectif_ BIGINT)
  RETURNS TABLE(commercial BIGINT, code CHARACTER VARYING, nom CHARACTER VARYING, periode BIGINT, entete CHARACTER VARYING, attente DOUBLE PRECISION, valeur DOUBLE PRECISION, rang INTEGER) AS
$BODY$
DECLARE 
	periode_ BIGINT;
    
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif(_commercial BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif;
	FOR periode_ IN SELECT p.id FROM yvs_com_periode_objectif p WHERE p.societe = societe_
	LOOP
		INSERT INTO table_objectif SELECT * FROM com_et_objectif_by_periode(periode_, objectif_);
	END LOOP;
	return QUERY SELECT * FROM table_objectif ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_et_objectif(BIGINT, BIGINT)
  OWNER TO postgres;
