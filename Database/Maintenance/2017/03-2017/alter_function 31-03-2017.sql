
  -- Function: et_balance_age_client(BIGINT, BIGINT, INTEGER, INTEGER)

-- DROP FUNCTION et_balance_age_client(BIGINT, BIGINT, INTEGER, INTEGER, DATE, DATE);

CREATE OR REPLACE FUNCTION et_balance_age_client(IN societe_ BIGINT, IN client_ BIGINT, IN ecart_ INTEGER, IN colonne_ INTEGER, date_debut_ DATE, date_fin_ DATE)
  RETURNS TABLE(client BIGINT, code CHARACTER VARYING, nom CHARACTER VARYING, jour CHARACTER VARYING, solde DOUBLE PRECISION, rang INTEGER, is_total BOOLEAN) AS
$BODY$
DECLARE
    _client_ RECORD;
    facture_ BIGINT;
    ttc_ DOUBLE PRECISION DEFAULT 0;
    avance_ DOUBLE PRECISION DEFAULT 0;
    solde_ DOUBLE PRECISION DEFAULT 0;
    debut_ INTEGER DEFAULT 0;
    fin_ INTEGER DEFAULT 0;
    
    i INTEGER DEFAULT 0;

BEGIN    
	DROP TABLE IF EXISTS client_balance_age;
	CREATE TEMP TABLE IF NOT EXISTS client_balance_age(_client BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _jour CHARACTER VARYING, _solde DOUBLE PRECISION, _rang INTEGER, _is_total BOOLEAN);
	SELECT INTO _client_ c.code_client AS code, c.nom FROM yvs_com_client c WHERE c.id = client_;
	IF(_client_ IS NOT NULL)THEN
		-- Solde du client
		SELECT INTO avance_ COALESCE(SUM(p.montant), 0) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id
			WHERE p.statut_piece = 'P' AND d.client = client_ AND p.date_paiement BETWEEN date_debut_ AND date_fin_;
		FOR facture_ IN SELECT d.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id WHERE d.type_doc = 'FV' AND d.statut = 'V' AND d.client = client_ AND e.date_entete BETWEEN date_debut_ AND date_fin_
		LOOP
			ttc_ = ttc_ + (select get_ttc_vente(facture_));
		END LOOP;
		solde_ = (ttc_ - avance_);
		INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, 'SOLDE', solde_, 0, FALSE);
		
		-- Etallage du solde
		fin_ = ecart_;
		SELECT INTO solde_ COALESCE(SUM(m.montant - m.avance), 0) FROM yvs_com_mensualite_facture_vente m INNER JOIN yvs_com_doc_ventes d ON m.facture = d.id
			WHERE d.statut = 'V' AND d.type_doc = 'FV' AND d.client = client_ AND (m.date_reglement BETWEEN date_debut_ AND date_fin_) AND ((current_date - m.date_reglement) BETWEEN debut_ AND (fin_-1));
		INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, debut_||' - '||fin_, solde_, 1, FALSE);
		FOR i IN 2..colonne_ LOOP
			debut_ = fin_;
			fin_ = fin_ + ecart_;
			SELECT INTO solde_ COALESCE(SUM(m.montant - m.avance), 0) FROM yvs_com_mensualite_facture_vente m INNER JOIN yvs_com_doc_ventes d ON m.facture = d.id
				WHERE d.statut = 'V' AND d.type_doc = 'FV' AND d.client = client_ AND (m.date_reglement BETWEEN date_debut_ AND date_fin_) AND ((current_date - m.date_reglement) BETWEEN debut_ AND (fin_-1));		    
			INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, debut_||' - '||fin_, solde_, i , FALSE);
		END LOOP;	
		debut_ = fin_;
		fin_ = fin_ + 1000000;
		SELECT INTO solde_ COALESCE(SUM(m.montant - m.avance), 0) FROM yvs_com_mensualite_facture_vente m INNER JOIN yvs_com_doc_ventes d ON m.facture = d.id
			WHERE d.statut = 'V' AND d.type_doc = 'FV' AND d.client = client_ AND (m.date_reglement BETWEEN date_debut_ AND date_fin_) AND ((current_date - m.date_reglement) BETWEEN debut_ AND (fin_-1));
		INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, '+ de '||(fin_ - 1000000), solde_, (colonne_ + 1), FALSE);	
	END IF;
	DELETE FROM client_balance_age WHERE _client = (SELECT _client FROM client_balance_age GROUP BY _client HAVING SUM(_solde) = 0);
	RETURN QUERY SELECT * FROM client_balance_age ORDER BY _is_total, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_balance_age_client(BIGINT, BIGINT, INTEGER, INTEGER, DATE, DATE)
  OWNER TO postgres;