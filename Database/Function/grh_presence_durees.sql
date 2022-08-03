-- Function: grh_presence_durees(bigint, bigint, bigint, date, date)

-- DROP FUNCTION grh_presence_durees(bigint, bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION grh_presence_durees(IN employe_ bigint, IN agence_ bigint, IN societe_ bigint, IN debut_ date, IN fin_ date)
  RETURNS TABLE(agence bigint, employe bigint, valeur double precision, element character varying, description character varying) AS
$BODY$
DECLARE 
	line_ record;
	date_ date;
	jour_ character varying;
	str_repos_ character varying default '';
	repos_ boolean default false;
	ferier_ boolean default false;
	jour_repos double precision default 0;
	valeur_ double precision default 0;	
	type_ character varying default '';
	description_ character varying default '';
	arrondi_result_ boolean default false;
	duree_annee integer default 0;
	duree_repos integer default 0;
	id_ bigint default 0;
	retard_ double precision default 0;
	total_retard_ double precision default 0;
	total_retard_begin_ double precision default 0;

BEGIN 	
-- 	SET DATESTYLE TO DMY;
	duree_annee = ((('31-12-'||(select extract(year from fin_)))::date) - (('01-01-'||(select extract(year from fin_)))::date) + 1);
	if(duree_annee = 366)then
		if((select extract(month from fin_)) = 3)then
			arrondi_result_ = ((fin_ - debut_) + 1) = 29;
		else
			arrondi_result_ = (((fin_ - debut_) + 1) = 30 or ((fin_ - debut_) + 1) = 31);
		end if;
	else
		if((select extract(month from fin_)) = 3)then
			arrondi_result_ = ((fin_ - debut_) + 1) = 28;
		else
			arrondi_result_ = (((fin_ - debut_) + 1) = 30 or ((fin_ - debut_) + 1) = 31);
		end if;
	end if;
	IF(fin_ > current_date)THEN
		fin_ = current_date;
	END IF;
	IF(employe_ IS NOT NULL AND employe_ > 0)THEN
		CREATE TEMP TABLE IF NOT EXISTS  table_presence_durees_by_employe(_agence bigint, _employe bigint, _valeur double precision, _element character varying, _description character varying); 
		DELETE FROM table_presence_durees_by_employe;
		IF(agence_ IS NULL OR agence_ < 1)THEN
			SELECT INTO agence_ e.agence FROM yvs_grh_employes e WHERE e.id = employe_;
		END IF;
		IF(societe_ IS NULL OR societe_ < 1)THEN
			SELECT INTO societe_ e.societe FROM yvs_agences e WHERE e.id = agence_;
		END IF;
		date_ = debut_;

		jour_repos = (fin_ - debut_) + 1;
		
		duree_repos=0;
		total_retard_=0;
		WHILE(date_ <= fin_) LOOP
			type_ = '';
			description_ = '';
			line_ = null;
			valeur_ = 0;
			repos_ = false;	
			ferier_ = false;
			
			jour_ = (select convert_integer_to_jourweek(extract(DOW from date_)::integer));	
			-- Vérifie si cette journées n'est pas ouvrée
			SELECT INTO line_ jo.id FROM yvs_jours_ouvres jo where jo.calendrier = (select c.calendrier FROM yvs_grh_contrat_emps c where c.employe=employe_ and c.contrat_principal is true limit 1) 
			AND jo.jour=jour_ AND (jo.jour_de_repos IS TRUE OR jo.ouvrable IS FALSE);
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				repos_ = true;
			ELSE
				-- Vérifie également dans les planning si la journée est marqué jour de repos
				SELECT INTO id_ pe.id FROM yvs_grh_planning_employe pe WHERE pe.employe=employe_ AND pe.repos IS TRUE AND pe.date_debut=date_;
				IF(id_ IS NOT NULL) THEN
					repos_=true;
				END IF ;
			END IF;	
			-- Vérifie si cette journées est feriée
			SELECT INTO line_ j.id FROM yvs_jours_feries j WHERE (j.jour=date_ AND j.all_year IS FALSE) OR (alter_date(j.jour, 'year', date_)=date_ AND j.all_year IS TRUE) AND j.societe = societe_;
			IF (line_ IS NOT NULL AND line_.id IS NOT NULL) THEN 
				ferier_ = true;
			END IF;
			-- Vérifie si l'employé a une fiche de présence
			SELECT INTO line_ p.id, p.total_heure_compensation, p.total_heure_sup, valider_hs, p.heure_debut, p.heure_fin, p.duree_pause,p.total_presence, p.total_retard FROM yvs_grh_presence p WHERE p.employe = employe_ AND p.valider IS TRUE AND p.date_debut = date_;
			IF (line_.id IS NOT NULL) THEN
				valeur_ = 1;				
				IF(ferier_)THEN
					IF(line_.valider_hs)THEN
						-- Vérifie s'il l'employé est présent compensatoire
						IF (line_.total_heure_compensation > 0) THEN
							valeur_ = line_.total_heure_compensation / 8;
							type_ = 'Jcf';
							description_ = 'Journée de compensation ferié';
							SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
							IF(jour_repos IS NOT NULL)THEN
								UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
							ELSE
								INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_,description_);
							END IF;
						END IF;	
						-- Vérifie s'il l'employé est présent supplementaire férié
						IF (line_.total_heure_sup > 0) THEN
							valeur_ = line_.total_heure_sup / 8;
							type_ = 'Jsf';
							description_ = 'Journée supplémentaire ferié';
							SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
							IF(jour_repos IS NOT NULL)THEN
								UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
							ELSE
								INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_, description_);
							END IF;
						END IF;
					END IF;
					IF(repos_ IS FALSE )THEN
						-- Vérifie s'il l'employé est présent un jour ferié
						valeur_ = 1;
						type_ = 'Jnf';
						description_ = 'Journée ferié';
						SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
						IF(jour_repos IS NOT NULL)THEN
							UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
						ELSE
							INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_, description_);
						END IF;		
					END IF;
					type_ = 'Fe';
				ELSE
					IF(line_.valider_hs)THEN
						-- Vérifie s'il l'employé est présent compensatoire
						IF (line_.total_heure_compensation > 0) THEN
							valeur_ = line_.total_heure_compensation / 8;
							type_ = 'Jc';
							description_ = 'Journée de compensation';
							SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
							IF(jour_repos IS NOT NULL)THEN
								UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
							ELSE
								INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_,description_);
							END IF;
						END IF;
						-- Vérifie s'il l'employé est présent supplementaire
						IF (line_.total_heure_sup > 0) THEN
							valeur_ = line_.total_heure_sup / 8;
							type_ = 'Js';
							description_ = 'Journée supplémentaire';
							SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
							IF(jour_repos IS NOT NULL)THEN
								UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
							ELSE
								INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_, description_);
							END IF;
						END IF;
					END IF;
					
					valeur_ = 1;
					IF(repos_)THEN
						type_ = 'Jnr';
						description_ = 'Journée de repos';
					ELSE
						--Calcul la durée du retard en heure si l'on n'est pas un jour de repos
						retard_= ((date_part('minute',(line_.heure_fin-line_.heure_debut))/60)+(date_part('hour',(line_.heure_fin-line_.heure_debut))))-
						(COALESCE((date_part( 'minute',line_.duree_pause)/60),0)+(COALESCE(date_part( 'hour',line_.duree_pause),0))) ;
						IF(retard_>0) THEN
							--recherche les permissions coutes durée autorisée
							retard_=retard_- COALESCE((SELECT SUM(c.duree) FROM yvs_grh_conge_emps c WHERE c.employe=employe_ AND c.date_debut=date_ AND c.effet='AUTORISE' AND c.nature='P' AND c.duree_permission='C' AND c.statut='V'),0);
						END IF;
						IF(COALESCE(retard_,0)<0) THEN
							retard_=0;
						END IF;
						total_retard_=total_retard_+retard_;
						total_retard_begin_ = total_retard_begin_ + line_.total_retard;
						type_ = 'Jn';
						description_ = 'Journée normale';						
					END IF;
				END IF;
			ELSE	-- L'employé n'a pas de fiche de présence
				-- Vérifie s'il l'employé est en mission
				SELECT INTO line_ m.id FROM yvs_grh_missions m WHERE m.employe=employe_ AND (date_ BETWEEN m.date_debut AND m.date_fin) AND (m.statut_mission IN ('V', 'T', 'C'));
				IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
					valeur_ = 1;
					type_ = 'Mi';
					description_ = 'Mission';
				ELSE
					-- Vérifie s'il l'employé est en formation
					SELECT INTO line_ f.id FROM yvs_grh_formation_emps f INNER JOIN yvs_grh_formation m ON f.formation = m.id WHERE f.employe=employe_ AND (date_ BETWEEN f.date_debut AND f.date_fin) AND (m.statut_formation IN ('V', 'T', 'C'));
					IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
						valeur_ = 1;
						type_ = 'Fo';
						description_ = 'Formation';
					ELSE
						-- Vérifie s'il l'employé est en congé maladie
						SELECT INTO line_ co.id, COALESCE(co.compter_jour_repos, FALSE) AS compter_jour_repos, co.effet FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'Maladie';
						IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
							valeur_ = 1;
							IF(line_.compter_jour_repos is false and ferier_)THEN
								type_ = 'Fe';
								description_ = 'Journées férié ';
							ELSIF(line_.compter_jour_repos is false and repos_)THEN
								type_ = 'Re';
								description_ = 'Journées de repos non travaillés';
							ELSE
								IF(line_.effet = 'AUTORISE' OR line_.effet = 'SPECIALE')THEN
									valeur_ = 1;
									type_ = 'Jn';
									description_ = 'Journées normales';
									SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
									IF(jour_repos IS NOT NULL)THEN
										UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
									ELSE
										INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_, description_);
									END IF;
								END IF;
								type_ = 'Cm';
								description_='Congé maladie ';
							END IF;
						ELSE
							-- Vérifie s'il l'employé est en congé annuel	
							SELECT INTO line_ co.id, COALESCE(co.compter_jour_repos, FALSE) AS compter_jour_repos, co.effet FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'Annuel';
							IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
								valeur_ = 1;
								IF(line_.compter_jour_repos is false and ferier_)THEN
									type_ = 'Fe';
									description_ = 'Journées fériés';
								ELSIF(line_.compter_jour_repos is false and repos_)THEN
									type_ = 'Re';
									description_ = 'Journées de repos non travaillés';
								ELSE
									type_ = 'Ca';
									description_ = 'Congés Annuel';
								END IF;
							ELSE
								-- Vérifie s'il l'employé est en congé technique
								SELECT INTO line_ co.id, COALESCE(co.compter_jour_repos, FALSE) AS compter_jour_repos FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'CT';
								IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
									valeur_ = 1;
									IF(line_.compter_jour_repos is false and ferier_)THEN
										type_ = 'Fe';
									ELSIF(line_.compter_jour_repos is false and repos_)THEN
										type_ = 'Re';
										description_ = 'Journées de repos non travaillés';
									ELSE
										type_ = 'Ct';
										description_ = 'Congés technique';
									END IF;
								ELSE
									-- Vérifie s'il l'employé est en permission courte durée
									SELECT INTO line_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='P' AND co.duree_permission = 'C';
									IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
										valeur_ = 1;
										type_ = 'Pc';
									ELSE
										-- Vérifie si l'employé est en permission longue durée
										SELECT INTO line_ co.id, co.effet, COALESCE(co.compter_jour_repos, FALSE) AS compter_jour_repos FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND (date_ BETWEEN co.date_debut AND co.date_fin) AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='P' AND co.duree_permission = 'L';
										IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
											valeur_ = 1;
											IF(line_.compter_jour_repos is false and ferier_)THEN
												type_ = 'Fe';
												description_ = 'Journées férié';
											ELSIF(line_.compter_jour_repos is false and repos_)THEN
												type_ = 'Re';
												description_ = 'Journées de repos non travaillés';
											ELSE
												IF(line_.effet = 'AUTORISE' OR line_.effet = 'SPECIALE')THEN
													valeur_ = 1;
													type_ = 'Jn';
													description_ = 'Journée normale';
													SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
													IF(jour_repos IS NOT NULL)THEN
														UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
													ELSE
														INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_);
													END IF;
												END IF;
												type_ = 'Pl';
											END IF;
										ELSE
											IF(repos_ IS FALSE)THEN
												-- Verifier si le jour n'est pas un jour férié
												IF (ferier_) THEN 
													valeur_ = 1;
													type_ = 'Fe';
													description_ = 'Journées férié';
												ELSE
													valeur_ = 1;
													type_ = 'Ab';
													description_ = 'Journées d''absence';
												END IF;
											ELSE
												valeur_ = 1;
												type_ = 'Re';
												description_ = 'Journées de repos non travaillés';
											END IF;
										END IF;
									END IF;
								END IF;
							END IF;
						END IF;
					END IF;
				END IF;
			END IF;
			IF(type_ != '')THEN
				SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
				IF(jour_repos IS NOT NULL)THEN
					UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
				ELSE
					INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_,description_);
				END IF;
			END IF;
			IF(repos_) THEN
				duree_repos=duree_repos+1;
			END IF;
			date_ = date_ + interval '1 day';								
		END LOOP;
		-- Recuperation de la durée de travail requis
		-- FOR jour_ IN SELECT jo.jour FROM yvs_jours_ouvres jo where jo.calendrier = (select c.calendrier FROM yvs_grh_contrat_emps c where c.employe=employe_ and c.contrat_principal is true limit 1) AND (jo.jour_de_repos IS TRUE OR jo.ouvrable IS FALSE)
		-- LOOP
			-- str_repos_= str_repos_ ||','||(select convert_jourweek_to_integer(jour_));
		-- END LOOP;
		-- Recuperation de la durée de travail requis
		-- nombre de 
		-- jour_repos = (SELECT COUNT(current_) FROM generate_series(debut_, fin_, '1 day'::interval) current_  WHERE ((SELECT EXTRACT(DOW FROM current_))::character in (SELECT val FROM regexp_split_to_table(str_repos_,',') val)));
		INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, ROUND(total_retard_::decimal,2), 'Rt', 'Retard cumulé');
		INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, ROUND(total_retard_begin_::decimal,2), 'Rtb', 'Durée Retard');
		jour_repos=duree_repos;
		INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, ((select (fin_ - debut_)) +1 - duree_repos), 'Tr', 'Durée de travail requis');		
		-- Recuperation de la durée de repos requis
		INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, duree_repos, 'Rr', 'Jours de repos');
		-- Recuperation de la durée de travail effectif
		SELECT INTO valeur_ SUM(p._valeur) FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element IN ('Jn','Mi','Fo');
		-- On verifi s'il y'a les fiches de présence
		IF(valeur_ IS NOT NULL AND valeur_ > 0) THEN
			SELECT INTO valeur_ SUM(p._valeur) FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element IN ('Jn');
			SELECT INTO jour_repos SUM(p._valeur) FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element IN ('Cm');
			-- Si l'employé est en congé maladi tout le mois
			IF(valeur_ = jour_repos)THEN
				valeur_ = 30;
				UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ WHERE _employe = employe_ AND _element = 'Cm';
			END IF;
			
			SELECT INTO valeur_ SUM(p._valeur) FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element IN ('Jn','Jnr','Re','Fe','Mi','Fo');
			IF(valeur_ IS NOT NULL AND valeur_ > 0)THEN
				IF(arrondi_result_) THEN
					jour_repos = (select (fin_ - debut_)) + 1;
					IF(jour_repos > 30) THEN -- cas des mois de 31 jours
						-- On verifi s'il y'a un congé technique
						SELECT INTO jour_repos SUM(p._valeur) FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element IN ('Ct');
						IF(jour_repos IS NOT NULL AND jour_repos > 0) THEN
							UPDATE table_presence_durees_by_employe SET _valeur =  _valeur - 1 WHERE _employe = employe_ AND _element = 'Ct';
						-- ELSE										
-- 							SELECT INTO jour_repos SUM(p._valeur) FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element IN ('Ab');
-- 							IF(jour_repos>0) THEN
-- 								valeur_ = valeur_ - 1;
-- 							END IF;
						END IF;
					END IF;
					jour_repos = (fin_ - debut_) + 1;
					IF(jour_repos < 30) THEN --cas du mois de février 
						valeur_ = valeur_ + (30 - jour_repos);
					END IF;
					IF(valeur_ >= 30) THEN
						-- On recupere les jours d'absence
						SELECT INTO jour_repos SUM(p._valeur) FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element IN ('Ab');
						IF(jour_repos > 0) THEN
							valeur_ = valeur_ - jour_repos;
						END IF;
					END IF;			
					IF(valeur_ > 30) THEN
						valeur_ = 30;
					END IF;
				END IF;	
				INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, 'Je', 'Journée effective de travail');
			END IF;
		END IF;
				
		RETURN QUERY SELECT * FROM table_presence_durees_by_employe ORDER BY _agence, _employe;
	ELSIF(agence_ IS NOT NULL AND agence_ > 0)THEN
		CREATE TEMP TABLE IF NOT EXISTS  table_presence_durees_by_agence(_agence bigint, _employe bigint, _valeur double precision, _element character varying); 
		DELETE FROM table_presence_durees_by_agence;
		date_ = debut_;
		WHILE(date_ <= fin_) LOOP
			type_ = '';
			line_ = null;
			valeur_ = 0;
			repos_ = false;	
			
			jour_ = (select convert_integer_to_jourweek(extract(DOW from date_)::integer));	
			-- Vérifie si cette journées est ouvrée
			SELECT INTO line_ jo.id FROM yvs_jours_ouvres jo WHERE jo.calendrier = (SELECT c.calendrier FROM yvs_grh_contrat_emps c INNER JOIN yvs_grh_employes e ON c.employe = e.id WHERE e.agence = agence_ AND c.contrat_principal IS TRUE LIMIT 1) AND jo.jour=jour_ AND jo.jour_de_repos IS TRUE;
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				repos_ = true;
			END IF;	
			-- Vérifie s'il l'employé est présent normal
			SELECT INTO line_ COUNT(p.id) as id FROM yvs_grh_presence p INNER JOIN yvs_grh_employes e ON p.employe = e.id WHERE e.agence = agence_ AND p.valider IS TRUE AND p.date_debut = date_ AND ((total_heure_sup IS NULL OR total_heure_sup <= 0) AND (total_heure_compensation IS NULL OR total_heure_compensation <= 0));
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				type_ = 'Jn';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + line_.id WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, line_.id, type_);
					END IF;
				END IF;
			END IF;
			-- Vérifie s'il l'employé est présent compensatoire
			SELECT INTO line_ COUNT(p.id) as id FROM yvs_grh_presence p INNER JOIN yvs_grh_employes e ON p.employe = e.id WHERE e.agence = agence_ AND p.valider IS TRUE AND p.date_debut = date_ AND total_heure_compensation > 0;
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				type_ = 'Jc';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + line_.id WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, line_.id, type_);
					END IF;
				END IF;
			END IF;
			-- Vérifie s'il l'employé est présent supplementaire
			SELECT INTO line_ COUNT(p.id) as id FROM yvs_grh_presence p INNER JOIN yvs_grh_employes e ON p.employe = e.id WHERE e.agence = agence_ AND p.valider IS TRUE AND p.date_debut = date_ AND total_heure_sup > 0;
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				type_ = 'Js';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + line_.id WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, line_.id, type_);
					END IF;
				END IF;
			END IF;
			-- Vérifie s'il l'employé est en mission
			SELECT INTO line_ COUNT(m.id) as id FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE e.agence = agence_ AND (date_ BETWEEN m.date_debut AND m.date_fin) AND (m.statut_mission IN ('V', 'T', 'C'));
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				type_ = 'Mi';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + line_.id WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, line_.id, type_);
					END IF;
				END IF;
			END IF;
			-- Vérifie s'il l'employé est en formation
			SELECT INTO line_ COUNT(f.id) as id FROM yvs_grh_formation_emps f INNER JOIN yvs_grh_formation m ON f.formation = m.id INNER JOIN yvs_grh_employes e ON f.employe = e.id WHERE e.agence = agence_ AND (date_ BETWEEN f.date_debut AND f.date_fin) AND (m.statut_formation IN ('V', 'T', 'C'));
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				type_ = 'Fo';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + line_.id WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, line_.id, type_);
					END IF;
				END IF;
			END IF;
			-- Vérifie s'il l'employé est en congé maladie
			SELECT INTO line_ COUNT(co.id) as id FROM yvs_grh_conge_emps co INNER JOIN yvs_grh_employes e ON co.employe = e.id WHERE e.agence = agence_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'Maladie';
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				type_ = 'Cm';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + line_.id WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, line_.id, type_);
					END IF;
				END IF;
			END IF;
			-- Vérifie s'il l'employé est en congé annuel
			SELECT INTO line_ COUNT(co.id) as id FROM yvs_grh_conge_emps co INNER JOIN yvs_grh_employes e ON co.employe = e.id WHERE e.agence = agence_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'Annuel';
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				type_ = 'Ca';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + line_.id WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, line_.id, type_);
					END IF;
				END IF;
			END IF;
			-- Vérifie s'il l'employé est en congé technique
			SELECT INTO line_ COUNT(co.id) as id FROM yvs_grh_conge_emps co INNER JOIN yvs_grh_employes e ON co.employe = e.id WHERE e.agence = agence_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'CT';
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				type_ = 'Ct';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + line_.id WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, line_.id, type_);
					END IF;
				END IF;
			END IF;
			-- Vérifie s'il l'employé est en permission courte durée
			SELECT INTO line_ COUNT(co.id) as id FROM yvs_grh_conge_emps co INNER JOIN yvs_grh_employes e ON co.employe = e.id WHERE e.agence = agence_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='P' AND co.duree_permission = 'C';
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				type_ = 'Pc';
				description_ = 'Permission courte durée';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + line_.id WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, line_.id, type_);
					END IF;
				END IF;
			END IF;
			-- Vérifie s'il l'employé est en permission longue durée
			SELECT INTO line_ COUNT(co.id) as id FROM yvs_grh_conge_emps co INNER JOIN yvs_grh_employes e ON co.employe = e.id WHERE e.agence = agence_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='P' AND co.duree_permission = 'L';
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				type_ = 'Pl';
				description_ = 'Permission longue durée';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + line_.id WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, line_.id, type_);
					END IF;
				END IF;
			END IF;
			-- Verifier si le jour n'est pas un jour férié
			SELECT INTO line_ j.* FROM yvs_jours_feries j WHERE (j.jour=date_ AND j.all_year IS FALSE) OR (alter_date(j.jour, 'year', date_)=date_ AND j.all_year IS TRUE);
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN 
				type_ = 'Fe';
				IF((line_ IS NOT NULL and line_.id IS NOT NULL) and type_ != '')THEN
					SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element = type_;
					IF(valeur_ IS NOT NULL)THEN
						UPDATE table_presence_durees_by_agence SET _valeur =  valeur_ + 1 WHERE _agence = agence_ AND _element = type_;
					ELSE
						INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, 1, type_);
					END IF;
				END IF;
			END IF;
			date_ = date_ + interval '1 day';								
		END LOOP;
		-- Recuperation de la durée de travail requis
		jour_repos = (SELECT COUNT(e.id) FROM yvs_grh_employes e WHERE e.agence = agence_);
		INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, (((select (fin_ - debut_)) + 1) * jour_repos), 'Tr');
		-- Recuperation de la durée de travail effectif
		SELECT INTO valeur_ SUM(p._valeur) FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element IN ('Js','Jn','Jc');
		IF(valeur_ IS NOT NULL AND valeur_ > 0)THEN
			INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, valeur_, 'Je');
		END IF;
		-- Recuperation de la durée d'absence
		SELECT INTO jour_repos SUM(p._valeur) FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element IN ('Je','Mi','Fo','Pc','Pl','Fe');	
		IF(jour_repos IS NULL OR jour_repos < 1)THEN
			jour_repos = 0;
		END IF;	
		SELECT INTO valeur_ p._valeur FROM table_presence_durees_by_agence p WHERE p._agence = agence_ AND p._element IN ('Tr');	
		IF(valeur_ IS NULL OR valeur_ < 1)THEN
			valeur_ = 0;
		END IF;	
		IF(valeur_ - jour_repos > 0)THEN
			INSERT INTO table_presence_durees_by_agence VALUES(agence_, 0, (valeur_ - jour_repos), 'Ab');
		END IF;
				
		RETURN QUERY SELECT * FROM table_presence_durees_by_agence ORDER BY _agence, _element;
	ELSIF(societe_ IS NOT NULL AND societe_ > 0)THEN
		CREATE TEMP TABLE IF NOT EXISTS  table_presence_durees_by_societe(_agence bigint, _employe bigint, _valeur double precision, _element character varying); 
		DELETE FROM table_presence_durees_by_societe;
		FOR agence_ IN SELECT a.id FROM yvs_agences a WHERE a.societe = societe_
		LOOP
			INSERT INTO table_presence_durees_by_societe SELECT y.agence, y.employe, y.valeur, y.element FROM grh_presence_durees(0, agence_, societe_, debut_, fin_) y;
		END LOOP;
		RETURN QUERY SELECT * FROM table_presence_durees_by_societe ORDER BY _agence, _employe;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_presence_durees(bigint, bigint, bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION grh_presence_durees(bigint, bigint, bigint, date, date) IS 'en fontion du type de résultat, retourne le nombre de présence, d''absence, de mission, 
de congé autorisé, de congé technique, de congé non autorisé d''un employé sur une période';
