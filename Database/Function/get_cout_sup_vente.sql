-- Function: get_cout_sup_vente(bigint, boolean)
-- DROP FUNCTION get_cout_sup_vente(bigint, boolean);
CREATE OR REPLACE FUNCTION get_cout_sup_vente(id_ bigint, service_ boolean)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	cs_m double precision default 0;
	cs_p double precision default 0;

BEGIN
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id  where doc_vente = id_ and t.augmentation is true and o.service = service_;
	if(cs_p is null)then
		cs_p = 0;
	end if;
	total_ = cs_p - cs_m;
	if(total_ is null)then
		total_ = 0;
	end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_cout_sup_vente(bigint, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION get_cout_sup_vente(bigint, boolean) IS 'retourne le cout supplementaire de la facture (service ou non)';
