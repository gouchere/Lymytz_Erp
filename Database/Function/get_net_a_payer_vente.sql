-- Function: get_net_a_payer_vente(bigint)

-- DROP FUNCTION get_net_a_payer_vente(bigint);

CREATE OR REPLACE FUNCTION get_net_a_payer_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	avance_ double precision default 0;
	ttc_ double precision default 0;

BEGIN
	-- Recupere le montant TTC de la facture
	ttc_ = (select get_ttc_vente(id_));	
	if(ttc_ is null)then
		ttc_= 0;
	end if;

	-- Recupere les reglements de la facture
	select into total_ sum(montant) from yvs_compta_caisse_piece_vente where vente = id_ and statut_piece = 'P' and mouvement = 'D';
	if(total_ is null)then
		total_= 0;
	end if;
	select into avance_ sum(montant) from yvs_compta_caisse_piece_vente where vente = id_ and statut_piece = 'P' and mouvement = 'R';
	if(avance_ is null)then
		avance_= 0;
	end if;
	avance_ = avance_ - total_;
	
	total_ = ttc_ - avance_;	
	if(total_ is null or total_ <1)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_net_a_payer_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_net_a_payer_vente(bigint) IS 'retourne le net à payer d''un doc vente';
