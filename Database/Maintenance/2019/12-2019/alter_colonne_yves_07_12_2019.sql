
-- ALTER TABLE yvs_com_client DROP COLUMN ligne;

ALTER TABLE yvs_com_client ADD COLUMN ligne bigint;
COMMENT ON COLUMN yvs_com_client.ligne IS 'Ligne géographique';

-- Foreign Key: yvs_com_client_ligne_fkey

-- ALTER TABLE yvs_com_client DROP CONSTRAINT yvs_com_client_ligne_fkey;

ALTER TABLE yvs_com_client
  ADD CONSTRAINT yvs_com_client_ligne_fkey FOREIGN KEY (ligne)
      REFERENCES yvs_base_point_livraison (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_compta_acompte_client DISABLE TRIGGER compta_action_on_acompte_client;
ALTER TABLE yvs_compta_acompte_client DISABLE TRIGGER action_listen_yvs_compta_acompte_client;
update yvs_compta_notif_reglement_vente set id=id;
ALTER TABLE yvs_compta_acompte_client ENABLE TRIGGER compta_action_on_acompte_client;
ALTER TABLE yvs_compta_acompte_client ENABLE TRIGGER action_listen_yvs_compta_acompte_client;

ALTER TABLE yvs_compta_acompte_fournisseur DISABLE TRIGGER compta_action_on_acompte_fournisseur;
ALTER TABLE yvs_compta_acompte_fournisseur DISABLE TRIGGER action_listen_yvs_compta_acompte_fournisseur;
update yvs_compta_notif_reglement_achat set id=id;
ALTER TABLE yvs_compta_acompte_fournisseur ENABLE TRIGGER compta_action_on_acompte_fournisseur;
ALTER TABLE yvs_compta_acompte_fournisseur ENABLE TRIGGER action_listen_yvs_compta_acompte_fournisseur;