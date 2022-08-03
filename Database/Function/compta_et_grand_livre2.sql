-- Function: compta_et_grand_livre(bigint, bigint, character varying, date, date, bigint, character varying, boolean)

-- DROP FUNCTION compta_et_grand_livre(bigint, bigint, character varying, date, date, bigint, character varying, boolean);

CREATE OR REPLACE FUNCTION compta_et_grand_livre(IN societe_ bigint, IN agence_ bigint, IN comptes_ character varying, IN date_debut_ date, IN date_fin_ date, IN journal_ bigint, IN type_ character varying, IN lettrer_ boolean)
  RETURNS TABLE(id bigint, code character varying, intitule character varying, compte character varying, designation character varying, numero character varying, date_piece date, jour integer, libelle character varying, lettrage character varying, journal character varying, debit double precision, credit double precision, compte_tiers bigint, table_tiers character varying, solde boolean) AS
$BODY$
declare 
	
begin 	
	RETURN  SELECT * FROM compta_et_grand_livre(societe_,agence_,comptes_, date_debut_, date_fin_, journal_,type_,lettrer_,false);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_grand_livre(bigint, bigint, character varying, date, date, bigint, character varying, boolean)
  OWNER TO postgres;
