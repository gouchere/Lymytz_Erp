-- Function: prod_warning_declaration_production()
-- DROP FUNCTION prod_warning_declaration_production();
CREATE OR REPLACE FUNCTION prod_warning_declaration_production()
  RETURNS trigger AS
$BODY$    
DECLARE		
	societe_ RECORD;	
	model_ RECORD;	
	article_ RECORD;
	
	session_of_ BIGINT DEFAULT 0;
	mouv_ BIGINT DEFAULT 0;

	last_pr_ double precision default 0;
	total_pr_ double precision default 0;
	taux_ double precision default 0;

	action_ character varying;
	statut_ character varying;
	titre_ character varying default 'CP_UPPER_PR';
	run_ BOOLEAN ;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		IF(NEW.execute_trigger='OK') THEN
			run_=false;
		ELSE
			run_=true;
		END IF;
		session_of_ = NEW.session_of;
		statut_ = NEW.statut;
	ELSE
		IF(OLD.execute_trigger='OK') THEN
			run_=false;
		ELSE
			run_=true;
		END IF;
		session_of_ = OLD.session_of;
		statut_ = OLD.statut;
	END IF;
	IF(run_) THEN 		
		SELECT INTO societe_ a.societe, o.agence FROM yvs_prod_session_of d INNER JOIN yvs_prod_session_prod e ON d.session_prod = e.id INNER JOIN yvs_base_depots o ON e.depot = o.id INNER JOIN yvs_agences a ON o.agence = a.id WHERE d.id = session_of_;
		SELECT INTO model_ m.id, m.description FROM yvs_workflow_model_doc m INNER JOIN yvs_warning_model_doc a ON a.model = m.id WHERE m.titre_doc = titre_ AND a.societe = societe_.societe;
		IF(COALESCE(model_.id, 0) > 0)THEN
			IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN 
				IF(NEW.statut = 'V')THEN
					SELECT INTO article_ article, marge_min, prix FROM yvs_base_conditionnement WHERE id = NEW.conditionnement;
					IF(COALESCE(article_.prix, 0) > 0)THEN
						IF(COALESCE(NEW.cout_production,0) > COALESCE(article_.prix, 0))THEN
							SELECT INTO mouv_ id FROM yvs_workflow_alertes WHERE id_element = NEW.id AND model_doc = model_.id;
							IF(COALESCE(mouv_, 0) > 0)THEN
								UPDATE yvs_workflow_alertes SET date_doc = NEW.date_declaration WHERE id = mouv_;
							ELSE
								INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau, description)
										VALUES (model_.id, 'CONFIRMATION', NEW.id, current_timestamp, current_timestamp, NEW.date_declaration, NEW.author, societe_.agence, 1, model_.description);
							END IF;
						END IF;
					ELSE
						DELETE FROM yvs_workflow_alertes a WHERE a.model_doc = model_.id AND a.id_element = NEW.id;
					END IF;
				ELSE
					DELETE FROM yvs_workflow_alertes a WHERE a.model_doc = model_.id AND a.id_element = NEW.id;
				END IF;
			ELSE
				DELETE FROM yvs_workflow_alertes a WHERE a.model_doc = model_.id AND a.id_element = OLD.id;
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
ALTER FUNCTION prod_warning_declaration_production()
  OWNER TO postgres;
