-- Function: get_remise_journal_vente(bigint)

-- DROP FUNCTION get_remise_journal_vente(bigint);

CREATE OR REPLACE FUNCTION get_remise_journal_vente(header_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	remise_ double precision default 0;
	vente_ bigint default 0;

BEGIN
	if(header_ is not null and header_ > 0)then
		for vente_ in select id from yvs_com_doc_ventes where type_doc = 'FV' and entete_doc = header_
		loop
			remise_ = remise_ + (select get_remise_vente(vente_));
		end loop;
	end if;
	if(remise_ is null or remise_ <=0)then
		remise_ = 0;
	end if;	
	return remise_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise_journal_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise_journal_vente(bigint) IS 'retourne la remise sur un journal de vente';
