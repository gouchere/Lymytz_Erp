-- Function: get_taxe_achat(bigint)

-- DROP FUNCTION get_taxe_achat(bigint);

CREATE OR REPLACE FUNCTION get_taxe_achat(IN id_ bigint)
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
	select into categorie_ categorie_comptable from yvs_com_doc_achats where id = id_;
	
	CREATE TEMP TABLE IF NOT EXISTS com_taxe_achat(taxe_ bigint, montant_ double precision);
	DELETE FROM com_taxe_achat;

	for contenu_ in select * from yvs_com_contenu_doc_achat where doc_achat = id_
	loop
		total_ = contenu_.quantite_attendu * (contenu_.pua_attendu);
		for data_ in select y.id, y.taux, t.app_remise from yvs_base_taxes y inner join yvs_base_article_categorie_comptable_taxe t on y.id = t.taxe inner join yvs_base_article_categorie_comptable c on c.id = t.article_categorie where c.article = contenu_.article and c.categorie = categorie_
		loop
			if(data_.app_remise)then
				valeur_ = (((total_ - contenu_.remise_attendu) * data_.taux) / 100);
			else
				valeur_ = ((total_ * data_.taux) / 100);
			end if;
			select into ligne_ taxe_ from com_taxe_achat where taxe_ = data_.id;
			if(ligne_ is not null and ligne_ >0)then
				update com_taxe_achat set montant_ = montant_ + valeur_ where taxe_ = ligne_;
			else
				insert into com_taxe_achat values (data_.id, valeur_);
			end if;
		end loop;
	end loop;
	
	return QUERY SELECT * FROM com_taxe_achat order by taxe_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION get_taxe_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_taxe_achat(bigint) IS 'retourne les taxes d''un document de achat';
