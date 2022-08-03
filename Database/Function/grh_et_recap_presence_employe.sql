-- Function: grh_et_recap_presence_employe(bigint, bigint, bigint, bigint, date, date)

-- DROP FUNCTION grh_et_recap_presence_employe(bigint, bigint, bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION grh_et_recap_presence_employe(IN societe_ bigint, IN agence_ bigint, IN service_ bigint, IN employe_ bigint, IN debut_ date, IN fin_ date)
  RETURNS TABLE(date_presence date, observation character varying, date_debut date, date_fin date, heure_debut time without time zone, heure_fin time without time zone, total_presence double precision, total_suppl double precision, validation character varying, jour character varying, employe bigint, nom character varying, prenom character varying, matricule character varying, duree_retard double precision, duree_pause time without time zone) AS
$BODY$
declare 
	_employe record;
	
	_connexion character varying default 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
	_query character varying default '';	
	
begin 	
	--DROP TABLE IF EXISTS _table_recap_presence_employe;
	CREATE TEMP TABLE IF NOT EXISTS _table_recap_presence_employe(date_presence_ date, observation_ character varying, date_debut_ date, date_fin_ date, heure_debut_ time without time zone, heure_fin_ time without time zone, 
					total_presence_ double precision, total_suppl_ double precision, validation_ character varying, _jour_ character varying, _employe_ bigint, nom_ character varying, prenom_ character varying, matricule_ character varying, _duree_retard double precision, _duree_pause time without time zone); 
	DELETE FROM _table_recap_presence_employe;
	IF(employe_ IS NOT NULL AND employe_ > 0)THEN
		_query = 'SELECT e.id, e.matricule, e.civilite, e.nom, e.prenom, e.date_embauche, e.agence, c.conge_acquis, c.source_first_conge, c.date_first_conge 
			FROM yvs_grh_employes e INNER JOIN yvs_grh_contrat_emps c ON c.employe = e.id WHERE e.actif IS TRUE AND e.id = '||employe_;
	ELSIF(service_ IS NOT NULL AND service_ > 0)THEN
		_query = 'SELECT e.id, e.matricule, e.civilite, e.nom, e.prenom, e.date_embauche, e.agence, c.conge_acquis, c.source_first_conge, c.date_first_conge 
			FROM yvs_grh_employes e INNER JOIN yvs_grh_contrat_emps c ON c.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON p.id=e.poste_actif WHERE e.actif IS TRUE AND p.departement = '||service_||' ORDER BY e.nom';
	ELSIF(agence_ IS NOT NULL AND agence_ > 0)THEN
		_query = 'SELECT e.id, e.matricule, e.civilite, e.nom, e.prenom, e.date_embauche, e.agence, c.conge_acquis, c.source_first_conge, c.date_first_conge 
			FROM yvs_grh_employes e INNER JOIN yvs_grh_contrat_emps c ON c.employe = e.id WHERE e.actif IS TRUE AND e.agence = '||agence_||' ORDER BY e.nom';
	ELSE
		_query = 'SELECT e.id, e.matricule, e.civilite, e.nom, e.prenom, e.date_embauche, e.agence, c.conge_acquis, c.source_first_conge, c.date_first_conge 
			FROM yvs_grh_employes e INNER JOIN yvs_grh_contrat_emps c ON c.employe = e.id INNER JOIN yvs_agences a ON a.id = e.agence WHERE e.actif IS TRUE AND a.societe = '||societe_||' ORDER BY e.nom';
	END IF;
	IF(_query IS NOT NULL AND _query != '')THEN
		FOR _employe IN EXECUTE _query			
		LOOP
			INSERT INTO _table_recap_presence_employe SELECT * FROM grh_et_recap_presence_employe(_employe.id, debut_, fin_);
		END LOOP;
	END IF;
	return QUERY SELECT * FROM _table_recap_presence_employe order by date_presence_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_presence_employe(bigint, bigint, bigint, bigint, date, date)
  OWNER TO postgres;
