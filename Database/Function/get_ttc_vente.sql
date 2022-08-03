-- Function: get_ttc_vente(bigint)

-- DROP FUNCTION get_ttc_vente(bigint);

CREATE OR REPLACE FUNCTION get_ttc_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	qte_ double precision default 0;
	cs_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le CA d'une facture
	total_ = (select get_ca_vente(id_));
	if(total_ is null)then
		total_ = 0;
	end if;
	
	-- Recupere le total des couts supplementaire d'une facture
	cs_ = (select get_cout_sup_vente(id_, false));
	total_ = total_ + cs_;
	
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
