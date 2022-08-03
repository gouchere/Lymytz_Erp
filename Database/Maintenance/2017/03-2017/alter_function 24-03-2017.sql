-- Function: et_total_article_client(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_article_client(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_client(IN agence_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    date_ date;
    _date_debut_ date;
    _date_fin_ date;
    taux_ double precision default 0;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    data_ record;
    _data_ record;
    i integer default 0;
    j integer default 0;
    insert_ boolean default false;
    _client_ record;

BEGIN    
	DROP TABLE IF EXISTS client_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS client_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _client_ c.code_client as code, c.nom from yvs_com_client c where c.id = client_;
	if(_client_ is not null)then
		if(period_ = 'A')then
			_date_debut_ = date_debut_ - interval '1 year';
		elsif(period_ = 'T')then
			_date_debut_ = date_debut_ - interval '3 month';
		elsif(period_ = 'M')then
			_date_debut_ = date_debut_ - interval '1 month';
		elsif(period_ = 'S')then
			_date_debut_ = date_debut_ - interval '1 week';
		else
			_date_debut_ = date_debut_ - interval '1 day';
		end if;
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
			_date_fin_ = (date_debut_ - interval '1 day');
			
			if(agence_ is null or agence_ < 1)then
				for  data_ in select c.article, a.ref_art, a.designation ,coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V' group by c.article, a.ref_art, a.designation order by a.ref_art
				loop
					select into _data_ coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc = 'FV' and e.date_entete between _date_debut_ and _date_fin_ and d.client = client_ and d.statut = 'V' and c.article = data_.article group by c.article;
					
					if(data_.total != 0)then
						insert_ = true;
						taux_ = data_.total - _data_.total;
						if(taux_ is null)then
							taux_ = 0;
						end if;
						if(_data_.total != 0)then
							if(taux_ != 0)then
								taux_ = (taux_ / _data_.total) * 100;
							end if;
						else
							taux_ = 100;
						end if;
					end if;
					
					insert into client_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
					if (insert_) then
						j = j + 1;
					end if;
				end loop;				
			else
				for data_ in select c.article, a.ref_art, a.designation ,coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V' group by c.article, a.ref_art, a.designation order by a.ref_art
				loop
					select into _data_ coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
						where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between _date_debut_ and _date_fin_ and d.client = client_ and d.statut = 'V' and c.article = data_.article group by c.article;
					
					if(data_.total != 0)then
						insert_ = true;
						taux_ = data_.total - _data_.total;
						if(taux_ is null)then
							taux_ = 0;
						end if;
						if(_data_.total != 0)then
							if(taux_ != 0)then
								taux_ = (taux_ / _data_.total) * 100;
							end if;
						else
							taux_ = 100;
						end if;
					end if;
					insert into client_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
					if (insert_) then
						j = j + 1;
					end if;
				end loop;
			end if;
			i = i + 1;
			
			if(period_ = 'A')then
				date_debut_ = date_debut_ + interval '1 year';
				_date_debut_ = _date_debut_ + interval '1 year';
			elsif(period_ = 'T')then
				date_debut_ = date_debut_ + interval '3 month';
				_date_debut_ = _date_debut_ + interval '3 month';
			elsif(period_ = 'M')then
				date_debut_ = date_debut_ + interval '1 month';
				_date_debut_ = _date_debut_ + interval '1 month';
			elsif(period_ = 'S')then
				date_debut_ = date_debut_ + interval '1 week';
				_date_debut_ = _date_debut_ + interval '1 week';
			else
				date_debut_ = date_debut_ + interval '1 day';
				_date_debut_ = _date_debut_ + interval '1 day';
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

  
  
  -- Function: et_total_article_vendeur(bigint, bigint, date, date, character varying)

DROP FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    date_ date;
    _date_debut_ date;
    _date_fin_ date;
    taux_ double precision default 0;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    data_ record;
    _data_ record;
    i integer default 0;
    j integer default 0;
    insert_ boolean default false;
    _vendeur_ record;

BEGIN    
	DROP TABLE IF EXISTS vendeur_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _vendeur_ code_users as code, nom_users as nom from yvs_users where id = vendeur_;
	if(_vendeur_ is not null)then
		if(period_ = 'A')then
			_date_debut_ = date_debut_ - interval '1 year';
		elsif(period_ = 'T')then
			_date_debut_ = date_debut_ - interval '3 month';
		elsif(period_ = 'M')then
			_date_debut_ = date_debut_ - interval '1 month';
		elsif(period_ = 'S')then
			_date_debut_ = date_debut_ - interval '1 week';
		else
			_date_debut_ = date_debut_ - interval '1 day';
		end if;
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
			_date_fin_ = (date_debut_ - interval '1 day');
			
			if(agence_ is null or agence_ < 1)then
				for  data_ in select c.article, a.ref_art, a.designation, coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V' group by c.article, a.ref_art, a.designation order by a.ref_art
				loop
					select into _data_ coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc = 'FV' and e.date_entete between _date_debut_ and _date_fin_ and u.users = vendeur_ and d.statut = 'V' and c.article = data_.article group by c.article;
					
					if(data_.total != 0)then
						insert_ = true;
						taux_ = data_.total - _data_.total;
						if(taux_ is null)then
							taux_ = 0;
						end if;
						if(_data_.total != 0)then
							if(taux_ != 0)then
								taux_ = (taux_ / _data_.total) * 100;
							end if;
						else
							taux_ = 100;
						end if;
					end if;
					
					insert into vendeur_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
					if (insert_) then
						j = j + 1;
					end if;
				end loop;				
			else
				for data_ in select c.article, a.ref_art, a.designation, coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V' group by c.article, a.ref_art, a.designation order by a.ref_art
				loop
					select into _data_ coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
						where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between _date_debut_ and _date_fin_ and u.users = vendeur_ and d.statut = 'V' and c.article = data_.article group by c.article;
					
					if(data_.total != 0)then
						insert_ = true;
						taux_ = data_.total - _data_.total;
						if(taux_ is null)then
							taux_ = 0;
						end if;
						if(_data_.total != 0)then
							if(taux_ != 0)then
								taux_ = (taux_ / _data_.total) * 100;
							end if;
						else
							taux_ = 100;
						end if;
					end if;
					insert into vendeur_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
					if (insert_) then
						j = j + 1;
					end if;
				end loop;
			end if;
			i = i + 1;
			
			if(period_ = 'A')then
				date_debut_ = date_debut_ + interval '1 year';
				_date_debut_ = _date_debut_ + interval '1 year';
			elsif(period_ = 'T')then
				date_debut_ = date_debut_ + interval '3 month';
				_date_debut_ = _date_debut_ + interval '3 month';
			elsif(period_ = 'M')then
				date_debut_ = date_debut_ + interval '1 month';
				_date_debut_ = _date_debut_ + interval '1 month';
			elsif(period_ = 'S')then
				date_debut_ = date_debut_ + interval '1 week';
				_date_debut_ = _date_debut_ + interval '1 week';
			else
				date_debut_ = date_debut_ + interval '1 day';
				_date_debut_ = _date_debut_ + interval '1 day';
			end if;
		end loop;
		for data_ in select art as article, _code as ref_art, _nom as designation, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(count(pos), 0) as pos from vendeur_article_by_jour y where y.total is false group by art
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

  
  
  -- Function: et_total_one_vendeur(bigint, bigint, date, date, character varying)

DROP FUNCTION et_total_one_vendeur(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_one_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, vendeur bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean) AS
$BODY$
DECLARE
    total_ record;
    _total_ record;
    taux_ double precision default 0;
    date_ date;
    _date_debut_ date;
    _date_fin_ date;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    i integer default 1;
    j integer default 0;
    insert_ boolean default false;
    _vendeur_ record;

BEGIN    
	DROP TABLE IF EXISTS vendeur_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_by_jour(_code character varying, _nom character varying, vend bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, tot boolean);
	select into _vendeur_ code_users as code, nom_users as nom from yvs_users where id = vendeur_;
	if(_vendeur_ is not null)then
		if(period_ = 'A')then
			_date_debut_ = date_debut_ - interval '1 year';
		elsif(period_ = 'T')then
			_date_debut_ = date_debut_ - interval '3 month';
		elsif(period_ = 'M')then
			_date_debut_ = date_debut_ - interval '1 month';
		elsif(period_ = 'S')then
			_date_debut_ = date_debut_ - interval '1 week';
		else
			_date_debut_ = date_debut_ - interval '1 day';
		end if;
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
			_date_fin_ = (date_debut_ - interval '1 day');
			
			if(agence_ is null or agence_ < 1)then
				select into total_ coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V';
					
				select into _total_ coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc = 'FV' and e.date_entete between _date_debut_ and _date_fin_ and u.users = vendeur_ and d.statut = 'V';
				
			else
				select into total_ coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V';
					
				select into _total_ coalesce(sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between _date_debut_ and _date_fin_ and u.users = vendeur_ and d.statut = 'V';
			end if;
			if(total_.ttc != 0)then
				insert_ = true;
				taux_ = total_.ttc - _total_.ttc;
				if(taux_ is null)then
					taux_ = 0;
				end if;
				if(_total_.ttc != 0)then
					if(taux_ != 0)then
						taux_ = (taux_ / _total_.ttc) * 100;
					end if;
				else
					taux_ = 100;
				end if;
			end if;
			insert into vendeur_by_jour values(_vendeur_.code, _vendeur_.nom, vendeur_, jour_, total_.ttc, total_.qte, taux_, i, false);
			if (insert_) then
				j = j + 1;
			end if;
			
			i = i + 1;

			if(period_ = 'A')then
				date_debut_ = date_debut_ + interval '1 year';
				_date_debut_ = _date_debut_ + interval '1 year';
			elsif(period_ = 'T')then
				date_debut_ = date_debut_ + interval '3 month';
				_date_debut_ = _date_debut_ + interval '3 month';
			elsif(period_ = 'M')then
				date_debut_ = date_debut_ + interval '1 month';
				_date_debut_ = _date_debut_ + interval '1 month';
			elsif(period_ = 'S')then
				date_debut_ = date_debut_ + interval '1 week';
				_date_debut_ = _date_debut_ + interval '1 week';
			else
				date_debut_ = date_debut_ + interval '1 day';
				_date_debut_ = _date_debut_ + interval '1 day';
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

  
  
  -- Function: et_total_vendeurs(bigint, date, date, character varying)

DROP FUNCTION et_total_vendeurs(bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_vendeurs(IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, vendeur bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    users_ bigint;
    total_ record;
    nbre integer default 0;

BEGIN    
	DROP TABLE IF EXISTS vendeur_by_jour_;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_by_jour_(_code character varying, _nom character varying, vend bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	if(agence_ is null or agence_ < 1)then
		for users_ in select y.id from yvs_users y where y.id in (select users from yvs_com_creneau_horaire_users where id in (select creneau from yvs_com_entete_doc_vente))
		loop
			insert into vendeur_by_jour_ select *, false from et_total_one_vendeur(null, users_, date_debut_, date_fin_, periode_);
		end loop;

	else
		for users_ in select y.id from yvs_users y where y.id in (select users from yvs_com_creneau_horaire_users where id in (select creneau from yvs_com_entete_doc_vente)) and y.agence = agence_
		loop
			insert into vendeur_by_jour_ select *, false from et_total_one_vendeur(agence_, users_, date_debut_, date_fin_, periode_);
		end loop;
	end if;
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
ALTER FUNCTION et_total_vendeurs(bigint, date, date, character varying)
  OWNER TO postgres;
