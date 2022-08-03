-- Function: jour_repos_pointe(bigint, date, date)

-- DROP FUNCTION jour_repos_pointe(bigint, date, date);

CREATE OR REPLACE FUNCTION jour_repos_pointe(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$   
DECLARE 
	employe_ RECORD;
        total_ double precision default 0;
        jour_ INTEGER ;
        chJour_ character varying ;
        date_ DATE default '01/01/1990';
        jo_ bigint;
        co_ bigint;
    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique, co.calendrier from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id
		inner join yvs_agences a on e.agence = a.id where e.id = id_;
	
	-- compte le nombre de jour férié compris entre les dates qui sont également des jours ouvré
	date_=date_debut_;
	while (date_ <= date_fin_) loop	
		jour_ = (select extract(DOW from date_));		
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
		--vérifie si cette journées est journée de repos
		SELECT INTO jo_ jo.id FROM yvs_jours_ouvres jo where jo.calendrier = employe_.calendrier and jo.jour=chJour_ and (jo.jour_de_repos = true or jo.ouvrable=false);
		IF (jo_ is not null) THEN 
			select into jo_ id from yvs_grh_presence  where employe = employe_.id and date_ = date_debut and valider = true;
			if(jo_ is not null)then
				total_=total_+1;
			end if;
		 END IF;
		 date_ = date_ + interval '1 day';
	end loop;
	return total_;
END
;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION jour_repos_pointe(bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION jour_repos_pointe(bigint, date, date) IS 'fonction qui compte le nombre de jours de repos pointé';
