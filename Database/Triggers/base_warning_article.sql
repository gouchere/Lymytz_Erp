-- Function: base_warning_article()
-- DROP FUNCTION base_warning_article();
CREATE OR REPLACE FUNCTION base_warning_article()
  RETURNS trigger AS
$BODY$    
DECLARE	
	agence_ bigint;

	action_ character varying;
	titre_ character varying default 'ARTICLE_NON_MOUVEMENTE';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN     
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			IF(COALESCE(NEW.author, 0) > 0)THEN
				SELECT INTO agence_ agence FROM yvs_users_agence WHERE id = NEW.author;
			ELSE
				SELECT INTO NEW.author author FROM yvs_base_famille_article WHERE id = NEW.famille;
				SELECT INTO agence_ a.id FROM yvs_agences a INNER JOIN yvs_base_famille_article f On a.societe = f.societe WHERE f.id = NEW.famille LIMIT 1;
			END IF;
			PERFORM workflow_add_warning(NEW.id, titre_, 'NON MOUVEMENTE', NEW.date_last_mvt, action_, FALSE, agence_, NEW.author); 
			IF(action_ = 'INSERT') THEN  
				PERFORM workflow_add_warning(NEW.id, 'ARTICLE', 'ARTICLE', NEW.date_save::date, action_, FALSE, agence_, NEW.author);
			END IF;	
		ELSE
			DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = titre_;
			titre_ = 'ARTICLE';
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
ALTER FUNCTION base_warning_article()
  OWNER TO postgres;
