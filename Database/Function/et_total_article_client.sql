-- Function: et_total_article_client(bigint, bigint, date, date, character varying)
DROP FUNCTION et_total_article_client(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_client(IN agence_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, marge_min double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
	societe_ BIGINT DEFAULT 0; 
BEGIN    
	IF(COALESCE(agence_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_agences a WHERE a.id = agence_;
	ELSIF(COALESCE(client_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_com_client d INNER JOIN yvs_base_tiers a ON d.tiers = a.id WHERE d.id = client_;
	END IF;
	RETURN QUERY SELECT * FROM com_et_total_articles(societe_, agence_, 0, 0, 0, client_, 0, 0, date_debut_, date_fin_, '', '', periode_, 0, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_client(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
