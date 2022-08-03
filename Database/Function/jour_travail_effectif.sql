-- Function: jour_travail_effectif(bigint, date, date)

-- DROP FUNCTION jour_travail_effectif(bigint, date, date);

CREATE OR REPLACE FUNCTION jour_travail_effectif(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$   
DECLARE 
	employe_ RECORD;
        total_ double precision default 0;
        tferie_ double precision default 0;
        jour_ INTEGER ;
        chJour_ character varying ;
        date_ DATE default '01/01/1990';
        jo_ bigint;
        co_ bigint;
        mi_ bigint;
        ferier_ DATE ;
    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique, co.calendrier from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id
		inner join yvs_agences a on e.agence = a.id where e.id = id_;
	select into total_ count(id) from yvs_grh_presence where employe = employe_.id and
		date_debut BETWEEN date_debut_ and date_fin_ and valider = true;
	if(total_ is null) then
		total_ = 0;
	end if;
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
		--vérifie si cette journées est ouvrée
		SELECT INTO jo_ jo.id FROM yvs_jours_ouvres jo where jo.calendrier = employe_.calendrier and jo.jour=chJour_ and jo.ouvrable = true AND jo.jour_de_repos is false;
		IF (jo_ is not null) THEN 
		    --vérifie si cette journée est férié
		    select into ferier_ jour from yvs_jours_feries where jour = date_ and societe = employe_.societe;
		    --vérifie si un congé ou une permission est en cours à cette date
		    select into co_ id from yvs_grh_conge_emps where employe=employe_.id and date_ BETWEEN date_debut and date_fin AND (statut='V' or statut='C') AND (effet='AUTORISE' OR effet='SPECIALE') AND (nature='C' OR (nature='P' AND duree_permission='L'));		 
		    --vérifie si une missin est en cours à cette date
		    select into mi_ id from yvs_grh_missions where employe=employe_.id and date_ BETWEEN date_debut and date_fin AND (statut_mission='V' or statut_mission='C');		 
		    IF(ferier_ IS NOT NULL OR co_ IS NOT NULL OR mi_ IS NOT NULL) THEN
			select into jo_ id from yvs_grh_presence  where employe = employe_.id and date_ between date_debut and date_fin and valider = true;
			if(jo_ is null)then
				tferie_=tferie_+1;
			end if;
		    END IF;		    
		 END IF;
		 date_ = date_ + interval '1 day';
	end loop;
	IF(tferie_ IS NULL) THEN
		tferie_=0; 
	END IF;
	total_ =total_ + tferie_;
	return total_;
END
;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION jour_travail_effectif(bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION jour_travail_effectif(bigint, date, date) IS 'fonction qui compte le nombre de jours travaillés entre deux date';
