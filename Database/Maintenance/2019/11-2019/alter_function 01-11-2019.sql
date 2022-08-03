-- Function: com_et_journal_vente(bigint, bigint, date, date, boolean)
-- DROP FUNCTION com_et_journal_vente(bigint, bigint, date, date, boolean);
CREATE OR REPLACE FUNCTION com_et_journal_vente(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN by_famille_ boolean)
  RETURNS TABLE(agence bigint, users bigint, code character varying, nom character varying, classe bigint, reference character varying, designation character varying, montant double precision, is_classe boolean, is_vendeur boolean, rang integer) AS
$BODY$
declare 
   vendeur_ RECORD;
   classe_ record;
   
   journal_ character varying;
   
   valeur_ double precision default 0;
   totaux_ double precision;
   
   query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.code_users, y.nom_users as nom, y.agence FROM yvs_users y INNER JOIN yvs_agences a ON y.agence = a.id WHERE code_users IS NOT NULL';
   
begin 	
	--DROP TABLE table_et_journal_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_journal_vente(_agence BIGINT, _users BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _classe BIGINT, _reference CHARACTER VARYING, _designation CHARACTER VARYING, _montant DOUBLE PRECISION, _is_classe BOOLEAN, _is_vendeur BOOLEAN, _rang INTEGER);
	DELETE FROM table_et_journal_vente;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND y.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	FOR vendeur_ IN EXECUTE query_
	LOOP
		IF(COALESCE(by_famille_, FALSE))THEN
			-- Vente directe par famille d'article
			FOR classe_ IN SELECT s.id, UPPER(s.reference_famille) AS code, UPPER(s.designation) AS intitule, SUM(c.prix_total - c.ristourne) AS valeur FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc 
				INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article s ON a.famille = s.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND s.societe = societe_ AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_ GROUP BY s.id
			LOOP
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(classe_.valeur, 0), TRUE, TRUE, 0);
			END LOOP;
		ELSE
			-- Vente directe par classe statistique
			FOR classe_ IN SELECT s.id, UPPER(s.code_ref) AS code, UPPER(s.designation) AS intitule, SUM(c.prix_total - c.ristourne) AS valeur FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc 
				INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_classes_stat s ON a.classe1 = s.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND s.societe = societe_ AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_ GROUP BY s.id
			LOOP
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(classe_.valeur, 0), TRUE, TRUE, 0);	
			END LOOP;
			
			-- CA Des article non classé
			SELECT INTO valeur_ SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
				INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND a.classe1 IS NULL AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), TRUE, TRUE, 0);
			END IF;
		END IF;
		
		-- CA Par vendeur
		SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = vendeur_.id; 	
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -1, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, TRUE, 1);
		
		-- Ristournes vente directe
		SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
			WHERE hu.users = vendeur_.id AND e.date_entete BETWEEN date_debut_ AND date_fin_ AND d.type_doc = 'FV' AND d.statut = 'V' AND (d.document_lie IS NULL OR (d.document_lie IS NOT NULL AND d.statut_regle IN ('R', 'P'))));
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -2, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, TRUE, 2);
		
		-- Commandes Annulées
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'A' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -3, 'CMDE.A', 'CMDE.A', COALESCE(valeur_, 0), FALSE, TRUE, 3);
					
		-- Commandes Recu
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'V' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -4, 'CMDE.R', 'CMDE.R', COALESCE(valeur_, 0), FALSE, TRUE, 4);		
		
		-- Versement attendu	
		SELECT INTO valeur_ com_get_versement_attendu_vendeur(vendeur_.id, date_debut_, date_fin_); 
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -5, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, TRUE, 5);	

		SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE y._users = vendeur_.id;
		IF(COALESCE(valeur_, 0) = 0)THEN
			DELETE FROM table_et_journal_vente WHERE _users = vendeur_.id;
		END IF;
	END LOOP;
	
	-- LIGNE DES COMMANDE SERVIES PAR CLASSE STAT
	query_ = 'SELECT SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
			WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND d.document_lie IS NOT NULL AND e.date_entete BETWEEN '''||date_debut_||''' AND '''||date_fin_||'''';
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND u.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND g.societe = '||societe_;
	END IF;
	IF(COALESCE(by_famille_, FALSE))THEN
		FOR classe_ IN SELECT c.id, UPPER(c.reference_famille) as code, UPPER(c.designation) as intitule FROM yvs_base_famille_article c WHERE c.societe = societe_
		LOOP			
			EXECUTE query_ || ' AND a.famille = '||classe_.id||')' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;
		END LOOP;
	ELSE
		FOR classe_ IN SELECT c.id, UPPER(c.code_ref) as code, UPPER(c.designation) as intitule FROM yvs_base_classes_stat c WHERE c.societe = societe_
		LOOP			
			EXECUTE query_ || ' AND a.classe1 = '||classe_.id||')' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;
		END LOOP;
		
		-- Autres aticles sans classe
		EXECUTE query_ || ' AND a.classe1 IS NULL) ' INTO valeur_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), FALSE, FALSE, 0);
		END IF;	
	END IF;
	
	-- CA sur commande reçu
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = 0; 
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -1, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, FALSE, 1);
	
	-- Ristourne sur commande reçu
	SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
		INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
		INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
		WHERE g.id = agence_ AND d.type_doc = 'BCV' AND d.statut = 'V' AND d.statut_livre = 'L' AND d.date_livraison BETWEEN date_debut_ AND date_fin_);
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -2, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, FALSE, 2);
	
	-- CMDE.A sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -3, 'CMDE.A', 'CMDE.A', 0, FALSE, FALSE, 3);
	
	-- CMDE.R sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -4, 'CMDE.R', 'CMDE.R', 0, FALSE, FALSE, 4);
	
	-- VERS.ATT sur commande reçu
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = 0 AND _rang > 0; 
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -5, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, FALSE, 5);

	FOR classe_ IN SELECT _classe, _reference, _designation, _rang, SUM(_montant) AS _valeur FROM table_et_journal_vente GROUP BY _classe, _reference, _designation, _rang
	LOOP 
		INSERT INTO table_et_journal_vente values (agence_, -1, 'TOTAUX','TOTAUX', classe_._classe, classe_._reference, classe_._designation, classe_._valeur, FALSE, FALSE, classe_._rang);
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_journal_vente ORDER BY _agence, _is_vendeur DESC, _code, _rang, _is_classe DESC, _classe;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_journal_vente(bigint, bigint, date, date, boolean)
  OWNER TO postgres;


-- Function: yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean)
-- DROP FUNCTION yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean);
CREATE OR REPLACE FUNCTION yvs_compta_content_journal(IN societe_ bigint, IN agences_ character varying, IN element_ bigint, IN table_ character varying, IN clear_ boolean)
  RETURNS TABLE(id bigint, jour integer, num_piece character varying, num_ref character varying, compte_general bigint, compte_tiers bigint, libelle character varying, debit double precision, credit double precision, echeance date, ref_externe bigint, table_externe character varying, statut character varying, error character varying, contenu bigint, centre bigint, coefficient double precision, numero integer, agence bigint, warning character varying, table_tiers character varying) AS
$BODY$
DECLARE
	_id_ BIGINT DEFAULT 0;
	_jour_ INTEGER DEFAULT 0; 
	_num_piece_ CHARACTER VARYING DEFAULT '';
	_num_ref_ CHARACTER VARYING DEFAULT ''; 
	_compte_general_ BIGINT; 
	_compte_tiers_ BIGINT; 
	_libelle_ CHARACTER VARYING DEFAULT ''; 
	_debit_ DOUBLE PRECISION DEFAULT 0; 
	_credit_ DOUBLE PRECISION DEFAULT 0; 
	_echeance_ DATE DEFAULT CURRENT_DATE; 
	_ref_externe_ BIGINT DEFAULT element_; 
	_table_externe_ CHARACTER VARYING DEFAULT table_; 
	_statut_ CHARACTER VARYING DEFAULT 'V';
	_error_ CHARACTER VARYING DEFAULT '';
	_warning_ CHARACTER VARYING DEFAULT '';
	_contenu_ BIGINT;
	_centre_ BIGINT;
	_coefficient_ DOUBLE PRECISION DEFAULT 0;
	_numero_ INTEGER DEFAULT 0;
	_agence_ BIGINT DEFAULT 0;
	_table_tiers_ CHARACTER VARYING DEFAULT '';

	ligne_ RECORD;
	sous_ RECORD;
	data_ RECORD;
	
	count_ BIGINT; 
	_compte_secondaire_ BIGINT; 
	
	taux_ DOUBLE PRECISION DEFAULT 100;
	total_ DOUBLE PRECISION DEFAULT 0; 
	somme_ DOUBLE PRECISION DEFAULT 0; 
	valeur_ DOUBLE PRECISION DEFAULT 0; 
	valeur_limite_arrondi_ DOUBLE PRECISION DEFAULT 0;
	
	i_ INTEGER DEFAULT 1;

	titre_ CHARACTER VARYING;
	ids_ CHARACTER VARYING DEFAULT '0';
BEGIN    
	IF(clear_)THEN
		DROP TABLE IF EXISTS table_compta_content_journal;
		CREATE TEMP TABLE IF NOT EXISTS table_compta_content_journal(_id BIGINT, _jour INTEGER, _num_piece CHARACTER VARYING, _num_ref CHARACTER VARYING, _compte_general BIGINT, _compte_tiers BIGINT, _libelle CHARACTER VARYING, _debit DOUBLE PRECISION, _credit DOUBLE PRECISION, _echeance DATE, _ref_externe BIGINT, _table_externe CHARACTER VARYING, _statut CHARACTER VARYING, _error character varying, _contenu bigint, _centre bigint, _coefficient double precision, _numero integer, _agence bigint, _warning character varying, _table_tiers character varying);
		DELETE FROM table_compta_content_journal;
	END IF;
	IF(table_ IS NOT NULL AND table_ NOT IN ('', ' '))THEN
		SELECT INTO valeur_limite_arrondi_ valeur_limite_arrondi FROM yvs_compta_parametre WHERE societe = societe_;
		valeur_limite_arrondi_ = COALESCE(valeur_limite_arrondi_, 0);
		IF(table_ = 'DOC_VENTE' OR table_ = 'AVOIR_VENTE')THEN	
			SELECT INTO data_ d.num_doc, d.nom_client, d.tiers, d.client, e.date_entete, c.compte, p.saisie_compte_tiers, u.agence FROM yvs_com_doc_ventes d LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_client c ON d.tiers = c.id 
				 LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_base_plan_comptable p ON c.compte = p.id WHERE d.id = element_;
			IF(data_.num_doc IS NOT NULL AND data_.num_doc NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				total_ = (SELECT get_ttc_vente(element_)); -- RECUPERATION DU TTC DE LA FACTURE
				IF(total_ IS NOT NULL AND total_ > 0)THEN	
					-- RECHERCHE DES ARTICLES SANS COMPTE POUR LA CATEGORIE DE LA FACTURE
					SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
					WHERE d.id = element_ AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) < 1;
					IF(COALESCE(count_, 0) < 1)THEN
						-- RECHERCHE DES ARTICLES AVEC PLUS D'UN COMPTE POUR LA CATEGORIE DE LA FACTURE
						SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
						WHERE d.id = element_ AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) > 1;
						IF(COALESCE(count_, 0) > 0)THEN
							_error_ = 'certains article sont rattaché a plusieurs comptes pour la catégorie de la facture';
						END IF;
					ELSE
						_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
					END IF;
					-- COMPTABILISATION DU TTC DE LA FACTURE
					IF(data_.client IS NULL OR data_.client < 1)THEN
						_error_ = 'cette facture de vente n''est pas rattachée à un client';
					ELSIF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette facture de vente n''est pas rattachée à un compte tiers';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette facture de vente est rattachée à un client qui n''a pas de compte collectif';
					END IF;
					_jour_ = to_char(data_.date_entete ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_entete;
					_compte_general_ = data_.compte;
					IF(data_.saisie_compte_tiers)THEN
						_compte_tiers_ = data_.tiers;
					ELSE
						_compte_tiers_ = null;
					END IF;
					_numero_ = 1;
					FOR ligne_ IN SELECT t.* FROM yvs_base_tranche_reglement t INNER JOIN yvs_base_model_reglement m ON t.model = m.id INNER JOIN yvs_com_doc_ventes d ON d.model_reglement = m.id WHERE d.id = element_ ORDER BY t.numero
					LOOP
						_debit_ = 0;
						_credit_ = 0;
						IF(table_ = 'DOC_VENTE')THEN
							_debit_ = (total_ * ligne_.taux / 100);
							somme_ = somme_ + _debit_;
							_libelle_ = UPPER('Echéancier '||ligne_.taux||'% pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);
						ELSE	
							_credit_ = (total_ * ligne_.taux / 100);
							somme_ = somme_ + _credit_;
							_libelle_ = UPPER('Echéancier '||ligne_.taux||'% pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_echeance_ = _echeance_ + ligne_.interval_jour;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;
					IF(total_ > somme_)THEN
						_debit_ = 0;
						_credit_ = 0;
						IF(somme_ > 0)THEN
							taux_ = (((total_ - somme_) / total_) * 100);
						END IF;
						IF(table_ = 'DOC_VENTE')THEN
							_debit_ = (total_ - somme_);
							_libelle_ = UPPER('Echéancier '||taux_||'% pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);
						ELSE	
							_credit_ = (total_ - somme_);
							_libelle_ = UPPER('Echéancier '||taux_||'% pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
					
					_error_ = null;
					_numero_ = 2;
					_echeance_ = data_.date_entete;
					 -- COMPTE DE RISTOURNE SUR VENTE
					SELECT INTO _compte_secondaire_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = 'RISTOURNE_VENTE';
					-- COMPTABILISATION DES ARTICLES
					FOR ligne_ IN SELECT  SUM(co.prix_total) AS total, SUM(co.ristourne) AS ristourne, pc.id AS compte, pc.intitule, pc.saisie_compte_tiers FROM yvs_com_contenu_doc_vente co INNER JOIN yvs_com_doc_ventes dv ON co.doc_vente = dv.id
						INNER JOIN yvs_base_article_categorie_comptable ca ON ca.article = co.article AND ca.categorie = dv.categorie_comptable LEFT JOIN yvs_base_plan_comptable pc ON ca.compte = pc.id 
						WHERE dv.id = element_ GROUP BY pc.id
					LOOP
						_debit_ = 0;					
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _credit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_vente tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_vente tc INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente 
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE ca.compte = ligne_.compte AND dv.id = element_);
						IF(table_ = 'DOC_VENTE')THEN
							_credit_ = ligne_.total - COALESCE(_credit_, 0);
							_libelle_ = UPPER(ligne_.intitule||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);	
							IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
								_credit_ = _credit_ - ligne_.ristourne;
							END IF;
						ELSE	
							_debit_ = ligne_.total - COALESCE(_credit_, 0);
							_libelle_ = UPPER(ligne_.intitule||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);	
							_credit_ = 0;		
							IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
								_debit_ = _debit_ - ligne_.ristourne;
							END IF;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						IF(ligne_.saisie_compte_tiers)THEN
							_compte_tiers_ = data_.tiers;
						ELSE
							_compte_tiers_ = null;
						END IF;					
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;

						IF(COALESCE(ligne_.ristourne, 0) > 0)THEN
							IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
								_credit_ = 0;
								_debit_ = 0;
								IF(table_ = 'DOC_VENTE')THEN
									_credit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								ELSE	
									_debit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne restituée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								END IF;
								IF(i_ < 10)THEN
									_num_ref_ = data_.num_doc ||'-0'||i_;
								ELSE
									_num_ref_ = data_.num_doc ||'-'||i_;
								END IF;
								_compte_general_ = _compte_secondaire_;
								_compte_tiers_ = null;		
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
							ELSE
								_credit_ = 0;
								_debit_ = 0;
								IF(table_ = 'DOC_VENTE')THEN
									_debit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								ELSE	
									_credit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne restituée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								END IF;
								IF(i_ < 10)THEN
									_num_ref_ = data_.num_doc ||'-0'||i_;
								ELSE
									_num_ref_ = data_.num_doc ||'-'||i_;
								END IF;
								_compte_tiers_ = null;		
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
								
								_credit_ = 0;
								_debit_ = 0;
								IF(table_ = 'DOC_VENTE')THEN
									_credit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								ELSE	
									_debit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne restituée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								END IF;
								IF(i_ < 10)THEN
									_num_ref_ = data_.num_doc ||'-0'||i_;
								ELSE
									_num_ref_ = data_.num_doc ||'-'||i_;
								END IF;
								_compte_general_ = data_.compte;
								_compte_tiers_ = data_.tiers;	
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
							END IF;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation, pc.saisie_compte_tiers FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_vente tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						LEFT JOIN yvs_base_plan_comptable pc ON tx.compte = pc.id WHERE dv.id = element_ GROUP BY tx.compte, tx.designation, pc.id 
					LOOP
						IF(ligne_.total != 0)THEN
							_debit_ = 0;
							_credit_ = 0;
							IF(table_ = 'DOC_VENTE')THEN
								_credit_ = ligne_.total;
								_libelle_ = ligne_.designation||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;	
							ELSE	
								_credit_ = ligne_.total;
								_libelle_ = ligne_.designation||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client;	
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;					
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation, pc.saisie_compte_tiers FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_vente co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						LEFT JOIN yvs_base_plan_comptable pc ON tx.compte = pc.id WHERE dv.id = element_ GROUP BY tx.id, tx.libelle, pc.id 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_VENTE')THEN
								IF(ligne_.augmentation)THEN
									_credit_ = ligne_.total;
								ELSE
									_debit_ = ligne_.total;
								END IF;
								_libelle_ = ligne_.libelle||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;
							ELSE	
								IF(ligne_.augmentation)THEN
									_debit_ = ligne_.total;
								ELSE
									_credit_ = ligne_.total;
								END IF;
								_libelle_ = ligne_.libelle||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							IF(ligne_.saisie_compte_tiers)THEN
								_compte_tiers_ = data_.tiers;
							ELSE
								_compte_tiers_ = null;
							END IF;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'JOURNAL_VENTE')THEN
			SELECT INTO data_ u.code_users, e.date_entete, p.code AS point, u.agence FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id 
				LEFT JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id LEFT JOIN yvs_base_point_vente p ON cp.point = p.id 
				WHERE e.id = element_;
			IF(data_.code_users IS NOT NULL AND data_.code_users NOT IN ('', ' '))THEN		
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;
				SELECT INTO count_ (d.id) FROM yvs_com_doc_ventes d WHERE d.entete_doc = element_ AND d.type_doc = 'FV' AND d.statut = 'V';	
				IF(count_ IS NOT NULL AND count_ > 0)THEN
					-- RECHERCHE DES ARTICLES SANS COMPTE POUR LA CATEGORIE DES FACTURES
					SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
					WHERE d.entete_doc = element_ AND d.type_doc = 'FV' AND d.statut = 'V' AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) < 1;
					IF(COALESCE(count_, 0) < 1)THEN
						-- RECHERCHE DES ARTICLES AVEC PLUS D'UN COMPTE POUR LA CATEGORIE DES FACTURES
						SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
						WHERE d.entete_doc = element_ AND d.type_doc = 'FV' AND d.statut = 'V' AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) > 1;
						IF(COALESCE(count_, 0) > 0)THEN
							_error_ = 'ce journal de vente est rattaché à des articles qui ont plusieurs comptes';
						END IF;
					ELSE
						_error_ = 'ce journal de vente est rattaché à des articles qui n''ont pas de compte';
					END IF;
					-- COMPTABILISATION DU TTC DE LA JOURNEE DE VENTE
					_jour_ = to_char(data_.date_entete ,'dd')::integer;
					_num_piece_ = data_.code_users || '/';
					IF(data_.point IS NOT NULL AND data_.point NOT IN ('', ' '))THEN
						_num_piece_ = _num_piece_ || data_.point || '/';
					ELSE
						_error_ = 'ce journal de vente n'' pas rattaché à un point de vente';
					END IF;
					_numero_ = 1;
					_num_piece_ = _num_piece_ || to_char(data_.date_entete ,'ddMMyy');	
					_echeance_ = data_.date_entete;
					 -- COMPTE DE RISTOURNE SUR VENTE
					SELECT INTO _compte_secondaire_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = 'RISTOURNE_VENTE';
					FOR ligne_ IN SELECT SUM(get_ttc_vente(dv.id)) as total , ts.compte, dv.tiers, CONCAT(ts.nom, ' ', ts.prenom) AS nom, pc.saisie_compte_tiers, SUM((SELECT SUM(co.ristourne) FROM yvs_com_contenu_doc_vente co WHERE co.doc_vente = dv.id)) AS ristourne 
						FROM yvs_com_doc_ventes dv INNER JOIN yvs_com_client ts ON dv.tiers = ts.id LEFT JOIN yvs_base_plan_comptable pc ON ts.compte = pc.id 
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_ AND dv.id NOT IN (SELECT _f.facture FROM yvs_compta_content_journal_facture_vente _f
									INNER JOIN yvs_com_doc_ventes _d ON _f.facture = _d.id WHERE _d.entete_doc = element_)
						GROUP BY dv.entete_doc, dv.tiers, ts.id, pc.id
					LOOP
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains client n''ont pas de compte collectif';
						END IF;
						_debit_ = ligne_.total;
						_credit_ = 0;
						somme_ = somme_ + _debit_;
						_libelle_ = 'Echéancier 100% pour le journal de vente N° '||_num_piece_;
						IF(i_ < 10)THEN
							_num_ref_ = _num_piece_ ||'-0'||i_;
						ELSE
							_num_ref_ = _num_piece_ ||'-'||i_;
						END IF;
						_compte_general_ = ligne_.compte;
						IF(ligne_.saisie_compte_tiers)THEN
							_compte_tiers_ = ligne_.tiers;
						ELSE
							_compte_tiers_ = null;
						END IF;
						_id_ = _id_ - 1;	
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
						
						IF(COALESCE(ligne_.ristourne, 0) > 0)THEN
							IF(COALESCE(_compte_secondaire_, 0) < 1)THEN	
								_debit_ = 0;
								_credit_ = ligne_.ristourne;
								_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||_num_piece_);
								IF(i_ < 10)THEN
									_num_ref_ = _num_piece_ ||'-0'||i_;
								ELSE
									_num_ref_ = _num_piece_ ||'-'||i_;
								END IF;
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
							END IF;
						END IF;
					END LOOP;

					_error_ = null;
					_numero_ = 2;
					-- COMPTABILISATION DES ARTICLES
					FOR ligne_ IN SELECT SUM(co.prix_total) AS total, SUM(co.ristourne) AS ristourne, pc.id AS compte, pc.intitule, pc.saisie_compte_tiers FROM yvs_com_contenu_doc_vente co INNER JOIN yvs_com_doc_ventes dv ON co.doc_vente = dv.id
						INNER JOIN yvs_base_article_categorie_comptable ca ON ca.article = co.article AND ca.categorie = dv.categorie_comptable
						LEFT JOIN yvs_base_plan_comptable pc ON ca.compte = pc.id 
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_
						AND dv.id NOT IN (SELECT _f.facture FROM yvs_compta_content_journal_facture_vente _f INNER JOIN yvs_compta_pieces_comptable _p ON _f.piece = _p.id
								INNER JOIN yvs_com_doc_ventes _d ON _f.facture = _d.id INNER JOIN yvs_compta_journaux _j ON _p.journal = _j.id INNER JOIN yvs_agences _a ON _j.agence = _a.id 
								WHERE _a.societe = societe_ AND _d.entete_doc = element_)
						GROUP BY pc.id
					LOOP
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _credit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_vente tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_vente tc INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente 
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND ca.compte = ligne_.compte AND dv.entete_doc = element_
								AND dv.id NOT IN (SELECT _f.facture FROM yvs_compta_content_journal_facture_vente _f INNER JOIN yvs_compta_pieces_comptable _p ON _f.piece = _p.id
								INNER JOIN yvs_com_doc_ventes _d ON _f.facture = _d.id INNER JOIN yvs_compta_journaux _j ON _p.journal = _j.id INNER JOIN yvs_agences _a ON _j.agence = _a.id 
								WHERE _a.societe = societe_ AND _d.entete_doc = element_));
						_credit_ = ligne_.total - COALESCE(_credit_, 0);
						IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
							_credit_ = _credit_ - ligne_.ristourne;
						END IF;
						_debit_ = 0;
						IF(i_ < 10)THEN
							_num_ref_ = _num_piece_ ||'-0'||i_;
						ELSE
							_num_ref_ = _num_piece_ ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;
						_libelle_ = ligne_.intitule||' pour le journal de vente N° '||_num_piece_;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;

						IF(COALESCE(ligne_.ristourne, 0) > 0)THEN
							IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
								_credit_ = ligne_.ristourne;
								_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||_num_piece_);
								IF(i_ < 10)THEN
									_num_ref_ = _num_piece_ ||'-0'||i_;
								ELSE
									_num_ref_ = _num_piece_ ||'-'||i_;
								END IF;
								_compte_general_ = _compte_secondaire_;
								_compte_tiers_ = null;		
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
							ELSE
								_credit_ = 0;
								_debit_ = ligne_.ristourne;
								_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||_num_piece_);
								IF(i_ < 10)THEN
									_num_ref_ = _num_piece_ ||'-0'||i_;
								ELSE
									_num_ref_ = _num_piece_ ||'-'||i_;
								END IF;		
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
							END IF;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT SUM(tc.montant) AS total, tx.compte, tx.designation FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_vente tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id 
						INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_
						AND dv.id NOT IN (SELECT _f.facture FROM yvs_compta_content_journal_facture_vente _f INNER JOIN yvs_compta_pieces_comptable _p ON _f.piece = _p.id
								INNER JOIN yvs_com_doc_ventes _d ON _f.facture = _d.id INNER JOIN yvs_compta_journaux _j ON _p.journal = _j.id INNER JOIN yvs_agences _a ON _j.agence = _a.id 
								WHERE _a.societe = societe_ AND _d.entete_doc = element_)
						GROUP BY tx.compte, tx.designation 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = ligne_.total;
							_debit_ = 0;
							IF(i_ < 10)THEN
								_num_ref_ = _num_piece_ ||'-0'||i_;
							ELSE
								_num_ref_ = _num_piece_ ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.designation||' pour le journal de vente N° '||_num_piece_;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT SUM(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_vente co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_ 
						AND dv.id NOT IN (SELECT _f.facture FROM yvs_compta_content_journal_facture_vente _f INNER JOIN yvs_compta_pieces_comptable _p ON _f.piece = _p.id
								INNER JOIN yvs_com_doc_ventes _d ON _f.facture = _d.id INNER JOIN yvs_compta_journaux _j ON _p.journal = _j.id INNER JOIN yvs_agences _a ON _j.agence = _a.id 
								WHERE _a.societe = societe_ AND _d.entete_doc = element_)
						GROUP BY tx.id, tx.libelle 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(ligne_.augmentation)THEN
								_credit_ = ligne_.total;
								_numero_ = 2;
							ELSE
								_debit_ = ligne_.total;
								_numero_ = 1;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = _num_piece_ ||'-0'||i_;
							ELSE
								_num_ref_ = _num_piece_ ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.libelle||' pour le journal de vente N° '||_num_piece_;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'DOC_ACHAT' OR table_ = 'AVOIR_ACHAT')THEN
			SELECT INTO data_ d.num_doc, d.date_doc, d.fournisseur, CONCAT(c.nom, ' ', c.prenom) AS nom, c.compte, d.fournisseur as tiers, pc.saisie_compte_tiers, d.agence, COALESCE(d.description, '') AS description FROM yvs_com_doc_achats d LEFT JOIN yvs_base_fournisseur c ON d.fournisseur = c.id LEFT JOIN yvs_base_plan_comptable pc ON c.compte = pc.id WHERE d.id = element_;
			IF(data_.num_doc IS NOT NULL AND data_.num_doc NOT IN ('', ' '))THEN		
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;			
				total_ = (SELECT get_ttc_achat(element_)); -- RECUPERATION DU TTC DE LA FACTURE
				IF(total_ IS NOT NULL AND total_ > 0)THEN
					-- RECHERCHE DES ARTICLES SANS COMPTE POUR LA CATEGORIE DE LA FACTURE
					SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_achat c INNER JOIN yvs_com_doc_achats d ON c.doc_achat = d.id
						WHERE d.id = element_ AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) < 1;
					IF(COALESCE(count_, 0) < 1)THEN
						-- RECHERCHE DES ARTICLES AVEC PLUS D'UN COMPTE POUR LA CATEGORIE DE LA FACTURE
						SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_achat c INNER JOIN yvs_com_doc_achats d ON c.doc_achat = d.id
							WHERE d.id = element_ AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) > 1;
						IF(COALESCE(count_, 0) > 0)THEN
							_error_ = 'certains article sont rattaché a plusieurs comptes pour la catégorie de la facture';
						END IF;
					ELSE
						_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
					END IF;	
					-- COMPTABILISATION DU TTC DE LA FACTURE
					IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
						_error_ = 'cette facture d''achat n''est pas rattachée à un fournisseur';
					ELSIF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette facture d''achat est rattachée à un fournisseur qui n''a pas de compte tiers';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette facture d''achat est rattachée à un fournisseur qui n''a pas de compte collectif';
					END IF;
					_jour_ = to_char(data_.date_doc ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_doc;
					_compte_general_ = data_.compte;
					_libelle_ = data_.description;
					IF(data_.saisie_compte_tiers)THEN
						_compte_tiers_ = data_.tiers;
					ELSE
						_compte_tiers_ = null;
					END IF;
					_numero_ = 2;
					FOR ligne_ IN SELECT t.* FROM yvs_base_tranche_reglement t INNER JOIN yvs_base_model_reglement m ON t.model = m.id INNER JOIN yvs_com_doc_achats d ON d.model_reglement = m.id WHERE d.id = element_ ORDER BY t.numero
					LOOP
						_debit_ = 0;
						_credit_ = 0;
						IF(table_ = 'DOC_ACHAT')THEN
							_credit_ = (SELECT arrondi(societe_, (total_ * ligne_.taux / 100)));
							somme_ = somme_ + _credit_;
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||ligne_.taux||'% pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE	
							_debit_ = (SELECT arrondi(societe_, (total_ * ligne_.taux / 100)));
							somme_ = somme_ + _debit_;
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||ligne_.taux||'% pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_echeance_ = _echeance_ + ligne_.interval_jour;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;
					IF(total_ > somme_)THEN
						_debit_ = 0;
						_credit_ = 0;
						IF(somme_ > 0)THEN
							taux_ = (((total_ - somme_) / total_) * 100);
						END IF;
						IF(table_ = 'DOC_ACHAT')THEN
							_credit_ = (SELECT arrondi(societe_, (total_ - somme_)));
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||taux_||'% pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE	
							_debit_ = (SELECT arrondi(societe_, (total_ - somme_)));
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||taux_||'% pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;

					_error_ = null;
					_numero_ = 1;
					_echeance_ = data_.date_doc;
					-- COMPTABILISATION DES ARTICLES
					FOR ligne_ IN SELECT  SUM(co.prix_total) AS total, pc.id AS compte, pc.intitule, pc.saisie_compte_tiers, pc.saisie_analytique FROM yvs_com_contenu_doc_achat co INNER JOIN yvs_com_doc_achats dv ON co.doc_achat = dv.id
						INNER JOIN yvs_base_article_categorie_comptable ca ON ca.article = co.article AND ca.categorie = dv.categorie_comptable LEFT JOIN yvs_base_plan_comptable pc ON ca.compte = pc.id 
						WHERE dv.id = element_ GROUP BY pc.id
					LOOP
						_credit_ = 0;
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _debit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_achat tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_achat tc INNER JOIN yvs_com_contenu_doc_achat co ON tc.contenu = co.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE ca.compte = ligne_.compte AND dv.id = element_);
						IF(table_ = 'DOC_ACHAT')THEN
							_debit_ = ligne_.total - COALESCE(_debit_, 0);
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = ligne_.intitule||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE
							_credit_ = ligne_.total - COALESCE(_debit_, 0);
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = ligne_.intitule||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
							_debit_ = 0;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
						IF(table_ = 'DOC_ACHAT')THEN
							valeur_ = _debit_;
						ELSE
							valeur_ = _credit_;
						END IF;
						IF(ligne_.saisie_analytique IS TRUE AND (valeur_ IS NOT NULL AND valeur_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
							_contenu_ = _id_;
							RAISE NOTICE 'valeur_ : %',valeur_;
							RAISE NOTICE '_compte_general_ : %',_compte_general_;
							FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, 0, 0, _compte_general_, null, valeur_, '', table_, FALSE, FALSE)
							LOOP
								_error_ = sous_.error;
								_centre_ = sous_.centre;
								_coefficient_ = sous_.coefficient;
								IF(table_ = 'DOC_ACHAT')THEN
									_debit_ = sous_.valeur;
								ELSE
									_credit_ = sous_.valeur;
								END IF;
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
							END LOOP;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_achat tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_achat co ON tc.contenu = co.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
						WHERE dv.id = element_ GROUP BY tx.compte, tx.designation 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_ACHAT')THEN
								_debit_ = ligne_.total;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.designation||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							ELSE
								_credit_ = ligne_.total;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.designation||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_achat co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
						WHERE dv.id = element_ GROUP BY tx.id, tx.libelle 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_ACHAT')THEN
								IF(ligne_.augmentation IS TRUE)THEN
									_debit_ = ligne_.total;
									_numero_ = 1;
								ELSE
									_credit_ = ligne_.total;
									_numero_ = 2;
								END IF;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.libelle||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							ELSE
								IF(ligne_.augmentation IS TRUE)THEN
									_credit_ = ligne_.total;
									_numero_ = 2;
								ELSE
									_debit_ = ligne_.total;
									_numero_ = 1;
								END IF;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.libelle||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'FRAIS_MISSION')THEN
			SELECT INTO data_ f.id, f.numero_piece, f.date_paiement, f.montant, c.journal, c.compte, o.compte_charge, m.ordre, m.objet_mission, m.numero_mission, e.agence, p.saisie_analytique, f.caisse FROM yvs_compta_caisse_piece_mission f LEFT JOIN yvs_grh_missions m ON f.mission = m.id 
				LEFT JOIN yvs_grh_employes e ON m.employe = e.id LEFT JOIN yvs_grh_objets_mission o ON m.objet_mission = o.id LEFT JOIN yvs_base_plan_comptable p ON o.compte_charge = p.id LEFT JOIN yvs_base_caisse c ON f.caisse = c.id WHERE f.id = element_;	
			_table_tiers_ = 'EMPLOYE';	
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero_mission;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.ordre;
				_compte_tiers_ = null;
				
				IF(data_.objet_mission IS NULL OR data_.objet_mission < 1)THEN
					_error_ = 'ce reglement est lié à une mission qui n''a pas d''objet de mission';
				ELSIF(data_.compte_charge IS NULL OR data_.compte_charge < 1)THEN
					_error_ = 'ce reglement est lié à une mission dont l''objet de mission n''a pas de compte de charge';
				END IF;
				_credit_ = 0;
				_debit_ = data_.montant;
				_numero_ = 1;
				_compte_general_ = data_.compte_charge;	
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
				valeur_ = _debit_;
				IF(data_.saisie_analytique IS TRUE AND (valeur_ IS NOT NULL AND valeur_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
					_contenu_ = _id_;
					FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, 0, 0, _compte_general_, null, valeur_, '', table_, FALSE, FALSE)
					LOOP
						_error_ = sous_.error;
						_centre_ = sous_.centre;
						_coefficient_ = sous_.coefficient;
						_debit_ = sous_.valeur;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
					END LOOP;
				END IF;

				_error_ = null;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est pas lié à une caisse';
				ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'ce reglement est lié à une caisse qui n''a pas de compte';
				END IF;
				_credit_ = data_.montant;
				_numero_ = 2;
				_debit_ = 0;
				_compte_general_ = data_.compte;	
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, y.caisse, p.vente, d.num_doc, d.client, t.compte as compte_tiers, t.suivi_comptable, d.tiers, m.compte_tiers AS tiers_interne, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, u.agence,
				r.libelle, r.mode_reglement, r.code_comptable, c.intitule, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.numero_piece) AS reference_externe FROM yvs_compta_phase_piece y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_vente p ON y.piece_vente = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_doc_ventes d ON p.vente = d.id LEFT JOIN yvs_com_client t ON d.client = t.id LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
				LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_grh_employes m ON u.id = m.code_users WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.vente IS NULL OR data_.vente < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune facture';
				ELSE
					_libelle_ = _libelle_|| ' du client '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece p ON p.phase_reg = r.id WHERE p.piece_vente = data_.id;
				RAISE NOTICE '%',data_.numero_phase;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSE
					IF(data_.numero_phase = ligne_.max)THEN
						IF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;					
					ELSE
						IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
							_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
						ELSE
							_compte_general_ = data_.compte_caisse;
						END IF;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.vente IS NOT NULL AND data_.vente > 0)THEN
						IF(data_.client IS NULL OR data_.client < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de client';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_vente v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_vente p ON y.piece_vente = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece _y INNER JOIN yvs_compta_caisse_piece_vente _p ON _y.piece_vente = _p.id INNER JOIN yvs_compta_phase_piece _y0 ON _y0.piece_vente = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.suivi_comptable)THEN
						IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
							_error_ = 'car ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte tiers';
						ELSE
							_compte_tiers_ = data_.tiers;
						END IF;
					ELSE
						IF(data_.tiers_interne IS NULL OR data_.tiers_interne < 1)THEN
							_error_ = 'car ce reglement est rattaché à une facture qui est liée à un vendeur qui n''a pas de compte tiers';
						ELSE
							_compte_tiers_ = data_.tiers_interne;
						END IF;
					END IF;		
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, y.caisse, p.achat, d.num_doc, d.fournisseur, t.compte as compte_tiers, d.fournisseur as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, d.agence,
				r.libelle, r.mode_reglement, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.numero_piece) AS reference_externe FROM yvs_compta_phase_piece_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_doc_achats d ON p.achat = d.id LEFT JOIN yvs_base_fournisseur t ON d.fournisseur = t.id WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.achat IS NULL OR data_.achat < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune facture';
				ELSE
					_libelle_ = _libelle_||' du fournisseur '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece_achat p ON p.phase_reg = r.id WHERE p.piece_achat = data_.id;
				RAISE NOTICE 'data_.numero_phase %',data_.numero_phase;
				RAISE NOTICE 'ligne_.max %',ligne_.max;
				RAISE NOTICE 'ligne_.min %',ligne_.min;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSE
					IF(data_.numero_phase = ligne_.min)THEN
						IF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;					
					ELSE
						IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
							_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
						ELSE
							_compte_general_ = data_.compte_caisse;
						END IF;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 2;
					_credit_ = data_.montant;	
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.achat IS NOT NULL AND data_.achat > 0)THEN
						IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de fournisseur';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_achat v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece_achat _y INNER JOIN yvs_compta_caisse_piece_achat _p ON _y.piece_achat = _p.id INNER JOIN yvs_compta_phase_piece_achat _y0 ON _y0.piece_achat = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'car ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;	
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE			
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN
			SELECT INTO data_ p.id, p.num_piece as numero_piece, p.date_valider as date_paiement, p.montant, y.caisse, p.doc_divers, d.num_piece as num_doc, d.id_tiers as tiers, name_tiers(d.id_tiers, d.table_tiers, 'C')::integer as compte_tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, d.table_tiers, d.agence,
				r.libelle, r.mode_reglement, y.statut, d.mouvement, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, name_tiers(d.id_tiers, d.table_tiers, 'N') AS nom_prenom,
				COALESCE(p.reference_externe, p.num_piece) AS reference_externe FROM yvs_compta_phase_piece_divers y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_divers p ON y.piece_divers = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_compta_caisse_doc_divers d ON p.doc_divers = d.id WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = data_.table_tiers;		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.doc_divers IS NULL OR data_.doc_divers < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune operation diverse';
				ELSIF(data_.nom_prenom NOT IN ('', ' '))THEN
					_libelle_ = _libelle_||' du tiers '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min, 0 as tmp FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece_divers p ON p.phase_reg = r.id WHERE p.piece_divers = data_.id;
				IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
					ligne_.tmp = ligne_.max;
					ligne_.max = ligne_.min;
					ligne_.min = ligne_.tmp;
				END IF;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune caisse';
					ELSIF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 2;
						_credit_ = data_.montant;
					ELSE
						_numero_ = 1;
						_debit_ = data_.montant;
					END IF;
				ELSE		
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 1;
						_debit_ = data_.montant;
					ELSE
						_numero_ = 2;
						_credit_ = data_.montant;
					END IF;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.doc_divers IS NOT NULL AND data_.doc_divers > 0)THEN
						IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
							_error_ = 'ce reglement est rattaché à une operation diverse d''un tiers qui n''a pas de compte collectif';
						ELSE
							_compte_general_ = data_.compte_tiers;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece_divers y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_divers v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_divers p ON y.piece_divers = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece_divers _y INNER JOIN yvs_compta_caisse_piece_divers _p ON _y.piece_divers = _p.id INNER JOIN yvs_compta_phase_piece_divers _y0 ON _y0.piece_divers = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'ce reglement est rattaché à une operation diverse qui n''a pas de tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;	
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 1;
						_debit_ = data_.montant;
					ELSE
						_numero_ = 2;
						_credit_ = data_.montant;
					END IF;
				ELSE		
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 2;
						_credit_ = data_.montant;
					ELSE
						_numero_ = 1;
						_debit_ = data_.montant;
					END IF;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN
			SELECT INTO data_ p.id, p.num_refrence, p.date_paiement, p.montant, y.caisse, t.compte as compte_tiers, p.fournisseur as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, y.piece_achat, m.societe, j.agence, p.nature, 
				r.libelle, r.mode_reglement, r.code_comptable, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.num_refrence) AS reference_externe FROM yvs_compta_phase_acompte_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_acompte_fournisseur p ON y.piece_achat = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_base_fournisseur t ON p.fournisseur = t.id LEFT JOIN yvs_base_mode_reglement m ON p.model = m.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE y.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_refrence;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.piece_achat IS NULL OR data_.piece_achat < 1)THEN
					_error_ = 'cette phase n''est rattachée a aucun acompte';
				ELSE
					_libelle_ = _libelle_||' du fournisseur '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_acompte_achat p ON p.phase_reg = r.id WHERE p.piece_achat = data_.id;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE			
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.nature = 'A')THEN
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des acomptes fournisseur n''est pas paramètré';
						END IF;
					ELSE
						IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
							_error_ = 'le fournisseur n''a pas de compte collectif';
						END IF;
						_compte_general_ = data_.compte_tiers;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_acompte_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_acompte_achat v ON v.etape = y.id INNER JOIN yvs_compta_acompte_fournisseur p ON y.piece_achat = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_acompte_achat _y INNER JOIN yvs_compta_acompte_fournisseur _p ON _y.piece_achat = _p.id INNER JOIN yvs_compta_phase_acompte_achat _y0 ON _y0.piece_achat = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;			
				END IF;	
				IF(data_.reglement_ok)THEN
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette phase est liée a un acompte fournisseur qui n'' pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;			
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN
			SELECT INTO data_ p.id, p.num_refrence, p.date_paiement, p.montant, y.caisse, t.compte as compte_tiers, p.client as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, y.piece_vente, m.societe, j.agence, p.nature,
				r.libelle, r.mode_reglement, r.code_comptable, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.num_refrence) AS reference_externe FROM yvs_compta_phase_acompte_vente y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_acompte_client p ON y.piece_vente = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_client t ON p.client = t.id LEFT JOIN yvs_base_mode_reglement m ON p.model = m.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE y.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_refrence;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.piece_vente IS NULL OR data_.piece_vente < 1)THEN
					_error_ = 'cette phase n''est rattachée a aucun acompte';
				ELSE
					_libelle_ = _libelle_||' du client '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_acompte_vente p ON p.phase_reg = r.id WHERE p.piece_vente = data_.id;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.nature = 'A')THEN
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des acomptes client n''est pas paramètré';
						END IF;
					ELSE
						IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
							_error_ = 'le client n''a pas de compte collectif';
						END IF;
						_compte_general_ = data_.compte_tiers;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_acompte_vente y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_acompte_vente v ON v.etape = y.id INNER JOIN yvs_compta_acompte_client p ON y.piece_vente = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_acompte_vente _y INNER JOIN yvs_compta_acompte_client _p ON _y.piece_vente = _p.id INNER JOIN yvs_compta_phase_acompte_vente _y0 ON _y0.piece_vente = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;			
				END IF;	
				IF(data_.reglement_ok)THEN
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette phase est liée a un acompte client qui n'' pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;			
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_VENTE')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, p.caisse, p.vente, d.num_doc, d.client, t.compte as compte_tiers, t.suivi_comptable, d.tiers, m.compte_tiers AS tiers_interne, c.compte, mo.type_reglement, mo.societe, u.agence
				FROM yvs_compta_caisse_piece_vente p LEFT JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_base_mode_reglement mo ON p.model = mo.id
				LEFT JOIN yvs_com_doc_ventes d ON p.vente = d.id LEFT JOIN yvs_com_client t ON d.client = t.id LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
				LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_grh_employes m ON u.id = m.code_users WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece p WHERE p.piece_vente = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_vente = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_VENTE', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_paiement ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_paiement;
					_libelle_ = 'Reglement Facture vente N° ';		
					_compte_tiers_ = null;	
					IF(data_.vente IS NULL OR data_.vente < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune facture';
					ELSE
						_libelle_ = _libelle_||data_.num_doc;
					END IF;
					IF(data_.type_reglement = 'COMPENSATION')THEN					
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'COMPENSATION';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des attentes pour compensation n''est pas paramètré';
						END IF;
					ELSE				
						SELECT INTO ligne_ COALESCE(a.nature, 'A') AS nature, CHAR_LENGTH(COALESCE(a.nature, 'A'))::bigint as count FROM yvs_compta_notif_reglement_vente n INNER JOIN yvs_compta_acompte_client a ON n.acompte = a.id WHERE n.piece_vente = element_ LIMIT 1;
						IF(ligne_.count IS NOT NULL AND ligne_.count > 0)THEN	
							IF(ligne_.nature = 'A')THEN
								SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
								IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
									_error_ = 'Le compte des acomptes client n''est paramètré';
								END IF;	
							ELSE
								IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
									_error_ = 'ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte général';
								ELSE
									_compte_general_ = data_.compte_tiers;
								END IF;
							END IF;
							_libelle_ = _libelle_|| ' par acompte';	
						ELSE
							IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
								_error_ = 'ce reglement n''est associé à aucune caisse';
							ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
								_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte;
							END IF;
						END IF;
					END IF;
					_numero_ = 1;			
					_credit_ = 0;
					_debit_ = data_.montant;	
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
						
					_error_ = '';
					IF(data_.vente IS NOT NULL AND data_.vente > 0)THEN
						IF(data_.client IS NULL OR data_.client < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de client';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
							IF(COALESCE(ligne_.count, 0) < 1)THEN
								IF(data_.suivi_comptable)THEN
									IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
										_error_ = 'car ce reglement est rattaché à une facture qui n''a pas de compte tiers';
									ELSE
										_compte_tiers_ = data_.tiers;
									END IF;
								ELSE
									IF(data_.tiers_interne IS NULL OR data_.tiers_interne < 1)THEN
										_error_ = 'car ce reglement est rattaché à une facture qui est liée à un vendeur qui n''a pas de compte tiers';
									ELSE
										_compte_tiers_ = data_.tiers_interne;
									END IF;
								END IF;
							END IF;
						END IF;
					ELSE
						_error_ = 'ce reglement n''est associé à aucune facture';
					END IF;
					_ref_externe_ = data_.id;
					_table_externe_ = 'CAISSE_VENTE';
					_numero_ = 2;
					_credit_ = data_.montant;
					_debit_ = 0;	
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CAISSE_ACHAT')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, p.caisse, p.achat, d.num_doc, d.fournisseur, t.compte AS compte_tiers, d.fournisseur as tiers, c.compte, mo.type_reglement, mo.societe, d.agence FROM yvs_compta_caisse_piece_achat p 
				LEFT JOIN yvs_base_caisse c ON p.caisse = c.id  LEFT JOIN yvs_base_mode_reglement mo ON p.model = mo.id
				LEFT JOIN yvs_com_doc_achats d ON p.achat = d.id LEFT JOIN yvs_base_fournisseur t ON d.fournisseur = t.id WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece_achat p WHERE p.piece_achat = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece_achat p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_achat = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_ACHAT', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_paiement ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_paiement;
					_libelle_ = 'Reglement Facture achat N° ';
					
					IF(data_.achat IS NULL OR data_.achat < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune facture';
					ELSE
						SELECT INTO ligne_ COALESCE(a.nature, 'A') AS nature, CHAR_LENGTH(COALESCE(a.nature, 'A'))::bigint as count FROM yvs_compta_notif_reglement_achat n INNER JOIN yvs_compta_acompte_fournisseur a ON n.acompte = a.id WHERE n.piece_achat = element_ LIMIT 1;
						_libelle_ = _libelle_||data_.num_doc;
						IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de fournisseur';
						ELSE
							
							IF(COALESCE(ligne_.count, 0) < 1)THEN
								IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
									_error_ = 'car ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte tiers';
								ELSE
									_compte_tiers_ = data_.tiers;
								END IF;
							END IF;
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
					_numero_ = 1;
					_credit_ = 0;
					_debit_ = data_.montant;
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
					
					_error_ = '';			
					_compte_tiers_ = null;	
					IF(data_.type_reglement = 'COMPENSATION')THEN					
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'COMPENSATION';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des attentes pour compensation n''est pas paramètré';
						END IF;
					ELSE		
						IF(ligne_.count IS NOT NULL AND ligne_.count > 0)THEN	
							IF(ligne_.nature = 'A')THEN
								SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
								IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
									_error_ = 'Le compte des acomptes fournisseur n''est paramètré';
								END IF;		
							ELSE
								IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
									_error_ = 'ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte général';
								ELSE
									_compte_general_ = data_.compte_tiers;
								END IF;
							END IF;
						ELSE
							IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
								_error_ = 'ce reglement n''est associé à aucune caisse';
							ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
								_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
							ELSE	
								_compte_general_ = data_.compte;
							END IF;
						END IF;
					END IF;
					_numero_ = 2;			
					_credit_ = data_.montant;
					_debit_ = 0;		
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CAISSE_DIVERS')THEN
			SELECT INTO data_ p.id, p.num_piece, p.date_piece, p.montant, p.caisse, p.doc_divers, d.num_piece as num_doc, CONCAT(d.type_doc, '') AS type_doc, CONCAT(d.mouvement, '') AS mouvement, d.id_tiers as tiers, d.compte_general, name_tiers(d.id_tiers, d.table_tiers, 'C')::integer as compte_collectif, 
				c.compte, mo.type_reglement, COALESCE(d.libelle_comptable, d.num_piece) AS description, d.table_tiers, d.agence 
				FROM yvs_compta_caisse_piece_divers p LEFT JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_base_mode_reglement mo ON p.mode_paiement = mo.id 
				LEFT JOIN yvs_compta_caisse_doc_divers d ON p.doc_divers = d.id WHERE p.id = element_;
			IF(data_.num_piece IS NOT NULL AND data_.num_piece NOT IN ('', ' '))THEN		
				_agence_ = data_.agence;
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece_divers p WHERE p.piece_divers = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece_divers p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_divers = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_DIVERS', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					SELECT INTO ligne_ COUNT(p.id) FROM yvs_compta_caisse_piece_cout_divers p WHERE p.piece = data_.id;
					IF(COALESCE(ligne_.count, 0) > 0)THEN
						_error_ = 'ce reglement est rattaché à un cout';
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					ELSE
						_table_tiers_ = data_.table_tiers;	
						_jour_ = to_char(data_.date_piece ,'dd')::integer;
						_num_piece_ = data_.num_doc;	
						_echeance_ = data_.date_piece;
						_libelle_ = 'Reglement N° '||data_.num_piece||' sur ';	
						
						IF(data_.doc_divers IS NULL OR data_.doc_divers < 1)THEN
							_error_ = 'ce reglement n''est associé à aucun document';
						ELSE
							_libelle_ = _libelle_||data_.description;
						END IF;
						
						_error_ = '';
						_compte_general_ = null;
						_compte_tiers_ = null;
						_debit_ = 0;
						_credit_ = 0;		
						IF(data_.mouvement = 'D')THEN
							_debit_ = data_.montant;
							_numero_ = 1;
						ELSE	
							_credit_ = data_.montant;	
							_numero_ = 2;			
						END IF;
						IF(data_.tiers IS NULL OR data_.tiers < 1)THEN							
							_error_ = 'devez comptabiliser le documents en question';
						ELSE
							_compte_tiers_ = data_.tiers;
							IF(data_.compte_collectif IS NULL OR data_.compte_collectif < 1)THEN							
								_error_ = 'car ce reglement est rattaché à un document d''un tiers qui n''a pas de compte collectif';
							ELSE
								_compte_general_ = data_.compte_collectif;
							END IF;	
						END IF;	
						_num_ref_ = data_.num_piece;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
						
						_error_ = '';
						_compte_general_ = null;
						_compte_tiers_ = null;
						_credit_ = 0;
						_debit_ = 0;
						IF(data_.mouvement = 'D')THEN
							_credit_ = data_.montant;
							_numero_ = 2;
						ELSE
							_debit_ = data_.montant;
							_numero_ = 1;		
						END IF;	
						IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
							_error_ = 'ce reglement n''est associé à aucune caisse';
						ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;	
						_num_ref_ = data_.num_piece;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
				END IF;
			END IF;
		ELSIF(table_ = 'ACOMPTE_VENTE')THEN
			SELECT INTO data_ a.id, a.date_acompte, a.num_refrence, a.montant, m.societe, c.compte, f.compte AS compte_collectif, a.client as tiers, m.type_reglement, j.agence, a.nature FROM yvs_compta_acompte_client a  LEFT JOIN yvs_base_caisse c ON a.caisse = c.id
				INNER JOIN yvs_base_mode_reglement m ON a.model = m.id LEFT JOIN yvs_com_client f ON a.client = f.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE a.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_acompte_vente p WHERE p.piece_vente = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_acompte_vente p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_vente = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_ACOMPTE_VENTE', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_acompte ,'dd')::integer;
					_num_piece_ = data_.num_refrence;	
					_echeance_ = data_.date_acompte;
					_libelle_ = 'Acompte N° '||data_.num_refrence;	
					IF(data_.nature = 'A')THEN			
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'le compte des acomptes client n''est pas paramètré';
						END IF;
					ELSE
						IF(data_.compte_collectif IS NULL OR data_.compte_collectif < 1)THEN
							_error_ = 'le client n''a pas de compte collectif';
						END IF;
						_compte_general_ = data_.compte_collectif;
					END IF;
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cet acompte est rattaché à un fournisseur qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;				
					END IF;				
					_credit_ = data_.montant;
					_debit_ = 0;	
					_numero_ = 2;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;

					_error_ = null;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cet acompte est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;
					_credit_ = 0;
					_debit_ = data_.montant;	
					_numero_ = 1;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'ACOMPTE_ACHAT')THEN
			SELECT INTO data_ a.id, a.date_acompte, a.num_refrence, a.montant, m.societe, c.compte, f.compte AS compte_collectif, a.fournisseur as tiers, m.type_reglement, j.agence, a.nature FROM yvs_compta_acompte_fournisseur a LEFT JOIN yvs_base_caisse c ON a.caisse = c.id 
				INNER JOIN yvs_base_mode_reglement m ON a.model = m.id INNER JOIN yvs_base_fournisseur f ON a.fournisseur = f.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE a.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_acompte_achat p WHERE p.piece_achat = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_acompte_achat p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_achat = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_ACOMPTE_ACHAT', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_acompte ,'dd')::integer;
					_num_piece_ = data_.num_refrence;	
					_echeance_ = data_.date_acompte;
					_libelle_ = 'Acompte N° '||data_.num_refrence;	
					IF(data_.nature = 'A')THEN				
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des acomptes fournisseur n''est pas paramètré';
						END IF;
					ELSE
						IF(data_.compte_collectif IS NULL OR data_.compte_collectif < 1)THEN
							_error_ = 'le fournisseur n''a pas de compte collectif';
						END IF;
						_compte_general_ = data_.compte_collectif;
					END IF;
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cet acompte est rattaché à un fournisseur qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;				
					END IF;			
					_credit_ = 0;
					_debit_ = data_.montant;	
					_numero_ = 1;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;

					_error_ = null;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cet acompte est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;
					_credit_ = data_.montant;
					_debit_ = 0;	
					_numero_ = 2;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CREDIT_VENTE')THEN
			SELECT INTO data_ a.date_credit, a.num_reference, a.montant, m.societe, c.compte, a.client as tiers, t.compte as compte_general, j.agence FROM yvs_compta_credit_client a 
				INNER JOIN yvs_com_client c ON a.client = c.id LEFT JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				LEFT JOIN yvs_grh_type_cout t ON a.type_credit = t.id WHERE a.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_credit ,'dd')::integer;
				_num_piece_ = data_.num_reference;	
				_echeance_ = data_.date_credit;
				_libelle_ = 'Credit N° '||data_.num_reference;				
				IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
					_error_ = 'Le credit est rattaché a un type qui n''a pas de compte';
				ELSE
					_compte_general_ = data_.compte_general;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				_compte_tiers_ = data_.tiers;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le client n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CREDIT_ACHAT')THEN
			SELECT INTO data_ a.date_credit, a.num_reference, a.montant, m.societe, c.compte, a.fournisseur as tiers, t.compte as compte_general, j.agence FROM yvs_compta_credit_fournisseur a 
				INNER JOIN yvs_base_fournisseur c ON a.fournisseur = c.id INNER JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				LEFT JOIN yvs_grh_type_cout t ON a.type_credit = t.id WHERE a.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_credit ,'dd')::integer;
				_num_piece_ = data_.num_reference;	
				_echeance_ = data_.date_credit;
				_libelle_ = 'Credit N° '||data_.num_reference;					
				IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
					_error_ = 'Le credit est rattaché a un type qui n''a pas de compte';
				ELSE
					_compte_general_ = data_.compte_general;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				_compte_tiers_ = data_.tiers;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le fournisseur n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_CREDIT_VENTE')THEN
			SELECT INTO data_ r.date_paiement, r.numero, a.num_reference, r.valeur as montant, m.societe, c.compte, a.client as tiers, t.compte as compte_general, j.agence ,r.caisse
				FROM yvs_compta_reglement_credit_client r LEFT JOIN yvs_compta_credit_client a ON r.credit = a.id LEFT JOIN yvs_base_caisse t ON r.caisse = t.id
				LEFT JOIN yvs_com_client c ON a.client = c.id INNER JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				WHERE r.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero;	
				_echeance_ = data_.date_paiement;
				_libelle_ = 'REGLEMENT N°'||data_.numero||' DU Credit N° '||data_.num_reference;					
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'Le reglement n''est pas rattaché à une caisse';
				ELSE
					IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
						_error_ = 'Le reglement est rattaché a une caisse qui n''a pas de compte';
					ELSE
						_compte_general_ = data_.compte_general;
					END IF;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 2;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le client n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_compte_tiers_ = data_.tiers;	
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_CREDIT_ACHAT')THEN
			SELECT INTO data_ r.date_paiement, r.numero, a.num_reference, r.valeur as montant, m.societe, c.compte, a.fournisseur as tiers, t.compte as compte_general, j.agence ,r.caisse
				FROM yvs_compta_reglement_credit_fournisseur r LEFT JOIN yvs_compta_credit_fournisseur a ON r.credit = a.id LEFT JOIN yvs_base_caisse t ON r.caisse = t.id
				LEFT JOIN yvs_base_fournisseur c ON a.fournisseur = c.id INNER JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				WHERE r.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero;	
				_echeance_ = data_.date_paiement;
				_libelle_ = 'REGLEMENT N°'||data_.numero||' DU Credit N° '||data_.num_reference;					
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'Le reglement n''est pas rattaché à une caisse';
				ELSE
					IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
						_error_ = 'Le reglement est rattaché a une caisse qui n''a pas de compte';
					ELSE
						_compte_general_ = data_.compte_general;
					END IF;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le fournisseur n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_compte_tiers_ = data_.tiers;	
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'DOC_VIREMENT')THEN
			SELECT INTO data_ p.numero_piece, p.date_paiement, p.montant, p.cible, p.source, COALESCE(p.note, 'Virement N° '||p.numero_piece) AS note, c.compte AS compte_cible, s.compte AS compte_source, (SELECT l.compte FROM yvs_base_liaison_caisse l WHERE l.caisse_cible = p.cible AND l.caisse_source = p.source LIMIT 1) AS compte_interne, j.agence
				FROM yvs_compta_caisse_piece_virement p LEFT JOIN yvs_base_caisse s ON p.source = s.id LEFT JOIN yvs_base_caisse c ON p.cible = c.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'EMPLOYE';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero_piece;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.note;

				_error_ = '';
				_debit_ = 0;
				_compte_general_ = null;
				IF(data_.source IS NULL OR data_.source < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune emetteur';
				ELSIF(data_.compte_source IS NULL OR data_.compte_source < 1)THEN
					_error_ = 'ce reglement est rattaché à un emetteur qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte_source;
				END IF;
				_credit_ = data_.montant;
				_numero_ = 2;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				_credit_ = 0;
				IF(data_.compte_interne IS NULL OR data_.compte_interne < 1)THEN
					_error_ = 'ces comptes n''ont pas de compte de liaison';
				ELSE
					_compte_general_ = data_.compte_interne;
				END IF;
				_debit_ = data_.montant;
				_numero_ = 2;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				
				_debit_ = 0;
				_credit_ = data_.montant;
				_numero_ = 1;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				_credit_ = 0;
				IF(data_.cible IS NULL OR data_.cible < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune recepteur';
				ELSIF(data_.compte_cible IS NULL OR data_.compte_cible < 1)THEN
					_error_ = 'ce reglement est rattaché à un recepteur qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte_cible;
				END IF;
				_debit_ = data_.montant;
				_numero_ = 1;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_numero_ = 2;
				FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_compta_cout_sup_piece_virement co ON co.type_cout = tx.id INNER JOIN yvs_compta_caisse_piece_virement dv ON dv.id = co.virement
					WHERE dv.id = element_ GROUP BY tx.id, tx.libelle 
				LOOP
					_compte_tiers_ = null;	
					IF(ligne_.total != 0)THEN
						_libelle_ = ligne_.libelle||' pour le virement N° '||data_.numero_piece;
						
						_credit_ = 0;
						_debit_ = ligne_.total;
						IF(i_ < 10)THEN
							_num_ref_ = data_.numero_piece ||'-0'||i_;
						ELSE
							_num_ref_ = data_.numero_piece ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
						END IF;
						_compte_general_ = ligne_.compte;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;

						
						_credit_ = ligne_.total;
						_debit_ = 0;
						IF(i_ < 10)THEN
							_num_ref_ = data_.numero_piece ||'-0'||i_;
						ELSE
							_num_ref_ = data_.numero_piece ||'-'||i_;
						END IF;
						IF(data_.source IS NULL OR data_.source < 1)THEN
							_error_ = 'ce reglement n''est associé à aucune emetteur';
						ELSIF(data_.compte_source IS NULL OR data_.compte_source < 1)THEN
							_error_ = 'ce reglement est rattaché à un emetteur qui n''a pas de compte général';
						END IF;
						_compte_general_ = data_.compte_source;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
				END LOOP;
			END IF;
		ELSIF(table_ = 'ORDRE_SALAIRE')THEN
			SELECT INTO data_ o.* FROM yvs_grh_ordre_calcul_salaire o WHERE o.id = element_;
			IF(data_.reference IS NOT NULL AND data_.reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'TIERS';	
				_jour_ = to_char(data_.date_execution ,'dd')::integer;
				_num_piece_ = data_.reference;	
				_echeance_ = data_.date_execution;
				-- Recupere gains
				-- récupère les éléments de gains en groupant par compte charge du gains
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, COALESCE(SUM(db.montant_payer),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition FROM yvs_grh_detail_bulletin db 
					LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat 
					LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id 
					WHERE el.visible_bulletin IS TRUE AND NOT (COALESCE(db.montant_payer,0)=0) AND el.actif IS TRUE AND el.retenue IS FALSE AND b.entete = element_ 
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, co.id ORDER BY ag.designation, el.nom
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;
					
					_error_ = '';
					_credit_ = 0;
					_numero_ = 1;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.id IS NULL OR ligne_.id < 1)THEN
						_error_ = 'certains bulletins ne sont pas rattachés aux elements de salaire';
					ELSIF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'le gain '||ligne_.nom||' n''est rattaché à aucun compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
-- 					RAISE NOTICE '- %',_compte_general_;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- récupère du net a payer en groupant par compte collectif employé
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, COALESCE(co.intitule, '') AS intitule, e.compte_collectif, (COALESCE(SUM(db.montant_payer),0) - COALESCE(SUM(db.retenu_salariale),0)) AS montant, co.saisie_analytique 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON e.compte_collectif = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.montant_payer,0)!=0 OR COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND b.entete = element_
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, co.id, e.compte_collectif ORDER BY ag.designation, e.compte_collectif, co.intitule
				LOOP
					_libelle_ = ligne_.intitule ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 1;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_collectif IS NULL OR ligne_.compte_collectif < 1)THEN
						_error_ = 'certains employes n''ont pas de compte collectif';
					ELSE
						_compte_general_ = ligne_.compte_collectif;
					END IF;
					_credit_ = ligne_.montant;
					IF(ligne_.montant = 177871.63)THEN
						RAISE NOTICE '_libelle_ : %', _libelle_;
						RAISE NOTICE '_compte_general_ : %', _compte_general_;
					END IF;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, 0, _compte_general_, _compte_tiers_, _credit_, '', table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- Recupere retenues
				-- récupère les retenues patronales
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, el.compte_cotisation, COALESCE(SUM(db.montant_employeur),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.montant_employeur,0)!=0) AND el.actif IS TRUE AND el.retenue IS TRUE AND b.entete = element_
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, el.compte_charge, el.compte_cotisation, co.id ORDER BY ag.designation , el.compte_charge
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 5;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_cotisation IS NULL OR ligne_.compte_cotisation < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de cotisation';
					ELSE
						_compte_general_ = ligne_.compte_cotisation;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;	

					_error_ = '';
					_credit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- récupère les retenues salariales sans saisie tiers en groupant par compte de cotisation des retenues
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_cotisation, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON el.compte_cotisation = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND (COALESCE(el.saisi_compte_tiers, false) IS FALSE) 
					AND el.retenue IS TRUE AND b.entete = element_ AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, el.compte_cotisation, co.id ORDER BY ag.designation , el.nom
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 2;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_cotisation IS NULL OR ligne_.compte_cotisation < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de cotisation';
					ELSE
						_compte_general_ = ligne_.compte_cotisation;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;	
				-- récupère les retenues salariales avec saisie tiers en groupant par compte de charge des retenues
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition, t.id AS tiers, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom
                                        FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
                                        LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
                                        LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id LEFT JOIN yvs_base_tiers t ON e.compte_tiers = t.id 
                                        WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND COALESCE(el.saisi_compte_tiers, false) IS TRUE 
                                        AND el.retenue IS TRUE AND b.entete = element_ AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val))))
                                        GROUP BY ag.id, el.id, el.compte_charge, co.id, t.id ORDER BY ag.designation, el.nom
				LOOP
					_libelle_ = ligne_.nom ||' liée '|| ligne_.nom_prenom;
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 3;
					_compte_general_ = null;
					IF(ligne_.tiers IS NULL OR ligne_.tiers < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' est rattachée à des employés qui n''ont pas de compte tiers';
					ELSE
						_compte_tiers_ = ligne_.tiers;
					END IF;
					IF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;	
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;	
			END IF;
		END IF;
	END IF;
	-- DETERMINATION DE L'ARRONDI
	SELECT INTO _debit_ SUM(_debit) FROM table_compta_content_journal;
	SELECT INTO _credit_ SUM(_credit) FROM table_compta_content_journal;
	somme_ = COALESCE(_debit_, 0) - COALESCE(_credit_, 0);
	IF(somme_ != 0 AND abs(somme_) <= valeur_limite_arrondi_)THEN
		_compte_tiers_ = null;
		IF(somme_ > 0)THEN
			SELECT INTO data_ * FROM table_compta_content_journal WHERE _credit > 0 ORDER BY _numero LIMIT 1;
			_libelle_ = 'arrondis gain';
			titre_ = 'ARRONDI_GAIN';
			IF(table_ = 'DOC_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'JOURNAL_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'DOC_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _credit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'FRAIS_MISSION')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _credit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CREDIT_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'CREDIT_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'DOC_VIREMENT')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'ORDRE_SALAIRE')THEN	
				_table_tiers_ = 'EMPLOYE';	
			END IF;				
			SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = titre_;
			IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
				_error_ = 'Le compte des '||_libelle_||' n''est pas paramètré';
			END IF;
			_credit_ = somme_;
			_debit_ = 0;
		ELSE
			SELECT INTO data_ * FROM table_compta_content_journal WHERE _debit > 0 ORDER BY _numero LIMIT 1;
			_libelle_ = 'arrondis perte';
			titre_ = 'ARRONDI_PERTE';
			IF(table_ = 'DOC_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _debit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'JOURNAL_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'DOC_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'FRAIS_MISSION')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _debit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CREDIT_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'CREDIT_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'DOC_VIREMENT')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'ORDRE_SALAIRE')THEN	
				_table_tiers_ = 'EMPLOYE';	
			END IF;
			_libelle_ = 'Arrondi Perte';				
			SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = titre_;
			IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
				_error_ = 'Le compte des '||_libelle_||' n''est pas paramètré';
			END IF;
			_credit_ = 0;
			_debit_ = -somme_;
		END IF;	
		_jour_ = data_._jour;
		_num_piece_ = data_._num_piece;
		_echeance_ = data_._echeance;
		_numero_ = data_._numero;	
		IF(i_ < 10)THEN
			_num_ref_ = _num_piece_ ||'-0'||i_;
		ELSE
			_num_ref_ = _num_piece_ ||'-'||i_;
		END IF;		
		_numero_ = COALESCE(_numero_, 0) + 1;
		_id_ = _id_ - 1;
		INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
	END IF;
	RETURN QUERY SELECT * FROM table_compta_content_journal ORDER BY _numero, _debit DESC, _credit DESC, _id DESC, _jour, _num_piece;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean)
  OWNER TO postgres;
  
  
-- Function: decoupage_interval_date(date, date, character varying)
DROP FUNCTION decoupage_interval_date(date, date, character varying);
CREATE OR REPLACE FUNCTION decoupage_interval_date(IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(intitule character varying, date_debut date, date_fin date, "position" integer) AS
$BODY$
DECLARE

BEGIN    
	return QUERY select * from decoupage_interval_date(date_debut_, date_fin_, periode_, false);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION decoupage_interval_date(date, date, character varying)
  OWNER TO postgres;


-- Function: decoupage_interval_date(date, date, character varying, boolean)
DROP FUNCTION decoupage_interval_date(date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION decoupage_interval_date(IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN view_date_ boolean)
  RETURNS TABLE(intitule character varying, date_debut date, date_fin date, "position" integer) AS
$BODY$
DECLARE

    date_ date;
    date_action_ date default date_debut_;

    intitule_ character varying;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;

    pattern_ character varying default 'dd'; 
    position_ integer default 0;

BEGIN    
-- 	DROP TABLE IF EXISTS table_decoupage_interval_date;
	CREATE TEMP TABLE IF NOT EXISTS table_decoupage_interval_date(_intitule character varying, _date_debut date, _date_fin date, _position integer);
	DELETE FROM table_decoupage_interval_date;
	if(view_date_)then
		pattern_ = 'dd-MM-yyyy';
	else
		if(to_char(date_debut_ , 'MM') != to_char(date_fin_ , 'MM'))then
			pattern_ =  pattern_ || '-MM';
		end if;
		if(to_char(date_debut_ , 'yyyy') != to_char(date_fin_ , 'yyyy'))then
			pattern_ =  pattern_ || '-yyyy';
		end if;
	end if;
	if(coalesce(periode_, '') not in ('', ' '))then
		while(date_action_ <= date_fin_)
		loop
			if(periode_ = 'A')then
				date_ = (date_action_ + interval '1 year' - interval '1 day');
				if(date_ > date_fin_)then
					date_ = date_ - (date_ - date_fin_);
				end if;		
				intitule_ = (select extract(year from date_action_));	
			elsif(periode_ = 'T')then
				date_ = (date_action_ + interval '3 month' - interval '1 day');
				if(date_ > date_fin_)then
					date_ = date_ - (date_ - date_fin_);
				end if;		
				jour_0 = (select extract(month from date_action_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = '('||jour_0||'/';
				
				jour_0 = (select extract(month from date_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				intitule_ = jour_||jour_0||')';		
			elsif(periode_ = 'M')then
				date_ = (date_action_ + interval '1 month' - interval '1 day');
				if(date_ > date_fin_)then
					date_ = date_ - (date_ - date_fin_);
				end if;		
				jour_ = (select extract(month from date_action_));
				if(char_length(jour_)<2)then
					jour_ = '0'||jour_;
				end if;			
				annee_ = (select extract(year from date_action_));	
				intitule_ = jour_ ||'-'|| annee_;
			elsif(periode_ = 'S')then
				date_ = (date_action_ + interval '1 week' - interval '1 day');	
				if(date_ > date_fin_)then
					date_ = date_ - (date_ - date_fin_);
				end if;
				
				jour_0 = (select extract(day from date_action_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = '('||jour_0||'/';
				jour_0 = (select extract(day from date_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = jour_||jour_0||')-';
				
				mois_ = (select extract(month from date_));
				if(char_length(mois_)<2)then
					mois_ = '0'||mois_;
				end if;
				mois_0 = (select extract(month from date_action_));
				if(char_length(mois_0)<2)then
					mois_0 = '0'||mois_0;
				end if;
				if(mois_ != mois_0)then
					mois_ = '('|| mois_0 || '/'|| mois_ ||')';
				end if;
				intitule_ = jour_ || mois_;
			else
				date_ = (date_action_ + interval '0 day');
				if(date_ > date_fin_)then
					date_ = date_ - (date_ - date_fin_);
				end if;	
				intitule_ = to_char(date_action_ , pattern_);
			end if;
			
			INSERT INTO table_decoupage_interval_date VALUES(intitule_, date_action_, date_, position_);
		
			if(periode_ = 'A')then
				date_action_ = date_action_ + interval '1 year';
			elsif(periode_ = 'T')then
				date_action_ = date_action_ + interval '3 month';
			elsif(periode_ = 'M')then
				date_action_ = date_action_ + interval '1 month';
			elsif(periode_ = 'S')then
				date_action_ = date_action_ + interval '1 week';
			else
				date_action_ = date_action_ + interval '1 day';
			end if;
			position_ = position_ + 1;
		end loop;
	else
		intitule_ = to_char(date_action_ , 'dd-MM-yyyy');
		INSERT INTO table_decoupage_interval_date VALUES(intitule_, date_debut_, date_fin_, position_);
	end if;
	return QUERY select * from table_decoupage_interval_date;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION decoupage_interval_date(date, date, character varying, boolean)
  OWNER TO postgres;


-- Function: com_et_ca_by_periode(bigint, bigint, character varying, date, date, character varying, character varying)
-- DROP FUNCTION com_et_ca_by_periode(bigint, bigint, character varying, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION com_et_ca_by_periode(IN societe_ bigint, IN agence_ bigint, IN element_ character varying, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN nature_ character varying)
  RETURNS TABLE(id bigint, code character varying, intitule character varying, periode character varying, date_debut date, date_fin date, montant double precision, rang integer) AS
$BODY$
declare 
   ligne_ RECORD;
   dates_ RECORD;
   
   ids_ character varying default '0';
   
   valeur_ double precision default 0;
   
   query_ CHARACTER VARYING;
   query_valeur_ character varying;
   
begin 	
	nature_ = COALESCE(nature_, 'C');
	--DROP TABLE table_et_ca_by_periode;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ca_by_periode(_id_ bigint, _code_ character varying, _intitule_ character varying, _periode_ character varying, _date_debut_ date, _date_fin_ date, _montant_ double precision, _rang_ integer);
	DELETE FROM table_et_ca_by_periode;
	IF(COALESCE(nature_, 'C') = 'C')THEN
		query_ = 'SELECT a.id::bigint, UPPER(a.code_ref) as code, UPPER(a.designation) as intitule FROM yvs_base_classes_stat a WHERE a.code_ref IS NOT NULL ';
		IF(societe_ IS NOT NULL AND societe_ > 0)THEN
			query_ = query_ || ' AND a.societe = '||societe_;
		END IF;
	ELSIF(COALESCE(nature_, 'C') = 'F')THEN
		query_ = 'SELECT a.id::bigint, UPPER(a.reference_famille) as code, UPPER(a.designation) as intitule FROM yvs_base_famille_article a WHERE a.reference_famille IS NOT NULL ';
		IF(societe_ IS NOT NULL AND societe_ > 0)THEN
			query_ = query_ || ' AND a.societe = '||societe_;
		END IF;
	ELSE
		query_ = 'SELECT a.id::bigint, UPPER(a.code_users) as code, UPPER(a.nom_users) as intitule FROM yvs_users a INNER JOIN yvs_agences y ON a.agence = y.id WHERE a.code_users IS NOT NULL';
		IF(agence_ IS NOT NULL AND agence_ > 0)THEN
			query_ = query_ || ' AND y.id = '||agence_;
		END IF;
		IF(societe_ IS NOT NULL AND societe_ > 0)THEN
			query_ = query_ || ' AND y.societe = '||societe_;
		END IF;
	END IF;
	ids_ = '0';
	IF(element_ IS NOT NULL AND element_ NOT IN ('', ' ', '0'))THEN
		FOR ligne_ IN select val from regexp_split_to_table(element_,',') val
		LOOP
			ids_ = ids_ ||','||ligne_.val;
		END LOOP;
		query_ = query_ || ' AND a.id in ('||ids_||')';
	END IF;
	FOR ligne_ IN EXECUTE query_
	LOOP
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, true)
		LOOP
			query_valeur_ = 'SELECT SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y INNER JOIN yvs_com_doc_ventes d ON d.id = y.doc_vente INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc 
				INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id INNER JOIN yvs_com_creneau_point cp ON hu.creneau_point = cp.id INNER JOIN yvs_base_point_vente pt ON cp.point = pt.id
				WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND d.document_lie IS NULL AND e.date_entete BETWEEN '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''';
			IF(COALESCE(nature_, 'C') = 'C')THEN
				query_valeur_ = query_valeur_ || ' AND a.classe1 = '||ligne_.id;
			ELSIF(COALESCE(nature_, 'C') = 'F')THEN
				query_valeur_ = query_valeur_ || ' AND a.famille = '||ligne_.id;
			ELSE
				query_valeur_ = query_valeur_ || ' AND hu.users = '||ligne_.id;
			END IF;
			IF(agence_ IS NOT NULL AND agence_ > 0)THEN
				query_ = query_ || ' AND pt.agence = '||agence_;
			END IF;
			EXECUTE query_valeur_ INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_ca_by_periode values (ligne_.id, ligne_.code, ligne_.intitule, dates_.intitule, dates_.date_debut, dates_.date_fin, COALESCE(valeur_, 0), dates_.position);
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ca_by_periode ORDER BY _id_, _code_, _rang_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ca_by_periode(bigint, bigint, character varying, date, date, character varying, character varying)
  OWNER TO postgres;


-- Function: com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint)
DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying, IN article_ character varying, IN offset_ bigint, IN limit_ bigint)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, emplacement bigint, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision, "position" double precision, count double precision) AS
$BODY$
DECLARE 
	
BEGIN 	
	RETURN QUERY SELECT * FROM com_inventaire_preparatoire(societe_, agence_, depot_, 0, famille_, categorie_, groupe_, date_, print_all_, option_print_, type_, article_, offset_, limit_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint)
  OWNER TO postgres;


-- Function: com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying)
DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying, IN article_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, emplacement bigint, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision, "position" double precision, count double precision) AS
$BODY$
DECLARE 
	
BEGIN 	
	RETURN QUERY SELECT * FROM com_inventaire_preparatoire(societe_, agence_, depot_, famille_, categorie_, groupe_, date_, print_all_, option_print_, type_, article_, 0, 0);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying)
  OWNER TO postgres;


-- Function: com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN agence_ bigint, IN depot_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, emplacement bigint, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision, "position" double precision, count double precision) AS
$BODY$
DECLARE 
BEGIN 	
	RETURN QUERY SELECT * FROM com_inventaire_preparatoire(agence_, depot_, 0, '', 0, date_, print_all_, option_print_, type_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;


-- Function: com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN agence_ bigint, IN depot_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, emplacement bigint, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision, "position" double precision, count double precision) AS
$BODY$
DECLARE 
	societe_ BIGINT DEFAULT 0; 
BEGIN 	
	IF(COALESCE(agence_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_agences a WHERE a.id = agence_;
	ELSIF(COALESCE(depot_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_base_depots d INNER JOIN yvs_agences a ON d.agence = a.id WHERE d.id = depot_;
	ELSIF(COALESCE(famille_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_base_famille_article a WHERE a.id = famille_;
	END IF;
	RETURN QUERY SELECT * FROM com_inventaire_preparatoire(societe_, agence_, depot_, 0, famille_, categorie_, groupe_, date_, print_all_, option_print_, type_, '', 0, 0);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;
  
  
 -- Function: com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
-- DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN agence_ bigint, IN depot_ bigint, IN emplacement_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, emplacement bigint, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision, "position" double precision, count double precision) AS
$BODY$
DECLARE 
	societe_ BIGINT DEFAULT 0; 
BEGIN 	
	IF(COALESCE(agence_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_agences a WHERE a.id = agence_;
	ELSIF(COALESCE(depot_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_base_depots d INNER JOIN yvs_agences a ON d.agence = a.id WHERE d.id = depot_;
	ELSIF(COALESCE(famille_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_base_famille_article a WHERE a.id = famille_;
	END IF;
	RETURN QUERY SELECT * FROM com_inventaire_preparatoire(societe_, agence_, depot_, emplacement_, famille_, categorie_, groupe_, date_, print_all_, option_print_, type_, '', 0, 0);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;


-- Function: com_inventaire_preparatoire(bigint, bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint)
-- DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN emplacement_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying, IN article_ character varying, IN offset_ bigint, IN limit_ bigint)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, emplacement bigint, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision, "position" double precision, count double precision) AS
$BODY$
DECLARE 
	articles_ RECORD;
	unites_ RECORD;

	insert_ BOOLEAN DEFAULT false;

	unite_ BIGINT DEFAULT 0;
	count_ BIGINT DEFAULT 0;
	i_count_ BIGINT DEFAULT 0;
	position_ BIGINT DEFAULT 0;
	
	prix_ DOUBLE PRECISION DEFAULT 0;
	stock_ DOUBLE PRECISION DEFAULT 0;
	reservation_ DOUBLE PRECISION DEFAULT 0;
	reste_a_livre_ DOUBLE PRECISION DEFAULT 0;

	colonne_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT y.id, y.article, y.depot, a.ref_art, a.designation, f.reference_famille, f.designation as famille, d.agence, y.actif, 
		c.id AS unite, u.id AS mesure, u.reference, COALESCE(c.prix, a.puv) AS puv, COALESCE(c.prix_achat, a.pua) AS pua, e.emplacement AS emplacement';
	query_ CHARACTER VARYING DEFAULT ' FROM yvs_base_article_depot y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON f.id = a.famille 
		INNER JOIN yvs_base_depots d ON y.depot = d.id INNER JOIN yvs_base_conditionnement c ON c.article = a.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id
		INNER JOIN yvs_base_mouvement_stock m ON m.conditionnement = c.id LEFT JOIN yvs_base_article_emplacement e ON y.id = e.article
		 WHERE y.article IS NOT NULL AND f.societe = '||societe_;
	ids_ character varying default '0';
	
BEGIN 	
	DROP TABLE IF EXISTS table_inventaire_preparatoire;
	CREATE TEMP TABLE IF NOT EXISTS table_inventaire_preparatoire(_depot bigint, _article bigint, _code character varying, _designation character varying, numero_ character varying, _famille character varying, _unite bigint, _reference character varying, _emplacement bigint, prix_ double precision, _puv double precision, _pua double precision, _pr double precision, _stock double precision, _reservation double precision, _reste_a_livre double precision, _position double precision, _count double precision); 
	DELETE FROM table_inventaire_preparatoire;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND d.agence = '||agence_;
	END IF;
	IF(depot_ IS NOT NULL AND depot_ > 0)THEN
		query_ = query_ || ' AND d.id = '||depot_;
	END IF;
	IF(famille_ IS NOT NULL AND famille_ > 0)THEN
		query_ = query_ || ' AND a.famille = '||famille_;
	END IF;
	IF(groupe_ IS NOT NULL AND groupe_ > 0)THEN
		query_ = query_ || ' AND a.groupe = '||groupe_;
	END IF;
	IF(TRIM(COALESCE(categorie_, '')) NOT IN ('', ' '))THEN
		query_ = query_ || ' AND a.categorie = '||QUOTE_LITERAL(categorie_);
	END IF;
	ids_ = '0';
	IF(article_ IS NOT NULL AND article_ NOT IN ('', ' '))THEN
		FOR articles_ IN select val from regexp_split_to_table(article_,',') val
		LOOP
			ids_ = ids_ ||','||articles_.val;
		END LOOP;
		query_ = query_ || ' AND a.id in ('||ids_||')';
	END IF;
	IF(emplacement_ IS NOT NULL AND emplacement_ > 0)THEN
		query_ = query_ || ' AND e.emplacement = '||emplacement_;
	END IF;
	EXECUTE 'SELECT COUNT(DISTINCT(c.id)) ' || query_ INTO count_;
	query_ = query_ || ' ORDER BY a.designation';
	IF(limit_ IS NOT NULL AND limit_ > 0)THEN
		query_ = query_ || ' offset '||offset_;
	END IF;
-- 	RAISE NOTICE '%',query_;
	option_print_ = COALESCE(option_print_, '');
	FOR articles_ IN EXECUTE colonne_ || query_
	LOOP
		position_ = position_ + 1;
		insert_ = false;
		stock_ = (SELECT get_stock(articles_.article, 0, articles_.depot, articles_.agence, 0, date_, articles_.unite, 0));
		SELECT INTO reservation_ SUM(c.quantite) FROM yvs_com_reservation_stock c WHERE c.depot = articles_.depot AND c.article = articles_.article AND c.conditionnement = articles_.unite AND c.statut = 'V' AND c.date_echeance <= date_;
		SELECT INTO reste_a_livre_ ((COALESCE((select sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on a.id=c.article inner join yvs_com_doc_ventes d on d.id=c.doc_vente  inner join yvs_com_entete_doc_vente en on en.id=d.entete_doc
				where d.type_doc = 'FV' and d.statut = 'V' and en.date_entete <= date_ AND c.article = articles_.article AND d.depot_livrer = articles_.depot AND c.conditionnement = articles_.unite limit 1), 0))
			- (COALESCE((select sum(c1.quantite) from yvs_com_contenu_doc_vente c1 inner join yvs_base_articles a1 on a1.id=c1.article inner join yvs_com_doc_ventes d1 on d1.id=c1.doc_vente 
				where d1.type_doc = 'BLV' and d1.statut = 'V' and d1.date_livraison <= date_ AND c1.article = articles_.article AND d1.depot_livrer = articles_.depot AND c1.conditionnement = articles_.unite limit 1), 0)));
		IF(reste_a_livre_ < 0)THEN
			reste_a_livre_ = 0;
		END IF;
		IF(print_all_)THEN
			IF(stock_ != 0)THEN
				insert_ = true;
			ELSE
				insert_ = articles_.actif;					
			END IF;
		ELSE
			IF(option_print_ = 'P')THEN
				IF(stock_ > 0)THEN
					insert_ = true;
				END IF;
			ELSIF(option_print_ = 'N')THEN
				IF(stock_ < 0)THEN
					insert_ = true;
				END IF;
			ELSIF(option_print_ = 'E')THEN
				IF(stock_ = 0)THEN
					insert_ = true;
				END IF;
			ELSE
				IF(stock_ != 0)THEN
					insert_ = true;
				END IF;
			END IF;
		END IF;
		IF(insert_)THEN
			IF(type_ = 'A')THEN
				prix_ = COALESCE((SELECT get_pua(articles_.article, 0, depot_, articles_.unite)), 0);
			ELSIF(type_ = 'V')THEN
				prix_ = articles_.puv;
			ELSE
				prix_ = COALESCE((SELECT get_pr(articles_.article, depot_, 0, date_, articles_.unite)), 0);
			END IF;
			i_count_ = i_count_ + 1;
			INSERT INTO table_inventaire_preparatoire VALUES(articles_.depot, articles_.article, articles_.ref_art, articles_.designation, articles_.reference_famille, articles_.famille, articles_.mesure, articles_.reference, articles_.emplacement, prix_, articles_.puv, articles_.pua, 0, stock_, COALESCE(reservation_, 0), COALESCE(reste_a_livre_, 0), position_, COALESCE(count_, 0));
		END IF;
		IF(limit_ IS NOT NULL AND limit_ > 0)THEN
			if(i_count_ >= limit_)THEN	
				EXIT;
			END IF;
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM table_inventaire_preparatoire ORDER BY _designation, _code, _depot, _famille;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint)
  OWNER TO postgres;
