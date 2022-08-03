-- Function: get_ttc_achat(bigint)

-- DROP FUNCTION get_ttc_achat(bigint);

CREATE OR REPLACE FUNCTION get_ttc_achat(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	qte_ double precision default 0;
	cs_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into total_ sum((quantite_attendu * (pua_attendu)) - remise_attendu + taxe) from yvs_com_contenu_doc_achat where doc_achat = id_;
	if(total_ is null)then
		total_ = 0;
	end if;
	
	-- Recupere le total des couts supplementaire d'une facture
	select into cs_ sum(o.montant) from yvs_com_cout_sup_doc_achat o where o.doc_achat = id_ and o.actif is true;
	if(cs_ is null)then
		cs_ = 0;
	end if;
	total_ = total_ + cs_;
		
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ttc_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ttc_achat(bigint) IS 'retourne le montant TTC d''un doc achat';
