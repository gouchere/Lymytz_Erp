-- Function: com_warning_approvisionnement()
-- DROP FUNCTION com_warning_approvisionnement();
CREATE OR REPLACE FUNCTION com_warning_approvisionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	agence_ bigint;

	action_ character varying;
	titre_ character varying default 'APPROVISIONNEMENT';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN 
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			SELECT INTO agence_ d.agence FROM yvs_base_depots d WHERE d.id = NEW.depot;
			PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', NEW.date_approvisionnement, action_, NEW.etat IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
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
ALTER FUNCTION com_warning_approvisionnement()
  OWNER TO postgres;
