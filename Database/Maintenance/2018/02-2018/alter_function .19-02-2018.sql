-- Function: com_et_objectif_by_periode(bigint, bigint, character varying)

-- DROP FUNCTION com_et_ration(bigint, bigint);

CREATE OR REPLACE FUNCTION com_et_ration(IN id_ bigint, IN societe_ bigint)
  RETURNS TABLE(code character varying, nom character varying, "reference" character varying, article character varying, quantite double precision, unite character varying, statut character varying) AS
$BODY$
DECLARE 
	ligne_ RECORD;
	ration_ RECORD;
	quantite_ DOUBLE PRECISION DEFAULT 0;
	taux_ DOUBLE PRECISION DEFAULT 0;
	statut_ CHARACTER VARYING DEFAULT 'En Attente';
    
BEGIN
-- 	DROP TABLE IF EXISTS table_ration;
	CREATE TEMP TABLE IF NOT EXISTS table_ration(_code character varying, _nom character varying, _reference character varying, _article character varying, _quantite double precision, _unite character varying, _statut character varying); 
	DELETE FROM table_ration;
	FOR ligne_ IN SELECT a.ref_art, a.designation, p.quantite, u.reference, t.code_tiers, CONCAT(t.nom, ' ', t.prenom) AS nom, p.periode, y.nbr_jr_usine, p.article, p.personnel, p.conditionnement, p.proportionnel, p.actif 
		FROM yvs_com_param_ration p INNER JOIN yvs_com_periode_ration e ON p.date_prise_effet <= e.date_debut INNER JOIN yvs_base_articles a ON p.article = a.id INNER JOIN yvs_base_conditionnement c ON p.conditionnement = c.id
		INNER JOIN yvs_base_tiers t ON p.personnel = t.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id INNER JOIN yvs_com_doc_ration y ON y.periode = e.id
		WHERE y.id = id_ AND f.societe = societe_ ORDER BY t.nom, t.prenom, t.code_tiers
	LOOP
		quantite_ = ligne_.quantite;
		statut_ = 'En Attente';
		if (ligne_.proportionnel) THEN
			taux_ = 1;
			if (ligne_.nbr_jr_usine < ligne_.periode) THEN
				taux_ = ligne_.nbr_jr_usine::decimal / ligne_.periode::decimal;
			END IF;
			quantite_ = round((quantite_ * taux_)::decimal, 0);
                END IF;
		SELECT INTO ration_ r.id, r.quantite FROM yvs_com_ration r INNER JOIN yvs_com_doc_ration d ON r.doc_ration = d.id WHERE d.id = id_ AND r.personnel = ligne_.personnel AND r.article = ligne_.article AND r.conditionnement = ligne_.conditionnement;
		IF(ration_.id IS NOT NULL AND ration_.id > 0)THEN
			quantite_ = ration_.quantite;
			statut_ = 'Pris';
		END IF;
		IF(ligne_.actif OR (ration_.id IS NOT NULL AND ration_.id > 0))THEN
			INSERT INTO table_ration VALUES (ligne_.code_tiers, ligne_.nom, ligne_.ref_art, ligne_.designation, quantite_, ligne_.reference, statut_);
		END IF;
	END LOOP;
	return QUERY SELECT DISTINCT * FROM table_ration ORDER BY _nom, _code;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ration(bigint, bigint)
  OWNER TO postgres;
