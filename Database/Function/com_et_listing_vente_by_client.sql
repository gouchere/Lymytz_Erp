-- Function: com_et_listing_vente_by_client(bigint, bigint, bigint, bigint, bigint, date, date, character varying, integer)
-- DROP FUNCTION com_et_listing_vente_by_client(bigint, bigint, bigint, bigint, bigint, date, date, character varying, integer);
CREATE OR REPLACE FUNCTION com_et_listing_vente_by_client(IN societe_ bigint, IN agence_ bigint, IN zone_ bigint, IN users_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN cumul_ integer)
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
						WHERE d.type_doc = ''FV'' AND e.date_entete BETWEEN'||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND a.societe = '||societe_;
   where_ CHARACTER VARYING DEFAULT '';
   query_ CHARACTER VARYING DEFAULT '';
   code_user_ CHARACTER VARYING DEFAULT '';
   name_user_ CHARACTER VARYING DEFAULT '';
   
   cond_ BIGINT DEFAULT 0;

   i integer default 0;
   
begin 	
	-- Si cumul=0 On ne cumule pas (détaillé)
	-- Si cumul=1 On ne cumule par client et par articles
	-- Si cumul=2 On ne cumule par client (récapitulatif)
	-- Si cumul=3 On ne cumule par Article Vendeur
	DROP TABLE IF EXISTS table_listing_vente_by_client;
	CREATE TEMP TABLE IF NOT EXISTS table_listing_vente_by_client(_client bigint, _code character varying, _nom character varying, _users bigint, _code_users character varying, _nom_users character varying, _unite bigint, _entete character varying, _quantite double precision, _prix_total double precision, _ristourne double precision, _rang integer);
	DELETE FROM table_listing_vente_by_client;
	IF(users_ IS NOT NULL AND users_ > 0)THEN
		save_ = save_ || ' AND u.id = '||users_;
	END IF;
	IF(client_ IS NOT NULL AND client_ > 0)THEN
		where_ = where_ || ' AND y.id = '||client_;
	END IF;
	IF(COALESCE(agence_,0)> 0)THEN
		where_ = where_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(zone_,0) > 0)THEN
		where_ = where_ || ' AND y.ligne = '||zone_;
	END IF;
-- 	RAISE NOTICE 'query_ %',query_;
		IF(cumul_=0)THEN
			
		ELSIF(cumul_=1) THEN --Par client et par article
			ristourne_ = 'SELECT y.id, c.article, SUM(COALESCE(c.ristourne, 0)) as ristourne, SUM(COALESCE(c.quantite, 0)) as quantite, SUM(COALESCE(c.prix_total, 0)) as prix_total, c.conditionnement';
		ELSIF(cumul_=2) THEN --Par clients
			ristourne_ = 'SELECT y.id, y.code_client, SUM(COALESCE(c.ristourne, 0)) as ristourne, SUM(COALESCE(c.quantite, 0)) as quantite, SUM(COALESCE(c.prix_total, 0)) as prix_total';
		ELSIF(cumul_=3) THEN --Par article et par vendeur
			ristourne_ = 'SELECT u.id, u.code_users, u.nom_users, SUM(COALESCE(c.ristourne, 0)) as ristourne, SUM(COALESCE(c.quantite, 0)) as quantite, SUM(COALESCE(c.prix_total, 0)) as prix_total, c.conditionnement';
		END IF;
	FOR clients_ IN EXECUTE colonne_ || save_ || where_
	LOOP
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			jour_ = dates_.intitule;
			query_ = ristourne_ || save_ || ' AND e.date_entete BETWEEN'||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND y.id = '||clients_.id;
			IF(cumul_=0)THEN
				--rien	
			ELSIF(cumul_=1) THEN --Par client et par article
					query_ = query_ || ' GROUP BY y.id, c.article, c.conditionnement';
			ELSIF(cumul_=2) THEN --Par clients
					query_ = query_ || ' GROUP BY y.id, y.code_client';					
			ELSIF(cumul_=3) THEN --Par article et par vendeur
					query_ = query_ || ' GROUP BY u.id, c.conditionnement';
			END IF;
			FOR data_ IN EXECUTE query_
			LOOP
				IF(cumul_=0)THEN
					code_user_=data_.code_users;
					name_user_=data_.nom_users;
					cond_=data_.conditionnement;
				END IF;
				IF(cumul_=3)THEN
					code_user_=data_.code_users;
					name_user_=data_.nom_users;
					cond_=data_.conditionnement;
				END IF;
				IF(cumul_=1)THEN
					cond_=data_.conditionnement;
				END IF;
				INSERT INTO table_listing_vente_by_client VALUES(clients_.id, clients_.code_client, clients_.nom, data_.id, code_user_, name_user_, cond_, jour_, data_.quantite, data_.prix_total, data_.ristourne, dates_.position);
			END LOOP;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_listing_vente_by_client ORDER BY _code, _rang, _unite;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_listing_vente_by_client(bigint, bigint, bigint, bigint, bigint, date, date, character varying, integer)
  OWNER TO postgres;
