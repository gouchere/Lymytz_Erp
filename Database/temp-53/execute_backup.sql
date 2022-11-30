-- FUNCTION: public.execute_backup(character varying, character varying)

-- DROP FUNCTION IF EXISTS public.execute_backup(character varying, character varying);

CREATE OR REPLACE FUNCTION public.execute_backup(
	directory_ character varying,
	query_ character varying)
RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$
DECLARE
	requete CHARACTER VARYING DEFAULT '';
	tableName CHARACTER VARYING DEFAULT '';
	fileName CHARACTER VARYING DEFAULT '';
    
    start_index INTEGER DEFAULT 0;
    end_index INTEGER DEFAULT 0;
    
BEGIN
	FOR requete IN SELECT "value" FROM regexp_split_to_table(query_, ';') "value"
    LOOP
        RAISE NOTICE '%',requete;
        start_index := (SELECT POSITION(' yvs' IN requete));
        RAISE NOTICE 'start_index : %', start_index;
        end_index := (SELECT POSITION(' y ' IN requete)) - start_index;
        RAISE NOTICE 'end_index : %', end_index;
        tableName := (SELECT TRIM(SUBSTRING(requete, start_index, end_index)));
        RAISE NOTICE 'tableName : %', tableName;
        fileName = directory_||tableName ||'.csv';        
        RAISE NOTICE 'fileName : %', fileName;
        
        EXECUTE 'COPY ('||TRIM(requete)||') TO '||QUOTE_LITERAL(fileName)||' CSV HEADER  DELIMITER AS '';''';
    END LOOP;
	RETURN TRUE;
END;
$BODY$;

ALTER FUNCTION public.execute_backup(character varying, character varying)
    OWNER TO postgres;
