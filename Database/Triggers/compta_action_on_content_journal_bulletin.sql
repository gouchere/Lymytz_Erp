-- Function: compta_action_on_content_journal_bulletin()

-- DROP FUNCTION compta_action_on_content_journal_bulletin();

CREATE OR REPLACE FUNCTION compta_action_on_content_journal_bulletin()
  RETURNS trigger AS
$BODY$    
DECLARE
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
IF(EXEC_) THEN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_grh_bulletins SET comptabilise = TRUE WHERE id = NEW.bulletin;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_grh_bulletins SET comptabilise = FALSE WHERE id = OLD.bulletin;
	END IF;
	END IF; RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_bulletin()
  OWNER TO postgres;
