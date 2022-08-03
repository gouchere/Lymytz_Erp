-- Function: equilibre_vente_livre(bigint, boolean)
-- DROP FUNCTION equilibre_vente_livre(bigint, boolean);
CREATE OR REPLACE FUNCTION equilibre_vente_livre(id_ bigint, by_parent_ boolean)
  RETURNS character varying AS
$BODY$
DECLARE
	ch_ bigint default 0;
	
	line_ record;
	contenu_ record;
	
	count_ bigint default 0;
	
	qte_ double precision default 0;
	
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

	statut_ character varying default 'W';
	query_control character varying;
	query_content character varying;

BEGIN
	-- Equilibre de l'etat reglé
	SELECT INTO line_ d.type_doc FROM yvs_com_doc_ventes d WHERE d.id = id_;
	IF(line_.type_doc = 'FV' OR line_.type_doc= 'BCV') THEN
		-- Equilibre de l'etat livré
		SELECT INTO count_ COUNT(c.id) FROM yvs_com_contenu_doc_vente c WHERE c.doc_vente = id_;
		IF(COALESCE(count_, 0) > 0)THEN
			in_ = true;
			query_control = 'select sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id 
				where d.type_doc = ''BLV'' and d.statut = ''V'' and d.document_lie = '||id_;
			IF(by_parent_)THEN
				query_content = 'select c.id, c.conditionnement as unite, c.quantite::decimal as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id where a.suivi_en_stock IS TRUE AND c.doc_vente = '||id_;
			ELSE
				query_content = 'select c.article as id, c.conditionnement as unite, sum(c.quantite)::decimal as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id where a.suivi_en_stock IS TRUE AND c.doc_vente = '||id_||' group by c.article, c.conditionnement ORDER BY c.article, c.conditionnement';
			END IF;
			for contenu_ in execute query_content
			loop
				IF(by_parent_)THEN
					execute query_control || ' and c.parent = '||COALESCE(contenu_.id, 0) into qte_;
				ELSE
					execute query_control || ' and c.conditionnement = '||COALESCE(contenu_.unite, 0) into qte_;
				END IF;
				qte_ = coalesce(qte_, 0);
				if(qte_ < contenu_.qte)then
					correct = false;
					if(qte_ > 0)then
						encours = true;
						exit;
					end if;
				end if;
			end loop;
			-- Bonus
			IF(by_parent_)THEN
				query_content = 'select c.id, c.conditionnement_bonus as unite, coalesce(c.quantite_bonus, 0)::decimal as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id where a.suivi_en_stock IS TRUE AND c.doc_vente = '||id_||' and coalesce(c.quantite_bonus, 0) > 0';
			ELSE
				query_content = 'select c.article_bonus as id, c.conditionnement_bonus as unite, sum(c.quantite_bonus)::decimal as qte from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on c.article = a.id where a.suivi_en_stock IS TRUE AND c.doc_vente = '||id_||' and coalesce(c.quantite_bonus, 0) > 0 group by c.article_bonus, c.conditionnement_bonus';
			END IF;
			for contenu_ in execute query_content
			loop
				IF(by_parent_)THEN
					execute query_control || ' and c.parent = '||COALESCE(contenu_.id, 0) into qte_;
				ELSE
					execute query_control || ' and c.conditionnement = '||COALESCE(contenu_.unite, 0) into qte_;
				END IF;
				qte_ = coalesce(qte_, 0);
				if(qte_ < contenu_.qte)then
					correct = false;
					if(qte_ > 0)then
						encours = true;
						exit;
					end if;
				end if;
			end loop;
		END IF;

		if(in_)then
			if(correct)then
				statut_ = 'L';
				update yvs_com_doc_ventes set statut_livre = statut_ where id = id_;
			else
				if(encours)then
					statut_ = 'R';
					update yvs_com_doc_ventes set statut_livre = statut_ where id = id_;
				elsif(by_parent_)then
					update yvs_com_doc_ventes set statut_livre = statut_ where id = id_;
				end if;
				IF(by_parent_)then
					statut_ = (SELECT equilibre_vente_livre(id_, false));
				end if;
			end if;	
		else
			update yvs_com_doc_ventes set statut_livre = statut_ where id = id_;
		end if;	
		update yvs_workflow_valid_facture_vente set date_update = date_update where facture_vente = id_;
	END IF;
	return statut_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente_livre(bigint, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente_livre(bigint, boolean) IS 'equilibre l''etat livré des documents de vente';
