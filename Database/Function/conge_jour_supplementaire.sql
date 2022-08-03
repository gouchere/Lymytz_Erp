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
	date_d date;
	duree_ integer default 0;
	interval_ interval;
	code_ RECORD; 
	 exercice_ bigint;
    
BEGIN
	-- recherche des informations sur un employe
	select into employe_ e.id, co.conge_acquis, e.date_embauche, e.civilite, a.societe, e.date_cloture_conge , a.societe as date_cloture
		from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id inner join yvs_agences a on e.agence = a.id 
		where e.id = id_ limit 1;		
                
	-- determination du nombre de jours de conge a prendre en fonction de l'anciennete
	--diff_ = (select extract(year from date_jour_)) - (select extract(year from employe_.date_embauche)); --ancienneté =différence entre la date du jour et la date d'embauche	
	diff_=(SELECT (12* extract(year FROM date_jour_)::integer) + (extract(month FROM date_jour_)::integer)) - (SELECT (12* extract(year FROM employe_.date_embauche)::integer) + (extract(month FROM employe_.date_embauche)::integer));
        if(diff_ is null)then
		diff_ = 0;
        end if;
        diff_=(select cast((diff_/12) AS integer)); --converti en année
        
	-- recuperation des parametre de conge
	select into infos_ id, anciennete, nb_jour_supp from yvs_grh_majoration_conge m where m.nature = 'Anciennete' and m.societe = employe_.societe;
	if(infos_ is not null)then
		supp_= (select cast((diff_/infos_.anciennete) AS integer))*infos_.nb_jour_supp;  --gagne 2 ans par periode de 5ans
	end if;
	if(supp_ is  null)then
		supp_ = 0;
	end if;
	
	if (employe_.civilite != 'M.') then
		-- recherche des personnes en charge d'un employe et calcul du total de jour
		for infos_ in select p.id, p.date_naissance from yvs_grh_personne_en_charge p where employe = employe_.id 
			and p.epouse = false order by p.id asc
		loop
			diff_ = (select extract(year from current_date)) - (select extract(year from infos_.date_naissance));		-- age de l'enfant
			-- determination du nombre de jours de conge a prendre si on est dans le cas d'une femme mere
			select into cpt_ i.nb_jour from yvs_grh_interval_majoration i inner join yvs_grh_majoration_conge m on i.majoration_conge = m.id 
				where m.nature = 'Dame mere' and m.societe = employe_.societe and (diff_ between i.valeur_maximal and i.valeur_minimal);
			if( cpt_ is  null)then
				cpt_ = 0;
			end if;
			total_ = total_ + cpt_;
		end loop;
	end if;
	select into code_ date_debut_exercice, duree_cumule_conge FROM yvs_parametre_grh WHERE societe = employe_.societe limit 1; 
	SELECT INTO date_d e.date_debut FROM yvs_base_exercice e WHERE e.id=exercice_;
	IF date_d IS NOT NULL THEN
		--nombre de mois entre les deux dates
		interval_=age(date_jour_,date_d);
		duree_ = (SELECT (12* extract(year FROM interval_)::integer) + (extract(month FROM interval_)::integer));
		IF (duree_ IS NOT NULL AND code_.duree_cumule_conge IS NOT NULL) THEN
		   IF(duree_>(code_.duree_cumule_conge *12)) THEN
			duree_=code_.duree_cumule_conge*12;
		    END IF;
		END IF;
		if(duree_ is null or duree_ < 0)then
			duree_ = 1;
		end if;
	END IF;
	RAISE NOTICE ' duree_ %',duree_;
	RAISE NOTICE ' Ancienneté %',diff_;
	RAISE NOTICE ' Suppp Ancienneté %',supp_;
	RAISE NOTICE ' MAJ Suppp Ancienneté %',employe_.date_embauche;
	
	return abs((total_ + supp_) * duree_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION conge_jour_supplementaire(bigint, date)
  OWNER TO postgres;
