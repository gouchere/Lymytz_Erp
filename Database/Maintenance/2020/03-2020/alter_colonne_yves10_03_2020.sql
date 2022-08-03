
ALTER TABLE yvs_com_doc_stocks DROP COLUMN nature_doc;
DROP TABLE yvs_com_nature_doc;
CREATE TABLE yvs_com_nature_doc
(
  id bigserial NOT NULL,
  nature character varying,
  description character varying,
  actif boolean,
  societe bigint,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  CONSTRAINT yvs_com_nature_doc_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_nature_doc_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_nature_doc_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_nature_doc
  OWNER TO postgres;


ALTER TABLE yvs_com_doc_stocks ADD COLUMN nature_doc bigint;

ALTER TABLE yvs_com_doc_stocks
  ADD CONSTRAINT yvs_com_doc_stocks_nature_doc_fkey FOREIGN KEY (nature_doc)
      REFERENCES yvs_com_nature_doc (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

--INSERT INTO yvs_com_nature_doc(nature, description,actif,societe, author, date_save, date_update) VALUES ('DEPRECIATION', 'Produits en démarques',true,2297, 16, current_date, current_date);
--INSERT INTO yvs_com_nature_doc(nature, description,actif,societe, author, date_save, date_update) VALUES ('INITIALISATION', 'Démarrage du dépôt',true,2297, 16, current_date, current_date);
--INSERT INTO yvs_com_nature_doc(nature, description,actif,societe, author, date_save, date_update) VALUES ('AJUSTEMENT STOCK', 'Ajustement dtock réel stock théorique',true,2297, 16, current_date, current_date);


ALTER TABLE yvs_workflow_etape_validation ADD COLUMN nature_doc bigint;
ALTER TABLE yvs_workflow_etape_validation
  ADD CONSTRAINT yvs_workflow_etape_validation_nature_doc_fkey FOREIGN KEY (nature_doc)
      REFERENCES yvs_com_nature_doc (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_prod_parametre ADD COLUMN converter_pf integer;

SELECT insert_droit('stock_update_qte_recu', 'Modifier les quantité receptionnées au transfert', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_transfert'), 16, 'A,B,J,F,D','R');
	
SELECT insert_droit('prod_create_other_session', 'Créer une session pour un producteur différent de moi ', 
	(SELECT id FROM yvs_page_module WHERE reference = 'ges_prod_of'), 16, 'A,B,J,F,D','R');

ALTER TABLE yvs_base_depots ADD COLUMN code_acces bigint;
ALTER TABLE yvs_base_depots
  ADD CONSTRAINT yvs_base_depots_code_acces_fkey FOREIGN KEY (code_acces)
      REFERENCES yvs_base_code_acces (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
