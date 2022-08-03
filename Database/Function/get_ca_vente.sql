-- Function: get_ca_vente(bigint)
-- DROP FUNCTION get_ca_vente(bigint);
CREATE OR REPLACE FUNCTION get_ca_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	cs_p double precision default 0;
	avoir_ double precision default 0;
	remise_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	SELECT INTO total_ SUM(COALESCE(c.prix_total, 0) - COALESCE(c.ristourne, 0)) FROM yvs_com_contenu_doc_vente c WHERE c.doc_vente = id_;	
	-- Recupere le total des couts de service supplementaire d'une facture
	SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id 
									WHERE o.doc_vente = id_ AND t.augmentation IS TRUE AND o.service = TRUE;
	SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id 
																							INNER JOIN yvs_com_doc_ventes fa ON da.document_lie = fa.id
															WHERE da.type_doc = 'FAV' AND da.statut = 'V' AND fa.id=id_;
	RETURN COALESCE(total_,0) + COALESCE(cs_p,0) -COALESCE(avoir_,0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ca_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ca_vente(bigint) IS 'retourne le chiffre d''affaire d''un doc vente';
