-- FUNCTION: public.grh_et_journal_paie_by_convention(bigint, character varying, character varying, bigint)

DROP FUNCTION IF EXISTS public.grh_et_journal_paie_by_convention(bigint, character varying, character varying, bigint);

CREATE OR REPLACE FUNCTION public.grh_et_journal_paie_by_convention(
	societe_ bigint,
	header_ character varying,
	agence_ character varying,
	categorie_ bigint,
	brouillon boolean)
RETURNS TABLE(regle bigint, numero integer, libelle character varying, groupe bigint, element bigint, categorie character varying, echellon character varying, montant double precision, rang integer, is_group boolean, is_total boolean) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$
DECLARE 
	titre_ CHARACTER VARYING DEFAULT 'JRN_PAIE_CONV';
	type_ CHARACTER VARYING DEFAULT 'CC';
	
	element_ record;
	regles_ record;
	second_ record;
	somme_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	is_total_ BOOLEAN DEFAULT FALSE;
	query_ CHARACTER VARYING;
   
BEGIN 	
	-- Creation de la table temporaire du tableau de bord
-- 	DROP TABLE sub_table_journal_paie_by_convention;
	CREATE TEMP TABLE IF NOT EXISTS sub_table_journal_paie_by_convention(_regle BIGINT, _numero INTEGER, _libelle CHARACTER VARYING, _groupe BIGINT, _element BIGINT, _categorie CHARACTER VARYING, _echellon CHARACTER VARYING, _montant DOUBLE PRECISION, _rang INTEGER, _is_group BOOLEAN, _is_total BOOLEAN);
	DELETE FROM sub_table_journal_paie_by_convention;
	IF((header_ IS NOT null and header_ != '') and (type_ IS NOT null))then
		FOR regles_ IN SELECT e.id AS ids, e.element_salaire AS id, e.libelle, COALESCE(y.num_sequence, 0) AS numero, COALESCE(e.ordre, 0) AS ordre, y.retenue, e.groupe_element AS groupe, by_formulaire FROM yvs_stat_grh_element_dipe e 
		INNER JOIN yvs_stat_grh_etat s ON s.id = e.etat INNER JOIN yvs_grh_element_salaire y ON e.element_salaire = y.id WHERE s.code = titre_ and s.societe = societe_ ORDER BY e.ordre
		LOOP
			somme_ = 0;
			IF(regles_.numero = 0)then
				is_total_ = TRUE;
			ELSE
				is_total_ = FALSE;
			END IF;
			FOR element_ IN SELECT cc.id, ca.categorie, ec.echelon FROM yvs_grh_convention_collective cc 
				INNER JOIN yvs_grh_echelons ec On cc.echellon = ec.id INNER JOIN yvs_grh_categorie_professionelle ca On cc.categorie = ca.id 
				WHERE cc.categorie = categorie_ 
			LOOP
				IF(regles_.by_formulaire)THEN
					valeur_ = (SELECT grh_get_valeur_formulaire_dipe(agence_, regles_.ids, header_, element_.id, type_, brouillon));					
				ELSE
					SELECT INTO second_ COALESCE(sum(d.montant_payer), 0) AS montant_payer, COALESCE(sum(d.retenu_salariale), 0) AS retenu_salariale 
							FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id INNER JOIN yvs_grh_header_bulletin h ON h.bulletin = b.id 
							INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id  INNER JOIN yvs_grh_employes e ON c.employe = e.id 
							INNER JOIN yvs_grh_convention_employe ce ON ce.employe = e.id AND ce.actif IS TRUE
							WHERE h.agence::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(agence_,',') val) AND d.element_salaire = regles_.id
							AND b.entete::CHARACTER VARYING IN (SELECT val FROM regexp_split_to_table(header_,',') val) AND ce.convention = element_.id
							AND ((brouillon IS FALSE AND b.statut IN ('V', 'P')) OR (brouillon IS TRUE AND b.statut IS NOT NULL));
					IF(regles_.retenue IS FALSE)then
						valeur_ = second_.montant_payer;
					ELSE
						valeur_ = second_.retenu_salariale;
					END IF;
				END IF;
				valeur_ = COALESCE(valeur_, 0);
				INSERT INTO sub_table_journal_paie_by_convention VALUES(regles_.id, regles_.numero, regles_.libelle, regles_.groupe, element_.id, element_.categorie, element_.echelon, valeur_, regles_.ordre, FALSE, is_total_);
				somme_ = somme_ + valeur_;
			END LOOP;
			IF(somme_ = 0)then
				DELETE FROM sub_table_journal_paie_by_convention WHERE _regle = regles_.id;
			END IF;
		END LOOP;
		
		-- Récuperation les totaux par groupe
		FOR regles_ IN SELECT distinct _groupe AS groupe, g.libelle FROM sub_table_journal_paie_by_convention y left JOIN yvs_stat_grh_groupe_element g ON y._groupe = g.id WHERE y._groupe > 0
		LOOP
			FOR second_ IN SELECT _element, _categorie, _echellon, sum(_montant) AS valeur_, count(_rang) ordre FROM sub_table_journal_paie_by_convention y WHERE y._groupe = regles_.groupe GROUP BY _element, _categorie, _echellon
			LOOP
				INSERT INTO sub_table_journal_paie_by_convention VALUES(0, 0, regles_.libelle, regles_.groupe, second_._element, second_._categorie, second_._echellon, second_.valeur_, (second_.ordre + 1), TRUE, FALSE);
			END LOOP;
		END LOOP;
	END IF;
	return QUERY SELECT * FROM sub_table_journal_paie_by_convention ORDER BY _is_total, _groupe, _rang, _element;
END;
$BODY$;

ALTER FUNCTION public.grh_et_journal_paie_by_convention(bigint, character varying, character varying, bigint, boolean)
    OWNER TO postgres;
