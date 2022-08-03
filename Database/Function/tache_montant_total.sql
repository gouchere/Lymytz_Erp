-- Function: tache_montant_total(bigint, date, date)

-- DROP FUNCTION tache_montant_total(bigint, date, date);

CREATE OR REPLACE FUNCTION tache_montant_total(id_ bigint, date_debut date, date_fin date)
  RETURNS double precision AS
$BODY$DECLARE
    vide_ VARCHAR default null; 
	employe_ RECORD;
	tache_ RECORD;
	montant_ double precision default 0;
	rem_tache_ RECORD;
	prime_ RECORD;
	interval_ RECORD ;
	quantite_ double precision default 0;
	
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, e.regle_tache, a.societe FROM yvs_grh_employes e inner join yvs_agences a on e.agence = a.id  where e.id = id_ limit 1;
	
	--trouve toutes les tâche réalisé par l'employé entre deux date
	for tache_ IN SELECT rt.*, te.id, te.tache AS id_montant_tache FROM yvs_grh_realisation_tache rt INNER JOIN yvs_grh_tache_emps te ON te.id=rt.tache WHERE te.employe=employe_.id AND rt.statut='T' 
		LOOP	
			--pour chacune de ces tâche calcul le montant
			SELECT INTO rem_tache_ m.* FROM yvs_grh_montant_tache m WHERE m.id=tache_.id_montant_tache AND m.actif is true;
			IF rem_tache_.montant IS NOT NULL THEN
				montant_ = (montant_ + (rem_tache_.montant * tache_.pourcentage_validation/100));
			END IF;
			IF (rem_tache_.prime_tache IS NOT NULL  AND tache_.quantite_realise IS NOT NULL )THEN
			
				--calcul la prime correspondant à la quantité réalisé de cette tache
					--récupération ordonné des primes par quantité
					FOR prime_ IN SELECT  i.* FROM yvs_grh_interval_prime_tache i INNER JOIN yvs_grh_prime_tache p ON p.id=i.prime_tache WHERE p.id=rem_tache_.prime_tache AND p.actif IS TRUE ORDER BY i.quantite DESC
					LOOP						
						IF tache_.quantite_realise >= prime_.quantite THEN
						  interval_ :=prime_;
						  EXIT; 
						END IF;
					END LOOP;					
				--IF interval_ IS NOT NULL THEN 					
				RAISE NOTICE 'HERE %',interval_.quantite;
					IF interval_.taux IS FALSE THEN
					  montant_=montant_+ (interval_.montant * tache_.quantite_realise);
					ELSE
					  montant_=montant_+ ((interval_.montant*rem_tache_.montant/100)*tache_.quantite_realise); 	
					END IF;
				--END IF;
			END IF;			
		END LOOP;
	return montant_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tache_montant_total(bigint, date, date)
  OWNER TO postgres;
