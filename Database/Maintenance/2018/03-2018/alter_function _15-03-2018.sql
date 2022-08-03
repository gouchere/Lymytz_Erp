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


-- Function: equilibre_approvision(bigint)

-- DROP FUNCTION equilibre_approvision(bigint);

CREATE OR REPLACE FUNCTION equilibre_approvision(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	fiche_ record;
	contenu_ record;
	achat_ double precision default 0;
	stock_ double precision default 0;
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

BEGIN
	select into fiche_ etat as statut from yvs_com_fiche_approvisionnement where id = id_;
	-- Equilibre de l'etat livré
	for contenu_ in select id, article, conditionnement as unite, quantite as qte from yvs_com_article_approvisionnement where fiche = id_
	loop
		in_ = true;
		select into achat_ COALESCE(SUM(c.quantite_recu), 0) from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on c.doc_achat = d.id
			where c.externe = contenu_.id and c.quantite_recu is not null and d.type_doc = 'FA' and d.statut in ('E','V');
		if(achat_ is null)then
			achat_ = 0;
		end if;
		select into stock_ COALESCE(SUM(c.quantite), 0) from yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on c.doc_stock = d.id
			where c.externe = contenu_.id and c.quantite is not null and d.type_doc = 'FA' and d.statut in ('E','V');
		if(stock_ is null)then
			stock_ = 0;
		end if;
		if((stock_ + achat_) < contenu_.qte)then
			correct = false;
			if((stock_ + achat_) > 0)then
				encours = true;
				exit;
			end if;
		end if;
	end loop;
	if(fiche_.statut = 'V')then
		if(in_)then
			if(correct)then
				update yvs_com_fiche_approvisionnement set statut_terminer = 'U' where id = id_;
			elsif(encours)then
				update yvs_com_fiche_approvisionnement set statut_terminer = 'R' where id = id_;
			else
				update yvs_com_fiche_approvisionnement set statut_terminer = 'W' where id = id_;
			end if;	
		else
			update yvs_com_fiche_approvisionnement set statut_terminer = 'W' where id = id_;
		end if;	
	end if;
	update yvs_workflow_valid_approvissionnement set date_update = date_update where document = id_;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_approvision(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_approvision(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de approvision';
