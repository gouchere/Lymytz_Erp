-- Function: get_pua(bigint, bigint, bigint)

-- DROP FUNCTION get_pua(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION get_pua(article_ bigint, fsseur_ bigint, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pua_ double precision;

BEGIN
	if(unite_ is not null and unite_ > 0)then
		select into pua_ c.pua_attendu from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat where c.article = article_ and c.conditionnement = unite_ and d.statut = 'V' order by d.date_doc desc limit 1;
		if(pua_ is null or pua_ < 1)then	
			select into pua_ y.pua from yvs_base_conditionnement_fournisseur y inner join yvs_base_article_fournisseur a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.fournisseur = fsseur_ and a.article = article_ and c.id = unite_ and actif is true limit 1;
			if(pua_ is null or pua_ < 1)then
				select into pua_ prix_achat from yvs_base_conditionnement where c.id = unite_;
				if(pua_ is null or pua_ < 1)then		
					select into pua_ pua from yvs_base_articles where id = article_;
					if(pua_ is null)then
						pua_ = 0;
					end if;
				end if;
			end if;
		end if;
	else
		select into pua_ c.pua_attendu from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat where c.article = article_ and d.statut = 'V' order by d.date_doc desc limit 1;
		if(pua_ is null or pua_ < 1)then
			select into pua_ puv from yvs_base_article_fournisseur where fournisseur = fsseur_ and article = article_;
			if(pua_ is null or pua_ < 1)then		
				select into pua_ pua from yvs_base_articles where id = article_;
				if(pua_ is null)then
					pua_ = 0;
				end if;
			end if;
		end if;
	end if;
	return pua_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pua(bigint, bigint, bigint)
  OWNER TO postgres;
