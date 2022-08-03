-- Function: get_net_a_payer_achat(bigint)

-- DROP FUNCTION get_net_a_payer_achat(bigint);

CREATE OR REPLACE FUNCTION get_net_a_payer_achat(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	avance_ double precision default 0;
	ttc_ double precision default 0;

BEGIN
	-- Recupere le montant TTC de la facture
	ttc_ = (select get_ttc_achat(id_));	
	if(ttc_ is null)then
		ttc_= 0;
	end if;

	-- Recupere les reglements de la facture
	select into avance_ sum(montant) from yvs_compta_caisse_piece_achat where achat = id_ and statut_piece = 'P';
	if(avance_ is null)then
		avance_= 0;
	end if;
	
	total_ = ttc_ - avance_;	
	if(total_ is null or total_ <1)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_net_a_payer_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_net_a_payer_achat(bigint) IS 'retourne le net à payer d''un doc achat';
