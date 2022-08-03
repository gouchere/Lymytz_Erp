-- Function: grh_et_progression_salariale(bigint, bigint, bigint, date, date)
-- DROP FUNCTION grh_et_progression_salariale(bigint, bigint, bigint, date, date);
CREATE OR REPLACE FUNCTION grh_et_progression_salariale(IN societe_ bigint, IN agence_ bigint, IN employe_ bigint, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(element bigint, designation character varying, retenue boolean, header bigint, reference character varying, date_debut date, date_fin date, montant double precision, is_total boolean) AS
$BODY$
DECLARE 
	data_ RECORD;	
	dates_ RECORD;
	header_ RECORD;

	montant_ DOUBLE PRECISION DEFAULT 0;
	gain_ DOUBLE PRECISION DEFAULT 0;
	retenue_ DOUBLE PRECISION DEFAULT 0;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_progression_salariale;
	CREATE TEMP TABLE IF NOT EXISTS table_progression_salariale(_element bigint, _designation character varying, _retenue boolean, _header bigint, _reference_ character varying, _date_debut date, _date_fin date, _montant double precision, _is_total boolean);
	DELETE FROM table_progression_salariale;
	FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, 'M')
	LOOP	
		SELECT INTO header_ * FROM yvs_grh_ordre_calcul_salaire o WHERE o.societe = societe_ AND o.debut_mois BETWEEN dates_.date_debut AND dates_.date_fin;
		IF(COALESCE(header_.id, 0) > 0)THEN
			FOR data_ IN SELECT * FROM yvs_grh_element_salaire e INNER JOIN yvs_grh_rubrique_bulletin r ON e.rubrique = r.id WHERE e.visible_bulletin IS TRUE AND r.societe = societe_
			LOOP
				IF(COALESCE(data_.retenue, false))THEN
					SELECT INTO montant_ COALESCE(d.retenu_salariale, 0) FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id 
						INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id WHERE d.element_salaire = data_.id AND b.entete = header_.id AND c.employe = employe_ and (b.statut IN ('V', 'P'));
				ELSE
					SELECT INTO montant_ COALESCE(d.montant_payer, 0) FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id 
						INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id WHERE d.element_salaire = data_.id AND b.entete = header_.id AND c.employe = employe_ and (b.statut IN ('V', 'P'));
				END IF;
				IF(COALESCE(montant_, 0) != 0)THEN
					INSERT INTO table_progression_salariale VALUES(data_.id, UPPER(data_.nom), COALESCE(data_.retenue, false), header_.id, header_.reference, header_.debut_mois, header_.fin_mois, COALESCE(montant_, 0), false);
				END IF;
			END LOOP;
			SELECT INTO retenue_ SUM(COALESCE(d.retenu_salariale, 0)) FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id 
				INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id INNER JOIN yvs_grh_element_salaire e ON d.element_salaire = e.id 
				WHERE COALESCE(e.retenue, FALSE) IS TRUE AND e.visible_bulletin IS TRUE AND b.entete = header_.id AND c.employe = employe_ and (b.statut IN ('V', 'P'));
			SELECT INTO gain_ SUM(COALESCE(d.montant_payer, 0)) FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id 
				INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id INNER JOIN yvs_grh_element_salaire e ON d.element_salaire = e.id 
				WHERE COALESCE(e.retenue, FALSE) IS FALSE AND e.visible_bulletin IS TRUE AND b.entete = header_.id AND c.employe = employe_ and (b.statut IN ('V', 'P'));
			montant_ = COALESCE(gain_, 0) - COALESCE(retenue_, 0);
			IF(COALESCE(montant_, 0) != 0)THEN
				INSERT INTO table_progression_salariale VALUES(0, 'NET A PAYER', FALSE, header_.id, header_.reference, header_.debut_mois, header_.fin_mois, COALESCE(montant_, 0), true);
			END IF;
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM table_progression_salariale ORDER BY _is_total, _date_debut, _date_fin, _retenue DESC, _designation;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_progression_salariale(bigint, bigint, bigint, date, date)
  OWNER TO postgres;
