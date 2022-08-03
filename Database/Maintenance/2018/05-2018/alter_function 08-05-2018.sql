-- Function: get_pua(bigint, bigint, bigint);
-- DROP FUNCTION get_pua(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION get_pua(article_ bigint, fsseur_ bigint, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE

BEGIN
	return get_pua(article_, fsseur_, 0::bigint, unite_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pua(bigint, bigint, bigint)
  OWNER TO postgres;
  
  -- Function: get_pua(bigint, bigint, bigint, bigint)
-- DROP FUNCTION get_pua(bigint, bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION get_pua(article_ bigint, fsseur_ bigint, depot_ bigint, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pua_ double precision;
	query_ character varying;

BEGIN
	-- A partir des facture d'achat
	query_ = 'select c.pua_attendu from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat where c.article = '||article_;
	if(fsseur_ is not null and fsseur_ > 0)then
		query_ = query_ || ' and d.fournisseur = '||fsseur_;
	end if;
	if(depot_ is not null and depot_ > 0)then
		query_ = query_ || ' and (d.depot_reception IS NOT NULL AND d.depot_reception = '||depot_||')';
	end if;
	if(unite_ is not null and unite_ > 0)then
		query_ = query_ || ' and c.conditionnement = '||unite_;
	end if;
	query_ = query_ || ' order by d.date_doc desc limit 1';
	EXECUTE query_ INTO pua_;
	if(pua_ is null or pua_ < 1)then
		-- A partir des conditionnement
		select into pua_ y.pua from yvs_base_conditionnement_fournisseur y inner join yvs_base_article_fournisseur a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.fournisseur = fsseur_ and a.article = article_ and c.id = unite_ limit 1;
		if(pua_ is null or pua_ < 1)then
			select into pua_ puv from yvs_base_article_fournisseur where fournisseur = fsseur_ and article = article_;
			if(pua_ is null or pua_ < 1)then
				select into pua_ prix_achat from yvs_base_conditionnement c where c.id = unite_;
				if(pua_ is null or pua_ < 1)then		
					select into pua_ pua from yvs_base_articles where id = article_; 
				end if;
			end if;	
		end if;
	end if;
	return COALESCE(pua_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pua(bigint, bigint, bigint, bigint)
  OWNER TO postgres;

  
-- Function: workflow(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint);
-- DROP FUNCTION workflow(bigint, bigint, bigint, bigint, bigint, bigint, bigint, bigint);

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
								AND (m.depot_reception IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.reponsable = employe_)));
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
									AND m.type_doc = type_ AND (m.depot_livraison IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.reponsable = employe_)));
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
								AND (m.source IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.reponsable = employe_)));
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
								AND (m.depot IN (SELECT c.depot FROM yvs_com_creneau_depot c INNER JOIN yvs_com_creneau_horaire_users h ON h.creneau_depot = c.id INNER JOIN yvs_base_depots d ON c.depot = d.id WHERE h.actif AND ((h.users = current_ AND (h.permanent OR h.date_travail = current_date)) OR d.reponsable = employe_)));
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

  
 -- Function: com_et_valorise_stock(date, character varying, character varying, character varying, bigint);
-- DROP FUNCTION com_et_valorise_stock(date, character varying, character varying, character varying, bigint);

CREATE OR REPLACE FUNCTION com_et_valorise_stock(IN date_ date, IN categorie_ character varying, IN depots_ character varying, IN groupe_by_ character varying, IN societe_ bigint)
  RETURNS TABLE(id bigint, reference character varying, designation character varying, unite character varying, depot character varying, quantite double precision, prix_revient double precision, prix_vente double precision, prix_achat double precision) AS
$BODY$
DECLARE
	depot_ record;
	articles_ record;
	familles_ record;
	unite_ record;
	
	query_ character varying default 'SELECT a.id, a.ref_art as code, a.designation FROM yvs_base_articles a INNER JOIN yvs_base_famille_article f On a.famille = f.id WHERE f.societe = '||societe_;
	query_depot_ character varying default 'SELECT id, code, designation FROM yvs_base_depots';

	ct_ double precision default 0;
	st_ double precision default 0;
	pr_ double precision default 0;
	pv_ double precision default 0;
	pa_ double precision default 0;
	
	i_ integer default 0;

BEGIN
	DROP TABLE IF EXISTS table_valorise_stock;
	CREATE TEMP TABLE IF NOT EXISTS table_valorise_stock(_id bigint, _reference character varying, _designation character varying, unite_ character varying, _depot character varying, _quantite double precision, _prix_revient double precision, _prix_vente double precision, _prix_achat double precision);
	if(societe_ is not null and societe_ > 0)then
		IF(depots_ IS NOT NULL AND depots_ NOT IN ('', ' '))THEN
			query_depot_ = query_depot_ || ' WHERE id in ('||(select val from regexp_split_to_table(depots_,',') val)||')';
		END IF;
		query_depot_ = query_depot_ || ' ORDER BY code';
		IF(categorie_ IS NOT NULL AND categorie_ NOT IN ('', ' '))THEN
			query_ = query_ || ' AND categorie = '''||categorie_||'''';
		END IF;
		query_ = query_ || ' ORDER BY ref_art';
		
		IF(groupe_by_ = 'F')THEN
			FOR familles_ IN SELECT f.id, f.reference_famille as code, f.designation FROM yvs_base_famille_article f WHERE f.societe = societe_
			LOOP			
				FOR depot_ IN EXECUTE query_depot_
				LOOP
					st_ = 0;
					pr_ = 0;
					pv_ = 0;
					pa_ = 0;
					i_ = 0;
					
					FOR articles_ IN SELECT a.id, a.ref_art as code, a.designation FROM yvs_base_articles a WHERE famille = familles_.id
					LOOP
						FOR unite_ IN SELECT c.id, u.reference as code FROM yvs_base_conditionnement c INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE c.article = articles_.id
						LOOP
							ct_ = COALESCE((SELECT get_stock_reel(articles_.id, 0, depot_.id, 0, 0, date_, unite_.id)), 0);
							IF(ct_ != 0)THEN
								st_ = st_ + ct_;
								pr_ = pr_ + COALESCE((SELECT get_pr(articles_.id, depot_.id, 0, date_, unite_.id)), 0);
								pv_ = pv_ + COALESCE((SELECT get_puv(articles_.id, 0, 0, 0, depot_.id, 0, date_, unite_.id, false)), 0);
								pa_ = pa_ + COALESCE((SELECT get_pua(articles_.id, 0, depot_.id, unite_.id)), 0);
								i_ = i_ + 1;
							END IF;
						END LOOP;
					END LOOP;
					IF(st_ != 0)THEN
						IF(pr_ != 0)THEN
							pr_ = pr_ / i_;
						END IF;
						IF(pv_ != 0)THEN
							pv_ = pv_ / i_;
						END IF;
						IF(pa_ != 0)THEN
							pa_ = pr_ / i_;
						END IF;
						INSERT INTO table_valorise_stock VALUES(familles_.id, familles_.code, familles_.designation, '', depot_.designation, st_, pr_, pv_, pa_);
					END IF;
				END LOOP;
			END LOOP;
		ELSE		
			FOR articles_ IN EXECUTE query_
			LOOP				
				FOR depot_ IN EXECUTE query_depot_
				LOOP
					FOR unite_ IN SELECT c.id, u.reference as code FROM yvs_base_conditionnement c INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE c.article = articles_.id
					LOOP
						st_ = COALESCE((SELECT get_stock_reel(articles_.id, 0, depot_.id, 0, 0, date_, unite_.id)), 0);
						IF(st_ != 0)THEN
							pr_ = COALESCE((SELECT get_pr(articles_.id, depot_.id, 0, date_, unite_.id)), 0);
							pv_ = COALESCE((SELECT get_puv(articles_.id, 0, 0, 0, depot_.id, 0, date_, unite_.id, false)), 0);
							pa_ = COALESCE((SELECT get_pua(articles_.id, 0, depot_.id, unite_.id)), 0);

							INSERT INTO table_valorise_stock VALUES(articles_.id, articles_.code, articles_.designation, unite_.code, depot_.designation, st_, pr_, pv_, pa_);
						END IF;
					END LOOP;
				END LOOP;			
			END LOOP;
		END IF;
	end if;
	return QUERY select * from table_valorise_stock order by _reference;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_valorise_stock(date, character varying, character varying, character varying, bigint)
  OWNER TO postgres;

-- Function: action_on_workflow_bon_provisoire();
-- DROP FUNCTION action_on_workflow_bon_provisoire();

CREATE OR REPLACE FUNCTION action_on_workflow_bon_provisoire()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
	id_ BIGINT DEFAULT 0;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT' OR action_='UPDATE' ) THEN
		id_ = NEW.doc_caisse;
	ELSIF (action_='TRUNCATE' OR action_='DELETE') THEN 
		id_ = OLD.doc_caisse;
	END IF;
	SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_bon_provisoire WHERE doc_caisse=id_;
	SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_bon_provisoire WHERE doc_caisse=id_ AND etape_valid IS TRUE;
	UPDATE yvs_compta_bon_provisoire SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=id_;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_bon_provisoire()
  OWNER TO postgres;
  
CREATE TRIGGER action_on_workflow_bon_provisoire
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_workflow_valid_bon_provisoire
  FOR EACH ROW
  EXECUTE PROCEDURE action_on_workflow_bon_provisoire();
  
-- Function: compta_action_on_bon_provisoire();
-- DROP FUNCTION compta_action_on_bon_provisoire();

CREATE OR REPLACE FUNCTION compta_action_on_bon_provisoire()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	id_model_ bigint;
	nom_prenom_ character varying default '';
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, NEW.numero, t.id as tiers, t.nom, t.prenom FROM yvs_base_tiers t INNER JOIN yvs_users_agence f ON f.id=t.author INNER JOIN yvs_agences ag ON ag.id=f.agence WHERE t.id=NEW.tiers; 
								    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_agences a WHERE a.id=NEW.agence;
		END IF;
		IF(NEW.tiers IS NOT NULL) THEN
			id_tiers_=NEW.tiers;
		ELSE
			id_tiers_=line_.tiers;
		END IF;
		nom_prenom_ = NEW.beneficiaire;
		IF(nom_prenom_ IS NULL OR nom_prenom_ IN ('', ' '))THEN
			IF(line_.nom IS NOT NULL) THEN
				nom_prenom_ = line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				nom_prenom_ = nom_prenom_ || ' '||line_.prenom;
			END IF;
		END IF;
		SELECT INTO id_model_ id FROM yvs_base_mode_reglement WHERE societe = line_.societe AND type_reglement = 'ESPECE' AND default_mode IS TRUE LIMIT 1;
		IF(id_model_ IS NULL OR id_model_ < 1)THEN
			SELECT INTO id_model_ id FROM yvs_base_mode_reglement WHERE societe = line_.societe AND type_reglement = 'ESPECE' LIMIT 1;
		END IF;
	END IF;         
	IF(action_='INSERT' AND NEW.statut_justify != 'J') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero, NEW.id, NEW.description, 'BON_PROVISOIRE', NEW.montant, line_.numero, NEW.date_bon, NEW.date_bon, NEW.date_bon,  NEW.statut_paiement, line_.societe, NEW.author, NEW.caisse, NEW.caissier, null, id_tiers_, 'D', nom_prenom_, id_model_);
	ELSIF (action_='UPDATE' AND NEW.statut_justify != 'J') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='BON_PROVISOIRE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero, note=NEW.description, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.numero, date_mvt=NEW.date_bon, model = id_model_, 
				date_paiment_prevu=NEW.date_bon, date_paye=NEW.date_bon, statut_piece=NEW.statut_paiement, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', societe=line_.societe, name_tiers=nom_prenom_
				WHERE table_externe='BON_PROVISOIRE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
				VALUES (NEW.numero, NEW.id, NEW.description, 'BON_PROVISOIRE', NEW.montant, line_.numero, NEW.date_bon, NEW.date_bon, NEW.date_bon,  NEW.statut_paiement, line_.societe, NEW.author, NEW.caisse, NEW.caissier, null, id_tiers_, 'D', nom_prenom_, id_model_);
		END IF;
		UPDATE yvs_compta_notif_reglement_achat SET date_update = current_date WHERE piece_achat = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE' OR NEW.statut_justify = 'J') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='BON_PROVISOIRE' AND id_externe=OLD.id;	
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_bon_provisoire()
  OWNER TO postgres;

CREATE TRIGGER compta_action_on_bon_provisoire
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_bon_provisoire
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_bon_provisoire();
  
  
-- Function: compta_action_on_piece_caisse_divers();
-- DROP FUNCTION compta_action_on_piece_caisse_divers();

CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	action_ character varying;
	id_ bigint;
	id_el_ bigint;
	id_tiers_ bigint;
	montant_ double precision default 0 ;
	nom_ character varying default '';
	prenom_ character varying default '';	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  dv.societe, dv.num_piece as num_doc, dv.tiers, dv.mouvement FROM yvs_compta_caisse_piece_divers pv 
			INNER JOIN yvs_compta_caisse_doc_divers dv ON dv.id=pv.doc_divers WHERE pv.id=NEW.id; 
								    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		--récupérer nom du tiers
		IF(line_.tiers IS NOT NULL) THEN
		  SELECT INTO tiers_ t.* FROM yvs_base_tiers t WHERE t.id=line_.tiers;
			IF(tiers_.nom IS NOT NULL) THEN
				nom_=tiers_.nom;
			END IF;
			IF(tiers_.prenom IS NOT NULL) THEN
				prenom_=tiers_.prenom;
			END IF;
			id_tiers_=tiers_.id;
		END IF;
		
	END IF;         
	IF(action_='INSERT') THEN
		id_ = NEW.doc_divers;
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.num_piece, NEW.id, NEW.note, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_piece,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, line_.mouvement, NEW.beneficiaire, NEW.mode_paiement);
	ELSIF (action_='UPDATE') THEN 
		id_ = NEW.doc_divers;
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.mode_paiement,
				date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_piece, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement=line_.mouvement, societe=line_.societe, name_tiers=NEW.beneficiaire
				WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe, mouvement,name_tiers, model)
				VALUES (NEW.num_piece, NEW.id, NEW.note, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_piece,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,line_.mouvement, NEW.beneficiaire, NEW.mode_paiement);
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		id_ = OLD.doc_divers;
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=OLD.id;	
	END IF;
	SELECT INTO montant_ SUM(y.montant) FROM yvs_compta_caisse_piece_divers y WHERE y.statut_piece = 'P' AND y.doc_divers = id_;
	SELECT INTO id_ y.bon FROM yvs_compta_justificatif_bon y WHERE piece = id_;
	if(id_ IS NOT NULL AND id_ > 0)THEN
		UPDATE yvs_compta_mouvement_caisse SET montant = ((SELECT montant FROM yvs_compta_bon_provisoire WHERE id = id_) - COALESCE(montant_, 0)) WHERE table_externe='BON_PROVISOIRE' AND id_externe=id_;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_divers()
  OWNER TO postgres;
