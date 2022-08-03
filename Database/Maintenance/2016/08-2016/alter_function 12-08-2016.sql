-- Function: heure_travail_effectif(bigint, date, date, boolean, boolean)

-- DROP FUNCTION heure_travail_effectif(bigint, date, date, boolean, boolean);

CREATE OR REPLACE FUNCTION heure_travail_effectif(id_ bigint, date_debut_ date, date_fin_ date, compensation_ boolean, supplementaire_ boolean)
  RETURNS double precision AS
$BODY$   
DECLARE 
	employe_ RECORD;
        date_ DATE default '01/01/1990';
	ferier_ RECORD;
	presence_ RECORD;
        jour_ INTEGER;
        calendrier_ INTEGER;
        duree_ TIME;
        total_ double precision default 0;
    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique, co.calendrier from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id
		inner join yvs_agences a on e.agence = a.id where e.id = id_;
	if (employe_.calendrier is null) then
		select into calendrier_ pa.calendrier from yvs_parametre_grh pa where pa.societe = employe_.societe;
	else
		calendrier_ = employe_.calendrier;
	end if;
	if(employe_.horaire_dynamique = false)then
		date_ = date_debut_;
		while (date_ <= date_fin_) loop	
			select into ferier_ jour from yvs_jours_feries where jour = date_ and societe = employe_.societe;
			jour_ = (select extract(DOW from date_));		
			-- test si le jour present est un non ouvree
			if(jour_ = 1) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Lundi';
			elsif(jour_ = 2) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Mardi';
			elsif(jour_ = 3) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Mercredi';
			elsif(jour_ = 4) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Jeudi';
			elsif(jour_ = 5) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Vendredi';
			elsif(jour_ = 6) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Samedi';
			elsif(jour_ = 0) then		
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Dimanche';
			end if;	
			if(duree_ is not null) then
				if (ferier_.jour is null) then
					select into presence_ pe.id, total_presence, date_fin from yvs_grh_presence pe where pe.employe = employe_.id and date_debut = date_;
					if (presence_.id is not null)then
						total_ = total_ + presence_.total_presence;
					end if;
				else 
					total_ = total_ + ((select extract(hour from duree_)) + ((select extract(minute from duree_))/60));
				end if;
			end if;
			date_ = date_ + interval '1 day';	
		end loop;
	else	
		date_ = date_debut_;
		while (date_ <= date_fin_) loop	
			-- recherche des informations sur les jours planifies
			select into presence_ pe.date_fin, pe.date_debut,((pe.date_fin+pe.heure_fin_) - (pe.date_debut+pe.heure_debut_)) as duree 
				from yvs_grh_planning_employe pe where pe.employe = employe_.id and pe.date_debut = date_;
			if (presence_.date_debut is not null) then
				total_ = total_ + (select heure_travail_effectif_jour(employe_.id, presence_.date_debut, presence_.date_fin, false, presence_.duree));
			end if;
			date_ = date_ + interval '1 day';
		end loop;
	end if;
	if(total_ is null) then
		total_ = 0;
	end if;
    return Total_;
END
;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION heure_travail_effectif(bigint, date, date, boolean, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION heure_travail_effectif(bigint, date, date, boolean, boolean) IS 'fonction qui retourne le nombre d''heure de travail du mois precedent
elle prend en parametre : l''id de l''employe, la date du mois de la recherche, un boolean qui precise si l''on prend en compte les heures de compensation';



-- Function: heure_travail_effectif(bigint, date, date, boolean)

-- DROP FUNCTION heure_travail_effectif(bigint, date, date, boolean);

CREATE OR REPLACE FUNCTION heure_travail_effectif(id_ bigint, date_debut_ date, date_fin_ date, compensation_ boolean)
  RETURNS double precision AS
$BODY$   
DECLARE 
	employe_ RECORD;
        date_ DATE default '01/01/1990';
	ferier_ RECORD;
	presence_ RECORD;
        jour_ INTEGER;
        calendrier_ INTEGER;
        duree_ TIME;
        total_ double precision default 0;
    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique, co.calendrier from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id
		inner join yvs_agences a on e.agence = a.id where e.id = id_;
	if (employe_.calendrier is null) then
		select into calendrier_ pa.calendrier from yvs_parametre_grh pa where pa.societe = employe_.societe;
	else
		calendrier_ = employe_.calendrier;
	end if;
	if(employe_.horaire_dynamique = false)then
		date_ = date_debut_;
		while (date_ <= date_fin_) loop	
			select into ferier_ jour from yvs_jours_feries where jour = date_ and societe = employe_.societe;
			jour_ = (select extract(DOW from date_));		
			-- test si le jour present est un non ouvree
			if(jour_ = 1) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Lundi';
			elsif(jour_ = 2) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Mardi';
			elsif(jour_ = 3) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Mercredi';
			elsif(jour_ = 4) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Jeudi';
			elsif(jour_ = 5) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Vendredi';
			elsif(jour_ = 6) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Samedi';
			elsif(jour_ = 0) then		
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Dimanche';
			end if;	
			if(duree_ is not null) then --les heure d'un jour férié ne sont prise en compte que si le jour est un férié ouvré
				if (ferier_.jour is null) then
					select into presence_ pe.id, total_presence, date_fin from yvs_grh_presence pe where pe.employe = employe_.id and date_debut = date_;
					if (presence_.id is not null)then
						total_ = total_ + presence_.total_presence;
					end if;
				else 
					total_ = total_ + ((select extract(hour from duree_)) + ((select extract(minute from duree_))/60));
				end if;
			end if;
			date_ = date_ + interval '1 day';
		end loop;
	else	
		date_ = date_debut_;
		while (date_ <= date_fin_) loop	
			-- recherche des informations sur les jours planifies
			select into presence_ pe.date_fin, pe.date_debut,((pe.date_fin+pe.heure_fin_) - (pe.date_debut+pe.heure_debut_)) as duree 
				from yvs_grh_planning_employe pe where pe.employe = employe_.id and pe.date_debut = date_;
			if (presence_.date_debut is not null) then
				total_ = total_ + (select heure_travail_effectif_jour(employe_.id, presence_.date_debut, presence_.date_fin, compensation_, presence_.duree));
			end if;
			date_ = date_ + interval '1 day';
		end loop;
	end if;
	if(total_ is null) then
		total_ = 0;
	end if;
    return Total_;
END
;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION heure_travail_effectif(bigint, date, date, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION heure_travail_effectif(bigint, date, date, boolean) IS 'fonction qui retourne le nombre d''heure de travail du mois precedent
elle prend en parametre : l''id de l''employe, la date du mois de la recherche, un boolean qui precise si l''on prend en compte les heures de compensation';



-- Function: heure_suppl_travail(bigint, date, date, boolean)

-- DROP FUNCTION heure_suppl_travail(bigint, date, date, boolean);

CREATE OR REPLACE FUNCTION heure_suppl_travail(id_ bigint, date_debut_ date, date_fin_ date, compensation_ boolean)
  RETURNS double precision AS
$BODY$   
DECLARE 
	employe_ RECORD;
        date_ DATE default '01/01/1990';
	presence_ RECORD;
	vu_ RECORD;
        jour_ INTEGER;
	duree_ double precision default 0;
        total_ double precision default 0;
        temps_ double precision default 0;
        periode_ RECORD;
        supplementaire_ double precision default 0;
        calendrier_ bigint;
    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id 
                where e.id = id_;

	select into calendrier_ calendrier from yvs_grh_contrat_emps where employe = employe_.id;
	if(calendrier_ is null)then
		select into calendrier_ calendrier from yvs_parametre_grh where societe = employe_.spciete;
	end if;
         
        select into supplementaire_ limit_heure_sup from yvs_parametre_grh where societe = employe_.societe;

	if (supplementaire_ >0) then
	for presence_ in select date_debut, date_fin from yvs_grh_presence where employe = id_
		and date_debut >= date_debut_ and date_fin <= date_fin_
	loop	
		temps_ = (select heure_suppl_travail_global(id_, presence_.date_debut,presence_.date_fin));
		Total_ = Total_ + temps_;
		
		date_ = presence_.date_debut;
		while (date_ <= presence_.date_fin) loop
			jour_ = (select extract(DOW from date_));		
			-- test si le jour present est un non ouvree
			if(jour_ = 1) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Lundi';
			elsif(jour_ = 2) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Mardi';
			elsif(jour_ = 3) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Mercredi';
			elsif(jour_ = 4) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Jeudi';
			elsif(jour_ = 5) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Vendredi';
			elsif(jour_ = 6) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Samedi';
			elsif(jour_ = 0) then		
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = 'Dimanche';
			end if;
			if(periode_ is not null) then
				select into vu_ id, total_presence, date_fin from yvs_grh_presence where employe = employe_.id and date_debut = date_ and valider = true;
				if (vu_.id is not null)then
					duree_ = duree_ + vu_.total_presence;
				end if;
			end if;
			date_ = date_ + interval '1 day';
		end loop;
	end loop;
	end if;
	if(duree_ is null) then
		duree_ = 0;
	end if;
	total_ = temps_ - duree_;
    return Total_;
END
;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION heure_suppl_travail(bigint, date, date, boolean)
  OWNER TO postgres;

  
  
  -- Function: travail_jour_dimanche(bigint, date, date)

-- DROP FUNCTION travail_jour_dimanche(bigint, date, date);

CREATE OR REPLACE FUNCTION travail_jour_dimanche(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0; 
	infos_ RECORD;
	employe_ RECORD;
	presence_ RECORD;
	jour_ INTEGER;
	date_ DATE;
	
BEGIN
    -- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id 
                where e.id = id_;
        date_ = date_debut_;
	while (date_ <= date_fin_)
	loop	
		jour_ = (select extract(DOW from date_));		
		-- test si le jour present est un dimanche
		if (jour_ = 0) then
			select into presence_ id, total_presence, date_fin from yvs_grh_presence where employe = employe_.id and date_debut = date_ and valider = true;
			if (presence_.id is not null)then
				total_ = total_ + presence_.total_presence;
			end if;
		end if;
		date_ = date_ + interval '1 day';
	end loop;
	return total_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION travail_jour_dimanche(bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION travail_jour_dimanche(bigint, date, date) IS 'fonction qui retourne la duree d''heure de travail des dimanche d''un employe au mois precedent
elle prend en parametre : l''id de l''employe, la date du mois de la recherche, un boolean qui precise si l''on prend en compte les heures de compensation';
