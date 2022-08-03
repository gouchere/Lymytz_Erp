-- Function: grh_et_recap_presence_employe(bigint, date, date)

-- DROP FUNCTION grh_et_recap_presence_employe(bigint, date, date);

CREATE OR REPLACE FUNCTION grh_et_recap_presence_employe(IN employe_ bigint, IN debut_ date, IN fin_ date)
  RETURNS TABLE(date_presence date, observation character varying, date_debut date, date_fin date, heure_debut time without time zone, heure_fin time without time zone, total_presence double precision, total_suppl double precision, validation character varying, jour character varying, employe bigint, nom character varying, prenom character varying) AS
$BODY$
declare 
	line_ record;
	dat_ date;
	jour_ character varying;
	num_jour_ character varying;
	repos_ boolean default false;
	_employe record;
	
begin 	
	-- DROP TABLE IF EXISTS table_recap_presence_employe;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_presence_employe(date_presence_ date, observation_ character varying, date_debut_ date, date_fin_ date, heure_debut_ time without time zone, heure_fin_ time without time zone, 
					total_presence_ double precision, total_suppl_ double precision, validation_ character varying, _jour_ character varying, _employe_ bigint, nom_ character varying, prenom_ character varying); 
	DELETE FROM table_recap_presence_employe;
	select into _employe y.id, y.matricule, y.civilite, y.nom, y.prenom, a.societe from yvs_grh_employes y inner join yvs_agences a on y.agence = a.id where y.id = employe_;
	dat_ = debut_;
	WHILE(dat_ <= fin_) LOOP
		repos_ = false;	
		num_jour_ = to_char(dat_ ,'dd');
		jour_ = (select convert_integer_to_jourweek(extract(DOW from dat_)::integer));	
		--vérifie si cette journées est ouvrée
		SELECT INTO line_ jo.id FROM yvs_jours_ouvres jo where jo.calendrier = (select c.calendrier FROM yvs_grh_contrat_emps c where c.employe=employe_ and c.contrat_principal is true limit 1) and jo.jour=jour_ and ouvrable = true AND jo.jour_de_repos IS false;
		IF (line_ IS NULL and line_.id IS NULL) THEN
			repos_ = true;
		END IF;	
		SELECT INTO line_ p.id, p.total_presence, p.total_heure_sup, p.date_debut, p.date_fin, p.heure_debut, p.heure_fin, v.libelle 
			FROM yvs_grh_presence p INNER JOIN yvs_grh_type_validation v ON p.type_validation = v.id WHERE p.employe=employe_ AND p.valider is true and p.date_debut = dat_;
		IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
			INSERT INTO table_recap_presence_employe VALUES(dat_, 'P', line_.date_debut, line_.date_fin, line_.heure_debut, line_.heure_fin, line_.total_presence, line_.total_heure_sup , line_.libelle, num_jour_, _employe.id, _employe.nom, _employe.prenom);
		ELSE
			--teste s'il y a eu mission
			SELECT INTO line_ m.id, m.date_debut, m.date_fin, m.duree_mission, m.heure_depart FROM yvs_grh_missions m WHERE m.employe=employe_ AND (dat_ BETWEEN m.date_debut and m.date_fin) AND (m.statut_mission='V' OR m.statut_mission='T' OR m.statut_mission='C' );
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				INSERT INTO table_recap_presence_employe VALUES(dat_, 'M', line_.date_debut, line_.date_fin, line_.heure_depart, null, null, null, null, num_jour_, _employe.id, _employe.nom, _employe.prenom);
			ELSE
				--teste s'il y a eu congé autorisé ou congé technique
				SELECT INTO line_ co.id, co.date_debut, co.date_fin, co.heure_debut, co.heure_fin FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND dat_ BETWEEN co.date_debut AND co.date_fin AND (co.statut='V' OR co.statut='T' OR co.statut='C' ) 
					AND (((co.nature='C' OR (co.nature='P' and co.duree_permission='L')) AND co.effet IN ('SPECIALE', 'AUTORISE')) AND type_conge!='CT');
				IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN 
					INSERT INTO table_recap_presence_employe VALUES(dat_, 'C', line_.date_debut, line_.date_fin, line_.heure_debut, line_.heure_fin, null, null, null, num_jour_, _employe.id, _employe.nom, _employe.prenom);
				ELSE
				   --teste si le jour n'est pas un jour férié
					SELECT INTO line_ j.id FROM yvs_jours_feries j WHERE (j.jour=dat_ AND j.all_year IS FALSE) OR (alter_date(j.jour, 'year', dat_)=dat_ AND j.all_year IS TRUE) AND j.societe = _employe.societe;
					IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN 
						INSERT INTO table_recap_presence_employe VALUES(dat_, 'F', null, null, null, null, null, null, null, num_jour_, _employe.id, _employe.nom, _employe.prenom);
					ELSE
						IF(repos_ IS FALSE)THEN
							INSERT INTO table_recap_presence_employe VALUES(dat_, 'A', null, null, null, null, null, null, null, num_jour_, _employe.id, _employe.nom, _employe.prenom);
						ELSE
							INSERT INTO table_recap_presence_employe VALUES(dat_, 'R', null, null, null, null, null, null, null, num_jour_, _employe.id, _employe.nom, _employe.prenom);
						END IF;
					end if;
				END IF;
			END IF;
		END IF;
		dat_ = dat_ + interval '1 day';								
	end loop;
	return QUERY SELECT * FROM table_recap_presence_employe order by date_presence_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_presence_employe(bigint, date, date)
  OWNER TO postgres;
