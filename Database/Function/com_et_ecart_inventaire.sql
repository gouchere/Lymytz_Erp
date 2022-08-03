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
					query_ = 'SELECT SUM(y.quantite * (COALESCE((SELECT get_pr(agence_,y.article, 0, 0, '||QUOTE_LITERAL(dates_.date_debut)||', c.id, 0)), 0)) * COALESCE('||coefficient_||', 1)) AS total ';
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
						pr_ = COALESCE((SELECT get_pr(agence_,data_.id, 0, 0, dates_.date_debut, data_.unite, 0)), 0);
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
