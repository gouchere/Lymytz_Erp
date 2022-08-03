-- Function: get_taxe(bigint, bigint, bigint, double precision, double precision, double precision, boolean)
-- DROP FUNCTION get_taxe(bigint, bigint, bigint, double precision, double precision, double precision, boolean);
CREATE OR REPLACE FUNCTION get_taxe(article_ bigint, categorie_ bigint, compte_ bigint, remise_ double precision, qte_ double precision, prix_ double precision, is_vente_ boolean, fournisseur_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	art_ record;
	taxe_ double precision default 0;
	valeur_ double precision default 0;
	data_ record;
	id_ bigint;
	pua_ttc_ boolean;

BEGIN
	select into art_ a.id, a.puv_ttc, coalesce(f.pua_ttc, a.pua_ttc) as pua_ttc 
	from yvs_base_articles a left join yvs_base_article_fournisseur f on f.article = a.id and fournisseur = fournisseur_ where a.id = article_;
	
	-- Recherche de la categorie comptable taxable
	if(compte_ is not null and compte_ > 0)then
		select into id_ id from yvs_base_article_categorie_comptable where article = article_ and categorie = categorie_ and compte = compte_ and actif = true;
	else
		select into id_ id from yvs_base_article_categorie_comptable where article = article_ and categorie = categorie_ and actif = true;
	end if;
	
	-- Verification si la categorie comptable taxable existe
	if(id_ is not null and id_ > 0)then
		if(is_vente_)then
			-- Verification si le prix de vente est le prix TTC
			if(art_.puv_ttc)then
				-- Calcul de la taxe sur le prix de vente
				for data_ in select t.taux from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
				loop
					taxe_ = taxe_ + data_.taux;
				end loop;
				-- On retire la taxe sur le prix de vente
				prix_ = prix_ / ( 1 + (taxe_ / 100));
			end if;
		else
			-- Verification si le prix d'achat est le prix TTC
			if(art_.pua_ttc)then
				-- Calcul de la taxe sur le prix d'achat
				for data_ in select t.taux from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
				loop
					taxe_ = taxe_ + ((data_.taux / 100 ) * (prix_ / (1 + (data_.taux / 100))));
				end loop;
				-- On retire la taxe sur le prix d'achat
				prix_ = prix_ - taxe_;
			end if;
		end if;
		-- Calcul de la valeur
		valeur_ = qte_ * prix_;
		
		-- Calcul de la taxe sur la valeur
		for data_ in select c.app_remise , t.taux from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
		loop
			-- Verification si la taxe s'applique sur la remise
			if(data_.app_remise)then
				taxe_ = taxe_ + (((valeur_ - remise_) * data_.taux) / 100);
			else
				taxe_ = taxe_ + ((valeur_ * data_.taux) / 100);
			end if;
		end loop;
	end if;	
	if(taxe_ is null or taxe_ <1)then
		taxe_ = 0;
	end if;
	return taxe_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_taxe(bigint, bigint, bigint, double precision, double precision, double precision, boolean, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_taxe(bigint, bigint, bigint, double precision, double precision, double precision, boolean, bigint) IS 'retourne la taxe d'' article';
