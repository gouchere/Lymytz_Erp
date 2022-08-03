--DROP FUNCTION et_total_one_vendeur(bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_one_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(vendeur bigint, jour character varying, total double precision, rang integer) AS
$BODY$
DECLARE
    total_ double precision;
    date_ date;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    i integer default 1;

BEGIN    
	DROP TABLE IF EXISTS vendeur_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_by_jour(vend bigint, jr character varying, ttc double precision, pos integer);
	select into jour_ code_users from yvs_users where id = vendeur_;
	if(jour_ is not null)then
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
				select into total_ sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V';
			else
				select into total_ sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where p.agence = agence_ and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V';
			end if;
			if(total_ is null)then
				total_ = 0;
				end if;
			insert into vendeur_by_jour values(vendeur_, jour_, total_, i);
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
	end if;
	return QUERY select * from vendeur_by_jour order by vend, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_one_vendeur(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
  
  -- Function: et_total_vendeurs(bigint, date, date, character varying)

-- DROP FUNCTION et_total_vendeurs(bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_vendeurs(IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(vendeur bigint, jour character varying, total double precision, rang integer) AS
$BODY$
DECLARE
    users_ bigint;
    total_ record;

BEGIN    
	DROP TABLE IF EXISTS vendeur_by_jour_;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_by_jour_(vend bigint, jr character varying, ttc double precision, pos integer);
	if(agence_ is null or agence_ < 1)then
		for users_ in select y.id from yvs_users y where y.id in (select users from yvs_com_creneau_horaire_users)
		loop
			insert into vendeur_by_jour_ select * from et_total_one_vendeur(null, users_, date_debut_, date_fin_, periode_);
		end loop;

	else
		for users_ in select y.id from yvs_users y where y.id in (select users from yvs_com_creneau_horaire_users) and y.agence = agence_
		loop
			insert into vendeur_by_jour_ select * from et_total_one_vendeur(agence_, users_, date_debut_, date_fin_, periode_);
		end loop;
	end if;
    return QUERY select * from vendeur_by_jour_ order by vend, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_vendeurs(bigint, date, date, character varying)
  OWNER TO postgres;
  
  
  
  
CREATE OR REPLACE FUNCTION et_total_article_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(article bigint, jour character varying, total double precision, rang integer) AS
$BODY$
DECLARE
    date_ date;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;
    data_ record;
    i integer default 0;

BEGIN    
	DROP TABLE IF EXISTS vendeur_article_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_article_by_jour(art bigint, jr character varying, ttc double precision, pos integer);
	select into jour_ code_users from yvs_users where id = vendeur_;
	if(jour_ is not null)then
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
				for  data_ in select c.article, a.ref_art, sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe) as total from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id where e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V' group by c.article, a.ref_art order by a.ref_art
				loop
					if(data_ is not null and data_.total is null)then
						data_.total = 0;
					end if;
					insert into vendeur_article_by_jour values(data_.article, jour_, data_.total, i);
				end loop;
			else
				for data_ in select c.article, a.ref_art, sum((c.quantite * (c.prix - c.rabais)) - c.remise + c.taxe) as total from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where p.agence = agence_ and e.date_entete between date_debut_ and date_ and u.users = vendeur_ and d.statut = 'V' group by c.article, a.ref_art order by a.ref_art
				loop
					if(data_ is not null and data_.total is null)then
						data_.total = 0;
					end if;
					insert into vendeur_article_by_jour values(data_.article, jour_, data_.total, i);
				end loop;
			end if;
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
	end if;
	return QUERY select * from vendeur_article_by_jour order by pos, art;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_article_vendeur(bigint, bigint, date, date, character varying)
  OWNER TO postgres;

