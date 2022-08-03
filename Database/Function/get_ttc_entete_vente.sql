-- Function: get_ttc_entete_vente(bigint)
-- DROP FUNCTION get_ttc_entete_vente(bigint);
CREATE OR REPLACE FUNCTION get_ttc_entete_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	cs_p double precision default 0;
	avoir_ double precision default 0;
BEGIN
	-- Recupere le montant TTC du contenu de la facture
	SELECT INTO total_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id WHERE d.entete_doc = id_ AND d.type_doc = 'FV' AND d.statut='V';	
	-- Recupere le total des couts de service supplementaire d'une facture
	SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id  INNER JOIN yvs_com_doc_ventes d ON o.doc_vente = d.id 
		WHERE d.entete_doc = id_ AND d.type_doc = 'FV' AND t.augmentation IS TRUE;
	SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id 
		WHERE da.type_doc = 'FAV' AND da.statut = 'V' AND da.entete_doc=id_;
	RETURN COALESCE(total_,0) + COALESCE(cs_p,0) -COALESCE(avoir_,0);															
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ttc_entete_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ttc_entete_vente(bigint) IS 'retourne le montant TTC d''un entete vente';
