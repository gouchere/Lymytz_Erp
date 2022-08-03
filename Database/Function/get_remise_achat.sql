-- Function: get_remise_achat(bigint, double precision, double precision, bigint)

-- DROP FUNCTION get_remise_achat(bigint, double precision, double precision, bigint);

CREATE OR REPLACE FUNCTION get_remise_achat(article_ bigint, qte_ double precision, prix_ double precision, fsseur_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	tarif_ record;
	remise_ double precision;
	valeur_ double precision default 0;

BEGIN
	valeur_ = qte_ * prix_;	
	select into tarif_ * from yvs_base_article_fournisseur where fournisseur = fsseur_ and article = article_;
	if(tarif_.id IS NOT NULL) then
		if(tarif_.nature_remise = 'TAUX')then
			remise_ = valeur_ * (tarif_.remise /100);
		else
			remise_ = tarif_.remise;
		end if;
	end if;
	if(remise_ is null)then
		remise_ = 0;
	end if;
	return remise_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise_achat(bigint, double precision, double precision, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise_achat(bigint, double precision, double precision, bigint) IS 'retourne le remise sur achat d'' article';
