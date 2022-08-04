SELECT
     yvs_missions."id" AS yvs_missions_id,
     yvs_missions."date_debut" AS yvs_missions_date_debut,
     yvs_missions."date_fin" AS yvs_missions_date_fin,
     yvs_missions."date_mission" AS yvs_missions_date_mission,
     yvs_missions."lieu" AS yvs_missions_lieu,
     yvs_missions."ordre" AS yvs_missions_ordre,
     yvs_missions."societe" AS yvs_missions_societe,
     yvs_mission_emps."id" AS yvs_mission_emps_id,
     yvs_mission_emps."mission" AS yvs_mission_emps_mission,
     yvs_mission_emps."employe" AS yvs_mission_emps_employe,
     yvs_mission_emps."role_employe" AS yvs_mission_emps_role_employe,
     yvs_mission_emps."detail_role" AS yvs_mission_emps_detail_role,
     yvs_employes."id" AS yvs_employes_id,
     yvs_employes."civilite" AS yvs_employes_civilite,
     yvs_employes."cni" AS yvs_employes_cni,
     yvs_employes."matricule" AS yvs_employes_matricule,
     yvs_employes."nom" AS yvs_employes_nom,
     yvs_employes."photos" AS yvs_employes_photos,
     yvs_employes."prenom" AS yvs_employes_prenom,
     yvs_employes."agence" AS yvs_employes_agence,
     yvs_agences."id" AS yvs_agences_id,
     yvs_agences."abbreviation" AS yvs_agences_abbreviation,
     yvs_agences."adresse" AS yvs_agences_adresse,
     yvs_agences."codeagence" AS yvs_agences_codeagence,
     yvs_agences."designation" AS yvs_agences_designation,
     yvs_agences."region" AS yvs_agences_region,
     yvs_agences."ville" AS yvs_agences_ville,
     yvs_agences."secteur_activite" AS yvs_agences_secteur_activite,
     yvs_agences."societe" AS yvs_agences_societe,
     yvs_societes."id" AS yvs_societes_id,
     yvs_societes."adress_siege" AS yvs_societes_adress_siege,
     yvs_societes."capital" AS yvs_societes_capital,
     yvs_societes."code_abreviation" AS yvs_societes_code_abreviation,
     yvs_societes."code_postal" AS yvs_societes_code_postal,
     yvs_societes."logo" AS yvs_societes_logo,
     yvs_societes."name" AS yvs_societes_name,
     yvs_societes."numero_registre_comerce" AS yvs_societes_numero_registre_comerce,
     yvs_societes."siege" AS yvs_societes_siege,
     yvs_societes."site_web" AS yvs_societes_site_web,
     yvs_societes."tel" AS yvs_societes_tel,
     yvs_societes."ville" AS yvs_societes_ville,
     yvs_poste_employes."id" AS yvs_poste_employes_id,
     yvs_poste_employes."employe" AS yvs_poste_employes_employe,
     yvs_poste_employes."poste" AS yvs_poste_employes_poste,
     yvs_poste_employes."date_acquisition" AS yvs_poste_employes_date_acquisition,
     yvs_poste_employes."valider" AS yvs_poste_employes_valider,
     yvs_poste_de_travail."id" AS yvs_poste_de_travail_id,
     yvs_poste_de_travail."degre" AS yvs_poste_de_travail_degre,
     yvs_poste_de_travail."description_poste" AS yvs_poste_de_travail_description_poste,
     yvs_poste_de_travail."intitule" AS yvs_poste_de_travail_intitule,
     yvs_poste_de_travail."departement" AS yvs_poste_de_travail_departement,
     yvs_poste_de_travail."niveau" AS yvs_poste_de_travail_niveau,
     yvs_poste_de_travail."poste_equivalent" AS yvs_poste_de_travail_poste_equivalent
FROM
     "public"."yvs_missions" yvs_missions INNER JOIN "public"."yvs_mission_emps" yvs_mission_emps ON yvs_missions."id" = yvs_mission_emps."mission"
     INNER JOIN "public"."yvs_employes" yvs_employes ON yvs_mission_emps."employe" = yvs_employes."id"
     INNER JOIN "public"."yvs_agences" yvs_agences ON yvs_employes."agence" = yvs_agences."id"
     INNER JOIN "public"."yvs_poste_employes" yvs_poste_employes ON yvs_employes."id" = yvs_poste_employes."employe"
     INNER JOIN "public"."yvs_poste_de_travail" yvs_poste_de_travail ON yvs_poste_employes."poste" = yvs_poste_de_travail."id"
     INNER JOIN "public"."yvs_societes" yvs_societes ON yvs_agences."societe" = yvs_societes."id"
     AND yvs_societes."id" = yvs_missions."societe"
WHERE
     yvs_poste_employes."valider" = true
 AND yvs_missions."id" = $P{ID_MISSION}
 AND yvs_employes."id" = $P{ID_EMPLOYE}