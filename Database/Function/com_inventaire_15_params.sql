-- Function: com_inventaire(bigint, bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint, boolean)
DROP FUNCTION com_inventaire(bigint, bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint, boolean);
CREATE OR REPLACE FUNCTION com_inventaire(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN emplacement_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying, IN article_ character varying, IN offset_ bigint, IN limit_ bigint, IN preparatoire_ boolean)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, emplacement bigint, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision, "position" double precision, count double precision, lot bigint) AS
$BODY$
DECLARE 
	
BEGIN 	
	RETURN QUERY SELECT * FROM com_inventaire(societe_, agence_, depot_, emplacement_, famille_, categorie_, groupe_, date_, print_all_, option_print_, type_, article_, offset_, limit_, preparatoire_, false);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire(bigint, bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint, boolean)
  OWNER TO postgres;
