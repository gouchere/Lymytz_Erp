-- Function: init_taxe_contenu(bigint)

-- DROP FUNCTION init_taxe_contenu(bigint);

CREATE OR REPLACE FUNCTION init_taxe_contenu(societe_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	facture record;
	contenu record;
	art_ record;
	data_ record;
	
	id_ bigint;
	deja_ bigint;
	
	valeur_ double precision default 0;
	remise_ double precision default 0;
	qte_ double precision default 0;
	prix_ double precision default 0;
	taxe_ double precision default 0;

BEGIN
	for facture in select * from yvs_com_doc_ventes
	loop
		for contenu in select * from yvs_com_contenu_doc_vente where doc_vente = facture.id
		loop
			remise_ = contenu.remise;
			qte_ = contenu.quantite;
			prix_ = contenu.prix - contenu.rabais;
			
			select into art_ * from yvs_base_articles where id = contenu.article;		
			-- Recherche de la categorie comptable taxable
			select into id_ id from yvs_base_article_categorie_comptable where article = contenu.article and categorie = facture.categorie_comptable and actif = true;
	
			-- Verification si la categorie comptable taxable existe
			if(id_ is not null and id_ > 0)then
				-- Verification si le prix de vente est le prix TTC
				if(art_.puv_ttc)then
					-- Calcul de la taxe sur le prix de vente
					for data_ in select t.taux from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
					loop
						taxe_ = taxe_ + data_.taux;
					end loop;
					-- On retire la taxe sur le prix de vente
					prix_ = prix_ / ( 1 + (taxe_/100));
				end if;
				-- Calcul de la valeur
				valeur_ = qte_ * prix_;
				
				-- Calcul de la taxe sur la valeur
				for data_ in select c.app_remise , t.taux, t.id from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
				loop
					taxe_ = 0;
					-- Verification si la taxe s'applique sur la remise
					if(data_.app_remise)then
						taxe_ = (((valeur_ - remise_) * data_.taux) / 100);
					else
						taxe_ = ((valeur_ * data_.taux) / 100);
					end if;
					taxe_ = (select arrondi(societe_, taxe_));
					if(taxe_ > 0)then
						select into deja_ c.id from yvs_com_taxe_contenu_vente c where c.contenu = contenu.id and c.taxe = data_.id;
						if(deja_ is null or deja_ < 1)then
							insert into yvs_com_taxe_contenu_vente(contenu, taxe, montant) values(contenu.id, data_.id, taxe_);
						else
							update yvs_com_taxe_contenu_vente set montant = taxe_ where id = deja_;
						end if;
					else
						select into deja_ c.id from yvs_com_taxe_contenu_vente c where c.contenu = contenu.id and c.taxe = data_.id;
						if(deja_ is not null and deja_ > 0)then
							delete from yvs_com_taxe_contenu_vente where id = deja_;
						end if;
					end if;
				end loop;
			end if;			
		end loop;
	end loop;
	return false;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION init_taxe_contenu(bigint)
  OWNER TO postgres;
