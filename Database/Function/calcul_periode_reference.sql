-- Function: calcul_periode_reference(bigint)

-- DROP FUNCTION calcul_periode_reference(bigint);

CREATE OR REPLACE FUNCTION calcul_periode_reference(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
	employe_ RECORD; 
	code_ RECORD;
	date_d DATE ;
	date_new_ DATE ;
	duree_ INTEGER DEFAULT 0;
	interval_ interval;
    
BEGIN
	-- recherche des informations sur un employe
	select into employe_ e.id, co.conge_acquis, e.date_embauche, e.civilite, a.societe
		from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id inner join yvs_agences a on e.agence = a.id 
		where e.id = id_ limit 1;
	--recherche la date du dernier conge annuelle
	SELECT INTO date_d co.date_fin FROM yvs_grh_conge_emps co WHERE  co.employe=id_ AND co.type_conge='Annuelle' AND co.statut='C' ORDER BY co.date_fin DESC offset 0 limit 1;
	select into code_ date_debut_exercice, duree_cumule_conge from yvs_parametre_grh 
                where societe = employe_.societe limit 1; 
                --recherche la date de départ du nouveau congé enregisté
	SELECT INTO date_new_ co.date_debut FROM yvs_grh_conge_emps co WHERE  co.employe=id_ AND co.type_conge='Annuelle' AND co.statut='V' ORDER BY co.date_fin DESC offset 0 limit 1;
	IF date_d IS NULL THEN		
		date_d =code_.date_debut_exercice;
	END IF;
	-- recherche des informations sur la date de debut d'exercice de la societe		
		IF date_d IS NOT NULL THEN
		  --nombre de mois entre les deux dates
		  interval_=age(date_new_,date_d);
		  duree_ = (SELECT (12* extract(year FROM interval_)::integer) + (extract(month FROM interval_)::integer));			
		END IF;
				
	-- calcul du nombre de jours de conge a prendre a la date du jour
	IF(duree_ IS NULL) THEN 
	   duree_=0;
	END IF;
	return duree_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION calcul_periode_reference(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION calcul_periode_reference(bigint) IS 'retourne le nombre de mois de référence. qui correspond au nombre de mois entre 
le retours du précédent congé et le départ pour le nouveau congé';
