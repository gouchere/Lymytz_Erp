-- Function: com_et_listing_achat(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying)
-- DROP FUNCTION com_et_listing_achat(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION com_et_listing_achat(societe_ bigint, agence_ bigint, depot_ bigint, fournisseur_ bigint, article_ bigint, unite_ bigint, date_debut_ date, date_fin_ date, type_ character varying)
  RETURNS SETOF yvs_com_contenu_doc_achat AS
$BODY$
declare 

   data_ RECORD;
      
   query_ CHARACTER VARYING DEFAULT 'SELECT  c.* FROM yvs_com_contenu_doc_achat c INNER JOIN yvs_com_doc_achats d ON c.doc_achat = d.id INNER JOIN yvs_agences a ON d.agence = a.id 
		LEFT JOIN yvs_base_depots p ON d.depot_reception = p.id
		INNER JOIN yvs_base_conditionnement o ON c.conditionnement = o.id INNER JOIN yvs_base_unite_mesure m ON o.unite = m.id INNER JOIN yvs_base_articles b ON c.article = b.id
		WHERE d.type_doc = ''FA'' AND d.statut = ''V'' AND d.date_doc BETWEEN'||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND a.societe = '||societe_;
   
begin 	
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(depot_, 0) > 0)THEN
		query_ = query_ || ' AND p.id = '||depot_;
	END IF;
	IF(COALESCE(fournisseur_, 0) > 0)THEN
		query_ = query_ || ' AND y.id = '||fournisseur_;
	END IF;
	IF(COALESCE(article_, 0) > 0)THEN
		query_ = query_ || ' AND b.id = '||article_;
	END IF;
	IF(COALESCE(unite_, 0) > 0)THEN
		query_ = query_ || ' AND m.id = '||unite_;
	END IF;
	IF(POSITION('R' IN COALESCE(type_, '')) > 0)THEN
-- 		query_ = query_ || ' AND c.prix < c.pr';
	END IF;
	IF(POSITION('M' IN COALESCE(type_, '')) > 0)THEN
-- 		query_ = query_ || ' AND c.prix < c.puv_min';
	END IF;
	RETURN QUERY EXECUTE query_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_listing_achat(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
