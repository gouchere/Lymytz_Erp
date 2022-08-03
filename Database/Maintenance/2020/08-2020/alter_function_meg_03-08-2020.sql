INSERT INTO yvs_stat_grh_etat(libelle, societe, code) VALUES ('JOURNAL DE PAIE PAR CATEGORIE', 2297, 'JRN_PAIE_CONV');
INSERT INTO yvs_stat_grh_element_dipe(libelle, element_salaire, etat, author, groupe_element, ordre, champ_valeur, commentaire_champ, by_formulaire)  
SELECT y.libelle, y.element_salaire, (SELECT e.id FROM yvs_stat_grh_etat e WHERE e.code = 'JRN_PAIE_CONV' AND e.societe = 2297), y.author, y.groupe_element, y.ordre, y.champ_valeur, y.commentaire_champ, y.by_formulaire 
FROM yvs_stat_grh_element_dipe y INNER JOIN yvs_stat_grh_etat e ON y.etat = e.id WHERE e.code = 'LIVRE_PAIE' AND e.societe = 2297;
