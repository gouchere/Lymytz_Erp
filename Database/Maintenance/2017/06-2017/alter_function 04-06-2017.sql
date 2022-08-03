-- Function: grh_et_livre_paie(bigint, bigint, character varying, character varying, character varying)

-- DROP FUNCTION grh_et_livre_paie(bigint, bigint, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_livre_paie(IN societe_ bigint, IN header_ bigint, IN agence_ character varying, IN code_ character varying, IN type_ character varying)
  RETURNS TABLE(regle bigint, numero integer, libelle character varying, groupe bigint, element bigint, montant double precision, rang integer, is_group boolean, is_total boolean) AS
$BODY$
declare 
	titre_ character varying default 'LIVRE DE PAIE';
	
	element_ record;
	regles_ record;
	second_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
	is_total_ boolean default false;
	query_ character varying;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_livre_paie;
	CREATE TEMP TABLE IF NOT EXISTS table_livre_paie(_regle bigint, _numero integer, _libelle character varying, _groupe bigint, _element bigint, _montant double precision, _rang integer, _is_group boolean, _is_total boolean);
	delete from table_livre_paie;
	if((header_ is not null and header_ > 0) and (type_ is not null))then
		for regles_ in select e.element_salaire as id, e.libelle, coalesce(y.num_sequence, 0) as numero, coalesce(e.ordre, 0) as ordre, y.retenue, e.groupe_element as groupe from yvs_stat_grh_element_dipe e 
		inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.libelle = titre_ and s.societe = societe_ order by e.ordre
		loop
			somme_ = 0;
			if(regles_.numero = 0)then
				is_total_ = true;
			else
				is_total_ = false;
			end if;
			if(type_ = 'E')then
				for element_ in select distinct(e.id) as id from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
				where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
				loop
					if(regles_.retenue is false)then
						select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regles_.id and c.employe = element_.id and b.entete = header_;
					else
						select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regles_.id and c.employe = element_.id and b.entete = header_;
					end if;
					valeur_ = coalesce(valeur_, 0);
					insert into table_livre_paie values(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, valeur_, regles_.ordre, false, is_total_);
					somme_ = somme_ + valeur_;
				end loop;
			elsif(type_ = 'S')then
				for element_ in select distinct(d.id) as id, d.print_with_children from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id
				inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id inner join yvs_grh_departement d on p.departement = d.id where d.visible_on_livre_paie is true 
				and e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
				loop
					if(regles_.retenue is false)then
						select into valeur_ coalesce(sum(d.montant_payer), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
							inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id inner join yvs_grh_departement de on p.departement = de.id 
							where d.element_salaire = regles_.id and b.entete = header_ and e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) 
							and (p.departement = element_.id or (element_.print_with_children is true and p.departement in (select id from grh_get_sous_service(element_.id, true))));
					else
						select into valeur_ coalesce(sum(d.retenu_salariale), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
							inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id inner join yvs_grh_departement de on p.departement = de.id 
							where d.element_salaire = regles_.id and b.entete = header_ and e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) 
							and (p.departement = element_.id or (element_.print_with_children is true and p.departement in (select id from grh_get_sous_service(element_.id, true))));
					end if;
-- 					RAISE NOTICE 'query_ %', query_;
					valeur_ = coalesce(valeur_, 0);
					insert into table_livre_paie values(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, valeur_, regles_.ordre, false, is_total_);
					somme_ = somme_ + valeur_;
				end loop;
			end if;
			if(somme_ = 0)then
				delete from table_livre_paie where _regle = regles_.id;
			end if;
		end loop;
		
		-- Récuperation les totaux par groupe
		for regles_ in select distinct _groupe as groupe, g.libelle from table_livre_paie y left join yvs_stat_grh_groupe_element g on y._groupe = g.id where y._groupe > 0
		loop
			for second_ in select _element, sum(_montant) as valeur_, count(_rang) ordre from table_livre_paie y where y._groupe = regles_.groupe group by _element
			loop
				insert into table_livre_paie values(0, 0, regles_.libelle, regles_.groupe, second_._element, second_.valeur_, (second_.ordre + 1), true, false);
			end loop;
		end loop;
	end if;
	return QUERY select * from table_livre_paie order by _is_total, _groupe, _rang, _element;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_livre_paie(bigint, bigint, character varying, character varying, character varying)
  OWNER TO postgres;

  
  
  -- Function: grh_presence_durees(bigint, bigint, bigint, date, date)

-- DROP FUNCTION grh_presence_durees(bigint, bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION grh_presence_durees(IN employe_ bigint, IN agence_ bigint, IN societe_ bigint, IN debut_ date, IN fin_ date)
  RETURNS TABLE(agence bigint, employe bigint, valeur double precision, element character varying) AS
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
	arrondi_result_ boolean default false;
	duree_annee integer default ((('31-12-'||(select extract(year from fin_)))::date) - (('01-01-'||(select extract(year from fin_)))::date) + 1);

BEGIN 	
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
		CREATE TEMP TABLE IF NOT EXISTS  table_presence_durees_by_employe(_agence bigint, _employe bigint, _valeur double precision, _element character varying); 
		DELETE FROM table_presence_durees_by_employe;
		IF(agence_ IS NULL OR agence_ < 1)THEN
			SELECT INTO agence_ e.agence FROM yvs_grh_employes e WHERE e.id = employe_;
		END IF;
		IF(societe_ IS NULL OR societe_ < 1)THEN
			SELECT INTO societe_ e.societe FROM yvs_agences e WHERE e.id = agence_;
		END IF;
		date_ = debut_;

		jour_repos = (fin_ - debut_) + 1;
		
		
		WHILE(date_ <= fin_) LOOP
			type_ = '';
			line_ = null;
			valeur_ = 0;
			repos_ = false;	
			ferier_ = false;
			
			jour_ = (select convert_integer_to_jourweek(extract(DOW from date_)::integer));	
			-- Vérifie si cette journées est ouvrée
			SELECT INTO line_ jo.id FROM yvs_jours_ouvres jo where jo.calendrier = (select c.calendrier FROM yvs_grh_contrat_emps c where c.employe=employe_ and c.contrat_principal is true limit 1) AND jo.jour=jour_ AND (jo.jour_de_repos IS TRUE OR jo.ouvrable IS FALSE);
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				repos_ = true;
			END IF;	
			-- Vérifie si cette journées est feriée
			SELECT INTO line_ j.id FROM yvs_jours_feries j WHERE (j.jour=date_ AND j.all_year IS FALSE) OR (alter_date(j.jour, 'year', date_)=date_ AND j.all_year IS TRUE) AND j.societe = societe_;
			IF (line_ IS NOT NULL AND line_.id IS NOT NULL) THEN 
				ferier_ = true;
			END IF;
			-- Vérifie si l'employé est présent normal
			SELECT INTO line_ p.id, p.total_heure_compensation, p.total_heure_sup, valider_hs FROM yvs_grh_presence p WHERE p.employe = employe_ AND p.valider IS TRUE AND p.date_debut = date_;
			IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
				valeur_ = 1;
				IF(ferier_)THEN
					IF(line_.valider_hs)THEN
						-- Vérifie s'il l'employé est présent compensatoire
						IF (line_.total_heure_compensation > 0) THEN
							valeur_ = line_.total_heure_compensation / 8;
							type_ = 'Jcf';
							SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
							IF(jour_repos IS NOT NULL)THEN
								UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
							ELSE
								INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_);
							END IF;
						END IF;
						-- Vérifie s'il l'employé est présent supplementaire férié
						IF (line_.total_heure_sup > 0) THEN
							valeur_ = line_.total_heure_sup / 8;
							type_ = 'Jsf';
							SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
							IF(jour_repos IS NOT NULL)THEN
								UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
							ELSE
								INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_);
							END IF;
							RAISE NOTICE 'date_ %', date_;
						END IF;
					END IF;
					IF(repos_ IS FALSE )THEN
						-- Vérifie s'il l'employé est présent un jour ferié
						valeur_ = 1;
						type_ = 'Jnf';
						SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
						IF(jour_repos IS NOT NULL)THEN
							UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
						ELSE
							INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_);
						END IF;		
					END IF;
					type_ = 'Fe';
				ELSE
					IF(line_.valider_hs)THEN
						-- Vérifie s'il l'employé est présent compensatoire
						IF (line_.total_heure_compensation > 0) THEN
							valeur_ = line_.total_heure_compensation / 8;
							type_ = 'Jc';
							SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
							IF(jour_repos IS NOT NULL)THEN
								UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
							ELSE
								INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_);
							END IF;
						END IF;
						-- Vérifie s'il l'employé est présent supplementaire
						IF (line_.total_heure_sup > 0) THEN
							valeur_ = line_.total_heure_sup / 8;
							type_ = 'Js';
							SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
							IF(jour_repos IS NOT NULL)THEN
								UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
							ELSE
								INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_);
							END IF;
						END IF;
					END IF;

					valeur_ = 1;
					IF(repos_)THEN
						type_ = 'Jnr';
					ELSE
						type_ = 'Jn';
					END IF;
				END IF;
			ELSE
				-- Vérifie s'il l'employé est en mission
				SELECT INTO line_ m.id FROM yvs_grh_missions m WHERE m.employe=employe_ AND (date_ BETWEEN m.date_debut AND m.date_fin) AND (m.statut_mission IN ('V', 'T', 'C'));
				IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
					valeur_ = 1;
					type_ = 'Mi';
				ELSE
					-- Vérifie s'il l'employé est en formation
					SELECT INTO line_ f.id FROM yvs_grh_formation_emps f INNER JOIN yvs_grh_formation m ON f.formation = m.id WHERE f.employe=employe_ AND (date_ BETWEEN f.date_debut AND f.date_fin) AND (m.statut_formation IN ('V', 'T', 'C'));
					IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
						valeur_ = 1;
						type_ = 'Fo';
					ELSE
						-- Vérifie s'il l'employé est en congé maladie
						SELECT INTO line_ co.id, COALESCE(co.compter_jour_repos, FALSE) AS compter_jour_repos, co.effet FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'Maladie';
						IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
							valeur_ = 1;
							IF(line_.compter_jour_repos is false and ferier_)THEN
								type_ = 'Fe';
							ELSIF(line_.compter_jour_repos is false and repos_)THEN
								type_ = 'Re';
							ELSE
								IF(line_.effet = 'AUTORISE' OR line_.effet = 'SPECIALE')THEN
									valeur_ = 1;
									type_ = 'Jn';
									SELECT INTO jour_repos p._valeur FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element = type_;
									IF(jour_repos IS NOT NULL)THEN
										UPDATE table_presence_durees_by_employe SET _valeur =  valeur_ + jour_repos WHERE _employe = employe_ AND _element = type_;
									ELSE
										INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_);
									END IF;
								END IF;
								type_ = 'Cm';
							END IF;
						ELSE
							-- Vérifie s'il l'employé est en congé annuel	
							SELECT INTO line_ co.id, COALESCE(co.compter_jour_repos, FALSE) AS compter_jour_repos, co.effet FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='C' AND co.type_conge = 'Annuel';
							IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
								valeur_ = 1;
								IF(line_.compter_jour_repos is false and ferier_)THEN
									type_ = 'Fe';
								ELSIF(line_.compter_jour_repos is false and repos_)THEN
									type_ = 'Re';
								ELSE
									type_ = 'Ca';
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
									ELSE
										type_ = 'Ct';
									END IF;
								ELSE
									-- Vérifie s'il l'employé est en permission courte durée
									SELECT INTO line_ co.id FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND date_ BETWEEN co.date_debut AND co.date_fin AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='P' AND co.duree_permission = 'C';
									IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
										valeur_ = 1;
										type_ = 'Pc';
									ELSE
										-- Vérifie s'il l'employé est en permission longue durée
										SELECT INTO line_ co.id, co.effet, COALESCE(co.compter_jour_repos, FALSE) AS compter_jour_repos FROM yvs_grh_conge_emps co WHERE co.employe=employe_ AND (date_ BETWEEN co.date_debut AND co.date_fin) AND (co.statut IN ('V', 'T' ,'C')) AND co.nature='P' AND co.duree_permission = 'L';
										IF (line_ IS NOT NULL and line_.id IS NOT NULL) THEN
											valeur_ = 1;
											IF(line_.compter_jour_repos is false and ferier_)THEN
												type_ = 'Fe';
											ELSIF(line_.compter_jour_repos is false and repos_)THEN
												type_ = 'Re';
											ELSE
												IF(line_.effet = 'AUTORISE' OR line_.effet = 'SPECIALE')THEN
													valeur_ = 1;
													type_ = 'Jn';
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
												ELSE
													valeur_ = 1;
													type_ = 'Ab';
												END IF;
											ELSE
												valeur_ = 1;
												type_ = 'Re';
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
					INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, type_);
				END IF;
			END IF;
			date_ = date_ + interval '1 day';								
		END LOOP;
		-- Recuperation de la durée de travail requis
		FOR jour_ IN SELECT jo.jour FROM yvs_jours_ouvres jo where jo.calendrier = (select c.calendrier FROM yvs_grh_contrat_emps c where c.employe=employe_ and c.contrat_principal is true limit 1) AND (jo.jour_de_repos IS TRUE OR jo.ouvrable IS FALSE)
		LOOP
			str_repos_= str_repos_ ||','||(select convert_jourweek_to_integer(jour_));
		END LOOP;
		jour_repos = (SELECT COUNT(current_) FROM generate_series(debut_, fin_, '1 day'::interval) current_  WHERE ((SELECT EXTRACT(DOW FROM current_))::character in (select val from regexp_split_to_table(str_repos_,',') val)));
		INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, ((select (fin_ - debut_)) +1 - jour_repos), 'Tr');
		-- Recuperation de la durée de repos requis
		INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, jour_repos, 'Rr');
		-- Recuperation de la durée de travail effectif
		SELECT INTO valeur_ SUM(p._valeur) FROM table_presence_durees_by_employe p WHERE p._employe = employe_ AND p._element IN ('Jn','Mi','Fo');
		-- On verifi s'il y'a les fiches de présence
		IF(valeur_ IS NOT NULL AND valeur_ > 0) THEN
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
				INSERT INTO table_presence_durees_by_employe VALUES(agence_, employe_, valeur_, 'Je');
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


DROP FUNCTION grh_et_journal_paye_gain(bigint, character varying, bigint);
CREATE OR REPLACE FUNCTION grh_et_journal_paye_gain(IN societe_ bigint, IN agence_ character varying, IN header_ character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, element character varying, valeur character varying, rang integer, is_double boolean) AS
$BODY$
declare 
	titre_ character varying default 'GEN_JS';
	
	employe_ record;
	element_ record;	
	valeur_ double precision default 0; 
	somme_ double precision default 0; 
	ordre_ integer default 0; 
   
begin 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	CREATE TEMP TABLE IF NOT EXISTS table_journal_paye(_employe bigint, _matricule character varying, _nom character varying, _element character varying, _valeur character varying, _rang integer, _is_double boolean);
	delete from table_journal_paye;
	
	if((header_ is not null and header_ != ''))then	
		for employe_ in select distinct(e.id) as id, e.matricule, concat(e.nom, ' ', e.prenom) as nom, ca.categorie, ec.echelon from yvs_grh_bulletins b 
			inner join yvs_grh_contrat_emps c on b.contrat = c.id 
			inner join yvs_grh_employes e on c.employe = e.id 
			inner join yvs_grh_convention_employe ce on ce.employe = e.id 
			inner join yvs_grh_convention_collective cc on cc.id = ce.convention
			inner join yvs_grh_echelons ec on cc.echellon = ec.id
			inner join yvs_grh_categorie_professionelle ca on cc.categorie = ca.id
			where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) and ce.actif is true
		loop
			insert into table_journal_paye values(employe_.id, employe_.matricule, employe_.nom, 'CAT  EC', concat(employe_.categorie,'  ',employe_.echelon), 0, false);
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e 
			inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id 
			where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				RAISE NOTICE 'element %',element_.libelle;
				ordre_ = element_.ordre;
				if(element_.retenue is true)then
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
					inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id
					where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id and c.employe = employe_.id;
				elsif(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_element_salaire e on d.element_salaire = e.id 
					inner join yvs_grh_bulletins b on d.bulletin = b.id inner join yvs_grh_contrat_emps c on b.contrat = c.id
					where b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) and e.id = element_.id and c.employe = employe_.id;
				else
					valeur_ = 0;
				end if;
				somme_ = somme_ + coalesce(valeur_, 0);
				insert into table_journal_paye values(employe_.id, employe_.matricule, employe_.nom, element_.libelle, valeur_::character varying, ordre_, true);
			end loop;
			if(somme_ = 0)then
				delete from table_journal_paye where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_journal_paye order by _nom, _matricule, _rang;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_journal_paye_gain(bigint, character varying, character varying)
  OWNER TO postgres;
  

DROP FUNCTION grh_et_livre_paie(bigint, bigint, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION grh_et_livre_paie(IN societe_ bigint, IN header_ character varying, IN agence_ character varying, IN code_ character varying, IN type_ character varying)
  RETURNS TABLE(regle bigint, numero integer, libelle character varying, groupe bigint, element bigint, montant double precision, rang integer, is_group boolean, is_total boolean) AS
$BODY$
declare 
	titre_ character varying default 'LIVRE DE PAIE';
	
	element_ record;
	regles_ record;
	second_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
	is_total_ boolean default false;
	query_ character varying;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_livre_paie;
	CREATE TEMP TABLE IF NOT EXISTS table_livre_paie(_regle bigint, _numero integer, _libelle character varying, _groupe bigint, _element bigint, _montant double precision, _rang integer, _is_group boolean, _is_total boolean);
	delete from table_livre_paie;
	if((header_ is not null and header_ != '') and (type_ is not null))then
		for regles_ in select e.element_salaire as id, e.libelle, coalesce(y.num_sequence, 0) as numero, coalesce(e.ordre, 0) as ordre, y.retenue, e.groupe_element as groupe from yvs_stat_grh_element_dipe e 
		inner join yvs_stat_grh_etat s on s.id = e.etat inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.libelle = titre_ and s.societe = societe_ order by e.ordre
		loop
			somme_ = 0;
			if(regles_.numero = 0)then
				is_total_ = true;
			else
				is_total_ = false;
			end if;
			if(type_ = 'E')then
				for element_ in select distinct(e.id) as id from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
				where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
				loop
					if(regles_.retenue is false)then
						select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regles_.id and c.employe = element_.id 
							and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
					else
						select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regles_.id and c.employe = element_.id 
							and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
					end if;
					valeur_ = coalesce(valeur_, 0);
					insert into table_livre_paie values(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, valeur_, regles_.ordre, false, is_total_);
					somme_ = somme_ + valeur_;
				end loop;
			elsif(type_ = 'S')then
				for element_ in select distinct(d.id) as id, d.print_with_children from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id
				inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id inner join yvs_grh_departement d on p.departement = d.id where d.visible_on_livre_paie is true 
				and e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
				loop
					if(regles_.retenue is false)then
						select into valeur_ coalesce(sum(d.montant_payer), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
							inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id inner join yvs_grh_departement de on p.departement = de.id 
							where d.element_salaire = regles_.id and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) 
							and e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) 
							and (p.departement = element_.id or (element_.print_with_children is true and p.departement in (select id from grh_get_sous_service(element_.id, true))));
					else
						select into valeur_ coalesce(sum(d.retenu_salariale), 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
							inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
							inner join yvs_grh_poste_de_travail p on e.poste_actif = p.id inner join yvs_grh_departement de on p.departement = de.id 
							where d.element_salaire = regles_.id and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val) 
							and e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val) 
							and (p.departement = element_.id or (element_.print_with_children is true and p.departement in (select id from grh_get_sous_service(element_.id, true))));
					end if;
-- 					RAISE NOTICE 'query_ %', query_;
					valeur_ = coalesce(valeur_, 0);
					insert into table_livre_paie values(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, valeur_, regles_.ordre, false, is_total_);
					somme_ = somme_ + valeur_;
				end loop;
			end if;
			if(somme_ = 0)then
				delete from table_livre_paie where _regle = regles_.id;
			end if;
		end loop;
		
		-- Récuperation les totaux par groupe
		for regles_ in select distinct _groupe as groupe, g.libelle from table_livre_paie y left join yvs_stat_grh_groupe_element g on y._groupe = g.id where y._groupe > 0
		loop
			for second_ in select _element, sum(_montant) as valeur_, count(_rang) ordre from table_livre_paie y where y._groupe = regles_.groupe group by _element
			loop
				insert into table_livre_paie values(0, 0, regles_.libelle, regles_.groupe, second_._element, second_.valeur_, (second_.ordre + 1), true, false);
			end loop;
		end loop;
	end if;
	return QUERY select * from table_livre_paie order by _is_total, _groupe, _rang, _element;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_livre_paie(bigint, character varying, character varying, character varying, character varying)
  OWNER TO postgres;

  
DROP FUNCTION grh_et_recap_dipe_cf(bigint, character varying, bigint);
CREATE OR REPLACE FUNCTION grh_et_recap_dipe_cf(IN societe_ bigint, IN agence_ character varying, IN header_ character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, entete character varying, valeur double precision, montant double precision, is_taux boolean) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_CF';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
	taux_ double precision default 0;
	ordre_ integer default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_cf;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_cf(_employe bigint, _matricule character varying, _nom character varying, _entete character varying, _valeur double precision, _montant double precision, _is_taux boolean);
	delete from table_recap_dipe_cf;
	if(header_ is not null and header_ != '')then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop			
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				else
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				end if;
				valeur_ = coalesce(valeur_, 0);
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, element_.libelle, ordre_, valeur_, false);
				somme_ = somme_ + valeur_;
			end loop;
			
			ordre_ = ordre_ + 1;
			select into valeur_ coalesce(y._montant, 0) from table_recap_dipe_cf y where y._employe = employe_.id and y._valeur = 1;
			select into taux_ coalesce(y.taux, 0) from yvs_stat_grh_taux_contribution y inner join yvs_stat_grh_etat s on s.id = y.etat where s.code = titre_ and s.societe = societe_ and y.type_taux = 'P' limit 1;
			if(taux_ > 0)then
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'Base CFPat', ordre_, valeur_, false);
				ordre_ = ordre_ + 1;
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'Tx Pat', ordre_+10, taux_, true);
				taux_ = valeur_ * taux_ / 100;
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'CF Pat', ordre_, taux_, false);
			end if;
			
			ordre_ = ordre_ + 1;
			select into taux_ coalesce(y.taux, 0) from yvs_stat_grh_taux_contribution y inner join yvs_stat_grh_etat s on s.id = y.etat where s.code = titre_ and s.societe = societe_ and y.type_taux = 'S' limit 1;
			if(taux_ > 0)then
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'Base CFSal', ordre_, valeur_, false);
				ordre_ = ordre_ + 1;
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'Tx Sal', ordre_+10, taux_, true);
				taux_ = valeur_ * taux_ / 100;
				insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'CF Sal', ordre_, taux_, false);
			end if;
			
			ordre_ = ordre_ + 1;
			select into valeur_ sum(coalesce(y._montant, 0)) from table_recap_dipe_cf y where y._employe = employe_.id and y._valeur in (3,5);
			insert into table_recap_dipe_cf values(employe_.id, employe_.matricule, employe_.nom, 'Total CF', ordre_, valeur_, false);
			
			if(somme_ = 0)then
				delete from table_recap_dipe_cf where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_cf order by _matricule, _valeur;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_cf(bigint, character varying, character varying)
  OWNER TO postgres;
  
  
 DROP FUNCTION grh_get_valeur_element_dipe(bigint, bigint, bigint);
CREATE OR REPLACE FUNCTION grh_get_valeur_element_dipe(element_ bigint, header_ character varying, employe_ bigint)
  RETURNS double precision AS
$BODY$
declare 
	valeur_ double precision default 0; 
	type_ character varying default 'V';
	retenue_ boolean default false;
	regle_ record;
begin 	
	select into regle_ * from yvs_stat_grh_element_dipe where id = element_;
	if(regle_.id is not null)then
		type_ = regle_.champ_valeur;
		if(type_ is not null and type_ != '')then
			CASE type_
				WHEN 'B' THEN 
					select into valeur_ coalesce(d.base, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'Q' THEN 
					select into valeur_ coalesce(d.quantite, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'TS' THEN 
					select into valeur_ coalesce(d.taux_salarial, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'TP' THEN 
					select into valeur_ coalesce(d.taux_patronal, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'TSP' THEN 
					select into valeur_ coalesce(d.taux_patronal, 0)+coalesce(d.taux_salarial, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'RS' THEN 
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				WHEN 'RP' THEN 
					select into valeur_ coalesce(d.montant_employeur, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				ELSE
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
			END CASE;
		else
			select into retenue_ retenue from yvs_grh_element_salaire where id = regle_.element_salaire;
			if(element_.retenue is false)then
				select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
					and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
			else
				select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = regle_.element_salaire and c.employe = employe_ 
					and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
			end if;
		end if;	 
	end if;
	return COALESCE(valeur_, 0);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_get_valeur_element_dipe(bigint, character varying, bigint)



DROP FUNCTION grh_et_recap_dipe_cnps(bigint, character varying, bigint, character varying);
CREATE OR REPLACE FUNCTION grh_et_recap_dipe_cnps(IN societe_ bigint, IN agence_ character varying, IN header_ character varying, IN sommable_ character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, numero character varying, entete character varying, rang integer, montant double precision) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_CNPS2';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	ordre_ integer default 0; 
	valeur_ double precision default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_cnps;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_cnps(_employe bigint, _matricule character varying, _nom character varying, _numero character varying, _entete character varying, _rang integer, _montant double precision);
	delete from table_recap_dipe_cnps;
	if(header_ is not null and header_ != '')then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom, e.matricule_cnps as numero from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.id as ids, e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				valeur_ = (select grh_get_valeur_element_dipe(element_.ids, header_, employe_.id));
				
				insert into table_recap_dipe_cnps values(employe_.id, employe_.matricule, employe_.nom, employe_.numero, element_.libelle, ordre_, valeur_);
				somme_ = somme_ + valeur_;
			end loop;		
			if(sommable_ is not null and sommable_ != '')then	
				select into valeur_ coalesce(sum(_montant), 0) from table_recap_dipe_cnps where _employe = employe_.id and _rang::character varying in (select colonne from regexp_split_to_table(sommable_,',') colonne);
				select into ordre_ max(colonne::integer) from regexp_split_to_table(sommable_,',') colonne;
				insert into table_recap_dipe_cnps values(employe_.id, employe_.matricule, employe_.nom, employe_.numero, 'Total PV', ordre_+1, valeur_);
			end if;
			if(somme_ = 0)then
				delete from table_recap_dipe_cnps where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_cnps order by _matricule, _rang;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_cnps(bigint, character varying, character varying, character varying)
  OWNER TO postgres;
  
  
DROP FUNCTION grh_et_recap_dipe_fne(bigint, character varying, bigint);
CREATE OR REPLACE FUNCTION grh_et_recap_dipe_fne(IN societe_ bigint, IN agence_ character varying, IN header_ character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, entete character varying, valeur double precision, montant double precision, is_taux boolean) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_FNE';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
	taux_ double precision default 0;
	ordre_ integer default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_fne;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_fne(_employe bigint, _matricule character varying, _nom character varying, _entete character varying, _valeur double precision, _montant double precision, _is_taux boolean);
	delete from table_recap_dipe_fne;
	if(header_ is not null and header_ != '')then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop			
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				else
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				end if;
				valeur_ = coalesce(valeur_, 0);
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, element_.libelle, ordre_, valeur_, false);
				somme_ = somme_ + valeur_;
			end loop;
			
			ordre_ = ordre_ + 1;
			select into valeur_ coalesce(y._montant, 0) from table_recap_dipe_fne y where y._employe = employe_.id and y._valeur = 1;
			select into taux_ coalesce(y.taux, 0) from yvs_stat_grh_taux_contribution y inner join yvs_stat_grh_etat s on s.id = y.etat where s.code = titre_ and s.societe = societe_ and y.type_taux = 'P' limit 1;
			if(taux_ > 0)then
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'Base CFPat', ordre_, valeur_, false);
				ordre_ = ordre_ + 1;
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'Tx Pat', ordre_+10, taux_, true);
				taux_ = valeur_ * taux_ / 100;
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'CF Pat', ordre_, taux_, false);
			end if;
			
			ordre_ = ordre_ + 1;
			select into taux_ coalesce(y.taux, 0) from yvs_stat_grh_taux_contribution y inner join yvs_stat_grh_etat s on s.id = y.etat where s.code = titre_ and s.societe = societe_ and y.type_taux = 'S' limit 1;
			if(taux_ > 0)then
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'Base CFSal', ordre_, valeur_, false);
				ordre_ = ordre_ + 1;
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'Tx Sal', ordre_+10, taux_, true);
				taux_ = valeur_ * taux_ / 100;
				insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'CF Sal', ordre_, taux_, false);
			end if;
			
			ordre_ = ordre_ + 1;
			select into valeur_ sum(coalesce(y._montant, 0)) from table_recap_dipe_fne y where y._employe = employe_.id and y._valeur in (3);
			insert into table_recap_dipe_fne values(employe_.id, employe_.matricule, employe_.nom, 'Total CF', ordre_, valeur_, false);
			
			if(somme_ = 0)then
				delete from table_recap_dipe_fne where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_fne order by _matricule, _valeur;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_fne(bigint, character varying, character varying)
  OWNER TO postgres;
  
  
  
DROP FUNCTION grh_et_recap_dipe_irpp2(bigint, character varying, bigint, character varying);
CREATE OR REPLACE FUNCTION grh_et_recap_dipe_irpp2(IN societe_ bigint, IN agence_ character varying, IN header_ character varying, IN sommable character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, entete character varying, rang integer, montant double precision) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_IRPP_2';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	ordre_ integer default 0; 
	valeur_ double precision default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_irpp;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_irpp(_employe bigint, _matricule character varying, _nom character varying, _entete character varying, _rang integer, _montant double precision);
	delete from table_recap_dipe_irpp;
	if(header_ is not null and header_ != '')then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.element_salaire as id, e.libelle, coalesce(e.ordre, 0) as ordre, y.retenue from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				ordre_ = element_.ordre;
				if(element_.retenue is false)then
					select into valeur_ coalesce(d.montant_payer, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				else
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val);
				end if;

				valeur_ = coalesce(valeur_, 0);
				insert into table_recap_dipe_irpp values(employe_.id, employe_.matricule, employe_.nom, element_.libelle, ordre_, valeur_);
				somme_ = somme_ + valeur_;
			end loop;	
			if(sommable is not null and sommable != '')then	
				select into valeur_ coalesce(sum(_montant), 0) from table_recap_dipe_irpp where _employe = employe_.id and _rang::character varying in (select colonne from regexp_split_to_table(sommable,',') colonne);
				insert into table_recap_dipe_irpp values(employe_.id, employe_.matricule, employe_.nom, 'Total imp', ordre_+1, valeur_);
			end if;
			if(somme_ = 0)then
				delete from table_recap_dipe_irpp where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_irpp order by _matricule, _rang;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_irpp2(bigint, character varying, character varying, character varying)
  OWNER TO postgres;
  
  
  
DROP FUNCTION grh_et_recap_dipe_rav(bigint, character varying, bigint);
CREATE OR REPLACE FUNCTION grh_et_recap_dipe_rav(IN societe_ bigint, IN agence_ character varying, IN header_ character varying)
  RETURNS TABLE(employe bigint, matricule character varying, nom character varying, entete character varying, valeur double precision, montant double precision) AS
$BODY$
declare 
	titre_ character varying default 'DIPE_RAV';
	
	employe_ record;
	element_ record;
	tranche_ record;
	somme_ double precision default 0;
	valeur_ double precision default 0;
   
begin 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_recap_dipe_rav;
	CREATE TEMP TABLE IF NOT EXISTS table_recap_dipe_rav(_employe bigint, _matricule character varying, _nom character varying, _entete character varying, _valeur double precision, _montant double precision);
	delete from table_recap_dipe_rav;
	if(header_ is not null and header_ != '')then	
		for employe_ in select distinct(e.id), e.matricule, concat(e.nom, ' ', e.prenom) as nom from yvs_grh_bulletins b inner join yvs_grh_contrat_emps c on b.contrat = c.id inner join yvs_grh_employes e on c.employe = e.id 
		where e.agence::character varying in (select val from regexp_split_to_table(agence_,',') val)
		loop
			somme_ = 0;
			for element_ in select e.element_salaire as id from yvs_stat_grh_element_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
			inner join yvs_grh_element_salaire y on e.element_salaire = y.id where s.code = titre_ and s.societe = societe_ order by e.ordre
			loop
				for tranche_ in select e.montant, e.tranche_min, e.tranche_max from yvs_stat_grh_grille_dipe e inner join yvs_stat_grh_etat s on s.id = e.etat 
							where s.code = titre_ and s.societe = societe_
				loop				
					select into valeur_ coalesce(d.retenu_salariale, 0) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
						inner join yvs_grh_contrat_emps c on b.contrat = c.id 
						where d.element_salaire = element_.id and c.employe = employe_.id 
						and b.entete::character varying in (select val from regexp_split_to_table(header_,',') val)
						and d.tranche_min <= tranche_.tranche_min and d.tranche_max >= tranche_.tranche_max;

					valeur_ = coalesce(valeur_, 0);
					insert into table_recap_dipe_rav values(employe_.id, employe_.matricule, employe_.nom, 'RAV'||tranche_.montant, tranche_.montant, valeur_);
					somme_ = somme_ + valeur_;
				end loop;
			end loop;
			if(somme_ = 0)then
				delete from table_recap_dipe_rav where _employe = employe_.id;
			end if;
		end loop;
	end if;
	return QUERY select * from table_recap_dipe_rav order by _matricule, _valeur;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_dipe_rav(bigint, character varying, character varying)
  OWNER TO postgres;
  
  
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


