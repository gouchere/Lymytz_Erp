-- Function: conge_jour_perm_pris(bigint, date, bigint, character varying)

-- DROP FUNCTION conge_jour_perm_pris(bigint, date, bigint, character varying);

CREATE OR REPLACE FUNCTION conge_jour_perm_pris(id_ bigint, date_fin_ date, exercice_ bigint, effet_ character varying)
  RETURNS double precision AS
$BODY$DECLARE 
	total_ REAL default 0; 
	employe_ RECORD;
	cpt_ REAL default 0;
	exo_ RECORD;
	code_ RECORD; 
	date_d date;
	--effet_ VARCHAR default 'CONGE ANNUEL';
    
BEGIN
      -- recherche des informations sur un employe
	select into employe_ e.id, co.conge_acquis, e.date_embauche, e.civilite, a.societe, co.source_first_conge, co.date_first_conge
		from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id inner join yvs_agences a on e.agence = a.id 
		where e.id = id_ limit 1;
	--recherche la date du dernier conge annuelle
	--SELECT INTO date_d co.date_fin FROM yvs_grh_conge_emps co WHERE co.type_conge='Annuel' AND co.employe=employe_.id AND (co.statut='C' or co.statut='V') ORDER BY co.date_fin DESC limit 1;
	--find last active Exercice
	SELECT INTO exo_ e.* FROM yvs_base_exercice e WHERE e.id=exercice_;
	select into code_ date_debut_exercice, duree_cumule_conge FROM yvs_parametre_grh 
								  WHERE societe = employe_.societe limit 1; 
	IF date_d IS NULL THEN
		IF(employe_.source_first_conge='DEX') THEN
			date_d =exo_.date_debut;
		ELSIF (employe_.source_first_conge='DE') THEN
			date_d =employe_.date_embauche;
		ELSIF(employe_.source_first_conge='DF') THEN
			date_d =employe_.date_first_conge;
		END IF;
	END IF;
	-- recherche des conges pris par l'employe a la date d'exercice
    select into cpt_ sum(c.date_fin - c.date_debut) from yvs_grh_conge_emps c where c.employe = employe_.id and c.date_debut >= date_d
            and c.date_fin < date_fin_ and c.effet = effet_ and (c.statut = 'V' or c.statut = 'C') AND (c.nature='P' AND c.duree_permission='L') group by employe;
	-- control pour connaitre si le resultat de la recherche des conges deja pris par l'employe a ete null
	if (cpt_ is null) then
		cpt_ =0;
	end if;
        total_ = cpt_;
    -- control pour connaitre si le resultat de la recherche des conges deja pris par l'employe a ete null
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION conge_jour_perm_pris(bigint, date, bigint, character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION conge_jour_perm_pris(bigint, date, bigint, character varying) IS 'retourne le nombre de jour de permission  pris et à faire valoir sur le congé annuel ou autorisé';
