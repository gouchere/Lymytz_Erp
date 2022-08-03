-- Function: com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN agence_ bigint, IN depot_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision) AS
$BODY$
DECLARE 
	articles_ RECORD;
	unites_ RECORD;

	insert_ BOOLEAN DEFAULT false;

	unite_ BIGINT DEFAULT 0;
	
	prix_ DOUBLE PRECISION DEFAULT 0;
	stock_ DOUBLE PRECISION DEFAULT 0;
	reservation_ DOUBLE PRECISION DEFAULT 0;
	reste_a_livre_ DOUBLE PRECISION DEFAULT 0;

	query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.article, y.depot, a.ref_art, a.designation, f.reference_famille, f.designation as famille, a.pua, a.puv, d.agence, y.actif FROM yvs_base_article_depot y 
		INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON f.id = a.famille INNER JOIN yvs_base_depots d ON y.depot = d.id WHERE y.article IS NOT NULL';
	
BEGIN 	
	DROP TABLE IF EXISTS table_inventaire_preparatoire;
	CREATE TEMP TABLE IF NOT EXISTS table_inventaire_preparatoire(_depot bigint, _article bigint, _code character varying, _designation character varying, numero_ character varying, _famille character varying, _unite bigint, _reference character varying, prix_ double precision, _puv double precision, _pua double precision, _pr double precision, _stock double precision, _reservation double precision, _reste_a_livre double precision); 
	DELETE FROM table_inventaire_preparatoire;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND d.agence = '||agence_;
	END IF;
	IF(depot_ IS NOT NULL AND depot_ > 0)THEN
		query_ = query_ || ' AND d.id = '||depot_;
	END IF;
	option_print_ = COALESCE(option_print_, '');
-- 	RAISE NOTICE '%',query_;
	FOR articles_ IN EXECUTE query_
	LOOP
		FOR unites_ IN SELECT y.id, y.unite, u.reference, COALESCE(y.prix, articles_.puv) AS puv, COALESCE(y.prix_achat, articles_.pua) AS pua FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = articles_.article
		LOOP
			insert_ = false;
			stock_ = (SELECT get_stock(articles_.article, 0, articles_.depot, articles_.agence, 0, date_, unites_.id, 0));
			SELECT INTO reservation_ SUM(c.quantite) FROM yvs_com_reservation_stock c WHERE c.depot = articles_.depot AND c.article = articles_.article AND c.conditionnement = unites_.id AND c.statut = 'V' AND c.date_echeance <= date_;
			SELECT INTO reste_a_livre_ ((COALESCE((select sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on a.id=c.article inner join yvs_com_doc_ventes d on d.id=c.doc_vente  inner join yvs_com_entete_doc_vente en on en.id=d.entete_doc
					where d.type_doc = 'FV' and d.statut = 'V' and en.date_entete <= date_ AND c.article = articles_.article AND d.depot_livrer = articles_.depot AND c.conditionnement = unites_.id limit 1), 0))
				- (COALESCE((select sum(c1.quantite) from yvs_com_contenu_doc_vente c1 inner join yvs_base_articles a1 on a1.id=c1.article inner join yvs_com_doc_ventes d1 on d1.id=c1.doc_vente 
					where d1.type_doc = 'BLV' and d1.statut = 'V' and d1.date_livraison <= date_ AND c1.article = articles_.article AND d1.depot_livrer = articles_.depot AND c1.conditionnement = unites_.id limit 1), 0)));
			IF(reste_a_livre_ < 0)THEN
				reste_a_livre_ = 0;
			END IF;
			IF(print_all_)THEN
				IF(stock_ != 0)THEN
					insert_ = true;
				ELSE
					insert_ = articles_.actif;					
				END IF;
			ELSE
				IF(option_print_ = 'P')THEN
					IF(stock_ > 0)THEN
						insert_ = true;
					END IF;
				ELSIF(option_print_ = 'N')THEN
					IF(stock_ < 0)THEN
						insert_ = true;
					END IF;
				ELSE
					IF(stock_ != 0)THEN
						insert_ = true;
					END IF;
				END IF;
			END IF;
			IF(insert_)THEN
				IF(type_ = 'A')THEN
					prix_ = COALESCE((SELECT get_pua(articles_.article, 0, depot_, unites_.id)), 0);
				ELSIF(type_ = 'V')THEN
					prix_ = unites_.puv;
				ELSE
					prix_ = COALESCE((SELECT get_pr(articles_.article, depot_, 0, date_, unites_.id)), 0);
				END IF;
				INSERT INTO table_inventaire_preparatoire VALUES(articles_.depot, articles_.article, articles_.ref_art, articles_.designation, articles_.reference_famille, articles_.famille, unites_.unite, unites_.reference, prix_, unites_.puv, unites_.pua, 0, stock_, COALESCE(reservation_, 0), COALESCE(reste_a_livre_, 0));
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_inventaire_preparatoire ORDER BY _depot, _famille, _code;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;
  
  
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
			operation_='Vente';
		WHEN 'yvs_com_contenu_doc_achat' THEN	
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_contenu_doc_achat where id = idexterne_;
			operation_='Achat';
		WHEN 'yvs_com_ration' THEN	
			select into ligne_ qualite, conditionnement, lot, calcul_pr from yvs_com_ration where id = idexterne_;
			operation_='Ration';
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			select into ligne_ d.type_doc, c.qualite, c.conditionnement, c.conditionnement_entree, c.lot_sortie as lot, calcul_pr FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
				if(mouvement_ = 'E')then
					ligne_.conditionnement = ligne_.conditionnement_entree;
				end if;
			elsif(ligne_.type_doc='TR')then
				if(mouvement_='S') then
					operation_='Reconditionnement';
				else
					select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot, calcul_pr from yvs_com_contenu_doc_stock where id = idexterne_;
					operation_='Reconditionnement';
				end if;
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



-- Function: delete_contenu_doc_stock_reception()
-- DROP FUNCTION delete_contenu_doc_stock_reception();
CREATE OR REPLACE FUNCTION delete_contenu_doc_stock_reception()
  RETURNS trigger AS
$BODY$    
DECLARE

BEGIN
	delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_contenu_doc_stock_reception()
  OWNER TO postgres;


-- Trigger: delete_ on yvs_com_contenu_doc_stock_reception
-- DROP TRIGGER delete_ ON yvs_com_contenu_doc_stock_reception;
CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_com_contenu_doc_stock_reception
  FOR EACH ROW
  EXECUTE PROCEDURE delete_contenu_doc_stock_reception();
  
  
-- Function: insert_contenu_doc_stock_reception()
-- DROP FUNCTION insert_contenu_doc_stock_reception();
CREATE OR REPLACE FUNCTION insert_contenu_doc_stock_reception()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	contenu_ record;
	tranche_ bigint;
	result boolean default false;
BEGIN
	select into contenu_ * from yvs_com_contenu_doc_stock where id = NEW.contenu;
	select into doc_ * from yvs_com_doc_stocks where id = contenu_.doc_stock;
	select into tranche_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
	
	result = (select valorisation_stock(contenu_.article, contenu_.conditionnement_entree, doc_.destination, tranche_, NEW.quantite, contenu_.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_reception));
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_stock_reception()
  OWNER TO postgres;


-- Trigger: insert_ on yvs_com_contenu_doc_stock_reception
-- DROP TRIGGER insert_ ON yvs_com_contenu_doc_stock_reception;
CREATE TRIGGER insert_
  AFTER INSERT
  ON yvs_com_contenu_doc_stock_reception
  FOR EACH ROW
  EXECUTE PROCEDURE insert_contenu_doc_stock_reception();
  
  
  
-- Function: update_contenu_doc_stock_reception()
-- DROP FUNCTION update_contenu_doc_stock_reception();
CREATE OR REPLACE FUNCTION update_contenu_doc_stock_reception()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	contenu_ record;
	ligne_ record;
	
	mouv_ bigint;
	tranche_ bigint;
	
	result boolean default false;
BEGIN
	select into contenu_ * from yvs_com_contenu_doc_stock where id = NEW.contenu;
	select into doc_ * from yvs_com_doc_stocks where id = contenu_.doc_stock;
	select into tranche_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = contenu_.article;
	--Insertion mouvement stock
	if(doc_.destination is not null)then			
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and mouvement = 'E' ORDER BY id;
		if(mouv_ is not null)then
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'E';
				result = (select valorisation_stock(contenu_.article, contenu_.conditionnement_entree, doc_.destination, tranche_, NEW.quantite, contenu_.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_reception));
			else
				UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = contenu_.prix_entree, article = contenu_.article, conditionnement = contenu_.conditionnement_entree, lot = contenu_.lot_sortie, calcul_pr = NEW.calcul_pr, date_doc = NEW.date_reception WHERE id = mouv_;
				FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'E' AND parent IS NULL ORDER BY id
				LOOP
					IF(ligne_.id != mouv_)THEN
						DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
					END IF;
				END LOOP;
			end if;
		else
			result = (select valorisation_stock(contenu_.article, contenu_.conditionnement_entree, doc_.destination, tranche_, NEW.quantite, contenu_.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_reception));
		end if;	
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_stock_reception()
  OWNER TO postgres;



-- Trigger: update_ on yvs_com_contenu_doc_stock_reception
-- DROP TRIGGER update_ ON yvs_com_contenu_doc_stock_reception;
CREATE TRIGGER update_
  BEFORE UPDATE
  ON yvs_com_contenu_doc_stock_reception
  FOR EACH ROW
  EXECUTE PROCEDURE update_contenu_doc_stock_reception();  
  
  
-- Function: insert_contenu_doc_stock()
-- DROP FUNCTION insert_contenu_doc_stock();
CREATE OR REPLACE FUNCTION insert_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	trancheD_ bigint;
	trancheS_ bigint;
	result boolean default false;
BEGIN
	select into doc_ * from yvs_com_doc_stocks where id = NEW.doc_stock;
	select into trancheD_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = doc_.creneau_source;
	
	if(doc_.statut = 'V' or doc_.statut = 'U' or doc_.statut = 'R') then
		if(doc_.type_doc = 'FT') then
			--Insertion mouvement stock		
			if(doc_.source is not null and (doc_.statut = 'U' or doc_.statut = 'V' or doc_.statut = 'R'))then
				result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'SS') then
			if(doc_.source is not null)then
				result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'ES') then
			IF(doc_.destination is not null) THEN
				result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			END IF;
		elsif(doc_.type_doc = 'IN') then
			if(doc_.source is not null)then
				if(NEW.quantite>0)then
					result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				elsif(NEW.quantite<0)then
					result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;
			end if;
		elsif(doc_.type_doc = 'TR') then
			if(doc_.source is not null)then
				result = (select valorisation_stock(NEW.article, NEW.conditionnement,doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				result = (select valorisation_stock(NEW.article, NEW.conditionnement_entree,doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_stock()
  OWNER TO postgres;



-- Function: update_contenu_doc_stock()
-- DROP FUNCTION update_contenu_doc_stock();
CREATE OR REPLACE FUNCTION update_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	entree_ record;
	sortie_ record;
	ligne_ record;
	
	mouv_ bigint;
	trancheD_ bigint;
	trancheS_ bigint;
	
	result boolean default false;
BEGIN
	select into doc_ * from yvs_com_doc_stocks where id = NEW.doc_stock;
	select into trancheD_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = doc_.creneau_source;
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	--Insertion mouvement stock
	if(doc_.statut = 'V' or doc_.statut = 'U' or doc_.statut = 'R') then
		if(doc_.type_doc = 'FT') then	
			if(doc_.source is not null and (doc_.statut = 'U' or doc_.statut = 'V' or doc_.statut = 'R'))then
				select into mouv_ id from yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME and depot = doc_.source and mouvement = 'S' ORDER BY id;				
				if(coalesce(mouv_, 0) > 0)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'S';
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, article = NEW.article, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr, date_doc=doc_.date_doc WHERE id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'S' AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;	
			end if;	
		elsif(doc_.type_doc = 'SS') then
			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
						result = (select valorisation_stock(NEW.article, NEW.conditionnement,doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, article = NEW.article, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr , date_doc=doc_.date_doc WHERE id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					result = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;	
			end if;
		elsif(doc_.type_doc = 'ES') then
			IF(doc_.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination AND mouvement = 'E';
				IF(mouv_ is not null)THEN
						IF(arts_.methode_val = 'FIFO')then
							delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = NEW.article and mouvement = 'E';
							result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
						ELSE
							UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, article = NEW.article, conditionnement = NEW.conditionnement_entree, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr , date_doc=doc_.date_doc WHERE id = mouv_;
							FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
						END IF;
				ELSE
					result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				END IF;	
			END IF;
		elsif(doc_.type_doc = 'IN') then
			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
						if(NEW.quantite>0)then
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						elsif(NEW.quantite<0)then
							result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
						end if;
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, article = NEW.article, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr , date_doc=doc_.date_doc WHERE id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					if(NEW.quantite>0)then
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					elsif(NEW.quantite<0)then
						result = (select valorisation_stock(NEW.article, NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					end if;
				end if;	
			end if;
		elsif(doc_.type_doc = 'TR') then
			if(doc_.source is not null)then
				select into entree_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement;
				select into sortie_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement_entree;

				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
						result = (select valorisation_stock(NEW.article,NEW.conditionnement,  doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree,  doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, article = NEW.article, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr, date_doc=doc_.date_doc
													    WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite_entree, cout_entree = NEW.prix_entree, article = NEW.article, conditionnement = NEW.conditionnement_entree, lot = NEW.lot_entree, calcul_pr = NEW.calcul_pr, date_doc=doc_.date_doc 
														WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'E';
					end if;
				else
					result = (select valorisation_stock(NEW.article,NEW.conditionnement,  doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree,  doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;	
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_stock()
  OWNER TO postgres;



-- Function: update_doc_stocks()
-- DROP FUNCTION update_doc_stocks();
CREATE OR REPLACE FUNCTION update_doc_stocks()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	entree_ record;
	sortie_ record;
	trancheD_ bigint;
	trancheS_ bigint;
	result boolean default false;
BEGIN
	select into trancheD_ tranche from yvs_com_creneau_depot where id = NEW.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = NEW.creneau_source;	
	if(NEW.type_doc = 'FT' and (NEW.statut = 'U' or NEW.statut = 'V' or NEW.statut = 'R')) then
		FOR cont_ in select id, article , quantite as qte, quantite_entree, prix, prix_entree, conditionnement_entree, conditionnement, date_reception FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock				
			IF(NEW.source is not null and (NEW.statut = 'U' or NEW.statut = 'V')) THEN       --traitement de la sortie dans le dépôt source
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
				result = (select valorisation_stock(cont_.article, cont_.conditionnement, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
			END IF;
		end loop;
	ELSIF(NEW.type_doc = 'FT' and NEW.statut != 'V' and NEW.statut != 'U' and NEW.statut != 'R') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;		
	ELSIF(NEW.type_doc = 'SS' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				end if;
				result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.source, trancheS_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
			end if;
		end loop;
	ELSIF(NEW.type_doc = 'SS' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock				
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'S'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	ELSIF(NEW.type_doc = 'ES' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.destination and article = cont_.article and mouvement = 'E';
				end if;
				result = (select valorisation_stock(cont_.article,cont_.conditionnement_entree, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
			end if;
		end loop;
	ELSIF(NEW.type_doc = 'ES' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and mouvement = 'E'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	ELSIF(NEW.type_doc = 'IN' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite as qte, prix, conditionnement_entree, conditionnement FROM yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			--Insertion mouvement stock
			if(NEW.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
				end if;
				if(cont_.qte>0)then
					result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				elsif(cont_.qte<0)then
					result = (select valorisation_stock(cont_.article, cont_.conditionnement_entree, NEW.destination, trancheD_, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				end if;
			end if;
		end loop;
	ELSIF(NEW.type_doc = 'IN' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	ELSIF(NEW.type_doc = 'TR' and NEW.statut = 'V') then
		for cont_ in select id, article , quantite, quantite_entree as resultante, prix, prix_entree ,conditionnement as entree, conditionnement_entree as sortie, calcul_pr from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			select into entree_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.entree;
			select into sortie_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.sortie;
			
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
			if(mouv_ is not null)then
				if(entree_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article;
					result = (select valorisation_stock(cont_.article,cont_.entree, NEW.source, trancheS_, cont_.quantite, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
					result = (select valorisation_stock(cont_.article,cont_.sortie, NEW.source, trancheS_, cont_.resultante, cont_.prix_entree, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
				else
					UPDATE yvs_base_mouvement_stock SET quantite = cont_.quantite, cout_entree = cont_.prix, conditionnement=cont_.sortie, calcul_pr=cont_.calcul_pr, date_doc=NEW.date_doc, tranche = trancheS_
													WHERE id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'E';
					UPDATE yvs_base_mouvement_stock SET quantite = cont_.resultante, cout_entree = cont_.prix_entree , conditionnement=cont_.entree, calcul_pr=cont_.calcul_pr, date_doc=NEW.date_doc, tranche = trancheS_
													WHERE id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock' and depot = NEW.source and article = cont_.article and mouvement = 'S';
				end if;
			else
				result = (select valorisation_stock(cont_.article,cont_.entree, NEW.source, trancheS_, cont_.quantite, cont_.prix, 'yvs_com_contenu_doc_stock', cont_.id, 'S', NEW.date_doc));
				result = (select valorisation_stock(cont_.article, cont_.sortie, NEW.source, trancheS_, cont_.resultante, cont_.prix_entree, 'yvs_com_contenu_doc_stock', cont_.id, 'E', NEW.date_doc));
			end if;	
		end loop;
	ELSIF(NEW.type_doc = 'TR' and NEW.statut != 'V') then
		for cont_ in select id, article , quantite as qte from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_stocks()
  OWNER TO postgres;




-- Function: update_()
-- DROP FUNCTION update_();
CREATE OR REPLACE FUNCTION update_()
  RETURNS boolean AS
$BODY$   
DECLARE	
	ligne_ RECORD;
	id_ BIGINT;
BEGIN
	ALTER TABLE yvs_base_mouvement_stock DISABLE TRIGGER stock_maj_prix_mvt;
	FOR ligne_  IN SELECT m.* FROM yvs_base_mouvement_stock m INNER JOIN yvs_com_contenu_doc_stock c ON m.id_externe = c.id INNER JOIN yvs_com_doc_stocks d ON c.doc_stock = d.id
		WHERE m.table_externe = 'yvs_com_contenu_doc_stock' AND m.mouvement = 'E' AND d.type_doc = 'FT'
	LOOP
		SELECT INTO id_ r.id FROM yvs_com_contenu_doc_stock_reception r WHERE r.contenu = ligne_.id_externe LIMIT 1;
		IF(COALESCE(id_, 0) > 0)THEN
			RAISE NOTICE 'mouvement : % ', ligne_.id;
			RAISE NOTICE '	reception : % ', id_;
			UPDATE yvs_base_mouvement_stock SET table_externe = 'yvs_com_contenu_doc_stock_reception', id_externe = id_ WHERE id = ligne_.id;
		END IF;
	END LOOP;
	ALTER TABLE yvs_base_mouvement_stock ENABLE TRIGGER stock_maj_prix_mvt;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_()
  OWNER TO postgres;


-- Function: com_get_versement_attendu_point(bigint, date, date)
-- DROP FUNCTION com_get_versement_attendu_point(bigint, date, date);
CREATE OR REPLACE FUNCTION com_get_versement_attendu_point(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;

	head_ RECORD;
BEGIN    
	ca_ = (select get_ca_point_vente(id_, date_debut_, date_fin_));
	FOR head_ IN SELECT h.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point p ON h.creneau_point= p.id 
		WHERE p.point = id_ AND e.date_entete BETWEEN date_debut_ AND date_fin_
	LOOP
		SELECT INTO result_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND p.caissier = head_.users AND p.date_paiement = head_.date_entete;
		RAISE NOTICE 'result_ : %',result_;
		avance_ = avance_ + COALESCE(result_, 0);
	END LOOP;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avances : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu_point(bigint, date, date)
  OWNER TO postgres;


-- Function: com_get_versement_attendu_vendeur(bigint, date, date)
-- DROP FUNCTION com_get_versement_attendu_vendeur(bigint, date, date);
CREATE OR REPLACE FUNCTION com_get_versement_attendu_vendeur(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;

	head_ RECORD;
BEGIN    
	ca_ = (select get_ca_vendeur(id_, date_debut_, date_fin_));
	FOR head_ IN SELECT h.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point p ON h.creneau_point= p.id 
		WHERE h.users = id_ AND e.date_entete BETWEEN date_debut_ AND date_fin_
	LOOP
		SELECT INTO result_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND p.caissier = head_.users AND p.date_paiement = head_.date_entete;
		RAISE NOTICE 'result_ : %',result_;
		avance_ = avance_ + COALESCE(result_, 0);
	END LOOP;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avances : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu_vendeur(bigint, date, date)
  OWNER TO postgres;



-- Function: com_et_journal_vente(bigint, bigint, date, date, boolean)
-- DROP FUNCTION com_et_journal_vente(bigint, bigint, date, date, boolean);
CREATE OR REPLACE FUNCTION com_et_journal_vente(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN by_famille_ boolean)
  RETURNS TABLE(agence bigint, users bigint, code character varying, nom character varying, classe bigint, reference character varying, designation character varying, montant double precision, is_classe boolean, is_vendeur boolean, rang integer) AS
$BODY$
declare 
   vendeur_ RECORD;
   classe_ record;
   
   journal_ character varying;
   
   valeur_ double precision default 0;
   totaux_ double precision;
   
   query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.code_users, y.nom_users as nom, y.agence FROM yvs_users y INNER JOIN yvs_agences a ON y.agence = a.id WHERE code_users IS NOT NULL';
   
begin 	
	--DROP TABLE table_et_journal_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_journal_vente(_agence BIGINT, _users BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _classe BIGINT, _reference CHARACTER VARYING, _designation CHARACTER VARYING, _montant DOUBLE PRECISION, _is_classe BOOLEAN, _is_vendeur BOOLEAN, _rang INTEGER);
	DELETE FROM table_et_journal_vente;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND y.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	FOR vendeur_ IN EXECUTE query_
	LOOP
		IF(COALESCE(by_famille_, FALSE))THEN
			-- Vente directe par famille d'article
			FOR classe_ IN SELECT s.id, UPPER(s.reference_famille) AS code, UPPER(s.designation) AS intitule, SUM(c.prix_total - c.ristourne) AS valeur FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc 
				INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article s ON a.famille = s.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND s.societe = societe_ AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_ GROUP BY s.id
			LOOP
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(classe_.valeur, 0), TRUE, TRUE, 0);
			END LOOP;
		ELSE
			-- Vente directe par classe statistique
			FOR classe_ IN SELECT s.id, UPPER(s.code_ref) AS code, UPPER(s.designation) AS intitule, SUM(c.prix_total - c.ristourne) AS valeur FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc 
				INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_classes_stat s ON a.classe1 = s.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND s.societe = societe_ AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_ GROUP BY s.id
			LOOP
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(classe_.valeur, 0), TRUE, TRUE, 0);	
			END LOOP;
			
			-- CA Des article non classé
			SELECT INTO valeur_ SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
				INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND a.classe1 IS NULL AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), TRUE, TRUE, 0);
			END IF;
		END IF;
		
		-- CA Par vendeur
		SELECT INTO valeur_ SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
			WHERE hu.users = vendeur_.id AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);	
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -1, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, TRUE, 1);
		
		-- Ristournes vente directe
		SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
			WHERE hu.users = vendeur_.id AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -2, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, TRUE, 2);
		
		-- Commandes Annulées
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'A' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -3, 'CMDE.A', 'CMDE.A', COALESCE(valeur_, 0), FALSE, TRUE, 3);
					
		-- Commandes Recu
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'V' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -4, 'CMDE.R', 'CMDE.R', COALESCE(valeur_, 0), FALSE, TRUE, 4);		
		
		-- Versement attendu	
		SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = vendeur_.id AND _rang > 0; 
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -5, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, TRUE, 5);	

		SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE y._users = vendeur_.id;
		IF(COALESCE(valeur_, 0) = 0)THEN
			DELETE FROM table_et_journal_vente WHERE _users = vendeur_.id;
		END IF;
	END LOOP;
	
	-- LIGNE DES COMMANDE SERVIES PAR CLASSE STAT
	query_ = 'SELECT SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
			WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND d.document_lie IS NOT NULL AND e.date_entete BETWEEN '''||date_debut_||''' AND '''||date_fin_||'''';
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND u.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND g.societe = '||societe_;
	END IF;
	IF(COALESCE(by_famille_, FALSE))THEN
		FOR classe_ IN SELECT c.id, UPPER(c.reference_famille) as code, UPPER(c.designation) as intitule FROM yvs_base_famille_article c WHERE c.societe = societe_
		LOOP			
			EXECUTE query_ || ' AND a.famille = '||classe_.id||')' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;
		END LOOP;
	ELSE
		FOR classe_ IN SELECT c.id, UPPER(c.code_ref) as code, UPPER(c.designation) as intitule FROM yvs_base_classes_stat c WHERE c.societe = societe_
		LOOP			
			EXECUTE query_ || ' AND a.classe1 = '||classe_.id||')' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;
		END LOOP;
		
		-- Autres aticles sans classe
		EXECUTE query_ || ' AND a.classe1 IS NULL) ' INTO valeur_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), FALSE, FALSE, 0);
		END IF;	
	END IF;
	
	-- CA sur commande reçu
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = 0; 
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -1, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, FALSE, 1);
	
	-- Ristourne sur commande reçu
	SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
		INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
		INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
		WHERE g.id = agence_ AND d.type_doc = 'BCV' AND d.statut = 'V' AND d.statut_livre = 'L' AND d.date_livraison BETWEEN date_debut_ AND date_fin_);
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -2, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, FALSE, 2);
	
	-- CMDE.A sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -3, 'CMDE.A', 'CMDE.A', 0, FALSE, FALSE, 3);
	
	-- CMDE.R sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -4, 'CMDE.R', 'CMDE.R', 0, FALSE, FALSE, 4);
	
	-- VERS.ATT sur commande reçu
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = 0 AND _rang > 0; 
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -5, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, FALSE, 5);

	FOR classe_ IN SELECT _classe, _reference, _designation, _rang, SUM(_montant) AS _valeur FROM table_et_journal_vente GROUP BY _classe, _reference, _designation, _rang
	LOOP 
		INSERT INTO table_et_journal_vente values (agence_, -1, 'TOTAUX','TOTAUX', classe_._classe, classe_._reference, classe_._designation, classe_._valeur, FALSE, FALSE, classe_._rang);
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_journal_vente ORDER BY _agence, _is_vendeur DESC, _code, _rang, _is_classe DESC, _classe;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_journal_vente(bigint, bigint, date, date, boolean)
  OWNER TO postgres;
