-- Function: com_et_journal_vente(bigint, bigint, date, date, character varying)

-- DROP FUNCTION com_et_journal_vente(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION com_et_journal_vente(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN type_ character varying)
  RETURNS TABLE(agence bigint, users bigint, code character varying, nom character varying, classe bigint, reference character varying, designation character varying, montant double precision, is_classe boolean, is_vendeur boolean, rang integer) AS
$BODY$
declare 
   vendeur_ RECORD;
   classe_ record;
   
   journal_ character varying;
   
   valeur_ double precision default 0;
   totaux_ double precision;
   
   query_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT y.id, y.code_users, y.nom_users as nom, y.agence FROM yvs_users y INNER JOIN yvs_com_creneau_horaire_users c ON c.users = y.id INNER JOIN yvs_com_entete_doc_vente e ON e.creneau = c.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
   query_avoir_ CHARACTER VARYING;
   
begin 	
-- 	DROP TABLE table_et_journal_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_journal_vente(_agence BIGINT, _users BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _classe BIGINT, _reference CHARACTER VARYING, _designation CHARACTER VARYING, _montant DOUBLE PRECISION, _is_classe BOOLEAN, _is_vendeur BOOLEAN, _rang INTEGER);
	DELETE FROM table_et_journal_vente;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND e.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	FOR vendeur_ IN EXECUTE query_
	LOOP
		IF(type_ = 'A')THEN
			-- Vente directe par famille d'article
			FOR classe_ IN SELECT a.id, UPPER(a.ref_art) AS code, UPPER(a.designation) AS intitule, SUM(c.prix_total - c.ristourne) AS valeur FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc 
				INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article s ON a.famille = s.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND s.societe = societe_ AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_ GROUP BY a.id
			LOOP
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(classe_.valeur, 0), TRUE, TRUE, 0);
			END LOOP;
		ELSIF(type_ = 'F')THEN
			-- Vente directe par famille d'article
			FOR classe_ IN SELECT s.id, UPPER(s.reference_famille) AS code, UPPER(s.designation) AS intitule, SUM(c.prix_total - c.ristourne) AS valeur FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc 
				INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article s ON a.famille = s.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND s.societe = societe_ AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_ GROUP BY s.id
			LOOP
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(classe_.valeur, 0), TRUE, TRUE, 0);
			END LOOP;
		ELSIF(type_ = 'FP')THEN
			-- Vente directe par famille d'article parent
			FOR classe_ IN SELECT s.id, UPPER(s.reference_famille) AS code, UPPER(s.designation) AS intitule FROM yvs_base_famille_article s WHERE s.societe = societe_ AND s.famille_parent IS NULL
			LOOP
				valeur_ := (SELECT SUM(c.prix_total - c.ristourne) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
						INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
						WHERE hu.users = vendeur_.id AND (a.famille = classe_.id OR a.famille IN (SELECT base_get_sous_famille(classe_.id, true))) 
						AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);
				IF(COALESCE(valeur_, 0) > 0)THEN
					INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, 0);
				END IF;
			END LOOP;
		ELSIF(type_ = 'CP')THEN
			-- Vente directe par classe statistique parent
			FOR classe_ IN SELECT s.id, UPPER(s.code_ref) AS code, UPPER(s.designation) AS intitule FROM yvs_base_classes_stat s WHERE s.societe = societe_ AND s.parent IS NULL
			LOOP
				valeur_ := (SELECT SUM(c.prix_total - c.ristourne) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
						INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
						WHERE hu.users = vendeur_.id AND (a.classe1 = classe_.id OR a.classe1 IN (SELECT base_get_sous_classe_stat(classe_.id, true))) 
						AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);
				IF(COALESCE(valeur_, 0) > 0)THEN
					INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, 0);
				END IF;
			END LOOP;
			
			-- CA Des article non classé
			SELECT INTO valeur_ SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
				INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND a.classe1 IS NULL AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), TRUE, TRUE, 0);
			END IF;
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
		SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE y._users = vendeur_.id; 	
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -1, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, TRUE, 1);
		END IF;
		
		-- Ristournes vente directe
		SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
			WHERE hu.users = vendeur_.id AND e.date_entete BETWEEN date_debut_ AND date_fin_ AND d.type_doc = 'FV' AND d.statut = 'V' AND (d.document_lie IS NULL OR (d.document_lie IS NOT NULL AND d.statut_regle IN ('R', 'P'))));
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -2, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, TRUE, 2);
		END IF;
		
		-- Commandes Annulées
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'A' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -3, 'CMDE.A', 'CMDE.A', COALESCE(valeur_, 0), FALSE, TRUE, 3);
		END IF;
		
		-- Commandes Recu
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND d.statut = 'V' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -4, 'CMDE.R', 'CMDE.R', COALESCE(valeur_, 0), FALSE, TRUE, 4);		
		END IF;
		
		-- Versement attendu	
		SELECT INTO valeur_ com_get_versement_attendu(vendeur_.id, date_debut_, date_fin_); 
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -5, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, TRUE, 5);	
		END IF;
	END LOOP;
	
	-- LIGNE DES COMMANDE SERVIES PAR CLASSE STAT
	query_ = 'SELECT SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
			WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND d.document_lie IS NOT NULL AND e.date_entete BETWEEN '''||date_debut_||''' AND '''||date_fin_||'''';
	-- LIGNE DES AVOIRS SERVIS PAR CLASSE STAT
	query_avoir_ = 'SELECT SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
			WHERE d.type_doc = ''FAV'' AND d.statut = ''V'' AND e.date_entete BETWEEN '''||date_debut_||''' AND '''||date_fin_||'''';
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND u.agence = '||agence_;
		query_avoir_ = query_avoir_ || ' AND u.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND g.societe = '||societe_;
		query_avoir_ = query_avoir_ || ' AND g.societe = '||societe_;
	END IF;
	IF(type_ = 'A')THEN
		FOR classe_ IN SELECT a.id, UPPER(a.ref_art) as code, UPPER(a.designation) as intitule, SUM(y.prix_total - y.ristourne), d.type_doc FROM yvs_com_contenu_doc_vente y		
			INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id INNER JOIN yvs_com_doc_ventes d ON y.doc_vente = d.id
			WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d  INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
				INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
				WHERE ((d.type_doc = 'FV' AND d.document_lie IS NOT NULL) OR (d.type_doc = 'FAV')) AND d.statut = 'V' AND e.date_entete BETWEEN date_debut_ AND date_fin_ AND g.societe = societe_)
			GROUP BY a.id, d.type_doc
		LOOP			
			IF(classe_.type_doc = 'FV')THEN
				INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			ELSE
				INSERT INTO table_et_journal_vente values (agence_, -1, 'FACT.AVOIR', 'FACT.AVOIR', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;
		END LOOP;
	ELSIF(type_ = 'F')THEN
		FOR classe_ IN SELECT c.id, UPPER(c.reference_famille) as code, UPPER(c.designation) as intitule FROM yvs_base_famille_article c WHERE c.societe = societe_
		LOOP			
			EXECUTE query_ || ' AND a.famille = '||classe_.id||')' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;	
			
			EXECUTE query_avoir_ || ' AND a.famille = '||classe_.id||')' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (agence_, -1, 'FACT.AVOIR', 'FACT.AVOIR', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;
		END LOOP;
	ELSIF(type_ = 'FP')THEN
		-- Vente directe par famille d'article parent
		FOR classe_ IN SELECT s.id, UPPER(s.reference_famille) AS code, UPPER(s.designation) AS intitule FROM yvs_base_famille_article s WHERE s.societe = societe_ AND s.famille_parent IS NULL
		LOOP
			EXECUTE query_ || ' AND (a.famille = '||classe_.id||' OR a.famille IN (SELECT base_get_sous_famille('||classe_.id||', true))))' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (vendeur_.agence, 0, 'CMDE.SERVI', 'CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, 0);
			END IF;
			
			EXECUTE query_avoir_ || ' AND (a.famille = '||classe_.id||' OR a.famille IN (SELECT base_get_sous_famille('||classe_.id||', true))))' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (vendeur_.agence, -1, 'FACT.AVOIR', 'FACT.AVOIR', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, 0);
			END IF;
		END LOOP;
	ELSIF(type_ = 'CP')THEN
		-- Vente directe par classe statistique parent
		FOR classe_ IN SELECT s.id, UPPER(s.code_ref) AS code, UPPER(s.designation) AS intitule FROM yvs_base_classes_stat s WHERE s.societe = societe_ AND s.parent IS NULL
		LOOP
			EXECUTE query_ || ' AND (a.classe1 = '||classe_.id||' OR a.classe1 IN (SELECT base_get_sous_classe_stat('||classe_.id||', true))))' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (vendeur_.agence, 0, 'CMDE.SERVI', 'CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, 0);
			END IF;
			
			EXECUTE query_avoir_ || ' AND (a.classe1 = '||classe_.id||' OR a.classe1 IN (SELECT base_get_sous_classe_stat('||classe_.id||', true))))' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (vendeur_.agence, -1, 'FACT.AVOIR', 'FACT.AVOIR', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, 0);
			END IF;
		END LOOP;
		
		-- Autres aticles sans classe
		EXECUTE query_ || ' AND a.classe1 IS NULL) ' INTO valeur_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), FALSE, FALSE, 0);
		END IF;	
		
		-- Autres aticles sans classe
		EXECUTE query_avoir_ || ' AND a.classe1 IS NULL) ' INTO valeur_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (agence_, -1, 'FACT.AVOIR','FACT.AVOIR', 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), FALSE, FALSE, 0);
		END IF;	
	ELSE
		FOR classe_ IN SELECT c.id, UPPER(c.code_ref) as code, UPPER(c.designation) as intitule FROM yvs_base_classes_stat c WHERE c.societe = societe_
		LOOP			
			EXECUTE query_ || ' AND a.classe1 = '||classe_.id||')' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;
					
			EXECUTE query_avoir_ || ' AND a.classe1 = '||classe_.id||')' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (agence_, -1, 'FACT.AVOIR','FACT.AVOIR', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;
		END LOOP;
		
		-- Autres aticles sans classe
		EXECUTE query_ || ' AND a.classe1 IS NULL) ' INTO valeur_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), FALSE, FALSE, 0);
		END IF;	
		
		-- Autres aticles sans classe
		EXECUTE query_avoir_ || ' AND a.classe1 IS NULL) ' INTO valeur_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (agence_, -1, 'FACT.AVOIR','FACT.AVOIR', 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), FALSE, FALSE, 0);
		END IF;	
	END IF;
	
	-- CA sur commande livrée
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = 0; 
	IF(COALESCE(valeur_, 0) > 0)THEN
		INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -1, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, FALSE, 1);
	END IF;
	
	-- CA sur facture avoir
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = -1; 
	IF(COALESCE(valeur_, 0) > 0)THEN
		INSERT INTO table_et_journal_vente values (agence_, -1, 'FACT.AVOIR','FACT.AVOIR', -1, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, FALSE, 1);
	END IF;
	
	-- Ristourne sur commande livrée
	SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
		INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
		INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
		WHERE g.id = agence_ AND d.type_doc = 'BCV' AND d.statut = 'V' AND d.statut_livre = 'L' AND d.date_livraison BETWEEN date_debut_ AND date_fin_);	
	IF(COALESCE(valeur_, 0) > 0)THEN
		INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -2, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, FALSE, 2);
	END IF;
	
	-- Ristourne sur facture avoir
	SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
		INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
		INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
		WHERE g.id = agence_ AND d.type_doc = 'FAV' AND d.statut = 'V' AND e.date_entete BETWEEN date_debut_ AND date_fin_);	
	IF(COALESCE(valeur_, 0) > 0)THEN
		INSERT INTO table_et_journal_vente values (agence_, -1, 'FACT.AVOIR','FACT.AVOIR', -2, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, FALSE, 2);
	END IF;
	
	-- VERS.ATT sur commande livrée
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE y._users = 0 AND y._rang > 0; 
	IF(COALESCE(valeur_, 0) > 0)THEN
		INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -5, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, FALSE, 5);
	END IF;	
	
	-- VERS.ATT sur facture avoir
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE y._users = -1 AND _rang > 0; 
	IF(COALESCE(valeur_, 0) > 0)THEN
		INSERT INTO table_et_journal_vente values (agence_, -1, 'FACT.AVOIR','FACT.AVOIR', -5, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, FALSE, 5);
	END IF;
	
	FOR classe_ IN SELECT _classe, _reference, _designation, _rang, SUM(_montant) AS _valeur FROM table_et_journal_vente WHERE _users != -1 GROUP BY _classe, _reference, _designation, _rang
	LOOP 
-- 		RAISE NOTICE 'classe_ : %',classe_;
		INSERT INTO table_et_journal_vente values (agence_, -2, 'TOTAUX','TOTAUX', classe_._classe, classe_._reference, classe_._designation, classe_._valeur, FALSE, FALSE, classe_._rang);
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_journal_vente ORDER BY _is_vendeur DESC, _code, _rang, _is_classe DESC, _classe;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_journal_vente(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
