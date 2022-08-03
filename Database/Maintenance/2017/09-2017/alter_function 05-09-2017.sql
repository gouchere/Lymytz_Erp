-- Function: et_progression_client(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_progression_client(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION decoupage_interval_date(IN date_entree_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(intitule character varying, date_sortie date) AS
$BODY$
DECLARE

    date_ date;

    intitule_ character varying;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;

BEGIN    
-- 	DROP TABLE IF EXISTS table_decoupage_interval_date;
	CREATE TEMP TABLE IF NOT EXISTS table_decoupage_interval_date(_intitule character varying, _date_sortie date);
	DELETE FROM table_decoupage_interval_date;
	if(periode_ = 'A')then
		date_ = (date_entree_ + interval '1 year' - interval '1 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		intitule_ = (select extract(year from date_entree_));	
	elsif(periode_ = 'T')then
		date_ = (date_entree_ + interval '3 month' - interval '1 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		jour_0 = (select extract(month from date_entree_));
		if(char_length(jour_0)<2)then
			jour_0 = '0'||jour_0;
		end if;
		jour_ = '('||jour_0||'/';
		
		jour_0 = (select extract(month from date_));
		if(char_length(jour_0)<2)then
			jour_0 = '0'||jour_0;
		end if;
		intitule_ = jour_||jour_0||')';		
	elsif(periode_ = 'M')then
		date_ = (date_entree_ + interval '1 month' - interval '1 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		intitule_ = (select extract(month from date_entree_));
		if(char_length(jour_)<2)then
			jour_ = '0'||jour_;
		end if;			
	elsif(periode_ = 'S')then
		date_ = (date_entree_ + interval '1 week' - interval '1 day');	
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;
		
		jour_0 = (select extract(day from date_entree_));
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
		mois_0 = (select extract(month from date_entree_));
		if(char_length(mois_0)<2)then
			mois_0 = '0'||mois_0;
		end if;
		if(mois_ != mois_0)then
			mois_ = '('|| mois_0 || '/'|| mois_ ||')';
		end if;
		intitule_ = jour_ || mois_;
	else
		date_ = (date_entree_ + interval '0 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		intitule_ = to_char(date_entree_ ,'dd');
	end if;
	INSERT INTO table_decoupage_interval_date VALUES(intitule_, date_);
	
	return QUERY select * from table_decoupage_interval_date;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION decoupage_interval_date(date, date, character varying)
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
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    total_ record;
    prec_ record;
    i integer default 0;
    j integer default 0;
    insert_ boolean default false;
    _client_ record;
    exo_ record;

    dates_ RECORD;

BEGIN    
	_save_debut_ = date_debut_;
	DROP TABLE IF EXISTS client_progress_by_annee;
	CREATE TEMP TABLE IF NOT EXISTS client_progress_by_annee(_code character varying, ans bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _client_ c.code_client as code, c.nom from yvs_com_client c where c.id = client_;
	if(_client_ is not null)then
		for exo_ in select e.id, e.reference , e.date_debut, e.date_fin from yvs_base_exercice e inner join yvs_agences a on a.societe = e.societe where a.id = agence_ order by date_fin
		loop
			RAISE NOTICE 'exo_ %',exo_.reference;
			date_debut_ = (select alter_date(date_debut_, 'year', exo_.date_debut));
			date_fin_ = (select alter_date(date_fin_, 'year', exo_.date_debut));
			insert_ = false;
			j = 0;
			i = 0;
			taux_ = 0;
			
			while(date_debut_ <= date_fin_)
			loop
				SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, period_);
				IF(dates_.date_sortie IS NOT NULL)THEN
					date_ = dates_.date_sortie;
					jour_ = dates_.intitule;
				END IF;
				RAISE NOTICE 'date_ % %', jour_,date_;
				
				if(agence_ is null or agence_ < 1)then
					select into total_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V';
					
				else
					select into total_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
						inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
						where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V';
				end if;
				select into prec_ ttc from client_progress_by_annee where ans = exo_.id order by rang desc limit 1;
					
				if(total_.ttc != 0)then
					insert_ = true;
				end if;
				if(prec_.ttc = 0)then
					select into prec_ ttc from client_progress_by_annee where ans = exo_.id and ttc > 0 order by rang desc limit 1;
				end if;
				taux_ = total_.ttc - prec_.ttc;
				if(taux_ is null)then
					taux_ = 0;
				end if;
				if(taux_ != 0)then
					taux_ = (taux_ / prec_.ttc) * 100;
				end if;
				insert into client_progress_by_annee values(exo_.reference, exo_.id, jour_, total_.ttc, total_.qte, taux_, i, false, false);

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

  
  -- Function: et_total_article_client(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_article_client(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_client(IN agence_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
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
    i integer default 0;
    j integer default 0;
    insert_ boolean default false;
    _client_ record;

    dates_ RECORD;

BEGIN    
	DROP TABLE IF EXISTS client_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS client_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _client_ c.code_client as code, c.nom from yvs_com_client c where c.id = client_;
	if(_client_ is not null)then
		while(date_debut_ <= date_fin_)
		loop
			SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, period_);
			IF(dates_.date_sortie IS NOT NULL)THEN
				date_ = dates_.date_sortie;
				jour_ = dates_.intitule;
			END IF;
			RAISE NOTICE 'date_ % %', jour_,date_;
			
			if(agence_ is null or agence_ < 1)then
				for  data_ in select c.article, a.ref_art, a.designation ,coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V' group by c.article, a.ref_art, a.designation order by a.ref_art
				loop
					select into prec_ ttc as total from client_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from client_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					taux_ = data_.total - prec_.total;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;
					
					insert into client_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
				end loop;				
			else
				for data_ in select c.article, a.ref_art, a.designation ,coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and d.client = client_ and d.statut = 'V' group by c.article, a.ref_art, a.designation order by a.ref_art
				loop
					select into prec_ ttc as total from client_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from client_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					taux_ = data_.total - prec_.total;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;
					
					insert into client_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
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

  
  -- Function: et_total_article_pt_vente(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_article_pt_vente(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_pt_vente(IN agence_ bigint, IN point_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
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
    i integer default 0;
    j integer default 0;
    insert_ boolean default false;
    _point_ record;

    dates_ RECORD;

BEGIN    
	DROP TABLE IF EXISTS point_vente_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS point_vente_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _point_ y.code, y.libelle as nom from yvs_base_point_vente y where y.id = point_;
	if(_point_ is not null)then
		while(date_debut_ <= date_fin_)
		loop
			SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, period_);
			IF(dates_.date_sortie IS NOT NULL)THEN
				date_ = dates_.date_sortie;
				jour_ = dates_.intitule;
			END IF;
			RAISE NOTICE 'date_ % %', jour_,date_;
			
			if(agence_ is null or agence_ < 1)then
				for  data_ in select c.article, a.ref_art, a.designation, coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id
					where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and o.point = point_ and d.statut = 'V' group by c.article, a.ref_art, a.designation order by a.ref_art
				loop
					
					select into prec_ ttc as total from point_vente_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from point_vente_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					taux_ = data_.total - prec_.total;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;
					
					insert into point_vente_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
				end loop;				
			else
				for data_ in select c.article, a.ref_art, a.designation, coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and o.point = point_ and d.statut = 'V' group by c.article, a.ref_art, a.designation order by a.ref_art
				loop
					select into prec_ ttc as total from point_vente_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from point_vente_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					taux_ = data_.total - prec_.total;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;
					insert into point_vente_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
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

  
  -- Function: et_total_article_vendeur(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_article_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
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
    i integer default 0;
    j integer default 0;
    insert_ boolean default false;
    _vendeur_ record;

    dates_ RECORD;

BEGIN    
	DROP TABLE IF EXISTS vendeur_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_article_by_jour(_code character varying, _nom character varying, art bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	select into _vendeur_ code_users as code, nom_users as nom from yvs_users where id = vendeur_;
	if(_vendeur_ is not null)then
		while(date_debut_ <= date_fin_)
		loop
			SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, period_);
			IF(dates_.date_sortie IS NOT NULL)THEN
				date_ = dates_.date_sortie;
				jour_ = dates_.intitule;
			END IF;
			RAISE NOTICE 'date_ % %', jour_,date_;	
			
			if(agence_ is null or agence_ < 1)then
				for  data_ in select c.article, a.ref_art, a.designation, coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V' group by c.article, a.ref_art, a.designation order by a.ref_art
				loop
					
					select into prec_ ttc as total from vendeur_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from vendeur_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					taux_ = data_.total - prec_.total;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;
					
					insert into vendeur_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
				end loop;				
			else
				for data_ in select c.article, a.ref_art, a.designation, coalesce(sum(c.prix_total), 0) as total, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V' group by c.article, a.ref_art, a.designation order by a.ref_art
				loop
					select into prec_ ttc as total from vendeur_article_by_jour where art = data_.article order by rang desc limit 1;
					
					if(data_.total != 0)then
						insert_ = true;
					end if;
					if(prec_.total = 0)then
						select into prec_ ttc as total from vendeur_article_by_jour where art = data_.article and ttc > 0 order by rang desc limit 1;
					end if;
					taux_ = data_.total - prec_.total;
					if(taux_ is null)then
						taux_ = 0;
					end if;
					if(taux_ != 0)then
						taux_ = (taux_ / prec_.total) * 100;
					end if;
					insert into vendeur_article_by_jour values(data_.ref_art, data_.designation, data_.article, jour_, data_.total, data_.qte, taux_, i, false, false);
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
    jour_0 CHARACTER VARYING;
    mois_ CHARACTER VARYING;
    mois_0 CHARACTER VARYING;
    annee_ CHARACTER VARYING;
    annee_0 CHARACTER VARYING;
    
    data_ RECORD;
    prec_ RECORD;
    _article RECORD;

    dates_ RECORD;
    
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
			SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, periode_);
			IF(dates_.date_sortie IS NOT NULL)THEN
				date_ = dates_.date_sortie;
				jour_ = dates_.intitule;
			END IF;
			RAISE NOTICE 'date_ % %', jour_,date_;
			

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
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying)
  OWNER TO postgres;

  
  -- Function: et_total_one_pt_vente(bigint, bigint, date, date, character varying)

-- DROP FUNCTION et_total_one_pt_vente(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_one_pt_vente(IN agence_ bigint, IN point_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, point bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean) AS
$BODY$
DECLARE
    total_ record;
    prec_ record;
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
    _point_ record;

    dates_ RECORD;

BEGIN    
	DROP TABLE IF EXISTS point_vente_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS point_vente_by_jour(_code character varying, _nom character varying, pt bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, tot boolean);
	select into _point_ y.code, y.libelle as nom from yvs_base_point_vente y where y.id = point_;
	if(_point_ is not null)then
		while(date_debut_ <= date_fin_)
		loop
			SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, period_);
			IF(dates_.date_sortie IS NOT NULL)THEN
				date_ = dates_.date_sortie;
				jour_ = dates_.intitule;
			END IF;
			RAISE NOTICE 'date_ % %', jour_,date_;
			
			if(agence_ is null or agence_ < 1)then
				select into total_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id
					where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and o.point = point_ and d.statut = 'V';
				
			else
				select into total_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and o.point = point_ and d.statut = 'V';
			end if;
			select into prec_ ttc from point_vente_by_jour where pt = point_ order by rang desc limit 1;
					
			if(total_.ttc != 0)then
				insert_ = true;
			end if;
			if(prec_.ttc = 0)then
				select into prec_ ttc from point_vente_by_jour where pt = point_ and ttc > 0 order by rang desc limit 1;
			end if;
			taux_ = total_.ttc - prec_.ttc;
			if(taux_ is null)then
				taux_ = 0;
			end if;
			if(taux_ != 0)then
				taux_ = (taux_ / prec_.ttc) * 100;
			end if;
			insert into point_vente_by_jour values(_point_.code, _point_.nom, point_, jour_, total_.ttc, total_.qte, taux_, i, false);
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
    _vendeur_ record;

    dates_ RECORD;

BEGIN    
	DROP TABLE IF EXISTS vendeur_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_by_jour(_code character varying, _nom character varying, vend bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, tot boolean);
	select into _vendeur_ code_users as code, nom_users as nom from yvs_users where id = vendeur_;
	if(_vendeur_ is not null)then
		while(date_debut_ <= date_fin_)
		loop
			SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, period_);
			IF(dates_.date_sortie IS NOT NULL)THEN
				date_ = dates_.date_sortie;
				jour_ = dates_.intitule;
			END IF;
			RAISE NOTICE 'date_ % %', jour_,date_;
			
			if(agence_ is null or agence_ < 1)then
				select into total_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where d.type_doc = 'FV' and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V';
				
			else
				select into total_ coalesce(sum(c.prix_total), 0) as ttc, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.type_doc = 'FV' and p.agence = agence_ and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V';
			end if;
			select into prec_ ttc from vendeur_by_jour where vend = vendeur_ order by rang desc limit 1;
					
			if(total_.ttc != 0)then
				insert_ = true;
			end if;
			if(prec_.ttc = 0)then
				select into prec_ ttc from vendeur_by_jour where vend = vendeur_ and ttc > 0 order by rang desc limit 1;
			end if;
			taux_ = total_.ttc - prec_.ttc;
			if(taux_ is null)then
				taux_ = 0;
			end if;
			if(taux_ != 0)then
				taux_ = (taux_ / prec_.ttc) * 100;
			end if;
			insert into vendeur_by_jour values(_vendeur_.code, _vendeur_.nom, vendeur_, jour_, total_.ttc, total_.qte, taux_, i, false);
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

  
  -- Function: grh_et_fiche_individuel(bigint, date, date, character varying)

-- DROP FUNCTION grh_et_fiche_individuel(bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION grh_et_fiche_individuel(IN employe_ bigint, IN debut_ date, IN fin_ date, IN periode_ character varying)
  RETURNS TABLE(element bigint, numero integer, libelle character varying, groupe bigint, entete character varying, montant double precision, rang integer, is_group boolean, is_total boolean) AS
$BODY$
declare 
	titre_ character varying default 'LIVRE_PAIE';

	date_save date default debut_;
	date_ date;
	taux_ double precision default 0;
	jour_ character varying;
	jour_0 character varying;
	mois_ character varying;
	mois_0 character varying;
	annee_ character varying;
	annee_0 character varying;
    
	element_ record;
	second_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
	is_total_ boolean default false;
	societe_ bigint;

    dates_ RECORD;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_fiche_individuel;
	CREATE TEMP TABLE IF NOT EXISTS table_fiche_individuel(_element bigint, _numero integer, _libelle character varying, _groupe bigint, _entete character varying, _montant double precision, _rang integer, _is_group boolean, _is_total boolean);
	delete from table_fiche_individuel;
	select into societe_ societe  from yvs_agences a inner join yvs_grh_employes e on e.agence = a.id where e.id = employe_;
	for element_ in select e.element_salaire as id, e.libelle, coalesce(y.num_sequence, 0) as numero, coalesce(e.ordre, 0) as ordre, y.retenue, e.groupe_element as groupe from yvs_stat_grh_element_dipe e 
	inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
	loop
		somme_ = 0;
		debut_ = date_save;
		while(debut_ <= fin_)
		loop
			SELECT INTO dates_ * FROM decoupage_interval_date(debut_, fin_, periode_);
			IF(dates_.date_sortie IS NOT NULL)THEN
				date_ = dates_.date_sortie;
				jour_ = dates_.intitule;
			END IF;
			RAISE NOTICE 'date_ % %', jour_,date_;

			if(element_.retenue is false)then
				select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_ordre_calcul_salaire o on b.entete = o.id
					where d.element_salaire = element_.id and c.employe = employe_ and o.debut_mois >= debut_ and o.fin_mois <= date_;
			else
				select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_ordre_calcul_salaire o on b.entete = o.id
					where d.element_salaire = element_.id and c.employe = employe_ and o.debut_mois >= debut_ and o.fin_mois <= date_;
			end if;
			insert into table_fiche_individuel values(element_.id, element_.numero, element_.libelle, element_.groupe, jour_, coalesce(valeur_, 0), element_.ordre, false, false);
			somme_ = somme_ + coalesce(valeur_, 0);
			
			if(periode_ = 'A')then
				debut_ = debut_ + interval '1 year';
			elsif(periode_ = 'T')then
				debut_ = debut_ + interval '3 month';
			elsif(periode_ = 'M')then
				debut_ = debut_ + interval '1 month';
			elsif(periode_ = 'S')then
				debut_ = debut_ + interval '1 week';
			else
				debut_ = debut_ + interval '1 day';
			end if;
		end loop;
		if(somme_ = 0)then
			delete from table_fiche_individuel where _element = element_.id;
		end if;
	end loop;
	return QUERY select * from table_fiche_individuel order by _is_total, _rang, _element;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_fiche_individuel(bigint, date, date, character varying)
  OWNER TO postgres;

  
  -- Function: grh_et_progression_all(bigint, bigint, bigint, bigint, date, date, character varying)

-- DROP FUNCTION grh_et_progression_all(bigint, bigint, bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION grh_et_progression_all(IN societe_ bigint, IN agence_ bigint, IN service_ bigint, IN employe_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(agence bigint, employe bigint, code character varying, nom character varying, periode character varying, rang integer, salaire double precision, presence double precision, conge double precision, permission double precision, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE 
	_connexion_ CHARACTER VARYING DEFAULT 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
	_query_ CHARACTER VARYING DEFAULT '';
	
	_employe_ RECORD;
	_data_ RECORD;
	
	_salaire_ DOUBLE PRECISION DEFAULT 0;
	_presence_ DOUBLE PRECISION DEFAULT 0;
	_conge_ DOUBLE PRECISION DEFAULT 0;
	_permission_ DOUBLE PRECISION DEFAULT 0;
	_sum_salaire_ DOUBLE PRECISION DEFAULT 0;
	_sum_presence_ DOUBLE PRECISION DEFAULT 0;
	_sum_conge_ DOUBLE PRECISION DEFAULT 0;
	_sum_permission_ DOUBLE PRECISION DEFAULT 0;
	
	_date_save_ DATE DEFAULT date_debut_;
	_date_fin_ DATE;
	_periode_ CHARACTER VARYING;
	_jour_ character varying;
	_mois_ character varying;
	_mois_0_ character varying;
	_annee_ character varying;
	_annee_0_ character varying;
	
	i INTEGER DEFAULT 0;

    dates_ RECORD;
BEGIN 	
	--DROP TABLE IF EXISTS grh_recap_presence;
	CREATE TEMP TABLE IF NOT EXISTS grh_table_progression_all(_agence BIGINT, _employe BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode CHARACTER VARYING, _rang INTEGER, _salaire DOUBLE PRECISION, _presence DOUBLE PRECISION, _conge DOUBLE PRECISION, _permission DOUBLE PRECISION, _is_total BOOLEAN, _is_footer BOOLEAN); 
	DELETE FROM grh_table_progression_all;
	IF(employe_ IS NOT NULL AND employe_ > 0)THEN
		_query_ = 'SELECT e.id, e.nom, e.prenom, e.matricule, e.agence FROM yvs_grh_employes e INNER JOIN yvs_agences a ON a.id = e.agence WHERE e.id = '||employe_||' ORDER BY e.nom';
	ELSIF(service_ IS NOT NULL AND service_ > 0)THEN
		_query_ = 'SELECT e.id, e.nom, e.prenom, e.matricule, e.agence FROM yvs_grh_employes e INNER JOIN yvs_grh_poste_de_travail p ON p.id = e.poste_actif WHERE p.departement = '||service_||' ORDER BY e.nom';
	ELSIF(agence_ IS NOT NULL AND agence_ > 0)THEN
		_query_ = 'SELECT e.id, e.nom, e.prenom, e.matricule, e.agence FROM yvs_grh_employes e INNER JOIN yvs_agences a ON a.id = e.agence WHERE e.agence = '||agence_||' ORDER BY e.nom';
	ELSE
		_query_ = 'SELECT e.id, e.nom, e.prenom, e.matricule, e.agence FROM yvs_grh_employes e INNER JOIN yvs_agences a ON a.id = e.agence WHERE a.societe = '||societe_||' ORDER BY a.designation, e.nom';
	END IF;
	
	RAISE NOTICE 'query %',_query_;
	IF(_query_ IS NOT NULL AND _query_ != '')THEN
		FOR _employe_ IN SELECT * FROM dblink(_connexion_, _query_) AS t(id BIGINT, nom CHARACTER VARYING, prenom CHARACTER VARYING, matricule CHARACTER VARYING, agence BIGINT)
		LOOP
			date_debut_ = _date_save_; i = 0;
			_sum_salaire_ = 0; _sum_presence_ = 0; _sum_conge_ = 0; _sum_permission_ = 0;
			WHILE(date_debut_ <= date_fin_)
			LOOP
				_salaire_ = 0; _presence_ = 0; _conge_ = 0; _permission_ = 0;
				SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, periode_);
				IF(dates_.date_sortie IS NOT NULL)THEN
					_date_fin_ = dates_.date_sortie;
					_periode_ = dates_.intitule;
				END IF;
				RAISE NOTICE 'date_ % %', _periode_,_date_fin_;
				

				-- Recuperation des informations de presence de l'employé dans l'interval de dates
				FOR _data_ IN SELECT * FROM grh_presence_durees(_employe_.id, _employe_.agence, societe_, date_debut_, _date_fin_)
				LOOP
					IF(_data_.element = 'Je')THEN
						_presence_ = _data_.valeur;
					ELSIF(_data_.element = 'Ca')THEN
						_conge_ = _data_.valeur;
					ELSIF(_data_.element = 'Pl')THEN
						_permission_ = _data_.valeur;
					ELSE
						
					END IF;
				END LOOP;
				--Calcul de la masse salarial de l'employe dans l'interval de dates
				SELECT INTO _salaire_ COALESCE(SUM(y.montant_payer - y.retenu_salariale), 0) FROM Yvs_grh_detail_bulletin y INNER JOIN yvs_grh_element_salaire s ON y.element_salaire = s.id 
				INNER JOIN yvs_grh_bulletins b ON y.bulletin = b.id INNER JOIN yvs_grh_ordre_calcul_salaire o ON b.entete = o.id
				INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id INNER JOIN yvs_grh_employes e ON c.employe = e.id
				WHERE c.actif AND s.visible_bulletin = true AND y.now_visible = true AND c.employe = _employe_.id AND 
					(y.montant_employeur != 0 OR y.montant_payer != 0 OR y.retenu_salariale != 0) 
					AND ((o.debut_mois BETWEEN date_debut_ AND _date_fin_) AND (o.fin_mois BETWEEN date_debut_ AND _date_fin_));
					
				_sum_salaire_ = _sum_salaire_ + _salaire_; 
				_sum_presence_ = _sum_presence_ + _presence_; 
				_sum_conge_ = _sum_conge_ + _conge_; 
				_sum_permission_ = _sum_permission_ + _permission_;
					
				INSERT INTO grh_table_progression_all VALUES(_employe_.agence, _employe_.id, _employe_.matricule, TRIM(CONCAT(_employe_.prenom, ' ', _employe_.nom)), _periode_, i, COALESCE(_salaire_, 0), COALESCE(_presence_, 0), COALESCE(_conge_, 0), COALESCE(_permission_, 0), FALSE, FALSE);
				i = i + 1;
				
				IF(periode_ = 'A')THEN
					date_debut_ = date_debut_ + INTERVAL '1 YEAR';
				ELSIF(periode_ = 'T')THEN
					date_debut_ = date_debut_ + INTERVAL '3 MONTH';
				ELSIF(periode_ = 'M')THEN
					date_debut_ = date_debut_ + INTERVAL '1 MONTH';
				ELSIF(periode_ = 'S')THEN
					date_debut_ = date_debut_ + INTERVAL '1 week';
				ELSE
					date_debut_ = date_debut_ + INTERVAL '1 DAY';
				END IF;
			END LOOP;	
			INSERT INTO grh_table_progression_all VALUES(_employe_.agence, _employe_.id, _employe_.matricule, TRIM(CONCAT(_employe_.prenom, ' ', _employe_.nom)), 'TOTAUX', i, COALESCE(_sum_salaire_, 0), COALESCE(_sum_presence_, 0), COALESCE(_conge_, 0), COALESCE(_sum_permission_, 0), TRUE, FALSE);
		END LOOP;
		
		FOR _data_ IN SELECT _agence, a.codeagence, a.designation, _periode, _rang, COALESCE(SUM(_salaire), 0) AS _salaire, COALESCE(SUM(_presence), 0) AS _presence, COALESCE(SUM(_conge), 0) AS _conge, COALESCE(SUM(_permission), 0) AS _permission, _is_total FROM grh_table_progression_all 
			INNER JOIN yvs_agences a ON _agence = a.id GROUP BY _agence, a.codeagence, a.designation, _periode, _rang, _is_total
		LOOP
			INSERT INTO grh_table_progression_all VALUES(_data_._agence, 0, _data_.codeagence, _data_.designation, _data_._periode, _data_._rang, COALESCE(_data_._salaire, 0), COALESCE(_data_._presence, 0), COALESCE(_data_._conge, 0), COALESCE(_data_._permission, 0), _data_._is_total, FALSE);
		END LOOP;
		FOR _data_ IN SELECT _periode, _rang, COALESCE(SUM(_salaire), 0) AS _salaire, COALESCE(SUM(_presence), 0) AS _presence, COALESCE(SUM(_conge), 0) AS _conge, COALESCE(SUM(_permission), 0) AS _permission FROM grh_table_progression_all WHERE _employe = 0 GROUP BY _periode, _rang
		LOOP
			INSERT INTO grh_table_progression_all VALUES(0, 0, 'TOTAUX', 'TOTAUX', _data_._periode, _data_._rang, COALESCE(_data_._salaire, 0), COALESCE(_data_._presence, 0), COALESCE(_data_._conge, 0), COALESCE(_data_._permission, 0), TRUE, TRUE);
		END LOOP;
	END IF;
	return QUERY SELECT * FROM grh_table_progression_all order by _is_footer, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_progression_all(bigint, bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
