-- Function: get_remise(bigint, double precision, double precision, bigint, date)

-- DROP FUNCTION get_remise(bigint, double precision, double precision, bigint, date);

CREATE OR REPLACE FUNCTION get_remise(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE

BEGIN
	return (select get_remise_vente(article_, qte_, prix_, client_, 0, date_));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise(bigint, double precision, double precision, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise(bigint, double precision, double precision, bigint, date) IS 'retourne la remise d'' article';
