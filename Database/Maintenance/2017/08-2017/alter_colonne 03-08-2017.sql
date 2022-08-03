ALTER TABLE yvs_com_plan_commission DROP COLUMN permanent;

ALTER TABLE yvs_com_doc_ventes ADD COLUMN commision double precision;
ALTER TABLE yvs_com_doc_ventes ALTER COLUMN commision SET DEFAULT 0;

ALTER TABLE yvs_com_facteur_taux DROP COLUMN article;
ALTER TABLE yvs_com_facteur_taux DROP COLUMN categorie;
ALTER TABLE yvs_com_facteur_taux ADD COLUMN permanent BOOLEAN DEFAULT false;

ALTER TABLE yvs_com_periode_validite_commision DROP COLUMN plan;
ALTER TABLE yvs_com_periode_validite_commision ADD COLUMN facteur bigint;
ALTER TABLE yvs_com_periode_validite_commision
  ADD CONSTRAINT yvs_com_periode_validite_commision_facteur_fkey FOREIGN KEY (facteur)
      REFERENCES yvs_com_facteur_taux (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_com_comerciale ADD COLUMN commission bigint;
ALTER TABLE yvs_com_comerciale
  ADD CONSTRAINT yvs_com_comerciale_commission_fkey FOREIGN KEY (commission)
      REFERENCES yvs_com_plan_commission (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_doc_ventes ADD COLUMN tiers bigint;
ALTER TABLE yvs_com_doc_ventes
  ADD CONSTRAINT yvs_com_doc_ventes_tiers_fkey FOREIGN KEY (tiers)
      REFERENCES yvs_base_tiers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_comerciale ADD COLUMN tiers bigint;
ALTER TABLE yvs_com_comerciale
  ADD CONSTRAINT yvs_com_comerciale_tiers_fkey FOREIGN KEY (tiers)
      REFERENCES yvs_base_tiers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
CREATE TABLE yvs_ext_employe
(
  id bigserial NOT NULL,
  employe bigint,
  code_externe character varying,
  date_save timestamp without time zone DEFAULT now(),
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_ext_employe_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_ext_employe_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_ext_employe_employe_fkey FOREIGN KEY (employe)
      REFERENCES yvs_grh_employes (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_ext_employe
  OWNER TO postgres;
  
  
CREATE TABLE yvs_ext_fournisseur
(
  id bigserial NOT NULL,
  fournisseur bigint,
  code_externe character varying,
  date_save timestamp without time zone DEFAULT now(),
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_ext_fournisseur_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_ext_fournisseur_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_ext_fournisseur_fournisseur_fkey FOREIGN KEY (fournisseur)
      REFERENCES yvs_base_fournisseur (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_ext_fournisseur
  OWNER TO postgres;