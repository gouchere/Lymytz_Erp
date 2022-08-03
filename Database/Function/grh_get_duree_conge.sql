-- Function: grh_get_duree_conge(date, date, bigint)

-- DROP FUNCTION grh_get_duree_conge(date, date, bigint);

CREATE OR REPLACE FUNCTION grh_get_duree_conge(periode_d_ date, periode_f_ date, employe_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	conges_ record;
	jour_ character varying default '';
	jr_ character varying;
	cout double precision default 0;
	nb_repos_ integer default 0;
	duree_ integer default 0;
	i_ integer default 0;

BEGIN
	--recherche tous les congés d'un emplyé dans une période (parcours)
	IF(periode_d_ <= periode_f_) THEN
	   FOR conges_  IN SELECT * FROM yvs_grh_conge_emps c WHERE ((periode_d_ BETWEEN c.date_debut AND c.date_fin) OR (periode_f_ BETWEEN c.date_debut AND c.date_fin) OR (c.date_debut between periode_d_ and periode_f_ AND c.date_fin between periode_d_ and periode_f_ ))
								    AND c.employe=employe_ AND (c.statut='V' OR c.statut='T' OR c.statut='C' ) AND (c.nature='C' OR (c.nature='P' and c.duree_permission='L'))	   
	   LOOP	
		RAISE NOTICE 'here';
		IF(conges_.date_debut<=conges_.date_fin) THEN
			--on distinge quatre cas
			IF(conges_.compter_jour_repos) THEN
				FOR jr_ IN SELECT j.jour FROM yvs_grh_contrat_emps c INNER JOIN yvs_calendrier cal ON cal.id=c.calendrier INNER JOIN yvs_jours_ouvres j ON j.calendrier=cal.id WHERE c.employe=employe_ AND (j.jour_de_repos IS true or j.ouvrable is false) 
				LOOP
				  jour_=jour_ ||','||(select convert_jourweek_to_integer(jr_));
				  i_=i_+1;
				END LOOP;
				
			END IF;
		--1. dc<dp et fc>fp
			IF(conges_.date_debut<= periode_d_ AND periode_f_ <=conges_.date_fin)THEN
				duree_=duree_+(select (periode_f_- periode_d_))+1;
				--compte les jour de repôs contenue dans la période
				IF(conges_.compter_jour_repos) THEN
				  nb_repos_=nb_repos_ +(SELECT COUNT(date_) FROM generate_series(periode_d_, (periode_f_  - interval '1 day'),'1 day'::interval) date_  WHERE ((SELECT EXTRACT(DOW FROM date_))::character in (select val from regexp_split_to_table(jour_,',') val)));			     			     
				END IF;
		--2. dc<dp et fc<fp
			ELSIF(conges_.date_debut<= periode_d_ AND periode_f_ >conges_.date_fin AND conges_.date_fin >=periode_d_) THEN
				duree_=duree_+(select (conges_.date_fin - periode_d_))+1;
				--compte les jour de repôs contenue dans la période
				IF(conges_.compter_jour_repos) THEN
				 nb_repos_=nb_repos_ +(SELECT COUNT(date_) FROM generate_series(periode_d_, (conges_.date_fin- interval '1 day') ,'1 day'::interval) date_  WHERE ((SELECT EXTRACT(DOW FROM date_))::character in (select val from regexp_split_to_table(jour_,',') val)));			     	     
				END IF;
			ELSIF (conges_.date_debut>= periode_d_ AND periode_f_ > conges_.date_fin) THEN
				duree_=duree_+(select (conges_.date_fin- conges_.date_debut))+1;
				--compte les jour de repôs contenue dans la période
				IF(conges_.compter_jour_repos) THEN
				  nb_repos_=nb_repos_ +(SELECT COUNT(date_) FROM generate_series(conges_.date_debut, (conges_.date_fin- interval '1 day') ,'1 day'::interval) date_  WHERE ((SELECT EXTRACT(DOW FROM date_))::character in (select val from regexp_split_to_table(jour_,',') val)));			     		     
				END IF;
		--4. dp<dc et fc>fp
			ELSIF(conges_.date_debut> periode_d_ AND periode_f_ <=conges_.date_fin) THEN
				duree_=duree_ +(select (periode_f_- conges_.date_debut))+1;
				--compte les jour de repôs contenue dans la période
				-- FOR jr_ IN select val from regexp_split_to_table(jour_,',') val loop			
-- 					RAISE NOTICE 'Jour %',jr_;	
-- 				END LOOP;
				IF(conges_.compter_jour_repos) THEN
				  nb_repos_=nb_repos_ +(SELECT COUNT(date_) FROM generate_series(conges_.date_debut, (periode_f_) ,'1 day'::interval) date_ WHERE ((SELECT EXTRACT(DOW FROM date_))::character in (select val from regexp_split_to_table(jour_,',') val)));			     
				END IF;
			ELSE
-- 				duree_= duree_;
			END IF;	
		END IF;
	   END LOOP;
	END IF;	
	RAISE NOTICE 'Repos %',nb_repos_;
    return (duree_-nb_repos_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_get_duree_conge(date, date, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION grh_get_duree_conge(date, date, bigint) IS 'La durée de congé dans une période';
