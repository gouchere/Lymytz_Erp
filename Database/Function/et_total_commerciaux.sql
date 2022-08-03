-- Function: et_total_commerciaux(bigint, bigint, date, date, character varying, character varying)
-- DROP FUNCTION et_total_commerciaux(bigint, bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_commerciaux(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN reference_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, commercial bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer) AS
$BODY$
DECLARE
    total_ record;
    prec_ record;
    commercial_ record;
    dates_ RECORD;
    
    taux_ double precision default 0;
    valeur_ double precision default 0;
    qte_ double precision default 0;
    avoir_ double precision default 0;
    
    date_ date; 

    deja_ boolean default false;
    
    jour_ character varying;  
    query_ character varying default 'select co.id, co.code_ref as code, concat(co.nom, '' '', co.prenom) as nom, coalesce(sum(c.quantite), 0) as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					inner join yvs_com_commercial_vente cv on cv.facture = d.id inner join yvs_com_comerciale co on cv.commercial = co.id inner join yvs_agences ag on p.agence = ag.id
					where d.statut = ''V''';    
BEGIN    
	DROP TABLE IF EXISTS table_total_commerciaux;
	CREATE TEMP TABLE IF NOT EXISTS table_total_commerciaux(_code character varying, _nom character varying, _commercial bigint, _jour character varying, _total double precision, _quantite double precision, _taux double precision, _rang integer);
	if(societe_ is not null and societe_ > 0)then
		query_ = query_ ||' and ag.societe = '||societe_;
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ ||' and p.agence = '||agence_;
	end if;
	if(reference_ is not null and reference_ NOT IN ('', ' '))then
		query_ = query_ ||' AND co.code_ref LIKE ANY (ARRAY[';
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
	for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_)
	loop
		jour_ = dates_.intitule;
		for commercial_ in execute query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''')) group by co.id'
		loop
			if(qte_ IS NULL)then
				qte_ = 0;
			end if;
			-- Evaluer le chiffre d'affaire
			valeur_ = (SELECT get_ca_commercial(commercial_.id, dates_.date_debut, dates_.date_fin));
			if(valeur_ IS NULL)then
				valeur_ = 0;
			end if;
			
			select into prec_ _total from table_total_commerciaux where _commercial = commercial_.id order by _rang desc limit 1;
			if(prec_._total = 0)then
				select into prec_ _total from table_total_commerciaux where _commercial = commercial_.id and _total > 0 order by _rang desc limit 1;
			end if;
			taux_ = valeur_ - COALESCE(prec_._total, 0);
			if(taux_ is null)then
				taux_ = 0;
			end if;
			if(taux_ != 0 and valeur_ != 0)then
				taux_ = (taux_ / valeur_) * 100;
			end if;
			if(valeur_ != 0 or qte_ != 0)then
				insert into table_total_commerciaux values(commercial_.code, commercial_.nom, commercial_.id, jour_, valeur_, qte_, taux_, dates_.position);
			end if;
		end loop;
	end loop;	
    return QUERY select * from table_total_commerciaux order by _commercial, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_commerciaux(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;
