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
				FOR ligne_ IN SELECT y.id, y.quantite, y.date_reception FROM yvs_com_contenu_doc_stock_reception y WHERE contenu = NEW.id
				LOOP
					for mouv_ in select id from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock_reception'
					loop
						delete from yvs_base_mouvement_stock where id = mouv_;
					end loop;
					result = (select valorisation_stock(NEW.article, NEW.conditionnement_entree, doc_.destination, trancheD_, ligne_.quantite, NEW.prix_entree, 'yvs_com_contenu_doc_stock_reception', ligne_.id, 'E', ligne_.date_reception));
				END LOOP;
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
