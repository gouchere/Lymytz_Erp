-- Function: com_get_versement_attendu_vendeur(bigint, date, date)
-- DROP FUNCTION com_get_versement_attendu_vendeur(bigint, date, date);
CREATE OR REPLACE FUNCTION com_get_versement_attendu_vendeur(users_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
BEGIN   
	RETURN (SELECT com_get_versement_attendu(users_, date_debut_, date_fin_));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu_vendeur(bigint, date, date)
  OWNER TO postgres;
