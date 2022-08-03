-- Function: et_total_vendeurs(bigint, bigint, date, date, character varying, character varying)
DROP FUNCTION et_total_vendeurs(bigint, bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_vendeurs(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN reference_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, vendeur bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer) AS
$BODY$
DECLARE
    users_ record;
    total_ record;
    nbre integer default 0;

    query_ character varying default 'select y.id, y.agence from yvs_users y inner join yvs_agences a on a.id = y.agence where y.id in (select users from yvs_com_creneau_horaire_users where id in (select creneau from yvs_com_entete_doc_vente))';
    jour_ character varying;
    
    deja_ BOOLEAN DEFAULT FALSE;
BEGIN    
	DROP TABLE IF EXISTS vendeur_by_jour_;
	CREATE TEMP TABLE IF NOT EXISTS vendeur_by_jour_(_code character varying, _nom character varying, vend bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer);
	if(societe_ is not null and societe_ > 0)then
		query_ = query_ ||' and a.societe = '||societe_;
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ ||' and y.agence = '||agence_;
	end if;
	if(reference_ is not null and reference_ NOT IN ('', ' '))then
		query_ = query_ ||' AND y.code_users LIKE ANY (ARRAY[';
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
	for users_ in execute query_
	loop
		insert into vendeur_by_jour_ select * from et_total_one_vendeur(users_.agence, users_.id, date_debut_, date_fin_, periode_);
	end loop;	
    return QUERY select * from vendeur_by_jour_ order by vend, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_vendeurs(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;
