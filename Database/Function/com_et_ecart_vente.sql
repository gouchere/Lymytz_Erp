-- Function: com_et_ecart_vente(bigint, bigint, character varying, date, date, character varying, boolean)

-- DROP FUNCTION com_et_ecart_vente(bigint, bigint, character varying, date, date, character varying, boolean);

CREATE OR REPLACE FUNCTION com_et_ecart_vente(IN societe_ bigint, IN agence_ bigint, IN element_ character varying, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN by_point_ boolean)
  RETURNS TABLE(agence bigint, element bigint, code character varying, nom character varying, entete character varying, versement_attendu double precision, versement_reel double precision, ecart double precision, solde double precision, rang integer, solde_initial boolean, manquant_planifier double precision, manquant_traite double precision, manquant_hors_limit boolean) AS
$BODY$
declare 

   vendeur_ RECORD;
   dates_ RECORD;
   
   jour_ character varying;
   ids_ character varying default '0';
   
   versement_attendu_ double precision default 0;
   versement_reel_ double precision default 0;
   ecart_ double precision default 0;
   solde_ double precision default 0;
   manquant_planifier_ double precision default 0;
   manquant_traite_ double precision default 0;
   
   manquant_hors_limit_ boolean default false;
   
   query_ CHARACTER VARYING DEFAULT '';
   query_montant_ CHARACTER VARYING DEFAULT '';

   i integer default 0;

   date_initial_ DATE DEFAULT '01-01-2000';
   date_debut_initial_ DATE DEFAULT date_debut_ - interval '1 day';
   
begin 	
	DROP TABLE IF EXISTS table_et_ecart_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ecart_vente(_agence bigint, _element bigint, _code character varying, _nom character varying, _entete character varying, _versement_attendu double precision, _versement_reel double precision, _ecart double precision, _solde double precision, _rang integer, _solde_initial boolean, _manquant_planifier double precision, _manquant_traite double precision, _manquant_hors_limit boolean);
	DELETE FROM table_et_ecart_vente;
	IF(by_point_)THEN
		query_ = 'SELECT DISTINCT y.id::bigint as id, y.code as code, y.libelle as nom';
	ELSE
		query_ = 'SELECT DISTINCT y.id::bigint as id, y.code_users as code, y.nom_users as nom';
	END IF;
	query_ = query_ || ', y.agence::bigint as agence FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id ';
	IF(by_point_)THEN
		query_ = query_ || ' INNER JOIN yvs_base_point_vente y ON c.point = y.id';
	ELSE
		query_ = query_ || ' INNER JOIN yvs_users y ON h.users = y.id';
	END IF;
	query_ = query_ || ' INNER JOIN yvs_agences a ON y.agence = a.id WHERE e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND y.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(COALESCE(element_, '') NOT IN ('', ' ', '0'))THEN
		FOR jour_ IN SELECT head FROM regexp_split_to_table(element_,',') head
		LOOP
			ids_ = ids_ ||','||jour_;
		END LOOP;
		query_ = query_ || ' AND y.id IN ('||ids_||')';
	END IF;
	RAISE NOTICE 'query_ %',query_;
	FOR vendeur_ IN EXECUTE query_
	LOOP
		i = 0;
		-- SOLDE INITIAL		
		IF(by_point_)THEN
			versement_attendu_ = (SELECT com_get_versement_attendu_point(vendeur_.id, date_initial_, date_debut_initial_));
		ELSE
			versement_attendu_ = (SELECT com_get_versement_attendu_vendeur(vendeur_.id, date_initial_, date_debut_initial_));
		END IF;
		versement_attendu_ = COALESCE(versement_attendu_, 0);
		
		manquant_hors_limit_ = false;
		IF(by_point_)THEN
			query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source IN (SELECT DISTINCT h.users FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE c.point = '||vendeur_.id||')';
		ELSE
			query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source = '||vendeur_.id;
			manquant_planifier_ = (SELECT (y.montant) FROM yvs_com_ecart_entete_vente y INNER JOIN yvs_com_reglement_ecart_vente r ON r.piece = y.id WHERE y.users = vendeur_.id AND y.date_debut BETWEEN date_debut_ AND date_fin_ AND date_fin_ < y.date_ecart AND r.statut = 'P');
			IF(COALESCE(manquant_planifier_, 0) > 0)THEN
				manquant_hors_limit_ = true;
			END IF;
		END IF;
		query_montant_ = query_montant_ || ' AND y.statut_piece = ''P'' AND y.date_paiement::date BETWEEN '||QUOTE_LITERAL(date_initial_)||' AND '||QUOTE_LITERAL(date_debut_initial_);
		EXECUTE query_montant_ INTO versement_reel_;
		versement_reel_ = COALESCE(versement_reel_, 0);
		ecart_ = versement_attendu_ - versement_reel_;
		solde_ = ecart_;
		IF(ecart_ != 0)THEN
			INSERT INTO table_et_ecart_vente VALUES(vendeur_.agence, vendeur_.id::bigint, vendeur_.code, vendeur_.nom, 'ECART INITIAL', versement_attendu_, versement_reel_, ecart_, solde_, i, true, 0, 0, manquant_hors_limit_);
		END IF;
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			i = i + 1;
			jour_ = dates_.intitule;		
			IF(by_point_)THEN
				versement_attendu_ = (SELECT com_get_versement_attendu_point(vendeur_.id, dates_.date_debut, dates_.date_fin));
			ELSE
				versement_attendu_ = (SELECT com_get_versement_attendu(vendeur_.id, dates_.date_debut, dates_.date_fin));
			END IF;
			versement_attendu_ = COALESCE(versement_attendu_, 0);
			
			IF(by_point_)THEN
				query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source IN (SELECT DISTINCT h.users FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE c.point = '||vendeur_.id||')';
			ELSE
				query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source = '||vendeur_.id;
			END IF;
			query_montant_ = query_montant_ || ' AND y.statut_piece = ''P'' AND y.date_paiement::date BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			
			EXECUTE query_montant_ INTO versement_reel_;
			versement_reel_ = COALESCE(versement_reel_, 0);
			
			ecart_ = versement_attendu_ - versement_reel_;
			solde_ = solde_ + ecart_;
			IF(versement_attendu_ != 0 or versement_reel_ != 0)THEN
				manquant_planifier_ = 0;
				manquant_traite_ = 0;
				IF(by_point_ IS FALSE)THEN
					manquant_planifier_ = (SELECT SUM(r.montant) FROM yvs_com_ecart_entete_vente y INNER JOIN yvs_com_reglement_ecart_vente r ON r.piece = y.id WHERE y.users = vendeur_.id AND y.statut = 'V' AND r.date_reglement BETWEEN dates_.date_debut AND dates_.date_fin);
					manquant_traite_ = (SELECT SUM(r.montant) FROM yvs_com_ecart_entete_vente y INNER JOIN yvs_com_reglement_ecart_vente r ON r.piece = y.id WHERE y.users = vendeur_.id AND y.statut = 'V' AND r.date_reglement BETWEEN dates_.date_debut AND dates_.date_fin AND r.statut = 'P');
				END IF;
				RAISE NOTICE 'query_montant_ : % = %',dates_, versement_reel_;
				INSERT INTO table_et_ecart_vente VALUES(vendeur_.agence, vendeur_.id::bigint, vendeur_.code, vendeur_.nom, jour_, versement_attendu_, versement_reel_, ecart_, solde_, i, false, COALESCE(manquant_planifier_, 0), COALESCE(manquant_traite_, 0), manquant_hors_limit_);
			END IF;
		END LOOP;
		i = i + 1;
		manquant_traite_ = (SELECT SUM(r.montant) FROM yvs_com_ecart_entete_vente y INNER JOIN yvs_com_reglement_ecart_vente r ON r.piece = y.id WHERE y.users = vendeur_.id AND y.statut = 'V' AND r.date_reglement BETWEEN date_debut_ AND date_fin_ AND r.statut = 'P');
		IF(COALESCE(manquant_traite_, 0) > 0)THEN
			INSERT INTO table_et_ecart_vente VALUES(vendeur_.agence, vendeur_.id::bigint, vendeur_.code, vendeur_.nom, 'MANQUANTS', 0, 0, 0, 0, i, false, -1, COALESCE(manquant_traite_, 0), manquant_hors_limit_);
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ecart_vente ORDER BY _agence, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ecart_vente(bigint, bigint, character varying, date, date, character varying, boolean)
  OWNER TO postgres;
