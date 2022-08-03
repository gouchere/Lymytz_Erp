-- Function: get_pua(bigint, bigint)

-- DROP FUNCTION get_pua(bigint, bigint);

CREATE OR REPLACE FUNCTION get_pua(article_ bigint, fsseur_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE

BEGIN
	return get_pua(article_ , fsseur_ , 0::bigint);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pua(bigint, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_pua(bigint, bigint) IS 'retourne le prix d''achat d'' article';

-- Function: get_pua(bigint, bigint)
-- DROP FUNCTION get_pua(bigint, bigint);

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
COMMENT ON FUNCTION get_pua(bigint, bigint) IS 'retourne le prix d''achat d'' article';

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES 
('fv_save_only_pv', 'Traitement uniquement dans mes points de ventes', 'Traitement uniquement dans mes points de ventes', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_fv'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES 
('fa_view_all_doc', 'Voir toutes les factures de la socièté', 'Voir toutes les factures de la socièté', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_fa'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES 
('fa_view_only_doc_agence', 'Voir uniquement les factures de l''agence', 'Voir uniquement les factures de l''agence', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_fa'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES 
('fa_view_only_doc_depot', 'Voir uniquement les factures de mes dépôts', 'Voir uniquement les factures de mes dépôts', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_fa'), 16);
INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES 
('fv_update_doc', 'Modifier une facture', 'Modifier une facture', (SELECT y.id FROM yvs_page_module y WHERE reference = 'gescom_fv'), 16);

