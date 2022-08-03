-- Function: base_warning_employe()
-- DROP FUNCTION base_warning_employe();
CREATE OR REPLACE FUNCTION base_warning_employe()
  RETURNS trigger AS
$BODY$    
DECLARE	
	agence_ bigint;

	action_ character varying;
	titre_ character varying default 'EMPLOYE';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN     
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			IF(action_ = 'INSERT') THEN  
				PERFORM workflow_add_warning(NEW.id, titre_, titre_, NEW.date_save::date, action_, FALSE, NEW.agence, NEW.author);
			END IF;	
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
ALTER FUNCTION base_warning_employe()
  OWNER TO postgres;

  
  -- Trigger: base_warning_employe on yvs_grh_employes
-- DROP TRIGGER base_warning_employe ON yvs_grh_employes;
CREATE TRIGGER base_warning_employe
  AFTER INSERT OR DELETE
  ON yvs_grh_employes
  FOR EACH ROW
  EXECUTE PROCEDURE base_warning_employe();
