-- Function: grh_conge_nbday_technique(date, date, integer, bigint)

-- DROP FUNCTION grh_conge_nbday_technique(date, date, integer, bigint);

CREATE OR REPLACE FUNCTION grh_conge_nbday_technique(periode_d_ date, periode_f_ date, num_mois integer, employe_ bigint)
  RETURNS integer AS
$BODY$
DECLARE 
	conges_ record;
	duree_conge integer;
	cumul_result integer default  0;
	nbr_conge integer; --durée du congé dans la période de référence
	
	data_ bigint;
	datas_ bigint[1000];
	compteur integer default 1;
	i integer;
	date_calcul date;
	existe_ boolean default false;
	--
	jour_ character varying default '';
	jr_ character varying;
	jours_conge integer;
	jour_repos integer;
	
	date_debut date;
	date_fin date;
	duree_ integer;

	add_one_ boolean default false;
    
BEGIN	
	IF(num_mois<=0) THEN
		RETURN 0;
	END IF;
	FOR jr_ IN SELECT j.jour FROM yvs_grh_contrat_emps c INNER JOIN yvs_calendrier cal ON cal.id=c.calendrier INNER JOIN yvs_jours_ouvres j ON j.calendrier=cal.id WHERE c.employe=employe_ AND (j.jour_de_repos IS true or j.ouvrable is false) 
	LOOP
		jour_=jour_ ||','||(select convert_jourweek_to_integer(jr_));
	END LOOP;
	FOR conges_ IN SELECT * FROM yvs_grh_conge_emps c WHERE ((periode_d_ BETWEEN c.date_debut AND c.date_fin) OR (periode_f_ BETWEEN c.date_debut AND c.date_fin) OR ((c.date_debut BETWEEN periode_d_ AND periode_f_) AND (c.date_fin BETWEEN periode_d_ AND periode_f_))) 
	AND c.employe=employe_ AND c.statut IN('V','T','C' ) AND c.type_conge='CT' order by c.date_debut desc
	LOOP			
		--1.Décompté la durée totale du congé
		jours_conge=(select (conges_.date_fin-conges_.date_debut))+1;
		--jour de repos das la période
		--jour_repos =jour_repos +(SELECT COUNT(date_) FROM generate_series(periode_d_, (periode_f_ ),'1 day'::interval) date_  WHERE ((SELECT EXTRACT(DOW FROM date_))::character in (select val from regexp_split_to_table(jour_,',') val)));
		duree_conge=(jours_conge);
		IF(jour_repos IS NULL) THEN 
			jour_repos=0;
		END IF;
		IF(duree_conge IS NULL) THEN 
			duree_conge=0;
		END IF;
		--2. durée du congé dans la période de référence
		--determination de la date de debut
		date_calcul = conges_.date_debut;
		existe_ = FALSE;
		-- recherche du congé consécutif suivant au congé en cours
		SELECT INTO data_ c.id FROM yvs_grh_conge_emps c WHERE c.employe = conges_.employe AND c.statut IN('V','T','C') AND c.type_conge='CT' AND (c.date_debut between conges_.date_fin and (conges_.date_fin + integer '1')::date ) LIMIT 1;
		IF(data_ IS NOT NULL AND data_ > 0 AND datas_ IS NOT NULL)THEN
			FOR i IN 1..1000
			LOOP
				IF(datas_[i] IS NULL OR datas_[i] = 0)THEN
					EXIT;
				END IF;
				IF(datas_[i] = data_)THEN
					existe_ = TRUE;
					EXIT;
				END IF;				
			END LOOP;
		END IF;
		IF(existe_ IS FALSE)THEN
			date_calcul = (SELECT grh_get_datedebut_ct(conges_.id));
			RAISE NOTICE 'date_calcul : %',date_calcul;
			RAISE NOTICE 'conges_.date_debut : %',conges_.date_debut;
			RAISE NOTICE 'conges_.date_fin : %',conges_.date_fin;
			date_debut = date_calcul + ((30* ((num_mois-1))||' day') :: interval);
			IF(num_mois > 1)THEN
				date_debut = date_debut + ('1 day') :: interval;
			END IF;
			RAISE NOTICE 'date_debut 0 : %',date_debut;
			
			--determination de la date de fin
			date_fin = date_debut + ('30 day') :: interval;
			RAISE NOTICE 'date_fin : %',date_fin;
			
			if(date_debut < periode_d_)then
				date_debut = periode_d_;
			end if;
			
			if(date_fin > periode_f_)then
				date_fin = periode_f_;
			end if;
			if(date_fin > conges_.date_fin)then
				date_fin = conges_.date_fin;
			end if;
			
			if(date_debut > date_fin)then
				duree_conge = 0;
				jour_repos = 0;
			else
				duree_conge = (select (date_fin - date_debut)) + 1;
				--compte les jour de repôs contenue dans la période
				IF(conges_.compter_jour_repos is false) THEN
					jour_repos =jour_repos +(SELECT COUNT(date_) FROM generate_series(date_debut, (date_fin ),'1 day'::interval) date_  WHERE ((SELECT EXTRACT(DOW FROM date_))::character in (select val from regexp_split_to_table(jour_,',') val)));
				ELSE
					jour_repos = 0;
				END IF;
			end if;
			
			IF(duree_conge IS NULL) THEN 
				duree_conge=0;
			END IF;
			IF(jour_repos IS NULL) THEN 
				jour_repos=0;
			END IF;
				
			RAISE NOTICE 'date_debut : %',date_debut;
			RAISE NOTICE 'date_fin : %',date_fin;
			RAISE NOTICE 'duree_conge : %',duree_conge;
			RAISE NOTICE 'jour_repos : %',jour_repos;
			nbr_conge = (duree_conge - jour_repos);	
			RAISE NOTICE 'nbr_conge : %',nbr_conge;
			IF(nbr_conge > 29)THEN
				duree_conge = (SELECT grh_conge_nbday_technique((periode_d_ - integer '30'), (periode_f_ - integer '30'), num_mois, employe_));
				RAISE NOTICE ' --- duree_conge précèdent : %', duree_conge;
				nbr_conge = nbr_conge - duree_conge;
			END IF;
			cumul_result = cumul_result + nbr_conge;
		END IF;
		datas_[compteur] = conges_.id;
		compteur = compteur + 1;
	END LOOP;
	IF(cumul_result IS NULL or cumul_result < 0) THEN 
		cumul_result=0;
	END IF;
	RETURN cumul_result;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_conge_nbday_technique(date, date, integer, bigint)
  OWNER TO postgres;

  
  -- Function: grh_conge_nbday_technique(date, date, integer, bigint)

 -- DROP FUNCTION grh_conge_nbday_technique(date, date, bigint);

CREATE OR REPLACE FUNCTION grh_conge_nbday_technique(periode_d_ date, periode_f_ date, employe_ bigint)
  RETURNS TABLE(num_mois integer, date_debut date, date_fin date, duree integer) AS
$BODY$
DECLARE 
	conges_ record;
	duree_conge integer;
	
	i integer;
	date_calcul date;	
	date_debut date;
	date_fin date;
    
BEGIN
-- 	DROP TABLE table_conge_nbday_technique;
	CREATE TEMP TABLE IF NOT EXISTS table_conge_nbday_technique(_num_mois integer, _date_debut date, _date_fin date, duree integer);
	delete from table_conge_nbday_technique;
	FOR conges_ IN SELECT * FROM yvs_grh_conge_emps c WHERE ((periode_d_ BETWEEN c.date_debut AND c.date_fin) OR (periode_f_ BETWEEN c.date_debut AND c.date_fin) OR ((c.date_debut BETWEEN periode_d_ AND periode_f_) AND (c.date_fin BETWEEN periode_d_ AND periode_f_))) 
	AND c.employe=employe_ AND c.statut IN('V','T','C' ) AND c.type_conge='CT' order by c.date_debut desc limit 1
	LOOP		
		date_calcul = (SELECT grh_get_datedebut_ct(conges_.id));
		FOR i IN 1..6
		LOOP
			duree_conge = (SELECT grh_conge_nbday_technique(periode_d_, periode_f_, i, employe_));
			date_debut = date_calcul + ((30* ((i-1))||' day') :: interval);
			IF(i > 1)THEN
				date_debut = date_debut + ('1 day') :: interval;
			END IF;
			date_fin = date_debut + ('30 day') :: interval;			
			if(date_debut < periode_d_)then
				date_debut = periode_d_;
			end if;			
			if(date_fin > periode_f_)then
				date_fin = periode_f_;
			end if;
			if(date_fin > conges_.date_fin)then
				date_fin = conges_.date_fin;
			end if;
			INSERT INTO table_conge_nbday_technique VALUES(i, date_debut, date_fin, duree_conge);	
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_conge_nbday_technique ORDER BY _num_mois;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_conge_nbday_technique(date, date, bigint)
  OWNER TO postgres;
