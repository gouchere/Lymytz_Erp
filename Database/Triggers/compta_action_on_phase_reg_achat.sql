-- Function: compta_action_on_phase_reg_achat()

-- DROP FUNCTION compta_action_on_phase_reg_achat();

CREATE OR REPLACE FUNCTION compta_action_on_phase_reg_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ integer default 0;
	etape_valide_ integer default 0;
	table_ character varying;
	action_ character varying;
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
IF(EXEC_) THEN 			
		action_=TG_OP;
		table_='DOC_ACHAT';		
	IF(action_='INSERT' OR action_='UPDATE') THEN	
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_achat WHERE piece_achat= NEW.piece_achat;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_achat WHERE piece_achat= NEW.piece_achat AND phase_ok IS TRUE;
				UPDATE yvs_compta_mouvement_caisse SET 
					etape_total=etape_total_, etape_valide=etape_valide_
				WHERE table_externe=table_ AND id_externe=NEW.piece_achat;		
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_achat WHERE piece_achat= OLD.piece_achat;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_achat WHERE piece_achat= OLD.piece_achat AND phase_ok IS TRUE;
			UPDATE yvs_compta_mouvement_caisse SET 
					etape_total=etape_total_, etape_valide=etape_valide_
				WHERE table_externe=table_ AND id_externe=OLD.piece_achat;	    	 
	END IF;
END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_phase_reg_achat()
  OWNER TO postgres;
