-- Function: conge_jour_supplementaire(bigint, date)

-- DROP FUNCTION conge_jour_supplementaire(bigint, date);

CREATE OR REPLACE FUNCTION conge_jour_supplementaire(id_ bigint, date_jour_ date)
  RETURNS double precision AS
$BODY$
DECLARE 
	total_ REAL default 0; 
	employe_ RECORD;
	infos_ RECORD;
	cpt_ REAL default 0; 
	diff_ INTEGER;
	supp_ INTEGER DEFAULT 0;
    
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, co.conge_acquis, e.date_embauche, e.civilite, a.societe, e.date_cloture_conge as date_cloture
		from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id inner join yvs_agences a on e.agence = a.id 
		where e.id = id_ limit 1;	
                
	-- determination du nombre de jours de conge a prendre en fonction de l'anciennete
	diff_ = (select extract(year from date_jour_)) - (select extract(year from employe_.date_embauche)); --ancienneté =différence entre la date du jour et la date d'embauche	
        if(diff_ is null)then
			diff_ = 0;
        end if;
		supp_= (select cast((diff_/5) AS integer))*2;  --gagne 2 ans par periode de 5ans
	RAISE NOTICE ' Suppp Ancienneté %',date_jour_;
	RAISE NOTICE ' MAJ Suppp Ancienneté %',employe_.date_embauche;
	
	if (employe_.civilite != 'M.') then
		-- recherche des personnes en charge d'un employe et calcul du total de jour
		for infos_ in select p.id, p.date_naissance from yvs_grh_personne_en_charge p where employe = employe_.id 
			and p.epouse = false order by p.id asc
		loop
			diff_ = (select extract(year from current_date)) - (select extract(year from infos_.date_naissance));		-- age de l'enfant
			-- determination du nombre de jours de conge a prendre si on est dans le cas d'une femme mere
			select into cpt_ i.nb_jour from yvs_interval_majoration i inner join yvs_grh_majoration_conge m on i.majoration_conge = m.id 
				where m.nature = 'Dame mere' and i.valeur_maximal >= diff_ and i.valeur_minimal <= diff_;
			if( cpt_ is  null)then
				cpt_ = 0;
			end if;
			total_ = total_ + cpt_;
		end loop;
	end if;
	return total_ + supp_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION conge_jour_supplementaire(bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION conge_jour_supplementaire(bigint, date) IS 'fonction qui retourne le total des jours de conge supplémentaire d''un employé par un employe au long de son exercice en fonction de sa derniere date de cloture des conges

elle prend en parametre l''''id de l''''employe, la date du jour,et la date du jour';
