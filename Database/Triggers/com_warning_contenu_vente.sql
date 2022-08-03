-- Function: com_warning_contenu_vente()
-- DROP FUNCTION com_warning_contenu_vente();
CREATE OR REPLACE FUNCTION com_warning_contenu_vente()
  RETURNS trigger AS
$BODY$    
DECLARE		
	societe_ RECORD;	
	model_ RECORD;
	
	doc_vente_ BIGINT DEFAULT 0;
	mouv_ BIGINT DEFAULT 0;

	marge_min_ double precision default 0;
	last_pr_ double precision default 0;
	total_pr_ double precision default 0;
	taux_ double precision default 0;

	action_ character varying;
	titre_ character varying default 'LOWER_MARGIN';
	run_ BOOLEAN ;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		IF(NEW.execute_trigger='OK') THEN
			run_=false;
		ELSE
			run_=true;
		END IF;
		doc_vente_ = NEW.doc_vente;
	ELSE
		IF(OLD.execute_trigger='OK') THEN
			run_=false;
		ELSE
			run_=true;
		END IF;
		doc_vente_ = OLD.doc_vente;
	END IF;
	IF(run_) THEN 		
		SELECT INTO societe_ a.societe, e.agence, e.date_entete, d.type_doc FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE d.id = doc_vente_;
		IF(societe_.type_doc = 'FV')THEN
			SELECT INTO model_ m.id, m.description FROM yvs_workflow_model_doc m INNER JOIN yvs_warning_model_doc a ON a.model = m.id WHERE m.titre_doc = titre_ AND a.societe = societe_.societe;
			IF(COALESCE(model_.id, 0) > 0)THEN
				IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN 
					SELECT INTO marge_min_ marge_min FROM yvs_base_conditionnement WHERE id = NEW.conditionnement;
					IF(COALESCE(marge_min_, 0) > 0)THEN
						last_pr_ = COALESCE(NEW.pr, 0);
						total_pr_ = COALESCE(NEW.quantite, 0) * COALESCE(last_pr_, 0);
						taux_ = ((COALESCE(NEW.prix_total, 0) - COALESCE(total_pr_, 0)) / COALESCE(NEW.prix_total, 0)) * 100;
						IF(COALESCE(marge_min_,0) > COALESCE(taux_, 0))THEN
							SELECT INTO mouv_ id FROM yvs_workflow_alertes WHERE id_element = NEW.id AND model_doc = model_.id;
							IF(COALESCE(mouv_, 0) > 0)THEN
								UPDATE yvs_workflow_alertes SET date_doc = societe_.date_entete WHERE id = mouv_;
							ELSE
								INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau, description)
										VALUES (model_.id, 'CONFIRMATION', NEW.id, current_timestamp, current_timestamp, societe_.date_entete, NEW.author, societe_.agence, 1, model_.description);
							END IF;
						ELSE
							DELETE FROM yvs_workflow_alertes a WHERE a.model_doc = model_.id AND a.id_element = NEW.id;
						END IF;
					ELSE
						DELETE FROM yvs_workflow_alertes a WHERE a.model_doc = model_.id AND a.id_element = NEW.id;
					END IF;
				ELSE
					DELETE FROM yvs_workflow_alertes a WHERE  a.model_doc = model_.id AND a.id_element = OLD.id;
				END IF;
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
ALTER FUNCTION com_warning_contenu_vente()
  OWNER TO postgres;
