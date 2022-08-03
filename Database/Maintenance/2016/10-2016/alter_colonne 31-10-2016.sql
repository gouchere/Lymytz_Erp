CREATE TABLE yvs_com_plan_reglement_categorie
(
  id bigserial NOT NULL,
  categorie bigint,
  model bigint,
  montant_minimal double precision DEFAULT 0,
  montant_maximal double precision DEFAULT 0,
  actif boolean DEFAULT true,
  author bigint,
  CONSTRAINT yvs_com_plan_reglement_categorie_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_plan_reglement_categorie_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_plan_reglement_categorie_categorie_fkey FOREIGN KEY (categorie)
      REFERENCES yvs_base_categorie_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_plan_reglement_categorie_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_model_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_plan_reglement_categorie
  OWNER TO postgres;
  
ALTER TABLE yvs_com_tranche_reglement_client RENAME plan TO plan_client;
ALTER TABLE yvs_com_tranche_reglement_client ADD COLUMN plan_categorie bigint;
ALTER TABLE yvs_com_tranche_reglement_client
  ADD CONSTRAINT yvs_com_tranche_reglement_client_plan_categorie_fkey FOREIGN KEY (plan_categorie)
      REFERENCES yvs_com_plan_reglement_categorie (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
DELETE FROM yvs_com_tranche_reglement_client;
DELETE FROM yvs_com_plan_reglement_client;
ALTER TABLE yvs_com_plan_reglement_client DROP CONSTRAINT yvs_com_plan_reglement_client_categorie_fkey;
ALTER TABLE yvs_com_plan_reglement_client RENAME categorie TO client;
ALTER TABLE yvs_com_plan_reglement_client
  ADD CONSTRAINT yvs_com_plan_reglement_client_categorie_fkey FOREIGN KEY (client)
      REFERENCES yvs_com_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;