-- Function: compta_et_brouillard_caisse(bigint, date, date)
-- DROP FUNCTION compta_et_brouillard_caisse(bigint, date, date);
CREATE OR REPLACE FUNCTION compta_et_brouillard_caisse(IN caisse_ bigint, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(id bigint, numero character varying, date_mvt date, note character varying, tiers character varying, mode character varying, montant double precision, type character varying, solde double precision, id_externe bigint, table_externe character varying) AS
$BODY$
declare 
   ligne_ RECORD;
   solde_initial DOUBLE PRECISION DEFAULT 0;
   solde_ DOUBLE PRECISION DEFAULT 0;

   mouvement_ CHARACTER VARYING DEFAULT 'DEPENSE'; 
   description_ CHARACTER VARYING; 
   query_ CHARACTER VARYING DEFAULT 'SELECT y.*, m.designation FROM yvs_compta_mouvement_caisse y LEFT JOIN yvs_base_mode_reglement m ON y.model = m.id WHERE y.statut_piece = ''P'' AND m.type_reglement = ''ESPECE'' AND y.table_externe NOT IN (''NOTIF_ACHAT'', ''NOTIF_VENTE'') AND 
					(y.table_externe NOT IN (''DOC_VENTE'',''DOC_ACHAT'') OR (y.table_externe = ''DOC_VENTE'' AND y.id_externe NOT IN (SELECT DISTINCT(a.piece_vente) FROM yvs_compta_notif_reglement_vente a)) 
							OR (y.table_externe = ''DOC_ACHAT'' AND y.id_externe NOT IN (SELECT DISTINCT(a.piece_achat) FROM yvs_compta_notif_reglement_achat a)))';
   
begin 	
	--DROP TABLE table_et_brouillard_caisse;
	CREATE TEMP TABLE IF NOT EXISTS table_et_brouillard_caisse(_id bigint, _numero character varying, _date_mvt date, _note character varying, _tiers character varying, _mode character varying, _montant double precision, _type character varying, _solde double precision, _id_externe bigint, _table_externe character varying);
	DELETE FROM table_et_brouillard_caisse;
	IF(caisse_ IS NOT NULL AND caisse_ > 0)THEN
		query_ = query_ || ' AND y.caisse = '||caisse_;
		query_ = query_ || ' AND y.date_paye BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
		solde_initial = (SELECT compta_total_caisse(0, caisse_, 0, '', '', 'ESPECE', 'P', (date_debut_ - interval '1 day')::date));
		solde_ = solde_initial;
		INSERT INTO table_et_brouillard_caisse VALUES(0, 'S.I', (date_debut_ - interval '1 day'), 'SOLDE INITIAL', '', 'ESPECE', 0, '', solde_, 0, 0);
		FOR ligne_ IN EXECUTE query_ || ' ORDER BY y.date_paye, y.numero, y.id'
		LOOP
			description_ = ligne_.note;
			IF(description_ IS NULL OR description_ IN ('', ' '))THEN
				IF(ligne_.table_externe = 'DOC_ACHAT')THEN
					description_ = 'Reglement Achat N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'DOC_VENTE')THEN
					description_ = 'Reglement Vente N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'MISSION')THEN
					description_ = 'Reglement Mission N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'DOC_VIREMENT')THEN
					description_ = 'Virement N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'ACOMPTE_VENTE')THEN
					description_ = 'Acompte Client N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'ACOMPTE_ACHAT')THEN
					description_ = 'Acompte Fournisseur N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'CREDIT_VENTE')THEN
					description_ = 'Crédit Client N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'CREDIT_ACHAT')THEN
					description_ = 'Crédit Fournisseur N° '||ligne_.reference_externe;
				END IF;
			END IF;
			IF(ligne_.mouvement = 'R')THEN
				mouvement_ = 'RECETTE';
				solde_ = solde_ + ligne_.montant;
			ELSE
				mouvement_ = 'DEPENSE';
				solde_ = solde_ - ligne_.montant;
			END IF;
			INSERT INTO table_et_brouillard_caisse VALUES(ligne_.id, ligne_.numero, ligne_.date_paye, description_, ligne_.name_tiers, ligne_.designation, ligne_.montant, mouvement_, solde_, ligne_.id_externe, ligne_.table_externe);
		END LOOP;
	END IF;
	RETURN QUERY SELECT * FROM table_et_brouillard_caisse ORDER BY _date_mvt, _numero;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_brouillard_caisse(bigint, date, date)
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
			for cont_ in select id, article , quantite_recu as qte, pua_recu as prix, conditionnement, calcul_pr FROM yvs_com_contenu_doc_achat where doc_achat = NEW.id
			loop
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;					
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and article = cont_.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and article = cont_.article;
						if(NEW.type_doc = 'BLA')then
							result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_livraison));
						else
							result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_livraison));
						end if;
					else
						UPDATE yvs_base_mouvement_stock SET quantite = cont_.qte, cout_entree = cont_.prix , conditionnement=cont_.conditionnement , calcul_pr=cont_.calcul_pr, tranche = NEW.tranche WHERE id = mouv_;
					end if;
				else
					if(NEW.type_doc = 'BLA')then
						result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'E', OLD.date_livraison));
					else
						result = (select valorisation_stock(cont_.article, cont_.conditionnement,NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_livraison));
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
	select into doc_ id, type_doc, date_livraison, depot_reception as depot, tranche, statut from yvs_com_doc_achats where id = NEW.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			if( doc_.type_doc = 'BLA')then
				result = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu, NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
			else
				result = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu, NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
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
	ligne_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, type_doc, date_livraison, depot_reception as depot, tranche, statut, date_livraison from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'E';
					if(doc_.type_doc = 'BLA')then
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
					else
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
					end if;
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_recu, cout_entree = NEW.pua_recu, conditionnement = NEW.conditionnement, lot = NEW.lot, calcul_pr = NEW.calcul_pr, date_doc = doc_.date_livraison, tranche = doc_.tranche where id = mouv_;
					FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
					LOOP
						IF(ligne_.id != mouv_)THEN
							DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
						END IF;
					END LOOP;
				end if;
			else
				if(doc_.type_doc = 'BLA')then			
					result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
				else
					result = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
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
  
  
-- Function: warning(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint)
-- DROP FUNCTION warning(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint);
CREATE OR REPLACE FUNCTION warning(IN users_ bigint, IN depot_ bigint, IN point_ bigint, IN equipe_ bigint, IN service_ bigint, IN agence_ bigint, IN societe_ bigint, IN niveau_ bigint)
  RETURNS TABLE(element character varying, valeur integer, model character varying, date_debut date) AS
$BODY$
DECLARE
	lect_ record;
	model_ record;

	date_debut_ date;
	
	compteur_ integer default 0;
	ecart_ integer default 0;
	current_ bigint default 0;
	employe_ bigint default 0;

	acces_ boolean default false;

	type_ character varying;

BEGIN
	DROP TABLE IF EXISTS table_warning;
	CREATE TEMP TABLE IF NOT EXISTS table_warning(elt character varying, nbr integer, mod character varying, date_ date);
	if(societe_ is not null and societe_ > 0)then
		SELECT INTO current_ COALESCE(users, 0) FROM yvs_users_agence WHERE id = users_;
		SELECT INTO employe_ COALESCE(id, 0) FROM yvs_grh_employes WHERE code_users = current_;
		SELECT INTO ecart_ COALESCE(ecart_document, 0) FROM yvs_societes WHERE id = societe_;
		for model_ in select w.titre_doc as model, w.description as titre, COALESCE(a.ecart, ecart_) as ecart from yvs_workflow_model_doc w left join yvs_warning_model_doc a on a.model = w.id and a.societe = societe_
		loop
			compteur_ = 0;
			type_ = '';
			IF(model_.model = 'MISSIONS')THEN
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAllScte';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C', 'A','S','A') AND a.societe = societe_;
					SELECT INTO date_debut_ m.date_mission FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C', 'A','S','A') AND a.societe = societe_ ORDER BY m.date_mission LIMIT 1;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAgence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C', 'A','S','A') AND a.id = agence_;
					ELSE
						-- voir seulement les dossiers des employés de son service
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheDepartement';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C', 'A','S','A') AND (p.departement = service_ OR p.departement IN (SELECT grh_get_sous_service(service_, true)));
							SELECT INTO date_debut_ m.date_mission FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C', 'A','S','A') AND (p.departement = service_ OR p.departement IN (SELECT grh_get_sous_service(service_, true))) ORDER BY m.date_mission LIMIT 1;
						ELSE
							-- voir seulement les dossiers des employés de son equipe
							select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheEquipe';
							IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
								
							ELSE
								-- voir seulement ses dossiers propre
								SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_missions m WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C', 'A', 'S', 'A') AND m.author = users_;
								SELECT INTO date_debut_ m.date_mission FROM yvs_grh_missions m WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C', 'A', 'S', 'A') AND m.author = users_ ORDER BY m.date_mission LIMIT 1;
							END IF;
						END IF;
					END IF;
				END IF;				
			ELSIF(model_.model = 'FORMATIONS')THEN
				
			ELSIF(model_.model = 'CONGES')THEN
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAllScte';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE (((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')) AND m.nature = 'C' AND a.societe = societe_;
					SELECT INTO date_debut_ m.date_conge FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE (((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')) AND m.nature = 'C' AND a.societe = societe_ ORDER BY m.date_conge LIMIT 1;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAgence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'C' AND a.id = agence_;
						SELECT INTO date_debut_ m.date_conge FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'C' AND a.id = agence_ ORDER BY m.date_conge LIMIT 1;
					ELSE
						-- voir seulement les dossiers des employés de son service
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheDepartement';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'C' AND (p.departement = service_ OR p.departement IN (SELECT grh_get_sous_service(service_, true)));
							SELECT INTO date_debut_ m.date_conge FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'C' AND (p.departement = service_ OR p.departement IN (SELECT grh_get_sous_service(service_, true))) ORDER BY m.date_conge LIMIT 1;
						ELSE
							-- voir seulement les dossiers des employés de son equipe
							select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheEquipe';
							IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
								
							ELSE
								-- voir seulement ses dossiers propre
								SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'C' AND m.author = users_;
								SELECT INTO date_debut_ m.date_conge FROM yvs_grh_conge_emps m WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'C' AND m.author = users_ ORDER BY m.date_conge LIMIT 1;
							END IF;
						END IF;
					END IF;
				END IF;
			ELSIF(model_.model = 'FACTURE_ACHAT' or model_.model = 'RETOUR_ACHAT' or model_.model = 'AVOIR_ACHAT')THEN
				IF(model_.model = 'FACTURE_ACHAT')THEN
					type_ = 'FA';
				ELSIF(model_.model = 'RETOUR_ACHAT')THEN
					type_ = 'BRA';
				ELSE
					type_ = 'FAA';
				END IF;
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_all_doc';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.societe = societe_;
					SELECT INTO date_debut_ m.date_doc FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.societe = societe_ ORDER BY m.date_doc LIMIT 1;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_only_doc_agence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.id = agence_;
						SELECT INTO date_debut_ m.date_doc FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.id = agence_ ORDER BY m.date_doc LIMIT 1;
					ELSE
						-- voir seulement les dossiers des employés de son depot
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_only_doc_depot';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_achats m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ 
								AND (m.depot_reception IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_)));
							SELECT INTO date_debut_ m.date_doc FROM yvs_com_doc_achats m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ 
								AND (m.depot_reception IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_))) ORDER BY m.date_doc LIMIT 1;
						ELSE
							-- voir seulement ses dossiers propre
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_achats m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND m.author = users_;
							SELECT INTO date_debut_ m.date_doc FROM yvs_com_doc_achats m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND m.author = users_ ORDER BY m.date_doc LIMIT 1;
						END IF;
					END IF;
				END IF;	
			ELSIF(model_.model = 'FACTURE_VENTE' or model_.model = 'RETOUR_VENTE' or model_.model = 'AVOIR_VENTE')THEN
				IF(model_.model = 'FACTURE_VENTE')THEN
					type_ = 'FV';
				ELSIF(model_.model = 'RETOUR_VENTE')THEN
					type_ = 'BRV';
				ELSE
					type_ = 'FAV';
				END IF;
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fv_view_all_doc';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id INNER JOIN yvs_base_point_vente p ON c.point = p.id INNER JOIN yvs_agences a ON p.agence = a.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.societe = societe_;
					SELECT INTO date_debut_ e.date_entete FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id INNER JOIN yvs_base_point_vente p ON c.point = p.id INNER JOIN yvs_agences a ON p.agence = a.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.societe = societe_ ORDER BY e.date_entete LIMIT 1;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fv_view_only_doc_agence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id INNER JOIN yvs_base_point_vente p ON c.point = p.id INNER JOIN yvs_agences a ON p.agence = a.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.id = agence_;
						SELECT INTO date_debut_ e.date_entete FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id INNER JOIN yvs_base_point_vente p ON c.point = p.id INNER JOIN yvs_agences a ON p.agence = a.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.id = agence_ ORDER BY e.date_entete LIMIT 1;
					ELSE
						-- voir seulement les dossiers des employés de son point vente
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fv_view_only_doc_pv';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') 
								AND m.type_doc = type_ AND (c.point IN (SELECT c.point FROM yvs_com_creneau_point c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_point = c.id WHERE h.users = current_ AND h.actif AND (h.permanent OR h.date_travail = current_date)));
							SELECT INTO date_debut_ e.date_entete FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') 
								AND m.type_doc = type_ AND (c.point IN (SELECT c.point FROM yvs_com_creneau_point c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_point = c.id WHERE h.users = current_ AND h.actif AND (h.permanent OR h.date_travail = current_date))) ORDER BY e.date_entete LIMIT 1;
						ELSE
							-- voir seulement les dossiers des employés de son depot
							select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fv_view_only_doc_depot';
							IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
								SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') 
									AND m.type_doc = type_ AND (m.depot_livrer IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_)));
							SELECT INTO date_debut_ e.date_entete FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') 
									AND m.type_doc = type_ AND (m.depot_livrer IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_))) ORDER BY e.date_entete LIMIT 1;
							ELSE
								-- voir seulement ses dossiers propre
								SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND m.author = users_;
								SELECT INTO date_debut_ e.date_entete FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND m.author = users_ ORDER BY e.date_entete LIMIT 1;
							END IF;
						END IF;
					END IF;
				END IF;	
			ELSIF(model_.model = 'PERMISSION_CD')THEN
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAllScte';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE (((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')) AND m.nature = 'P' AND a.societe = societe_;
					SELECT INTO date_debut_ m.date_conge FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE (((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')) AND m.nature = 'P' AND a.societe = societe_ ORDER BY m.date_conge LIMIT 1;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAgence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'P' AND a.id = agence_;
						SELECT INTO date_debut_ m.date_conge FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'P' AND a.id = agence_ ORDER BY m.date_conge LIMIT 1;
					ELSE
						-- voir seulement les dossiers des employés de son service
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheDepartement';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'P' AND (p.departement = service_ OR p.departement IN (SELECT grh_get_sous_service(service_, true)));
							SELECT INTO date_debut_ m.date_conge FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'P' AND (p.departement = service_ OR p.departement IN (SELECT grh_get_sous_service(service_, true))) ORDER BY m.date_conge LIMIT 1;
						ELSE
							-- voir seulement les dossiers des employés de son equipe
							select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheEquipe';
							IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
								
							ELSE
								-- voir seulement ses dossiers propre
								SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'P' AND m.author = users_;
								SELECT INTO date_debut_ m.date_conge FROM yvs_grh_conge_emps m WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')  AND m.nature = 'P' AND m.author = users_ ORDER BY m.date_conge LIMIT 1;
							END IF;
						END IF;
					END IF;
				END IF;
			ELSIF(model_.model = 'SORTIE_STOCK')THEN
				type_ = 'SS';
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'd_stock_view_all_date';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots d On m.source = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.societe = societe_;
					SELECT INTO date_debut_ m.date_doc FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots d On m.source = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.societe = societe_ ORDER BY m.date_doc LIMIT 1;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'd_stock_view_all_agence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots d On m.source = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.id = agence_;
						SELECT INTO date_debut_ m.date_doc FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots d On m.source = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.id = agence_ ORDER BY m.date_doc LIMIT 1;
					ELSE
						-- voir seulement les dossiers des employés de son depot
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'd_stock_view_all_depot';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_stocks m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ 
								AND (m.source IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_)));
							SELECT INTO date_debut_ m.date_doc FROM yvs_com_doc_stocks m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ 
								AND (m.source IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_))) ORDER BY m.date_doc LIMIT 1;
						ELSE
							-- voir seulement ses dossiers propre
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_stocks m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND m.author = users_;
							SELECT INTO date_debut_ m.date_doc FROM yvs_com_doc_stocks m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND m.author = users_ ORDER BY m.date_doc LIMIT 1;
						END IF;
					END IF;
				END IF;	
			ELSIF(model_.model = 'OPERATION_DIVERS')THEN
				-- voir le dossier de tous les employés de la société
				SELECT INTO compteur_ COUNT(m.id) FROM yvs_compta_caisse_doc_divers m INNER JOIN yvs_users_agence u ON m.author = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE ((m.date_doc::date + model_.ecart) < CURRENT_DATE) AND m.statut_doc NOT IN ('V', 'T', 'C', 'A') AND a.societe = societe_;	
				SELECT INTO date_debut_ m.date_doc::date FROM yvs_compta_caisse_doc_divers m INNER JOIN yvs_users_agence u ON m.author = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE ((m.date_doc::date + model_.ecart) < CURRENT_DATE) AND m.statut_doc NOT IN ('V', 'T', 'C', 'A') AND a.societe = societe_ ORDER BY m.date_doc::date LIMIT 1;
			ELSIF(model_.model = 'BON_OPERATION_DIVERS')THEN
				-- voir le dossier de tous les employés de la société
				SELECT INTO compteur_ COUNT(m.id) FROM yvs_compta_bon_provisoire m INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((m.date_bon::date + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = societe_;
				SELECT INTO date_debut_ m.date_bon::date FROM yvs_compta_bon_provisoire m INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((m.date_bon::date + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = societe_ ORDER BY m.date_bon::date LIMIT 1;
			ELSIF(model_.model = 'APPROVISIONNEMENT')THEN
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_all_doc';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_fiche_approvisionnement m INNER JOIN yvs_base_depots d On m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C', 'A', 'A') AND m.statut_terminer != 'T' AND a.societe = societe_;
					SELECT INTO date_debut_ m.date_approvisionnement FROM yvs_com_fiche_approvisionnement m INNER JOIN yvs_base_depots d On m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C', 'A', 'A') AND m.statut_terminer != 'T' AND a.societe = societe_ ORDER BY m.date_approvisionnement LIMIT 1;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_only_doc_agence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_fiche_approvisionnement m INNER JOIN yvs_base_depots d On m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C', 'A', 'A') AND m.statut_terminer != 'T' AND a.id = agence_;
						SELECT INTO date_debut_ m.date_approvisionnement FROM yvs_com_fiche_approvisionnement m INNER JOIN yvs_base_depots d On m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C', 'A', 'A') AND m.statut_terminer != 'T' AND a.id = agence_ ORDER BY m.date_approvisionnement LIMIT 1;
					ELSE
						-- voir seulement les dossiers des employés de son depot
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_only_doc_depot';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_fiche_approvisionnement m WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C', 'A', 'A') AND m.statut_terminer != 'T'
								AND (m.depot IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_)));
							SELECT INTO date_debut_ m.date_approvisionnement FROM yvs_com_fiche_approvisionnement m WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C', 'A', 'A') AND m.statut_terminer != 'T'
								AND (m.depot IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_))) ORDER BY m.date_approvisionnement LIMIT 1;
						ELSE
							-- voir seulement ses dossiers propre
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_fiche_approvisionnement m WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C', 'A', 'A') AND m.statut_terminer != 'T' AND m.author = users_;
							SELECT INTO date_debut_ m.date_approvisionnement FROM yvs_com_fiche_approvisionnement m WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C', 'A', 'A') AND m.statut_terminer != 'T' AND m.author = users_ ORDER BY m.date_approvisionnement LIMIT 1;
						END IF;
					END IF;
				END IF;	
			ELSIF(model_.model = 'PIECE_CAISSE')THEN
				-- voir le dossier de tous les employés de la société
				SELECT INTO compteur_ COUNT(m.id) FROM yvs_compta_mouvement_caisse m LEFT JOIN yvs_base_mode_reglement y ON m.model = y.id WHERE ((m.date_paiment_prevu::date + model_.ecart) < CURRENT_DATE) AND m.statut_piece NOT IN ('P') AND m.societe = societe_
					AND (m.model IS NULL OR y.type_reglement = 'ESPECE') AND m.table_externe NOT IN ('NOTIF_VENTE', 'NOTIF_ACHAT');
				SELECT INTO date_debut_ m.date_paiment_prevu::date FROM yvs_compta_mouvement_caisse m LEFT JOIN yvs_base_mode_reglement y ON m.model = y.id WHERE ((m.date_paiment_prevu::date + model_.ecart) < CURRENT_DATE) AND m.statut_piece NOT IN ('P') AND m.societe = societe_
					AND (m.model IS NULL OR y.type_reglement = 'ESPECE') AND m.table_externe NOT IN ('NOTIF_VENTE', 'NOTIF_ACHAT') ORDER BY m.date_paiment_prevu::date LIMIT 1;
			ELSE
			
			END IF;
			if(compteur_ IS NOT NULL AND compteur_ > 0)then
				insert into table_warning values(model_.titre, compteur_, model_.model, date_debut_);
			end if;
		end loop;
	end if;
	return QUERY select * from table_warning order by elt;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION warning(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint)
  OWNER TO postgres;



-- Function: com_recalcule_pr_periode(bigint, bigint, bigint, bigint, date, date)
DROP FUNCTION com_recalcule_pr_periode(bigint, bigint, bigint, bigint, date, date);
CREATE OR REPLACE FUNCTION com_recalcule_pr_periode(societe_ bigint, agence_ bigint, depot_ bigint, article_ character varying, debut_ date, fin_ date)
  RETURNS double precision AS
$BODY$
declare 
	line_ record;

	query_ character varying default 'SELECT m.* FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE m.date_doc BETWEEN '||QUOTE_LITERAL(debut_)||' AND '|| QUOTE_LITERAL(fin_);
	ids_ character varying default '''0''';
	id_ character varying default '0';
	type_ character varying;
	
	total_ double precision default 0;
	pr_ double precision default 0;
	last_pr_ double precision default 0;
	new_cout double precision default 0;
	stock_ double precision default 0;
	prix_arr_ numeric;

	last_article_ BIGINT DEFAULT 0;
begin 
	IF(COALESCE(societe_, 0) > 0) THEN 
		query_= query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(COALESCE(agence_, 0) > 0) THEN 
		query_= query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(depot_, 0) > 0) THEN 
		query_= query_ || ' AND d.id = '||depot_;
	END IF;
	IF(COALESCE(article_, '') NOT IN ('', ' ', '0'))THEN
		FOR id_ IN SELECT art FROM regexp_split_to_table(article_,',') art WHERE CHAR_LENGTH(art) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(id_);
		END LOOP;
		query_= query_ || ' AND m.article::text IN ('||ids_||')';
	END IF;
	ALTER TABLE yvs_base_mouvement_stock DISABLE TRIGGER stock_maj_prix_mvt;
	FOR line_ IN EXECUTE query_ || ' ORDER BY m.conditionnement, m.date_doc ASC, m.mouvement'
	LOOP
		IF(last_article_ != line_.conditionnement)THEN
			stock_ = get_stock_reel(line_.article, 0, line_.depot, 0, 0, line_.date_doc - 1, line_.conditionnement, 0);
			last_pr_ = get_pr(line_.article, line_.depot, 0, line_.date_doc - 1, line_.conditionnement, line_.id);
			last_article_ = line_.conditionnement;
			pr_ = last_pr_;
		ELSE
			last_pr_ = pr_;
		END IF;
		
		IF(line_.table_externe = 'yvs_com_contenu_doc_stock')THEN
			SELECT INTO type_ d.type_doc FROM yvs_com_doc_stocks d INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = d.id WHERE c.id = line_.id_externe;
			ALTER TABLE yvs_com_contenu_doc_stock DISABLE TRIGGER update_;
			IF(type_ = 'FT' OR type_ = 'TR')THEN
				IF(line_.mouvement = 'E')THEN
					UPDATE yvs_com_contenu_doc_stock SET prix_entree = pr_ WHERE id = line_.id_externe;
				ELSE
					UPDATE yvs_com_contenu_doc_stock SET prix = pr_ WHERE id = line_.id_externe;
				END IF;
				UPDATE yvs_base_mouvement_stock SET cout_entree = pr_ WHERE id = line_.id;
				line_.cout_entree = pr_;
			ELSIF(line_.mouvement = 'S')THEN
				UPDATE yvs_com_contenu_doc_stock SET prix_entree = pr_, prix = pr_ WHERE id = line_.id_externe;
				UPDATE yvs_base_mouvement_stock SET cout_entree = pr_ WHERE id = line_.id;
				line_.cout_entree = pr_;
			END IF;
			ALTER TABLE yvs_com_contenu_doc_stock ENABLE TRIGGER update_;
		ELSIF(line_.table_externe = 'yvs_prod_of_suivi_flux')THEN
			UPDATE yvs_prod_of_suivi_flux SET cout = pr_ WHERE id = line_.id_externe;
			UPDATE yvs_base_mouvement_stock SET cout_entree = pr_ WHERE id = line_.id;
			line_.cout_entree = pr_;
		END IF;
		
		IF(line_.mouvement = 'E')THEN
			-- Retourne le nouveau cout moyen calculé
			IF(COALESCE(line_.quantite, 0) + stock_ != 0)THEN
				new_cout = COALESCE((((stock_ * COALESCE(last_pr_, 0)) + ((COALESCE(line_.quantite, 0) * COALESCE(line_.cout_entree, 0)))) / (COALESCE(line_.quantite, 0) + stock_)), 0);
				-- Arrondi les chiffres
				pr_ = round(new_cout::numeric, 3);
			ELSE
				pr_ = COALESCE(last_pr_, 0);
			END IF;
		END IF;
		IF(line_.mouvement = 'E')THEN
			stock_ = stock_ + COALESCE(line_.quantite, 0);
		ELSE
			stock_ = stock_ - COALESCE(line_.quantite, 0);
		END IF;
		UPDATE yvs_base_mouvement_stock SET cout_stock = pr_ WHERE id = line_.id;
	END LOOP;
	ALTER TABLE yvs_base_mouvement_stock ENABLE TRIGGER stock_maj_prix_mvt;
	RETURN pr_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_recalcule_pr_periode(bigint, bigint, bigint, character varying, date, date)
  OWNER TO postgres;


-- Function: equilibre_achat(bigint, boolean)
-- DROP FUNCTION equilibre_achat(bigint, boolean);
CREATE OR REPLACE FUNCTION equilibre_achat(id_ bigint, by_parent_ boolean)
  RETURNS boolean AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	av_ double precision default 0;
	ch_ bigint default 0;
	contenu_ record;
	qte_ double precision default 0;
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

	query_control character varying;
	query_content character varying;

BEGIN
	-- Equilibre de l'etat reglé
	SELECT INTO ch_ a.societe FROM yvs_com_doc_achats d INNER JOIN yvs_agences a ON d.agence = a.id WHERE d.id = id_;
	ttc_ = (select get_ttc_achat(id_));
	ttc_ = arrondi(ch_, ttc_);
	select into av_ sum(montant) from yvs_compta_caisse_piece_achat where achat = id_ and statut_piece = 'P';
	if(av_ is null)then
		av_ = 0;
	end if;

	-- Equilibre de l'etat livré
	query_control = 'select sum(c.quantite_recu) from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on c.doc_achat = d.id 
		where d.type_doc = ''BLA'' and d.statut = ''V'' and d.document_lie = '||id_;
	IF(by_parent_)THEN
		query_content = 'select id, conditionnement as unite, quantite_attendu::decimal as qte from yvs_com_contenu_doc_achat where doc_achat = '||id_;
	ELSE
		query_content = 'select article as id, conditionnement as unite, sum(quantite_attendu)::decimal as qte from yvs_com_contenu_doc_achat where doc_achat = '||id_||' group by article, conditionnement';	
	END IF;
	for contenu_ in execute query_content
	loop
		in_ = true;
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
		query_content = 'select id, conditionnement_bonus as unite, coalesce(quantite_bonus, 0)::decimal as qte from yvs_com_contenu_doc_achat where doc_achat = '||id_||' and coalesce(quantite_bonus, 0) > 0';
	ELSE
		query_content = 'select article_bonus as id, conditionnement_bonus as unite, sum(quantite_bonus)::decimal as qte from yvs_com_contenu_doc_achat where doc_achat = '||id_||' and coalesce(quantite_bonus, 0) > 0 group by article_bonus, conditionnement_bonus';
	END IF;
	for contenu_ in execute query_content
	loop
		in_ = true;
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
	RAISE NOTICE 'in_ %',in_;
	if(in_)then
		select into ch_ count(y.id) from yvs_compta_caisse_piece_achat y inner join yvs_base_mode_reglement m on y.model = m.id where y.achat = id_ and m.type_reglement = 'BANQUE';
		if(av_ > 0 and av_>=ttc_)then
			update yvs_com_doc_achats set statut_regle = 'P' where id = id_;
		elsif (av_ > 0 or ch_ > 0) then
			update yvs_com_doc_achats set statut_regle = 'R' where id = id_;
		else
			update yvs_com_doc_achats set statut_regle = 'W' where id = id_;
		end if;
		
		RAISE NOTICE 'correct % ',correct;
		if(correct)then
			update yvs_com_doc_achats set statut_livre = 'L' where id = id_;
		else
			RAISE NOTICE 'encours % ',encours;
			if(encours)then
				update yvs_com_doc_achats set statut_livre = 'R' where id = id_;
			elsif(by_parent_)then
				update yvs_com_doc_achats set statut_livre = 'W' where id = id_;
			end if;
			IF(by_parent_)then
				PERFORM equilibre_achat(id_, false);
			end if;
		end if;	
	else
		update yvs_com_doc_achats set statut_regle = 'W', statut_livre = 'W' where id = id_;
	end if;	
	update yvs_workflow_valid_facture_achat set date_update = date_update where facture_achat = id_;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_achat(bigint, boolean)
  OWNER TO postgres;
