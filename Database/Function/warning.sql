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
		-- voir le dossier de tous les employés de la société
		SELECT INTO compteur_ COUNT(m.id) FROM yvs_compta_caisse_doc_divers m INNER JOIN yvs_users_agence u ON m.author = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE COALESCE((SELECT COUNT(p.id) FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = m.id), 0) < 1 AND m.statut_doc IN ('V', 'T', 'C') AND a.societe = societe_;	
		SELECT INTO date_debut_ m.date_doc::date FROM yvs_compta_caisse_doc_divers m INNER JOIN yvs_users_agence u ON m.author = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE COALESCE((SELECT COUNT(p.id) FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = m.id), 0) < 1 AND m.statut_doc IN ('V', 'T', 'C') AND a.societe = societe_ ORDER BY m.date_doc::date LIMIT 1;
		if(compteur_ IS NOT NULL AND compteur_ > 0)then
			insert into table_warning values('Opération diverse non planifiées', compteur_, 'OPERATION_DIVERS', date_debut_);
		end if;	
	end if;
	return QUERY select * from table_warning order by elt;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION warning(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint)
  OWNER TO postgres;
