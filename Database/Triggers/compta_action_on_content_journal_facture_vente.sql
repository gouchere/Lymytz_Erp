-- Function: compta_action_on_content_journal_facture_vente()

-- DROP FUNCTION compta_action_on_content_journal_facture_vente();

CREATE OR REPLACE FUNCTION compta_action_on_content_journal_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	IF(EXEC_) THEN	
		IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
			UPDATE yvs_com_doc_ventes SET comptabilise = TRUE WHERE id = NEW.facture;
		ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
			UPDATE yvs_com_doc_ventes SET comptabilise = FALSE WHERE id = OLD.facture;
		END IF;
	END IF; 
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_facture_vente()
  OWNER TO postgres;
