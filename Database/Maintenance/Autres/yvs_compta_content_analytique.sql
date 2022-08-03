
CREATE OR REPLACE FUNCTION yvs_compta_content_analytique(IN agence_ bigint, IN header_ bigint, IN element_ bigint, IN compte_ bigint, IN tiers_ bigint, IN valeur_ double precision, IN mode_ character varying, IN analytique_ boolean, IN retenue_ boolean, IN salarial_ boolean)
  RETURNS TABLE(error character varying, centre bigint, valeur double precision, coefficient double precision) AS
$BODY$
DECLARE
	_error_ CHARACTER VARYING DEFAULT '';

	ligne_ RECORD;
	query_ CHARACTER VARYING DEFAULT '';
BEGIN   
	DROP TABLE IF EXISTS table_compta_content_analytique;
	CREATE TEMP TABLE IF NOT EXISTS table_compta_content_analytique(_error character varying, _centre bigint, _valeur double precision, _coefficient double precision);
	DELETE FROM table_compta_content_analytique;
	IF(analytique_ IS TRUE AND (valeur_ IS NOT NULL AND valeur_ > 0) AND (compte_ IS NOT NULL AND compte_ > 0))THEN
		IF(element_ IS NULL OR element_ < 1 )THEN
			query_ = 'SELECT ap.centre_analytique AS centre, (('||valeur_||') *(COALESCE(ap.coefficient, 1)/100)) AS montant, ap.coefficient AS coeficient FROM yvs_compta_affectation_gen_anal ap 
					WHERE (COALESCE(ap.coefficient,0)!=0) AND ap.compte = '||compte_||'';
		ELSE
			query_ = 'SELECT ap.centre, SUM((SELECT COALESCE(SUM(_db.';
			IF(retenue_)THEN
				IF(salarial_)THEN
					query_ = query_ ||'retenu_salariale';
				ELSE
					query_ = query_ ||'montant_employeur';
				END IF;
			ELSE
				query_ = query_ ||'montant_payer';
			END IF;
			IF(mode_= 'E')THEN
				query_ = query_ ||'), 1) FROM yvs_grh_detail_bulletin _db INNER JOIN yvs_grh_bulletins _b ON _b.id=_db.bulletin INNER JOIN yvs_grh_contrat_emps _c ON _c.id=_b.contrat 
									WHERE _b.entete = b.entete  AND db.element_salaire = '||element_||' AND _c.employe= e.id) *(COALESCE(ap.coeficient, 1)/100)) AS montant, ap.coeficient 
					FROM yvs_compta_affec_anal_emp ap INNER JOIN yvs_grh_employes e ON e.id=ap.employe INNER JOIN yvs_agences ag ON e.agence=ag.id INNER JOIN yvs_grh_contrat_emps c ON c.employe=e.id INNER JOIN yvs_grh_bulletins b ON c.id=b.contrat 
					INNER JOIN yvs_compta_affectation_gen_anal ca ON ap.centre=ca.centre_analytique WHERE (COALESCE(ap.coeficient,0)!=0) AND ca.compte = '||compte_||' AND b.entete = '||header_||' AND ag.id = '||agence_||'';
			ELSE
				query_ = query_ ||'), 1) FROM yvs_grh_detail_bulletin _db INNER JOIN yvs_grh_bulletins _b ON _b.id=_db.bulletin INNER JOIN yvs_grh_contrat_emps _c ON _c.id=_b.contrat 
									INNER JOIN yvs_grh_employes _e ON _e.id=_c.employe INNER JOIN yvs_grh_poste_employes _pe ON _e.id=_pe.employe INNER JOIN yvs_grh_poste_de_travail _po ON _po.id = _pe.poste 
									WHERE _b.entete = b.entete AND _db.element_salaire = '||element_||' AND _po.departement in (select grh_get_sous_service(d.id, true))) *(COALESCE(ap.coeficient, 1)/100)) AS montant, ap.coeficient 
					FROM yvs_compta_affec_anal_departement ap INNER JOIN yvs_grh_departement d ON d.id=ap.departement INNER JOIN yvs_grh_poste_de_travail po ON po.departement = d.id 
					INNER JOIN yvs_grh_poste_employes pe ON pe.poste = po.id INNER JOIN yvs_grh_employes e ON e.id=pe.employe INNER JOIN yvs_grh_contrat_emps c ON c.employe=e.id INNER JOIN yvs_grh_bulletins b ON c.id=b.contrat 
					INNER JOIN yvs_compta_affectation_gen_anal ca ON ap.centre=ca.centre_analytique INNER JOIN yvs_agences ag ON e.agence=ag.id 
					WHERE (COALESCE(ap.coeficient,0)!=0) AND ca.compte = '||compte_||' AND b.entete = '||header_||' AND ag.id = '||agence_||'';
			END IF;
			IF(tiers_ IS NOT NULL AND tiers_ > 0)THEN
				query_ = query_ ||' AND e.compte_tiers = '||tiers_||'';
			END IF;
		END IF;
		query_ = query_ ||' GROUP BY ap.id';
		FOR ligne_ IN EXECUTE query_
		LOOP
			INSERT INTO table_compta_content_journal VALUES(_error_, ligne_.centre, ligne_.montant, ligne_.coeficient);
		END LOOP;
	END IF;
	RETURN QUERY SELECT * FROM table_compta_content_analytique;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION yvs_compta_content_analytique(bigint, bigint, bigint, bigint, bigint, double precision, character varying, boolean, boolean, boolean)
  OWNER TO postgres;