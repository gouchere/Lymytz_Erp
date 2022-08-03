-- Function: equilibre_vente(bigint)
DROP FUNCTION equilibre_vente(bigint);
CREATE OR REPLACE FUNCTION equilibre_vente(id_ bigint)
  RETURNS TABLE(statut_livre character varying, statut_regle character varying) AS
$BODY$
DECLARE

BEGIN
	RETURN QUERY SELECT * FROM equilibre_vente(id_, true);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de achat';
