-- Function: insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
-- DROP FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date);
CREATE OR REPLACE FUNCTION insert_mouvement_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, coutentree_ double precision, cout_ double precision, parent_ bigint, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE
       operation_ character varying default '';
       ligne_ record;
BEGIN
	CASE tableexterne_
		WHEN 'yvs_com_contenu_doc_vente' THEN  
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_contenu_doc_vente where id = idexterne_;
			operation_='Vente';
		WHEN 'yvs_com_contenu_doc_achat' THEN	
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_contenu_doc_achat where id = idexterne_;
			operation_='Achat';
		WHEN 'yvs_com_ration' THEN	
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_ration where id = idexterne_;
			operation_='Ration';
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			if(mouvement_='S') then
				select into ligne_ d.type_doc, c.qualite, c.conditionnement, c.conditionnement_entree, c.lot_sortie as lot, calcul_pr FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			else
				select into ligne_ d.type_doc, c.qualite_entree AS qualite, c.conditionnement_entree AS conditionnement, c.lot_entree as lot, calcul_pr FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			end if;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
			elsif(ligne_.type_doc='TR')then
				operation_='Reconditionnement';
			else
				if(mouvement_='S') then
					operation_='Sortie';
				else
					operation_='Entrée';
				end if;
			end if;	
		WHEN 'yvs_com_contenu_doc_stock_reception' THEN	
			select into ligne_ c.qualite_entree as qualite, c.conditionnement_entree as conditionnement, c.lot_entree as lot, r.calcul_pr from yvs_com_contenu_doc_stock_reception r inner join yvs_com_contenu_doc_stock c on r.contenu = c.id where r.id = idexterne_;
			operation_='Transfert';
		WHEN 'yvs_com_reconditionnement' THEN	
			if(mouvement_='S') then
				select into ligne_ qualite as qualite, conditionnement as conditionnement, lot as lot, calcul_pr from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			else
				select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot, calcul_pr from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			end if;	
		WHEN 'yvs_prod_of_suivi_flux' THEN	
			select into ligne_ c.unite as conditionnement, null::bigint as qualite, null::bigint as lot, calcul_pr from yvs_prod_of_suivi_flux y inner join yvs_prod_flux_composant f on y.composant = f.id inner join yvs_prod_composant_of c on f.composant = c.id where y.id = idexterne_;
			if(mouvement_='S') then
				operation_='Consommation';
			else
				operation_='Production';
			end if;
		WHEN 'yvs_prod_declaration_production' THEN	
			select into ligne_ conditionnement, null::bigint as qualite, null::bigint as lot, calcul_pr from yvs_prod_declaration_production where id = idexterne_;
			operation_='Production';
		WHEN 'yvs_prod_contenu_conditionnement' THEN	
			select into ligne_ conditionnement, null::bigint as qualite, null::bigint as lot, calcul_pr from yvs_prod_contenu_conditionnement where id = idexterne_;
			operation_='Conditionnement';
		WHEN 'yvs_prod_fiche_conditionnement' THEN	
			select into ligne_ unite_mesure as conditionnement, null::bigint as qualite, null::bigint as lot, null::boolean calcul_pr from yvs_prod_fiche_conditionnement f inner join yvs_prod_nomenclature n on f.nomenclature = n.id where f.id = idexterne_;
			operation_='Conditionnement';
		ELSE
			RETURN FALSE;
	END CASE;
	if(parent_ is not null)then
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, tranche_::bigint, parent_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, parent_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, tranche_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date) IS 'Insert une ligne de sortie de mouvement de stock';


-- Function: com_et_listing_vente(bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying)
-- DROP FUNCTION com_et_listing_vente(bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION com_et_listing_vente(IN societe_ bigint, IN agence_ bigint, IN point_ bigint, IN users_ bigint, IN client_ bigint, IN article_ bigint, IN unite_ bigint, IN date_debut_ date, IN date_fin_ date, IN type_ character varying)
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
ALTER FUNCTION com_et_listing_vente(bigint, bigint, bigint, bigint, bigint, bigint, bigint,  date, date, character varying)
  OWNER TO postgres;



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
		SELECT INTO valeur_ SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN (SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
			WHERE hu.users = vendeur_.id AND e.date_entete BETWEEN date_debut_ AND date_fin_ AND d.type_doc = 'FV' AND d.statut = 'V' AND (d.document_lie IS NULL OR (d.document_lie IS NOT NULL AND d.statut_regle IN ('R', 'P'))));	
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
		SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = vendeur_.id AND _rang > 0; 
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
  
  

-- Function: com_get_versement_attendu_vendeur(bigint, date, date)
-- DROP FUNCTION com_get_versement_attendu_vendeur(bigint, date, date);
CREATE OR REPLACE FUNCTION com_get_versement_attendu_vendeur(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;

	cs_m double precision default 0;
	cs_p double precision default 0;
	cs_ double precision default 0;
	
	head_ RECORD;
BEGIN    
	-- Recupere le montant TTC du contenu de la facture
	select into ca_ sum(c.prix_total) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
	inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id where h.users = id_ and d.type_doc = 'FV' and d.statut = 'V'
	and e.date_entete between date_debut_ and date_fin_ and (d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	-- Recupere le total des couts de service supplementaire d'une facture
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id 
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where h.users = id_ and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is true and o.service = true and e.date_entete between date_debut_ and date_fin_ and
		(d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where h.users = id_ and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is false and o.service = true and e.date_entete between date_debut_ and date_fin_ and
		(d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	cs_  = COALESCE(cs_p, 0) - COALESCE(cs_m, 0);
	ca_ = COALESCE(ca_, 0) + COALESCE(cs_, 0);
	
	FOR head_ IN SELECT h.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point p ON h.creneau_point= p.id 
		WHERE h.users = id_ AND e.date_entete BETWEEN date_debut_ AND date_fin_
	LOOP
		SELECT INTO result_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE d.type_doc = 'BCV' AND p.caissier = head_.users AND p.date_paiement = head_.date_entete;
		RAISE NOTICE 'result_ : %',result_;
		avance_ = avance_ + COALESCE(result_, 0);
	END LOOP;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avances : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu_vendeur(bigint, date, date)
  OWNER TO postgres;
  
  
-- Function: com_get_versement_attendu_point(bigint, date, date)
-- DROP FUNCTION com_get_versement_attendu_point(bigint, date, date);
CREATE OR REPLACE FUNCTION com_get_versement_attendu_point(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;

	cs_m double precision default 0;
	cs_p double precision default 0;
	cs_ double precision default 0;

	head_ RECORD;
BEGIN    
	-- Recupere le montant TTC du contenu de la facture
	select into ca_ sum(c.prix_total) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
	inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id where p.point = id_ and d.type_doc = 'FV' and d.statut = 'V'
	and e.date_entete between date_debut_ and date_fin_ and (d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	-- Recupere le total des couts de service supplementaire d'une facture
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id 
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where p.point = id_ and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is true and o.service = true and e.date_entete between date_debut_ and date_fin_ and
		(d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where p.point = id_ and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is false and o.service = true and e.date_entete between date_debut_ and date_fin_ and
		(d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	cs_  = COALESCE(cs_p, 0) - COALESCE(cs_m, 0);
	ca_ = COALESCE(ca_, 0) + COALESCE(cs_, 0);
	
	FOR head_ IN SELECT h.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point p ON h.creneau_point= p.id 
		WHERE p.point = id_ AND e.date_entete BETWEEN date_debut_ AND date_fin_
	LOOP
		SELECT INTO result_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE d.type_doc = 'BCV' AND p.caissier = head_.users AND p.date_paiement = head_.date_entete;
		RAISE NOTICE 'result_ : %',result_;
		avance_ = avance_ + COALESCE(result_, 0);
	END LOOP;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avances : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu_point(bigint, date, date)
  OWNER TO postgres;