-- Function: com_et_journal_vente(bigint, bigint, date, date, boolean)
-- DROP FUNCTION com_et_journal_vente(bigint, bigint, date, date, boolean);
CREATE OR REPLACE FUNCTION com_et_journal_vente(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN by_famille_ boolean)
  RETURNS TABLE(agence bigint, users bigint, code character varying, nom character varying, classe bigint, reference character varying, designation character varying, montant double precision, is_classe boolean, is_vendeur boolean, rang integer) AS
$BODY$
declare	
   type_ character varying default 'C';
begin 		
	IF(COALESCE(by_famille_, FALSE))THEN
		type_ = 'F';
	END IF;
	RETURN QUERY SELECT * FROM com_et_journal_vente(societe_, agence_, date_debut_, date_fin_, type_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_journal_vente(bigint, bigint, date, date, boolean)
  OWNER TO postgres;
