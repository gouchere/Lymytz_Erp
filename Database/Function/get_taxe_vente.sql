-- Function: get_taxe_vente(bigint)

-- DROP FUNCTION get_taxe_vente(bigint);

CREATE OR REPLACE FUNCTION get_taxe_vente(IN id_ bigint)
  RETURNS TABLE(taxe bigint, montant double precision) AS
$BODY$
DECLARE
	contenu_ record;
	categorie_ bigint;
	data_ record;
	ligne_ bigint;
	total_ double precision default 0;
	valeur_ double precision default 0;
	

BEGIN
	select into categorie_ categorie_comptable from yvs_com_doc_ventes where id = id_;
	
	CREATE TEMP TABLE IF NOT EXISTS com_taxe_vente(taxe_ bigint, montant_ double precision);
	DELETE FROM com_taxe_vente;

	for contenu_ in select * from yvs_com_contenu_doc_vente where doc_vente = id_
	loop
		total_ = contenu_.quantite * (contenu_.prix - contenu_.rabais);
		if(total_ is null)then
			total_ = 0;
		end if;
		for data_ in select y.id, y.taux, t.app_remise from yvs_base_taxes y inner join yvs_base_article_categorie_comptable_taxe t on y.id = t.taxe inner join yvs_base_article_categorie_comptable c on c.id = t.article_categorie where c.article = contenu_.article and c.categorie = categorie_
		loop
			if(data_.app_remise)then
				valeur_ = (((total_ - contenu_.remise) * data_.taux) / 100);
			else
				valeur_ = ((total_ * data_.taux) / 100);
			end if;
			if(valeur_ is null)then
				valeur_ = 0;
			end if;
			select into ligne_ taxe_ from com_taxe_vente where taxe_ = data_.id;
			if(ligne_ is not null and ligne_ >0)then
				update com_taxe_vente set montant_ = montant_ + valeur_ where taxe_ = ligne_;
			else
				insert into com_taxe_vente values (data_.id, valeur_);
			end if;
		end loop;
	end loop;
	
	return QUERY SELECT * FROM com_taxe_vente order by taxe_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION get_taxe_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_taxe_vente(bigint) IS 'retourne les taxes d''un document de vente';
