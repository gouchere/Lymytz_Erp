-- Function: com_et_total_articles_achat(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying, character varying, character varying, character varying, bigint, bigint)
-- DROP FUNCTION com_et_total_articles_achat(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying, character varying, character varying, character varying, bigint, bigint);
CREATE OR REPLACE FUNCTION com_et_total_articles_achat(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN fournisseur_ bigint, IN famille_ bigint, IN classe_ bigint, IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN categorie_ character varying, IN periode_ character varying, IN type_ character varying, IN offset_ bigint, IN limit_ bigint)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, taux double precision, rang integer) AS
$BODY$
DECLARE
    _date_save DATE DEFAULT date_debut_;
    date_ DATE;
    
    taux_ DOUBLE PRECISION DEFAULT 0;
    pr_ DOUBLE PRECISION DEFAULT 0;
    totalpr_ DOUBLE PRECISION DEFAULT 0;
    
    _total DOUBLE PRECISION DEFAULT 0;
    _quantite DOUBLE PRECISION DEFAULT 0;
    _taux DOUBLE PRECISION DEFAULT 0;
    
    i INTEGER DEFAULT 0;
    count_ INTEGER DEFAULT 0;
    count_periode_ INTEGER DEFAULT 0;

    
    jour_ CHARACTER VARYING;
    execute_ CHARACTER VARYING;   
    facture_avoir_ CHARACTER VARYING;   
    query_ CHARACTER VARYING default 'select c.id as unite, a.id, a.ref_art, a.designation, m.reference, sum(coalesce(y.prix_total, 0)) as total, sum(coalesce(y.quantite_attendu, 0)) as qte from yvs_com_contenu_doc_achat y 
					inner join yvs_base_articles a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id inner join yvs_base_unite_mesure m on c.unite = m.id 
					inner join yvs_base_famille_article f ON a.famille = f.id inner join yvs_com_doc_achats d on y.doc_achat = d.id  
					where d.statut = ''V'' AND f.societe = '||societe_;
    group_by_ CHARACTER VARYING default ' c.id, m.id, a.id';
    
    data_ RECORD;
    avoir_ RECORD;
    prec_ RECORD;
    _article RECORD;
    dates_ RECORD;

    deja_ BOOLEAN DEFAULT FALSE;
    
BEGIN    
	DROP TABLE IF EXISTS table_total_articles_achat;
	CREATE TEMP TABLE IF NOT EXISTS table_total_articles_achat(_code_ CHARACTER VARYING, _nom_ CHARACTER VARYING, _article_ BIGINT, _unite bigint, _reference character varying, _periode_ CHARACTER VARYING, _total_ DOUBLE PRECISION, _quantite_ DOUBLE PRECISION, _taux_ DOUBLE PRECISION, _rang_ INTEGER);
	DELETE FROM table_total_articles_achat;
	deja_ = false;
	type_ = COALESCE(type_, 'A');
	IF(type_ = 'C')THEN-- Par classe statistique detaille
		query_ = 'select 0::bigint AS unite, m.id, m.code_ref AS ref_art, m.designation, null AS reference, sum(coalesce(y.prix_total, 0)) as total, sum(coalesce(y.quantite_attendu, 0)) as qte from yvs_com_contenu_doc_achat y 
				inner join yvs_base_articles a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id inner join yvs_base_classes_stat m on a.classe1 = m.id 
				inner join yvs_base_famille_article f ON a.famille = f.id inner join yvs_com_doc_achats d on y.doc_achat = d.id  
				where d.statut = ''V'' AND f.societe = '||societe_;
		group_by_ = ' m.id';
	ELSIF(type_ = 'G')THEN-- Par classe statistique parent
		query_ = 'select 0::bigint AS unite, m.id, m.code_ref AS ref_art, m.designation, null AS reference, sum(coalesce(y.prix_total, 0)) as total, sum(coalesce(y.quantite_attendu, 0)) as qte from yvs_base_classes_stat m, 
				yvs_com_contenu_doc_achat y inner join yvs_base_articles a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id
				inner join yvs_base_famille_article f ON a.famille = f.id inner join yvs_com_doc_achats d on y.doc_achat = d.id  
				where d.statut = ''V'' AND f.societe = '||societe_||' AND (a.classe1 = m.id OR a.classe1 IN (SELECT base_get_sous_classe_stat(m.id, true))) AND m.parent IS NULL';
		group_by_ = ' m.id';
	ELSIF(type_ = 'F')THEN-- Par famille article detaille
		query_ = 'select 0::bigint AS unite, f.id, f.reference_famille AS ref_art, f.designation, null AS reference, sum(coalesce(y.prix_total, 0)) as total, sum(coalesce(y.quantite_attendu, 0)) as qte from yvs_com_contenu_doc_achat y 
				inner join yvs_base_articles a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id left join yvs_base_classes_stat m on a.classe1 = m.id 
				inner join yvs_base_famille_article f ON a.famille = f.id inner join yvs_com_doc_achats d on y.doc_achat = d.id  
				where d.statut = ''V'' AND f.societe = '||societe_;
		group_by_ = ' f.id';
	ELSIF(type_ = 'M')THEN-- Par famille article parent
		query_ = 'select 0::bigint AS unite, f.id, f.reference_famille AS ref_art, f.designation, null AS reference, sum(coalesce(y.prix_total, 0)) as total, sum(coalesce(y.quantite_attendu, 0)) as qte from yvs_base_famille_article f,
				yvs_com_contenu_doc_achat y inner join yvs_base_articles a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id 
				inner join yvs_com_doc_achats d on y.doc_achat = d.id  left join yvs_base_classes_stat m on a.classe1 = m.id 
				where d.statut = ''V'' AND f.societe = '||societe_||' AND (a.famille = f.id OR a.famille IN (SELECT base_get_sous_famille(f.id, true))) AND f.famille_parent IS NULL';
		group_by_ = ' f.id';
	END IF;
	if(article_ is not null and article_ NOT IN ('', ' '))then
		IF(type_ = 'C' OR type_ = 'G')THEN
			query_ = query_ ||' AND m.code_ref LIKE ANY (ARRAY[';
		ELSIF(type_ = 'F' OR type_ = 'M')THEN
			query_ = query_ ||' AND f.reference_famille LIKE ANY (ARRAY[';				
		ELSE
			query_ = query_ ||' AND a.ref_art LIKE ANY (ARRAY[';			
		END IF;
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
	if(classe_ is not null and classe_ > 0)then
		query_ = query_ ||' and a.classe1 = '||classe_;
	end if;
	if(categorie_ is not null and categorie_ NOT IN ('', ' '))then
		query_ = query_ ||' and a.categorie = '||QUOTE_LITERAL(categorie_);
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ ||' and d.agence = '||agence_;
	end if;
	if(depot_ is not null and depot_ > 0)then
		query_ = query_ ||' and d.depot_reception = '||depot_;
	end if;
	if(fournisseur_ is not null and fournisseur_ > 0)then
		query_ = query_ ||' and d.fournisseur = '||fournisseur_;
	end if;
	
	date_debut_ = _date_save;
	i = 0;
	for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_)
	loop
		jour_ = dates_.intitule;
		count_ = 0;
		_total = 0;
		_quantite = 0;
		_taux = 0;
		execute_ = query_ ||' and ((d.type_doc = ''FA'' and d.date_doc between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))' || ' GROUP BY '||group_by_;
		if(limit_ is not null and limit_ > 0)then
		 	execute_ = execute_ ||' OFFSET '|| offset_ ||' LIMIT '||limit_;
		end if;
		FOR _article IN EXECUTE execute_
		LOOP			
			IF(type_ = 'A')THEN
				facture_avoir_ = query_ ||' and y.conditionnement = '||_article.unite||' and y.article = '||_article.id||'';
			ELSIF(type_ = 'F' OR type_ = 'M')THEN
				facture_avoir_ = query_ ||' and a.famille = '||_article.id||'';
			ELSIF(type_ = 'C' OR type_ = 'G')THEN
				facture_avoir_ = query_ ||' and a.classe1 = '||_article.id||'';
			END IF;
			facture_avoir_ = facture_avoir_ || ' and ((d.type_doc = ''FAA'' and d.date_doc between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''')
					 or (d.type_doc = ''BRA'' and d.date_livraison between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''')) GROUP BY '||group_by_;
			execute facture_avoir_ into avoir_;	
			if(avoir_.total IS NULL)then
				avoir_.total = 0;					
			end if;
			_article.total = _article.total - avoir_.total;
			
			SELECT INTO prec_ _total_ AS total FROM table_total_articles_achat WHERE _article_ = _article.id and _unite = _article.unite ORDER BY _rang_ DESC LIMIT 1;
			IF(prec_.total = 0)THEN
				SELECT INTO prec_ _total_ AS total FROM table_total_articles_achat WHERE _article_ = _article.id and _unite = _article.unite AND _total_ > 0 ORDER BY _rang_ DESC LIMIT 1;
			END IF;
			taux_ = _article.total - prec_.total;
			IF(taux_ IS NULL)THEN
				taux_ = 0;
			END IF;
			IF(taux_ != 0 and coalesce(prec_.total, 0) != 0)THEN
				taux_ = (taux_ / prec_.total) * 100;
			END IF;
			INSERT INTO table_total_articles_achat VALUES(_article.ref_art, _article.designation, _article.id, _article.unite, _article.reference, jour_, _article.total, _article.qte, taux_, i);

			_total = _total + _article.total;
			_quantite = _quantite + _article.qte;
			_taux = _taux + taux_;
			count_ = count_ + 1;
		END LOOP;
		i = i + 1;
		IF(count_ = 0)THEN
			count_ = 1;
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM table_total_articles_achat ORDER BY _nom_, _code_, _rang_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_total_articles_achat(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying, character varying, character varying, character varying, bigint, bigint)
  OWNER TO postgres;
