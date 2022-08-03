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
