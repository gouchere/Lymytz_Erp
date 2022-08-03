ALTER TABLE yvs_users ADD COLUMN abbreviation character varying;
UPDATE yvs_users SET abbreviation = SUBSTRING(nom_users, 0, 4);
ALTER TABLE yvs_com_parametre_vente ADD COLUMN model_facture_vente character varying DEFAULT 'facture_vente';

ALTER TABLE yvs_compta_mouvement_caisse ADD COLUMN agence bigint;
ALTER TABLE yvs_compta_mouvement_caisse
  ADD CONSTRAINT yvs_compta_mouvement_caisse_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
	  
UPDATE yvs_compta_mouvement_caisse SET agence = (SELECT j.agence FROM yvs_base_caisse y INNER JOIN yvs_compta_journaux j ON y.journal = j.id WHERE y.id = caisse) WHERE caisse IS NOT NULL AND agence IS NULL;
UPDATE yvs_compta_mouvement_caisse SET agence = (SELECT y.agence FROM yvs_users y WHERE y.id = caissier) WHERE caissier IS NOT NULL AND agence IS NULL;
UPDATE yvs_compta_mouvement_caisse SET agence = (SELECT y.agence FROM yvs_users_agence y WHERE y.id = author LIMIT 1) WHERE author IS NOT NULL AND agence IS NULL;