-- Function: jour_travail_requis(bigint, date, date)

-- DROP FUNCTION jour_travail_requis(bigint, date, date);

CREATE OR REPLACE FUNCTION jour_travail_requis(id_ bigint, date_prec_ date, date_suiv_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	employe_ RECORD;
	jour_ INTEGER;
	date_jour_ DATE;
	nature_ VARCHAR;
	calendrier_ INTEGER;
	infos_ VARCHAR;
	time_ INTEGER;
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

	if (employe_.id is null) then
		total_ = 0;
	else
		--if(employe_.horaire_dynamique = false)then
		if(true) then
			date_jour_ = date_prec_;
			while (date_jour_ <= date_suiv_) loop
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
				select into infos_ jour from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = nature_ and (ouvrable = true and jour_de_repos=false);
				if (infos_ is not null) then
					total_ = total_ + 1;
				end if;
				date_jour_ = date_jour_ + interval '1 day';
			end loop;
		else			
			--date_jour_ = date_prec_;
			--while (date_jour_ <= date_suiv_) loop
				-- recherche des informations sur les jours planifies
			--	select into time_ (pe.date_fin - pe.date_debut) from yvs_grh_planning_employe pe where pe.employe = employe_.id
			--		and pe.date_debut = date_jour_;
			--	if(time_ is null)then
			--		time_ = 0;
			--	end if;
			--	total_ = total_ + time_;
			--	date_jour_ = date_jour_ + interval '1 day';
			--end loop;
		end if;
	end if;
	if (total_ is null) then
		total_ = 0;
	end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION jour_travail_requis(bigint, date, date)
  OWNER TO postgres;
