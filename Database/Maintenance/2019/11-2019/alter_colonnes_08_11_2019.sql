-- Table: yvs_workflow_alertes

-- DROP TABLE yvs_workflow_alertes;

CREATE TABLE yvs_workflow_alertes
(
  id bigserial NOT NULL,
  model_doc bigint,
  nature_alerte character varying,
  id_element bigint,
  silence boolean,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  agence bigint,
  niveau integer, -- Niveau de criticité de l'alerte:...
  CONSTRAINT yvs_workflow_alertes_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_alertes_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_alertes_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_alertes_model_doc_fkey FOREIGN KEY (model_doc)
      REFERENCES yvs_workflow_model_doc (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_alertes
  OWNER TO postgres;
COMMENT ON COLUMN yvs_workflow_alertes.niveau IS 'Niveau de criticité de l''alerte:
1=Simple; 2=Sérieuse; 3=Severte; 4=Critique';


-- Table: yvs_workflow_user_alert

-- DROP TABLE yvs_workflow_user_alert;

CREATE TABLE yvs_workflow_user_alert
(
  id bigserial NOT NULL,
  users bigint,
  alerte bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  actif boolean,
  CONSTRAINT yvs_workflow_user_alert_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_user_alert_alerte_fkey FOREIGN KEY (alerte)
      REFERENCES yvs_workflow_alertes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_user_alert_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_user_alert_users_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_user_alert
  OWNER TO postgres;
  

CREATE TRIGGER action_on_mouvement_caisse
  AFTER UPDATE
  ON yvs_compta_mouvement_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE listen_alertes_op_caisses();


ALTER TABLE yvs_workflow_model_doc ADD COLUMN workflow boolean;
COMMENT ON COLUMN yvs_workflow_model_doc.workflow IS 'Détermine si on peux définir un workflow sur la ligne';

UPDATE yvs_workflow_model_doc SET workflow=true;

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('FACTURE_VENTE_LIVRE', 'yvs_com_doc_ventes', 16, current_timestamp, current_timestamp, false, false, 'Factures de vente au statut livré', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('FACTURE_VENTE_REGLE', 'yvs_com_doc_ventes', 16, current_timestamp, current_timestamp, false, false, 'Factures de vente au statut réglé', false, false);

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('BON_LIVRAISON_VENTE', 'yvs_com_doc_ventes', 16, current_timestamp, current_timestamp, false, false, 'Bon de livraison vente', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('FACTURE_AVOIR_VENTE', 'yvs_com_doc_ventes', 16, current_timestamp, current_timestamp, false, false, 'Factures d''avoir vente', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('BON_RETOUR_VENTE', 'yvs_com_doc_ventes', 16, current_timestamp, current_timestamp, false, false, 'Bon de retourn vente', false, false);

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('FACTURE_ACHAT_LIVRE', 'yvs_com_doc_achats', 16, current_timestamp, current_timestamp, false, false, 'Factures d''achat au statut livré', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('FACTURE_ACHAT_REGLE', 'yvs_com_doc_achats', 16, current_timestamp, current_timestamp, false, false, 'Factures d''achat au statut réglé', false, false);

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('BON_LIVRAISON_ACHAT', 'yvs_com_doc_achats', 16, current_timestamp, current_timestamp, false, false, 'Bon de reception achat', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('FACTURE_AVOIR_ACHAT', 'yvs_com_doc_achats', 16, current_timestamp, current_timestamp, false, false, 'Factures d''avoir achat', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('BON_RETOUR_ACHAT', 'yvs_com_doc_achats', 16, current_timestamp, current_timestamp, false, false, 'Bon de retour achat', false, false);

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('DOC_DIVERS_DEPENSE', 'yvs_compta_caisse_doc_divers', 16, current_timestamp, current_timestamp, false, false, 'Opération diverses dépenses', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('DOC_DIVERS_RECETTE', 'yvs_compta_caisse_doc_divers', 16, current_timestamp, current_timestamp, false, false, 'Opération diverses recerre', false, false);

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('STOCK_ARTICLE', 'yvs_base_article_depot', 16, current_timestamp, current_timestamp, false, false, 'Stock Dépôts', false, false);

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('SORTIE_STOCK', 'yvs_com_doc_stocks', 16, current_timestamp, current_timestamp, false, false, 'Document de sortie de stock', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('ENTREE_STOCK', 'yvs_com_doc_stocks', 16, current_timestamp, current_timestamp, false, false, 'Document d''entrée en stocks', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('RECONDITIONNEMENT_STOCK', 'yvs_com_doc_stocks', 16, current_timestamp, current_timestamp, false, false, 'Document de reconditionnement', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('INVENTAIRE_STOCK', 'yvs_com_doc_stocks', 16, current_timestamp, current_timestamp, false, false, 'Document d''inventaire', false, false);
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison,defined_update, description, defined_reglement, workflow)
    VALUES ('TRANSFERT_STOCK', 'yvs_com_doc_stocks', 16, current_timestamp, current_timestamp, false, false, 'Document de transfert de stock', false, false);

