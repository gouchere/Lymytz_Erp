-- Function: public.dblink_exec(text, text)
-- DROP FUNCTION public.dblink_exec(text, text);
CREATE OR REPLACE FUNCTION public.dblink_exec(text, text)
  RETURNS text AS
'$libdir/dblink', 'dblink_exec'
  LANGUAGE c VOLATILE STRICT
  COST 1;
ALTER FUNCTION public.dblink_exec(text, text)
  OWNER TO postgres;
  
-- Function: public.dblink_exec(text)
-- DROP FUNCTION public.dblink_exec(text);
CREATE OR REPLACE FUNCTION public.dblink_exec(text)
  RETURNS text AS
'$libdir/dblink', 'dblink_exec'
  LANGUAGE c VOLATILE STRICT
  COST 1;
ALTER FUNCTION public.dblink_exec(text)
  OWNER TO postgres;

  -- Function: public.dblink_exec(text, boolean)
-- DROP FUNCTION public.dblink_exec(text, boolean);
CREATE OR REPLACE FUNCTION public.dblink_exec(text, boolean)
  RETURNS text AS
'$libdir/dblink', 'dblink_exec'
  LANGUAGE c VOLATILE STRICT
  COST 1;
ALTER FUNCTION public.dblink_exec(text, boolean)
  OWNER TO postgres;

-- Function: public.dblink_exec(text, text, boolean)
-- DROP FUNCTION public.dblink_exec(text, text, boolean);
CREATE OR REPLACE FUNCTION public.dblink_exec(text, text, boolean)
  RETURNS text AS
'$libdir/dblink', 'dblink_exec'
  LANGUAGE c VOLATILE STRICT
  COST 1;
ALTER FUNCTION public.dblink_exec(text, text, boolean)
  OWNER TO postgres;
