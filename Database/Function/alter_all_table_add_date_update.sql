-- Function: alter_all_table_add_date_update()

-- DROP FUNCTION alter_all_table_add_date_update();

CREATE OR REPLACE FUNCTION alter_all_table_add_date_update()
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
        for colonne_ in SELECT column_name, data_type FROM information_schema.columns WHERE table_name = table_.tablename
        loop
            if (colonne_.column_name = 'date_update' and (colonne_.data_type = 'date')) then
                deja_ = true;
                exit;
            else
		deja_ = false;            
            end if;
        end loop;

        if (deja_ = false) then
		EXECUTE 'ALTER TABLE '|| table_.tablename ||' ADD COLUMN date_update DATE DEFAULT CURRENT_DATE';
        end if;
    end loop;
    return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_all_table_add_date_update()
  OWNER TO postgres;
