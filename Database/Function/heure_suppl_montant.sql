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
