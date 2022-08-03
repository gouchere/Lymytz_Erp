-- Function: calcul_montant_heure(bigint, double precision)

-- DROP FUNCTION calcul_montant_heure(bigint, double precision);

CREATE OR REPLACE FUNCTION calcul_montant_heure(id_ bigint, somme_ double precision)
  RETURNS double precision AS
$BODY$   DECLARE 
		employe_ RECORD;
		montant_ double precision default 0;    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, c.salaire_horaire from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id
				inner join yvs_grh_contrat_emps c on e.id = c.employe where c.id = id_;
	montant_ = somme_ * employe_.salaire_horaire;
	return montant_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION calcul_montant_heure(bigint, double precision)
  OWNER TO postgres;
COMMENT ON FUNCTION calcul_montant_heure(bigint, double precision) IS 'fonction qui retourne le montant des heures totales
elle prend en parametre l''id de l''employe en question et l''equivalent du total de ses heures de travail';


-----------------------------------------------------------------------------------------------------------------------------------
-- Function: calcul_montant_total(bigint, date, date, boolean, boolean)

-- DROP FUNCTION calcul_montant_total(bigint, date, date, boolean, boolean);

CREATE OR REPLACE FUNCTION calcul_montant_total(id_ bigint, date_debut_ date, date_fin_ date, compensation_ boolean, supplementaire_ boolean)
  RETURNS double precision AS
$BODY$
DECLARE
    vide_ VARCHAR default null; 
    jour_ INTEGER;
	employe_ RECORD;
	presence_ RECORD;
    ferier_ RECORD;
    montant_ REAL default 0;
	date_ DATE;
    temps_ double precision default 0;
    deja_ BOOLEAN default false;
	
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, a.societe, c.salaire_horaire, p.date_paiement_salaire from yvs_grh_employes e 
		inner join yvs_agences a on e.agence = a.id inner join yvs_societes s on a.societe = s.id 
		inner join yvs_parametre_grh p on p.societe = s.id inner join yvs_grh_contrat_emps c on c.employe = e.id
        where e.id = id_ limit 1;

    -- recherche des heures supplementaires sur une date
	for presence_ in select y.date_fiche from yvs_grh_presence p inner join yvs_grh_fiche_presence y on p.fiche_presence = y.id 
                    where p.employe = employe_.id and (y.date_fiche >= date_debut_ and y.date_fiche < date_fin_)
                    and p.valider = true and p.compensation_heure = compensation_ and heure_supplementaire = supplementaire_
	loop
        jour_ = (select extract(DOW from presence_.date_fiche));
        if (jour_ = 0) then
            temps_ = (select duree_travail_jour(employe_.id, presence_.date_fiche, compensation_, supplementaire_));
            montant_ = montant_ + (select calcul_montant_heure_suppl(employe_.id,temps_,false,true,false));
        else
            -- recherche des jour ferier sur la periode
            select into ferier_ jour from yvs_jours_feries where jour = presence_.date_fiche and societe = employe_.societe;
            vide_ = ferier_.jour;
            if (char_length(vide_) > 0) then
                -- cas ou l'employe a travaille un jour ferier
                temps_ = (select duree_travail_jour(employe_.id, ferier_.jour, compensation_, supplementaire_));
                montant_ = montant_ + (select calcul_montant_heure_suppl(employe_.id,temps_,false,false,true));
                -- si oui
                deja_ = true;
            else
                -- si non
                deja_ = false;
            end if;
            -- cas ou c'est un jour normal
            if (!deja_) then
                temps_ = (select duree_travail_jour(employe_.id, presence_.date_fiche, compensation_, supplementaire_));
                montant_ = montant_ + (select calcul_montant_heure(employe_.id, temps_));
            end if;
        end if;
	end loop;
	-- recherche des heures de conge a effet permission pris sur la periode
	temps_ = (select calcul_jour_conge_pris_a(employe_.id, date_debut_ ,date_fin_, true));
	vide_ = temps_;
	if (char_length(vide_)>0) then
		montant_ = montant_ + (select calcul_montant_heure(employe_.id, temps_));
	end if;
	
	return montant_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION calcul_montant_total(bigint, date, date, boolean, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION calcul_montant_total(bigint, date, date, boolean, boolean) IS 'fonction qui retourne le montant total des heures de travail sur une periode
elle prend en parametre l''id de l''employe,  la date de debut et de fin de la periode, un booleen qui indique si l''on tient en compte les heures de compensation 
et un booleen qui indique si on tient en compte les heures supplementaires';


------------------------------------------------------------------------------

-- Function: conge_jour_pris_salaire(bigint, date, date)

-- DROP FUNCTION conge_jour_pris_salaire(bigint, date, date);

CREATE OR REPLACE FUNCTION conge_jour_pris_salaire(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$DECLARE 
    total_ REAL default 0; 
	employe_ RECORD;
	cpt_ REAL default 0; 
	effet_ VARCHAR default 'SALAIRE';
    
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, e.nb_jour_conge, e.date_embauche, e.civilite, a.societe from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id where e.id = id_ limit 1;

	-- recherche des conges pris par l'employe a la date d'exercice
    select into cpt_ sum(c.date_fin - c.date_debut) from yvs_conge_emps c where c.employe = employe_.id and c.date_debut >= date_debut_
            and c.date_debut < date_fin_ and effet = effet_ and c.valider = true group by employe;
	-- control pour connaitre si le resultat de la recherche des conges deja pris par l'employe a ete null
	if (cpt_ is null) then
		cpt_ =0;
	end if;
        total_ = total_ + cpt_;
    -- control pour connaitre si le resultat de la recherche des conges deja pris par l'employe a ete null
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION conge_jour_pris_salaire(bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION conge_jour_pris_salaire(bigint, date, date) IS 'fonction qui retourne le montant des conges a effet sur le salaire a deduire sur le salaire d''un mois precedent
elle prend en parametre l''id de l''employe et la date du jour';



--------------------------------------------------------------------------------

-- Function: conge_total_pris_annuel(bigint, date)

-- DROP FUNCTION conge_total_pris_annuel(bigint, date);

CREATE OR REPLACE FUNCTION conge_total_pris_annuel(id_ bigint, date_jour date)
  RETURNS double precision AS
$BODY$
DECLARE
total_ real default 0;
	employe_ RECORD;
    
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, e.nb_jour_conge, e.date_embauche, e.civilite, a.societe, e.date_cloture_conge as date_cloture
		from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id where e.id = id_ limit 1;

	-- recherche des conges pris par l'employe a la date d'exercice
	total_ = (select conge_jour_pris_annuel(employe_.id,employe_.date_cloture,date_jour));
    -- control pour connaitre si le resultat de la recherche des conges deja pris par l'employe a ete null
    if(total_ is null)then
	total_=0;
    end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION conge_total_pris_annuel(bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION conge_total_pris_annuel(bigint, date) IS 'fonction qui retourne le total des jours de conge pris par un employe au long de son exercice en fonction de sa derniere date de cloture des conges

elle prend en parametre l''''id de l''''employe, la date du jour,et la date du jour et un booleen qui precise si l''on prend en compte les congés pris sur permission';


----------------
-- Function: conge_total_pris_autorise(bigint, date)

-- DROP FUNCTION conge_total_pris_autorise(bigint, date);

CREATE OR REPLACE FUNCTION conge_total_pris_autorise(id_ bigint, date_jour date)
  RETURNS double precision AS
$BODY$
DECLARE
total_ real default 0;
	employe_ RECORD;
    
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, e.nb_jour_conge, e.date_embauche, e.civilite, a.societe, e.date_cloture_conge as date_cloture
		from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id where e.id = id_ limit 1;

	-- recherche des conges pris par l'employe a la date d'exercice
	total_ = (select conge_jour_pris_autorise(employe_.id,employe_.date_cloture,date_jour));
    -- control pour connaitre si le resultat de la recherche des conges deja pris par l'employe a ete null
    if(total_ is null)then
	total_=0;
    end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION conge_total_pris_autorise(bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION conge_total_pris_autorise(bigint, date) IS 'fonction qui retourne le total des jours de conge pris par un employe au long de son exercice en fonction de sa derniere date de cloture des conges

elle prend en parametre l''''id de l''''employe, la date du jour,et la date du jour et un booleen qui precise si l''on prend en compte les congés pris sur permission';

-------------------------

-- Function: conge_total_pris_salaire(bigint, date)

-- DROP FUNCTION conge_total_pris_salaire(bigint, date);

CREATE OR REPLACE FUNCTION conge_total_pris_salaire(id_ bigint, date_jour date)
  RETURNS double precision AS
$BODY$
DECLARE
total_ real default 0;
	employe_ RECORD;
    
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, e.nb_jour_conge, e.date_embauche, e.civilite, a.societe, e.date_cloture_conge as date_cloture
		from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id where e.id = id_ limit 1;

	-- recherche des conges pris par l'employe a la date d'exercice
	total_ = (select conge_jour_pris_salaire(employe_.id,employe_.date_cloture,date_jour));
    -- control pour connaitre si le resultat de la recherche des conges deja pris par l'employe a ete null
    if(total_ is null)then
	total_=0;
    end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION conge_total_pris_salaire(bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION conge_total_pris_salaire(bigint, date) IS 'fonction qui retourne le total des jours de conge pris par un employe au long de son exercice en fonction de sa derniere date de cloture des conges

elle prend en parametre l''''id de l''''employe, la date du jour,et la date du jour et un booleen qui precise si l''on prend en compte les congés pris sur permission';


--------------------------------

-- Function: give_nbday_in_periode_reference(bigint, bigint)

-- DROP FUNCTION give_nbday_in_periode_reference(bigint, bigint);

CREATE OR REPLACE FUNCTION give_nbday_in_periode_reference(employe_ bigint, societe_ bigint)
  RETURNS integer AS
$BODY$
DECLARE 
	data_ RECORD;
	date_debut_ DATE;
	date_fin_ DATE;
        result_ INTEGER;
        nbre_ double precision;
        next_ date;
        param_ RECORD;
        data_employe_ RECORD;
    
BEGIN
result_=0;
--recherche la date de fin du dernier congé annuelle de cet employé
  select into data_ co.date_fin FROM yvs_grh_conge_emps co WHERE co.nature='ANNUEL' AND co.employe=employe_ AND co.statut='O' ORDER BY co.date_fin DESC limit 1;
--récupère la date de fin. elle correspond à la date de début du dernier contrat non encore jouit, mais validé
  select into date_fin_ co.date_debut FROM yvs_grh_conge_emps co  WHERE co.nature='ANNUEL' AND co.employe=employe_ AND co.statut='V' ORDER BY co.date_fin DESC limit 1;
--recherche la date d'embauche de l'employé
  select into data_employe_ e.date_embauche FROM yvs_grh_employes e WHERE e.id=employe_ ;
  --récupère les donnée de paramétrage généreaux
  select into param_ p.nbre_jour_mois_ref, p.duree_cumule_conge FROM yvs_parametre_grh p  WHERE p.societe=societe_ limit 1;
  IF(data_.date_fin IS NULL) THEN
	--cherche la date d'embauche
	date_debut_=data_employe_.date_embauche;
 ELSE
	date_debut_=data_.date_fin;
  END IF;
  --à enlever plus tard
  IF(date_fin_ IS NULL) THEN
	date_fin_=now();
  END IF;
  IF( date_debut_ IS NULL OR date_fin_ IS NULL) THEN
	RETURN 0;
  END IF;
  --compte le nombre de mois entre date_debut et date_fin dont le nombre de jour de travail est supérieure ou égale au nombre de jour de référence pris dans les paramétrage généraux.
  --si le résultat est supérieur à la durée limite de cumul de congé, on renvoie la durée limite  
	while (date_debut_ <= date_fin_) loop
			next_=date_debut_ + interval '1 month';
				select into nbre_ jour_travail_effectif(employe_,date_debut_,next_);
				IF nbre_ IS NOT NULL THEN
					IF nbre_>=param_.nbre_jour_mois_ref THEN
						result_ = result_ + nbre_;
					END IF;
				END IF;
				date_debut_=next_;
	end loop;
	RAISE NOTICE 'Date début %',date_debut_;
	RAISE NOTICE 'Résultat %',result_;
	IF(result_>=param_.duree_cumule_conge) THEN 
		 return param_.duree_cumule_conge;
	ELSE 
		return result_;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION give_nbday_in_periode_reference(bigint, bigint)
  OWNER TO postgres;

  
  ----------------
  
  -- Function: give_periode_reference(bigint, bigint)

-- DROP FUNCTION give_periode_reference(bigint, bigint);

CREATE OR REPLACE FUNCTION give_periode_reference(employe_ bigint, societe_ bigint)
  RETURNS integer AS
$BODY$
DECLARE 
	data_ RECORD;
	date_debut_ DATE;
	date_fin_ DATE;
        result_ INTEGER;
        nbre_ double precision;
        next_ date;
        param_ RECORD;
        data_employe_ RECORD;
    
BEGIN
result_=0;
--recherche la date de fin du dernier congé annuelle de cet employé
  select into data_ co.date_fin FROM yvs_grh_conge_emps co WHERE co.type_conge='ANNUEL' AND co.employe=employe_ AND co.statut='O' ORDER BY co.date_fin DESC limit 1;
--récupère la date de fin. elle correspond à la date de début du dernier contrat non encore jouit, mais validé
  select into date_fin_ co.date_debut FROM yvs_grh_conge_emps co  WHERE co.type_conge='ANNUEL' AND co.employe=employe_ AND co.statut='V' ORDER BY co.date_fin DESC limit 1;
--recherche la date d'embauche de l'employé
  select into data_employe_ e.date_embauche FROM yvs_grh_employes e WHERE e.id=employe_ ;
  --récupère les donnée de paramétrage généreaux
  select into param_ p.nbre_jour_mois_ref, p.duree_cumule_conge FROM yvs_parametre_grh p  WHERE p.societe=societe_ limit 1;
  IF(data_.date_fin IS NULL) THEN
	--cherche la date d'embauche
	date_debut_=data_employe_.date_embauche;
 ELSE
	date_debut_=data_.date_fin;
  END IF;
  --à enlever plus tard
  IF(date_fin_ IS NULL) THEN
	date_fin_=now();
  END IF;
  IF( date_debut_ IS NULL OR date_fin_ IS NULL) THEN
	RETURN 0;
  END IF;
  --compte le nombre de mois entre date_debut et date_fin dont le nombre de jour de travail est supérieure ou égale au nombre de jour de référence pris dans les paramétrage généraux.
  --si le résultat est supérieur à la durée limite de cumul de congé, on renvoie la durée limite  
	while (date_debut_ <= date_fin_) loop
			next_=date_debut_ + interval '1 month';
				select into nbre_ jour_travail_effectif(employe_,date_debut_,next_);
				IF nbre_ IS NOT NULL THEN
					IF nbre_>=param_.nbre_jour_mois_ref THEN
						result_ = result_ +1;
					END IF;
				END IF;
				date_debut_=next_;
	end loop;
	RAISE NOTICE 'Date début %',date_debut_;
	RAISE NOTICE 'Résultat %',result_;
	IF(result_>=param_.duree_cumule_conge*12) THEN 
		 return param_.duree_cumule_conge*12;
	ELSE 
		return result_;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION give_periode_reference(bigint, bigint)
  OWNER TO postgres;

  
  ----------------
  
  -- Function: heure_suppl_montant(bigint, double precision, boolean, boolean, boolean)

-- DROP FUNCTION heure_suppl_montant(bigint, double precision, boolean, boolean, boolean);

CREATE OR REPLACE FUNCTION heure_suppl_montant(id_ bigint, somme_ double precision, tranche_ boolean, dimanche_ boolean, ferier_ boolean)
  RETURNS double precision AS
$BODY$   DECLARE 
		periode_ RECORD;
        infos_ RECORD;
		employe_ RECORD;
		montant_ REAL default 0;
		marge_ INTEGER default 0;
		vide_ VARCHAR;
    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, c.salaire_horaire from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id
				inner join yvs_grh_contrat_emps c on e.id = c.employe where e.id = id_;
	-- recherche des informations concernant la societe
	select into infos_ heure_supplementaire, date_paiement_salaire, total_heure_travail_hebd, limit_heure_travail 
                from yvs_parametre_grh where societe = employe_.societe;

	if (infos_.heure_supplementaire = true) then
	-- teste de la totalite des jours de travail
        if (tranche_) then
            if (somme_ > infos_.total_heure_travail_hebd and somme_ <= infos_.limit_heure_travail) then
                for periode_ in select valeur_minimal, valeur_maximal, majoration from yvs_param_heure_supplementaire 
					where societe = employe_.societe and plage_horaire = true order by valeur_minimal asc
                loop
                    -- recherche des tranches d'heure supplementaire
                    if (somme_ >= periode_.valeur_minimal and somme_ <= periode_.valeur_maximal) then
                        -- recherche des heures supplementaires
                        vide_ = periode_.valeur_minimal;

                        if (char_length(vide_) > 0) then
                            if (somme_ > periode_.valeur_minimal) then
                                if (somme_ > periode_.valeur_maximal) then
                                    marge_ = periode_.valeur_maximal - periode_.valeur_minimal;
                                else
                                    marge_ = somme_ - periode_.valeur_minimal;
                                end if;
                            else
                                exit;
                            end if;
                        end if;
                    end if;
                    -- calcul du montant des heures supplementaires
                    montant_ = montant_ + (marge_ * (employe_.salaire_horaire * periode_.majoration));
                end loop;
            end if;	
        else
		select into periode_ majoration from yvs_param_heure_supplementaire 
			where societe = employe_.societe and plage_horaire = false and dimanche = dimanche_ and ferier = ferier_;
			montant_ = somme_ * (employe_.salaire_horaire * periode_.majoration);
			-- somme_ = somme + 1;
        end if;
	end if;
	return montant_ ;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION heure_suppl_montant(bigint, double precision, boolean, boolean, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION heure_suppl_montant(bigint, double precision, boolean, boolean, boolean) IS 'fonction qui retourne le montant des heures supplementaires
elle prend en parametre l''id de l''employe,  le total des heures de travail, un boolean qui indique si l''on prend les valeurs d''une tranche et le libelle de la categorie d''heure supplementaire';


---------------------------

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
        supplementaire_ BOOLEAN default false;
    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id 
                where e.id = id_;
        select into supplementaire_ heure_supplementaire from yvs_parametre_grh where societe = employe_.societe;

	if (supplementaire_ = true) then
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
				from yvs_jours_ouvres jo inner join yvs_parametre_grh pa on jo.calendrier = pa.calendrier where societe = employe_.societe and jo.jour = 'Lundi';
			elsif(jour_ = 2) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo inner join yvs_parametre_grh pa on jo.calendrier = pa.calendrier where societe = employe_.societe and jo.jour = 'Mardi';
			elsif(jour_ = 3) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo inner join yvs_parametre_grh pa on jo.calendrier = pa.calendrier where societe = employe_.societe and jo.jour = 'Mercredi';
			elsif(jour_ = 4) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo inner join yvs_parametre_grh pa on jo.calendrier = pa.calendrier where societe = employe_.societe and jo.jour = 'Jeudi';
			elsif(jour_ = 5) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo inner join yvs_parametre_grh pa on jo.calendrier = pa.calendrier where societe = employe_.societe and jo.jour = 'Vendredi';
			elsif(jour_ = 6) then
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo inner join yvs_parametre_grh pa on jo.calendrier = pa.calendrier where societe = employe_.societe and jo.jour = 'Samedi';
			elsif(jour_ = 0) then		
				select into periode_ jo.jour, jo.heure_debut_travail, jo.heure_fin_travail, jo.duree_pause, jo.heure_debut_pause, jo.heure_fin_pause
				from yvs_jours_ouvres jo inner join yvs_parametre_grh pa on jo.calendrier = pa.calendrier where societe = employe_.societe and jo.jour = 'Dimanche';
			end if;
			if(periode_ is not null) then
				select into vu_ id from yvs_grh_presence where employe = employe_.id and
					date_debut <= date_ and date_fin >= date_ and valider = true;
				if(vu_.id is not null) then
					duree_ = duree_ + (select duree_journee(vu_.id, date_, true));
					duree_ = duree_ - (select duree_journee(vu_.id, date_, false));
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

  
  
  ------------------------
  -- Function: heure_suppl_travail_global(bigint, date, date)

-- DROP FUNCTION heure_suppl_travail_global(bigint, date, date);

CREATE OR REPLACE FUNCTION heure_suppl_travail_global(id_ bigint, date_prec_ date, date_suiv_ date)
  RETURNS double precision AS
$BODY$
DECLARE 
	employe_ RECORD;
        Total_ double precision default 0;
        time_ TIME;
    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique , p.heure_supplementaire from yvs_grh_employes e 
	inner join yvs_agences a on e.agence = a.id inner join yvs_parametre_grh p on p.societe = a.societe
                where e.id = id_;
	
	-- recherche des heures supplementaires sur une date
	if (employe_.heure_supplementaire = true) then
		select into time_ sum(po.heure_sortie - po.heure_entree) from yvs_grh_pointage po inner join yvs_grh_presence pe on po.presence = pe.id 
			where pe.employe = employe_.id and pe.date_debut = date_prec_ and pe.date_fin = date_suiv_ and po.valider = true and po.actif = true
			and po.heure_supplementaire = true;
	end if;
	if(time_ is null) then
		Total_ = 0;
	else
		Total_ = ((select extract(hour from time_)) + ((select extract(minute from time_))/60));
	end if;
	return Total_; 
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION heure_suppl_travail_global(bigint, date, date)
  OWNER TO postgres;

  --------
  
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
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Lundi';
			elsif(jour_ = 2) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Mardi';
			elsif(jour_ = 3) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Mercredi';
			elsif(jour_ = 4) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Jeudi';
			elsif(jour_ = 5) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Vendredi';
			elsif(jour_ = 6) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Samedi';
			elsif(jour_ = 0) then		
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Dimanche';
			end if;	
			if(duree_ is not null) then --les heure d'un jour férié ne sont prise en compte que si le jour est un férié ouvré
				if (ferier_.jour is null) then
					select into presence_ pe.id from yvs_grh_presence pe where pe.employe = employe_.id and date_debut = date_;
					if (presence_.id is not null)then
						total_ = total_ + (select heure_travail_effectif_jour_unique(presence_.id, date_, false));
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


---------------------------------
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
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Lundi';
			elsif(jour_ = 2) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Mardi';
			elsif(jour_ = 3) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Mercredi';
			elsif(jour_ = 4) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Jeudi';
			elsif(jour_ = 5) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Vendredi';
			elsif(jour_ = 6) then
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Samedi';
			elsif(jour_ = 0) then		
				select into duree_ jo.heure_fin_travail - jo.heure_debut_travail from yvs_jours_ouvres jo where jo.calendrier = calendrier and jo.jour = 'Dimanche';
			end if;	
			if(duree_ is not null) then
				if (ferier_.jour is null) then
					select into presence_ pe.id from yvs_grh_presence pe where pe.employe = employe_.id and date_debut = date_;
					if (presence_.id is not null)then
						total_ = total_ + (select heure_travail_effectif_jour_unique(presence_.id, date_, false));
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

------------------------

-- Function: heure_travail_effectif_jour(bigint, date, date, boolean, double precision)

-- DROP FUNCTION heure_travail_effectif_jour(bigint, date, date, boolean, double precision);

CREATE OR REPLACE FUNCTION heure_travail_effectif_jour(id_ bigint, date_prec date, date_suiv date, compensation_ boolean, duree_ double precision)
  RETURNS double precision AS
$BODY$
    DECLARE 
	employe_ RECORD;
	heure_ TIME;
        total_ double precision default 0;
    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique from yvs_grh_employes e inner join yvs_grh_contrat_emps co
		on co.employe = e.id inner join yvs_agences a on e.agence = a.id where e.id = id_;
	
	-- recherche des heures supplementaires sur une date
	select into heure_ sum(po.heure_sortie - po.heure_entree) from yvs_grh_pointage po inner join yvs_grh_presence pe on pe.id = po.presence
		where pe.employe = employe_.id and (pe.date_debut = date_prec and pe.date_fin = date_suiv) and po.valider = true and po.actif = true 
		and heure_supplementaire = false;
	if(heure_ is null)then
		heure_ = '00:00:00';
	end if;
	total_ = ((select extract(hour from heure_)) + ((select extract(minute from heure_))/60));
	if(total_ is null) then
		total_ = 0;
	end if; 
	if (compensation_ = false and total_ > duree_) then 
		total_ = duree_;
	end if;
	
	return total_; 
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION heure_travail_effectif_jour(bigint, date, date, boolean, double precision)
  OWNER TO postgres;
COMMENT ON FUNCTION heure_travail_effectif_jour(bigint, date, date, boolean, double precision) IS 'fonction qui retourne la duree d''heure de travail d''un employe a une date
elle prend en parametre : l''id de l''employe, la date du jour de la recherche, un boolean qui precise si l''on prend en compte les heures de compensation';


-------

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
		select into time_ (jo.heure_fin_travail - jo.heure_debut_travail) from yvs_jours_ouvres jo where jo.calendrier = calendrier_
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
			and pe.date_debut = date_jour_;
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

  -------------------
  -- Function: insertion_avancement(bigint)

-- DROP FUNCTION insertion_avancement(bigint);

CREATE OR REPLACE FUNCTION insertion_avancement(id_ bigint)
  RETURNS bigint AS
$BODY$
DECLARE 
    result BIGINT default 0;
    max_e INTEGER; 
	max_c INTEGER;
	employe_ RECORD;
	infos_ RECORD;
	code_c INTEGER default 0;
	code_e INTEGER default 0;
	code_ RECORD;
	total_horaire_min_ double precision default 0;
	total_min_ double precision default 0;
	date_ date default '01-01-1990';
	periode_ double precision default 0;
    
BEGIN
	select into max_c degre from yvs_grh_categorie_professionelle order by degre desc limit 1;
	select into max_e degre from yvs_grh_echelons order by degre desc limit 1;
	
	select into employe_ e.id, a.societe, a.secteur_activite as secteur from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id inner join yvs_societes s on a.societe = s.id  where e.id = id_;
	
	select into infos_ ce.id, cc.salaire_horaire_min, cc.salaire_min, ce.date_change, ce.convention, ca.degre as degre_c, e.degre as degre_e, cc.categorie, cc.echellon from yvs_grh_convention_employe ce 
		inner join yvs_grh_convention_collective cc on ce.convention = cc.id inner join yvs_grh_categorie_professionelle ca on cc.categorie = ca.id 
		inner join yvs_grh_echelons e on cc.echellon = e.id where employe = employe_.id  and ce.actif = true;
	
	if (infos_.degre_e < max_e) then
		code_c = infos_.categorie;
		select into code_e id from yvs_grh_echelons where degre = infos_.degre_e + 1 and societe = employe_.societe order by degre asc limit 1;
	end if;
	
	select into code_ id, salaire_horaire_min, salaire_min from yvs_grh_convention_collective where categorie = code_c and echellon = code_e and secteur = employe_.secteur;
	total_horaire_min_ = (code_.salaire_horaire_min - infos_.salaire_horaire_min);
	total_min_ = code_.salaire_min - infos_.salaire_min;
	
	if (code_.id is not null) then
		select into infos_ p.periode_davancement, p.periode_premier_avancement from yvs_parametre_grh p inner join yvs_agences a on p.societe = a.societe
			inner join yvs_grh_employes e on e.agence = a.id where e.id = employe_.id;
		periode_ = infos_.periode_davancement;
		date_ = to_date((((select extract(day from current_date)) || '-' || (select extract(month from current_date)) ||'-'|| (select extract(year from current_date)) + periode_)),'DD MM YYYY');
	
		update yvs_grh_convention_employe set actif = false where employe = employe_.id;
		insert into yvs_grh_convention_employe(employe, convention, date_change, actif) values (employe_.id, code_.id, current_date, true);
		update yvs_grh_contrat_emps set salaire_horaire = (salaire_horaire + total_horaire_min_), salaire_mensuel = (salaire_mensuel + total_min_) where employe = employe_.id;
		update yvs_grh_employes set date_prochain_avancement = date_ where id = employe_.id;

		select into result id from yvs_grh_convention_employe where employe = employe_.id and convention = code_.id and actif = true;
	end if;
    return result;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insertion_avancement(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION insertion_avancement(bigint) IS 'fonction modifie la convention collective d''un employe et retourne l''id de la nouvelle convention de l''employe
elle prend en parametre l''id de l''employe
';


----------------
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
        ferier_ DATE ;
    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique, co.calendrier from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id
		inner join yvs_agences a on e.agence = a.id where e.id = id_;
	select into total_ count(id) from yvs_grh_presence where employe = employe_.id and
		date_debut >= date_debut_ and date_fin <= date_fin_ and valider = true;
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
			SELECT INTO jo_ jo.id FROM yvs_jours_ouvres jo where jo.calendrier = employe_.calendrier and jo.jour=chJour_;
			IF (jo_ is not null) THEN 
			    --vérifie si cette journée est férié
			    select into ferier_ jour from yvs_jours_feries where jour = date_ and societe = employe_.societe;
			    IF(ferier_ IS NOT NULL) THEN
			      tferie_=tferie_+1;
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

-----------------

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
		if(employe_.horaire_dynamique = false)then
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
				select into infos_ jour from yvs_jours_ouvres jo where jo.calendrier = calendrier_ and jo.jour = nature_;
				if (infos_ is not null) then
					total_ = total_ + 1;
				end if;
				date_jour_ = date_jour_ + interval '1 day';
			end loop;
		else			
			date_jour_ = date_prec_;
			while (date_jour_ <= date_suiv_) loop
				-- recherche des informations sur les jours planifies
				select into time_ (pe.date_fin - pe.date_debut) from yvs_grh_planning_employe pe where pe.employe = employe_.id
					and pe.date_debut = date_jour_;
				if(time_ is null)then
					time_ = 0;
				end if;
				total_ = total_ + time_;
				date_jour_ = date_jour_ + interval '1 day';
			end loop;
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

  
  ---------------
  
  -- Function: synchronise_employe_mutuelle()

-- DROP FUNCTION synchronise_employe_mutuelle();

CREATE OR REPLACE FUNCTION synchronise_employe_mutuelle()
  RETURNS integer AS
$BODY$
DECLARE 
	id_ RECORD;
	emp_ bigint default 0;
	i int default 0;
    
BEGIN
	for id_ in select id from yvs_grh_employes
	loop
		select into emp_ id from yvs_mut_employe where employe = id_.id;
		if(emp_ is null or emp_ < 1)then
			insert into yvs_mut_employe("employe") values (id_.id);
			i = i+1;
		end if;
	end loop;
	return i;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION synchronise_employe_mutuelle()
  OWNER TO postgres;

  
  
  ------
  -- Function: tache_montant_all_total(bigint, date, date)

-- DROP FUNCTION tache_montant_all_total(bigint, date, date);

CREATE OR REPLACE FUNCTION tache_montant_all_total(id_ bigint, date_debut date, date_fin date)
  RETURNS double precision AS
$BODY$DECLARE
    vide_ VARCHAR default null; 
	employe_ RECORD;
	tache_ RECORD;
    montant_ REAL default 0;
	
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, e.regle_tache, a.societe, c.id from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id 
        inner join yvs_grh_contrat_emps c on c.employe = e.id where e.id = id_ limit 1;
        
        -- recherche des informations sur les tache
	
	-- recherche des informations sur les tache
    for tache_ in select tache from yvs_grh_montant_tache where regle_tache = employe_.regle_tache
    loop
        montant_ = montant_ + (select tache_montant_total(employe_.id, id_tache_, date_debut, date_fin));
    end loop;
	return montant_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tache_montant_all_total(bigint, date, date)
  OWNER TO postgres;

  -----------------------
  
  -- Function: tache_montant_total(bigint, integer, date, date)

-- DROP FUNCTION tache_montant_total(bigint, integer, date, date);

CREATE OR REPLACE FUNCTION tache_montant_total(id_ bigint, id_tache_ integer, date_debut date, date_fin date)
  RETURNS double precision AS
$BODY$DECLARE
    vide_ VARCHAR default null; 
	employe_ RECORD;
	tache_ RECORD;
    montant_ REAL default 0;
	
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, e.regle_tache, a.societe, c.id from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id 
        inner join yvs_grh_contrat_emps c on c.employe = e.id where e.id = id_ limit 1;
	
	-- recherche des informations sur les tache
    for tache_ in select quantite from yvs_grh_tache_emps where employe = employe_.id and tache < id_tache_ 
	and date_realisation >= date_debut and date_realisation <= date_fin 
    loop
        montant_ = montant_ + (select tache_montant(employe_.id, id_tache_, tache_.quantite));
    end loop;
	return montant_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tache_montant_total(bigint, integer, date, date)
  OWNER TO postgres;

  
  ---------
  
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
			select into presence_ id from yvs_grh_presence where employe = id_ and
				date_debut <= date_ and date_fin >= date_ and valider = true;
			if(presence_.id is not null) then
				total_ = total_ + (select heure_travail_effectif_jour_unique(presence_.id, date_, true));
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

-------------

-- Function: travail_jour_ferier(bigint, date, date)

-- DROP FUNCTION travail_jour_ferier(bigint, date, date);

CREATE OR REPLACE FUNCTION travail_jour_ferier(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0; 
	employe_ RECORD;
	presence_ RECORD;
	ferier_ RECORD;
	
BEGIN
	-- recherche des informations sur un employe
	select into employe_ e.id, a.societe, c.salaire_horaire, p.date_paiement_salaire from yvs_grh_employes e 
		inner join yvs_agences a on e.agence = a.id inner join yvs_societes s on a.societe = s.id 
		inner join yvs_parametre_grh p on p.societe = s.id inner join yvs_grh_contrat_emps c on c.employe = e.id
		where e.id = id_ limit 1;

	for ferier_ in select jour from yvs_jours_feries where (jour >= date_debut_ and jour < date_fin_) and societe = employe_.societe
	loop
		select into presence_ id from yvs_grh_presence where employe = id_ and
			date_debut <= ferier_.jour and date_fin >= ferier_.jour and valider = true;
		if(presence_.id is not null) then
			total_ = total_ + (select heure_travail_effectif_jour_unique(presence_.id, ferier_.jour, true));
		end if;
	end loop;
	if(total_ is null) then
		total_ = 0;
	end if;
	return total_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION travail_jour_ferier(bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION travail_jour_ferier(bigint, date, date) IS 'fonction qui compte le nombre d''heure travaillé durant les jours férié compris entre deux dates';

-------------------