-- Function: compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying)

-- DROP FUNCTION compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying);

CREATE OR REPLACE FUNCTION return_field(IN table_ CHARACTER VARYING, IN column_ CHARACTER VARYING, IN table_liee_ CHARACTER VARYING)
  RETURNS TABLE(field CHARACTER VARYING) AS
$BODY$
DECLARE 
	field_ CHARACTER VARYING default '';
	
begin 	
	CREATE TEMP TABLE IF NOT EXISTS table_field(_field CHARACTER VARYING); 
	DELETE FROM table_field;
	IF(table_ IS NULL OR table_ = '')THEN
		INSERT INTO table_field SELECT tablename FROM pg_tables WHERE tablename NOT LIKE 'pg_%' AND schemaname = 'public' ORDER BY tablename;
	ELSE
		IF(column_ IS NULL OR column_ = '')THEN
			INSERT INTO table_field SELECT column_name FROM information_schema.columns WHERE table_name = table_;
		ELSE
			IF(table_liee_ IS NULL OR table_liee_ = '')THEN
				INSERT INTO table_field SELECT DISTINCT(f.TABLE_NAME) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
				INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				WHERE k.table_schema = 'public' AND k.COLUMN_NAME = column_ AND k.TABLE_NAME = table_;
			ELSE
				INSERT INTO table_field SELECT DISTINCT(f.COLUMN_NAME) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
				INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				WHERE k.table_schema = 'public' AND k.COLUMN_NAME = column_ AND f.TABLE_NAME = table_liee_ AND k.TABLE_NAME = table_;
			END IF;
		END IF;
	END IF;
	RETURN QUERY SELECT * FROM table_field ORDER BY _field;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION return_field(CHARACTER VARYING, CHARACTER VARYING, CHARACTER VARYING)
  OWNER TO postgres;
