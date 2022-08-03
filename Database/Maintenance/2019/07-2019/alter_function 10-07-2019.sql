UPDATE yvs_com_entete_doc_vente SET agence = (SELECT u.agence FROM yvs_users u INNER JOIN yvs_com_creneau_horaire_users h ON u.id = h.users WHERE h.id = creneau) WHERE agence IS NULL;

INSERT INTO yvs_base_element_reference(designation, module, author, model_courant, default_prefix) VALUES ('Piece Caisse Vente', 'COFI', 16, false, 'PV');
INSERT INTO yvs_base_element_reference(designation, module, author, model_courant, default_prefix) VALUES ('Piece Caisse Achat', 'COFI', 16, false, 'PA');
INSERT INTO yvs_base_element_reference(designation, module, author, model_courant, default_prefix) VALUES ('Piece Caisse Divers', 'COFI', 16, false, 'PD');
INSERT INTO yvs_base_element_reference(designation, module, author, model_courant, default_prefix) VALUES ('Piece Caisse Mission', 'COFI', 16, false, 'PM');
INSERT INTO yvs_base_element_reference(designation, module, author, model_courant, default_prefix) VALUES ('Piece Caisse Salaire', 'COFI', 16, false, 'PS');

-- Function: com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
-- DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN agence_ bigint, IN depot_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision) AS
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
	RETURN QUERY SELECT * FROM com_inventaire_preparatoire(societe_, agence_, depot_, famille_, categorie_, groupe_, date_, print_all_, option_print_, type_, '');
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;
  
  -- Function: com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
-- DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying, IN article_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision) AS
$BODY$
DECLARE 
	
BEGIN 	
	RETURN QUERY SELECT * FROM com_inventaire_preparatoire(societe_, agence_, depot_, famille_, categorie_, groupe_, date_, print_all_, option_print_, type_, article_, 0, 0);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;

-- Function: com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying)
-- DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying, IN article_ character varying, IN offset_ bigint, IN limit_ bigint)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision) AS
$BODY$
DECLARE 
	articles_ RECORD;
	unites_ RECORD;

	insert_ BOOLEAN DEFAULT false;

	unite_ BIGINT DEFAULT 0;
	
	prix_ DOUBLE PRECISION DEFAULT 0;
	stock_ DOUBLE PRECISION DEFAULT 0;
	reservation_ DOUBLE PRECISION DEFAULT 0;
	reste_a_livre_ DOUBLE PRECISION DEFAULT 0;

	query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.article, y.depot, a.ref_art, a.designation, f.reference_famille, f.designation as famille, a.pua, a.puv, d.agence, y.actif FROM yvs_base_article_depot y 
		INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON f.id = a.famille INNER JOIN yvs_base_depots d ON y.depot = d.id WHERE y.article IS NOT NULL AND f.societe = '||societe_;
	ids_ character varying default '0';
	
BEGIN 	
	DROP TABLE IF EXISTS table_inventaire_preparatoire;
	CREATE TEMP TABLE IF NOT EXISTS table_inventaire_preparatoire(_depot bigint, _article bigint, _code character varying, _designation character varying, numero_ character varying, _famille character varying, _unite bigint, _reference character varying, prix_ double precision, _puv double precision, _pua double precision, _pr double precision, _stock double precision, _reservation double precision, _reste_a_livre double precision); 
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
	option_print_ = COALESCE(option_print_, '');
-- 	RAISE NOTICE '%',query_;
	IF(limit_ IS NOT NULL AND limit_ > 0)THEN
		query_ = query_ || ' offset '||offset_||' limit '||limit_;
	END IF;
	FOR articles_ IN EXECUTE query_
	LOOP
		FOR unites_ IN SELECT y.id, y.unite, u.reference, COALESCE(y.prix, articles_.puv) AS puv, COALESCE(y.prix_achat, articles_.pua) AS pua FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = articles_.article
		LOOP
			insert_ = false;
			stock_ = (SELECT get_stock(articles_.article, 0, articles_.depot, articles_.agence, 0, date_, unites_.id, 0));
			SELECT INTO reservation_ SUM(c.quantite) FROM yvs_com_reservation_stock c WHERE c.depot = articles_.depot AND c.article = articles_.article AND c.conditionnement = unites_.id AND c.statut = 'V' AND c.date_echeance <= date_;
			SELECT INTO reste_a_livre_ ((COALESCE((select sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on a.id=c.article inner join yvs_com_doc_ventes d on d.id=c.doc_vente  inner join yvs_com_entete_doc_vente en on en.id=d.entete_doc
					where d.type_doc = 'FV' and d.statut = 'V' and en.date_entete <= date_ AND c.article = articles_.article AND d.depot_livrer = articles_.depot AND c.conditionnement = unites_.id limit 1), 0))
				- (COALESCE((select sum(c1.quantite) from yvs_com_contenu_doc_vente c1 inner join yvs_base_articles a1 on a1.id=c1.article inner join yvs_com_doc_ventes d1 on d1.id=c1.doc_vente 
					where d1.type_doc = 'BLV' and d1.statut = 'V' and d1.date_livraison <= date_ AND c1.article = articles_.article AND d1.depot_livrer = articles_.depot AND c1.conditionnement = unites_.id limit 1), 0)));
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
				ELSE
					IF(stock_ != 0)THEN
						insert_ = true;
					END IF;
				END IF;
			END IF;
			IF(insert_)THEN
				IF(type_ = 'A')THEN
					prix_ = COALESCE((SELECT get_pua(articles_.article, 0, depot_, unites_.id)), 0);
				ELSIF(type_ = 'V')THEN
					prix_ = unites_.puv;
				ELSE
					prix_ = COALESCE((SELECT get_pr(articles_.article, depot_, 0, date_, unites_.id)), 0);
				END IF;
				INSERT INTO table_inventaire_preparatoire VALUES(articles_.depot, articles_.article, articles_.ref_art, articles_.designation, articles_.reference_famille, articles_.famille, unites_.unite, unites_.reference, prix_, unites_.puv, unites_.pua, 0, stock_, COALESCE(reservation_, 0), COALESCE(reste_a_livre_, 0));
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_inventaire_preparatoire ORDER BY _depot, _famille, _code;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint)
  OWNER TO postgres;


-- Function: com_et_total_articles(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint,  date, date, character varying, character varying, character varying, bigint, bigint)
-- DROP FUNCTION com_et_total_articles(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint,  date, date, character varying, character varying, character varying, bigint, bigint);
CREATE OR REPLACE FUNCTION com_et_total_articles(IN societe_ bigint, IN agence_ bigint, IN point_ bigint, IN vendeur_ bigint, IN commercial_ bigint, IN client_ bigint, IN famille_ bigint, IN groupe_ bigint,  IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN categorie_ character varying, IN periode_ character varying, IN offset_ bigint, IN limit_ bigint)
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
					where d.statut = ''V'' AND f.societe = '||societe_;
    
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
	if(famille_ is not null and famille_ > 0)then
		query_ = query_ ||' and a.famille = '||famille_;
	end if;
	if(groupe_ is not null and groupe_ > 0)then
		query_ = query_ ||' and a.groupe = '||groupe_;
	end if;
	if(categorie_ is not null and categorie_ NOT IN ('', ' '))then
		query_ = query_ ||' and a.categorie = '||QUOTE_LITERAL(categorie_);
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ ||' and p.agence = '||agence_;
	end if;
	if(point_ is not null and point_ > 0)then
		query_ = query_ ||' and o.point = '||point_;
	end if;
	if(client_ is not null and client_ > 0)then
		query_ = query_ ||' and d.client = '||client_;
	end if;
	if(vendeur_ is not null and vendeur_ > 0)then
		query_ = query_ ||' and u.users = '||vendeur_;
	end if;
	if(commercial_ is not null and commercial_ > 0)then
-- 		query_ = query_ ||' and mo.utilisateur = '||commercial_;
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
		_totalpr = 0;
		execute_ = query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))' || ' GROUP BY c.id, m.id, a.id';
		if(limit_ is not null and limit_ > 0)then
		 	execute_ = execute_ ||' OFFSET '|| offset_ ||' LIMIT '||limit_;
		end if;
-- 		RAISE NOTICE 'execute_ : %',execute_;
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
ALTER FUNCTION com_et_total_articles(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint,  date, date, character varying, character varying, character varying, bigint, bigint)
  OWNER TO postgres;


-- Function: et_total_articles(bigint, bigint, date, date, character varying, character varying, character varying)
-- DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN categorie_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    
BEGIN    
	RETURN QUERY SELECT * FROM com_et_total_articles(societe_, agence_, 0, 0, 0, 0, 0, 0, date_debut_, date_fin_, article_, categorie_, periode_, 0, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying, character varying)
  OWNER TO postgres;

-- Function: et_total_article_vendeur(bigint, bigint, date, date, character varying, character varying)
DROP FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_article_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
	societe_ BIGINT DEFAULT 0; 
BEGIN    
	IF(COALESCE(agence_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_agences a WHERE a.id = agence_;
	ELSIF(COALESCE(vendeur_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_users d INNER JOIN yvs_agences a ON d.agence = a.id WHERE d.id = vendeur_;
	END IF;
	RETURN QUERY SELECT * FROM com_et_total_articles(societe_, agence_, 0, vendeur_, 0, 0, 0, 0, date_debut_, date_fin_, article_, '', periode_, 0, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;


-- Function: et_total_article_vendeur(bigint, bigint, date, date, character varying)
DROP FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

BEGIN    
	return QUERY select * from et_total_article_vendeur(agence_, vendeur_, date_debut_, date_fin_, '', period_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying)
  OWNER TO postgres;


-- Function: et_total_article_pt_vente(bigint, bigint, bigint, character varying, date, date, character varying)
DROP FUNCTION et_total_article_pt_vente(bigint, bigint, bigint, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_pt_vente(IN societe_ bigint, IN agence_ bigint, IN point_ bigint, IN article_ character varying, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

BEGIN    
	RETURN QUERY SELECT * FROM com_et_total_articles(societe_, agence_, point_, 0, 0, 0, 0, 0, date_debut_, date_fin_, article_, '', periode_, 0, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_pt_vente(bigint, bigint, bigint, character varying, date, date, character varying)
  OWNER TO postgres;
  
  
-- Function: et_total_article_pt_vente(bigint, bigint, character varying, date, date, character varying)
DROP FUNCTION et_total_article_pt_vente(bigint, bigint, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_pt_vente(IN agence_ bigint, IN point_ bigint, IN articles_ character varying, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE 
	societe_ BIGINT DEFAULT 0; 
BEGIN    
	IF(COALESCE(agence_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_agences a WHERE a.id = agence_;
	ELSIF(COALESCE(point_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_base_point_vente d INNER JOIN yvs_agences a ON d.agence = a.id WHERE d.id = point_;
	END IF;
	return QUERY select * from  et_total_article_pt_vente(0, agence_, point_, articles_, date_debut_, date_fin_, period_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_pt_vente(bigint, bigint, character varying, date, date, character varying)
  OWNER TO postgres;


-- Function: et_total_article_pt_vente(bigint, bigint, date, date, character varying)
DROP FUNCTION et_total_article_pt_vente(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_pt_vente(IN agence_ bigint, IN point_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE    

BEGIN    
	return QUERY SELECT * FROM et_total_article_pt_vente(agence_ , point_, '', date_debut_, date_fin_, period_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_pt_vente(bigint, bigint, date, date, character varying)
  OWNER TO postgres;


-- Function: et_total_article_client(bigint, bigint, date, date, character varying)
DROP FUNCTION et_total_article_client(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_client(IN agence_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
	societe_ BIGINT DEFAULT 0; 
BEGIN    
	IF(COALESCE(agence_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_agences a WHERE a.id = agence_;
	ELSIF(COALESCE(client_, 0) > 0)THEN
		SELECT INTO societe_ a.societe FROM yvs_com_client d INNER JOIN yvs_base_tiers a ON d.tiers = a.id WHERE d.id = client_;
	END IF;
	RETURN QUERY SELECT * FROM com_et_total_articles(societe_, agence_, 0, 0, 0, client_, 0, 0, date_debut_, date_fin_, '', '', periode_, 0, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_client(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
