-- Function: alter_all_table_add_author()

-- DROP FUNCTION alter_all_table_add_author();

CREATE OR REPLACE FUNCTION alter_all_table_add_author()
  RETURNS boolean AS
$BODY$
DECLARE
    table_ RECORD;
    colonne_ RECORD;
    deja_ BOOLEAN default false;   	
    trouve_ BOOLEAN default false;

BEGIN
    for table_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = 'public'
    loop
		RAISE NOTICE 'Table %',table_.tablename;
        for colonne_ in SELECT column_name, data_type FROM information_schema.columns WHERE table_name = table_.tablename
        loop
            if (colonne_.column_name = 'execute_trigger' and (colonne_.data_type = 'character varying' or colonne_.data_type = 'bigserial' or colonne_.data_type = 'bigint' or colonne_.data_type = 'serial' or colonne_.data_type = 'integer')) then
				EXECUTE 'ALTER TABLE '|| table_.tablename ||' DROP COLUMN execute_trigger';
				RAISE NOTICE 'DEJA';
                exit;
            end if;
        end loop;

		EXECUTE 'alter table '|| table_.tablename ||' add column execute_trigger character varying';			
    end loop;
    return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_all_table_add_author()
  OWNER TO postgres;
