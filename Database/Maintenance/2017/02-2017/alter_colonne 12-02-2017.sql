CREATE TABLE yvs_workflow_valid_divers
(
  divers bigint,
  etape bigint,
  author bigint,
  etape_valid boolean,
  id bigserial NOT NULL,
  CONSTRAINT yvs_workflow_valid_divers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_valid_divers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_valid_divers_divers_fkey FOREIGN KEY (divers)
      REFERENCES yvs_compta_caisse_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_workflow_valid_divers_etape_fkey FOREIGN KEY (etape)
      REFERENCES yvs_workflow_etape_validation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_valid_divers
  OWNER TO postgres;
  
  CREATE TABLE yvs_workflow_valid_doc_stock
(
  doc_stock bigint,
  etape bigint,
  author bigint,
  etape_valid boolean,
  id bigserial NOT NULL,
  CONSTRAINT yvs_workflow_valid_doc_stock_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_valid_doc_stock_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_valid_doc_stock_doc_stock_fkey FOREIGN KEY (doc_stock)
      REFERENCES yvs_com_doc_stocks (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_workflow_valid_doc_stock_etape_fkey FOREIGN KEY (etape)
      REFERENCES yvs_workflow_etape_validation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_valid_doc_stock
  OWNER TO postgres;
  
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table) VALUES ('SORTIE_STOCK', 'yvs_com_doc_stocks');
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table) VALUES ('OPERATION_DIVERS', 'yvs_compta_caisse_doc_divers');