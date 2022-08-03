-- select id, nom , prenom, horaire_dynamique, agence from yvs_grh_employes where matricule = 'I240'
-- select employe from yvs_grh_presence where id = 2937
-- select date_debut, date_fin, heure_debut, heure_fin from yvs_grh_planning_employe p inner join yvs_grh_tranche_horaire t on p.tranche = t.id where p.employe = 2937
-- select p.id, p.date_debut, p.date_fin, p.heure_debut, p.heure_fin, o.heure_entree, o.heure_sortie, o.valider from yvs_grh_pointage o inner join yvs_grh_presence p on o.presence = p.id where p.employe = 2937
select p.id, p.total_presence, p.date_debut, p.date_fin, p.heure_debut, p.heure_fin, o.heure_entree, o.heure_sortie, o.valider from yvs_grh_pointage o inner join yvs_grh_presence p on o.presence = p.id where o.valider = true
-- select p.id, p.date_debut, p.date_fin, p.heure_debut, p.heure_fin, o.heure_entree, o.heure_sortie, o.valider from yvs_grh_pointage o inner join yvs_grh_presence p on o.presence = p.id where ((o.heure_entree < (select ((p.date_debut || ' ' || p.heure_debut)::timestamp))) or (o.heure_sortie > (select ((p.date_fin || ' ' || p.heure_fin)::timestamp)))) and o.valider = true
