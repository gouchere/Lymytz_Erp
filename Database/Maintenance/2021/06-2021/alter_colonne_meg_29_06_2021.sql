-- Index: yvs_grh_presence_employe_idx
-- DROP INDEX yvs_grh_presence_employe_idx;
CREATE INDEX yvs_grh_presence_employe_idx
  ON yvs_grh_presence
  USING btree
  (employe);
  
-- Index: yvs_grh_presence_type_validation_idx
-- DROP INDEX yvs_grh_presence_type_validation_idx;
CREATE INDEX yvs_grh_presence_type_validation_idx
  ON yvs_grh_presence
  USING btree
  (type_validation);
  
-- Index: yvs_grh_presence_date_debut_idx
-- DROP INDEX yvs_grh_presence_date_debut_idx;
CREATE INDEX yvs_grh_presence_date_debut_idx
  ON yvs_grh_presence
  USING btree
  (date_debut);
  
-- Index: yvs_grh_presence_date_fin_idx
-- DROP INDEX yvs_grh_presence_date_fin_idx;
CREATE INDEX yvs_grh_presence_date_fin_idx
  ON yvs_grh_presence
  USING btree
  (date_fin);
  
-- Index: yvs_grh_pointage_presence_idx
-- DROP INDEX yvs_grh_pointage_presence_idx;
CREATE INDEX yyvs_grh_pointage_presence_idx
  ON yvs_grh_pointage
  USING btree
  (presence);
  
-- Index: yvs_grh_pointage_heure_entree_idx
-- DROP INDEX yvs_grh_pointage_heure_entree_idx;
CREATE INDEX yyvs_grh_pointage_heure_entree_idx
  ON yvs_grh_pointage
  USING btree
  (heure_entree);
  
-- Index: yvs_grh_pointage_heure_sortie_idx
-- DROP INDEX yvs_grh_pointage_heure_sortie_idx;
CREATE INDEX yyvs_grh_pointage_heure_sortie_idx
  ON yvs_grh_pointage
  USING btree
  (heure_sortie);