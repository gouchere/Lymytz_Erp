-- Function: get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, bigint)

-- DROP FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, bigint);

CREATE OR REPLACE FUNCTION get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE

BEGIN
	return get_remise_vente(article_, qte_, prix_, client_, point_, date_, unite_ ::integer);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, bigint) IS 'retourne la remise sur vente d'' article';
