-- Function: grh_et_recap_presence_absence(date, date, bigint)

-- DROP FUNCTION grh_et_recap_presence_absence(date, date, bigint);

CREATE OR REPLACE FUNCTION grh_et_recap_presence_absence(IN debut_ date, IN fin_ date, IN agence_ bigint)
  RETURNS TABLE(nom_prenom character varying, poste character varying, d_requis integer, nbre_presence integer, nbre_abs integer, nbre_mission integer, nbre_conge integer, autre integer) AS
$BODY$
declare 
	employe_ record;
	nb_presence integer default 0;
	nb_abs integer default 0;
	nb_mission integer default 0;
	nb_conge integer default 0;
	nb_others integer default 0;
	d_requis integer default 0;
	name_ character varying ;
	pname_ character varying ;
	dat_ DATE;
	jour_ INTEGER ;
	chJour_ character varying ;
	jo_ bigint;
	id_ bigint;
	sup_ double precision default 0;
	line_ record;
	jf_ integer default 0;
	
	
begin 	
	--DROP TABLE IF EXISTS grh_recap_presence;
	CREATE TEMP TABLE IF NOT EXISTS   grh_recap_presence_abs(nom_prenom_ character varying, 
					     poste_ character varying,
					     d_requis_ integer,
				             nbre_presence_ integer, 
				             nbre_abs_ integer, 
				             nbre_mission_ integer, 
					     nbre_conge_ integer, 
					     autre_ integer); 
	DELETE FROM grh_recap_presence_abs;
	SELECT INTO d_requis COUNT(date_) FROM generate_series(debut_,fin_, interval '1 day') date_;
	for employe_ in select e.id, e.nom, e.prenom, e.matricule, p.intitule  FROM yvs_grh_employes e INNER JOIN yvs_grh_poste_de_travail p ON p.id=e.poste_actif 
												       WHERE e.agence=agence_ AND e.actif is true
												       ORDER BY e.nom
		loop
			dat_=debut_;
			nb_presence=0;
			sup_=0;nb_mission=0;nb_conge=0;nb_abs=0;nb_others=0;
			WHILE(dat_ <= fin_) LOOP	
						jour_ = (select extract(DOW from dat_));		
						if(jour_ = 1) then
							chJour_ = 'Lundi';
						elsif(jour_ = 2) then
							chJour_ = 'Mardi';
						elsif(jour_ = 3) then
							chJour_ =  'Mercredi';
						elsif(jour_ = 4) then
							chJour_ =  'Jeudi';
						elsif(jour_ = 5) then
							chJour_ = 'Vendredi';
						elsif(jour_ = 6) then
							chJour_ =  'Samedi';
						elsif(jour_ = 0) then		
							chJour_ =  'Dimanche';
						end if;	
						--vérifie si cette journées est ouvrée
						SELECT INTO jo_ jo.id FROM yvs_jours_ouvres jo where jo.calendrier = (select c.calendrier FROM yvs_grh_contrat_emps c where c.employe=employe_.id and c.contrat_principal is true limit 1) and jo.jour=chJour_ and ouvrable = true AND jo.jour_de_repos IS false;
						IF jo_ IS NOT NULL THEN
							SELECT INTO line_ p.id, p.total_heure_sup FROM yvs_grh_presence p WHERE p.employe=employe_.id AND p.valider is true and p.date_debut=dat_;
							IF line_ IS NOT NULL THEN
								nb_presence= nb_presence + 1;								
							ELSE
								--teste s'il y a eu mission
									SELECT INTO id_ m.id FROM yvs_grh_missions m WHERE m.employe=employe_.id AND (dat_ BETWEEN m.date_debut and m.date_fin) AND (m.statut_mission='V' OR m.statut_mission='T' OR m.statut_mission='C' );
									IF id_ IS NOT NULL THEN
										nb_mission=nb_mission+1;
									ELSE
										--teste s'il y a eu congé autorisé ou congé technique
										SELECT INTO id_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_.id AND dat_ BETWEEN co.date_debut AND co.date_fin AND (co.statut='V' OR co.statut='T' OR co.statut='C' ) AND (((co.nature='C' OR (co.nature='P' and co.duree_permission='L')) AND co.effet IN ('SPECIALE', 'AUTORISE')) AND type_conge!='CT');
										IF id_ IS NOT NULL THEN 
											nb_conge=nb_conge+1;											
										ELSE
											--teste si le jour n'est pas un jour férié
											jf_=(SELECT COUNT (j.*) from yvs_jours_feries j where (j.jour=dat_ and j.all_year is false) or (alter_date(j.jour, 'year', dat_)=dat_ and j.all_year is true));
											IF(jf_ is not null) then 
												IF(jf_ <=0) THEN -- ce n'est pas un jour férie 
													nb_abs=nb_abs+1;													
												ELSE  --c'est un jour férié
													-- si le congé décompte les jours de repos
													SELECT INTO id_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_.id AND dat_ BETWEEN co.date_debut AND co.date_fin AND (co.statut='V' OR co.statut='T' OR co.statut='C' ) AND (co.nature='C' OR (co.nature='P' and co.duree_permission='L')) AND compter_jour_repos IS true;
												     IF id_ IS NOT NULL THEN 
													nb_abs=nb_abs+1;
											             END IF;
												END IF;
											ELSE
												nb_abs=nb_abs+1;
											END IF;
										END IF;
									END IF;
							END IF;
						ELSE
							-- si un congé tombe un jour de repos
							SELECT INTO id_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_.id AND dat_ BETWEEN co.date_debut AND co.date_fin AND (co.statut='V' OR co.statut='T' OR co.statut='C' ) AND (co.nature='C' OR (co.nature='P' and co.duree_permission='L')) AND compter_jour_repos IS true;
								IF id_ IS NOT NULL THEN 
									nb_abs=nb_abs+1;
								END IF;
						END IF;	
						dat_ = dat_ + interval '1 day';								
			end loop;
				--calcul le totale des heures sup
				sup_=(select sum(p.total_heure_sup) FROM yvs_grh_presence p where (p.date_debut between  debut_ and fin_ ) and p.valider is true and p.employe=employe_.id);
				IF(sup_ is null)then
					sup_=0;
				END if;
				name_=employe_.nom;
				pname_=employe_.prenom;
				IF employe_.nom is null then 
					name_='';
				END IF;
				IF employe_.prenom is null then 
					pname_='';
				END IF;	
			INSERT INTO grh_recap_presence_abs VALUES(name_ ||' ' ||pname_, employe_.intitule,d_requis ,nb_presence, nb_abs , nb_mission,nb_conge,(sup_/8));
	END LOOP;

	return QUERY SELECT * FROM grh_recap_presence_abs order by nom_prenom_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_presence_absence(date, date, bigint)
  OWNER TO postgres;
