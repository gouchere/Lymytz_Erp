-- Function: alter_all_table_update_date_update()

-- DROP FUNCTION alter_all_table_update_date_update();

CREATE OR REPLACE FUNCTION alter_all_table_update_date_update()
  RETURNS boolean AS
$BODY$
DECLARE
    table_ RECORD;
    colonne_ RECORD;
    deja_ BOOLEAN default false;   
    trouve_ BOOLEAN default false;

BEGIN
    for table_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = 'public' order by tablename
    loop
	RAISE NOTICE 'Table %',table_.tablename;
	EXECUTE 'ALTER TABLE '|| table_.tablename ||' ALTER COLUMN date_update TYPE TIMESTAMP';
    end loop;
    return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_all_table_update_date_update()
  OWNER TO postgres;
