-- Function: prod_warning_ordre_fabrication()
-- DROP FUNCTION prod_warning_ordre_fabrication();
CREATE OR REPLACE FUNCTION prod_warning_ordre_fabrication()
  RETURNS trigger AS
$BODY$    
DECLARE
	agence_ bigint;
	date_reglement_ date;

	action_ character varying;

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			SELECT INTO agence_ agence FROM yvs_prod_site_production WHERE id = NEW.site_production;
			IF(NEW.statut_ordre IN ('R', 'T'))THEN
				PERFORM workflow_add_warning(NEW.id, 'ORDRE_FABRICATION_DECLARE', 'NON DECLARE', NEW.date_debut, action_, NEW.statut_declaration IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
			END IF;
			PERFORM workflow_add_warning(NEW.id, 'ORDRE_FABRICATION_TERMINE', 'NON TERMINEE', NEW.date_debut, action_, NEW.statut_ordre IN ('T', 'C', 'A'), agence_, NEW.author);
		ELSE
			DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'ORDRE_FABRICATION_DECLARE';
			DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'ORDRE_FABRICATION_TERMINE';
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
ALTER FUNCTION prod_warning_ordre_fabrication()
  OWNER TO postgres;



-- Trigger: prod_warning_ordre_fabrication on yvs_prod_ordre_fabrication
-- DROP TRIGGER prod_warning_ordre_fabrication ON yvs_prod_ordre_fabrication;
CREATE TRIGGER prod_warning_ordre_fabrication
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_prod_ordre_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE prod_warning_ordre_fabrication();