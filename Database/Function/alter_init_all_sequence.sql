-- Function: alter_init_all_sequence()

-- DROP FUNCTION alter_init_all_sequence();

CREATE OR REPLACE FUNCTION alter_init_all_sequence()
  RETURNS boolean AS
$BODY$
DECLARE
    seq_ RECORD;
    detail_ RECORD;

BEGIN
    for seq_ in select c.relname from pg_class c where c.relkind = 'S'
    loop
        execute 'alter sequence '|| seq_.relname ||' restart with 1 ';
    end loop;
    return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_init_all_sequence()
  OWNER TO postgres;
COMMENT ON FUNCTION alter_init_all_sequence() IS 'initialise toutes les sequences';
