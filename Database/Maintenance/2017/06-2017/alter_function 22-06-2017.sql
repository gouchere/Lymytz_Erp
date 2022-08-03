-- Function: get_ttc_vente(bigint)

-- DROP FUNCTION get_ttc_vente(bigint);

CREATE OR REPLACE FUNCTION get_ttc_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	taxe_r_ double precision default 0;
	qte_ double precision default 0;
	cs_ double precision default 0;
	remise_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into total_ sum(prix_total) from yvs_com_contenu_doc_vente where doc_vente = id_;
	--récupère l'equivalent des taxes déjà incluse dans le prix unitaire (cas du prix unitaire ttc)
	select into taxe_r_ sum(taxe) from yvs_com_contenu_doc_vente c INNER JOIN yvs_base_articles a ON a.id=c.article where doc_vente = id_ and a.puv_ttc is true;
	if(total_ is null)then
		total_ = 0;
	end if;
	if( taxe_r_ is null)then
		 taxe_r_ = 0;
	end if;
	-- Recupere le total des quantitées d'une facture
	select into qte_ sum(quantite) from yvs_com_contenu_doc_vente where doc_vente = id_;
	if(qte_ is null)then
		qte_ = 0;
	end if;
	
	-- Recupere le total des couts supplementaire d'une facture
	select into cs_ sum(montant) from yvs_com_cout_sup_doc_vente where doc_vente = id_ and actif = true;
	if(cs_ is null)then
		cs_ = 0;
	end if;
	total_ = total_ + cs_- taxe_r_;

	-- Recupere le total des remises sur la facture
	remise_ = (select get_remise_vente(id_));
	if(remise_ is null)then
		remise_= 0;
	end if;
	total_ = total_ - remise_;
	
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ttc_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ttc_vente(bigint) IS 'retourne le montant TTC d''un doc vente';


-- Function: get_ttc_entete_vente(bigint)

-- DROP FUNCTION get_ttc_entete_vente(bigint);

CREATE OR REPLACE FUNCTION get_ttc_entete_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	vente_ bigint;

BEGIN
	for vente_ in select id from yvs_com_doc_ventes where entete_doc = id_ and type_doc = 'FV'
	loop
		total_ = total_ + (select get_ttc_vente(vente_));
	end loop;
	for vente_ in select id from yvs_com_doc_ventes where entete_doc = id_ and type_doc = 'FAV'
	loop
		total_ = total_ - (select get_ttc_vente(vente_));
	end loop;
	if(total_ is null)then
		total_ = 0;
	end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ttc_entete_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ttc_entete_vente(bigint) IS 'retourne le montant TTC d''un entete vente';


-- Function: et_total_one_vendeur(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_one_vendeur(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_one_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, vendeur bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean) AS
$BODY$
DECLARE

    avoir_ record;
    total_ record;
    prec_ record;
    _vendeur_ record;
    
    ttc_ double precision default 0;
    qte_ double precision default 0;
    taux_ double precision default 0;
    
    date_ date; 
    
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    
    i integer default 1;
    j integer default 0;
    
    insert_ boolean default false;

BEGIN    
	DROP TABLE IF EXISTS vendeur_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_by_jour(_code character varying, _nom character varying, vend bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, tot boolean);
	select into _vendeur_ code_users as code, nom_users as nom from yvs_users where id = vendeur_;
	if(_vendeur_ is not null)then
		while(date_debut_ <= date_fin_)
		loop
			if(period_ = 'A')then
				date_ = (date_debut_ + interval '1 year' - interval '1 day');
				jour_ = (select extract(year from date_debut_));
				
			elsif(period_ = 'T')then
				date_ = (date_debut_ + interval '3 month' - interval '1 day');
				jour_0 = (select extract(month from date_debut_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = '('||jour_0||'/';
				
				jour_0 = (select extract(month from date_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = jour_||jour_0||')';
				
				annee_ = (select extract(year from date_));
				annee_0 = (select extract(year from date_debut_));
				if(annee_ != annee_0)then
					annee_ = '('|| annee_0 || '/'|| annee_ ||')';
				end if;
				jour_ = jour_||'-'||annee_;
				
			elsif(period_ = 'M')then
				date_ = (date_debut_ + interval '1 month' - interval '1 day');
				jour_ = (select extract(month from date_debut_));
				if(char_length(jour_)<2)then
					jour_ = '0'||jour_;
				end if;
				jour_ = jour_||'-'||(select extract(year from date_debut_));
				
			elsif(period_ = 'S')then
				date_ = (date_debut_ + interval '1 week' - interval '1 day');	
				
				jour_0 = (select extract(day from date_debut_));
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
				mois_0 = (select extract(month from date_debut_));
				if(char_length(mois_0)<2)then
					mois_0 = '0'||mois_0;
				end if;
				if(mois_ != mois_0)then
					mois_ = '('|| mois_0 || '/'|| mois_ ||')';
				end if;
				
				jour_ = jour_||mois_||'-'||(select extract(year from date_));	
			else
				date_ = (date_debut_ + interval '0 day');
				jour_ = to_char(date_debut_ ,'dd-MM-yyyy');
			end if;
			
			if(agence_ is null or agence_ < 1)then
				select into total_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id 
					where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V';

				select into avoir_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id 
					where d.type_doc = 'FAV' and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V';				
			else
				select into total_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V';

				select into avoir_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FAV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V';
			end if;
			ttc_ = total_.ttc;
			qte_ = total_.qte;
			
			if(avoir_ is not null and avoir_.ttc is not null)then
				ttc_ = ttc_ - avoir_.ttc;
				qte_ = qte_ - avoir_.qte;
			end if;
			select into prec_ ttc from vendeur_by_jour where vend = vendeur_ order by rang desc limit 1;
					
			if(total_.ttc != 0)then
				insert_ = true;
			end if;
			if(prec_.ttc = 0)then
				select into prec_ ttc from vendeur_by_jour where vend = vendeur_ and ttc > 0 order by rang desc limit 1;
			end if;
			taux_ = ttc_ - prec_.ttc;
			if(taux_ is null)then
				taux_ = 0;
			end if;
			if(taux_ != 0)then
				taux_ = (taux_ / prec_.ttc) * 100;
			end if;
			insert into vendeur_by_jour values(_vendeur_.code, _vendeur_.nom, vendeur_, jour_, ttc_, qte_, taux_, i, false);
			j = j + 1;			
			i = i + 1;

			if(period_ = 'A')then
				date_debut_ = date_debut_ + interval '1 year';
			elsif(period_ = 'T')then
				date_debut_ = date_debut_ + interval '3 month';
			elsif(period_ = 'M')then
				date_debut_ = date_debut_ + interval '1 month';
			elsif(period_ = 'S')then
				date_debut_ = date_debut_ + interval '1 week';
			else
				date_debut_ = date_debut_ + interval '1 day';
			end if;
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

  
  -- Function: et_total_article_vendeur(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

    current_ bigint default 0;
    
    date_ date;
    
    taux_ double precision default 0;
    
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    
    avoir_ record;
    data_ record;
    prec_ record;
    
    i integer default 0;
    j integer default 0;
    
    insert_ boolean default false;
    
    _vendeur_ record;

BEGIN    
	DROP TABLE IF EXISTS vendeur_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _vendeur_ code_users as code, nom_users as nom from yvs_users where id = vendeur_;
	if(_vendeur_ is not null)then
		while(date_debut_ <= date_fin_)
		loop
			if(period_ = 'A')then
				date_ = (date_debut_ + interval '1 year' - interval '1 day');
				jour_ = (select extract(year from date_debut_));
				
			elsif(period_ = 'T')then
				date_ = (date_debut_ + interval '3 month' - interval '1 day');
				jour_0 = (select extract(month from date_debut_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = '('||jour_0||'/';
				
				jour_0 = (select extract(month from date_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = jour_||jour_0||')';
				
				annee_ = (select extract(year from date_));
				annee_0 = (select extract(year from date_debut_));
				if(annee_ != annee_0)then
					annee_ = '('|| annee_0 || '/'|| annee_ ||')';
				end if;
				jour_ = jour_||'-'||annee_;
				
			elsif(period_ = 'M')then
				date_ = (date_debut_ + interval '1 month' - interval '1 day');
				jour_ = (select extract(month from date_debut_));
				if(char_length(jour_)<2)then
					jour_ = '0'||jour_;
				end if;
				jour_ = jour_||'-'||(select extract(year from date_debut_));
				
			elsif(period_ = 'S')then
				date_ = (date_debut_ + interval '1 week' - interval '1 day');	
				
				jour_0 = (select extract(day from date_debut_));
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
				mois_0 = (select extract(month from date_debut_));
				if(char_length(mois_0)<2)then
					mois_0 = '0'||mois_0;
				end if;
				if(mois_ != mois_0)then
					mois_ = '('|| mois_0 || '/'|| mois_ ||')';
				end if;
				
				jour_ = jour_||mois_||'-'||(select extract(year from date_));	
			else
				date_ = (date_debut_ + interval '0 day');
				jour_ = to_char(date_debut_ ,'dd-MM-yyyy');
			end if;
			
			if(agence_ is null or agence_ < 1)then
				for  data_ in select c.article, a.ref_art, a.designation, coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte, d.type_doc as typ from yvs_com_contenu_doc_vente c 
					inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc in ('FV', 'FAV') and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V' 
					group by c.article, a.ref_art, a.designation, d.type_doc order by a.ref_art
				loop					
					select into prec_ ttc as total from vendeur_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from vendeur_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					if(data_.typ = 'FV')then
						taux_ = data_.total - prec_.total;
					else
						taux_ = data_.total + prec_.total;
					end if;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;

					select into current_ coalesce(count(art), 0) from vendeur_article_by_jour where art = data_.article and jr = jour_;
					if(current_ > 0)then
						if(data_.typ = 'FV')then
							update vendeur_article_by_jour set ttc = ttc + data_.total, qte = qte + data_.qte, ttx = ttx + taux_ where art = data_.article and jr = jour_;
						else
							update vendeur_article_by_jour set ttc = ttc - data_.total, qte = qte - data_.qte, ttx = ttx - taux_ where art = data_.article and jr = jour_;
						end if;
					else
						if(data_.typ = 'FV')then
							insert into vendeur_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
						else
							insert into vendeur_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, - data_.total, - data_.qte, taux_, i, false, false);
						end if;
					end if;					
				end loop;				
			else
				for data_ in select c.article, a.ref_art, a.designation, coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte, d.type_doc as typ from yvs_com_contenu_doc_vente c 
					inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc in ('FV', 'FAV') and p.agence = agence_ and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V' 
					group by c.article, a.ref_art, a.designation, d.type_doc order by a.ref_art
				loop
					select into prec_ ttc as total from vendeur_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from vendeur_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					if(data_.typ = 'FV')then
						taux_ = data_.total - prec_.total;
					else
						taux_ = data_.total + prec_.total;
					end if;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;

					select into current_ coalesce(count(art), 0) from vendeur_article_by_jour where art = data_.article and jr = jour_;
					if(current_ > 0)then
						if(data_.typ = 'FV')then
							update vendeur_article_by_jour set ttc = ttc + data_.total, qte = qte + data_.qte, ttx = ttx + taux_ where art = data_.article and jr = jour_;
						else
							update vendeur_article_by_jour set ttc = ttc - data_.total, qte = qte - data_.qte, ttx = ttx - taux_ where art = data_.article and jr = jour_;
						end if;
					else
						if(data_.typ = 'FV')then
							insert into vendeur_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
						else
							insert into vendeur_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, - data_.total, - data_.qte, taux_, i, false, false);
						end if;
					end if;	
				end loop;
			end if;
			i = i + 1;
			j = j + 1;
			
			if(period_ = 'A')then
				date_debut_ = date_debut_ + interval '1 year';
			elsif(period_ = 'T')then
				date_debut_ = date_debut_ + interval '3 month';
			elsif(period_ = 'M')then
				date_debut_ = date_debut_ + interval '1 month';
			elsif(period_ = 'S')then
				date_debut_ = date_debut_ + interval '1 week';
			else
				date_debut_ = date_debut_ + interval '1 day';
			end if;
		end loop;
		for data_ in select art as article, _code as ref_art, _nom as designation, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(count(pos), 0) as pos from vendeur_article_by_jour y where y.total is false group by art, _code, _nom
		loop
			insert into vendeur_article_by_jour values(data_.ref_art, data_.designation, data_.article, 'TOTAUX', data_.ttc, data_.qte, data_.ttx / j, data_.pos + 1, false, true);
		end loop;
		for data_ in select jr as jour, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(sum(pos), 0) as pos from vendeur_article_by_jour y where y.total is false group by jr
		loop
			insert into vendeur_article_by_jour values('TOTAUX', 'TOTAUX', 0, data_.jour, data_.ttc, data_.qte, data_.ttx, data_.pos + 1, true, true);
		end loop;
	end if;
	return QUERY select * from vendeur_article_by_jour order by pos, art;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying)
  OWNER TO postgres;


  
  -- Function: et_total_article_pt_vente(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_article_pt_vente(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_pt_vente(IN agence_ bigint, IN point_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

    current_ bigint default 0;
    
    date_ date;
    
    taux_ double precision default 0;
    
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    
    data_ record;
    prec_ record;
    _point_ record;
    
    i integer default 0;
    j integer default 0;
    
    insert_ boolean default false;

BEGIN    
	DROP TABLE IF EXISTS point_vente_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS point_vente_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _point_ y.code, y.libelle as nom from yvs_base_point_vente y where y.id = point_;
	if(_point_ is not null)then
		while(date_debut_ <= date_fin_)
		loop
			if(period_ = 'A')then
				date_ = (date_debut_ + interval '1 year' - interval '1 day');
				jour_ = (select extract(year from date_debut_));
				
			elsif(period_ = 'T')then
				date_ = (date_debut_ + interval '3 month' - interval '1 day');
				jour_0 = (select extract(month from date_debut_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = '('||jour_0||'/';
				
				jour_0 = (select extract(month from date_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = jour_||jour_0||')';
				
				annee_ = (select extract(year from date_));
				annee_0 = (select extract(year from date_debut_));
				if(annee_ != annee_0)then
					annee_ = '('|| annee_0 || '/'|| annee_ ||')';
				end if;
				jour_ = jour_||'-'||annee_;
				
			elsif(period_ = 'M')then
				date_ = (date_debut_ + interval '1 month' - interval '1 day');
				jour_ = (select extract(month from date_debut_));
				if(char_length(jour_)<2)then
					jour_ = '0'||jour_;
				end if;
				jour_ = jour_||'-'||(select extract(year from date_debut_));
				
			elsif(period_ = 'S')then
				date_ = (date_debut_ + interval '1 week' - interval '1 day');	
				
				jour_0 = (select extract(day from date_debut_));
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
				mois_0 = (select extract(month from date_debut_));
				if(char_length(mois_0)<2)then
					mois_0 = '0'||mois_0;
				end if;
				if(mois_ != mois_0)then
					mois_ = '('|| mois_0 || '/'|| mois_ ||')';
				end if;
				
				jour_ = jour_||mois_||'-'||(select extract(year from date_));	
			else
				date_ = (date_debut_ + interval '0 day');
				jour_ = to_char(date_debut_ ,'dd-MM-yyyy');
			end if;
			
			if(agence_ is null or agence_ < 1)then
				for  data_ in select c.article, a.ref_art, a.designation, coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte, d.type_doc as typ from yvs_com_contenu_doc_vente c 
					inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id
					where d.type_doc in ('FV', 'FAV') and e.date_entete between date_debut_ and date_ and o.point = point_ and d.statut = 'V' group by c.article, a.ref_art, a.designation, d.type_doc 
					order by a.ref_art
				loop					
					select into prec_ ttc as total from point_vente_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from point_vente_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					if(data_.typ = 'FV')then
						taux_ = data_.total - prec_.total;
					else
						taux_ = data_.total + prec_.total;
					end if;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;

					select into current_ coalesce(count(art), 0) from point_vente_article_by_jour where art = data_.article and jr = jour_;
					if(current_ > 0)then
						if(data_.typ = 'FV')then
							update point_vente_article_by_jour set ttc = ttc + data_.total, qte = qte + data_.qte, ttx = ttx + taux_ where art = data_.article and jr = jour_;
						else
							update point_vente_article_by_jour set ttc = ttc - data_.total, qte = qte - data_.qte, ttx = ttx - taux_ where art = data_.article and jr = jour_;
						end if;
					else
						if(data_.typ = 'FV')then
							insert into point_vente_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
						else
							insert into point_vente_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, - data_.total, - data_.qte, taux_, i, false, false);
						end if;
					end if;	
				end loop;				
			else
				for data_ in select c.article, a.ref_art, a.designation, coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte, d.type_doc as typ from yvs_com_contenu_doc_vente c 
					inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc in ('FV', 'FAV') and p.agence = agence_ and e.date_entete between date_debut_ and date_ and o.point = point_ and d.statut = 'V' 
					group by c.article, a.ref_art, a.designation, d.type_doc order by a.ref_art
				loop
					select into prec_ ttc as total from point_vente_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from point_vente_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					if(data_.typ = 'FV')then
						taux_ = data_.total - prec_.total;
					else
						taux_ = data_.total + prec_.total;
					end if;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;

					select into current_ coalesce(count(art), 0) from point_vente_article_by_jour where art = data_.article and jr = jour_;
					if(current_ > 0)then
						if(data_.typ = 'FV')then
							update point_vente_article_by_jour set ttc = ttc + data_.total, qte = qte + data_.qte, ttx = ttx + taux_ where art = data_.article and jr = jour_;
						else
							update point_vente_article_by_jour set ttc = ttc - data_.total, qte = qte - data_.qte, ttx = ttx - taux_ where art = data_.article and jr = jour_;
						end if;
					else
						if(data_.typ = 'FV')then
							insert into point_vente_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
						else
							insert into point_vente_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, - data_.total, - data_.qte, taux_, i, false, false);
						end if;
					end if;	
				end loop;
			end if;
			i = i + 1;
			j = j + 1;
			
			if(period_ = 'A')then
				date_debut_ = date_debut_ + interval '1 year';
			elsif(period_ = 'T')then
				date_debut_ = date_debut_ + interval '3 month';
			elsif(period_ = 'M')then
				date_debut_ = date_debut_ + interval '1 month';
			elsif(period_ = 'S')then
				date_debut_ = date_debut_ + interval '1 week';
			else
				date_debut_ = date_debut_ + interval '1 day';
			end if;
		end loop;
		for data_ in select art as article, _code as ref_art, _nom as designation, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(count(pos), 0) as pos from point_vente_article_by_jour y where y.total is false group by art, _code, _nom
		loop
			insert into point_vente_article_by_jour values(data_.ref_art, data_.designation, data_.article, 'TOTAUX', data_.ttc, data_.qte, data_.ttx / j, data_.pos + 1, false, true);
		end loop;
		for data_ in select jr as jour, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(sum(pos), 0) as pos from point_vente_article_by_jour y where y.total is false group by jr
		loop
			insert into point_vente_article_by_jour values('TOTAUX', 'TOTAUX', 0, data_.jour, data_.ttc, data_.qte, data_.ttx, data_.pos + 1, true, true);
		end loop;
	end if;
	return QUERY select * from point_vente_article_by_jour order by pos, art;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_pt_vente(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
  
  
  -- Function: et_total_article_client(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_article_client(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_client(IN agence_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

    current_ bigint default 0;
    
    date_ date;
    
    taux_ double precision default 0;
    
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    
    data_ record;
    prec_ record;
    _client_ record;
    
    i integer default 0;
    j integer default 0;
    
    insert_ boolean default false;    

BEGIN    
	DROP TABLE IF EXISTS client_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS client_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _client_ c.code_client as code, c.nom from yvs_com_client c where c.id = client_;
	if(_client_ is not null)then
		while(date_debut_ <= date_fin_)
		loop
			if(period_ = 'A')then
				date_ = (date_debut_ + interval '1 year' - interval '1 day');
				jour_ = (select extract(year from date_debut_));				
			elsif(period_ = 'T')then
				date_ = (date_debut_ + interval '3 month' - interval '1 day');
				jour_0 = (select extract(month from date_debut_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = '('||jour_0||'/';
				
				jour_0 = (select extract(month from date_));
				if(char_length(jour_0)<2)then
					jour_0 = '0'||jour_0;
				end if;
				jour_ = jour_||jour_0||')';
				
				annee_ = (select extract(year from date_));
				annee_0 = (select extract(year from date_debut_));
				if(annee_ != annee_0)then
					annee_ = '('|| annee_0 || '/'|| annee_ ||')';
				end if;
				jour_ = jour_||'-'||annee_;
				
			elsif(period_ = 'M')then
				date_ = (date_debut_ + interval '1 month' - interval '1 day');
				jour_ = (select extract(month from date_debut_));
				if(char_length(jour_)<2)then
					jour_ = '0'||jour_;
				end if;
				jour_ = jour_||'-'||(select extract(year from date_debut_));
				
			elsif(period_ = 'S')then
				date_ = (date_debut_ + interval '1 week' - interval '1 day');	
				
				jour_0 = (select extract(day from date_debut_));
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
				mois_0 = (select extract(month from date_debut_));
				if(char_length(mois_0)<2)then
					mois_0 = '0'||mois_0;
				end if;
				if(mois_ != mois_0)then
					mois_ = '('|| mois_0 || '/'|| mois_ ||')';
				end if;
				
				jour_ = jour_||mois_||'-'||(select extract(year from date_));	
			else
				date_ = (date_debut_ + interval '0 day');
				jour_ = to_char(date_debut_ ,'dd-MM-yyyy');
			end if;
			
			if(agence_ is null or agence_ < 1)then
				for  data_ in select c.article, a.ref_art, a.designation ,coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte, d.type_doc as typ from yvs_com_contenu_doc_vente c 
					inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc in ('FV', 'FAV') and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V' 
					group by c.article, a.ref_art, a.designation, d.type_doc order by a.ref_art
				loop
					select into prec_ ttc as total from client_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from client_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					if(data_.typ = 'FV')then
						taux_ = data_.total - prec_.total;
					else
						taux_ = data_.total + prec_.total;
					end if;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;

					select into current_ coalesce(count(art), 0) from client_article_by_jour where art = data_.article and jr = jour_;
					if(current_ > 0)then
						if(data_.typ = 'FV')then
							update client_article_by_jour set ttc = ttc + data_.total, qte = qte + data_.qte, ttx = ttx + taux_ where art = data_.article and jr = jour_;
						else
							update client_article_by_jour set ttc = ttc - data_.total, qte = qte - data_.qte, ttx = ttx - taux_ where art = data_.article and jr = jour_;
						end if;
					else
						if(data_.typ = 'FV')then
							insert into client_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
						else
							insert into client_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, - data_.total, - data_.qte, taux_, i, false, false);
						end if;
					end if;	
				end loop;				
			else
				for data_ in select c.article, a.ref_art, a.designation ,coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte, d.type_doc as typ from yvs_com_contenu_doc_vente c 
					inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc in ('FV', 'FAV') and p.agence = agence_ and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V' 
					group by c.article, a.ref_art, a.designation, d.type_doc order by a.ref_art
				loop
					select into prec_ ttc as total from client_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from client_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					if(data_.typ = 'FV')then
						taux_ = data_.total - prec_.total;
					else
						taux_ = data_.total + prec_.total;
					end if;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;

					select into current_ coalesce(count(art), 0) from client_article_by_jour where art = data_.article and jr = jour_;
					if(current_ > 0)then
						if(data_.typ = 'FV')then
							update client_article_by_jour set ttc = ttc + data_.total, qte = qte + data_.qte, ttx = ttx + taux_ where art = data_.article and jr = jour_;
						else
							update client_article_by_jour set ttc = ttc - data_.total, qte = qte - data_.qte, ttx = ttx - taux_ where art = data_.article and jr = jour_;
						end if;
					else
						if(data_.typ = 'FV')then
							insert into client_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
						else
							insert into client_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, - data_.total, - data_.qte, taux_, i, false, false);
						end if;
					end if;	
				end loop;
			end if;
			j = j + 1;
			i = i + 1;
			
			if(period_ = 'A')then
				date_debut_ = date_debut_ + interval '1 year';
			elsif(period_ = 'T')then
				date_debut_ = date_debut_ + interval '3 month';
			elsif(period_ = 'M')then
				date_debut_ = date_debut_ + interval '1 month';
			elsif(period_ = 'S')then
				date_debut_ = date_debut_ + interval '1 week';
			else
				date_debut_ = date_debut_ + interval '1 day';
			end if;
		end loop;
		for data_ in select art as article, _code as ref_art, _nom as designation, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(count(pos), 0) as pos from client_article_by_jour y where y.total is false group by art, _code, _nom
		loop
			insert into client_article_by_jour values(data_.ref_art, data_.designation, data_.article, 'TOTAUX', data_.ttc, data_.qte, data_.ttx / j, data_.pos + 1, false, true);
		end loop;
		for data_ in select jr as jour, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(sum(pos), 0) as pos from client_article_by_jour y where y.total is false group by jr
		loop
			insert into client_article_by_jour values('TOTAUX', 'TOTAUX', 0, data_.jour, data_.ttc, data_.qte, data_.ttx, data_.pos + 1, true, true);
		end loop;
	end if;
	return QUERY select * from client_article_by_jour order by pos, art;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_client(bigint, bigint, date, date, character varying)
  OWNER TO postgres;


  
  -- Function: et_progression_client(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_progression_client(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_progression_client(IN agence_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, annee bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

    date_ date;
    _save_debut_ date;
    
    taux_ double precision default 0;
    ttc_ double precision default 0;
    qte_ double precision default 0;
    
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    
    avoir_ record;
    total_ record;
    prec_ record;
    _client_ record;
    exo_ record;
    
    i integer default 0;
    j integer default 0;
    
    insert_ boolean default false;

BEGIN    
	_save_debut_ = date_debut_;
	DROP TABLE IF EXISTS client_progress_by_annee;
	CREATE TEMP TABLE IF NOT EXISTS client_progress_by_annee(_code character varying, ans bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _client_ c.code_client as code, c.nom from yvs_com_client c where c.id = client_;
	if(_client_ is not null)then
		for exo_ in select e.id, e.reference , e.date_debut, e.date_fin from yvs_base_exercice e inner join yvs_agences a on a.societe = e.societe where a.id = agence_ order by date_fin
		loop
			date_debut_ = (select alter_date(date_debut_, 'year', exo_.date_debut));
			date_fin_ = (select alter_date(date_fin_, 'year', exo_.date_debut));
			insert_ = false;
			j = 0;
			i = 0;
			taux_ = 0;
			
			while(date_debut_ <= date_fin_)
			loop
				if(period_ = 'A')then
					date_ = (date_debut_ + interval '1 year' - interval '1 day');
					jour_ = (select extract(year from date_debut_));					
				elsif(period_ = 'T')then
					date_ = (date_debut_ + interval '3 month' - interval '1 day');
					jour_0 = (select extract(month from date_debut_));
					if(char_length(jour_0)<2)then
						jour_0 = '0'||jour_0;
					end if;
					jour_ = '('||jour_0||'/';
					
					jour_0 = (select extract(month from date_));
					if(char_length(jour_0)<2)then
						jour_0 = '0'||jour_0;
					end if;
					jour_ = jour_||jour_0||')';
					
				elsif(period_ = 'M')then
					date_ = (date_debut_ + interval '1 month' - interval '1 day');
					jour_ = (select extract(month from date_debut_));
					if(char_length(jour_)<2)then
						jour_ = '0'||jour_;
					end if;					
				elsif(period_ = 'S')then
					date_ = (date_debut_ + interval '1 week' - interval '1 day');	
					
					jour_0 = (select extract(day from date_debut_));
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
					mois_0 = (select extract(month from date_debut_));
					if(char_length(mois_0)<2)then
						mois_0 = '0'||mois_0;
					end if;
					if(mois_ != mois_0)then
						mois_ = '('|| mois_0 || '/'|| mois_ ||')';
					end if;
				else
					date_ = (date_debut_ + interval '0 day');
					jour_ = to_char(date_debut_ ,'dd');
				end if;
				
				if(agence_ is null or agence_ < 1)then
					select into total_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id 
						where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V';
						
					select into avoir_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id 
						where d.type_doc = 'FAV' and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V';					
				else
					select into total_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
						where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V';
						
					select into avoir_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
						where d.type_doc = 'FAV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V';
				end if;
				ttc_ = total_.ttc;
				qte_ = total_.qte;
			
				if(avoir_ is not null and avoir_.ttc is not null)then
					ttc_ = ttc_ - avoir_.ttc;
					qte_ = qte_ - avoir_.qte;
				end if;
				select into prec_ ttc from client_progress_by_annee where ans = exo_.id order by rang desc limit 1;
					
				if(total_.ttc != 0)then
					insert_ = true;
				end if;
				if(prec_.ttc = 0)then
					select into prec_ ttc from client_progress_by_annee where ans = exo_.id and ttc > 0 order by rang desc limit 1;
				end if;
				taux_ = ttc_ - prec_.ttc;
				if(taux_ is null)then
					taux_ = 0;
				end if;
				if(taux_ != 0)then
					taux_ = (taux_ / prec_.ttc) * 100;
				end if;
				insert into client_progress_by_annee values(exo_.reference, exo_.id, jour_, ttc_, qte_, taux_, i, false, false);

				j = j + 1;
				i = i + 1;
				
				if(period_ = 'A')then
					date_debut_ = date_debut_ + interval '1 year';
				elsif(period_ = 'T')then
					date_debut_ = date_debut_ + interval '3 month';
				elsif(period_ = 'M')then
					date_debut_ = date_debut_ + interval '1 month';
				elsif(period_ = 'S')then
					date_debut_ = date_debut_ + interval '1 week';
				else
					date_debut_ = date_debut_ + interval '1 day';
				end if;
			end loop;
			date_debut_ = _save_debut_;
		end loop;
		if (j = 0) then
			j = 1;
		end if;
		for total_ in select ans as annee, _code, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(count(pos), 0) as pos from client_progress_by_annee y where y.total is false group by ans, _code
		loop
			insert into client_progress_by_annee values(total_._code, total_.annee, 'TOTAUX', total_.ttc, total_.qte, total_.ttx / j, total_.pos + 1, true, false);
		end loop;
		for total_ in select jr as jour, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(sum(pos), 0) as pos from client_progress_by_annee y where y.total is false group by jr
		loop
			insert into client_progress_by_annee values('TOTAUX', 0, total_.jour, total_.ttc, total_.qte, total_.ttx, total_.pos + 1, true, true);
		end loop;
	end if;
	return QUERY select * from client_progress_by_annee order by footer, ans, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_progression_client(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
