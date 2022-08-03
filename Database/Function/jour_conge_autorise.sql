-- Function: jour_conge_autorise(bigint, date, date, boolean)

-- DROP FUNCTION jour_conge_autorise(bigint, date, date, boolean);

CREATE OR REPLACE FUNCTION jour_conge_autorise(id_ bigint, date_debut_ date, date_fin_ date, autoriser_ boolean)
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
        effet_ character varying;
	effet1_ character varying;
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique, co.calendrier from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id
		inner join yvs_agences a on e.agence = a.id where e.id = id_;
	
	-- compte le nombre de jour férié compris entre les dates qui sont également des jours ouvré
	date_=date_debut_;
	IF (autoriser_) THEN
		effet_ ='AUTORISE';
		effet1_ ='SPECIALE';
	ELSE
		effet_ ='SALAIRE';
		effet1_ ='CONGE ANNUEL';
	END IF;
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
		SELECT INTO jo_ jo.id FROM yvs_jours_ouvres jo where jo.calendrier = employe_.calendrier and jo.jour=chJour_ and ouvrable = true;
		IF (jo_ is not null) THEN 
		    --vérifie si un congé ou une permission est en cours à cette date
		    select into co_ id from yvs_grh_conge_emps where employe=employe_.id and date_ BETWEEN date_debut and date_fin AND (statut='V' or statut='C') AND (effet=effet_ OR effet=effet1_) AND type_conge!='CT';		 
		    --vérifie si une missin est en cours à cette date
		    IF(co_ IS NOT NULL) THEN
			select into jo_ id from yvs_grh_presence  where employe = employe_.id and date_ = date_debut and valider = true;
			if(jo_ is null)then
				total_=total_+1;
			end if;
		    END IF;		    
		 END IF;
		 date_ = date_ + interval '1 day';
	end loop;
	return total_;
END
;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION jour_conge_autorise(bigint, date, date, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION jour_conge_autorise(bigint, date, date, boolean) IS 'fonction qui compte le nombre de jours de congé autorisé dans une période de calcul de salaire. (Congé maladie par exemple)';
