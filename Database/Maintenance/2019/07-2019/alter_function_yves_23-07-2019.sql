SELECT insert_droit('compta_justif_bp', 'Justifier un bon provisoire non équilibré', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_bon_prov'), 16, 'A,B,O','R');

-- Function: insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)

-- DROP FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date);

CREATE OR REPLACE FUNCTION insert_mouvement_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, coutentree_ double precision, cout_ double precision, parent_ bigint, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE
       operation_ character varying default '';
       ligne_ record;
BEGIN
	CASE tableexterne_
		WHEN 'yvs_com_contenu_doc_vente' THEN  
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_contenu_doc_vente where id = idexterne_;
			IF(mouvement_='E') THEN
				operation_='Retour vente';
			ELSE
				operation_='Vente';
			END IF;
		WHEN 'yvs_com_contenu_doc_achat' THEN	
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_contenu_doc_achat where id = idexterne_;			
			IF(mouvement_='E') THEN
				operation_='Achat';
			ELSE
				operation_='Retour Achat';
			END IF;
		WHEN 'yvs_com_ration' THEN	
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_ration where id = idexterne_;
			operation_='Ration';
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			if(mouvement_='S') then
				select into ligne_ d.type_doc, c.qualite, c.conditionnement, c.conditionnement_entree, c.lot_sortie as lot, calcul_pr FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			else
				select into ligne_ d.type_doc, c.qualite_entree AS qualite, c.conditionnement_entree AS conditionnement, c.lot_entree as lot, calcul_pr FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			end if;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
			elsif(ligne_.type_doc='TR')then
				operation_='Reconditionnement';
			else
				if(mouvement_='S') then
					operation_='Sortie';
				else
					operation_='Entrée';
				end if;
			end if;	
		WHEN 'yvs_com_contenu_doc_stock_reception' THEN	
			select into ligne_ c.qualite_entree as qualite, c.conditionnement_entree as conditionnement, c.lot_entree as lot, r.calcul_pr from yvs_com_contenu_doc_stock_reception r inner join yvs_com_contenu_doc_stock c on r.contenu = c.id where r.id = idexterne_;
			operation_='Transfert';
		WHEN 'yvs_com_reconditionnement' THEN	
			if(mouvement_='S') then
				select into ligne_ qualite as qualite, conditionnement as conditionnement, lot as lot, calcul_pr from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			else
				select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot, calcul_pr from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			end if;	
		WHEN 'yvs_prod_of_suivi_flux' THEN	
			select into ligne_ c.unite as conditionnement, null::bigint as qualite, null::bigint as lot, calcul_pr from yvs_prod_of_suivi_flux y inner join yvs_prod_flux_composant f on y.composant = f.id inner join yvs_prod_composant_of c on f.composant = c.id where y.id = idexterne_;
			if(mouvement_='S') then
				operation_='Consommation';
			else
				operation_='Production';
			end if;
		WHEN 'yvs_prod_declaration_production' THEN	
			select into ligne_ conditionnement, null::bigint as qualite, null::bigint as lot, calcul_pr from yvs_prod_declaration_production where id = idexterne_;
			operation_='Production';
		WHEN 'yvs_prod_contenu_conditionnement' THEN	
			select into ligne_ conditionnement, null::bigint as qualite, null::bigint as lot, calcul_pr from yvs_prod_contenu_conditionnement where id = idexterne_;
			operation_='Conditionnement';
		WHEN 'yvs_prod_fiche_conditionnement' THEN	
			select into ligne_ unite_mesure as conditionnement, null::bigint as qualite, null::bigint as lot, null::boolean calcul_pr from yvs_prod_fiche_conditionnement f inner join yvs_prod_nomenclature n on f.nomenclature = n.id where f.id = idexterne_;
			operation_='Conditionnement';
		ELSE
			RETURN FALSE;
	END CASE;
	if(parent_ is not null)then
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, tranche_::bigint, parent_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, parent_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, tranche_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot, calcul_pr)
					VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, coutentree_, cout_, current_timestamp, ligne_.qualite::bigint, ligne_.conditionnement::bigint, ligne_.lot::bigint, ligne_.calcul_pr);
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date) IS 'Insert une ligne de sortie de mouvement de stock';
	