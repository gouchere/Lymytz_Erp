-- Function: compta_action_on_cout_piece_virement()

-- DROP FUNCTION compta_action_on_cout_piece_virement();

CREATE OR REPLACE FUNCTION compta_action_on_cout_piece_virement()
  RETURNS trigger AS
$BODY$    
DECLARE
	action_ character varying;
	virement_ bigint;
	frais_ double precision default 0;
	montant_ double precision default 0;
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
BEGIN	
IF(EXEC_) THEN 		
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		virement_ = NEW.virement;
	ELSE
		virement_ = OLD.virement;
	END IF;
	SELECT INTO frais_ SUM(y.montant) FROM yvs_compta_cout_sup_piece_virement y WHERE y.virement = virement_;
	SELECT INTO montant_ y.montant FROM yvs_compta_caisse_piece_virement y WHERE y.id = virement_;
	UPDATE yvs_compta_mouvement_caisse SET montant = COALESCE(montant_, 0) + COALESCE(frais_, 0) WHERE table_externe = 'DOC_VIREMENT' AND id_externe = virement_ AND mouvement = 'D';
END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_cout_piece_virement()
  OWNER TO postgres;
