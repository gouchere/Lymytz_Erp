INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison, 
            defined_update, description, defined_reglement, workflow)
    VALUES ('ARTICLE_NON_MOUVEMENTE', 'yvs_base_article_depot', 16, current_date, current_date, FALSE, FALSE, 
            'Article non mouvementé', FALSE, FALSE);
			
ALTER TABLE yvs_base_articles DISABLE TRIGGER action_listen_yvs_base_articles;
UPDATE yvs_base_articles y SET date_last_mvt = (SELECT m.date_doc FROM yvs_base_mouvement_stock m WHERE m.article = y.id ORDER BY m.date_doc DESC LIMIT 1);
UPDATE yvs_base_articles y SET date_last_mvt = COALESCE(date_save, current_date) WHERE date_last_mvt IS NULL;
ALTER TABLE yvs_base_articles ENABLE TRIGGER action_listen_yvs_base_articles;
			
SELECT insert_droit('param_warning_view_all', 'Voir toutes les alertes de la socièté', 
	(SELECT id FROM yvs_page_module WHERE reference = 'param_workflow'), 16, 'A,B,C','R');	
	
SELECT fusion_data_for_table('yvs_workflow_model_doc', (SELECT id FROM yvs_workflow_model_doc WHERE titre_doc = 'RETOUR_VENTE'), (SELECT id FROM yvs_workflow_model_doc WHERE titre_doc = 'BON_RETOUR_VENTE'));
SELECT fusion_data_for_table('yvs_workflow_model_doc', (SELECT id FROM yvs_workflow_model_doc WHERE titre_doc = 'RETOUR_ACHAT'), (SELECT id FROM yvs_workflow_model_doc WHERE titre_doc = 'BON_RETOUR_ACHAT'));
SELECT fusion_data_for_table('yvs_workflow_model_doc', (SELECT id FROM yvs_workflow_model_doc WHERE titre_doc = 'AVOIR_VENTE'), (SELECT id FROM yvs_workflow_model_doc WHERE titre_doc = 'FACTURE_AVOIR_VENTE'));
SELECT fusion_data_for_table('yvs_workflow_model_doc', (SELECT id FROM yvs_workflow_model_doc WHERE titre_doc = 'AVOIR_ACHAT'), (SELECT id FROM yvs_workflow_model_doc WHERE titre_doc = 'FACTURE_AVOIR_ACHAT'));
			
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

	action_ character varying;
	titre_ character varying default 'STOCK_ARTICLE';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN 
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
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
		SELECT INTO model_ m.id FROM yvs_workflow_model_doc m WHERE m.titre_doc = titre_;
		IF(COALESCE(model_, 0) > 0)THEN
			SELECT INTO line_ ad.*, d.agence FROM yvs_base_article_depot ad INNER JOIN yvs_base_depots d ON d.id=ad.depot WHERE ad.depot=depot_ AND ad.article=article_;
			IF(line_.id IS NOT NULL) THEN
				agence_ = line_.agence;
				IF(COALESCE(line_.stock_min, 0) > 0 OR COALESCE(line_.stock_alert, 0) > 0) THEN
					IF((SELECT COUNT(c.id) FROM yvs_base_conditionnement c WHERE article = article_ ) > 1) THEN 
						IF(COALESCE(line_.default_cond, 0) > 0) THEN
							FOR cond_ IN SELECT c.* FROM yvs_base_conditionnement c WHERE c.article = article_
							LOOP
								new_quantite = get_stock_reel(article_, 0, depot_, 0, 0, current_date, cond_.id, 0);
								IF(new_quantite > 0 AND line_.default_cond != conditionnement_ AND line_.default_cond IS NOT NULL) THEN									
									-- Convertir le stock dans l'unitÃ© de stockage	
									SELECT INTO unite_c c.unite FROM yvs_base_conditionnement c WHERE c.id = line_.default_cond;
									SELECT INTO coef_ COALESCE(taux_change,0) FROM yvs_base_table_conversion WHERE unite = cond_.unite AND unite_equivalent = unite_c;
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
						SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a WHERE a.model_doc = model_ AND a.nature_alerte = 'STOCK ALERTE' AND a.id_element = id_;
						IF(COALESCE(mouv_, 0) < 1) THEN
							INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, date_doc, author, agence, niveau)
									VALUES (model_, 'STOCK ALERTE', id_, FALSE, current_timestamp, current_timestamp, date_, author_, agence_, (duree_/delai_)::integer);
						ELSE
							UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_, silence = silence_ WHERE id = mouv_;
						END IF;
					END IF;
					IF(stock_ < line_.stock_min) THEN
						SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a WHERE a.model_doc = model_ AND a.nature_alerte = 'STOCK SECURITE' AND a.id_element = id_;
						IF(COALESCE(mouv_, 0) < 1) THEN
							INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, date_doc, author, agence, niveau)
									VALUES (model_, 'STOCK SECURITE', id_, FALSE, current_timestamp, current_timestamp, date_, author_, agence_, (duree_/delai_)::integer);
						ELSE
							UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_, silence = silence_ WHERE id = mouv_;
						END IF;
					END IF;
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
ALTER FUNCTION base_warning_mouvement_stock()
  OWNER TO postgres;

-- Trigger: base_warning_mouvement_stock on yvs_base_mouvement_stock
-- DROP TRIGGER base_warning_mouvement_stock ON yvs_base_mouvement_stock;
CREATE TRIGGER base_warning_mouvement_stock
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_base_mouvement_stock
  FOR EACH ROW
  EXECUTE PROCEDURE base_warning_mouvement_stock();
  
  
-- Function: base_warning_article()
-- DROP FUNCTION base_warning_article();
CREATE OR REPLACE FUNCTION base_warning_article()
  RETURNS trigger AS
$BODY$    
DECLARE
	model_ bigint;
	agence_ bigint;
	mouv_ bigint;

	action_ character varying;
	titre_ character varying default 'ARTICLE_NON_MOUVEMENTE';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN   
		SELECT INTO model_ m.id FROM yvs_workflow_model_doc m WHERE m.titre_doc = titre_;
		IF(COALESCE(model_, 0) > 0)THEN
			action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE  
			IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
				SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a WHERE a.model_doc = model_ AND a.nature_alerte = 'NON MOUVEMENTE' AND a.id_element = NEW.id;
				IF(COALESCE(mouv_, 0) < 1) THEN
					SELECT INTO agence_ ua.agence FROM yvs_users_agence ua WHERE ua.id = NEW.author;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, date_doc, author, agence, niveau)
							VALUES (model_, 'NON MOUVEMENTE', NEW.id, FALSE, current_timestamp, current_timestamp, current_date, NEW.author, agence_, 1);
				END IF;
			ELSE  
				DELETE FROM yvs_workflow_alertes WHERE model_doc = model_ AND nature_alerte = 'NON MOUVEMENTE' AND id_element = NEW.id;
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
ALTER FUNCTION base_warning_article()
  OWNER TO postgres;

-- Trigger: base_warning_article on yvs_base_articles
-- DROP TRIGGER base_warning_article ON yvs_base_articles;
CREATE TRIGGER base_warning_article
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_base_articles
  FOR EACH ROW
  EXECUTE PROCEDURE base_warning_article();
  
  
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
							INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, date_doc, author, agence, niveau)
									VALUES (model_, 'STOCK ALERTE', NEW.id, FALSE, current_timestamp, current_timestamp, date_, NEW.author, agence_, (duree_/delai_)::integer);
						ELSE
							UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_, silence = FALSE WHERE id = mouv_;
						END IF;
					END IF;
					IF(stock_ < NEW.stock_min) THEN
						SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a WHERE a.model_doc = model_ AND a.nature_alerte = 'STOCK SECURITE' AND a.id_element = NEW.id;
						IF(COALESCE(mouv_, 0) < 1) THEN
							INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, date_doc, author, agence, niveau)
									VALUES (model_, 'STOCK SECURITE', NEW.id, FALSE, current_timestamp, current_timestamp, date_, NEW.author, agence_, (duree_/ delai_)::integer);
						ELSE
							UPDATE yvs_workflow_alertes SET date_update = current_timestamp, date_doc = date_, silence = FALSE WHERE id = mouv_;
						END IF;
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

-- Trigger: base_warning_article_depot on yvs_base_article_depot
-- DROP TRIGGER base_warning_article_depot ON yvs_base_article_depot;
CREATE TRIGGER base_warning_article_depot
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_base_article_depot
  FOR EACH ROW
  EXECUTE PROCEDURE base_warning_article_depot();

