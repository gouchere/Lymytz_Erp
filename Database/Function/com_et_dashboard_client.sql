-- Function: com_et_dashboard_client(bigint, bigint, bigint, date, date, character varying, character varying, boolean, boolean, boolean)

-- DROP FUNCTION com_et_dashboard_client(bigint, bigint, bigint, date, date, character varying, character varying, boolean, boolean, boolean);

CREATE OR REPLACE FUNCTION com_et_dashboard_client(IN societe_ bigint, IN agence_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN type_ character varying, IN group_ boolean, IN solde_ boolean, IN clear_ boolean)
  RETURNS TABLE(id bigint, numero character varying, date date, montant double precision, avance double precision, acompte double precision, credit double precision, reste double precision, solde_initial double precision, rang integer) AS
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
   
begin 	
-- 	DROP TABLE IF EXISTS table_et_dashboard_client;
	CREATE TEMP TABLE IF NOT EXISTS table_et_dashboard_client(_id bigint, _numero character varying, _date date, _montant double precision, _avance double precision, _acompte double precision, _credit double precision, _reste double precision, _solde_initial double precision, _rang integer);
	IF(clear_)THEN
		DELETE FROM table_et_dashboard_client;
	END IF;
	IF(COALESCE(client_, 0) > 0)THEN
		SELECT INTO ligne_ y.id, t.nom, t.prenom FROM yvs_com_client y INNER JOIN yvs_base_tiers t ON y.tiers = t.id WHERE y.id = client_;
		IF(COALESCE(ligne_.id, 0) > 0)THEN
			IF(type_ = 'C')THEN -- SOLDE GENERAL DU CLIENT
				IF(group_)THEN
					FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
					LOOP
						PERFORM com_et_dashboard_client(societe_, agence_, client_, dates_.date_debut, dates_.date_fin, periode_, type_, false, true, false);
					END LOOP;
				ELSE
					-- SOLDE INITIAL
					solde_initial_ = 0;
					IF(solde_)THEN
						SELECT INTO solde_initial_ s.reste FROM com_et_dashboard_client(societe_, agence_, client_, date_initial, (date_debut_ - interval '1 day')::date, periode_, type_, false, false, false) s ORDER BY s.rang DESC LIMIT 1;
						DELETE FROM table_et_dashboard_client WHERE _date = date_initial;
					END IF;
					-- CHIFFRE AFFAIRE 
					SELECT INTO montant_ SUM(get_ttc_vente(y.id)) FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id WHERE y.type_doc = 'FV' AND y.statut = 'V' AND y.client = client_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
					-- REGLEMENT 
					SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE y.statut_piece = 'P' AND d.client = client_ AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
					-- ACOMPTES
					SELECT INTO acompte_ SUM(y.montant - COALESCE((SELECT SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_compta_notif_reglement_vente n ON p.id = n.piece_vente WHERE p.statut_piece = 'P' AND n.acompte = y.id) ,0)) FROM yvs_compta_acompte_client y WHERE y.statut = 'P' AND y.client = client_ AND COALESCE(y.date_paiement, y.date_acompte) BETWEEN date_debut_ AND date_fin_;
					-- CREDITS
					SELECT INTO credit_ SUM(y.montant - COALESCE((SELECT SUM(p.valeur) FROM yvs_compta_reglement_credit_client p WHERE p.statut = 'P' AND p.credit = y.id) ,0)) FROM yvs_compta_credit_client y WHERE y.statut = 'V' AND y.client = client_ AND y.date_credit BETWEEN date_debut_ AND date_fin_;
					-- SOLDE
					reste_ = COALESCE(montant_, 0) - COALESCE(avance_, 0) - COALESCE(acompte_, 0) + COALESCE(credit_, 0) + COALESCE(solde_initial_, 0);
					SELECT INTO rang_ MAX(_rang) FROM table_et_dashboard_client;
					rang_ = COALESCE(rang_, 0) + 1;
					IF(COALESCE(montant_, 0) != 0 OR COALESCE(credit_, 0) != 0 OR COALESCE(avance_, 0) != 0 OR COALESCE(acompte_, 0) != 0 OR rang_ <= 1)THEN
						INSERT INTO table_et_dashboard_client VALUES(ligne_.id, CONCAT(ligne_.nom, ' ', ligne_.prenom), date_debut_, COALESCE(montant_, 0), COALESCE(avance_, 0), COALESCE(acompte_, 0), COALESCE(credit_, 0), COALESCE(reste_, 0), COALESCE(solde_initial_, 0), rang_);
					END IF;
				END IF;
			ELSIF(type_ = 'F')THEN -- SOLDE DES FACTURES DU CLIENT
				IF(group_)THEN -- SOLDE CUMULES PAR PERIODE
					FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
					LOOP
						-- CHIFFRE AFFAIRE 
						SELECT INTO montant_ SUM(get_ttc_vente(y.id)) FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id WHERE y.type_doc = 'FV' AND y.statut = 'V' AND y.client = client_ AND e.date_entete BETWEEN dates_.date_debut AND dates_.date_fin;
						-- REGLEMENT 
						SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id WHERE y.statut_piece = 'P' AND d.client = client_ AND e.date_entete BETWEEN dates_.date_debut AND dates_.date_fin;
						reste_ = COALESCE(solde_initial_, 0) + ((COALESCE(montant_, 0) + COALESCE(credit_, 0)) - (COALESCE(avance_, 0) + COALESCE(acompte_, 0)));
						IF(reste_ != 0)THEN
							SELECT INTO rang_ MAX(rang_) FROM table_et_dashboard_client;
							rang_ = COALESCE(rang_, 0) + 1;
							INSERT INTO table_et_dashboard_client VALUES(client_, dates_.intitule, dates_.date_debut, COALESCE(montant_, 0), COALESCE(avance_, 0), COALESCE(acompte_, 0), COALESCE(credit_, 0), COALESCE(reste_, 0), COALESCE(solde_initial_, 0), rang_);
						END IF;
					END LOOP;
				ELSE	-- SOLDE DETAILLE PAR FACTURE
					FOR ligne_ IN SELECT y.id, y.num_doc, e.date_entete FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id WHERE y.type_doc = 'FV' AND y.client = client_ AND e.date_entete BETWEEN date_debut_ AND date_fin_ ORDER BY e.date_entete
					LOOP
						-- CHIFFRE AFFAIRE 
						montant_ = (SELECT get_ttc_vente(ligne_.id));
						-- REGLEMENT 
						SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y WHERE y.statut_piece = 'P' AND y.vente = ligne_.id;
						reste_ = COALESCE(solde_initial_, 0) + ((COALESCE(montant_, 0) + COALESCE(credit_, 0)) - (COALESCE(avance_, 0) + COALESCE(acompte_, 0)));
						IF(reste_ != 0)THEN
							SELECT INTO rang_ MAX(rang_) FROM table_et_dashboard_client;
							rang_ = COALESCE(rang_, 0) + 1;
							INSERT INTO table_et_dashboard_client VALUES(ligne_.id, ligne_.num_doc, ligne_.date_entete, COALESCE(montant_, 0), COALESCE(avance_, 0), COALESCE(acompte_, 0), COALESCE(credit_, 0), COALESCE(reste_, 0), COALESCE(solde_initial_, 0), rang_);
						END IF;
					END LOOP;
				END IF;
			ELSIF(type_ = 'A')THEN -- SOLDE DES ACOMPTES DU CLIENT
				IF(group_)THEN -- SOLDE CUMULES PAR PERIODE
					FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
					LOOP
						-- CHIFFRE AFFAIRE 
						SELECT INTO montant_ SUM(y.montant) FROM yvs_compta_acompte_client y WHERE y.statut = 'P' AND y.client = client_ AND y.date_acompte BETWEEN dates_.date_debut AND dates_.date_fin;
						-- REGLEMENT 
						SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_compta_notif_reglement_vente n ON y.id = n.piece_vente INNER JOIN yvs_compta_acompte_client a ON n.acompte = a.id WHERE y.statut_piece = 'P' AND a.client = client_ AND a.date_acompte BETWEEN dates_.date_debut AND dates_.date_fin;
						reste_ = COALESCE(solde_initial_, 0) + ((COALESCE(montant_, 0) + COALESCE(credit_, 0)) - (COALESCE(avance_, 0) + COALESCE(acompte_, 0)));
						IF(reste_ != 0)THEN
							SELECT INTO rang_ MAX(rang_) FROM table_et_dashboard_client;
							rang_ = COALESCE(rang_, 0) + 1;
							INSERT INTO table_et_dashboard_client VALUES(client_, dates_.intitule, dates_.date_debut, COALESCE(montant_, 0), COALESCE(avance_, 0), COALESCE(acompte_, 0), COALESCE(credit_, 0), COALESCE(reste_, 0), COALESCE(solde_initial_, 0), rang_);
						END IF;
					END LOOP;
				ELSE	-- SOLDE DETAILLE PAR FACTURE
					FOR ligne_ IN SELECT y.id, y.num_refrence, y.date_acompte, y.montant FROM yvs_compta_acompte_client y WHERE y.statut = 'P' AND y.client = client_ AND y.date_acompte BETWEEN date_debut_ AND date_fin_ ORDER BY y.date_acompte
					LOOP
						-- CHIFFRE AFFAIRE 
						montant_ = ligne_.montant;
						-- REGLEMENT 
						SELECT INTO avance_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_compta_notif_reglement_vente n ON p.id = n.piece_vente WHERE p.statut_piece = 'P' AND n.acompte = ligne_.id;
						reste_ = COALESCE(solde_initial_, 0) + ((COALESCE(montant_, 0) + COALESCE(credit_, 0)) - (COALESCE(avance_, 0) + COALESCE(acompte_, 0)));
						IF(reste_ != 0)THEN
							SELECT INTO rang_ MAX(rang_) FROM table_et_dashboard_client;
							rang_ = COALESCE(rang_, 0) + 1;
							INSERT INTO table_et_dashboard_client VALUES(ligne_.id, ligne_.num_refrence, ligne_.date_acompte, COALESCE(montant_, 0), COALESCE(avance_, 0), COALESCE(acompte_, 0), COALESCE(credit_, 0), COALESCE(reste_, 0), COALESCE(solde_initial_, 0), rang_);
						END IF;
					END LOOP;
				END IF;
			END IF;
		END IF;
	END IF;
	RETURN QUERY SELECT * FROM table_et_dashboard_client ORDER BY _rang, _date DESC, numero DESC;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_dashboard_client(bigint, bigint, bigint, date, date, character varying, character varying, boolean, boolean, boolean)
  OWNER TO postgres;
