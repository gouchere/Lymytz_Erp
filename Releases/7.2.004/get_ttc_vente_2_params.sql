CREATE OR REPLACE FUNCTION public.get_ttc_vente(id_ bigint, with_avoir_ boolean)
 RETURNS double precision
 LANGUAGE plpgsql
AS $function$
DECLARE	
	total_ double precision default 0;
	cs_p double precision default 0;
	avoir_ double precision default 0;
BEGIN
	-- Recupere le montant TTC du contenu de la facture
	SELECT INTO total_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c WHERE c.doc_vente = id_;	
	-- Recupere le total des couts de service supplementaire d'une facture
	SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id 
									WHERE o.doc_vente = id_ AND t.augmentation IS TRUE;
	SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id 
																							INNER JOIN yvs_com_doc_ventes fa ON da.document_lie = fa.id
															WHERE da.type_doc = 'FAV' AND da.statut = 'V' AND fa.id=id_;
	IF(COALESCE(with_avoir_, true))THEN
		RETURN COALESCE(total_,0) + COALESCE(cs_p,0) -COALESCE(avoir_,0);
	ELSE
		RETURN COALESCE(total_,0) + COALESCE(cs_p,0);
	END IF;
END;$function$
;
