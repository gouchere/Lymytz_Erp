-- Function: et_balance_age_client(bigint, integer, integer, date, date)

-- DROP FUNCTION et_balance_age_client(bigint, integer, integer, date, date);

CREATE OR REPLACE FUNCTION et_balance_age_client(IN societe_ bigint, IN ecart_ integer, IN colonne_ integer, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(client bigint, code character varying, nom character varying, jour character varying, solde double precision, rang integer, is_total boolean) AS
$BODY$
DECLARE
    client_ BIGINT;
    total_ RECORD;

BEGIN    
	DROP TABLE IF EXISTS clients_balance_age;
	CREATE TEMP TABLE IF NOT EXISTS clients_balance_age(_client BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _jour CHARACTER VARYING, _solde DOUBLE PRECISION, _rang INTEGER, _is_total BOOLEAN);
	FOR client_ IN SELECT c.id FROM yvs_com_client c INNER JOIN yvs_base_tiers t ON c.tiers = t.id WHERE t.societe = societe_
	LOOP
		INSERT INTO clients_balance_age SELECT * FROM et_balance_age_client(societe_, client_, ecart_, colonne_, date_debut_, date_fin_);
	END LOOP;
	FOR total_ IN SELECT _jour AS jour, COALESCE(sum(_solde), 0) AS ttc, COALESCE(sum(_rang), 0) AS pos FROM clients_balance_age y GROUP BY _jour
	LOOP
		INSERT INTO clients_balance_age VALUES(0, 'TOTAUX', 'TOTAUX', total_.jour, total_.ttc, total_.pos + 1, true);
	END LOOP;
	RETURN QUERY SELECT * FROM clients_balance_age ORDER BY _is_total, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_balance_age_client(bigint, integer, integer, date, date)
  OWNER TO postgres;
