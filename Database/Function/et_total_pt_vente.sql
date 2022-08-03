-- Function: et_total_pt_vente(bigint, bigint, date, date, character varying, character varying)
DROP FUNCTION et_total_pt_vente(bigint, bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_pt_vente(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN reference_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, point bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer) AS
$BODY$
DECLARE
    points_ bigint;
    total_ record;
    jour_ character varying;
    query_ character varying default 'select distinct y.id from yvs_base_point_vente y inner join yvs_com_creneau_point o on o.point = y.id inner join yvs_com_creneau_horaire_users u on u.creneau_point = o.id inner join yvs_com_entete_doc_vente e on u.id = e.creneau inner join yvs_agences a on y.agence = a.id where y.id is not null';
    nbre integer default 0;

    deja_ BOOLEAN DEFAULT FALSE;
BEGIN    
	DROP TABLE IF EXISTS point_vente_by_jour_;
	CREATE TEMP TABLE IF NOT EXISTS point_vente_by_jour_(_code character varying, _nom character varying, pt bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer);

	if(societe_ is not null and societe_ > 0)then
		query_ = query_ ||' and a.societe = '||societe_;
	end if;	
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ ||' and y.agence = '||agence_;
	end if;	
	if(reference_ is not null and reference_ NOT IN ('', ' '))then
		query_ = query_ ||' AND y.code LIKE ANY (ARRAY[';
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
	for points_ in execute query_
	loop
		insert into point_vente_by_jour_ select * from et_total_one_pt_vente(agence_, CAST(points_ AS character varying), date_debut_, date_fin_, periode_);
	end loop;	
    return QUERY select * from point_vente_by_jour_ order by pt, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_pt_vente(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;
