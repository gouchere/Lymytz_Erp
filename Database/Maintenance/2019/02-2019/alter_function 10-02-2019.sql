-- Function: import_ristourne(character varying, integer, character varying, character varying, character varying)
-- DROP FUNCTION import_ristourne(character varying, integer, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION import_ristourne(serveur character varying, port integer, database character varying, users character varying, password character varying)
  RETURNS boolean AS
$BODY$
DECLARE
    plans_ RECORD;
    ristournes_ RECORD;
    article_ RECORD;
    
    plan_ BIGINT DEFAULT 0;
    ristourne_ BIGINT DEFAULT 0;
    grille_ BIGINT DEFAULT 0;

    permanent_ BOOLEAN DEFAULT FALSE;

    base_ CHARACTER VARYING DEFAULT 'QTE';
    nature_montant_ CHARACTER VARYING DEFAULT 'TAUX';

    max_ DOUBLE PRECISION DEFAULT 10000000;
    montant_ DOUBLE PRECISION DEFAULT 0;

BEGIN
	FOR plans_  IN SELECT * FROM dblink('host='||serveur||' dbname='||database||' user='||users||' password='||password,'select * FROM planderistourne ')
		AS t(id integer, base character varying, calcul character varying, debut date, domaine character varying, fin date, intitule character varying, modeapplication character varying, objectif character varying, code_acces bigint)
	LOOP
		IF(UPPER(COALESCE(plans_.modeapplication, '')) = 'PERMANENCE')THEN
			permanent_ = TRUE;
		ELSE
			permanent_ = FALSE;
		END IF;
		IF(UPPER(COALESCE(plans_.base, '')) != '')THEN
			base_ = UPPER(COALESCE(plans_.base, ''));
		ELSE
			base_ = 'QTE';
		END IF;
		SELECT INTO plan_ id FROM yvs_com_plan_ristourne WHERE reference = plans_.intitule;
		IF(COALESCE(plan_, 0) = 0)THEN
			INSERT INTO yvs_com_plan_ristourne(actif, reference, societe, author) VALUES (true, plans_.intitule, 2290, 16);
			plan_ = currval('yvs_com_plan_ristourne_id_seq');
		END IF;
		FOR ristournes_  IN SELECT * FROM dblink('host='||serveur||' dbname='||database||' user='||users||' password='||password,'select * FROM articlesristourne where idristourne = '||plans_.id)
			AS t(id integer, borne double precision, istranche integer, taux double precision, tranche integer, valeur double precision, idristourne integer, refart character varying)
		LOOP
			SELECT INTO article_ c.id as unite, a.id FROM yvs_base_articles a LEFT JOIN yvs_base_conditionnement c ON c.article = a.id WHERE c.cond_vente AND a.ref_art = ristournes_.refart LIMIT 1;
			IF(COALESCE(article_.id, 0) > 0)THEN
				SELECT INTO ristourne_ id FROM yvs_com_ristourne WHERE article = article_.id AND plan = plan_;
				IF(COALESCE(ristourne_, 0) = 0)THEN
					INSERT INTO yvs_com_ristourne(date_debut, date_fin, permanent, actif, author, article, plan, nature, date_update, date_save, conditionnement)
						VALUES (COALESCE(plans_.debut, current_date), COALESCE(plans_.fin, current_date), permanent_, true, 16, article_.id, plan_, 'R', current_timestamp, current_timestamp, article_.unite);
					ristourne_ = currval('yvs_com_ristourne_id_seq');
				END IF;
				IF(COALESCE(ristournes_.taux, 0) != 0)THEN
					nature_montant_ = 'TAUX';
					montant_ = COALESCE(ristournes_.taux, 0);
				ELSE
					nature_montant_ = 'MONTANT';
					montant_ = COALESCE(ristournes_.valeur, 0);
				END IF;
				SELECT INTO grille_ id FROM yvs_com_grille_ristourne WHERE ((nature_montant = 'TAUX' AND montant_ristourne = ristournes_.taux) OR (nature_montant = 'MONTANT' AND montant_ristourne = ristournes_.valeur)) AND ristourne = ristourne_;
				IF(COALESCE(grille_, 0) = 0)THEN
					INSERT INTO yvs_com_grille_ristourne(montant_minimal, montant_maximal, montant_ristourne, nature_montant, ristourne, author, base, date_update, date_save, article, conditionnement)
						VALUES (0, max_, montant_, nature_montant_, ristourne_, 16, base_, current_timestamp, current_timestamp, article_.id, article_.unite);
					grille_ = currval('yvs_com_grille_ristourne_id_seq');
				END IF;
			END IF;
		END LOOP;
	END LOOP;
	return true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION import_ristourne(character varying, integer, character varying, character varying, character varying)
  OWNER TO postgres;

  
-- Function: et_total_articles(bigint, bigint, date, date, character varying)
-- DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
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

 

-- Function: et_total_articles(bigint, bigint, date, date, character varying, character varying)
-- DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
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
    execute_ CHARACTER VARYING;   
    query_ CHARACTER VARYING DEFAULT 'SELECT c.id as unite, a.id, a.ref_art, a.designation, m.reference FROM yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id inner join yvs_base_unite_mesure m on c.unite = m.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = '||societe_||'';   
    save_ CHARACTER VARYING default 'select coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id 
					inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					inner join yvs_base_conditionnement i on c.conditionnement = i.id inner join yvs_base_unite_mesure m on i.unite = m.id
					where d.statut = ''V''';
    
    data_ RECORD;
    avoir_ RECORD;
    prec_ RECORD;
    _article RECORD;
    dates_ RECORD;

    deja_ BOOLEAN DEFAULT FALSE;
    
BEGIN    
	CREATE TEMP TABLE IF NOT EXISTS table_total_articles(_code_ CHARACTER VARYING, _nom_ CHARACTER VARYING, _article_ BIGINT, _unite bigint, _reference character varying, _periode_ CHARACTER VARYING, _total_ DOUBLE PRECISION, _quantite_ DOUBLE PRECISION, _taux_ DOUBLE PRECISION, _rang_ INTEGER, _is_total_ BOOLEAN, _is_footer_ BOOLEAN);
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
	query_ = query_ || ' ORDER BY a.designation';
	if(agence_ is not null and agence_ > 0)then
		save_ = save_ ||' and p.agence = '||agence_;
	end if;
	FOR _article IN EXECUTE query_
	LOOP
		date_debut_ = _date_save;
		i = 0;
		_total = 0;
		_quantite = 0;
		_taux = 0;
		query_ = save_ ||' and c.article = '||_article.id ||' and c.conditionnement = '||_article.unite;
		for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_)
		loop
			jour_ = dates_.intitule;
			execute_ = query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))';
			execute execute_ into data_;				
			if(data_.total IS NULL)then
				data_.total = 0;					
			end if;
			
			execute_ = query_ ||' and ((d.type_doc = ''FAV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''') or (d.type_doc = ''BRV'' and d.date_livraison between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))';
			execute execute_ into avoir_;	
			if(avoir_.total IS NULL)then
				avoir_.total = 0;					
			end if;
			data_.total = data_.total - avoir_.total;
			
			SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id and _unite = _article.unite ORDER BY _rang_ DESC LIMIT 1;
			IF(prec_.total = 0)THEN
				SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id and _unite = _article.unite AND _total_ > 0 ORDER BY _rang_ DESC LIMIT 1;
			END IF;
			taux_ = data_.total - prec_.total;
			IF(taux_ IS NULL)THEN
				taux_ = 0;
			END IF;
			IF(taux_ != 0 and coalesce(prec_.total, 0) != 0)THEN
				taux_ = (taux_ / prec_.total) * 100;
			END IF;

			if(coalesce(data_.total, 0) != 0 or coalesce(data_.qte, 0) != 0 or coalesce(taux_, 0) !=0)then
				INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, _article.unite, _article.reference, jour_, data_.total, data_.qte, taux_, i, FALSE, FALSE);
			END IF;
			i = i + 1;
			_total = _total + data_.total;
			_quantite = _quantite + data_.qte;
			_taux = _taux + taux_;
		END LOOP;
		IF(_total > 0)THEN
			INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, _article.unite, _article.reference, 'TOTAUX', _total, _quantite, (_taux / i), i, TRUE, FALSE);
		END IF;
	END LOOP;
	FOR data_ IN SELECT _periode_ AS jour, COALESCE(sum(_total_), 0) AS total, COALESCE(sum(_quantite_), 0) AS qte, COALESCE(sum(_taux_), 0) AS taux, COALESCE(sum(_rang_), 0) AS rang FROM table_total_articles y GROUP BY jour
	LOOP		
		SELECT INTO i count(_rang_) FROM table_total_articles WHERE _periode_ = data_.jour;
		IF(i IS NULL OR i = 0)THEN
			i = 1;
		END IF;
		INSERT INTO table_total_articles VALUES('TOTAUX', 'TOTAUX', 0, 0, '', data_.jour, data_.total, data_.qte, (data_.taux / i), data_.rang + 1, TRUE, TRUE);
	END LOOP;
	
	RETURN QUERY SELECT * FROM table_total_articles ORDER BY _is_footer_, _nom_, _code_, _rang_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;

  
  
-- Function: et_total_article_pt_vente(bigint, bigint, bigint, character varying, date, date, character varying)
-- DROP FUNCTION et_total_article_pt_vente(bigint, bigint, bigint, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_pt_vente(IN societe_ bigint, IN agence_ bigint, IN point_ bigint, IN articles_ character varying, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    date_ date;
    taux_ double precision default 0;
    somme_ double precision default 0;
    
    jour_ character varying;
    execute_ character varying;   
    query_ character varying;   
    select_ character varying default 'select distinct c.conditionnement, c.article, a.ref_art, a.designation, m.reference ';   
    save_ character varying default 'from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id inner join yvs_agences g on p.agence = g.id
					inner join yvs_base_conditionnement i on c.conditionnement = i.id inner join yvs_base_unite_mesure m on i.unite = m.id
					where d.statut = ''V''';
    article_ record;
    data_ record;
    avoir_ record;
    prec_ record;
    _point_ record;
    dates_ RECORD;
    
    i integer default 0;
    j integer default 0;    

    deja_ BOOLEAN DEFAULT FALSE;
BEGIN    
	DROP TABLE IF EXISTS point_vente_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS point_vente_article_by_jour(_code character varying, _nom character varying, art bigint, _unite bigint, _reference character varying, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _point_ y.code, y.libelle as nom from yvs_base_point_vente y where y.id = point_;
	if(societe_ is not null and societe_ > 0)then
		save_ = save_ ||' and g.societe = '||societe_;
	end if;
	if(agence_ is not null and agence_ > 0)then
		save_ = save_ ||' and p.agence = '||agence_;
	end if;
	if(point_ is not null and point_ > 0)then
		save_ = save_ ||' and p.id = '||point_;
	end if;
	if(articles_ is not null and articles_ not in ('', ' '))then
		save_ = save_ ||' AND a.ref_art LIKE ANY (ARRAY[';
		for jour_ IN select val from regexp_split_to_table(articles_,',') val
		loop
			if(deja_)then
				save_ = save_ || ',';
			end if;
			save_ = save_ || ''''||TRIM(jour_)||'%''';
			deja_ = true;
		end loop;
		save_ = save_ || '])';
	end if;
		
	select_ = 'select distinct c.conditionnement, c.article, a.ref_art, a.designation, m.reference ';
	for article_ in execute select_ || save_ || ' order by a.ref_art'
	loop
		i = 1;
		j = 1;
		for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, period_)
		loop
			jour_ = dates_.intitule;				
			select_ = 'select coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte ';
			query_ = save_ ||' and c.article = '||article_.article ||' and c.conditionnement = '||article_.conditionnement;
			
			execute_ = select_ || query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))';
			execute execute_ into data_;				
			if(data_.total IS NULL)then
				data_.total = 0;					
			end if;
			
			execute_ = select_ || query_ ||' and ((d.type_doc = ''FAV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''') or (d.type_doc = ''BRV'' and d.date_livraison between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))';
			execute execute_ into avoir_;	
			if(avoir_.total IS NULL)then
				avoir_.total = 0;					
			end if;
			data_.total = data_.total - avoir_.total;
			
			select into prec_ ttc as total from point_vente_article_by_jour where art = article_.article and _unite = article_.conditionnement order by rang desc limit 1;
			if(prec_.total = 0)then
				select into prec_ ttc as total from point_vente_article_by_jour where art = article_.article and _unite = article_.conditionnement and ttc > 0 order by rang desc limit 1;
			end if;
			taux_ = data_.total - prec_.total;
			if(taux_ is null)then
				taux_ = 0;
			end if;
			if(taux_ != 0 and coalesce(prec_.total, 0) != 0)then
				taux_ = (taux_ / prec_.total) * 100;
			end if;
			if(coalesce(data_.total, 0) != 0 or coalesce(data_.qte, 0) != 0 or coalesce(taux_, 0) !=0)then
				insert into point_vente_article_by_jour values(article_.ref_art, article_.designation, article_.article, article_.conditionnement, article_.reference, jour_, data_.total, data_.qte, taux_, i, false, false);
			end if;
			i = i + 1;
			j = j + 1;
		end loop;
		select into somme_ sum(ttc) from point_vente_article_by_jour where art = article_.article;
		if(coalesce(somme_, 0) = 0)then
			delete from point_vente_article_by_jour where art = article_.article;
		end if;
	end loop;
	for data_ in select art as article, _unite as unite, _code as ref_art, _nom as designation, _reference as reference, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(count(pos), 0) as pos from point_vente_article_by_jour y where y.total is false group by art, _code, _nom, _unite, _reference
	loop
		insert into point_vente_article_by_jour values(data_.ref_art, data_.designation, data_.article, data_.unite, data_.reference, 'TOTAUX', data_.ttc, data_.qte, data_.ttx / j, data_.pos + 1, false, true);
	end loop;
	for data_ in select jr as jour, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(sum(pos), 0) as pos from point_vente_article_by_jour y where y.total is false group by jr
	loop
		insert into point_vente_article_by_jour values('TOTAUX', 'TOTAUX', 0, 0, '', data_.jour, data_.ttc, data_.qte, data_.ttx, data_.pos + 1, true, true);
	end loop;
	return QUERY select * from point_vente_article_by_jour order by pos, art;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_pt_vente(bigint, bigint, bigint, character varying, date, date, character varying)
  OWNER TO postgres;

  
  
-- Function: et_total_article_vendeur(bigint, bigint, date, date, character varying)
-- DROP FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_article_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
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

  
-- Function: et_total_article_vendeur(bigint, bigint, date, date, character varying, character varying)
-- DROP FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_article_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN articles_ character varying, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    date_ date;
    taux_ double precision default 0;
    
    jour_ character varying;
    execute_ character varying;   
    query_ character varying;   
    select_ character varying default 'select distinct c.conditionnement, c.article, a.ref_art, a.designation, m.reference ';   
    save_ character varying default 'from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					inner join yvs_base_conditionnement i on c.conditionnement = i.id inner join yvs_base_unite_mesure m on i.unite = m.id
					where d.statut = ''V''';
    article_ record;
    data_ record;
    avoir_ record;
    prec_ record;
    _vendeur_ record;
    dates_ RECORD;
    
    i integer default 0;
    j integer default 0;

    deja_ BOOLEAN DEFAULT FALSE;
BEGIN    
	DROP TABLE IF EXISTS vendeur_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_article_by_jour(_code character varying, _nom character varying, art bigint, _unite bigint, _reference character varying, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	if(coalesce(vendeur_, 0) > 0 or coalesce(agence_, 0) > 0)then
		if(agence_ is not null and agence_ > 0)then
			save_ = save_ ||' and p.agence = '||agence_;
		end if;
		if(articles_ is not null and articles_ NOT IN ('', ' '))then
			save_ = save_ ||' AND a.ref_art LIKE ANY (ARRAY[';
			for jour_ IN select val from regexp_split_to_table(articles_,',') val
			loop
				if(deja_)then
					save_ = save_ || ',';
				end if;
				save_ = save_ || ''''||TRIM(jour_)||'%''';
				deja_ = true;
			end loop;
			save_ = save_ || '])';
		end if;
		if(vendeur_ is not null and vendeur_ > 0)then
			save_ = save_ ||' and u.users = '||vendeur_;
		end if;
		for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, period_)
		loop
			jour_ = dates_.intitule;
			select_ = 'select distinct c.conditionnement, c.article, a.ref_art, a.designation, m.reference ';			
			
			for article_ in execute select_ || save_ || ' order by a.ref_art'
			loop				
				select_ = 'select coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte ';
				query_ = save_ ||' and c.article = '||article_.article ||' and c.conditionnement = '||article_.conditionnement;
				
				execute_ = select_ || query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))';
				execute execute_ into data_;				
				if(data_.total IS NULL)then
					data_.total = 0;					
				end if;
				
				execute_ = select_ || query_ ||' and ((d.type_doc = ''FAV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''') or (d.type_doc = ''BRV'' and d.date_livraison between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))';
				execute execute_ into avoir_;	
				if(avoir_.total IS NULL)then
					avoir_.total = 0;					
				end if;
				data_.total = data_.total - avoir_.total;
				
				select into prec_ ttc as total from vendeur_article_by_jour where art = article_.article and _unite = article_.conditionnement order by rang desc limit 1;
				if(prec_.total = 0)then
					select into prec_ ttc as total from vendeur_article_by_jour where art = article_.article and _unite = article_.conditionnement and ttc > 0 order by rang desc limit 1;
				end if;
				taux_ = data_.total - prec_.total;
				if(taux_ is null)then
					taux_ = 0;
				end if;
				if(taux_ != 0 and coalesce(prec_.total, 0) != 0)then
					taux_ = (taux_ / prec_.total) * 100;
				end if;
				
				if(coalesce(data_.total, 0) != 0 or coalesce(data_.qte, 0) != 0 or coalesce(taux_, 0) !=0)then
					insert into vendeur_article_by_jour values(article_.ref_art, article_.designation, article_.article, article_.conditionnement, article_.reference, jour_, data_.total, data_.qte, taux_, i, false, false);
				end if;
			end loop;
			i = i + 1;
			j = j + 1;
		end loop;
		for data_ in select art as article, _unite as unite, _code as ref_art, _nom as designation, _reference as reference, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(count(pos), 0) as pos from vendeur_article_by_jour y where y.total is false group by art, _code, _nom, _unite, _reference
		loop
			insert into vendeur_article_by_jour values(data_.ref_art, data_.designation, data_.article, data_.unite, data_.reference, 'TOTAUX', data_.ttc, data_.qte, data_.ttx / j, data_.pos + 1, false, true);
		end loop;
		for data_ in select jr as jour, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(sum(pos), 0) as pos from vendeur_article_by_jour y where y.total is false group by jr
		loop
			insert into vendeur_article_by_jour values('TOTAUX', 'TOTAUX', 0, 0 ,'', data_.jour, data_.ttc, data_.qte, data_.ttx, data_.pos + 1, true, true);
		end loop;
	end if;
	return QUERY select * from vendeur_article_by_jour order by pos, art;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;

  
  
-- Function: et_total_pt_vente(bigint, bigint, date, date, character varying)
-- DROP FUNCTION et_total_pt_vente(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_pt_vente(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, point bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

BEGIN    
	return QUERY select * from et_total_pt_vente(societe_, agence_, date_debut_, date_fin_, '', periode_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_pt_vente(bigint, bigint, date, date, character varying)
  OWNER TO postgres;

  
-- Function: et_total_pt_vente(bigint, bigint, date, date, character varying, character varying)
-- DROP FUNCTION et_total_pt_vente(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_pt_vente(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN reference_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, point bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    points_ bigint;
    total_ record;
    jour_ character varying;
    query_ character varying default 'select distinct y.id from yvs_base_point_vente y inner join yvs_com_creneau_point o on o.point = y.id inner join yvs_com_creneau_horaire_users u on u.creneau_point = o.id inner join yvs_com_entete_doc_vente e on u.id = e.creneau inner join yvs_agences a on y.agence = a.id where y.id is not null';
    nbre integer default 0;

    deja_ BOOLEAN DEFAULT FALSE;
BEGIN    
	DROP TABLE IF EXISTS point_vente_by_jour_;
	CREATE TEMP TABLE IF NOT EXISTS point_vente_by_jour_(_code character varying, _nom character varying, pt bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);

	if(societe_ is not null and societe_ > 0)then
		query_ = query_ ||' and a.societe = '||societe_;
	end if;	
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ ||' and y.agence = '||agence_;
	end if;	
	if(reference_ is not null and reference_ NOT IN ('', ' '))then
		query_ = query_ ||' AND y.code LIKE ANY (ARRAY[';
		for jour_ IN select val from regexp_split_to_table(reference_,',') val
		loop
			if(deja_)then
				query_ = query_ || ',';
			end if;
			query_ = query_ || ''''||TRIM(jour_)||'%''';
			deja_ = true;
		end loop;
		query_ = query_ || '])';
	end if;
	for points_ in execute query_
	loop
		insert into point_vente_by_jour_ select *, false from et_total_one_pt_vente(agence_, CAST(points_ AS character varying), date_debut_, date_fin_, periode_);
	end loop;
	for total_ in select jr as jour, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(sum(pos), 0) as pos from point_vente_by_jour_ y group by jr
	loop		
		select into nbre count(pos) from point_vente_by_jour_ where jr = total_.jour;
		if(nbre is null or nbre = 0)then
			nbre = 1;
		end if;
		insert into point_vente_by_jour_ values('TOTAUX', 'TOTAUX', 0, total_.jour, total_.ttc, total_.qte, (total_.ttx / nbre), total_.pos + 1, true, true);
	end loop;
	
    return QUERY select * from point_vente_by_jour_ order by footer, pt, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_pt_vente(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;

  
  
-- Function: et_total_vendeurs(bigint, bigint, date, date, character varying)
-- DROP FUNCTION et_total_vendeurs(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_vendeurs(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, vendeur bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

BEGIN    
	return QUERY select * from et_total_vendeurs(societe_, agence_, date_debut_, date_fin_, '', periode_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_vendeurs(bigint, bigint, date, date, character varying)
  OWNER TO postgres;

  
-- Function: et_total_vendeurs(bigint, bigint, date, date, character varying, character varying)
-- DROP FUNCTION et_total_vendeurs(bigint, bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_vendeurs(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN reference_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, vendeur bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    users_ record;
    total_ record;
    nbre integer default 0;

    query_ character varying default 'select y.id, y.agence from yvs_users y inner join yvs_agences a on a.id = y.agence where y.id in (select users from yvs_com_creneau_horaire_users where id in (select creneau from yvs_com_entete_doc_vente))';
    jour_ character varying;
    
    deja_ BOOLEAN DEFAULT FALSE;
BEGIN    
	DROP TABLE IF EXISTS vendeur_by_jour_;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_by_jour_(_code character varying, _nom character varying, vend bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	if(societe_ is not null and societe_ > 0)then
		query_ = query_ ||' and a.societe = '||societe_;
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ ||' and y.agence = '||agence_;
	end if;
	if(reference_ is not null and reference_ NOT IN ('', ' '))then
		query_ = query_ ||' AND y.code_users LIKE ANY (ARRAY[';
		for jour_ IN select val from regexp_split_to_table(reference_,',') val
		loop
			if(deja_)then
				query_ = query_ || ',';
			end if;
			query_ = query_ || ''''||TRIM(jour_)||'%''';
			deja_ = true;
		end loop;
		query_ = query_ || '])';
	end if;
	for users_ in execute query_
	loop
		insert into vendeur_by_jour_ select *, false from et_total_one_vendeur(users_.agence, users_.id, date_debut_, date_fin_, periode_);
	end loop;
	for total_ in select jr as jour, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(sum(pos), 0) as pos from vendeur_by_jour_ y group by jr
	loop		
		select into nbre count(pos) from vendeur_by_jour_ where jr = total_.jour;
		if(nbre is null or nbre = 0)then
			nbre = 1;
		end if;
		insert into vendeur_by_jour_ values('TOTAUX', 'TOTAUX', 0, total_.jour, total_.ttc, total_.qte, (total_.ttx / nbre), total_.pos + 1, true, true);
	end loop;
	
    return QUERY select * from vendeur_by_jour_ order by footer, vend, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_vendeurs(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;

  
-- Function: et_total_one_vendeur(bigint, bigint, date, date, character varying)
-- DROP FUNCTION et_total_one_vendeur(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_one_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, vendeur bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean) AS
$BODY$
DECLARE
    total_ record;
    prec_ record;
    _vendeur_ record;
    dates_ RECORD;
    
    taux_ double precision default 0;
    valeur_ double precision default 0;
    qte_ double precision default 0;
    avoir_ double precision default 0;
    
    date_ date; 
    
    jour_ character varying;
    execute_ character varying;   
    query_ character varying;   
    select_ character varying default 'select coalesce(sum(c.quantite), 0) as qte  ';  
    save_ character varying default 'from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					inner join yvs_com_commercial_vente cv on cv.facture = d.id inner join yvs_com_comerciale co on cv.commercial = co.id
					where d.statut = ''V''';    
    i integer default 1;
    j integer default 0;

BEGIN    
	DROP TABLE IF EXISTS vendeur_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_by_jour(_code character varying, _nom character varying, vend bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, tot boolean);
	select into _vendeur_ code_users as code, nom_users as nom from yvs_users where id = vendeur_;
	if(_vendeur_ is not null)then
		if(agence_ is not null and agence_ > 0)then
			save_ = save_ ||' and p.agence = '||agence_;
		end if;
		query_ = save_ ||' and co.utilisateur = '||vendeur_;
		for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, period_)
		loop
			jour_ = dates_.intitule;
			-- Evaluer les quantitées
			select_ = 'select coalesce(sum(c.quantite), 0) as qte ';
			execute_ = select_ || query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))';
			execute execute_ into qte_;
			if(qte_ IS NULL)then
				qte_ = 0;
			end if;
			-- Evaluer le chiffre d'affaire
			select_ = 'select coalesce(sum(get_ca_vente(y.id)), 0) as prix from yvs_com_doc_ventes y where y.id in (select distinct d.id ';
			execute_ = select_ || query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''')))';
			execute execute_ into valeur_;
			if(valeur_ IS NULL)then
				valeur_ = 0;
			end if;
			-- Evaluer les avoirs/retours sur facture	
			select_ = 'select coalesce(sum(c.prix_total), 0) as prix ';
			execute_ = select_ || query_ ||' and ((d.type_doc = ''FAV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''') or (d.type_doc = ''BRV'' and d.date_livraison between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))';
			execute execute_ into avoir_;	
			if(avoir_ IS NULL)then
				avoir_ = 0;
			end if;	
			valeur_ = valeur_ - avoir_;
			
			select into prec_ ttc from vendeur_by_jour where vend = vendeur_ order by rang desc limit 1;
			if(prec_.ttc = 0)then
				select into prec_ ttc from vendeur_by_jour where vend = vendeur_ and ttc > 0 order by rang desc limit 1;
			end if;
			taux_ = valeur_ - prec_.ttc;
			if(taux_ is null)then
				taux_ = 0;
			end if;
			if(taux_ != 0 and valeur_ != 0)then
				taux_ = (taux_ / valeur_) * 100;
			end if;
			if(valeur_ != 0 or qte_ != 0)then
				insert into vendeur_by_jour values(_vendeur_.code, _vendeur_.nom, vendeur_, jour_, valeur_, qte_, taux_, i, false);
			end if;
			j = j + 1;			
			i = i + 1;
		end loop;
		
		select into total_ coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx from vendeur_by_jour;
		if(total_ is null or (total_.ttc = 0 and total_.qte = 0))then
			delete from vendeur_by_jour where vend = vendeur_;
		else
			insert into vendeur_by_jour values(_vendeur_.code, _vendeur_.nom, vendeur_, 'TOTAUX', total_.ttc, total_.qte, total_.ttx / j, i, true);		
		end if;
		
	end if;
	return QUERY select * from vendeur_by_jour order by vend, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_one_vendeur(bigint, bigint, date, date, character varying)
  OWNER TO postgres;

  
  
-- Function: com_et_journal_vente(bigint, bigint, date, date)
DROP FUNCTION com_et_journal_vente(bigint, bigint, date, date);
CREATE OR REPLACE FUNCTION com_et_journal_vente(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date)
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
	-- DROP TABLE IF EXISTS table_et_journal_vente;
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
		-- Vente directe par classe statistique
		FOR classe_ IN SELECT c.id, UPPER(c.code_ref) as code, UPPER(c.designation) as intitule FROM yvs_base_classes_stat c WHERE c.societe = societe_
		LOOP
			SELECT INTO valeur_ SUM(y.prix_total) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id
				INNER JOIN yvs_com_commercial_vente cv ON cv.facture = d.id INNER JOIN yvs_com_comerciale co ON cv.commercial = co.id
				WHERE co.utilisateur = vendeur_.id AND a.classe1 = classe_.id AND d.type_doc = 'FV' AND d.statut = 'V' AND e.date_entete BETWEEN date_debut_ AND date_fin_);
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, 0);	
			END IF;
		END LOOP;
		
		-- CA Des article non classé
		SELECT INTO valeur_ SUM(y.prix_total) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id
			INNER JOIN yvs_com_commercial_vente cv ON cv.facture = d.id INNER JOIN yvs_com_comerciale co ON cv.commercial = co.id
			WHERE co.utilisateur = vendeur_.id AND a.classe1 IS NULL AND d.type_doc = 'FV' AND d.statut = 'V' AND e.date_entete BETWEEN date_debut_ AND date_fin_);
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), TRUE, TRUE, 0);
		END IF;
		
		-- CA Par vendeur
		SELECT INTO valeur_ SUM(y.prix_total) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id
			INNER JOIN yvs_com_commercial_vente cv ON cv.facture = d.id INNER JOIN yvs_com_comerciale co ON cv.commercial = co.id
			WHERE co.utilisateur = vendeur_.id AND d.type_doc = 'FV' AND d.statut = 'V' AND e.date_entete BETWEEN date_debut_ AND date_fin_);	
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, TRUE, 1);
		
		-- Ristournes vente directe
		SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id
			INNER JOIN yvs_com_commercial_vente cv ON cv.facture = d.id INNER JOIN yvs_com_comerciale co ON cv.commercial = co.id
			WHERE co.utilisateur = vendeur_.id AND d.type_doc = 'FV' AND d.statut = 'V' AND e.date_entete BETWEEN date_debut_ AND date_fin_);
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, TRUE, 2);
		
		-- Commandes Annulées
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'A' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'CMDE.A', 'CMDE.A', COALESCE(valeur_, 0), FALSE, TRUE, 3);
					
		-- Commandes Recu
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'V' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'CMDE.R', 'CMDE.R', COALESCE(valeur_, 0), FALSE, TRUE, 4);		
		
		-- Versement attendu	
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, TRUE, 5);	

		SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE y._users = vendeur_.id;
		IF(COALESCE(valeur_, 0) = 0)THEN
			DELETE FROM table_et_journal_vente WHERE _users = vendeur_.id;
		END IF;
	END LOOP;
	
	-- LIGNE DES COMMANDE SERVIES PAR CLASSE STAT
	query_ = 'SELECT SUM(y.prix_total) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id
			INNER JOIN yvs_com_commercial_vente cv ON cv.facture = d.id INNER JOIN yvs_com_comerciale co ON cv.commercial = co.id INNER JOIN yvs_users u ON co.utilisateur = u.id INNER JOIN yvs_agences g ON u.agence = g.id
			WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND d.statut_livre = ''L'' AND d.date_livraison BETWEEN '''||date_debut_||''' AND '''||date_fin_||'''';
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND u.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND g.societe = '||societe_;
	END IF;
	FOR classe_ IN SELECT c.id, UPPER(c.code_ref) as code, UPPER(c.designation) as intitule FROM yvs_base_classes_stat c WHERE c.societe = societe_
	LOOP			
		EXECUTE query_ || ' AND a.classe1 = '||classe_.id||')' INTO valeur_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, TRUE, 0);	
		END IF;
	END LOOP;
	
	-- Autres aticles sans classe
	EXECUTE query_ || ' AND a.classe1 IS NULL) ' INTO valeur_;
	IF(COALESCE(valeur_, 0) > 0)THEN
		INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), FALSE, TRUE, 0);
	END IF;			
	
	-- CA sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'C.A', 'C.A', 0, FALSE, FALSE, 1);
	
	-- Ristourne sur commande reçu
	SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id
			INNER JOIN yvs_com_commercial_vente cv ON cv.facture = d.id INNER JOIN yvs_com_comerciale co ON cv.commercial = co.id INNER JOIN yvs_users u ON co.utilisateur = u.id INNER JOIN yvs_agences g ON u.agence = g.id
			WHERE g.id = agence_ AND d.type_doc = 'BCV' AND d.statut = 'V' AND d.statut_livre = 'L' AND d.date_livraison BETWEEN date_debut_ AND date_fin_);
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, FALSE, 2);
	
	-- CMDE.A sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'CMDE.A', 'CMDE.A', 0, FALSE, FALSE, 3);
	
	-- CMDE.R sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'CMDE.R', 'CMDE.R', 0, FALSE, FALSE, 4);
	
	-- VERS.ATT sur commande reçu
	SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'V' AND d.statut_livre = 'L' AND y.statut_piece = 'P'AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, FALSE, 5);

	FOR classe_ IN SELECT _classe, _reference, _designation, _rang, SUM(_montant) AS _valeur FROM table_et_journal_vente WHERE _users != 0 GROUP BY _classe, _reference, _designation, _rang
	LOOP 
		INSERT INTO table_et_journal_vente values (agence_, -1, 'TOTAUX','TOTAUX', classe_._classe, classe_._reference, classe_._designation, classe_._valeur, FALSE, FALSE, classe_._rang);
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_journal_vente ORDER BY _agence, _is_vendeur DESC, _code, _rang, _is_classe DESC, _classe;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_journal_vente(bigint, bigint, date, date)
  OWNER TO postgres;