ALTER TABLE yvs_com_doc_ventes DROP CONSTRAINT yvs_com_doc_ventes_livreur_fkey;
ALTER TABLE yvs_com_doc_ventes
  ADD CONSTRAINT yvs_com_doc_ventes_livreur_fkey FOREIGN KEY (livreur)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
DROP TABLE yvs_com_employe;

ALTER TABLE yvs_users ADD COLUMN categorie bigint;
ALTER TABLE yvs_users ADD COLUMN plan_commission bigint;
ALTER TABLE yvs_users
  ADD CONSTRAINT yvs_users_categorie_fkey FOREIGN KEY (categorie)
      REFERENCES yvs_com_categorie_personnel (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_users
  ADD CONSTRAINT yvs_users_plan_commission_fkey FOREIGN KEY (plan_commission)
      REFERENCES yvs_com_plan_commission (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;