-- Function: conge_jour_pris_annuel(bigint, date, date)

 DROP FUNCTION conge_jour_pris_annuel(bigint, date, date);

CREATE OR REPLACE FUNCTION conge_jour_pris_annuel(id_ bigint, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE 
    total_ REAL default 0; 
	employe_ RECORD;
	cpt_ REAL default 0; 
	date_d DATE ;
	effet_ VARCHAR default 'CONGE ANNUEL';
	type_ VARCHAR default 'Annuel';
    
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, e.date_embauche, e.civilite, a.societe, e.date_cloture_conge as date_cloture
	from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id where e.id = id_ limit 1;
	--recherche la date du dernier conge annuelle
	SELECT INTO date_d co.date_fin FROM yvs_grh_conge_emps co WHERE co.type_conge='Annuel' AND co.employe=employe_.id AND co.statut='O' ORDER BY co.date_fin DESC limit 1;
	-- recherche des conges pris par l'employe a la date d'exercice
	select into cpt_ sum(c.date_fin - c.date_debut) from yvs_grh_conge_emps c where c.employe = employe_.id and c.date_debut >= date_d
            and c.date_fin <= date_fin_ and (effet = effet_  OR type_conge=type_) and c.statut = 'O' group by employe;
	-- control pour connaitre si le resultat de la recherche des conges deja pris par l'employe a ete null
	if (cpt_ is null) then
		cpt_ =0;
	end if;
        total_ = total_ + cpt_;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION conge_jour_pris_annuel(bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION conge_jour_pris_annuel(bigint, date, date) IS 'fonction qui retourne le total des jours de conge pris par un employe au mois precedent
elle prend en parametre l''id de l''employe, la date du jour, l''effet du conge et un booleen qui precise si on retourne les jours permis ou non';
