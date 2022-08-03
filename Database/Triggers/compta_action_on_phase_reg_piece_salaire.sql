-- Function: compta_action_on_phase_reg_piece_salaire()

-- DROP FUNCTION compta_action_on_phase_reg_piece_salaire();

CREATE OR REPLACE FUNCTION compta_action_on_phase_reg_piece_salaire()
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
		table_='DOC_SALAIRE';		
	IF(action_='INSERT' OR action_='UPDATE') THEN	
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE salaire= NEW.salaire;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE salaire= NEW.salaire AND phase_ok IS TRUE;
				UPDATE yvs_compta_mouvement_caisse SET 
					etape_total=etape_total_, etape_valide=etape_valide_
				WHERE table_externe=table_ AND id_externe=NEW.salaire;		
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE salaire= OLD.salaire;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE salaire= OLD.salaire AND phase_ok IS TRUE;
			UPDATE yvs_compta_mouvement_caisse SET 
					etape_total=etape_total_, etape_valide=etape_valide_
				WHERE table_externe=table_ AND id_externe=OLD.salaire;	    	 
	END IF;
END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_phase_reg_piece_salaire()
  OWNER TO postgres;
