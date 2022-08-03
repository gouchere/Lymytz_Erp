-- Function: equilibre_acompte_fournisseur(bigint)
-- DROP FUNCTION equilibre_acompte_fournisseur(bigint);
CREATE OR REPLACE FUNCTION equilibre_acompte_fournisseur(id_ bigint)
  RETURNS character varying AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	cout_ double precision default 0;
	paye_ double precision default 0;
	statut_ character varying ='W';

BEGIN
	-- montant total du document
	SELECT INTO ttc_ y.montant FROM yvs_compta_acompte_fournisseur y WHERE y.id = id_;
	-- Montant totale des pièces payé
	SELECT INTO paye_ SUM(y.montant) FROM yvs_compta_caisse_piece_achat y INNER JOIN yvs_compta_notif_reglement_achat n ON n.piece_achat = y.id WHERE y.statut_piece='P' AND n.acompte = id_;
	IF(COALESCE(paye_, 0) > 0)THEN
		statut_ = 'R';
		IF(paye_ >= ttc_) THEN
			statut_ = 'P';
		END IF;
	END IF;
	UPDATE yvs_compta_acompte_fournisseur SET statut_notif = statut_ WHERE id=id_;
	return statut_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_acompte_fournisseur(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_acompte_fournisseur(bigint) IS 'equilibre l''etat reglé et des documents acomptes fournisseur';
