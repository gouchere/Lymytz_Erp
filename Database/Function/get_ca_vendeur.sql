-- Function: get_ca_vendeur(bigint, date, date)
-- DROP FUNCTION get_ca_vendeur(bigint, date, date);
CREATE OR REPLACE FUNCTION get_ca_vendeur(id_ bigint, date_debut_ date, date_fin_ date)
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
			-- CA point de vente sans limite de date
			SELECT INTO total_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
				INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
				WHERE d.type_doc = 'FV' AND d.statut = 'V' AND h.users=id_;
			SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id 
				INNER JOIN yvs_com_entete_doc_vente e ON da.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
				WHERE da.type_doc = 'FAV' AND da.statut = 'V' AND h.users=id_;
			SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id 
				INNER JOIN yvs_com_doc_ventes d ON o.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
				INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
				WHERE d.type_doc = 'FV' AND d.statut = 'V' AND h.users=id_;
		ELSE
			-- CA client avec un vendeur sur la période indiqué
			SELECT INTO total_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
				INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
				WHERE d.type_doc = 'FV' AND d.statut = 'V' AND h.users=id_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
			SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id 
				INNER JOIN yvs_com_entete_doc_vente e ON da.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
				WHERE da.type_doc = 'FAV' AND da.statut = 'V' AND h.users=id_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
			SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id 
				INNER JOIN yvs_com_doc_ventes d ON o.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
				INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
				WHERE d.type_doc = 'FV' AND d.statut = 'V' AND h.users=id_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
		END IF;
	END IF;
	RETURN COALESCE(total_,0) + COALESCE(cs_p,0) -COALESCE(avoir_,0);			
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ca_vendeur(bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ca_vendeur(bigint, date, date) IS 'retourne le chiffre d''affaire d''un vendeur';
