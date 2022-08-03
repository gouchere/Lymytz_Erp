-- Function: heure_travail_requis_jour(bigint, date)

-- DROP FUNCTION heure_travail_requis_jour(bigint, date);

CREATE OR REPLACE FUNCTION heure_travail_requis_jour(id_ bigint, date_jour_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	employe_ RECORD;
	jour_ INTEGER;
	nature_ VARCHAR;
	calendrier_ INTEGER;
	time_ TIME;
	total_ DOUBLE PRECISION default 0;
	
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
		-- recherche du jour effectif de la date de travail
		jour_ = (select extract(DOW from date_jour_));
		if(jour_ = 0) then
			nature_ = 'Dimanche';
		elsif(jour_ = 1) then
			nature_ = 'Lundi';
		elsif(jour_ = 2) then
			nature_ = 'Mardi';
		elsif(jour_ = 3) then
			nature_ = 'Mercredi';
		elsif(jour_ = 4) then
			nature_ = 'Jeudi';
		elsif(jour_ = 5) then
			nature_ = 'Vendredi';
		elsif(jour_ = 6) then
			nature_ = 'Samedi';
		end if;
	
		-- recherche des informations sur les jours ouvres
		select into time_ (jo.heure_fin_travail - jo.heure_debut_travail) from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and ouvrable = true
			and jo.jour = nature_;
		if(time_ is null)then
			time_ = '00:00:00';
		end if;
		total_ = ((select extract(hour from time_)) + ((select extract(minute from time_))/60));
		if (total_ is null) then
			total_ = 0;
		end if;
	else
		-- recherche des informations sur les jours ouvres
		select into time_ ((pe.date_fin+pe.heure_fin_) - (pe.date_debut+pe.heure_debut_)) from yvs_grh_planning_employe pe where pe.employe = employe_.id
			and pe.date_debut = date_jour_ and ouvrable = false;
		if(time_ is null)then
			time_ = '00:00:00';
		end if;
		total_ = ((select extract(hour from time_)) + ((select extract(minute from time_))/60));
		if (total_ is null) then
			total_ = 0;
		end if;
	end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION heure_travail_requis_jour(bigint, date)
  OWNER TO postgres;
