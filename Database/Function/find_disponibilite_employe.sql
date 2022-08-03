-- Function: find_disponibilite_employe(bigint, date)

-- DROP FUNCTION find_disponibilite_employe(bigint, date);

CREATE OR REPLACE FUNCTION find_disponibilite_employe(employe_ bigint, date_jour_ date)
  RETURNS character varying AS
$BODY$
DECLARE result_ character varying; 
	id_ bigint;
    
BEGIN
 SELECT INTO id_ m.id FROM yvs_grh_missions m WHERE m.employe=employe_ AND (date_jour_ BETWEEN m.date_debut AND m.date_fin) AND m.actif is true;
 IF id_ IS NOT NULL THEN 
	result_='EN Mission';
 END IF;
SELECT INTO id_ y.id FROM yvs_grh_formation_emps y WHERE y.employe=employe_ AND (date_jour_ BETWEEN y.date_debut AND y.date_fin) AND y.actif is true;
 IF id_ IS NOT NULL THEN 
	result_='EN Formation';
 END IF;
 SELECT INTO id_ y.id FROM yvs_grh_conge_emps y WHERE y.employe=employe_ AND (date_jour_ BETWEEN y.date_debut AND y.date_fin) AND y.actif is true;
 IF id_ IS NOT NULL THEN 
	result_='En Congé';
 END IF;
 --cherche la journée férié
 SELECT INTO id_ a.societe FROM yvs_grh_employes y INNER JOIN yvs_agences a ON a.id=y.agence WHERE y.id=employe_ LIMIT 1;
 SELECT INTO result_ y.titre FROM yvs_jours_feries y WHERE date_jour_ =y.jour AND y.actif is true AND y.societe=id_;
	RETURN result_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION find_disponibilite_employe(bigint, date)
  OWNER TO postgres;
