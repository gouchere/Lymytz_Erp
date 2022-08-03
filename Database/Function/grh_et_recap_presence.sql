-- Function: grh_et_recap_presence(date, date, bigint)

-- DROP FUNCTION grh_et_recap_presence(date, date, bigint);

CREATE OR REPLACE FUNCTION grh_et_recap_presence(IN debut_ date, IN fin_ date, IN societe_ bigint)
  RETURNS TABLE(nom_prenom character varying, poste character varying, nbre_presence integer, nbre_requis integer, nbre_mission integer, nbre_conge integer, autre integer) AS
$BODY$
declare 
	employe_ record;
	nb_presence integer;
	nb_requis integer;
	nb_mission integer;
	nb_conge integer;
	nb_others integer;
	name_ character varying ;
	pname_ character varying ;
	
	
begin 	
	--DROP TABLE IF EXISTS grh_recap_presence;
	CREATE TEMP TABLE IF NOT EXISTS  grh_recap_presence(nom_prenom_ character varying, 
					     poste_ character varying,
				             nbre_presence_ integer, 
				             nbre_requis_ integer, 
				             nbre_mission_ integer, 
					     nbre_conge_ integer, 
					     autre_ integer); 
	DELETE FROM grh_recap_presence;
	for employe_ in select e.id, e.nom, e.prenom, e.matricule, p.intitule  FROM yvs_grh_employes e INNER JOIN yvs_grh_poste_de_travail p ON p.id=e.poste_actif 
												       INNER JOIN yvs_agences ag on ag.id=e.agence WHERE ag.id=societe_ AND e.actif is true
												       ORDER BY e.agence, e.nom
	loop
		--compte les présence
		SELECT INTO nb_presence COUNT(*) FROM yvs_grh_presence p WHERE p.employe=employe_.id AND p.valider is true and p.date_debut BETWEEN debut_ and fin_ ;
		IF nb_presence is null then 
			nb_presence=0;
		END IF;
		--compte les présence
		SELECT INTO nb_requis jour_travail_requis(employe_.id,debut_,fin_);
		IF nb_requis is null then 
			nb_requis=0;
		END IF;
		--compte les mission
		SELECT INTO nb_mission SUM(m.duree_mission) FROM yvs_grh_missions m WHERE m.employe=employe_.id AND (m.date_debut BETWEEN debut_ and fin_) AND (m.statut_mission='V' OR m.statut_mission='T' OR m.statut_mission='C' );
		--compte les congé
		IF nb_mission is null then 
			nb_mission=0;
		END IF;
		nb_conge=(select grh_get_duree_conge(debut_,fin_,employe_.id));		
		IF nb_conge is null then 
			nb_conge=0;
		END IF;
		name_=employe_.nom;
		pname_=employe_.prenom;
		IF employe_.nom is null then 
			name_='';
		END IF;
		IF employe_.prenom is null then 
			pname_='';
		END IF;
		
		INSERT INTO grh_recap_presence VALUES(name_ ||' ' ||pname_, employe_.intitule,nb_presence,nb_requis, nb_mission,nb_conge,0);
	end loop;

	return QUERY SELECT * FROM grh_recap_presence order by nom_prenom_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_presence(date, date, bigint)
  OWNER TO postgres;
