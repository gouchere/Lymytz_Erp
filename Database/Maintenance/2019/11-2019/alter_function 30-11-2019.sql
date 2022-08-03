-- Function: stock_maj_prix_mvt()
-- DROP FUNCTION stock_maj_prix_mvt();
CREATE OR REPLACE FUNCTION stock_maj_prix_mvt()
  RETURNS trigger AS
$BODY$    
DECLARE
	prix_arr_ double precision;
	stock_ double precision default 0;
	coef_ double precision default 0;
	last_pr_ double precision;
	new_cout double precision;
	new_quantite double precision;
	
	unite_c BIGINT;
	mouv_ BIGINT;
	
	line_ RECORD;
	cond_ RECORD;

	duree_ INTEGER DEFAULT 1 ;
	delai_ INTEGER DEFAULT 1 ;
	
	action_ character varying;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN
	-- fonction de calcul du prix de revient dans la table mvt de stock
	IF(EXEC_) THEN
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_='INSERT') THEN  
			new_quantite = 0;
		ELSIF(action_='UPDATE') THEN  
			new_quantite = NEW.quantite - OLD.quantite;
		END IF;
		last_pr_= COALESCE(get_pr(NEW.article, NEW.depot, 0, NEW.date_doc, NEW.conditionnement, NEW.id), 0);
		IF(NEW.mouvement = 'E' AND NEW.calcul_pr IS TRUE) THEN 
			--calcule le stock
			stock_= get_stock_reel(NEW.article,0,NEW.depot, 0, 0, NEW.date_doc, NEW.conditionnement, 0) + new_quantite;
			-- Recherche des valeurs null
			if(stock_ is null) THEN
				stock_=0;
			END IF;
			if(NEW.quantite is null)then
				NEW.quantite = 0;
			end if;
			if(NEW.cout_entree is null)then
				NEW.cout_entree = 0;
			end if;
			-- Calcul du cout de stockage
			prix_arr_ = stock_ + NEW.quantite;
			if(prix_arr_ <= 0)then
				prix_arr_ = 1;
			end if;
			new_cout = (((stock_ * last_pr_) + (NEW.quantite * NEW.cout_entree)) / (prix_arr_));
			-- Retourne le nouveau cout moyen calculÃƒÂ©
			if(new_cout is null)then
				new_cout = 0;
			end if;
		ELSE 
			new_cout = last_pr_;
		END IF;
		NEW.cout_stock = ROUND(new_cout::decimal, 3);
	END IF;
	-- Traitement alerte stock
	IF(EXEC_T_) THEN 
		SELECT INTO line_ ad.*, d.agence FROM yvs_base_article_depot ad INNER JOIN yvs_base_depots d ON d.id=ad.depot WHERE ad.depot=NEW.depot AND ad.article=NEW.article;
		IF(line_.id IS NOT NULL) THEN
			IF(COALESCE(line_.stock_min,0)>0 OR COALESCE(line_.stock_alert,0)>0) THEN
				IF((SELECT COUNT(c.id) FROM yvs_base_conditionnement c WHERE article=NEW.article)>1) THEN 
					IF(COALESCE(line_.default_cond,0)>0) THEN
						FOR cond_ IN SELECT c.* FROM yvs_base_conditionnement c WHERE c.article=NEW.article
							LOOP
								new_quantite= get_stock_reel(NEW.article,0,NEW.depot, 0, 0, current_date, cond_.id, 0);
								IF(new_quantite>0 AND line_.default_cond!=NEW.conditionnement AND line_.default_cond IS NOT NULL) THEN									
									-- Convertir le stock dans l'unitÃ© de stockage	
									SELECT 	INTO unite_c c.unite FROM yvs_base_conditionnement c WHERE c.id=line_.default_cond;
									SELECT INTO coef_ COALESCE(taux_change,0) FROM yvs_base_table_conversion WHERE unite=cond_.unite AND unite_equivalentunite_equivalent=unite_c;
									IF(coef_>0) THEN 
										stock_=stock_+(new_quantite*coef_);
									END IF;
								END IF;
							END LOOP;
					END IF;
				ELSE
					stock_= get_stock_reel(NEW.article,0,NEW.depot, 0, 0, current_date, NEW.conditionnement, 0);
				END IF;
				IF(stock_<line_.stock_alert) THEN
					SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a INNER JOIN yvs_workflow_model_doc m ON m.id=a.model_doc  WHERE silence=false AND titre_doc='STOCK_ARTICLE' AND nature_alerte='STOCK ALERTE' AND id_element=NEW.id;
					IF(COALESCE(mouv_,0)<=0) THEN
						INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau)
								VALUES ((SELECT id FROM yvs_workflow_model_doc WHERE titre_doc='STOCK_ARTICLE' LIMIT 1), 'STOCK ALERTE', NEW.id, FALSE, current_timestamp, current_timestamp, NEW.author, line_.agence, (duree_/delai_)::integer);
					END IF;
				END IF;
				IF(stock_<line_.stock_min) THEN
					SELECT INTO mouv_ a.id FROM yvs_workflow_alertes a INNER JOIN yvs_workflow_model_doc m ON m.id=a.model_doc  WHERE silence=false AND titre_doc='STOCK_ARTICLE' AND nature_alerte='STOCK SECURITE' AND id_element=NEW.id;
					IF(COALESCE(mouv_,0)<=0) THEN
						INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau)
								VALUES ((SELECT id FROM yvs_workflow_model_doc WHERE titre_doc='STOCK_ARTICLE' LIMIT 1), 'STOCK SECURITE', NEW.id, FALSE, current_timestamp, current_timestamp, NEW.author, line_.agence, (duree_/delai_)::integer);
					END IF;
				END IF;
			END IF;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION stock_maj_prix_mvt()
  OWNER TO postgres;
COMMENT ON FUNCTION stock_maj_prix_mvt() IS 'CETTE FONCTION PERMET DE CALCULER LE COUT DU STOCK EN PRENANT EN COMPTE LA MODIFICATION D''UNE LIGNE DE STOCK';


-- Function: com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean)
-- DROP FUNCTION com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION com_et_ristourne_vente(IN societe_ bigint, IN users_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN cumul_ boolean)
  RETURNS TABLE(client bigint, code character varying, nom character varying, users bigint, code_users character varying, nom_users character varying, unite bigint, entete character varying, quantite double precision, prix_total double precision, ristourne double precision, rang integer) AS
$BODY$
declare 
   
begin 	
	RETURN QUERY SELECT * FROM com_et_ristourne_vente(societe_, users_, client_, date_debut_, date_fin_, period_, cumul_, false);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean)
  OWNER TO postgres;


-- Function: com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean, boolean)
-- DROP FUNCTION com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean, boolean);
CREATE OR REPLACE FUNCTION com_et_ristourne_vente(IN societe_ bigint, IN users_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN cumul_ boolean, IN total_ boolean)
  RETURNS TABLE(client bigint, code character varying, nom character varying, users bigint, code_users character varying, nom_users character varying, unite bigint, entete character varying, quantite double precision, prix_total double precision, ristourne double precision, rang integer) AS
$BODY$
declare 

   clients_ RECORD;
   data_ RECORD;
   dates_ RECORD;
   
   jour_ character varying;
   
   ristourne_ CHARACTER VARYING DEFAULT 'SELECT u.id, u.code_users, u.nom_users, c.ristourne, c.quantite, c.prix_total, c.conditionnement';
   colonne_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT y.id, y.code_client, CONCAT(y.nom, '' '', y.prenom) AS nom';
   save_ CHARACTER VARYING DEFAULT ' FROM yvs_com_client y INNER JOIN yvs_com_doc_ventes d ON d.client = y.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
						INNER JOIN yvs_com_contenu_doc_vente c ON c.doc_vente = d.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id
						WHERE d.type_doc = ''FV'' AND e.date_entete BETWEEN'||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND COALESCE(c.ristourne, 0) > 0 AND a.societe = '||societe_;
   where_ CHARACTER VARYING DEFAULT '';
   query_ CHARACTER VARYING DEFAULT '';

   i integer default 0;
   
begin 	
	DROP TABLE IF EXISTS table_et_ristourne_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ristourne_vente(_client bigint, _code character varying, _nom character varying, _users bigint, _code_users character varying, _nom_users character varying, _unite bigint, _entete character varying, _quantite double precision, _prix_total double precision, _ristourne double precision, _rang integer);
	DELETE FROM table_et_ristourne_vente;
	IF(users_ IS NOT NULL AND users_ > 0)THEN
		save_ = save_ || ' AND u.id = '||users_;
	END IF;
	IF(client_ IS NOT NULL AND client_ > 0)THEN
		where_ = ' AND y.id = '||client_;
	END IF;
-- 	RAISE NOTICE 'query_ %',query_;
	IF(total_)THEN
		ristourne_ = 'SELECT SUM(COALESCE(c.ristourne, 0)) as ristourne, SUM(COALESCE(c.prix_total, 0)) as prix_total';
	ELSIF(cumul_)THEN
		ristourne_ = 'SELECT u.id, u.code_users, u.nom_users, SUM(COALESCE(c.ristourne, 0)) as ristourne, SUM(COALESCE(c.quantite, 0)) as quantite, SUM(COALESCE(c.prix_total, 0)) as prix_total, c.conditionnement';
	END IF;
	FOR clients_ IN EXECUTE colonne_ || save_ || where_
	LOOP
		i = 0;
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			i = i + 1;
			jour_ = dates_.intitule;
			query_ = ristourne_ || save_ || ' AND e.date_entete BETWEEN'||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND y.id = '||clients_.id;
			IF(total_)THEN
				EXECUTE query_ INTO data_;
				IF(COALESCE(data_.ristourne, 0) != 0)THEN
					INSERT INTO table_et_ristourne_vente VALUES(clients_.id, clients_.code_client, clients_.nom, 0, '', '', 0, jour_, 0, data_.prix_total, data_.ristourne, i);
				END IF;
			ELSE
				IF(cumul_)THEN
					query_ = query_ || ' GROUP BY u.id, c.conditionnement';
				END IF;
				FOR data_ IN EXECUTE query_
				LOOP
					IF(COALESCE(data_.ristourne, 0) != 0)THEN
						INSERT INTO table_et_ristourne_vente VALUES(clients_.id, clients_.code_client, clients_.nom, data_.id, data_.code_users, data_.nom_users, data_.conditionnement, jour_, data_.quantite, data_.prix_total, data_.ristourne, i);
					END IF;
				END LOOP;
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ristourne_vente ORDER BY _client, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean, boolean)
  OWNER TO postgres;


-- Function: get_ca_vente(bigint)
-- DROP FUNCTION get_ca_vente(bigint);
CREATE OR REPLACE FUNCTION get_ca_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	qte_ double precision default 0;
	cs_ double precision default 0;
	remise_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into total_ sum(prix_total) from yvs_com_contenu_doc_vente where doc_vente = id_;
	if(total_ is null)then
		total_ = 0;
	end if;

	-- Recupere le total des remises sur la facture
	remise_ = (select get_remise_vente(id_));
	if(remise_ is null)then
		remise_= 0;
	end if;
	total_ = total_ - remise_;
	
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ca_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ca_vente(bigint) IS 'retourne le chiffre d''affaire d''un doc vente';


-- Function: get_pr(bigint, bigint, bigint, bigint, date, bigint, bigint)
-- DROP FUNCTION get_pr(bigint, bigint, bigint, bigint, date, bigint, bigint);
CREATE OR REPLACE FUNCTION get_pr(article_ bigint, agence_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	_depot_ bigint ;

BEGIN
	IF(COALESCE(depot_, 0) > 0)THEN
		RETURN get_pr(article_, depot_, tranche_, date_, unite_, current_);
	ELSE
		_depot_ := (SELECT ad.depot FROM yvs_base_article_depot ad INNER JOIN yvs_base_depots d ON ad.depot = d.id WHERE ad.article = article_ AND ad.default_pr IS TRUE AND d.agence = agence_ LIMIT 1);
		RETURN get_pr(article_, _depot_, tranche_, date_, unite_, current_);
	END IF;
	RETURN 0;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, bigint, date, bigint, bigint) IS 'retourne le prix de revient d'' article';



-- Function: com_et_listing_vente(bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying)
-- DROP FUNCTION com_et_listing_vente(bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION com_et_listing_vente(societe_ bigint, agence_ bigint, point_ bigint, users_ bigint, client_ bigint, article_ bigint, unite_ bigint, date_debut_ date, date_fin_ date, type_ character varying)
  RETURNS SETOF yvs_com_contenu_doc_vente AS
$BODY$
declare 

   data_ RECORD;
      
   query_ CHARACTER VARYING DEFAULT 'SELECT  c.* FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_client y ON d.client = y.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id
		INNER JOIN yvs_base_conditionnement o ON c.conditionnement = o.id INNER JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id INNER JOIN yvs_base_point_vente p ON cp.point = p.id 
		INNER JOIN yvs_base_unite_mesure m ON o.unite = m.id INNER JOIN yvs_base_articles b ON c.article = b.id
		WHERE d.type_doc = ''FV'' AND e.date_entete BETWEEN'||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND a.societe = '||societe_;
   
begin 	
	DROP TABLE IF EXISTS table_et_listing_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_listing_vente(_users_ bigint, _code_users_ character varying, _nom_users_ character varying, _point_ bigint, _code_ character varying, _libelle_ character varying, _date_entete_ date, _num_doc_ character varying, _client_ bigint,
		_code_client_ character varying, _nom_ character varying, _nom_client_ character varying, _contenu_ bigint, _article_ bigint, _ref_art_ character varying, _designation_ character varying, _quantite_ double precision,
		_prix_ double precision, _prix_minimal_ double precision, _prix_revient_ double precision, _unite_ bigint, _reference_ character varying);
	DELETE FROM table_et_listing_vente;
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(point_, 0) > 0)THEN
		query_ = query_ || ' AND p.id = '||point_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	IF(COALESCE(client_, 0) > 0)THEN
		query_ = query_ || ' AND y.id = '||client_;
	END IF;
	IF(COALESCE(article_, 0) > 0)THEN
		query_ = query_ || ' AND b.id = '||article_;
	END IF;
	IF(COALESCE(unite_, 0) > 0)THEN
		query_ = query_ || ' AND m.id = '||unite_;
	END IF;
	IF(POSITION('R' IN COALESCE(type_, '')) > 0)THEN
		query_ = query_ || ' AND c.prix < c.pr';
	END IF;
	IF(POSITION('M' IN COALESCE(type_, '')) > 0)THEN
		query_ = query_ || ' AND c.prix < c.puv_min';
	END IF;
	RETURN QUERY EXECUTE query_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_listing_vente(bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;


-- Function: com_et_ecart_inventaire(bigint, bigint, bigint, character varying, character varying, double precision, date, date, character varying, boolean)
-- DROP FUNCTION com_et_ecart_inventaire(bigint, bigint, bigint, character varying, character varying, double precision, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION com_et_ecart_inventaire(IN societe_ bigint, IN agence_ bigint, IN user_ bigint, IN nature_ character varying, IN groupe_ character varying, IN coefficient_ double precision, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN cumul_ boolean)
  RETURNS TABLE(users bigint, code_users character varying, nom_users character varying, article bigint, refart character varying, designation character varying, unite bigint, reference character varying, jour character varying, quantite double precision, pr double precision, puv double precision, total double precision, rang integer) AS
$BODY$
declare 

   users_ RECORD;
   dates_ RECORD;
   data_ RECORD;

   pr_ DOUBLE PRECISION DEFAULT 0;
   puv_ DOUBLE PRECISION DEFAULT 0;
   total_ DOUBLE PRECISION DEFAULT 0;

   query_ CHARACTER VARYING DEFAULT 'SELECT u.id, u.code_users, u.nom_users FROM yvs_users u INNER JOIN yvs_agences a ON u.agence = a.id INNER JOIN yvs_societes s ON a.societe = s.id WHERE s.id = '||societe_;
   
begin 	
	nature_ = COALESCE(nature_, 'MANQUANT');
	DROP TABLE IF EXISTS table_et_ecart_inventaire;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ecart_inventaire(_users_ bigint, _code_users_ character varying, _nom_users_ character varying, _article_ bigint, _refart_ character varying, _designation_ character varying, _unite_ bigint, _reference_ character varying, _jour_ character varying, _quantite_ double precision, _pr_ double precision, _puv_ double precision, _total_ double precision, _rang_ integer);
	DELETE FROM table_et_ecart_inventaire;
	IF(COALESCE(user_, 0) > 0)THEN
		query_ =  query_ || ' AND u.id = '||user_;
	ELSE
		IF(groupe_ = 'VENTE')THEN
			query_ = 'SELECT DISTINCT u.id, u.code_users, u.nom_users FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id INNER JOIN
					yvs_users u ON c.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id INNER JOIN yvs_societes s ON a.societe = s.id WHERE s.id = '||societe_;
			IF(date_debut_ IS NOT NULL AND date_fin_ IS NOT NULL)THEN
				query_ =  query_ || ' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
			END IF;
		ELSIF(groupe_ = 'PRODUCTION')THEN
			query_ = 'SELECT DISTINCT u.id, u.code_users, u.nom_users FROM yvs_prod_session_of o INNER JOIN yvs_prod_session_prod p ON o.session_prod = p.id INNER JOIN
					yvs_users u ON p.producteur = u.id INNER JOIN yvs_agences a ON u.agence = a.id INNER JOIN yvs_societes s ON a.societe = s.id WHERE s.id = '||societe_;					
			IF(date_debut_ IS NOT NULL AND date_fin_ IS NOT NULL)THEN
				query_ =  query_ || ' AND p.date_session BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
			END IF;
		END IF;
		IF(COALESCE(agence_, 0) > 0)THEN
			query_ =  query_ || ' AND a.id = '||agence_;
		END IF;
	END IF;
	FOR users_ IN EXECUTE query_ 
	LOOP
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			IF(cumul_)THEN
				IF(groupe_ = 'PRODUCTION')THEN
					query_ = 'SELECT SUM(y.quantite * (COALESCE((SELECT get_pr(y.article, '||agence_||', 0, 0, '||QUOTE_LITERAL(dates_.date_debut)||', c.id, 0)), 0)) * COALESCE('||coefficient_||', 1)) AS total ';
				ELSE
					query_ = 'SELECT SUM(y.quantite * c.prix * COALESCE('||coefficient_||', 1)) AS total ';
				END IF;
				query_ =  query_ || 'FROM yvs_com_contenu_doc_stock y INNER JOIN yvs_base_conditionnement c ON y.conditionnement = c.id 
					INNER JOIN yvs_com_doc_stocks d ON y.doc_stock = d.id INNER JOIN yvs_com_doc_stocks i ON d.document_lie = i.id
					WHERE i.type_doc = ''IN'' AND i.editeur = '||users_.id||' AND i.statut = ''V'' 
					AND i.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
				IF(nature_ = 'EXCEDENT')THEN
					query_ = query_ || ' AND d.type_doc = ''SS''';
				ELSE
					query_ = query_ || ' AND d.type_doc = ''ES''';
				END IF;
				EXECUTE query_ INTO data_;
				IF(COALESCE(data_.total, 0) > 0)THEN
					INSERT INTO table_et_ecart_inventaire VALUES(users_.id, users_.code_users, users_.nom_users, 0, '', '', 0, '', dates_.intitule, 0, 0, 0, data_.total, dates_.position);
				END IF;
			ELSE
				query_ = 'SELECT a.id, a.ref_art, a.designation, c.id AS unite, u.reference, SUM(y.quantite) AS quantite, c.prix 
						FROM yvs_com_contenu_doc_stock y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_conditionnement c ON y.conditionnement = c.id
						INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_com_doc_stocks d ON y.doc_stock = d.id INNER JOIN yvs_com_doc_stocks i ON d.document_lie = i.id
						WHERE i.type_doc = ''IN'' AND i.editeur = '||users_.id||' AND i.statut = ''V'' AND i.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
				IF(nature_ = 'EXCEDENT')THEN
					query_ = query_ || ' AND d.type_doc = ''SS''';
				ELSE
					query_ = query_ || ' AND d.type_doc = ''ES''';
				END IF;
				RAISE NOTICE 'query_ : %',query_;
				FOR data_ IN EXECUTE query_ || ' GROUP BY a.id, c.id, u.id '
				LOOP
					total_ = data_.quantite * data_.prix * COALESCE(coefficient_, 1);
					IF(groupe_ = 'PRODUCTION')THEN
						pr_ = COALESCE((SELECT get_pr(data_.id, agence_, 0, 0, dates_.date_debut, data_.unite, 0)), 0);
						total_ = data_.quantite * pr_ * COALESCE(coefficient_, 1);
					END IF;
					INSERT INTO table_et_ecart_inventaire VALUES(users_.id, users_.code_users, users_.nom_users, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, dates_.intitule, data_.quantite, pr_, data_.prix, total_, dates_.position);
				END LOOP;
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ecart_inventaire ORDER BY _users_, _refart_, _rang_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ecart_inventaire(bigint, bigint, bigint, character varying, character varying, double precision, date, date, character varying, boolean)
  OWNER TO postgres;
