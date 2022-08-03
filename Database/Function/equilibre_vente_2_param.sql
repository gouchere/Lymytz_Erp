-- Function: equilibre_vente(date, date)

-- DROP FUNCTION equilibre_vente(date, date);

CREATE OR REPLACE FUNCTION equilibre_vente(date_debut_ date, date_fin_ date)
  RETURNS boolean AS
$BODY$
DECLARE
	vente_ bigint;

BEGIN
	for vente_ in select d.id from yvs_com_doc_ventes d inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc where e.date_entete between date_debut_ and date_fin_
	loop
		PERFORM equilibre_vente(vente_);
	end loop; 
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(date, date)
  OWNER TO postgres;
