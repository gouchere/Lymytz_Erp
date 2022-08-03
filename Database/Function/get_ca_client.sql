-- Function: get_ca_client(bigint, bigint, date, date)

-- DROP FUNCTION get_ca_client(bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION get_ca_client(id_ bigint, vendeur_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;

	avoir_ double precision default 0;
	cs_p double precision default 0;
	cs_ double precision default 0;
	
	remise_ double precision default 0;
	data_ record;
	
	header_ record;
	qte_ double precision;

BEGIN
	IF(id_ IS NOT NULL AND id_>0) THEN
		IF(date_debut_ IS NULL OR date_fin_ IS NULL) THEN
			IF(vendeur_ IS NULL OR vendeur<=0) THEN
				-- CA client juste
				SELECT INTO total_ SUM(c.prix_total-c.ristourne) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
													 WHERE d.client = id_ AND d.type_doc = 'FV' AND d.statut = 'V';
				SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
													 WHERE d.client = id_ AND d.type_doc = 'FAV' AND d.statut = 'V';
				SELECT INTO cs_p   SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id 
																					INNER JOIN yvs_com_doc_ventes d ON o.doc_vente = d.id 
												  WHERE d.client = id_ AND d.type_doc = 'FV' AND d.statut = 'V' AND t.augmentation IS TRUE AND o.service IS TRUE; 
			ELSE
				-- CA client avec un vendeur
				SELECT INTO total_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
																					  INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
																					  INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
													 WHERE d.client = id_ AND d.type_doc = 'FV' AND d.statut = 'V' AND h.users=vendeur_;
				SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id 
																							INNER JOIN yvs_com_doc_ventes fa ON da.document_lie = fa.id
																							INNER JOIN yvs_com_entete_doc_vente e ON fa.entete_doc = e.id
																							INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
															WHERE da.client=id_ AND da.type_doc = 'FAV' AND da.statut = 'V' AND h.users=vendeur_;
				SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id 
																				  INNER JOIN yvs_com_doc_ventes d ON o.doc_vente = d.id 
																				  INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
																				  INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
													 WHERE d.client = id_ AND d.type_doc = 'FV' AND d.statut = 'V' AND h.users=vendeur_;
			END IF;
		ELSE
			IF(vendeur_ IS NULL OR vendeur<=0) THEN
				-- CA client juste sur la période indiqué
				SELECT INTO total_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
																					  INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
													 WHERE d.client = id_ AND d.type_doc = 'FV' AND d.statut = 'V' AND e.date_entete BETWEEN date_debut_ AND date_fin_;
				SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id 
																							INNER JOIN yvs_com_doc_ventes fa ON da.document_lie = fa.id
																							INNER JOIN yvs_com_entete_doc_vente e ON fa.entete_doc = e.id
															WHERE da.client=id_ AND da.type_doc = 'FAV' AND da.statut = 'V' AND e.date_entete BETWEEN date_debut_ AND date_fin_;
				SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id 
																				  INNER JOIN yvs_com_doc_ventes d ON o.doc_vente = d.id 
																				  INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
													 WHERE d.client = id_ AND d.type_doc = 'FV' AND d.statut = 'V' AND e.date_entete BETWEEN date_debut_ AND date_fin_;
			ELSE
				-- CA client avec un vendeur sur la période indiqué
				SELECT INTO total_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
																					  INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
																					  INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
													 WHERE d.client = id_ AND d.type_doc = 'FV' AND d.statut = 'V' AND h.users=vendeur_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
				SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id 
																							INNER JOIN yvs_com_doc_ventes fa ON da.document_lie = fa.id
																							INNER JOIN yvs_com_entete_doc_vente e ON fa.entete_doc = e.id
																							INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
															WHERE da.client=id_ AND da.type_doc = 'FAV' AND da.statut = 'V' AND h.users=vendeur_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
				SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id 
																				  INNER JOIN yvs_com_doc_ventes d ON o.doc_vente = d.id 
																				  INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
																				  INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
													 WHERE d.client = id_ AND d.type_doc = 'FV' AND d.statut = 'V' AND h.users=vendeur_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
			END IF;
		END IF;
	END IF;
	
	-- -- Recupere le montant TTC du contenu de la facture
	-- select into total_ sum(c.prix_total) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
	-- inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id where d.client = id_ and d.type_doc = 'FV' and d.statut = 'V'
	-- and e.date_entete between date_debut_ and date_fin_ and ((COALESCE(vendeur_, 0)> 0 and h.users = vendeur_) or (COALESCE(vendeur_, 0) < 1 and d.id IS NOT NULL));
	-- if(total_ is null)then
		-- total_ = 0;
	-- end if;
	-- -- Recupere le total des couts de service supplementaire d'une facture
	-- select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id 
	-- inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	-- where d.client = id_ and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is true and o.service = true and e.date_entete between date_debut_ and date_fin_
	 -- and ((COALESCE(vendeur_, 0)> 0 and h.users = vendeur_) or (COALESCE(vendeur_, 0) < 1 and d.id IS NOT NULL));
	-- if(cs_p is null)then
		-- cs_p = 0;
	-- end if;
	-- cs_  = cs_p - cs_m;
	RETURN COALESCE(total_,0) + COALESCE(cs_p,0) -COALESCE(avoir_,0);	
	-- if(total_ is null)then
		-- total_ = 0;
	-- end if;
    -- return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ca_client(bigint, bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ca_client(bigint, bigint, date, date) IS 'retourne le chiffre d''affaire d''un vendeur';
