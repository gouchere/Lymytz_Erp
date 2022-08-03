
ALTER TABLE yvs_compta_parametre ADD COLUMN plafond_bp double precision;
ALTER TABLE yvs_compta_parametre ALTER COLUMN plafond_bp SET DEFAULT 0;

ALTER TABLE yvs_compta_parametre ADD COLUMN nb_max_bp integer;
ALTER TABLE yvs_compta_parametre ALTER COLUMN nb_max_bp SET DEFAULT 0;

-- Table: yvs_workflow_model_doc
-- DROP TABLE yvs_workflow_etats_signatures;
CREATE TABLE yvs_workflow_etats_signatures
(
  id bigserial NOT NULL,
  titre1 character varying,
  titre2 character varying,
  titre3 character varying,
  titre4 character varying,
  titre5 character varying,
  model_doc bigint,
  societe integer,
  author bigint,
  CONSTRAINT yvs_workflow_etats_signatures_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_etats_signatures_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_etats_signatures_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_etats_signatures_model_doc_fkey FOREIGN KEY (model_doc)
      REFERENCES yvs_workflow_model_doc (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_etats_signatures
  OWNER TO postgres;

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, date_update, date_save, defined_livraison, 
            defined_update, description, defined_reglement, workflow, execute_trigger, 
            nature)
    VALUES ('COMMANDE_ACHAT', 'yvs_com_doc_achats', null, current_timestamp, current_timestamp, false, false, 
            'Bon de commande achat', false, false, null, 'A');



--DROP TABLE yvs_param_adresses_allowed
CREATE TABLE yvs_param_adresses_allowed
(
  id bigserial NOT NULL,
  adresse_mac character varying,
  name_machine character varying,
  description_machine character varying,-- localisation
  actif boolean,
  societe integer,
  author bigint,
  date_update timestamp without time zone,
  CONSTRAINT yvs_param_adresses_allowed_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_param_adresses_allowed_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT yvs_workflow_etats_signatures_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE yvs_param_adresses_allowed
  OWNER TO postgres;
ALTER TABLE yvs_users ADD COLUMN connect_without_allowed boolean;
ALTER TABLE yvs_users ALTER COLUMN connect_without_allowed SET DEFAULT true;

SELECT insert_droit('param_allowed_adresse_','Autoriser une adresse sur votre serveur', (SELECT id FROM yvs_page_module WHERE reference = 'param_societ_'), null,'A','R');
SELECT insert_droit('compta_valid_all_bp','Valider tous les bons sans restriction de montant ', (SELECT id FROM yvs_page_module WHERE reference = 'compta_view_bon_prov'), null,'A','R');
SELECT insert_droit('compta_valid_bp','Valider un bon provisoire', (SELECT id FROM yvs_page_module WHERE reference = 'compta_view_bon_prov'), null,'A','R');
SELECT insert_droit('compta_valid_bp_under_max','Valider même lorsque le nombre max. de Bp non justifié est atteint', (SELECT id FROM yvs_page_module WHERE reference = 'compta_view_bon_prov'), null,'A','R');