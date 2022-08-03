-- Function: grh_conge_get_duree_one_conge(date, date, bigint)

-- DROP FUNCTION grh_conge_get_duree_one_conge(date, date, bigint);

CREATE OR REPLACE FUNCTION grh_conge_get_duree_one_conge(periode_d_ date, periode_f_ date, id_conge_ bigint)
  RETURNS integer AS
$BODY$
DECLARE
	conges_ record;
	jour_ integer;
	jr_ character varying;
	nb_repos_ integer;
	duree_ integer;

BEGIN
	--recherche tous les congés d'un emplyé dans une période (parcours)
	IF(periode_d_ <= periode_f_) THEN
	   SELECT INTO conges_ * FROM yvs_grh_conge_emps c WHERE c.id=id_conge_;	   
		IF(conges_.date_debut<=conges_.date_fin) THEN
			--on distinge quatre cas
			IF(conges_.compter_jour_repos) THEN
				SELECT INTO jr_ j.jour FROM yvs_grh_contrat_emps c INNER JOIN yvs_calendrier cal ON cal.id=c.calendrier INNER JOIN yvs_jours_ouvres j ON j.calendrier=cal.id WHERE c.employe=conges_.employe AND j.jour_de_repos IS true;
				jour_=(select convert_jourweek_to_integer(jr_));
			END IF;
		--1. dc<dp et fc>fp
			IF(conges_.date_debut<= periode_d_ AND periode_f_ <=conges_.date_fin)THEN
				duree_=(select (periode_f_- periode_d_));
				--compte les jour de repôs contenue dans la période
				IF(conges_.compter_jour_repos) THEN
				  nb_repos_=(SELECT COUNT(date_) FROM generate_series(periode_d_, (periode_f_  - interval '1 day'),'1 day'::interval) date_ WHERE ((SELECT EXTRACT(DOW FROM date_))=jour_));			     
				END IF;
		--2. dc<dp et fc<fp
			ELSIF(conges_.date_debut<= periode_d_ AND periode_f_ >conges_.date_fin AND conges_.date_fin >=periode_d_) THEN
				duree_=(select (conges_.date_fin - periode_d_));
				--compte les jour de repôs contenue dans la période
				IF(conges_.compter_jour_repos) THEN
				  nb_repos_=(SELECT COUNT(date_) FROM generate_series(periode_d_, (conges_.date_fin- interval '1 day') ,'1 day'::interval) date_ WHERE ((SELECT EXTRACT(DOW FROM date_))=jour_));		     
				END IF;
			ELSIF (conges_.date_debut>= periode_d_ AND periode_f_ > conges_.date_fin) THEN
				duree_=(select (conges_.date_fin- conges_.date_debut));
				--compte les jour de repôs contenue dans la période
				IF(conges_.compter_jour_repos) THEN
				  nb_repos_=(SELECT COUNT(date_) FROM generate_series(conges_.date_debut, (conges_.date_fin- interval '1 day') ,'1 day'::interval) date_ WHERE ((SELECT EXTRACT(DOW FROM date_))=jour_));			     
				END IF;
		--4. dp<dc et fc>fp
			ELSIF(conges_.date_debut> periode_d_ AND periode_f_ <=conges_.date_fin) THEN
				duree_=(select (periode_f_- conges_.date_debut));
				--compte les jour de repôs contenue dans la période
				IF(conges_.compter_jour_repos) THEN
				  nb_repos_=(SELECT COUNT(date_) FROM generate_series(conges_.date_debut, (periode_f_- interval '1 day') ,'1 day'::interval) date_ WHERE ((SELECT EXTRACT(DOW FROM date_))=jour_));			     
				END IF;
			ELSE
				duree_=0;
			END IF;	
		END IF;
	END IF;	
	IF(duree_ IS NULL) THEN 
		duree_=0;
	END IF;
	IF(nb_repos_ IS NULL) THEN 
		nb_repos_=0;
	END IF;
    return (duree_-nb_repos_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_conge_get_duree_one_conge(date, date, bigint)
  OWNER TO postgres;
