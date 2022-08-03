-- Function: et_total_article_pt_vente(bigint, bigint, bigint, character varying, date, date, character varying)
DROP FUNCTION et_total_article_pt_vente(bigint, bigint, bigint, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_pt_vente(IN societe_ bigint, IN agence_ bigint, IN point_ bigint, IN article_ character varying, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, marge_min double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

BEGIN    
	RETURN QUERY SELECT * FROM com_et_total_articles(societe_, agence_, point_, 0, 0, 0, 0, 0, date_debut_, date_fin_, article_, '', periode_, 0, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_pt_vente(bigint, bigint, bigint, character varying, date, date, character varying)
  OWNER TO postgres;
