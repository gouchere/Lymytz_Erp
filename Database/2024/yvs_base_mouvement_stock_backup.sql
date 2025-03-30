-- DROP FUNCTION recrutement.create_schema();
CREATE OR REPLACE FUNCTION public.create_schema_backup()
    RETURNS boolean
    LANGUAGE plpgsql
AS $function$
DECLARE
    COUNT_ INTEGER;
BEGIN
    SELECT INTO COUNT_ COUNT(nspname) FROM pg_catalog.pg_namespace WHERE nspname = 'backup';
    IF(COALESCE(COUNT_, 0) < 1)THEN
        CREATE SCHEMA backup;
        RETURN TRUE;
    END IF;
    RETURN FALSE;
END;
$function$;

SELECT public.create_schema_backup();
DROP FUNCTION public.create_schema_backup();

CREATE TABLE "backup".yvs_base_mouvement_stock (LIKE "public".yvs_base_mouvement_stock INCLUDING CONSTRAINTS);
