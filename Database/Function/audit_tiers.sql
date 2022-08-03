-- Function: audit_tiers(bigint)

-- DROP FUNCTION audit_tiers(bigint);

CREATE OR REPLACE FUNCTION audit_tiers(IN societe_ bigint)
  RETURNS SETOF bigint AS
$BODY$
DECLARE 
	tiers_ BIGINT;
	other_ BIGINT;
	table_ CHARACTER VARYING;
	colonne_ CHARACTER VARYING;
	query_ CHARACTER VARYING DEFAULT '';
	correct_ BOOLEAN DEFAULT FALSE;
    
BEGIN
-- 	DROP TABLE IF EXISTS table_audit_tiers;
	CREATE TEMP TABLE IF NOT EXISTS table_audit_tiers(_tiers BIGINT); 
	DELETE FROM table_audit_tiers;
	FOR tiers_ IN SELECT y.id FROM yvs_base_tiers y WHERE y.societe = societe_
	LOOP
		correct_ = FALSE;
		FOR table_ IN SELECT DISTINCT(k.TABLE_NAME) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
			INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			WHERE k.table_schema = 'public' AND f.COLUMN_NAME = 'id' AND f.TABLE_NAME = 'yvs_base_tiers' AND k.TABLE_NAME != f.TABLE_NAME
		LOOP
			SELECT INTO colonne_ DISTINCT(k.COLUMN_NAME) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
			INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			WHERE k.table_schema = 'public' AND f.COLUMN_NAME = 'id' AND f.TABLE_NAME = 'yvs_base_tiers' AND k.TABLE_NAME = table_;

			query_ = 'SELECT '||colonne_||' FROM '||table_||' WHERE '||colonne_||' = '||tiers_;
			EXECUTE query_ INTO other_;
			IF(other_ IS NOT NULL AND other_ > 0)THEN
				correct_ = TRUE;
				EXIT;
			END IF;
		END LOOP;	
		IF(correct_ IS FALSE)THEN
			INSERT INTO table_audit_tiers VALUES(tiers_);
		END IF;
	END LOOP;
	return QUERY SELECT * FROM table_audit_tiers ORDER BY _tiers;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION audit_tiers(bigint)
  OWNER TO postgres;
