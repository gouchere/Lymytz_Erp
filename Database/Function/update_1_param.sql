-- Function: update_(bigint)
-- DROP FUNCTION update_(bigint);
CREATE OR REPLACE FUNCTION update_(societe_ bigint)
  RETURNS boolean AS
$BODY$   
DECLARE 	
	lect_ record;
	model_ record;

	date_debut_ date;
	
	compteur_ integer default 0;
	ecart_ integer default 0;

	acces_ boolean default false;

	type_ character varying;
BEGIN	
	if(societe_ is not null and societe_ > 0)then
		SELECT INTO ecart_ COALESCE(ecart_document, 1) FROM yvs_societes WHERE id = societe_;
		for model_ in select w.id, w.titre_doc as model, w.description as titre, COALESCE(a.ecart, ecart_) as ecart from yvs_workflow_model_doc w left join yvs_warning_model_doc a on a.model = w.id and a.societe = societe_
		loop
			IF(COALESCE(model_.ecart, 0) = 0)THEN
				model_.ecart = 1;
			END IF;
			IF(model_.model = 'MISSIONS')THEN
				FOR lect_ IN SELECT m.id, m.date_mission, m.author, e.agence, (current_date - COALESCE(date_mission, current_date)) AS duree FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id 
					INNER JOIN yvs_agences a ON e.agence = a.id WHERE ((m.date_mission + model_.ecart) < CURRENT_DATE) AND m.statut_mission NOT IN ('V', 'T', 'C', 'A','S','A') AND a.societe = societe_
				LOOP
					RAISE NOTICE 'yvs_grh_missions [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'VALIDATION', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_mission);
				END LOOP;
			ELSIF(model_.model = 'OD_NON_PLANNIFIE')THEN
				FOR lect_ IN SELECT m.id, m.date_doc, m.author, m.agence, (current_date - COALESCE(date_doc, current_date)) AS duree FROM yvs_compta_caisse_doc_divers m INNER JOIN yvs_users_agence u ON m.author = u.id 
					INNER JOIN yvs_agences a ON u.agence = a.id WHERE COALESCE((SELECT COUNT(p.id) FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = m.id), 0) < 1 AND m.statut_doc IN ('V', 'T', 'C') AND a.societe = societe_	
				LOOP
					RAISE NOTICE 'yvs_compta_caisse_doc_divers [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'NON PLANNIFIE', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_doc);
				END LOOP;
			ELSIF(model_.model = 'CONGES')THEN
				FOR lect_ IN SELECT m.id, m.date_conge, m.author, e.agence, (current_date - COALESCE(date_conge, current_date)) AS duree FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id 
					INNER JOIN yvs_agences a ON e.agence = a.id WHERE (((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')) AND m.nature = 'C' AND a.societe = societe_
				LOOP
					RAISE NOTICE 'yvs_grh_conge_emps [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'VALIDATION', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_conge);
				END LOOP;
			ELSIF(model_.model = 'FACTURE_ACHAT' or model_.model = 'RETOUR_ACHAT' or model_.model = 'AVOIR_ACHAT')THEN
				IF(model_.model = 'FACTURE_ACHAT')THEN
					type_ = 'FA';
				ELSIF(model_.model = 'RETOUR_ACHAT')THEN
					type_ = 'BRA';
				ELSE
					type_ = 'FAA';
				END IF;
				FOR lect_ IN SELECT m.id, m.date_doc, m.author, m.agence, (current_date - COALESCE(date_doc, current_date)) AS duree FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id 
					WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.societe = societe_
				LOOP
					RAISE NOTICE 'yvs_com_doc_achats [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'VALIDATION', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_doc);
				END LOOP;
			ELSIF(model_.model = 'FACTURE_VENTE' or model_.model = 'RETOUR_VENTE' or model_.model = 'AVOIR_VENTE')THEN
				IF(model_.model = 'FACTURE_VENTE')THEN
					type_ = 'FV';
				ELSIF(model_.model = 'RETOUR_VENTE')THEN
					type_ = 'BRV';
				ELSE
					type_ = 'FAV';
				END IF;
				FOR lect_ IN SELECT m.id, e.date_entete, m.author, p.agence, (current_date - COALESCE(date_entete, current_date)) AS duree FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id 
					INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id INNER JOIN yvs_base_point_vente p ON c.point = p.id INNER JOIN yvs_agences a ON p.agence = a.id 
					WHERE ((e.date_entete + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.societe = societe_
				LOOP
					RAISE NOTICE 'yvs_com_doc_ventes [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'VALIDATION', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_entete);
				END LOOP;
			ELSIF(model_.model = 'PERMISSION_CD')THEN
				FOR lect_ IN SELECT m.id, m.date_conge, m.author, e.agence, (current_date - COALESCE(date_conge, current_date)) AS duree FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id 
					INNER JOIN yvs_agences a ON e.agence = a.id WHERE (((m.date_conge + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A')) AND m.nature = 'P' AND a.societe = societe_
				LOOP
					RAISE NOTICE 'yvs_grh_conge_emps [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'VALIDATION', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_conge);
				END LOOP;
			ELSIF(model_.model = 'SORTIE_STOCK')THEN
				type_ = 'SS';
				FOR lect_ IN SELECT m.id, m.date_doc, m.author, d.agence, (current_date - COALESCE(date_doc, current_date)) AS duree FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots d On m.source = d.id 
					INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_doc + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND m.type_doc = type_ AND a.societe = societe_
				LOOP
					RAISE NOTICE 'yvs_com_doc_stocks [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'VALIDATION', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_doc);
				END LOOP;
			ELSIF(model_.model = 'OPERATION_DIVERS')THEN
				-- voir le dossier de tous les employés de la société
				FOR lect_ IN SELECT m.id, m.date_doc, m.author, m.agence, (current_date - COALESCE(date_doc, current_date)) AS duree FROM yvs_compta_caisse_doc_divers m INNER JOIN yvs_users_agence u ON m.author = u.id 
					INNER JOIN yvs_agences a ON m.agence = a.id WHERE ((m.date_doc::date + model_.ecart) < CURRENT_DATE) AND m.statut_doc NOT IN ('V', 'T', 'C', 'A') AND a.societe = societe_	
				LOOP
					RAISE NOTICE 'yvs_compta_caisse_doc_divers [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'VALIDATION', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_doc);
				END LOOP;
			ELSIF(model_.model = 'BON_OPERATION_DIVERS')THEN
				-- voir le dossier de tous les employés de la société
				FOR lect_ IN SELECT m.id, m.date_bon::date, m.author, m.agence, (current_date - COALESCE(date_bon::date, current_date)) AS duree FROM yvs_compta_bon_provisoire m INNER JOIN yvs_agences a ON m.agence = a.id 
					WHERE ((m.date_bon::date + model_.ecart) < CURRENT_DATE) AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = societe_
				LOOP
					RAISE NOTICE 'yvs_compta_bon_provisoire [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'VALIDATION', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_bon);
				END LOOP;
			ELSIF(model_.model = 'APPROVISIONNEMENT')THEN
				FOR lect_ IN SELECT m.id, m.date_approvisionnement, m.author, d.agence, (current_date - COALESCE(date_approvisionnement, current_date)) AS duree FROM yvs_com_fiche_approvisionnement m INNER JOIN yvs_base_depots d On m.depot = d.id 
					INNER JOIN yvs_agences a ON d.agence = a.id WHERE ((m.date_approvisionnement + model_.ecart) < CURRENT_DATE) AND m.etat NOT IN ('V', 'T', 'C', 'A', 'A') AND m.statut_terminer != 'T' AND a.societe = societe_
				LOOP
					RAISE NOTICE 'yvs_com_fiche_approvisionnement [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'VALIDATION', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_approvisionnement);
				END LOOP;
			ELSIF(model_.model = 'PIECE_CAISSE')THEN
				-- voir le dossier de tous les employés de la société
				FOR lect_ IN SELECT m.id, m.date_paiment_prevu::date, m.author, u.agence, (current_date - COALESCE(date_paiment_prevu::date, current_date)) AS duree FROM yvs_compta_mouvement_caisse m INNER JOIN yvs_users_agence u ON m.author = u.id LEFT JOIN yvs_base_mode_reglement y ON m.model = y.id 
					WHERE ((m.date_paiment_prevu::date + model_.ecart) < CURRENT_DATE) AND m.statut_piece NOT IN ('P') AND m.societe = societe_ AND (m.model IS NULL OR y.type_reglement = 'ESPECE') AND m.table_externe NOT IN ('NOTIF_VENTE', 'NOTIF_ACHAT')
				LOOP
					RAISE NOTICE 'yvs_compta_mouvement_caisse [%]', lect_.id;
					INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, silence, date_save, date_update, author, agence, niveau, date_doc)
						VALUES(model_.id, 'VALIDATION', lect_.id, FALSE, current_timestamp, current_timestamp, lect_.author, lect_.agence, 1, lect_.date_paiment_prevu);
				END LOOP;
			END IF;
		end loop;
	end if;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_(bigint)
  OWNER TO postgres;
