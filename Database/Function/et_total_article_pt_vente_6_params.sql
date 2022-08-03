-- Function: et_total_article_pt_vente(bigint, bigint, character varying, date, date, character varying)
DROP FUNCTION et_total_article_pt_vente(bigint, bigint, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_pt_vente(IN agence_ bigint, IN point_ bigint, IN articles_ character varying, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, marge_min double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE 
	societe_ BIGINT DEFAULT 0; 
BEGIN    
	IF(COALESCE(agence_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_agences a WHERE a.id = agence_;
	ELSIF(COALESCE(point_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_base_point_vente d INNER JOIN yvs_agences a ON d.agence = a.id WHERE d.id = point_;
	END IF;
	return QUERY select * from  et_total_article_pt_vente(0, agence_, point_, articles_, date_debut_, date_fin_, period_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_pt_vente(bigint, bigint, character varying, date, date, character varying)
  OWNER TO postgres;
