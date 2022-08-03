-- Function: compta_action_on_content_journal_piece_vente()

-- DROP FUNCTION compta_action_on_content_journal_piece_vente();

CREATE OR REPLACE FUNCTION compta_action_on_content_journal_piece_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	IF(EXEC_) THEN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_caisse_piece_vente SET comptabilise = TRUE WHERE id = NEW.reglement;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_caisse_piece_vente SET comptabilise = FALSE WHERE id = OLD.reglement;
	END IF;
	END IF; RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_piece_vente()
  OWNER TO postgres;
