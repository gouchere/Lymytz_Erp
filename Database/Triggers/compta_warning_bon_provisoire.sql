-- Function: compta_warning_bon_provisoire()
-- DROP FUNCTION compta_warning_bon_provisoire();
CREATE OR REPLACE FUNCTION compta_warning_bon_provisoire()
  RETURNS trigger AS
$BODY$    
DECLARE	
	action_ character varying;
	titre_ character varying default 'BON_OPERATION_DIVERS';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN 
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', NEW.date_bon, action_, NEW.statut IN ('V', 'T', 'C', 'A'), NEW.agence, NEW.author);
		ELSE
			DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = titre_;
		END IF;
	END IF;
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		RETURN NEW;
	ELSE
		RETURN OLD;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_warning_bon_provisoire()
  OWNER TO postgres;
