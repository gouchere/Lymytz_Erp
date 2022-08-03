
-- Function: compta_action_on_content_journal_entete_bulletin()

-- DROP FUNCTION compta_action_on_content_journal_entete_bulletin();

CREATE OR REPLACE FUNCTION compta_action_on_content_journal_entete_bulletin()
  RETURNS trigger AS
$BODY$    
DECLARE
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');BEGIN	IF(EXEC_) THEN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_grh_ordre_calcul_salaire SET comptabilise = TRUE WHERE id = NEW.entete;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_grh_ordre_calcul_salaire SET comptabilise = FALSE WHERE id = OLD.entete;
	END IF;
	END IF; RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_entete_bulletin()
  OWNER TO postgres;

