-- Function: get_pr(bigint, bigint, bigint, date, bigint, bigint)
-- DROP FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint);
CREATE OR REPLACE FUNCTION get_pr(article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pr_ double precision;
	_depot_ bigint ;
	query_ character varying default 'SELECT m.cout_stock FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id WHERE m.calcul_pr IS TRUE AND m.mouvement = ''E''';

BEGIN
	SELECT INTO _depot_ depot_pr FROM yvs_base_article_depot WHERE article = article_ AND depot = depot_;
	_depot_ = COALESCE(_depot_, depot_);
	query_ = query_ || ' AND m.article = '||COALESCE(article_, 0)||' AND m.date_doc <= '||QUOTE_LITERAL(COALESCE(date_, CURRENT_DATE));
	IF(COALESCE(_depot_, 0) > 0)THEN
		query_ = query_ || ' AND m.depot = '||_depot_;
	END IF;
	IF(COALESCE(tranche_, 0) > 0)THEN
		query_ = query_ || ' AND m.tranche = '||tranche_;
	END IF;
	IF(COALESCE(unite_, 0) > 0)THEN
		query_ = query_ || ' AND m.conditionnement = '||unite_;
	END IF;
	IF(COALESCE(current_, 0) > 0)THEN
		query_ = query_ || ' AND m.id != '||current_;
	END IF;
	EXECUTE query_ INTO pr_;
	IF(pr_ IS NULL OR pr_ <=0)THEN
		pr_ = get_pua(article_, 0, 0, unite_);
	END IF;
	RETURN pr_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pr(bigint, bigint, bigint, date, bigint, bigint) IS 'retourne le prix de revient d'' article';


-- Function: et_total_articles(bigint, bigint, date, date, character varying, character varying)
DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

    
BEGIN    
	RETURN QUERY SELECT * FROM et_total_articles(societe_, agence_, date_debut_, date_fin_, '', '', periode_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;


-- Function: et_total_articles(bigint, bigint, date, date, character varying)
DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    
BEGIN    
	RETURN QUERY SELECT * FROM et_total_articles(societe_, agence_, date_debut_, date_fin_, '', periode_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying)
  OWNER TO postgres;



-- Function: et_total_articles(bigint, bigint, date, date, character varying, character varying, character varying)
DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN categorie_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    _date_save DATE DEFAULT date_debut_;
    date_ DATE;
    
    taux_ DOUBLE PRECISION DEFAULT 0;
    pr_ DOUBLE PRECISION DEFAULT 0;
    totalpr_ DOUBLE PRECISION DEFAULT 0;
    
    _total DOUBLE PRECISION DEFAULT 0;
    _quantite DOUBLE PRECISION DEFAULT 0;
    _pr DOUBLE PRECISION DEFAULT 0;
    _totalpr DOUBLE PRECISION DEFAULT 0;
    _taux DOUBLE PRECISION DEFAULT 0;
    
    i INTEGER DEFAULT 0;
    count_ INTEGER DEFAULT 0;

    
    jour_ CHARACTER VARYING;
    execute_ CHARACTER VARYING;   
    facture_avoir_ CHARACTER VARYING;   
    query_ CHARACTER VARYING default 'select c.id as unite, a.id, a.ref_art, a.designation, m.reference, sum(coalesce(y.prix_total, 0)) as total, sum(coalesce(y.quantite, 0)) as qte from yvs_com_contenu_doc_vente y 
					inner join yvs_base_articles a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id inner join yvs_base_unite_mesure m on c.unite = m.id 
					inner join yvs_base_famille_article f ON a.famille = f.id inner join yvs_com_doc_ventes d on y.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc 
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id  inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.statut = ''V''';
    
    data_ RECORD;
    avoir_ RECORD;
    prec_ RECORD;
    _article RECORD;
    dates_ RECORD;

    deja_ BOOLEAN DEFAULT FALSE;
    
BEGIN    
	DROP TABLE IF EXISTS table_total_articles;
	CREATE TEMP TABLE IF NOT EXISTS table_total_articles(_code_ CHARACTER VARYING, _nom_ CHARACTER VARYING, _article_ BIGINT, _unite bigint, _reference character varying, _periode_ CHARACTER VARYING, _total_ DOUBLE PRECISION, _quantite_ DOUBLE PRECISION, _pr_ DOUBLE PRECISION, _totalpr_ DOUBLE PRECISION, _taux_ DOUBLE PRECISION, _rang_ INTEGER, _is_total_ BOOLEAN, _is_footer_ BOOLEAN);
	DELETE FROM table_total_articles;
	deja_ = false;
	query_ = query_ ||' AND f.societe = '||societe_;
	if(article_ is not null and article_ NOT IN ('', ' '))then
		query_ = query_ ||' AND a.ref_art LIKE ANY (ARRAY[';
		for jour_ IN select val from regexp_split_to_table(article_,',') val
		loop
			if(deja_)then
				query_ = query_ || ',';
			end if;
			query_ = query_ || ''''||TRIM(jour_)||'%''';
			deja_ = true;
		end loop;
		query_ = query_ || '])';
	end if;
	if(categorie_ is not null and categorie_ NOT IN ('', ' '))then
		query_ = query_ ||' and a.categorie = '||QUOTE_LITERAL(categorie_);
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ ||' and p.agence = '||agence_;
	end if;
	
	date_debut_ = _date_save;
	i = 0;
	for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_)
	loop
		jour_ = dates_.intitule;
		count_ = 1;
		_total = 0;
		_quantite = 0;
		_taux = 0;
		_pr = 0;
		execute_ = query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))' || ' GROUP BY c.id, m.id, a.id';
		FOR _article IN EXECUTE execute_
		LOOP			
			facture_avoir_ = query_ ||' and y.conditionnement = '||_article.unite||' and y.article = '||_article.id||'
					and ((d.type_doc = ''FAV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''') or (d.type_doc = ''BRV'' and d.date_livraison between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''')) GROUP BY c.id, m.id, a.id';
			execute facture_avoir_ into avoir_;	
			if(avoir_.total IS NULL)then
				avoir_.total = 0;					
			end if;
			_article.total = _article.total - avoir_.total;
			
			SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id and _unite = _article.unite ORDER BY _rang_ DESC LIMIT 1;
			IF(prec_.total = 0)THEN
				SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id and _unite = _article.unite AND _total_ > 0 ORDER BY _rang_ DESC LIMIT 1;
			END IF;
			taux_ = _article.total - prec_.total;
			IF(taux_ IS NULL)THEN
				taux_ = 0;
			END IF;
			IF(taux_ != 0 and coalesce(prec_.total, 0) != 0)THEN
				taux_ = (taux_ / prec_.total) * 100;
			END IF;
			SELECT INTO pr_ AVG(y.cout_stock) FROM yvs_base_mouvement_stock y WHERE y.conditionnement = _article.unite AND y.date_doc BETWEEN dates_.date_debut AND dates_.date_fin;
			IF(pr_ IS NULL)THEN
				pr_ = 0;
			END IF;
			totalpr_ = (pr_ *  _article.qte);
			INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, _article.unite, _article.reference, jour_, _article.total, _article.qte, pr_, totalpr_, taux_, i, FALSE, FALSE);

			_total = _total + _article.total;
			_quantite = _quantite + _article.qte;
			_taux = _taux + taux_;
			_pr = _pr + pr_;
			_totalpr = _totalpr + totalpr_;
			count_ = count_ + 1;
		END LOOP;
		i = i + 1;
		IF(_total > 0)THEN
			INSERT INTO table_total_articles VALUES('TOTAUX', 'TOTAUX', 0, 0, '', jour_, _total, _quantite, (_pr / count_), _totalpr, (_taux / count_), i, TRUE, TRUE);
		END IF;
	END LOOP;
	FOR _article IN SELECT _article_ AS id, _nom_ AS designation, _code_ AS ref_art, _unite AS unite, _reference AS reference, COALESCE(sum(_total_), 0) AS total, COALESCE(sum(_quantite_), 0) AS qte, COALESCE(avg(_pr_), 0) AS pr, COALESCE(sum(_totalpr_), 0) AS totalpr, COALESCE(sum(_taux_), 0) AS taux 
		FROM table_total_articles y GROUP BY _article_, _code_, _nom_, _unite, _reference
	LOOP		
		SELECT INTO i count(_rang_) FROM table_total_articles WHERE _article_ = _article.id AND _unite = _article.unite;
		IF(i IS NULL OR i = 0)THEN
			i = 1;
		END IF;
		INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, _article.unite, _article.reference, 'TOTAUX', _article.total, _article.qte, _article.pr, _article.totalpr, (_article.taux / i), i+1, TRUE, FALSE);
	END LOOP;
	
	RETURN QUERY SELECT * FROM table_total_articles ORDER BY _is_total_, _is_footer_, _nom_, _code_, _rang_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying, character varying)
  OWNER TO postgres;
