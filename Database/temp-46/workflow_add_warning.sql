-- Function: workflow_add_warning(bigint, character varying, character varying, date, character varying, boolean, bigint, bigint)

-- DROP FUNCTION workflow_add_warning(bigint, character varying, character varying, date, character varying, boolean, bigint, bigint);

CREATE OR REPLACE FUNCTION workflow_add_warning(id_ bigint, titre_ character varying, nature_ character varying, date_ date, action_ character varying, remove_ boolean, agence_ bigint, author_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE 
   ligne_ RECORD;   
   model_ RECORD;
   
   delai_ INTEGER DEFAULT 0;
   duree_ INTEGER DEFAULT 0;
BEGIN 	
	-- On recherche le model correspondant
	SELECT INTO model_ m.id, m.description FROM yvs_workflow_model_doc m WHERE m.titre_doc = titre_;
	IF(COALESCE(model_.id, 0) > 0)THEN
		-- On recupere le delai autorisé					
		SELECT INTO delai_ COALESCE(w.ecart, 1) FROM yvs_warning_model_doc w WHERE w.model = model_.id AND w.societe=(select a.societe FROM yvs_agences a WHERE a.id=agence_ limit 1);
		SELECT INTO duree_ (current_date - COALESCE(date_, current_date));
		If(delai_ = 0)THEN
			delai_ = 1;
		END IF;
		IF(action_ = 'INSERT') THEN  
			IF(remove_ IS FALSE)THEN
				INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau, description)
					VALUES (model_.id, nature_, id_, current_timestamp, current_timestamp, date_, author_, agence_, (duree_/delai_)::integer, model_.description);	
			END IF;
		ELSIF(action_ = 'UPDATE') THEN  
			-- Cas ou le delai est depassé
			IF(remove_ IS TRUE)THEN
				DELETE FROM yvs_workflow_alertes WHERE model_doc = model_.id AND nature_alerte = nature_ AND id_element = id_;
			ELSE
				SELECT INTO ligne_ a.id FROM yvs_workflow_alertes a WHERE a.model_doc = model_.id AND a.nature_alerte = nature_ AND a.id_element = id_;
				-- Teste d'abord si la ligne n'existe pas déjà
				IF(COALESCE(ligne_.id, 0) < 1) THEN
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau, description)
						VALUES (model_.id, nature_, id_, current_timestamp, current_timestamp, date_, author_, agence_, (duree_/delai_)::integer, model_.description);
				ELSE					
					UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_, agence = agence_, description = model_.description WHERE id = COALESCE(ligne_.id, 0);
				END IF;		
			END IF;
		ELSE --Désactive l'alerte
			DELETE FROM yvs_workflow_alertes a WHERE a.model_doc = model_.id AND a.nature_alerte = nature_ AND a.id_element = id_;
		END IF;
	END IF;
	RETURN TRUE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION workflow_add_warning(bigint, character varying, character varying, date, character varying, boolean, bigint, bigint)
  OWNER TO postgres;
