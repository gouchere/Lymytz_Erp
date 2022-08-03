-- Function: base_warning_client()
-- DROP FUNCTION base_warning_client();
CREATE OR REPLACE FUNCTION base_warning_client()
  RETURNS trigger AS
$BODY$    
DECLARE	
	agence_ bigint;

	action_ character varying;
	titre_ character varying default 'CLIENT';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN     
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			IF(COALESCE(NEW.author, 0) < 1)THEN
				SELECT INTO NEW.author author FROM yvs_base_tiers WHERE id = NEW.tiers;
			END IF;
			IF(COALESCE(NEW.author, 0) > 0)THEN
				SELECT INTO agence_ agence FROM yvs_users_agence WHERE id = NEW.author;
			END IF;
			IF(action_ = 'INSERT') THEN  
				PERFORM workflow_add_warning(NEW.id, titre_, titre_, NEW.date_creation::date, action_, FALSE, agence_, NEW.author);
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
ALTER FUNCTION base_warning_client()
  OWNER TO postgres;
  
  
-- Trigger: base_warning_client on yvs_com_client
-- DROP TRIGGER base_warning_client ON yvs_com_client;
CREATE TRIGGER base_warning_client
  AFTER INSERT OR DELETE
  ON yvs_com_client
  FOR EACH ROW
  EXECUTE PROCEDURE base_warning_client();

