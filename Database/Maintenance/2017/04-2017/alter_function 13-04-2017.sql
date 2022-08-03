select alter_action_colonne_key('yvs_mut_mutualiste', true, true);

-- Function: et_balance_age_client(bigint, bigint, integer, integer, date, date)

-- DROP FUNCTION et_balance_age_client(bigint, bigint, integer, integer, date, date);

CREATE OR REPLACE FUNCTION et_balance_age_client(IN societe_ bigint, IN client_ bigint, IN ecart_ integer, IN colonne_ integer, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(client bigint, code character varying, nom character varying, jour character varying, solde double precision, rang integer, is_total boolean) AS
$BODY$
DECLARE
    _client_ RECORD;
    facture_ BIGINT;
    ttc_ DOUBLE PRECISION DEFAULT 0;
    avance_ DOUBLE PRECISION DEFAULT 0;
    solde_ DOUBLE PRECISION DEFAULT 0;
    debut_ INTEGER DEFAULT 0;
    fin_ INTEGER DEFAULT 0;
    
    i INTEGER DEFAULT 0;

BEGIN    
	DROP TABLE IF EXISTS client_balance_age;
	CREATE TEMP TABLE IF NOT EXISTS client_balance_age(_client BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _jour CHARACTER VARYING, _solde DOUBLE PRECISION, _rang INTEGER, _is_total BOOLEAN);
	SELECT INTO _client_ c.code_client AS code, c.nom FROM yvs_com_client c WHERE c.id = client_;
	IF(_client_ IS NOT NULL)THEN
		-- Solde du client
		SELECT INTO avance_ COALESCE(SUM(p.montant), 0) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
			WHERE p.statut_piece = 'P' AND d.statut = 'V' AND d.client = client_ AND (p.date_paiement BETWEEN date_debut_ AND date_fin_) AND (e.date_entete BETWEEN date_debut_ AND date_fin_);
		SELECT INTO ttc_ COALESCE(SUM(get_ttc_vente(d.id)), 0) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
			WHERE d.type_doc = 'FV' AND d.statut = 'V' AND d.client = client_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
		solde_ = (ttc_ - avance_);
		INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, 'SOLDE', solde_, 0, FALSE);
		
		-- Etallage du solde
		fin_ = ecart_;
		SELECT INTO solde_ COALESCE(SUM(m.montant - m.avance), 0) FROM yvs_com_mensualite_facture_vente m INNER JOIN yvs_com_doc_ventes d ON m.facture = d.id
			WHERE d.statut = 'V' AND d.type_doc = 'FV' AND d.client = client_ AND (m.date_reglement BETWEEN date_debut_ AND date_fin_) AND ((current_date - m.date_reglement) BETWEEN debut_ AND (fin_-1));
		INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, debut_||' - '||fin_, solde_, 1, FALSE);
		FOR i IN 2..colonne_ LOOP
			debut_ = fin_;
			fin_ = fin_ + ecart_;
			SELECT INTO solde_ COALESCE(SUM(m.montant - m.avance), 0) FROM yvs_com_mensualite_facture_vente m INNER JOIN yvs_com_doc_ventes d ON m.facture = d.id
				WHERE d.statut = 'V' AND d.type_doc = 'FV' AND d.client = client_ AND (m.date_reglement BETWEEN date_debut_ AND date_fin_) AND ((current_date - m.date_reglement) BETWEEN debut_ AND (fin_-1));		    
			INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, debut_||' - '||fin_, solde_, i , FALSE);
		END LOOP;	
		debut_ = fin_;
		fin_ = fin_ + 1000000;
		SELECT INTO solde_ COALESCE(SUM(m.montant - m.avance), 0) FROM yvs_com_mensualite_facture_vente m INNER JOIN yvs_com_doc_ventes d ON m.facture = d.id
			WHERE d.statut = 'V' AND d.type_doc = 'FV' AND d.client = client_ AND (m.date_reglement BETWEEN date_debut_ AND date_fin_) AND ((current_date - m.date_reglement) BETWEEN debut_ AND (fin_-1));
		INSERT INTO client_balance_age VALUES(client_, _client_.code, _client_.nom, '+ de '||(fin_ - 1000000), solde_, (colonne_ + 1), FALSE);	
	END IF;
	DELETE FROM client_balance_age WHERE _client = (SELECT _client FROM client_balance_age GROUP BY _client HAVING SUM(_solde) = 0);
	RETURN QUERY SELECT * FROM client_balance_age ORDER BY _is_total, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_balance_age_client(bigint, bigint, integer, integer, date, date)
  OWNER TO postgres;

  
  -- Function: et_total_articles(DATE, DATE, BOOLEAN, BIGINT)

-- DROP FUNCTION et_total_articles(DATE, DATE, BOOLEAN, BIGINT);

CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ BIGINT, IN agence_ BIGINT, IN date_debut_ DATE, IN date_fin_ DATE, IN periode_ CHARACTER VARYING)
  RETURNS TABLE(code CHARACTER VARYING, nom CHARACTER VARYING, article BIGINT, periode CHARACTER VARYING, total DOUBLE PRECISION, quantite DOUBLE PRECISION, taux DOUBLE PRECISION, rang INTEGER, is_total BOOLEAN, is_footer BOOLEAN) AS
$BODY$
DECLARE
    _date_save DATE DEFAULT date_debut_;
    date_ DATE;
    
    taux_ DOUBLE PRECISION DEFAULT 0;
    _total DOUBLE PRECISION DEFAULT 0;
    _quantite DOUBLE PRECISION DEFAULT 0;
    _taux DOUBLE PRECISION DEFAULT 0;
    
    i INTEGER DEFAULT 0;
    
    jour_ CHARACTER VARYING;
    jour_0 CHARACTER VARYING;
    mois_ CHARACTER VARYING;
    mois_0 CHARACTER VARYING;
    annee_ CHARACTER VARYING;
    annee_0 CHARACTER VARYING;
    
    data_ RECORD;
    prec_ RECORD;
    _article RECORD;
    
BEGIN    
	CREATE TEMP TABLE IF NOT EXISTS table_total_articles(_code_ CHARACTER VARYING, _nom_ CHARACTER VARYING, _article_ BIGINT, _periode_ CHARACTER VARYING, _total_ DOUBLE PRECISION, _quantite_ DOUBLE PRECISION, _taux_ DOUBLE PRECISION, _rang_ INTEGER, _is_total_ BOOLEAN, _is_footer_ BOOLEAN);
	DELETE FROM table_total_articles;

	FOR _article IN SELECT a.id, a.ref_art, a.designation FROM yvs_base_articles a INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = societe_ ORDER BY a.designation
	LOOP
		date_debut_ = _date_save;
		i = 0;
		_total = 0;
		_quantite = 0;
		_taux = 0;
		WHILE(date_debut_ <= date_fin_)
		LOOP
			IF(periode_ = 'A')THEN
				date_ = (date_debut_ + INTERVAL '1 YEAR' - INTERVAL '1 DAY');
				jour_ = (SELECT EXTRACT(YEAR FROM date_debut_));				
			ELSIF(periode_ = 'T')THEN
				date_ = (date_debut_ + INTERVAL '3 MONTH' - INTERVAL '1 DAY');
				jour_0 = (SELECT EXTRACT(MONTH FROM date_debut_));
				IF(char_length(jour_0)<2)THEN
					jour_0 = '0'||jour_0;
				END IF;
				jour_ = '('||jour_0||'/';
				
				jour_0 = (SELECT EXTRACT(MONTH FROM date_));
				IF(char_length(jour_0)<2)THEN
					jour_0 = '0'||jour_0;
				END IF;
				jour_ = jour_||jour_0||')';
				
				annee_ = (SELECT EXTRACT(YEAR FROM date_));
				annee_0 = (SELECT EXTRACT(YEAR FROM date_debut_));
				IF(annee_ != annee_0)THEN
					annee_ = '('|| annee_0 || '/'|| annee_ ||')';
				END IF;
				jour_ = jour_||'-'||annee_;
				
			ELSIF(periode_ = 'M')THEN
				date_ = (date_debut_ + INTERVAL '1 MONTH' - INTERVAL '1 DAY');
				jour_ = (SELECT EXTRACT(MONTH FROM date_debut_));
				IF(char_length(jour_)<2)THEN
					jour_ = '0'||jour_;
				END IF;
				jour_ = jour_||'-'||(SELECT EXTRACT(YEAR FROM date_debut_));
				
			ELSIF(periode_ = 'S')THEN
				date_ = (date_debut_ + INTERVAL '1 week' - INTERVAL '1 DAY');	
				
				jour_0 = (SELECT EXTRACT(DAY FROM date_debut_));
				IF(char_length(jour_0)<2)THEN
					jour_0 = '0'||jour_0;
				END IF;
				jour_ = '('||jour_0||'/';
				jour_0 = (SELECT EXTRACT(DAY FROM date_));
				IF(char_length(jour_0)<2)THEN
					jour_0 = '0'||jour_0;
				END IF;
				jour_ = jour_||jour_0||')-';
				
				mois_ = (SELECT EXTRACT(MONTH FROM date_));
				IF(char_length(mois_)<2)THEN
					mois_ = '0'||mois_;
				END IF;
				mois_0 = (SELECT EXTRACT(MONTH FROM date_debut_));
				IF(char_length(mois_0)<2)THEN
					mois_0 = '0'||mois_0;
				END IF;
				IF(mois_ != mois_0)THEN
					mois_ = '('|| mois_0 || '/'|| mois_ ||')';
				END IF;
				
				jour_ = jour_||mois_||'-'||(SELECT EXTRACT(YEAR FROM date_));	
			else
				date_ = (date_debut_ + INTERVAL '0 DAY');
				jour_ = to_char(date_debut_ ,'dd-MM-yyyy');
			END IF;
			

			IF(agence_ IS NULL OR agence_ < 1)THEN
				SELECT INTO data_ COALESCE(sum(c.prix_total), 0) AS total, COALESCE(sum(c.quantite), 0) AS qte FROM yvs_com_contenu_doc_vente c 
					INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
					INNER JOIN yvs_com_entete_doc_vente e ON e.id = d.entete_doc INNER JOIN yvs_com_creneau_horaire_users u ON e.creneau = u.id 
					WHERE d.type_doc = 'FV' AND e.date_entete between date_debut_ AND date_ AND c.article = _article.id AND d.statut = 'V';				
			else
				SELECT INTO data_ COALESCE(sum(c.prix_total), 0) AS total, COALESCE(sum(c.quantite), 0) AS qte FROM yvs_com_contenu_doc_vente c 
					INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
					INNER JOIN yvs_com_entete_doc_vente e ON e.id = d.entete_doc INNER JOIN yvs_com_creneau_horaire_users u ON e.creneau = u.id 
					INNER JOIN yvs_com_creneau_point o ON u.creneau_point = o.id INNER JOIN yvs_base_point_vente p ON o.point = p.id
					WHERE d.type_doc = 'FV' AND p.agence = agence_ AND e.date_entete between date_debut_ AND date_ AND c.article = _article.id AND d.statut = 'V';
			END IF;
			
			SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id ORDER BY _rang_ DESC LIMIT 1;
			IF(prec_.total = 0)THEN
				SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id AND _total_ > 0 ORDER BY _rang_ DESC LIMIT 1;
			END IF;
			taux_ = data_.total - prec_.total;
			IF(taux_ IS NULL)THEN
				taux_ = 0;
			END IF;
			IF(taux_ != 0)THEN
				taux_ = (taux_ / prec_.total) * 100;
			END IF;
			
			INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, jour_, data_.total, data_.qte, taux_, i, FALSE, FALSE);
			
			i = i + 1;
			_total = _total + data_.total;
			_quantite = _quantite + data_.qte;
			_taux = _taux + taux_;
			
			IF(periode_ = 'A')THEN
				date_debut_ = date_debut_ + INTERVAL '1 year';
			ELSIF(periode_ = 'T')THEN
				date_debut_ = date_debut_ + INTERVAL '3 month';
			ELSIF(periode_ = 'M')THEN
				date_debut_ = date_debut_ + INTERVAL '1 month';
			ELSIF(periode_ = 'S')THEN
				date_debut_ = date_debut_ + INTERVAL '1 week';
			else
				date_debut_ = date_debut_ + INTERVAL '1 day';
			END IF;
		END LOOP;
		IF(_total > 0)THEN
			INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, 'TOTAUX', _total, _quantite, (_taux / i), i, TRUE, FALSE);
		ELSE
			DELETE FROM table_total_articles WHERE _article_ = _article.id;
		END IF;
	END LOOP;
	FOR data_ IN SELECT _periode_ AS jour, COALESCE(sum(_total_), 0) AS total, COALESCE(sum(_quantite_), 0) AS qte, COALESCE(sum(_taux_), 0) AS taux, COALESCE(sum(_rang_), 0) AS rang FROM table_total_articles y GROUP BY jour
	LOOP		
		SELECT INTO i count(_rang_) FROM table_total_articles WHERE _periode_ = data_.jour;
		IF(i IS NULL OR i = 0)THEN
			i = 1;
		END IF;
		INSERT INTO table_total_articles VALUES('TOTAUX', 'TOTAUX', 0, data_.jour, data_.total, data_.qte, (data_.taux / i), data_.rang + 1, TRUE, TRUE);
	END LOOP;
	
	RETURN QUERY SELECT * FROM table_total_articles ORDER BY _is_footer_, _nom_, _code_, _rang_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(BIGINT, BIGINT, DATE, DATE, CHARACTER VARYING)
  OWNER TO postgres;
