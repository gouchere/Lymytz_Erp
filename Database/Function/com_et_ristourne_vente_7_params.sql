-- Function: com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean)
DROP FUNCTION com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION com_et_ristourne_vente(IN societe_ bigint, IN users_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN cumul_ boolean)
  RETURNS TABLE(client bigint, code character varying, nom character varying, users bigint, code_users character varying, nom_users character varying, unite bigint, entete character varying, quantite double precision, prix_total double precision, ristourne double precision, rang integer, id_facture bigint, numero_facture character varying) AS
$BODY$
declare 
   
begin 	
	RETURN QUERY SELECT * FROM com_et_ristourne_vente(societe_, users_, client_, date_debut_, date_fin_, period_, cumul_, false);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean)
  OWNER TO postgres;
