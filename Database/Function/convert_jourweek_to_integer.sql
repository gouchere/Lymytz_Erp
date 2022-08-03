-- Function: convert_jourweek_to_integer(character varying)

-- DROP FUNCTION convert_jourweek_to_integer(character varying);

CREATE OR REPLACE FUNCTION convert_jourweek_to_integer(jour_ character varying)
  RETURNS integer AS
$BODY$
DECLARE 	
    
BEGIN
	CASE jour_
		WHEN 'Dimanche' THEN
		  RETURN 0;
		WHEN 'Lundi' THEN
		  RETURN 1;
		WHEN 'Mardi' THEN
		  RETURN 2;
		WHEN 'Mercredi' THEN
		  RETURN 3;
		WHEN 'Jeudi' THEN
		  RETURN 4;
		WHEN 'Vendredi' THEN
		  RETURN 5;
		WHEN 'Samedi' THEN
		  RETURN 6;
		ELSE
		  RETURN -1;
	END CASE;
	
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION convert_jourweek_to_integer(character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION convert_jourweek_to_integer(character varying) IS 'cette fonction permet de retourner le numéro correspondant à un jour de la semaine';
