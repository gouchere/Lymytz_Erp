-- Function: insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date, double precision, bigint)
-- DROP FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date, double precision, bigint);
CREATE OR REPLACE FUNCTION insert_mouvement_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, coutentree_ double precision, cout_ double precision, parent_ bigint, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date, last_pr_ double precision, id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	ligne_ record;
	lot_ record;
	qualite_ record;
	
	operation_ character varying default '';
	
	val_ecart double precision;
	calcul_pr_ boolean default true;
BEGIN
	IF(COALESCE(idexterne_, 0) > 0 and tableexterne_ IS NOT NULL)THEN
		CASE tableexterne_
			WHEN 'yvs_com_contenu_doc_vente' THEN  
				select into ligne_ c.qualite, c.conditionnement, c.lot, c.calcul_pr, d.type_doc, d.num_doc from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id where c.id = idexterne_;
				operation_='Vente';
			WHEN 'yvs_com_contenu_doc_achat' THEN	
				select into ligne_ c.qualite, c.conditionnement, c.lot, c.calcul_pr, d.type_doc, d.num_doc from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on c.doc_achat = d.id  where c.id = idexterne_;
				operation_='Achat';
			WHEN 'yvs_com_ration' THEN	
				select into ligne_ c.qualite, c.conditionnement, c.lot, c.calcul_pr, 'RA'::character varying as type_doc, d.num_doc from yvs_com_ration c inner join yvs_com_doc_ration d on c.doc_ration = d.id  where c.id = idexterne_;
				operation_='Ration';
			WHEN 'yvs_com_contenu_doc_stock' THEN	
				if(mouvement_='S') then
					select into ligne_ d.type_doc, c.qualite, c.conditionnement, c.conditionnement_entree, c.lot_sortie as lot, calcul_pr, d.num_doc, d.document_lie FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
				else
					select into ligne_ d.type_doc, c.qualite_entree AS qualite, c.conditionnement_entree AS conditionnement, c.lot_entree as lot, calcul_pr, d.num_doc, d.document_lie FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
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
				if(ligne_.document_lie IS NOT NULL)then
					ligne_.type_doc := (SELECT d.type_doc FROM yvs_com_doc_stocks d WHERE d.id = ligne_.document_lie);
				end if;
			WHEN 'yvs_com_contenu_doc_stock_reception' THEN	
				select into ligne_ c.qualite_entree as qualite, c.conditionnement_entree as conditionnement, c.lot_entree as lot, r.calcul_pr, d.type_doc, d.num_doc from yvs_com_contenu_doc_stock_reception r inner join yvs_com_contenu_doc_stock c on r.contenu = c.id inner join yvs_com_doc_stocks d on d.id=c.doc_stock where r.id = idexterne_;
				operation_='Transfert';
			WHEN 'yvs_com_reconditionnement' THEN	
				if(mouvement_='S') then
					select into ligne_ c.qualite as qualite, c.conditionnement as conditionnement, c.lot as lot, c.calcul_pr, 'TR'::character varying as type_doc, d.num_doc from yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock where c.id = idexterne_;
					operation_='Reconditionnement';
				else
					select into ligne_ c.qualite_entree as qualite, c.conditionnement_entree as conditionnement, c.lot_entree as lot, c.calcul_pr, 'TR'::character varying as type_doc, d.num_doc from yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock where c.id = idexterne_;
					operation_='Reconditionnement';
				end if;	
			WHEN 'yvs_prod_of_suivi_flux' THEN	
				select into ligne_ c.unite as conditionnement, null::bigint as qualite, null::bigint as lot, calcul_pr, 'CO'::character varying as type_doc, d.code_ref AS num_doc from yvs_prod_of_suivi_flux y inner join yvs_prod_flux_composant f on y.composant = f.id inner join yvs_prod_composant_of c on f.composant = c.id
				inner join yvs_prod_ordre_fabrication d on c.ordre_fabrication = d.id where y.id = idexterne_;
				if(mouvement_='S') then
					operation_='Consommation';
				else
					operation_='Production';
				end if;
			WHEN 'yvs_prod_declaration_production' THEN	
				select into ligne_ c.conditionnement, null::bigint as qualite, null::bigint as lot, c.calcul_pr, 'DE'::character varying as type_doc, d.code_ref AS num_doc from yvs_prod_declaration_production c inner join yvs_prod_ordre_fabrication d on c.ordre = d.id where c.id = idexterne_;
				operation_='Production';
			WHEN 'yvs_prod_contenu_conditionnement' THEN	
				select into ligne_ c.conditionnement, null::bigint as qualite, null::bigint as lot, c.calcul_pr, 'RE'::character varying as type_doc, d.numero AS num_doc from yvs_prod_contenu_conditionnement c inner join yvs_prod_fiche_conditionnement d on c.fiche = d.id where c.id = idexterne_;
				operation_='Conditionnement';
			WHEN 'yvs_prod_fiche_conditionnement' THEN	
				select into ligne_ unite_mesure as conditionnement, null::bigint as qualite, null::bigint as lot, null::boolean calcul_pr, 'RE'::character varying as type_doc, f.numero AS num_doc from yvs_prod_fiche_conditionnement f inner join yvs_prod_nomenclature n on f.nomenclature = n.id where f.id = idexterne_;
				operation_='Conditionnement';
			ELSE
				RETURN FALSE;
		END CASE;
		val_ecart := (select taux_ecart_pr from yvs_base_articles where id = article_) ;
		if(COALESCE(val_ecart,0)>0)then
			if(abs(cout_ - last_pr_) > abs(val_ecart))then
				calcul_pr_ = false;
			 end if;
		end if;
		
		if(parent_ is not null)then
			if(tranche_ is not null and tranche_ > 0)then
				if(COALESCE(id_, 0) < 1)then
					INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement, conditionnement, calcul_pr, type_doc, num_doc)
						VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, tranche_::bigint, parent_::bigint, coutentree_, cout_, current_timestamp, ligne_.conditionnement::bigint, COALESCE(calcul_pr_, FALSE), ligne_.type_doc, ligne_.num_doc);
				else	
					UPDATE yvs_base_mouvement_stock SET quantite = quantite_, date_doc = date_, mouvement = mouvement_, article = article_::bigint, supp = false, actif = true, id_externe = idexterne_::bigint, table_externe = tableexterne_, description = operation_, depot = depot_::bigint, 
						tranche = tranche_::bigint, parent = parent_::bigint, cout_entree = coutentree_, cout_stock = cout_, conditionnement = ligne_.conditionnement::bigint, calcul_pr = COALESCE(calcul_pr_, FALSE), 
						type_doc = ligne_.type_doc, num_doc = ligne_.num_doc WHERE id = id_;
				end if;
			else
				if(COALESCE(id_, 0) < 1)then
					INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement, conditionnement, calcul_pr, type_doc, num_doc)
							VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, parent_::bigint, coutentree_, cout_, current_timestamp, ligne_.conditionnement::bigint, COALESCE(calcul_pr_, FALSE), ligne_.type_doc, ligne_.num_doc);
				else	
					UPDATE yvs_base_mouvement_stock SET quantite = quantite_, date_doc = date_, mouvement = mouvement_, article = article_::bigint, supp = false, actif = true, id_externe = idexterne_::bigint, table_externe = tableexterne_, description = operation_, depot = depot_::bigint, 
						parent = parent_::bigint, cout_entree = coutentree_, cout_stock = cout_, conditionnement = ligne_.conditionnement::bigint, calcul_pr = COALESCE(calcul_pr_, FALSE), 
						type_doc = ligne_.type_doc, num_doc = ligne_.num_doc WHERE id = id_;
				end if;
			end if;
		else
			if(tranche_ is not null and tranche_ > 0)then
				if(COALESCE(id_, 0) < 1)then
					INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement, conditionnement, calcul_pr, type_doc, num_doc)
							VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, tranche_::bigint, coutentree_, cout_, current_timestamp, ligne_.conditionnement::bigint, COALESCE(calcul_pr_, FALSE), ligne_.type_doc, ligne_.num_doc);
				else	
					UPDATE yvs_base_mouvement_stock SET quantite = quantite_, date_doc = date_, mouvement = mouvement_, article = article_::bigint, supp = false, actif = true, id_externe = idexterne_::bigint, table_externe = tableexterne_, description = operation_, depot = depot_::bigint, 
						tranche = tranche_::bigint, cout_entree = coutentree_, cout_stock = cout_, conditionnement = ligne_.conditionnement::bigint, calcul_pr = COALESCE(calcul_pr_, FALSE), 
						type_doc = ligne_.type_doc, num_doc = ligne_.num_doc WHERE id = id_;
				end if;
			else
				if(COALESCE(id_, 0) < 1)then
					INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement, conditionnement, calcul_pr, type_doc, num_doc)
							VALUES (quantite_, date_, mouvement_, article_::bigint, false, true, idexterne_::bigint, tableexterne_, operation_, depot_::bigint, coutentree_, cout_, current_timestamp, ligne_.conditionnement::bigint, COALESCE(calcul_pr_, FALSE), ligne_.type_doc, ligne_.num_doc);
				else	
					UPDATE yvs_base_mouvement_stock SET quantite = quantite_, date_doc = date_, mouvement = mouvement_, article = article_::bigint, supp = false, actif = true, id_externe = idexterne_::bigint, table_externe = tableexterne_, description = operation_, depot = depot_::bigint, 
						cout_entree = coutentree_, cout_stock = cout_, conditionnement = ligne_.conditionnement::bigint, calcul_pr = COALESCE(calcul_pr_, FALSE), 
						type_doc = ligne_.type_doc, num_doc = ligne_.num_doc WHERE id = id_;
				end if;
			end if;
		end if;
		IF(COALESCE(ligne_.lot, 0) > 0)THEN
			IF(COALESCE(id_, 0) > 0)THEN
				SELECT INTO lot_ l.id, l.lot FROM yvs_base_mouvement_stock_lot l WHERE l.mouvement = COALESCE(id_, 0);
				IF(COALESCE(lot_.id, 0) < 1)THEN
					INSERT INTO yvs_base_mouvement_stock_lot(lot, mouvement) VALUES(ligne_.lot, id_);
				ELSIF(COALESCE(lot_.id, 0) > 0 AND lot_.lot != ligne_.lot)THEN
					UPDATE yvs_base_mouvement_stock_lot SET lot = ligne_.lot WHERE id = lot_.id;
				END IF;
			ELSE
				SELECT INTO id_ id FROM yvs_base_mouvement_stock WHERE id_externe = idexterne_ and table_externe = tableexterne_;
				IF(COALESCE(id_, 0) > 0)THEN
					INSERT INTO yvs_base_mouvement_stock_lot(lot, mouvement) VALUES(ligne_.lot, id_);
				END IF;
			END IF;
		END IF;
		IF(COALESCE(ligne_.qualite, 0) > 0)THEN
			IF(COALESCE(id_, 0) > 0)THEN
				SELECT INTO qualite_ l.id, l.qualite FROM yvs_base_mouvement_stock_qualite l WHERE l.mouvement = COALESCE(id_, 0);
				IF(COALESCE(qualite_.id, 0) < 1)THEN
					INSERT INTO yvs_base_mouvement_stock_qualite(qualite, mouvement) VALUES(ligne_.qualite, id_);
				ELSIF(COALESCE(qualite_.id, 0) > 0 AND qualite_.qualite != ligne_.qualite)THEN
					UPDATE yvs_base_mouvement_stock_qualite SET qualite = ligne_.qualite WHERE id = qualite_.id;
				END IF;
			ELSE
				SELECT INTO id_ id FROM yvs_base_mouvement_stock WHERE id_externe = idexterne_ and table_externe = tableexterne_;
				IF(COALESCE(id_, 0) > 0)THEN
					INSERT INTO yvs_base_mouvement_stock_qualite(qualite, mouvement) VALUES(ligne_.qualite, id_);
				END IF;
			END IF;
		END IF;
		return true;
	END IF;
	return false;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date, double precision, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date, double precision, bigint) IS 'Insert une ligne de sortie de mouvement de stock';
