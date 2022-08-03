-- Function: equilibre_achat(bigint)
DROP FUNCTION equilibre_achat(bigint);
CREATE OR REPLACE FUNCTION equilibre_achat(id_ bigint)
  RETURNS TABLE(statut_livre character varying, statut_regle character varying) AS
$BODY$
DECLARE

BEGIN
	return QUERY SELECT * FROM equilibre_achat(id_, true);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_achat(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de achat';
