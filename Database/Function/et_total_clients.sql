-- Function: et_total_clients(bigint, date, date, character varying, character varying)
DROP FUNCTION et_total_clients(bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_clients(IN societe_ bigint, IN date_debut_ date, IN date_fin_ date, IN reference_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, client bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer) AS
$BODY$
DECLARE
    client_ record;
    dates_ record;
    total_ record;
    prec_ record;
    nbre integer default 0;
    i integer default 0;
    j integer default 0;
	
    valeur_ double precision default 0;
    taux_ double precision default 0;
    qte_ double precision default 0;

    query_ character varying default 'SELECT DISTINCT y.id, y.code_client AS code, t.nom, t.prenom FROM yvs_com_client y INNER JOIN yvs_base_tiers t ON t.id = y.tiers 
										INNER JOIN yvs_com_doc_ventes d ON d.client=y.id INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE e.date_entete BETWEEN '''||date_debut_||''' AND '''||date_fin_||'''';
    jour_ character varying;
    
    deja_ BOOLEAN DEFAULT FALSE;
BEGIN    
	DROP TABLE IF EXISTS clients_by_jour_;
	CREATE TEMP TABLE IF NOT EXISTS clients_by_jour_(_code character varying, _nom character varying, _client bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer);
	if(societe_ is not null and societe_ > 0)then
		query_ = query_ ||' AND t.societe = '||societe_;
	end if;
	if(reference_ is not null and reference_ NOT IN ('', ' '))then
		query_ = query_ ||' AND y.id IN (';
		for jour_ IN select val from regexp_split_to_table(reference_,',') val
		loop
			if(deja_)then
				query_ = query_ || ',';
			end if;
			query_ = query_ || ''||TRIM(jour_)||'';
			deja_ = true;
		end loop;
		query_ = query_ || ')';
	end if;
	for client_ in execute query_
	loop
		i = 0;
		for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_)
		loop
			jour_ = dates_.intitule;
			-- Evaluer le chiffre d'affaire
			valeur_ = (SELECT get_ca_vente(societe_, 0, 'V',null, null, 0, 0, client_.id, 0,0, dates_.date_debut, dates_.date_fin, 'CA'));
			if(valeur_ IS NULL)then
				valeur_ = 0;
			end if;
			
			select into prec_ ttc from clients_by_jour_ where _client = client_.id order by rang desc limit 1;
			if(prec_.ttc = 0)then
				select into prec_ ttc from clients_by_jour_ where _client = client_.id and ttc > 0 order by rang desc limit 1;
			end if;
			taux_ = valeur_ - prec_.ttc;
			if(taux_ is null)then
				taux_ = 0;
			end if;
			if(taux_ != 0 and valeur_ != 0)then
				taux_ = (taux_ / valeur_) * 100;
			end if;
			if(valeur_ != 0 or qte_ != 0)then
				insert into clients_by_jour_ values(client_.code, client_.nom, client_.id, jour_, valeur_, qte_, taux_, dates_.position);
			end if;			
			i = i + 1;
		end loop;
	end loop;	
    return QUERY select * from clients_by_jour_ order by _client, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_clients(bigint, date, date, character varying, character varying)
  OWNER TO postgres;
