-- Function: compta_warning_doc_divers()

-- DROP FUNCTION compta_warning_doc_divers();

CREATE OR REPLACE FUNCTION compta_warning_doc_divers()
  RETURNS trigger AS
$BODY$    
DECLARE	
	mouvement_ character varying;
	mouvement_OLD character varying;

	action_ character varying;
	titre_ character varying default 'OPERATION_DIVERS';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN 
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			mouvement_ = NEW.mouvement;
			mouvement_OLD = NEW.mouvement;
			IF(action_ = 'UPDATE') THEN  
				mouvement_OLD = OLD.mouvement;
			END IF;
			IF(mouvement_ != mouvement_OLD)THEN
				IF(mouvement_OLD = 'D') THEN
					titre_ = 'DOC_DIVERS_DEPENSE';
				ELSIF(mouvement_OLD = 'R') THEN
					titre_ = 'DOC_DIVERS_RECETTE';
				END IF;
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = titre_;
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'OD_NON_COMPTABILISE';
			END IF;
			IF(mouvement_ = 'D') THEN
				titre_ = 'DOC_DIVERS_DEPENSE';
			ELSIF(mouvement_ = 'R') THEN
				titre_ = 'DOC_DIVERS_RECETTE';
			END IF;
			PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', NEW.date_doc::date, action_, NEW.statut_doc IN ('V', 'C', 'A', 'T'), NEW.agence, NEW.author);
			PERFORM workflow_add_warning(NEW.id, 'OD_NON_COMPTABILISE', 'COMPTABILISE', NEW.date_doc::date, action_, NEW.comptabilise, NEW.agence, NEW.author);
		ELSE
			IF(OLD.mouvement = 'D') THEN
				titre_ = 'DOC_DIVERS_DEPENSE';
			ELSIF(OLD.mouvement = 'R') THEN
				titre_ = 'DOC_DIVERS_RECETTE';
			END IF;
			DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = titre_;
			DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'OD_NON_COMPTABILISE';
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
ALTER FUNCTION compta_warning_doc_divers()
  OWNER TO postgres;
