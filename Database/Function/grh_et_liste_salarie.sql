-- Function: grh_et_liste_salarie(bigint, character varying, character varying, integer)
-- DROP FUNCTION grh_et_liste_salarie(bigint, character varying, character varying, integer);
CREATE OR REPLACE FUNCTION grh_et_liste_salarie(IN societe_ bigint, IN header_ character varying, IN agence_ character varying, IN colonne_ integer)
  RETURNS TABLE(id bigint, matricule character varying, nom_prenom character varying, element bigint, numero character varying, libelle character varying, montant double precision, rang integer) AS
$BODY$
DECLARE 
	titre_ CHARACTER VARYING DEFAULT 'GEN_LS';
	
	element_ record;
	regles_ record;
	
	valeur_ DOUBLE PRECISION DEFAULT 0;
	rang_ INTEGER DEFAULT 0;
	i_ INTEGER DEFAULT 0;
   
BEGIN 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_liste_salarie;
	CREATE TEMP TABLE IF NOT EXISTS table_liste_salarie(_id bigint, _matricule character varying, _nom_prenom character varying, _element bigint, _numero character varying, _libelle character varying, _montant double precision, _rang integer);
	DELETE FROM table_liste_salarie;
	IF((header_ IS NOT null and header_ != ''))then
		FOR regles_ IN SELECT e.id AS ids, e.element_salaire AS id, e.libelle, COALESCE(y.num_sequence, 0) AS numero, COALESCE(e.ordre, 0) AS ordre, y.retenue, e.groupe_element AS groupe, by_formulaire FROM yvs_stat_grh_element_dipe e 
		INNER JOIN yvs_stat_grh_etat s ON s.id = e.etat INNER JOIN yvs_grh_element_salaire y ON e.element_salaire = y.id WHERE s.code = titre_ and s.societe = societe_ ORDER BY e.ordre
		LOOP
			IF(regles_.by_formulaire)THEN
				FOR element_ IN SELECT DISTINCT(e.id) AS id, e.matricule, CONCAT(e.nom, ' ', e.prenom) AS nom_prenom FROM yvs_grh_bulletins b INNER JOIN yvs_grh_header_bulletin h ON h.bulletin = b.id 
				INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id  INNER JOIN yvs_grh_employes e ON c.employe = e.id 
					WHERE h.agence::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(agence_,',') val)
					AND b.entete::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(header_,',') val)
				LOOP
					valeur_ = (SELECT grh_get_valeur_formulaire_dipe(agence_, regles_.ids, header_, element_.id, 'E'));
					
					valeur_ = COALESCE(valeur_, 0);
					INSERT INTO table_liste_salarie VALUES(element_.id, element_.matricule, element_.nom_prenom, regles_.id, regles_.numero, regles_.libelle, valeur_, regles_.ordre);
					rang_ = regles_.ordre;
				END LOOP;
			ELSE
				FOR element_ IN SELECT DISTINCT(e.id) AS id, e.matricule, CONCAT(e.nom, ' ', e.prenom) AS nom_prenom, COALESCE(sum(d.montant_payer), 0) AS montant_payer, COALESCE(sum(d.retenu_salariale), 0) AS retenu_salariale 
					FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id INNER JOIN yvs_grh_header_bulletin h ON h.bulletin = b.id 
					INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id  INNER JOIN yvs_grh_employes e ON c.employe = e.id 
					WHERE h.agence::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(agence_,',') val) AND d.element_salaire = regles_.id
					AND b.entete::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(header_,',') val) GROUP BY e.id
				LOOP
					IF(regles_.retenue IS FALSE)then
						valeur_ = element_.montant_payer;
					ELSE
						valeur_ = element_.retenu_salariale;
					END IF;
						
					valeur_ = COALESCE(valeur_, 0);
					INSERT INTO table_liste_salarie VALUES(element_.id, element_.matricule, element_.nom_prenom, regles_.id, regles_.numero, regles_.libelle, valeur_, regles_.ordre);
					rang_ = regles_.ordre;
				END LOOP;
			END IF;
		END LOOP;
		FOR element_ IN SELECT DISTINCT _id AS id, _matricule AS matricule, _nom_prenom AS nom_prenom FROM table_liste_salarie
		LOOP
			FOR i_ IN 1..(colonne_) LOOP
				INSERT INTO table_liste_salarie VALUES(element_.id, element_.matricule, element_.nom_prenom, 0, '', '', 0, (rang_ + i_));
			END LOOP;
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_liste_salarie ORDER BY _matricule, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_liste_salarie(bigint, character varying, character varying, integer)
  OWNER TO postgres;
