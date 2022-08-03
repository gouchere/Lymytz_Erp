-- Function: equilibre_vente(bigint, boolean)
DROP FUNCTION equilibre_vente(bigint, boolean);
CREATE OR REPLACE FUNCTION equilibre_vente(id_ bigint, by_parent_ boolean)
  RETURNS TABLE(statut_livre character varying, statut_regle character varying) AS
$BODY$
DECLARE
	statut_livre_ character varying default 'W'; 
	statut_regle_ character varying default 'W';
BEGIN
	CREATE TEMP TABLE IF NOT EXISTS table_statut_vente(_statut_livre_ character varying, _statut_regle_ character varying); 
	DELETE FROM table_statut_vente;
	statut_livre_ = (SELECT equilibre_vente_livre(id_, by_parent_));
	statut_regle_ = (SELECT equilibre_vente_regle(id_, by_parent_));
	INSERT INTO table_statut_vente VALUES(statut_livre_, statut_regle_);
	RETURN QUERY SELECT * FROM table_statut_vente;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(bigint, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente(bigint, boolean) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';
