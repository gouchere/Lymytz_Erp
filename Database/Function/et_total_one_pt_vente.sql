-- Function: et_total_one_pt_vente(bigint, character varying, date, date, character varying)
DROP FUNCTION et_total_one_pt_vente(bigint, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_one_pt_vente(IN agence_ bigint, IN point_ character varying, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, point bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer) AS
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
    first_ character varying default 'from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.statut = ''V''';   
    save_ character varying;
    i integer default 1;
    j integer default 0;   

BEGIN    
	DROP TABLE IF EXISTS point_vente_by_jour;
	CREATE TEMP TABLE IF NOT EXISTS point_vente_by_jour(_code character varying, _nom character varying, pt bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer);

	if(agence_ is not null and agence_ > 0)then
		save_ = save_ ||' and p.agence = '||agence_;
	end if;
	FOR _point_ IN SELECT y.id, y.code, y.libelle as nom from yvs_base_point_vente y where y.id::character varying in (select val from regexp_split_to_table(point_,',') val)
	LOOP
		i = 0;
		j = 0;
		query_ = first_ ||' and p.id = '||_point_.id;
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
			valeur_ = (SELECT get_ca_point_vente(_point_.id, dates_.date_debut, dates_.date_fin));
			if(valeur_ IS NULL)then
				valeur_ = 0;
			end if;
			
			select into prec_ ttc from point_vente_by_jour where pt = _point_.id order by rang desc limit 1;
			if(prec_.ttc = 0)then
				select into prec_ ttc from point_vente_by_jour where pt = _point_.id and ttc > 0 order by rang desc limit 1;
			end if;
			taux_ = valeur_ - prec_.ttc;
			if(taux_ is null)then
				taux_ = 0;
			end if;
			if(taux_ != 0)then
				taux_ = (taux_ / prec_.ttc) * 100;
			end if;
			if(valeur_ != 0 or qte_ != 0)then
				insert into point_vente_by_jour values(_point_.code, _point_.nom, _point_.id, jour_, valeur_, qte_, taux_, i);
			end if;
			j = j + 1;			
			i = i + 1;
		end loop;	
	end loop;
	return QUERY select * from point_vente_by_jour order by pt, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_one_pt_vente(bigint, character varying, date, date, character varying)
  OWNER TO postgres;
