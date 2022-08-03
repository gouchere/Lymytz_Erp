-- Function: convert_integer_to_jourweek(integer)

-- DROP FUNCTION convert_integer_to_jourweek(integer);

CREATE OR REPLACE FUNCTION convert_integer_to_jourweek(jour_ integer)
  RETURNS character varying AS
$BODY$
DECLARE 	
    
BEGIN
	CASE jour_
		WHEN 0 THEN
		  RETURN 'Dimanche';
		WHEN 1 THEN
		  RETURN 'Lundi';
		WHEN 2 THEN
		  RETURN 'Mardi';
		WHEN 3 THEN
		  RETURN 'Mercredi';
		WHEN 4 THEN
		  RETURN 'Jeudi';
		WHEN 5 THEN
		  RETURN 'Vendredi';
		WHEN 6 THEN
		  RETURN 'Samedi';
		ELSE
		  RETURN '';
	END CASE;
	
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION convert_integer_to_jourweek(integer)
  OWNER TO postgres;
COMMENT ON FUNCTION convert_integer_to_jourweek(integer) IS 'cette fonction permet de retourner le jour de la semaine en fonction de son numéro correspondant';
