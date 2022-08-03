-- Function: compta_action_on_content_journal_acompte_fournisseur()

-- DROP FUNCTION compta_action_on_content_journal_acompte_fournisseur();

CREATE OR REPLACE FUNCTION compta_action_on_content_journal_acompte_fournisseur()
  RETURNS trigger AS
$BODY$    
DECLARE
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
IF(EXEC_) THEN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_acompte_fournisseur SET comptabilise = TRUE WHERE id = NEW.acompte;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_acompte_fournisseur SET comptabilise = FALSE WHERE id = OLD.acompte;
	END IF;
	END IF; RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_acompte_fournisseur()
  OWNER TO postgres;
