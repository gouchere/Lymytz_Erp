-- Function: com_et_total_articles(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint,  date, date, character varying, character varying, character varying, bigint, bigint)
-- DROP FUNCTION com_et_total_articles(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint,  date, date, character varying, character varying, character varying, bigint, bigint);
CREATE OR REPLACE FUNCTION com_et_total_articles(IN societe_ bigint, IN agence_ bigint, IN point_ bigint, IN vendeur_ bigint, IN commercial_ bigint, IN client_ bigint, IN famille_ bigint, IN groupe_ bigint,  IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN categorie_ character varying, IN periode_ character varying, IN offset_ bigint, IN limit_ bigint)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, pr double precision, totalpr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    
BEGIN    
	RETURN QUERY SELECT * FROM com_et_total_articles(societe_, agence_, point_, vendeur_, commercial_, client_, famille_, groupe_, date_debut_, date_fin_, article_, categorie_, periode_, 'A', offset_, limit_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_total_articles(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint,  date, date, character varying, character varying, character varying, bigint, bigint)
  OWNER TO postgres;
  
  
-- Function: com_et_total_articles(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying, character varying, character varying, character varying, bigint, bigint)
-- DROP FUNCTION com_et_total_articles(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying, character varying, character varying, character varying, bigint, bigint);
CREATE OR REPLACE FUNCTION com_et_total_articles(IN societe_ bigint, IN agence_ bigint, IN point_ bigint, IN vendeur_ bigint, IN commercial_ bigint, IN client_ bigint, IN famille_ bigint, IN groupe_ bigint, IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN categorie_ character varying, IN periode_ character varying, IN type_ character varying, IN offset_ bigint, IN limit_ bigint)
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
    group_by_ CHARACTER VARYING default ' c.id, m.id, a.id';
    
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
	type_ = COALESCE(type_, 'A');
	IF(type_ = 'F')THEN
		
	END IF;
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
		query_ = replace(query_, 'where', 'inner join yvs_com_commercial_vente co on co.facture = d.id inner join yvs_com_comerciale mo on co.commercial = mo.id where') ||' and mo.utilisateur = '||commercial_;
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
		execute_ = query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))' || ' GROUP BY '||group_by_;
		if(limit_ is not null and limit_ > 0)then
		 	execute_ = execute_ ||' OFFSET '|| offset_ ||' LIMIT '||limit_;
		end if;
-- 		RAISE NOTICE 'execute_ : %',execute_;
		FOR _article IN EXECUTE execute_
		LOOP			
			IF(type_ = 'A')THEN
				facture_avoir_ = query_ ||' and y.conditionnement = '||_article.unite||' and y.article = '||_article.id||'';
				SELECT INTO pr_ AVG(y.cout_stock) FROM yvs_base_mouvement_stock y WHERE y.conditionnement = _article.unite AND y.date_doc BETWEEN dates_.date_debut AND dates_.date_fin;
			ELSIF(type_ = 'F')THEN
				facture_avoir_ = query_ ||' and a.famille = '||_article.id||'';
			ELSIF(type_ = 'C')THEN
				facture_avoir_ = query_ ||' and a.classe1 = '||_article.id||'';
			END IF;
			facture_avoir_ = facture_avoir_ || ' and ((d.type_doc = ''FAV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''')
					 or (d.type_doc = ''BRV'' and d.date_livraison between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''')) GROUP BY '||group_by_;
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
			IF(pr_ IS NULL)THEN
				pr_ = 0;
			END IF;
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
ALTER FUNCTION com_et_total_articles(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying, character varying, character varying, character varying, bigint, bigint)
  OWNER TO postgres;
  
  
-- Function: action_on_all_tables_maj()
-- DROP FUNCTION action_on_all_tables_maj();
CREATE OR REPLACE FUNCTION action_on_all_tables_maj()
  RETURNS trigger AS
$BODY$    
DECLARE
	id_ bigint;
	author_ bigint;
	oldId bigint;
	
	action_ character varying;
	authors_ character varying;

	verify_author_ boolean default false;
	
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE  
	FOR authors_ IN SELECT column_name FROM information_schema.columns WHERE table_name = TG_TABLE_NAME AND column_name = 'author'
	LOOP
		verify_author_ = TRUE;
		EXIT;
	END LOOP;  
	IF(action_='INSERT' OR action_='UPDATE') THEN  
		id_ = NEW.id;
		IF(verify_author_ IS TRUE)THEN
			author_ = NEW.author;
		END IF;
	ELSE
		id_ = OLD.id;
		IF(verify_author_ IS TRUE)THEN
			author_ = OLD.author;
		END IF;
	END IF;
	
	INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, groupe_table, to_listen, action_name) VALUES (TG_TABLE_NAME, id_, current_timestamp, '', true, action_);
	IF(verify_author_ IS TRUE)THEN
		INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, groupe_table, to_listen, action_name, author) VALUES (TG_TABLE_NAME, id_, current_timestamp, '', true, action_, author_);
	END IF;
	IF(action_='INSERT' OR action_='UPDATE') THEN  
		RETURN NEW;
	ELSE
		RETURN OLD;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_all_tables_maj()
  OWNER TO postgres;

