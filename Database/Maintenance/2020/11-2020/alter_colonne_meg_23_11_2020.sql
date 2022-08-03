ALTER TABLE yvs_grh_detail_prelevement_emps ADD COLUMN date_begin DATE;
ALTER TABLE yvs_parametre_grh ADD COLUMN nombre_mois_avance_max_retenue INTEGER DEFAULT 5;
ALTER TABLE yvs_parametre_grh ADD COLUMN compta_libelle_frais_mission BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN yvs_com_contenu_doc_stock.qte_attendu IS 'utilisé aussi pour definir la quantitée justifiée';

ALTER TABLE yvs_base_conditionnement_point ADD COLUMN proportion_pua BOOLEAN DEFAULT FALSE;
ALTER TABLE yvs_base_conditionnement_point ADD COLUMN taux_pua DOUBLE PRECISION DEFAULT 0;

ALTER TABLE yvs_base_conditionnement ADD COLUMN proportion_pua BOOLEAN DEFAULT FALSE;
ALTER TABLE yvs_base_conditionnement ADD COLUMN taux_pua DOUBLE PRECISION DEFAULT 0;

ALTER TABLE yvs_base_tiers ADD COLUMN agence bigint;
ALTER TABLE yvs_base_tiers
  ADD CONSTRAINT yvs_base_tiers_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;