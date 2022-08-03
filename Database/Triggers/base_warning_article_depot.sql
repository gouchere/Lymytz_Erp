-- Function: base_warning_article_depot()
-- DROP FUNCTION base_warning_article_depot();
CREATE OR REPLACE FUNCTION base_warning_article_depot()
  RETURNS trigger AS
$BODY$    
DECLARE	
	line_ RECORD;
	cond_ RECORD;
	
	id_ bigint;
	agence_ bigint;
	author_ bigint;
	date_ date default current_date;
	depot_ bigint;
	article_ bigint;
	conditionnement_ bigint;
	
	unite_c BIGINT;
	mouv_ BIGINT;
	model_ BIGINT DEFAULT 0;

	duree_ INTEGER DEFAULT 1 ;
	delai_ INTEGER DEFAULT 1 ;

	new_quantite double precision default 0;
	stock_ double precision default 0;
	coef_ double precision default 0;

	action_ character varying;
	titre_ character varying default 'STOCK_ARTICLE';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN 
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE 
		SELECT INTO model_ m.id FROM yvs_workflow_model_doc m WHERE m.titre_doc = titre_;
		IF(COALESCE(model_, 0) > 0)THEN   
			IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN 
				SELECT INTO agence_ d.agence FROM yvs_base_depots d WHERE d.id = NEW.depot;
				IF(COALESCE(NEW.stock_min, 0) > 0 OR COALESCE(NEW.stock_alert, 0) > 0) THEN
					IF(COALESCE(NEW.default_cond, 0) > 0) THEN
						FOR cond_ IN SELECT c.* FROM yvs_base_conditionnement c WHERE c.article = NEW.article
						LOOP
							new_quantite = get_stock_reel(NEW.article, 0, NEW.depot, 0, 0, current_date, cond_.id, 0);
							IF(new_quantite > 0 AND NEW.default_cond != cond_.id) THEN									
								-- Convertir le stock dans l'unitÃ© de stockage	
								SELECT INTO unite_c c.unite FROM yvs_base_conditionnement c WHERE c.id = NEW.default_cond;
								SELECT INTO coef_ COALESCE(taux_change, 0) FROM yvs_base_table_conversion WHERE unite = cond_.unite AND unite_equivalent = unite_c;
								IF(coef_ > 0) THEN 
									stock_= stock_+ (new_quantite * coef_);
								END IF;
							ELSE
								stock_ = stock_ + new_quantite;
							END IF;
						END LOOP;
					END IF;
					IF(stock_ < NEW.stock_alert) THEN
						SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a WHERE a.model_doc = model_ AND a.nature_alerte = 'STOCK ALERTE' AND a.id_element = NEW.id;
						IF(COALESCE(mouv_, 0) < 1) THEN
							INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau)
									VALUES (model_, 'STOCK ALERTE', NEW.id, current_timestamp, current_timestamp, date_, NEW.author, agence_, (duree_/delai_)::integer);
						ELSE
							UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_ WHERE id = mouv_;
						END IF;
					ELSE
						DELETE FROM yvs_workflow_alertes WHERE model_doc = model_ AND nature_alerte = 'STOCK ALERTE' AND id_element = NEW.id;
					END IF;
					IF(stock_ < NEW.stock_min) THEN
						SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a WHERE a.model_doc = model_ AND a.nature_alerte = 'STOCK SECURITE' AND a.id_element = NEW.id;
						IF(COALESCE(mouv_, 0) < 1) THEN
							INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau)
									VALUES (model_, 'STOCK SECURITE', NEW.id, current_timestamp, current_timestamp, date_, NEW.author, agence_, (duree_/ delai_)::integer);
						ELSE
							UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_ WHERE id = mouv_;
						END IF;
					ELSE
						DELETE FROM yvs_workflow_alertes WHERE model_doc = model_ AND nature_alerte = 'STOCK SECURITE' AND id_element = NEW.id;
					END IF;
				END IF;
			ELSE
				DELETE FROM yvs_workflow_alertes WHERE model_doc = model_ AND nature_alerte = 'STOCK ALERTE' AND id_element = OLD.id;
				DELETE FROM yvs_workflow_alertes WHERE model_doc = model_ AND nature_alerte = 'STOCK SECURITE' AND id_element = OLD.id;
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
ALTER FUNCTION base_warning_article_depot()
  OWNER TO postgres;
