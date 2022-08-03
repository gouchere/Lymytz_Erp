-- Function: grh_et_journal_paie_by_convention(bigint, character varying, character varying)
-- DROP FUNCTION grh_et_journal_paie_by_convention(bigint, character varying, character varying);
CREATE OR REPLACE FUNCTION grh_et_journal_paie_by_convention(IN societe_ bigint, IN header_ character varying, IN agence_ character varying)
  RETURNS TABLE(regle bigint, numero integer, libelle character varying, groupe bigint, element bigint, categorie character varying, echellon character varying, montant double precision, rang integer, is_group boolean, is_total boolean) AS
$BODY$
DECLARE 
	
	categorie_ record;
   
BEGIN 	
	-- Creation de la table temporaire du tableau de bord
	DROP TABLE table_journal_paie_by_convention;
	CREATE TEMP TABLE IF NOT EXISTS table_journal_paie_by_convention(_regle BIGINT, _numero INTEGER, _libelle CHARACTER VARYING, _groupe BIGINT, _element BIGINT, _categorie CHARACTER VARYING, _echellon CHARACTER VARYING, _montant DOUBLE PRECISION, _rang INTEGER, _is_group BOOLEAN, _is_total BOOLEAN);
	DELETE FROM table_journal_paie_by_convention;
	IF(header_ IS NOT null and header_ != '')then
		FOR categorie_ IN SELECT id FROM yvs_grh_categorie_professionelle WHERE societe = societe_
		LOOP
			INSERT INTO table_journal_paie_by_convention SELECT * FROM grh_et_journal_paie_by_convention(societe_, header_, agence_, categorie_.id::bigint);
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_journal_paie_by_convention ORDER BY _is_total, _groupe, _rang, _element;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_journal_paie_by_convention(bigint, character varying, character varying)
  OWNER TO postgres;
