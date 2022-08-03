
ALTER TABLE yvs_parametre_grh ADD COLUMN default_horaire boolean;
ALTER TABLE yvs_parametre_grh ADD COLUMN heure_debut_travail time without time zone;
ALTER TABLE yvs_parametre_grh ADD COLUMN heure_fin_travail time without time zone;
ALTER TABLE yvs_parametre_grh ADD COLUMN heure_fin_pause time without time zone;
ALTER TABLE yvs_parametre_grh ADD COLUMN heure_debut_pause time without time zone;