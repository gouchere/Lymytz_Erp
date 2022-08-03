-- Function: com_et_bonus_vente(bigint, bigint, bigint, bigint, bigint, date, date, character varying, integer)
-- DROP FUNCTION com_et_bonus_vente(bigint, bigint, bigint, bigint, bigint, date, date, character varying, integer);
CREATE OR REPLACE FUNCTION com_et_bonus_vente(IN societe_ bigint, IN agence_ bigint, IN zone_ bigint, IN users_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN cumul_ integer)
  RETURNS TABLE(client bigint, code character varying, nom character varying, users bigint, code_users character varying, nom_users character varying, unite bigint, entete character varying, quantite double precision, prix_total double precision, bonus double precision, rang integer, id_facture bigint, numero_facture character varying) AS
$BODY$
declare 

   clients_ RECORD;
   data_ RECORD;
   dates_ RECORD;
   
   jour_ character varying;
   
   bonus_ CHARACTER VARYING DEFAULT 'SELECT u.id, u.code_users, u.nom_users, c.quantite, c.quantite_bonus as bonus, c.prix_total, c.conditionnement_bonus as conditionnement, d.id as facture, d.num_doc';
   colonne_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT y.id, y.code_client, CONCAT(y.nom, '' '', y.prenom) AS nom';
   save_ CHARACTER VARYING DEFAULT ' FROM yvs_com_client y INNER JOIN yvs_com_doc_ventes d ON d.client = y.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
						INNER JOIN yvs_com_contenu_doc_vente c ON c.doc_vente = d.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id
						INNER JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id INNER JOIN yvs_base_point_vente p ON cp.point = p.id INNER JOIN yvs_agences a ON p.agence = a.id
						WHERE d.type_doc = ''FV'' AND e.date_entete BETWEEN'||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND COALESCE(c.quantite_bonus, 0) > 0 AND a.societe = '||societe_;
   where_ CHARACTER VARYING DEFAULT '';
   query_ CHARACTER VARYING DEFAULT '';
   code_user_ CHARACTER VARYING DEFAULT '';
   name_user_ CHARACTER VARYING DEFAULT '';
   
   cond_ BIGINT DEFAULT 0;

   i integer default 0;
   
begin 	
	-- Si cumul=0 On ne cumule pas (détaillé)
	-- Si cumul=1 On cumule par client et par articles
	-- Si cumul=2 On cumule par Article Vendeur
	-- Si cumul=3 On ne cumule pas (par facture)
	DROP TABLE IF EXISTS table_et_bonus_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_bonus_vente(_client bigint, _code character varying, _nom character varying, _users bigint, _code_users character varying, _nom_users character varying, _unite bigint, _entete character varying, _quantite double precision, _prix_total double precision, _bonus double precision, _rang integer, _id_facture bigint, _numero_facture character varying);
	DELETE FROM table_et_bonus_vente;
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
	IF(cumul_=0)THEN
		
	ELSIF(cumul_=1) THEN --Par client et par article
		bonus_ = 'SELECT y.id, c.article_bonus as article, SUM(COALESCE(c.quantite_bonus, 0)) as bonus, SUM(COALESCE(c.quantite, 0)) as quantite, SUM(COALESCE(c.prix_total, 0)) as prix_total, c.conditionnement_bonus as conditionnement, null::bigint AS facture, null AS num_doc';
	ELSIF(cumul_=2) THEN --Par article et par vendeur
		bonus_ = 'SELECT u.id, u.code_users, u.nom_users, SUM(COALESCE(c.quantite_bonus, 0)) as bonus, SUM(COALESCE(c.quantite, 0)) as quantite, SUM(COALESCE(c.prix_total, 0)) as prix_total, c.conditionnement_bonus as conditionnement, null::bigint AS facture, null AS num_doc';
	END IF;
	FOR clients_ IN EXECUTE colonne_ || save_ || where_
	LOOP
		i = 0;
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			i = i + 1;
			jour_ = dates_.intitule;
			query_ = bonus_ || save_ || where_ || ' AND e.date_entete BETWEEN'||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND y.id = '||clients_.id;
			IF(cumul_=0 or cumul_=4)THEN
				--rien	
			ELSIF(cumul_=1) THEN --Par client et par article
					query_ = query_ || ' GROUP BY y.id, c.article_bonus, c.conditionnement_bonus';					
			ELSIF(cumul_=2) THEN --Par article et par vendeur
					query_ = query_ || ' GROUP BY u.id, c.conditionnement_bonus';
			END IF;
			RAISE NOTICE 'query_ %',query_;
			FOR data_ IN EXECUTE query_
				LOOP
					IF(cumul_=0)THEN
						code_user_=data_.code_users;
						name_user_=data_.nom_users;
						cond_=data_.conditionnement;
					END IF;
					IF(cumul_=2)THEN
						code_user_=data_.code_users;
						name_user_=data_.nom_users;
						cond_=data_.conditionnement;
					END IF;
					IF(cumul_=1)THEN
						cond_=data_.conditionnement;
					END IF;
					IF(COALESCE(data_.bonus, 0) != 0)THEN
						INSERT INTO table_et_bonus_vente VALUES(clients_.id, clients_.code_client, clients_.nom, data_.id, code_user_, name_user_, cond_, jour_, data_.quantite, data_.prix_total, data_.bonus, i, data_.facture, data_.num_doc);
					END IF;
			END LOOP;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_bonus_vente ORDER BY _client, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_bonus_vente(bigint, bigint, bigint, bigint, bigint, date, date, character varying, integer)
  OWNER TO postgres;
