-- Function: conge_jour_avoir(bigint, date, bigint)

-- DROP FUNCTION conge_jour_avoir(bigint, date, bigint);

CREATE OR REPLACE FUNCTION conge_jour_avoir(id_ bigint, date_jour_ date, exercice_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
	total_ REAL default 0; 
	employe_ RECORD;
	infos_ RECORD;
	code_ RECORD;
	exo_ RECORD;
	cpt_ REAL default 0; 
	date_d DATE ;
	duree_ INTEGER DEFAULT 0;
	interval_ interval;
    
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, co.conge_acquis, e.date_embauche, e.civilite, a.societe, co.source_first_conge, co.date_first_conge
		from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id inner join yvs_agences a on e.agence = a.id 
		where e.id = id_ limit 1;
	--recherche la date du dernier conge annuelle
	--SELECT INTO date_d co.date_fin FROM yvs_grh_conge_emps co WHERE co.type_conge='Annuel' AND co.employe=employe_.id AND (co.statut='C' or co.statut='V') ORDER BY co.date_fin DESC limit 1;
	--find last active Exercice
	SELECT INTO exo_ e.* FROM yvs_base_exercice e WHERE e.id=exercice_;
	select INTO code_  duree_cumule_conge FROM yvs_parametre_grh 
					      WHERE societe = employe_.societe limit 1; 
	--IF date_d IS NULL THEN
		--IF(employe_.source_first_conge='DEX') THEN
			date_d =exo_.date_debut;
		--ELSIF (employe_.source_first_conge='DE') THEN
			--date_d =employe_.date_embauche;
		--ELSIF(employe_.source_first_conge='DF') THEN
			--date_d =employe_.date_first_conge;
		--END IF;
	--END IF;
	-- recherche des informations sur la date de debut d'exercice de la societe		
		IF date_d IS NOT NULL THEN
			--nombre de mois entre les deux dates
			interval_=age(date_jour_,date_d);
			duree_ = (SELECT (12* extract(year FROM interval_)::integer) + (extract(month FROM interval_)::integer));
			IF (duree_ IS NOT NULL AND code_.duree_cumule_conge IS NOT NULL) THEN
			   IF(duree_>(code_.duree_cumule_conge *12)) THEN
				duree_=code_.duree_cumule_conge*12;
			    END IF;
			END IF;
		END IF;
				
	-- calcul du nombre de jours de conge a prendre a la date du jour
	total_ = (employe_.conge_acquis ) * duree_;	
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION conge_jour_avoir(bigint, date, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION conge_jour_avoir(bigint, date, bigint) IS 'retourne le nombre de jour du à un employé sur un intervalle de date';
