-- Vider la table grille de taux
-- Initialiser la colonne in_solde_caisse de la table mouvement_caisse
update yvs_mut_operation_compte SET nature='RETENUE FIXE' WHERE nature='RETENU FIXE';
ALTER TABLE yvs_mut_reglement_mensualite ADD COLUMN code_operation character varying;
ALTER TABLE yvs_mut_contribution_evenement DROP COLUMN caisse;
ALTER TABLE yvs_mut_evenement ADD COLUMN caisse_event bigint;
ALTER TABLE yvs_mut_evenement
  ADD CONSTRAINT yvs_mut_evenement_caisse_event_fkey FOREIGN KEY (caisse_event)
      REFERENCES yvs_mut_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


