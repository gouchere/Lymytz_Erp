-- Function: grh_warning_in_conge_emps()
-- DROP FUNCTION grh_warning_in_conge_emps();
CREATE OR REPLACE FUNCTION grh_warning_in_conge_emps()
  RETURNS trigger AS
$BODY$    
DECLARE
	agence_ bigint;

	action_ character varying;
	titre_ character varying default 'CONGES';
	
	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_)THEN	
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			SELECT INTO agence_ agence FROM yvs_grh_employes WHERE id = NEW.employe;
			IF(NEW.nature = 'P')THEN
				titre_ = 'PERMISSION_'||NEW.duree_permission||'D';
			END IF;
			PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', NEW.date_conge, action_, NEW.statut IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
		ELSE
			IF(OLD.nature = 'P')THEN
				titre_ = 'PERMISSION_'||OLD.duree_permission||'D';
			END IF;
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
ALTER FUNCTION grh_warning_in_conge_emps()
  OWNER TO postgres;
