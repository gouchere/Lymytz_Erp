-- Function: do_audit_stock()

-- DROP FUNCTION do_audit_stock();

CREATE OR REPLACE FUNCTION do_audit_stock()
  RETURNS SETOF bigint AS
$BODY$   
DECLARE 
	data_ record;
	source_ integer default 0;
BEGIN
	CREATE TEMP TABLE IF NOT EXISTS table_audit_stock(mouvement_ bigint);
	DELETE FROM table_audit_stock;
	FOR data_ IN SELECT id , id_externe, table_externe FROM yvs_base_mouvement_stock 
	LOOP
		EXECUTE 'SELECT id FROM '||data_.table_externe||' WHERE id = '||data_.id_externe INTO source_;
		IF(source_ IS NULL OR source_ < 1)THEN
			INSERT INTO table_audit_stock values(data_.id);
		END IF;
	END LOOP;
	return QUERY select * from table_audit_stock order by mouvement_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION do_audit_stock()
  OWNER TO postgres;
