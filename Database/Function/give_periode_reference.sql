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
