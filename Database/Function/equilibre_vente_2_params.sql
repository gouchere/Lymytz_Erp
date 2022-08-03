-- Function: equilibre_vente(bigint, boolean)
-- DROP FUNCTION equilibre_vente(bigint, boolean);
CREATE OR REPLACE FUNCTION equilibre_vente(id_ bigint, by_parent_ boolean)
  RETURNS boolean AS
$BODY$
DECLARE

BEGIN
	PERFORM equilibre_vente_livre(id_, by_parent_);
	PERFORM equilibre_vente_regle(id_, by_parent_);
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(bigint, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente(bigint, boolean) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';
