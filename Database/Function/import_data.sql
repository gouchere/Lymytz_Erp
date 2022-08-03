-- Function: import_data()

-- DROP FUNCTION import_data();

CREATE OR REPLACE FUNCTION import_data()
  RETURNS boolean AS
$BODY$
DECLARE
    data_ RECORD;
    other_ RECORD;
    id_ bigint default 0;

BEGIN
	FOR data_  IN SELECT * FROM dblink('dbname=old_data_lymytz user=postgres password=yves1910/','select * FROM yvs_grh_element_additionel ')
		AS t(description character varying,
			montant_element double precision,
			contrat bigint,
			type_element integer,
			id bigINT,
			planifier boolean ,
			date_element date ,
			date_debut date ,
			date_fin date ,
			permanent boolean ,
			author bigint,
			statut character(1),
			plan_prelevement bigint,
			pice_reglement bigint)
	loop
		INSERT INTO yvs_grh_element_additionel(description, montant_element, contrat, type_element, planifier, 
		    date_element, date_debut, date_fin, permanent, author, statut,plan_prelevement, pice_reglement)
		VALUES (data_.description, data_.montant_element, data_.contrat, data_.type_element, data_.planifier, 
		    data_.date_element, data_.date_debut, data_.date_fin, data_.permanent, data_.author, data_.statut, data_.plan_prelevement, data_.pice_reglement);
		    
		SELECT INTO id_ id FROM yvs_grh_element_additionel ORDER BY id DESC LIMIT 1;
		FOR other_  IN SELECT * FROM dblink('dbname=old_data_lymytz user=postgres password=yves1910/','select * FROM yvs_grh_detail_prelevement_emps WHERE retenue = '||data_.id)
			AS t(id bigint,
				date_prelevement date,
				statut_reglement character(1),
				reference_retenu character varying,
				retenue bigint,
				plan_prelevement bigint,
				valeur double precision,
				author bigint,
				date_preleve timestamp without time zone, -- Précise la date de prélèvement effectif d'une retenue
				retenu_fixe boolean)
		loop
			INSERT INTO yvs_grh_detail_prelevement_emps(date_prelevement, statut_reglement, reference_retenu, retenue, 
			    plan_prelevement, valeur, author, date_preleve, retenu_fixe)
			VALUES (other_.date_prelevement, other_.statut_reglement, other_.reference_retenu, id_, 
			    other_.plan_prelevement, other_.valeur, other_.author, other_.date_preleve, other_.retenu_fixe);
		END LOOP;
	END LOOP;
	return true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION import_data()
  OWNER TO postgres;
