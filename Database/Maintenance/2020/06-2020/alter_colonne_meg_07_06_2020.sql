ALTER TABLE yvs_societe_infos_vente ADD COLUMN display_catalogue_on_list BOOLEAN DEFAULT false;

ALTER TABLE yvs_base_conditionnement_point ADD COLUMN avance_commance DOUBLE PRECISION DEFAULT 0;
ALTER TABLE yvs_base_point_vente ADD COLUMN miminum_active_livraison DOUBLE PRECISION DEFAULT 0;

ALTER TABLE yvs_base_tarif_point_livraison DROP COLUMN tarif_livraison_domicile;
ALTER TABLE yvs_base_tarif_point_livraison DROP COLUMN nombre_jour_livraison_domicile;
ALTER TABLE yvs_base_tarif_point_livraison RENAME tarif_livraison_retrait TO frais_livraison;
ALTER TABLE yvs_base_tarif_point_livraison RENAME nombre_jour_livraison_retrait TO delai_livraison;
ALTER TABLE yvs_base_tarif_point_livraison RENAME active_livraison_domicile TO livraison_domicile;
ALTER TABLE yvs_base_tarif_point_livraison ADD COLUMN article bigint;
ALTER TABLE yvs_base_tarif_point_livraison
  ADD CONSTRAINT yvs_base_tarif_point_livraison_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;