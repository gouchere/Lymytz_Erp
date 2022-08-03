-- Function: get_pua(bigint, bigint, bigint)

-- DROP FUNCTION get_pua(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION get_pua(article_ bigint, fsseur_ bigint, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pua_ double precision;
	query_ character varying default 'select COALESCE(c.pua_recu, 0) from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat where c.pua_recu is not null and d.statut = ''V'' and c.article = '||article_||'';

BEGIN
	if(unite_ is not null and unite_ > 0)then
		query_ = query_ || ' and c.conditionnement = '||unite_||'';
	end if;
	if(fsseur_ is not null and fsseur_ > 0)then
		query_ = query_ || ' and d.fournisseur = '||fsseur_||'';
	end if;
	query_ = query_ || ' order by d.date_doc desc limit 1';
	EXECUTE query_ INTO pua_;
	if(pua_ is null or pua_ < 1)then
		if(unite_ is not null and unite_ > 0)then		
			if(fsseur_ is not null and fsseur_ > 0)then
				query_ = 'select COALESCE(y.pua, 0) from yvs_base_conditionnement_fournisseur y inner join yvs_base_article_fournisseur a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.fournisseur = '||fsseur_||' and a.article = '||article_||' and c.id = '||unite_||' limit 1';
			else
				query_ = 'select COALESCE(prix_achat, 0) from yvs_base_conditionnement c where c.id = '||unite_||'';
			end if;
			EXECUTE query_ INTO pua_;
			if(pua_ is null or pua_ < 1)then
				select into pua_ COALESCE(prix_achat, 0) from yvs_base_conditionnement c where c.id = unite_;
				if(pua_ is null or pua_ < 1)then		
					
				end if;
			end if;
		else
			if(fsseur_ is not null and fsseur_ > 0)then
				query_ = 'select COALESCE(y.puv, 0) from yvs_base_article_fournisseur y where fournisseur = '||fsseur_||' and article = '||article_||'';
			else
				query_ = 'select COALESCE(pua, 0) from yvs_base_articles c where c.id = '||article_||'';
			end if;
			EXECUTE query_ INTO pua_;
		end if;
		select into pua_ pua from yvs_base_articles where id = article_;
		if(pua_ is null)then
			pua_ = 0;
		end if;
	end if;
	return pua_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pua(bigint, bigint, bigint)
  OWNER TO postgres;

  
  -- Function: get_ttc_vente(bigint)

-- DROP FUNCTION get_ttc_vente(bigint);

CREATE OR REPLACE FUNCTION get_ttc_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	qte_ double precision default 0;
	cs_m double precision default 0;
	cs_p double precision default 0;
	data_ record;

BEGIN
	-- Recupere le CA d'une facture
	total_ = (select get_ca_vente(id_));
	if(total_ is null)then
		total_ = 0;
	end if;
	
	-- Recupere le total des quantitées d'une facture
	select into qte_ sum(quantite) from yvs_com_contenu_doc_vente where doc_vente = id_;
	if(qte_ is null)then
		qte_ = 0;
	end if;
	
	-- Recupere le total des couts supplementaire d'une facture
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id  where doc_vente = id_ and actif = true and t.augmentation is true;
	if(cs_p is null)then
		cs_p = 0;
	end if;
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id  where doc_vente = id_ and actif = true and t.augmentation is false;
	if(cs_m is null)then
		cs_m = 0;
	end if;
	total_ = total_ + cs_p - cs_m;
	
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ttc_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ttc_vente(bigint) IS 'retourne le montant TTC d''un doc vente';
