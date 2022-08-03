-- Function: et_balance_age_client(bigint, bigint, integer, integer, date, date)

-- DROP FUNCTION et_balance_age_client(bigint, bigint, integer, integer, date, date);

CREATE OR REPLACE FUNCTION et_balance_age_client(IN societe_ bigint, IN client_ bigint, IN ecart_ integer, IN colonne_ integer, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(client bigint, code character varying, nom character varying, jour character varying, solde double precision, rang integer, is_total boolean) AS
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
		SELECT INTO solde_ COALESCE(SUM(COALESCE(m.montant,0) - COALESCE(m.avance,0)), 0) FROM yvs_com_mensualite_facture_vente m INNER JOIN yvs_com_doc_ventes d ON m.facture = d.id
			WHERE d.statut = 'V' AND d.type_doc = 'FV' AND d.client = client_;
		INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, 'SOLDE', solde_, 0, FALSE);
		
		-- Etallage du solde
		fin_ = ecart_;
		SELECT INTO solde_ COALESCE(SUM(COALESCE(m.montant,0) - COALESCE(m.avance,0)), 0) FROM yvs_com_mensualite_facture_vente m INNER JOIN yvs_com_doc_ventes d ON m.facture = d.id
			WHERE d.statut = 'V' AND d.type_doc = 'FV' AND d.client = client_ AND (m.date_reglement BETWEEN date_debut_ AND date_fin_) AND ((current_date - m.date_reglement) BETWEEN debut_ AND (fin_-1));
		INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, debut_||' - '||fin_, solde_, 1, FALSE);
		FOR i IN 2..colonne_ LOOP
			debut_ = fin_;
			fin_ = fin_ + ecart_;
			SELECT INTO solde_ COALESCE(SUM(COALESCE(m.montant,0) - COALESCE(m.avance,0)), 0) FROM yvs_com_mensualite_facture_vente m INNER JOIN yvs_com_doc_ventes d ON m.facture = d.id
				WHERE d.statut = 'V' AND d.type_doc = 'FV' AND d.client = client_ AND (m.date_reglement BETWEEN date_debut_ AND date_fin_) AND ((current_date - m.date_reglement) BETWEEN debut_ AND (fin_-1));		    
			INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, debut_||' - '||fin_, solde_, i , FALSE);
		END LOOP;	
		debut_ = fin_;
		fin_ = fin_ + 1000000;
		SELECT INTO solde_ COALESCE(SUM(COALESCE(m.montant,0) - COALESCE(m.avance,0)), 0) FROM yvs_com_mensualite_facture_vente m INNER JOIN yvs_com_doc_ventes d ON m.facture = d.id
			WHERE d.statut = 'V' AND d.type_doc = 'FV' AND d.client = client_ AND (m.date_reglement BETWEEN date_debut_ AND date_fin_) AND ((current_date - m.date_reglement) BETWEEN debut_ AND (fin_-1));
		INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, '+ de '||(fin_ - 1000000), solde_, (colonne_ + 1), FALSE);	
	END IF;
	DELETE FROM client_balance_age WHERE _client = (SELECT _client FROM client_balance_age GROUP BY _client HAVING SUM(_solde) = 0);
	RETURN QUERY SELECT * FROM client_balance_age ORDER BY _is_total, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_balance_age_client(bigint, bigint, integer, integer, date, date)
  OWNER TO postgres;
