-- Function: base_warning_mouvement_stock()
-- DROP FUNCTION base_warning_mouvement_stock();
CREATE OR REPLACE FUNCTION base_warning_mouvement_stock()
  RETURNS trigger AS
$BODY$    
DECLARE	
	line_ RECORD;
	cond_ RECORD;
	
	id_ bigint;
	agence_ bigint;
	author_ bigint;
	date_ date;
	depot_ bigint;
	article_ bigint;
	conditionnement_ bigint;
	
	unite_c BIGINT;
	mouv_ BIGINT;
	model_ BIGINT DEFAULT 0;

	new_quantite double precision default 0;
	stock_ double precision default 0;
	coef_ double precision default 0;
	last_pr_ double precision;
	taux_ecart double precision;
	val_ecart double precision;

	duree_ INTEGER DEFAULT 1 ;
	delai_ INTEGER DEFAULT 1 ;

	action_ character varying;
	titre_ character varying default 'STOCK_ARTICLE';
	run_ BOOLEAN ;
	--EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		IF(NEW.execute_trigger='OK') THEN
			run_=false;
		ELSE
			run_=true;
		END IF;
	ELSE
		IF(OLD.execute_trigger='OK') THEN
			run_=false;
		ELSE
			run_=true;
		END IF;
	END IF;
	IF(run_) THEN 		
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			id_ = NEW.id; 
			author_ = NEW.author; 
			date_ = NEW.date_doc; 
			depot_ = NEW.depot; 
			article_ = NEW.article; 
			conditionnement_ = NEW.conditionnement; 
		ELSE
			id_ = OLD.id;
			author_ = OLD.author; 
			date_ = OLD.date_doc; 
			depot_ = OLD.depot; 
			article_ = OLD.article; 
			conditionnement_ = OLD.conditionnement; 
		END IF;
		SELECT INTO line_ ad.*, d.agence FROM yvs_base_article_depot ad INNER JOIN yvs_base_depots d ON d.id=ad.depot WHERE ad.depot=depot_ AND ad.article=article_;
		agence_ = line_.agence;
		titre_ = 'STOCK_ARTICLE';
		SELECT INTO model_ m.id FROM yvs_workflow_model_doc m WHERE m.titre_doc = titre_;
		IF(COALESCE(model_, 0) > 0)THEN
			IF(line_.id IS NOT NULL) THEN
				IF(COALESCE(line_.stock_min, 0) > 0 OR COALESCE(line_.stock_alert, 0) > 0) THEN
					IF((SELECT COUNT(c.id) FROM yvs_base_conditionnement c WHERE article = article_ ) > 1) THEN 
						IF(COALESCE(line_.default_cond, 0) > 0) THEN
							FOR cond_ IN SELECT c.* FROM yvs_base_conditionnement c WHERE c.article = article_
							LOOP
								new_quantite = get_stock_reel(article_, 0, depot_, 0, 0, current_date, cond_.id, 0);
								IF(new_quantite > 0 AND line_.default_cond != conditionnement_ AND line_.default_cond IS NOT NULL) THEN									
									-- Convertir le stock dans l'unitÃ© de stockage	
									SELECT INTO unite_c c.unite FROM yvs_base_conditionnement c WHERE c.id = line_.default_cond;
									SELECT INTO coef_ COALESCE(taux_change, 0) FROM yvs_base_table_conversion WHERE unite = cond_.unite AND unite_equivalent = unite_c;
									IF(coef_ > 0) THEN 
										stock_= stock_+ (new_quantite * coef_);
									END IF;
								END IF;
							END LOOP;
						END IF;
					ELSE
						stock_= get_stock_reel(article_, 0, depot_, 0, 0, current_date, conditionnement_, 0);
					END IF;
					IF(stock_ < line_.stock_alert) THEN
						SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a WHERE a.model_doc = model_ AND a.nature_alerte = 'STOCK ALERTE' AND a.id_element = line_.id;
						IF(COALESCE(mouv_, 0) < 1) THEN
							INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau)
									VALUES (model_, 'STOCK ALERTE', line_.id, current_timestamp, current_timestamp, date_, author_, agence_, (duree_/delai_)::integer);
						ELSE
							UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_ WHERE id = mouv_;
						END IF;
					ELSE
						DELETE FROM yvs_workflow_alertes WHERE id_element = line_.id AND model_doc = model_ AND nature_alerte = 'STOCK ALERTE';
					END IF;
					IF(stock_ < line_.stock_min) THEN
						SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a WHERE a.model_doc = model_ AND a.nature_alerte = 'STOCK SECURITE' AND a.id_element = line_.id;
						IF(COALESCE(mouv_, 0) < 1) THEN
							INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau)
									VALUES (model_, 'STOCK SECURITE', line_.id, current_timestamp, current_timestamp, date_, author_, agence_, (duree_/ delai_)::integer);
						ELSE
							UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_ WHERE id = mouv_;
						END IF;
					ELSE
						DELETE FROM yvs_workflow_alertes WHERE id_element = line_.id AND model_doc = model_ AND nature_alerte = 'STOCK SECURITE';
					END IF;
				ELSE
					DELETE FROM yvs_workflow_alertes WHERE id_element = line_.id AND model_doc = model_;
				END IF;
			END IF;
		END IF;
		titre_ = 'ARTICLE_NON_MOUVEMENTE';
		SELECT INTO model_ m.id FROM yvs_workflow_model_doc m WHERE m.titre_doc = titre_;
		IF(COALESCE(model_, 0) > 0)THEN
			SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a WHERE a.model_doc = model_ AND a.nature_alerte = 'NON MOUVEMENTE' AND a.id_element = article_;
			IF(COALESCE(mouv_, 0) < 1) THEN
				INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau)
					VALUES (model_, 'NON MOUVEMENTE', line_.id, current_timestamp, current_timestamp, date_, author_, agence_, (duree_/ delai_)::integer);
			ELSE
				UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_ WHERE id = mouv_;
			END IF;
		END IF;
		titre_ = 'HIGH_PR_ARTICLE';
		SELECT INTO model_ m.id FROM yvs_workflow_model_doc m WHERE m.titre_doc = titre_;
		IF(COALESCE(model_, 0) > 0)THEN
			IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
				taux_ecart := (select taux_ecart_pr from yvs_base_articles where id = NEW.article) ;
				IF(COALESCE(taux_ecart, 0) > 0)THEN
					last_pr_= COALESCE(get_pr(agence_,NEW.article, NEW.depot, 0, NEW.date_doc, NEW.conditionnement, NEW.id), 0);
					val_ecart = (last_pr_ * taux_ecart) / 100;
					IF((ABS(NEW.cout_stock - last_pr_) > ABS(val_ecart)))THEN
						SELECT INTO mouv_ id FROM yvs_workflow_alertes WHERE id_element = NEW.id AND model_doc = model_;
						IF(COALESCE(mouv_, 0) > 0)THEN
							UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_ WHERE id = mouv_;
						ELSE
							INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, date_save, date_update, date_doc, author, agence, niveau)
									VALUES (model_, 'CONFIRMATION', NEW.id, current_timestamp, current_timestamp, date_, author_, agence_, 1);
						END IF;
					END IF;
				ELSE
					DELETE FROM yvs_workflow_alertes WHERE id_element = NEW.id AND model_doc = model_;
				END IF;
			ELSE
				DELETE FROM yvs_workflow_alertes WHERE id_element = OLD.id AND model_doc = model_;
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
ALTER FUNCTION base_warning_mouvement_stock()
  OWNER TO postgres;
