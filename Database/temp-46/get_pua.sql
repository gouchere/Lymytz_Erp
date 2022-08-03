-- Function: get_pua(bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_pua(bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_pua(article_ bigint, fsseur_ bigint, depot_ bigint, unite_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	agence_ bigint default 0;
BEGIN
	IF(COALESCE(depot_,0)>0) THEN
		SELECT INTO agence_ agence FROM yvs_base_depots WHERE id=depot_;
	END IF;
	return  get_pua(article_ , fsseur_ , depot_ , unite_ , date_ , COALESCE(agence_,0));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pua(bigint, bigint, bigint, bigint, date)
  OWNER TO postgres;
