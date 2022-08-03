DROP FUNCTION get_cout_total_achat_contenu(bigint);

CREATE OR REPLACE FUNCTION get_cout_sup_vente(id_ bigint, service_ boolean)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	cs_m double precision default 0;
	cs_p double precision default 0;

BEGIN
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id  where doc_vente = id_ and t.augmentation is true and o.service = service_;
	if(cs_p is null)then
		cs_p = 0;
	end if;
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id  where doc_vente = id_ and t.augmentation is false and o.service = service_;
	if(cs_m is null)then
		cs_m = 0;
	end if;
	total_ = cs_p - cs_m;
	if(total_ is null)then
		total_ = 0;
	end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_cout_sup_vente(bigint, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION get_cout_sup_vente(bigint, boolean) IS 'retourne le cout supplementaire de la facture (service ou non)';


CREATE OR REPLACE FUNCTION get_cout_sup_achat(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	cs_m double precision default 0;
	cs_p double precision default 0;

BEGIN
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id  where doc_vente = id_ and t.augmentation is true;
	if(cs_p is null)then
		cs_p = 0;
	end if;
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id  where doc_vente = id_ and t.augmentation is false;
	if(cs_m is null)then
		cs_m = 0;
	end if;
	total_ = cs_p - cs_m;
	if(total_ is null)then
		total_ = 0;
	end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_cout_sup_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_cout_sup_achat(bigint) IS 'retourne le cout supplementaire de la facture (service ou non)';


-- Function: get_ca_vente(bigint)

-- DROP FUNCTION get_ca_vente(bigint);

CREATE OR REPLACE FUNCTION get_ca_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	qte_ double precision default 0;
	cs_ double precision default 0;
	remise_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into total_ sum(prix_total) from yvs_com_contenu_doc_vente where doc_vente = id_;
	if(total_ is null)then
		total_ = 0;
	end if;
	-- Recupere le total des quantitées d'une facture
	select into qte_ sum(quantite) from yvs_com_contenu_doc_vente where doc_vente = id_;
	if(qte_ is null)then
		qte_ = 0;
	end if;

	-- Recupere le total des couts de service supplementaire d'une facture
	cs_ = (select get_cout_sup_vente(id_, true));
	total_ = total_ + cs_;
	
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
ALTER FUNCTION get_ca_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ca_vente(bigint) IS 'retourne le chiffre d''affaire d''un doc vente';

-- Function: get_ttc_vente(bigint)

-- DROP FUNCTION get_ttc_vente(bigint);

CREATE OR REPLACE FUNCTION get_ttc_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	qte_ double precision default 0;
	cs_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le CA d'une facture
	total_ = (select get_ca_vente(id_));
	if(total_ is null)then
		total_ = 0;
	end if;
	
	-- Recupere le total des couts supplementaire d'une facture
	cs_ = (select get_cout_sup_vente(id_, false));
	total_ = total_ + cs_;
	
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


-- Function: get_ttc_achat(bigint)

-- DROP FUNCTION get_ttc_achat(bigint);

CREATE OR REPLACE FUNCTION get_ttc_achat(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	qte_ double precision default 0;
	cs_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into total_ sum(prix_total) from yvs_com_contenu_doc_achat where doc_achat = id_;
	if(total_ is null)then
		total_ = 0;
	end if;
	
	-- Recupere le total des couts supplementaire d'une facture
	cs_ = (select get_cout_sup_achat(id_));
	total_ = total_ + cs_;
		
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ttc_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ttc_achat(bigint) IS 'retourne le montant TTC d''un doc achat';


-- Function: et_total_article_client(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_article_client(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_client(IN agence_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    date_ date;
    
    taux_ double precision default 0;
    
    jour_ character varying;    
    execute_ character varying;   
    query_ character varying;   
    select_ character varying default 'select distinct c.article, a.ref_art, a.designation ';   
    save_ character varying default 'from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.statut = ''V''';
    
    article_ record;
    data_ record;
    avoir_ record;
    prec_ record;
    _client_ record;
    dates_ record;
    
    i integer default 0;
    j integer default 0;
    
    insert_ boolean default false;

BEGIN    
	DROP TABLE IF EXISTS client_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS client_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _client_ c.code_client as code, c.nom from yvs_com_client c where c.id = client_;
	if(_client_.code is not null)then
		if(agence_ is not null and agence_ > 0)then
			save_ = save_ ||' and p.agence = '||agence_||' and d.client = '||client_;
		end if;
		for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, period_)
		loop
			jour_ = dates_.intitule;
			select_ = 'select distinct c.article, a.ref_art, a.designation ';
			
			for article_ in execute select_ || save_ || ' order by a.ref_art'
			loop
				select_ = 'select coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte ';
				query_ = save_ ||' and c.article = '||article_.article;
				
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
					
				select into prec_ ttc as total from client_article_by_jour where art = article_.article order by rang desc limit 1;					
				if(prec_.total = 0)then
					select into prec_ ttc as total from client_article_by_jour where art = article_.article and ttc > 0 order by rang desc limit 1;
				end if;
				taux_ = data_.total - prec_.total;
				if(taux_ is null)then
					taux_ = 0;
				end if;
				if(taux_ != 0)then
					taux_ = (taux_ / prec_.total) * 100;
				end if;
				
				insert into client_article_by_jour values(article_.ref_art, article_.designation, article_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
			end loop;
			j = j + 1;
			i = i + 1;
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
  
  -- Function: et_total_article_pt_vente(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_article_pt_vente(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_pt_vente(IN agence_ bigint, IN point_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    date_ date;
    taux_ double precision default 0;
    
    jour_ character varying;
    execute_ character varying;   
    query_ character varying;   
    select_ character varying default 'select distinct c.article, a.ref_art, a.designation ';   
    save_ character varying default 'from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.statut = ''V''';
    article_ record;
    data_ record;
    avoir_ record;
    prec_ record;
    _point_ record;
    dates_ RECORD;
    
    i integer default 0;
    j integer default 0;
    

BEGIN    
	DROP TABLE IF EXISTS point_vente_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS point_vente_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _point_ y.code, y.libelle as nom from yvs_base_point_vente y where y.id = point_;
	if(_point_ is not null)then
		if(agence_ is not null and agence_ > 0)then
			save_ = save_ ||' and p.agence = '||agence_;
		end if;
		save_ = save_ ||' and p.id = '||point_;
		for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, period_)
		loop
			jour_ = dates_.intitule;
			select_ = 'select distinct c.article, a.ref_art, a.designation ';
			
			for article_ in execute select_ || save_ || ' order by a.ref_art'
			loop				
				select_ = 'select coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte ';
				query_ = save_ ||' and c.article = '||article_.article;
				
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
				
				select into prec_ ttc as total from point_vente_article_by_jour where art = article_.article order by rang desc limit 1;
				if(prec_.total = 0)then
					select into prec_ ttc as total from point_vente_article_by_jour where art = article_.article and ttc > 0 order by rang desc limit 1;
				end if;
				taux_ = data_.total - prec_.total;
				if(taux_ is null)then
					taux_ = 0;
				end if;
				if(taux_ != 0)then
					taux_ = (taux_ / prec_.total) * 100;
				end if;
				
				insert into point_vente_article_by_jour values(article_.ref_art, article_.designation, article_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
			end loop;
			i = i + 1;
			j = j + 1;
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

  -- Function: et_total_article_vendeur(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    date_ date;
    taux_ double precision default 0;
    
    jour_ character varying;
    execute_ character varying;   
    query_ character varying;   
    select_ character varying default 'select distinct c.article, a.ref_art, a.designation ';   
    save_ character varying default 'from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.statut = ''V''';
    article_ record;
    data_ record;
    avoir_ record;
    prec_ record;
    _vendeur_ record;
    dates_ RECORD;
    
    i integer default 0;
    j integer default 0;

BEGIN    
	DROP TABLE IF EXISTS vendeur_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _vendeur_ code_users as code, nom_users as nom from yvs_users where id = vendeur_;
	if(_vendeur_ is not null)then
		if(agence_ is not null and agence_ > 0)then
			save_ = save_ ||' and p.agence = '||agence_;
		end if;
		save_ = save_ ||' and u.users = '||vendeur_;
		for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, period_)
		loop
			jour_ = dates_.intitule;
			select_ = 'select distinct c.article, a.ref_art, a.designation ';			
			
			for article_ in execute select_ || save_ || ' order by a.ref_art'
			loop				
				select_ = 'select coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte ';
				query_ = save_ ||' and c.article = '||article_.article;
				
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
				
				select into prec_ ttc as total from vendeur_article_by_jour where art = article_.article order by rang desc limit 1;
				if(prec_.total = 0)then
					select into prec_ ttc as total from vendeur_article_by_jour where art = article_.article and ttc > 0 order by rang desc limit 1;
				end if;
				taux_ = data_.total - prec_.total;
				if(taux_ is null)then
					taux_ = 0;
				end if;
				if(taux_ != 0)then
					taux_ = (taux_ / prec_.total) * 100;
				end if;
				
				insert into vendeur_article_by_jour values(article_.ref_art, article_.designation, article_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
			end loop;
			i = i + 1;
			j = j + 1;
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
    _taux DOUBLE PRECISION DEFAULT 0;
    
    i INTEGER DEFAULT 0;
    
    jour_ CHARACTER VARYING;
    execute_ CHARACTER VARYING;   
    query_ CHARACTER VARYING;   
    save_ CHARACTER VARYING default 'select coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id 
					inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.statut = ''V''';
    
    data_ RECORD;
    avoir_ RECORD;
    prec_ RECORD;
    _article RECORD;
    dates_ RECORD;
    
BEGIN    
	CREATE TEMP TABLE IF NOT EXISTS table_total_articles(_code_ CHARACTER VARYING, _nom_ CHARACTER VARYING, _article_ BIGINT, _periode_ CHARACTER VARYING, _total_ DOUBLE PRECISION, _quantite_ DOUBLE PRECISION, _taux_ DOUBLE PRECISION, _rang_ INTEGER, _is_total_ BOOLEAN, _is_footer_ BOOLEAN);
	DELETE FROM table_total_articles;
	
	if(agence_ is not null and agence_ > 0)then
		save_ = save_ ||' and p.agence = '||agence_;
	end if;
	FOR _article IN SELECT a.id, a.ref_art, a.designation FROM yvs_base_articles a INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = societe_ ORDER BY a.designation
	LOOP
		date_debut_ = _date_save;
		i = 0;
		_total = 0;
		_quantite = 0;
		_taux = 0;
		query_ = save_ ||' and c.article = '||_article.id;
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

			IF(data_.total != 0)THEN
				INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, jour_, data_.total, data_.qte, taux_, i, FALSE, FALSE);
			END IF;
			i = i + 1;
			_total = _total + data_.total;
			_quantite = _quantite + data_.qte;
			_taux = _taux + taux_;
		END LOOP;
		IF(_total > 0)THEN
			INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, 'TOTAUX', _total, _quantite, (_taux / i), i, TRUE, FALSE);
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

  -- Function: et_total_clients(date, date, boolean, bigint)

-- DROP FUNCTION et_total_clients(date, date, boolean, bigint);

CREATE OR REPLACE FUNCTION et_total_clients(IN date_debut_ date, IN date_fin_ date, IN all_ boolean, IN societe_ bigint)
  RETURNS TABLE(code_client character varying, nom_client character varying, ca double precision, t_croissance double precision, total_facture integer, total_reg double precision, total_impaye double precision) AS
$BODY$
DECLARE
    _temp_ double precision default 0;
    _chiffre_ double precision default 0;
    _day_debut_ date;
    _day_fin_ date;
    
    client_ record;
    chiffre_ double precision default 0;
    nb_fact_ integer default 0;
    taux_ double precision default 0;
    reg_ double precision default 0;
    impyaye_ double precision default 0;
    
BEGIN    
	DROP TABLE IF EXISTS vente_clients;
	CREATE TEMP TABLE IF NOT EXISTS vente_clients(code_client_ character varying, nom_client_ character varying, ca_ double precision, t_croissance_ double precision, total_facture_ integer, total_reg_ double precision, total_impaye_ double precision);

	_day_fin_ = date_debut_ - interval '1 day';
	_day_debut_ = _day_fin_ - ((date_fin_ - date_debut_ ) - 1);
	
	FOR client_ IN SELECT c.* FROM yvs_com_client c INNER JOIN yvs_base_tiers t ON t.id=c.tiers WHERE t.societe=societe_ ORDER BY c.code_client
	LOOP
		--calcule le ca
		chiffre_ = (SELECT SUM(get_ttc_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente en on en.id=d.entete_doc 
				WHERE en.date_entete BETWEEN date_debut_ and date_fin_ and d.type_doc='FV' AND d.statut='V' AND d.client=client_.id);
		IF (chiffre_ IS NULL) THEN
			chiffre_=0;
		END IF;
		_temp_ = (SELECT SUM(get_ttc_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente en on en.id=d.entete_doc 
				WHERE ((en.date_entete BETWEEN date_debut_ and date_fin_ and d.type_doc='FAV') OR (d.date_livraison BETWEEN date_debut_ and date_fin_ and d.type_doc='BRV'))
				AND d.statut='V' AND d.client=client_.id);
		IF (_temp_ IS NULL) THEN
			_temp_=0;
		END IF;
		chiffre_ = chiffre_ - _temp_;
		
		-- evalue le taux croissance sur l'exercice
		_chiffre_ = (SELECT SUM(get_ttc_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente en on en.id=d.entete_doc 
				WHERE en.date_entete BETWEEN _day_debut_ and _day_fin_ and d.type_doc='FV' AND d.statut='V' AND d.client=client_.id);
		IF (_chiffre_ IS NULL) THEN
			_chiffre_= 0;
		END IF;
		_temp_ = (SELECT SUM(get_ttc_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente en on en.id=d.entete_doc 
				WHERE ((en.date_entete BETWEEN _day_debut_ and _day_fin_ and d.type_doc='FAV') OR (d.date_livraison BETWEEN _day_debut_ and _day_fin_ and d.type_doc='BRV'))
				AND d.statut='V' AND d.client=client_.id);
		IF (_temp_ IS NULL) THEN
			_temp_=0;
		END IF;
		_chiffre_ = _chiffre_ - _temp_;
		
		taux_ = chiffre_ - _chiffre_;
		IF (chiffre_ != 0 AND taux_ != 0) THEN
			taux_ = (taux_ / chiffre_) * 100;
		ELSE			
			taux_ = 0;
		END IF;
		
		-- evalue le nb de facture
		nb_fact_ = (SELECT COUNT(*) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente en on en.id=d.entete_doc 
				WHERE d.client=client_.id AND d.type_doc='FV' AND d.statut='V' AND en.date_entete BETWEEN date_debut_ and date_fin_);
		IF (nb_fact_ IS NULL) THEN
			nb_fact_=0;
		END IF;
		-- evalue le total réglé
		reg_= (SELECT SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON d.id=p.vente
			    WHERE (p.date_paiement BETWEEN date_debut_ and date_fin_ ) AND p.statut_piece='P' AND d.client=client_.id);
		IF (reg_ IS NULL) THEN
			reg_=0;
		END IF; 
		IF(not all_)THEN
		   IF(nb_fact_!=0 OR reg_!=0 ) THEN
			INSERT INTO vente_clients VALUES(client_.code_client, client_.nom, chiffre_, taux_, nb_fact_, reg_, (chiffre_-reg_));
		   END IF;
		ELSE
		     INSERT INTO vente_clients VALUES(client_.code_client, client_.nom, chiffre_, taux_, nb_fact_, reg_, (chiffre_-reg_));
		END IF;
	END LOOP;
    return QUERY select * from vente_clients;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_clients(date, date, boolean, bigint)
  OWNER TO postgres;

 
 -- Function: et_total_one_pt_vente(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_one_pt_vente(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_one_pt_vente(IN agence_ bigint, IN point_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, point bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean) AS
$BODY$
DECLARE
    total_ record;
    prec_ record;
    _point_ record;
    dates_ RECORD;
    
    taux_ double precision default 0;
    avoir_ double precision default 0;
    valeur_ double precision default 0;
    qte_ double precision default 0;
    
    date_ date; 
    
    jour_ character varying;
    execute_ character varying;   
    query_ character varying;   
    select_ character varying default 'select coalesce(sum(c.quantite), 0) as qte  ';  
    save_ character varying default 'from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.statut = ''V''';    
    i integer default 1;
    j integer default 0;   

BEGIN    
	DROP TABLE IF EXISTS point_vente_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS point_vente_by_jour(_code character varying, _nom character varying, pt bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, tot boolean);
	select into _point_ y.code, y.libelle as nom from yvs_base_point_vente y where y.id = point_;
	if(_point_ is not null)then
		if(agence_ is not null and agence_ > 0)then
			save_ = save_ ||' and p.agence = '||agence_;
		end if;
		query_ = save_ ||' and p.id = '||point_;
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
			
			select into prec_ ttc from point_vente_by_jour where pt = point_ order by rang desc limit 1;
			if(prec_.ttc = 0)then
				select into prec_ ttc from point_vente_by_jour where pt = point_ and ttc > 0 order by rang desc limit 1;
			end if;
			taux_ = valeur_ - prec_.ttc;
			if(taux_ is null)then
				taux_ = 0;
			end if;
			if(taux_ != 0)then
				taux_ = (taux_ / prec_.ttc) * 100;
			end if;
			if(valeur_ != 0 or qte_ != 0)then
				insert into point_vente_by_jour values(_point_.code, _point_.nom, point_, jour_, valeur_, qte_, taux_, i, false);
			end if;
			j = j + 1;			
			i = i + 1;
		end loop;
		
		select into total_ coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx from point_vente_by_jour;
		if(total_ is null or (total_.ttc = 0 and total_.qte = 0))then
			delete from point_vente_by_jour where pt = point_;
		else
			insert into point_vente_by_jour values(_point_.code, _point_.nom, point_, 'TOTAUX', total_.ttc, total_.qte, total_.ttx / j, i, true);		
		end if;
		
	end if;
	return QUERY select * from point_vente_by_jour order by pt, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_one_pt_vente(bigint, bigint, date, date, character varying)
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
		query_ = save_ ||' and u.users = '||vendeur_;
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
			if(taux_ != 0)then
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
			select into ligne_ qualite, conditionnement, lot from yvs_com_contenu_doc_vente where id = idexterne_;
			operation_='Vente';
		WHEN 'yvs_com_contenu_doc_achat' THEN	
			select into ligne_ qualite, conditionnement, lot from yvs_com_contenu_doc_achat where id = idexterne_;
			operation_='Achat';
		WHEN 'yvs_com_ration' THEN	
			select into ligne_ qualite, conditionnement, lot from yvs_com_ration where id = idexterne_;
			operation_='Ration';
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			select into ligne_ d.type_doc, c.qualite, c.conditionnement, c.lot_sortie as lot FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
			elsif(ligne_.type_doc='TR')then
				if(mouvement_='S') then
					operation_='Reconditionnement';
				else
					select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot from yvs_com_contenu_doc_stock where id = idexterne_;
					operation_='Reconditionnement';
				end if;
			else
				if(mouvement_='S') then
					operation_='Sortie';
				else
					operation_='Entrée';
				end if;
			end if;	
		WHEN 'yvs_com_reconditionnement' THEN	
			if(mouvement_='S') then
				select into ligne_ qualite as qualite, conditionnement as conditionnement, lot as lot from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			else
				select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			end if;	
		WHEN 'yvs_prod_flux_composant' THEN	
			select into ligne_ c.unite as conditionnement, null::integer as qualite, null::integer as lot  from yvs_prod_flux_composant y inner join yvs_prod_composant_of c on y.composant = c.id where y.id = idexterne_;
			if(mouvement_='S') then
				operation_='Consommation';
			else
				operation_='Production';
			end if;
		WHEN 'yvs_prod_declaration_production' THEN	
			select into ligne_ conditionnement, null::integer as qualite, null::integer as lot from yvs_prod_declaration_production where id = idexterne_;
			operation_='Production';
		WHEN 'yvs_prod_contenu_conditionnement' THEN	
			select into ligne_ conditionnement, null::integer as qualite, null::integer as lot from yvs_prod_contenu_conditionnement where id = idexterne_;
			operation_='Conditionnement';
		WHEN 'yvs_prod_fiche_conditionnement' THEN	
			select into ligne_ unite_mesure as conditionnement, null::integer as qualite, null::integer as lot from yvs_prod_fiche_conditionnement f inner join yvs_prod_nomenclature n on f.nomenclature = n.id where f.id = idexterne_;
			operation_='Conditionnement';
		ELSE
			RETURN -1;
	END CASE;
	if(parent_ is not null)then
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, parent_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, parent_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date) IS 'Insert une ligne de sortie de mouvement de stock';


CREATE OR REPLACE FUNCTION delete_fiche_conditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
BEGIN
	if(OLD.statut = 'V') then
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;		
		for cont_ in select id from yvs_prod_contenu_conditionnement where fiche = OLD.id
		loop			
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_fiche_conditionnement()
  OWNER TO postgres;
  
CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_prod_fiche_conditionnement
  FOR EACH ROW
  EXECUTE PROCEDURE delete_fiche_conditionnement();

-- Function: update_fiche_conditionnement()

-- DROP FUNCTION update_fiche_conditionnement();

CREATE OR REPLACE FUNCTION update_fiche_conditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	article_ record;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		select into cont_ * from yvs_prod_nomenclature where id = NEW.nomenclature;
		select into article_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.unite_mesure;			
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME and depot = NEW.depot and article = cont_.article;
		if(mouv_ is not null)then
			if(article_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME and depot = NEW.depot and article = cont_.article;
				result = (select valorisation_stock(cont_.article, NEW.depot, 0, NEW.quantite, article_.prix, TG_TABLE_NAME, NEW.id, 'S', NEW.date_conditionnement));
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = entree_.prix where id_externe = NEW.id and table_externe = TG_TABLE_NAME and depot = NEW.depot and article = cont_.article and mouvement = 'S';
			end if;
		else
			result = (select valorisation_stock(cont_.article, NEW.depot, 0, NEW.quantite, article_.prix, TG_TABLE_NAME, NEW.id, 'S', NEW.date_conditionnement));
		end if;	
		
		for cont_ in select id, article , quantite, conditionnement, consommable from yvs_prod_contenu_conditionnement where fiche = OLD.id
		loop
			select into article_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.conditionnement;			
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot = NEW.depot and article = cont_.article;
			if(mouv_ is not null)then
				if(article_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot = NEW.depot and article = cont_.article;
					if(cont_.consommable)then
						result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'S', NEW.date_conditionnement));
					else
						result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'E', NEW.date_conditionnement));
					end if;
				else
					if(cont_.consommable)then
						update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = entree_.prix where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot = NEW.depot and article = cont_.article and mouvement = 'S';
					else
						update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = entree_.prix where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement' and depot = NEW.depot and article = cont_.article and mouvement = 'E';
					end if;
				end if;
			else
				if(cont_.consommable)then
					result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'S', NEW.date_conditionnement));
				else
					result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_prod_contenu_conditionnement', cont_.id, 'E', NEW.date_conditionnement));
				end if;
			end if;	
		end loop;
	elsif(NEW.statut != 'V')then
		delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME;
		for cont_ in select id from yvs_prod_contenu_conditionnement where fiche = OLD.id
		loop				
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_prod_contenu_conditionnement';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_fiche_conditionnement()
  OWNER TO postgres;

CREATE TRIGGER update_
  BEFORE UPDATE
  ON yvs_prod_fiche_conditionnement
  FOR EACH ROW
  EXECUTE PROCEDURE update_fiche_conditionnement();
  
  
CREATE OR REPLACE FUNCTION delete_contenu_conditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	mouv_ bigint;
BEGIN
	--Insertion mouvement stock
	delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_contenu_conditionnement()
  OWNER TO postgres;
  
CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_prod_contenu_conditionnement
  FOR EACH ROW
  EXECUTE PROCEDURE delete_contenu_conditionnement();
  
  
CREATE OR REPLACE FUNCTION insert_contenu_conditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	entree_ record;
	sortie_ record;
	arts_ record;
	result boolean default false;
BEGIN
	select into doc_ id, statut, depot, date_conditionnement from yvs_prod_fiche_conditionnement where id = NEW.fiche;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		if(doc_.depot is not null)then
			result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_conditionnement));
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_conditionnement()
  OWNER TO postgres;
  
CREATE TRIGGER insert_
  AFTER INSERT
  ON yvs_prod_contenu_conditionnement
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_conditionnement();
  
  -- Function: update_ration()

-- DROP FUNCTION update_ration();

CREATE OR REPLACE FUNCTION update_contenu_conditionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	article_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, statut, depot, date_conditionnement from yvs_prod_fiche_conditionnement where id = NEW.fiche;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		select into article_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement;
		if(doc_.depot is not null)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_conditionnement));
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = article_.prix, conditionnement = NEW.conditionnement where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'S';
				end if;
			else
				result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_conditionnement));
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_conditionnement()
  OWNER TO postgres;


CREATE TRIGGER update_
  BEFORE UPDATE
  ON yvs_prod_contenu_conditionnement
  FOR EACH ROW
  EXECUTE PROCEDURE update_contenu_conditionnement();