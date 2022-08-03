-- Function: grh_presence_duree_month(date, date, bigint, integer)

-- DROP FUNCTION grh_presence_duree_month(date, date, bigint, integer);

CREATE OR REPLACE FUNCTION grh_presence_duree_month(debut_ date, fin_ date, employe_ bigint, resultat_ integer)
  RETURNS double precision AS
$BODY$
declare 
	nb_presence integer default 0;
	nb_abs integer default 0;
	nb_mission integer default 0;
	nb_conge integer default 0;
	nb_others integer default 0;
	d_periode integer default 0;  --durée de la période
	dat_ DATE;
	jour_ INTEGER ;
	chJour_ character varying ;
	jo_ bigint;
	id_ bigint;
	sup_ double precision default 0;
	line_ record;	
	jf_ integer;
begin 	
	
			dat_=debut_;			
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
						SELECT INTO jo_ jo.id FROM yvs_jours_ouvres jo where jo.calendrier = (select c.calendrier FROM yvs_grh_contrat_emps c where c.employe=employe_ and c.contrat_principal is true limit 1) and jo.jour=chJour_ and ouvrable = true AND jo.jour_de_repos IS false;
						IF jo_ IS NOT NULL THEN
							SELECT INTO line_ p.id, p.total_heure_sup FROM yvs_grh_presence p WHERE p.employe=employe_ AND p.valider is true and p.date_debut=dat_;
							IF line_ IS NOT NULL THEN
								nb_presence= nb_presence + 1;
							ELSE
								--teste s'il y a eu mission
									SELECT INTO id_ m.id FROM yvs_grh_missions m WHERE m.employe=employe_ AND (dat_ BETWEEN m.date_debut and m.date_fin) AND (m.statut_mission='V' OR m.statut_mission='T' OR m.statut_mission='C' );
									IF id_ IS NOT NULL THEN
										nb_mission=nb_mission+1;
									ELSE
									--teste s'il y a eu congé autorisé ou congé technique
									SELECT INTO id_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND dat_ BETWEEN co.date_debut AND co.date_fin AND (co.statut='V' OR co.statut='T' OR co.statut='C' ) AND (((co.nature='C' OR (co.nature='P' and co.duree_permission='L')) AND co.effet IN ('SPECIALE', 'AUTORISE')) AND type_conge!='CT');
									IF id_ IS NOT NULL THEN 
									   nb_conge=nb_conge+1;									  
									ELSE
									--teste si le jour n'est pas un jour férié
									   jf_=(SELECT COUNT (j.*) from yvs_jours_feries j where (j.jour=dat_ and j.all_year is false) or (alter_date(j.jour, 'year', dat_)=dat_ and j.all_year is true));
									    IF(jf_ is not null) then
										IF(jf_ <=0) THEN 
										   nb_abs=nb_abs+1;
										ELSE  --c'est un jour férié
											 -- si le congé décompte les jours de repos
											SELECT INTO id_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND dat_ BETWEEN co.date_debut AND co.date_fin AND (co.statut='V' OR co.statut='T' OR co.statut='C' ) AND (co.nature='C' OR (co.nature='P' and co.duree_permission='L')) AND compter_jour_repos IS true;
												IF id_ IS NOT NULL THEN 
													nb_abs=nb_abs+1;
												END IF;
										END IF;
									    ELSE
									      nb_abs=nb_abs+1;
									    end if;
									END IF;
								END IF;
							END IF;
						ELSE
						-- si un congé tombe un jour de repos
						SELECT INTO id_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND dat_ BETWEEN co.date_debut AND co.date_fin AND (co.statut='V' OR co.statut='T' OR co.statut='C' ) AND (co.nature='C' OR (co.nature='P' and co.duree_permission='L')) AND compter_jour_repos IS true;
						  IF id_ IS NOT NULL THEN 
							nb_abs=nb_abs+1;
						  END IF;
						END IF;	
						dat_ = dat_ + interval '1 day';								
			end loop;
			--calcul le totale des heures sup
			sup_=(select sum(p.total_heure_sup ) FROM yvs_grh_presence p where p.date_debut between  debut_ and fin_ and p.valider is true and p.employe=employe_);
			
		CASE resultat_
		WHEN 1 THEN   --présence
		  RETURN nb_presence;
		WHEN 2 THEN		--absence
		  RETURN nb_abs;
		WHEN 3 THEN	--Mission
		  RETURN nb_mission;
		WHEN 4 THEN 	-- congé autorisé
		  RETURN nb_conge;
		WHEN 5 THEN 	--Congé technique
		  RETURN -1;
		WHEN 6 THEN	--Congé non autorisé
		  RETURN -1;
		WHEN 7 THEN	--Jours suplémentaire
		  RETURN (sup/8);
		WHEN 8 THEN	--durée de la période
			SELECT INTO d_periode COUNT(date_) FROM generate_series(debut_,fin_, interval '1 day') date_;
			RETURN d_periode;
		  RETURN (sup/8);
		ELSE
		  RETURN -1;
	END CASE;				
	
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_presence_duree_month(date, date, bigint, integer)
  OWNER TO postgres;
COMMENT ON FUNCTION grh_presence_duree_month(date, date, bigint, integer) IS 'en fontion du type de résultat, retourne le nombre de présence, d''absence, de mission, de congé autorisé, de congé technique, de congé non autorisé d''un employé sur une période';
