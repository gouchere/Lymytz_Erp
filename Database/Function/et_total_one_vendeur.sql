-- Function: et_total_one_vendeur(bigint, bigint, date, date, character varying)
DROP FUNCTION et_total_one_vendeur(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_one_vendeur(IN agence_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(code character varying, nom character varying, vendeur bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer) AS
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
	CREATE TEMP TABLE IF NOT EXISTS vendeur_by_jour(_code character varying, _nom character varying, vend bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer);
	select into _vendeur_ id, code_users as code, nom_users as nom from yvs_users where id = vendeur_;
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
			valeur_ = (SELECT get_ca_vendeur(_vendeur_.id, dates_.date_debut, dates_.date_fin));
			if(valeur_ IS NULL)then
				valeur_ = 0;
			end if;
			
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
				insert into vendeur_by_jour values(_vendeur_.code, _vendeur_.nom, vendeur_, jour_, valeur_, qte_, taux_, i);
			end if;
			j = j + 1;			
			i = i + 1;
		end loop;		
	end if;
	return QUERY select * from vendeur_by_jour order by vend, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_one_vendeur(bigint, bigint, date, date, character varying)
  OWNER TO postgres;
