-- Function: get_pua_total(bigint)

-- DROP FUNCTION get_pua_total(bigint);

CREATE OR REPLACE FUNCTION get_pua_total(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	contenu_ record;
	remise_ double precision;
	somme_ double precision;
	total_ double precision;

BEGIN
	select into contenu_ quantite_attendu as qte, pua_attendu as pua, remise_attendu as rem 
		from yvs_com_contenu_doc_achat where id = id_;
	somme_ =  contenu_.qte * contenu_.pua;
	if(somme_ is null)then
		somme_ = 0;
	end if;
	remise_ = somme_ * (contenu_.rem / 100);
	if(remise_ is null)then
		remise_ = 0;
	end if;
	total_ = somme_ - remise_;
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pua_total(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pua_total(bigint) IS 'retourne le prix d''achat d'' article';
