ALTER TABLE yvs_mut_mutuelle ADD COLUMN montant_assurance double precision;
ALTER TABLE yvs_mut_mutuelle ALTER COLUMN montant_assurance SET DEFAULT 0;

ALTER TABLE yvs_mut_parametre ADD COLUMN taux_couverture_credit double precision;
ALTER TABLE yvs_mut_parametre ALTER COLUMN taux_couverture_credit SET DEFAULT 100;

--Supprimer les trigger de la table operations compte
--Supprimer le trigger insert de la table crédit

DROP TRIGGER update_ ON yvs_mut_credit;
DROP FUNCTION update_credit();

ALTER TABLE yvs_mut_caisse ADD COLUMN type_de_flux character varying default 'PHYSIQUE';
COMMENT ON COLUMN yvs_mut_caisse.type_de_flux IS 'PHYSIQUE
SCRIPTURALE';

ALTER TABLE yvs_mut_reglement_credit ADD COLUMN compte bigint;

ALTER TABLE yvs_mut_reglement_credit
  ADD CONSTRAINT yvs_mut_reglement_credit_compte_fkey FOREIGN KEY (compte)
      REFERENCES yvs_mut_compte (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_mut_reglement_credit ADD COLUMN mode_paiement character varying;
COMMENT ON COLUMN yvs_mut_reglement_credit.mode_paiement IS 'Le crédit peut être payé au mutualiste en espèce ou par dépôt /virement dans l''un de ses compte
ESPECE; COMPTE';

ALTER TABLE yvs_base_article_depot ADD COLUMN suivi_stock boolean;
ALTER TABLE yvs_base_article_depot ALTER COLUMN suivi_stock SET DEFAULT true;


ALTER TABLE yvs_mut_reglement_mensualite ADD COLUMN compte bigint;

ALTER TABLE yvs_mut_reglement_mensualite
  ADD CONSTRAINT yvs_mut_reglement_mensualite_compte_fkey FOREIGN KEY (compte)
      REFERENCES yvs_mut_compte (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_mut_reglement_mensualite ADD COLUMN mode_paiement character varying;
COMMENT ON COLUMN yvs_mut_reglement_mensualite.mode_paiement IS 'Le crédit peut être payé au mutualiste en espèce ou par dépôt /virement dans l''un de ses compte
ESPECE; COMPTE';
