-- Function: get_montants_vente(bigint, character varying)

-- DROP FUNCTION get_montants_vente(bigint, character varying);

CREATE OR REPLACE FUNCTION get_montants_vente(IN id_ bigint, IN model_ character varying)
  RETURNS TABLE(libelle character varying, montant double precision, rang integer) AS
$BODY$
DECLARE
	taxe_ record;
	view_taxe_facture_ boolean default true;
	
	total_remise_ double precision default 0;	
	total_ttc_ double precision default 0;	
	total_cs_ double precision default 0;	
	total_ht_ double precision default 0;	
	total_taxe_ double precision default 0;	
	net_a_payer_ double precision default 0;	

	rang_ integer default 2;

BEGIN	
	CREATE TEMP TABLE IF NOT EXISTS table_montants_vente(_libelle_ character varying, _montant_ double precision, _rang_ integer);
	DELETE FROM table_montants_vente;

	SELECT INTO view_taxe_facture_ COALESCE(y.view_taxe_facture, TRUE) FROM yvs_print_facture_vente y INNER JOIN yvs_agences a ON (y.societe = a.societe AND y.nom = model_)
	INNER JOIN yvs_base_point_vente p ON p.agence = a.id INNER JOIN yvs_com_creneau_point c ON c.point = p.id 
	INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_point = c.id INNER JOIN yvs_com_entete_doc_vente e ON e.creneau = h.id
	INNER JOIN yvs_com_doc_ventes d ON d.entete_doc = e.id WHERE d.id = id_;

	SELECT INTO total_remise_ SUM(y.remise) FROM yvs_com_contenu_doc_vente y WHERE y.doc_vente = id_;
	total_ttc_ := (SELECT get_ttc_vente(id_));
	IF(COALESCE(view_taxe_facture_, TRUE) IS TRUE)THEN
		FOR taxe_ IN SELECT COALESCE(t.libelle_print, t.code_taxe) AS taxe_code, t.designation, y.montant FROM public.get_taxe_vente(id_) y INNER JOIN yvs_base_taxes t ON y.taxe = t.id
		LOOP			
			total_taxe_ = total_taxe_ + taxe_.montant;
			INSERT INTO table_montants_vente VALUES(taxe_.taxe_code, taxe_.montant, rang_);
			rang_ = rang_ + 1;
		END LOOP;
		total_ht_ = total_ttc_ - total_taxe_;
		IF(COALESCE(total_remise_, 0) > 0)THEN
			INSERT INTO table_montants_vente VALUES('Remises', total_remise_, 0);
		END IF;
		INSERT INTO table_montants_vente VALUES('Mtant HT', total_ht_, 1);
		IF(COALESCE(total_taxe_, 0) < 1)THEN
			INSERT INTO table_montants_vente VALUES('Mtant Taxes', total_taxe_, rang_);
			rang_ = rang_ + 1;
		END IF;
		INSERT INTO table_montants_vente VALUES('Mtant TTC', total_ttc_, rang_);
		rang_ = rang_ + 1;
	END IF;
	net_a_payer_ = total_ttc_ + total_cs_;
	INSERT INTO table_montants_vente VALUES('Net à payer', net_a_payer_, rang_);
	rang_ = rang_ + 1;
	return QUERY SELECT * FROM table_montants_vente order by _rang_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION get_montants_vente(bigint, character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION get_montants_vente(bigint, character varying) IS 'retourne les montants d''un document de vente';
