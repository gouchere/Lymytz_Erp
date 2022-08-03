-- Function: warning(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint)
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
						SELECT INTO compteur_ COUNT(m.id) FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id INNER JOIN yvs_base_point_vente p ON c.point = p.id INNER JOIN yvs_agences a ON p.agence = a.id WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C') AND m.type_doc = type_ AND a.id = agence_;
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

  
-- Function: get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer)
-- DROP FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer);
CREATE OR REPLACE FUNCTION get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ integer)
  RETURNS double precision AS
$BODY$
DECLARE
	data_ record;
	tarif_ record;
	
	valeur_ double precision default 0;
	remise_ double precision;
	garde_ double precision;

BEGIN
	valeur_ = qte_ * prix_;	
	if(client_ is not null and client_ > 0)then
		for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
		loop
			if(data_.permanent is false)then
				if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
					if(unite_ is not null and unite_ > 0)then
						select into tarif_ y.* from yvs_base_plan_tarifaire y inner join yvs_base_conditionnement c on y.conditionnement = c.id where y.categorie = data_.categorie and y.article = article_ and c.unite = unite_ and y.actif is true limit 1;
						if(tarif_.id is not null)then
							if(tarif_.nature_remise = 'TAUX')then
								garde_ = valeur_ * (tarif_.remise /100);
							else
								garde_ = tarif_.remise;
							end if;
							select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
							if(tarif_.id IS NOT NULL) then
								if(tarif_.nature_remise = 'TAUX')then
									remise_ = valeur_ * (tarif_.remise /100);
								else
									remise_ = tarif_.remise;
								end if;
							end if;
							if(remise_ is null or remise_ < 1)then
								remise_ = garde_;
							end if;
						end if;
					else
						select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
						if(tarif_.id is not null)then
							if(tarif_.nature_remise = 'TAUX')then
								garde_ = valeur_ * (tarif_.remise /100);
							else
								garde_ = tarif_.remise;
							end if;
							select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
							if(tarif_.id IS NOT NULL) then
								if(tarif_.nature_remise = 'TAUX')then
									remise_ = valeur_ * (tarif_.remise /100);
								else
									remise_ = tarif_.remise;
								end if;
							end if;
							if(remise_ is null or remise_ < 1)then
								remise_ = garde_;
							end if;
						end if;
					end if;
					exit;
				end if;
			else
				if(unite_ is not null and unite_ > 0)then
					select into tarif_ y.* from yvs_base_plan_tarifaire y inner join yvs_base_conditionnement c on y.conditionnement = c.id where y.categorie = data_.categorie and y.article = article_ and c.unite = unite_ and y.actif is true limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
						else
							garde_ = tarif_.remise;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL) then
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
							else
								remise_ = tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ < 1)then
							remise_ = garde_;
						end if;
					end if;
				else
					select into tarif_ * from yvs_base_plan_tarifaire where categorie = data_.categorie and article = article_ and actif is true limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
						else
							garde_ = tarif_.remise;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL) then
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
							else
								remise_ = tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ <= 0)then
							remise_ = garde_;
						end if;
						exit;
					end if;
				end if;
				--exit;
			end if;
		end loop;
	end if;
	if(remise_ is null or remise_ <= 0)then
		if(unite_ is not null and unite_ > 0)then
			select into tarif_ y.* from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.unite = unite_ and actif is true limit 1;
			if(tarif_.id IS NOT NULL) then
					if(tarif_.nature_remise = 'TAUX')then
						remise_ = valeur_ * (tarif_.remise /100);
					else
						remise_ = tarif_.remise;
					end if;
			end if;
		else
			select into tarif_ * from yvs_base_article_point where point = point_ and article = article_ and actif is true limit 1;
			if(tarif_.id IS NOT NULL) then
				if(tarif_.nature_remise = 'TAUX')then
					remise_ = valeur_ * (tarif_.remise /100);
				else
					remise_ = tarif_.remise;
				end if;
			end if;
			if(remise_ is null or remise_ <= 0)then
				if(unite_ is not null and unite_ > 0)then
					select into remise_ remise from yvs_base_conditionnement where article = article_ and conditionnement = unite_;
					if(remise_ is not null) then
						remise_ = valeur_ * (remise_/100);
					end if;
				else
					select into remise_ remise from yvs_base_articles where id = article_; 
					if(remise_ is not null) then
						remise_ = valeur_ * (remise_/100);
					end if;
				end if;
			end if;	
		end if;
	end if;	
	if(remise_ is null or remise_ <=0)then
		remise_ = 0;
	end if;	
	return remise_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer) IS 'retourne la remise sur vente d'' article';
