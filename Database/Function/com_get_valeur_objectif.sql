-- Function: com_get_valeur_objectif(bigint, bigint, bigint)

-- DROP FUNCTION com_get_valeur_objectif(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION com_get_valeur_objectif(commercial_ bigint, periode_ bigint, objectif_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
	objectif RECORD;
	periode RECORD;

	colonne CHARACTER VARYING DEFAULT '*';
	condition CHARACTER VARYING DEFAULT '';
	query CHARACTER VARYING DEFAULT '';
	
	valeur DOUBLE PRECISION DEFAULT 0;
	
	count_article BIGINT DEFAULT 0;
	count_client BIGINT DEFAULT 0;
	count_zone BIGINT DEFAULT 0;
    
BEGIN
	-- Recuperation des informations de la periode
	SELECT INTO periode * FROM yvs_com_periode_objectif WHERE id = periode_;
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
		condition = condition ||' AND ((d.adresse in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''DOC_ZONE'' AND o.id = '||objectif_||')) 
					OR (d.adresse in (SELECT f.id FROM yvs_dictionnaire f WHERE f.parent in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''DOC_ZONE'' AND o.id = '||objectif_||'))))';
	END IF;

	query = 'SELECT * FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.id = c.doc_vente 
			INNER JOIN yvs_com_entete_doc_vente t ON d.entete_doc = t.id
			INNER JOIN yvs_com_commercial_vente y ON y.facture = d.id
		WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND t.date_entete BETWEEN '''||periode.date_debut||''' AND '''||periode.date_fin||''' AND y.commercial = '||commercial_||''||condition;	

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
