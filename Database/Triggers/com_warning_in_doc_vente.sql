-- Function: com_warning_in_doc_vente()
-- DROP FUNCTION com_warning_in_doc_vente();
CREATE OR REPLACE FUNCTION com_warning_in_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	ligne_ RECORD;
	warning_ RECORD;	
	model_ RECORD;
	
	agence_ bigint;
	date_doc_ date;
	date_reglement_ date;
	duree_ INTEGER DEFAULT 0;

	action_ character varying;

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  		
			SELECT INTO ligne_ e.date_entete, e.agence, p.vente_online, p.activer_notification, p.libelle FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
				INNER JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id INNER JOIN yvs_base_point_vente p ON cp.point = p.id WHERE e.id = NEW.entete_doc;
			agence_ = ligne_.agence;
			date_doc_ = COALESCE(ligne_.date_entete, current_date); 	
			SELECT INTO date_reglement_ m.date_reglement FROM yvs_com_mensualite_facture_vente m WHERE m.facture = NEW.id ORDER BY m.date_reglement DESC LIMIT 1;
			IF(NEW.type_doc='FV') THEN
				PERFORM workflow_add_warning(NEW.id, 'FACTURE_VENTE', 'VALIDATION', date_doc_, action_, NEW.statut IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
				IF(NEW.statut = 'V')THEN
					PERFORM workflow_add_warning(NEW.id, 'VENTE_NON_COMPTABILISE', 'COMPTABILISATION', date_doc_, action_, NEW.comptabilise, agence_, NEW.author);
					PERFORM workflow_add_warning(NEW.id, 'FACTURE_VENTE_LIVRE', 'LIVRAISON', COALESCE(NEW.date_livraison_prevu::date, date_doc_), action_, NEW.statut_livre IN ('L', 'C', 'A'), agence_, NEW.author);
					PERFORM workflow_add_warning(NEW.id, 'FACTURE_VENTE_REGLE', 'REGLEMENT', COALESCE(date_reglement_, date_doc_), action_, NEW.statut_regle IN ('P', 'C', 'A'), agence_, NEW.author);
				ELSIF(action_ = 'UPDATE' AND NEW.statut != 'V' AND OLD.statut = 'V')THEN
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'VENTE_NON_COMPTABILISE';
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'FACTURE_VENTE_LIVRE';
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'FACTURE_VENTE_REGLE';
				END IF;
				IF(NEW.statut != 'V' AND ligne_.activer_notification)THEN
					SELECT INTO model_ m.id, m.description, w.id AS warning, COALESCE(w.ecart, 0) AS ecart FROM yvs_workflow_model_doc m LEFT JOIN yvs_warning_model_doc w ON w.model = m.id LEFT JOIN yvs_agences a ON w.societe = a.societe AND a.id = agence_ WHERE m.titre_doc = 'FACTURE_VENTE';
					IF(COALESCE(model_.id, 0) > 0)THEN
						SELECT INTO warning_ y.id, y.description FROM yvs_workflow_alertes y WHERE y.model_doc = model_.id AND y.id_element = NEW.id;
						IF(COALESCE(warning_.id, 0) < 1)THEN
							INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau, description)
								VALUES (model_.id, 'VALIDATION', NEW.id, current_timestamp, current_timestamp, date_doc_, NEW.author, agence_, 1, 'Nouvelle commande sur le point '||ligne_.libelle);
						ELSE
							SELECT INTO duree_ (current_date - COALESCE(date_doc_, current_date));
							IF(COALESCE(duree_, 0) <= COALESCE(model_.ecart, 0) OR COALESCE(model_.warning, 0) < 1) THEN
								UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_doc_, agence = agence_, description = 'Nouvelle commande sur le point '||ligne_.libelle WHERE id = COALESCE(warning_.id, 0);
							END IF;
						END IF;
					END IF;
				END IF;
			ELSIF(NEW.type_doc='BLV')THEN
				PERFORM workflow_add_warning(NEW.id, 'BON_LIVRAISON_VENTE', 'VALIDATION', COALESCE(NEW.date_livraison_prevu::date, date_doc_), action_, NEW.statut IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
			ELSIF(NEW.type_doc = 'FAV') THEN
				PERFORM workflow_add_warning(NEW.id, 'AVOIR_VENTE', 'VALIDATION', date_doc_, action_, NEW.statut IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
			ELSE
				PERFORM workflow_add_warning(NEW.id, 'RETOUR_VENTE', 'VALIDATION', date_doc_, action_, NEW.statut IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
			END IF;
		ELSE
			IF(OLD.type_doc = 'FV') THEN
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'FACTURE_VENTE';
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'VENTE_NON_COMPTABILISE';
				IF(OLD.statut = 'V')THEN
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'FACTURE_VENTE_LIVRE';
					DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'FACTURE_VENTE_REGLE';
				END IF;
			ELSIF(OLD.type_doc='BLV')THEN
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'BON_LIVRAISON_VENTE';
			ELSIF(OLD.type_doc = 'FAV') THEN
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'AVOIR_VENTE';
			ELSE	
				DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = 'RETOUR_VENTE';
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
ALTER FUNCTION com_warning_in_doc_vente()
  OWNER TO postgres;
