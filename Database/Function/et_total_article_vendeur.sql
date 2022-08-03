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
