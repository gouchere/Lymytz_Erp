-- Function: compta_action_on_content_journal_doc_divers()

-- DROP FUNCTION compta_action_on_content_journal_doc_divers();

CREATE OR REPLACE FUNCTION compta_action_on_content_journal_doc_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	IF(EXEC_) THEN	
		IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
			UPDATE yvs_compta_caisse_doc_divers SET comptabilise = TRUE WHERE id = NEW.divers;
		ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
			UPDATE yvs_compta_caisse_doc_divers SET comptabilise = FALSE WHERE id = OLD.divers;
		END IF;
	END IF; 
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_doc_divers()
  OWNER TO postgres;
