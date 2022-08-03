-- Function: alter_all_table_add_date_save()

-- DROP FUNCTION alter_all_table_add_date_save();

CREATE OR REPLACE FUNCTION alter_all_table_add_date_save()
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
	deja_ = false;
        for colonne_ in SELECT column_name, data_type FROM information_schema.columns WHERE table_name = table_.tablename
        loop
            if (colonne_.column_name = 'date_save' and (colonne_.data_type = 'date' or colonne_.data_type = 'timestamp' or colonne_.data_type = 'timestamp without time zone')) then
                deja_ = true;
                exit;
            else
		deja_ = false;            
            end if;
        end loop;

        if (deja_ = false) then
		EXECUTE 'ALTER TABLE '|| table_.tablename ||' ADD COLUMN date_save DATE DEFAULT CURRENT_DATE';
        end if;
    end loop;
    return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_all_table_add_date_save()
  OWNER TO postgres;
