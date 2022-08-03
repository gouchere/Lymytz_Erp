-- Function: com_et_objectif(bigint, bigint, character varying, character varying)

-- DROP FUNCTION com_et_objectif(bigint, bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN objectif_ bigint, IN periodes_ character varying, IN type_ character varying)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	periode_ BIGINT;
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif;	
	IF(periodes_ IS NULL OR periodes_ IN ('', ' '))THEN
		FOR periode_ IN SELECT p.id FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
		LOOP
			INSERT INTO table_objectif SELECT * FROM com_et_objectif_by_periode(periode_, objectif_, type_);
		END LOOP;
	ELSE
		FOR periode_ IN SELECT p.id FROM yvs_com_periode_objectif p WHERE p.societe = societe_ AND p.id::character varying in (SELECT val from regexp_split_to_table(periodes_,',') val) ORDER BY p.date_debut
		LOOP
			INSERT INTO table_objectif SELECT * FROM com_et_objectif_by_periode(periode_, objectif_, type_);
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_objectif ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, bigint, character varying, character varying)
  OWNER TO postgres;
