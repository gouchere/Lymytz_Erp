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
			select into ligne_ qualite, conditionnement from yvs_com_contenu_doc_vente where id = idexterne_;
			operation_='Vente';
		WHEN 'yvs_com_contenu_doc_achat' THEN	
			select into ligne_ qualite, conditionnement from yvs_com_contenu_doc_achat where id = idexterne_;
			operation_='Achat';
		WHEN 'yvs_com_ration' THEN	
			select into ligne_ qualite, conditionnement from yvs_com_ration where id = idexterne_;
			operation_='Ration';
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			select into ligne_ d.type_doc, c.qualite, c.conditionnement FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
			else
				if(mouvement_='S') then
					operation_='Sortie';
				else
					operation_='Entrée';
				end if;
			end if;	
		WHEN 'yvs_com_reconditionnement' THEN	
			if(mouvement_='S') then
				select into ligne_ qualite_source as qualite, unite_source as conditionnement from yvs_com_reconditionnement where id = idexterne_;
				operation_='Sortie';
			else
				select into ligne_ qualite_destination as qualite, unite_destination as conditionnement from yvs_com_reconditionnement where id = idexterne_;
				operation_='Entrée';
			end if;
		ELSE
		  RETURN -1;
	END CASE;
	if(parent_ is not null)then
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, parent_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, parent_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement);
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement, qualite, conditionnement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement, qualite, conditionnement)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement);
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date) IS 'Insert une ligne de sortie de mouvement de stock';


-- Function: com_commission(bigint, bigint)

-- DROP FUNCTION com_commission(bigint, bigint);

CREATE OR REPLACE FUNCTION com_commission(IN plan_ bigint, IN vente_ bigint)
  RETURNS TABLE(vente bigint, contenu bigint, facteur bigint, taux double precision, cible character varying, nature character varying) AS
$BODY$
DECLARE 
	taux_ DOUBLE PRECISION DEFAULT 0;
	total_ DOUBLE PRECISION DEFAULT 0;
	remise_ DOUBLE PRECISION DEFAULT 0;
	marge_ DOUBLE PRECISION DEFAULT 0;
	
	facteur_ RECORD;
	type_ RECORD;
	client_ RECORD;
	element_ RECORD;
	tranche_ RECORD;
	ligne_ RECORD;
	
	contenu_ BIGINT DEFAULT 0;

	evalue_ BOOLEAN DEFAULT FALSE;
	verifier_ BOOLEAN DEFAULT FALSE;
	lier_ BOOLEAN DEFAULT FALSE;

	cible_ CHARACTER VARYING DEFAULT '';
	nature_ CHARACTER VARYING DEFAULT 'T';
	
BEGIN 	
-- 	DROP TABLE IF EXISTS table_com_commission;
	CREATE TEMP TABLE IF NOT EXISTS table_com_commission(_vente BIGINT, _contenu BIGINT, _facteur BIGINT, _taux DOUBLE PRECISION, _cible CHARACTER VARYING, _nature CHARACTER VARYING); 
	DELETE FROM table_com_commission;
	FOR facteur_ IN SELECT * FROM yvs_com_facteur_taux f LEFT JOIN yvs_com_periode_validite_commision p ON f.id = p.facteur WHERE f.plan = plan_ 
		AND (f.permanent IS TRUE OR (f.permanent IS FALSE AND ((p.periodicite = 'J' AND current_date <= p.date_fin ) OR (p.periodicite = 'A' AND p.date_debut <= current_date) OR (p.periodicite = 'D' AND current_date BETWEEN p.date_debut AND p.date_fin))))
	LOOP
		taux_ = 0;
		cible_ = '';
		nature_ = 'T';
		contenu_ = 0;
		lier_ = FALSE;
		evalue_ = FALSE;
		verifier_ = FALSE;
		
		SELECT INTO type_ * FROM yvs_com_type_grille t WHERE t.id = facteur_.type_grille;
		IF(facteur_.general)THEN -- Verification si le facteur est général donc n'est rattaché à aucun element
			total_ = (SELECT get_ttc_vente(vente_));
			remise_ = (SELECT get_remise_vente(vente_));
			SELECT INTO client_ c.id, COALESCE(c.date_entete, CURRENT_DATE) date_creation FROM yvs_com_entete_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.entete_doc = c.id 
				INNER JOIN  yvs_com_doc_ventes y ON y.client = d.client
			WHERE y.id = vente_ ORDER BY c.date_entete LIMIT 1;
			IF(facteur_.nouveau_client)THEN -- Verification si le facteur est lié aux nouveaux clients
				IF(current_date - 30 <= client_.date_creation)THEN   -- Verification si le client est un nouveau client
					verifier_ = TRUE;
				END IF;
			ELSE
				verifier_ = TRUE;
			END IF;
			IF(verifier_)THEN			
				IF(type_.id IS NULL OR type_.id < 1)THEN
					taux_ = COALESCE(facteur_.taux, 0);
				ELSE
					cible_ = type_.cible;
					CASE cible_
						WHEN 'C' THEN
							SELECT INTO tranche_ t.id, COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type_grille AND (t.tranche_minimal <= total_ AND t.tranche_maximal >= total_);
						WHEN 'R' THEN
							IF(remise_ > 0)THEN
								remise_ = (remise_ / (total_ + remise_));
							END IF;
							SELECT INTO tranche_ t.id,COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type_grille AND (t.tranche_minimal <= remise_ AND t.tranche_maximal >= remise_);
						WHEN 'M' THEN
							SELECT INTO tranche_ t.id,COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type_grille AND (t.tranche_minimal <= marge_ AND t.tranche_maximal >= marge_);
					END CASE;
					IF(tranche_.id IS NOT NULL AND tranche_.id >0)THEN
						taux_ = tranche_.taux;
						nature_ = tranche_.nature;
					END IF;
				END IF;
				INSERT INTO table_com_commission VALUES(vente_, contenu_, facteur_.id, taux_, cible_, nature_);
			END IF;
		ELSE
			SELECT INTO client_ c.id, COALESCE(c.date_entete, CURRENT_DATE) date_creation FROM yvs_com_entete_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.entete_doc = c.id 
				INNER JOIN  yvs_com_doc_ventes y ON y.client = d.client
			WHERE y.id = vente_ ORDER BY c.date_entete LIMIT 1;
			IF(facteur_.nouveau_client)THEN -- Verification si le facteur est lié aux nouveaux clients
				IF(current_date - 30 <= client_.date_creation)THEN   -- Verification si le client est un nouveau client
					verifier_ = TRUE;
				END IF;
			ELSE
				verifier_ = TRUE;
			END IF;
			IF(verifier_)THEN
				FOR ligne_ IN SELECT y.* FROM yvs_com_cible_facteur_taux y WHERE y.facteur = facteur_.id
				LOOP
					lier_ = FALSE;
					evalue_ = FALSE;
					evalue_ = FALSE;
					CASE ligne_.table_externe
						WHEN 'BASE_ARTICLE' THEN
							SELECT INTO element_ c.id, COALESCE(c.prix_total, 0) AS prix_total, COALESCE(c.remise, 0) AS remise FROM yvs_com_contenu_doc_vente c WHERE c.doc_vente = vente_ AND c.article = ligne_.id_externe;
							IF(element_.id IS NOT NULL AND element_.id > 0)THEN	-- Verification si la facture est liée à l'article du facteur	
								total_ = element_.prix_total;
								remise_ = element_.remise;
								evalue_ = TRUE;	
							END IF;	
							lier_ = TRUE;
						WHEN 'DOC_ZONE'THEN
							SELECT INTO element_ c.client FROM yvs_com_doc_ventes c WHERE c.adresse = ligne_.id_externe OR (c.adresse in (SELECT y.id FROM yvs_dictionnaire y WHERE y.parent = ligne_.id_externe));
							IF(element_.client IS NOT NULL AND element_.client > 0)THEN -- Verification si le client est de la catégorie du facteur
								total_ = (SELECT get_ttc_vente(vente_));
								remise_ = (SELECT get_remise_vente(vente_));
								evalue_ = TRUE;	
							END IF;
						WHEN 'BASE_CLIENT' THEN
							IF(client_.id = ligne_.id_externe)THEN -- Verification si le client est de la catégorie du facteur
								total_ = (SELECT get_ttc_vente(vente_));
								remise_ = (SELECT get_remise_vente(vente_));
								evalue_ = TRUE;	
							END IF;
						ELSE
							SELECT INTO element_ c.client FROM yvs_com_categorie_tarifaire c WHERE c.categorie = ligne_.id_externe AND c.client = client_.id;
							IF(element_.client IS NOT NULL AND element_.client > 0)THEN -- Verification si le client est de la catégorie du facteur
								total_ = (SELECT get_ttc_vente(vente_));
								remise_ = (SELECT get_remise_vente(vente_));
								evalue_ = TRUE;	
							END IF;
					END CASE;
					IF(evalue_)THEN
						IF(type_.id IS NULL OR type_.id < 1)THEN
							taux_ = COALESCE(facteur_.taux, 0);
						ELSE
							cible_ = type_.cible;
							CASE cible_
								WHEN 'C' THEN
									SELECT INTO tranche_ t.id, COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type_grille AND (t.tranche_minimal <= total_ AND t.tranche_maximal >= total_);
								WHEN 'R' THEN
									IF(remise_ > 0)THEN
										remise_ = (remise_ / (total_ + remise_));
									END IF;
									SELECT INTO tranche_ t.id,COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type_grille AND (t.tranche_minimal <= remise_ AND t.tranche_maximal >= remise_);
								WHEN 'M' THEN
									SELECT INTO tranche_ t.id,COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type_grille AND (t.tranche_minimal <= marge_ AND t.tranche_maximal >= marge_);
							END CASE;
							IF(tranche_.id IS NOT NULL AND tranche_.id >0)THEN
								taux_ = tranche_.taux;
								nature_ = tranche_.nature;
							END IF;
						END IF;
						IF(lier_)THEN
							FOR contenu_ IN SELECT c.id FROM yvs_com_contenu_doc_vente c WHERE c.doc_vente = vente_ AND c.article = ligne_.id_externe
							LOOP
								INSERT INTO table_com_commission VALUES(vente_, contenu_, facteur_.id, taux_, cible_, nature_);
							END LOOP;
						ELSE
							INSERT INTO table_com_commission VALUES(vente_, contenu_, facteur_.id, taux_, cible_, nature_);
						END IF;
					END IF;
				END LOOP;
			END IF;
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM table_com_commission ORDER BY _vente;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_commission(bigint, bigint)
  OWNER TO postgres;
  
  -- Function: delete_ration()

-- DROP FUNCTION delete_ration();

CREATE OR REPLACE FUNCTION delete_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	mouv_ bigint;
BEGIN
	select into doc_ id, statut, depot from yvs_com_doc_ration where id = OLD.fiche;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_ration()
  OWNER TO postgres;

CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_com_ration
  FOR EACH ROW
  EXECUTE PROCEDURE delete_ration();
  
  -- Function: insert_ration()

-- DROP FUNCTION insert_ration();

CREATE OR REPLACE FUNCTION insert_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	entree_ record;
	sortie_ record;
	arts_ record;
	result boolean default false;
BEGIN
	select into doc_ id, statut, depot from yvs_com_doc_ration where id = NEW.fiche;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		if(doc_.depot is not null)then
			result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_ration()
  OWNER TO postgres;

CREATE TRIGGER insert_
  AFTER INSERT
  ON yvs_com_ration
  FOR EACH ROW
  EXECUTE PROCEDURE insert_ration();
  
  -- Function: update_ration()

-- DROP FUNCTION update_ration();

CREATE OR REPLACE FUNCTION update_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	article_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, statut, depot from yvs_com_doc_ration where id = NEW.fiche;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		select into article_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement;
		if(doc_.depot is not null)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = article_.prix, conditionnement = NEW.unite_source where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'S';
				end if;
			else
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_ration()
  OWNER TO postgres;

CREATE TRIGGER update_
  BEFORE UPDATE
  ON yvs_com_ration
  FOR EACH ROW
  EXECUTE PROCEDURE update_ration();
  
  -- Function: delete_doc_ration()

-- DROP FUNCTION delete_doc_ration();

CREATE OR REPLACE FUNCTION delete_doc_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
BEGIN
	if(OLD.statut = 'V') then
		for cont_ in select id from yvs_com_reconditionnement where fiche = OLD.id
		loop			
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_ration';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_doc_ration()
  OWNER TO postgres;
	
CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_com_doc_ration
  FOR EACH ROW
  EXECUTE PROCEDURE delete_doc_ration();
  
  -- Function: update_doc_ration()

-- DROP FUNCTION update_doc_ration();

CREATE OR REPLACE FUNCTION update_doc_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	article_ record;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		for cont_ in select id, article , quantite, conditionnement, date_ration from yvs_com_ration where fiche = OLD.id
		loop
			select into article_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.conditionnement;
			
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_ration' and depot = NEW.depot and article = cont_.article;
			if(mouv_ is not null)then
				if(entree_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_ration' and depot = NEW.depot and article = cont_.article;
					result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_com_ration', cont_.id, 'S', cont_.date_ration));
				else
					update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = entree_.prix where id_externe = cont_.id and table_externe = 'yvs_com_ration' and depot = NEW.depot and article = cont_.article and mouvement = 'S';
				end if;
			else
				result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_com_ration', cont_.id, 'S', cont_.date_ration));
			end if;	
		end loop;
	elsif(NEW.statut != 'V')then
		for cont_ in select id from yvs_com_ration where fiche = OLD.id
		loop				
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_ration';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ration()
  OWNER TO postgres;

CREATE TRIGGER update_
  BEFORE UPDATE
  ON yvs_com_doc_ration
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_ration();
  
  -- Function: insert_ration()

-- DROP FUNCTION insert_ration();

CREATE OR REPLACE FUNCTION insert_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	entree_ record;
	sortie_ record;
	arts_ record;
	result boolean default false;
BEGIN
	select into doc_ id, statut, depot from yvs_com_doc_ration where id = NEW.doc_ration;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		if(doc_.depot is not null)then
			result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_ration()
  OWNER TO postgres;
  
  -- Function: update_ration()

-- DROP FUNCTION update_ration();

CREATE OR REPLACE FUNCTION update_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	article_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, statut, depot from yvs_com_doc_ration where id = NEW.doc_ration;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		select into article_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement;
		if(doc_.depot is not null)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article;
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = article_.prix, conditionnement = NEW.unite_source where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'S';
				end if;
			else
					result = (select valorisation_stock(NEW.article, doc_.depot, 0, NEW.quantite, article_.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_ration));
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_ration()
  OWNER TO postgres;

  
  -- Function: delete_ration()

-- DROP FUNCTION delete_ration();

CREATE OR REPLACE FUNCTION delete_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	mouv_ bigint;
BEGIN
	select into doc_ id, statut, depot from yvs_com_doc_ration where id = OLD.doc_ration;
	--Insertion mouvement stock
	if(doc_.statut = 'V')then
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_ration()
  OWNER TO postgres;

  -- Function: delete_doc_ration()

-- DROP FUNCTION delete_doc_ration();

CREATE OR REPLACE FUNCTION delete_doc_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
BEGIN
	if(OLD.statut = 'V') then
		for cont_ in select id from yvs_com_ration where doc_ration = OLD.id
		loop			
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_ration';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_doc_ration()
  OWNER TO postgres;

  -- Function: update_doc_ration()

-- DROP FUNCTION update_doc_ration();

CREATE OR REPLACE FUNCTION update_doc_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	article_ record;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		for cont_ in select id, article , quantite, conditionnement, date_ration from yvs_com_ration where doc_ration = OLD.id
		loop
			select into article_ a.id, a.methode_val, u.prix from yvs_base_articles a inner join yvs_base_conditionnement u on u.article = a.id where a.id = cont_.article and u.id = cont_.conditionnement;
			
			--Insertion mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_ration' and depot = NEW.depot and article = cont_.article;
			if(mouv_ is not null)then
				if(entree_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_ration' and depot = NEW.depot and article = cont_.article;
					result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_com_ration', cont_.id, 'S', cont_.date_ration));
				else
					update yvs_base_mouvement_stock set quantite = cont_.quantite, cout_entree = entree_.prix where id_externe = cont_.id and table_externe = 'yvs_com_ration' and depot = NEW.depot and article = cont_.article and mouvement = 'S';
				end if;
			else
				result = (select valorisation_stock(cont_.article, NEW.depot, 0, cont_.quantite, article_.prix, 'yvs_com_ration', cont_.id, 'S', cont_.date_ration));
			end if;	
		end loop;
	elsif(NEW.statut != 'V')then
		for cont_ in select id from yvs_com_ration where doc_ration = OLD.id
		loop				
			--Recherche mouvement stock
			delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_ration';
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ration()
  OWNER TO postgres;


  
 -- Function: com_commission(BIGINT, BIGINT)

-- DROP FUNCTION com_commission(BIGINT, BIGINT);

CREATE OR REPLACE FUNCTION com_calcul_commission(IN periode_ BIGINT)
  RETURNS TABLE(vente BIGINT) AS
$BODY$
DECLARE 	
	commercial_ RECORD;
	commission_ RECORD;
	facture_ RECORD;
	facteur_ RECORD;
	ligne_ RECORD;

	vente_ BIGINT DEFAULT 0;
	
	date_ DATE DEFAULT CURRENT_DATE;

	somme_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;
BEGIN 	
-- 	DROP TABLE IF EXISTS table_calcul_commission;
	CREATE TEMP TABLE IF NOT EXISTS table_calcul_commission(_vente BIGINT); 
	DELETE FROM table_calcul_commission;

	-- Recuperation des informations de la periode
	SELECT INTO ligne_ y.* FROM yvs_com_periode_objectif y WHERE y.id = periode_;
	IF(ligne_.id IS NOT NULL AND ligne_.id > 0)THEN
		-- Recherche des factures enregistrées dans la période
		FOR facture_ IN SELECT y.* FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id WHERE y.statut NOT IN ('E','A') AND e.date_entete BETWEEN ligne_.date_debut AND ligne_.date_fin
		LOOP
			valeur_ = 0;
			vente_ = 0;
			somme_ = 0;
			-- Vérification si la facture est rattachée à un commercial responsable
			SELECT INTO commercial_ y.* FROM yvs_com_comerciale y INNER JOIN yvs_com_commercial_vente c ON c.commercial = y.id WHERE c.facture = facture_.id AND c.responsable;
			IF(commercial_.id IS NOT NULL AND commercial_.id > 0)THEN
				-- Recuperation des taux de commission de la facture calculées pour le plan de commission du responsable
				FOR commission_ IN SELECT * FROM com_commission(COALESCE(commercial_.commission, 0), facture_.id)
				LOOP					
					-- Recuperation des informations du facteur de taux
					SELECT INTO facteur_ y.* FROM yvs_com_facteur_taux y WHERE y.id = commission_.facteur;
					IF(facteur_.champ_application = 'R')THEN -- Facteur de taux appliqué sur les factures reglées
						SELECT INTO date_ COALESCE(y.date_paiement, CURRENT_DATE) FROM yvs_compta_caisse_piece_vente y WHERE p.statut_piece = 'P' AND vente = facture_.id ORDER BY date_paiement DESC LIMIT 1;
						IF(date_ BETWEEN ligne_.date_debut AND ligne_.date_fin)THEN
							vente_ = facture_.id;
						END IF;
					ELSE -- Facteur de taux appliqué sur les factures validées
						IF(facture_.date_valider BETWEEN ligne_.date_debut AND ligne_.date_fin)THEN
							vente_ = facture_.id;
						END IF;
					END IF;
					IF(vente_ > 0)THEN
						IF(commission_.nature = 'T')THEN
							IF(commission_.contenu IS NOT NULL AND commission_.contenu > 0)THEN
								CASE facteur_.base
									WHEN 'H' THEN -- Facteur de taux basé sur le valeur HT
										SELECT INTO somme_ ((COALESCE(y.quantite, 0) * (COALESCE(y.prix, 0) - COALESCE(y.rabais, 0))- COALESCE(y.remise, 0))) FROM yvs_com_contenu_doc_vente y WHERE id = commission_.contenu;
									WHEN 'T' THEN -- Facteur de taux basé sur le valeur TTC
										SELECT INTO somme_ (COALESCE(y.prix_total, 0)) FROM yvs_com_contenu_doc_vente y WHERE id = commission_.contenu;
									WHEN 'M' THEN -- Facteur de taux basé sur le valeur Marge
										SELECT INTO somme_ (COALESCE(y.prix_total, 0) - ((COALESCE(y.prix, 0) - COALESCE(y.pr, 0))* COALESCE(y.quantite, 0))) FROM yvs_com_contenu_doc_vente y WHERE id = commission_.contenu;
								END CASE;
								somme_ = (somme_ * commission_.taux) / 100;
								UPDATE yvs_com_contenu_doc_vente SET comission = somme_ WHERE id = commission_.contenu;
							ELSE
								CASE facteur_.base
									WHEN 'H' THEN -- Facteur de taux basé sur le valeur HT
										SELECT INTO somme_ (SUM(COALESCE(y.quantite, 0) * (COALESCE(y.prix, 0) - COALESCE(y.rabais, 0))- COALESCE(y.remise, 0))) FROM yvs_com_contenu_doc_vente y WHERE doc_vente = vente_;
									WHEN 'T' THEN -- Facteur de taux basé sur le valeur TTC
										SELECT INTO somme_ (SUM(COALESCE(y.prix_total, 0))) FROM yvs_com_contenu_doc_vente y WHERE doc_vente = vente_;
									WHEN 'M' THEN -- Facteur de taux basé sur le valeur Marge
										SELECT INTO somme_ (SUM(COALESCE(y.prix_total, 0) - ((COALESCE(y.prix, 0) - COALESCE(y.pr, 0))* COALESCE(y.quantite, 0)))) FROM yvs_com_contenu_doc_vente y WHERE doc_vente = vente_;
								END CASE;
								somme_ = (somme_ * commission_.taux) / 100;
							END IF;
							valeur_ = valeur_ + somme_;
						ELSE
							IF(commission_.contenu IS NOT NULL AND commission_.contenu > 0)THEN
								UPDATE yvs_com_contenu_doc_vente SET comission = commission_.taux WHERE id = commission_.contenu;
							END IF;
							valeur_ = valeur_ + commission_.taux;
						END IF;
						UPDATE yvs_com_doc_ventes SET commision = valeur_ WHERE id = vente_;
					END IF;
				END LOOP;
				IF(valeur_ > 0)THEN
					INSERT INTO table_calcul_commission VALUES(vente_);
				END IF;
			END IF;
		END LOOP; 
	END IF;	
	RETURN QUERY SELECT * FROM table_calcul_commission ORDER BY _vente;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_calcul_commission(BIGINT)
  OWNER TO postgres;


