-- Function: com_et_dashboard_vendeur(bigint, bigint, bigint, bigint, date, date, boolean)
-- DROP FUNCTION com_et_dashboard_vendeur(bigint, bigint, bigint, bigint, date, date, boolean);
CREATE OR REPLACE FUNCTION com_et_dashboard_vendeur(IN societe_ bigint, IN agence_ bigint, IN client_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN group_ boolean)
  RETURNS TABLE(client bigint, nom_client character varying, numero character varying, date date, montant double precision, avance double precision, acompte double precision, credit double precision, reste double precision, solde_initial double precision, rang integer, vendeur bigint, nom_vendeur character varying) AS
$BODY$
declare 
   ligne_ RECORD;
   dates_ RECORD;
   
   montant_ DOUBLE PRECISION DEFAULT 0;
   avance_ DOUBLE PRECISION DEFAULT 0;
   acompte_ DOUBLE PRECISION DEFAULT 0;
   credit_ DOUBLE PRECISION DEFAULT 0;
   reste_ DOUBLE PRECISION DEFAULT 0;
   solde_initial_ DOUBLE PRECISION DEFAULT 0;

   rang_ INTEGER DEFAULT 0;

   date_initial DATE DEFAULT '01-01-2000';

   query_ CHARACTER VARYING DEFAULT '';
   
begin 	
	DROP TABLE IF EXISTS table_et_dashboard_vendeur;
	CREATE TEMP TABLE IF NOT EXISTS table_et_dashboard_vendeur(_client bigint, _nom_client character varying, _numero character varying, _date date, _montant double precision, _avance double precision, _acompte double precision, _credit double precision, _reste double precision, _solde_initial double precision, _rang integer, _vendeur bigint, _nom_vendeur character varying);
	DELETE FROM table_et_dashboard_vendeur;
	IF(group_)THEN -- SOLDE CUMULES PAR PERIODE
		query_ = 'SELECT DISTINCT cl.id AS client, CONCAT(cl.nom, '' '', cl.prenom) AS nom, u.id AS users, u.nom_users FROM  yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id 
			INNER JOIN yvs_com_client cl ON y.client = cl.id INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id INNER JOIN yvs_users u ON c.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id
			WHERE y.type_doc = ''FV'' AND y.statut = ''V'' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
		IF(societe_ IS NOT NULL AND societe_ > 0)THEN
			query_ = query_ || ' AND a.societe = '||societe_;
		END IF;
		IF(agence_ IS NOT NULL AND agence_ > 0)THEN
			query_ = query_ || ' AND a.id = '||agence_;
		END IF;
		IF(client_ IS NOT NULL AND client_ > 0)THEN
			query_ = query_ || ' AND y.client = '||client_;
		END IF;
		IF(vendeur_ IS NOT NULL AND vendeur_ > 0)THEN
			query_ = query_ || ' AND c.users = '||vendeur_;
		END IF;
-- 		RAISE NOTICE 'query_ : %', query_;
		FOR ligne_ IN EXECUTE query_ || ' ORDER BY u.nom_users, nom'
		LOOP
			-- CHIFFRE AFFAIRE 
			montant_ = (SELECT get_ca_client(ligne_.client, ligne_.users, date_debut_, date_fin_));
			-- REGLEMENT 
			SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id WHERE y.statut_piece = 'P' AND d.client = ligne_.client AND y.caissier = ligne_.users AND e.date_entete BETWEEN date_debut_ AND date_fin_;
			reste_ = COALESCE(solde_initial_, 0) + ((COALESCE(montant_, 0) + COALESCE(credit_, 0)) - (COALESCE(avance_, 0) + COALESCE(acompte_, 0)));
			SELECT INTO rang_ MAX(rang_) FROM table_et_dashboard_vendeur;
			rang_ = COALESCE(rang_, 0) + 1;
			INSERT INTO table_et_dashboard_vendeur VALUES(ligne_.client, 'CREANCE', ligne_.nom, date_debut_, COALESCE(montant_, 0), COALESCE(avance_, 0), COALESCE(acompte_, 0), COALESCE(credit_, 0), COALESCE(reste_, 0), COALESCE(solde_initial_, 0), rang_, ligne_.users, ligne_.nom_users);
		END LOOP;	
	ELSE	-- SOLDE DETAILLE PAR FACTURE
		query_ = 'SELECT y.id, y.num_doc, e.date_entete, cl.id AS client, CONCAT(cl.nom, '' '', cl.prenom) AS nom, u.id AS users, u.nom_users FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id 
			INNER JOIN yvs_com_client cl ON y.client = cl.id INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id INNER JOIN yvs_users u ON c.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id
			WHERE y.type_doc = ''FV'' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
		IF(societe_ IS NOT NULL AND societe_ > 0)THEN
			query_ = query_ || ' AND a.societe = '||societe_;
		END IF;
		IF(agence_ IS NOT NULL AND agence_ > 0)THEN
			query_ = query_ || ' AND a.id = '||agence_;
		END IF;
		IF(client_ IS NOT NULL AND client_ > 0)THEN
			query_ = query_ || ' AND y.client = '||client_;
		END IF;
		IF(vendeur_ IS NOT NULL AND vendeur_ > 0)THEN
			query_ = query_ || ' AND c.users = '||vendeur_;
		END IF;
		RAISE NOTICE 'query_ : %', query_;
		FOR ligne_ IN EXECUTE query_ || ' ORDER BY e.date_entete'
		LOOP
			-- CHIFFRE AFFAIRE 
			montant_ = (SELECT get_ca_vente(ligne_.id));
			-- REGLEMENT 
			SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y WHERE y.statut_piece = 'P' AND y.vente = ligne_.id;
			-- RAISE NOTICE 'avance_ : %', avance_;
			reste_ = COALESCE(solde_initial_, 0) + ((COALESCE(montant_, 0) + COALESCE(credit_, 0)) - (COALESCE(avance_, 0) + COALESCE(acompte_, 0)));
-- 			SELECT INTO rang_ MAX(rang_) FROM table_et_dashboard_vendeur;
			rang_ = COALESCE(rang_, 0) + 1;
			INSERT INTO table_et_dashboard_vendeur VALUES(ligne_.client, ligne_.nom, ligne_.num_doc, ligne_.date_entete, COALESCE(montant_, 0), COALESCE(avance_, 0), COALESCE(acompte_, 0), COALESCE(credit_, 0), COALESCE(reste_, 0), COALESCE(solde_initial_, 0), rang_, ligne_.users, ligne_.nom_users);
		END LOOP;
	END IF;
	RETURN QUERY SELECT * FROM table_et_dashboard_vendeur ORDER BY _rang, _date DESC, numero DESC;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_dashboard_vendeur(bigint, bigint, bigint, bigint, date, date, boolean)
  OWNER TO postgres;
