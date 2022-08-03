-- Function: get_pua(bigint, bigint, bigint, bigint, date)
-- DROP FUNCTION get_pua(bigint, bigint, bigint, bigint, date);
CREATE OR REPLACE FUNCTION get_pua(article_ bigint, fsseur_ bigint, depot_ bigint, unite_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	line_ record;
	
	pua_ double precision;
	coef_ double precision;
	-- A partir des facture d'achat
	query_ character varying default 'select c.pua_recu, c.conditionnement from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat where c.article = '||article_;

BEGIN
	if(fsseur_ is not null and fsseur_ > 0)then
		query_ = query_ || ' and d.fournisseur = '||fsseur_;
	end if;
	if(depot_ is not null and depot_ > 0)then
		query_ = query_ || ' and d.agence = (SELECT a.agence FROM yvs_base_depots a WHERE a.id = '||depot_||')';
	end if;
	if(date_ is not null)then
		query_ = query_ || ' and d.date_doc <= '||QUOTE_LITERAL(date_);
	end if;
	EXECUTE query_ || ' order by d.date_doc desc limit 1' INTO line_;
	IF(COALESCE(line_.conditionnement, 0) > 0)THEN
		IF(line_.conditionnement = unite_) THEN
			pua_ = COALESCE(line_.pua_recu, 0);
		ELSE		
			SELECT INTO coef_ taux_change FROM yvs_base_table_conversion t INNER JOIN yvs_base_conditionnement us ON us.unite = t.unite
				  INNER JOIN yvs_base_conditionnement ud ON ud.unite = t.unite_equivalent WHERE us.id=unite_ AND ud.id = line_.conditionnement;
			IF(COALESCE(coef_, 0) > 0)THEN
				pua_ = COALESCE(line_.pua_recu, 0) * COALESCE(coef_, 0); 
				RAISE NOTICE 'here % % %',line_.conditionnement, line_.pua_recu, pua_;
			END IF;
		END IF;
	END IF;
	IF(COALESCE(pua_, 0) <= 0)THEN
		if(unite_ is not null and unite_ > 0)then
			query_ = query_ || ' and c.conditionnement = '||unite_;
		end if;
		EXECUTE query_ || ' order by d.date_doc desc limit 1' INTO line_;
		IF(COALESCE(line_.conditionnement, 0) > 0)THEN
			pua_ = COALESCE(line_.pua_recu, 0);
		END IF;
	END IF;
	IF(COALESCE(pua_, 0) <= 0)THEN
		-- A partir des conditionnement
		select into pua_ y.pua from yvs_base_conditionnement_fournisseur y inner join yvs_base_article_fournisseur a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.fournisseur = fsseur_ and a.article = article_ and c.id = unite_ limit 1;
		if(pua_ is null or pua_ <= 0)then
			select into pua_ puv from yvs_base_article_fournisseur where fournisseur = fsseur_ and article = article_;
			if(pua_ is null or pua_ <= 0)then
				select into pua_ prix_achat from yvs_base_conditionnement c where c.id = unite_;
				if(pua_ is null or pua_ <= 0)then		
					select into pua_ pua from yvs_base_articles where id = article_; 
				end if;
			end if;	
		end if;
	END IF;
	return COALESCE(pua_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pua(bigint, bigint, bigint, bigint, date)
  OWNER TO postgres;
