-- Function: com_et_objectif(bigint, bigint, character varying);
-- DROP FUNCTION com_et_objectif(bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN objectif_ bigint, IN type_ character varying)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	periode_ BIGINT;
	i INTEGER DEFAULT 1;
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif;			
	FOR periode_ IN SELECT p.id FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
	LOOP
		INSERT INTO table_objectif SELECT y.element, y.code, y.nom, y.periode, y.entete, y.attente, y.valeur, i FROM com_et_objectif_by_periode(periode_, objectif_, type_) y;
		i = i + 1;
	END LOOP;
	return QUERY SELECT * FROM table_objectif ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, bigint, character varying)
  OWNER TO postgres;
  

-- Function: com_et_objectif(bigint, character varying);
-- DROP FUNCTION com_et_objectif(bigint, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN type_ character varying)
  RETURNS TABLE(indicateur character varying, objectif character varying, element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	objectif_ RECORD;

BEGIN
-- 	DROP TABLE IF EXISTS table_objectifs;
	CREATE TEMP TABLE IF NOT EXISTS table_objectifs(_indicateur character varying, _objectif character varying, _element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectifs;
	FOR objectif_ IN SELECT y.id, y.indicateur, y.description FROM yvs_com_modele_objectif y WHERE y.societe = societe_
	LOOP
		INSERT INTO table_objectifs SELECT objectif_.indicateur, objectif_.description, * FROM com_et_objectif(societe_, objectif_.id, type_);
	END LOOP;
	return QUERY SELECT * FROM table_objectifs ORDER BY _indicateur, _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, character varying)
  OWNER TO postgres;

  
-- Function: com_et_objectif(bigint, bigint, character varying, character varying);
-- DROP FUNCTION com_et_objectif(bigint, bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN objectif_ bigint, IN periodes_ character varying, IN type_ character varying)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	periode_ BIGINT;
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif;	
	IF(periodes_ IS NULL OR periodes_ IN ('', ' '))THEN
		FOR periode_ IN SELECT p.id FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
		LOOP
			INSERT INTO table_objectif SELECT * FROM com_et_objectif_by_periode(periode_, objectif_, type_);
		END LOOP;
	ELSE
		FOR periode_ IN SELECT p.id FROM yvs_com_periode_objectif p WHERE p.societe = societe_ AND p.id::character varying in (SELECT val from regexp_split_to_table(periodes_,',') val) ORDER BY p.date_debut
		LOOP
			INSERT INTO table_objectif SELECT * FROM com_et_objectif_by_periode(periode_, objectif_, type_);
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_objectif ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, bigint, character varying)
  OWNER TO postgres;
  
-- Function: com_et_objectif(bigint, character varying, character varying);
-- DROP FUNCTION com_et_objectif(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN periodes_ character varying, IN type_ character varying)
  RETURNS TABLE(indicateur character varying, objectif character varying, element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	objectif_ RECORD;

BEGIN
-- 	DROP TABLE IF EXISTS table_objectifs;
	CREATE TEMP TABLE IF NOT EXISTS table_objectifs(_indicateur character varying, _objectif character varying, _element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectifs;
	FOR objectif_ IN SELECT y.id, y.indicateur, y.description FROM yvs_com_modele_objectif y WHERE y.societe = societe_
	LOOP
		INSERT INTO table_objectifs SELECT objectif_.indicateur, objectif_.description, * FROM com_et_objectif(societe_, objectif_.id, periodes_, type_);
	END LOOP;
	return QUERY SELECT * FROM table_objectifs ORDER BY _indicateur, _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, character varying, character varying)
  OWNER TO postgres;
  

-- Function: warning(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint);
-- DROP FUNCTION warning(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION warning(IN users_ bigint, IN depot_ bigint, IN point_ bigint, IN equipe_ bigint, IN service_ bigint, IN agence_ bigint, IN societe_ bigint, IN niveau_ bigint)
  RETURNS TABLE(element character varying, valeur integer, model character varying) AS
$BODY$
DECLARE
	lect_ record;
	model_ record;
	
	compteur_ integer default 0;
	ecart_ integer default 0;
	current_ bigint default 0;
	employe_ bigint default 0;

	acces_ boolean default false;

	type_ character varying;

BEGIN
	DROP TABLE IF EXISTS table_warning;
	CREATE TEMP TABLE IF NOT EXISTS table_warning(elt character varying, nbr integer, mod character varying);
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
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C') AND a.societe = societe_;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAgence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C') AND a.id = agence_;
					ELSE
						-- voir seulement les dossiers des employés de son service
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheDepartement';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C') AND (p.departement = service_ OR p.departement IN (SELECT grh_get_sous_service(service_, true)));
						ELSE
							-- voir seulement les dossiers des employés de son equipe
							select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheEquipe';
							IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
								
							ELSE
								-- voir seulement ses dossiers propre
								SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_missions m WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C') AND m.author = users_;
							END IF;
						END IF;
					END IF;
				END IF;				
			ELSIF(model_.model = 'FORMATIONS')THEN
				
			ELSIF(model_.model = 'CONGES')THEN
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAllScte';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE (((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C')) AND m.nature = 'C' AND a.societe = societe_;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAgence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C')  AND m.nature = 'C' AND a.id = agence_;
					ELSE
						-- voir seulement les dossiers des employés de son service
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheDepartement';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C')  AND m.nature = 'C' AND (p.departement = service_ OR p.departement IN (SELECT grh_get_sous_service(service_, true)));
						ELSE
							-- voir seulement les dossiers des employés de son equipe
							select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheEquipe';
							IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
								
							ELSE
								-- voir seulement ses dossiers propre
								SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C')  AND m.nature = 'C' AND m.author = users_;
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
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ AND a.societe = societe_;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_only_doc_agence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ AND a.id = agence_;
					ELSE
						-- voir seulement les dossiers des employés de son depot
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_only_doc_depot';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_achats m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ 
								AND (m.depot_reception IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_)));
						ELSE
							-- voir seulement ses dossiers propre
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_achats m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ AND m.author = users_;
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
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id INNER JOIN yvs_base_point_vente p ON c.point = p.id INNER JOIN yvs_agences a ON p.agence = a.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ AND a.societe = societe_;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fv_view_only_doc_agence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id INNER JOIN yvs_base_point_vente p ON c.point = p.id INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ AND a.id = agence_;
					ELSE
						-- voir seulement les dossiers des employés de son point vente
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fv_view_only_doc_pv';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') 
								AND m.type_doc = type_ AND (c.point IN (SELECT c.point FROM yvs_com_creneau_point c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_point = c.id WHERE h.users = current_ AND h.actif AND (h.permanent OR h.date_travail = current_date)));
						ELSE
							-- voir seulement les dossiers des employés de son depot
							select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fv_view_only_doc_depot';
							IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
								SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') 
									AND m.type_doc = type_ AND (m.depot_livraison IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_)));
							ELSE
								-- voir seulement ses dossiers propre
								SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ AND m.author = users_;
							END IF;
						END IF;
					END IF;
				END IF;	
			ELSIF(model_.model = 'PERMISSION_CD')THEN
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAllScte';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE (((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C')) AND m.nature = 'P' AND a.societe = societe_;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheAgence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C')  AND m.nature = 'P' AND a.id = agence_;
					ELSE
						-- voir seulement les dossiers des employés de son service
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheDepartement';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON e.poste_actif = p.id WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C')  AND m.nature = 'P' AND (p.departement = service_ OR p.departement IN (SELECT grh_get_sous_service(service_, true)));
						ELSE
							-- voir seulement les dossiers des employés de son equipe
							select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'point_validFicheEquipe';
							IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
								
							ELSE
								-- voir seulement ses dossiers propre
								SELECT INTO compteur_ COUNT(m.id) FROM yvs_grh_conge_emps m WHERE ((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C')  AND m.nature = 'P' AND m.author = users_;
							END IF;
						END IF;
					END IF;
				END IF;
			ELSIF(model_.model = 'SORTIE_STOCK')THEN
				type_ = 'SS';
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'd_stock_view_all_date';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots d On m.source = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ AND a.societe = societe_;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'd_stock_view_all_agence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots d On m.source = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ AND a.id = agence_;
					ELSE
						-- voir seulement les dossiers des employés de son depot
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'd_stock_view_all_depot';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_stocks m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ 
								AND (m.source IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_)));
						ELSE
							-- voir seulement ses dossiers propre
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_stocks m WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ AND m.author = users_;
						END IF;
					END IF;
				END IF;	
			ELSIF(model_.model = 'OPERATION_DIVERS')THEN
				-- voir le dossier de tous les employés de la société
				SELECT INTO compteur_ COUNT(m.id) FROM yvs_compta_caisse_doc_divers m INNER JOIN yvs_users_agence u ON m.author = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE ((m.date_doc::date + model_.ecart) < CURRENT_DATE) AND m.statut_doc NOT IN ('V', 'T', 'C') AND a.societe = societe_;	
			ELSIF(model_.model = 'BON_OPERATION_DIVERS')THEN
				-- voir le dossier de tous les employés de la société
				SELECT INTO compteur_ COUNT(m.id) FROM yvs_compta_bon_provisoire m INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((m.date_bon::date + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND a.societe = societe_;
			ELSIF(model_.model = 'APPROVISIONNEMENT')THEN
				-- voir le dossier de tous les employés de la société
				select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_all_doc';
				IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
					SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_fiche_approvisionnement m INNER JOIN yvs_base_depots d On m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C') AND m.statut_terminer != 'T' AND a.societe = societe_;
				ELSE
					-- voir les dossiers des employés de l'agence de connexion seulement
					select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_only_doc_agence';
					IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_fiche_approvisionnement m INNER JOIN yvs_base_depots d On m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C') AND m.statut_terminer != 'T' AND a.id = agence_;
					ELSE
						-- voir seulement les dossiers des employés de son depot
						select into acces_ COALESCE(acces, false) FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE a.niveau_acces = niveau_ AND r.reference_ressource = 'fa_view_only_doc_depot';
						IF(acces_ IS NOT NULL AND acces_ IS TRUE)THEN
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_fiche_approvisionnement m WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C') AND m.statut_terminer != 'T'
								AND (m.depot IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.responsable = employe_)));
						ELSE
							-- voir seulement ses dossiers propre
							SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_fiche_approvisionnement m WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C') AND m.statut_terminer != 'T' AND m.author = users_;
						END IF;
					END IF;
				END IF;	
			ELSE
			
			END IF;
			if(compteur_ IS NOT NULL AND compteur_ > 0)then
				insert into table_warning values(model_.titre, compteur_, model_.model);
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

  
-- Function: com_et_objectif_by_periode(bigint, bigint, character varying);
-- DROP FUNCTION com_et_objectif_by_periode(bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif_by_periode(IN periode_ bigint, IN objectif_ bigint, IN type_ character varying)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	ligne_ RECORD;
	valeur_ DOUBLE PRECISION DEFAULT 0;

	i INTEGER DEFAULT 0;
	somme_ DOUBLE PRECISION DEFAULT 0;
	attente_ DOUBLE PRECISION DEFAULT 0;
	entete_ CHARACTER VARYING;
    
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif_by_periode;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif_by_periode(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif_by_periode;
	SELECT INTO entete_ p.code_ref FROM yvs_com_periode_objectif p WHERE p.id = periode_;
	i = 0;
	IF(type_ IS NULL OR type_ = '')THEN
		FOR ligne_ IN SELECT c.id, c.code_ref AS code, c.nom, c.prenom, o.valeur AS attente FROM yvs_com_comerciale c INNER JOIN yvs_com_objectifs_comercial o ON o.commercial = c.id WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			i = i + 1;
			attente_ = COALESCE(ligne_.attente , 0);
			valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_));
			valeur_ = COALESCE(valeur_ , 0);
			IF(valeur_ != 0 OR attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom ||' '||ligne_.prenom, periode_, entete_, attente_, valeur_, i);
			END IF;	
		END LOOP;
	ELSIF(type_ = 'A')THEN
		FOR ligne_ IN SELECT y.id, y.codeagence AS code, y.designation AS nom, o.valeur AS attente FROM yvs_agences y INNER JOIN yvs_com_objectifs_agence o ON o.agence = y.id WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			i = i + 1;
			attente_ = COALESCE(ligne_.attente , 0);
			IF(attente_ IS NULL OR attente_ < 0)THEN
				SELECT INTO attente_ SUM(COALESCE(o.valeur, 0)) FROM yvs_com_objectifs_comercial o INNER JOIN yvs_com_comerciale y ON o.commercial = y.id WHERE o.periode = periode_ AND o.objectif = objectif_ AND y.agence = ligne_.id;
			END IF;
			attente_ = COALESCE(ligne_.attente , 0);
			valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_));
			valeur_ = COALESCE(valeur_ , 0);
			IF(valeur_ != 0 OR attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom, periode_, entete_, attente_, valeur_, i);
			END IF;
		END LOOP;
	ELSE
		FOR ligne_ IN SELECT y.id, y.code, y.libelle AS nom, o.valeur AS attente FROM yvs_base_point_vente y INNER JOIN yvs_com_objectifs_point_vente o ON o.point_vente = y.id WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			i = i + 1;
			attente_ = COALESCE(ligne_.attente , 0);
			IF(attente_ IS NULL OR attente_ < 0)THEN
				SELECT INTO attente_ SUM(COALESCE(o.valeur, 0)) FROM yvs_com_objectifs_comercial o INNER JOIN yvs_com_comerciale c ON o.commercial = c.id INNER JOIN yvs_com_creneau_horaire_users ch ON c.utilisateur = ch.users INNER JOIN yvs_com_creneau_point cp ON ch.creneau_point = cp.id
				WHERE o.periode = periode_ AND o.objectif = objectif_ AND cp.point = ligne_.id;
			END IF;
			attente_ = COALESCE(ligne_.attente , 0);
			valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_));
			valeur_ = COALESCE(valeur_ , 0);
			IF(valeur_ != 0 OR attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom, periode_, entete_, attente_, valeur_, i);
			END IF;
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_objectif_by_periode ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif_by_periode(bigint, bigint, character varying)
  OWNER TO postgres;
  

DROP FUNCTION com_et_objectif_by_periode(bigint, bigint);
DROP FUNCTION com_et_objectif(bigint, bigint, character varying);
DROP FUNCTION com_et_objectif(bigint, bigint);
DROP FUNCTION com_et_objectif(bigint, character varying);
DROP FUNCTION com_get_valeur_objectif(bigint, bigint, bigint);