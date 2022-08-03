-- Function: et_total_articles(bigint, bigint, date, date, character varying)
DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, marge_min double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    
BEGIN    
	RETURN QUERY SELECT * FROM et_total_articles(societe_, agence_, date_debut_, date_fin_, '', periode_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
