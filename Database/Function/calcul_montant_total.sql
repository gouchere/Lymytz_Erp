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
