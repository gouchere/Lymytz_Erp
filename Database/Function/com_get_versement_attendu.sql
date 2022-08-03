-- Function: com_get_versement_attendu(bigint, date, date)

-- DROP FUNCTION com_get_versement_attendu(bigint, date, date);

CREATE OR REPLACE FUNCTION com_get_versement_attendu(users_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	avoir_ DOUBLE PRECISION DEFAULT 0;
	cs_p DOUBLE PRECISION DEFAULT 0;
BEGIN    	
	-- Recupere le montant TTC du contenu de la facture
	SELECT INTO ca_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
																	   INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
																	   INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
									  WHERE h.users=users_ AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL
														   AND e.date_entete BETWEEN date_debut_ AND date_fin_;

	-- Recupere le total des couts de service supplementaire d'une facture
	SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id 
																	  INNER JOIN yvs_com_doc_ventes d ON o.doc_vente = d.id
																	  INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
																	  INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
									WHERE h.users=users_ AND d.type_doc = 'FV' AND d.statut = 'V' AND t.augmentation IS TRUE
														 AND e.date_entete BETWEEN date_debut_ AND date_fin_;	
	ca_ = COALESCE(ca_, 0) + COALESCE(cs_p, 0);

	-- Evalue les avoir sur factures de vente	
	SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id 
				INNER JOIN yvs_com_entete_doc_vente e ON da.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
				WHERE da.type_doc = 'FAV' AND da.statut = 'V' AND h.users=users_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
	
	-- Evalue les avances sur commandes des factures des headers	
	SELECT INTO avance_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id 
										   WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND p.caissier=users_ 
												  AND p.date_paiement BETWEEN date_debut_ AND date_fin_;
	-- END LOOP;
	RETURN COALESCE(ca_, 0) + COALESCE(avance_, 0) - COALESCE(avoir_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu(bigint, date, date)
  OWNER TO postgres;
