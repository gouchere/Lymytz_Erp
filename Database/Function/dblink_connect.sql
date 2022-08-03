-- Function: dblink_connect(text)

-- DROP FUNCTION dblink_connect(text);

CREATE OR REPLACE FUNCTION dblink_connect(text)
  RETURNS text AS
'$libdir/dblink', 'dblink_connect'
  LANGUAGE c VOLATILE STRICT
  COST 1;
ALTER FUNCTION dblink_connect(text)
  OWNER TO postgres;
