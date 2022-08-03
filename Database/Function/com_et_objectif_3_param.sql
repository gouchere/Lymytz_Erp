-- Function: com_et_objectif(bigint, character varying, character varying)

-- DROP FUNCTION com_et_objectif(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN periodes_ character varying, IN type_ character varying)
  RETURNS TABLE(indicateur character varying, objectif character varying, element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	objectif_ RECORD;

BEGIN
-- 	DROP TABLE IF EXISTS table_objectifs;
	CREATE TEMP TABLE IF NOT EXISTS table_objectifs(_indicateur character varying, _objectif character varying, _element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectifs;
	FOR objectif_ IN SELECT y.id, y.indicateur, y.description FROM yvs_com_modele_objectif y WHERE y.societe = societe_
	LOOP
		INSERT INTO table_objectifs SELECT objectif_.indicateur, objectif_.description, * FROM com_et_objectif(societe_, objectif_.id, periodes_, type_);
	END LOOP;
	return QUERY SELECT * FROM table_objectifs ORDER BY _indicateur, _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, character varying, character varying)
  OWNER TO postgres;
