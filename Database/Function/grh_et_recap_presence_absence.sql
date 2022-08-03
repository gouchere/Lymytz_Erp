-- Function: grh_et_recap_presence_absence(date, date, bigint, bigint, character varying)
DROP FUNCTION grh_et_recap_presence_absence(date, date, bigint, bigint, character varying);
CREATE OR REPLACE FUNCTION grh_et_recap_presence_absence(IN debut_ date, IN fin_ date, IN agence_ bigint, IN service_ bigint, IN emp_ character varying)
  RETURNS TABLE(nom_prenom character varying, poste character varying, d_requis integer, nbre_presence integer, nbre_abs integer, nbre_mission integer, nbre_conge integer, autre integer, jour_supf double precision, matricule character varying, nbre_retard double precision) AS
$BODY$
declare 
	employe_ record;
	nb_presence double precision default 0;
	nb_abs double precision default 0;
	nb_mission double precision default 0;
	nb_conge double precision default 0;
	nb_others double precision default 0;
	nb_retard double precision default 0;
	d_requis double precision default 0;
	name_ character varying ;
	pname_ character varying ;
	dat_ DATE;
	jour_ INTEGER ;
	chJour_ character varying ;
	jo_ bigint;
	id_ bigint;
	sup_ double precision default 0;
	sup_f double precision default 0;
	line_ record;
	jf_ integer default 0;
	
	connexion character varying default 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
	query_ character varying default '';	
	
begin 	
	DROP TABLE IF EXISTS grh_recap_presence_abs;
	CREATE TEMP TABLE IF NOT EXISTS grh_recap_presence_abs(nom_prenom_ character varying, poste_ character varying, d_requis_ integer, nbre_presence_ integer, 
				             nbre_abs_ integer, nbre_mission_ integer, nbre_conge_ integer, autre_ integer, jour_supf_ double precision, matricule_ character varying, nbre_retard_ double precision); 
	DELETE FROM grh_recap_presence_abs;
	IF(service_ IS NULL OR service_ < 1)THEN
		if(emp_ is null or emp_ = '')then
			query_ = 'select e.id, e.nom, e.prenom, e.matricule, p.intitule FROM yvs_grh_employes e INNER JOIN yvs_grh_poste_de_travail p ON p.id=e.poste_actif 
					WHERE e.agence = '||agence_||' AND e.actif is true ORDER BY e.nom';
		else
			query_ = 'select e.id, e.nom, e.prenom, e.matricule, p.intitule FROM yvs_grh_employes e INNER JOIN yvs_grh_poste_de_travail p ON p.id=e.poste_actif 
					WHERE e.agence = '||agence_||' AND e.actif is true AND (upper(e.nom) like '''||upper(emp_)||'%'' or upper(e.prenom) like '''||upper(emp_)||'%'' or upper(e.matricule) like '''||upper(emp_)||'%'')
					ORDER BY e.nom';
		end if;
	ELSE
		if(emp_ is null or emp_ = '')then
			query_ = 'select e.id, e.nom, e.prenom, e.matricule, p.intitule FROM yvs_grh_employes e INNER JOIN yvs_grh_poste_de_travail p ON p.id=e.poste_actif 
					WHERE e.agence = '||agence_||' AND e.actif is true AND p.departement = '||service_||' ORDER BY e.nom';
		else
			query_ = 'select e.id, e.nom, e.prenom, e.matricule, p.intitule FROM yvs_grh_employes e INNER JOIN yvs_grh_poste_de_travail p ON p.id=e.poste_actif 
					WHERE e.agence = '||agence_||' AND e.actif is true AND p.departement = '||service_||' 
					AND (upper(e.nom) like '''||upper(emp_)||'%'' or upper(e.prenom) like '''||upper(emp_)||'%'' or upper(e.matricule) like '''||upper(emp_)||'%'')
					ORDER BY e.nom';
		end if;
	END IF;
	IF(query_ is not null and query_ != '')THEN
		FOR employe_ IN EXECUTE query_
		LOOP
			nb_presence=0;
			sup_=0;nb_mission=0;nb_conge=0;nb_abs=0;nb_others=0;nb_retard=0;
			FOR line_ IN SELECT * from grh_presence_durees(employe_.id, agence_, 0, debut_, fin_)
			LOOP
				if(line_.element = 'Js')then
					sup_= line_.valeur;
				elsif(line_.element = 'Jsf')then
					sup_f= line_.valeur;				
				elsif(line_.element = 'Je')then
					nb_presence=line_.valeur;
				elsif(line_.element = 'Mi')then
					nb_mission=line_.valeur;
				elsif(line_.element = 'Ct')then
					nb_conge=line_.valeur;
				elsif(line_.element = 'Ab')then
					nb_abs=line_.valeur;
				elsif(line_.element = 'Tr')then
					d_requis=line_.valeur;
				elsif(line_.element = 'Rtb')then
					nb_retard=line_.valeur;
				else
					nb_others= nb_others + line_.valeur;
				end if;
			END LOOP;
			name_=employe_.nom;
			pname_=employe_.prenom;
			IF employe_.nom is null then 
				name_='';
			END IF;
			IF employe_.prenom is null then 
				pname_='';
			END IF;	
			INSERT INTO grh_recap_presence_abs VALUES(name_ ||' ' ||pname_, employe_.intitule, d_requis , nb_presence, nb_abs , nb_mission, nb_conge, sup_, sup_f, employe_.matricule, nb_retard);
		END LOOP;
	END IF;

	return QUERY SELECT * FROM grh_recap_presence_abs order by nom_prenom_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_recap_presence_absence(date, date, bigint, bigint, character varying)
  OWNER TO postgres;
