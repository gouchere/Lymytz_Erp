UPDATE yvs_workflow_model_doc SET defined_livraison = TRUE WHERE id = 5;
UPDATE yvs_com_periode_ration SET abbreviation = reference_periode;
UPDATE yvs_grh_ordre_calcul_salaire SET abbreviation = reference;

DROP FUNCTION get_cout_achat_contenu(bigint);

DROP FUNCTION grh_et_masse_salariale(bigint, character varying);

CREATE OR REPLACE FUNCTION grh_et_masse_salariale(IN societe_ bigint, IN agence_ character varying, IN periode_ character varying)
  RETURNS TABLE(element bigint, code character varying, periode bigint, entete character varying, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	element_ RECORD;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	somme_ DOUBLE PRECISION DEFAULT 0;
	i INTEGER DEFAULT 0;
	ordre_ RECORD;
BEGIN
-- 	DROP TABLE IF EXISTS table_masse_salariale;
	CREATE TEMP TABLE IF NOT EXISTS table_masse_salariale(_element bigint, _code character varying, _periode bigint, _entete character varying, _valeur double precision, _rang integer); 
	DELETE FROM table_masse_salariale;
	FOR element_ IN SELECT y.id, y.nom, y.retenue FROM yvs_grh_element_salaire y INNER JOIN yvs_grh_categorie_element c ON y.categorie = c.id WHERE y.visible_bulletin IS TRUE AND c.societe = societe_ ORDER BY y.nom
	LOOP
		i = 0;
		somme_ = 0;
		FOR ordre_ IN SELECT o.id, o.abbreviation AS code FROM yvs_grh_ordre_calcul_salaire o WHERE o.societe = societe_ AND o.id::character varying in (SELECT val from regexp_split_to_table(periode_,',') val) ORDER BY o.debut_mois 
		LOOP
			i = i + 1;
			valeur_ = 0;
			if(element_.retenue is true)then
				IF(agence_ IS NOT NULL AND agence_ != '')THEN
					SELECT INTO valeur_ SUM(coalesce(d.retenu_salariale, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id and y.agence::character varying in (SELECT val from regexp_split_to_table(agence_,',') val);
				ELSE
					SELECT INTO valeur_ SUM(coalesce(d.retenu_salariale, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id;
				END IF;

				somme_ = somme_ + coalesce(valeur_, 0);
				valeur_ = -coalesce(valeur_, 0);
			elsif(element_.retenue is false)then
				IF(agence_ IS NOT NULL AND agence_ != '')THEN
					SELECT INTO valeur_ SUM(coalesce(d.montant_payer, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id and y.agence::character varying in (SELECT val from regexp_split_to_table(agence_,',') val);
				ELSE
					SELECT INTO valeur_ SUM(coalesce(d.montant_payer, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id;
				END IF;
				
				somme_ = somme_ + coalesce(valeur_, 0);
			end if;
			insert INTO table_masse_salariale values(element_.id, element_.nom, ordre_.id, ordre_.code, coalesce(valeur_, 0), i);
		END LOOP;
		IF(coalesce(somme_, 0) = 0)THEN
			delete from table_masse_salariale where _element = element_.id;
		END IF;
	end loop;
	FOR ordre_ IN SELECT o._periode AS periode, SUM(o._valeur) AS somme FROM table_masse_salariale o GROUP BY o._periode
	LOOP
		IF(ordre_.somme = 0)THEN
			delete from table_masse_salariale where _periode = ordre_.periode;
		END IF;
	END LOOP;
	return QUERY SELECT * FROM table_masse_salariale ORDER BY _code, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_masse_salariale(bigint, character varying, character varying)
  OWNER TO postgres;
  
  -- Function: insert_contenu_doc_achat()

-- DROP FUNCTION insert_contenu_doc_achat();

CREATE OR REPLACE FUNCTION insert_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	result boolean default false;
BEGIN
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut from yvs_com_doc_achats where id = NEW.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			if( doc_.type_doc = 'BLA')then
				result = (select valorisation_stock(NEW.article, doc_.depot, doc_.tranche, NEW.quantite_attendu, NEW.pua_attendu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			else
				result = (select valorisation_stock(NEW.article, doc_.depot, doc_.tranche, NEW.quantite_attendu, NEW.pua_attendu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_achat()
  OWNER TO postgres;
-- Function: update_contenu_doc_achat()

-- DROP FUNCTION update_contenu_doc_achat();

CREATE OR REPLACE FUNCTION update_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = OLD.article and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = OLD.article and mouvement = 'E';
					if(doc_.type_doc = 'BLA')then
						result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  NEW.pua_attendu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  NEW.pua_attendu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					end if;
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_attendu, cout_entree = NEW.pua_attendu, conditionnement = NEW.conditionnement where id = mouv_;
				end if;
			else
				if(doc_.type_doc = 'BLA')then			
					result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  NEW.pua_attendu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					result = (select valorisation_stock(OLD.article, doc_.depot, doc_.tranche, NEW.quantite_attendu,  NEW.pua_attendu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_achat()
  OWNER TO postgres;

  
  -- Function: update_doc_achats()

-- DROP FUNCTION update_doc_achats();

CREATE OR REPLACE FUNCTION update_doc_achats()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
	depot_ bigint;
	tranche_ bigint;
BEGIN
	if(NEW.type_doc = 'FRA' or NEW.type_doc = 'BLA')then
			if(NEW.statut = 'V')then
				for cont_ in select id, article , quantite_attendu as qte, pua_attendu as prix from yvs_com_contenu_doc_achat where doc_achat = OLD.id
				loop
					select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;					
					--Insertion mouvement stock
					select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and article = cont_.article;
					if(mouv_ is not null)then
						if(arts_.methode_val = 'FIFO')then
							delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and article = cont_.article;
							if(NEW.type_doc = 'BLA')then
								result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
							else
								result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
							end if;
						else
							update yvs_base_mouvement_stock set quantite = cont_.qte, cout_entree = cont_.prix where id = mouv_;
						end if;
					else
						if(NEW.type_doc = 'BLA')then
							result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_doc));
						else
							result = (select valorisation_stock(cont_.article, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_doc));
						end if;
					end if;	
				end loop;
			elsif(NEW.statut != 'V')then
				for cont_ in select id from yvs_com_contenu_doc_achat where doc_achat = OLD.id
				loop		
					
					--Recherche mouvement stock
					for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat'
					loop
						delete from yvs_base_mouvement_stock where id = mouv_;
					end loop;
				end loop;
			end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_achats()
  OWNER TO postgres;
  
  -- Function: compta_action_on_piece_caisse_vente()

-- DROP FUNCTION compta_action_on_piece_caisse_vente();

CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';
	echeance_ RECORD;
	vente_ BIGINT;
	solde_ double precision default 0;
	payer_ double precision default 0;
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;
		
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, dv.num_doc,cu.users, dv.client, cl.nom, cl.prenom  FROM yvs_compta_caisse_piece_vente pv INNER JOIN yvs_com_doc_ventes dv ON dv.id=pv.vente 
			INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc
			INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
			INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE pv.id=NEW.id; 
									    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		IF(NEW.client IS NOT NULL) THEN
			id_tiers_=NEW.client;
		ELSE
			id_tiers_=line_.client;
		END IF;
		--récupère le code tièrs de ce clients
		SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id=id_tiers_;
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
		vente_ = NEW.vente;
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=line_.num_doc, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,
				NEW.caisse, NEW.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
		END IF;	
		vente_ = NEW.vente;	           
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=OLD.id;		
		vente_ = OLD.vente;		    	 
	END IF;

	-- Mise à jour de l'échéancier
	SELECT INTO solde_ coalesce(SUM(montant), 0) FROM yvs_com_mensualite_facture_vente WHERE facture = vente_;
	SELECT INTO payer_ coalesce(SUM(montant), 0) FROM yvs_compta_caisse_piece_vente WHERE vente = vente_ AND statut_piece = 'P';
	IF(payer_ <= 0 OR solde_ <= 0)THEN
		UPDATE yvs_com_mensualite_facture_vente SET etat = 'W', avance = 0 WHERE facture = vente_ AND etat != 'W';
	ELSIF(solde_ > payer_)THEN
		WHILE(payer_ > 0)
		LOOP
			FOR echeance_ IN SELECT * FROM yvs_com_mensualite_facture_vente WHERE facture = vente_ ORDER BY date_reglement ASC
			LOOP
				IF(payer_ >= echeance_.montant)THEN
					UPDATE yvs_com_mensualite_facture_vente SET etat = 'P', avance = montant WHERE id = echeance_.id;
				ELSE
					UPDATE yvs_com_mensualite_facture_vente SET etat = 'R', avance = (montant - payer_) WHERE id = echeance_.id;
				END IF;
				payer_ = payer_ - echeance_.montant;
			END LOOP;
		END LOOP;
	ELSE
		UPDATE yvs_com_mensualite_facture_vente SET etat = 'P', avance = montant WHERE facture = vente_ AND etat != 'P';
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_vente()
  OWNER TO postgres;

  
-- Function: compta_action_on_acompte_client()

-- DROP FUNCTION compta_action_on_acompte_client();

CREATE OR REPLACE FUNCTION compta_action_on_acompte_client()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;	
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, y.num_refrence, dp.users, y.client, cl.nom, cl.prenom , em.code_users AS caissier FROM yvs_compta_acompte_client y 
			INNER JOIN yvs_com_client cl ON y.client = cl.id INNER JOIN yvs_base_caisse ca ON y.caisse = ca.id LEFT JOIN yvs_grh_employes em ON ca.responsable = em.id
			LEFT JOIN yvs_users_agence dp ON dp.id = y.author INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE y.id= NEW.id; 
			
		IF(NEW.client IS NOT NULL) THEN
			id_tiers_=NEW.client;
		ELSE
			id_tiers_=line_.client;
		END IF;
		--récupère le code tièrs de ce clients
		SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id= NEW.client;
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;
	END IF;         
	IF(action_='INSERT') THEN
	    -- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
		VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_VENTE', NEW.montant, null, NEW.date_acompte, null, NEW.date_acompte,  NEW.statut, 
				line_.societe, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_refrence, note=NEW.commentaire, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=null, date_mvt=NEW.date_acompte, date_paiment_prevu=null, date_paye=NEW.date_acompte, 
				statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='ACOMPTE_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_VENTE', NEW.montant, null, NEW.date_acompte, null, NEW.date_acompte,  NEW.statut, line_.societe,
				NEW.author,NEW.caisse, line_.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_VENTE' AND id_externe=OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_acompte_client()
  OWNER TO postgres;  
  
CREATE TRIGGER compta_action_on_acompte_client
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_acompte_client
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_acompte_client();

  -- Function: compta_action_on_notif_reglement_vente()

-- DROP FUNCTION compta_action_on_notif_reglement_vente();

CREATE OR REPLACE FUNCTION compta_action_on_notif_reglement_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;	
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_ ag.societe, dv.num_doc,cu.users, dv.client, cl.nom, cl.prenom, pv.numero_piece, pv.note, pv.montant, pv.date_piece, pv.date_paiment_prevu, pv.date_paiement, pv.statut_piece,
				ac.caisse, pv.caissier, pv.model, pv.client
			FROM yvs_compta_notif_reglement y INNER JOIN yvs_compta_acompte_client ac ON y.acompte = ac.id
			INNER JOIN yvs_compta_caisse_piece_vente pv ON y.piece_vente = pv.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = pv.vente
			INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc
			INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
			INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE y.id = NEW.id; 
		
		--récupère le code tièrs de ce clients
		SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id= line_.client;
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (line_.numero_piece, NEW.id, line_.note, 'NOTIF_VENTE', -line_.montant, line_.num_doc, line_.date_piece, line_.date_paiment_prevu, line_.date_paiement,  line_.statut_piece, 
				line_.societe, NEW.author, line_.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), line_.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='NOTIF_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=line_.numero_piece, note=line_.note, montant= -line_.montant, tiers_externe=id_tiers_, model = line_.model,
				reference_externe=line_.num_doc, date_mvt= line_.date_piece, date_paiment_prevu= line_.date_paiment_prevu, date_paye= line_.date_paiement, 
				statut_piece= line_.statut_piece, caisse= line_.caisse, caissier= line_.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='NOTIF_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (line_.numero_piece, NEW.id, line_.note, 'NOTIF_VENTE', -line_.montant, line_.num_doc, line_.date_piece, line_.date_paiment_prevu, line_.date_paiement,  line_.statut_piece, line_.societe,NEW.author,
				line_.caisse, line_.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_), line_.model);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='NOTIF_VENTE' AND id_externe=OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_notif_reglement_vente()
  OWNER TO postgres;  

  
CREATE TRIGGER compta_action_on_notif_reglement_vente
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_notif_reglement_vente
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_notif_reglement_vente();