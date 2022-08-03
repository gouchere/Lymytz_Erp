ALTER TABLE yvs_base_tarif_point_livraison DROP COLUMN societe;
ALTER TABLE yvs_base_tarif_point_livraison DROP COLUMN lieux;
COMMENT ON COLUMN yvs_base_tarif_point_livraison.livraison_domicile IS 'Détermine si dans cette ville la société accepte de livrer à domicile';
ALTER TABLE yvs_base_tarif_point_livraison ADD COLUMN lieux_liv bigint;
COMMENT ON COLUMN yvs_base_tarif_point_livraison.lieux_liv IS 'Lieux de livraison (ville)';
ALTER TABLE yvs_base_tarif_point_livraison
  ADD CONSTRAINT yvs_base_tarif_point_livraison_lieux_liv_fkey FOREIGN KEY (lieux_liv)
      REFERENCES yvs_dictionnaire_informations (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
COMMENT ON COLUMN yvs_base_tarif_point_livraison.delai_for_retrait IS 'Détermine le delai de livraison lorsque le client à choisit de se faire livrer dans un point de retrait';


ALTER TABLE yvs_base_article_depot ADD COLUMN categorie character varying;
