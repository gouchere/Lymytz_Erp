-- Function: et_total_article_pt_vente(bigint, bigint, date, date, character varying)
DROP FUNCTION et_total_article_pt_vente(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_pt_vente(IN agence_ bigint, IN point_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, marge_min double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE    

BEGIN    
	return QUERY SELECT * FROM et_total_article_pt_vente(agence_ , point_, '', date_debut_, date_fin_, period_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_pt_vente(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
