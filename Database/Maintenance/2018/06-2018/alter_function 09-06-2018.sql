-- Function: update_doc_ventes()
-- DROP FUNCTION update_doc_ventes();

CREATE OR REPLACE FUNCTION update_doc_ventes()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	dep_ record;
	result boolean default false;
BEGIN
	if(NEW.type_doc = 'BLV' or NEW.type_doc = 'BRV') then
		if(NEW.statut = 'V') then
			select into dep_ e.date_entete as date_doc from yvs_com_entete_doc_vente e where e.id = NEW.entete_doc;
			for cont_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop
-- 				RAISE NOTICE 'cont_ : %',cont_.id; 
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;
				
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article;
						if(NEW.type_doc = 'BLV')then
							result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', NEW.date_livraison));
						else
							result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', NEW.date_livraison));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = (cont_.qte), cout_entree = cont_.prix where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente' and depot = NEW.depot_livrer and tranche = NEW.tranche_livrer and article = cont_.article;
					end if;
				else
					if(NEW.type_doc = 'BLV')then
						result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'S', NEW.date_livraison));
					else
						result = (select valorisation_stock(cont_.article, NEW.depot_livrer, NEW.tranche_livrer, (cont_.qte), 0, 'yvs_com_contenu_doc_vente', cont_.id, 'E', NEW.date_livraison));
					end if;
				end if;	
			end loop;
		elsif(NEW.statut != 'V')then
			for cont_ in select id from yvs_com_contenu_doc_vente where doc_vente = OLD.id
			loop				
				--Recherche mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente';
				if(mouv_ is not null)then
					delete from yvs_base_mouvement_stock where id = mouv_;
				end if;
			end loop;
		end if;
	end if;
	if(NEW.type_doc = 'FV' AND NEW.statut_livre = 'L') then
		for cont_ in select id, article , quantite as qte, quantite_bonus as bonus ,prix, id_reservation from yvs_com_contenu_doc_vente where doc_vente = NEW.id
		    LOOP
			--change le statut de la reservation
			IF(cont_.id_reservation IS NOT NULL) THEN
				UPDATE yvs_com_reservation_stock SET statut='T' WHERE id=cont_.id_reservation AND statut='V';
			END IF;
			
		    END LOOP;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ventes()
  OWNER TO postgres;

  
-- Function: delete_doc_ventes()
-- DROP FUNCTION delete_doc_ventes();

CREATE OR REPLACE FUNCTION delete_doc_ventes()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	dep_ bigint;
BEGIN
	if((OLD.type_doc = 'BRV' or OLD.type_doc = 'BLV') and OLD.statut = 'V') then
		for cont_ in select id from yvs_com_contenu_doc_vente where doc_vente = OLD.id
		loop
			
			--Recherche mouvement stock
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_vente';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_doc_ventes()
  OWNER TO postgres;

  
-- Function: insert_contenu_doc_vente()
-- DROP FUNCTION insert_contenu_doc_vente();

CREATE OR REPLACE FUNCTION insert_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	dep_ record;
	arts_ record;
	result boolean default false;
BEGIN
	select into doc_ id, type_doc, statut, entete_doc, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'BRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			if(doc_.depot_livrer is not null)then
				if(doc_.type_doc = 'BLV')then
					result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
				else
					result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
				end if;
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_vente()
  OWNER TO postgres;

  
-- Function: update_contenu_doc_vente()
-- DROP FUNCTION update_contenu_doc_vente();

CREATE OR REPLACE FUNCTION update_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	dep_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'BRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article and mouvement = 'S';
						if(doc_.type_doc = 'BLV')then
							result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
						else
							result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = (NEW.quantite), cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot where id = mouv_;
					end if;
				else
					if(doc_.type_doc = 'BLV')then
						result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
					else
						result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
					end if;
				end if;	
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_vente()
  OWNER TO postgres;

  
-- Function: get_cout_sup_achat(bigint)
-- DROP FUNCTION get_cout_sup_achat(bigint);

CREATE OR REPLACE FUNCTION get_cout_sup_achat(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	cs_m double precision default 0;
	cs_p double precision default 0;

BEGIN
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_achat o inner join yvs_grh_type_cout t on o.type_cout = t.id  where doc_achat = id_ and t.augmentation is true;
	if(cs_p is null)then
		cs_p = 0;
	end if;
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_achat o inner join yvs_grh_type_cout t on o.type_cout = t.id  where doc_achat = id_ and t.augmentation is false;
	if(cs_m is null)then
		cs_m = 0;
	end if;
	total_ = cs_p - cs_m;
	if(total_ is null)then
		total_ = 0;
	end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_cout_sup_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_cout_sup_achat(bigint) IS 'retourne le cout supplementaire de la facture (service ou non)';

-- Function: com_et_objectif(bigint, bigint, character varying, character varying)
-- DROP FUNCTION com_et_objectif(bigint, bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN objectif_ bigint, IN periodes_ character varying, IN type_ character varying)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	periode_ BIGINT;
	i_ INTEGER DEFAULT 0;
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif;	
	IF(periodes_ IS NULL OR periodes_ IN ('', ' '))THEN
		FOR periode_ IN SELECT p.id FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
		LOOP
			i_ = i_ + 1;
			INSERT INTO table_objectif SELECT * FROM com_et_objectif_by_periode(periode_, objectif_, type_, i_);
		END LOOP;
	ELSE
		FOR periode_ IN SELECT p.id FROM yvs_com_periode_objectif p WHERE p.societe = societe_ AND p.id::character varying in (SELECT val from regexp_split_to_table(periodes_,',') val) ORDER BY p.date_debut
		LOOP
			i_ = i_ + 1;
			INSERT INTO table_objectif SELECT * FROM com_et_objectif_by_periode(periode_, objectif_, type_, i_);
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_objectif ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, bigint, character varying, character varying)
  OWNER TO postgres;

-- Function: com_et_objectif_by_periode(bigint, bigint, character varying, integer)
DROP FUNCTION com_et_objectif_by_periode(bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif_by_periode(IN periode_ bigint, IN objectif_ bigint, IN type_ character varying, IN rang_ integer)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	ligne_ RECORD;
	valeur_ DOUBLE PRECISION DEFAULT 0;

	somme_ DOUBLE PRECISION DEFAULT 0;
	attente_ DOUBLE PRECISION DEFAULT 0;
	entete_ CHARACTER VARYING;
    
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif_by_periode;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif_by_periode(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif_by_periode;
	SELECT INTO entete_ p.code_ref FROM yvs_com_periode_objectif p WHERE p.id = periode_;
	IF(type_ = 'P')THEN
		FOR ligne_ IN SELECT y.id, y.code, y.libelle AS nom FROM yvs_base_point_vente y
		LOOP
			SELECT INTO attente_ COALESCE(o.valeur, 0) FROM yvs_com_objectifs_point_vente o WHERE o.point_vente = ligne_.id AND o.periode = periode_ AND o.objectif = objectif_;
			IF(attente_ IS NULL OR attente_ < 0)THEN
				SELECT INTO attente_ SUM(COALESCE(o.valeur, 0)) FROM yvs_com_objectifs_comercial o INNER JOIN yvs_com_comerciale c ON o.commercial = c.id INNER JOIN yvs_com_creneau_horaire_users ch ON c.utilisateur = ch.users INNER JOIN yvs_com_creneau_point cp ON ch.creneau_point = cp.id
				WHERE o.periode = periode_ AND o.objectif = objectif_ AND cp.point = ligne_.id;
			END IF;
			attente_ = COALESCE(attente_ , 0);
			valeur_ = COALESCE((SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_)), 0);
			IF(attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom, periode_, entete_, attente_, valeur_, rang_);
			END IF;
		END LOOP;		
	ELSIF(type_ = 'A')THEN
		FOR ligne_ IN SELECT y.id, y.codeagence AS code, y.designation AS nom FROM yvs_agences y 
		LOOP
			SELECT INTO attente_ COALESCE(o.valeur, 0) FROM yvs_com_objectifs_agence o WHERE o.agence = ligne_.id AND o.periode = periode_ AND o.objectif = objectif_;
			IF(attente_ IS NULL OR attente_ < 0)THEN
				SELECT INTO attente_ SUM(COALESCE(o.valeur, 0)) FROM yvs_com_objectifs_comercial o INNER JOIN yvs_com_comerciale y ON o.commercial = y.id WHERE o.periode = periode_ AND o.objectif = objectif_ AND y.agence = ligne_.id;
			END IF;
			attente_ = COALESCE(attente_ , 0);
			valeur_ = COALESCE((SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_)), 0);
			IF(attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom, periode_, entete_, attente_, valeur_, rang_);
			END IF;
		END LOOP;
	ELSE
		FOR ligne_ IN SELECT c.id, c.code_ref AS code, c.nom, c.prenom, COALESCE(o.valeur, 0) AS attente FROM yvs_com_comerciale c INNER JOIN  yvs_com_objectifs_comercial o ON o.commercial = c.id WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			attente_ = COALESCE(ligne_.attente, 0);
			valeur_ = COALESCE((SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_)), 0);
			IF(valeur_ != 0 OR attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom ||' '||ligne_.prenom, periode_, entete_, attente_, valeur_, rang_);
			END IF;	
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_objectif_by_periode ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif_by_periode(bigint, bigint, character varying, integer)
  OWNER TO postgres;

-- Function: com_get_valeur_objectif(bigint, bigint, bigint, character varying)
-- DROP FUNCTION com_get_valeur_objectif(bigint, bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION com_get_valeur_objectif(id_ bigint, periode_ bigint, objectif_ bigint, type_ character varying)
  RETURNS double precision AS
$BODY$
DECLARE 
	objectif RECORD;
	periode RECORD;

	colonne CHARACTER VARYING DEFAULT '*';
	condition CHARACTER VARYING DEFAULT '';
	query CHARACTER VARYING DEFAULT '';
	
	valeur DOUBLE PRECISION DEFAULT 0;
	
	count_article BIGINT DEFAULT 0;
	count_unite BIGINT DEFAULT 0;
	count_client BIGINT DEFAULT 0;
	count_zone BIGINT DEFAULT 0;
    
BEGIN
	-- Recuperation des informations de la periode
	SELECT INTO periode * FROM yvs_com_periode_objectif WHERE id = periode_;
	-- Recuperation des informations de l'objectif
	SELECT INTO objectif * FROM yvs_com_modele_objectif WHERE id = objectif_;

	-- Verification si l'objectif porte sur les articles
	SELECT INTO count_article COALESCE(count(e.id), 0) FROM yvs_com_cible_objectif e WHERE e.objectif = objectif_ AND table_externe = 'BASE_ARTICLE';
	IF(count_article > 0)THEN
		condition = ' AND (c.article in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''BASE_ARTICLE'' AND o.id = '||objectif_||'))';
	END IF;
	-- Verification si l'objectif porte sur les conditionnements d'article
	SELECT INTO count_unite COALESCE(count(e.id), 0) FROM yvs_com_cible_objectif e WHERE e.objectif = objectif_ AND table_externe = 'BASE_CONDITIONNEMENT';
	IF(count_unite > 0)THEN
		condition = condition ||' AND (c.conditionnement in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''BASE_CONDITIONNEMENT'' AND o.id = '||objectif_||'))';
	END IF;
	-- Verification si l'objectif porte sur les clients
	SELECT INTO count_client COALESCE(count(e.id), 0) FROM yvs_com_cible_objectif e WHERE e.objectif = objectif_ AND table_externe = 'BASE_CLIENT';
	IF(count_client > 0)THEN
		condition = condition ||' AND (d.client in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''BASE_CLIENT'' AND o.id = '||objectif_||'))';
	END IF;
	-- Verification si l'objectif porte sur les zones
	SELECT INTO count_zone COALESCE(count(e.id), 0) FROM yvs_com_cible_objectif e WHERE e.objectif = objectif_ AND table_externe = 'DOC_ZONE';
	IF(count_zone > 0)THEN
		condition = condition ||' AND ((d.adresse in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''DOC_ZONE'' AND o.id = '||objectif_||')) 
					OR (d.adresse in (SELECT f.id FROM yvs_dictionnaire f WHERE f.parent in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''DOC_ZONE'' AND o.id = '||objectif_||'))))';
	END IF;

	query = 'SELECT * FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.id = c.doc_vente INNER JOIN yvs_com_entete_doc_vente t ON d.entete_doc = t.id
			INNER JOIN yvs_com_creneau_horaire_users u ON t.creneau = u.id INNER JOIN yvs_com_creneau_point s ON u.creneau_point = s.id
			INNER JOIN yvs_base_point_vente p ON s.point = p.id INNER JOIN yvs_com_commercial_vente y ON y.facture = d.id
		WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND t.date_entete BETWEEN '||QUOTE_LITERAL(periode.date_debut)||' AND '||QUOTE_LITERAL(periode.date_fin)||' ';	
	IF(type_ IS NULL OR type_ IN ('', ' '))THEN
		query = query || 'AND y.commercial = '||id_;
	ELSIF(type_ = 'A')THEN
		query = query || 'AND p.agence = '||id_;
	ELSE
		query = query || 'AND p.id = '||id_;
	END IF;
	query = query || condition;
	CASE objectif.indicateur
		WHEN 'CA' THEN 	
			colonne = 'COALESCE(sum(c.prix_total), 0)';
		WHEN 'QUANTITE' THEN 
			colonne = 'COALESCE(sum(c.quantite), 0)';
		WHEN 'MARGE' THEN 
			colonne = 'sum(c.id)';
		ELSE
			colonne = 'sum(c.id)';
	END CASE;
	query = (select replace(query, '*', colonne));
	RAISE NOTICE 'query % ',query;
	execute query INTO valeur;

	return COALESCE(valeur, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_valeur_objectif(bigint, bigint, bigint, character varying)
  OWNER TO postgres;
  
  
-- Function: com_update_all_data_for_client(bigint)
-- DROP FUNCTION com_update_all_data_for_client(bigint);
CREATE OR REPLACE FUNCTION com_update_all_data_for_client(client_ bigint)
  RETURNS boolean AS
$BODY$   
DECLARE 
	vente_ RECORD;
	commercial_ RECORD;
BEGIN
	FOR commercial_ IN SELECT y.id, y.tiers, e.compte_tiers FROM yvs_com_comerciale y INNER JOIN yvs_grh_employes e ON e.code_users = y.utilisateur WHERE y.tiers IS NULL
	LOOP
		IF(commercial_.tiers IS NULL OR commercial_.tiers < 1)THEN
			UPDATE yvs_com_comerciale SET tiers = commercial_.compte_tiers WHERE id = commercial_.id;
		END IF;
	END LOOP;
	FOR vente_ IN SELECT y.id, c.users, l.tiers, l.suivi_comptable, h.point, p.commission_for FROM yvs_com_doc_ventes y INNER JOIN yvs_com_client l ON y.client = l.id INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id INNER JOIN yvs_com_creneau_point h ON h.id = c.creneau_point INNER JOIN yvs_base_point_vente p ON h.point = p.id
	WHERE y.client = client_ AND y.type_doc in ('FV','FAV') AND y.id NOT IN (SELECT w.ref_externe FROM yvs_compta_content_journal w WHERE w.table_externe = 'DOC_VENTE')
	LOOP
		SELECT INTO commercial_ y.id, c.tiers FROM yvs_com_commercial_vente y INNER JOIN yvs_com_comerciale c ON y.commercial = c.id WHERE y.facture = vente_.id AND y.responsable IS TRUE;
		IF(commercial_.id IS NULL OR commercial_.id < 1)THEN
			SELECT INTO commercial_ y.id, c.tiers FROM yvs_com_commercial_vente y INNER JOIN yvs_com_comerciale c ON y.commercial = c.id WHERE y.facture = vente_.id AND c.utilisateur = vente_.users;
			IF(commercial_.id IS NULL OR commercial_.id < 1)THEN
				IF(vente_.commission_for = 'C')THEN					
					SELECT INTO commercial_ y.id, y.tiers FROM yvs_com_comerciale y WHERE y.utilisateur = vente_.users;
					IF(commercial_.id IS NOT NULL OR commercial_.id > 0)THEN
						INSERT INTO yvs_com_commercial_vente(commercial, facture, taux, responsable) VALUES(commercial_.id, vente_.id, 100, true);
					END IF;
				ELSE
					SELECT INTO commercial_ y.id, y.tiers FROM yvs_com_comerciale y INNER JOIN yvs_com_commercial_point c ON c.commercial = y.id WHERE c.point = vente_.point LIMIT 1;
					IF(commercial_.id IS NOT NULL OR commercial_.id > 0)THEN
						INSERT INTO yvs_com_commercial_vente(commercial, facture, taux, responsable) VALUES(commercial_.id, vente_.id, 100, true);
					END IF;
				END IF;
			END IF;
		END IF;
		IF(vente_.suivi_comptable IS TRUE)THEN
			UPDATE yvs_com_doc_ventes SET tiers = vente_.tiers WHERE id = vente_.id;
		ELSIF(commercial_.tiers IS NOT NULL OR commercial_.tiers > 0)THEN
			UPDATE yvs_com_doc_ventes SET tiers = commercial_.tiers WHERE id = vente_.id;
		ELSE
			SELECT INTO commercial_ y.id, c.tiers FROM yvs_com_commercial_vente y INNER JOIN yvs_com_comerciale c ON y.commercial = c.id WHERE y.facture = vente_.id AND y.responsable IS TRUE;
-- 			RAISE NOTICE 'commercial_.tiers %',commercial_.tiers;
			IF(commercial_.tiers IS NOT NULL OR commercial_.tiers > 0)THEN
				UPDATE yvs_com_doc_ventes SET tiers = commercial_.tiers WHERE id = vente_.id;
			END IF;
		END IF;
	END LOOP;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_update_all_data_for_client(bigint)
  OWNER TO postgres;


