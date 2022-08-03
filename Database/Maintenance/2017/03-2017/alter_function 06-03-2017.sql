-- Function: insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)

-- DROP FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date);

CREATE OR REPLACE FUNCTION insert_mouvement_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, coutentree_ double precision, cout_ double precision, parent_ bigint, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE
       operation_ character varying default '';
       type_doc_ character varying;
       qualite_ bigint;
BEGIN
	CASE tableexterne_
		WHEN 'yvs_com_contenu_doc_vente' THEN  
			select into qualite_ qualite from yvs_com_contenu_doc_vente where id = idexterne_;
			operation_='Vente';
		WHEN 'yvs_com_contenu_doc_achat' THEN	
			select into qualite_ qualite from yvs_com_contenu_doc_achat where id = idexterne_;
			operation_='Achat';
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			select into qualite_ qualite from yvs_com_contenu_doc_stock where id = idexterne_;
			select into type_doc_ d.type_doc FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			if(type_doc_='FT') then 
				operation_='Transfert';
			else
				if(mouvement_='S') then
					operation_='Sortie';
				else
					operation_='Entrée';
				end if;
			end if;		
		ELSE
		  RETURN -1;
	END CASE;
	if(parent_ is not null)then
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement, qualite)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, parent_, coutentree_, cout_, current_timestamp, qualite_);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement, qualite)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, parent_, coutentree_, cout_, current_timestamp, qualite_);
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement, qualite)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, coutentree_, cout_, current_timestamp, qualite_);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement, qualite)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, coutentree_, cout_, current_timestamp, qualite_);
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date) IS 'Insert une ligne de sortie de mouvement de stock';
