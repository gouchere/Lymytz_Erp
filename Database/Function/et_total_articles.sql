-- Function: et_total_articles(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, periode character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    _date_save DATE DEFAULT date_debut_;
    date_ DATE;
    
    taux_ DOUBLE PRECISION DEFAULT 0;
    _total DOUBLE PRECISION DEFAULT 0;
    _quantite DOUBLE PRECISION DEFAULT 0;
    ttc_ DOUBLE PRECISION DEFAULT 0;
    qte_ DOUBLE PRECISION DEFAULT 0;
    _taux DOUBLE PRECISION DEFAULT 0;
    
    i INTEGER DEFAULT 0;
    
    jour_ CHARACTER VARYING;
    jour_0 CHARACTER VARYING;
    mois_ CHARACTER VARYING;
    mois_0 CHARACTER VARYING;
    annee_ CHARACTER VARYING;
    annee_0 CHARACTER VARYING;
    
    avoir_ RECORD;
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
					
				SELECT INTO avoir_ COALESCE(sum(c.prix_total), 0) AS total, COALESCE(sum(c.quantite), 0) AS qte FROM yvs_com_contenu_doc_vente c 
					INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
					INNER JOIN yvs_com_entete_doc_vente e ON e.id = d.entete_doc INNER JOIN yvs_com_creneau_horaire_users u ON e.creneau = u.id 
					WHERE d.type_doc = 'FAV' AND e.date_entete between date_debut_ AND date_ AND c.article = _article.id AND d.statut = 'V';				
			else
				SELECT INTO data_ COALESCE(sum(c.prix_total), 0) AS total, COALESCE(sum(c.quantite), 0) AS qte FROM yvs_com_contenu_doc_vente c 
					INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
					INNER JOIN yvs_com_entete_doc_vente e ON e.id = d.entete_doc INNER JOIN yvs_com_creneau_horaire_users u ON e.creneau = u.id 
					INNER JOIN yvs_com_creneau_point o ON u.creneau_point = o.id INNER JOIN yvs_base_point_vente p ON o.point = p.id
					WHERE d.type_doc = 'FV' AND p.agence = agence_ AND e.date_entete between date_debut_ AND date_ AND c.article = _article.id AND d.statut = 'V';
					
				SELECT INTO avoir_ COALESCE(sum(c.prix_total), 0) AS total, COALESCE(sum(c.quantite), 0) AS qte FROM yvs_com_contenu_doc_vente c 
					INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id 
					INNER JOIN yvs_com_entete_doc_vente e ON e.id = d.entete_doc INNER JOIN yvs_com_creneau_horaire_users u ON e.creneau = u.id 
					INNER JOIN yvs_com_creneau_point o ON u.creneau_point = o.id INNER JOIN yvs_base_point_vente p ON o.point = p.id
					WHERE d.type_doc = 'FAV' AND p.agence = agence_ AND e.date_entete between date_debut_ AND date_ AND c.article = _article.id AND d.statut = 'V';
			END IF;
			ttc_ = data_.total;
			qte_ = data_.qte;
			
			if(avoir_ is not null and avoir_.total is not null)then
				ttc_ = ttc_ - avoir_.total;
				qte_ = qte_ - avoir_.qte;
			end if;
			
			SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id ORDER BY _rang_ DESC LIMIT 1;
			IF(prec_.total = 0)THEN
				SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id AND _total_ > 0 ORDER BY _rang_ DESC LIMIT 1;
			END IF;
			taux_ = ttc_ - prec_.total;
			IF(taux_ IS NULL)THEN
				taux_ = 0;
			END IF;
			IF(taux_ != 0)THEN
				taux_ = (taux_ / prec_.total) * 100;
			END IF;
			
			INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, jour_, ttc_, qte_, taux_, i, FALSE, FALSE);
			
			i = i + 1;
			_total = _total + ttc_;
			_quantite = _quantite + qte_;
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
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
