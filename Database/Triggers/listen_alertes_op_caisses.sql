-- Function: listen_alertes_op_caisses()
-- DROP FUNCTION listen_alertes_op_caisses();
CREATE OR REPLACE FUNCTION listen_alertes_op_caisses()
  RETURNS trigger AS
$BODY$    
DECLARE

	action_ character varying;
	titre_ character varying default 'PIECE_CAISSE';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN 
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', COALESCE(NEW.date_paiment_prevu, NEW.date_mvt), action_, NEW.statut_piece IN ('P', 'C', 'A'), NEW.agence, NEW.author);
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
ALTER FUNCTION listen_alertes_op_caisses()
  OWNER TO postgres;
