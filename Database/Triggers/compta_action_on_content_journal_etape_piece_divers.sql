-- Function: compta_action_on_content_journal_etape_piece_divers()

-- DROP FUNCTION compta_action_on_content_journal_etape_piece_divers();

CREATE OR REPLACE FUNCTION compta_action_on_content_journal_etape_piece_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');BEGIN	IF(EXEC_) THEN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_phase_piece_divers SET comptabilise = TRUE WHERE id = NEW.etape;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_phase_piece_divers SET comptabilise = FALSE WHERE id = OLD.etape;
	END IF;
	END IF; RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_etape_piece_divers()
  OWNER TO postgres;

