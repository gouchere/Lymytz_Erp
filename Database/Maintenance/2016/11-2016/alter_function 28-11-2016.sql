DROP FUNCTION get_montant_ht_doc_vente(bigint);
DROP FUNCTION get_montant_ttc_doc_vente(bigint);
DROP FUNCTION get_puv_total(bigint);
DROP FUNCTION get_taxe_total_vente(bigint);
DROP FUNCTION get_montant_tva_doc_vente(bigint);
DROP FUNCTION get_total_reglement_mensualite_fv(bigint);

-- Function: get_montant_ttc_doc_vente(bigint)

-- DROP FUNCTION get_montant_ttc_doc_vente(bigint);

CREATE OR REPLACE FUNCTION get_ttc_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	qte_ double precision default 0;
	cs_ double precision default 0;
	remise_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into total_ sum((quantite * (prix - rabais)) - remise + taxe) from yvs_com_contenu_doc_vente where doc_vente = id_;
	if(total_ is null)then
		total_ = 0;
	end if;
	-- Recupere le total des quantitées d'une facture
	select into qte_ sum(quantite) from yvs_com_contenu_doc_vente where doc_vente = id_;
	if(qte_ is null)then
		qte_ = 0;
	end if;
	
	-- Recupere le total des couts supplementaire d'une facture
	select into cs_ sum(montant) from yvs_com_cout_sup_doc_vente where doc_vente = id_ and actif = true;
	if(cs_ is null)then
		cs_ = 0;
	end if;
	total_ = total_ + cs_;

	-- Recupere le total des remises sur la facture
	for data_ in select g.* from yvs_com_grille_remise g inner join yvs_com_remise_doc_vente r on g.remise = r.remise where r.doc_vente = id_ and ((g.base = 'QTE' and qte_ between montant_minimal and montant_maximal) or (g.base = 'CA' and total_ between montant_minimal and montant_maximal))
	loop
		if(data_.nature_montant = 'TAUX')then
			remise_ = remise_ + ((total_ * data_.montant_remise)/100);
		else
			remise_ = remise_ + data_.montant_remise;
		end if;
	end loop;
	if(remise_ is null)then
		remise_= 0;
	end if;
	total_ = total_ - remise_;
	
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ttc_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ttc_vente(bigint) IS 'retourne le montant TTC d''un doc vente';
