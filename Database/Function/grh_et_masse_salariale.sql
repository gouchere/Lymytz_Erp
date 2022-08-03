-- Function: grh_et_masse_salariale(bigint, character varying, character varying)
-- DROP FUNCTION grh_et_masse_salariale(bigint, character varying, character varying);
CREATE OR REPLACE FUNCTION grh_et_masse_salariale(IN societe_ bigint, IN agence_ character varying, IN periode_ character varying)
  RETURNS TABLE(element bigint, code character varying, periode bigint, entete character varying, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	element_ RECORD;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	somme_ DOUBLE PRECISION DEFAULT 0;
	i INTEGER DEFAULT 0;
	ordre_ RECORD;
BEGIN
-- 	DROP TABLE IF EXISTS table_masse_salariale;
	CREATE TEMP TABLE IF NOT EXISTS table_masse_salariale(_element bigint, _code character varying, _periode bigint, _entete character varying, _valeur double precision, _rang integer); 
	DELETE FROM table_masse_salariale;
	FOR element_ IN SELECT y.id, y.nom, y.retenue FROM yvs_grh_element_salaire y INNER JOIN yvs_grh_categorie_element c ON y.categorie = c.id WHERE y.visible_bulletin IS TRUE AND c.societe = societe_ ORDER BY y.nom
	LOOP
		i = 0;
		somme_ = 0;
		FOR ordre_ IN SELECT o.id, o.abbreviation AS code FROM yvs_grh_ordre_calcul_salaire o WHERE o.societe = societe_ AND o.id::character varying in (SELECT val from regexp_split_to_table(periode_,',') val) ORDER BY o.debut_mois 
		LOOP
			i = i + 1;
			valeur_ = 0;
			if(element_.retenue is true)then
				IF(agence_ IS NOT NULL AND agence_ != '')THEN
					SELECT INTO valeur_ SUM(coalesce(d.retenu_salariale, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id and y.agence::character varying in (SELECT val from regexp_split_to_table(agence_,',') val) and (b.statut IN ('V', 'P'));
				ELSE
					SELECT INTO valeur_ SUM(coalesce(d.retenu_salariale, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id and (b.statut IN ('V', 'P'));
				END IF;

				somme_ = somme_ + coalesce(valeur_, 0);
				valeur_ = -coalesce(valeur_, 0);
			elsif(element_.retenue is false)then
				IF(agence_ IS NOT NULL AND agence_ != '')THEN
					SELECT INTO valeur_ SUM(coalesce(d.montant_payer, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id and y.agence::character varying in (SELECT val from regexp_split_to_table(agence_,',') val) and (b.statut IN ('V', 'P'));
				ELSE
					SELECT INTO valeur_ SUM(coalesce(d.montant_payer, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id and (b.statut IN ('V', 'P'));
				END IF;
				
				somme_ = somme_ + coalesce(valeur_, 0);
			end if;
			insert INTO table_masse_salariale values(element_.id, element_.nom, ordre_.id, ordre_.code, coalesce(valeur_, 0), i);
		END LOOP;
		IF(coalesce(somme_, 0) = 0)THEN
			delete from table_masse_salariale where _element = element_.id;
		END IF;
	end loop;
	FOR ordre_ IN SELECT o._periode AS periode, SUM(o._valeur) AS somme FROM table_masse_salariale o GROUP BY o._periode
	LOOP
		IF(ordre_.somme = 0)THEN
			delete from table_masse_salariale where _periode = ordre_.periode;
		END IF;
	END LOOP;
	return QUERY SELECT * FROM table_masse_salariale ORDER BY _code, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_masse_salariale(bigint, character varying, character varying)
  OWNER TO postgres;
