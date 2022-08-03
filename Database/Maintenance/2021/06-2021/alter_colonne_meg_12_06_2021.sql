-- Table: yvs_base_point_vente_user
-- DROP TABLE yvs_base_point_vente_user;
CREATE TABLE yvs_base_point_vente_user
(
  id bigserial NOT NULL,
  users bigint,
  point bigint,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  actif boolean DEFAULT true,
  execute_trigger character varying,
  CONSTRAINT yvs_base_point_vente_user_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_point_vente_user_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_point_vente_user_point_fkey FOREIGN KEY (point)
      REFERENCES yvs_base_point_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_point_vente_user_users_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_point_vente_user
  OWNER TO postgres;
  
  
ALTER TABLE yvs_base_model_reglement ADD COLUMN code_acces bigint;
ALTER TABLE yvs_base_model_reglement
  ADD CONSTRAINT yvs_base_model_reglement_code_acces_fkey FOREIGN KEY (code_acces)
      REFERENCES yvs_base_code_acces (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
