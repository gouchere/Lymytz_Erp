ALTER TABLE yvs_base_tarif_point_livraison DROP COLUMN active_livraison_domicile;

ALTER TABLE yvs_base_tarif_point_livraison ADD COLUMN active_livraison_domicile BOOLEAN;
ALTER TABLE yvs_base_tarif_point_livraison ALTER COLUMN active_livraison_domicile SET DEFAULT FALSE;

-- Table: yvs_user_view_alertes
-- DROP TABLE yvs_user_view_alertes;
CREATE TABLE yvs_user_view_alertes
(
  id bigserial NOT NULL,
  users bigint,
  document_type bigint,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  actif boolean,
  CONSTRAINT yvs_user_view_alertes_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_user_view_alertes_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_user_view_alertes_document_type_fkey FOREIGN KEY (document_type)
      REFERENCES yvs_workflow_model_doc (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_user_view_alertes_user_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_user_view_alertes
  OWNER TO postgres;

