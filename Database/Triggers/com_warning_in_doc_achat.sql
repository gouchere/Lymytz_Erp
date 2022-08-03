-- Function: com_warning_in_doc_achat()

-- DROP FUNCTION com_warning_in_doc_achat();

CREATE OR REPLACE FUNCTION com_warning_in_doc_achat()
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
			agence_= NEW.agence;
			IF(agence_ IS NULL) THEN
				SELECT INTO agence_ d.agence FROM yvs_com_doc_achats a INNER JOIN yvs_base_depots d ON d.id=a.depot_reception WHERE a.id=NEW.id;
			END IF;
			SELECT INTO date_reglement_ m.date_reglement FROM yvs_com_mensualite_facture_achat m WHERE m.facture = NEW.id ORDER BY m.date_reglement DESC LIMIT 1;
			IF(NEW.type_doc = 'FA') THEN
				PERFORM workflow_add_warning(NEW.id, 'FACTURE_ACHAT', 'VALIDATION', NEW.date_doc, action_, NEW.statut IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
				IF(NEW.statut = 'V')THEN
					PERFORM workflow_add_warning(NEW.id, 'ACHAT_NON_COMPTABILISE', 'COMPTABILISE', NEW.date_doc, action_, NEW.comptabilise, agence_, NEW.author);
					PERFORM workflow_add_warning(NEW.id, 'FACTURE_ACHAT_LIVRE', 'LIVRAISON', COALESCE(NEW.date_doc, NEW.date_doc), action_, NEW.statut_livre IN ('L', 'C', 'A'), agence_, NEW.author);
					PERFORM workflow_add_warning(NEW.id, 'FACTURE_ACHAT_REGLE', 'REGLEMENT', COALESCE(date_reglement_, NEW.date_doc), action_, NEW.statut_regle IN ('P', 'C', 'A'), agence_, NEW.author);
				ELSIF(action_ = 'UPDATE' AND NEW.statut != 'V' AND OLD.statut = 'V')THEN
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'FACTURE_ACHAT_LIVRE';
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'FACTURE_ACHAT_REGLE';
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'ACHAT_NON_COMPTABILISE';
				END IF;
			ELSIF(NEW.type_doc='BLA')THEN
				PERFORM workflow_add_warning(NEW.id, 'BON_LIVRAISON_ACHAT', 'VALIDATION', NEW.date_doc, action_, NEW.statut IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
			ELSIF(NEW.type_doc = 'FAA') THEN
				PERFORM workflow_add_warning(NEW.id, 'AVOIR_ACHAT', 'VALIDATION', NEW.date_doc, action_, NEW.statut IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
			ELSE	
				PERFORM workflow_add_warning(NEW.id, 'RETOUR_ACHAT', 'VALIDATION', NEW.date_doc, action_, NEW.statut IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
			END IF;
		ELSE
			IF(OLD.type_doc = 'FA') THEN
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'FACTURE_ACHAT';
				IF(OLD.statut = 'V')THEN
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'FACTURE_ACHAT_LIVRE';
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'FACTURE_ACHAT_REGLE';
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'ACHAT_NON_COMPTABILISE';
				END IF;
			ELSIF(OLD.type_doc='BLA')THEN
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'BON_LIVRAISON_ACHAT';
			ELSIF(OLD.type_doc = 'FAA') THEN
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'AVOIR_ACHAT';
			ELSE	
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'RETOUR_ACHAT';
			END IF;
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
ALTER FUNCTION com_warning_in_doc_achat()
  OWNER TO postgres;
