-- FUNCTION: public.grh_et_livre_paie(bigint, character varying, character varying, character varying, character varying)

DROP FUNCTION IF EXISTS public.grh_et_livre_paie(bigint, character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION public.grh_et_livre_paie(
	societe_ bigint,
	header_ character varying,
	agence_ character varying,
	code_ character varying,
	type_ character varying,
	brouillon boolean)
RETURNS TABLE(regle bigint, numero integer, libelle character varying, groupe bigint, element bigint, montant double precision, rang integer, is_group boolean, is_total boolean) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$
DECLARE 
	titre_ CHARACTER VARYING DEFAULT 'LIVRE_PAIE';
	
	element_ record;
	regles_ record;
	second_ record;
	somme_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	is_total_ BOOLEAN DEFAULT FALSE;
	query_ CHARACTER VARYING;
   
BEGIN 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE table_livre_paie;
	CREATE TEMP TABLE IF NOT EXISTS table_livre_paie(_regle BIGINT, _numero INTEGER, _libelle CHARACTER VARYING, _groupe BIGINT, _element BIGINT, _montant DOUBLE PRECISION, _rang INTEGER, _is_group BOOLEAN, _is_total BOOLEAN);
	DELETE FROM table_livre_paie;
	IF((header_ IS NOT null and header_ != '') and (type_ IS NOT null))then
		FOR regles_ IN SELECT e.id AS ids, e.element_salaire AS id, e.libelle, COALESCE(y.num_sequence, 0) AS numero, COALESCE(e.ordre, 0) AS ordre,
		y.retenue, e.groupe_element AS groupe, by_formulaire 
		FROM yvs_stat_grh_element_dipe e INNER JOIN yvs_stat_grh_etat s ON s.id = e.etat 
		INNER JOIN yvs_grh_element_salaire y ON e.element_salaire = y.id 
		WHERE s.code = titre_ and s.societe = societe_ ORDER BY e.ordre
		LOOP
			somme_ = 0;
			IF(regles_.numero = 0)then
				is_total_ = TRUE;
			ELSE
				is_total_ = FALSE;
			END IF;
			--type désigne le type moèle de groupage qu'on veut: Livre de paie par employés, Service, ou Catégorie professionnelle
			IF(regles_.by_formulaire)THEN
				IF(type_ = 'E')then
					FOR element_ IN SELECT DISTINCT(e.id) AS id FROM yvs_grh_bulletins b INNER JOIN yvs_grh_header_bulletin h ON h.bulletin = b.id 
					INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id  INNER JOIN yvs_grh_employes e ON c.employe = e.id 
						WHERE h.agence::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(agence_,',') val)
						AND b.entete::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(header_,',') val)
					LOOP
						valeur_ = (SELECT grh_get_valeur_formulaire_dipe(agence_, regles_.ids, header_, element_.id, type_, brouillon));
						
						valeur_ = COALESCE(valeur_, 0);
						INSERT INTO table_livre_paie VALUES(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, valeur_, regles_.ordre, FALSE, is_total_);
						somme_ = somme_ + valeur_;
					END LOOP;
				ELSE
					FOR element_ IN SELECT d.id AS id, d.print_with_children FROM yvs_grh_departement d WHERE d.societe = societe_ AND d.visible_on_livre_paie IS TRUE 
					LOOP
						valeur_ = (SELECT grh_get_valeur_formulaire_dipe(agence_, regles_.ids, header_, element_.id, type_, brouillon));
						
						valeur_ = COALESCE(valeur_, 0);
						INSERT INTO table_livre_paie VALUES(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, valeur_, regles_.ordre, FALSE, is_total_);
						somme_ = somme_ + valeur_;
					END LOOP;
				END IF;
			ELSE
				IF(type_ = 'E')then
					FOR element_ IN SELECT DISTINCT(e.id) AS id, COALESCE(sum(d.montant_payer), 0) AS montant_payer, COALESCE(sum(d.retenu_salariale), 0) AS retenu_salariale 
						FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id INNER JOIN yvs_grh_header_bulletin h ON h.bulletin = b.id 
						INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id  INNER JOIN yvs_grh_employes e ON c.employe = e.id 
						WHERE h.agence::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(agence_,',') val) AND d.element_salaire = regles_.id
						AND b.entete::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(header_,',') val) 
                        AND ((brouillon IS FALSE AND b.statut IN ('V', 'P')) OR (brouillon IS TRUE AND b.statut IS NOT NULL)) GROUP BY e.id
					LOOP
						IF(regles_.retenue IS FALSE)then
							valeur_ = element_.montant_payer;
						ELSE
							valeur_ = element_.retenu_salariale;
						END IF;
							
						valeur_ = COALESCE(valeur_, 0);
						INSERT INTO table_livre_paie VALUES(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, valeur_, regles_.ordre, FALSE, is_total_);
						somme_ = somme_ + valeur_;
					END LOOP;
				elsif(type_ = 'S')then
					FOR element_ IN SELECT d.id AS id, d.print_with_children FROM yvs_grh_departement d WHERE d.societe = societe_ AND d.visible_on_livre_paie IS TRUE 
					LOOP
						IF(regles_.retenue IS FALSE)then
							SELECT INTO valeur_ COALESCE(sum(d.montant_payer), 0) FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id 
								INNER JOIN yvs_grh_header_bulletin h ON h.bulletin = b.id INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id INNER JOIN yvs_grh_employes e ON c.employe = e.id 
								WHERE d.element_salaire = regles_.id and b.entete::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(header_,',') val) 
									and h.agence::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(agence_,',') val) and ((brouillon IS FALSE AND b.statut IN ('V', 'P')) OR (brouillon IS TRUE AND b.statut IS NOT NULL))
									and (h.id_service = element_.id OR (element_.print_with_children IS TRUE and h.id_service IN (SELECT id FROM grh_get_sous_service(element_.id, TRUE))));
						ELSE
							SELECT INTO valeur_ COALESCE(sum(d.retenu_salariale), 0) FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id 
								INNER JOIN yvs_grh_header_bulletin h ON h.bulletin = b.id INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id INNER JOIN yvs_grh_employes e ON c.employe = e.id 
								WHERE d.element_salaire = regles_.id and b.entete::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(header_,',') val) 
									and h.agence::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(agence_,',') val) and ((brouillon IS FALSE AND b.statut IN ('V', 'P')) OR (brouillon IS TRUE AND b.statut IS NOT NULL)) 
									and (h.id_service = element_.id OR (element_.print_with_children IS TRUE and h.id_service IN (SELECT id FROM grh_get_sous_service(element_.id, TRUE))));
						END IF;
							
						valeur_ = COALESCE(valeur_, 0);
						IF(valeur_ != 0)then
							INSERT INTO table_livre_paie VALUES(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, valeur_, regles_.ordre, FALSE, is_total_);
						END IF;
						somme_ = somme_ + valeur_;
					END LOOP;
				END IF;
			END IF;
			IF(somme_ = 0)then
				DELETE FROM table_livre_paie WHERE _regle = regles_.id;
			END IF;
		END LOOP;
		
		-- Récuperation les totaux par groupe
		FOR regles_ IN SELECT distinct _groupe AS groupe, g.libelle FROM table_livre_paie y left JOIN yvs_stat_grh_groupe_element g ON y._groupe = g.id WHERE y._groupe > 0
		LOOP
			FOR second_ IN SELECT _element, sum(_montant) AS valeur_, count(_rang) ordre FROM table_livre_paie y WHERE y._groupe = regles_.groupe GROUP BY _element
			LOOP
				INSERT INTO table_livre_paie VALUES(0, 0, regles_.libelle, regles_.groupe, second_._element, second_.valeur_, (second_.ordre + 1), TRUE, FALSE);
			END LOOP;
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_livre_paie ORDER BY _is_total, _groupe, _rang, _element;
END;
$BODY$;

ALTER FUNCTION public.grh_et_livre_paie(bigint, character varying, character varying, character varying, character varying, boolean)
    OWNER TO postgres;
