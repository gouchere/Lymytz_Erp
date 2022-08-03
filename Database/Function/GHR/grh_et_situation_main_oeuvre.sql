-- Function: grh_et_situation_main_oeuvre(bigint, character varying, bigint, boolean)

-- DROP FUNCTION grh_et_situation_main_oeuvre(bigint, character varying, bigint, boolean);

CREATE OR REPLACE FUNCTION grh_et_situation_main_oeuvre(IN societe_ bigint, IN agence_ character varying, IN header_ bigint, IN annuel_ boolean)
  RETURNS TABLE(id bigint, matricule character varying, nom character varying, sexe character varying, situation character varying, categorie character varying, echellon character varying, anciennete integer, cnps character varying, date_naissance date, age integer, date_embauche date, poste character varying, service character varying, salaire double precision) AS
$BODY$
DECLARE 
	data_ RECORD;
	query_ CHARACTER VARYING DEFAULT '';
	valeur_ CHARACTER VARYING;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_situation_main_oeuvre;
	CREATE TEMP TABLE IF NOT EXISTS table_situation_main_oeuvre(_id bigint, _matricule character varying, _nom character varying, _sexe character varying, _situation character varying, _categorie character varying, _echellon character varying, _anciennete integer, _cnps character varying, _date_naissance date, _age integer, _date_embauche date, _poste character varying, _service character varying, _salaire double precision);
	DELETE FROM table_situation_main_oeuvre;
	IF(annuel_)THEN
		query_ = 'SELECT e.id, concat(e.nom,'' '',e.prenom) AS nom, (SELECT (12* EXTRACT(YEAR FROM age(current_date, e.date_embauche))::integer) + (EXTRACT(MONTH FROM age(current_date, e.date_embauche))::integer)) AS anciennete,
			e.date_naissance , (SELECT EXTRACT(YEAR FROM current_date::date) - EXTRACT(YEAR FROM e.date_naissance)) AS age, e.matricule, e.etat_civil, c.categorie,
			h.echelon, e.date_embauche, p.intitule as poste, d.intitule as service, e.matricule_cnps as cnps, e.civilite,
			(SELECT SUM(grh_get_brut_annuel(x.societe, e.id, s.id)) FROM yvs_grh_ordre_calcul_salaire s INNER JOIN yvs_base_exercice x ON s.debut_mois BETWEEN x.date_debut AND x.date_fin INNER JOIN  yvs_grh_ordre_calcul_salaire o ON o.debut_mois BETWEEN x.date_debut AND x.date_fin WHERE o.id = '||header_||' AND x.societe = '||societe_||') as salaire
			FROM yvs_grh_categorie_professionelle c INNER JOIN yvs_grh_convention_collective cc ON c.id = cc.categorie INNER JOIN  yvs_grh_convention_employe ce ON cc.id = ce.convention
			INNER JOIN yvs_grh_echelons h ON cc.echellon = h.id INNER JOIN yvs_grh_employes e ON ce.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id INNER JOIN yvs_grh_departement d ON p.departement = d.id
			WHERE e.actif = TRUE AND ce.actif = TRUE';
	ELSE
		query_ = 'SELECT e.id, concat(e.nom,'' '',e.prenom) AS nom, (SELECT (12* EXTRACT(YEAR FROM age(current_date, e.date_embauche))::integer) + (EXTRACT(MONTH FROM age(current_date, e.date_embauche))::integer)) AS anciennete,
			e.date_naissance , (SELECT EXTRACT(YEAR FROM current_date::date) - EXTRACT(YEAR FROM e.date_naissance)) AS age, e.matricule, e.etat_civil, c.categorie,
			h.echelon, e.date_embauche, p.intitule as poste, d.intitule as service, e.matricule_cnps as cnps, e.civilite,
			grh_get_brut_annuel('||societe_||', e.id, '||header_||') as salaire
			FROM yvs_grh_categorie_professionelle c INNER JOIN yvs_grh_convention_collective cc ON c.id = cc.categorie INNER JOIN  yvs_grh_convention_employe ce ON cc.id = ce.convention
			INNER JOIN yvs_grh_echelons h ON cc.echellon = h.id INNER JOIN yvs_grh_employes e ON ce.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id INNER JOIN yvs_grh_departement d ON p.departement = d.id
			WHERE e.actif = TRUE AND ce.actif = TRUE';
	END IF;
	IF(agence_ IS NOT NULL AND agence_ NOT IN ('', ' '))THEN
		query_ = query_ || ' AND e.agence IN (0';
		FOR valeur_ IN SELECT val FROM regexp_split_to_table(agence_,',') val
		LOOP
			query_ = query_ || ', '||valeur_;
		END LOOP;
		query_ = query_ || ')';
	END IF;
	query_ = query_ || ' ORDER BY e.matricule';
	FOR data_ IN EXECUTE query_
	LOOP
		IF(COALESCE(data_.salaire, 0) > 0)THEN
			INSERT INTO table_situation_main_oeuvre VALUES(data_.id, data_.matricule, data_.nom, data_.civilite, data_.etat_civil, data_.categorie, data_.echelon, data_.anciennete, data_.cnps, data_.date_naissance, data_.age, data_.date_embauche, data_.poste, data_.service, COALESCE(data_.salaire, 0));
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM table_situation_main_oeuvre ORDER BY _matricule, _nom;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_situation_main_oeuvre(bigint, character varying, bigint, boolean)
  OWNER TO postgres;
