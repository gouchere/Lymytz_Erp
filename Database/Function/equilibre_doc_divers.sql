-- Function: equilibre_doc_divers(bigint)
-- DROP FUNCTION equilibre_doc_divers(bigint);
CREATE OR REPLACE FUNCTION equilibre_doc_divers(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	doc_ RECORD;
	ttc_ double precision default 0;
	cout_ double precision default 0;
	paye_ bigint default 0;
	statut_regle_ character varying ='W';
	statut_ character varying ='V';
	
	etape_total_ integer default 0;
	etape_valide_ integer default 0;

BEGIN
	-- montant total du document: c'est le montant du doc + le montant des charges supplémentaire
	SELECT INTO doc_ y.montant,  y.statut_doc FROM yvs_compta_caisse_doc_divers y WHERE y.id = id_;
	statut_ = doc_.statut_doc;
	SELECT INTO cout_ COALESCE((SELECT SUM(y.montant) FROM yvs_compta_cout_sup_doc_divers y WHERE y.doc_divers = id_), 0);
	ttc_= cout_ + (COALESCE(doc_.montant, 0));
	-- Montant totale des pièces payé
	SELECT INTO paye_ COALESCE((SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_divers y WHERE y.statut_piece='P' AND y.doc_divers = id_), 0);
	IF(paye_ < ttc_) THEN
		statut_regle_ = 'W';
	ELSE
		statut_regle_ = 'P';
	END IF;
	-- Nombre d'etape
	SELECT INTO etape_total_ COALESCE((SELECT COUNT(y.id) FROM yvs_workflow_valid_doc_caisse y WHERE y.doc_caisse = id_), 0);
	-- Nombre d'etape valide
	SELECT INTO etape_valide_ COALESCE((SELECT COUNT(y.id) FROM yvs_workflow_valid_doc_caisse y WHERE y.doc_caisse = id_ AND y.etape_valid IS TRUE), 0);
	IF(paye_ < ttc_) THEN
		statut_regle_ = 'W';
	ELSE
		statut_regle_ = 'P';
	END IF;
	IF(COALESCE(etape_total_, 0) > 0)THEN
		IF(COALESCE(etape_total_, 0) = COALESCE(etape_valide_, 0))THEN
			statut_ = 'V';
		ELSIF(COALESCE(etape_valide_, 0) > 0)THEN
			statut_ = 'R';
		ELSE
			statut_ = 'E';
		END IF;
	END IF;
	UPDATE yvs_compta_caisse_doc_divers SET statut_regle = statut_regle_, statut_doc = statut_, etape_total = etape_total_, etape_valide = etape_valide_ WHERE id = id_;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_doc_divers(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_doc_divers(bigint) IS 'equilibre l''etat reglé et des documents divers';
