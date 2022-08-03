-- Function: equilibre_achat(date, date)

-- DROP FUNCTION equilibre_achat(date, date);

CREATE OR REPLACE FUNCTION equilibre_achat(date_debut_ date, date_fin_ date)
  RETURNS boolean AS
$BODY$
DECLARE
	achat_ bigint;

BEGIN
	for achat_ in select d.id from yvs_com_doc_achats d where d.date_doc between date_debut_ and date_fin_
	loop
		PERFORM equilibre_achat(achat_);
	end loop; 
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_achat(date, date)
  OWNER TO postgres;
